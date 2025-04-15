package service;

import dao.PaymentDAO;
import model.CoursePaid;
import model.Payment;

import java.math.BigDecimal;
import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    // Thêm Payment kèm theo thông tin Course đã mua tính toán doanh thu cho expert và doanh thu cho nền tảng
    public boolean addPaymentWithCourse(Payment payment, List<CoursePaid> coursePaids) {
        return paymentDAO.addPaymentWithCourses(payment, coursePaids);
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
