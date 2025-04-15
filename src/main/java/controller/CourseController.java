package controller;

import dao.CourseContentDAO;
import dao.CourseDAO;
import model.Course;
import model.Expert;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebServlet("/course")
@MultipartConfig
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
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addCourse(request, response);
                    break;
                case "update":
                    updateCourse(request, response, expert);
                    break;
                default:
                    response.sendRedirect("error.jsp");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error occurred", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid input format", e);
        }
    }
    private void showCourseDetail(HttpServletRequest request, HttpServletResponse response, Expert expert)
            throws ServletException, IOException, SQLException {
        int courseId = Integer.parseInt(request.getParameter("courseID"));
        Course course = courseDAO.getCourseById(courseId);

        if (course == null || course.getExpertID() != expert.getExpertID()) {
            response.sendRedirect("error.jsp");
            return;
        }

        // Thêm dòng này để debug
        System.out.println("Course data: " + course.toString());

        request.setAttribute("course", course);
        request.getRequestDispatcher("courseDetail.jsp").forward(request, response);
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
        request.getRequestDispatcher("addCourse.jsp").forward(request, response);
    }

    private void addCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priceParam = request.getParameter("price");
        String status = request.getParameter("status");
        System.out.println(title + " " + description + " " + priceParam + " " + status);
        double price = 0.0;
        if (priceParam != null && !priceParam.trim().isEmpty()) {
            try {
                price = Double.parseDouble(priceParam);
            } catch (NumberFormatException e) {
                throw new ServletException("Giá khóa học không hợp lệ", e);
            }
        }

        // **Xử lý upload ảnh**
        Part filePart = request.getPart("image");
        System.out.println(title + " " + description + " " + priceParam + " " + status + " " + filePart.toString());
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            imageUrl = courseContentDAO.convertMediaToUrl(filePart);
        } else {
            throw new ServletException("Ảnh khóa học là bắt buộc");
        }

        Course course = new Course();
        course.setCourseTitle(title);
        course.setCourseDescription(description);
        course.setCourseImg(imageUrl);
        course.setPrice(BigDecimal.valueOf(price));
        course.setStatus(status);
        course.setExpertID(expert.getExpertID());
        course.setDateCreated(new Date());
        course.setLastUpdated(new Date());

        try {
            courseDAO.addCourse(course);
            response.sendRedirect("course?action=list");
        } catch (SQLException e) {
            throw new ServletException(e);
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

        // Cập nhật thông tin
        existingCourse.setCourseTitle(request.getParameter("title"));
        existingCourse.setCourseDescription(request.getParameter("description"));
        existingCourse.setPrice(BigDecimal.valueOf(Double.parseDouble(request.getParameter("price"))));
        existingCourse.setStatus(request.getParameter("status"));
        existingCourse.setLastUpdated(new Date());

        // Xử lý ảnh mới (nếu có)
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            String newImageUrl = courseContentDAO.convertMediaToUrl(filePart);
            existingCourse.setCourseImg(newImageUrl);
        }

        // Cập nhật vào database
        courseDAO.updateCourse(existingCourse);

        request.getSession().setAttribute("message", "Cập nhật thành công");
        request.getSession().setAttribute("messageType", "success");
        response.sendRedirect("course?action=detail&courseID=" + courseId);    }

}
