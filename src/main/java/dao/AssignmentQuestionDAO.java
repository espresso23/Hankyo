package dao;

import model.AssignmentQuestion;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentQuestionDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public List<AssignmentQuestion> getQuestionsByAssignmentId(int assignmentId) {
        List<AssignmentQuestion> questions = new ArrayList<>();
        String query = "SELECT aq.assignmentQuesID, aq.assignmentID, aq.questionID, "
                    + "q.questionText, q.questionImg, q.audio_file, q.questionType, q.questionMark "
                    + "FROM Assignment_Question aq "
                    + "JOIN Question q ON aq.questionID = q.questionID "
                    + "WHERE aq.assignmentID = ?";
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, assignmentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        } catch (Exception e) {
            System.out.println("Error at getQuestionsByAssignmentId: " + e.getMessage());
        } finally {
            closeResources();
        }
        return questions;
    }

    public AssignmentQuestion getQuestionById(int questionId) {
        String query = "SELECT aq.assignmentQuesID, aq.assignmentID, aq.questionID, "
                    + "q.questionText, q.questionImg, q.audio_file, q.questionType, q.questionMark "
                    + "FROM Assignment_Question aq "
                    + "JOIN Question q ON aq.questionID = q.questionID "
                    + "WHERE aq.questionID = ?";
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, questionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToQuestion(rs);
            }
        } catch (Exception e) {
            System.out.println("Error at getQuestionById: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    private AssignmentQuestion mapResultSetToQuestion(ResultSet rs) throws SQLException {
        AssignmentQuestion question = new AssignmentQuestion();
        question.setAssignQuesID(rs.getInt("assignmentQuesID"));
        question.setAssignmentID(rs.getInt("assignmentID"));
        question.setQuestionID(rs.getInt("questionID"));
        question.setQuestionText(rs.getString("questionText"));
        question.setQuestionImg(rs.getString("questionImg"));
        question.setAudioFile(rs.getString("audio_file"));
        question.setQuestionType(rs.getString("questionType"));
        question.setQuestionMark(rs.getDouble("questionMark"));
        return question;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
} 