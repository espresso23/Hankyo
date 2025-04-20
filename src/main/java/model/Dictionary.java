package model;

public class Dictionary {
    private int wordID;
    private String word;
    private String definition;
    private String type;
    private String mean;

    public Dictionary(int wordID, String word, String definition, String type, String mean) {
        this.wordID = wordID;
        this.word = word;
        this.definition = definition;
        this.type = type;
        this.mean = mean;
    }

    public Dictionary(String word, String definition, String type, String mean) {
        this.word = word;
        this.definition = definition;
        this.type = type;
        this.mean = mean;
    }

    public Dictionary() {
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                ", type='" + type + '\'' +
                ", mean='" + mean + '\'' +
                '}';
    }
}
