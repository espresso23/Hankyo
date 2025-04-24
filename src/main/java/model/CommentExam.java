//package model;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//public class CommentExam {
//    private int commentExamID;
//    private int examID;
//    private int userID;
//    private String userFullName;
//    private String userAvatarURL;
//    private String content;
//    private int parentCommentID;
//    private int score;
//    private Date createdAt;
//
//    public CommentExam(int commentExamID, int examID, int userID, String userFullName, String userAvatarURL, String content, int parentCommentID, int score, Date createdAt) {
//        this.commentExamID = commentExamID;
//        this.examID = examID;
//        this.userID = userID;
//        this.userFullName = userFullName;
//        this.userAvatarURL = userAvatarURL;
//        this.content = content;
//        this.parentCommentID = parentCommentID;
//        this.score = score;
//        this.createdAt = createdAt;
//    }
//
//    public int getCommentExamID() {
//        return commentExamID;
//    }
//
//    public void setCommentExamID(int commentExamID) {
//        this.commentExamID = commentExamID;
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
//    public String getUserAvatarURL() {
//        return userAvatarURL;
//    }
//
//    public void setUserAvatarURL(String userAvatarURL) {
//        this.userAvatarURL = userAvatarURL;
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
//    public int getParentCommentID() {
//        return parentCommentID;
//    }
//
//    public void setParentCommentID(int parentCommentID) {
//        this.parentCommentID = parentCommentID;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    @Override
//    public String toString() {
//        return "CommentExam{" +
//                "commentExamID=" + commentExamID +
//                ", examID=" + examID +
//                ", userID=" + userID +
//                ", userFullName='" + userFullName + '\'' +
//                ", userAvatarURL='" + userAvatarURL + '\'' +
//                ", content='" + content + '\'' +
//                ", parentCommentID=" + parentCommentID +
//                ", score=" + score +
//                ", createdAt=" + createdAt +
//                '}';
//    }
//}