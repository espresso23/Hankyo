package model;

public class AssignmentResult {
    private int resultID;
    private int assignmentQuesID;  // FK to Assignment_Question
    private int learnerID;         // FK to Learner
    private float mark;            // Điểm của câu trả lời
    private String answerLabel;    // Nhãn câu trả lời (A, B, C, D)
    private boolean answerIsCorrect; // Trạng thái đúng/sai
    private int assignTakenID;     // FK to Assignment_Taken
    
    // Các trường bổ sung cho thống kê
    private int correctCount;
    private int totalQuestions;
    private float totalMark;
    private float maxMark;
    private float score;

    // Constructor
    public AssignmentResult() {
    }

    // Getters and Setters
    public int getResultID() {
        return resultID;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    public int getAssignmentQuesID() {
        return assignmentQuesID;
    }

    public void setAssignmentQuesID(int assignmentQuesID) {
        this.assignmentQuesID = assignmentQuesID;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public void setAnswerLabel(String answerLabel) {
        this.answerLabel = answerLabel;
    }

    public boolean isAnswerIsCorrect() {
        return answerIsCorrect;
    }

    public void setAnswerIsCorrect(boolean answerIsCorrect) {
        this.answerIsCorrect = answerIsCorrect;
    }

    public int getAssignTakenID() {
        return assignTakenID;
    }

    public void setAssignTakenID(int assignTakenID) {
        this.assignTakenID = assignTakenID;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public float getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(float totalMark) {
        this.totalMark = totalMark;
    }

    public float getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(float maxMark) {
        this.maxMark = maxMark;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "AssignmentResult{" +
                "resultID=" + resultID +
                ", assignmentQuesID=" + assignmentQuesID +
                ", learnerID=" + learnerID +
                ", mark=" + mark +
                ", answerLabel='" + answerLabel + '\'' +
                ", answerIsCorrect=" + answerIsCorrect +
                ", assignTakenID=" + assignTakenID +
                ", correctCount=" + correctCount +
                ", totalQuestions=" + totalQuestions +
                ", totalMark=" + totalMark +
                ", maxMark=" + maxMark +
                ", score=" + score +
                '}';
    }
}
