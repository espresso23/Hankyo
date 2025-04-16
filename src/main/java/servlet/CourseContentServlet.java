package servlet;

import dao.CourseContentDAO;
import dao.ProgressDAO;
import model.Course;
import model.CourseContent;
import model.Progress;
import model.Learner;
import service.CourseService;
import service.ProgressService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CourseContentServlet", value = "/learn-course")
public class CourseContentServlet extends HttpServlet {
    private CourseContentDAO courseContentDAO;
    private ProgressService progressService;
    private CourseService courseService;

    @Override
    public void init() {
        courseContentDAO = new CourseContentDAO();
        progressService = new ProgressService();
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        if (courseContentDAO != null) {
            courseContentDAO.closeConnection();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            if (learner == null) {
                response.sendRedirect("login");
                return;
            }

            int courseID = Integer.parseInt(request.getParameter("courseID"));
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            
            // Kiểm tra xem học viên có đăng ký khóa học này không
            if (!courseService.isEnrolled(learner.getLearnerID(), courseID)) {
                response.sendRedirect("my-courses");
                return;
            }
            
            // Lấy thông tin khóa học
            Course course = courseService.getCourseById(courseID);
            request.setAttribute("course", course);
            
            // Lấy danh sách nội dung khóa học
            List<CourseContent> contents = courseContentDAO.listCourseContentsByCourseID(courseID);
            request.setAttribute("courseContents", contents);
            
            // Lấy nội dung hiện tại
            CourseContent currentContent = courseContentDAO.getCourseContentById(contentID);
            if (currentContent == null) {
                response.sendRedirect("my-courses");
                return;
            }
            request.setAttribute("currentContent", currentContent);
            
            // Lấy tiến độ của học viên
            int progress = progressService.calculateCourseProgress(learner.getLearnerID(), courseID);
            request.setAttribute("courseProgress", progress);
            
            // Chuyển hướng đến trang học
            request.getRequestDispatcher("/course-content.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            response.sendRedirect("my-courses");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            if (learner == null) {
                response.sendRedirect("login");
                return;
            }

            int courseID = Integer.parseInt(request.getParameter("courseID"));
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            
            // Cập nhật tiến độ
            progressService.markContentCompleted(learner.getLearnerID(), courseID, contentID);
            
            // Tính toán tiến độ mới
            int progress = progressService.calculateCourseProgress(learner.getLearnerID(), courseID);
            
            // Trả về kết quả dưới dạng JSON
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"progress\": " + progress + "}");
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            response.sendRedirect("my-courses");
        }
    }
} 