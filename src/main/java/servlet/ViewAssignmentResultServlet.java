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
import java.sql.SQLException;
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
        System.out.println("ViewAssignmentResultServlet.doGet() called"); // Log 1

        try {
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            
            if (learner == null) {
                System.out.println("Learner not found in session"); // Log 2
                request.setAttribute("error", "Vui lòng đăng nhập để xem kết quả");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            String assignTakenIDStr = request.getParameter("assignTakenID");
            System.out.println("assignTakenID parameter: " + assignTakenIDStr); // Log 3

            if (assignTakenIDStr == null || assignTakenIDStr.isEmpty()) {
                System.out.println("Missing assignTakenID parameter"); // Log 4
                request.setAttribute("error", "Thiếu thông tin bài làm");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            int assignTakenID = Integer.parseInt(assignTakenIDStr);
            System.out.println("Parsed assignTakenID: " + assignTakenID); // Log 5

            // Lấy thông tin bài làm
            AssignmentTaken assignmentTaken = assignmentTakenDAO.getAssignmentTakenById(assignTakenID);
            System.out.println("AssignmentTaken found: " + (assignmentTaken != null)); // Log 6

            if (assignmentTaken == null) {
                System.out.println("AssignmentTaken not found"); // Log 7
                request.setAttribute("error", "Không tìm thấy bài làm");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách câu hỏi
            List<AssignmentQuestion> questions = assignmentQuestionDAO.getQuestionsByAssignmentId(assignmentTaken.getAssignmentID());
            System.out.println("Number of questions found: " + questions.size()); // Log 8

            // Lấy kết quả
            List<AssignmentResult> results = assignmentResultDAO.getResultsByTakenID(assignTakenID);
            System.out.println("Number of results found: " + results.size()); // Log 9

            // Tính điểm
            float totalMark = 0;
            float maxMark = 0;
            int correctCount = 0;

            for (AssignmentQuestion question : questions) {
                maxMark += question.getQuestionMark();
                for (AssignmentResult result : results) {
                    if (result.getAssignmentQuesID() == question.getAssignQuesID()) {
                        totalMark += result.getMark();
                        if (result.isAnswerIsCorrect()) {
                            correctCount++;
                        }
                    }
                }
            }

            System.out.println("Calculated marks:"); // Log 10
            System.out.println("totalMark: " + totalMark);
            System.out.println("maxMark: " + maxMark);
            System.out.println("correctCount: " + correctCount);

            // Tính điểm trên thang 10
            float score = (totalMark / maxMark) * 10;
            System.out.println("Final score: " + score); // Log 11

            // Set attributes
            request.setAttribute("assignmentTaken", assignmentTaken);
            request.setAttribute("questions", questions);
            request.setAttribute("results", results);
            request.setAttribute("totalMark", totalMark);
            request.setAttribute("maxMark", maxMark);
            request.setAttribute("correctCount", correctCount);
            request.setAttribute("score", score);

            // Forward to view
            request.getRequestDispatcher("/view-assignment-result.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage()); // Log 12
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage()); // Log 14
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
} 