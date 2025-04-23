package model;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private int questionID;
    private String questionText;
    private String questionImage;
    private String audioFile;
    private String questionType;
    private double questionMark;
    private int assignmentID;
    private List<Answer> answers = new ArrayList<>();//luu bo cau hoi cho 1 question

    public Question(int questionID, String questionText, String questionImage, String questionType, String audioFile) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.questionImage = questionImage;
        this.questionType = questionType;
        this.audioFile = audioFile;
    }

    public Question() {
    }

    public double getQuestionMark() {
        return questionMark;
    }

    public void setQuestionMark(double questionMark) {
        this.questionMark = questionMark;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    @Override
    public String toString() {
        return "Question{" +
                "audioFile='" + audioFile + '\'' +
                ", questionID=" + questionID +
                ", questionText='" + questionText + '\'' +
                ", questionImage='" + questionImage + '\'' +
                ", questionType='" + questionType + '\'' +
                '}';
    }
}
