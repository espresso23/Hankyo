package controller.payment;

import dao.VipDAO;
import dao.PaymentDAO;
import model.Vip;
import model.VipUser;
import model.Payment;
import service.PaymentService;
import util.DBConnect;
import vn.payos.type.PaymentData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.math.BigDecimal;

@WebServlet("/vip-payment-success")
public class VipPaymentSuccessServlet extends HttpServlet {
    private final VipDAO vipDAO = new VipDAO();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String orderCode = request.getParameter("orderCode");
            String learnerIDParam = request.getParameter("learnerID");
            String vipIDParam = request.getParameter("vipID");

            if (orderCode == null || learnerIDParam == null || vipIDParam == null) {
                throw new Exception("Thiếu thông tin thanh toán VIP");
            }
            int learnerID = Integer.parseInt(learnerIDParam);
            int vipID = Integer.parseInt(vipIDParam);

            HttpSession session = request.getSession();
            PaymentData paymentData = (PaymentData) session.getAttribute("vipPaymentData");
            if (paymentData == null) {
                throw new Exception("Không tìm thấy thông tin thanh toán VIP");
            }
            // Lấy thông tin VIP
            Vip vip = vipDAO.getVipById(vipID);
            if (vip == null) {
                throw new Exception("Không tìm thấy gói VIP");
            }
            // Kiểm tra số tiền
            int expectedAmount = (int) Math.round(vip.getPrice());
            if (paymentData.getAmount() != expectedAmount) {
                throw new Exception("Số tiền thanh toán không khớp");
            }

            // --- Thêm bản ghi Payment (type = 'vip') ---
            Payment payment = new Payment();
            payment.setPaymentID(String.valueOf(orderCode));
            payment.setLearnerID(learnerID);
            payment.setTotalAmount(BigDecimal.valueOf(vip.getPrice()));
            payment.setDescription("Thanh toán VIP: " + vip.getVipName());
            payment.setPayDate(java.time.LocalDateTime.now());
            payment.setStatus("completed");
            payment.setType("vip");
            PaymentDAO paymentDAO = new PaymentDAO();
            boolean paymentSuccess = paymentDAO.addPayment(payment);
            if (!paymentSuccess) {
                throw new Exception("Lỗi khi lưu thông tin thanh toán VIP");
            }

            // Tạo bản ghi Vip_User
            VipUser vipUser = new VipUser();
            vipUser.setLearnerID(learnerID);
            vipUser.setVipID(vipID);
            vipUser.setStartDate(new Date());
            // Tính endDate dựa trên duration của gói VIP
            LocalDateTime endDate = LocalDateTime.now().plusDays(vip.getDuration());
            vipUser.setEndDate(Timestamp.valueOf(endDate));
            vipUser.setStatus("ACTIVE");
            vipUser.setPaymentID(String.valueOf(orderCode));
            boolean success = vipDAO.addVipUser(vipUser);
            if (!success) {
                throw new Exception("Lỗi khi lưu thông tin VIP user");
            }
            // Xóa session
            session.removeAttribute("vipPaymentData");
            session.removeAttribute("vipID");
            session.removeAttribute("learnerID");
            // Redirect sang trang thành công
            response.sendRedirect("vip-payment-success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vip-payment-error.jsp");
        }
    }
} 