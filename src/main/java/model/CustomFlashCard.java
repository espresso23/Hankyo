package model;

public class CustomFlashCard {
<<<<<<< HEAD
   private int CFCID;
    private Integer learnerID;
   private String word;
   private String mean;
    private String topic;
    public CustomFlashCard(int learnerID) {
    }

=======
    private int CFCID;
    private Integer learnerID;
    private String word;
    private String mean;
    private String topic;
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447

    public CustomFlashCard(Integer learnerID, String word, String mean, String topic) {
        this.learnerID = learnerID;
        this.word = word;
        this.mean = mean;
        this.topic = topic;
    }

<<<<<<< HEAD
    public CustomFlashCard(int CFCID, String topic, String word, String mean, Integer learnerID) {
        this.CFCID = CFCID;
        this.topic = topic;
        this.word = word;
        this.mean = mean;
        this.learnerID = learnerID;
=======
    public CustomFlashCard(int CFCID, Integer learnerID, String word, String mean, String topic) {
        this.CFCID = CFCID;
        this.learnerID = learnerID;
        this.word = word;
        this.mean = mean;
        this.topic = topic;
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447
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
<<<<<<< HEAD
                ", topic='" + topic + '\'' +
                ", word='" + word + '\'' +
                ", mean='" + mean + '\'' +
                ", learnerID=" + learnerID +
                '}';
    }
}
=======
                ", learnerID=" + learnerID +
                ", word='" + word + '\'' +
                ", mean='" + mean + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447
