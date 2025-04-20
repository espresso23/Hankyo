package model;

public class Report {
    private int reportID;
    private int reporterID;
    private int reportedUserID;
    private int reportTypeID;
    private String reason;
    private int chatID;
    private int postID;
    private String reportDate;
    private String status;

    // Default constructor
    public Report() {
        this.status = "Pending"; // Default status
    }

    // Constructor for chat report
    public Report(int reporterID, int reportedUserID, int reportTypeID, String reason, int chatID) {
        this.reporterID = reporterID;
        this.reportedUserID = reportedUserID;
        this.reportTypeID = reportTypeID;
        this.reason = reason;
        this.chatID = chatID;
        this.status = "Pending";
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

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
