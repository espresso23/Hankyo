package model;

import java.util.Date;

public class HonourOwned {
    private int honour_ownedID;
    Honour honour;
    Expert expert;
    Learner learner;
    private Date dateAdded;
    private boolean isEquipped;

    public HonourOwned() {
    }

    public HonourOwned(int honour_ownedID, Honour honour, Learner learner, Date dateAdded, boolean isEquipped) {
        this.honour_ownedID = honour_ownedID;
        this.honour = honour;
        this.learner = learner;
        this.dateAdded = dateAdded;
        this.isEquipped = isEquipped;
    }

    public HonourOwned(Honour honour, Learner learner, Date dateAdded, boolean isEquipped) {
        this.honour = honour;
        this.learner = learner;
        this.dateAdded = dateAdded;
        this.isEquipped = isEquipped;
    }

    public int getHonour_ownedID() {
        return honour_ownedID;
    }

    public void setHonour_ownedID(int honour_ownedID) {
        this.honour_ownedID = honour_ownedID;
    }

    public Honour getHonour() {
        return honour;
    }

    public void setHonour(Honour honour) {
        this.honour = honour;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    @Override
    public String toString() {
        return "HonourOwned{" +
                "honour_ownedID=" + honour_ownedID +
                ", honour=" + honour +
                ", expert=" + expert +
                ", learner=" + learner +
                ", dateAdded=" + dateAdded +
                ", isEquipped=" + isEquipped +
                '}';
    }
}