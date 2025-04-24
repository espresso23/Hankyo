package controller;

import cloud.CloudinaryConfig;
import com.google.gson.Gson;
import dao.ExamDAO;
import dao.ExamQuestionDAO;
import model.Answer;
import model.Exam;
import model.ExamQuestion;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
                case "importQuestions":
                    handleImportQuestions(request, response);
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
            String[] option_labels = null;

            if ("multiple_choice".equals(questionType)) {
                answers = request.getParameterValues("answers");
                isCorrect = request.getParameterValues("isCorrect");
                option_labels = request.getParameterValues("option_labels");

                // Debug log
                System.out.println("=== Debug thông tin câu trả lời ===");
                System.out.println("answers: " + (answers != null ? answers.length : "null"));
                System.out.println("isCorrect: " + (isCorrect != null ? isCorrect.length : "null"));
                System.out.println("option_labels: " + (option_labels != null ? option_labels.length : "null"));

                if (answers == null || isCorrect == null || option_labels == null) {
                    out.write("{\"success\": false, \"message\": \"Thiếu thông tin đáp án\"}");
                    return;
                }

                // Kiểm tra số lượng đáp án
                if (answers.length != 4) {
                    out.write("{\"success\": false, \"message\": \"Câu hỏi trắc nghiệm phải có đúng 4 đáp án. Hiện tại có " + answers.length + " đáp án.\"}");
                    return;
                }

                // Kiểm tra ít nhất một đáp án đúng
                boolean hasCorrectAnswer = false;
                for (String correct : isCorrect) {
                    if ("1".equals(correct)) {
                        hasCorrectAnswer = true;
                        break;
                    }
                }

                if (!hasCorrectAnswer) {
                    out.write("{\"success\": false, \"message\": \"Phải có ít nhất một đáp án đúng\"}");
                    return;
                }

                // Kiểm tra nội dung đáp án không được trống
                for (String answer : answers) {
                    if (answer == null || answer.trim().isEmpty()) {
                        out.write("{\"success\": false, \"message\": \"Nội dung đáp án không được để trống\"}");
                        return;
                    }
                }

                // Debug: Log các câu trả lời
                System.out.println("=== Chi tiết câu trả lời ===");
                for (int i = 0; i < answers.length; i++) {
                    System.out.println("Đáp án " + (i + 1) + ":");
                    System.out.println("- Nội dung: " + answers[i]);
                    System.out.println("- Đúng/Sai: " + (isCorrect != null && i < isCorrect.length ? isCorrect[i] : "N/A"));
                    System.out.println("- Label: " + (option_labels != null && i < option_labels.length ? option_labels[i] : "N/A"));
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
        String status = request.getParameter("status");

        // Cập nhật thông tin exam
        Exam exam = new Exam();
        exam.setExamID(examID);
        exam.setExamName(examName);
        exam.setExamDescription(examDescription);
        exam.setExamType(examType);
        exam.setStatus(status);

        boolean isUpdated = examDAO.updateExam(exam);
        if (isUpdated) {
            response.sendRedirect("edit-exam?action=getExam&examID=" + examID);
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
                String[] answerTexts = request.getParameterValues("edit_answers");
                String[] isCorrects = request.getParameterValues("edit_isCorrect");
                String[] optionLabels = {"A", "B", "C", "D"}; // Sử dụng labels cố định

                if (answerTexts == null || isCorrects == null || 
                    answerTexts.length != 4 || isCorrects.length != 4) {
                    out.write("{\"success\": false, \"message\": \"Câu hỏi trắc nghiệm phải có đúng 4 đáp án\"}");
                    return;
                }

                // Kiểm tra ít nhất một đáp án đúng
                boolean hasCorrectAnswer = false;
                for (String correct : isCorrects) {
                    if ("1".equals(correct) || "on".equals(correct)) {
                        hasCorrectAnswer = true;
                        break;
                    }
                }

                if (!hasCorrectAnswer) {
                    out.write("{\"success\": false, \"message\": \"Phải có ít nhất một đáp án đúng\"}");
                    return;
                }

                // Kiểm tra nội dung đáp án không được trống
                for (String answer : answerTexts) {
                    if (answer == null || answer.trim().isEmpty()) {
                        out.write("{\"success\": false, \"message\": \"Nội dung đáp án không được để trống\"}");
                        return;
                    }
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
                    answer.setCorrect("1".equals(isCorrects[i]) || "on".equals(isCorrects[i]));
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

    private void handleImportQuestions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int examID = Integer.parseInt(request.getParameter("examID"));
            Part filePart = request.getPart("questionFile");
            String fileName = filePart.getSubmittedFileName().toLowerCase();
            int importedCount = 0;
            int totalCount = 0;

            if (fileName.endsWith(".csv")) {
                importedCount = importQuestionsFromCSV(filePart.getInputStream(), examID);
                System.out.println("Đã import " + importedCount + " câu hỏi từ file CSV");
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                importedCount = importQuestionsFromExcel(filePart.getInputStream(), examID);
                System.out.println("Đã import " + importedCount + " câu hỏi từ file Excel");
            } else {
                out.write("{\"success\": false, \"message\": \"Định dạng file không được hỗ trợ\"}");
                return;
            }

            out.write("{\"success\": true, \"message\": \"Đã import thành công " + importedCount + " câu hỏi\", \"importedCount\": " + importedCount + "}");
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Lỗi khi import: " + e.getMessage() + "\"}");
        }
    }

    private int importQuestionsFromCSV(InputStream inputStream, int examID) throws Exception {
        int importedCount = 0;
        int totalCount = 0;
        Connection conn = null;
        BufferedReader reader = null;
        CSVParser parser = null;
        
        try {
            // Tạo connection mới cho mỗi lần import
            conn = DBConnect.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Không thể tạo kết nối database");
            }
            
            // Kiểm tra connection có đóng không
            if (conn.isClosed()) {
                conn = DBConnect.getInstance().getConnection();
                if (conn == null || conn.isClosed()) {
                    throw new SQLException("Không thể tạo kết nối database mới");
                }
            }

            conn.setAutoCommit(false);

            // Đọc file CSV với encoding UTF-8
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreEmptyLines()
                    .withTrim()
                    .parse(reader);
            
            // Đếm tổng số records
            List<CSVRecord> records = parser.getRecords();
            totalCount = records.size();
            
            System.out.println("Tổng số câu hỏi cần import: " + totalCount);
            
            for (CSVRecord record : records) {
                // Kiểm tra connection trước mỗi lần insert
                if (conn.isClosed()) {
                    conn = DBConnect.getInstance().getConnection();
                    if (conn == null || conn.isClosed()) {
                        throw new SQLException("Connection bị đóng trong quá trình xử lý");
                    }
                    conn.setAutoCommit(false);
                }

                try {
                    // Debug: In ra thông tin record đang xử lý
                    System.out.println("\nĐang xử lý câu hỏi thứ " + (importedCount + 1));
                    System.out.println("Question Type: " + record.get("questionType"));
                    System.out.println("Question Text: " + record.get("questionText"));
                    System.out.println("Question Mark: " + record.get("questionMark"));

                    Question question = new Question();
                    question.setExamID(examID);
                    question.setQuestionType(record.get("questionType"));
                    question.setQuestionText(record.get("questionText"));
                    question.setQuestionMark(Double.parseDouble(record.get("questionMark")));

                    // Xử lý hình ảnh và audio nếu có
                    String questionImg = record.get("questionImg");
                    String audioFile = record.get("audioFile");
                    if (questionImg != null && !questionImg.trim().isEmpty()) {
                        question.setQuestionImage(questionImg);
                    }
                    if (audioFile != null && !audioFile.trim().isEmpty()) {
                        question.setAudioFile(audioFile);
                    }

                    // Debug: In ra thông tin câu trả lời
                    System.out.println("Đang xử lý câu trả lời:");
                    List<Answer> answers = new ArrayList<>();
                    for (int i = 1; i <= 4; i++) {
                        Answer answer = new Answer();
                        String answerText = record.get("answer" + i);
                        String isCorrectStr = record.get("isCorrect" + i);
                        
                        System.out.println("Answer " + i + ": " + answerText);
                        System.out.println("Is Correct " + i + ": " + isCorrectStr);
                        
                        answer.setAnswerText(answerText);
                        answer.setCorrect("1".equals(isCorrectStr));
                        answer.setOptionLabel(String.valueOf((char)('A' + i - 1)));
                        answers.add(answer);
                    }

                    // Thêm câu hỏi và câu trả lời vào database
                    if (examService.addQuestionToExam(question, 
                        answers.stream().map(Answer::getAnswerText).toArray(String[]::new),
                        answers.stream().map(a -> a.isCorrect() ? "1" : "0").toArray(String[]::new),
                        answers.stream().map(Answer::getOptionLabel).toArray(String[]::new),
                        examID)) {
                        importedCount++;
                        System.out.println("Thêm câu hỏi thành công!");
                    } else {
                        System.out.println("Không thể thêm câu hỏi!");
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi khi xử lý record: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            }
            
            // Commit transaction nếu thành công
            if (!conn.isClosed()) {
                conn.commit();
                System.out.println("Đã commit transaction thành công!");
            }
            
            return importedCount;
        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.rollback();
                    System.out.println("Đã rollback transaction!");
                } catch (SQLException ex) {
                    throw new Exception("Lỗi khi rollback: " + ex.getMessage());
                }
            }
            throw new Exception("Lỗi khi import CSV: " + e.getMessage());
        } finally {
            // Đóng tất cả resources trong finally block
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int importQuestionsFromExcel(InputStream inputStream, int examID) throws Exception {
        int importedCount = 0;
        int totalCount = 0;
        Connection conn = null;
        Workbook workbook = null;
        
        try {
            // Tạo connection mới cho mỗi lần import
            conn = DBConnect.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Không thể tạo kết nối database");
            }
            
            // Kiểm tra connection có đóng không
            if (conn.isClosed()) {
                conn = DBConnect.getInstance().getConnection();
                if (conn == null || conn.isClosed()) {
                    throw new SQLException("Không thể tạo kết nối database mới");
                }
            }

            conn.setAutoCommit(false);

            // Đọc file Excel
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headers = new HashMap<>();
            
            // Đếm tổng số records
            totalCount = sheet.getLastRowNum();
            
            // Đọc header
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    headers.put(cell.getStringCellValue(), i);
                }
            }

            // Đọc dữ liệu
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // Kiểm tra connection trước mỗi lần insert
                if (conn.isClosed()) {
                    conn = DBConnect.getInstance().getConnection();
                    if (conn == null || conn.isClosed()) {
                        throw new SQLException("Connection bị đóng trong quá trình xử lý");
                    }
                    conn.setAutoCommit(false);
                }

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Question question = new Question();
                question.setExamID(examID);
                question.setQuestionType(getCellValue(row.getCell(headers.get("questionType"))));
                question.setQuestionText(getCellValue(row.getCell(headers.get("questionText"))));
                question.setQuestionMark(Double.parseDouble(getCellValue(row.getCell(headers.get("questionMark")))));

                // Xử lý hình ảnh và audio nếu có
                String questionImg = getCellValue(row.getCell(headers.get("questionImg")));
                String audioFile = getCellValue(row.getCell(headers.get("audioFile")));
                if (questionImg != null && !questionImg.trim().isEmpty()) {
                    question.setQuestionImage(questionImg);
                }
                if (audioFile != null && !audioFile.trim().isEmpty()) {
                    question.setAudioFile(audioFile);
                }

                List<Answer> answers = new ArrayList<>();
                for (int j = 1; j <= 4; j++) {
                    Answer answer = new Answer();
                    answer.setAnswerText(getCellValue(row.getCell(headers.get("answer" + j))));
                    answer.setCorrect("1".equals(getCellValue(row.getCell(headers.get("isCorrect" + j)))));
                    answer.setOptionLabel(String.valueOf((char)('A' + j - 1)));
                    answers.add(answer);
                }

                // Thêm câu hỏi và câu trả lời vào database
                if (examService.addQuestionToExam(question,
                    answers.stream().map(Answer::getAnswerText).toArray(String[]::new),
                    answers.stream().map(a -> a.isCorrect() ? "1" : "0").toArray(String[]::new),
                    answers.stream().map(Answer::getOptionLabel).toArray(String[]::new),
                    examID)) {
                    importedCount++;
                }
            }
            
            // Commit transaction nếu thành công
            if (!conn.isClosed()) {
                conn.commit();
            }
            return importedCount;
        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new Exception("Lỗi khi rollback: " + ex.getMessage());
                }
            }
            throw new Exception("Lỗi khi import Excel: " + e.getMessage());
        } finally {
            // Đóng workbook trong finally block
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            // Đóng connection trong finally block
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

}