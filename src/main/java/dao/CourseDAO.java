package dao;

import model.Category;
import model.Course;
import model.Expert;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDAO {
    private ExpertDAO expertDAO = new ExpertDAO();

    public List<Course> getCourseByExpert(int expertID) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE expertID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expertID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Tạo đối tượng Course mới cho mỗi bản ghi
                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCourseImg(rs.getString("course_img"));
                course.setStatus(rs.getString("status"));
                course.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
                course.setDateCreated(rs.getDate("createdAt"));
                course.setLastUpdated(rs.getDate("updateAt"));

                courses.add(course);
            }
        }
        return courses;
    }

    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO Course (title, course_description, course_img, status, price, createdAt, updateAt, expertID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseTitle());
            pstmt.setString(2, course.getCourseDescription());
            pstmt.setString(3, course.getCourseImg());
            pstmt.setString(4, course.getStatus());
            pstmt.setBigDecimal(5, course.getPrice());
            pstmt.setDate(6, new java.sql.Date(course.getDateCreated().getTime()));
            pstmt.setDate(7, new java.sql.Date(course.getLastUpdated().getTime()));
            pstmt.setInt(8, course.getExpertID());

            pstmt.executeUpdate();
        }
    }

    public Course getCourseById(int id) throws SQLException {
        // Tách các câu lệnh SQL thành các phần riêng biệt
        String courseInfoSQL = "SELECT c.*, " +
                "cat.categoryID, cat.categoryName, cat.description as categoryDescription " +
                "FROM Course c " +
                "LEFT JOIN Category cat ON c.categoryID = cat.categoryID " +
                "WHERE c.courseID = ?";

        String expertInfoSQL = "SELECT e.expertID, e.userID as expert_userID, e.certificate as expert_certificate, " +
                "u.username as expert_username, u.gmail as expert_gmail, " +
                "u.phone as expert_phone, u.fullName as expert_fullName, " +
                "u.gender as expert_gender, u.avatar as expert_avatar, " +
                "u.status as expert_status " +
                "FROM Expert e " +
                "JOIN [User] u ON e.userID = u.userID " +
                "WHERE e.expertID = ?";

        String courseStatsSQL = "SELECT " +
                "(SELECT COUNT(*) FROM enrollments en WHERE en.courseID = ? AND en.status = 'active') as student_count, " +
                "(SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = ?) as avg_rating, " +
                "(SELECT COUNT(*) FROM CourseFeedback f WHERE f.courseID = ?) as rating_count";//dem so nguoi da feedback

        try (Connection conn = DBConnect.getInstance().getConnection()) {
            // Lấy thông tin cơ bản của khóa học
            Course course = getBasicCourseInfo(conn, courseInfoSQL, id);
            if (course == null) {
                return null;
            }

            // Lấy thông tin expert
            Expert expert = getExpertInfo(conn, expertInfoSQL, course.getExpertID());
            course.setExpert(expert);

            // Lấy thông tin thống kê
            Map<String, Object> stats = getCourseStats(conn, courseStatsSQL, id);
            course.setLearnersCount((Integer) stats.get("student_count"));
            course.setRating((Double) stats.get("avg_rating"));
            course.setRatingCount((Integer) stats.get("rating_count"));

            // Kiểm tra trạng thái đăng ký
            course.setEnrolled(isEnrolled(id, course.getExpertID()));

            return course;
        }
    }

    private Course getBasicCourseInfo(Connection conn, String sql, int courseId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCourseImg(rs.getString("course_img"));
                course.setStatus(rs.getString("status"));
                course.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
                course.setOriginalPrice(BigDecimal.valueOf(rs.getDouble("original_price")));
                course.setDateCreated(rs.getDate("createdAt"));
                course.setLastUpdated(rs.getDate("updateAt"));
                course.setExpertID(rs.getInt("expertID"));


                // Set category
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("categoryDescription"));
                course.setCategory(category);

                return course;
            }
        }
        return null;
    }

    private Expert getExpertInfo(Connection conn, String sql, int expertId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expertId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Expert expert = new Expert();
                expert.setExpertID(rs.getInt("expertID"));
                expert.setUserID(rs.getInt("expert_userID"));
                expert.setUsername(rs.getString("expert_username"));
                expert.setGmail(rs.getString("expert_gmail"));
                expert.setPhone(rs.getString("expert_phone"));
                expert.setFullName(rs.getString("expert_fullName"));
                expert.setGender(rs.getString("expert_gender"));
                expert.setStatus(rs.getString("expert_status"));
                expert.setCertificate(rs.getString("expert_certificate"));
                return expert;
            }
        }
        return null;
    }

    private Map<String, Object> getCourseStats(Connection conn, String sql, int courseId) throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setInt(2, courseId);
            pstmt.setInt(3, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.put("student_count", rs.getInt("student_count"));
                double rating = rs.getDouble("avg_rating");
                stats.put("avg_rating", rating != 0 ? rating : 0.0);
                stats.put("rating_count", rs.getInt("rating_count"));
            }
        }
        return stats;
    }

    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE Course SET title = ?, course_description = ?, course_img = ?, status = ?, price = ?, updateAt = ? WHERE courseID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseTitle());
            pstmt.setString(2, course.getCourseDescription());
            pstmt.setString(3, course.getCourseImg());
            pstmt.setString(4, course.getStatus());
            pstmt.setBigDecimal(5, course.getPrice());
            pstmt.setDate(6, new java.sql.Date(course.getLastUpdated().getTime()));
            pstmt.setInt(7, course.getCourseID());

            pstmt.executeUpdate();
        }
    }

    // Lấy danh sách khóa học theo khoảng giá
    public List<Course> getCoursesByPriceRange(double minPrice, double maxPrice) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE price BETWEEN ? AND ? AND status = 'active'";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, minPrice);
            pstmt.setDouble(2, maxPrice);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
        }
        return courses;
    }

    // Lấy các khóa học hot nhất dựa trên rating trung bình và số lượng đánh giá
    public List<Course> getHotCourses(int limit) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, " +
                    "COUNT(f.feedbackID) as review_count, " +
                    "AVG(CAST(f.rating AS FLOAT)) as avg_rating " +
                    "FROM Course c " +
                    "LEFT JOIN CourseFeedback f ON c.courseID = f.courseID " +
                    "GROUP BY c.courseID, c.title, c.course_description, c.course_img, " +
                    "c.status, c.price, c.createdAt, c.updateAt, c.expertID " +
                    "HAVING COUNT(f.feedbackID) > 0 " +
                    "ORDER BY avg_rating DESC, review_count DESC " +
                    "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
        }
        return courses;
    }

    // Lấy khóa học mới nhất
    public List<Course> getNewestCourses(int limit) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*,(SELECT COUNT(*) FROM enrollments e WHERE e.courseID = c.courseID AND e.status = 'active') as student_count,\n" +
                "           (SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = c.courseID) as avg_rating, \n" +
                "\t\t   (SELECT COUNT(*) FROM CourseFeedback f WHERE f.courseID =  c.courseID) as rating_count, ct.categoryName as category_name,\n" +
                "\t\t  u.fullName as expert_name\n" +
                "             FROM Course c join Category ct on c.categoryID = ct.categoryID join Expert ep on c.expertID = ep.expertID join [User] u on ep.userID = u.userID  WHERE c.status = 'active' \n" +
                "             ORDER BY c.createdAt DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
        }
        return courses;
    }

    // Tìm kiếm khóa học theo từ khóa (tên hoặc mô tả)
    public List<Course> searchCourses(String keyword) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql =
                "SELECT c.*, " +
                        "       (SELECT COUNT(*) FROM enrollments e WHERE e.courseID = c.courseID AND e.status = 'active') AS student_count, " +
                        "       (SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = c.courseID) AS avg_rating, " +
                        "       (SELECT COUNT(*) FROM CourseFeedback f WHERE f.courseID = c.courseID) AS rating_count, " +
                        "       ct.categoryName AS category_name, " +
                        "       u.fullName AS expert_name " +
                        "FROM Course c " +
                        "JOIN Category ct ON c.categoryID = ct.categoryID " +
                        "JOIN Expert ep ON c.expertID = ep.expertID " +
                        "JOIN [User] u ON ep.userID = u.userID " +
                        "WHERE c.status = 'active' AND (c.title LIKE ? OR c.course_description LIKE ?)";

        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
        }
        return courses;
    }

    // Phương thức hỗ trợ để trích xuất thông tin khóa học từ ResultSet
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();

        course.setCourseID(rs.getObject("courseID") != null ? rs.getInt("courseID") : null);
        course.setCourseTitle(rs.getString("title"));
        course.setCourseDescription(rs.getString("course_description"));
        course.setCourseImg(rs.getString("course_img"));
        course.setStatus(rs.getString("status"));
        course.setPrice(rs.getBigDecimal("price"));
        course.setOriginalPrice(rs.getBigDecimal("original_price"));
        course.setDateCreated(rs.getDate("createdAt"));
        course.setLastUpdated(rs.getDate("updateAt"));
        course.setExpertID(rs.getObject("expertID") != null ? rs.getInt("expertID") : null);

        // Handle Category object
        Category category = new Category();
        category.setCategoryName(rs.getString("category_name") != null ? rs.getString("category_name") : null);
        course.setCategory(category);

        course.setLearnersCount(rs.getObject("student_count") != null ? rs.getInt("student_count") : 0);

        // Handle Expert object
        Expert expert = new Expert();
        expert.setFullName(rs.getString("expert_name") != null ? rs.getString("expert_name") : null);
        course.setExpert(expert);

        Double rating = rs.getObject("avg_rating") != null ? rs.getDouble("avg_rating") : 0;
        course.setRating(rating);

        course.setRatingCount(rs.getObject("rating_count") != null ? rs.getInt("rating_count") : 0);

        return course;
    }

    // Đăng ký khóa học
    public void enrollCourse(int learnerID, int courseID) throws SQLException {
        String sql = "INSERT INTO enrollments (learnerID, courseID, enrollDate, status) VALUES (?, ?, GETDATE(), 'active')";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, courseID);
            pstmt.executeUpdate();
        }
    }

    // Kiểm tra xem học viên đã đăng ký khóa học chưa
    public boolean isEnrolled(int learnerID, int courseID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE learnerID = ? AND courseID = ? AND status = 'active'";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, courseID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Lấy danh sách khóa học đã đăng ký của học viên
    public List<Course> getEnrolledCourses(int learnerID) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, e.enrollDate, e.status as enrollStatus " +
                    "FROM Course c " +
                    "JOIN enrollments e ON c.courseID = e.courseID " +
                    "WHERE e.learnerID = ? " +
                    "ORDER BY e.enrollDate DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, learnerID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                // Thêm thông tin đăng ký
                course.setEnrollDate(rs.getTimestamp("enrollDate"));
                course.setEnrollStatus(rs.getString("enrollStatus"));
                courses.add(course);
            }
        }
        return courses;
    }

    // Cập nhật trạng thái đăng ký
    public void updateEnrollmentStatus(int learnerID, int courseID, String status) throws SQLException {
        String sql = "UPDATE enrollments SET status = ? WHERE learnerID = ? AND courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, learnerID);
            pstmt.setInt(3, courseID);
            pstmt.executeUpdate();
        }
    }

    // Lấy thống kê số lượng học viên đăng ký theo khóa học
    public int getEnrollmentCount(int courseID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE courseID = ? AND status = 'active'";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Lấy số lượng học viên của khóa học
    public int getStudentCount(int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE courseID = ? AND status = 'active'";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Lấy điểm đánh giá trung bình của khóa học
    public double getAverageRating(int courseId) throws SQLException {
        String sql = "SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double rating = rs.getDouble(1);
                return rating != 0 ? rating : 0.0;
            }
        }
        return 0.0;
    }

    // Tạo bảng Category
    public void createCategoryTable() throws SQLException {
        String sql = "CREATE TABLE Category (" +
                    "categoryID INT IDENTITY(1,1) PRIMARY KEY, " +
                    "categoryName NVARCHAR(100) NOT NULL, " +
                    "description NVARCHAR(500), " +
                    "parentID INT, " +
                    "FOREIGN KEY (parentID) REFERENCES Category(categoryID)" +
                    ")";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }


    // Thêm category mới
    public int addCategory(String name, String description, Integer parentID) throws SQLException {
        String sql = "INSERT INTO Category (categoryName, description, parentID) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            if (parentID != null) {
                pstmt.setInt(3, parentID);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // Lấy danh sách category
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category ORDER BY parentID NULLS FIRST, categoryName";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("description"));
                category.setParentID(rs.getObject("parentID", Integer.class));
                categories.add(category);
            }
        }
        return categories;
    }

    // Lấy khóa học theo category
    public List<Course> getCoursesByCategory(int categoryID) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, u.fullName as expertName, cat.categoryName, " +
                    "(SELECT COUNT(*) FROM Course_Paid cp WHERE cp.courseID = c.courseID) as learnersCount, " +
                    "(SELECT AVG(rating) FROM CourseFeedback cf WHERE cf.courseID = c.courseID) as rating, " +
                    "(SELECT COUNT(*) FROM CourseFeedback cf WHERE cf.courseID = c.courseID) as ratingCount " +
                    "FROM Course c " +
                    "JOIN Expert e ON c.expertID = e.expertID " +
                    "JOIN Category cat ON c.categoryID = cat.categoryID " +
                    "JOIN [User] u ON u.userID = e.userID " +
                    "WHERE c.categoryID = ? " +
                    "ORDER BY c.createdAt DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, categoryID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getInt("courseID"));
                    course.setCourseTitle(rs.getString("title"));
                    course.setCourseDescription(rs.getString("course_description"));
                    course.setCourseImg(rs.getString("course_img"));
                    course.setPrice(rs.getBigDecimal("price"));
                    course.setRating(rs.getDouble("rating"));
                    course.setRatingCount(rs.getInt("ratingCount"));
                    course.setLearnersCount(rs.getInt("learnersCount"));
                    course.setDateCreated(rs.getTimestamp("createdAt"));
                    
                    // Set expert info
                    Expert expert = new Expert();
                    expert.setExpertID(rs.getInt("expertID"));
                    expert.setFullName(rs.getString("expertName"));
                    course.setExpert(expert);
                    
                    // Set category info
                    Category category = new Category();
                    category.setCategoryID(rs.getInt("categoryID"));
                    category.setCategoryName(rs.getString("categoryName"));
                    course.setCategory(category);
                    
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Lấy khóa học theo level
    public List<Course> getCoursesByLevel(String level) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, " +
                    "(SELECT COUNT(*) FROM enrollments e WHERE e.courseID = c.courseID AND e.status = 'active') as student_count, " +
                    "(SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = c.courseID) as avg_rating " +
                    "FROM Course c WHERE c.level = ? AND c.status = 'active'";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, level);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        }
        return courses;
    }
}
