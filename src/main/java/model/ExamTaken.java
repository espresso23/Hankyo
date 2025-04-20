package model;

import java.sql.Time;
import java.util.Date;

public class ExamTaken {
        private int examTakenID;
        private int examID;
        private int learnerID;
        private Time timeTaken;
        private Time timeInput;
        private Date dateCreated;
        private float finalMark;
        private int skipQues;
        private int doneQues;

    public ExamTaken() {
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getExamTakenID() {
        return examTakenID;
    }

    public void setExamTakenID(int examTakenID) {
        this.examTakenID = examTakenID;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public Time getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Time timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Time getTimeInput() {
        return timeInput;
    }

    public void setTimeInput(Time timeInput) {
        this.timeInput = timeInput;
    }

    public float getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(float finalMark) {
        this.finalMark = finalMark;
    }

    public int getSkipQues() {
        return skipQues;
    }

    public void setSkipQues(int skipQues) {
        this.skipQues = skipQues;
    }

    public int getDoneQues() {
        return doneQues;
    }

    public void setDoneQues(int doneQues) {
        this.doneQues = doneQues;
    }

    @Override
    public String toString() {
        return "ExamTaken{" +
                "examTakenID=" + examTakenID +
                ", examID=" + examID +
                ", learnerID=" + learnerID +
                ", timeTaken=" + timeTaken +
                ", timeInput=" + timeInput +
                ", dateCreated=" + dateCreated +
                ", finalMark=" + finalMark +
                ", skipQues=" + skipQues +
                ", doneQues=" + doneQues +
                '}';
    }
}
