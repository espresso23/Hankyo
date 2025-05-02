package model;

import java.sql.Date;

public class ExpertRegister {
    private int registerID;
    private String username;
    private String password;
    private String gmail;
    private String phone;
    private String role;
    private String status;
    private String fullName;
    private Date dateCreate;
    private String gender;
    private String avatar;
    private String certificate;
    private String cccd;
    private String cccdFront;
    private String cccdBack;
    private String approveStatus;

    // Constructors
    public ExpertRegister() {
    }

    public ExpertRegister(int registerID, String username, String password, String gmail, String phone, String role, String status, String fullName, Date dateCreate, String gender, String avatar, String certificate) {
        this.registerID = registerID;
        this.username = username;
        this.password = password;
        this.gmail = gmail;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.fullName = fullName;
        this.dateCreate = dateCreate;
        this.gender = gender;
        this.avatar = avatar;
        this.certificate = certificate;
    }

    public int getRegisterID() {
        return registerID;
    }

    public void setRegisterID(int registerID) {
        this.registerID = registerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getCccdFront() {
        return cccdFront;
    }

    public void setCccdFront(String cccdFront) {
        this.cccdFront = cccdFront;
    }

    public String getCccdBack() {
        return cccdBack;
    }

    public void setCccdBack(String cccdBack) {
        this.cccdBack = cccdBack;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    @Override
    public String toString() {
        return "ExpertRegister{" +
                "registerID=" + registerID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gmail='" + gmail + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dateCreate=" + dateCreate +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", certificate='" + certificate + '\'' +
                ", cccd='" + cccd + '\'' +
                ", cccdFront='" + cccdFront + '\'' +
                ", cccdBack='" + cccdBack + '\'' +
                ", approveStatus='" + approveStatus + '\'' +
                '}';
    }
}