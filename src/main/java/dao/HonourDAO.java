package dao;

import model.Honour;
import model.Login;
import model.User;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HonourDAO {
    public HonourDAO() {
        // No connection stored
    }

    public List<Honour> getAllHonours() {
        String query = "SELECT honourID, honourName, honour_img, honour_type, title_color, gradient_start, gradient_end FROM Honour";
        List<Honour> honours = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Honour h = new Honour(
                        resultSet.getInt("honourID"),
                        resultSet.getString("honourName"),
                        resultSet.getString("honour_img"),
                        resultSet.getString("honour_type")
                );
                h.setTitleColor(resultSet.getString("title_color"));
                h.setGradientStart(resultSet.getString("gradient_start"));
                h.setGradientEnd(resultSet.getString("gradient_end"));
                honours.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách honour: " + e.getMessage(), e);
        }
        return honours;
    }

    public Set<String> getUniqueTypes() {
        List<Honour> honours = getAllHonours();
        return honours.stream()
                .map(Honour::getHonourType)
                .collect(Collectors.toSet());
    }

    public void addHonourIntoHonourOwned(int honourID, int learnerID) {
        if (!isHonourExists(honourID)) {
            throw new IllegalArgumentException("Invalid honourID: " + honourID + " does not exist in Honour table.");
        }

        if (!isLearnerExists(learnerID)) {
            throw new IllegalArgumentException("Invalid learnerID: " + learnerID + " does not exist in Learner table.");
        }

        String query = "INSERT INTO Honour_Owned (honourID, learnerID, dateAdded) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, honourID);
            ps.setInt(2, learnerID);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into HonourOwned: " + e.getMessage(), e);
        }
    }

    boolean isHonourExists(int honourID) {
        String query = "SELECT COUNT(*) FROM Honour WHERE honourID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, honourID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking Honour existence: " + e.getMessage(), e);
        }
        return false;
    }

    private boolean isLearnerExists(int learnerID) {
        String query = "SELECT COUNT(*) FROM Learner WHERE learnerID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking Learner existence: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean if100Comments(int userID) {
        if (userID <= 0) {
            System.err.println("Invalid userID: " + userID);
            return false;
        }
        String query = "SELECT COUNT(*) FROM Comment WHERE UserID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Comment count for userID " + userID + ": " + count);
                    return count >= 100;
                } else {
                    System.err.println("No result for Comment count query, userID: " + userID);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in if100Comments for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean if10UpVote(int userID) {
        if (userID <= 0) {
            System.err.println("Invalid userID: " + userID);
            return false;
        }
        String query = "SELECT COUNT(*) FROM Post WHERE UserID = ? AND ScorePost >= 10";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Posts with 10+ upvotes for userID " + userID + ": " + count);
                    return count > 0;
                } else {
                    System.err.println("No result for Post count query, userID: " + userID);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in if10UpVote for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean if10CustomFlashCard(int learnerID) {
        if (learnerID <= 0) {
            System.err.println("Invalid learnerID: " + learnerID);
            return false;
        }
        String query = "SELECT COUNT(*) FROM CustomFlashCard WHERE learnerID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Flashcard count for learnerID " + learnerID + ": " + count);
                    return count >= 0;
                } else {
                    System.err.println("No result for CustomFlashCard count query, learnerID: " + learnerID);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in if10CustomFlashCard for learnerID " + learnerID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Honour getHonourById(int honourID) {
        String sql = "SELECT honourID, honourName, honour_img, honour_type, title_color, gradient_start, gradient_end " +
                "FROM Honour WHERE honourID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, honourID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Honour honour = new Honour();
                    honour.setHonourID(rs.getInt("honourID"));
                    honour.setHonourName(rs.getString("honourName"));
                    honour.setHonourImg(rs.getString("honour_img"));
                    honour.setHonourType(rs.getString("honour_type"));
                    honour.setTitleColor(rs.getString("title_color"));
                    honour.setGradientStart(rs.getString("gradient_start"));
                    honour.setGradientEnd(rs.getString("gradient_end"));
                    return honour;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getHonourById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Integer getUserIDFromLearnerID(int learnerID) {
        String query = "SELECT userID FROM Learner WHERE learnerID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mapping learnerID " + learnerID + " to userID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Login getLoginByUserID(int userID) {
        String query = "SELECT LoginID, UserID, lastDateLogin, loginDays FROM Login WHERE UserID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Login login = new Login();
                    login.setLoginID(rs.getInt("LoginID"));
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    login.setUser(user);
                    login.setLastDateLogin(rs.getDate("lastDateLogin"));
                    login.setLoginDays(rs.getInt("loginDays"));
                    return login;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getLoginByUserID for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updateLoginDays(int userID, int loginDays, Date lastDateLogin) {
        String query = "UPDATE Login SET loginDays = ?, lastDateLogin = ? WHERE UserID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, loginDays);
            ps.setDate(2, new java.sql.Date(lastDateLogin.getTime()));
            ps.setInt(3, userID);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.err.println("No Login record found for userID " + userID);
            }
        } catch (SQLException e) {
            System.err.println("Error in updateLoginDays for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createLogin(int userID) {
        String query = "INSERT INTO Login (UserID, lastDateLogin, loginDays) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            ps.setDate(2, new java.sql.Date(new Date().getTime()));
            ps.setInt(3, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in createLogin for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Date getUserDateCreate(int userID) {
        String query = "SELECT dateCreate FROM [User] WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("dateCreate");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching dateCreate for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}