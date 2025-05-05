package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import util.DBConnect;
import model.Learner;

public class VipUserDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public boolean isVipUser(int userID) throws Exception {
        // Đầu tiên, chuyển đổi từ userID sang learnerID
        Learner learner = null;
        String learnerQuery = "SELECT learnerID FROM Learner WHERE userID = ?";
        int learnerID = -1;
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(learnerQuery)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    learnerID = rs.getInt("learnerID");
                } else {
                    // User không phải learner, không thể là VIP
                    return false;
                }
            }
        }
        
        // Sau đó kiểm tra VIP với learnerID
        String sql = "SELECT 1 FROM Vip_User WHERE learnerID = ? AND status = 'ACTIVE' AND endDate >= GETDATE()";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, learnerID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Lấy số lần sử dụng AI của learner trong ngày hôm nay
     */
    public int getTodayUsageCount(int learnerID) throws SQLException {
        String sql = "SELECT usageCount FROM AI_Usage WHERE learnerID = ? AND usageDate = CAST(GETDATE() AS DATE)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("usageCount");
            }
        }
        return 0;
    }

    /**
     * Tăng số lần sử dụng AI của learner trong ngày hôm nay lên 1
     */
    public void incrementTodayUsage(int learnerID) throws SQLException {
        String selectSql = "SELECT usageCount FROM AI_Usage WHERE learnerID = ? AND usageDate = CAST(GETDATE() AS DATE)";
        String updateSql = "UPDATE AI_Usage SET usageCount = usageCount + 1 WHERE learnerID = ? AND usageDate = CAST(GETDATE() AS DATE)";
        String insertSql = "INSERT INTO AI_Usage (learnerID, usageDate, usageCount) VALUES (?, CAST(GETDATE() AS DATE), 1)";
        try (Connection conn = DBConnect.getInstance().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, learnerID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Đã có record hôm nay, update
                        try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                            ups.setInt(1, learnerID);
                            ups.executeUpdate();
                        }
                    } else {
                        // Chưa có, insert mới
                        try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                            ins.setInt(1, learnerID);
                            ins.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    /**
     * Kiểm tra learner có thể dùng AI không (VIP thì luôn true, thường thì tối đa 20 lần/ngày)
     */
    public boolean canUseAI(int learnerID) throws Exception {
        // Cần chuyển đổi từ userID sang learnerID nếu cần
        int learnerIDToCheck = learnerID;
        if (learnerID > 1000) { // Giả sử một ngưỡng để phân biệt userID và learnerID
            // Nếu đây là userID, cần lấy learnerID tương ứng
            String query = "SELECT learnerID FROM Learner WHERE userID = ?";
            try (Connection conn = DBConnect.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, learnerID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        learnerIDToCheck = rs.getInt("learnerID");
                    } else {
                        return false; // Không phải learner
                    }
                }
            }
        }
        
        if (isVipUser(learnerIDToCheck)) return true;
        return getTodayUsageCount(learnerIDToCheck) < 20;
    }
}
