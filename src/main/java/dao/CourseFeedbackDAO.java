package dao;

import model.CourseFeedback;
import model.Learner;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseFeedbackDAO {
    public List<CourseFeedback> getFeedbacksByCourseID(int courseID) {
        List<CourseFeedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, l.learnerID, l.userID, u.fullName, u.avatar " +
                    "FROM CourseFeedback f " +
                    "JOIN Learner l ON f.learnerID = l.learnerID " +
                    "JOIN [User] u ON l.userID = u.userID " +
                    "WHERE f.courseID = ? " +
                    "ORDER BY f.createdAt DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseFeedback feedback = new CourseFeedback();
                feedback.setFeedbackID(rs.getInt("feedbackID"));
                feedback.setRating(rs.getFloat("rating"));
                feedback.setComment(rs.getString("comment"));
                feedback.setCreatedAt(rs.getDate("createdAt"));

                // Set learner info
                Learner learner = new Learner();
                learner.setLearnerID(rs.getInt("learnerID"));
                learner.setUserID(rs.getInt("userID"));
                learner.setFullName(rs.getString("fullName"));
                learner.setAvatar(rs.getString("avatar"));
                feedback.setLearner(learner);

                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    public boolean addFeedback(CourseFeedback feedback) {
        String sql = "INSERT INTO CourseFeedback (courseID, learnerID, rating, comment, createdAt) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, feedback.getCourse().getCourseID());
            pstmt.setInt(2, feedback.getLearner().getLearnerID());
            pstmt.setFloat(3, feedback.getRating());
            pstmt.setString(4, feedback.getComment());
            pstmt.setDate(5, new java.sql.Date(feedback.getCreatedAt().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getAverageRating(int courseID) {
        String sql = "SELECT AVG(CAST(rating AS FLOAT)) FROM CourseFeedback WHERE courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double rating = rs.getDouble(1);
                return rating != 0 ? rating : 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getRatingCount(int courseID) {
        String sql = "SELECT COUNT(*) FROM CourseFeedback WHERE courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, Double> getRatingPercentages(int courseID) {
        Map<Integer, Double> percentages = new HashMap<>();
        int totalCount = getRatingCount(courseID);
        
        if (totalCount == 0) {
            for (int i = 1; i <= 5; i++) {
                percentages.put(i, 0.0);
            }
            return percentages;
        }

        String sql = "SELECT rating, COUNT(*) as count " +
                    "FROM CourseFeedback " +
                    "WHERE courseID = ? " +
                    "GROUP BY rating";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();

            // Khởi tạo tất cả các giá trị là 0
            for (int i = 1; i <= 5; i++) {
                percentages.put(i, 0.0);
            }

            // Cập nhật phần trăm cho các rating có dữ liệu
            while (rs.next()) {
                int rating = rs.getInt("rating");
                int count = rs.getInt("count");
                double percentage = (count * 100.0) / totalCount;
                percentages.put(rating, Math.round(percentage * 10.0) / 10.0); // Làm tròn đến 1 chữ số thập phân
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return percentages;
    }
}
