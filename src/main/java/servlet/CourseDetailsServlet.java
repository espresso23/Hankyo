package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dao.*;
import model.Course;
import model.CourseContent;
import model.CourseFeedback;
import model.Expert;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/course-details")
public class CourseDetailsServlet extends HttpServlet {
    private CourseDAO courseDAO;
    private CourseContentDAO courseContentDAO;
    private ExpertDAO expertDAO;
    private CoursePaidDAO coursePaidDAO;
    private EnrollmentDAO enrollmentDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            courseDAO = new CourseDAO();
            courseContentDAO = new CourseContentDAO();
            expertDAO = new ExpertDAO();
            coursePaidDAO = new CoursePaidDAO();
            enrollmentDAO = new EnrollmentDAO();
            
            // Cấu hình Gson với TypeAdapter cho LocalDateTime
            gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME));
                    }
                })
                .create();
        } catch (Exception e) {
            throw new ServletException("Không thể khởi tạo DAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String courseID = request.getParameter("courseID");
            System.out.println("nhận courseID " + courseID);
            if (courseID == null || courseID.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số courseID");
                return;
            }

            int courseId = Integer.parseInt(courseID);
            Course course = courseDAO.getCourseById(courseId);
            Learner learner = (Learner) request.getSession().getAttribute("learner");
            if (learner != null) {
                course.setPurchased(coursePaidDAO.isCoursePurchased(learner.getLearnerID(), course.getCourseID()));//da mua hay chua
                course.setEnrolled(enrollmentDAO.isEnroll(learner.getLearnerID(), course.getCourseID()));//da tham gia hay chua
                course.setLearnersCount(enrollmentDAO.countEnrolledLearners(course.getCourseID()));//co bao nhieu hoc vien
            }
            System.out.println(course.toString());
            if (course == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy khóa học");
                return;
            }

            Expert expert = expertDAO.getExpertById(course.getExpertID());//thong tin giang vien
            List<CourseContent> courseContents = courseContentDAO.listCourseContentsByCourseID(courseId);//noi dung khoa hoc

            // Tạo đối tượng JSON phù hợp với courseDetail
            Map<String, Object> courseDetail = new HashMap<>();

            // Thông tin cơ bản của khóa học
            courseDetail.put("c_id", course.getCourseID());
            courseDetail.put("c_name", course.getCourseTitle());
            courseDetail.put("c_category", course.getCategory() != null ? course.getCategory().getCategoryName() : "");
            courseDetail.put("c_status", course.getStatus());
            courseDetail.put("c_des", course.getCourseDescription());
            courseDetail.put("c_lastUpdate", course.getLastUpdated());
            courseDetail.put("numOfStudent", course.getLearnersCount());
            courseDetail.put("rating", course.getRating());
            courseDetail.put("c_img", course.getCourseImg());
            courseDetail.put("price", course.getPrice());
            courseDetail.put("originalPrice", course.getOriginalPrice());
            courseDetail.put("ratingCount", course.getRatingCount());
            courseDetail.put("enrolled", course.isEnrolled());

            // Thông tin expert
            if (expert != null) {
                Map<String, Object> expertInfo = new HashMap<>();
                expertInfo.put("expertID", expert.getExpertID());
                expertInfo.put("fullName", expert.getFullName());
                expertInfo.put("gmail", expert.getGmail());
                expertInfo.put("phone", expert.getPhone());
                expertInfo.put("certificate", expert.getCertificate());
                expertInfo.put("avatar", expert.getAvatar());
                expertInfo.put("gender", expert.getGender());
                courseDetail.put("expert", expertInfo);
            }

            // Thông tin nội dung khóa học
            courseDetail.put("c_content", courseContents);

            // Lấy thông tin đánh giá
            List<CourseFeedback> feedbacks = new CourseFeedbackDAO().getFeedbacksByCourseID(course.getCourseID());
            request.setAttribute("feedbacks", feedbacks);

            // Thiết lập response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // Gửi response
            String json = gson.toJson(courseDetail);
            response.getWriter().write(json);

            request.setAttribute("course", course);
            request.setAttribute("expert", expert);
            request.setAttribute("contents", courseContents);
            System.out.println(course.toString());
            System.out.println(expert.displayInfo());
            courseContents.forEach(System.out::println);

            // Forward sang JSP
            request.getRequestDispatcher("/course-details-learner.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "courseID không hợp lệ");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
            e.printStackTrace();
        }
    }

    private void handleGetCourseContentDetail(HttpServletResponse resp, Integer courseID) throws IOException, SQLException {
        setNoCacheHeaders(resp);
        List<CourseContent> courseContent = courseContentDAO.listCourseContentsByCourseID(courseID);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = gson.toJson(courseContent);
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }

    private void setNoCacheHeaders(HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
    }
} 