package dao;

import model.Answer;
import model.Question;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionAndAnswerDAO {
    private static final Logger LOGGER = Logger.getLogger(QuestionAndAnswerDAO.class.getName());
    private final Connection connection;

    public QuestionAndAnswerDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public int addQuestion(Question question) throws SQLException {
        String sqlQuestion = "INSERT INTO Question (questionText, questionImg, audio_file, questionType, questionMark, correctAnswer) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAnswer = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
        String sqlAssignmentQuestion = "INSERT INTO Assignment_Question (assignmentID, questionID, answerID) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            int questionID = insertQuestion(question, sqlQuestion);
            List<Integer> answerIDs = insertAnswers(question, questionID, sqlAnswer);
            linkQuestionToAssignment(question, questionID, answerIDs, sqlAssignmentQuestion);
            connection.commit();
            LOGGER.log(Level.INFO, "Đã thêm câu hỏi {0} thành công", questionID);
            return questionID;
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm câu hỏi", e);
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private int insertQuestion(Question question, String sql) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, question.getQuestionText());
            pstmt.setString(2, question.getQuestionImage());
            pstmt.setString(3, question.getAudioFile());
            pstmt.setString(4, question.getQuestionType());
            pstmt.setDouble(5, question.getQuestionMark());
            pstmt.setString(6, question.getCorrectAnswer());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Không thể lấy ID của câu hỏi mới");
            }
        }
    }

    private List<Integer> insertAnswers(Question question, int questionID, String sql) throws SQLException {
        List<Integer> answerIDs = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Answer answer : question.getAnswers()) {
                pstmt.setString(1, answer.getAnswerText());
                pstmt.setBoolean(2, answer.isCorrect());
                pstmt.setString(3, String.valueOf(answer.getOptionLabel()));
                pstmt.setInt(4, questionID);
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        answerIDs.add(rs.getInt(1));
                    }
                }
            }
        }
        return answerIDs;
    }

    private void linkQuestionToAssignment(Question question, int questionID, List<Integer> answerIDs, String sql) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int answerID : answerIDs) {
                pstmt.setInt(1, question.getAssignmentID());
                pstmt.setInt(2, questionID);
                pstmt.setInt(3, answerID);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public void updateQuestion(Question question, List<Answer> answers) throws SQLException {
        String updateQuestionSQL = "UPDATE Question SET questionText = ?, questionImg = ?, audio_file = ?, questionType = ?, questionMark = ?, correctAnswer = ? WHERE questionID = ?";
        String deleteAnswersSQL = "DELETE FROM Answer WHERE questionID = ?";
        String deleteAssignmentQuestionSQL = "DELETE FROM Assignment_Question WHERE questionID = ?";
        String insertAnswerSQL = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
        String insertAssignmentQuestionSQL = "INSERT INTO Assignment_Question (assignmentID, questionID, answerID) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            
            // 1. Cập nhật thông tin câu hỏi
            try (PreparedStatement pstmt = connection.prepareStatement(updateQuestionSQL)) {
                pstmt.setString(1, question.getQuestionText());
                pstmt.setString(2, question.getQuestionImage());
                pstmt.setString(3, question.getAudioFile());
                pstmt.setString(4, question.getQuestionType());
                pstmt.setDouble(5, question.getQuestionMark());
                pstmt.setString(6, question.getCorrectAnswer());
                pstmt.setInt(7, question.getQuestionID());
                pstmt.executeUpdate();
            }

            // 2. Xóa các câu trả lời cũ và liên kết
            try (PreparedStatement pstmt = connection.prepareStatement(deleteAssignmentQuestionSQL)) {
                pstmt.setInt(1, question.getQuestionID());
                pstmt.executeUpdate();
            }
            
            try (PreparedStatement pstmt = connection.prepareStatement(deleteAnswersSQL)) {
                pstmt.setInt(1, question.getQuestionID());
                pstmt.executeUpdate();
            }

            // 3. Thêm các câu trả lời mới
            try (PreparedStatement pstmt = connection.prepareStatement(insertAnswerSQL, Statement.RETURN_GENERATED_KEYS)) {
                for (Answer answer : answers) {
                    pstmt.setString(1, answer.getAnswerText());
                    pstmt.setBoolean(2, answer.isCorrect());
                    pstmt.setString(3, String.valueOf(answer.getOptionLabel()));
                    pstmt.setInt(4, question.getQuestionID());
                    pstmt.executeUpdate();
                    
                    // Lấy ID của câu trả lời vừa thêm
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int answerID = rs.getInt(1);
                            // 4. Thêm liên kết Assignment_Question
                            try (PreparedStatement linkStmt = connection.prepareStatement(insertAssignmentQuestionSQL)) {
                                linkStmt.setInt(1, question.getAssignmentID());
                                linkStmt.setInt(2, question.getQuestionID());
                                linkStmt.setInt(3, answerID);
                                linkStmt.executeUpdate();
                            }
                        }
                    }
                }
            }

            connection.commit();
            LOGGER.log(Level.INFO, "Đã cập nhật câu hỏi {0} thành công", question.getQuestionID());
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật câu hỏi", e);
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void deleteQuestion(int questionID) throws SQLException {
        String deleteAssignmentQuestionSQL = "DELETE FROM Assignment_Question WHERE questionID = ?";
        String deleteAnswersSQL = "DELETE FROM Answer WHERE questionID = ?";
        String deleteQuestionSQL = "DELETE FROM Question WHERE questionID = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(deleteAssignmentQuestionSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = connection.prepareStatement(deleteAnswersSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = connection.prepareStatement(deleteQuestionSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            connection.commit();
            LOGGER.log(Level.INFO, "Đã xóa câu hỏi {0} thành công", questionID);
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa câu hỏi", e);
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Question> getQuestionsByAssignmentId(int assignmentID) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT DISTINCT q.*, a.answerID, a.answerText, a.isCorrect, a.option_label " +
                "FROM Question q " +
                "JOIN Assignment_Question aq ON q.questionID = aq.questionID " +
                "JOIN Answer a ON aq.answerID = a.answerID " +
                "WHERE aq.assignmentID = ? " +
                "ORDER BY q.questionID, a.answerID";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, assignmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                Question currentQuestion = null;
                while (rs.next()) {
                    int questionId = rs.getInt("questionID");

                    if (currentQuestion == null || currentQuestion.getQuestionID() != questionId) {
                        currentQuestion = createQuestionFromResultSet(rs, assignmentID);
                        questions.add(currentQuestion);
                    }

                    Answer answer = createAnswerFromResultSet(rs, questionId);
                    currentQuestion.getAnswers().add(answer);
                }
            }
        }

        return questions;
    }

    private Question createQuestionFromResultSet(ResultSet rs, int assignmentID) throws SQLException {
        Question question = new Question();
        question.setQuestionID(rs.getInt("questionID"));
        question.setQuestionText(rs.getString("questionText"));
        question.setQuestionImage(rs.getString("questionImg"));
        question.setAudioFile(rs.getString("audio_file"));
        question.setQuestionType(rs.getString("questionType"));
        question.setQuestionMark(rs.getDouble("questionMark"));
        question.setCorrectAnswer(rs.getString("correctAnswer"));
        question.setAssignmentID(assignmentID);
        question.setAnswers(new ArrayList<>());
        return question;
    }

    private Answer createAnswerFromResultSet(ResultSet rs, int questionId) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerID(rs.getInt("answerID"));
        answer.setAnswerText(rs.getString("answerText"));
        answer.setCorrect(rs.getBoolean("isCorrect"));
        answer.setOptionLabel(rs.getString("option_label").charAt(0));
        answer.setQuestionID(questionId);
        return answer;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
