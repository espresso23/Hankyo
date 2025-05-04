package controller.courseContentLearner;

import dao.CourseDAO;
import model.Course;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/suggest-courses")
public class SuggestCoursesServlet extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject json = new JSONObject(sb.toString());
            double score = json.optDouble("score", 0);
            String skill = json.optString("skill", "");
            List<Course> suggested = courseDAO.suggestCourses(skill, score);
            suggested.forEach(System.out::println);
            if (suggested.isEmpty()) {
                resp.getWriter().write("<div class='alert alert-info'>Chưa có khóa học phù hợp để đề xuất.</div>");
                return;
            }
            StringBuilder html = new StringBuilder();
            html.append("<div class='row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3'>");
            for (Course c : suggested) {
                html.append("<div class='col'><div class='card h-100 shadow-sm'>");
                html.append("<img src='" + (c.getCourseImg() != null ? c.getCourseImg() : "asset/png/icon/logo.jpg") + "' class='card-img-top' alt='Khóa học'>");
                html.append("<div class='card-body'>");
                html.append("<h5 class='card-title'>" + c.getCourseTitle() + "</h5>");
                html.append("<p class='card-text'>" + (c.getCourseDescription() != null ? c.getCourseDescription() : "") + "</p>");
                html.append("<div class='mb-2'><span class='badge bg-primary me-1'>" + (c.getCategory() != null && c.getCategory().getCategoryName() != null ? c.getCategory().getCategoryName() : "") + "</span></div>");
                if (c.getExpert() != null && c.getExpert().getFullName() != null) {
                    html.append("<div class='d-flex align-items-center mb-2'><img src='" + (c.getExpert().getAvatar() != null ? c.getExpert().getAvatar() : "asset/png/icon/logo.jpg") + "' class='rounded-circle me-2' style='width:28px;height:28px;object-fit:cover;'>");
                    html.append("<span>GV: <b>" + c.getExpert().getFullName() + "</b></span></div>");
                }
                html.append("<div class='mb-1 small text-muted'>Học viên: <b>" + c.getLearnersCount() + "</b> | Đánh giá: <b>" + c.getRating() + "/5</b> (" + c.getRatingCount() + ")</div>");
                html.append("</div>");
                html.append("<div class='card-footer text-end'><a href='courseDetail.jsp?courseID=" + c.getCourseID() + "' class='btn btn-sm btn-outline-primary'>Xem chi tiết</a></div>");
                html.append("</div></div>");
            }
            html.append("</div>");
            resp.getWriter().write(html.toString());
        } catch (Exception e) {
            resp.getWriter().write("<div class='alert alert-danger'>Lỗi khi lấy gợi ý khóa học: " + e.getMessage() + "</div>");
        }
    }
} 