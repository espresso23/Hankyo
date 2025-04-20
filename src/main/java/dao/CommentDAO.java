/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Comment;
import util.DBConnect;


public class CommentDAO {

    private DBConnect dbContext;

    public CommentDAO() {
        dbContext = new DBConnect();
    }

    // Method to add a comment
    public boolean addComment(Comment comment) throws Exception {
        String sql = "INSERT INTO Comment (UserID, PostID, Content, CreatedDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comment.getUserID());
            ps.setInt(2, comment.getPostID());
            ps.setString(3, comment.getContent());
            ps.setTimestamp(4, new Timestamp(comment.getCreatedDate().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addReplyComment(Comment comment) throws Exception {
        String sql = "INSERT INTO Comment (UserID, PostID, Content, CreatedDate, ParentCommentID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comment.getUserID());
            ps.setInt(2, comment.getPostID());
            ps.setString(3, comment.getContent());
            ps.setTimestamp(4, new Timestamp(comment.getCreatedDate().getTime()));
            ps.setInt(5, comment.getParentCommentID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a comment
    public boolean updateComment(int commentID, String newContent) throws Exception {
        String sql = "UPDATE Comment SET Content = ? WHERE CommentID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newContent);
            ps.setInt(2, commentID);

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a comment
    public boolean deleteComment(int commentID) throws Exception {
        String sql = "DELETE FROM Comment WHERE CommentID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve a comment by ID
    public Comment getCommentByID(int commentID) throws Exception {
        String sql = "SELECT * FROM Comment WHERE CommentID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Comment(
                            rs.getInt("CommentID"),
                            rs.getInt("UserID"),
                            rs.getInt("PostID"),
                            rs.getString("Content"),
                            rs.getTimestamp("CreatedDate")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to retrieve all comments for a specific post with user details
    public List<Comment> getCommentsByPostID(int postID) throws Exception {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.fullName, u.avatar FROM Comment c JOIN [User] u ON c.UserID = u.UserID WHERE c.PostID = ? AND ParentCommentID IS NULL ORDER BY c.CreatedDate DESC";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                            rs.getInt("CommentID"),
                            rs.getInt("UserID"),
                            rs.getString("fullName"),
                            rs.getString("avatar"),
                            rs.getInt("PostID"),
                            rs.getString("Content"),
                            rs.getTimestamp("CreatedDate")
                    );
                    comment.setScore(rs.getInt("Score"));
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public List<Comment> getRepliesByCommentID(int commentID) throws Exception {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.fullName, u.avatar FROM Comment c JOIN [User] u ON c.UserID = u.UserID WHERE ParentCommentID = ? ORDER BY c.CreatedDate DESC";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                            rs.getInt("CommentID"),
                            rs.getInt("UserID"),
                            rs.getString("fullName"),
                            rs.getString("avatar"),
                            rs.getInt("PostID"),
                            rs.getString("Content"),
                            rs.getTimestamp("CreatedDate")
                    );
                    comment.setScore(rs.getInt("Score"));
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }


    // Method to delete spam comment
    public boolean deleteSpamComment(int userID, String content) throws Exception {
        String sql = " DELETE Comment WHERE UserID= ? AND Content =?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setString(2, content);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //    public boolean updateScore(int commentID, int score) {
//        boolean flag = false;
//        String sql = "UPDATE Comment SET Score = ? WHERE CommentID = ?";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, score);   // Set score first
//            ps.setInt(2, commentID); // Set commentID second
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected > 0) {
//                flag = true;
//            }
//            ps.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }
    public boolean updateUserVote(int userID, int commentID, int voteType) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            conn = dbContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Kiểm tra xem người dùng đã vote chưa
            String checkSql = "SELECT VoteType FROM UserVotes WHERE UserID = ? AND CommentID = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, userID);
            ps.setInt(2, commentID);
            ResultSet rs = ps.executeQuery();

            int oldVoteType = 0;
            boolean existingVote = false;

            if (rs.next()) {
                existingVote = true;
                oldVoteType = rs.getInt("VoteType");
            }

            // 2. Nếu vote mới giống vote cũ, hủy vote
            if (existingVote && oldVoteType == voteType) {
                // Xóa vote
                String deleteSql = "DELETE FROM UserVotes WHERE UserID = ? AND CommentID = ?";
                ps = conn.prepareStatement(deleteSql);
                ps.setInt(1, userID);
                ps.setInt(2, commentID);
                ps.executeUpdate();

                // Cập nhật điểm (bỏ vote cũ)
                updateCommentScore(conn, commentID, -oldVoteType);
            }
            // 3. Nếu đã vote khác loại hoặc chưa vote
            else {
                if (existingVote) {
                    // Cập nhật vote từ loại cũ sang loại mới
                    String updateSql = "UPDATE UserVotes SET VoteType = ?, VoteDate = CURRENT_TIMESTAMP WHERE UserID = ? AND CommentID = ?";
                    ps = conn.prepareStatement(updateSql);
                    ps.setInt(1, voteType);
                    ps.setInt(2, userID);
                    ps.setInt(3, commentID);
                    ps.executeUpdate();

                    // Cập nhật điểm (bỏ vote cũ, thêm vote mới)
                    updateCommentScore(conn, commentID, -oldVoteType); // Bỏ vote cũ
                    updateCommentScore(conn, commentID, voteType);    // Thêm vote mới
                } else {
                    // Thêm vote mới
                    String insertSql = "INSERT INTO UserVotes (UserID, CommentID, VoteType) VALUES (?, ?, ?)";
                    ps = conn.prepareStatement(insertSql);
                    ps.setInt(1, userID);
                    ps.setInt(2, commentID);
                    ps.setInt(3, voteType);
                    ps.executeUpdate();

                    // Cập nhật điểm (thêm vote mới)
                    updateCommentScore(conn, commentID, voteType);
                }
            }

            conn.commit(); // Kết thúc transaction thành công
            success = true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); // Hoàn tác nếu lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    // Hàm hỗ trợ để cập nhật score của comment
    private void updateCommentScore(Connection conn, int commentID, int scoreChange) throws SQLException {
        String sql = "UPDATE Comment SET Score = Score + ? WHERE CommentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scoreChange);
            ps.setInt(2, commentID);
            ps.executeUpdate();
        }
    }

    // Hàm để lấy trạng thái vote của người dùng cho từng comment
    public Map<Integer, Integer> getUserVotes(int userID, List<Integer> commentIDs) {
        Map<Integer, Integer> userVotes = new HashMap<>();

        if (commentIDs.isEmpty()) {
            return userVotes;
        }

        StringBuilder sql = new StringBuilder("SELECT CommentID, VoteType FROM UserVotes WHERE UserID = ? AND CommentID IN (");
        for (int i = 0; i < commentIDs.size(); i++) {
            if (i > 0) sql.append(",");
            sql.append("?");
        }
        sql.append(")");

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setInt(1, userID);
            for (int i = 0; i < commentIDs.size(); i++) {
                ps.setInt(i + 2, commentIDs.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int commentID = rs.getInt("CommentID");
                int voteType = rs.getInt("VoteType");
                userVotes.put(commentID, voteType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userVotes;
    }
    // Method to retrieve the score of a comment by its ID
    public int getCommentScore(int commentID) throws SQLException {
        String sql = "SELECT Score FROM Comment WHERE CommentID = ?";
        int score = 0; // Default score is 0 if not found

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    score = rs.getInt("Score"); // Retrieve score from the database
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error if any
            throw new SQLException("Error retrieving score for commentID: " + commentID, e);
        }

        return score; // Return the score of the comment
    }

}

