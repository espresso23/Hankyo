package dao;

import dto.TransactionDTO;
import util.DBConnect;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionDAO {
    private static final Logger LOGGER = Logger.getLogger(TransactionDAO.class.getName());

    public List<TransactionDTO> getAllTransactions(int expertId, int page, int pageSize) {
        List<TransactionDTO> transactions = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        String sql = "SELECT o.orderID as orderId, o.orderDate as orderDate, " +
                    "o.totalAmount as totalAmount, c.title as courseName, u.fullName as learnerName, " +
                    "u.gmail, o.status, o.courseID as courseId, o.learnerID as learnerId " +
                    "FROM [Hankyo].[dbo].[Orders] o " +
                    "INNER JOIN [Hankyo].[dbo].[Course] c ON o.courseID = c.courseID " +
                    "INNER JOIN [Hankyo].[dbo].[User] u ON o.learnerID = u.userID " +
                    "WHERE c.expertID = ? " +
                    "ORDER BY o.orderDate DESC " +
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, expertId);
            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TransactionDTO transaction = mapResultSetToTransaction(rs);
                    LOGGER.info("Mapped transaction: " + transaction.toString());
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all transactions", e);
        }
        return transactions;
    }

    public List<TransactionDTO> searchTransactions(int expertId, String searchTerm, 
            LocalDateTime startDate, LocalDateTime endDate, int page, int pageSize) {
        List<TransactionDTO> transactions = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String sql = "SELECT o.orderID as orderId, o.orderDate as orderDate, " +
                    "o.totalAmount as totalAmount, c.title as courseName, u.fullName as learnerName, " +
                    "u.gmail, o.status, o.courseID as courseId, o.learnerID as learnerId " +
                    "FROM [Hankyo].[dbo].[Orders] o " +
                    "INNER JOIN [Hankyo].[dbo].[Course] c ON o.courseID = c.courseID " +
                    "INNER JOIN [Hankyo].[dbo].[User] u ON o.learnerID = u.userID " +
                    "WHERE c.expertID = ? " +
                    "AND (c.title LIKE ? OR u.fullName LIKE ? OR CAST(o.orderID as VARCHAR) LIKE ?) " +
                    "AND (o.orderDate BETWEEN ? AND ?) " +
                    "ORDER BY o.orderDate DESC " +
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setInt(1, expertId);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setTimestamp(5, Timestamp.valueOf(startDate));
            stmt.setTimestamp(6, Timestamp.valueOf(endDate));
            stmt.setInt(7, offset);
            stmt.setInt(8, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TransactionDTO transaction = mapResultSetToTransaction(rs);
                    LOGGER.info("Mapped transaction: " + transaction.toString());
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching transactions", e);
        }
        return transactions;
    }

    public int getTotalTransactions(int expertId) {
        String sql = "SELECT COUNT(*) FROM [Hankyo].[dbo].[Orders] o " +
                    "INNER JOIN [Hankyo].[dbo].[Course] c ON o.courseID = c.courseID " +
                    "WHERE c.expertID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, expertId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total transactions count", e);
        }
        return 0;
    }

    public int getTotalFilteredTransactions(int expertId, String searchTerm, 
            LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM [Hankyo].[dbo].[Orders] o " +
                    "INNER JOIN [Hankyo].[dbo].[Course] c ON o.courseID = c.courseID " +
                    "INNER JOIN [Hankyo].[dbo].[User] u ON o.learnerID = u.userID " +
                    "WHERE c.expertID = ? " +
                    "AND (c.title LIKE ? OR u.fullName LIKE ? OR CAST(o.orderID as VARCHAR) LIKE ?) " +
                    "AND (o.orderDate BETWEEN ? AND ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setInt(1, expertId);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setTimestamp(5, Timestamp.valueOf(startDate));
            stmt.setTimestamp(6, Timestamp.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting filtered transactions count", e);
        }
        return 0;
    }

    private TransactionDTO mapResultSetToTransaction(ResultSet rs) throws SQLException {
        TransactionDTO dto = new TransactionDTO(
            rs.getString("orderId"),
            rs.getTimestamp("orderDate").toLocalDateTime(),
            rs.getBigDecimal("totalAmount"),
            rs.getString("courseName"),
            rs.getString("learnerName"),
            rs.getString("gmail"),
            rs.getString("status"),
            rs.getInt("courseId"),
            rs.getInt("learnerId")
        );
        LOGGER.info("ResultSet to DTO mapping - gmail: " + rs.getString("gmail"));
        return dto;
    }
} 