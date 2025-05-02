package service;

import dao.ProgressDAO;
import model.Progress;
import util.DBConnect;

import java.sql.*;
import java.util.List;

public class ProgressService {
    private final ProgressDAO progressDAO = new ProgressDAO();

    // Cập nhật tiến độ học tập
    public void updateProgress(int learnerID, int courseID, int contentID, String status) throws SQLException {
        progressDAO.saveProgress(learnerID, courseID, contentID, status);
    }

    // Lấy tiến độ của một khóa học
    public List<Progress> getCourseProgress(int learnerID, int courseID) throws SQLException {
        return progressDAO.getCourseProgress(learnerID, courseID);
    }

    // Tính phần trăm hoàn thành của khóa học
    public int calculateCourseProgress(int learnerID, int courseID) throws SQLException {
        int completedCount = progressDAO.getCompletedContentCount(learnerID, courseID);
        int totalCount = getTotalContentCount(courseID);
        
        if (totalCount == 0) return 0;
        return (int) ((completedCount * 100.0) / totalCount);
    }

    // Đánh dấu nội dung đã hoàn thành
    public void markContentCompleted(int learnerID, int courseID, int contentID) throws SQLException {
        progressDAO.markContentCompleted(learnerID, courseID, contentID);
    }

    // Đánh dấu nội dung đang học
    public void markContentInProgress(int learnerID, int courseID, int contentID) throws SQLException {
        progressDAO.markContentInProgress(learnerID, courseID, contentID);
    }

    // Lấy trạng thái của một nội dung
    public String getContentStatus(int learnerID, int courseID, int contentID) throws SQLException {
        return progressDAO.getContentStatus(learnerID, courseID, contentID);
    }

    // Lấy tổng số nội dung của khóa học
    private int getTotalContentCount(int courseID) throws SQLException {
        String sql = "SELECT COUNT(*) as totalCount FROM Course_Content WHERE courseID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalCount");
            }
        }
        return 0;
    }

    public List<Integer> getCompletedContentIDs(int learnerID, int courseID) throws SQLException {
        return progressDAO.getCompletedContentIDs(learnerID, courseID);
    }
} 