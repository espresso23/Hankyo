package model;

import java.util.Date;

public class Exam {
    private int examID;
    private int expertID;
    private String examName;
    private String examDescription;
    private Date dateCreated;
    private String examType;
    private int numCompleted;

    public Exam(String examName, String examDescription, int expertID) {
        this.examName = examName;
        this.examDescription = examDescription;
        this.expertID = expertID;
    }

    public Exam(String examName, String examDescription, int expertID, String examType) {
        this.examName = examName;
        this.examDescription = examDescription;
        this.expertID = expertID;
        this.examType = examType;
    }

    public Exam(int examID, String examName, String examDescription, String examType) {
        this.examID = examID;
        this.examName = examName;
        this.examDescription = examDescription;
        this.examType = examType;
    }

    public Exam() {
    }

    public String getExamDescription() {
        return examDescription;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
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

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public int getNumCompleted() {
        return numCompleted;
    }

    public void setNumCompleted(int numCompleted) {
        this.numCompleted = numCompleted;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examID=" + examID +
                ", expertID=" + expertID +
                ", examName='" + examName + '\'' +
                ", examDescription='" + examDescription + '\'' +
                ", dateCreated=" + dateCreated +
                ", examType='" + examType + '\'' +
                ", numCompleted=" + numCompleted +
                '}';
    }

    public Exam(int examID, String examName, String examDescription) {
        this.examID = examID;
        this.examName = examName;
        this.examDescription = examDescription;
    }
}
