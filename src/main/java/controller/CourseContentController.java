package controller;

import dao.AssignmentDAO;
import dao.CourseContentDAO;
import dao.ExamDAO;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet("/course-content")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 100,      // 100 MB (increased from 10MB)
        maxRequestSize = 1024 * 1024 * 150    // 150 MB (increased from 100MB)
)
public class CourseContentController extends HttpServlet {
    private CourseContent courseContent = new CourseContent();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        if (expert == null) {
            resp.sendRedirect("login");
            return;
        }
        String action = req.getParameter("action");
        String courseIDStr = req.getParameter("courseID");
        if (courseIDStr == null || courseIDStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số courseID");
            return;
        }
        int courseID = Integer.parseInt(courseIDStr);

        switch (action) {
            case "addContentView":
                try {
                    showAddContentPage(req, resp);
                } catch (SQLException e) {
                    System.out.println("Loi xay ra " + e.getMessage());
                }
                break;
            case "deleteContent":
                try {
                    handleDeleteContent(req, resp, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private void showAddContentPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        CourseContentDAO dao = null;
        try {
            dao = new CourseContentDAO();
            int courseId = Integer.parseInt(request.getParameter("courseID"));
            List<CourseContent> courseContents = dao.listCourseContentsByCourseID(courseId);
            System.out.println(courseContents);
            request.setAttribute("contents", courseContents);
            request.setAttribute("courseID", courseId);
            System.out.println("Open add course content page");
            request.getRequestDispatcher("addCourseContent.jsp").forward(request, response);
        } finally {
            if (dao != null) {
                dao.closeConnection();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int courseID = Integer.parseInt(request.getParameter("courseID"));

        switch (action) {
            case "addVideo":
                try {
                    handleAddVideo(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "createEmptyAssignment":
                try {
                    handleCreateEmptyAssignment(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "updateVideo":
                try {
                    handleUpdateVideo(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "updateAssignment":
                try {
                    handleUpdateAssignment(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                break;
        }
    }

    private void handleAddVideo(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, ServletException, SQLException {
        CourseContentDAO dao = null;
        try {
            dao = new CourseContentDAO();
            Part videoPart = request.getPart("video");
            String videoUrl = dao.convertMediaToUrl(videoPart);
            CourseContent videoContent = new CourseContent();
            videoContent.setTitle(request.getParameter("title"));
            videoContent.setMedia(videoUrl);
            videoContent.setDescription(request.getParameter("description"));
            videoContent.setCourseID(Integer.parseInt(request.getParameter("courseID")));
            System.out.println(videoContent.toString());
            dao.addCourseContent(videoContent);
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
        } finally {
            if (dao != null) {
                dao.closeConnection();
            }
        }
    }

    private void handleCreateEmptyAssignment(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, SQLException {
        AssignmentDAO assignmentDAO = null;
        CourseContentDAO courseContentDAO = null;
        try {
            // 1. Validate và lấy thông tin từ form
            String title = request.getParameter("titleA");
            String description = request.getParameter("descriptionA");

            // 2. Kiểm tra dữ liệu đầu vào
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Tiêu đề assignment không được để trống");
            }

            // 3. Tạo assignment với thông tin từ form
            assignmentDAO = new AssignmentDAO();
            int assignmentID = assignmentDAO.createEmptyAssignment(title, description);

            // 4. Tạo và liên kết course content
            CourseContent assignmentContent = new CourseContent();
            assignmentContent.setCourseID(courseID);
            assignmentContent.setTitle(title);
            assignmentContent.setDescription(description != null ? description : "");

            Assignment assignment = new Assignment();
            assignment.setAssignmentID(assignmentID);
            assignment.setAssignmentTitle(title);
            assignmentContent.setAssignment(assignment);

            // 5. Lưu vào database
            courseContentDAO = new CourseContentDAO();
            courseContentDAO.addCourseContentAssignment(assignmentContent);

            // 6. Chuyển hướng với thông báo thành công
            request.getSession().setAttribute("successMessage", "Tạo assignment thành công!");
            response.sendRedirect("edit-assignment?assignmentID=" + assignmentID + "&courseID=" + courseID);

        } catch (NumberFormatException e) {
            handleError(request, response, "ID khóa học không hợp lệ", courseID);
        } catch (IllegalArgumentException e) {
            handleError(request, response, e.getMessage(), courseID);
        } catch (SQLException e) {
            handleError(request, response, "Lỗi database: " + e.getMessage(), courseID);
        } catch (Exception e) {
            handleError(request, response, "Lỗi hệ thống: " + e.getMessage(), courseID);
        } finally {
            if (assignmentDAO != null) {
                try {
                    assignmentDAO.closeConnection();
                } catch (SQLException e) {
                    System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
                }
            }
            if (courseContentDAO != null) {
                courseContentDAO.closeConnection();
            }
        }
    }

//    private void handleAddExam(HttpServletRequest request, HttpServletResponse response, int courseID)
//            throws IOException, SQLException {
//        try {
//            // 1. Validate và lấy thông tin từ form
//            String title = request.getParameter("title");
//            String description = request.getParameter("description");
//            int duration = Integer.parseInt(request.getParameter("duration"));
//            int questionCount = Integer.parseInt(request.getParameter("questionCount"));
//
//            // 2. Kiểm tra dữ liệu đầu vào
//            if (title == null || title.trim().isEmpty()) {
//                throw new IllegalArgumentException("Tiêu đề exam không được để trống");
//            }
//            if (duration <= 0 || duration > 300) {
//                throw new IllegalArgumentException("Thời gian làm bài phải từ 1-300 phút");
//            }
//            if (questionCount <= 0 || questionCount > 100) {
//                throw new IllegalArgumentException("Số lượng câu hỏi phải từ 1-100");
//            }
//
//            // 3. Tạo exam với thông tin từ form
//            ExamDAO examDAO = new ExamDAO();
//            int examID = examDAO.createEmptyExam(title, description, duration, questionCount);
//
//            // 4. Tạo và liên kết course content
//            CourseContent examContent = new CourseContent();
//            examContent.setCourseID(courseID);
//            examContent.setTitle(title);
//            examContent.setDescription(description != null ? description : "");
//
//            Exam exam = new Exam();
//            exam.setExamID(examID);
//            exam.setExamTitle(title);
//            exam.setDuration(duration);
//            exam.setQuestionCount(questionCount);
//            examContent.setExam(exam);
//
//            // 5. Lưu vào database
//            courseContentDAO.addCourseContentExam(examContent);
//
//            // 6. Chuyển hướng với thông báo thành công
//            request.getSession().setAttribute("successMessage", "Tạo exam thành công!");
//            response.sendRedirect("edit-exam?examID=" + examID + "&courseID=" + courseID);
//
//        } catch (NumberFormatException e) {
//            handleError(request, response, "Dữ liệu không hợp lệ", courseID);
//        } catch (IllegalArgumentException e) {
//            handleError(request, response, e.getMessage(), courseID);
//        } catch (SQLException e) {
//            handleError(request, response, "Lỗi database: " + e.getMessage(), courseID);
//        } catch (Exception e) {
//            handleError(request, response, "Lỗi hệ thống: " + e.getMessage(), courseID);
//        }
//    }

    private void handleUpdateVideo(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, ServletException, SQLException {
        CourseContentDAO dao = null;
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");

            // Lấy thông tin video hiện tại
            dao = new CourseContentDAO();
            CourseContent currentContent = dao.getCourseContentById(contentID);

            // Cập nhật thông tin cơ bản
            currentContent.setTitle(title);
            currentContent.setDescription(description);

            // Kiểm tra xem có video mới được tải lên không
            Part newVideoPart = request.getPart("video");
            if (newVideoPart != null && newVideoPart.getSize() > 0) {
                // Có video mới, tải lên và cập nhật URL
                String newVideoUrl = dao.convertMediaToUrl(newVideoPart);
                currentContent.setMedia(newVideoUrl);
            }

            // Cập nhật vào database
            dao.updateCourseContent(currentContent);

            // Thêm thông báo thành công và chuyển hướng
            request.getSession().setAttribute("successMessage", "Cập nhật video thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);

        } catch (SQLException e) {
            handleError(request, response, "Lỗi database: " + e.getMessage(),
                    "edit-video?contentID=" + request.getParameter("contentID") + "&courseID=" + request.getParameter("courseID"));
        } catch (Exception e) {
            handleError(request, response, "Lỗi hệ thống: " + e.getMessage(),
                    "course-content?action=addContentView&courseID=" + courseID);
        } finally {
            if (dao != null) {
                dao.closeConnection();
            }
        }
    }

    private void handleUpdateAssignment(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, SQLException {
        AssignmentDAO assignmentDAO = null;
        try {
            // 1. Cập nhật thông tin cơ bản của Assignment
            Assignment assignment = new Assignment();
            assignment.setAssignmentID(Integer.parseInt(request.getParameter("assignmentID")));
            assignment.setAssignmentTitle(request.getParameter("title"));
            assignment.setDescription(request.getParameter("description"));

            assignmentDAO = new AssignmentDAO();
            assignmentDAO.updateAssignment(assignment);

            // 2. Xóa toàn bộ câu hỏi cũ của Assignment
            assignmentDAO.deleteAllQuestions(assignment.getAssignmentID());

            // 3. Thu thập câu hỏi và đáp án từ form
            List<Question> questions = new ArrayList<>();
            List<List<Answer>> answersList = new ArrayList<>();

            // Lấy số lượng câu hỏi từ form
            int questionCount = Integer.parseInt(request.getParameter("questionCount"));
            System.out.println("Số lượng câu hỏi: " + questionCount);

            // Duyệt qua từng câu hỏi
            for (int i = 1; i <= questionCount; i++) {
                String questionText = request.getParameter("questionText_" + i);
                String questionType = request.getParameter("questionType_" + i);
                String questionMarkStr = request.getParameter("questionMark_" + i);

                System.out.println("Đang xử lý câu hỏi " + i);
                System.out.println("Question Text: " + questionText);
                System.out.println("Question Type: " + questionType);
                System.out.println("Question Mark: " + questionMarkStr);

                if (questionText != null && !questionText.trim().isEmpty()) {
                    double questionMark = Double.parseDouble(questionMarkStr);

                    // Tạo đối tượng Question
                    Question question = new Question();
                    question.setQuestionText(questionText);
                    question.setQuestionType(questionType);
                    question.setQuestionMark(questionMark);
                    questions.add(question);

                    // Xử lý đáp án tùy theo loại câu hỏi
                    List<Answer> answers = new ArrayList<>();
                    if (questionType.equals("MULTIPLE_CHOICE")) {
                        processMultipleChoiceAnswers(request, i, answers);
                    } else if (questionType.equals("TRUE_FALSE")) {
                        processTrueFalseAnswers(request, i, answers);
                    } else {
                        processShortAnswer(request, i, answers);
                    }
                    answersList.add(answers);
                }
            }

            // 4. Lưu tất cả câu hỏi và đáp án vào database
            assignmentDAO.addQuestionsToAssignment(
                    assignment.getAssignmentID(),
                    assignment,
                    questions,
                    answersList
            );

            // 5. Thêm thông báo thành công và chuyển hướng
            request.getSession().setAttribute("successMessage", "Cập nhật Assignment thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + request.getParameter("courseID"));

        } catch (SQLException e) {
            handleError(request, response, "Lỗi database: " + e.getMessage(),
                    "edit-assignment?assignmentID=" + request.getParameter("assignmentID") + "&courseID=" + request.getParameter("courseID"));
        } catch (Exception e) {
            handleError(request, response, "Lỗi hệ thống: " + e.getMessage(),
                    "course-content?action=addContentView&courseID=" + request.getParameter("courseID"));
        } finally {
            if (assignmentDAO != null) {
                try {
                    assignmentDAO.closeConnection();
                } catch (SQLException e) {
                    System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
                }
            }
        }
    }

    private void processMultipleChoiceAnswers(HttpServletRequest request, int questionNum, List<Answer> answers) {
        // Xử lý 4 đáp án trắc nghiệm
        for (int i = 1; i <= 4; i++) {
            String answerText = request.getParameter("answerText_" + questionNum + "_" + i);
            String correctAnswer = request.getParameter("correctAnswer_" + questionNum);

            System.out.println("Đáp án " + i + ": " + answerText);
            System.out.println("Đáp án đúng: " + correctAnswer);

            if (answerText != null && !answerText.trim().isEmpty()) {
                Answer answer = new Answer();
                answer.setAnswerText(answerText);
                answer.setCorrect(correctAnswer != null && correctAnswer.equals(String.valueOf(i)));
                answer.setOptionLabel((char) ('A' + i - 1)); // Gán nhãn A, B, C, D
                answers.add(answer);
            }
        }
    }

    private void processTrueFalseAnswers(HttpServletRequest request, int questionNum, List<Answer> answers) {
        // Xử lý True/False
        String correctAnswer = request.getParameter("correctAnswer_" + questionNum);
        System.out.println("Đáp án True/False: " + correctAnswer);

        Answer trueAnswer = new Answer();
        trueAnswer.setAnswerText("True");
        trueAnswer.setCorrect(correctAnswer != null && correctAnswer.equals("1"));
        answers.add(trueAnswer);

        Answer falseAnswer = new Answer();
        falseAnswer.setAnswerText("False");
        falseAnswer.setCorrect(correctAnswer != null && correctAnswer.equals("2"));
        answers.add(falseAnswer);
    }

    private void processShortAnswer(HttpServletRequest request, int questionNum, List<Answer> answers) {
        // Xử lý câu hỏi tự luận ngắn
        String answerText = request.getParameter("correctAnswer_" + questionNum);
        System.out.println("Đáp án tự luận: " + answerText);

        if (answerText != null && !answerText.trim().isEmpty()) {
            Answer answer = new Answer();
            answer.setAnswerText(answerText);
            answer.setCorrect(true);
            answers.add(answer);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage, int courseID)
            throws IOException {
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage, String redirectUrl)
            throws IOException {
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect(redirectUrl);
    }

    private void handleDeleteContent(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, SQLException {
        CourseContentDAO courseContentDAO = null;
        AssignmentDAO assignmentDAO = null;
        ExamDAO examDAO = null;
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            System.out.println("Bắt đầu xóa nội dung với ID: " + contentID);

            courseContentDAO = new CourseContentDAO();

            // Lấy thông tin content trước khi xóa
            CourseContent content = courseContentDAO.getCourseContentById(contentID);
            System.out.println("Thông tin nội dung cần xóa: " + content);

            if (content == null) {
                System.out.println("Không tìm thấy nội dung với ID: " + contentID);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Không tìm thấy nội dung cần xóa!");
                return;
            }

            // Xóa các bản ghi liên quan trước
            if (content.getAssignment() != null) {
                System.out.println("Xóa assignment liên quan: " + content.getAssignment().getAssignmentID());
                assignmentDAO = new AssignmentDAO();
                assignmentDAO.deleteAllQuestions(content.getAssignment().getAssignmentID());
                assignmentDAO.deleteAssignment(content.getAssignment().getAssignmentID());
            }

//            if (content.getExam() != null) {
//                System.out.println("Xóa exam liên quan: " + content.getExam().getExamID());
//                examDAO = new ExamDAO();
//                examDAO.deleteExam(content.getExam().getExamID());
//            }

            // Xóa content
            System.out.println("Xóa nội dung chính");
            boolean success = courseContentDAO.deleteCourseContent(contentID);

            if (success) {
                System.out.println("Xóa nội dung thành công");
                // Xóa file media nếu có
                if (content.getMedia() != null && !content.getMedia().isEmpty()) {
                    System.out.println("Có file media cần xóa: " + content.getMedia());
                    // TODO: Implement delete media file from storage
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Đã xóa nội dung thành công!");
            } else {
                System.out.println("Không thể xóa nội dung");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Không thể xóa nội dung!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Lỗi định dạng ID: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID nội dung không hợp lệ!");
        } catch (SQLException e) {
            System.out.println("Lỗi database: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi database: " + e.getMessage());
        } finally {
            if (courseContentDAO != null) {
                courseContentDAO.closeConnection();
            }
            if (assignmentDAO != null) {
                try {
                    assignmentDAO.closeConnection();
                } catch (SQLException e) {
                    System.out.println("Lỗi khi đóng kết nối AssignmentDAO: " + e.getMessage());
                }
            }
//            if (examDAO != null) {
//                try {
//                    examDAO.closeConnection();
//                } catch (SQLException e) {
//                    System.out.println("Lỗi khi đóng kết nối ExamDAO: " + e.getMessage());
//                }
//            }
        }
    }
}
