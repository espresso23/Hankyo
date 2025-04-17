package dao;

import model.Report;
import model.ReportType;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    private static final String INSERT_REPORT = "INSERT INTO Report (reporterID, reportedUserID, reportTypeID, reason, chatID, reportDate, status) VALUES (?, ?, ?, ?, ?, GETDATE(), 'Pending')";
    private static final String GET_REPORT_TYPES = "SELECT * FROM ReportType";
    private static final String GET_CHAT_BY_ID = "SELECT * FROM Chat WHERE chatID = ?";
    private static final String GET_CHAT_USER = "SELECT userID FROM Chat WHERE chatID = ?";

    // Create a new report
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

    // Get all report types
    public List<ReportType> getAllReportTypes() {
        List<ReportType> types = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_REPORT_TYPES)) {

            while (rs.next()) {
                ReportType type = new ReportType();
                type.setReportTypeID(rs.getInt("reportTypeID"));
                type.setTypeName(rs.getString("typeName"));
                types.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    // Get user ID of chat author
    public int getChatUserID(int chatID) throws SQLException {
        System.out.println("Getting userID for chatID: " + chatID);
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_CHAT_USER)) {

            ps.setInt(1, chatID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("userID");
                    System.out.println("Found userID: " + userID);
                    return userID;
                } else {
                    System.err.println("No chat found with ID: " + chatID);
                    throw new SQLException("Chat not found with ID: " + chatID);
                }
            }
        }
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