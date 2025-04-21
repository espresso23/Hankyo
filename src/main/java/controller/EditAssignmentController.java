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