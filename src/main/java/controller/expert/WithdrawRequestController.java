package controller.expert;

import dao.ExpertBankDAO;
import dao.ExpertRevenueDAO;
import dao.WithdrawRequestDAO;
import model.Expert;
import model.ExpertBank;
import model.ExpertRevenue;
import model.WithdrawRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/expert/withdraw")
public class WithdrawRequestController extends HttpServlet {
    private WithdrawRequestDAO withdrawRequestDAO;
    private ExpertBankDAO expertBankDAO;
    private ExpertRevenueDAO expertRevenueDAO;

    @Override
    public void init() throws ServletException {
        withdrawRequestDAO = new WithdrawRequestDAO();
        expertBankDAO = new ExpertBankDAO();
        expertRevenueDAO = new ExpertRevenueDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get expert's bank accounts
        List<ExpertBank> expertBanks = expertBankDAO.getExpertBanks(expert.getExpertID());
        request.setAttribute("expertBanks", expertBanks);

        // Get expert's revenue
        ExpertRevenue expertRevenue = expertRevenueDAO.getExpertRevenue(expert.getExpertID());
        request.setAttribute("expertRevenue", expertRevenue);

        // Get withdraw requests history and convert dates
        List<WithdrawRequest> withdrawRequests = withdrawRequestDAO.getWithdrawRequestsByExpertId(expert.getExpertID());
        for (WithdrawRequest withdrawRequest : withdrawRequests) {
            if (withdrawRequest.getRequestDate() != null) {
                withdrawRequest.setFormattedDate(java.sql.Timestamp.valueOf(withdrawRequest.getRequestDate()));
            }
        }
        request.setAttribute("withdrawRequests", withdrawRequests);

        request.getRequestDispatcher("/withdraw-request.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Phiên đăng nhập đã hết hạn\"}");
            return;
        }

        try {
            // Get parameters
            int eBankID = Integer.parseInt(request.getParameter("eBankID"));
            double amount = Double.parseDouble(request.getParameter("amount"));
            String note = request.getParameter("note");

            // Validate amount
            ExpertRevenue expertRevenue = expertRevenueDAO.getExpertRevenue(expert.getExpertID());
            if (amount > expertRevenue.getTotalRevenue()) {
                response.getWriter().write("{\"success\": false, \"message\": \"Số tiền rút vượt quá số dư hiện có\"}");
                return;
            }

            if (amount < 50000) {
                response.getWriter().write("{\"success\": false, \"message\": \"Số tiền rút tối thiểu là 50,000₫\"}");
                return;
            }

            // Create withdraw request
            WithdrawRequest withdrawRequest = new WithdrawRequest(expert.getExpertID(), amount, note, eBankID);
            boolean success = withdrawRequestDAO.createWithdrawRequest(withdrawRequest);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Yêu cầu rút tiền đã được tạo thành công\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể tạo yêu cầu rút tiền\"}");
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Dữ liệu không hợp lệ\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra, vui lòng thử lại sau\"}");
        }
    }
} 