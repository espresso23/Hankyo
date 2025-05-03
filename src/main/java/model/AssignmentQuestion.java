package model;

import java.io.Serializable;
import java.util.List;

public class AssignmentQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    private int assignQuesID;
    private int questionID;
    private int assignmentID;
    private String questionText;    // từ bảng Question
    private String questionImg;     // từ bảng Question
    private String audioFile;       // từ bảng Question
    private String questionType;    // từ bảng Question
    private double questionMark;    // từ bảng Question
    private List<Answer> answers;   // danh sách câu trả lời

    public AssignmentQuestion() {
    }

    public int getAssignQuesID() {
        return assignQuesID;
    }

    public void setAssignQuesID(int assignQuesID) {
        this.assignQuesID = assignQuesID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public double getQuestionMark() {
        return questionMark;
    }

    public void setQuestionMark(double questionMark) {
        this.questionMark = questionMark;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "AssignmentQuestion{" +
                "assignQuesID=" + assignQuesID +
                ", questionID=" + questionID +
                ", assignmentID=" + assignmentID +
                ", questionText='" + questionText + '\'' +
                ", questionImg='" + questionImg + '\'' +
                ", audioFile='" + audioFile + '\'' +
                ", questionType='" + questionType + '\'' +
                ", questionMark=" + questionMark +
                '}';
    }
}
