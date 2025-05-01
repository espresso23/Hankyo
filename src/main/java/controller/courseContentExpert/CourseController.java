package controller.courseContentExpert;

import dao.CategoryDAO;
import dao.CourseContentDAO;
import dao.CourseDAO;
import dao.CourseFeedbackDAO;
import dao.CoursePaidDAO;
import model.Category;
import model.Course;
import model.Expert;
import model.CourseFeedback;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/course")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1MB
    maxFileSize = 1024 * 1024 * 5,    // 5MB
    maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class CourseController extends HttpServlet {
    private CourseDAO courseDAO = new CourseDAO(); // Đảm bảo courseDAO được khởi tạo
    private CourseContentDAO courseContentDAO = new CourseContentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        System.out.println(session.toString());
        switch (action) {
            case "list":
                listCourses(request, response, expert);
                break;
            case "addForm":
                showAddForm(request, response);
                break;
            case "detail": // THÊM ACTION DETAIL
                try {
                    showCourseDetail(request, response, expert);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addCourse(request, response, expert);
                    break;
                case "update":
                    updateCourse(request, response, expert);
                    break;
                default:
                    response.sendRedirect("error.jsp");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    private void showCourseDetail(HttpServletRequest request, HttpServletResponse response, Expert expert)
            throws ServletException, IOException, SQLException {
        try {
            int courseId = Integer.parseInt(request.getParameter("courseID"));
            CourseDAO courseDAO = new CourseDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            CourseFeedbackDAO feedbackDAO = new CourseFeedbackDAO();
            CoursePaidDAO paidDAO = new CoursePaidDAO();

            // Lấy thông tin khóa học
            Course course = courseDAO.getCourseById(courseId);
            if (course == null || course.getExpertID() != expert.getExpertID()) {
                response.sendRedirect("error.jsp");
                return;
            }

            // Lấy danh sách danh mục cho form cập nhật
            List<Category> categories = categoryDAO.getAllCategories();
            System.out.println("Number of categories: " + categories.size()); // Debug log

            // Lấy thông tin đánh giá
            double averageRating = feedbackDAO.getAverageRating(courseId);
            int ratingCount = feedbackDAO.getRatingCount(courseId);
            Map<Integer, Double> ratingPercentages = feedbackDAO.getRatingPercentages(courseId);

            // Lấy danh sách đánh giá chi tiết
            List<CourseFeedback> feedbacks = feedbackDAO.getFeedbacksByCourseID(courseId);

            // Lấy thông tin doanh thu
            BigDecimal totalRevenue = paidDAO.getTotalRevenue(courseId);
            int purchaseCount = paidDAO.getPurchaseCount(courseId);

            // Cập nhật thông tin vào đối tượng Course
            course.setRating(averageRating);
            course.setRatingCount(ratingCount);
            course.setLearnersCount(purchaseCount);
            course.setTotalRevenue(totalRevenue);

            // Set attributes
            request.setAttribute("course", course);
            request.setAttribute("categories", categories);
            request.setAttribute("feedbackStats", ratingPercentages);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("purchaseCount", purchaseCount);
            request.setAttribute("feedbacks", feedbacks);

            // Thêm dòng này để debug
            System.out.println("Course data: " + course.toString());
            System.out.println("Total Revenue: " + totalRevenue);
            System.out.println("Rating Percentages: " + ratingPercentages);
            System.out.println("Number of feedbacks: " + (feedbacks != null ? feedbacks.size() : 0));

            request.getRequestDispatcher("courseDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("course?action=list");
        }
    }

    private void listCourses(HttpServletRequest request, HttpServletResponse response, Expert expert) throws ServletException, IOException {
        if (expert == null) {
            System.out.println("expert not found");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Course> courses = courseDAO.getCourseByExpert(expert.getExpertID());
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("listCourse.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();
        System.out.println("Number of categories: " + categories.size()); // Debug log
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("addCourse.jsp").forward(request, response);
    }

    private void addCourse(HttpServletRequest request, HttpServletResponse response, Expert expert) throws ServletException, IOException {
        try {
            // Kiểm tra các trường bắt buộc
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String categoryIDStr = request.getParameter("categoryID");
            String priceStr = request.getParameter("price");
            String status = request.getParameter("status");

            if (title == null || title.trim().isEmpty() ||
                description == null || description.trim().isEmpty() ||
                categoryIDStr == null || categoryIDStr.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                status == null || status.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
                showAddForm(request, response);
                return;
            }

            int categoryID = Integer.parseInt(categoryIDStr);
            BigDecimal price = new BigDecimal(priceStr);
            String originalPriceStr = request.getParameter("originalPrice");
            BigDecimal originalPrice = (originalPriceStr != null && !originalPriceStr.isEmpty()) ?
                    new BigDecimal(originalPriceStr) : price;

            // Xử lý ảnh
            String fileImg = null;
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getSubmittedFileName(filePart);
                if (fileName != null && !fileName.isEmpty()) {
                    fileImg = courseContentDAO.convertMediaToUrl(filePart);
                }
            }

            // Tạo đối tượng Course
            Course course = new Course();
            course.setCourseTitle(title);
            course.setCourseDescription(description);
            course.setPrice(price);
            course.setOriginalPrice(originalPrice);
            course.setStatus(status);
            course.setCourseImg(fileImg);
            course.setExpertID(expert.getExpertID());
            course.setDateCreated(new java.sql.Date(System.currentTimeMillis()));
            course.setLastUpdated(new java.sql.Date(System.currentTimeMillis()));

            // Set category
            Category category = new Category();
            category.setCategoryID(categoryID);
            course.setCategory(category);

            // Thêm vào database
            courseDAO.addCourse(course);
            request.getSession().setAttribute("message", "Thêm khóa học thành công");
            request.getSession().setAttribute("messageType", "success");
            response.sendRedirect("course?action=list");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá tiền không hợp lệ");
            showAddForm(request, response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            showAddForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi thêm khóa học: " + e.getMessage());
            showAddForm(request, response);
        }
    }
    private void updateCourse(HttpServletRequest request, HttpServletResponse response, Expert expert)
            throws ServletException, IOException, SQLException {
        int courseId = Integer.parseInt(request.getParameter("courseID"));

        // Lấy thông tin khóa học hiện tại
        Course existingCourse = courseDAO.getCourseById(courseId);
        if (existingCourse == null || existingCourse.getExpertID() != expert.getExpertID()) {
            response.sendRedirect("error.jsp");
            return;
        }

        try {
            // Cập nhật thông tin cơ bản
            existingCourse.setCourseTitle(request.getParameter("title"));
            existingCourse.setCourseDescription(request.getParameter("description"));
            existingCourse.setPrice(BigDecimal.valueOf(Double.parseDouble(request.getParameter("price"))));
            
            // Xử lý giá gốc
            String originalPriceStr = request.getParameter("originalPrice");
            if (originalPriceStr != null && !originalPriceStr.isEmpty()) {
                existingCourse.setOriginalPrice(BigDecimal.valueOf(Double.parseDouble(originalPriceStr)));
            } else {
                existingCourse.setOriginalPrice(existingCourse.getPrice());
            }
            
            // Cập nhật trạng thái
            existingCourse.setStatus(request.getParameter("status"));
            
            // Cập nhật danh mục
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            Category category = new Category();
            category.setCategoryID(categoryID);
            existingCourse.setCategory(category);
            
            // Cập nhật thời gian
            existingCourse.setLastUpdated(new Date());

            // Xử lý ảnh mới (nếu có)
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String newImageUrl = courseContentDAO.convertMediaToUrl(filePart);
                if (newImageUrl != null && !newImageUrl.isEmpty()) {
                    existingCourse.setCourseImg(newImageUrl);
                }
            }

            // Cập nhật vào database
            courseDAO.updateCourse(existingCourse);

            request.getSession().setAttribute("message", "Cập nhật khóa học thành công");
            request.getSession().setAttribute("messageType", "success");
            response.sendRedirect("course?action=detail&courseID=" + courseId);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Giá tiền không hợp lệ");
            request.getSession().setAttribute("messageType", "danger");
            response.sendRedirect("course?action=detail&courseID=" + courseId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
            request.getSession().setAttribute("messageType", "danger");
            response.sendRedirect("course?action=detail&courseID=" + courseId);
        }
    }

    private String getSubmittedFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return "";
    }
}
