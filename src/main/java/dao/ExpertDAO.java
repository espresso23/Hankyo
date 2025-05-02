package dao;

import model.Expert;
import model.ExpertBank;
import util.DBConnect;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ExpertDAO {
    private Connection connection;

    public ExpertDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public boolean createExpert(Expert expert) throws SQLException {
        String insertUserQuery = "INSERT INTO [User] (username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertLearnerQuery = "INSERT INTO Expert (userID, certificate, honour_ownedID) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Insert into Users table
            try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, expert.getUsername());
                userStmt.setString(2, expert.getPassword());
                userStmt.setString(3, expert.getGmail());
                userStmt.setString(4, expert.getPhone());
                userStmt.setString(5, expert.getRole());
                userStmt.setString(6, expert.getStatus());
                userStmt.setString(7, expert.getFullName());
                userStmt.setString(8, expert.getSocialID());
                userStmt.setDate(9, new java.sql.Date(expert.getDateCreate().getTime()));
                userStmt.setString(10, expert.getGender());
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    expert.setUserID(generatedKeys.getInt(1));
                }
            }

            // Insert into Learners table
            try (PreparedStatement learnerStmt = connection.prepareStatement(insertLearnerQuery)) {
                learnerStmt.setInt(1, expert.getUserID());
                learnerStmt.setString(2, expert.getCertificate());
                learnerStmt.setInt(3, Types.NULL);
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
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private Expert mapResultSetToExpert(ResultSet rs) throws SQLException {
        Expert expert = new Expert();

        expert.setUserID(rs.getInt("userID"));
        expert.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
        expert.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
        expert.setGmail(rs.getString("gmail") != null ? rs.getString("gmail") : "");
        expert.setPhone(rs.getString("phone") != null ? rs.getString("phone") : "");
        expert.setRole(rs.getString("role") != null ? rs.getString("role") : "");
        expert.setStatus(rs.getString("status") != null ? rs.getString("status") : "");
        expert.setFullName(rs.getString("fullName") != null ? rs.getString("fullName") : "");
        expert.setSocialID(rs.getString("socialID") != null ? rs.getString("socialID") : "");

        if (rs.getDate("dateCreate") != null) {
            expert.setDateCreate(new Date(rs.getDate("dateCreate").getTime()));
        } else {
            expert.setDateCreate(null);
        }

        expert.setGender(rs.getString("gender") != null ? rs.getString("gender") : "");
        expert.setCertificate(rs.getString("certificate") != null ? rs.getString("certificate") : "");

        int honourOwnedID = rs.getInt("honour_ownedID");
        if (rs.wasNull()) {
            honourOwnedID = 0;
        }
        expert.setHonour_ownedID(honourOwnedID);

        expert.setExpertID(rs.getInt("expertID"));

        return expert;
    }

    public Expert getExpertById(int expertID) throws SQLException {
        String selectExpertQuery = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE e.expertID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectExpertQuery)) {
            stmt.setInt(1, expertID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToExpert(rs);
            }
        }
        return null;
    }

    public Expert getExpertByUserID(int userID) {
        String sql = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE e.userID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToExpert(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNewBankAccount(ExpertBank expertBank) {
        String sql = "Insert into ExpertBankAccount(bankAccount,binCode,bankName,expertID) values(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, expertBank.getBankAccount());
            preparedStatement.setString(2, expertBank.getBinCode());
            preparedStatement.setString(3, expertBank.getBankName());
            preparedStatement.setInt(4, expertBank.getExpertID());
            return preparedStatement.executeUpdate() > 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Expert> getExpertsByQuery(String query) throws SQLException {
        List<Expert> experts = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Expert expert = new Expert();
                expert.setExpertID(rs.getInt("expertID"));
                expert.setUserID(rs.getInt("userID"));
                expert.setCertificate(rs.getString("certificate"));
                expert.setStatus(rs.getString("status"));
                
                // Thông tin từ bảng User
                expert.setFullName(rs.getString("fullName"));
                expert.setGmail(rs.getString("gmail"));
                expert.setAvatar(rs.getString("avatar"));
                expert.setRole(rs.getString("role"));
                
                experts.add(expert);
            }
        }
        return experts;
    }
}
