package model;
import java.sql.Timestamp;

public class Report {
    private int reportID;
    private int reporterID;
    private int reportedUserID;
    private int reportTypeID;
    private String reason;
    private int messageID;
    private int postID;
    private Timestamp reportDate;
    private String status;
    private int commentID;

    // Default constructor
    public Report() {
    }

    // Constructor with essential fields for post reporting
    public Report(int reporterID, int reportedUserID, int reportTypeID, String reason,
                  int postID, Timestamp reportDate) {
        this.reporterID = reporterID;
        this.reportedUserID = reportedUserID;
        this.reportTypeID = reportTypeID;
        this.reason = reason;
        this.postID = postID;
        this.reportDate = reportDate;
        this.status = "pending"; // Default status
    }

    // Getters and setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getReporterID() {
        return reporterID;
    }

    public void setReporterID(int reporterID) {
        this.reporterID = reporterID;
    }

    public int getReportedUserID() {
        return reportedUserID;
    }

    public void setReportedUserID(int reportedUserID) {
        this.reportedUserID = reportedUserID;
    }

    public int getReportTypeID() {
        return reportTypeID;
    }

    public void setReportTypeID(int reportTypeID) {
        this.reportTypeID = reportTypeID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }

    public void setReportDate(Timestamp reportDate) {
        this.reportDate = reportDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }
}
