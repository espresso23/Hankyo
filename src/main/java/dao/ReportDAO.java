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

    // Create a new report
//    public int createReport(Report report) throws SQLException {
//        String sql = "INSERT INTO Report (reporterID, reportedUserID, reportTypeID, reason, postID, reportDate, status) "
//                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        try (Connection conn = DBConnect.getInstance().getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            stmt.setInt(1, report.getReporterID());
//            stmt.setInt(2, report.getReportedUserID());
//            stmt.setInt(3, report.getReportTypeID());
//            stmt.setString(4, report.getReason());
//            stmt.setInt(5, report.getPostID());
//            stmt.setTimestamp(6, report.getReportDate());
//            stmt.setString(7, report.getStatus());
//
//            int affectedRows = stmt.executeUpdate();
//
//            if (affectedRows == 0) {
//                throw new SQLException("Creating report failed, no rows affected.");
//            }
//
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    return generatedKeys.getInt(1);
//                } else {
//                    throw new SQLException("Creating report failed, no ID obtained.");
//                }
//            }
//        }
//    }

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
}