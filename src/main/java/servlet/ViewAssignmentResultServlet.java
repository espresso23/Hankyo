package servlet;

import dao.AssignmentQuestionDAO;
import dao.AssignmentResultDAO;
import dao.AssignmentTakenDAO;
import model.AssignmentQuestion;
import model.AssignmentResult;
import model.AssignmentTaken;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/view-assignment-result")
public class ViewAssignmentResultServlet extends HttpServlet {
    private AssignmentResultDAO assignmentResultDAO;
    private AssignmentQuestionDAO assignmentQuestionDAO;
    private AssignmentTakenDAO assignmentTakenDAO;

    @Override
    public void init() throws ServletException {
        assignmentResultDAO = new AssignmentResultDAO();
        assignmentQuestionDAO = new AssignmentQuestionDAO();
        assignmentTakenDAO = new AssignmentTakenDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");

        if (learner == null) {
            response.sendRedirect("login");
            return;
        }

        String assignmentIDStr = request.getParameter("assignmentID");
        if (assignmentIDStr == null || assignmentIDStr.isEmpty()) {
            request.setAttribute("error", "Thiếu thông tin bài tập");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int assignmentID = Integer.parseInt(assignmentIDStr);
            int learnerID = learner.getLearnerID();

            // Lấy thông tin AssignmentTaken
            AssignmentTaken taken = assignmentTakenDAO.getAssignmentTakenByLearnerAndAssignment(learnerID, assignmentID);
            if (taken == null) {
                request.setAttribute("error", "Không tìm thấy bài làm của bạn");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách câu hỏi của assignment
            List<AssignmentQuestion> questions = assignmentQuestionDAO.getQuestionsByAssignmentId(assignmentID);
            if (questions == null || questions.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy câu hỏi của bài tập");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Lấy kết quả chi tiết từng câu
            List<AssignmentResult> results = assignmentResultDAO.getResultsByLearnerAndAssignment(learnerID, assignmentID);

            // Tính toán thống kê
            int totalQuestions = questions.size();
            int correctCount = 0;
            float totalMark = 0;
            float maxMark = 0;

            // Tính tổng điểm tối đa
            for (AssignmentQuestion question : questions) {
                maxMark += question.getQuestionMark();
            }

            // Tính điểm thực tế
            for (AssignmentResult result : results) {
                if (result.isAnswerIsCorrect()) {
                    correctCount++;
                    totalMark += result.getMark();
                }
            }

            // Set attributes
            request.setAttribute("taken", taken);
            request.setAttribute("questions", questions);
            request.setAttribute("results", results);
            request.setAttribute("totalQuestions", totalQuestions);
            request.setAttribute("correctCount", correctCount);
            request.setAttribute("totalMark", totalMark);
            request.setAttribute("maxMark", maxMark);
            request.setAttribute("score", (totalMark / maxMark) * 10); // Tính điểm thang 10

            // Forward to JSP
            request.getRequestDispatcher("view-assignment-result.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID bài tập không hợp lệ");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi truy xuất dữ liệu");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
} 