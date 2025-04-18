package java.model;

import java.util.Date;

public class User {
    private int userID;
    private String username;
    private String password;
    private String gmail;
    private String phone;
    private String role;
    private String status;
    private String fullName;
    private String socialID;
    private Date dateCreate;
    private String gender;
    private Date dateOfBirth;
    private String avatar;

    public User(int userID, String username, String password, String gmail, String phone, String role, String status, String fullName, String socialID, Date dateCreate, String gender, Date dateOfBirth, String avatar) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.gmail = gmail;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.fullName = fullName;
        this.socialID = socialID;
        this.dateCreate = dateCreate;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
    }

    public User() {

    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getSocialID() {
        return socialID;
    }

    public void setSocialID(String socialID) {
        this.socialID = socialID;
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

    public String displayInfo() {
        return "Information: \n" +
                "Gmail: " + gmail + "\n" +
                "Phone: " + phone + "\n" +
                "Role: " + role + "\n" +
                "Status: " + status + "\n" +
                "Full Name: " + fullName + "\n" +
                "Social ID: " + socialID + "\n" +
                "Date Created: " + dateCreate + "\n" +
                "Gender: " + gender + "\n" + "Date Of Birth: " + dateOfBirth + "\n";
    }
}

