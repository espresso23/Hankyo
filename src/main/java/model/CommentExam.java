//package model;
//
//import java.io.Serializable;
//import java.util.Date;
//
//public class CommentExam implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    private int examCommentID;
//    private int userID;
//    private String userFullName;
//    private String userAvtURL;
//    private int examID;
//    private String content;
//    private Date createdDate;
//    private int parentExamCommentID;
//    private int score;
//
//    public CommentExam() {
//    }
//
//    public CommentExam(int examCommentID, int userID, String userFullName, String userAvtURL, int examID, String content, Date createdDate) {
//        this.examCommentID = examCommentID;
//        this.userID = userID;
//        this.userFullName = userFullName;
//        this.userAvtURL = userAvtURL;
//        this.examID = examID;
//        this.content = content;
//        this.createdDate = createdDate;
//    }
//
//    public CommentExam(int examCommentID, int userID, int examID, String content, Date createdDate) {
//        this.examCommentID = examCommentID;
//        this.userID = userID;
//        this.examID = examID;
//        this.content = content;
//        this.createdDate = createdDate;
//    }
//
//    public CommentExam(int userID, String userFullName, String userAvtURL, int examID, String content) {
//        this.userID = userID;
//        this.userFullName = userFullName;
//        this.userAvtURL = userAvtURL;
//        this.examID = examID;
//        this.content = content;
//    }
//
//    // Getters and Setters
//    public int getExamCommentID() {
//        return examCommentID;
//    }
//
//    public void setExamCommentID(int examCommentID) {
//        this.examCommentID = examCommentID;
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public String getUserFullName() {
//        return userFullName;
//    }
//
//    public void setUserFullName(String userFullName) {
//        this.userFullName = userFullName;
//    }
//
//    public String getUserAvtURL() {
//        return userAvtURL;
//    }
//
//    public void setUserAvtURL(String userAvtURL) {
//        this.userAvtURL = userAvtURL;
//    }
//
//    public int getExamID() {
//        return examID;
//    }
//
//    public void setExamID(int examID) {
//        this.examID = examID;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public int getParentExamCommentID() {
//        return parentExamCommentID;
//    }
//
//    public void setParentExamCommentID(int parentExamCommentID) {
//        this.parentExamCommentID = parentExamCommentID;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//}