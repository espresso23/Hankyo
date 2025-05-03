package dao;

import model.Course;
import model.CoursePaid;
import model.Learner;
import model.Payment;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private Connection connection;

    public PaymentDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public List<CoursePaid> getAllCoursePaid() {
        List<CoursePaid> list = new ArrayList<>();
        String sql = "SELECT cp.*, c.title, u.fullName AS learnerName " +
                "FROM Course_Paid cp " +
                "JOIN Course c ON cp.courseID = c.courseID " +
                "JOIN Learner l ON cp.learnerID = l.learnerID " +
                "JOIN [User] u ON l.userID = u.userID";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(rs.getInt("courseID"), rs.getString("title"));
                Learner learner = new Learner();
                learner.setLearnerID(rs.getInt("learnerID"));
                learner.setFullName(rs.getString("learnerName"));
                /// ////////////////////////////
                Timestamp timestamp = rs.getTimestamp("datePaid");
                LocalDateTime datePaid = timestamp.toLocalDateTime();
                CoursePaid cp = new CoursePaid();
                cp.setCourseID(rs.getInt("courseID"));
                cp.setLearnerID(rs.getInt("learnerID"));
                cp.setDatePaid(datePaid);

                list.add(cp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<CoursePaid> getPaymentHistoryByLearnerID(int learnerID) {
        List<CoursePaid> list = new ArrayList<>();
        String sql = "SELECT cp.*, c.title, u.fullName AS learnerName " +
                "FROM Course_Paid cp " +
                "JOIN Course c ON cp.courseID = c.courseID " +
                "JOIN Learner l ON cp.learnerID = l.learnerID " +
                "JOIN [User] u ON l.userID = u.userID " +
                "WHERE cp.learnerID = ?";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, learnerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course course = new Course(rs.getInt("courseID"), rs.getString("title"));
                Learner learner = new Learner();
                learner.setLearnerID(learnerID);
                learner.setFullName(rs.getString("learnerName"));
                Timestamp timestamp = rs.getTimestamp("datePaid");
                LocalDateTime datePaid = timestamp.toLocalDateTime();
                CoursePaid cp = new CoursePaid();
                cp.setCourseID(rs.getInt("courseID"));
                cp.setLearnerID(rs.getInt("learnerID"));
                cp.setDatePaid(datePaid);

                list.add(cp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.fullName as learnerName FROM Payment p JOIN Learner l on p.learnerID = l.learnerID JOIN [User] u on u.userID = l.userID ORDER BY paymentDate DESC";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setLearnerID(rs.getInt("learnerID"));
                payment.setLearnerName(rs.getString("learnerName"));
                payment.setTotalAmount(rs.getBigDecimal("amount"));
                payment.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("paymentDate");
                payment.setPayDate(ts != null ? ts.toLocalDateTime() : null);
                payment.setStatus(rs.getString("status"));
                payment.setType(rs.getString("type"));
                payments.add(payment);
            }
        }
        return payments;
    }

    public List<Payment> getPaymentsByLearner(int learnerId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment p WHERE learnerID = ? ORDER BY paymentDate DESC";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, learnerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setLearnerID(rs.getInt("learnerID"));
                payment.setTotalAmount(rs.getBigDecimal("amount"));
                payment.setDescription(rs.getString("description"));
                payment.setPayDate(rs.getTimestamp("paymentDate").toLocalDateTime() != null ? rs.getTimestamp("paymentDate").toLocalDateTime() : null);
                payment.setStatus(rs.getString("status"));
                payment.setType(rs.getString("type"));
                payments.add(payment);
            }
        }
        return payments;


    }
    public boolean addPaymentWithCourses(Payment payment, List<CoursePaid> coursePaidList) {
        String paymentSQL = "INSERT INTO Payment (paymentID,amount, paymentDate,description,learnerID,status) VALUES (?,?, ?, ?, ?,?)";
        String coursePaidSQL = "INSERT INTO Course_Paid (paymentID, courseID, learnerID, datePaid) VALUES (?, ?, ?, ?)";
        String expertSQL = "SELECT expertID, price FROM Course WHERE courseID = ?";
        String updateRevenueSQL = "INSERT INTO ExpertRevenue  (expertID, totalRevenue, lastUpdated) VALUES (?, ?,?);";
        String insertPlatformRevenueSQL = "INSERT INTO Revenue (amount, date,description) VALUES (?, GETDATE(),?)";

        try (Connection connection = DBConnect.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            // 1. Insert Payment
            PreparedStatement psPayment = connection.prepareStatement(paymentSQL);
            psPayment.setString(1,payment.getPaymentID());
            psPayment.setBigDecimal(2, payment.getTotalAmount());
            psPayment.setTimestamp(3, Timestamp.valueOf(payment.getPayDate()));
            psPayment.setString(4, payment.getDescription());
            psPayment.setInt(5, payment.getLearnerID());
            psPayment.setString(6,payment.getStatus());
            psPayment.executeUpdate();

            double totalPlatformRevenue = 0.0;

            for (CoursePaid cp : coursePaidList) {
                // 2. Insert vào Course_Paid
                PreparedStatement psCoursePaid = connection.prepareStatement(coursePaidSQL);
                psCoursePaid.setString(1, payment.getPaymentID());
                psCoursePaid.setInt(2, cp.getCourseID());
                psCoursePaid.setInt(3, cp.getLearnerID());
                psCoursePaid.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                psCoursePaid.executeUpdate();

                // 3. Lấy expertID và price
                PreparedStatement psExpert = connection.prepareStatement(expertSQL);
                psExpert.setInt(1, cp.getCourseID());
                ResultSet rsExpert = psExpert.executeQuery();

                if (rsExpert.next()) {
                    int expertID = rsExpert.getInt("expertID");
                    double price = rsExpert.getDouble("price");
                    double expertShare = price * 0.95;
                    double platformShare = price * 0.05;
                    totalPlatformRevenue += platformShare;

                    // 4. Update doanh thu expert
                    PreparedStatement psRevenue = connection.prepareStatement(updateRevenueSQL);
                    psRevenue.setInt(1, expertID);
                    psRevenue.setDouble(2, expertShare);
                    psRevenue.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    psRevenue.executeUpdate();
                }
            }

            // 5. Update doanh thu nền tảng (platform)
            if (totalPlatformRevenue > 0) {
                PreparedStatement psPlatform = connection.prepareStatement(insertPlatformRevenueSQL);
                psPlatform.setDouble(1, totalPlatformRevenue);
                psPlatform.setString(2, payment.getDescription());
                psPlatform.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public BigDecimal getTotalRevenue(int expertID) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        try (Connection con = DBConnect.getInstance().getConnection()) {
            String GET_TOTAL_REVENUE_QUERY = "SELECT totalRevenue FROM ExpertRevenue WHERE expertID = ?";
            PreparedStatement ps = con.prepareStatement(GET_TOTAL_REVENUE_QUERY);
            ps.setInt(1, expertID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    // Cập nhật doanh thu sau khi rút tiền
    public boolean withdrawRevenue(int expertID, BigDecimal amountToWithdraw) {
        String UPDATE_REVENUE_QUERY = "UPDATE ExpertRevenue SET totalRevenue = totalRevenue - ? WHERE expertID = ? AND totalRevenue >= ?";
        // Lấy tổng doanh thu trước khi rút tiền
        BigDecimal totalRevenue = getTotalRevenue(expertID);

        if (totalRevenue.compareTo(amountToWithdraw) < 0) {
            // Nếu số tiền rút lớn hơn tổng doanh thu, trả về false
            return false;
        }

        // Cập nhật doanh thu sau khi rút tiền
        try (Connection con = DBConnect.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement(UPDATE_REVENUE_QUERY);
            ps.setBigDecimal(1, amountToWithdraw);
            ps.setInt(2, expertID);
            ps.setBigDecimal(3, amountToWithdraw);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createPayment(Payment payment, Connection connection) {
        String sql = "INSERT INTO Payment (paymentID, learnerID, amount, description, paymentDate, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getPaymentID());
            stmt.setInt(2, payment.getLearnerID());
            stmt.setBigDecimal(3, payment.getTotalAmount());
            stmt.setString(4, payment.getDescription());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPayDate()));
            stmt.setString(6, payment.getStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createCoursePaid(CoursePaid coursePaid, Connection connection) {
        String sql = "INSERT INTO Course_Paid (courseID, learnerID,paymentID, datePaid) VALUES (?, ?,?,GETDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, coursePaid.getCourseID());
            stmt.setInt(2, coursePaid.getLearnerID());
            stmt.setString(3, coursePaid.getPaymentID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
