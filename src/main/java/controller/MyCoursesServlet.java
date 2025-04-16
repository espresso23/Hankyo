package controller;

import dao.CourseDAO;
import model.Learner;
import service.EnrollmentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/my-courses")
public class MyCoursesServlet extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");

        // Kiểm tra đăng nhập
        if (learner == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Lấy danh sách khóa học đã mua
            List<model.Course> purchasedCourses = courseDAO.getPurchasedCourses(learner.getLearnerID());
            
            // Tính tiến độ học tập cho mỗi khóa học
            for (model.Course course : purchasedCourses) {
                int progress = enrollmentService.calculateCourseProgress(learner.getLearnerID(), course.getCourseID());
                course.setProgress(progress);
            }

            request.setAttribute("courses", purchasedCourses);
            request.getRequestDispatcher("/my-courses.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách khóa học");
            request.getRequestDispatcher("/my-courses.jsp").forward(request, response);
        }
    }
} 