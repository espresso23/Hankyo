package servlet;

import com.google.gson.JsonObject;
import dao.AssignmentResultDAO;
import dao.AssignmentTakenDAO;
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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetAssignmentResultServlet", value = "/get-assignment-result")
public class GetAssignmentResultServlet extends HttpServlet {
    private AssignmentResultDAO assignmentResultDAO;
    private AssignmentTakenDAO assignmentTakenDAO;

    @Override
    public void init() {
        assignmentResultDAO = new AssignmentResultDAO();
        assignmentTakenDAO = new AssignmentTakenDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            if (learner == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Vui lòng đăng nhập để xem kết quả");
                out.println(jsonResponse.toString());
                return;
            }

            // Lấy assignmentID từ request
            String assignmentIDStr = request.getParameter("assignmentID");
            if (assignmentIDStr == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Thiếu thông tin bài tập");
                out.println(jsonResponse.toString());
                return;
            }

            int assignmentID = Integer.parseInt(assignmentIDStr);

            // Lấy kết quả bài làm
            List<AssignmentResult> results = assignmentResultDAO.getResultsByLearnerAndAssignment(learner.getLearnerID(), assignmentID);
            if (results.isEmpty()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không tìm thấy kết quả bài làm");
                out.println(jsonResponse.toString());
                return;
            }

            // Lấy thông tin bài làm
            AssignmentTaken taken = assignmentTakenDAO.getAssignmentTakenByID(results.get(0).getAssignTakenID());
            if (taken == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không tìm thấy thông tin bài làm");
                out.println(jsonResponse.toString());
                return;
            }

            // Tạo response
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("score", taken.getFinalMark());
            jsonResponse.addProperty("correctCount", results.stream().filter(AssignmentResult::isAnswerIsCorrect).count());
            jsonResponse.addProperty("totalQuestions", results.size());
            jsonResponse.addProperty("doneCount", taken.getDoneQues());

            // Thêm thông tin chi tiết từng câu hỏi
            List<JsonObject> questionsList = new ArrayList<>();
            for (AssignmentResult result : results) {
                JsonObject question = new JsonObject();
                question.addProperty("questionText", result.getQuestionText());
                question.addProperty("userAnswer", result.getAnswerLabel());
                question.addProperty("correctAnswer", result.getCorrectAnswer());
                question.addProperty("isCorrect", result.isAnswerIsCorrect());
                questionsList.add(question);
            }
            jsonResponse.add("questions", new com.google.gson.Gson().toJsonTree(questionsList));

        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Dữ liệu không hợp lệ");
            e.printStackTrace();
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Có lỗi xảy ra");
            e.printStackTrace();
        }

        out.println(jsonResponse.toString());
    }
} 