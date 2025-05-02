package dao;

import model.Signification;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignificationDAO {
    public SignificationDAO() {
    }

    public List<Signification> getUnreadSignifications(int userID) {
        List<Signification> significations = new ArrayList<>();
        String sql = "SELECT s.*, st.typeName " +
                "FROM Signification s " +
                "JOIN SignificationType st ON s.typeID = st.typeID " +
                "WHERE s.userID = ? AND s.isRead = 0 " +
                "ORDER BY s.dateGiven DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Signification sig = new Signification();
                    sig.setSignificationID(rs.getInt("significationID"));
                    sig.setUserID(rs.getInt("userID"));
                    sig.setTypeID(rs.getInt("typeID"));
                    sig.setSourceID(rs.getInt("sourceID"));
                    sig.setDescription(rs.getString("description"));
                    sig.setDateGiven(rs.getTimestamp("dateGiven"));
                    sig.setTypeName(rs.getString("typeName"));
                    // We'll handle sourceTitle separately
                    significations.add(sig);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Now let's fetch the source titles separately for each notification
        for (Signification sig : significations) {
            try (Connection conn = DBConnect.getInstance().getConnection()) {
                String titleSql = "";
                switch (sig.getTypeName()) {
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
                        ps.setInt(1, sig.getSourceID());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                sig.setSourceTitle(rs.getString("title"));
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // If we fail to get the source title, just continue with the next notification
            }
        }
        return significations;
    }

    public int getUnreadCount(int userID) {
        String sql = "SELECT COUNT(*) as count FROM Signification WHERE userID = ? AND isRead = 0";
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

    public void markAsRead(int significationID) {
        String sql = "UPDATE Signification SET isRead = 1 WHERE significationID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, significationID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAllAsRead(int userID) {
        String sql = "UPDATE Signification SET isRead = 1 WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSignification(Signification signification) {
        String sql = "INSERT INTO Signification (userID, typeID, sourceID, description, dateGiven, isRead) " +
                "VALUES (?, ?, ?, ?, GETDATE(), 0)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, signification.getUserID());
            ps.setInt(2, signification.getTypeID());
            ps.setInt(3, signification.getSourceID());
            ps.setString(4, signification.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCommentReplyNotification(int recipientUserID, int commentID, String replierName) {
        String description = replierName + " replied to your comment";
        Signification notification = new Signification();
        notification.setUserID(recipientUserID);
        notification.setTypeID(2); // Assuming 2 is for Comment type
        notification.setSourceID(commentID);
        notification.setDescription(description);
        addSignification(notification);
    }

    public void addCommentVoteNotification(int recipientUserID, int commentID, String voterName, boolean isUpvote) {
        String voteType = isUpvote ? "upvoted" : "downvoted";
        String description = voterName + " " + voteType + " your comment";
        Signification notification = new Signification();
        notification.setUserID(recipientUserID);
        notification.setTypeID(2); // Assuming 2 is for Comment type
        notification.setSourceID(commentID);
        notification.setDescription(description);
        addSignification(notification);
    }

    public void addPostCommentNotification(int postOwnerID, int postID, String commenterName) {
        String description = commenterName + " commented on your post";
        Signification notification = new Signification();
        notification.setUserID(postOwnerID);
        notification.setTypeID(1); // Assuming 1 is for Forum/Post type
        notification.setSourceID(postID);
        notification.setDescription(description);
        addSignification(notification);
    }

    public void addPostVoteNotification(int postOwnerID, int postID, String voterName, boolean isUpvote) {
        String voteType = isUpvote ? "upvoted" : "downvoted";
        String description = voterName + " " + voteType + " your post";
        Signification notification = new Signification();
        notification.setUserID(postOwnerID);
        notification.setTypeID(1); // Assuming 1 is for Forum/Post type
        notification.setSourceID(postID);
        notification.setDescription(description);
        addSignification(notification);
    }

    private Signification mapSignification(ResultSet rs) throws SQLException {
        Signification sig = new Signification();
        sig.setSignificationID(rs.getInt("significationID"));
        sig.setUserID(rs.getInt("userID"));
        sig.setTypeID(rs.getInt("typeID"));
        sig.setSourceID(rs.getInt("sourceID"));
        sig.setDescription(rs.getString("description"));
        sig.setDateGiven(rs.getTimestamp("dateGiven"));
        sig.setTypeName(rs.getString("typeName"));
        sig.setSourceTitle(rs.getString("sourceTitle"));
        return sig;
    }
}