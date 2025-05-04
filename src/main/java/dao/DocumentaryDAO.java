package dao;

import model.Documentary;
import util.DBConnect;

import java.sql.*;
import java.util.*;

public class DocumentaryDAO {
    private DBConnect dbContext;

    public DocumentaryDAO() {
        dbContext = DBConnect.getInstance();
    }
    public boolean checkConnection() throws Exception {
        try (Connection conn = dbContext.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<String> getAllDistinctTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM Documentary WHERE type IS NOT NULL ORDER BY type ASC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }

    public List<Documentary> getAllDocuments() throws SQLException {
        List<Documentary> list = new ArrayList<>();
        String sql = "SELECT * FROM Documentary";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Documentary(
                        rs.getInt("docID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("source"),
                        rs.getString("doc_content"),
                        rs.getString("type"),
                        rs.getString("audioPath"),
                        rs.getString("thumbnail")
                ));
            }
        }
        return list;
    }

    public Documentary getDocumentById(int id) throws SQLException {
        String sql = "SELECT * FROM Documentary WHERE docID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Documentary(
                        rs.getInt("docID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("source"),
                        rs.getString("doc_content"),
                        rs.getString("type"),
                        rs.getString("audioPath"),
                        rs.getString("thumbnail")
                );
            }
        }
        return null;
    }
    public void insertDocument(Documentary doc) throws SQLException {
        String sql = "INSERT INTO Documentary (title, author, source, doc_content, type, audioPath, thumbnail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doc.getTitle());
            ps.setString(2, doc.getAuthor());
            ps.setString(3, doc.getSource());
            ps.setString(4, doc.getDocContent());
            ps.setString(5, doc.getType());
            ps.setString(6, doc.getAudioPath());
            ps.setString(7, doc.getThumbnail());

            ps.executeUpdate();
        }
    }
    public List<Documentary> getDocumentsByType(String type) throws SQLException {
        List<Documentary> list = new ArrayList<>();
        String sql = "SELECT * FROM Documentary WHERE type = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Documentary(
                        rs.getInt("docID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("source"),
                        rs.getString("doc_content"),
                        rs.getString("type"),
                        rs.getString("audioPath"),
                        rs.getString("thumbnail")
                ));
            }
        }
        return list;
    }

    public List<String> getAllDocumentTypes() throws SQLException {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM Documentary";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        }
        return types;
    }


    //VIP Document
    public int getLearnerIdByUserId(int userID) throws SQLException {
        String sql = "SELECT learnerID FROM Learner WHERE userID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("learnerID");
            }
        }
        return -1; // hoặc throw exception
    }

    public boolean isLearnerVIP(int learnerID) {
        String sql = "SELECT 1 FROM Vip_User WHERE learnerID = ? AND endDate >= GETDATE() AND status = 'ACTIVE'";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking VIP status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
