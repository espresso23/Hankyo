package model;

import java.io.Serializable;

public class FavoriteFlashCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private int FCID;
    private Dictionary dictionary;
    private String nameOfList;
    private Learner learner;

    public FavoriteFlashCard(int FCID, Dictionary dictionary, Learner learner) {
        this.FCID = FCID;
        this.dictionary = dictionary;
        this.learner = learner;
    }

    public FavoriteFlashCard(int FCID, Dictionary dictionary, String nameOfList) {
        this.FCID = FCID;
        this.dictionary = dictionary;
        this.nameOfList = nameOfList;
    }

    public FavoriteFlashCard(Dictionary dictionary, Learner learner) {
        this.dictionary = dictionary;
        this.learner = learner;
    }

    public FavoriteFlashCard(Dictionary dictionary, String nameOfList) {
        this.dictionary = dictionary;
        this.nameOfList = nameOfList;
    }

    public FavoriteFlashCard(int FCID, Dictionary dictionary, String nameOfList, Learner learner) {
        this.FCID = FCID;
        this.dictionary = dictionary;
        this.nameOfList = nameOfList;
        this.learner = learner;
    }

    public FavoriteFlashCard(Dictionary dictionary, String nameOfList, Learner learner) {
        this.dictionary = dictionary;
        this.nameOfList = nameOfList;
        this.learner = learner;
    }

    // Getter & Setter
    public int getFCID() {
        return FCID;
    }

    public void setFCID(int FCID) {
        this.FCID = FCID;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public String getNameOfList() {
        return nameOfList;
    }

    public void setNameOfList(String nameOfList) {
        this.nameOfList = nameOfList;
    }

    @Override
    public String toString() {
        return "FavoriteFlashCard{" +
                "FCID=" + FCID +
                ", dictionary=" + dictionary +
                ", learner=" + learner +
                ", nameOfList='" + nameOfList + '\'' +
                '}';
    }
}
