package service;

import dao.AssignmentDAO;
import dao.QuestionAndAnswerDAO;
import dao.AssignmentQuestionDAO;
import dao.AssignmentTakenDAO;
import dao.AssignmentResultDAO;
import model.Answer;
import model.Assignment;
import model.Question;
import model.AssignmentQuestion;
import model.AssignmentTaken;
import model.AssignmentResult;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AssignmentService {
    private final AssignmentDAO assignmentDAO;
    private final QuestionAndAnswerDAO questionAndAnswerDAO;
    private final AssignmentQuestionDAO questionDAO;
    private final AssignmentTakenDAO takenDAO;
    private final AssignmentResultDAO resultDAO;

    public AssignmentService() {
        this.assignmentDAO = new AssignmentDAO();
        this.questionAndAnswerDAO = new QuestionAndAnswerDAO();
        this.questionDAO = new AssignmentQuestionDAO();
        this.takenDAO = new AssignmentTakenDAO();
        this.resultDAO = new AssignmentResultDAO();
    }

    /**
     * Tạo một assignment mới trống
     *
     * @param title       Tiêu đề assignment
     * @param description Mô tả assignment
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createEmptyAssignment(String title, String description) {
        try {
            assignmentDAO.createEmptyAssignment(title, description);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm danh sách câu hỏi và câu trả lời vào assignment
     *
     * @param assignmentID ID của assignment
     * @param questionID   ID của question
     */
    public void addQuestionsToAssignment(int assignmentID, int questionID) {
        assignmentDAO.addQuestionsToAssignment(assignmentID, questionID);
    }

    /**
     * Lấy tất cả các assignment
     *
     * @return Danh sách các assignment
     */
    public List<Assignment> getAllAssignments() {
        return assignmentDAO.getAllAssignments();
    }

    /**
     * Cập nhật thông tin assignment
     *
     * @param assignment Đối tượng assignment cần cập nhật
     * @throws SQLException nếu có lỗi khi cập nhật
     */
    public void updateAssignment(Assignment assignment) throws SQLException {
        assignmentDAO.updateAssignment(assignment);
    }

    /**
     * Lấy assignment theo ID
     *
     * @param assignmentID ID của assignment cần lấy
     * @return Đối tượng assignment
     * @throws SQLException nếu có lỗi khi truy vấn
     */
    public Assignment getAssignmentById(int assignmentID) throws SQLException {
        return assignmentDAO.getAssignmentById(assignmentID);
    }

    /**
     * Xóa tất cả câu hỏi của một assignment
     *
     * @param assignmentID ID của assignment
     * @throws SQLException nếu có lỗi khi xóa
     */
    public void deleteAllQuestions(int assignmentID) throws SQLException {
        assignmentDAO.deleteAllQuestions(assignmentID);
    }

    /**
     * Lấy tất cả câu hỏi của một assignment
     *
     * @param assignmentID ID của assignment
     * @return Danh sách các câu hỏi
     */
    public List<Question> getAllQuestionOfAssignment(int assignmentID) {
        return assignmentDAO.getAllQuestionOfAssignment(assignmentID);
    }
    /**
     * Lấy tất cả câu hỏi của một assignment
     *
     * @param questionID ID của assignment
     * @return Danh sách các câu trả lời của câu hỏi đó
     */
    public List<Answer> getAllAnswerOfThisQuestion(int questionID) throws SQLException {
        return questionAndAnswerDAO.getAllAnswerOfOneQuestion(questionID);
    }
    /**
     * Cập nhật thông tin câu hỏi và câu trả lời
     *
     * @param question Đối tượng câu hỏi
     * @param answers  Danh sách câu trả lời
     * @throws SQLException nếu có lỗi khi cập nhật
     */
    public void updateQuestionForAssignment(Question question, List<Answer> answers) throws SQLException {
        // Cập nhật thông tin câu hỏi
        assignmentDAO.updateQuestionForAssignment(question);

        // Xóa tất cả câu trả lời cũ
        questionAndAnswerDAO.deleteAnswersByQuestionId(question.getQuestionID());

        // Thêm các câu trả lời mới
        if (answers != null && !answers.isEmpty()) {
            for (Answer answer : answers) {
                answer.setQuestionID(question.getQuestionID());
                questionAndAnswerDAO.addAnswer(answer);
            }
        }
    }

    /**
     * Xóa một câu hỏi
     *
     * @param questionID ID của câu hỏi cần xóa
     * @throws SQLException nếu có lỗi khi xóa
     */
    public void deleteQuestion(int questionID) throws SQLException {
        assignmentDAO.deleteQuestion(questionID);
    }

    /**
     * Xóa một assignment và tất cả câu hỏi, câu trả lời liên quan
     *
     * @param assignmentID ID của assignment cần xóa
     * @throws SQLException nếu có lỗi khi xóa
     */
    public void deleteAssignment(int assignmentID) throws SQLException {
        assignmentDAO.deleteAssignment(assignmentID);
    }

    /**
     * Đóng kết nối database
     *
     * @throws SQLException nếu có lỗi khi đóng kết nối
     */
    public void closeConnection() throws SQLException {
        assignmentDAO.closeConnection();
    }

    /**
     * Lấy tất cả câu hỏi và câu trả lời của một assignment
     *
     * @param assignmentID ID của assignment
     * @return Danh sách các câu hỏi kèm theo câu trả lời
     */
    public List<Question> getAllQuestionsAndAnswersOfAssignment(int assignmentID) throws SQLException {
        List<Question> questions = assignmentDAO.getAllQuestionOfAssignment(assignmentID);
        for (Question question : questions) {
            List<Answer> answers = questionAndAnswerDAO.getAllAnswerOfOneQuestion(question.getQuestionID());
            question.setAnswers(answers);
        }
        return questions;
    }

    public boolean addQuestion(Question question, String[] answers, String[] isCorrect, String[] optionLabels, int assignmentID) throws SQLException {
        // Thêm câu hỏi vào database
        try {
            int questionId = questionAndAnswerDAO.addQuestion(question);
            assignmentDAO.addQuestionsToAssignment(assignmentID, questionId);

            // Cập nhật audio file và question image nếu có

            // Nếu là câu hỏi trắc nghiệm, thêm các câu trả lời
            if (question.getQuestionType().equals("multiple_choice") && answers != null && isCorrect != null) {
                for (int i = 0; i < answers.length; i++) {
                    Answer answer = new Answer();
                    answer.setQuestionID(questionId);
                    answer.setAnswerText(answers[i]);
                    answer.setCorrect(isCorrect[i].equals("1")); // 1 = đúng, 0 = sai
                    answer.setOptionLabel(optionLabels[i]); // Thêm option_label
                    questionAndAnswerDAO.addAnswer(answer);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Hàm main để test chức năng
     */
    public static void main(String[] args) {
        try {
            AssignmentService service = new AssignmentService();

            // Test lấy tất cả assignment
            System.out.println("=== DANH SÁCH TẤT CẢ ASSIGNMENT ===");
            List<Assignment> assignments = service.getAllAssignments();
            for (Assignment assignment : assignments) {
                System.out.println("Assignment ID: " + assignment.getAssignmentID());
                System.out.println("Tiêu đề: " + assignment.getAssignmentTitle());
                System.out.println("------------------------");
            }

            // Test lấy câu hỏi và câu trả lời của một assignment cụ thể
            int testAssignmentId = 5; // Thay đổi ID này để test với assignment khác
            System.out.println("\n=== CHI TIẾT ASSIGNMENT ID " + testAssignmentId + " ===");

            // Lấy thông tin assignment
            Assignment assignment = service.getAssignmentById(testAssignmentId);
            if (assignment != null) {
                System.out.println("Tiêu đề: " + assignment.getAssignmentTitle());
                System.out.println("Mô tả: " + assignment.getDescription());

                // Lấy câu hỏi và câu trả lời
                List<Question> questions = service.getAllQuestionsAndAnswersOfAssignment(testAssignmentId);
                System.out.println("\nSố câu hỏi: " + questions.size());

                // In chi tiết từng câu hỏi và câu trả lời
                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    System.out.println("\nCâu hỏi " + (i + 1) + ":");
                    System.out.println("Nội dung: " + question.getQuestionText());
                    System.out.println("Loại câu hỏi: " + question.getQuestionType());
                    System.out.println("Điểm: " + question.getQuestionMark());

                    if (question.getQuestionImage() != null) {
                        System.out.println("Hình ảnh: " + question.getQuestionImage());
                    }
                    if (question.getAudioFile() != null) {
                        System.out.println("File âm thanh: " + question.getAudioFile());
                    }

                    System.out.println("Các câu trả lời:");
                    for (Answer answer : question.getAnswers()) {
                        System.out.println("- " + answer.getAnswerText() +
                                " (Đáp án " + answer.getOptionLabel() + ")" +
                                (answer.isCorrect() ? " [ĐÚNG]" : " [SAI]"));
                    }
                }
            } else {
                System.out.println("Không tìm thấy assignment với ID: " + testAssignmentId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // Đóng kết nối
                new AssignmentService().closeConnection();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    /**
     * Lấy thông tin một câu hỏi theo ID
     *
     * @param questionID ID của câu hỏi
     * @return Đối tượng Question hoặc null nếu không tìm thấy
     */
    public Question getQuestionById(int questionID) throws SQLException {
        return questionAndAnswerDAO.getQuestionById(questionID);
    }

    // Lấy thông tin bài tập và câu hỏi
    public Assignment getAssignmentWithQuestions(int assignmentID) throws SQLException {
        Assignment assignment = assignmentDAO.getAssignmentById(assignmentID);
        if (assignment != null) {
            List<AssignmentQuestion> questions = questionDAO.getQuestionsByAssignmentId(assignmentID);
            assignment.setAssignmentQuestions(questions);
        }
        return assignment;
    }

    // Tạo một bài làm mới
    public AssignmentTaken createAssignmentTaken(int assignmentID, int learnerID) {
        AssignmentTaken taken = new AssignmentTaken();
        taken.setAssignmentID(assignmentID);
        taken.setLearnerID(learnerID);
        taken.setDateCreated(new Date());
        taken.setFinalMark(0);
        taken.setSkipQues(0);
        taken.setDoneQues(0);

        if (takenDAO.addAssignmentTaken(taken)) {
            return taken;
        }
        return null;
    }

    // Lấy bài làm theo ID
    public AssignmentTaken getAssignmentTakenById(int takenID) {
        return takenDAO.getAssignmentTakenById(takenID);
    }

    // Lấy danh sách câu hỏi của bài tập
    public List<AssignmentQuestion> getQuestionsByAssignmentId(int assignmentID) {
        return questionDAO.getQuestionsByAssignmentId(assignmentID);
    }

    // Lưu kết quả một câu trả lời
    public boolean saveAssignmentResult(AssignmentResult result) {
        return resultDAO.addAssignmentResult(result);
    }

    // Cập nhật thông tin bài làm
    public boolean updateAssignmentTaken(AssignmentTaken taken) {
        return takenDAO.updateAssignmentTaken(taken);
    }

    // Lấy kết quả của một bài làm
    public List<AssignmentResult> getResultsByTakenID(int takenID) {
        return resultDAO.getResultsByTakenID(takenID);
    }

    // Kiểm tra xem học viên đã làm bài này chưa
    public boolean hasCompletedAssignment(int assignmentID, int learnerID) {
        AssignmentTaken taken = takenDAO.getAssignmentTakenByLearnerAndAssignment(learnerID, assignmentID);
        return taken != null;
    }
} 