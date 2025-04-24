package dao;

import model.Signification;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignificationDAO {
    private Connection conn;

    public SignificationDAO() {
        conn = DBConnect.getInstance().getConnection();
    }

    public List<Signification> getUnreadSignifications(int userID) {
        List<Signification> significations = new ArrayList<>();
        String sql = "SELECT s.*, st.typeName, " +
                "CASE " +
                "   WHEN st.typeName = 'Forum' THEN (SELECT title FROM Post WHERE postID = s.sourceID) " +
                "   WHEN st.typeName = 'Comment' THEN (SELECT content FROM Comment WHERE commentID = s.sourceID) " +
                "   WHEN st.typeName = 'Course' THEN (SELECT courseName FROM Course WHERE courseID = s.sourceID) " +
                "   WHEN st.typeName = 'Honour' THEN (SELECT honourName FROM Honour WHERE honourID = s.sourceID) " +
                "END as sourceTitle " +
                "FROM Signification s " +
                "JOIN SignificationType st ON s.typeID = st.typeID " +
                "WHERE s.userID = ? AND s.isRead = 0 " +
                "ORDER BY s.dateGiven DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    significations.add(mapSignification(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return significations;
    }

    public int getUnreadCount(int userID) {
        String sql = "SELECT COUNT(*) as count FROM Signification WHERE userID = ? AND isRead = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void markAsRead(int significationID) {
        String sql = "UPDATE Signification SET isRead = 1 WHERE significationID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, significationID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAllAsRead(int userID) {
        String sql = "UPDATE Signification SET isRead = 1 WHERE userID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSignification(Signification signification) {
        String sql = "INSERT INTO Signification (userID, typeID, sourceID, description, dateGiven, isRead) " +
                "VALUES (?, ?, ?, ?, GETDATE(), 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, signification.getUserID());
            ps.setInt(2, signification.getTypeID());
            ps.setInt(3, signification.getSourceID());
            ps.setString(4, signification.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Signification mapSignification(ResultSet rs) throws SQLException {
        Signification sig = new Signification();
        sig.setSignificationID(rs.getInt("significationID"));
        sig.setUserID(rs.getInt("userID"));
        sig.setTypeID(rs.getInt("typeID"));
        sig.setSourceID(rs.getInt("sourceID"));
        sig.setDescription(rs.getString("description"));
        sig.setDateGiven(rs.getTimestamp("dateGiven"));
        sig.setTypeName(rs.getString("typeName"));
        sig.setSourceTitle(rs.getString("sourceTitle"));
        return sig;
    }
}