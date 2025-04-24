package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderID;
    private String paymentID;
    private int expertID;
    private int courseID;
    private int learnerID;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    
    // Constructors
    public Order() {
    }
    
    public Order(String orderID, String paymentID, int expertID, int courseID, int learnerID, 
                LocalDateTime orderDate, BigDecimal totalAmount, String status) {
        this.orderID = orderID;
        this.paymentID = paymentID;
        this.expertID = expertID;
        this.courseID = courseID;
        this.learnerID = learnerID;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }
    
    // Getters and Setters
    public String getOrderID() {
        return orderID;
    }
    
    public void setOrderID(String orderID) {
        this.orderID = orderID;
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
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
} 