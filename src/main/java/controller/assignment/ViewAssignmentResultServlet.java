package controller.assignment;

import dao.*;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ViewAssignmentResultServlet", value = "/view-assignment-result")
public class ViewAssignmentResultServlet extends HttpServlet {
    private AssignmentResultDAO assignmentResultDAO;
    private AssignmentDAO assignmentDAO;
    private AssignmentTakenDAO assignmentTakenDAO;
    private QuestionAndAnswerDAO questionAndAnswerDAO;

    @Override
    public void init() {
        assignmentResultDAO = new AssignmentResultDAO();
        assignmentDAO = new AssignmentDAO();
        assignmentTakenDAO = new AssignmentTakenDAO();
        questionAndAnswerDAO = new QuestionAndAnswerDAO();
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

            // Lấy assignmentID từ request
            String assignmentIDStr = request.getParameter("assignmentID");
            String courseIDStr = request.getParameter("courseID");
            String courseContentIDStr = request.getParameter("courseContentID");
            
            if (assignmentIDStr == null || courseIDStr == null || courseContentIDStr == null) {
                response.sendRedirect("home");
                return;
            }

            int assignmentID = Integer.parseInt(assignmentIDStr);
            int courseID = Integer.parseInt(courseIDStr);
            int courseContentID = Integer.parseInt(courseContentIDStr);

            // Lấy thông tin bài tập
            Assignment assignment = assignmentDAO.getAssignmentById(assignmentID);
            if (assignment == null) {
                response.sendRedirect("home");
                return;
            }

            // Lấy kết quả bài làm gần nhất
            List<AssignmentResult> results = assignmentResultDAO.getResultsByLearnerAndAssignment(learner.getLearnerID(), assignmentID);
            if (results.isEmpty()) {
                response.sendRedirect("home");
                return;
            }

            // Lấy thông tin bài làm
            AssignmentTaken taken = assignmentTakenDAO.getAssignmentTakenById(results.get(0).getAssignTakenID());
            if (taken == null) {
                response.sendRedirect("home");
                return;
            }

            // Lấy danh sách câu hỏi của bài tập
            List<AssignmentQuestion> questions = assignmentDAO.getAssignmentWithQuestions(assignmentID).getAssignmentQuestions();
            
            // Lấy chi tiết câu trả lời cho mỗi câu hỏi
            for (AssignmentQuestion question : questions) {
                List<Answer> answers = questionAndAnswerDAO.getAllAnswerOfOneQuestion(question.getQuestionID());
                question.setAnswers(answers);
            }
            
            // Tạo map để map kết quả với câu hỏi tương ứng
            Map<Integer, AssignmentResult> resultMap = results.stream()
                .collect(Collectors.toMap(
                    AssignmentResult::getAssignmentQuesID,
                    result -> result,
                    (existing, replacement) -> replacement // Nếu có duplicate, giữ lại kết quả mới nhất
                ));

            // Tính toán thống kê
            int correctCount = 0;
            float totalMark = 0;
            for (AssignmentResult result : results) {
                if (result.isAnswerIsCorrect()) {
                    correctCount++;
                }
                totalMark += result.getMark();
            }

            // Set attributes cho JSP
            request.setAttribute("assignment", assignment);
            request.setAttribute("assignmentTaken", taken);
            request.setAttribute("questionResults", results);
            request.setAttribute("questions", questions);
            request.setAttribute("resultMap", resultMap);
            request.setAttribute("courseID", courseID);
            request.setAttribute("courseContentID", courseContentID);
            
            int finalCorrectCount = correctCount;
            float finalTotalMark = totalMark;
            request.setAttribute("assignmentResult", new AssignmentResult() {{
                setCorrectCount(finalCorrectCount);
                setTotalQuestions(results.size());
                setTotalMark(finalTotalMark);
                setScore(taken.getFinalMark());
            }});

            // Forward đến trang JSP
            request.getRequestDispatcher("view-assignment-result.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    public void destroy() {
    }
} 