package controller.courseContentLearner;

import dao.AssignmentResultDAO;
import dao.CourseContentDAO;
import dao.AssignmentDAO;
import dao.AssignmentTakenDAO;
import model.*;
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
    private AssignmentResultDAO assignmentResultDAO;
    private AssignmentDAO assignmentDAO;
    private AssignmentTakenDAO assignmentTakenDAO;

    @Override
    public void init() {
        courseContentDAO = new CourseContentDAO();
        progressService = new ProgressService();
        courseService = new CourseService();
        assignmentResultDAO = new AssignmentResultDAO();
        assignmentDAO = new AssignmentDAO();
        assignmentTakenDAO = new AssignmentTakenDAO();
    }

    @Override
    public void destroy() {
        if (courseContentDAO != null) {
            courseContentDAO.closeConnection();
        }
        if (assignmentDAO != null) {
            assignmentDAO.closeConnection();
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
            String courseContentIDStr = request.getParameter("courseContentID");
            
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
            CourseContent currentContent = null;
            if (courseContentIDStr != null) {
                int courseContentID = Integer.parseInt(courseContentIDStr);
                currentContent = contents.stream()
                    .filter(c -> c.getCourseContentID() == courseContentID)
                    .findFirst()
                    .orElse(null);
            }

            // Nếu không có nội dung nào, lấy nội dung đầu tiên
            if (currentContent == null && !contents.isEmpty()) {
                currentContent = contents.get(0);
            }

            // Xử lý bài tập nếu có
            if (currentContent != null) {
                Assignment assignment = currentContent.getAssignment();
                if (assignment != null) {
                    // Lấy thông tin bài tập đã làm
                    AssignmentTaken latestTaken = assignmentTakenDAO.getLatestAssignmentTaken(
                        learner.getLearnerID(), 
                        currentContent.getCourseContentID()
                    );
                    if (latestTaken != null) {
                        request.setAttribute("latestTaken", latestTaken);
                    }
                }
            }

            // Lấy tiến độ của học viên
            int courseProgress = progressService.calculateCourseProgress(learner.getLearnerID(), courseID);
            request.setAttribute("courseProgress", courseProgress);

            // Đặt các thuộc tính vào request
            request.setAttribute("course", course);
            request.setAttribute("courseContents", contents);
            request.setAttribute("currentContent", currentContent);
            request.setAttribute("isEmpty", currentContent == null);

            // Chuyển hướng đến trang học
            request.getRequestDispatcher("/learn-course.jsp").forward(request, response);

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