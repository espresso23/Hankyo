//package dao;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import model.CommentExam;
//import util.DBConnect;
//
//public class CommentExamDAO {
//    private DBConnect dbContext;
//
//    public CommentExamDAO() {
//        dbContext = new DBConnect();
//    }
//
//    // Method to add a comment
//    public boolean addComment(CommentExam comment) throws Exception {
//        String sql = "INSERT INTO ExamComment (UserID, ExamID, Content, CreatedDate) VALUES (?, ?, ?, ?)";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, comment.getUserID());
//            ps.setInt(2, comment.getExamID());
//            ps.setString(3, comment.getContent());
//            ps.setTimestamp(4, new Timestamp(comment.getCreatedDate().getTime()));
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    // Method to add a reply comment
//    public boolean addReplyComment(CommentExam comment) throws Exception {
//        String sql = "INSERT INTO ExamComment (UserID, ExamID, Content, CreatedDate, ParentExamCommentID) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, comment.getUserID());
//            ps.setInt(2, comment.getExamID());
//            ps.setString(3, comment.getContent());
//            ps.setTimestamp(4, new Timestamp(comment.getCreatedDate().getTime()));
//            ps.setInt(5, comment.getParentExamCommentID());
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    // Method to update a comment
//    public boolean updateComment(int examCommentID, String newContent) throws Exception {
//        String sql = "UPDATE ExamComment SET Content = ? WHERE ExamCommentID = ?";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, newContent);
//            ps.setInt(2, examCommentID);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    // Method to delete a comment
//    public boolean deleteComment(int examCommentID) throws Exception {
//        String sql = "DELETE FROM ExamComment WHERE ExamCommentID = ?";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, examCommentID);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    // Method to retrieve a comment by ID
//    public CommentExam getCommentByID(int examCommentID) throws Exception {
//        String sql = "SELECT * FROM ExamComment WHERE ExamCommentID = ?";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, examCommentID);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return new CommentExam(
//                            rs.getInt("ExamCommentID"),
//                            rs.getInt("UserID"),
//                            rs.getInt("ExamID"),
//                            rs.getString("Content"),
//                            rs.getTimestamp("CreatedDate")
//                    );
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // Method to retrieve all comments for a specific exam with user details
//    public List<CommentExam> getCommentsByExamID(int examID) throws Exception {
//        List<CommentExam> comments = new ArrayList<>();
//        String sql = "SELECT c.*, u.fullName, u.avatar FROM ExamComment c JOIN [User] u ON c.UserID = u.UserID WHERE c.ExamID = ? AND ParentExamCommentID IS NULL ORDER BY c.CreatedDate DESC";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, examID);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    CommentExam comment = new CommentExam(
//                            rs.getInt("ExamCommentID"),
//                            rs.getInt("UserID"),
//                            rs.getString("fullName"),
//                            rs.getString("avatar"),
//                            rs.getInt("ExamID"),
//                            rs.getString("Content"),
//                            rs.getTimestamp("CreatedDate")
//                    );
//                    comment.setScore(rs.getInt("Score"));
//                    comments.add(comment);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return comments;
//    }
//
//    // Method to retrieve replies for a specific comment
//    public List<CommentExam> getRepliesByCommentID(int examCommentID) throws Exception {
//        List<CommentExam> comments = new ArrayList<>();
//        String sql = "SELECT c.*, u.fullName, u.avatar FROM ExamComment c JOIN [User] u ON c.UserID = u.UserID WHERE ParentExamCommentID = ? ORDER BY c.CreatedDate DESC";
//        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, examCommentID);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    CommentExam comment = new CommentExam(
//                            rs.getInt("ExamCommentID"),
//                            rs.getInt("UserID"),
//                            rs.getString("fullName"),
//                            rs.getString("avatar"),
//                            rs.getInt("ExamID"),
//                            rs.getString("Content"),
//                            rs.getTimestamp("CreatedDate")
//                    );
//                    comment.setScore(rs.getInt("Score"));
//                    comments.add(comment);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return comments;
//    }
//
//    // Method to update user vote for a comment
//    public boolean updateUserVote(int userID, int examCommentID, int voteType) {
//        Connection conn = null;
//        PreparedStatement ps = null;
//        boolean success = false;
//
//        try {
//            conn = dbContext.getConnection();
//            conn.setAutoCommit(false);
//
//            // Check if user has already voted
//            String checkSql = "SELECT VoteType FROM UserExamCommentVotes WHERE UserID = ? AND ExamCommentID = ?";
//            ps = conn.prepareStatement(checkSql);
//            ps.setInt(1, userID);
//            ps.setInt(2, examCommentID);
//            ResultSet rs = ps.executeQuery();
//
//            int oldVoteType = 0;
//            boolean existingVote = false;
//
//            if (rs.next()) {
//                existingVote = true;
//                oldVoteType = rs.getInt("VoteType");
//            }
//
//            // If new vote is same as old vote, remove vote
//            if (existingVote && oldVoteType == voteType) {
//                String deleteSql = "DELETE FROM UserExamCommentVotes WHERE UserID = ? AND ExamCommentID = ?";
//                ps = conn.prepareStatement(deleteSql);
//                ps.setInt(1, userID);
//                ps.setInt(2, examCommentID);
//                ps.executeUpdate();
//
//                updateCommentScore(conn, examCommentID, -oldVoteType);
//            }
//            // If different vote or no vote
//            else {
//                if (existingVote) {
//                    String updateSql = "UPDATE UserExamCommentVotes SET VoteType = ?, VoteDate = CURRENT_TIMESTAMP WHERE UserID = ? AND ExamCommentID = ?";
//                    ps = conn.prepareStatement(updateSql);
//                    ps.setInt(1, voteType);
//                    ps.setInt(2, userID);
//                    ps.setInt(3, examCommentID);
//                    ps.executeUpdate();
//
//                    updateCommentScore(conn, examCommentID, -oldVoteType);
//                    updateCommentScore(conn, examCommentID, voteType);
//                } else {
//                    String insertSql = "INSERT INTO UserExamCommentVotes (UserID, ExamCommentID, VoteType) VALUES (?, ?, ?)";
//                    ps = conn.prepareStatement(insertSql);
//                    ps.setInt(1, userID);
//                    ps.setInt(2, examCommentID);
//                    ps.setInt(3, voteType);
//                    ps.executeUpdate();
//
//                    updateCommentScore(conn, examCommentID, voteType);
//                }
//            }
//
//            conn.commit();
//            success = true;
//        } catch (Exception e) {
//            try {
//                if (conn != null) conn.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        } finally {
//            try {
//                if (ps != null) ps.close();
//                if (conn != null) {
//                    conn.setAutoCommit(true);
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return success;
//    }
//
//    // Helper method to update comment score
//    private void updateCommentScore(Connection conn, int examCommentID, int scoreChange) throws SQLException {
//        String sql = "UPDATE ExamComment SET Score = Score + ? WHERE ExamCommentID = ?";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, scoreChange);
//            ps.setInt(2, examCommentID);
//            ps.executeUpdate();
//        }
//    }
//
//    // Method to get user votes for multiple comments
//    public Map<Integer, Integer> getUserVotes(int userID, List<Integer> examCommentIDs) {
//        Map<Integer, Integer> userVotes = new HashMap<>();
//
//        if (examCommentIDs.isEmpty()) {
//            return userVotes;
//        }
//
//        StringBuilder sql = new StringBuilder("SELECT ExamCommentID, VoteType FROM UserExamCommentVotes WHERE UserID = ? AND ExamCommentID IN (");
//        for (int i = 0; i < examCommentIDs.size(); i++) {
//            if (i > 0) sql.append(",");
//            sql.append("?");
//        }
//        sql.append(")");
//
//        try (Connection conn = dbContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//
//            ps.setInt(1, userID);
//            for (int i = 0; i < examCommentIDs.size(); i++) {
//                ps.setInt(i + 2, examCommentIDs.get(i));
//            }
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                int examCommentID = rs.getInt("ExamCommentID");
//                int voteType = rs.getInt("VoteType");
//                userVotes.put(examCommentID, voteType);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return userVotes;
//    }
//
//    // Method to get comment score
//    public int getCommentScore(int examCommentID) throws SQLException {
//        String sql = "SELECT Score FROM ExamComment WHERE ExamCommentID = ?";
//        int score = 0;
//
//        try (Connection conn = dbContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, examCommentID);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    score = rs.getInt("Score");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Error retrieving score for examCommentID: " + examCommentID, e);
//        }
//
//        return score;
//    }
//}