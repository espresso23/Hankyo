package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import model.Post;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    UserDAO userDAO = new UserDAO ();
    private DBConnect dbContext;

    public PostDAO() {
        dbContext = DBConnect.getInstance();
    }

    public boolean checkConnection() throws Exception {
        try (Connection conn = dbContext.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAvatarByPostId(int postID) throws Exception {
        String avatarImg = null;
        String query = "SELECT u.avatar FROM Post p INNER JOIN dbo.[User] u ON p.UserID = u.UserID WHERE p.PostID = ?";

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, postID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    avatarImg = rs.getString("avatar");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving avatar image by PostID: " + e.getMessage());
        }

        return avatarImg;
    }

    public int getUserIDByPostId(int postID) throws Exception {
        int userID = 0;
        String query = "SELECT UserID FROM Post WHERE PostID = ?";

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
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

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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
                            rs.getBoolean("status")
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
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

    // Qhuy lock illegal post
    public boolean lockIllegalPost(int postID) {
        boolean flag = false;
        String sql = " UPDATE Post\n"
                + "  SET status = 0\n"
                + "  WHERE PostID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postID);
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

    // Admin - unlockPost
    public boolean unlockPost(int postID) {
        boolean flag = false;
        String sql = " UPDATE Post\n"
                + "  SET status = 1\n"
                + "  WHERE PostID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postID);
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

    // Admin function - getNumberOfProduct
    public int getNumberOfPosts() {
        int postCount = 0;
        String sql = "SELECT COUNT(*) FROM Post";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                postCount = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postCount;
    }

    // Admin function - getNumberOfProduct
    public int getNumberOfLockedPosts() {
        int postCount = 0;
        String sql = "SELECT COUNT(*) AS LockedCount FROM Post WHERE Status = 0";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                postCount = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postCount;
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

    // Admin get all post kể cả inactive
    public List<Post> getAllPostsData() throws Exception {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM Post ORDER BY CreatedDate DESC";
        System.out.println("Executing query: " + query);

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("PostID"),
                        rs.getInt("UserID"),
                        rs.getString("ImgURL"),
                        rs.getString("Heading"),
                        formatContent(rs.getString("Content")), // Gọi hàm formatContent để định dạng nội dung
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

}

