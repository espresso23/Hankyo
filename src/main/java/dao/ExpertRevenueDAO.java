package dao;

import model.ExpertRevenue;
import util.DBConnect;

import java.sql.*;
import java.time.LocalDateTime;

public class ExpertRevenueDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public ExpertRevenueDAO() {
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) DBConnect.getInstance().closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ExpertRevenue getExpertRevenue(int expertId) {
        ExpertRevenue revenue = null;
        String sql = "SELECT * FROM ExpertRevenue WHERE expertID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, expertId);
            rs = ps.executeQuery();

            if (rs.next()) {
                revenue = new ExpertRevenue(
                    rs.getInt("expertID"),
                    rs.getDouble("totalRevenue"),
                    rs.getTimestamp("lastUpdated").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return revenue;
    }

    public boolean updateExpertRevenue(int expertId, double amount) {
        boolean success = false;
        String sql = "UPDATE ExpertRevenue SET totalRevenue = totalRevenue + ?, lastUpdated = ? WHERE expertID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, expertId);

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }

    public boolean createExpertRevenue(ExpertRevenue revenue) {
        boolean success = false;
        String sql = "INSERT INTO ExpertRevenue (expertID, totalRevenue, lastUpdated) VALUES (?, ?, ?)";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, revenue.getExpertID());
            ps.setDouble(2, revenue.getTotalRevenue());
            ps.setTimestamp(3, Timestamp.valueOf(revenue.getLastUpdated()));

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }
} 