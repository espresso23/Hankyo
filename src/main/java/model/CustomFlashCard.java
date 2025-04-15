package model;

public class CustomFlashCard {
   private int CFCID;
    private Integer learnerID;
   private String word;
   private String mean;
    private String topic;
    public CustomFlashCard(int learnerID) {
    }


    public CustomFlashCard(Integer learnerID, String word, String mean, String topic) {
        this.learnerID = learnerID;
        this.word = word;
        this.mean = mean;
        this.topic = topic;
    }

    public CustomFlashCard(int CFCID, String topic, String word, String mean, Integer learnerID) {
        this.CFCID = CFCID;
        this.topic = topic;
        this.word = word;
        this.mean = mean;
        this.learnerID = learnerID;
    }

    public CustomFlashCard(String word, String mean, String topic) {
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

    @Override
    public String toString() {
        return "CustomFlashCard{" +
                "CFCID=" + CFCID +
                ", topic='" + topic + '\'' +
                ", word='" + word + '\'' +
                ", mean='" + mean + '\'' +
                ", learnerID=" + learnerID +
                '}';
    }
}
