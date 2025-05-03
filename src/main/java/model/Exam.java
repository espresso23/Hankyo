package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;
    private int examID;
    private String examName;
    private String examDescription;
    private LocalDateTime dateCreated;
    private String examType;
    private String status;

    public Exam() {
    }

    public Exam(String examName, String examType, String examDescription, String status) {
        this.examName = examName;
        this.examType = examType;
        this.examDescription = examDescription;
        this.status = status;
    }

    public Exam(int examID) {
        this.examID = examID;
    }

    public Exam(int examID, int expertID, String examName, String examDescription, LocalDateTime dateCreated, String examType, int numCompleted) {
        this.examID = examID;
        this.examName = examName;
        this.examDescription = examDescription;
        this.dateCreated = dateCreated;
        this.examType = examType;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }


    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examID=" + examID +
                ", examName='" + examName + '\'' +
                ", examDescription='" + examDescription + '\'' +
                ", dateCreated=" + dateCreated +
                ", examType='" + examType + '\'' +
                '}';
    }
}
