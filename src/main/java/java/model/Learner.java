package java.model;

import model.Honour;
import model.Reward;
import model.User;
import model.Vip;

import java.util.Date;

public class Learner extends User {
    private int learnerID;
    private double hankyoPoint;
    private Honour honour;
    private model.Reward reward;
    private model.Vip vip;

    public Learner() {

    }
public  Learner(int learnerID){
        this.learnerID = learnerID;
}
    public Learner(double hankyoPoint, Honour honour, int learnerID, model.Reward reward, model.Vip vip) {
        this.hankyoPoint = hankyoPoint;
        this.honour = honour;
        this.learnerID = learnerID;
        this.reward = reward;
        this.vip = vip;
    }

    public Learner(int userID, String username, String password, String gmail, String phone, String role, String status, String fullName, String socialID, Date dateCreate, String gender, Date dateOfBirth, String avatar, double hankyoPoint, Honour honour, int learnerID, model.Reward reward, model.Vip vip) {
        super(userID, username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, dateOfBirth, avatar);
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

    public model.Reward getReward() {
        return reward;
    }

    public void setReward(model.Reward reward) {
        this.reward = reward;
    }

    public model.Vip getVip() {
        return vip;
    }

    public void setVip(model.Vip vip) {
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
        learner.setStatus("Active");
        learner.setSocialID("1234567890");
        learner.setDateCreate(new Date());
        Honour honour = new Honour(1, "IMG1", "SLIVER", "normal");
        learner.setHonour(honour);
        model.Vip vip = new Vip(new Date(), 3, "active", 123, "VIP");
        learner.setVip(vip);
        model.Reward reward = new Reward(3, "Hard-Working", "IMG", new Date());
        learner.setReward(reward);
        System.out.println(learner.displayInfo());

    }
}
