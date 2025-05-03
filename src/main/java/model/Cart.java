package model;

import java.io.Serializable;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;//luu danh sach course se mua
    private int cartID;
    private String status;
    private Course course;//lay ten, hinh anh preview
    private Learner learner;//lay id de luu vao ai dang so huu

    public Cart() {
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
