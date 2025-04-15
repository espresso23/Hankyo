package controller;

import com.google.gson.Gson;
import dao.AssignmentDAO;
import dao.CourseContentDAO;
import model.Answer;
import model.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
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
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final CourseContentDAO courseContentDAO = new CourseContentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            String assignmentIDStr = request.getParameter("assignmentID");
            String questionIDStr = request.getParameter("questionID");

            if (!isValidId(assignmentIDStr)) {
                sendErrorResponse(response, "ID assignment không hợp lệ");
                return;
            }

            int assignmentID = Integer.parseInt(assignmentIDStr);

            switch (action) {
                case "getQuestions":
                    List<Question> questions = assignmentDAO.getQuestionsByAssignmentId(assignmentID);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");
            String assignmentIDStr = request.getParameter("assignmentID");
            String questionIDStr = request.getParameter("questionID");

            if (!isValidId(assignmentIDStr)) {
                sendErrorResponse(response, "ID assignment không hợp lệ");
                return;
            }

            int assignmentID = Integer.parseInt(assignmentIDStr);

            switch (action) {
                case "add":
                    handleAddQuestion(request, response, assignmentID);
                    break;
                case "update":
                    if (!isValidId(questionIDStr)) {
                        sendErrorResponse(response, "ID câu hỏi không hợp lệ");
                        return;
                    }
                    int questionID = Integer.parseInt(questionIDStr);
                    handleUpdateQuestion(request, response, questionID);
                    break;
                case "delete":
                    if (!isValidId(questionIDStr)) {
                        sendErrorResponse(response, "ID câu hỏi không hợp lệ");
                        return;
                    }
                    questionID = Integer.parseInt(questionIDStr);
                    handleDeleteQuestion(request, response, questionID);
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

    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response, int assignmentID) 
            throws IOException, ServletException, SQLException {
        Question question = new Question();
        question.setAssignmentID(assignmentID);
        question.setQuestionText(request.getParameter("questionText"));
        question.setQuestionType(request.getParameter("questionType"));
        question.setQuestionMark(Double.parseDouble(request.getParameter("questionMark")));

        // Xử lý file media
        handleMediaFiles(request, question);

        // Xử lý đáp án
        List<Answer> answers = processAnswers(request, question);
        question.setAnswers(answers);

        // Thêm câu hỏi vào database
        assignmentDAO.addQuestionToAssignment(assignmentID, question, answers);
        sendJsonResponse(response, true, "Thêm câu hỏi thành công");
    }

    private void handleUpdateQuestion(HttpServletRequest request, HttpServletResponse response, int questionID) 
            throws IOException, ServletException, SQLException {
        Question question = new Question();
        question.setQuestionID(questionID);
        question.setQuestionText(request.getParameter("questionText"));
        question.setQuestionType(request.getParameter("questionType"));
        question.setQuestionMark(Double.parseDouble(request.getParameter("questionMark")));

        // Xử lý file media
        handleMediaFiles(request, question);

        // Xử lý đáp án
        List<Answer> answers = processAnswers(request, question);
        question.setAnswers(answers);

        // Cập nhật câu hỏi trong database
        assignmentDAO.updateQuestion(question, answers);
        sendJsonResponse(response, true, "Cập nhật câu hỏi thành công");
    }

    private void handleDeleteQuestion(HttpServletRequest request, HttpServletResponse response, int questionID) 
            throws SQLException, IOException {
        assignmentDAO.deleteQuestion(questionID);
        sendJsonResponse(response, true, "Xóa câu hỏi thành công");
    }

    private void handleMediaFiles(HttpServletRequest request, Question question) throws IOException, ServletException {
        // Xử lý hình ảnh
        Part imagePart = request.getPart("questionImage");
        if (imagePart != null && imagePart.getSize() > 0) {
            String imageUrl = courseContentDAO.convertMediaToUrl(imagePart);
            question.setQuestionImage(imageUrl);
        }

        // Xử lý âm thanh
        Part audioPart = request.getPart("audioFile");
        if (audioPart != null && audioPart.getSize() > 0) {
            String audioUrl = courseContentDAO.convertMediaToUrl(audioPart);
            question.setAudioFile(audioUrl);
        }
    }

    private List<Answer> processAnswers(HttpServletRequest request, Question question) {
        List<Answer> answers = new ArrayList<>();
        String questionType = question.getQuestionType();

        switch (questionType) {
            case "MULTIPLE_CHOICE":
                String correctAnswer = request.getParameter("correctAnswer");
                for (int i = 1; i <= 5; i++) { // A, B, C, D, E
                    String answerText = request.getParameter("answerText_" + i);
                    if (answerText != null && !answerText.trim().isEmpty()) {
                        Answer answer = new Answer();
                        answer.setAnswerText(answerText);
                        answer.setCorrect(correctAnswer.equals(String.valueOf(i)));
                        answer.setOptionLabel((char)('A' + i - 1));
                        answers.add(answer);
                    }
                }
                break;

            case "TRUE_FALSE":
                String tfAnswer = request.getParameter("correctAnswer");
                Answer trueAnswer = new Answer();
                trueAnswer.setAnswerText("True");
                trueAnswer.setCorrect("1".equals(tfAnswer));
                trueAnswer.setOptionLabel('A');
                answers.add(trueAnswer);

                Answer falseAnswer = new Answer();
                falseAnswer.setAnswerText("False");
                falseAnswer.setCorrect("2".equals(tfAnswer));
                falseAnswer.setOptionLabel('B');
                answers.add(falseAnswer);
                break;

            case "SHORT_ANSWER":
                String answerText = request.getParameter("correctAnswer");
                Answer answer = new Answer();
                answer.setAnswerText(answerText);
                answer.setCorrect(true);
                answer.setOptionLabel('A');
                answers.add(answer);
                break;
        }

        return answers;
    }

    private Question getQuestionById(int questionID) throws SQLException {
        List<Question> questions = assignmentDAO.getQuestionsByAssignmentId(0); // Lấy tất cả câu hỏi
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