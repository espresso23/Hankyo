package servlet;

import dao.CartDAO;
import model.Cart;
import service.PaymentService;
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

@WebServlet("/create-payment-link")
public class CreatePaymentLinkServlet extends HttpServlet {
    private final PayOS payOS = new PayOS("10309db4-d116-4c91-9ba8-1a95a495e6dd", "f0e1b0e7-c815-4f93-b3e0-4840e9e45353", "9246f470e539cb0d64a4cc128007653352c28d84e5603748c70a03ffce9f1b35");
    private final CartDAO cartDAO = new CartDAO();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] courseIDs = request.getParameterValues("courseIDs");
        String[] courseNames = request.getParameterValues("courseNames");
        String[] coursePrices = request.getParameterValues("coursePrices");
        String learnerID = request.getParameter("learnerID");

        // Lấy thông tin description từ giỏ hàng
        List<Cart> cartItems = cartDAO.getCartItems(learnerID);
        List<String> courseDescriptions = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            courseDescriptions.add(cartItem.getCourse().getCourseDescription());
        }

        String domain = "http://localhost:8080/Hankyo";
        Long orderCode = System.currentTimeMillis() / 1000;
        
        // Tạo chuỗi thông tin khóa học
        StringBuilder courseInfo = new StringBuilder();
        for (int i = 0; i < courseIDs.length; i++) {
            if (i > 0) courseInfo.append(",");
            courseInfo.append(courseIDs[i]);
        }
        
        // Thêm thông tin vào URL callback
        String returnUrl = domain + "/payment-success?orderCode=" + orderCode + 
                          "&learnerID=" + learnerID + 
                          "&courses=" + courseInfo.toString();
        System.out.println("returnUrl= " + returnUrl);
        String cancelUrl = domain + "/cart";
        System.out.println("cancelUrl= " + cancelUrl);

        // Tạo danh sách items cho PayOS
        List<ItemData> items = new ArrayList<>();
        for (int i = 0; i < courseIDs.length; i++) {
            double price = Double.parseDouble(coursePrices[i]);
            int priceInVND = (int) Math.round(price);

            ItemData item = ItemData.builder()
                    .name(courseNames[i])
                    .quantity(1)
                    .price(priceInVND)
                    .build();
            items.add(item);
        }

        // Tính tổng tiền
        int totalAmount = 0;
        for (String price : coursePrices) {
            double priceValue = Double.parseDouble(price);
            totalAmount += Math.round(priceValue);
        }
        // Tạo payment data cho PayOS
        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(totalAmount)
                .description("TT don hang so " + orderCode)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .items(items)
                .build();

        try {
            // Tạo payment link từ PayOS
            CheckoutResponseData result = payOS.createPaymentLink(paymentData);
            // Lưu thông tin payment data vào session
            String transactionCode = result.getDescription();
            String fullDescription = "TT đơn hàng #" + orderCode + " - Mã GD: " + transactionCode;
            request.getSession().setAttribute("transactionCode", fullDescription);
            request.getSession().setAttribute("paymentData", paymentData);
            request.getSession().setAttribute("courseDescriptions", courseDescriptions);
            request.getSession().setAttribute("coursePrices", coursePrices);
            // Chuyển hướng đến trang thanh toán PayOS
            response.sendRedirect(result.getCheckoutUrl());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi tạo link thanh toán");
        }
    }
}
