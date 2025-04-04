package dao;

import model.Learner;
import model.User;
import util.DBConnect;
import util.Encrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAO {
    public Learner getLearnerByUserID(int UserID) throws SQLException {
        String query = "SELECT * FROM Learner WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, UserID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Learner learner = new Learner();
                    learner.setUserID(rs.getInt("userID"));
                    learner.setLearnerID(rs.getInt("learnerID"));
                    return learner;
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching learner: " + e.getMessage());
        }
        return null;
    }

    public String register(User user) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Kiểm tra email đã tồn tại chưa
            try (PreparedStatement checkEmailSt = conn.prepareStatement("SELECT COUNT(*) FROM [User] WHERE gmail = ?")) {
                checkEmailSt.setString(1, user.getGmail());
                try (ResultSet rs = checkEmailSt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "Email already exists.";
                    }
                }
            }

            // Mã hóa mật khẩu
            user.setPassword(Encrypt.hashPassword(user.getPassword()));

            // Thêm user mới
            String insertUserQuery = "INSERT INTO [User] (username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, user.getUsername());
                userStmt.setString(2, user.getPassword());
                userStmt.setString(3, user.getGmail());
                userStmt.setString(4, user.getPhone());
                userStmt.setString(5, "Learner");
                userStmt.setString(6, "Normal");
                userStmt.setString(7, user.getFullName());
                userStmt.setString(8, user.getSocialID());
                userStmt.setDate(9, new java.sql.Date(user.getDateCreate().getTime()));
                userStmt.setString(10, user.getGender());

                if (userStmt.executeUpdate() == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userID = generatedKeys.getInt(1);

                        // Thêm thông tin vào bảng Learner
                        String insertLearnerQuery = "INSERT INTO Learner (userID, hankyoPoint, honour_ownedID) VALUES (?, ?, ?)";
                        try (PreparedStatement learnerStmt = conn.prepareStatement(insertLearnerQuery)) {
                            learnerStmt.setInt(1, userID);
                            learnerStmt.setDouble(2, 0.0);
                            learnerStmt.setNull(3, Types.INTEGER);
                            learnerStmt.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            conn.commit();
            return "Registration Successful.";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Đóng kết nối sau khi hoàn tất
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
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

    public boolean userExistsSocial(String socialID) throws SQLException {
        String query = "SELECT COUNT(*) FROM [User] WHERE socialID = ?";
        try (Connection con = DBConnect.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, socialID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveUserSocialMedia(User user) {
        String checkEmailQuery = "SELECT COUNT(*) FROM [User] WHERE gmail = ?";
        String saveUserQuery = "INSERT INTO [User] (fullName, gmail, socialID, role, dateCreate, avatar, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertLearnerQuery = "INSERT INTO Learner (userID, hankyoPoint, honour_ownedID) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = DBConnect.getInstance().getConnection();
            con.setAutoCommit(false); // Bắt đầu transaction

            // Kiểm tra xem email đã tồn tại chưa
            try (PreparedStatement checkEmailStmt = con.prepareStatement(checkEmailQuery)) {
                checkEmailStmt.setString(1, user.getGmail());
                try (ResultSet rs = checkEmailStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // Email đã tồn tại
                    }
                }
            }

            // Thêm user vào bảng User
            int userID;
            try (PreparedStatement ps = con.prepareStatement(saveUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getFullName());
                ps.setString(2, user.getGmail());
                ps.setString(3, user.getSocialID());
                ps.setString(4, "Learner");
                ps.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
                ps.setString(6, user.getAvatar());
                ps.setString(7, "active");
                int rowAffected = ps.executeUpdate();

                if (rowAffected == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // Thêm user vào bảng Learner
            try (PreparedStatement learnerStmt = con.prepareStatement(insertLearnerQuery)) {
                learnerStmt.setInt(1, userID);
                learnerStmt.setDouble(2, 0.0);
                learnerStmt.setNull(3, Types.INTEGER);
                learnerStmt.executeUpdate();
            }

            con.commit(); // Xác nhận transaction
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Hoàn tác nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close(); // Đóng kết nối
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }


    public User getUserBySocialID(String socialID) throws SQLException {
        String query = "SELECT * FROM [User] WHERE socialID = ?";
        try (Connection con = DBConnect.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, socialID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setSocialID(rs.getString("socialID"));
                user.setFullName(rs.getString("fullName"));
                user.setGmail(rs.getString("gmail"));
                user.setRole(rs.getString("role"));
                // Add other fields if necessary
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUserBannedSocial(String socialID) {
        String sql = "SELECT status FROM [User] WHERE socialID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, socialID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status").equals("banned") ? true : false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // User not found or not banned
    }

    public boolean login(String Username, String Password) {
        boolean result = false;
        try {
            String hashedPassword = Encrypt.hashPassword(Password);
            Connection conn = DBConnect.getInstance().getConnection();  // Updated to use DatabaseConnect
            PreparedStatement st = conn.prepareStatement("SELECT * FROM [User] WHERE username=? AND password=?");
            st.setString(1, Username);
            st.setString(2, hashedPassword);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return result;
    }

    public User getUserByUserName(String Username) throws SQLException {
        User u = new User();
        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM [User] WHERE username = ?");
            st.setString(1, Username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                u.setUserID(rs.getInt("userID"));
                u.setUsername(rs.getString("Username"));
                u.setPassword(rs.getString("Password"));
                u.setGmail(rs.getString("Gmail"));
                u.setPhone(rs.getString("Phone"));
                u.setRole(rs.getString("role"));
                u.setStatus(rs.getString("status"));
                u.setFullName(rs.getString("fullName"));
                u.setDateCreate(rs.getDate("dateCreate"));
                u.setGender(rs.getString("gender"));
                return u;
            }

        } catch (Exception e) {
            System.out.println("Action Failed: " + e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM [User] WHERE username = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public User getUserByEmail(String gmail) throws SQLException {
        User user = null;
        String query = "SELECT * FROM [User] WHERE gmail = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, gmail);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setGmail(rs.getString("gmail"));  // Đồng bộ với trường `gmail`
                    user.setPhone(rs.getString("phone"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setFullName(rs.getString("fullName"));
                    user.setDateCreate(rs.getDate("dateCreate"));
                    user.setGender(rs.getString("gender"));
                }
            }
        } catch (Exception e) {
            System.out.println("Action Failed: " + e.getMessage());
        }
        return user;
    }

    public void updatePassword(User user, String newPassword) throws SQLException {
        String hashPass = util.Encrypt.hashPassword(newPassword);
        String sql = "UPDATE [User] SET password = ? WHERE userID = ?";
        try (Connection con = DBConnect.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hashPass);
            ps.setInt(2, user.getUserID());
            ps.executeUpdate();
        }
    }

    public  User getUserByID(int userID) throws SQLException {
        User user = null;
        String query = "SELECT * FROM [User] WHERE userID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, userID);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setGmail(rs.getString("gmail"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setFullName(rs.getString("fullName"));
                    user.setDateCreate(rs.getDate("dateCreate"));
                    user.setGender(rs.getString("gender"));
                    user.setDateOfBirth(rs.getDate("dateOfBirth"));
                    user.setAvatar(rs.getString("avatar"));

                }
            }
        } catch (Exception e) {
            System.out.println("Action Failed: " + e.getMessage());
        }
        return user;
    }
    public boolean verifyPassword(User user, String oldPassword) {
        try {
            String hashedOldPassword = Encrypt.hashPassword(oldPassword); // Mã hóa mật khẩu cũ
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM [USER] WHERE userID=? AND password=?");
            st.setInt(1, user.getUserID());
            st.setString(2, hashedOldPassword);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true; // Mật khẩu cũ đúng
            }
        } catch (SQLException e) {
            System.out.println("Error verifying old password: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false; // Mật khẩu cũ sai
    }
    public void updateUserProfile(User user) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE [User] SET ");
        List<Object> params = new ArrayList<>();
        if (user.getPhone() != null) {
            sql.append("phone = ?, ");
            params.add(user.getPhone());
        }
        if (user.getAvatar() != null) {
            sql.append("avatar = ?, ");
            params.add(user.getAvatar());
        }
        if(user.getDateOfBirth() !=null){
            sql.append("dateOfBirth = ?, ");
            params.add("dateOfBirth = ? ,");
        }
        if(user.getGender() != null){
            sql.append("gender = ?, ");
            params.add("gender = ?, ");
        }
        if (user.getFullName() != null){
            sql.append("fullName = ?, ");
            params.add("fullName = ?, ");
        }

        // Remove the last comma and space
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE userID = ?");
        params.add(user.getUserID());

        try (Connection con = DBConnect.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
        }
    }

}
