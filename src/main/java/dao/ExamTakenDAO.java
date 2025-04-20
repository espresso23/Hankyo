package dao;

import model.ExamTaken;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamTakenDAO {
    private Connection connection;

    public ExamTakenDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public int createExamTaken(int learnerID, int examID) throws SQLException {
        String query = "INSERT INTO Exam_Taken (learnerID, examID, dateCreated) VALUES (?, ?, ?)";
        int examTakenID = -1;
        
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, learnerID);
            stmt.setInt(2, examID);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            
            System.out.println("=== Creating new exam taken ===");
            System.out.println("learnerID: " + learnerID);
            System.out.println("examID: " + examID);
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                examTakenID = rs.getInt(1);
                System.out.println("Created examTakenID: " + examTakenID);
            }
        } catch (SQLException e) {
            System.out.println("Error creating exam taken: " + e.getMessage());
            throw e;
        }
        return examTakenID;
    }

    public void updateExamTaken(int examTakenID, Time timeTaken, Time timeInput, Float finalMark, Integer skipQues, Integer doneQues) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE Exam_Taken SET ");
        List<Object> params = new ArrayList<>();
        
        if (timeTaken != null) {
            query.append("timeTaken = ?, ");
            params.add(timeTaken);
        }
        if (timeInput != null) {
            query.append("timeInput = ?, ");
            params.add(timeInput);
        }
        if (finalMark != null) {
            query.append("finalMark = ?, ");
            params.add(finalMark);
        }
        if (skipQues != null) {
            query.append("skipQues = ?, ");
            params.add(skipQues);
        }
        if (doneQues != null) {
            query.append("doneQues = ?, ");
            params.add(doneQues);
        }
        
        // Remove trailing comma and space
        if (params.isEmpty()) {
            return; // Nothing to update
        }
        query.setLength(query.length() - 2);
        query.append(" WHERE examTakenID = ?");
        params.add(examTakenID);
        
        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Time) {
                    stmt.setTime(i + 1, (Time) param);
                } else if (param instanceof Float) {
                    stmt.setFloat(i + 1, (Float) param);
                } else if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                }
            }
            
            System.out.println("=== Updating exam taken ===");
            System.out.println("examTakenID: " + examTakenID);
            System.out.println("timeTaken: " + timeTaken);
            System.out.println("timeInput: " + timeInput);
            System.out.println("finalMark: " + finalMark);
            System.out.println("skipQues: " + skipQues);
            System.out.println("doneQues: " + doneQues);
            System.out.println("SQL Query: " + query.toString());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("Error updating exam taken: " + e.getMessage());
            throw e;
        }
    }

    public List<ExamTaken> getExamTakenByLearner(int learnerID) {
        List<ExamTaken> examTakens = new ArrayList<>();
        String query = "SELECT * FROM Exam_Taken WHERE learnerID = ? ORDER BY dateCreated DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ExamTaken examTaken = new ExamTaken();
                examTaken.setExamTakenID(rs.getInt("examTakenID"));
                examTaken.setLearnerID(rs.getInt("learnerID"));
                examTaken.setExamID(rs.getInt("examID"));
                examTaken.setDateCreated(rs.getTimestamp("dateCreated"));
                
                Time timeTaken = rs.getTime("timeTaken");
                if (!rs.wasNull()) {
                    examTaken.setTimeTaken(timeTaken);
                }
                
                Time timeInput = rs.getTime("timeInput");
                if (!rs.wasNull()) {
                    examTaken.setTimeInput(timeInput);
                }
                
                float finalMark = rs.getFloat("finalMark");
                if (!rs.wasNull()) {
                    examTaken.setFinalMark(finalMark);
                }
                
                int skipQues = rs.getInt("skipQues");
                if (!rs.wasNull()) {
                    examTaken.setSkipQues(skipQues);
                }
                
                int doneQues = rs.getInt("doneQues");
                if (!rs.wasNull()) {
                    examTaken.setDoneQues(doneQues);
                }
                
                examTakens.add(examTaken);
            }
        } catch (SQLException e) {
            System.out.println("Error getting exam taken by learner: " + e.getMessage());
            e.printStackTrace();
        }
        return examTakens;
    }

    public ExamTaken getExamTakenById(int examTakenID) {
        String query = "SELECT * FROM Exam_Taken WHERE examTakenID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, examTakenID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ExamTaken examTaken = new ExamTaken();
                examTaken.setExamTakenID(rs.getInt("examTakenID"));
                examTaken.setLearnerID(rs.getInt("learnerID"));
                examTaken.setExamID(rs.getInt("examID"));
                examTaken.setDateCreated(rs.getTimestamp("dateCreated"));
                examTaken.setTimeTaken(rs.getTime("timeTaken"));
                examTaken.setTimeInput(rs.getTime("timeInput"));
                examTaken.setFinalMark(rs.getFloat("finalMark"));
                examTaken.setSkipQues(rs.getInt("skipQues"));
                examTaken.setDoneQues(rs.getInt("doneQues"));
                return examTaken;
            }
        } catch (SQLException e) {
            System.out.println("Error getting exam taken by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updateExamTaken(int examTakenID, float finalMark, Time timeTaken, int doneQues, int skipQues) {
    }
    public static void main(String[] args) {
        try {
            ExamTakenDAO examTakenDAO = new ExamTakenDAO();

            int learnerID = 1; // thay bằng ID thật trong database của bạn
            int examID = 3;    // thay bằng ID đề thi thật trong database

            int examTakenID = examTakenDAO.createExamTaken(learnerID, examID);

            System.out.println("✅ examTakenID được tạo: " + examTakenID);
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi tạo examTaken: " + e.getMessage());
            e.printStackTrace();
        }
    }

}