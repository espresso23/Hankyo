package model;

public class ExpertRating {
    private int ratingID;
    private int expertID;
    private int learnerID;
    private Integer teachingQuality;
    private Integer replyQuality;
    private Integer courseQuality;
    private int friendlyQuality;

    public ExpertRating() {}

    public ExpertRating(int expertID, int learnerID, Integer teachingQuality, Integer replyQuality, Integer courseQuality, int friendlyQuality) {
        this.expertID = expertID;
        this.learnerID = learnerID;
        this.teachingQuality = teachingQuality;
        this.replyQuality = replyQuality;
        this.courseQuality = courseQuality;
        this.friendlyQuality = friendlyQuality;
    }

    public int getRatingID() { return ratingID; }
    public void setRatingID(int ratingID) { this.ratingID = ratingID; }
    public int getExpertID() { return expertID; }
    public void setExpertID(int expertID) { this.expertID = expertID; }
    public int getLearnerID() { return learnerID; }
    public void setLearnerID(int learnerID) { this.learnerID = learnerID; }
    public Integer getTeachingQuality() { return teachingQuality; }
    public void setTeachingQuality(Integer teachingQuality) { this.teachingQuality = teachingQuality; }
    public Integer getReplyQuality() { return replyQuality; }
    public void setReplyQuality(Integer replyQuality) { this.replyQuality = replyQuality; }
    public Integer getCourseQuality() { return courseQuality; }
    public void setCourseQuality(Integer courseQuality) { this.courseQuality = courseQuality; }
    public int getFriendlyQuality() { return friendlyQuality; }
    public void setFriendlyQuality(int friendlyQuality) { this.friendlyQuality = friendlyQuality; }
} 