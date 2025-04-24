package service;

import dao.ExamDAO;
import dao.ExamQuestionDAO;
import dao.QuestionAndAnswerDAO;
import model.Answer;
import model.Question;
import util.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ExamService {
    private ExamDAO examDAO = new ExamDAO(DBConnect.getInstance().getConnection());
    private QuestionAndAnswerDAO questionAndAnswerDAO = new QuestionAndAnswerDAO();
    private ExamQuestionDAO examQuestionDAO = new ExamQuestionDAO();

    /**
     * Thêm câu hỏi vào bài kiểm tra
     *
     * @param question     Đối tượng câu hỏi
     * @param answers      Danh sách các câu trả lời
     * @param isCorrect    Danh sách các câu trả lời đúng hay sai
     * @param optionLabels Danh sách các option_label
     * @param examID       ID của bài kiểm tra
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addQuestionToExam(Question question, String[] answers, String[] isCorrect, String[] optionLabels, int examID) throws SQLException {
        Connection conn = null;
        try {
            // Lấy connection và tắt auto commit
            conn = DBConnect.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Không thể tạo kết nối database");
            }
            conn.setAutoCommit(false);
            System.out.println("Bắt đầu transaction");

            // Thêm câu hỏi vào database và lấy ID
            int questionId = questionAndAnswerDAO.addQuestion(question, conn);
            System.out.println("Đã thêm câu hỏi với ID: " + questionId);

            if (questionId <= 0) {
                throw new SQLException("Không thể thêm câu hỏi vào database");
            }

            // Thêm liên kết giữa exam và question
            String eQuesType = question.getAudioFile() != null ? "Listening" : "Reading";
            examQuestionDAO.saveExamQuestion(examID, questionId, eQuesType, conn);
            System.out.println("Đã thêm liên kết exam-question: examID=" + examID + ", questionId=" + questionId + ", type=" + eQuesType);

            // Nếu là câu hỏi trắc nghiệm, thêm các câu trả lời
            if ("multiple_choice".equals(question.getQuestionType()) && answers != null && isCorrect != null) {
                System.out.println("Bắt đầu thêm " + answers.length + " câu trả lời cho câu hỏi ID " + questionId);
                for (int i = 0; i < answers.length; i++) {
                    Answer answer = new Answer();
                    answer.setQuestionID(questionId);
                    answer.setAnswerText(answers[i]);
                    answer.setCorrect(isCorrect[i].equals("1")); // 1 = đúng, 0 = sai
                    answer.setOptionLabel(String.valueOf((char) ('A' + i)));

                    System.out.println("Thêm câu trả lời thứ " + (i + 1) + ": " + answer.getAnswerText() +
                            " (Đúng/Sai: " + answer.isCorrect() + ", Label: " + answer.getOptionLabel() + ")");

                    boolean answerAdded = questionAndAnswerDAO.addAnswer(answer, conn);
                    if (!answerAdded) {
                        throw new SQLException("Không thể thêm câu trả lời thứ " + (i + 1));
                    }
                    System.out.println("Đã thêm câu trả lời " + (i + 1) + " thành công");
                }
            }

            // Commit nếu mọi thứ thành công
            conn.commit();
            System.out.println("Đã commit transaction thành công");
            return true;

        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.rollback();
                    System.out.println("Đã rollback do lỗi: " + e.getMessage());
                } catch (SQLException ex) {
                    System.out.println("Lỗi khi rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.out.println("Lỗi trong addQuestionToExam: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Reset auto commit và đóng connection
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Lỗi khi set auto commit: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Lấy tất cả câu hỏi và câu trả lời của một assignment
     *
     * @param examID ID của examID
     * @return Danh sách các câu hỏi kèm theo câu trả lời
     */
    public List<Question> getAllQuestionsAndAnswersOfExam(int examID) throws SQLException {
        List<Question> questions = examQuestionDAO.gellAllQuestionsOfExam(examID);
        for (Question question : questions) {
            List<Answer> answers = questionAndAnswerDAO.getAllAnswerOfOneQuestion(question.getQuestionID());
            question.setAnswers(answers);
        }
        return questions;
    }

    /**
     * Xóa câu hỏi trong bài kiểm tra
     *
     * @param questionID ID câu hỏi cần xóa
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean deleteExamQuestion(int questionID) {
        try {
            examQuestionDAO.deleteExamQuestion(questionID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin câu hỏi và câu trả lời
     *
     * @param question Đối tượng câu hỏi
     * @param answers  Danh sách câu trả lời
     * @throws SQLException nếu có lỗi khi cập nhật
     */
    public void updateQuestionForExam(Question question, List<Answer> answers) throws SQLException {
        // Cập nhật thông tin câu hỏi
        questionAndAnswerDAO.updateQuestion(question);

        // Xóa tất cả câu trả lời cũ
        questionAndAnswerDAO.deleteAnswersByQuestionId(question.getQuestionID());

        // Thêm các câu trả lời mới
        if (answers != null && !answers.isEmpty()) {
            for (Answer answer : answers) {
                answer.setQuestionID(question.getQuestionID());
                questionAndAnswerDAO.addAnswer(answer, DBConnect.getInstance().getConnection());
            }
        }
    }

    public List<Question> getAllQuestionOfExam(int examID) {
        return examQuestionDAO.gellAllQuestionsOfExam(examID);
    }

    /**
     * Lấy tất cả câu hỏi của một exam
     *
     * @param questionID ID của question cần lấy câu trả lời
     * @return Danh sách các câu trả lời của câu hỏi đó
     */
    public List<Answer> getAllAnswerOfThisQuestion(int questionID) throws SQLException {
        return questionAndAnswerDAO.getAllAnswerOfOneQuestion(questionID);
    }

    public Question getQuestionById(int questionID) throws SQLException {
        return questionAndAnswerDAO.getQuestionById(questionID);
    }
}
