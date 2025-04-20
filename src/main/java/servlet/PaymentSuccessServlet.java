package servlet;

import dao.LearnerDAO;
import model.*;
import service.CartService;
import service.PaymentService;
import vn.payos.type.PaymentData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            // Lấy thông tin từ request
            String orderCode = request.getParameter("orderCode");
            Integer learnerID = Integer.parseInt(request.getParameter("learnerID"));
            String[] courseIDs = request.getParameter("courses").split(",");
            PaymentData paymentData = (PaymentData) request.getSession().getAttribute("paymentData");
            String transactionDescription = (String) request.getSession().getAttribute("transactionCode");
            System.out.println("leanerIDpayment= " + learnerID);
            // Lấy thông tin người học
            Learner learner = learnerDAO.getLearnerById(learnerID);
            if (learner == null) {
                throw new Exception("Không tìm thấy thông tin người học");
            }

            // Tạo đối tượng Payment - setup thông tin payment
            Payment payment = new Payment();
            payment.setPaymentID(orderCode);
            payment.setLearnerID(learnerID);
            payment.setDescription(transactionDescription);
            payment.setPayDate(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            payment.setStatus("Completed");

            // Tạo danh sách CoursePaid
            List<CoursePaid> coursePaids = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            // Xử lý từng khóa học
            for (String courseID : courseIDs) {
                // Lấy thông tin khóa học từ giỏ hàng
                List<Cart> cartItems = cartService.getPendingCartItems(learnerID);
                for (Cart cartItem : cartItems) {
                    if (cartItem.getCourse().getCourseID() == Integer.parseInt(courseID)) {
                        Course course = cartItem.getCourse();

                        // Tạo CoursePaid
                        CoursePaid coursePaid = new CoursePaid();
                        coursePaid.setCourseID(course.getCourseID());
                        coursePaid.setLearnerID(learnerID);
                        coursePaids.add(coursePaid);

                        // Cộng dồn tổng tiền
                        totalAmount = totalAmount.add(course.getPrice());

                        // Xóa khóa học khỏi giỏ hàng
                        cartService.removeFromCart(cartItem.getCartID(), learnerID);
                        break;
                    }
                }
            }

            // Cập nhật tổng tiền cho payment
            payment.setTotalAmount(totalAmount);

            // Lưu thông tin thanh toán và khóa học đã mua
            boolean paymentSuccess = paymentService.addPaymentWithCourse(payment, coursePaids);

            if (!paymentSuccess) {
                throw new Exception("Lỗi khi lưu thông tin thanh toán");
            }
// Xóa session sau khi thanh toán thành công
            request.getSession().removeAttribute("paymentData");
            request.getSession().removeAttribute("courseDescriptions");
            request.getSession().removeAttribute("coursePrices");
            request.getSession().removeAttribute("transactionCode"); // nếu có

            // Chuyển hướng đến trang thông báo thành công
            response.sendRedirect("payment-success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("payment-error.jsp");
        }
    }
} 