package model;

import java.io.Serializable;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;
    private int enrollID;
    private Learner learner;
    private Course course;
    private String status;


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getEnrollID() {
        return enrollID;
    }

    public void setEnrollID(int enrollID) {
        this.enrollID = enrollID;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
