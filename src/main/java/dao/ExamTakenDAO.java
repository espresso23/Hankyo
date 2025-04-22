package dao;

import model.ExamTaken;
import util.DBConnect;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExamTakenDAO {

    public ExamTakenDAO() {
    }

    public int createExamTaken(int learnerID, int examID, int timeInputInMinutes) throws SQLException {
        String query = "INSERT INTO Exam_Taken (learnerID, examID, dateCreated, timeInput, timeTaken, finalMark, skipQues, doneQues) " +
                "VALUES (?, ?, GETDATE(), ?, '00:00:00', 0, 0, 0)";
        int examTakenID = -1;

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, learnerID);
            stmt.setInt(2, examID);
            // Chuyển timeInput thành định dạng TIME (HH:mm:ss)
            String timeInputFormatted = String.format("%02d:00:00", timeInputInMinutes);
            stmt.setTime(3, Time.valueOf(timeInputFormatted));

            System.out.println("=== Creating new exam taken ===");
            System.out.println("learnerID: " + learnerID);
            System.out.println("examID: " + examID);
            System.out.println("timeInput: " + timeInputFormatted);
            System.out.println("Executing INSERT query with:");
            System.out.println("learnerID: " + learnerID);
            System.out.println("examID: " + examID);
            System.out.println("Query: " + query);
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

    public List<ExamTaken> getExamTakenByLearner(int learnerID) {
        List<ExamTaken> examTakens = new ArrayList<>();
        String query = "SELECT * FROM Exam_Taken et join Exam_Question eq on et.examID = eq.examID WHERE learnerID = ? ORDER BY dateCreated DESC";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExamTaken examTaken = new ExamTaken();
                examTaken.setExamTakenID(rs.getInt("examTakenID"));
                examTaken.setLearnerID(rs.getInt("learnerID"));
                examTaken.setExamID(rs.getInt("examID"));

                // Xử lý dateCreated
                Timestamp dateCreated = rs.getTimestamp("dateCreated");
                examTaken.setDateCreated(dateCreated != null ? dateCreated.toLocalDateTime() : null);

                Time timeTaken = rs.getTime("timeTaken");
                examTaken.setTimeTaken(timeTaken);

                Time timeInput = rs.getTime("timeInput");
                examTaken.setTimeInput(timeInput);

                float finalMark = rs.getFloat("finalMark");
                examTaken.setFinalMark(finalMark);

                int skipQues = rs.getInt("skipQues");
                examTaken.setSkipQues(skipQues);

                int doneQues = rs.getInt("doneQues");
                examTaken.setDoneQues(doneQues);
                examTaken.seteQuesType(rs.getString("eQuesType"));

                examTakens.add(examTaken);
            }
        } catch (SQLException e) {
            System.out.println("Error getting exam taken by learner: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve exam taken records: " + e.getMessage(), e);
        }
        return examTakens;
    }

    public ExamTaken getExamTakenById(int examTakenID) {
        String query = "SELECT * FROM Exam_Taken WHERE examTakenID = ?";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, examTakenID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ExamTaken examTaken = new ExamTaken();
                examTaken.setExamTakenID(rs.getInt("examTakenID"));
                examTaken.setLearnerID(rs.getInt("learnerID"));
                examTaken.setExamID(rs.getInt("examID"));

                // Xử lý dateCreated
                Timestamp dateCreated = rs.getTimestamp("dateCreated");
                examTaken.setDateCreated(dateCreated != null ? dateCreated.toLocalDateTime() : null);

                Time timeTaken = rs.getTime("timeTaken");
                examTaken.setTimeTaken(timeTaken);

                Time timeInput = rs.getTime("timeInput");
                examTaken.setTimeInput(timeInput);

                float finalMark = rs.getFloat("finalMark");
                examTaken.setFinalMark(finalMark);

                int skipQues = rs.getInt("skipQues");
                examTaken.setSkipQues(skipQues);

                int doneQues = rs.getInt("doneQues");
                examTaken.setDoneQues(doneQues);

                return examTaken;
            }
        } catch (SQLException e) {
            System.out.println("Error getting exam taken by ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve exam taken by ID: " + e.getMessage(), e);
        }
        return null;
    }

    public void updateExamTaken(int examTakenID, float finalMark, Time timeTaken, int doneQues, int skipQues) throws SQLException {
        String query = "UPDATE Exam_Taken SET finalMark = ?, timeTaken = ?, doneQues = ?, skipQues = ? WHERE examTakenID = ?";

        System.out.println("\n=== Updating Exam Taken ===");
        System.out.println("ExamTakenID: " + examTakenID);
        System.out.println("Final Mark: " + finalMark);
        System.out.println("Time Taken: " + timeTaken);
        System.out.println("Done Questions: " + doneQues);
        System.out.println("Skipped Questions: " + skipQues);

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setFloat(1, finalMark);
            stmt.setTime(2, timeTaken);
            stmt.setInt(3, doneQues);
            stmt.setInt(4, skipQues);
            stmt.setInt(5, examTakenID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No exam taken record found with ID: " + examTakenID);
            }

            System.out.println("Successfully updated " + rowsAffected + " row(s)");
        } catch (SQLException e) {
            System.out.println("Error updating exam taken: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            ExamTakenDAO examTakenDAO = new ExamTakenDAO();

            int learnerID = 1; // thay bằng ID thật trong database của bạn
            int examID = 3;    // thay bằng ID đề thi thật trong database
            int timeInputInMinutes = 60; // Giá trị mặc định 60 phút

            int examTakenID = examTakenDAO.createExamTaken(learnerID, examID, timeInputInMinutes);

            System.out.println("✅ examTakenID được tạo: " + examTakenID);
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi tạo examTaken: " + e.getMessage());
            e.printStackTrace();
        }
    }
}