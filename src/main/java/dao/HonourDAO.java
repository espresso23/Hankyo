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
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            statement = conn.prepareStatement(query);
            resultSet = statement.executeQuery();

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
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
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
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, honourID);
            ps.setInt(2, learnerID);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into HonourOwned: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    boolean isHonourExists(int honourID) {
        String query = "SELECT COUNT(*) FROM Honour WHERE honourID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, honourID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking Honour existence: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }

    private boolean isLearnerExists(int learnerID) {
        String query = "SELECT COUNT(*) FROM Learner WHERE learnerID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, learnerID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking Learner existence: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean if100Comments(int userID) {
        if (userID <= 0) {
            System.err.println("Invalid userID: " + userID);
            return false;
        }
        String query = "SELECT COUNT(*) FROM Comment WHERE UserID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Comment count for userID " + userID + ": " + count);
                return count >= 100;
            } else {
                System.err.println("No result for Comment count query, userID: " + userID);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error in if100Comments for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean if10UpVote(int userID) {
        if (userID <= 0) {
            System.err.println("Invalid userID: " + userID);
            return false;
        }
        String query = "SELECT COUNT(*) FROM Post WHERE UserID = ? AND ScorePost >= 10";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Posts with 10+ upvotes for userID " + userID + ": " + count);
                return count > 0;
            } else {
                System.err.println("No result for Post count query, userID: " + userID);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error in if10UpVote for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean if10CustomFlashCard(int learnerID) {
        if (learnerID <= 0) {
            System.err.println("Invalid learnerID: " + learnerID);
            return false;
        }
        String query = "SELECT COUNT(*) FROM CustomFlashCard WHERE learnerID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, learnerID);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Flashcard count for learnerID " + learnerID + ": " + count);
                return count >= 0;
            } else {
                System.err.println("No result for CustomFlashCard count query, learnerID: " + learnerID);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error in if10CustomFlashCard for learnerID " + learnerID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public Honour getHonourById(int honourID) {
        String sql = "SELECT honourID, honourName, honour_img, honour_type, title_color, gradient_start, gradient_end " +
                "FROM Honour WHERE honourID = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, honourID);
            rs = stmt.executeQuery();
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
        } catch (SQLException e) {
            System.err.println("Error in getHonourById: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    public Integer getUserIDFromLearnerID(int learnerID) {
        String query = "SELECT userID FROM Learner WHERE learnerID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, learnerID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        } catch (SQLException e) {
            System.err.println("Error mapping learnerID " + learnerID + " to userID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    public Login getLoginByUserID(int userID) {
        String query = "SELECT LoginID, UserID, lastDateLogin, loginDays FROM Login WHERE UserID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            rs = ps.executeQuery();
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
        } catch (SQLException e) {
            System.err.println("Error in getLoginByUserID for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    public void updateLoginDays(int userID, int loginDays, Date lastDateLogin) {
        String query = "UPDATE Login SET loginDays = ?, lastDateLogin = ? WHERE UserID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, loginDays);
            ps.setDate(2, new java.sql.Date(lastDateLogin.getTime()));
            ps.setInt(3, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in updateLoginDays for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void createLogin(int userID) {
        String query = "INSERT INTO Login (UserID, lastDateLogin, loginDays) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            ps.setDate(2, new java.sql.Date(new Date().getTime()));
            ps.setInt(3, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in createLogin for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public Date getUserDateCreate(int userID) {
        String query = "SELECT dateCreate FROM [User] WHERE userID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDate("dateCreate");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching dateCreate for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }
}