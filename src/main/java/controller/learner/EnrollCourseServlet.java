package controller.learner;

import dao.LearnerDAO;
import model.Learner;
import org.cloudinary.json.JSONObject;
import service.EnrollmentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/enroll-course")
public class EnrollCourseServlet extends HttpServlet {
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final LearnerDAO learnerDAO = new LearnerDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        
        if (learner == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Vui lòng đăng nhập để tham gia khóa học");
            out.print(jsonResponse.toString());
            return;
        }

        try {
            int courseId = Integer.parseInt(request.getParameter("courseID"));
            boolean success = enrollmentService.enrollIfNotExists(learner.getLearnerID(), courseId);
            
            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Đăng ký khóa học thành công");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Bạn đã đăng ký khóa học này trước đó");
            }
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "ID khóa học không hợp lệ");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Có lỗi xảy ra khi đăng ký khóa học");
            e.printStackTrace();
        }
        
        out.print(jsonResponse.toString());
    }
} 