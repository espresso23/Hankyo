package service;

import dao.OrderDAO;
import dao.PaymentDAO;
import model.CoursePaid;
import model.Order;
import model.Payment;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;
    private final Connection connection;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.orderDAO = new OrderDAO();
        this.connection = DBConnect.getInstance().getConnection();
    }

    // Thêm Payment kèm theo thông tin Course đã mua tính toán doanh thu cho expert và doanh thu cho nền tảng
    public boolean addPaymentWithCourse(Payment payment, List<CoursePaid> coursePaids) {
        try {
            connection.setAutoCommit(false);

            // Lưu payment
            boolean paymentSuccess = paymentDAO.createPayment(payment);
            if (!paymentSuccess) {
                throw new SQLException("Failed to create payment");
            }

            // Lưu coursePaid và tạo order cho mỗi khóa học
            for (CoursePaid coursePaid : coursePaids) {
                boolean coursePaidSuccess = paymentDAO.createCoursePaid(coursePaid);
                if (!coursePaidSuccess) {
                    throw new SQLException("Failed to create course paid");
                }

                // Tạo order mới cho mỗi khóa học
                Order order = new Order();
                order.setOrderID(UUID.randomUUID().toString());
                order.setPaymentID(payment.getPaymentID());
                order.setExpertID(coursePaid.getCourse().getExpertID());
                order.setCourseID(coursePaid.getCourseID());
                order.setLearnerID(payment.getLearnerID());
                order.setOrderDate(payment.getPayDate());
                order.setTotalAmount(coursePaid.getCourse().getPrice());
                order.setStatus("Completed");

                boolean orderSuccess = orderDAO.createOrder(order);
                if (!orderSuccess) {
                    throw new SQLException("Failed to create order");
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Lấy tất cả các khóa học đã được thanh toán (cho admin hoặc expert xem)
    public List<CoursePaid> getAllCoursePaid() {
        return paymentDAO.getAllCoursePaid();
    }

    // Lấy lịch sử thanh toán của learner dựa theo learnerID
    public List<CoursePaid> getPaymentHistoryByLearnerID(int learnerID) {
        return paymentDAO.getPaymentHistoryByLearnerID(learnerID);
    }

    public boolean withdrawRevenue(int expertID, BigDecimal amountToDraw) {
        return paymentDAO.withdrawRevenue(expertID, amountToDraw);
    }
}
