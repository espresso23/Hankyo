package model;

import java.sql.Timestamp;

public class Chat {
    private int chatID;
    private int userID; // ID của người gửi
    private String message;   // Nội dung tin nhắn
    private Timestamp sendAt; // Thời gian gửi
    private String fullName; // Tên nhân vật gửi tin nhắn
    private String pictureSend; // URL or Base64 of the sent picture

    // Constructor mặc định
    public Chat() {
    }

    // Constructor cho tin nhắn công khai
    public Chat(int chatID, int userID, String message, Timestamp sendAt, String fullName, String pictureSend) {
        this.chatID = chatID;
        this.userID = userID;
        this.message = message;
        this.sendAt = sendAt;
        this.fullName = fullName;
        this.pictureSend = pictureSend;
    }

    // Constructor cho tin nhắn mới
    public Chat(int userID, String message, String fullName, String pictureSend) {
        this.userID = userID;
        this.message = message;
        this.fullName = fullName;
        this.pictureSend = pictureSend;
    }

    // Getter và Setter
    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getSendAt() {
        return sendAt;
    }

    public void setSendAt(Timestamp sendAt) {
        this.sendAt = sendAt;
    }

    public String getPictureSend() {
        return pictureSend;
    }

    public void setPictureSend(String pictureSend) {
        this.pictureSend = pictureSend;
    }
}
