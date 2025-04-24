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

    public int addQuestion(Question question, Connection conn) throws SQLException {
        String sql = "INSERT INTO Question (questionText,questionImg,audio_file, questionType, questionMark) VALUES (?, ?, ?, ?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getQuestionImage());
            ps.setString(3, question.getAudioFile());
            ps.setString(4, question.getQuestionType());
            ps.setDouble(5, question.getQuestionMark());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Generated Question ID: " + id);
                    return id;
                }
            }
        }

        throw new SQLException("Không thể tạo câu hỏi, không nhận được ID.");
    }

    public boolean addAnswer(Answer answer, Connection conn) throws SQLException {
        String sql = "INSERT INTO Answer (questionID, answerText, isCorrect, option_label) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, answer.getQuestionID());
            ps.setString(2, answer.getAnswerText());
            ps.setBoolean(3, answer.isCorrect());
            ps.setString(4, answer.getOptionLabel());
            
            System.out.println("Executing SQL: " + sql);
            System.out.println("Parameters: questionID=" + answer.getQuestionID() + 
                             ", text=" + answer.getAnswerText() + 
                             ", isCorrect=" + answer.isCorrect() + 
                             ", option_label=" + answer.getOptionLabel());
            
            int result = ps.executeUpdate();
            System.out.println("Answer insert result: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error while adding answer: " + e.getMessage());
            e.printStackTrace();
            throw e;
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

    public void updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE Question SET questionText = ?, questionImg = ?, audio_file = ?, questionType = ?, questionMark = ? WHERE questionID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getQuestionImage());
            ps.setString(3, question.getAudioFile());
            ps.setString(4, question.getQuestionType());
            ps.setDouble(5, question.getQuestionMark());
            ps.setInt(6, question.getQuestionID());
            ps.executeUpdate();
        }
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
