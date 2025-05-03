package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CoursePaid implements Serializable {
    private static final long serialVersionUID = 1L;
    private int course_paidID;
    private int courseID;//get courseID
    private int learnerID;//get learnerID
    private int expertID; //get expertID
    private Course course;
    private String paymentID;
    private LocalDateTime datePaid;


    public LocalDateTime getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDateTime datePaid) {
        this.datePaid = datePaid;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public int getCourse_paidID() {
        return course_paidID;
    }

    public void setCourse_paidID(int course_paidID) {
        this.course_paidID = course_paidID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }
}
