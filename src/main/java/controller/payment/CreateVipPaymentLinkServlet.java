package controller.payment;

import dao.VipDAO;
import model.Vip;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create-vip-payment-link")
public class CreateVipPaymentLinkServlet extends HttpServlet {
    private final PayOS payOS = new PayOS("10309db4-d116-4c91-9ba8-1a95a495e6dd", "f0e1b0e7-c815-4f93-b3e0-4840e9e45353", "9246f470e539cb0d64a4cc128007653352c28d84e5603748c70a03ffce9f1b35");
    private final VipDAO vipDAO = new VipDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vipID = request.getParameter("vipID");
        String learnerID = request.getParameter("learnerID");
        try {
            // Lấy thông tin VIP từ DB
            Vip vip = vipDAO.getVipById(Integer.parseInt(vipID));
            if (vip == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Gói VIP không tồn tại");
                return;
            }
            // Tạo item cho PayOS
            List<ItemData> items = new ArrayList<>();
            ItemData item = ItemData.builder()
                    .name(vip.getVipName())
                    .quantity(1)
                    .price((int) Math.round(vip.getPrice()))
                    .build();
            items.add(item);

            Long orderCode = System.currentTimeMillis() / 1000;
            String domain = "http://localhost:8080/Hankyo";
            String returnUrl = domain + "/vip-payment-success?orderCode=" + orderCode + "&learnerID=" + learnerID + "&vipID=" + vipID;
            String cancelUrl = domain + "/vip-bundles";

            int totalAmount = (int) Math.round(vip.getPrice());
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(totalAmount)
                    .description("Thanh toán VIP " + vip.getVipName())
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .items(items)
                    .build();
            try {
                CheckoutResponseData result = payOS.createPaymentLink(paymentData);
                // Lưu thông tin payment vào session
                request.getSession().setAttribute("vipPaymentData", paymentData);
                request.getSession().setAttribute("vipID", vipID);
                request.getSession().setAttribute("learnerID", learnerID);
                // Redirect sang link thanh toán PayOS
                response.sendRedirect(result.getCheckoutUrl());
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi tạo link thanh toán VIP");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý thanh toán VIP");
        }
    }
} 