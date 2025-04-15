package model;

public class Answer {
    private int answerID;
    private int questionID;
    private String answerText;
    private boolean isCorrect;
    private char optionLabel;

    public Answer() {
    }

    public Answer(int answerID, int questionID, String answerText, boolean isCorrect, char optionLabel) {
        this.answerID = answerID;
        this.questionID = questionID;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.optionLabel = optionLabel;
    }

    public Answer(String answerText, boolean isCorrect, char optionLabel) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.optionLabel = optionLabel;
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
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
                ", questionID=" + questionID +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                ", optionLabel=" + optionLabel +
                '}';
    }
}
