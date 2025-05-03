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
    public boolean createPostReport(Report report) {
        String sql = "INSERT INTO Report (PostID, ReporterID, ReportTypeID, Reason, Status, ReportDate) " +
                "VALUES (?, ?, ?, ?,?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getPostID());
            ps.setInt(2, report.getReporterID());
            ps.setInt(3, report.getReportTypeID());
            ps.setString(4, report.getReason());
            ps.setString(5, report.getStatus());

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createCommentReport(Report report) {
        String sql = "INSERT INTO Report (CommentID, PostID, ReporterID, ReportTypeID, Reason, Status, ReportDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getCommentID());
            ps.setInt(2, report.getPostID());
            ps.setInt(3, report.getReporterID());
            ps.setInt(4, report.getReportTypeID());
            ps.setString(5, report.getReason());
            ps.setString(6, report.getStatus());

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Report> getReportedPostsByUserID(int userID) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT r.*, p.Heading FROM Report r JOIN Post p ON r.PostID = p.PostID " +
                "WHERE r.ReporterID = ? AND r.CommentID IS NULL";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.setReportID(rs.getInt("ReportID"));
                report.setPostID(rs.getInt("PostID"));
                report.setReporterID(rs.getInt("ReporterID"));
                report.setReportTypeID(rs.getInt("ReportTypeID"));
                report.setReason(rs.getString("Reason"));
                report.setStatus(rs.getString("Status"));
                report.setReportDate(rs.getTimestamp("ReportDate"));
                report.setPostTitle(rs.getString("Heading"));
                reports.add(report);
            }
        }

        return reports;
    }

    public List<Report> getReportedCommentsByUserID(int userID) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT r.*, c.Content FROM Report r JOIN Comment c ON r.CommentID = c.CommentID " +
                "WHERE r.ReporterID = ? AND r.CommentID IS NOT NULL";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.setReportID(rs.getInt("ReportID"));
                report.setCommentID(rs.getInt("CommentID"));
                report.setPostID(rs.getInt("PostID"));
                report.setReporterID(rs.getInt("ReporterID"));
                report.setReportTypeID(rs.getInt("ReportTypeID"));
                report.setReason(rs.getString("Reason"));
                report.setStatus(rs.getString("Status"));
                report.setReportDate(rs.getTimestamp("ReportDate"));
                report.setCommentContent(rs.getString("Content"));
                reports.add(report);
            }
        }

        return reports;
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
    // Delete a report by ID
    public boolean deleteReportByID(int reportID) {
        String sql = "DELETE FROM Report WHERE ReportID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reportID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}