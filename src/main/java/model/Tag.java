package model;

import java.io.Serializable;

public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    private int tagID;
    private String name;

    // Constructors
    public Tag() {
    }

    public Tag(int tagID, String name) {
        this.tagID = tagID;
        this.name = name;
    }

    // Getters and Setters
    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}