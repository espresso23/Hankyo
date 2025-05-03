package model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

public class Vip implements Serializable {
    private static final long serialVersionUID = 1L;
    private int vipID;
    private String vipName;
    private String description;
    private double price;
    private double yearlyPrice;
    private Timestamp createAt;
    private String vipType; // FREE, POPULAR, PREMIUM
    private String status; // ACTIVE, INACTIVE
    private String vip_img;
    private String features; // JSON string to store features list
    private int duration; // Duration in days
    private boolean isYearly; // true for yearly plan, false for monthly

    public Vip() {
    }

    public Vip(int vipID, String vipName, String description, double price, double yearlyPrice, 
               Timestamp createAt, String vipType, String status, String vip_img, 
               String features, int duration, boolean isYearly) {
        this.vipID = vipID;
        this.vipName = vipName;
        this.description = description;
        this.price = price;
        this.yearlyPrice = yearlyPrice;
        this.createAt = createAt;
        this.vipType = vipType;
        this.status = status;
        this.vip_img = vip_img;
        this.features = features;
        this.duration = duration;
        this.isYearly = isYearly;
    }

    // Getters and Setters
    public int getVipID() {
        return vipID;
    }

    public void setVipID(int vipID) {
        this.vipID = vipID;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(double yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVip_img() {
        return vip_img;
    }

    public void setVip_img(String vip_img) {
        this.vip_img = vip_img;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isYearly() {
        return isYearly;
    }

    public void setYearly(boolean yearly) {
        isYearly = yearly;
    }

    public Date calculateEndDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(this.createAt.getTime()));
        calendar.add(Calendar.MONTH, month); // Thêm số tháng được chỉ định
        return calendar.getTime();
    }

    public String displayInfo() {
        return "VIP ID: " + vipID + ", Type: " + vipType + ", Status: " + status + ", Created: " + createAt + ", Expires: " + calculateEndDate(duration);
    }
}
