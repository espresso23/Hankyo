package model;

import java.util.Date;

public class Learner extends User {
    private int learnerID;
    private double hankyoPoint;
    private Honour honour;
    private Reward reward;
    private Vip vip;

    public Learner() {

    }

    public Learner(double hankyoPoint, Honour honour, int learnerID, Reward reward, Vip vip) {
        this.hankyoPoint = hankyoPoint;
        this.honour = honour;
        this.learnerID = learnerID;
        this.reward = reward;
        this.vip = vip;
    }

    public Learner(int userID, String username, String password, String gmail, String phone, String role, String status, String fullName, String socialID, Date dateCreate, String gender, int age, String avatar, double hankyoPoint, Honour honour, int learnerID, Reward reward, Vip vip) {
        super(userID, username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, age, avatar);
        this.hankyoPoint = hankyoPoint;
        this.honour = honour;
        this.learnerID = learnerID;
        this.reward = reward;
        this.vip = vip;
    }

    public double getHankyoPoint() {
        return hankyoPoint;
    }

    public void setHankyoPoint(double hankyoPoint) {
        this.hankyoPoint = hankyoPoint;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Vip getVip() {
        return vip;
    }

    public void setVip(Vip vip) {
        this.vip = vip;
    }

    public Honour getHonour() {
        return honour;
    }

    public void setHonour(Honour honour) {
        this.honour = honour;
    }

    public int getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(int learnerID) {
        this.learnerID = learnerID;
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + "Learner ID: " + getLearnerID() + "\n" + "Full Name: " + getFullName() + "\n" + "Hankyo Point: " + getHankyoPoint() + "\n" + reward.displayInfo() + "\n" + vip.displayInfo() + "\n";
    }

    public static void main(String[] args) {
        Learner learner = new Learner();
        learner.setGmail("tan@gmail.com");
        learner.setFullName("Tan Pham");
        learner.setHankyoPoint(100.30);
        learner.setLearnerID(12345);
        learner.setGender("Male");
        learner.setPhone("0987654321");
        learner.setRole("Learner");
        learner.setAge(20);
        learner.setStatus("Active");
        learner.setSocialID("1234567890");
        learner.setDateCreate(new Date());
        Honour honour = new Honour(1, "IMG1", "SLIVER", "normal");
        learner.setHonour(honour);
        Vip vip = new Vip(new Date(), 3, "active", 123, "VIP");
        learner.setVip(vip);
        Reward reward = new Reward(3, "Hard-Working", "IMG", new Date());
        learner.setReward(reward);
        System.out.println(learner.displayInfo());

    }
}
