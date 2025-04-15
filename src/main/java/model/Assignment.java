package model;

import java.util.List;

public class Assignment {
    private int assignmentID;
    private String assignmentTitle;
    private Question question;
    private List<Answer> answer;
    private String media;
    private String description;

    //constructor

    public Assignment(int assignmentID, String assignmentTitle, Question question, List<Answer> answer, String media) {
        this.assignmentID = assignmentID;
        this.assignmentTitle = assignmentTitle;
        this.question = question;
        this.answer = answer;
        this.media = media;
    }

    public Assignment() {
    }
// Getters and setters


    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
