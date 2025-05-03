package dao;

import model.Vip;
import util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VipDAO {
    
    // Create new VIP package
    public boolean addVip(Vip vip) throws SQLException {
        String query = "INSERT INTO Vip (vipName, description, price, yearlyPrice, vipType, status, vip_img, features, duration, isYearly) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, vip.getVipName());
            ps.setString(2, vip.getDescription());
            ps.setDouble(3, vip.getPrice());
            ps.setDouble(4, vip.getYearlyPrice());
            ps.setString(5, vip.getVipType());
            ps.setString(6, vip.getStatus());
            ps.setString(7, vip.getVip_img());
            ps.setString(8, vip.getFeatures());
            ps.setInt(9, vip.getDuration());
            ps.setBoolean(10, vip.isYearly());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Read VIP package by ID
    public Vip getVipById(int vipId) throws SQLException {
        String query = "SELECT * FROM Vip WHERE vipID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, vipId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVip(rs);
                }
            }
        }
        return null;
    }
    
    // Get all VIP packages
    public List<Vip> getAllVips() throws SQLException {
        List<Vip> vips = new ArrayList<>();
        String query = "SELECT * FROM Vip ORDER BY vipID DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                vips.add(mapResultSetToVip(rs));
            }
        }
        return vips;
    }
    
    // Get active VIP packages
    public List<Vip> getActiveVips() throws SQLException {
        List<Vip> vips = new ArrayList<>();
        String query = "SELECT * FROM Vip WHERE status = 'ACTIVE' ORDER BY vipID DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                vips.add(mapResultSetToVip(rs));
            }
        }
        return vips;
    }
    
    // Update VIP package
    public boolean updateVip(Vip vip) throws SQLException {
        String query = "UPDATE Vip SET vipName = ?, description = ?, price = ?, yearlyPrice = ?, " +
                      "vipType = ?, status = ?, vip_img = ?, features = ?, duration = ?, isYearly = ? " +
                      "WHERE vipID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, vip.getVipName());
            ps.setString(2, vip.getDescription());
            ps.setDouble(3, vip.getPrice());
            ps.setDouble(4, vip.getYearlyPrice());
            ps.setString(5, vip.getVipType());
            ps.setString(6, vip.getStatus());
            ps.setString(7, vip.getVip_img());
            ps.setString(8, vip.getFeatures());
            ps.setInt(9, vip.getDuration());
            ps.setBoolean(10, vip.isYearly());
            ps.setInt(11, vip.getVipID());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Delete VIP package
    public boolean deleteVip(int vipId) throws SQLException {
        // Kiểm tra xem có người dùng nào đang sử dụng gói VIP này không
        if (hasActiveUsers(vipId)) {
            throw new SQLException("Không thể xóa gói VIP vì đang có người dùng đang sử dụng");
        }
        
        String query = "DELETE FROM Vip WHERE vipID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, vipId);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Check if VIP package has active users
    public boolean hasActiveUsers(int vipId) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM Vip_User WHERE vipID = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, vipId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }
    
    // Get VIP package by type
    public Vip getVipByType(String vipType) throws SQLException {
        String query = "SELECT * FROM Vip WHERE vipType = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, vipType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVip(rs);
                }
            }
        }
        return null;
    }
    
    // Get user's active VIP package
    public Vip getUserActiveVip(int learnerId) throws SQLException {
        String query = "SELECT v.* FROM Vip v " +
                      "JOIN Vip_User vu ON v.vipID = vu.vipID " +
                      "WHERE vu.learnerID = ? AND vu.status = 'ACTIVE' " +
                      "AND vu.endDate > GETDATE()";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, learnerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVip(rs);
                }
            }
        }
        return null;
    }
    
    // Get VIP package statistics
    public Map<String, Object> getVipStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        // Tổng số gói VIP
        String totalQuery = "SELECT COUNT(*) as total FROM Vip";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(totalQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("totalVips", rs.getInt("total"));
            }
        }
        
        // Số người dùng đang sử dụng từng gói
        String userQuery = "SELECT v.vipType, COUNT(*) as userCount " +
                          "FROM Vip_User vu " +
                          "JOIN Vip v ON vu.vipID = v.vipID " +
                          "WHERE vu.status = 'ACTIVE' AND vu.endDate > GETDATE() " +
                          "GROUP BY v.vipType";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(userQuery);
             ResultSet rs = ps.executeQuery()) {
            
            Map<String, Integer> userCounts = new HashMap<>();
            while (rs.next()) {
                userCounts.put(rs.getString("vipType"), rs.getInt("userCount"));
            }
            stats.put("userCounts", userCounts);
        }
        
        // Doanh thu theo gói
        String revenueQuery = "SELECT v.vipType, SUM(p.amount) as revenue " +
                            "FROM Payment p " +
                            "JOIN Vip_User vu ON p.paymentID = vu.paymentID " +
                            "JOIN Vip v ON vu.vipID = v.vipID " +
                            "WHERE p.status = 'completed' " +
                            "GROUP BY v.vipType";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(revenueQuery);
             ResultSet rs = ps.executeQuery()) {
            
            Map<String, Double> revenues = new HashMap<>();
            while (rs.next()) {
                revenues.put(rs.getString("vipType"), rs.getDouble("revenue"));
            }
            stats.put("revenues", revenues);
        }
        
        return stats;
    }
    
    // Thêm mới bản ghi Vip_User
    public boolean addVipUser(model.VipUser vipUser) throws SQLException {
        String sql = "INSERT INTO Vip_User (learnerID, vipID, startDate, endDate, status, paymentID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = util.DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vipUser.getLearnerID());
            ps.setInt(2, vipUser.getVipID());
            ps.setTimestamp(3, new java.sql.Timestamp(vipUser.getStartDate().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(vipUser.getEndDate().getTime()));
            ps.setString(5, vipUser.getStatus());
            ps.setString(6, vipUser.getPaymentID());
            return ps.executeUpdate() > 0;
        }
    }
    
    // Helper method to map ResultSet to Vip object
    private Vip mapResultSetToVip(ResultSet rs) throws SQLException {
        Vip vip = new Vip();
        vip.setVipID(rs.getInt("vipID"));
        vip.setVipName(rs.getString("vipName"));
        vip.setDescription(rs.getString("description"));
        vip.setPrice(rs.getDouble("price"));
        vip.setYearlyPrice(rs.getDouble("yearlyPrice"));
        vip.setCreateAt(rs.getTimestamp("createAt"));
        vip.setVipType(rs.getString("vipType"));
        vip.setStatus(rs.getString("status"));
        vip.setVip_img(rs.getString("vip_img"));
        vip.setFeatures(rs.getString("features"));
        vip.setDuration(rs.getInt("duration"));
        vip.setYearly(rs.getBoolean("isYearly"));
        return vip;
    }
} 