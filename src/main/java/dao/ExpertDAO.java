package dao;

import model.Expert;
import model.ExpertBank;
import util.DBConnect;
import util.Encrypt;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ExpertDAO {
    private Connection connection;

    public ExpertDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public boolean createExpert(Expert expert) throws SQLException {
        String insertUserQuery = "INSERT INTO [User] (username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertLearnerQuery = "INSERT INTO Expert (userID, certificate, CCCD) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Mã hóa password trước khi lưu
            expert.setPassword(Encrypt.hashPassword(expert.getPassword()));

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
                userStmt.setString(11, expert.getAvatar());
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    expert.setUserID(generatedKeys.getInt(1));
                }
            }

            // Insert into Expert table
            try (PreparedStatement expertStmt = connection.prepareStatement(insertLearnerQuery)) {
                expertStmt.setInt(1, expert.getUserID());
                expertStmt.setString(2, expert.getCertificate());
                expertStmt.setString(3, expert.getCCCD());
                expertStmt.executeUpdate();
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
        expert.setCCCD(rs.getString("CCCD") != null ? rs.getString("CCCD") : "");

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

    public boolean updateExpert(Expert expert) throws SQLException {
        String updateUserQuery = "UPDATE [User] SET username=?, password=?, gmail=?, phone=?, role=?, status=?, fullName=?, socialID=?, gender=?, avatar=? WHERE userID=?";
        String updateExpertQuery = "UPDATE Expert SET certificate=?, honour_ownedID=?, CCCD=? WHERE expertID=?";

        try {
            connection.setAutoCommit(false);

            // Mã hóa password trước khi cập nhật
            expert.setPassword(Encrypt.hashPassword(expert.getPassword()));

            // Update User table
            try (PreparedStatement userStmt = connection.prepareStatement(updateUserQuery)) {
                userStmt.setString(1, expert.getUsername());
                userStmt.setString(2, expert.getPassword());
                userStmt.setString(3, expert.getGmail());
                userStmt.setString(4, expert.getPhone());
                userStmt.setString(5, expert.getRole());
                userStmt.setString(6, expert.getStatus());
                userStmt.setString(7, expert.getFullName());
                userStmt.setString(8, expert.getSocialID());
                userStmt.setString(9, expert.getGender());
                userStmt.setString(10, expert.getAvatar());
                userStmt.setInt(11, expert.getUserID());
                userStmt.executeUpdate();
            }

            // Update Expert table
            try (PreparedStatement expertStmt = connection.prepareStatement(updateExpertQuery)) {
                expertStmt.setString(1, expert.getCertificate());
                expertStmt.setInt(2, expert.getHonour_ownedID());
                expertStmt.setString(3, expert.getCCCD());
                expertStmt.setInt(4, expert.getExpertID());
                expertStmt.executeUpdate();
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

    public boolean deleteExpert(int expertID) throws SQLException {
        String deleteExpertQuery = "DELETE FROM Expert WHERE expertID=?";
        String deleteUserQuery = "DELETE FROM [User] WHERE userID=(SELECT userID FROM Expert WHERE expertID=?)";

        try {
            connection.setAutoCommit(false);

            // Delete from Expert table first
            try (PreparedStatement expertStmt = connection.prepareStatement(deleteExpertQuery)) {
                expertStmt.setInt(1, expertID);
                expertStmt.executeUpdate();
            }

            // Delete from User table
            try (PreparedStatement userStmt = connection.prepareStatement(deleteUserQuery)) {
                userStmt.setInt(1, expertID);
                userStmt.executeUpdate();
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

    public List<Expert> getAllExperts() throws SQLException {
        List<Expert> experts = new ArrayList<>();
        String query = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                experts.add(mapResultSetToExpert(rs));
            }
        }
        return experts;
    }

    public List<Expert> searchExpertsByName(String name) throws SQLException {
        List<Expert> experts = new ArrayList<>();
        String query = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE u.fullName LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                experts.add(mapResultSetToExpert(rs));
            }
        }
        return experts;
    }

    public boolean updateExpertStatus(int expertID, String status) throws SQLException {
        String query = "UPDATE [User] SET status = ? WHERE userID = (SELECT userID FROM Expert WHERE expertID = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, expertID);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Expert> getExpertsByStatus(String status) throws SQLException {
        List<Expert> experts = new ArrayList<>();
        String query = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE u.status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                experts.add(mapResultSetToExpert(rs));
            }
        }
        return experts;
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

    /**
     * Chuyển đổi từ ExpertRegister sang Expert để phục vụ duyệt đơn đăng ký expert
     */
    public static model.Expert convertFromExpertRegister(model.ExpertRegister reg) {
        model.Expert expert = new model.Expert();
        expert.setUsername(reg.getUsername());
        expert.setPassword(reg.getPassword());
        expert.setGmail(reg.getGmail());
        expert.setPhone(reg.getPhone());
        expert.setRole(reg.getRole());
        expert.setStatus("active"); // hoặc "pending" tùy logic
        expert.setFullName(reg.getFullName());
        expert.setDateCreate(reg.getDateCreate());
        expert.setGender(reg.getGender());
        expert.setAvatar(reg.getAvatar());
        expert.setCertificate(reg.getCertificate());
        expert.setCCCD(reg.getCccd());
        // Nếu có các trường khác cần chuyển, bổ sung tại đây
        return expert;
    }
}
