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
        String sql = "SELECT 1 FROM Vip_User vu JOIN VipDetails vd ON vu.vipID = vd.vipID WHERE vu.learnerID = ? AND vu.vipStatus = 'active' AND vu.endDate >= GETDATE()";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }


    }
}
