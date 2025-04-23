package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Folder implements Serializable {
    private static final long serialVersionUID = 1L;
    private int folderID;
    private String name;
    private int parentID;
    private int ownerID;
    private Timestamp createdAt;
    private String privacyLevel;

    public Folder() {
    }

    public Folder(int folderID, String name, int parentID, int ownerID, Timestamp createdAt, String privacyLevel) {
        this.folderID = folderID;
        this.name = name;
        this.parentID = parentID;
        this.ownerID = ownerID;
        this.createdAt = createdAt;
        this.privacyLevel = privacyLevel;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(String privacyLevel) {
        this.privacyLevel = privacyLevel;
    }
}