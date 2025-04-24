package model;

import java.util.Date;

public class Signification {
    private int significationID;
    private int userID;
    private int typeID;
    private Integer sourceID;
    private String description;
    private Date dateGiven;
    private String typeName; // For display purposes
    private String sourceTitle; // For display purposes (e.g., post title, comment content)

    public Signification() {
    }

    public Signification(int significationID, int userID, int typeID, Integer sourceID, 
                        String description, Date dateGiven) {
        this.significationID = significationID;
        this.userID = userID;
        this.typeID = typeID;
        this.sourceID = sourceID;
        this.description = description;
        this.dateGiven = dateGiven;
    }

    // Getters and Setters
    public int getSignificationID() {
        return significationID;
    }

    public void setSignificationID(int significationID) {
        this.significationID = significationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(Date dateGiven) {
        this.dateGiven = dateGiven;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }
} 