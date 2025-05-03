package dao;

import model.HonourOwned;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HonourOwnedDAO {

    public HonourOwnedDAO() {
        // Constructor rỗng
    }

    // Kiểm tra xem learner đã sở hữu honour này chưa
    public boolean hasHonour(int learnerID, int honourID) {
        String query = "SELECT COUNT(*) FROM Honour_Owned WHERE learnerID = ? AND honourID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            ps.setInt(2, honourID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in hasHonour: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra xem bản ghi có tồn tại trong Honour_Owned không
    private boolean honourOwnedExists(int learnerID, int honourID) {
        return hasHonour(learnerID, honourID);
    }

    // Thêm mới bản ghi vào Honour_Owned
    private void insertHonourOwned(int learnerID, int honourID) {
        String query = "INSERT INTO Honour_Owned (honourID, learnerID, dateAdded) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, honourID);
            ps.setInt(2, learnerID);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into Honour_Owned: " + e.getMessage(), e);
        }
    }

    // Thêm mới bản ghi vào Honour_Owned từ đối tượng HonourOwned
    public void addHonour(HonourOwned honourOwned) {
        String query = "INSERT INTO Honour_Owned (honourID, learnerID, dateAdded) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, honourOwned.getHonour().getHonourID());
            stmt.setInt(2, honourOwned.getLearner().getLearnerID());
            stmt.setDate(3, new java.sql.Date(honourOwned.getDateAdded().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in addHonour for honourID " + honourOwned.getHonour().getHonourID() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Trang bị một thành tựu
    public boolean equipHonour(int learnerID, int honourID, int userID) {
        // Kiểm tra xem learner đã sở hữu honour này chưa
        if (!honourOwnedExists(learnerID, honourID)) {
            // Nếu chưa sở hữu, thêm vào Honour_Owned trước
            insertHonourOwned(learnerID, honourID);
        }

        // Kiểm tra xem honourID có tồn tại trong bảng Honour không
        HonourDAO honourDAO = new HonourDAO();
        if (!honourDAO.isHonourExists(honourID)) {
            System.err.println("HonourID " + honourID + " does not exist in Honour table");
            return false;
        }

        // Cập nhật equippedHonourID trong bảng User
        String query = "UPDATE [User] SET equippedHonourID = ? WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, honourID);
            ps.setInt(2, userID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error in equipHonour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Bỏ trang bị thành tựu
    public boolean unequipHonour(int userID) {
        String query = "UPDATE [User] SET equippedHonourID = NULL WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error in unequipHonour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy ID của thành tựu đang được trang bị
    public Integer getEquippedHonourID(int userID) {
        String query = "SELECT equippedHonourID FROM [User] WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("equippedHonourID") != -1 ? rs.getInt("equippedHonourID") : null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getEquippedHonourID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}