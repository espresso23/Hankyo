package controller.courseContentLearner;

import dao.AssignmentDAO;
import dao.AssignmentResultDAO;
import dao.AssignmentTakenDAO;
import dao.CourseContentDAO;
import model.*;
import service.CourseService;
import service.ProgressService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/learn-course")
public class LearnCourseServlet extends HttpServlet {
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
            model.User user = (model.User) session.getAttribute("user");
            boolean isAdmin = user != null && "admin".equalsIgnoreCase(user.getRole());
            if (learner == null && !isAdmin) {
                response.sendRedirect("login");
                return;
            }

            int courseID = Integer.parseInt(request.getParameter("courseID"));
            String courseContentIDStr = request.getParameter("courseContentID");

            // Kiểm tra xem học viên có đăng ký khóa học này không (admin thì bỏ qua)
            if (!isAdmin && !courseService.isEnrolled(learner.getLearnerID(), courseID)) {
                response.sendRedirect("my-courses");
                return;
            }

            // Lấy thông tin khóa học
            Course course = courseService.getCourseById(courseID);
            request.setAttribute("course", course);

            // Lấy danh sách nội dung khóa học
            List<CourseContent> contents = courseContentDAO.listCourseContentsByCourseID(courseID);

            // Nạp luôn danh sách câu hỏi cho tất cả assignment (nếu có)
            for (CourseContent content : contents) {
                if (content.getAssignment() != null && content.getAssignment().getAssignmentID() > 0) {
                    Assignment assignment = assignmentDAO.getAssignmentWithQuestions(content.getAssignment().getAssignmentID());
                    if (assignment != null) {
                        content.setAssignment(assignment);
                    }
                }
            }

            // Lấy danh sách contentID đã hoàn thành (chỉ learner mới có)
            List<Integer> completedContentIDs = isAdmin ? java.util.Collections.emptyList() : progressService.getCompletedContentIDs(learner.getLearnerID(), courseID);
            for (CourseContent content : contents) {
                if (!isAdmin && completedContentIDs.contains(content.getCourseContentID())) {
                    content.setCompleted(true);
                } else {
                    content.setCompleted(false);
                }
            }

            request.setAttribute("courseContents", contents);

            // Lấy nội dung hiện tại
            CourseContent currentContent = null;

            if (courseContentIDStr != null && !courseContentIDStr.isEmpty()) {
                int courseContentID = Integer.parseInt(courseContentIDStr);
                // Tìm currentContent từ danh sách contents đã load đầy đủ
                for (CourseContent content : contents) {
                    if (content.getCourseContentID() == courseContentID) {
                        currentContent = content;
                        break;
                    }
                }
            } else if (!contents.isEmpty()) {
                // Nếu không có courseContentID, lấy nội dung đầu tiên
                currentContent = contents.get(0);
            }

            if (currentContent == null && !contents.isEmpty()) {
                // Nếu vẫn không có currentContent nhưng có nội dung trong khóa học
                currentContent = contents.get(0);
            }

            // Gán currentContent vào request
            request.setAttribute("currentContent", currentContent);

            // Load thông tin AssignmentQuestion nếu là assignment
            if (currentContent.getAssignment() != null && currentContent.getAssignment().getAssignmentID() > 0) {
                Assignment assignment = assignmentDAO.getAssignmentWithQuestions(currentContent.getAssignment().getAssignmentID());
                if (assignment != null) {
                    currentContent.setAssignment(assignment);

                    // Lấy AssignmentTaken gần nhất của học viên (admin thì bỏ qua)
                    AssignmentTaken latestTaken = null;
                    if (!isAdmin) {
                        latestTaken = assignmentTakenDAO.getLatestAssignmentTaken(
                                learner.getLearnerID(),
                                assignment.getAssignmentID()
                        );
                    }

                    if (latestTaken != null) {
                        // Lấy kết quả bài làm từ AssignmentTaken gần nhất
                        List<AssignmentResult> results = assignmentResultDAO.getResultsByTakenID(latestTaken.getAssignTakenID());

                        if (!results.isEmpty()) {
                            // Tính toán thống kê
                            int totalQuestions = assignment.getAssignmentQuestions().size();
                            int correctCount = 0;
                            float totalMark = 0;
                            float maxMark = 0;

                            // Tính tổng điểm tối đa
                            for (AssignmentQuestion question : assignment.getAssignmentQuestions()) {
                                maxMark += question.getQuestionMark();
                            }

                            // Tính điểm thực tế
                            for (AssignmentResult result : results) {
                                if (result.isAnswerIsCorrect()) {
                                    correctCount++;
                                    totalMark += result.getMark();
                                }
                            }

                            // Tạo đối tượng AssignmentResult tổng hợp
                            AssignmentResult summary = new AssignmentResult();
                            summary.setCorrectCount(correctCount);
                            summary.setTotalQuestions(totalQuestions);
                            summary.setTotalMark(totalMark);
                            summary.setMaxMark(maxMark);

                            // Tính điểm theo thang 10
                            float score = (totalMark / maxMark) * 10;
                            summary.setScore(score);

                            // Chỉ đánh dấu hoàn thành nếu đạt >= 80%
                            if (score >= 8) {
                                currentContent.setCompleted(true);
                            } else {
                                currentContent.setCompleted(false);
                            }
                            // Gắn assignmentResult vào assignment
                            currentContent.getAssignment().setAssignmentResult(summary);

                            // Gán kết quả và AssignmentTaken vào request để hiển thị
                            request.setAttribute("assignmentResult", summary);
                            request.setAttribute("latestTaken", latestTaken);
                        }
                    }
                } else {
                    currentContent.setAssignment(null);
                }
            }

            // Lấy tiến độ của học viên (admin thì bỏ qua)
            int courseProgress = isAdmin ? 0 : progressService.calculateCourseProgress(learner.getLearnerID(), courseID);
            request.setAttribute("courseProgress", courseProgress);

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
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            if (learner == null) {
                response.sendRedirect("login");
                return;
            }

            int courseID = Integer.parseInt(request.getParameter("courseID"));
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String type = request.getParameter("type");

            boolean canMarkCompleted = false;

            if ("video".equals(type)) {
                boolean watchedAll = Boolean.parseBoolean(request.getParameter("watchedAll"));
                if (watchedAll) canMarkCompleted = true;
            } else if ("reading".equals(type)) {
                boolean readAll = Boolean.parseBoolean(request.getParameter("readAll"));
                if (readAll) canMarkCompleted = true;
            } else if ("assignment".equals(type)) {
                int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));
                // Lấy kết quả làm bài gần nhất
                AssignmentTaken latestTaken = assignmentTakenDAO.getLatestAssignmentTaken(learner.getLearnerID(), assignmentID);
                if (latestTaken != null) {
                    List<AssignmentResult> results = assignmentResultDAO.getResultsByTakenID(latestTaken.getAssignTakenID());
                    int totalQuestions = results.size();
                    int correctCount = 0;
                    for (AssignmentResult result : results) {
                        if (result.isAnswerIsCorrect()) correctCount++;
                    }
                    double percent = (double) correctCount / totalQuestions * 100;
                    if (percent >= 80) canMarkCompleted = true;
                }
            }

            if (canMarkCompleted) {
                progressService.markContentCompleted(learner.getLearnerID(), courseID, contentID);
                int progress = progressService.calculateCourseProgress(learner.getLearnerID(), courseID);
                System.out.println("[PROGRESS] Học viên " + learner.getLearnerID() + " đã hoàn thành content " + contentID + " của khóa " + courseID + ". Tiến độ mới: " + progress + "%");
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true, \"progress\": " + progress + ", \"message\": \"Cập nhật tiến độ thành công!\"}");
            } else {
                System.out.println("[PROGRESS] Học viên " + learner.getLearnerID() + " chưa đủ điều kiện hoàn thành content " + contentID + " của khóa " + courseID);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Bạn chưa hoàn thành đủ điều kiện!\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}