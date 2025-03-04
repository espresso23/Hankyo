package model;

import java.util.Date;

public class Expert extends User {
    private int expertID;
    private Honour honour;//from class Honour
    private String certificate;

    public Expert(int userID, String username, String password, String gmail, String phone, String role, String status, String fullName, String socialID, Date dateCreate, String gender, int age, String avatar, int expertID, Honour honour, String certificate) {
        super(userID, username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, age, avatar);
        this.expertID = expertID;
        this.honour = honour;
        this.certificate = certificate;
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

    public Honour getHonour() {
        return honour;
    }

    public void setHonour(Honour honour) {
        this.honour = honour;
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
                "Honour ID: " + honour.getHonourID() + "\n" +
                "Honour Image: " + honour.getHonourImg() + "\n" +
                "Honour Name: " + honour.getHonourName() + "\n" +
                "Honour Type: " + honour.getHonourType() + "\n" +
                "Certificate: " + certificate;
    }

    public static void main(String[] args) {
        Expert expert = new Expert();
        expert.setFullName("Pham Tan");
        expert.setGmail("tan@gmail.com");
        expert.setPhone("0987654321");
        expert.setRole("Expert");
        expert.setStatus("Active");
        expert.setSocialID("ABC123");
        expert.setDateCreate(new Date());
        expert.setGender("Male");
        expert.setExpertID(1);
        expert.setCertificate("TOPIK 5");
        Honour honour = new Honour(1, "Honour1", "Image1", "Type1");
        expert.setHonour(honour);
        System.out.println(expert.displayInfo());
    }
}
