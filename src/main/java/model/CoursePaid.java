package model;

import java.time.LocalDateTime;

public class CoursePaid {
    private int course_paidID;
    private int courseID;//get courseID
    private int learnerID;//get learnerID
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
}
