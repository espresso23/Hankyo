package controller.learner;

import dao.LearnerDAO;
import model.Learner;
import service.EnrollmentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/enroll-course")
public class EnrollCourseServlet extends HttpServlet {
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final LearnerDAO learnerDAO = new LearnerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");

        if (learner == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            
            // Kiểm tra và thực hiện đăng ký
            if (enrollmentService.enrollIfNotExists(learner.getLearnerID(), courseID)) {
                // Chuyển hướng đến trang học nếu đăng ký thành công
                response.sendRedirect("learn-course?courseID=" + courseID + "&contentID=" + contentID);
            } else {
                // Nếu đã đăng ký rồi, chuyển hướng về trang khóa học của tôi
                response.sendRedirect("my-courses");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("my-courses");
        }
    }
} 