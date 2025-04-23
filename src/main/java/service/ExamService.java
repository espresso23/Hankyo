package service;

import dao.ExamDAO;
import dao.ExamQuestionDAO;
import dao.QuestionAndAnswerDAO;
import model.Answer;
import model.Question;
import util.DBConnect;

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
        // Thêm câu hỏi vào database
        try {
            int questionId = questionAndAnswerDAO.addQuestion(question);
            if (questionId != 0) {
                String eQuesType = question.getAudioFile() != null ? "Listening" : "Reading";
                examQuestionDAO.saveExamQuestion(examID, questionId, eQuesType);
            }

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
                questionAndAnswerDAO.addAnswer(answer);
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
