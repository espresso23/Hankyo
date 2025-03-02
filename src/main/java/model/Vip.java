package model;

import java.util.Calendar;
import java.util.Date;

public class Vip {
    private int vipID;
    private String vipType;
    private String status;
    private Date dateCreated;
    private Date endDate;

    public Vip() {
    }

    public Vip(Date dateCreated, Date endDate, String status, int vipID, String vipType) {
        this.dateCreated = dateCreated;
        this.endDate = endDate;
        this.status = status;
        this.vipID = vipID;
        this.vipType = vipType;
    }

    public Vip(Date dateCreated, int month, String status, int vipID, String vipType) {
        this.dateCreated = dateCreated;
        this.endDate = calculateEndDate(month);
        this.status = status;
        this.vipID = vipID;
        this.vipType = vipType;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(int month) {
        this.endDate = calculateEndDate(month);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVipID() {
        return vipID;
    }

    public void setVipID(int vipID) {
        this.vipID = vipID;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public Date calculateEndDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.dateCreated);
        calendar.add(Calendar.MONTH, month); // Thêm 1 tháng
        return calendar.getTime();
    }

    public String displayInfo() {
        return "VIP ID: " + vipID + ", Type: " + vipType + ", Status: " + status + ", Created: " + dateCreated + ", Expires: " + endDate;
    }
}
