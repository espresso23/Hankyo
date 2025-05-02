package dao;

import model.Progress;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressDAO {
    
    // Lưu tiến độ học tập
    public void saveProgress(int learnerID, int courseID, int contentID, String status) throws SQLException {
        String sql = "MERGE INTO Progress AS target " +
                    "USING (VALUES (?, ?, ?, ?, GETDATE())) AS source (learnerID, courseID, contentID, status, lastUpdated) " +
                    "ON target.learnerID = source.learnerID AND target.courseID = source.courseID AND target.contentID = source.contentID " +
                    "WHEN MATCHED THEN " +
                    "    UPDATE SET status = source.status, lastUpdated = source.lastUpdated " +
                    "WHEN NOT MATCHED THEN " +
                    "    INSERT (learnerID, courseID, contentID, status, lastUpdated) " +
                    "    VALUES (source.learnerID, source.courseID, source.contentID, source.status, source.lastUpdated);";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, courseID);
            pstmt.setInt(3, contentID);
            pstmt.setString(4, status);
            pstmt.executeUpdate();
        }
    }

    // Lấy trạng thái tiến độ của một nội dung
    public String getContentStatus(int learnerID, int courseID, int contentID) throws SQLException {
        String sql = "SELECT status FROM Progress WHERE learnerID = ? AND courseID = ? AND contentID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, courseID);
            pstmt.setInt(3, contentID);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        }
        return "NOT_STARTED";
    }

    // Lấy danh sách tiến độ của một khóa học
    public List<Progress> getCourseProgress(int learnerID, int courseID) throws SQLException {
        List<Progress> progressList = new ArrayList<>();
        String sql = "SELECT p.*, cc.title as contentTitle, cc.type as contentType " +
                    "FROM Progress p " +
                    "JOIN Course_Content cc ON p.contentID = cc.courseContentID " +
                    "WHERE p.learnerID = ? AND p.courseID = ? " +
                    "ORDER BY cc.orderNumber";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, courseID);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Progress progress = new Progress();
                progress.setLearnerID(rs.getInt("learnerID"));
                progress.setCourseID(rs.getInt("courseID"));
                progress.setContentID(rs.getInt("contentID"));
                progress.setStatus(rs.getString("status"));
                progress.setLastUpdated(rs.getTimestamp("lastUpdated"));
                progress.setContentTitle(rs.getString("contentTitle"));
                progress.setContentType(rs.getString("contentType"));
                progressList.add(progress);
            }
        }
        return progressList;
    }

    // Đánh dấu nội dung đã hoàn thành
    public void markContentCompleted(int learnerID, int courseID, int contentID) throws SQLException {
        saveProgress(learnerID, courseID, contentID, "COMPLETED");
    }

    // Đánh dấu nội dung đang học
    public void markContentInProgress(int learnerID, int courseID, int contentID) throws SQLException {
        saveProgress(learnerID, courseID, contentID, "IN_PROGRESS");
    }

    // Tính tổng số nội dung đã hoàn thành trong khóa học
    public int getCompletedContentCount(int learnerID, int courseID) throws SQLException {
        String sql = "SELECT COUNT(*) as completedCount " +
                    "FROM Progress " +
                    "WHERE learnerID = ? AND courseID = ? AND status = 'COMPLETED'";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, courseID);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("completedCount");
            }
        }
        return 0;
    }
} 