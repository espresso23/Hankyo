package dao;

import model.Media;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MediaDAO {

    public void addMedia(Media media) throws SQLException {
        String sql = "INSERT INTO Media (fileName, url, type, uploadDate) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, media.getFileName());
            stmt.setString(2, media.getUrl());
            stmt.setString(3, media.getType());
            stmt.executeUpdate();
        }
    }

    public List<Media> getAllMedia() throws SQLException {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM Media ORDER BY uploadDate DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Media media = new Media();
                media.setMediaId(rs.getInt("mediaId"));
                media.setFileName(rs.getString("fileName"));
                media.setUrl(rs.getString("url"));
                media.setType(rs.getString("type"));
                media.setUploadDate(rs.getString("uploadDate"));
                mediaList.add(media);
            }
        }

        return mediaList;
    }

    public void deleteMedia(int mediaId) throws SQLException {
        String sql = "DELETE FROM Media WHERE mediaId = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mediaId);
            stmt.executeUpdate();
        }
    }
} 