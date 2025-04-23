package model;

import java.io.Serializable;

public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    private int permissionID;
    private int fileID;
    private int folderID;
    private int userID;
    private String permissionType;

    public Permission() {
    }

    public Permission(int permissionID, int fileID, int folderID, int userID, String permissionType) {
        this.permissionID = permissionID;
        this.fileID = fileID;
        this.folderID = folderID;
        this.userID = userID;
        this.permissionType = permissionType;
    }

    // Getters and setters remain the same, except no UserEmail getter/setter
    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }
}