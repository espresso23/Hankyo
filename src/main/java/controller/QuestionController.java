package controller;

import com.google.gson.Gson;
//import dao.AssignmentDAO;
import dao.CourseContentDAO;
import dao.ExamDAO;
import model.Answer;
import model.Question;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "QuestionController", urlPatterns = {"/question"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
public class QuestionController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(QuestionController.class.getName());
    private final Gson gson = new Gson();
    private Connection connection = DBConnect.getInstance().getConnection();
    private final ExamDAO examDAO = new ExamDAO(connection);

    private final CourseContentDAO courseContentDAO = new CourseContentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            String examIDStr = request.getParameter("examID");
            String questionIDStr = request.getParameter("questionID");

            if (!isValidId(examIDStr)) {
                sendErrorResponse(response, "ID exam không hợp lệ");
                return;
            }
            int examID = Integer.parseInt(request.getParameter("examID"));

            switch (action) {
                case "getQuestions":
                    List<Question> questions = examDAO.getQuestionsByExamId(examID);
                    sendJsonResponse(response, true, "Lấy danh sách câu hỏi thành công", questions);
                    break;
                case "getQuestion":
                    if (!isValidId(questionIDStr)) {
                        sendErrorResponse(response, "ID câu hỏi không hợp lệ");
                        return;
                    }
                    int questionID = Integer.parseInt(questionIDStr);
                    Question question = getQuestionById(questionID);
                    if (question != null) {
                        sendJsonResponse(response, true, "Lấy câu hỏi thành công", question);
                    } else {
                        sendErrorResponse(response, "Không tìm thấy câu hỏi");
                    }
                    break;
                default:
                    sendErrorResponse(response, "Action không hợp lệ");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            sendErrorResponse(response, "Lỗi database: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            sendErrorResponse(response, e.getMessage());
        }
    }




    private Question getQuestionById(int questionID) throws SQLException {
        List<Question> questions = examDAO.getQuestionsByExamId(0); // Lấy tất cả câu hỏi
        for (Question question : questions) {
            if (question.getQuestionID() == questionID) {
                return question;
            }
        }
        return null;
    }

    private boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty() && id.matches("\\d+");
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("success", false);
        errorData.put("message", message);
        response.getWriter().write(gson.toJson(errorData));
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("success", success);
        data.put("message", message);
        response.getWriter().write(gson.toJson(data));
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, Object data) throws IOException {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", success);
        responseData.put("message", message);
        responseData.put("data", data);
        response.getWriter().write(gson.toJson(responseData));
    }
}