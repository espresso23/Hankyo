package model;

public class CustomFlashCard {
    private int CFCID;
    private Integer learnerID;
    private String word;
    private String mean;
    private String topic;
    private boolean isPublic;
    private boolean canEdit;

    public CustomFlashCard(Integer learnerID, String word, String mean, String topic) {
        this.learnerID = learnerID;
        this.word = word;
        this.mean = mean;
        this.topic = topic;
        this.isPublic = false;
        this.canEdit = false;
    }

    public CustomFlashCard(int CFCID, Integer learnerID, String word, String mean, String topic) {
        this.CFCID = CFCID;
        this.learnerID = learnerID;
        this.word = word;
        this.mean = mean;
        this.topic = topic;
        this.isPublic = false;
        this.canEdit = false;
    }

    public CustomFlashCard(int CFCID, Integer learnerID, String word, String mean, String topic, boolean isPublic) {
        this.CFCID = CFCID;
        this.learnerID = learnerID;
        this.word = word;
        this.mean = mean;
        this.topic = topic;
        this.isPublic = isPublic;
        this.canEdit = false;
    }

    public int getCFCID() {
        return CFCID;
    }

    public void setCFCID(int CFCID) {
        this.CFCID = CFCID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public Integer getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(Integer learnerID) {
        this.learnerID = learnerID;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public String toString() {
        return "CustomFlashCard{" +
                "CFCID=" + CFCID +
                ", learnerID=" + learnerID +
                ", word='" + word + '\'' +
                ", mean='" + mean + '\'' +
                ", topic='" + topic + '\'' +
                ", isPublic=" + isPublic +
                ", canEdit=" + canEdit +
                '}';
    }
}