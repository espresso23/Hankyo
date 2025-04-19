package dao;

import model.Course;
import model.CoursePaid;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoursePaidDAO {
    
    // Kiểm tra xem người học đã mua khóa học chưa
    public boolean isCoursePurchased(int learnerID, int courseID) {
        String sql = "SELECT COUNT(*) FROM Course_Paid WHERE learnerID = ? AND courseID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, learnerID);
            ps.setInt(2, courseID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách khóa học đã mua của người học
    public List<CoursePaid> getPurchasedCourses(int learnerID) {
        String query = "SELECT cp.*, c.title, c.course_description, c.price " +
                      "FROM Course_Paid cp " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "WHERE cp.learnerID = ?";
        List<CoursePaid> purchasedCourses = new ArrayList<>();
        
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CoursePaid coursePaid = new CoursePaid();
                Course course = new Course();
                course.setCourseDescription(rs.getString("course_description"));
                course.setPrice(rs.getBigDecimal("price"));
                coursePaid.setCourseID(rs.getInt("courseID"));
                coursePaid.setLearnerID(rs.getInt("learnerID"));
                coursePaid.setDatePaid(rs.getTimestamp("datePaid").toLocalDateTime());
                
                purchasedCourses.add(coursePaid);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách khóa học đã mua", e);
        }
        
        return purchasedCourses;
    }

    // Kiểm tra xem người học đã mua tất cả các khóa học trong danh sách chưa
    public boolean areAllCoursesPurchased(int learnerID, List<Integer> courseIDs) {
        String query = "SELECT COUNT(DISTINCT courseID) as count FROM Course_Paid " +
                      "WHERE learnerID = ? AND courseID IN (" + 
                      String.join(",", courseIDs.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") == courseIDs.size();
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra danh sách khóa học đã mua", e);
        }
    }

    public BigDecimal getTotalRevenue(int courseID) {
        String sql = "SELECT SUM(c.price) as total_revenue " +
                    "FROM Course_Paid cp " +
                    "JOIN Course c ON cp.courseID = c.courseID " +
                    "WHERE cp.courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal("total_revenue");
                return revenue != null ? revenue : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tính tổng doanh thu khóa học", e);
        }
        return BigDecimal.ZERO;
    }

    public int getPurchaseCount(int courseID) {
        String sql = "SELECT COUNT(*) FROM Course_Paid WHERE courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm số lượng khóa học đã bán", e);
        }
        return 0;
    }
} 