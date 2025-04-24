//package dao;
//
//import model.CommentExam;
//import util.DBConnect;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CommentExamDAO {
//    private static final String INSERT_COMMENT = "INSERT INTO CommentExam (examID, userID, content, parentCommentID, score, createdAt, userFullName) VALUES (?, ?, ?, ?, 0, GETDATE(), ?)";
//    private static final String UPDATE_COMMENT = "UPDATE CommentExam SET content = ? WHERE commentExamID = ? AND userID = ?";
//    private static final String DELETE_COMMENT = "DELETE FROM CommentExam WHERE commentExamID = ? AND userID = ?";
//    private static final String GET_COMMENT_BY_ID = "SELECT c.*, u.fullName AS userFullName, u.avatar AS userAvatarURL FROM CommentExam c JOIN Users u ON c.userID = u.userID WHERE commentExamID = ?";
//    private static final String GET_COMMENTS_BY_EXAM = "SELECT c.*, u.fullName AS userFullName, u.avatar AS userAvatarURL FROM CommentExam c JOIN Users u ON c.userID = u.userID WHERE examID = ? ORDER BY createdAt DESC";
//    private static final String UPDATE_SCORE = "UPDATE CommentExam SET score = score + ? WHERE commentExamID = ?";
//    private static final String INSERT_VOTE = "INSERT INTO UserVoteExams (userID, commentExamID, voteType, voteDate) VALUES (?, ?, ?, GETDATE())";
//    private static final String CHECK_VOTE = "SELECT voteType FROM UserVoteExams WHERE userID = ? AND commentExamID = ?";
//    private static final String UPDATE_VOTE = "UPDATE UserVoteExams SET voteType = ?, voteDate = GETDATE() WHERE userID = ? AND commentExamID = ?";
//
//    // Add a new comment
//    public CommentExam addComment(int examId, int userId, String content, Integer parentCommentId) throws SQLException {
//        try (Connection connection = DBConnect.getInstance().getConnection()) {
//            // Fetch the user's fullName from the Users table
//            String fullName = null;
//            String getUserQuery = "SELECT fullName FROM Users WHERE userID = ?";
//            try (PreparedStatement userPs = connection.prepareStatement(getUserQuery)) {
//                userPs.setInt(1, userId);
//                try (ResultSet rs = userPs.executeQuery()) {
//                    if (rs.next()) {
//                        fullName = rs.getString("fullName");
//                    }
//                }
//            }
//
//            // If fullName is null, use a fallback value
//            if (fullName == null || fullName.trim().isEmpty()) {
//                fullName = "Anonymous User"; // Fallback to avoid NOT NULL constraint violation
//            }
//
//            // Insert the comment with the fetched fullName
//            try (PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setInt(1, examId);
//                ps.setInt(2, userId);
//                ps.setString(3, content);
//                if (parentCommentId != null) {
//                    ps.setInt(4, parentCommentId);
//                } else {
//                    ps.setNull(4, Types.INTEGER);
//                }
//                ps.setString(5, fullName);
//
//                int affectedRows = ps.executeUpdate();
//                if (affectedRows == 0) {
//                    return null;
//                }
//
//                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        int commentId = generatedKeys.getInt(1);
//                        return getCommentById(commentId);
//                    } else {
//                        return null;
//                    }
//                }
//            }
//        }
//    }
//
//    // Get a single comment by ID
//    public CommentExam getCommentById(int commentId) throws SQLException {
//        try (Connection connection = DBConnect.getInstance().getConnection();
//             PreparedStatement ps = connection.prepareStatement(GET_COMMENT_BY_ID)) {
//            ps.setInt(1, commentId);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return mapResultSetToComment(rs);
//                }
//            }
//        }
//        return null;
//    }
//
//    // Edit a comment
//    public CommentExam editComment(int commentId, int userId, String newContent) throws SQLException {
//        try (Connection connection = DBConnect.getInstance().getConnection();
//             PreparedStatement ps = connection.prepareStatement(UPDATE_COMMENT)) {
//            ps.setString(1, newContent);
//            ps.setInt(2, commentId);
//            ps.setInt(3, userId);
//
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows > 0) {
//                return getCommentById(commentId);
//            }
//            return null;
//        }
//    }
//
//    // Delete a comment
//    public boolean deleteComment(int commentId, int userId) throws SQLException {
//        try (Connection connection = DBConnect.getInstance().getConnection();
//             PreparedStatement ps = connection.prepareStatement(DELETE_COMMENT)) {
//            ps.setInt(1, commentId);
//            ps.setInt(2, userId);
//            return ps.executeUpdate() > 0;
//        }
//    }
//
//    // Get comments for an exam
//    public List<CommentExam> getCommentsByExam(int examId) throws SQLException {
//        List<CommentExam> comments = new ArrayList<>();
//        try (Connection connection = DBConnect.getInstance().getConnection();
//             PreparedStatement ps = connection.prepareStatement(GET_COMMENTS_BY_EXAM)) {
//            ps.setInt(1, examId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    comments.add(mapResultSetToComment(rs));
//                }
//            }
//        }
//        return comments;
//    }
//
//    // Helper method to map ResultSet to CommentExam object
//    private CommentExam mapResultSetToComment(ResultSet rs) throws SQLException {
//        return new CommentExam(
//                rs.getInt("commentExamID"),
//                rs.getInt("examID"),
//                rs.getInt("userID"),
//                rs.getString("userFullName"),
//                rs.getString("userAvatarURL"),
//                rs.getString("content"),
//                rs.getInt("parentCommentID") != 0 ? rs.getInt("parentCommentID") : 0,
//                rs.getInt("score"),
//                rs.getTimestamp("createdAt")
//        );
//    }
//
//    // Vote (like/dislike) a comment
//    public CommentExam voteComment(int userId, int commentId, int voteType) throws SQLException {
//        Connection connection = null;
//        try {
//            connection = DBConnect.getInstance().getConnection();
//            connection.setAutoCommit(false);
//
//            // Check if user has already voted
//            try (PreparedStatement checkPs = connection.prepareStatement(CHECK_VOTE)) {
//                checkPs.setInt(1, userId);
//                checkPs.setInt(2, commentId);
//                ResultSet rs = checkPs.executeQuery();
//
//                PreparedStatement votePs;
//                if (rs.next()) {
//                    // Update existing vote
//                    int currentVote = rs.getInt("voteType");
//                    if (currentVote == voteType) {
//                        // If same vote type, remove vote
//                        voteType = 0;
//                    }
//                    votePs = connection.prepareStatement(UPDATE_VOTE);
//                    votePs.setInt(1, voteType);
//                    votePs.setInt(2, userId);
//                    votePs.setInt(3, commentId);
//                } else {
//                    // Insert new vote
//                    votePs = connection.prepareStatement(INSERT_VOTE);
//                    votePs.setInt(1, userId);
//                    votePs.setInt(2, commentId);
//                    votePs.setInt(3, voteType);
//                }
//                votePs.executeUpdate();
//
//                // Update comment score
//                try (PreparedStatement scorePs = connection.prepareStatement(UPDATE_SCORE)) {
//                    scorePs.setInt(1, voteType);
//                    scorePs.setInt(2, commentId);
//                    scorePs.executeUpdate();
//                }
//            }
//
//            connection.commit();
//            // Return updated comment
//            return getCommentById(commentId);
//        } catch (SQLException e) {
//            if (connection != null) {
//                connection.rollback();
//            }
//            throw e;
//        } finally {
//            if (connection != null) {
//                connection.setAutoCommit(true);
//                connection.close();
//            }
//        }
//    }
//}