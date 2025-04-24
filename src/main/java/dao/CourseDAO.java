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
        String sql = "SELECT c.*," +
                    "(SELECT COUNT(*) FROM enrollments e WHERE e.courseID = c.courseID AND e.status = 'active') as student_count, " +
                    "(SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = c.courseID) as avg_rating, " +
                    "(SELECT COUNT(*) FROM CourseFeedback f WHERE f.courseID = c.courseID) as rating_count, " +
                    "ct.categoryID, ct.categoryName, ct.description as category_description, " +
                    "u.fullName as expert_name, u.avatar as expert_avatar, e.certificate as expert_certificate " +
                    "FROM Course c " +
                    "LEFT JOIN Category ct ON c.categoryID = ct.categoryID " +
                    "LEFT JOIN Expert e ON c.expertID = e.expertID " +
                    "LEFT JOIN [User] u ON e.userID = u.userID " +
                    "WHERE c.expertID = ? " +
                    "ORDER BY c.createdAt DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expertID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                
                // Thông tin cơ bản của khóa học
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCourseImg(rs.getString("course_img"));
                course.setStatus(rs.getString("status"));
                course.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
                course.setOriginalPrice(rs.getBigDecimal("original_price"));
                course.setDateCreated(rs.getDate("createdAt"));
                course.setLastUpdated(rs.getDate("updateAt"));
                course.setExpertID(expertID);

                // Thông tin danh mục
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("category_description"));
                course.setCategory(category);

                // Thông tin chuyên gia
                Expert expert = new Expert();
                expert.setExpertID(expertID);
                expert.setFullName(rs.getString("expert_name"));
                expert.setAvatar(rs.getString("expert_avatar"));
                expert.setCertificate(rs.getString("expert_certificate"));
                course.setExpert(expert);

                // Thông tin thống kê
                course.setLearnersCount(rs.getInt("student_count"));
                Double rating = rs.getObject("avg_rating") != null ? rs.getDouble("avg_rating") : 0.0;
                course.setRating(rating);
                course.setRatingCount(rs.getInt("rating_count"));

                courses.add(course);
            }
        }
        return courses;
    }

    public void addCourse(Course course) {
        String sql = "INSERT INTO Course (title, course_description, course_img, status, price, original_price, createdAt, updateAt, expertID, categoryID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseTitle());
            pstmt.setString(2, course.getCourseDescription());
            pstmt.setString(3, course.getCourseImg());
            pstmt.setString(4, course.getStatus());
            pstmt.setBigDecimal(5, course.getPrice());
            pstmt.setBigDecimal(6, course.getOriginalPrice());
            pstmt.setDate(7, new java.sql.Date(course.getDateCreated().getTime()));
            pstmt.setDate(8, new java.sql.Date(course.getLastUpdated().getTime()));
            pstmt.setInt(9, course.getExpertID());
            pstmt.setInt(10, course.getCategory().getCategoryID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể thêm khóa học vào cơ sở dữ liệu", e);
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
                "(SELECT COUNT(*) FROM enrollments en WHERE en.courseID = ? ) as student_count, " +
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
        String sql = "SELECT c.*, " +
                    "       (SELECT COUNT(*) FROM enrollments e WHERE e.courseID = c.courseID AND e.status = 'active') AS student_count, " +
                    "       (SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = c.courseID) AS avg_rating, " +
                    "       (SELECT COUNT(*) FROM CourseFeedback f WHERE f.courseID = c.courseID) AS rating_count, " +
                    "       ct.categoryName AS category_name, " +
                    "       u.fullName AS expert_name " +
                    "FROM Course c " +
                    "JOIN Category ct ON c.categoryID = ct.categoryID " +
                    "JOIN Expert ep ON c.expertID = ep.expertID " +
                    "JOIN [User] u ON ep.userID = u.userID " +
                    "WHERE c.status = 'active' AND c.price BETWEEN ? AND ?";

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
        String sql = "SELECT COUNT(*) FROM enrollments WHERE learnerID = ? AND courseID = ?";
        
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
        String sql = "SELECT * FROM Category ORDER BY categoryName";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
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

    public List<Course> getPurchasedCourses(int learnerID) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, \n" +
                "       cat.categoryName, \n" +
                "       u.fullName, \n" +
                "       u.avatar, \n" +
                "       e.certificate,\n" +
                "       (SELECT COUNT(*) \n" +
                "        FROM Enrollments \n" +
                "        WHERE courseID = c.courseID) AS learnersCount,\n" +
                "       (SELECT AVG(rating) \n" +
                "        FROM CourseFeedback f \n" +
                "        WHERE f.courseID = c.courseID) AS rating,\n" +
                "       (SELECT COUNT(*) \n" +
                "        FROM CourseFeedback \n" +
                "        WHERE courseID = c.courseID) AS ratingCount\n" +
                "FROM Course c\n" +
                "JOIN Category cat ON c.categoryID = cat.categoryID\n" +
                "JOIN Expert e ON c.expertID = e.expertID\n" +
                "JOIN [User] u ON u.userID = e.userID\n" +
                "JOIN Course_Paid cp on cp.courseID = c.courseID\n" +
                "WHERE cp.learnerID = ? ";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCourseImg(rs.getString("course_img"));
                course.setPrice(rs.getBigDecimal("price"));
                course.setOriginalPrice(rs.getBigDecimal("original_price"));
                course.setLastUpdated(rs.getTimestamp("updateAt"));
                
                // Set category
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                course.setCategory(category);
                
                // Set expert
                Expert expert = new Expert();
                expert.setExpertID(rs.getInt("expertID"));
                expert.setFullName(rs.getString("fullName"));
                expert.setAvatar(rs.getString("avatar"));
                expert.setCertificate(rs.getString("certificate"));
                course.setExpert(expert);
                
                // Set additional info
                course.setLearnersCount(rs.getInt("learnersCount"));
                course.setRating(rs.getDouble("rating"));
                course.setRatingCount(rs.getInt("ratingCount"));
                
                courses.add(course);
            }
        }
        return courses;
    }

    public int getFirstContentID(int courseID) throws SQLException {
        String sql = "SELECT TOP 1 course_contentID FROM Course_Content WHERE courseID = ? ORDER BY course_contentID ASC";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int contentID = rs.getInt(1);
                System.out.println("First Content ID for Course " + courseID + ": " + contentID);
                return contentID;
            }
            System.out.println("No content found for Course " + courseID);
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting first content ID for Course " + courseID + ": " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public Course getCourseByID(int courseID) throws SQLException {
        String sql = "SELECT c.*, cat.categoryID, cat.categoryName " +
                    "FROM Course c " +
                    "JOIN Category cat ON c.categoryID = cat.categoryID " +
                    "WHERE c.courseID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCourseImg(rs.getString("course_img"));
                course.setPrice(rs.getBigDecimal("price"));
                course.setOriginalPrice(rs.getBigDecimal("original_price"));
                course.setStatus(rs.getString("status"));
                course.setDateCreated(rs.getTimestamp("dateCreated"));
                course.setLastUpdated(rs.getTimestamp("lastUpdated"));
                
                // Set category information
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                course.setCategory(category);
                
                return course;
            }
        }
        return null;
    }

    // Lấy khóa học nổi bật cho expert dashboard
    public List<Course> getHighlightedCoursesForExpert(int expertId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.courseID, c.title, c.price, " +
                    "(SELECT COUNT(*) FROM enrollments e WHERE e.courseID = c.courseID AND e.status = 'active') as student_count, " +
                    "(SELECT AVG(CAST(f.rating AS FLOAT)) FROM CourseFeedback f WHERE f.courseID = c.courseID) as avg_rating, " +
                    "(SELECT COUNT(*) FROM CourseFeedback f WHERE f.courseID = c.courseID) as rating_count, " +
                    "(SELECT COUNT(*) * c.price FROM Course_Paid cp WHERE cp.courseID = c.courseID) as total_revenue, " +
                    "(SELECT COUNT(*) FROM Course_Paid cp WHERE cp.courseID = c.courseID) as total_sales " +
                    "FROM Course c " +
                    "WHERE c.expertID = ? AND c.status = 'active' " +
                    "ORDER BY student_count DESC, avg_rating DESC";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expertId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("courseID"));
                course.setCourseTitle(rs.getString("title"));
                course.setPrice(rs.getBigDecimal("price"));
                course.setLearnersCount(rs.getInt("student_count"));
                
                // Xử lý rating
                Double rating = rs.getObject("avg_rating") != null ? rs.getDouble("avg_rating") : 0.0;
                course.setRating(rating);
                course.setRatingCount(rs.getInt("rating_count"));

                // Xử lý doanh thu
                BigDecimal totalRevenue = rs.getBigDecimal("total_revenue");
                if (totalRevenue == null) {
                    totalRevenue = BigDecimal.ZERO;
                }
                course.setTotalRevenue(totalRevenue);

                // Xử lý tổng số lượng bán
                BigDecimal totalSales = BigDecimal.valueOf(rs.getLong("total_sales"));
                course.setTotalSales(totalSales);

                courses.add(course);
            }
        }
        return courses;
    }
}
