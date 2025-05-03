package controller.payment;

import dao.LearnerDAO;
import model.*;
import service.CartService;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/payment-success")
public class PaymentSuccessServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final LearnerDAO learnerDAO = new LearnerDAO();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Validate input parameters
            String orderCode = request.getParameter("orderCode");
            String coursesParam = request.getParameter("courses");
            String learnerIDParam = request.getParameter("learnerID");

            if (orderCode == null || coursesParam == null || learnerIDParam == null) {
                throw new Exception("Thiếu thông tin thanh toán");
            }

            Integer learnerID = Integer.parseInt(learnerIDParam);
            String[] courseIDs = coursesParam.split(",");
            
            if (courseIDs.length == 0) {
                throw new Exception("Không có khóa học được chọn");
            }

            // Validate payment data from session
            HttpSession session = request.getSession();
            PaymentData paymentData = (PaymentData) session.getAttribute("paymentData");
            String transactionDescription = (String) session.getAttribute("transactionCode");

            if (paymentData == null) {
                throw new Exception("Không tìm thấy thông tin thanh toán");
            }

            // Validate learner
            Learner learner = learnerDAO.getLearnerById(learnerID);
            if (learner == null) {
                throw new Exception("Không tìm thấy thông tin người học");
            }

            // Get all pending cart items once
            List<Cart> allCartItems = cartService.getPendingCartItems(learnerID);
            if (allCartItems.isEmpty()) {
                throw new Exception("Giỏ hàng trống");
            }

            // Create payment object
            Payment payment = new Payment();
            payment.setPaymentID(orderCode);
            payment.setLearnerID(learnerID);
            payment.setDescription(transactionDescription);
            payment.setPayDate(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            payment.setStatus("completed");

            // Process courses
            List<CoursePaid> coursePaids = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (String courseIDStr : courseIDs) {
                int courseID = Integer.parseInt(courseIDStr);
                Cart matchingCart = allCartItems.stream()
                    .filter(cart -> cart.getCourse().getCourseID() == courseID)
                    .findFirst()
                    .orElse(null);

                if (matchingCart == null) {
                    throw new Exception("Không tìm thấy khóa học trong giỏ hàng: " + courseID);
                }

                Course course = matchingCart.getCourse();
                
                // Create CoursePaid
                CoursePaid coursePaid = new CoursePaid();
                coursePaid.setCourseID(course.getCourseID());
                coursePaid.setLearnerID(learnerID);
                coursePaid.setPaymentID(orderCode);
                coursePaid.setExpertID(course.getExpertID());
                coursePaid.setCourse(course);
                coursePaids.add(coursePaid);

                // Add to total amount
                totalAmount = totalAmount.add(course.getPrice());

                // Remove from cart
                cartService.removeFromCart(matchingCart.getCartID(), learnerID);
            }

            // Validate total amount with payment data
            if (paymentData != null) {
                // Làm tròn số tiền để so sánh
                BigDecimal paymentAmount = new BigDecimal(paymentData.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                
                if (!totalAmount.equals(paymentAmount)) {
                    System.out.println("Expected amount: " + paymentAmount + ", Actual amount: " + totalAmount);
                    throw new Exception("Số tiền thanh toán không khớp");
                }
            }

            // Update payment total
            payment.setTotalAmount(totalAmount);

            // Save payment and course information
            boolean paymentSuccess = paymentService.addPaymentWithCourse(payment, coursePaids, DBConnect.getInstance().getConnection());

            if (!paymentSuccess) {
                throw new Exception("Lỗi khi lưu thông tin thanh toán");
            }

            // Clear session data
            session.removeAttribute("paymentData");
            session.removeAttribute("courseDescriptions");
            session.removeAttribute("coursePrices");
            session.removeAttribute("transactionCode");

            // Redirect to success page
            response.sendRedirect("payment-success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            // Log the error
            System.err.println("Payment processing error: " + e.getMessage());
            response.sendRedirect("payment-error.jsp");
        }
    }
} 