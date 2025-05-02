package service;

import dao.OrderDAO;
import dao.PaymentDAO;
import model.CoursePaid;
import model.Payment;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
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

    public boolean addPaymentWithCourse(Payment payment, List<CoursePaid> coursePaidList, Connection connection) {
        String paymentSQL = "INSERT INTO Payment (paymentID, amount, paymentDate, description, learnerID, status) VALUES (?, ?, ?, ?, ?, ?)";
        String coursePaidSQL = "INSERT INTO Course_Paid (paymentID, courseID, learnerID, datePaid) VALUES (?, ?, ?, ?)";
        String expertSQL = "SELECT expertID, price FROM Course WHERE courseID = ?";
        String updateRevenueSQL = "INSERT INTO ExpertRevenue (expertID, totalRevenue, lastUpdated) VALUES (?, ?, ?)";
        String insertPlatformRevenueSQL = "INSERT INTO Revenue (amount, date, description,paymentID) VALUES (?, GETDATE(), ?,?)";
        String orderSQL = "INSERT INTO Orders (orderID, paymentID, expertID, courseID, learnerID, orderDate, totalAmount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // 1. Insert Payment
            try (PreparedStatement psPayment = connection.prepareStatement(paymentSQL)) {
                psPayment.setString(1, payment.getPaymentID());
                psPayment.setBigDecimal(2, payment.getTotalAmount());
                psPayment.setTimestamp(3, Timestamp.valueOf(payment.getPayDate()));
                psPayment.setString(4, payment.getDescription());
                psPayment.setInt(5, payment.getLearnerID());
                psPayment.setString(6, payment.getStatus());
                psPayment.executeUpdate();
            }

            double totalPlatformRevenue = 0.0;

            for (CoursePaid cp : coursePaidList) {
                // 2. Insert Course_Paid
                try (PreparedStatement psCoursePaid = connection.prepareStatement(coursePaidSQL)) {
                    psCoursePaid.setString(1, payment.getPaymentID());
                    psCoursePaid.setInt(2, cp.getCourseID());
                    psCoursePaid.setInt(3, cp.getLearnerID());
                    psCoursePaid.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    psCoursePaid.executeUpdate();
                }

                // 3. Get expertID and price
                try (PreparedStatement psExpert = connection.prepareStatement(expertSQL)) {
                    psExpert.setInt(1, cp.getCourseID());
                    ResultSet rsExpert = psExpert.executeQuery();

                    if (rsExpert.next()) {
                        int expertID = rsExpert.getInt("expertID");
                        double price = rsExpert.getDouble("price");
                        double expertShare = price * 0.95;
                        double platformShare = price * 0.05;
                        totalPlatformRevenue += platformShare;

                        // 4. Update expert revenue
                        try (PreparedStatement psRevenue = connection.prepareStatement(updateRevenueSQL)) {
                            psRevenue.setInt(1, expertID);
                            psRevenue.setDouble(2, expertShare);
                            psRevenue.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                            psRevenue.executeUpdate();
                        }

                        // 5. Create Order
                        try (PreparedStatement psOrder = connection.prepareStatement(orderSQL)) {
                            String orderID = UUID.randomUUID().toString();
                            psOrder.setString(1, orderID);
                            psOrder.setString(2, payment.getPaymentID());
                            psOrder.setInt(3, expertID);
                            psOrder.setInt(4, cp.getCourseID());
                            psOrder.setInt(5, payment.getLearnerID());
                            psOrder.setTimestamp(6, Timestamp.valueOf(payment.getPayDate()));
                            psOrder.setBigDecimal(7, BigDecimal.valueOf(price));
                            psOrder.setString(8, "Completed");
                            psOrder.executeUpdate();
                        }
                    }
                }
            }

            // 6. Update platform revenue
            if (totalPlatformRevenue > 0) {
                try (PreparedStatement psPlatform = connection.prepareStatement(insertPlatformRevenueSQL)) {
                    psPlatform.setDouble(1, totalPlatformRevenue);
                    psPlatform.setString(2, payment.getDescription());
                    psPlatform.setString(3, payment.getPaymentID());
                    psPlatform.executeUpdate();
                }
            }

            connection.commit();
            return true;

        } catch (Exception e) {
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
