package model;

import java.util.List;

public class ExamQuestion {
    private int examQuestionID;
    private Question question;
    private List<Answer> answer;
    private int orderIndex;
    private double mark;
    private String description;

    public ExamQuestion() {
    }

    public ExamQuestion(int examQuestionID, String description, Question question, List<Answer> answer, double mark, int orderIndex) {
        this.examQuestionID = examQuestionID;
        this.description = description;
        this.question = question;
        this.answer = answer;
        this.mark = mark;
        this.orderIndex = orderIndex;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExamQuestionID() {
        return examQuestionID;
    }

    public void setExamQuestionID(int examQuestionID) {
        this.examQuestionID = examQuestionID;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
