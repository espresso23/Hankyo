package model;

public class FavoriteFlashCard {
    private int FCID;
    private Dictionary dictionary;
    private Learner learner;

    public FavoriteFlashCard(Dictionary dictionary, Learner learner) {
        this.dictionary = dictionary;
        this.learner = learner;
    }

    // Constructor
    public FavoriteFlashCard(int FCID, Dictionary dictionary, Learner learner) {
        this.FCID = FCID;
        this.dictionary = dictionary;
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

    // Optional: toString() để dễ debug
    @Override
    public String toString() {
        return "FavoriteFlashCard{" +
                "FCID=" + FCID +
                ", dictionary=" + dictionary +
                ", learner=" + learner +
                '}';
    }
}
