package model;

import java.io.Serializable;
import java.util.Date;

public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;
    private int examID;
    private int expertID;
    private String examName;
    private String examDescription;
    private Date dateCreated;
    private String examType;
    private int numCompleted;

    public Exam() {
    }

    public Exam(int examID) {
        this.examID = examID;
    }

    public Exam(int examID, int expertID, String examName, String examDescription, Date dateCreated, String examType, int numCompleted) {
        this.examID = examID;
        this.expertID = expertID;
        this.examName = examName;
        this.examDescription = examDescription;
        this.dateCreated = dateCreated;
        this.examType = examType;
        this.numCompleted = numCompleted;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
}
