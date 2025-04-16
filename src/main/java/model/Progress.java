package model;

import java.sql.Timestamp;

public class Progress {
    private int learnerID;
    private int courseID;
    private int contentID;
    private String status;
    private Timestamp lastUpdated;
    private String contentTitle;
    private String contentType;

    // Constructors
    public Progress() {}

    public Progress(int learnerID, int courseID, int contentID, String status) {
        this.learnerID = learnerID;
        this.courseID = courseID;
        this.contentID = contentID;
        this.status = status;
    }

    // Getters and Setters
    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
} 