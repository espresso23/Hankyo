package model;

import java.io.Serializable;
import java.util.Date;

public class VipUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private int vipUserID;
    private int learnerID;
    private int vipID;
    private Date startDate;
    private Date endDate;
    private String status; // ACTIVE, EXPIRED, CANCELLED
    private String paymentID;

    public VipUser() {
    }

    public VipUser(int vipUserID, int learnerID, int vipID, Date startDate, Date endDate, String status, String paymentID) {
        this.vipUserID = vipUserID;
        this.learnerID = learnerID;
        this.vipID = vipID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.paymentID = paymentID;
    }

    public int getVipUserID() {
        return vipUserID;
    }

    public void setVipUserID(int vipUserID) {
        this.vipUserID = vipUserID;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    public int getVipID() {
        return vipID;
    }

    public void setVipID(int vipID) {
        this.vipID = vipID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    @Override
    public String toString() {
        return "VipUser{" +
                "vipUserID=" + vipUserID +
                ", learnerID=" + learnerID +
                ", vipID=" + vipID +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", paymentID='" + paymentID + '\'' +
                '}';
    }
} 