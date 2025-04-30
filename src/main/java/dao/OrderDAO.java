package dao;

import model.Order;
import dto.RevenueStatDTO;
import dto.TopCourseDTO;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;
    
    private Connection getConnectionWithRetry() throws SQLException {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                Connection conn = DBConnect.getInstance().getConnection();
                conn.setNetworkTimeout(null, 30000); // 30 seconds timeout
                return conn;
            } catch (SQLException e) {
                retries++;
                if (retries == MAX_RETRIES) {
                    throw e;
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Connection retry interrupted", ie);
                }
            }
        }
        throw new SQLException("Failed to get connection after " + MAX_RETRIES + " retries");
    }
    
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO Orders (orderID, paymentID, expertID, courseID, learnerID, orderDate, totalAmount, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            LOGGER.log(Level.SEVERE, "Error creating order: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Order> getOrdersByExpertId(int expertId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE expertID = ? ORDER BY orderDate DESC";
        
        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expertId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting orders by expert ID: " + e.getMessage(), e);
        }
        return orders;
    }

    public List<RevenueStatDTO> getRevenueStatsByPeriod(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        List<RevenueStatDTO> stats = new ArrayList<>();
        String sql;
        
        // Xác định loại filter dựa vào khoảng thời gian
        LocalDateTime now = LocalDateTime.now();
        
        // Hôm qua
        if (startDate.getDayOfYear() == now.minusDays(1).getDayOfYear() 
            && endDate.getDayOfYear() == now.getDayOfYear()) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND orderDate >= DATEADD(day, -1, CAST(GETDATE() AS DATE)) " +
                  "AND orderDate < CAST(GETDATE() AS DATE) " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Hôm nay 
        else if (startDate.getDayOfYear() == now.getDayOfYear()) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND CAST(orderDate AS DATE) = CAST(GETDATE() AS DATE) " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Tuần này
        else if (startDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == now.get(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND orderDate >= DATEADD(week, DATEDIFF(week, 0, GETDATE()), 0) " +
                  "AND orderDate < DATEADD(week, DATEDIFF(week, 0, GETDATE()) + 1, 0) " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Tháng này
        else if (startDate.getMonth() == now.getMonth()) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND orderDate >= DATEADD(month, DATEDIFF(month, 0, GETDATE()), 0) " +
                  "AND orderDate < DATEADD(month, DATEDIFF(month, 0, GETDATE()) + 1, 0) " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Tháng trước
        else if (startDate.getMonth() == now.minusMonths(1).getMonth()) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND orderDate >= DATEADD(month, DATEDIFF(month, 0, GETDATE()) - 1, 0) " +
                  "AND orderDate < DATEADD(month, DATEDIFF(month, 0, GETDATE()), 0) " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Năm nay
        else if (startDate.getYear() == now.getYear()) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND YEAR(orderDate) = YEAR(GETDATE()) " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Năm trước
        else if (startDate.getYear() == now.minusYears(1).getYear()) {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "AND YEAR(orderDate) = YEAR(GETDATE()) - 1 " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }
        // Mặc định - toàn bộ
        else {
            sql = "SELECT CAST(orderDate AS DATE) as date, status, " +
                  "COUNT(*) as orderCount, SUM(totalAmount) as totalAmount " +
                  "FROM Orders " +
                  "WHERE expertID = ? " +
                  "GROUP BY CAST(orderDate AS DATE), status " +
                  "ORDER BY date";
        }

        System.out.println("Executing revenue query for expertId=" + expertId);
        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
        System.out.println("SQL Query: " + sql);

        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, expertId);
            
            System.out.println("Executing prepared statement...");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime period = rs.getTimestamp("date").toLocalDateTime();
                    BigDecimal amount = rs.getBigDecimal("totalAmount");
                    if (amount == null) {
                        amount = BigDecimal.ZERO;
                    }
                    long orderCount = rs.getLong("orderCount");
                    String status = rs.getString("status");
                    
                    System.out.println("Found revenue stat: " +
                        "\n  Date: " + period +
                        "\n  Status: " + status +
                        "\n  Amount: " + amount +
                        "\n  Order Count: " + orderCount);
                    
                    stats.add(new RevenueStatDTO(period, amount, orderCount, status));
                }
            }
            
            System.out.println("Total revenue stats found: " + stats.size());
            for (RevenueStatDTO stat : stats) {
                System.out.println("Revenue stat: " + stat.toString());
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting revenue stats: " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }

    public List<TopCourseDTO> getTopCoursesByExpert(int expertId) {
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
        
        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expertId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int courseId = rs.getInt("courseID");
                    String title = rs.getString("title");
                    BigDecimal totalRevenue = rs.getBigDecimal("totalRevenue");
                    BigDecimal totalSales = BigDecimal.valueOf(rs.getLong("totalSales"));
                    int studentCount = rs.getInt("studentCount");
                    double rating = rs.getDouble("avgRating");
                    
                    topCourses.add(new TopCourseDTO(courseId, title, totalRevenue, totalSales, studentCount, rating));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting top courses by expert: " + e.getMessage(), e);
        }
        return topCourses;
    }

    public boolean updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE orderID = ?";
        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order status: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Order> getAllCompletedOrdersByExpert(int expertId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE expertID = ? AND status = 'Completed'";
        
        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, expertId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all completed orders by expert: " + e.getMessage(), e);
        }
        
        return orders;
    }
    
    public List<Order> getOrdersByExpertAndDateRange(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE expertID = ? AND orderDate BETWEEN ? AND ?";
        
        try (Connection conn = getConnectionWithRetry();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, expertId);
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            stmt.setTimestamp(3, Timestamp.valueOf(endDate));
            
            LOGGER.info("Executing orders query for expertId={}, startDate={}, endDate={}"
            );
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }
            
            LOGGER.info("Found {} orders");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting orders by date range: " + e.getMessage(), e);
        }
        
        return orders;
    }
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderID(rs.getString("orderID"));
        order.setPaymentID(rs.getString("paymentID"));
        order.setExpertID(rs.getInt("expertID"));
        order.setCourseID(rs.getInt("courseID"));
        order.setLearnerID(rs.getInt("learnerID"));
        order.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime());
        order.setTotalAmount(rs.getBigDecimal("totalAmount"));
        order.setStatus(rs.getString("status"));
        return order;
    }
} 