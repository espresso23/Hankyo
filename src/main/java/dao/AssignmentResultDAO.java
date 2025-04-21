package dao;

import model.AssignmentResult;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentResultDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public boolean addAssignmentResult(AssignmentResult result) {
        String sql = "INSERT INTO Assignment_Result (assignmentQuesID, learnerID, mark, answerLabel, answerIsCorrect, assignTakenID) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, result.getAssignmentQuesID());
            ps.setInt(2, result.getLearnerID());
            ps.setFloat(3, result.getMark());
            ps.setString(4, result.getAnswerLabel());
            ps.setBoolean(5, result.isAnswerIsCorrect());
            ps.setInt(6, result.getAssignTakenID());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result.setResultID(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    public List<AssignmentResult> getResultsByTakenID(int takenID) {
        List<AssignmentResult> results = new ArrayList<>();
        String sql = "SELECT * FROM Assignment_Result WHERE assignTakenID = ?";
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, takenID);
            rs = ps.executeQuery();

            while (rs.next()) {
                AssignmentResult result = new AssignmentResult();
                result.setResultID(rs.getInt("resultID"));
                result.setAssignmentQuesID(rs.getInt("assignmentQuesID"));
                result.setLearnerID(rs.getInt("learnerID"));
                result.setMark(rs.getFloat("mark"));
                result.setAnswerLabel(rs.getString("answerLabel"));
                result.setAnswerIsCorrect(rs.getBoolean("answerIsCorrect"));
                result.setAssignTakenID(rs.getInt("assignTakenID"));
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return results;
    }

    public List<AssignmentResult> getResultsByLearnerAndAssignment(int learnerID, int assignmentID) {
        List<AssignmentResult> results = new ArrayList<>();
        String sql = "SELECT r.* FROM Assignment_Result r " +
                    "JOIN Assignment_Taken t ON r.assignTakenID = t.assignTakenID " +
                    "JOIN Assignment_Question aq ON r.assignmentQuesID = aq.assignmentQuesID " +
                    "WHERE t.learnerID = ? AND t.assignmentID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, learnerID);
            ps.setInt(2, assignmentID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AssignmentResult result = new AssignmentResult();
                result.setResultID(rs.getInt("resultID"));
                result.setAssignmentQuesID(rs.getInt("assignmentQuesID"));
                result.setLearnerID(rs.getInt("learnerID"));
                result.setMark(rs.getFloat("mark"));
                result.setAnswerLabel(rs.getString("answerLabel"));
                result.setAnswerIsCorrect(rs.getBoolean("answerIsCorrect"));
                result.setAssignTakenID(rs.getInt("assignTakenID"));
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Lưu kết quả trả lời của một câu hỏi
    public boolean saveAnswer(AssignmentResult result) throws SQLException {
        String sql = "INSERT INTO Assignment_Result (assignmentQuesID, learnerID, mark, answerLabel, answerIsCorrect, assignTakenID) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, result.getAssignmentQuesID());
            ps.setInt(2, result.getLearnerID());
            ps.setFloat(3, result.getMark());
            ps.setString(4, result.getAnswerLabel());
            ps.setBoolean(5, result.isAnswerIsCorrect());
            ps.setInt(6, result.getAssignTakenID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 