package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Files implements Serializable {
    private static final long serialVersionUID = 1L;
    private int fileID;
    private String name;
    private int folderID;
    private String fileType;
    private int ownerID;
    private String filePath;
    private Timestamp uploadedAt;
    private String privacyLevel;

    public Files() {
    }

    public Files(int fileID, String name, int folderID, String fileType, int ownerID, String filePath, Timestamp uploadedAt, String privacyLevel) {
        this.fileID = fileID;
        this.name = name;
        this.folderID = folderID;
        this.fileType = fileType;
        this.ownerID = ownerID;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
        this.privacyLevel = privacyLevel;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(String privacyLevel) {
        this.privacyLevel = privacyLevel;
    }
}