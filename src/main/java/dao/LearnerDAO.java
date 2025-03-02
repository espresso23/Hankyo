package dao;

import model.Honour;
import model.Learner;
import model.Reward;
import model.Vip;
import util.DBConnect;

import java.sql.*;
import java.util.Date;

public class LearnerDAO {

    private Connection connection;

    public LearnerDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public Learner getLearnerById(int learnerId) {
        String query = "SELECT l.*, u.*, r.*, v.*, h.* FROM Learner l JOIN [User] u ON l.userID = u.userID LEFT JOIN Reward r ON l.learnerID = r.learnerID LEFT JOIN VipDetails v ON l.learnerID = v.learnerID LEFT JOIN Honour h ON l.honourID = h.honourID WHERE l.learnerID = ? AND l.status = 'active'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, learnerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLearner(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createLearner(Learner learner) throws SQLException {
        String insertUserQuery = "INSERT INTO Users (username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertLearnerQuery = "INSERT INTO Learners (userID, hankyoPoint, honourID, rewardID, vipID) VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Insert into Users table
            try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, learner.getUsername());
                userStmt.setString(2, learner.getPassword());
                userStmt.setString(3, learner.getGmail());
                userStmt.setString(4, learner.getPhone());
                userStmt.setString(5, learner.getRole());
                userStmt.setString(6, learner.getStatus());
                userStmt.setString(7, learner.getFullName());
                userStmt.setString(8, learner.getSocialID());
                userStmt.setDate(9, new java.sql.Date(learner.getDateCreate().getTime()));
                userStmt.setString(10, learner.getGender());
                userStmt.setInt(11, learner.getAge());
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    learner.setUserID(generatedKeys.getInt(1));
                }
            }

            // Insert into Learners table
            try (PreparedStatement learnerStmt = connection.prepareStatement(insertLearnerQuery)) {
                learnerStmt.setInt(1, learner.getUserID());
                learnerStmt.setDouble(2, learner.getHankyoPoint());
                learnerStmt.setInt(3, learner.getHonour() != null ? learner.getHonour().getHonourID() : Types.NULL);
                learnerStmt.setInt(4, learner.getReward() != null ? learner.getReward().getRewardID() : Types.NULL);
                learnerStmt.setInt(5, learner.getVip() != null ? learner.getVip().getVipID() : Types.NULL);
                learnerStmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false;
    }

    public boolean updateLearner(Learner learner) throws SQLException {
        String updateUserQuery = "UPDATE [User] SET gmail = ?, phone = ?, fullName = ? WHERE userID = ?";


        // Update Users table
        try (PreparedStatement userStmt = connection.prepareStatement(updateUserQuery)) {
            userStmt.setString(1, learner.getGmail());
            userStmt.setString(2, learner.getPhone());
            userStmt.setString(3, learner.getFullName());
            userStmt.setInt(4, learner.getUserID());
            userStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false;
    }

    public boolean updateHonourByLearnerId(int learnerId, Honour honour) throws SQLException {
        String query = "UPDATE Honour SET honour_img = ?, honourName = ?, honour_type = ? " +
                "WHERE honourID = (SELECT honourID FROM Learner WHERE learnerID = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, honour.getHonourImg());
            stmt.setString(2, honour.getHonourName());
            stmt.setString(3, honour.getHonourType());
            stmt.setInt(4, learnerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false;
    }

    public boolean updateRewardByLearnerId(int learnerId, Reward reward) throws SQLException {
        String query = "UPDATE Reward SET icon = ?, rewardName = ?, dateCreated = ? " +
                "WHERE rewardID = (SELECT rewardID FROM Reward WHERE learnerID = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reward.getIcon());
            stmt.setString(2, reward.getRewardName());
            stmt.setDate(3, new java.sql.Date(reward.getDateCreated().getTime()));
            stmt.setInt(4, learnerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false;
    }

    public boolean updateVipByLearnerId(int learnerId, Vip vip) throws SQLException {
        String query = "UPDATE Vip SET creatAt = ?, endDate = ?, status = ?, vipType = ? WHERE learnerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(vip.getDateCreated().getTime()));
            stmt.setDate(2, new java.sql.Date(vip.getEndDate().getTime()));
            stmt.setString(3, vip.getStatus());
            stmt.setString(4, vip.getVipType());
            stmt.setInt(5, learnerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false;
    }
    public boolean deleteLearner(int learnerId) throws SQLException {
        String query = "UPDATE Learner SET  status = 'deleted' WHERE learnerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, learnerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false;
    }

    private Learner mapResultSetToLearner(ResultSet rs) throws SQLException {
        Learner learner = new Learner();
        learner.setUserID(rs.getInt("userID"));
        learner.setUsername(rs.getString("username"));
        learner.setPassword(rs.getString("password"));
        learner.setGmail(rs.getString("gmail"));
        learner.setPhone(rs.getString("phone"));
        learner.setRole(rs.getString("role"));
        learner.setStatus(rs.getString("status"));
        learner.setFullName(rs.getString("fullName"));
        learner.setSocialID(rs.getString("socialID"));
        learner.setDateCreate(new Date(rs.getDate("dateCreate").getTime()));
        learner.setGender(rs.getString("gender"));
        learner.setAge(rs.getInt("age"));
        learner.setHankyoPoint(rs.getDouble("hankyoPoint"));

        int honourID = rs.getInt("honourID");
        if (!rs.wasNull()) {
            learner.setHonour(new Honour(honourID, rs.getString("honour_img"), rs.getString("honourName"), rs.getString("honourType")));
        }

        int rewardID = rs.getInt("rewardID");
        if (!rs.wasNull()) {
            learner.setReward(new Reward(rewardID, rs.getString("rewardName"), rs.getString("icon"), rs.getDate("dateCreated")));
        }

        int vipID = rs.getInt("vipID");
        if (!rs.wasNull()) {
            learner.setVip(new Vip(rs.getDate("createAt"), rs.getDate("endDate"), rs.getString("status"), vipID, rs.getString("vipType")));
        }

        learner.setLearnerID(rs.getInt("learnerID"));
        return learner;
    }

}
