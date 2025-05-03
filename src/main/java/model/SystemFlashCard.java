package model;

import java.io.Serializable;

public class SystemFlashCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private int hiddenID;
    private int SFCID;
    private int wordID;
    private String topic;
    private Dictionary dictionary; // Thêm thuộc tính Dictionary

    public SystemFlashCard(int hiddenID, int SFCID, int wordID, String topic) {
        this.hiddenID = hiddenID;
        this.SFCID = SFCID;
        this.wordID = wordID;
        this.topic = topic;
    }

    public SystemFlashCard(int SFCID, int wordID, String topic) {
        this.SFCID = SFCID;
        this.wordID = wordID;
        this.topic = topic;
    }

    public int getHiddenID() {
        return hiddenID;
    }

    public void setHiddenID(int hiddenID) {
        this.hiddenID = hiddenID;
    }

    public SystemFlashCard() {
    }

    public int getSFCID() {
        return SFCID;
    }

    public void setSFCID(int SFCID) {
        this.SFCID = SFCID;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String toString() {
        return "SystemFlashCard{" +
                "SFCID=" + SFCID +
                ", wordID=" + wordID +
                ", topic='" + topic + '\'' +
                ", dictionary=" + dictionary +
                '}';
    }
}