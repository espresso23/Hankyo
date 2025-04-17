package dao;

import model.Cart;
import model.Course;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {


    public boolean updateStatusAfterPurchased(Cart cart) {//cap nhap status cho cart sau khi purchased
        String updateStatusQuery = "Update Cart Set status = 'done' WHERE cartID = ?";
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateStatusQuery);
            preparedStatement.setInt(1, cart.getCartID());
            return preparedStatement.executeUpdate() > 1;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Thêm các phương thức sau vào CartDAO.java

    public boolean addToCart(int learnerID, int courseID) {
        String checkQuery = "SELECT * FROM Cart WHERE learnerID = ? AND courseID = ? AND status = 'pending'";
        String insertQuery = "INSERT INTO Cart(courseID, status, learnerID) VALUES (?, 'pending', ?)";

        try {
            Connection connection = DBConnect.getInstance().getConnection();
            // Kiểm tra xem khóa học đã có trong giỏ hàng chưa
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, learnerID);
            checkStmt.setInt(2, courseID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // Đã có trong giỏ hàng
            }

            // Thêm vào giỏ hàng
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, courseID);
            insertStmt.setInt(2, learnerID);
            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding to cart", e);
        }
    }

    public int getCartItemCount(int learnerID) {
        String query = "SELECT COUNT(*) as count FROM Cart WHERE learnerID = ? AND status = 'pending'";
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting cart count", e);
        }
    }

    public List<Cart> getPendingCartItems(int learnerID) {
        String query = "SELECT c.*, co.title, co.course_description as description, co.price, co.course_img FROM Cart c " +
                "JOIN Course co ON c.courseID = co.courseID " +
                "WHERE c.learnerID = ? AND c.status = 'pending'";
        List<Cart> cartItems = new ArrayList<>();
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartID(rs.getInt("cartID"));
                cart.setStatus(rs.getString("status"));

                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setPrice(rs.getBigDecimal("price"));
                course.setCourseImg(rs.getString("course_img"));
                course.setCourseDescription(rs.getString("description"));

                cart.setCourse(course);
                cartItems.add(cart);
            }
            return cartItems;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting cart items", e);
        }
    }

    public boolean removeFromCart(int cartID, int learnerID) {
        String query = "DELETE FROM Cart WHERE cartID = ? AND learnerID = ?";
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, cartID);
            stmt.setInt(2, learnerID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error removing from cart", e);
        }
    }

    public List<Cart> getCartItems(String learnerID) {
        String query = "SELECT c.*, co.title, co.price, co.course_img, co.expertID FROM Cart c " +
                "JOIN Course co ON c.courseID = co.courseID " +
                "WHERE c.learnerID = ?";
        List<Cart> cartItems = new ArrayList<>();
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, learnerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartID(rs.getInt("cartID"));
                cart.setStatus(rs.getString("status"));

                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setPrice(rs.getBigDecimal("price"));
                course.setCourseImg(rs.getString("course_img"));
                course.setExpertID(rs.getInt("expertID"));

                cart.setCourse(course);
                cartItems.add(cart);
            }
            return cartItems;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting cart items", e);
        }
    }

    private Cart maptoResultSet(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartID(rs.getInt("cartID"));
        cart.setStatus(rs.getString("status"));
        Course course = new Course();
        course.setCourseID(rs.getInt("courseID"));
        course.setCourseTitle(rs.getString("title"));
        course.setPrice(rs.getBigDecimal("price"));
        course.setCourseImg(rs.getString("course_img"));
        cart.setCourse(course);
        return cart;
    }
}
