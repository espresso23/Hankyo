package dao;

import model.Tag;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    private Connection connection;

    public TagDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM Tag ORDER BY name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tag tag = new Tag(rs.getInt("tagID"), rs.getString("name"));
                tags.add(tag);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tags: " + e.getMessage());
        }

        return tags;
    }

    public Tag getTagById(int tagId) {
        Tag tag = null;
        String sql = "SELECT * FROM Tag WHERE tagID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, tagId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tag = new Tag(rs.getInt("tagID"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tag: " + e.getMessage());
        }

        return tag;
    }

    public int createTag(String tagName) {
        int generatedTagID = -1;
        String sql = "INSERT INTO Tag (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tagName);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedTagID = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating tag: " + e.getMessage());
        }

        return generatedTagID;
    }

    public Tag findOrCreateTag(String tagName) {
        Tag tag = findTagByName(tagName);

        if (tag == null) {
            int newTagId = createTag(tagName);
            if (newTagId != -1) {
                tag = new Tag(newTagId, tagName);
            }
        }

        return tag;
    }

    public Tag findTagByName(String tagName) {
        Tag tag = null;
        String sql = "SELECT * FROM Tag WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, tagName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tag = new Tag(rs.getInt("tagID"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tag by name: " + e.getMessage());
        }

        return tag;
    }
}
