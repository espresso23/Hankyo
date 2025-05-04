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
            System.out.println(score + " " + skill);
            List<Course> suggested = courseDAO.suggestCourses(skill, score);
            suggested.forEach(System.out::println);
            if (suggested.isEmpty()) {
                resp.getWriter().write("<div class='alert alert-info'>Chưa có khóa học phù hợp để đề xuất.</div>");
                return;
            }
            StringBuilder html = new StringBuilder();
            html.append("<style>\n" +
                    ".suggest-card {\n" +
                    "    background: linear-gradient(303deg, #ffeded, #c6c6c600);\n" +
                    "    border-radius: 18px;\n" +
                    "    box-shadow: 8px 7px 3px 0px rgba(160, 229, 215, 0.13), 0 1.5px 6px rgb(0 0 0 / 8%);\n" +
                    "    transition: transform 0.18s cubic-bezier(.4,0,.2,1), box-shadow 0.18s;\n" +
                    "    padding: 10px 10px 0 10px;\n" +
                    "    min-width: 181px;\n" +
                    "    max-width: 280px;\n" +
                    "    border: darkgray;\n" +
                    "    margin-bottom: 10px;\n" +
                    "    display: flex\n" +
                    ";\n" +
                    "    flex-direction: column;\n" +
                    "    height: 100%;\n" +
                    "}\n" +
                    ".suggest-card .card-img-top {\n" +
                    "height: 90px; object-fit: cover; border-radius: 12px 12px 0 0;\n" +
                    "}\n" +
                    ".suggest-card .card-title { font-size: 1rem; font-weight: 600; margin-bottom: 4px;}\n" +
                    ".suggest-card .card-body { padding: 10px 10px 6px 10px;}\n" +
                    ".suggest-card .badge { font-size: 0.75em; background: #fff; color: #f48fb1; font-weight: 600;}\n" +
                    ".suggest-card .btn { font-size: 0.95em; border-radius: 8px; margin-top: 6px;}\n" +
                    ".suggest-card .rating { font-size: 0.98em; color: #ffc107; font-weight: 600;}\n" +
                     ".btn:hover{" +
                    "background-color: #ffe7e7 !important; color: #fff !important; border-color: lightpink !important;}"+
                    "</style>");
            html.append("<div class='row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3'>");
            for (Course c : suggested) {
                double rating = c.getRating();
                int ratingCount = c.getRatingCount();
                int fullStars = (int) rating;
                boolean halfStar = (rating - fullStars) >= 0.5;
                int emptyStars = 5 - fullStars - (halfStar ? 1 : 0);
                StringBuilder stars = new StringBuilder();
                for (int i = 0; i < fullStars; i++) stars.append("<i class='fas fa-star' style='color:#ffc107;'></i>");
                if (halfStar) stars.append("<i class='fas fa-star-half-alt' style='color:#ffc107;'></i>");
                for (int i = 0; i < emptyStars; i++) stars.append("<i class='far fa-star' style='color:#ffc107;'></i>");
                html.append("<div class='col'><div class='suggest-card'>");
                html.append("<img src='" + (c.getCourseImg() != null ? c.getCourseImg() : "asset/png/icon/logo.jpg") + "' class='card-img-top' alt='Khóa học'>");
                html.append("<div class='card-body'>");
                html.append("<h6 class='card-title'>" + c.getCourseTitle() + "</h6>");
                html.append("<div style='font-size:0.93rem;color:#666;margin-bottom:7px;'>" + (c.getCourseDescription() != null ? c.getCourseDescription() : "") + "</div>");
                html.append("<div style='margin-bottom:7px;'><span class='badge bg-primary'>" + (c.getCategory() != null && c.getCategory().getCategoryName() != null ? c.getCategory().getCategoryName() : "") + "</span></div>");
                html.append("<div style='font-size:0.92em;color:#555;margin-bottom:6px;'><i class='fas fa-chalkboard-teacher'></i> GV: <b>" + (c.getExpert() != null && c.getExpert().getFullName() != null ? c.getExpert().getFullName() : "") + "</b></div>");
                html.append("<div style='font-size:0.92em;color:#555;margin-bottom:6px;'><i class='fas fa-users'></i> Học viên: " + c.getLearnersCount() + "</div>");
                html.append("<div class='rating' style='margin-bottom:6px;'><i class='fas fa-star' style='color:#ffc107;'></i> Đánh giá: ");
                if (ratingCount > 0) {
                    html.append(stars.toString() + " <span style='color:#333;font-weight:500;'>" + String.format("%.1f", rating) + "/5</span> (<span style='color:#888;'>" + ratingCount + "</span>)");
                } else {
                    html.append("<span style='color:#888;'>Chưa có đánh giá</span>");
                }
                html.append("</div>");
                html.append("<a href='course-details?courseID=" + c.getCourseID() + "' class='btn btn-outline-primary btn-sm' style='width:100%; background-color: white !important; border-color: lightpink !important; color: black !important;'>Xem chi tiết</a>");
                html.append("</div></div></div>");
            }
            html.append("</div>");
            resp.getWriter().write(html.toString());
        } catch (Exception e) {
            resp.getWriter().write("<div class='alert alert-danger'>Lỗi khi lấy gợi ý khóa học: " + e.getMessage() + "</div>");
        }
    }
} 