package model;

import java.io.Serializable;
import java.util.List;

public class ExamQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    private int eQuestID;  // Primary key
    private Exam exam;
    private int orderIndex;
    private String description;
    private String eTime;  // Thời gian làm câu hỏi
    private String eQuesType;
    // Fields for question data
    private Question question;

    public ExamQuestion() {
    }

    public ExamQuestion(int eQuestID, Exam exam, int orderIndex, String description, String eTime, String eQuesType, Question question) {
        this.eQuestID = eQuestID;
        this.exam = exam;
        this.orderIndex = orderIndex;
        this.description = description;
        this.eTime = eTime;
        this.eQuesType = eQuesType;
        this.question = question;
    }

// Getters and setters

    public int geteQuestID() {
        return eQuestID;
    }

    public void seteQuestID(int eQuestID) {
        this.eQuestID = eQuestID;
    }

    public String geteQuesType() {
        return eQuesType;
    }

    public void seteQuesType(String eQuesType) {
        this.eQuesType = eQuesType;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setExamQuestionID(int eQuestID) {
        this.eQuestID = eQuestID;
    }


    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Override
    public String toString() {
        return "ExamQuestion{" +
                "eQuestID=" + eQuestID +
                ", exam=" + exam +
                ", orderIndex=" + orderIndex +
                ", description='" + description + '\'' +
                ", eTime='" + eTime + '\'' +
                ", eQuesType='" + eQuesType + '\'' +
                ", question=" + question +
                '}';
    }
}
