package controller;

import cloud.CloudinaryConfig;
import com.google.gson.Gson;
import dao.ExamDAO;
import dao.ExamQuestionDAO;
import model.Answer;
import model.Exam;
import model.Question;
import model.User;
import service.ExamService;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/edit-exam")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
public class EditExamController extends HttpServlet {
    private ExamService examService;
    private ExamDAO examDAO;
    private ExamQuestionDAO examQuestionDAO;
    private CloudinaryConfig cloudinaryConfig;

    @Override
    public void init() throws ServletException {
        examService = new ExamService();
        examDAO = new ExamDAO(DBConnect.getInstance().getConnection());
        examQuestionDAO = new ExamQuestionDAO();
        cloudinaryConfig = new CloudinaryConfig();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("login");
                return;
            }
            Integer examID = Integer.parseInt(request.getParameter("examID"));

            String action = request.getParameter("action");
            if (action == null) {
                action = "getExam";
            }

            switch (action) {
                case "getExam":
                    handleGetExam(request, response, examID);
                    break;
                case "getExamLibrary":
                    handleGetExamLibrary(request, response);
                    break;
                case "deleteQuestion":
                    handleDeleteQuestion(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi database: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("login");
                return;
            }
            String action = request.getParameter("action");
            if (action == null) {
                action = "addQuestion";
            }

            switch (action) {
                case "addQuestion":
                    handleAddQuestion(request, response);
                    break;
                case "updateExam":
                    handleUpdateExam(request, response);
                    break;
                case "deleteQuestion":
                    handleDeleteQuestion(request, response);
                    break;
                case "getQuestionDetails":
                    handleGetQuestionDetails(request, response);
                    break;
                case "updateQuestion":
                    handleUpdateQuestion(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi database: " + e.getMessage());
        }
    }

    private void handleGetExam(HttpServletRequest request, HttpServletResponse response, int examID)
            throws SQLException, ServletException, IOException {
        try {
            // Lấy thông tin exam
            Exam exam = examDAO.getExamById(examID);
            if (exam == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy Exam");
                return;
            }

            System.out.println("=== Thông tin Exam ===");
            System.out.println("ExamID: " + exam.getExamID());
            System.out.println("ExamName: " + exam.getExamName());
            System.out.println("ExamDescription: " + exam.getExamDescription());
            System.out.println("DateCreated: " + exam.getDateCreated());
            System.out.println("ExamType: " + exam.getExamType());

            // Lấy danh sách câu hỏi va câu trả lời
            List<Question> questionList = new ArrayList<>();
            questionList = examService.getAllQuestionsAndAnswersOfExam(examID);

            // Kiểm tra null và lấy câu trả lời cho mỗi câu hỏi
            if (questionList != null && !questionList.isEmpty()) {
                System.out.println("Số lượng câu hỏi: " + questionList.size());

            } else {
                System.out.println("Không có câu hỏi nào cho exam này");
                questionList = new ArrayList<>(); // Khởi tạo list rỗng để tránh null
            }

            // Đặt dữ liệu vào request
            request.setAttribute("exam", exam);
            request.setAttribute("questions", questionList);

            // Chuyển hướng đến trang edit exam
            request.getRequestDispatcher("editExam.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin exam: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra khi xử lý yêu cầu");
        }
    }

    private void handleGetExamLibrary(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            // Lấy danh sách exam
            List<Exam> examList = examDAO.getAllExams();
            if (examList == null || examList.isEmpty()) {
                examList = new ArrayList<>(); // Khởi tạo list rỗng để tránh null
            }
            // Đặt dữ liệu vào request
            request.setAttribute("examList", examList);


            // Chuyển hướng đến trang exam management
            request.getRequestDispatcher("examManagement.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin exam: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra khi xử lý yêu cầu");
        }
    }


    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Debug: Log tất cả các parameters
            System.out.println("=== Thông tin FormData ===");
            request.getParameterMap().forEach((key, values) -> {
                System.out.print(key + ": ");
                for (String value : values) {
                    System.out.print(value + " ");
                }
                System.out.println();
            });

            // Validate required parameters
            String examIDStr = request.getParameter("examID");
            String questionText = request.getParameter("questionText");
            String questionType = request.getParameter("questionType");
            String questionMarkStr = request.getParameter("questionMark");

            if (examIDStr == null || questionText == null || questionType == null || questionMarkStr == null) {
                out.write("{\"success\": false, \"message\": \"Thiếu thông tin bắt buộc\"}");
                return;
            }

            int examID;
            double questionMark;
            try {
                examID = Integer.parseInt(examIDStr);
                questionMark = Double.parseDouble(questionMarkStr);
            } catch (NumberFormatException e) {
                out.write("{\"success\": false, \"message\": \"Định dạng số không hợp lệ\"}");
                return;
            }

            // Tạo đối tượng Question
            Question question = new Question();
            question.setExamID(examID);
            question.setQuestionText(questionText);
            question.setQuestionType(questionType);
            question.setQuestionMark(questionMark);

            // Xử lý file upload
            Part audioPart = request.getPart("audioFile");
            Part imagePart = request.getPart("questionImage");

            if (audioPart != null && audioPart.getSize() > 0) {
                String audioUrl = cloudinaryConfig.convertMediaToUrl(audioPart);
                question.setAudioFile(audioUrl);
            }

            if (imagePart != null && imagePart.getSize() > 0) {
                String imageUrl = cloudinaryConfig.convertMediaToUrl(imagePart);
                question.setQuestionImage(imageUrl);
            }

            // Xử lý câu trả lời (nếu là trắc nghiệm)
            String[] answers = null;
            String[] isCorrect = null;
            String[] option_labels = request.getParameterValues("option_labels");

            if ("multiple_choice".equals(questionType)) {
                answers = request.getParameterValues("answers");
                isCorrect = request.getParameterValues("isCorrect");

                if (answers == null || isCorrect == null || option_labels == null ||
                        answers.length == 0 || isCorrect.length != answers.length || option_labels.length != answers.length) {
                    out.write("{\"success\": false, \"message\": \"Thông tin câu trả lời không hợp lệ\"}");
                    return;
                }

                // Debug: Log các câu trả lời
                System.out.println("=== Câu trả lời ===");
                for (int i = 0; i < answers.length; i++) {
                    System.out.println(answers[i] + " - " + isCorrect[i] + " - " + option_labels[i]);
                }
            }

            // Thêm câu hỏi vào database trong một transaction
            boolean result = examService.addQuestionToExam(question, answers, isCorrect, option_labels, examID);

            if (result) {
                out.write("{\"success\": true, \"message\": \"Thêm câu hỏi thành công\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"Không thể thêm câu hỏi\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }

    private void handleUpdateExam(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int examID = Integer.parseInt(request.getParameter("examID"));
        String examName = request.getParameter("examName");
        String examDescription = request.getParameter("examDescription");
        String examType = request.getParameter("examType");

        // Cập nhật thông tin exam
        Exam exam = new Exam();
        exam.setExamID(examID);
        exam.setExamName(examName);
        exam.setExamDescription(examDescription);
        exam.setExamType(examType);

        boolean isUpdated = examDAO.updateExam(exam);
        if (isUpdated) {
            response.sendRedirect("edit-exam?action=getAssignment&examID=" + examID);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể cập nhật thông tin bài thi");
        }

    }

    private void handleDeleteQuestion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            System.out.println("=== Bắt đầu xóa câu hỏi ===");

            // Validate parameters
            String questionIDStr = request.getParameter("questionID");
            String examIDStr = request.getParameter("examID");

            if (questionIDStr == null || examIDStr == null) {
                out.write("{\"success\": false, \"message\": \"Thiếu thông tin câu hỏi hoặc bài thi\"}");
                return;
            }

            int questionID = Integer.parseInt(questionIDStr);
            int examID = Integer.parseInt(examIDStr);

            System.out.println("Thông tin xóa câu hỏi:");
            System.out.println("- Question ID: " + questionID);
            System.out.println("- Exam ID: " + examID);

            // Xóa câu hỏi
            examService.deleteExamQuestion(questionID);
            System.out.println("=== Xóa câu hỏi thành công ===");

            // Trả về response thành công
            out.write("{\"success\": true, \"message\": \"Xóa câu hỏi thành công\"}");

        } catch (NumberFormatException e) {
            System.out.println("=== Lỗi: ID không hợp lệ ===");
            out.write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
        } catch (Exception e) {
            System.out.println("=== Lỗi không xác định: " + e.getMessage() + " ===");
            out.write("{\"success\": false, \"message\": \"Lỗi không xác định: " + e.getMessage() + "\"}");
        }
    }

    private void handleGetQuestionDetails(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Validate parameters
            String questionIDStr = request.getParameter("questionID");
            if (questionIDStr == null) {
                out.write("{\"success\": false, \"message\": \"Thiếu thông tin câu hỏi\"}");
                return;
            }

            int questionID = Integer.parseInt(questionIDStr);

            // Lấy thông tin câu hỏi
            Question question = examService.getQuestionById(questionID);
            if (question == null) {
                out.write("{\"success\": false, \"message\": \"Không tìm thấy câu hỏi\"}");
                return;
            }

            // Lấy danh sách câu trả lời nếu là câu hỏi trắc nghiệm
            if ("multiple_choice".equals(question.getQuestionType())) {
                List<Answer> answers = examService.getAllAnswerOfThisQuestion(questionID);
                question.setAnswers(answers);
            }

            // Convert question to JSON
            Gson gson = new Gson();
            String questionJson = gson.toJson(question);

            // Trả về response thành công
            out.write("{\"success\": true, \"question\": " + questionJson + "}");

        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
        } catch (Exception e) {
            out.write("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }

    private void handleUpdateQuestion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Validate parameters
            String questionIDStr = request.getParameter("questionID");
            String questionText = request.getParameter("questionText");
            String questionType = request.getParameter("questionType");
            String questionMarkStr = request.getParameter("questionMark");

            if (questionIDStr == null || questionText == null || questionType == null || questionMarkStr == null) {
                out.write("{\"success\": false, \"message\": \"Thiếu thông tin bắt buộc\"}");
                return;
            }

            int questionID = Integer.parseInt(questionIDStr);
            double questionMark = Double.parseDouble(questionMarkStr);

            // Tạo đối tượng Question
            Question question = new Question();
            question.setQuestionID(questionID);
            question.setQuestionText(questionText);
            question.setQuestionType(questionType);
            question.setQuestionMark(questionMark);

            // Xử lý file upload
            Part audioPart = request.getPart("audioFile");
            Part imagePart = request.getPart("questionImage");

            // Lấy thông tin câu hỏi hiện tại
            Question currentQuestion = examService.getQuestionById(questionID);
            if (currentQuestion == null) {
                out.write("{\"success\": false, \"message\": \"Không tìm thấy câu hỏi\"}");
                return;
            }

            // Xử lý hình ảnh
            if (imagePart != null && imagePart.getSize() > 0) {
                String imageUrl = cloudinaryConfig.convertMediaToUrl(imagePart);
                question.setQuestionImage(imageUrl);
            } else {
                // Giữ nguyên hình ảnh cũ nếu không có upload mới
                question.setQuestionImage(currentQuestion.getQuestionImage());
            }

            // Xử lý audio
            if (audioPart != null && audioPart.getSize() > 0) {
                String audioUrl = cloudinaryConfig.convertMediaToUrl(audioPart);
                question.setAudioFile(audioUrl);
            } else {
                // Giữ nguyên audio cũ nếu không có upload mới
                question.setAudioFile(currentQuestion.getAudioFile());
            }

            // Xử lý câu trả lời (nếu là trắc nghiệm)
            List<Answer> answers = new ArrayList<>();
            if ("multiple_choice".equals(questionType)) {
                String[] answerTexts = request.getParameterValues("answers");
                String[] isCorrects = request.getParameterValues("isCorrect");
                String[] optionLabels = request.getParameterValues("option_labels");

                if (answerTexts == null || isCorrects == null || optionLabels == null ||
                        answerTexts.length == 0 || isCorrects.length != answerTexts.length || optionLabels.length != answerTexts.length) {
                    out.write("{\"success\": false, \"message\": \"Thông tin câu trả lời không hợp lệ\"}");
                    return;
                }

                // Debug log
                System.out.println("=== Thông tin câu trả lời ===");
                for (int i = 0; i < answerTexts.length; i++) {
                    System.out.println("Câu trả lời " + (i + 1) + ":");
                    System.out.println("- Nội dung: " + answerTexts[i]);
                    System.out.println("- Đúng/Sai: " + isCorrects[i]);
                    System.out.println("- Label: " + optionLabels[i]);
                }

                for (int i = 0; i < answerTexts.length; i++) {
                    Answer answer = new Answer();
                    answer.setQuestionID(questionID);
                    answer.setAnswerText(answerTexts[i]);
                    answer.setCorrect("1".equals(isCorrects[i]));
                    answer.setOptionLabel(optionLabels[i]);
                    answers.add(answer);
                }
            }

            // Cập nhật câu hỏi và câu trả lời
            examService.updateQuestionForExam(question, answers);

            // Trả về response thành công
            out.write("{\"success\": true, \"message\": \"Cập nhật câu hỏi thành công\"}");

        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"Định dạng số không hợp lệ\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }

}