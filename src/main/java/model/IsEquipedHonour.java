package model;

public class IsEquipedHonour {
    private Learner learner;
    private Honour honour;
    private HonourOwned owned;

    public IsEquipedHonour() {
    }

    public IsEquipedHonour(Learner learner, Honour honour, HonourOwned owned) {
        this.learner = learner;
        this.honour = honour;
        this.owned = owned;
    }
}
