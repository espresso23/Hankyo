package model;

import java.util.Date;

public class Reward {
    private int rewardID;
    private String rewardName;
    private String icon;
    private Date dateCreated;

    public Reward() {
    }

    public Reward(int rewardID, String rewardName, String icon, Date dateCreated) {
        this.rewardID = rewardID;
        this.rewardName = rewardName;
        this.icon = icon;
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getRewardID() {
        return rewardID;
    }

    public void setRewardID(int rewardID) {
        this.rewardID = rewardID;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String displayInfo() {
        return "Reward ID: " + rewardID + "\n" + "Reward Name: " + rewardName + "\n" + "Reward Icon: " + icon + "\n" + "Date Created: " + dateCreated + "\n";
    }
}
