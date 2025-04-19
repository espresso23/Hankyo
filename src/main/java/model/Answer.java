package model;

public class Answer {
    private int answerID;
    private String answerText;
    private boolean isCorrect;
    private String optionLabel;
    private int questionID;


    public Answer(int answerID, String answerText, boolean isCorrect, String optionLabel) {
        this.answerID = answerID;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.optionLabel = optionLabel;
    }

    public Answer() {
    }

    public Answer(String answerText, boolean isCorrect, String optionLabel) {
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

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
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
