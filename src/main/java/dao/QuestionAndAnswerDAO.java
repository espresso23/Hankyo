package dao;

import model.Answer;
import model.Question;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionAndAnswerDAO {

    public int addQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO Question (questionText,questionImg,audio_file, questionType, questionMark) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getQuestionImage());
            ps.setString(3, question.getAudioFile());
            ps.setString(4, question.getQuestionType());
            ps.setDouble(5, question.getQuestionMark());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Không thể tạo câu hỏi, không nhận được ID.");
    }

    public void addAnswer(Answer answer) throws SQLException {
        String sql = "INSERT INTO Answer (questionID, answerText, isCorrect, option_label) VALUES (?, ?, ?,?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, answer.getQuestionID());
            ps.setString(2, answer.getAnswerText());
            ps.setBoolean(3, answer.isCorrect());
            ps.setString(4, answer.getOptionLabel());
            ps.executeUpdate();
        }
    }


    public List<Answer> getAllAnswerOfOneQuestion(int questionId) throws SQLException {
        String sql = "SELECT * FROM Answer WHERE questionID = ?";
        List<Answer> answers = new ArrayList<>();

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, questionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Answer answer = new Answer();
                    answer.setAnswerID(rs.getInt("answerID"));
                    answer.setQuestionID(rs.getInt("questionID"));
                    answer.setAnswerText(rs.getString("answerText"));
                    answer.setCorrect(rs.getBoolean("isCorrect"));
                    answer.setOptionLabel(rs.getString("option_label"));
                    answers.add(answer);
                }
            }
        }

        return answers;
    }

    public Question getQuestionById(int questionID) throws SQLException {
        String sql = "SELECT * FROM Question WHERE questionID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, questionID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Question question = new Question();
                question.setQuestionID(rs.getInt("questionID"));
                question.setQuestionText(rs.getString("questionText"));
                question.setQuestionImage(rs.getString("questionImg"));
                question.setAudioFile(rs.getString("audio_file"));
                question.setQuestionType(rs.getString("questionType"));
                question.setQuestionMark(rs.getDouble("questionMark"));
                return question;
            }
        }

        return null;
    }

    public void updateAnswer(Answer answer) throws SQLException {
        String sql = "UPDATE Answer SET answerText = ?, isCorrect = ?, option_label = ? WHERE questionID = ? AND option_label = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, answer.getAnswerText());
            ps.setBoolean(2, answer.isCorrect());
            ps.setString(3, answer.getOptionLabel());
            ps.setInt(4, answer.getQuestionID());
            ps.setString(5, answer.getOptionLabel());
            ps.executeUpdate();
        }
    }

    public void deleteAnswersByQuestionId(int questionId) throws SQLException {
        String sql = "DELETE FROM Answer WHERE questionID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, questionId);
            ps.executeUpdate();
        }
    }
}
