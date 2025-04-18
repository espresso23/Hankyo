package java.model;

public class Vocabulary {
    private String vocabID;
    private String wordID;
    private String custom_definition;
    private Boolean isFavorite;
    private String learnerID;

    public Vocabulary() {
    }

    public Vocabulary(String vocabID, String wordID, String custom_definition, Boolean isFavorite, String learnerID) {
        this.vocabID = vocabID;
        this.wordID = wordID;
        this.custom_definition = custom_definition;
        this.isFavorite = isFavorite;
        this.learnerID = learnerID;
    }

    public String getVocabID() {
        return vocabID;
    }

    public void setVocabID(String vocabID) {
        this.vocabID = vocabID;
    }

    public String getWordID() {
        return wordID;
    }

    public void setWordID(String wordID) {
        this.wordID = wordID;
    }

    public String getCustom_definition() {
        return custom_definition;
    }

    public void setCustom_definition(String custom_definition) {
        this.custom_definition = custom_definition;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(String learnerID) {
        this.learnerID = learnerID;
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "vocabID='" + vocabID + '\'' +
                ", wordID='" + wordID + '\'' +
                ", custom_definition='" + custom_definition + '\'' +
                ", isFavorite=" + isFavorite +
                ", learnerID='" + learnerID + '\'' +
                '}';
    }
}
