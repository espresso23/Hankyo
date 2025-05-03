package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Post;
import util.DBConnect;

public class PostDAO {

    UserDAO userDAO = new UserDAO();

    public PostDAO() {
    }

    public boolean checkConnection() throws Exception {
        try (Connection conn = DBConnect.getInstance().getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAvatarByPostId(int postID) throws Exception {
        String avatarImg = null;
        String query = "SELECT u.avatar FROM Post p INNER JOIN dbo.[User] u ON p.UserID = u.UserID WHERE p.PostID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    avatarImg = rs.getString("avatar");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return avatarImg;
    }

    public int getUserIDByPostId(int postID) throws Exception {
        int userID = 0;
        String query = "SELECT UserID FROM Post WHERE PostID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userID = rs.getInt("UserID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving UserID by PostID: " + e.getMessage());
        }

        return userID;
    }

    public List<Post> getAllPostsHaveFullNameAndAvtImg() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post WHERE status = 1 ORDER BY CreatedDate DESC";
        System.out.println("Executing query: " + query);

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setScore(rs.getInt("ScorePost"));
                String fullName = userDAO.getFullNameByUserId(post.getUserID());
                if (fullName != null) {
                    post.setUserFullName(fullName);
                } else {
                    post.setUserFullName("Unknown User");
                }

                String avatarURL = userDAO.getAvatarByUserId(post.getUserID());
                if (avatarURL != null) {
                    post.setAvtUserImg(avatarURL);
                } else {
                    post.setAvtUserImg("default-avatar.png");
                }

                posts.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving posts: " + e.getMessage());
        }

        System.out.println("Number of posts retrieved: " + posts.size());
        return posts;
    }

    // Get all active posts
    public List<Post> getAllPosts() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post WHERE status = 1 ORDER BY CreatedDate DESC";
        System.out.println("Executing query: " + query);

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Post post;
                post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving posts: " + e.getMessage());
        }
        System.out.println("Number of posts retrieved: " + posts.size());
        return posts;
    }

    // Get all posts with full name
    public List<Post> getAllPostsHaveFullName() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post WHERE status = 1 ORDER BY CreatedDate DESC";
        System.out.println("Executing query: " + query);

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                String fullName = userDAO.getFullNameByUserId(post.getUserID());
                post.setUserFullName(fullName); // Assuming Post has setter for FullName
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving posts: " + e.getMessage());
        }
        System.out.println("Number of posts retrieved: " + posts.size());
        return posts;
    }

    // Get post by ID if it's active
    public Post getPostById(int postID) throws Exception {
        Post post = null;
        String query = "SELECT * FROM Post WHERE PostID = ? AND status = 1";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(
                            rs.getInt("PostID"),
                            rs.getInt("UserID"),
                            rs.getString("ImgURL"),
                            rs.getString("Heading"),
                            rs.getString("Content"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getBoolean("status"),
                            rs.getInt("ScorePost")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    // Create new post
    public boolean createPost(Post post) throws Exception {
        String query = "INSERT INTO Post (UserID, ImgURL, Heading, Content, CreatedDate, status) VALUES (?, ?, ?, ?, GETDATE(), 1)";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, post.getUserID());
            ps.setString(2, post.getImgURL());
            ps.setString(3, post.getHeading());
            ps.setString(4, post.getContent());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update post
    public boolean updatePost(Post post) throws Exception {
        String query = "UPDATE Post SET UserID = ?, ImgURL = ?, Heading = ?, Content = ?, CreatedDate = GETDATE() WHERE PostID = ? AND status = 1";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, post.getUserID());
            ps.setString(2, post.getImgURL());  // Update the image URL
            ps.setString(3, post.getHeading());  // Update the heading
            ps.setString(4, post.getContent());  // Update the content/description
            ps.setInt(5, post.getPostID());  // Specify the post ID to update
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error updating the post: " + e.getMessage());
        }
    }

    // Soft delete a post by setting its status to false
    public boolean deletePost(int postID) throws Exception {
        String query = "UPDATE Post SET status = 0 WHERE PostID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get posts ordered by latest (newest to oldest)
    public List<Post> getPostsOrderedByLatest() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post WHERE status = 1 ORDER BY CreatedDate DESC";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setScore(rs.getInt("ScorePost"));

                String fullName = userDAO.getFullNameByUserId(post.getUserID());
                post.setUserFullName(fullName != null ? fullName : "Unknown");

                String avatarURL = userDAO.getAvatarByUserId(post.getUserID());
                post.setAvtUserImg(avatarURL != null ? avatarURL : "default-avatar.png");

                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Get the latest post
    public Post getLatestPost() throws Exception {
        Post post = null;
        String query = "SELECT TOP 1 * FROM Post WHERE status = 1 ORDER BY CreatedDate DESC";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(
                            rs.getInt("PostID"),
                            rs.getInt("UserID"),
                            rs.getString("ImgURL"),
                            rs.getString("Heading"),
                            rs.getString("Content"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getBoolean("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    // Get the full name of the user based on the UserID of the post
    public String getFullNameByPostId(int postID) throws Exception {
        String fullName = null;
        String query = "SELECT u.fullName FROM Post p INNER JOIN [User] u ON p.UserID = u.UserID WHERE p.PostID = ? AND p.status = 1";

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fullName = rs.getString("FullName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }

    // Get the avatar image URL of the user based on the UserID
    public String getAvatarByUserId(int userID) throws Exception {
        String avatarImg = null;
        String query = "SELECT avatar FROM [User] WHERE UserID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    avatarImg = rs.getString("avatar");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avatarImg;
    }

    public List<Post> getPostsByUserID(int userId) throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post WHERE status = 1 AND UserID = ? ORDER BY CreatedDate DESC";
        System.out.println("Executing query: " + query);

        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId); // Đặt tham số cho PreparedStatement

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post(
                            rs.getInt("PostID"),
                            rs.getInt("UserID"),
                            rs.getString("ImgURL"),
                            rs.getString("Heading"),
                            rs.getString("Content"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getBoolean("status")
                    );

                    String fullName = userDAO.getFullNameByUserId(post.getUserID());
                    if (fullName != null) {
                        post.setUserFullName(fullName);
                    } else {
                        post.setUserFullName("Unknown User");
                    }

                    String avatarURL = userDAO.getAvatarByUserId(post.getUserID());
                    if (avatarURL != null) {
                        post.setAvtUserImg(avatarURL);
                    } else {
                        post.setAvtUserImg("default-avatar.png");
                    }

                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving posts: " + e.getMessage());
        }

        System.out.println("Number of posts retrieved for user ID " + userId + ": " + posts.size());
        return posts;
    }


    public String formatContent(String content) {
        String[] words = content.split(" ");
        StringBuilder formattedContent = new StringBuilder();

        // Lấy tối đa 7 từ đầu tiên
        int limit = Math.min(7, words.length);
        for (int i = 0; i < limit; i++) {
            formattedContent.append(words[i]).append(" ");
        }

        // Thêm dấu "..." nếu nội dung có nhiều hơn 7 từ
        if (words.length > 7) {
            formattedContent.append("...");
        }

        return formattedContent.toString().trim();
    }

    public boolean updateScore(int postID, int score) {
        boolean flag = false;
        String sql = "UPDATE Post SET ScorePost = ?  WHERE PostID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, postID);
            ps.setInt(1, score);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                flag = true;
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<Post> getPostsOrderedFilterByScore() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post ORDER BY ScorePost DESC";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setScore(rs.getInt("ScorePost"));
                String fullName = userDAO.getFullNameByUserId(post.getUserID());
                post.setUserFullName(fullName != null ? fullName : "Unknown");

                String avatarURL = userDAO.getAvatarByUserId(post.getUserID());
                post.setAvtUserImg(avatarURL != null ? avatarURL : "default-avatar.png");

                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    public List<Post> getPostsOrderedByScore() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT TOP 3 * FROM Post ORDER BY ScorePost DESC";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setScore(rs.getInt("ScorePost"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public int getCommentCount(int postID) {
        int count = 0;
        String query = "SELECT COUNT(c.commentID) FROM Comment c INNER JOIN Post p ON c.postID = p.postID WHERE c.postID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean updateUserVote(int userID, int postID, int voteType) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            conn = DBConnect.getInstance().getConnection();
            conn.setAutoCommit(false);

            // 1. Check if user already voted
            String checkSql = "SELECT VoteType FROM PostVotes WHERE UserID = ? AND PostID = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, userID);
            ps.setInt(2, postID);
            ResultSet rs = ps.executeQuery();

            int oldVoteType = 0;
            boolean existingVote = false;

            if (rs.next()) {
                existingVote = true;
                oldVoteType = rs.getInt("VoteType");
            }

            // 2. If new vote is same as old vote, remove vote
            if (existingVote && oldVoteType == voteType) {
                // Delete vote
                String deleteSql = "DELETE FROM PostVotes WHERE UserID = ? AND PostID = ?";
                ps = conn.prepareStatement(deleteSql);
                ps.setInt(1, userID);
                ps.setInt(2, postID);
                ps.executeUpdate();

                // Update post score (remove old vote)
                updatePostScore(conn, postID, -oldVoteType);
            }
            // 3. If changing vote or new vote
            else {
                if (existingVote) {
                    // Update existing vote
                    String updateSql = "UPDATE PostVotes SET VoteType = ?, VoteDate = CURRENT_TIMESTAMP WHERE UserID = ? AND PostID = ?";
                    ps = conn.prepareStatement(updateSql);
                    ps.setInt(1, voteType);
                    ps.setInt(2, userID);
                    ps.setInt(3, postID);
                    ps.executeUpdate();

                    // Update score (remove old vote, add new vote)
                    updatePostScore(conn, postID, -oldVoteType);
                    updatePostScore(conn, postID, voteType);
                } else {
                    // Insert new vote
                    String insertSql = "INSERT INTO PostVotes (UserID, PostID, VoteType) VALUES (?, ?, ?)";
                    ps = conn.prepareStatement(insertSql);
                    ps.setInt(1, userID);
                    ps.setInt(2, postID);
                    ps.setInt(3, voteType);
                    ps.executeUpdate();

                    // Update score (add new vote)
                    updatePostScore(conn, postID, voteType);
                }
            }

            conn.commit();
            success = true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
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

    private void updatePostScore(Connection conn, int postID, int scoreChange) throws SQLException {
        String sql = "UPDATE Post SET ScorePost = ScorePost + ? WHERE PostID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scoreChange);
            ps.setInt(2, postID);
            ps.executeUpdate();
        }
    }

    public Map<Integer, Integer> getUserVotes(int userId, List<Integer> postIds) {
        Map<Integer, Integer> votes = new HashMap<>();

        if (postIds == null || postIds.isEmpty()) return votes;

        StringBuilder sb = new StringBuilder("SELECT PostID, VoteType FROM PostVotes WHERE UserID = ? AND PostID IN (");
        for (int i = 0; i < postIds.size(); i++) {
            sb.append("?");
            if (i < postIds.size() - 1) sb.append(",");
        }
        sb.append(")");

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sb.toString())) {
            stmt.setInt(1, userId);
            for (int i = 0; i < postIds.size(); i++) {
                stmt.setInt(i + 2, postIds.get(i)); // offset 2 because userID is the 1st
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                votes.put(rs.getInt("PostID"), rs.getInt("VoteType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return votes;
    }

    public int getPostScore(int postId) {
        String sql = "SELECT SUM(VoteType) AS Score FROM PostVotes WHERE PostID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean addOrUpdateVote(int userID, int postID, int voteType) {
        String checkSql = "SELECT VoteType FROM PostVotes WHERE UserID = ? AND PostID = ?";
        String updateSql = "UPDATE PostVotes SET VoteType = ? WHERE UserID = ? AND PostID = ?";
        String insertSql = "INSERT INTO PostVotes (UserID, PostID, VoteType) VALUES (?, ?, ?)";

        try (Connection conn = DBConnect.getInstance().getConnection()) {
            // Check existing vote
            int existingVote = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, userID);
                ps.setInt(2, postID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    existingVote = rs.getInt("VoteType");
                }
            }

            // Calculate score change
            int scoreChange = voteType;
            if (existingVote != 0) {
                if (existingVote == voteType) {
                    // Clicking same vote again removes it
                    scoreChange = -voteType;
                    voteType = 0;
                } else {
                    // Changing vote (e.g., from up to down)
                    scoreChange = voteType - existingVote;
                }
            }

            // Update or insert vote
            if (voteType == 0) {
                // Remove vote
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PostVotes WHERE UserID = ? AND PostID = ?")) {
                    ps.setInt(1, userID);
                    ps.setInt(2, postID);
                    ps.executeUpdate();
                }
            } else if (existingVote != 0) {
                // Update existing vote
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, voteType);
                    ps.setInt(2, userID);
                    ps.setInt(3, postID);
                    ps.executeUpdate();
                }
            } else {
                // Insert new vote
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, userID);
                    ps.setInt(2, postID);
                    ps.setInt(3, voteType);
                    ps.executeUpdate();
                }
            }

            // Update post score
            return updatePostScore(postID, scoreChange);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatePostScore(int postID, int scoreChange) {
        String sql = "UPDATE Post SET ScorePost = ScorePost + ? WHERE PostID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scoreChange);
            ps.setInt(2, postID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserVote(int userID, int postID) {
        String sql = "SELECT VoteType FROM PostVotes WHERE UserID = ? AND PostID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setInt(2, postID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("VoteType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // 0 means no vote
    }
    public List<Post> searchPosts(String query) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.fullName, u.avatar, (SELECT COUNT(*) FROM Comment c WHERE c.postID = p.postID) AS commentCount FROM Post p JOIN dbo.[User] u ON p.userID = u.userID WHERE p.heading LIKE ? OR p.content LIKE ? ORDER BY p.CreatedDate DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setPostID(rs.getInt("PostID"));
                post.setHeading(rs.getString("Heading"));
                post.setContent(rs.getString("Content"));
                post.setUserID(rs.getInt("UserID"));
                post.setCreatedDate(rs.getTimestamp("CreatedDate"));
                post.setUserFullName(rs.getString("fullName"));
                post.setAvtUserImg(rs.getString("avatar"));
                post.setCommentCount(rs.getInt("commentCount"));
                post.setScore(rs.getInt("ScorePost"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    public List<Post> getPostsOrderedByOldest() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post WHERE status = 1 ORDER BY CreatedDate ASC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setScore(rs.getInt("ScorePost"));

                String fullName = userDAO.getFullNameByUserId(post.getUserID());
                post.setUserFullName(fullName != null ? fullName : "Unknown");

                String avatarURL = userDAO.getAvatarByUserId(post.getUserID());
                post.setAvtUserImg(avatarURL != null ? avatarURL : "default-avatar.png");

                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving oldest posts: " + e.getMessage());
        }
        return posts;
    }
    public List<Post> getUpvotedPostsByUserID(int userID) throws Exception {
        List<Post> upvotedPosts = new ArrayList<>();
        String sql = "SELECT p.*,u.username, u.fullName, u.avatar " +
                "FROM PostVotes v " +
                "JOIN Post p ON v.PostID = p.PostID " +
                "JOIN [User] u ON p.UserID = u.UserID " +
                "WHERE v.UserID = ? AND v.VoteType = 1 AND p.status = 1 " +
                "ORDER BY p.CreatedDate DESC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setUserName(rs.getString("username"));
                post.setScore(rs.getInt("ScorePost"));
                post.setUserFullName(rs.getString("fullName"));
                post.setAvtUserImg(rs.getString("avatar"));

                upvotedPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving upvoted posts: " + e.getMessage());
        }

        return upvotedPosts;
    }
    public List<Post> getDownvotedPostsByUserID(int userID) throws Exception {
        List<Post> downvotedPosts = new ArrayList<>();
        String sql = "SELECT p.*,u.username, u.fullName, u.avatar " +
                "FROM PostVotes v " +
                "JOIN Post p ON v.PostID = p.PostID " +
                "JOIN [User] u ON p.UserID = u.UserID " +
                "WHERE v.UserID = ? AND v.VoteType = -1 AND p.status = 1 " +
                "ORDER BY p.CreatedDate DESC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        rs.getString("Content"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getBoolean("status")
                );
                post.setUserName(rs.getString("username"));
                post.setScore(rs.getInt("ScorePost"));
                post.setUserFullName(rs.getString("fullName"));
                post.setAvtUserImg(rs.getString("avatar"));

                downvotedPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving downvoted posts: " + e.getMessage());
        }

        return downvotedPosts;
    }

    public int getPostCountByUserID(int userID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Post WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Post> getPostsByQuery(String query) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Post post = new Post();
                post.setPostID(rs.getInt("postID"));
                post.setUserID(rs.getInt("userID"));
                post.setHeading(rs.getString("heading"));
                post.setContent(rs.getString("content"));
                post.setCreatedDate(rs.getDate("createdDate"));
                post.setStatus(rs.getBoolean("status"));
                posts.add(post);
            }
        }
        return posts;
    }

}


