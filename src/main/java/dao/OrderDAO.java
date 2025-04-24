package dao;

import model.Order;
import dto.RevenueStatDTO;
import dto.TopCourseDTO;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderDAO {
    private Connection connection;

    public OrderDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public boolean createOrder(Order order) {
        String sql = "INSERT INTO Orders (orderID, paymentID, expertID, courseID, learnerID, orderDate, totalAmount, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderID());
            stmt.setString(2, order.getPaymentID());
            stmt.setInt(3, order.getExpertID());
            stmt.setInt(4, order.getCourseID());
            stmt.setInt(5, order.getLearnerID());
            stmt.setTimestamp(6, Timestamp.valueOf(order.getOrderDate()));
            stmt.setBigDecimal(7, order.getTotalAmount());
            stmt.setString(8, order.getStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByExpertId(int expertId, Connection connection  ) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE expertID = ? ORDER BY orderDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expertId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getString("orderID"));
                order.setPaymentID(rs.getString("paymentID"));
                order.setExpertID(rs.getInt("expertID"));
                order.setCourseID(rs.getInt("courseID"));
                order.setLearnerID(rs.getInt("learnerID"));
                order.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime());
                order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<RevenueStatDTO> getRevenueStatsByPeriod(int expertId, LocalDateTime startDate, LocalDateTime endDate, Connection connection) {
        List<RevenueStatDTO> stats = new ArrayList<>();
        String sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                    "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                    "FROM Orders " +
                    "WHERE expertID = ? AND orderDate BETWEEN ? AND ? " +
                    "GROUP BY CAST(orderDate AS DATE), status " +
                    "ORDER BY date";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expertId);
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            stmt.setTimestamp(3, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LocalDateTime period = rs.getTimestamp("date").toLocalDateTime();
                BigDecimal amount = rs.getBigDecimal("totalAmount");
                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }
                long orderCount = rs.getLong("orderCount");
                String status = rs.getString("status");
                
                stats.add(new RevenueStatDTO(period, amount, orderCount, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<TopCourseDTO> getTopCoursesByExpert(int expertId, Connection connection) {
        List<TopCourseDTO> topCourses = new ArrayList<>();
        String sql = "SELECT c.courseID, c.title, " +
                    "COUNT(DISTINCT o.learnerID) as studentCount, " +
                    "SUM(o.totalAmount) as totalRevenue, " +
                    "COUNT(o.orderID) as totalSales, " +
                    "AVG(COALESCE(cf.rating, 0)) as avgRating " +
                    "FROM Course c " +
                    "LEFT JOIN Orders o ON c.courseID = o.courseID " +
                    "LEFT JOIN CourseFeedback cf ON c.courseID = cf.courseID " +
                    "WHERE c.expertID = ? " +
                    "GROUP BY c.courseID, c.title " +
                    "ORDER BY totalRevenue DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expertId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int courseId = rs.getInt("courseID");
                String title = rs.getString("title");
                BigDecimal totalRevenue = rs.getBigDecimal("totalRevenue");
                BigDecimal totalSales = BigDecimal.valueOf(rs.getLong("totalSales"));
                int studentCount = rs.getInt("studentCount");
                double rating = rs.getDouble("avgRating");
                
                topCourses.add(new TopCourseDTO(courseId, title, totalRevenue, totalSales, studentCount, rating));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topCourses;
    }


    public boolean updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE orderID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 