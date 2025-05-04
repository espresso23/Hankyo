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

    public boolean isVipUser(int userID) throws Exception {
        // Đầu tiên lấy learnerID từ userID
        String sql = "SELECT * FROM Vip_User v " +
                     "INNER JOIN Learner l ON v.learnerID = l.learnerID " +
                     "WHERE l.userID = ? AND v.status = 'ACTIVE' AND v.endDate >= GETDATE()";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
