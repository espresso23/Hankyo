package dao;

import model.ExpertRegister;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpertRegisterDAO {
    private Connection getConnection() throws SQLException {
        return DBConnect.getInstance().getConnection();
    }

    public boolean createExpertRegister(ExpertRegister register) throws SQLException {
        String sql = "INSERT INTO ExpertRegister (username, password, gmail, phone, role, status, fullName, dateCreate, gender, avatar, certificate, cccd, cccdFront, cccdBack, approveStatus) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, register.getUsername());
            stmt.setString(2, register.getPassword());
            stmt.setString(3, register.getGmail());
            stmt.setString(4, register.getPhone());
            stmt.setString(5, "expert"); // Mặc định role là expert
            stmt.setString(6, "pending"); // Mặc định status là pending
            stmt.setString(7, register.getFullName());
            stmt.setDate(8, new Date(System.currentTimeMillis())); // Ngày hiện tại
            stmt.setString(9, register.getGender());
            stmt.setString(10, register.getAvatar());
            stmt.setString(11, register.getCertificate());
            stmt.setString(12, register.getCccd());
            stmt.setString(13, register.getCccdFront());
            stmt.setString(14, register.getCccdBack());
            stmt.setString(15, "waiting"); // Mặc định approveStatus là waiting
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        register.setRegisterID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<ExpertRegister> getAllRegistrations() throws SQLException {
        List<ExpertRegister> registrations = new ArrayList<>();
        String sql = "SELECT * FROM ExpertRegister ORDER BY dateCreate DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ExpertRegister register = new ExpertRegister();
                register.setRegisterID(rs.getInt("registerID"));
                register.setUsername(rs.getString("username"));
                register.setPassword(rs.getString("password"));
                register.setGmail(rs.getString("gmail"));
                register.setPhone(rs.getString("phone"));
                register.setRole(rs.getString("role"));
                register.setStatus(rs.getString("status"));
                register.setFullName(rs.getString("fullName"));
                register.setDateCreate(rs.getDate("dateCreate"));
                register.setGender(rs.getString("gender"));
                register.setAvatar(rs.getString("avatar"));
                register.setCertificate(rs.getString("certificate"));
                register.setCccd(rs.getString("cccd"));
                register.setCccdFront(rs.getString("cccdFront"));
                register.setCccdBack(rs.getString("cccdBack"));
                register.setApproveStatus(rs.getString("approveStatus"));
                registrations.add(register);
            }
        }
        return registrations;
    }

    public List<ExpertRegister> getRegistrationsByStatus(String status) throws SQLException {
        List<ExpertRegister> registrations = new ArrayList<>();
        String sql = "SELECT * FROM ExpertRegister WHERE status = ? ORDER BY dateCreate DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExpertRegister register = new ExpertRegister();
                    register.setRegisterID(rs.getInt("registerID"));
                    register.setUsername(rs.getString("username"));
                    register.setPassword(rs.getString("password"));
                    register.setGmail(rs.getString("gmail"));
                    register.setPhone(rs.getString("phone"));
                    register.setRole(rs.getString("role"));
                    register.setStatus(rs.getString("status"));
                    register.setFullName(rs.getString("fullName"));
                    register.setDateCreate(rs.getDate("dateCreate"));
                    register.setGender(rs.getString("gender"));
                    register.setAvatar(rs.getString("avatar"));
                    register.setCertificate(rs.getString("certificate"));
                    register.setCccd(rs.getString("cccd"));
                    register.setCccdFront(rs.getString("cccdFront"));
                    register.setCccdBack(rs.getString("cccdBack"));
                    register.setApproveStatus(rs.getString("approveStatus"));
                    registrations.add(register);
                }
            }
        }
        return registrations;
    }

    public boolean updateRegistrationStatus(int registerID, String status) throws SQLException {
        String sql = "UPDATE ExpertRegister SET status = ? WHERE registerID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, registerID);
            return stmt.executeUpdate() > 0;
        }
    }

    public ExpertRegister getRegistrationByID(int registerID) throws SQLException {
        String sql = "SELECT * FROM ExpertRegister WHERE registerID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, registerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ExpertRegister register = new ExpertRegister();
                    register.setRegisterID(rs.getInt("registerID"));
                    register.setUsername(rs.getString("username"));
                    register.setPassword(rs.getString("password"));
                    register.setGmail(rs.getString("gmail"));
                    register.setPhone(rs.getString("phone"));
                    register.setRole(rs.getString("role"));
                    register.setStatus(rs.getString("status"));
                    register.setFullName(rs.getString("fullName"));
                    register.setDateCreate(rs.getDate("dateCreate"));
                    register.setGender(rs.getString("gender"));
                    register.setAvatar(rs.getString("avatar"));
                    register.setCertificate(rs.getString("certificate"));
                    register.setCccd(rs.getString("cccd"));
                    register.setCccdFront(rs.getString("cccdFront"));
                    register.setCccdBack(rs.getString("cccdBack"));
                    register.setApproveStatus(rs.getString("approveStatus"));
                    return register;
                }
            }
        }
        return null;
    }

    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ExpertRegister WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isGmailExists(String gmail) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ExpertRegister WHERE gmail = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
} 