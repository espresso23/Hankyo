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
    public boolean equipHonour(int learnerID, int honourID) {
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

        try (Connection conn = DBConnect.getInstance().getConnection()) {
            // Bắt đầu transaction để đảm bảo tính nhất quán
            conn.setAutoCommit(false);
            try {
                // Xóa thành tựu đang được trang bị (nếu có)
                String deleteQuery = "DELETE FROM IsEquippedHonour WHERE learnerID = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, learnerID);
                    deleteStmt.executeUpdate();
                }

                // Thêm thành tựu mới vào IsEquippedHonour
                String insertQuery = "INSERT INTO IsEquippedHonour (learnerID, honourID) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, learnerID);
                    insertStmt.setInt(2, honourID);
                    insertStmt.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error in equipHonour: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Bỏ trang bị thành tựu
    public boolean unequipHonour(int learnerID) {
        String query = "DELETE FROM IsEquippedHonour WHERE learnerID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error in unequipHonour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy ID của thành tựu đang được trang bị
    public Integer getEquippedHonourID(int learnerID) {
        String query = "SELECT honourID FROM IsEquippedHonour WHERE learnerID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("honourID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getEquippedHonourID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}