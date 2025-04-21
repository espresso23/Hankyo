package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Report;
import model.ReportType;
import util.DBConnect;

public class ReportDAO {

    public boolean createReport(Report report) throws SQLException {
        String sql = "INSERT INTO Report (reporterID, reportedUserID, reportTypeID, reason, chatID, reportDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, report.getReporterID());
            ps.setInt(2, report.getReportedUserID());
            ps.setInt(3, report.getReportTypeID());
            ps.setString(4, report.getReason());
            ps.setInt(5, report.getChatID());
            return ps.executeUpdate() > 0;
        }
    }
    // Create a new report
    public boolean createPostReport(Report report) {
        String sql = "INSERT INTO Report (PostID, ReporterID, ReportTypeID, Reason, ReportDate) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getPostID());
            ps.setInt(2, report.getReporterID());
            ps.setInt(3, report.getReportTypeID());
            ps.setString(4, report.getReason());

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // Get all report types
    public List<ReportType> getAllReportTypes() throws SQLException {
        List<ReportType> reportTypes = new ArrayList<>();
        String sql = "SELECT reportTypeID, typeName FROM ReportType";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ReportType type = new ReportType();
                type.setReportTypeID(rs.getInt("reportTypeID"));
                type.setTypeName(rs.getString("typeName"));
                reportTypes.add(type);
            }
        }

        return reportTypes;
    }
    // Get chat report details
    public Report getChatReport(int chatID) {
        String sql = "SELECT c.chatID, c.userID " +
                "FROM Chat c " +
                "WHERE c.chatID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chatID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Report report = new Report();
                    report.setChatID(chatID);
                    report.setReportedUserID(rs.getInt("userID"));
                    return report;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Get user ID of post author
    public int getPostAuthorID(int postID) throws SQLException {
        String sql = "SELECT UserID FROM Post WHERE PostID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UserID");
                } else {
                    throw new SQLException("Post not found with ID: " + postID);
                }
            }
        }
    }
    // Create a new chat report
    public boolean createChatReport(Report report) throws SQLException {
        // Debug log
        System.out.println("Creating chat report with values:");
        System.out.println("reporterID: " + report.getReporterID());
        System.out.println("reportedUserID: " + report.getReportedUserID());
        System.out.println("reason: " + report.getReason());
        System.out.println("chatID: " + report.getChatID());

        // Set reportTypeID to 1 (Report Chat)
        report.setReportTypeID(1);

        // Kiểm tra xem reportTypeID có tồn tại trong bảng ReportType không
        String checkTypeSQL = "SELECT COUNT(*) FROM ReportType WHERE reportTypeID = ? AND typeName = 'Report Chat'";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkTypeSQL)) {
            checkStmt.setInt(1, report.getReportTypeID());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                System.err.println("ReportTypeID " + report.getReportTypeID() + " with typeName 'Report Chat' does not exist in ReportType table");
                return false;
            }
        }

        // Nếu reportTypeID hợp lệ, thực hiện insert
        String sql = "INSERT INTO Report (reporterID, reportedUserID, reportTypeID, reason, chatID, reportDate, status) VALUES (?, ?, ?, ?, ?, GETDATE(), 'Pending')";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, report.getReporterID());
            ps.setInt(2, report.getReportedUserID());
            ps.setInt(3, report.getReportTypeID());
            ps.setString(4, report.getReason());
            ps.setInt(5, report.getChatID());

            int result = ps.executeUpdate();
            System.out.println("ExecuteUpdate result: " + result);
            return result > 0;
        }
    }
}