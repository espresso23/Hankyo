package dao;

import model.Notification;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    public NotificationDAO() {
    }

    public List<Notification> getUnreadNotifications(int userID) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT s.*, st.typeName " +
                "FROM Notification s " +
                "JOIN NotificationType st ON s.typeID = st.typeID " +
                "WHERE s.userID = ? AND s.isRead = 0 " +
                "ORDER BY s.dateGiven DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationID(rs.getInt("notificationID"));
                    notification.setUserID(rs.getInt("userID"));
                    notification.setTypeID(rs.getInt("typeID"));
                    notification.setSourceID(rs.getInt("sourceID"));
                    notification.setDescription(rs.getString("description"));
                    notification.setDateGiven(rs.getTimestamp("dateGiven"));
                    notification.setTypeName(rs.getString("typeName"));
                    // We'll handle sourceTitle separately
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Now let's fetch the source titles separately for each notification
        for (Notification notification : notifications) {
            try (Connection conn = DBConnect.getInstance().getConnection()) {
                String titleSql = "";
                switch (notification.getTypeName()) {
                    case "Forum":
                        titleSql = "SELECT heading as title FROM Post WHERE postID = ?";
                        break;
                    case "Comment":
                        titleSql = "SELECT content as title FROM Comment WHERE commentID = ?";
                        break;
                    case "Course":
                        titleSql = "SELECT courseName as title FROM Course WHERE courseID = ?";
                        break;
                    case "Honour":
                        titleSql = "SELECT honourName as title FROM Honour WHERE honourID = ?";
                        break;
                }
                if (!titleSql.isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(titleSql)) {
                        ps.setInt(1, notification.getSourceID());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                notification.setSourceTitle(rs.getString("title"));
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // If we fail to get the source title, just continue with the next notification
            }
        }
        return notifications;
    }

    public int getUnreadCount(int userID) {
        String sql = "SELECT COUNT(*) as count FROM Notification WHERE userID = ? AND isRead = 0";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void markAsRead(int notificationID) {
        String sql = "UPDATE Notification SET isRead = 1 WHERE notificationID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAllAsRead(int userID) {
        String sql = "UPDATE Notification SET isRead = 1 WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNotification(Notification notification) {
        String sql = "INSERT INTO Notification (userID, typeID, sourceID, description, dateGiven, isRead) " +
                "VALUES (?, ?, ?, ?, GETDATE(), 0)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notification.getUserID());
            ps.setInt(2, notification.getTypeID());
            ps.setInt(3, notification.getSourceID());
            ps.setString(4, notification.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCommentReplyNotification(int recipientUserID, int commentID, String replierName) {
        String description = replierName + " replied to your comment";
        Notification notification = new Notification();
        notification.setUserID(recipientUserID);
        notification.setTypeID(2); // Assuming 2 is for Comment type
        notification.setSourceID(commentID);
        notification.setDescription(description);
        addNotification(notification);
    }

    public void addCommentVoteNotification(int recipientUserID, int commentID, String voterName, boolean isUpvote) {
        String voteType = isUpvote ? "upvoted" : "downvoted";
        String description = voterName + " " + voteType + " your comment";
        Notification notification = new Notification();
        notification.setUserID(recipientUserID);
        notification.setTypeID(2); // Assuming 2 is for Comment type
        notification.setSourceID(commentID);
        notification.setDescription(description);
        addNotification(notification);
    }

    public void addPostCommentNotification(int postOwnerID, int postID, String commenterName) {
        String description = commenterName + " commented on your post";
        Notification notification = new Notification();
        notification.setUserID(postOwnerID);
        notification.setTypeID(1); // Assuming 1 is for Forum/Post type
        notification.setSourceID(postID);
        notification.setDescription(description);
        addNotification(notification);
    }

    public void addPostVoteNotification(int postOwnerID, int postID, String voterName, boolean isUpvote) {
        String voteType = isUpvote ? "upvoted" : "downvoted";
        String description = voterName + " " + voteType + " your post";
        Notification notification = new Notification();
        notification.setUserID(postOwnerID);
        notification.setTypeID(1); // Assuming 1 is for Forum/Post type
        notification.setSourceID(postID);
        notification.setDescription(description);
        addNotification(notification);
    }

    private Notification mapNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationID(rs.getInt("notificationID"));
        notification.setUserID(rs.getInt("userID"));
        notification.setTypeID(rs.getInt("typeID"));
        notification.setSourceID(rs.getInt("sourceID"));
        notification.setDescription(rs.getString("description"));
        notification.setDateGiven(rs.getTimestamp("dateGiven"));
        notification.setTypeName(rs.getString("typeName"));
        notification.setSourceTitle(rs.getString("sourceTitle"));
        return notification;
    }
}