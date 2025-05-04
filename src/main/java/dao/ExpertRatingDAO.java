package dao;

import model.ExpertRating;
import util.DBConnect;
import java.sql.*;

public class ExpertRatingDAO {
    public boolean addRating(ExpertRating rating) {
        String sql = "INSERT INTO expertRating (expertID, learnerID, teachingQuality, replyQuality, courseQuality, friendlyQuality) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rating.getExpertID());
            ps.setInt(2, rating.getLearnerID());
            ps.setObject(3, rating.getTeachingQuality(), java.sql.Types.INTEGER);
            ps.setObject(4, rating.getReplyQuality(), java.sql.Types.INTEGER);
            ps.setObject(5, rating.getCourseQuality(), java.sql.Types.INTEGER);
            ps.setInt(6, rating.getFriendlyQuality());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 