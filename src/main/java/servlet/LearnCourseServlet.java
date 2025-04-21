//package servlet;
//
//import dao.AssignmentResultDAO;
//import dao.CourseDAO;
//import dao.CourseContentDAO;
//import model.AssignmentResult;
//import model.CourseContent;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//
//@WebServlet("/learn-course")
//public class LearnCourseServlet extends HttpServlet {
//    private CourseDAO courseDAO;
//    private CourseContentDAO courseContentDAO;
//    private AssignmentResultDAO assignmentResultDAO;
//
//    @Override
//    public void init() throws ServletException {
//        courseDAO = new CourseDAO();
//        courseContentDAO = new CourseContentDAO();
//        assignmentResultDAO = new AssignmentResultDAO();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        int learnerID = (int) session.getAttribute("learnerID");
//        int courseID = Integer.parseInt(request.getParameter("courseID"));
//        int contentID = Integer.parseInt(request.getParameter("contentID"));
//
//        CourseContent currentContent = null;
//        try {
//            currentContent = courseContentDAO.getCourseContentById(contentID);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        request.setAttribute("currentContent", currentContent);
//
//        // Nếu content hiện tại là assignment, lấy thông tin kết quả bài làm
//        if (currentContent != null && currentContent.getAssignment() != null) {
//            List<AssignmentResult> results = assignmentResultDAO.getResultsByLearnerAndAssignment(
//                learnerID,
//                currentContent.getAssignment().getAssignmentID()
//            );
//
//            if (!results.isEmpty()) {
//                // Tính toán số câu đúng và điểm số
//                int correctCount = 0;
//                int totalQuestions = results.size();
//
//                for (AssignmentResult result : results) {
//                    if (result.isAnswerIsCorrect()) {
//                        correctCount++;
//                    }
//                }
//
//                // Tạo đối tượng AssignmentResult tổng hợp
//                AssignmentResult summary = new AssignmentResult();
//                summary.setCorrectCount(correctCount);
//                summary.setTotalQuestions(totalQuestions);
//                summary.setScore((double) correctCount * 10 / totalQuestions); // Tính điểm thang 10
//
//                request.setAttribute("assignmentResult", summary);
//            }
//        }
//
//        request.getRequestDispatcher("course-content.jsp").forward(request, response);
//    }
//
//    // ... rest of the servlet ...
//}