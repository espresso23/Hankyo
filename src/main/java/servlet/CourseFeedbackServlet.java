package servlet;

import dao.CourseFeedbackDAO;
import model.Course;
import model.CourseFeedback;
import model.Learner;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@WebServlet("/course-feedback")
public class CourseFeedbackServlet extends HttpServlet {
    private CourseFeedbackDAO feedbackDAO;

    @Override
    public void init() {
        feedbackDAO = new CourseFeedbackDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Kiểm tra đăng nhập
            Learner learner = (Learner) request.getSession().getAttribute("learner");
            if (learner == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Vui lòng đăng nhập để đánh giá\"}");
                return;
            }

            // Lấy thông tin từ request
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            float rating = Float.parseFloat(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            // Kiểm tra xem học viên đã đánh giá chưa
            if (hasFeedback(learner.getLearnerID(), courseID)) {
                response.getWriter().write("{\"success\": false, \"message\": \"Bạn đã đánh giá khóa học này rồi\"}");
                return;
            }

            // Tạo đối tượng feedback mới
            CourseFeedback feedback = new CourseFeedback();
            Course course = new Course();
            course.setCourseID(courseID);
            feedback.setCourse(course);
            feedback.setLearner(learner);
            feedback.setRating(rating);
            feedback.setComment(comment);
            feedback.setCreatedAt(new Date());

            // Lưu feedback vào database
            boolean success = feedbackDAO.addFeedback(feedback);

            if (success) {
                response.getWriter().write("{\"success\": true}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra khi lưu đánh giá\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra\"}");
        }
    }

    private boolean hasFeedback(int learnerID, int courseID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CourseFeedback WHERE learnerID = ? AND courseID = ?";
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
} 