package controller;

import dao.AssignmentDAO;
import dao.CourseContentDAO;
import model.Answer;
import model.Assignment;
import model.Expert;
import model.Question;
import service.AssignmentService;
import com.google.gson.Gson;

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
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@WebServlet("/edit-assignment")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
public class EditAssignmentController extends HttpServlet {
    private AssignmentService assignmentService;
    private AssignmentDAO assignmentDAO;
    private CourseContentDAO courseContentDAO;

    @Override
    public void init() throws ServletException {
        assignmentService = new AssignmentService();
        assignmentDAO = new AssignmentDAO();
        courseContentDAO = new CourseContentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            Expert expert = (Expert) request.getSession().getAttribute("expert");
            if (expert == null) {
                response.sendRedirect("login");
                return;
            }

            String action = request.getParameter("action");
            if (action == null) {
                action = "getAssignment";
            }

            switch (action) {
                case "getAssignment":
                    handleGetAssignment(request, response);
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
            Expert expert = (Expert) request.getSession().getAttribute("expert");
            if (expert == null) {
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
                case "updateAssignment":
                    handleUpdateAssignment(request, response);
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

    private void handleGetAssignment(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));
            int courseID = Integer.parseInt(request.getParameter("courseID"));

            // Lấy thông tin assignment
            Assignment assignment = assignmentDAO.getAssignmentById(assignmentID);
            if (assignment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy assignment");
                return;
            }

            System.out.println("=== Thông tin Assignment ===");
            System.out.println("Assignment ID: " + assignment.getAssignmentID());
            System.out.println("Assignment Title: " + assignment.getAssignmentTitle());
            System.out.println("Course ID: " + courseID);

            // Lấy danh sách câu hỏi
            List<Question> questions = assignmentService.getAllQuestionOfAssignment(assignmentID);
            
            // Kiểm tra null và lấy câu trả lời cho mỗi câu hỏi
            if (questions != null && !questions.isEmpty()) {
                System.out.println("Số lượng câu hỏi: " + questions.size());
                
                for (Question question : questions) {
                    // Lấy danh sách câu trả lời cho câu hỏi
                    List<Answer> answers = assignmentService.getAllAnswerOfThisQuestion(question.getQuestionID());
                    if (answers != null && !answers.isEmpty()) {
                        question.setAnswers(answers); // Chỉ cần set một lần cho mỗi câu hỏi
                    }
                }
            } else {
                System.out.println("Không có câu hỏi nào cho assignment này");
                questions = new ArrayList<>(); // Khởi tạo list rỗng để tránh null
            }

            // Đặt dữ liệu vào request
            request.setAttribute("assignment", assignment);
            request.setAttribute("questions", questions);
            request.setAttribute("courseID", courseID);

            // Chuyển hướng đến trang edit assignment
            request.getRequestDispatcher("editAssignment.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin assignment: " + e.getMessage());
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
            String assignmentIDStr = request.getParameter("assignmentID");
            String questionText = request.getParameter("questionText");
            String questionType = request.getParameter("questionType");
            String questionMarkStr = request.getParameter("questionMark");

            if (assignmentIDStr == null || questionText == null || questionType == null || questionMarkStr == null) {
                out.write("{\"success\": false, \"message\": \"Thiếu thông tin bắt buộc\"}");
                return;
            }

            int assignmentID;
            double questionMark;
            try {
                assignmentID = Integer.parseInt(assignmentIDStr);
                questionMark = Double.parseDouble(questionMarkStr);
            } catch (NumberFormatException e) {
                out.write("{\"success\": false, \"message\": \"Định dạng số không hợp lệ\"}");
                return;
            }

            // Tạo đối tượng Question
            Question question = new Question();
            question.setAssignmentID(assignmentID);
            question.setQuestionText(questionText);
            question.setQuestionType(questionType);
            question.setQuestionMark(questionMark);

            // Xử lý file upload
            Part audioPart = request.getPart("audioFile");
            Part imagePart = request.getPart("questionImage");

            if (audioPart != null && audioPart.getSize() > 0) {
                String audioUrl = courseContentDAO.convertMediaToUrl(audioPart);
                question.setAudioFile(audioUrl);
            }

            if (imagePart != null && imagePart.getSize() > 0) {
                String imageUrl = courseContentDAO.convertMediaToUrl(imagePart);
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
            boolean result = assignmentService.addQuestion(question, answers, isCorrect, option_labels, assignmentID);

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

    private void handleUpdateAssignment(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        // Cập nhật thông tin assignment
        Assignment assignment = new Assignment();
        assignment.setAssignmentID(assignmentID);
        assignment.setAssignmentTitle(title);
        assignment.setDescription(description);

        assignmentDAO.updateAssignment(assignment);

        // Chuyển hướng về trang edit assignment
        response.sendRedirect("edit-assignment?action=getAssignment&assignmentID=" + assignmentID
                + "&courseID=" + request.getParameter("courseID"));
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
            String assignmentIDStr = request.getParameter("assignmentID");
            
            if (questionIDStr == null || assignmentIDStr == null) {
                out.write("{\"success\": false, \"message\": \"Thiếu thông tin câu hỏi hoặc bài tập\"}");
                return;
            }

            int questionID = Integer.parseInt(questionIDStr);
            int assignmentID = Integer.parseInt(assignmentIDStr);

            System.out.println("Thông tin xóa câu hỏi:");
            System.out.println("- Question ID: " + questionID);
            System.out.println("- Assignment ID: " + assignmentID);

            // Xóa câu hỏi
            assignmentService.deleteQuestion(questionID);
            System.out.println("=== Xóa câu hỏi thành công ===");

            // Trả về response thành công
            out.write("{\"success\": true, \"message\": \"Xóa câu hỏi thành công\"}");

        } catch (NumberFormatException e) {
            System.out.println("=== Lỗi: ID không hợp lệ ===");
            out.write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
        } catch (SQLException e) {
            System.out.println("=== Lỗi SQL: " + e.getMessage() + " ===");
            out.write("{\"success\": false, \"message\": \"Lỗi database: " + e.getMessage() + "\"}");
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
            Question question = assignmentService.getQuestionById(questionID);
            if (question == null) {
                out.write("{\"success\": false, \"message\": \"Không tìm thấy câu hỏi\"}");
                return;
            }

            // Lấy danh sách câu trả lời nếu là câu hỏi trắc nghiệm
            if ("multiple_choice".equals(question.getQuestionType())) {
                List<Answer> answers = assignmentService.getAllAnswerOfThisQuestion(questionID);
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
            Question currentQuestion = assignmentService.getQuestionById(questionID);
            if (currentQuestion == null) {
                out.write("{\"success\": false, \"message\": \"Không tìm thấy câu hỏi\"}");
                return;
            }

            // Xử lý hình ảnh
            if (imagePart != null && imagePart.getSize() > 0) {
                String imageUrl = courseContentDAO.convertMediaToUrl(imagePart);
                question.setQuestionImage(imageUrl);
            } else {
                // Giữ nguyên hình ảnh cũ nếu không có upload mới
                question.setQuestionImage(currentQuestion.getQuestionImage());
            }

            // Xử lý audio
            if (audioPart != null && audioPart.getSize() > 0) {
                String audioUrl = courseContentDAO.convertMediaToUrl(audioPart);
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
            assignmentService.updateQuestionForAssignment(question, answers);

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
            int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));
            Part filePart = request.getPart("questionFile");
            
            if (filePart == null) {
                out.write("{\"success\": false, \"message\": \"Vui lòng chọn file\"}");
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            if (fileName == null || fileName.trim().isEmpty()) {
                out.write("{\"success\": false, \"message\": \"Tên file không hợp lệ\"}");
                return;
            }

            fileName = fileName.toLowerCase();
            int importedCount = 0;

            if (fileName.endsWith(".csv")) {
                importedCount = importQuestionsFromCSV(filePart.getInputStream(), assignmentID);
                System.out.println("Đã import " + importedCount + " câu hỏi từ file CSV");
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                importedCount = importQuestionsFromExcel(filePart.getInputStream(), assignmentID);
                System.out.println("Đã import " + importedCount + " câu hỏi từ file Excel");
            } else {
                out.write("{\"success\": false, \"message\": \"Chỉ chấp nhận file Excel (.xlsx, .xls) hoặc CSV (.csv)\"}");
                return;
            }

            out.write("{\"success\": true, \"message\": \"Đã import thành công " + importedCount + " câu hỏi\"}");

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Có lỗi xảy ra khi import file. Vui lòng kiểm tra định dạng file và thử lại.";
            }
            out.write("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
        }
    }

    private int importQuestionsFromCSV(InputStream inputStream, int assignmentID) throws Exception {
        int importedCount = 0;
        BufferedReader reader = null;
        CSVParser parser = null;
        
        try {
            // Đọc file CSV với encoding UTF-8
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreEmptyLines()
                    .withTrim()
                    .parse(reader);
            
            // Lấy danh sách records
            List<CSVRecord> records = parser.getRecords();
            if (records.isEmpty()) {
                throw new IllegalArgumentException("File CSV không có dữ liệu");
            }
            
            System.out.println("Tổng số câu hỏi cần import: " + records.size());
            
            for (CSVRecord record : records) {
                try {
                    // Debug: In ra thông tin record đang xử lý
                    System.out.println("\nĐang xử lý câu hỏi thứ " + (importedCount + 1));
                    
                    // Validate và đọc thông tin cơ bản
                    String questionType = record.get("questionType");
                    String questionText = record.get("questionText");
                    String questionMarkStr = record.get("questionMark");

                    // Validate dữ liệu
                    if (questionText == null || questionText.trim().isEmpty()) {
                        throw new IllegalArgumentException("Nội dung câu hỏi không được để trống");
                    }

                    if (questionType == null || (!questionType.equals("multiple_choice") && !questionType.equals("short_answer"))) {
                        throw new IllegalArgumentException("Loại câu hỏi không hợp lệ. Phải là 'multiple_choice' hoặc 'short_answer'");
                    }

                    double questionMark;
                    try {
                        questionMark = Double.parseDouble(questionMarkStr);
                        if (questionMark <= 0) {
                            throw new IllegalArgumentException("Điểm số phải lớn hơn 0");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Điểm số không hợp lệ");
                    }

                    // Tạo đối tượng Question
                    Question question = new Question();
                    question.setAssignmentID(assignmentID);
                    question.setQuestionText(questionText);
                    question.setQuestionType(questionType);
                    question.setQuestionMark(questionMark);

                    // Xử lý hình ảnh và audio nếu có
                    try {
                        String questionImg = record.get("questionImage");
                        if (questionImg != null && !questionImg.trim().isEmpty()) {
                            question.setQuestionImage(questionImg);
                        }
                    } catch (IllegalArgumentException e) {
                        // Bỏ qua nếu không có cột questionImage
                    }

                    try {
                        String audioFile = record.get("audioFile");
                        if (audioFile != null && !audioFile.trim().isEmpty()) {
                            question.setAudioFile(audioFile);
                        }
                    } catch (IllegalArgumentException e) {
                        // Bỏ qua nếu không có cột audioFile
                    }

                    // Xử lý câu trả lời cho câu hỏi trắc nghiệm
                    if ("multiple_choice".equals(questionType)) {
                        List<String> answerList = new ArrayList<>();
                        List<String> correctList = new ArrayList<>();
                        List<String> labelList = new ArrayList<>();
                        boolean hasCorrectAnswer = false;
                        int answerCount = 0;

                        // Đếm số lượng câu trả lời có sẵn
                        for (int j = 1; j <= 10; j++) {
                            try {
                                String answerText = record.get("answer" + j);
                                if (answerText == null || answerText.trim().isEmpty()) {
                                    break;
                                }
                                answerCount++;
                            } catch (IllegalArgumentException e) {
                                break;
                            }
                        }

                        // Kiểm tra số lượng câu trả lời
                        if (answerCount < 2) {
                            throw new IllegalArgumentException("Câu hỏi trắc nghiệm phải có ít nhất 2 đáp án");
                        }

                        if (answerCount > 10) {
                            throw new IllegalArgumentException("Câu hỏi trắc nghiệm không được có quá 10 đáp án");
                        }

                        // Đọc các câu trả lời
                        for (int j = 1; j <= answerCount; j++) {
                            String answerText = record.get("answer" + j);
                            String isCorrectValue = record.get("isCorrect" + j);

                            if (answerText == null || answerText.trim().isEmpty()) {
                                throw new IllegalArgumentException("Nội dung đáp án " + j + " không được để trống");
                            }

                            answerList.add(answerText);
                            correctList.add(isCorrectValue != null && isCorrectValue.equals("1") ? "1" : "0");
                            labelList.add(String.valueOf((char)('A' + j - 1)));

                            if ("1".equals(isCorrectValue)) {
                                hasCorrectAnswer = true;
                            }
                        }

                        // Kiểm tra ít nhất một đáp án đúng
                        if (!hasCorrectAnswer) {
                            throw new IllegalArgumentException("Câu hỏi trắc nghiệm phải có ít nhất một đáp án đúng");
                        }

                        // Thêm câu hỏi vào database
                        if (assignmentService.addQuestion(question, 
                                                        answerList.toArray(new String[0]), 
                                                        correctList.toArray(new String[0]), 
                                                        labelList.toArray(new String[0]), 
                                                        assignmentID)) {
                            importedCount++;
                        }
                    } else {
                        // Câu hỏi tự luận
                        if (assignmentService.addQuestion(question, null, null, null, assignmentID)) {
                            importedCount++;
                        }
                    }

                } catch (Exception e) {
                    throw new IllegalArgumentException("Lỗi tại câu hỏi " + (importedCount + 1) + ": " + e.getMessage());
                }
            }
            
            return importedCount;

        } finally {
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
        }
    }

    private int importQuestionsFromExcel(InputStream inputStream, int assignmentID) throws Exception {
        int importedCount = 0;
        Workbook workbook = null;
        
        try {
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet.getPhysicalNumberOfRows() <= 1) {
                throw new IllegalArgumentException("File Excel không có dữ liệu (chỉ có header hoặc trống)");
            }

            // Đọc header
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headers = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    headers.put(cell.getStringCellValue().trim(), i);
                }
            }

            // Validate required columns
            String[] requiredColumns = {"questionType", "questionText", "questionMark"};
            for (String column : requiredColumns) {
                if (!headers.containsKey(column)) {
                    throw new IllegalArgumentException("File thiếu cột bắt buộc: " + column);
                }
            }

            // Đọc dữ liệu từng dòng
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // Đọc thông tin câu hỏi
                    String questionType = getCellValue(row.getCell(headers.get("questionType")));
                    String questionText = getCellValue(row.getCell(headers.get("questionText")));
                    String questionMarkStr = getCellValue(row.getCell(headers.get("questionMark")));

                    // Validate dữ liệu
                    if (questionText == null || questionText.trim().isEmpty()) {
                        throw new IllegalArgumentException("Nội dung câu hỏi không được để trống tại dòng " + (i + 1));
                    }

                    if (questionType == null || (!questionType.equals("multiple_choice") && !questionType.equals("short_answer"))) {
                        throw new IllegalArgumentException("Loại câu hỏi không hợp lệ tại dòng " + (i + 1) + 
                            ". Phải là 'multiple_choice' hoặc 'short_answer'. Giá trị hiện tại: '" + questionType + "'");
                    }

                    double questionMark;
                    try {
                        questionMark = Double.parseDouble(questionMarkStr);
                        if (questionMark <= 0) {
                            throw new IllegalArgumentException("Điểm số phải lớn hơn 0 tại dòng " + (i + 1));
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Điểm số không hợp lệ tại dòng " + (i + 1));
                    }

                    // Tạo đối tượng Question
                    Question question = new Question();
                    question.setAssignmentID(assignmentID);
                    question.setQuestionText(questionText);
                    question.setQuestionType(questionType);
                    question.setQuestionMark(questionMark);

                    // Xử lý hình ảnh và audio nếu có
                    if (headers.containsKey("questionImage")) {
                        String imageUrl = getCellValue(row.getCell(headers.get("questionImage")));
                        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                            question.setQuestionImage(imageUrl);
                        }
                    }

                    if (headers.containsKey("audioFile")) {
                        String audioUrl = getCellValue(row.getCell(headers.get("audioFile")));
                        if (audioUrl != null && !audioUrl.trim().isEmpty()) {
                            question.setAudioFile(audioUrl);
                        }
                    }

                    // Xử lý câu trả lời cho câu hỏi trắc nghiệm
                    if ("multiple_choice".equals(questionType)) {
                        List<String> answerList = new ArrayList<>();
                        List<String> correctList = new ArrayList<>();
                        List<String> labelList = new ArrayList<>();
                        boolean hasCorrectAnswer = false;
                        int answerCount = 0;

                        // Đếm số lượng câu trả lời có sẵn
                        for (int j = 1; j <= 10; j++) {
                            String answerKey = "answer" + j;
                            String correctKey = "isCorrect" + j;
                            
                            if (!headers.containsKey(answerKey) || !headers.containsKey(correctKey)) {
                                break;
                            }

                            String answerText = getCellValue(row.getCell(headers.get(answerKey)));
                            if (answerText == null || answerText.trim().isEmpty()) {
                                break;
                            }

                            answerCount++;
                        }

                        // Kiểm tra số lượng câu trả lời
                        if (answerCount < 2) {
                            throw new IllegalArgumentException("Câu hỏi trắc nghiệm phải có ít nhất 2 đáp án");
                        }

                        if (answerCount > 10) {
                            throw new IllegalArgumentException("Câu hỏi trắc nghiệm không được có quá 10 đáp án");
                        }

                        // Đọc các câu trả lời
                        for (int j = 1; j <= answerCount; j++) {
                            String answerKey = "answer" + j;
                            String correctKey = "isCorrect" + j;
                            
                            String answerText = getCellValue(row.getCell(headers.get(answerKey)));
                            Cell correctCell = row.getCell(headers.get(correctKey));
                            
                            // Debug log
                            System.out.println("Đáp án " + j + ": " + answerText);
                            System.out.println("Cell isCorrect" + j + " type: " + (correctCell != null ? correctCell.getCellType() : "null"));
                            System.out.println("Cell isCorrect" + j + " raw value: " + (correctCell != null ? correctCell.toString() : "null"));

                            String isCorrectValue = "0";
                            if (correctCell != null) {
                                switch (correctCell.getCellType()) {
                                    case NUMERIC:
                                        isCorrectValue = correctCell.getNumericCellValue() > 0 ? "1" : "0";
                                        break;
                                    case STRING:
                                        String strValue = correctCell.getStringCellValue().trim();
                                        isCorrectValue = "1".equals(strValue) || "true".equalsIgnoreCase(strValue) ? "1" : "0";
                                        break;
                                    case BOOLEAN:
                                        isCorrectValue = correctCell.getBooleanCellValue() ? "1" : "0";
                                        break;
                                    default:
                                        isCorrectValue = "0";
                                }
                            }

                            answerList.add(answerText);
                            correctList.add(isCorrectValue);
                            labelList.add(String.valueOf((char)('A' + j - 1)));
                            
                            if ("1".equals(isCorrectValue)) {
                                hasCorrectAnswer = true;
                            }
                        }

                        // Kiểm tra ít nhất một đáp án đúng
                        if (!hasCorrectAnswer) {
                            throw new IllegalArgumentException("Câu hỏi trắc nghiệm phải có ít nhất một đáp án đúng");
                        }

                        // Thêm câu hỏi vào database
                        if (assignmentService.addQuestion(question, 
                                                        answerList.toArray(new String[0]), 
                                                        correctList.toArray(new String[0]), 
                                                        labelList.toArray(new String[0]), 
                                                        assignmentID)) {
                            importedCount++;
                        }
                    } else {
                        // Câu hỏi tự luận
                        if (assignmentService.addQuestion(question, null, null, null, assignmentID)) {
                            importedCount++;
                        }
                    }

                } catch (Exception e) {
                    throw new IllegalArgumentException("Lỗi tại dòng " + (i + 1) + ": " + e.getMessage());
                }
            }

            return importedCount;

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Tránh hiển thị số dưới dạng khoa học (1.0E-4)
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    @Override
    public void destroy() {
        if (assignmentDAO != null) {
            assignmentDAO.closeConnection();
        }
        if (courseContentDAO != null) {
            courseContentDAO.closeConnection();
        }
    }
} 