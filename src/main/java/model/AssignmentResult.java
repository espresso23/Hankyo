package model;

public class AssignmentResult {
    private int resultID;
    private int assignmentQuesID;
    private int learnerID;
    private boolean mark;
    private String answerLabel;
    private boolean answerIsCorrect;
    private int assignTakenID;

    public AssignmentResult() {

    }

    public boolean isAnswerIsCorrect() {
        return answerIsCorrect;
    }

    public void setAnswerIsCorrect(boolean answerIsCorrect) {
        this.answerIsCorrect = answerIsCorrect;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public void setAnswerLabel(String answerLabel) {
        this.answerLabel = answerLabel;
    }

    public int getAssignmentQuesID() {
        return assignmentQuesID;
    }

    public void setAssignmentQuesID(int assignmentQuesID) {
        this.assignmentQuesID = assignmentQuesID;
    }

    public int getAssignTakenID() {
        return assignTakenID;
    }

    public void setAssignTakenID(int assignTakenID) {
        this.assignTakenID = assignTakenID;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public int getResultID() {
        return resultID;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    @Override
    public String toString() {
        return "AssignmentResult{" +
                "answerIsCorrect=" + answerIsCorrect +
                ", resultID=" + resultID +
                ", assignmentQuesID=" + assignmentQuesID +
                ", learnerID=" + learnerID +
                ", mark=" + mark +
                ", answerLabel='" + answerLabel + '\'' +
                ", assignmentTakenID=" + assignTakenID +
                '}';
    }
}
