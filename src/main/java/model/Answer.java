package model;

public class Answer {
    private int answerID;
    private String answerText;
    private boolean isCorrect;
    private char optionLabel;

    public Answer(int answerID, String answerText, boolean isCorrect, char optionLabel) {
        this.answerID = answerID;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.optionLabel = optionLabel;
    }

    public Answer() {
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public char getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(char optionLabel) {
        this.optionLabel = optionLabel;
    }


    @Override
    public String toString() {
        return "Answer{" +
                "answerID=" + answerID +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                ", optionLabel=" + optionLabel +
                '}';
    }
}
