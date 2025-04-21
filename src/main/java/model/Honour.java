package model;

public class Honour {
    private int honourID;
    private String honourName;
    private String honourImg;
    private String honourType;
    private String titleColor;
    private String gradientStart;
    private String gradientEnd;

    // Constructor
    public Honour() {
    }

    public Honour(int honourID, String honourName, String honourImg, String honourType) {
        this.honourID = honourID;
        this.honourName = honourName;
        this.honourImg = honourImg;
        this.honourType = honourType;
    }

    // Getters and Setters
    public int getHonourID() {
        return honourID;
    }

    public void setHonourID(int honourID) {
        this.honourID = honourID;
    }

    public String getHonourName() {
        return honourName;
    }

    public void setHonourName(String honourName) {
        this.honourName = honourName;
    }

    public String getHonourImg() {
        return honourImg;
    }

    public void setHonourImg(String honourImg) {
        this.honourImg = honourImg;
    }

    public String getHonourType() {
        return honourType;
    }

    public void setHonourType(String honourType) {
        this.honourType = honourType;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getGradientStart() {
        return gradientStart;
    }

    public void setGradientStart(String gradientStart) {
        this.gradientStart = gradientStart;
    }

    public String getGradientEnd() {
        return gradientEnd;
    }

    public void setGradientEnd(String gradientEnd) {
        this.gradientEnd = gradientEnd;
    }
}