package model;

import java.io.Serializable;
import java.util.Date;

public class Expert extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int expertID;
    private int honour_ownedID;
    private String certificate;
    private String CCCD;

    public Expert() {
    }

    public Expert(int expertID, int honour_ownedID, String certificate, String CCCD) {
        this.expertID = expertID;
        this.honour_ownedID = honour_ownedID;
        this.certificate = certificate;
        this.CCCD = CCCD;
    }

    public Expert(int userID, String username, String password, String gmail, String phone, String role, String status, String fullName, String socialID, Date dateCreate, String gender, Date dateOfBirth, String avatar, int expertID, int honour_ownedID, String certificate, String CCCD) {
        super(userID, username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, dateOfBirth, avatar);
        this.expertID = expertID;
        this.honour_ownedID = honour_ownedID;
        this.certificate = certificate;
        this.CCCD = CCCD;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public int getHonour_ownedID() {
        return honour_ownedID;
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

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    @Override
    public String toString() {
        return "Expert{" +
                "expertID=" + expertID +
                ", honour_ownedID=" + honour_ownedID +
                ", certificate='" + certificate + '\'' +
                ", CCCD='" + CCCD + '\'' +
                "} " + super.toString();
    }

    // Method to display required fields
    @Override
    public String displayInfo() {
        return super.displayInfo() + 
               "Expert ID: " + expertID + "\n" +
               "Honour ID: " + honour_ownedID + "\n" +
               "Certificate: " + certificate + "\n" +
               "CCCD: " + CCCD;
    }

    public static void main(String[] args) {

    }
}
