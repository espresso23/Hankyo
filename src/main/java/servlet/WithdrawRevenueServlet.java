package servlet;

import service.PaymentService;
import vn.payos.PayOS;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/withdrawRevenue")
public class WithdrawRevenueServlet extends HttpServlet {
    private PaymentService paymentService = new PaymentService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int expertID = Integer.parseInt(request.getParameter("expertID"));
        BigDecimal amountToWithdraw = new BigDecimal(request.getParameter("amount"));

        // Thực hiện rút tiền
        boolean success = paymentService.withdrawRevenue(expertID, amountToWithdraw);
        if (success) {
            response.getWriter().write("Rút tiền thành công! Số tiền rút là: " + amountToWithdraw);
        } else {
            response.getWriter().write("Số tiền yêu cầu rút vượt quá doanh thu hoặc không hợp lệ!");
        }
    }
}
