package controller.expert;

import dao.ExpertDAO;
import model.Expert;
import model.ExpertBank;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/submitBankDetails")
public class BankDetailsServlet extends HttpServlet {
    private ExpertDAO expertDAO;

    @Override
    public void init() throws ServletException {
        expertDAO = new ExpertDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        if (expert != null) {
            String bankAccount = request.getParameter("bankAccount");
            String[] bankInfo = request.getParameter("bank").split(",");
            String bankID = bankInfo[0];
            String binCode = bankInfo[1];

            ExpertBank expertBank = new ExpertBank(bankAccount, bankID, binCode, expert.getExpertID());
            boolean added = expertDAO.addNewBankAccount(expertBank);
            if(added){
                // Lưu vào DB hoặc session, tùy nhu cầu
                request.setAttribute("expertBank", expertBank);
            }
            // Chuyển tiếp tới trang xác nhận hoặc tiếp theo
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
        }

    }
}
