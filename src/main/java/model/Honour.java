package model;

public class Honour {
    private int honourID;
    private String honourImg;
    private String honourName;
    private String honourType;
    private int honour_ownedID;

    public Honour(int honourID, String honourImg, String honourName, String honourType) {
        this.honourID = honourID;
        this.honourImg = honourImg;
        this.honourName = honourName;
        this.honourType = honourType;
    }

    public Honour() {

    }
    // Getters and Setters
    public int getHonourID() {
        return honourID;
    }

    public void setHonourID(int honourID) {
        this.honourID = honourID;
    }

    public String getHonourImg() {
        return honourImg;
    }

    public void setHonourImg(String honourImg) {
        this.honourImg = honourImg;
    }

    public String getHonourName() {
        return honourName;
    }

    public void setHonourName(String honourName) {
        this.honourName = honourName;
    }

    public String getHonourType() {
        return honourType;
    }

    public void setHonourType(String honourType) {
        this.honourType = honourType;
    }
}
