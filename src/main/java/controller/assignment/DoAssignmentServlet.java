package controller.assignment;

import com.google.gson.JsonObject;
import model.*;
import service.AssignmentService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "DoAssignmentServlet", value = "/do-assignment")
public class DoAssignmentServlet extends HttpServlet {
    private AssignmentService assignmentService;

    @Override
    public void init() throws ServletException {
        assignmentService = new AssignmentService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DoAssignmentServlet.doPost() called"); // Log 1

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        if (learner == null) {
            System.out.println("Learner not found in session"); // Log 2
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng đăng nhập để làm bài");
            out.print(jsonResponse.toString());
            return;
        }

        String action = request.getParameter("action");
        String assignmentIDStr = request.getParameter("assignmentID");

        System.out.println("Action: " + action); // Log 3
        System.out.println("AssignmentID: " + assignmentIDStr); // Log 4

        if (assignmentIDStr == null || assignmentIDStr.isEmpty()) {
            System.out.println("AssignmentID is null or empty"); // Log 5
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Thiếu thông tin bài tập");
            out.print(jsonResponse.toString());
            return;
        }

        try {
            int assignmentID = Integer.parseInt(assignmentIDStr);

            if ("create".equals(action)) {
                System.out.println("Creating new assignment taken for learner: " + learner.getLearnerID()); // Log 6

                AssignmentTaken taken = assignmentService.createAssignmentTaken(assignmentID, learner.getLearnerID());

                if (taken != null) {
                    System.out.println("Assignment taken created successfully with ID: " + taken.getAssignTakenID()); // Log 7
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("assignTakenID", taken.getAssignTakenID());
                } else {
                    System.out.println("Failed to create assignment taken"); // Log 8
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Không thể tạo bài làm mới");
                }
            } else {
                System.out.println("Invalid action: " + action); // Log 9
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Hành động không hợp lệ");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing assignmentID: " + e.getMessage()); // Log 10
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "ID bài tập không hợp lệ");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage()); // Log 11
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Có lỗi xảy ra: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DoAssignmentServlet.doGet() called"); // Log 12

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");

        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String assignmentIDStr = request.getParameter("assignmentID");
        String takenIDStr = request.getParameter("assignTakenID");

        System.out.println("GET - AssignmentID: " + assignmentIDStr); // Log 13
        System.out.println("GET - TakenID: " + takenIDStr); // Log 14

        if (assignmentIDStr == null || takenIDStr == null) {
            request.setAttribute("error", "Thiếu thông tin bài tập");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int assignmentID = Integer.parseInt(assignmentIDStr);
            int takenID = Integer.parseInt(takenIDStr);

            // Lấy thông tin assignment và danh sách câu hỏi
            Assignment assignment = assignmentService.getAssignmentWithQuestions(assignmentID);
            System.out.println("Assignment loaded: " + (assignment != null)); // Log 15

            if (assignment != null && assignment.getAssignmentQuestions() != null) {
                // Lấy câu trả lời cho mỗi câu hỏi
                for (AssignmentQuestion question : assignment.getAssignmentQuestions()) {
                    List<Answer> answers = assignmentService.getAllAnswerOfThisQuestion(question.getQuestionID());
                    System.out.println("Loaded " + (answers != null ? answers.size() : 0) +
                                     " answers for question " + question.getQuestionID()); // Log 16
                    question.setAnswers(answers);
                }
            }

            // Lấy thông tin bài làm
            AssignmentTaken taken = assignmentService.getAssignmentTakenById(takenID);
            System.out.println("Taken loaded: " + (taken != null)); // Log 17

            if (assignment != null && taken != null) {
                request.setAttribute("assignment", assignment);
                request.setAttribute("taken", taken);
                request.getRequestDispatcher("do-assignment.jsp").forward(request, response);
            } else {
                System.out.println("Assignment or Taken is null"); // Log 18
                request.setAttribute("error", "Không tìm thấy bài tập");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing IDs: " + e.getMessage()); // Log 19
            request.setAttribute("error", "ID không hợp lệ");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Unexpected error in doGet: " + e.getMessage()); // Log 20
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}