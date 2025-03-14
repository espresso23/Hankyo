package dao;

import model.User;
import util.Encrypt;
import util.DBConnect;

import java.sql.*;

public class UserDAO {
    public String Register(User user) throws Exception {
        Connection conn = null;
        PreparedStatement checkEmailSt = null;
        PreparedStatement userStmt = null;
        PreparedStatement learnerStmt = null;
        ResultSet rs = null;
        ResultSet generatedKeys = null;

        try {
            String hashPassword = Encrypt.hashPassword(user.getPassword());
            user.setPassword(hashPassword);

            conn = DBConnect.getInstance().getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            checkEmailSt = conn.prepareStatement("SELECT COUNT(*) FROM [User] WHERE gmail = ?");
            checkEmailSt.setString(1, user.getGmail());
            rs = checkEmailSt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "Email already exists.";
            }

            // Insert new user
            String insertUserQuery = "INSERT INTO [User] (username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String insertLearnerQuery = "INSERT INTO Learner (userID, hankyoPoint, honour_ownedID) VALUES (?, ?, ?)";

            conn.setAutoCommit(false);
            userStmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, user.getPassword());
            userStmt.setString(3, user.getGmail());
            userStmt.setString(4, user.getPhone());
            userStmt.setString(5, "Learner");
            userStmt.setString(6, "Normal");
            userStmt.setString(7, user.getFullName());
            userStmt.setString(8, user.getSocialID());
            userStmt.setDate(9, new Date(System.currentTimeMillis()));
            userStmt.setString(10, user.getGender());

            int affectedRows = userStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            generatedKeys = userStmt.getGeneratedKeys();
            int userID;
            if (generatedKeys.next()) {
                userID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            learnerStmt = conn.prepareStatement(insertLearnerQuery);
            learnerStmt.setInt(1, userID);
            learnerStmt.setDouble(2, 0.0);
            learnerStmt.setNull(3, Types.INTEGER);
            learnerStmt.executeUpdate();

            conn.commit();
            return "Registration Successful.";
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (rs != null) rs.close();
                if (checkEmailSt != null) checkEmailSt.close();
                if (userStmt != null) userStmt.close();
                if (learnerStmt != null) learnerStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    public boolean userExists(String gmail) {
        String query = "SELECT COUNT(*) FROM [User] WHERE gmail = ?";
        try (Connection con = DBConnect.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, gmail);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
