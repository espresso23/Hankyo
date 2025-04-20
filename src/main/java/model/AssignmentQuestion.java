package model;

public class AssignmentQuestion {
    private int assignmentQuesID;
    private int assignmentID;
    private int questionID;
    private String description;

    public AssignmentQuestion() {
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public int getAssignmentQuesID() {
        return assignmentQuesID;
    }

    public void setAssignmentQuesID(int assignmentQuesID) {
        this.assignmentQuesID = assignmentQuesID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    @Override
    public String toString() {
        return "AssignmentQuestion{" +
                "assignmentID=" + assignmentID +
                ", assignmentQuesID=" + assignmentQuesID +
                ", questionID=" + questionID +
                ", description='" + description + '\'' +
                '}';
    }
}
