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
    public Learner getLearnerByUserID(int userID) {
        String query = "SELECT l.*, u.*, r.*, v.*, h.* " +
                "FROM Learner l " +
                "JOIN [User] u ON l.userID = u.userID " +
                "LEFT JOIN Reward r ON l.learnerID = r.learnerID " +
                "LEFT JOIN VipDetails v ON l.learnerID = v.learnerID " +
                "LEFT JOIN Honour h ON l.honourID = h.honourID " +
                "WHERE u.userID = ? AND l.status = 'active'";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLearner(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    //UPDATE HONOUR of LEARNERS READ ALL HONOUR WHOSE BY LEARNERS AND CHANGE -> List ra danh sach danh huu dang so huu va chọn ra 1 danh hiệu sau đó thay đổi
    public boolean updateHonourByLearnerId(int learnerId, int newHonourOwnedId) throws SQLException {
        String updateQuery = "UPDATE Learner " +
                "SET honour_ownedID = ? " +
                "WHERE learnerID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, newHonourOwnedId);
            preparedStatement.setInt(2, learnerId);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        return false;
        } finally {
            connection.close();
        }
    }


    public boolean updateVipByLearnerId(int learnerId, Vip vip) throws SQLException {
        String query = "UPDATE Vip_User SET dateCreated = ?, endDate = ?, vipStatus = ? WHERE learnerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(vip.getDateCreated().getTime()));
            stmt.setDate(2, new java.sql.Date(vip.getEndDate().getTime()));
            stmt.setString(3, vip.getVipLearnerStatus());//expired || ongoing
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
        learner.setDateOfBirth(new Date(rs.getDate("age").getTime()));
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
