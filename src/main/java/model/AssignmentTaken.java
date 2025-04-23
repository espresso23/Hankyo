package model;

import java.io.Serializable;
import java.util.Date;

public class AssignmentTaken implements Serializable {
    private static final long serialVersionUID = 1L;
    private int assignTakenID;
    private Date dateCreated;
    private int learnerID;
    private float finalMark;
    private int skipQues;
    private int doneQues;
    private int assignmentID;

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public int getAssignTakenID() {
        return assignTakenID;
    }

    public void setAssignTakenID(int assignTakenID) {
        this.assignTakenID = assignTakenID;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDoneQues() {
        return doneQues;
    }

    public void setDoneQues(int doneQues) {
        this.doneQues = doneQues;
    }

    public float getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(float finalMark) {
        this.finalMark = finalMark;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public int getSkipQues() {
        return skipQues;
    }

    public void setSkipQues(int skipQues) {
        this.skipQues = skipQues;
    }
}
