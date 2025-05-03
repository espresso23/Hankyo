package dao;

import model.Category;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT categoryID, categoryName, description FROM Category ORDER BY categoryID DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        }
        
        return categories;
    }

    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO Category (categoryName, description) VALUES (?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setCategoryID(rs.getInt(1));
                }
            }
        }
    }

    public void updateCategory(Category category) throws SQLException {
        String query = "UPDATE Category SET categoryName = ?, description = ? WHERE categoryID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryID());
            ps.executeUpdate();
        }
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM Category WHERE categoryID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }

    public Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT categoryID, categoryName, description FROM Category WHERE categoryID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setCategoryID(rs.getInt("categoryID"));
                    category.setCategoryName(rs.getString("categoryName"));
                    category.setDescription(rs.getString("description"));
                    return category;
                }
            }
        }
        return null;
    }

    public boolean isCategoryInUse(int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM Course WHERE categoryID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }
} 