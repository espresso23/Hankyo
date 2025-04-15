package controller;

import com.google.gson.Gson;
import dao.AssignmentDAO;
import dao.QuestionAndAnswerDAO;
import model.Answer;
import model.Assignment;
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

@WebServlet(name = "EditAssignmentController", urlPatterns = {"/edit-assignment", "/editAssignment"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
public class EditAssignmentController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditAssignmentController.class.getName());
    private final Gson gson = new Gson();
    private AssignmentDAO assignmentDAO;
    private QuestionAndAnswerDAO questionAndAnswerDAO;
    private static final int MAX_QUESTIONS = 50;
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif"};
    private static final String[] ALLOWED_AUDIO_TYPES = {"audio/mpeg", "audio/wav"};

    @Override
    public void init() throws ServletException {
        super.init();
        assignmentDAO = new AssignmentDAO();
        questionAndAnswerDAO = new QuestionAndAnswerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String assignmentIDStr = request.getParameter("assignmentID");
            String courseIDStr = request.getParameter("courseID");

            if (!isValidId(assignmentIDStr) || !isValidId(courseIDStr)) {
                sendErrorResponse(response, "ID không hợp lệ");
                return;
            }

            int assignmentID = Integer.parseInt(assignmentIDStr);
            int courseID = Integer.parseInt(courseIDStr);

            Assignment assignment = assignmentDAO.getAssignmentById(assignmentID);
            if (assignment == null) {
                sendErrorResponse(response, "Không tìm thấy assignment");
                return;
            }

            List<Question> questions = questionAndAnswerDAO.getQuestionsByAssignmentId(assignmentID);

            request.setAttribute("assignment", assignment);
            request.setAttribute("questions", questions);
            request.setAttribute("courseID", courseID);

            request.getRequestDispatcher("/editAssignment.jsp").forward(request, response);

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

        String action = request.getParameter("action");
        if (action == null) {
            sendErrorResponse(response, "Thiếu tham số action");
            return;
        }

        try {
            switch (action) {
                case "update":
                    handleUpdateAssignment(request, response);
                    break;
                case "delete":
                    handleDeleteQuestion(request, response);
                    break;
                case "addQuestion":
                    handleAddQuestion(request, response);
                    break;
                case "updateQuestion":
                    handleUpdateQuestion(request, response);
                    break;
                default:
                    sendErrorResponse(response, "Action không hợp lệ");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            sendErrorResponse(response, "Lỗi: " + e.getMessage());
        }
    }

    private void handleUpdateAssignment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String assignmentIDStr = request.getParameter("assignmentID");
            String courseIDStr = request.getParameter("courseID");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String questionCountStr = request.getParameter("questionCount");

            if (!isValidId(assignmentIDStr) || !isValidId(courseIDStr)) {
                sendErrorResponse(response, "ID không hợp lệ");
                return;
            }

            if (title == null || title.trim().isEmpty()) {
                sendErrorResponse(response, "Tiêu đề không được để trống");
                return;
            }

            int assignmentID = Integer.parseInt(assignmentIDStr);
            int courseID = Integer.parseInt(courseIDStr);
            int questionCount = Integer.parseInt(questionCountStr);

            Assignment assignment = new Assignment();
            assignment.setAssignmentID(assignmentID);
            assignment.setAssignmentTitle(title);
            assignment.setDescription(description);

            assignmentDAO.updateAssignment(assignment);

            for (int i = 0; i < questionCount; i++) {
                String questionIDStr = request.getParameter("questionID_" + i);
                
                if (questionIDStr != null && !questionIDStr.equals("new")) {
                    int questionID = Integer.parseInt(questionIDStr);
                    Question question = createQuestionFromRequest(request, i, assignmentID);
                    question.setQuestionID(questionID);
                    List<Answer> answers = createAnswersFromRequest(request, i, questionID);
                    questionAndAnswerDAO.updateQuestion(question, answers);
                    LOGGER.log(Level.INFO, "Đã cập nhật câu hỏi ID: {0}", questionID);
                } else {
                    Question question = createQuestionFromRequest(request, i, assignmentID);
                    List<Answer> answers = createAnswersFromRequest(request, i, 0);
                    question.setAnswers(answers);
                    int newQuestionID = questionAndAnswerDAO.addQuestion(question);
                    LOGGER.log(Level.INFO, "Đã thêm câu hỏi mới ID: {0}", newQuestionID);
                }
            }

            sendJsonResponse(response, true, "Cập nhật assignment thành công");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            sendErrorResponse(response, "Lỗi database: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            sendErrorResponse(response, e.getMessage());
        }
    }

    private Question createQuestionFromRequest(HttpServletRequest request, int index, int assignmentID) throws ServletException, IOException {
        Question question = new Question();
        question.setQuestionText(request.getParameter("questionText_" + index));
        question.setQuestionType(request.getParameter("questionType_" + index));
        question.setQuestionMark(Double.parseDouble(request.getParameter("questionMark_" + index)));
        question.setAssignmentID(assignmentID);
        
        Part imagePart = request.getPart("questionImage_" + index);
        if (imagePart != null && imagePart.getSize() > 0) {
            String imageUrl = saveMediaFile(imagePart);
            question.setQuestionImage(imageUrl);
        }

        Part audioPart = request.getPart("audioFile_" + index);
        if (audioPart != null && audioPart.getSize() > 0) {
            String audioUrl = saveMediaFile(audioPart);
            question.setAudioFile(audioUrl);
        }

        return question;
    }

    private List<Answer> createAnswersFromRequest(HttpServletRequest request, int index, int questionID) {
        List<Answer> answers = new ArrayList<>();
        String questionType = request.getParameter("questionType_" + index);
        String correctAnswer = request.getParameter("correctAnswer_" + index);

        if ("MULTIPLE_CHOICE".equals(questionType)) {
            int answerCount = 0;
            for (int i = 1; i <= 5; i++) {
                String answerText = request.getParameter("answerText_" + index + "_" + i);
                if (answerText != null && !answerText.trim().isEmpty()) {
                    Answer answer = new Answer();
                    answer.setAnswerText(answerText);
                    answer.setCorrect(String.valueOf(i).equals(correctAnswer));
                    answer.setQuestionID(questionID);
                    answer.setOptionLabel((char)('A' + answerCount));
                    answers.add(answer);
                    answerCount++;
                }
            }
        } else if ("TRUE_FALSE".equals(questionType)) {
            Answer trueAnswer = new Answer();
            trueAnswer.setAnswerText("True");
            trueAnswer.setCorrect("1".equals(correctAnswer));
            trueAnswer.setQuestionID(questionID);
            trueAnswer.setOptionLabel('A');
            answers.add(trueAnswer);

            Answer falseAnswer = new Answer();
            falseAnswer.setAnswerText("False");
            falseAnswer.setCorrect("2".equals(correctAnswer));
            falseAnswer.setQuestionID(questionID);
            falseAnswer.setOptionLabel('B');
            answers.add(falseAnswer);
        } else {
            Answer answer = new Answer();
            answer.setAnswerText(correctAnswer);
            answer.setCorrect(true);
            answer.setQuestionID(questionID);
            answer.setOptionLabel('A');
            answers.add(answer);
        }

        return answers;
    }

    private void handleDeleteQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int questionID = Integer.parseInt(request.getParameter("questionID"));
            int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));

            if (!isValidId(String.valueOf(questionID)) || !isValidId(String.valueOf(assignmentID))) {
                sendErrorResponse(response, "ID không hợp lệ");
                return;
            }

            questionAndAnswerDAO.deleteQuestion(questionID);
            sendJsonResponse(response, true, "Đã xóa câu hỏi thành công");

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format", e);
            sendErrorResponse(response, "ID không hợp lệ");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            sendErrorResponse(response, "Lỗi khi xóa câu hỏi: " + e.getMessage());
        }
    }

    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));
            if (!isValidId(String.valueOf(assignmentID))) {
                sendErrorResponse(response, "ID không hợp lệ");
                return;
            }

            Question question = new Question();
            question.setQuestionText(request.getParameter("questionText"));
            question.setQuestionType(request.getParameter("questionType"));
            question.setQuestionMark(Double.parseDouble(request.getParameter("questionMark")));
            question.setAssignmentID(assignmentID);

            handleMediaFiles(request, question, 0);
            List<Answer> answers = processAnswers(request, question, 0);
            question.setAnswers(answers);

            questionAndAnswerDAO.addQuestion(question);
            sendJsonResponse(response, true, "Đã thêm câu hỏi thành công");

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format", e);
            sendErrorResponse(response, "Dữ liệu không hợp lệ");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            sendErrorResponse(response, "Lỗi khi thêm câu hỏi: " + e.getMessage());
        } catch (ServletException e) {
            LOGGER.log(Level.SEVERE, "Servlet error", e);
            sendErrorResponse(response, "Lỗi: " + e.getMessage());
        }
    }

    private void handleUpdateQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int questionID = Integer.parseInt(request.getParameter("questionID"));
            if (!isValidId(String.valueOf(questionID))) {
                sendErrorResponse(response, "ID không hợp lệ");
                return;
            }

            Question question = new Question();
            question.setQuestionID(questionID);
            question.setQuestionText(request.getParameter("questionText"));
            question.setQuestionType(request.getParameter("questionType"));
            question.setQuestionMark(Double.parseDouble(request.getParameter("questionMark")));

            handleMediaFiles(request, question, 0);
            List<Answer> answers = processAnswers(request, question, 0);
            question.setAnswers(answers);

            questionAndAnswerDAO.updateQuestion(question, answers);
            sendJsonResponse(response, true, "Đã cập nhật câu hỏi thành công");

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format", e);
            sendErrorResponse(response, "Dữ liệu không hợp lệ");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            sendErrorResponse(response, "Lỗi khi cập nhật câu hỏi: " + e.getMessage());
        } catch (ServletException e) {
            LOGGER.log(Level.SEVERE, "Servlet error", e);
            sendErrorResponse(response, "Lỗi: " + e.getMessage());
        }
    }

    private void handleMediaFiles(HttpServletRequest request, Question question, int index) throws IOException, ServletException {
        try {
            Part imagePart = request.getPart("questionImage_" + index);
            if (imagePart != null && imagePart.getSize() > 0) {
                String contentType = imagePart.getContentType();
                if (contentType != null && contentType.startsWith("image/")) {
                    String imageUrl = saveMediaFile(imagePart);
                    question.setQuestionImage(imageUrl);
                }
            }

            Part audioPart = request.getPart("audioFile_" + index);
            if (audioPart != null && audioPart.getSize() > 0) {
                String contentType = audioPart.getContentType();
                if (contentType != null && contentType.startsWith("audio/")) {
                    String audioUrl = saveMediaFile(audioPart);
                    question.setAudioFile(audioUrl);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling media files", e);
            throw new ServletException("Lỗi khi xử lý file media: " + e.getMessage());
        }
    }

    private String saveMediaFile(Part filePart) throws IOException {
        // Implement your file saving logic here
        return "path/to/saved/file";
    }

    private List<Answer> processAnswers(HttpServletRequest request, Question question, int index) throws ServletException {
        List<Answer> answers = new ArrayList<>();
        String questionType = question.getQuestionType();

        try {
            switch (questionType) {
                case "MULTIPLE_CHOICE":
                    String correctAnswer = request.getParameter("correctAnswer_" + index);
                    if (correctAnswer == null) {
                        throw new ServletException("Thiếu đáp án đúng cho câu hỏi trắc nghiệm");
                    }

                    for (int i = 1; i <= 4; i++) {
                        String answerText = request.getParameter("answerText_" + index + "_" + i);
                        if (answerText != null && !answerText.trim().isEmpty()) {
                            Answer answer = new Answer();
                            answer.setAnswerText(answerText);
                            answer.setCorrect(correctAnswer.equals(String.valueOf(i)));
                            answer.setQuestionID(question.getQuestionID());
                            answer.setOptionLabel((char)('A' + i - 1));
                            answers.add(answer);
                        }
                    }
                    break;

                case "TRUE_FALSE":
                    String tfAnswer = request.getParameter("correctAnswer_" + index);
                    if (tfAnswer == null) {
                        throw new ServletException("Thiếu đáp án đúng cho câu hỏi đúng/sai");
                    }

                    Answer trueAnswer = new Answer();
                    trueAnswer.setAnswerText("True");
                    trueAnswer.setCorrect("1".equals(tfAnswer));
                    trueAnswer.setQuestionID(question.getQuestionID());
                    trueAnswer.setOptionLabel('A');
                    answers.add(trueAnswer);

                    Answer falseAnswer = new Answer();
                    falseAnswer.setAnswerText("False");
                    falseAnswer.setCorrect("2".equals(tfAnswer));
                    falseAnswer.setQuestionID(question.getQuestionID());
                    falseAnswer.setOptionLabel('B');
                    answers.add(falseAnswer);
                    break;

                case "SHORT_ANSWER":
                case "ESSAY":
                    String answerText = request.getParameter("correctAnswer_" + index);
                    if (answerText == null || answerText.trim().isEmpty()) {
                        throw new ServletException("Thiếu đáp án cho câu hỏi tự luận");
                    }

                    Answer answer = new Answer();
                    answer.setAnswerText(answerText);
                    answer.setCorrect(true);
                    answer.setQuestionID(question.getQuestionID());
                    answer.setOptionLabel('A');
                    answers.add(answer);
                    break;

                default:
                    throw new ServletException("Loại câu hỏi không hợp lệ: " + questionType);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing answers", e);
            throw new ServletException("Lỗi khi xử lý đáp án: " + e.getMessage());
        }

        return answers;
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

    @Override
    public void destroy() {
        try {
            if (assignmentDAO != null) {
                assignmentDAO.closeConnection();
            }
            if (questionAndAnswerDAO != null) {
                questionAndAnswerDAO.closeConnection();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing connection", e);
        }
    }
}