package model;

import java.util.Date;

public class Expert extends User {
    private int expertID;
    private int honour_ownedID;
    private String certificate;
    private Feedback feedback;
    private Honour honour;

    public Expert(String certificate, int expertID, int honour_ownedID) {
        this.certificate = certificate;
        this.expertID = expertID;
        this.honour_ownedID = honour_ownedID;
    }

    public Expert(int userID, String username, String password, String gmail, String phone, String role, String status, String fullName, String socialID, Date dateCreate, String gender, Date dateOfBirth, String avatar, Integer equippedHonourID, int expertID, int honour_ownedID, String certificate, Feedback feedback, Honour honour) {
        super(userID, username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, dateOfBirth, avatar, equippedHonourID);
        this.expertID = expertID;
        this.honour_ownedID = honour_ownedID;
        this.certificate = certificate;
        this.feedback = feedback;
        this.honour = honour;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Expert() {
    }

    // Getters and Setters
    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public int getHonour_ownedID() {
        return honour_ownedID;
    }

    public Honour getHonour() {
        return honour;
    }

    public void setHonour(Honour honour) {
        this.honour = honour;
    }

    public void setHonour_ownedID(int honour_ownedID) {
        this.honour_ownedID = honour_ownedID;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    // Method to display required fields
    @Override
    public String displayInfo() {
        return super.displayInfo() + "Expert ID: " + expertID + "\n" +
                "Honour ID: " + honour_ownedID + "\n" +
                "Certificate: " + certificate;
    }

    public static void main(String[] args) {

    }
}
