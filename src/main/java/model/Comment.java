/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;


public class Comment {

    private int commentID;
    private int userID;
    private String userFullName;
    private String userAvtURL;
    private int postID;
    private String content;
    private Date createdDate;

    public Comment() {
    }

    public Comment(int commentID, int userID, String userFullName, String userAvtURL, int postID, String content, Date createdDate) {
        this.commentID = commentID;
        this.userID = userID;
        this.userFullName = userFullName;
        this.userAvtURL = userAvtURL;
        this.postID = postID;
        this.content = content;
        this.createdDate = createdDate;
    }

    public Comment(int commentID, int userID, int postID, String content, Date createdDate) {
        this.commentID = commentID;
        this.userID = userID;
        this.postID = postID;
        this.content = content;
        this.createdDate = createdDate;
    }

    public Comment(int userID, String userFullName, String userAvtURL, int postID, String content) {
        this.userID = userID;
        this.userFullName = userFullName;
        this.postID = postID;
        this.content = content;
        this.userAvtURL = userAvtURL;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserAvtURL() {
        return userAvtURL;
    }

    public void setUserAvtURL(String userAvtURL) {
        this.userAvtURL = userAvtURL;
    }



    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
