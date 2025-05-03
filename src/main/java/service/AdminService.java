package service;

import dao.*;
import model.*;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public class AdminService {
    private UserDAO userDAO;
    private CourseDAO courseDAO;
    private PaymentDAO paymentDAO;
    private PostDAO postDAO;
    private ExpertDAO expertDAO;
    private ReportDAO reportDAO;
    private TransactionDAO transactionDAO;

    public AdminService() {
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.paymentDAO = new PaymentDAO();
        this.postDAO = new PostDAO();
        this.expertDAO = new ExpertDAO();
        this.reportDAO = new ReportDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // 1. Thống kê tổng quan
    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();

        // Tổng số người dùng
        String userCountQuery = "SELECT COUNT(*) as total FROM [User]";
        int totalUsers = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(userCountQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalUsers = rs.getInt("total");
                stats.put("totalUsers", totalUsers);
            }
        }

        // Tổng số người dùng tháng trước
        String userCountLastMonthQuery = "SELECT COUNT(*) as total FROM [User] WHERE YEAR(dateCreate) = YEAR(GETDATE()) AND MONTH(dateCreate) = MONTH(GETDATE())-1";
        int totalUsersLastMonth = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(userCountLastMonthQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalUsersLastMonth = rs.getInt("total");
            }
        }
        double userGrowth = totalUsersLastMonth == 0 ? 100 : ((double)(totalUsers - totalUsersLastMonth) / Math.max(1, totalUsersLastMonth)) * 100;
        stats.put("userGrowth", userGrowth);

        // Tổng số khóa học
        String courseCountQuery = "SELECT COUNT(*) as total FROM Course";
        int totalCourses = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(courseCountQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalCourses = rs.getInt("total");
                stats.put("totalCourses", totalCourses);
            }
        }
        // Tổng số khóa học tháng trước
        String courseCountLastMonthQuery = "SELECT COUNT(*) as total FROM Course WHERE YEAR(createdAt) = YEAR(GETDATE()) AND MONTH(createdAt) = MONTH(GETDATE())-1";
        int totalCoursesLastMonth = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(courseCountLastMonthQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalCoursesLastMonth = rs.getInt("total");
            }
        }
        double courseGrowth = totalCoursesLastMonth == 0 ? 100 : ((double)(totalCourses - totalCoursesLastMonth) / Math.max(1, totalCoursesLastMonth)) * 100;
        stats.put("courseGrowth", courseGrowth);

        // Tổng doanh thu tháng 4/2025 (test)
        String revenueQuery = "SELECT SUM(amount) as total FROM Revenue WHERE YEAR(date) = 2025 AND MONTH(date) = 4";
        double totalRevenue = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(revenueQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalRevenue = rs.getDouble("total");
                stats.put("totalRevenue", totalRevenue);
            }
        }
        // Doanh thu tháng trước (tháng 3/2025, test)
        String revenueLastMonthQuery = "SELECT SUM(amount) as total FROM Revenue WHERE YEAR(date) = 2025 AND MONTH(date) = 3";
        double totalRevenueLastMonth = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(revenueLastMonthQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalRevenueLastMonth = rs.getDouble("total");
            }
        }
        // Tính tăng trưởng doanh thu
        double revenueGrowth = 0;
        if (totalRevenueLastMonth == 0) {
            revenueGrowth = totalRevenue > 0 ? 100 : 0;
        } else {
            revenueGrowth = ((totalRevenue - totalRevenueLastMonth) / totalRevenueLastMonth) * 100;
        }
        stats.put("revenueGrowth", revenueGrowth);

        // Số bài viết mới (7 ngày)
        String postCountQuery = "SELECT COUNT(*) as total FROM Post WHERE status = 1 AND CreatedDate >= DATEADD(day, -7, GETDATE())";
        int newPosts = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(postCountQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                newPosts = rs.getInt("total");
                stats.put("newPosts", newPosts);
            }
        }
        // Số bài viết mới tuần trước
        String postCountLastWeekQuery = "SELECT COUNT(*) as total FROM Post WHERE status = 1 AND CreatedDate >= DATEADD(day, -14, GETDATE()) AND CreatedDate < DATEADD(day, -7, GETDATE())";
        int newPostsLastWeek = 0;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(postCountLastWeekQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                newPostsLastWeek = rs.getInt("total");
            }
        }
        double postGrowth = newPostsLastWeek == 0 ? 100 : ((double)(newPosts - newPostsLastWeek) / Math.max(1, newPostsLastWeek)) * 100;
        stats.put("postGrowth", postGrowth);

        // Thêm dữ liệu doanh thu theo tháng
        stats.put("monthlyRevenue", getMonthlyRevenue());

        // Thêm dữ liệu thống kê người dùng theo tháng
        int currentYear = java.time.LocalDate.now().getYear();
        stats.put("userStats", getUserStatsByMonth(currentYear));

        // Thêm dữ liệu khóa học bán chạy
        stats.put("topSellingCourses", getTopSellingCoursesThisMonth());

        return stats;
    }

    // 2. Quản lý người dùng
    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT u.*, CASE WHEN EXISTS (SELECT 1 FROM Report r WHERE r.reportedUserID = u.userID) THEN 1 ELSE 0 END AS isReported FROM [User] u ORDER BY u.userID DESC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setFullName(rs.getString("fullName"));
                user.setGmail(rs.getString("gmail"));
                user.setStatus(rs.getString("status"));
                user.setRole(rs.getString("role"));
                user.setDateCreate(rs.getDate("dateCreate"));
                user.setIsReported(rs.getInt("isReported") == 1);
                users.add(user);
            }
        }
        return users;
    }

    public boolean blockUser(int userId) throws SQLException {
        String query = "UPDATE [User] SET status = 'blocked' WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean unblockUser(int userId) throws SQLException {
        String query = "UPDATE [User] SET status = 'active' WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<User> searchUsers(String searchTerm) throws SQLException {
        String query = "SELECT * FROM [User] WHERE fullName LIKE ? OR gmail LIKE ? ORDER BY userID DESC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setFullName(rs.getString("fullName"));
                    user.setGmail(rs.getString("gmail"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                    user.setDateCreate(rs.getDate("dateCreate"));
                    users.add(user);
                }
            }
        }
        return users;
    }

    public List<User> filterUsersByRole(String role) throws SQLException {
        String query = "SELECT * FROM [User] WHERE role = ? ORDER BY userID DESC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setFullName(rs.getString("fullName"));
                    user.setGmail(rs.getString("gmail"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                    user.setDateCreate(rs.getDate("dateCreate"));
                    users.add(user);
                }
            }
        }
        return users;
    }

    public Map<String, Object> getUserDetails(int userId) throws SQLException {
        Map<String, Object> userDetails = new HashMap<>();
        
        // Get basic user info
        String userQuery = "SELECT * FROM [User] WHERE userID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(userQuery)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userDetails.put("userID", rs.getInt("userID"));
                    userDetails.put("username", rs.getString("username"));
                    userDetails.put("fullName", rs.getString("fullName"));
                    userDetails.put("gmail", rs.getString("gmail"));
                    userDetails.put("phone", rs.getString("phone"));
                    userDetails.put("role", rs.getString("role"));
                    userDetails.put("status", rs.getString("status"));
                    userDetails.put("dateCreate", rs.getDate("dateCreate"));
                    userDetails.put("gender", rs.getString("gender"));
                    userDetails.put("dateOfBirth", rs.getDate("dateOfBirth"));
                    userDetails.put("avatar", rs.getString("avatar"));
                }
            }
        }

        // Get additional info based on role
        String role = (String) userDetails.get("role");
        if ("learner".equals(role)) {
            String learnerQuery = "SELECT * FROM Learner WHERE userID = ?";
            try (Connection conn = DBConnect.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(learnerQuery)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userDetails.put("learnerID", rs.getInt("learnerID"));
                        userDetails.put("level", rs.getString("level"));
                        userDetails.put("learningProgress", rs.getInt("learningProgress"));
                    }
                }
            }
        } else if ("expert".equals(role)) {
            String expertQuery = "SELECT * FROM Expert WHERE userID = ?";
            try (Connection conn = DBConnect.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(expertQuery)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userDetails.put("expertID", rs.getInt("expertID"));
                        userDetails.put("expertise", rs.getString("expertise"));
                        userDetails.put("rating", rs.getDouble("rating"));
                    }
                }
            }
        }

        return userDetails;
    }

    // 3. Quản lý khóa học
    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public List<Course> searchCourses(String keyword) throws SQLException {
        return courseDAO.searchCoursesForAdmin(keyword);
    }

    public List<Course> filterCourses(String status, String expert, String category) throws SQLException {
        return courseDAO.filterCourses(status, expert, category);
    }

    // 4. Quản lý thanh toán
    public List<Map<String, Object>> getRecentPayments() throws SQLException {
        List<Map<String, Object>> payments = new ArrayList<>();
        
        String query = "SELECT TOP 50 " +
                      "cp.courseID, cp.learnerID, cp.datePaid, " +
                      "p.paymentID, p.amount, p.status as paymentStatus, p.description, " +
                      "c.title as courseTitle, c.course_img, c.price, " +
                      "u.fullName as learnerName, u.gmail as learnerEmail, " +
                      "e.expertID, u2.fullName as expertName " +
                      "FROM Course_Paid cp " +
                      "JOIN Payment p ON cp.paymentID = p.paymentID " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "JOIN Learner l ON cp.learnerID = l.learnerID " +
                      "JOIN [User] u ON l.userID = u.userID " +
                      "JOIN Expert e ON c.expertID = e.expertID " +
                      "JOIN [User] u2 ON e.userID = u2.userID " +
                      "WHERE p.status = 'completed' " +
                      "ORDER BY cp.datePaid DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> payment = new HashMap<>();
                // Thông tin thanh toán
                payment.put("paymentID", rs.getString("paymentID"));
                payment.put("amount", rs.getBigDecimal("amount"));
                payment.put("paymentStatus", rs.getString("paymentStatus"));
                payment.put("description", rs.getString("description"));
                payment.put("datePaid", rs.getTimestamp("datePaid"));
                
                // Thông tin khóa học
                payment.put("courseID", rs.getInt("courseID"));
                payment.put("courseTitle", rs.getString("courseTitle"));
                payment.put("courseImg", rs.getString("course_img"));
                payment.put("coursePrice", rs.getBigDecimal("price"));
                
                // Thông tin người học
                payment.put("learnerID", rs.getInt("learnerID"));
                payment.put("learnerName", rs.getString("learnerName"));
                payment.put("learnerEmail", rs.getString("learnerEmail"));
                
                // Thông tin expert
                payment.put("expertID", rs.getInt("expertID"));
                payment.put("expertName", rs.getString("expertName"));
                
                payments.add(payment);
            }
        }
        
        return payments;
    }

    public boolean processDrawRequest(int requestID) throws SQLException {
        String query = "UPDATE WithdrawRequest SET status = 'done' WHERE requestID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, requestID);
            return ps.executeUpdate() > 0;
        }
    }

    // 5. Quản lý nội dung
    public List<Post> getReportedPosts() throws SQLException {
        String query = "SELECT p.*, u.fullName as authorName, COUNT(r.reportID) as reportCount " +
                "FROM Post p " +
                "JOIN [User] u ON p.userID = u.userID " +
                "LEFT JOIN Report r ON p.postID = r.postID " +
                "GROUP BY p.postID, u.fullName " +
                "HAVING COUNT(r.reportID) > 0 " +
                "ORDER BY reportCount DESC";
        return postDAO.getPostsByQuery(query);
    }


    // 6. Quản lý expert
    public List<Expert> getExperts() throws SQLException {
        String query = "SELECT e.*, u.fullName, u.gmail, u.avatar, u.role " +
                "FROM Expert e " +
                "JOIN [User] u ON e.userID = u.userID " +
                "ORDER BY e.expertID DESC";
        return expertDAO.getExpertsByQuery(query);
    }

    // 7. Báo cáo và thống kê
    public Map<String, Object> getRevenueReport(String startDate, String endDate) throws SQLException {
        Map<String, Object> report = new HashMap<>();

        String query = "SELECT " +
                "COUNT(*) as totalTransactions, " +
                "SUM(amount) as totalRevenue, " +
                "AVG(amount) as averageTransaction " +
                "FROM Payment " +
                "WHERE paymentDate BETWEEN ? AND ? " +
                "AND status = 'completed'";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    report.put("totalTransactions", rs.getInt("totalTransactions"));
                    report.put("totalRevenue", rs.getDouble("totalRevenue"));
                    report.put("averageTransaction", rs.getDouble("averageTransaction"));
                }
            }
        }

        return report;
    }

    public Map<String, Object> getUserGrowthReport(String startDate, String endDate) throws SQLException {
        Map<String, Object> report = new HashMap<>();
        
        // Lấy tổng số learner
        String learnerQuery = "SELECT COUNT(*) as totalLearners " +
                             "FROM Learner l " +
                             "JOIN [User] u ON l.userID = u.userID " +
                             "WHERE u.createdAt BETWEEN ? AND ?";
        
        // Lấy tổng số expert
        String expertQuery = "SELECT COUNT(*) as totalExperts " +
                            "FROM Expert e " +
                            "JOIN [User] u ON e.userID = u.userID " +
                            "WHERE u.createdAt BETWEEN ? AND ?";
        
        // Lấy tổng số VIP user
        String vipQuery = "SELECT COUNT(*) as totalVipUsers " +
                         "FROM Vip_User vu " +
                         "JOIN Learner l ON vu.learnerID = l.learnerID " +
                         "JOIN [User] u ON l.userID = u.userID " +
                         "WHERE u.createdAt BETWEEN ? AND ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection()) {
            // Lấy số learner
            try (PreparedStatement ps = conn.prepareStatement(learnerQuery)) {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        report.put("totalLearners", rs.getInt("totalLearners"));
                    }
                }
            }
            
            // Lấy số expert
            try (PreparedStatement ps = conn.prepareStatement(expertQuery)) {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        report.put("totalExperts", rs.getInt("totalExperts"));
                    }
                }
            }
            
            // Lấy số VIP user
            try (PreparedStatement ps = conn.prepareStatement(vipQuery)) {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        report.put("totalVipUsers", rs.getInt("totalVipUsers"));
                    }
                }
            }
        }
        
        return report;
    }

    public List<Map<String, Object>> getHighRatedCourses() throws SQLException {
        List<Map<String, Object>> highRatedCourses = new ArrayList<>();
        
        String query = "SELECT c.courseID, c.title AS CourseTitle, c.course_description, c.price, " +
                      "c.createdAt, c.updateAt, c.course_img, c.categoryID, cat.categoryName, " +
                      "e.expertID, u.userID, u.fullName AS ExpertName, u.gmail AS ExpertEmail, " +
                      "u.phone AS ExpertPhone " +
                      "FROM Course AS c " +
                      "JOIN Category AS cat ON c.categoryID = cat.categoryID " +
                      "JOIN Expert AS e ON c.expertID = e.expertID " +
                      "JOIN [User] AS u ON e.userID = u.userID " +
                      "JOIN CourseFeedback AS cf ON c.courseID = cf.courseID " +
                      "WHERE cf.rating >= 4 " +
                      "ORDER BY cf.rating DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                
                // Thông tin khóa học
                course.put("courseID", rs.getInt("courseID"));
                course.put("title", rs.getString("CourseTitle"));
                course.put("description", rs.getString("course_description"));
                course.put("price", rs.getBigDecimal("price"));
                course.put("createdAt", rs.getTimestamp("createdAt"));
                course.put("updateAt", rs.getTimestamp("updateAt"));
                course.put("courseImg", rs.getString("course_img"));
                
                // Thông tin danh mục
                course.put("categoryID", rs.getInt("categoryID"));
                course.put("categoryName", rs.getString("categoryName"));
                
                // Thông tin expert
                course.put("expertID", rs.getInt("expertID"));
                course.put("userID", rs.getInt("userID"));
                course.put("expertName", rs.getString("ExpertName"));
                course.put("expertEmail", rs.getString("ExpertEmail"));
                course.put("expertPhone", rs.getString("ExpertPhone"));
                
                highRatedCourses.add(course);
            }
        }
        
        return highRatedCourses;
    }

    public List<Map<String, Object>> getLowRatedCourses() throws SQLException {
        List<Map<String, Object>> lowRatedCourses = new ArrayList<>();
        
        String query = "SELECT c.courseID, c.title AS CourseTitle, c.course_description, c.price, " +
                      "c.createdAt, c.updateAt, c.course_img, c.categoryID, cat.categoryName, " +
                      "e.expertID, u.userID, u.fullName AS ExpertName, u.gmail AS ExpertEmail, " +
                      "u.phone AS ExpertPhone " +
                      "FROM Course AS c " +
                      "JOIN Category AS cat ON c.categoryID = cat.categoryID " +
                      "JOIN Expert AS e ON c.expertID = e.expertID " +
                      "JOIN [User] AS u ON e.userID = u.userID " +
                      "JOIN CourseFeedback AS cf ON c.courseID = cf.courseID " +
                      "WHERE cf.rating < 3 " +
                      "ORDER BY cf.rating ASC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                
                // Thông tin khóa học
                course.put("courseID", rs.getInt("courseID"));
                course.put("title", rs.getString("CourseTitle"));
                course.put("description", rs.getString("course_description"));
                course.put("price", rs.getBigDecimal("price"));
                course.put("createdAt", rs.getTimestamp("createdAt"));
                course.put("updateAt", rs.getTimestamp("updateAt"));
                course.put("courseImg", rs.getString("course_img"));
                
                // Thông tin danh mục
                course.put("categoryID", rs.getInt("categoryID"));
                course.put("categoryName", rs.getString("categoryName"));
                
                // Thông tin expert
                course.put("expertID", rs.getInt("expertID"));
                course.put("userID", rs.getInt("userID"));
                course.put("expertName", rs.getString("ExpertName"));
                course.put("expertEmail", rs.getString("ExpertEmail"));
                course.put("expertPhone", rs.getString("ExpertPhone"));
                
                lowRatedCourses.add(course);
            }
        }
        
        return lowRatedCourses;
    }

    public List<Map<String, Object>> getMonthlyRevenue() throws SQLException {
        List<Map<String, Object>> monthlyRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "MONTH(date) AS month, " +
                      "SUM(amount) AS monthlyRevenue, " +
                      "COUNT(*) AS transactionCount " +
                      "FROM Revenue " +
                      "GROUP BY MONTH(date) " +
                      "ORDER BY month ASC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", rs.getInt("month"));
                monthData.put("revenue", rs.getBigDecimal("monthlyRevenue"));
                monthData.put("transactionCount", rs.getInt("transactionCount"));
                monthlyRevenue.add(monthData);
            }
        }
        
        return monthlyRevenue;
    }

    public List<Map<String, Object>> getYearlyRevenue() throws SQLException {
        List<Map<String, Object>> yearlyRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "YEAR(date) AS year, " +
                      "SUM(amount) AS yearlyRevenue, " +
                      "COUNT(*) AS transactionCount " +
                      "FROM Revenue " +
                      "GROUP BY YEAR(date) " +
                      "ORDER BY year ASC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> yearData = new HashMap<>();
                yearData.put("year", rs.getInt("year"));
                yearData.put("revenue", rs.getBigDecimal("yearlyRevenue"));
                yearData.put("transactionCount", rs.getInt("transactionCount"));
                yearlyRevenue.add(yearData);
            }
        }
        
        return yearlyRevenue;
    }

    public List<Map<String, Object>> getWeeklyRevenue() throws SQLException {
        List<Map<String, Object>> weeklyRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "YEAR(date) AS year, " +
                      "DATEPART(week, date) AS week, " +
                      "SUM(amount) AS weeklyRevenue, " +
                      "COUNT(*) AS transactionCount " +
                      "FROM Revenue " +
                      "GROUP BY YEAR(date), DATEPART(week, date) " +
                      "ORDER BY year ASC, week ASC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> weekData = new HashMap<>();
                weekData.put("year", rs.getInt("year"));
                weekData.put("week", rs.getInt("week"));
                weekData.put("revenue", rs.getBigDecimal("weeklyRevenue"));
                weekData.put("transactionCount", rs.getInt("transactionCount"));
                weeklyRevenue.add(weekData);
            }
        }
        
        return weeklyRevenue;
    }

    public List<Map<String, Object>> getDetailedRevenue() throws SQLException {
        List<Map<String, Object>> detailedRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "r.revenueID, r.amount as platformRevenue, r.date, r.description, " +
                      "p.paymentID, p.amount as totalPayment, p.paymentDate, p.status as paymentStatus, " +
                      "cp.courseID, c.title as courseTitle, c.price as coursePrice, " +
                      "l.learnerID, u.fullName as learnerName, u.gmail as learnerEmail " +
                      "FROM Revenue r " +
                      "JOIN Payment p ON r.paymentID = p.paymentID " +
                      "JOIN Course_Paid cp ON p.paymentID = cp.paymentID " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "JOIN Learner l ON cp.learnerID = l.learnerID " +
                      "JOIN [User] u ON l.userID = u.userID " +
                      "ORDER BY r.date DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> revenueDetail = new HashMap<>();
                
                // Thông tin doanh thu nền tảng
                revenueDetail.put("revenueID", rs.getInt("revenueID"));
                revenueDetail.put("platformRevenue", rs.getBigDecimal("platformRevenue"));
                revenueDetail.put("date", rs.getTimestamp("date"));
                revenueDetail.put("description", rs.getString("description"));
                
                // Thông tin thanh toán
                revenueDetail.put("paymentID", rs.getString("paymentID"));
                revenueDetail.put("totalPayment", rs.getBigDecimal("totalPayment"));
                revenueDetail.put("paymentDate", rs.getTimestamp("paymentDate"));
                revenueDetail.put("paymentStatus", rs.getString("paymentStatus"));
                
                // Thông tin khóa học
                revenueDetail.put("courseID", rs.getInt("courseID"));
                revenueDetail.put("courseTitle", rs.getString("courseTitle"));
                revenueDetail.put("coursePrice", rs.getBigDecimal("coursePrice"));
                
                // Thông tin người học
                revenueDetail.put("learnerID", rs.getInt("learnerID"));
                revenueDetail.put("learnerName", rs.getString("learnerName"));
                revenueDetail.put("learnerEmail", rs.getString("learnerEmail"));
                
                detailedRevenue.add(revenueDetail);
            }
        }
        
        return detailedRevenue;
    }

    public List<Map<String, Object>> getDetailedRevenueByDay(String date) throws SQLException {
        List<Map<String, Object>> detailedRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "r.revenueID, r.amount as platformRevenue, r.date, r.description, " +
                      "p.paymentID, p.amount as totalPayment, p.paymentDate, p.status as paymentStatus, " +
                      "cp.courseID, c.title as courseTitle, c.price as coursePrice, " +
                      "l.learnerID, u.fullName as learnerName, u.gmail as learnerEmail " +
                      "FROM Revenue r " +
                      "JOIN Payment p ON r.paymentID = p.paymentID " +
                      "JOIN Course_Paid cp ON p.paymentID = cp.paymentID " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "JOIN Learner l ON cp.learnerID = l.learnerID " +
                      "JOIN [User] u ON l.userID = u.userID " +
                      "WHERE CAST(r.date AS DATE) = ? " +
                      "ORDER BY r.date DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> revenueDetail = new HashMap<>();
                    revenueDetail.put("revenueID", rs.getInt("revenueID"));
                    revenueDetail.put("platformRevenue", rs.getBigDecimal("platformRevenue"));
                    revenueDetail.put("date", rs.getTimestamp("date"));
                    revenueDetail.put("description", rs.getString("description"));
                    revenueDetail.put("paymentID", rs.getString("paymentID"));
                    revenueDetail.put("totalPayment", rs.getBigDecimal("totalPayment"));
                    revenueDetail.put("paymentDate", rs.getTimestamp("paymentDate"));
                    revenueDetail.put("paymentStatus", rs.getString("paymentStatus"));
                    revenueDetail.put("courseID", rs.getInt("courseID"));
                    revenueDetail.put("courseTitle", rs.getString("courseTitle"));
                    revenueDetail.put("coursePrice", rs.getBigDecimal("coursePrice"));
                    revenueDetail.put("learnerID", rs.getInt("learnerID"));
                    revenueDetail.put("learnerName", rs.getString("learnerName"));
                    revenueDetail.put("learnerEmail", rs.getString("learnerEmail"));
                    detailedRevenue.add(revenueDetail);
                }
            }
        }
        
        return detailedRevenue;
    }

    public List<Map<String, Object>> getDetailedRevenueByWeek(int year, int week) throws SQLException {
        List<Map<String, Object>> detailedRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "r.revenueID, r.amount as platformRevenue, r.date, r.description, " +
                      "p.paymentID, p.amount as totalPayment, p.paymentDate, p.status as paymentStatus, " +
                      "cp.courseID, c.title as courseTitle, c.price as coursePrice, " +
                      "l.learnerID, u.fullName as learnerName, u.gmail as learnerEmail " +
                      "FROM Revenue r " +
                      "JOIN Payment p ON r.paymentID = p.paymentID " +
                      "JOIN Course_Paid cp ON p.paymentID = cp.paymentID " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "JOIN Learner l ON cp.learnerID = l.learnerID " +
                      "JOIN [User] u ON l.userID = u.userID " +
                      "WHERE YEAR(r.date) = ? AND DATEPART(week, r.date) = ? " +
                      "ORDER BY r.date DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            ps.setInt(2, week);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> revenueDetail = new HashMap<>();
                    revenueDetail.put("revenueID", rs.getInt("revenueID"));
                    revenueDetail.put("platformRevenue", rs.getBigDecimal("platformRevenue"));
                    revenueDetail.put("date", rs.getTimestamp("date"));
                    revenueDetail.put("description", rs.getString("description"));
                    revenueDetail.put("paymentID", rs.getString("paymentID"));
                    revenueDetail.put("totalPayment", rs.getBigDecimal("totalPayment"));
                    revenueDetail.put("paymentDate", rs.getTimestamp("paymentDate"));
                    revenueDetail.put("paymentStatus", rs.getString("paymentStatus"));
                    revenueDetail.put("courseID", rs.getInt("courseID"));
                    revenueDetail.put("courseTitle", rs.getString("courseTitle"));
                    revenueDetail.put("coursePrice", rs.getBigDecimal("coursePrice"));
                    revenueDetail.put("learnerID", rs.getInt("learnerID"));
                    revenueDetail.put("learnerName", rs.getString("learnerName"));
                    revenueDetail.put("learnerEmail", rs.getString("learnerEmail"));
                    detailedRevenue.add(revenueDetail);
                }
            }
        }
        
        return detailedRevenue;
    }

    public List<Map<String, Object>> getDetailedRevenueByMonth(int year, int month) throws SQLException {
        List<Map<String, Object>> detailedRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "r.revenueID, r.amount as platformRevenue, r.date, r.description, " +
                      "p.paymentID, p.amount as totalPayment, p.paymentDate, p.status as paymentStatus, " +
                      "cp.courseID, c.title as courseTitle, c.price as coursePrice, " +
                      "l.learnerID, u.fullName as learnerName, u.gmail as learnerEmail " +
                      "FROM Revenue r " +
                      "JOIN Payment p ON r.paymentID = p.paymentID " +
                      "JOIN Course_Paid cp ON p.paymentID = cp.paymentID " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "JOIN Learner l ON cp.learnerID = l.learnerID " +
                      "JOIN [User] u ON l.userID = u.userID " +
                      "WHERE YEAR(r.date) = ? AND MONTH(r.date) = ? " +
                      "ORDER BY r.date DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> revenueDetail = new HashMap<>();
                    revenueDetail.put("revenueID", rs.getInt("revenueID"));
                    revenueDetail.put("platformRevenue", rs.getBigDecimal("platformRevenue"));
                    revenueDetail.put("date", rs.getTimestamp("date"));
                    revenueDetail.put("description", rs.getString("description"));
                    revenueDetail.put("paymentID", rs.getString("paymentID"));
                    revenueDetail.put("totalPayment", rs.getBigDecimal("totalPayment"));
                    revenueDetail.put("paymentDate", rs.getTimestamp("paymentDate"));
                    revenueDetail.put("paymentStatus", rs.getString("paymentStatus"));
                    revenueDetail.put("courseID", rs.getInt("courseID"));
                    revenueDetail.put("courseTitle", rs.getString("courseTitle"));
                    revenueDetail.put("coursePrice", rs.getBigDecimal("coursePrice"));
                    revenueDetail.put("learnerID", rs.getInt("learnerID"));
                    revenueDetail.put("learnerName", rs.getString("learnerName"));
                    revenueDetail.put("learnerEmail", rs.getString("learnerEmail"));
                    detailedRevenue.add(revenueDetail);
                }
            }
        }
        
        return detailedRevenue;
    }

    public List<Map<String, Object>> getDetailedRevenueByYear(int year) throws SQLException {
        List<Map<String, Object>> detailedRevenue = new ArrayList<>();
        
        String query = "SELECT " +
                      "r.revenueID, r.amount as platformRevenue, r.date, r.description, " +
                      "p.paymentID, p.amount as totalPayment, p.paymentDate, p.status as paymentStatus, " +
                      "cp.courseID, c.title as courseTitle, c.price as coursePrice, " +
                      "l.learnerID, u.fullName as learnerName, u.gmail as learnerEmail " +
                      "FROM Revenue r " +
                      "JOIN Payment p ON r.paymentID = p.paymentID " +
                      "JOIN Course_Paid cp ON p.paymentID = cp.paymentID " +
                      "JOIN Course c ON cp.courseID = c.courseID " +
                      "JOIN Learner l ON cp.learnerID = l.learnerID " +
                      "JOIN [User] u ON l.userID = u.userID " +
                      "WHERE YEAR(r.date) = ? " +
                      "ORDER BY r.date DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> revenueDetail = new HashMap<>();
                    revenueDetail.put("revenueID", rs.getInt("revenueID"));
                    revenueDetail.put("platformRevenue", rs.getBigDecimal("platformRevenue"));
                    revenueDetail.put("date", rs.getTimestamp("date"));
                    revenueDetail.put("description", rs.getString("description"));
                    revenueDetail.put("paymentID", rs.getString("paymentID"));
                    revenueDetail.put("totalPayment", rs.getBigDecimal("totalPayment"));
                    revenueDetail.put("paymentDate", rs.getTimestamp("paymentDate"));
                    revenueDetail.put("paymentStatus", rs.getString("paymentStatus"));
                    revenueDetail.put("courseID", rs.getInt("courseID"));
                    revenueDetail.put("courseTitle", rs.getString("courseTitle"));
                    revenueDetail.put("coursePrice", rs.getBigDecimal("coursePrice"));
                    revenueDetail.put("learnerID", rs.getInt("learnerID"));
                    revenueDetail.put("learnerName", rs.getString("learnerName"));
                    revenueDetail.put("learnerEmail", rs.getString("learnerEmail"));
                    detailedRevenue.add(revenueDetail);
                }
            }
        }
        
        return detailedRevenue;
    }

    public List<Map<String, Object>> getReportDetails() throws SQLException {
        List<Map<String, Object>> reportDetails = new ArrayList<>();
        
        String query = "SELECT " +
                      "r.reportID, rt.typeName, " +
                      "u.fullName AS ReporterName, u2.fullName AS ReportedUserName, " +
                      "r.reason, r.messageID, r.postID, r.reportDate, r.status, r.courseID " +
                      "FROM Report AS r " +
                      "JOIN ReportType AS rt ON r.reportTypeID = rt.reportTypeID " +
                      "JOIN [User] AS u ON r.reporterID = u.userID " +
                      "JOIN [User] AS u2 ON r.reportedUserID = u2.userID " +
                      "ORDER BY r.reportDate ASC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> reportDetail = new HashMap<>();
                
                // Thông tin báo cáo
                reportDetail.put("reportID", rs.getInt("reportID"));
                reportDetail.put("typeName", rs.getString("typeName"));
                reportDetail.put("reason", rs.getString("reason"));
                reportDetail.put("messageID", rs.getInt("messageID"));
                reportDetail.put("postID", rs.getInt("postID"));
                reportDetail.put("reportDate", rs.getTimestamp("reportDate"));
                reportDetail.put("status", rs.getString("status"));
                reportDetail.put("courseID", rs.getObject("courseID") != null ? rs.getInt("courseID") : null);
                // Thông tin người báo cáo và bị báo cáo
                reportDetail.put("reporterName", rs.getString("ReporterName"));
                reportDetail.put("reportedUserName", rs.getString("ReportedUserName"));
                
                reportDetails.add(reportDetail);
            }
        }
        
        return reportDetails;
    }

    public List<Map<String, Object>> getReportsByStatus(String status) throws SQLException {
        List<Map<String, Object>> reportDetails = new ArrayList<>();
        String query = "SELECT " +
                      "r.reportID, rt.typeName, " +
                      "u.fullName AS ReporterName, u2.fullName AS ReportedUserName, " +
                      "r.reason, r.messageID, r.postID, r.reportDate, r.status, r.courseID " +
                      "FROM Report AS r " +
                      "JOIN ReportType AS rt ON r.reportTypeID = rt.reportTypeID " +
                      "JOIN [User] AS u ON r.reporterID = u.userID " +
                      "JOIN [User] AS u2 ON r.reportedUserID = u2.userID " +
                      "WHERE r.status = ? " +
                      "ORDER BY r.reportDate DESC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> reportDetail = new HashMap<>();
                    reportDetail.put("reportID", rs.getInt("reportID"));
                    reportDetail.put("typeName", rs.getString("typeName"));
                    reportDetail.put("reporterName", rs.getString("ReporterName"));
                    reportDetail.put("reportedUserName", rs.getString("ReportedUserName"));
                    reportDetail.put("reason", rs.getString("reason"));
                    reportDetail.put("messageID", rs.getInt("messageID"));
                    reportDetail.put("postID", rs.getInt("postID"));
                    reportDetail.put("reportDate", rs.getTimestamp("reportDate"));
                    reportDetail.put("status", rs.getString("status"));
                    reportDetail.put("courseID", rs.getObject("courseID") != null ? rs.getInt("courseID") : null);
                    reportDetails.add(reportDetail);
                }
            }
        }
        return reportDetails;
    }

    public List<Map<String, Object>> getReportsByReporter(int reporterID) throws SQLException {
        List<Map<String, Object>> reportDetails = new ArrayList<>();
        String query = "SELECT " +
                      "r.reportID, rt.typeName, " +
                      "u.fullName AS ReporterName, u2.fullName AS ReportedUserName, " +
                      "r.reason, r.messageID, r.postID, r.reportDate, r.status, r.courseID " +
                      "FROM Report AS r " +
                      "JOIN ReportType AS rt ON r.reportTypeID = rt.reportTypeID " +
                      "JOIN [User] AS u ON r.reporterID = u.userID " +
                      "JOIN [User] AS u2 ON r.reportedUserID = u2.userID " +
                      "WHERE r.reporterID = ? " +
                      "ORDER BY r.reportDate DESC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reporterID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> reportDetail = new HashMap<>();
                    reportDetail.put("reportID", rs.getInt("reportID"));
                    reportDetail.put("typeName", rs.getString("typeName"));
                    reportDetail.put("reporterName", rs.getString("ReporterName"));
                    reportDetail.put("reportedUserName", rs.getString("ReportedUserName"));
                    reportDetail.put("reason", rs.getString("reason"));
                    reportDetail.put("messageID", rs.getInt("messageID"));
                    reportDetail.put("postID", rs.getInt("postID"));
                    reportDetail.put("reportDate", rs.getTimestamp("reportDate"));
                    reportDetail.put("status", rs.getString("status"));
                    reportDetail.put("courseID", rs.getObject("courseID") != null ? rs.getInt("courseID") : null);
                    reportDetails.add(reportDetail);
                }
            }
        }
        return reportDetails;
    }

    public List<Map<String, Object>> getReportsByReportedUser(int reportedUserID) throws SQLException {
        List<Map<String, Object>> reportDetails = new ArrayList<>();
        String query = "SELECT " +
                      "r.reportID, rt.typeName, " +
                      "u.fullName AS ReporterName, u2.fullName AS ReportedUserName, " +
                      "r.reason, r.messageID, r.postID, r.reportDate, r.status, r.courseID " +
                      "FROM Report AS r " +
                      "JOIN ReportType AS rt ON r.reportTypeID = rt.reportTypeID " +
                      "JOIN [User] AS u ON r.reporterID = u.userID " +
                      "JOIN [User] AS u2 ON r.reportedUserID = u2.userID " +
                      "WHERE r.reportedUserID = ? " +
                      "ORDER BY r.reportDate DESC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reportedUserID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> reportDetail = new HashMap<>();
                    reportDetail.put("reportID", rs.getInt("reportID"));
                    reportDetail.put("typeName", rs.getString("typeName"));
                    reportDetail.put("reporterName", rs.getString("ReporterName"));
                    reportDetail.put("reportedUserName", rs.getString("ReportedUserName"));
                    reportDetail.put("reason", rs.getString("reason"));
                    reportDetail.put("messageID", rs.getInt("messageID"));
                    reportDetail.put("postID", rs.getInt("postID"));
                    reportDetail.put("reportDate", rs.getTimestamp("reportDate"));
                    reportDetail.put("status", rs.getString("status"));
                    reportDetail.put("courseID", rs.getObject("courseID") != null ? rs.getInt("courseID") : null);
                    reportDetails.add(reportDetail);
                }
            }
        }
        return reportDetails;
    }

    public List<Map<String, Object>> getReportsByDateRange(String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> reportDetails = new ArrayList<>();
        String query = "SELECT " +
                      "r.reportID, rt.typeName, " +
                      "u.fullName AS ReporterName, u2.fullName AS ReportedUserName, " +
                      "r.reason, r.messageID, r.postID, r.reportDate, r.status, r.courseID " +
                      "FROM Report AS r " +
                      "JOIN ReportType AS rt ON r.reportTypeID = rt.reportTypeID " +
                      "JOIN [User] AS u ON r.reporterID = u.userID " +
                      "JOIN [User] AS u2 ON r.reportedUserID = u2.userID " +
                      "WHERE r.reportDate BETWEEN ? AND ? " +
                      "ORDER BY r.reportDate DESC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> reportDetail = new HashMap<>();
                    reportDetail.put("reportID", rs.getInt("reportID"));
                    reportDetail.put("typeName", rs.getString("typeName"));
                    reportDetail.put("reporterName", rs.getString("ReporterName"));
                    reportDetail.put("reportedUserName", rs.getString("ReportedUserName"));
                    reportDetail.put("reason", rs.getString("reason"));
                    reportDetail.put("messageID", rs.getInt("messageID"));
                    reportDetail.put("postID", rs.getInt("postID"));
                    reportDetail.put("reportDate", rs.getTimestamp("reportDate"));
                    reportDetail.put("status", rs.getString("status"));
                    reportDetail.put("courseID", rs.getObject("courseID") != null ? rs.getInt("courseID") : null);
                    reportDetails.add(reportDetail);
                }
            }
        }
        return reportDetails;
    }

    public int getTotalCourses() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM Course";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Lấy danh sách tất cả danh mục
    public List<Map<String, Object>> getAllCategories() throws SQLException {
        List<Map<String, Object>> categories = new ArrayList<>();
        
        String query = "SELECT categoryID, categoryName, description FROM Category ORDER BY categoryID DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> category = new HashMap<>();
                category.put("categoryID", rs.getInt("categoryID"));
                category.put("categoryName", rs.getString("categoryName"));
                category.put("description", rs.getString("description"));
                categories.add(category);
            }
        }
        
        return categories;
    }

    // Thêm danh mục mới
    public boolean addCategory(String categoryName, String description) throws SQLException {
        String query = "INSERT INTO Category (categoryName, description) VALUES (?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, categoryName);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật danh mục
    public boolean updateCategory(int categoryID, String categoryName, String description) throws SQLException {
        String query = "UPDATE Category SET categoryName = ?, description = ? WHERE categoryID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, categoryName);
            ps.setString(2, description);
            ps.setInt(3, categoryID);
            return ps.executeUpdate() > 0;
        }
    }

    // Xóa danh mục
    public boolean deleteCategory(int categoryID) throws SQLException {
        // Kiểm tra xem danh mục có đang được sử dụng không
        String checkQuery = "SELECT COUNT(*) as count FROM Course WHERE categoryID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setInt(1, categoryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("count") > 0) {
                    throw new SQLException("Không thể xóa danh mục vì đang có khóa học sử dụng");
                }
            }
        }
        
        // Nếu không có khóa học nào sử dụng, tiến hành xóa
        String deleteQuery = "DELETE FROM Category WHERE categoryID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, categoryID);
            return ps.executeUpdate() > 0;
        }
    }

    // Lấy thông tin một danh mục theo ID
    public Map<String, Object> getCategoryById(int categoryID) throws SQLException {
        String query = "SELECT categoryID, categoryName, description FROM Category WHERE categoryID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("categoryID", rs.getInt("categoryID"));
                    category.put("categoryName", rs.getString("categoryName"));
                    category.put("description", rs.getString("description"));
                    return category;
                }
            }
        }
        return null;
    }

    // Thống kê lượng user theo ngày
    public List<Map<String, Object>> getUserStatsByDay(String date) throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        
        String query = "SELECT " +
                      "COUNT(*) as totalUsers, " +
                      "SUM(CASE WHEN role = 'learner' THEN 1 ELSE 0 END) as totalLearners, " +
                      "SUM(CASE WHEN role = 'expert' THEN 1 ELSE 0 END) as totalExperts, " +
                      "SUM(CASE WHEN role = 'examManager' THEN 1 ELSE 0 END) as totalExamManagers, " +
                      "SUM(CASE WHEN role = 'admin' THEN 1 ELSE 0 END) as totalAdmins " +
                      "FROM [User] " +
                      "WHERE CAST(dateCreate AS DATE) = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("totalUsers", rs.getInt("totalUsers"));
                    stat.put("totalLearners", rs.getInt("totalLearners"));
                    stat.put("totalExperts", rs.getInt("totalExperts"));
                    stat.put("totalExamManagers", rs.getInt("totalExamManagers"));
                    stat.put("totalAdmins", rs.getInt("totalAdmins"));
                    stats.add(stat);
                }
            }
        }
        
        return stats;
    }
    public List<Map<String, Object>> getUserStatsByMonth(int year) throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        String query = "SELECT MONTH(dateCreate) as month, COUNT(*) as totalUsers FROM [User] WHERE YEAR(dateCreate) = ? GROUP BY MONTH(dateCreate) ORDER BY month ASC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("month", rs.getInt("month"));
                    stat.put("totalUsers", rs.getInt("totalUsers"));
                    stats.add(stat);
                }
            }
        }
        return stats;
    }
    // Thống kê lượng user theo tháng
    public List<Map<String, Object>> getUserStatsByMonth(int year, int month) throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        
        String query = "SELECT " +
                      "COUNT(*) as totalUsers, " +
                      "SUM(CASE WHEN role = 'learner' THEN 1 ELSE 0 END) as totalLearners, " +
                      "SUM(CASE WHEN role = 'expert' THEN 1 ELSE 0 END) as totalExperts, " +
                      "SUM(CASE WHEN role = 'examManager' THEN 1 ELSE 0 END) as totalExamManagers, " +
                      "SUM(CASE WHEN role = 'admin' THEN 1 ELSE 0 END) as totalAdmins " +
                      "FROM [User] " +
                      "WHERE YEAR(dateCreate) = ? AND MONTH(dateCreate) = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("totalUsers", rs.getInt("totalUsers"));
                    stat.put("totalLearners", rs.getInt("totalLearners"));
                    stat.put("totalExperts", rs.getInt("totalExperts"));
                    stat.put("totalExamManagers", rs.getInt("totalExamManagers"));
                    stat.put("totalAdmins", rs.getInt("totalAdmins"));
                    stats.add(stat);
                }
            }
        }
        
        return stats;
    }

    // Thống kê lượng user theo năm
    public List<Map<String, Object>> getUserStatsByYear(int year) throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        
        String query = "SELECT " +
                      "COUNT(*) as totalUsers, " +
                      "SUM(CASE WHEN role = 'learner' THEN 1 ELSE 0 END) as totalLearners, " +
                      "SUM(CASE WHEN role = 'expert' THEN 1 ELSE 0 END) as totalExperts, " +
                      "SUM(CASE WHEN role = 'examManager' THEN 1 ELSE 0 END) as totalExamManagers, " +
                      "SUM(CASE WHEN role = 'admin' THEN 1 ELSE 0 END) as totalAdmins " +
                      "FROM [User] " +
                      "WHERE YEAR(dateCreate) = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("totalUsers", rs.getInt("totalUsers"));
                    stat.put("totalLearners", rs.getInt("totalLearners"));
                    stat.put("totalExperts", rs.getInt("totalExperts"));
                    stat.put("totalExamManagers", rs.getInt("totalExamManagers"));
                    stat.put("totalAdmins", rs.getInt("totalAdmins"));
                    stats.add(stat);
                }
            }
        }
        
        return stats;
    }

    // Thống kê lượng user theo khoảng thời gian
    public List<Map<String, Object>> getUserStatsByDateRange(String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        
        String query = "SELECT " +
                      "COUNT(*) as totalUsers, " +
                      "SUM(CASE WHEN role = 'learner' THEN 1 ELSE 0 END) as totalLearners, " +
                      "SUM(CASE WHEN role = 'expert' THEN 1 ELSE 0 END) as totalExperts, " +
                      "SUM(CASE WHEN role = 'examManager' THEN 1 ELSE 0 END) as totalExamManagers, " +
                      "SUM(CASE WHEN role = 'admin' THEN 1 ELSE 0 END) as totalAdmins " +
                      "FROM [User] " +
                      "WHERE CAST(dateCreate AS DATE) BETWEEN ? AND ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("totalUsers", rs.getInt("totalUsers"));
                    stat.put("totalLearners", rs.getInt("totalLearners"));
                    stat.put("totalExperts", rs.getInt("totalExperts"));
                    stat.put("totalExamManagers", rs.getInt("totalExamManagers"));
                    stat.put("totalAdmins", rs.getInt("totalAdmins"));
                    stats.add(stat);
                }
            }
        }
        
        return stats;
    }

    // Lấy danh sách đơn hàng của Expert
    public List<Map<String, Object>> getExpertOrders() throws SQLException {
        List<Map<String, Object>> orders = new ArrayList<>();
        
        String query = "SELECT " +
                      "o.orderID, o.paymentID, o.expertID, o.courseID, o.learnerID, " +
                      "o.orderDate, o.totalAmount, o.status, " +
                      "u.userID, u.gmail, u.phone, u.role, u.status as userStatus, " +
                      "u.fullName, u.avatar " +
                      "FROM Orders AS o " +
                      "JOIN Expert AS e ON o.expertID = e.expertID " +
                      "JOIN [User] AS u ON e.userID = u.userID " +
                      "ORDER BY o.orderDate DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                
                // Thông tin đơn hàng
                order.put("orderID", rs.getInt("orderID"));
                order.put("paymentID", rs.getString("paymentID"));
                order.put("expertID", rs.getInt("expertID"));
                order.put("courseID", rs.getInt("courseID"));
                order.put("learnerID", rs.getInt("learnerID"));
                order.put("orderDate", rs.getTimestamp("orderDate"));
                order.put("totalAmount", rs.getBigDecimal("totalAmount"));
                order.put("status", rs.getString("status"));
                
                // Thông tin người dùng (Expert)
                order.put("userID", rs.getInt("userID"));
                order.put("gmail", rs.getString("gmail"));
                order.put("phone", rs.getString("phone"));
                order.put("role", rs.getString("role"));
                order.put("userStatus", rs.getString("userStatus"));
                order.put("fullName", rs.getString("fullName"));
                order.put("avatar", rs.getString("avatar"));
                
                orders.add(order);
            }
        }
        
        return orders;
    }

    // Đếm số lượng đơn hàng hôm nay
    public int getTodayOrdersCount() throws SQLException {
        String query = "SELECT COUNT(*) as total " +
                      "FROM Orders " +
                      "WHERE CAST(orderDate AS DATE) = CAST(GETDATE() AS DATE)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Đếm số lượng đơn hàng tuần này
    public int getThisWeekOrdersCount() throws SQLException {
        String query = "SELECT COUNT(*) as total " +
                      "FROM Orders " +
                      "WHERE DATEPART(week, orderDate) = DATEPART(week, GETDATE()) " +
                      "AND YEAR(orderDate) = YEAR(GETDATE())";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Đếm số lượng đơn hàng tháng này
    public int getThisMonthOrdersCount() throws SQLException {
        String query = "SELECT COUNT(*) as total " +
                      "FROM Orders " +
                      "WHERE MONTH(orderDate) = MONTH(GETDATE()) " +
                      "AND YEAR(orderDate) = YEAR(GETDATE())";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Lấy thống kê chi tiết về đơn hàng
    public Map<String, Object> getOrdersStatistics() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        // Đếm tổng số đơn hàng
        String totalQuery = "SELECT COUNT(*) as total FROM Orders";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(totalQuery);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("totalOrders", rs.getInt("total"));
            }
        }
        
        // Đếm số đơn hàng hôm nay
        stats.put("todayOrders", getTodayOrdersCount());
        
        // Đếm số đơn hàng tuần này
        stats.put("thisWeekOrders", getThisWeekOrdersCount());
        
        // Đếm số đơn hàng tháng này
        stats.put("thisMonthOrders", getThisMonthOrdersCount());
        
        // Đếm số đơn hàng theo trạng thái
        String statusQuery = "SELECT status, COUNT(*) as count " +
                            "FROM Orders " +
                            "GROUP BY status";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(statusQuery);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> statusCounts = new HashMap<>();
            while (rs.next()) {
                statusCounts.put(rs.getString("status"), rs.getInt("count"));
            }
            stats.put("statusCounts", statusCounts);
        }
        
        return stats;
    }

    // Duyệt report
    public boolean approveReport(int reportID, String status) throws SQLException {
        // Chỉ cho phép 3 trạng thái
        if (!"pending".equalsIgnoreCase(status) && !"approved".equalsIgnoreCase(status) && !"rejected".equalsIgnoreCase(status)) {
            return false;
        }
        // Lấy thông tin report
        String getReportQuery = "SELECT reportTypeID, postID, courseID, reportedUserID FROM Report WHERE reportID = ?";
        int reportTypeID = 0;
        Integer postID = null;
        Integer courseID = null;
        Integer reportedUserID = null;
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(getReportQuery)) {
            ps.setInt(1, reportID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reportTypeID = rs.getInt("reportTypeID");
                    postID = rs.getObject("postID") != null ? rs.getInt("postID") : null;
                    courseID = rs.getObject("courseID") != null ? rs.getInt("courseID") : null;
                    reportedUserID = rs.getObject("reportedUserID") != null ? rs.getInt("reportedUserID") : null;
                }
            }
        }

        // Cập nhật trạng thái report
        String updateReportQuery = "UPDATE Report SET status = ? WHERE reportID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(updateReportQuery)) {
            ps.setString(1, status);
            ps.setInt(2, reportID);
            ps.executeUpdate();
        }

        // Xử lý theo loại report khi approved
        if ("approved".equalsIgnoreCase(status)) {
            if (reportTypeID == 1 && courseID != null) { // course
                String updateCourseQuery = "UPDATE Course SET status = 'blocked' WHERE courseID = ?";
                try (Connection conn = DBConnect.getInstance().getConnection();
                     PreparedStatement ps = conn.prepareStatement(updateCourseQuery)) {
                    ps.setInt(1, courseID);
                    ps.executeUpdate();
                }
            } else if ((reportTypeID == 2 || reportTypeID == 4) && reportedUserID != null) { // chat hoặc comment
                String updateUserQuery = "UPDATE [User] SET status = 'warning' WHERE userID = ?";
                try (Connection conn = DBConnect.getInstance().getConnection();
                     PreparedStatement ps = conn.prepareStatement(updateUserQuery)) {
                    ps.setInt(1, reportedUserID);
                    ps.executeUpdate();
                }
            } else if (reportTypeID == 3 && postID != null) { // post
                String updatePostQuery = "UPDATE Post SET status = 0 WHERE postID = ?";
                try (Connection conn = DBConnect.getInstance().getConnection();
                     PreparedStatement ps = conn.prepareStatement(updatePostQuery)) {
                    ps.setInt(1, postID);
                    ps.executeUpdate();
                }
            }
        }
        return true;
    }

    // Khóa nội dung bị báo cáo
    public boolean blockReportedContent(int reportID) throws SQLException {
        String query = "UPDATE Post SET status = 0 WHERE postID = (SELECT postID FROM Report WHERE reportID = ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reportID);
            return ps.executeUpdate() > 0;
        }
    }

    // Thống kê khóa học bán chạy trong tháng
    public List<Map<String, Object>> getTopSellingCoursesThisMonth() throws SQLException {
        List<Map<String, Object>> topCourses = new ArrayList<>();
        
        String query = "SELECT TOP 5 " +
                      "c.courseID, c.title, c.course_description, c.price, c.course_img, " +
                      "COUNT(cp.courseID) as totalSales, " +
                      "SUM(p.amount) as totalRevenue, " +
                      "e.expertID, u.fullName as expertName, " +
                      "AVG(cf.rating) as rating " +
                      "FROM Course c " +
                      "JOIN Course_Paid cp ON c.courseID = cp.courseID " +
                      "JOIN Payment p ON cp.paymentID = p.paymentID " +
                      "JOIN Expert e ON c.expertID = e.expertID " +
                      "JOIN [User] u ON e.userID = u.userID " +
                      "JOIN CourseFeedback AS cf ON c.courseID = cf.courseID " +
                      "WHERE MONTH(cp.datePaid) = MONTH(GETDATE()) " +
                      "AND YEAR(cp.datePaid) = YEAR(GETDATE()) " +
                      "AND p.status = 'completed' " +
                      "GROUP BY c.courseID, c.title, c.course_description, c.price, c.course_img, e.expertID, u.fullName " +
                      "ORDER BY totalSales DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("courseID", rs.getInt("courseID"));
                course.put("title", rs.getString("title"));
                course.put("description", rs.getString("course_description"));
                course.put("price", rs.getBigDecimal("price"));
                course.put("courseImg", rs.getString("course_img"));
                course.put("totalSales", rs.getInt("totalSales"));
                course.put("totalRevenue", rs.getBigDecimal("totalRevenue"));
                course.put("expertID", rs.getInt("expertID"));
                course.put("expertName", rs.getString("expertName"));
                course.put("rating", rs.getObject("rating") != null ? rs.getDouble("rating") : 0.0);
                topCourses.add(course);
            }
        }
        
        return topCourses;
    }

    // Lấy chi tiết khóa học cho admin
    public Map<String, Object> getCourseDetailForAdmin(int courseId) throws SQLException {
        CourseDAO courseDAO = new CourseDAO();
        CourseFeedbackDAO feedbackDAO = new CourseFeedbackDAO();
        CoursePaidDAO paidDAO = new CoursePaidDAO();
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) return null;
        var feedbacks = feedbackDAO.getFeedbacksByCourseID(courseId);
        java.math.BigDecimal totalRevenue = paidDAO.getTotalRevenue(courseId);
        int purchaseCount = paidDAO.getPurchaseCount(courseId);
        Map<String, Object> detail = new java.util.HashMap<>();
        detail.put("course", course);
        detail.put("feedbacks", feedbacks);
        detail.put("totalRevenue", totalRevenue);
        detail.put("purchaseCount", purchaseCount);
        return detail;
    }

    // Lấy doanh thu theo khóa học
    public Map<String, Object> getCourseRevenue(int courseId) throws SQLException {
        CoursePaidDAO paidDAO = new CoursePaidDAO();
        java.math.BigDecimal totalRevenue = paidDAO.getTotalRevenue(courseId);
        int purchaseCount = paidDAO.getPurchaseCount(courseId);
        Map<String, Object> revenue = new java.util.HashMap<>();
        revenue.put("totalRevenue", totalRevenue);
        revenue.put("purchaseCount", purchaseCount);
        return revenue;
    }

    // Thêm phương thức để đóng kết nối
    public void close() {
        DBConnect.getInstance().shutdown();
    }

    public List<String> getAllExpertNames() throws SQLException {
        return courseDAO.getAllExpertNames();
    }

    public List<String> getAllCategoryNames() throws SQLException {
        return courseDAO.getAllCategoryNames();
    }
} 