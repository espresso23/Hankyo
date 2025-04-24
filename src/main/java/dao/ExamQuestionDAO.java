package dao;

import model.Question;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamQuestionDAO {

    public void saveExamQuestion(int examId, int questionId, String eQuesType, Connection conn) throws SQLException {
        System.out.println("Saving exam question: " + examId + " - " + questionId + " - " + eQuesType);
        String sql = "INSERT INTO exam_question (examID, questionID, eQuesType) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, examId);
            ps.setInt(2, questionId);
            ps.setString(3, eQuesType);
            int result = ps.executeUpdate();
            System.out.println("ExamQuestion insert result: " + result);
            if (result <= 0) {
                throw new SQLException("Không thể thêm liên kết exam-question");
            }
        } catch (SQLException e) {
            System.out.println("Error while saving exam question: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteExamQuestion(int questionID) throws SQLException {
        String deleteExamQuestionSQL = "DELETE FROM Exam_Question WHERE questionID = ?";
        String deleteAnswersSQL = "DELETE FROM Answer WHERE questionID = ?";
        String deleteQuestionSQL = "DELETE FROM Question WHERE questionID = ?";
        Connection connection1 = DBConnect.getInstance().getConnection();
        try {
            // Bắt đầu transaction
            connection1.setAutoCommit(false);

            // Xóa các bản ghi trong Assignment_Question
            try (PreparedStatement pstmt = connection1.prepareStatement(deleteExamQuestionSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            // Xóa các câu trả lời
            try (PreparedStatement pstmt = connection1.prepareStatement(deleteAnswersSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            // Xóa câu hỏi
            try (PreparedStatement pstmt = connection1.prepareStatement(deleteQuestionSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            // Commit transaction
            connection1.commit();
        } catch (SQLException e) {
            connection1.rollback();
            throw e;
        } finally {
            connection1.setAutoCommit(true);
        }
    }

    public List<Question> gellAllQuestionsOfExam(int examID) {
        String sql = "SELECT q.*, eq.examID\n" +
                "FROM Question q\n" +
                "JOIN Exam_Question eq ON q.questionID = eq.questionID\n" +
                "WHERE eq.examID = ?;";
        List<Question> questions = null;
        try {
            Connection con = DBConnect.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, examID);
            ResultSet rs = ps.executeQuery();
            questions = new ArrayList<>();
            while (rs.next()) {
                Question question = new Question();
                question.setQuestionID(rs.getInt("questionID"));
                question.setQuestionText(rs.getString("questionText"));
                question.setQuestionImage(rs.getString("questionImg"));
                question.setAudioFile(rs.getString("audio_file"));
                question.setQuestionType(rs.getString("questionType"));
                question.setQuestionMark(rs.getDouble("questionMark"));
                question.setExamID(rs.getInt("examID"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

}
