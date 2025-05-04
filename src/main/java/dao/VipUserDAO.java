package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import util.DBConnect;

public class VipUserDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public boolean isVipUser(int userId) throws Exception {
        String sql = "SELECT 1 FROM Vip_User vu JOIN Vip vd ON vu.vipID = vd.vipID WHERE vu.learnerID = ? AND vu.status = 'active' AND vu.endDate >= GETDATE()";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
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
        if (isVipUser(learnerID)) return true;
        return getTodayUsageCount(learnerID) < 20;
    }
}
