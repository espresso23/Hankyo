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
        String query = "INSERT INTO Exam_Taken (learnerID, examID, dateCreated) VALUES (?, ?, GETDATE())";
        int examTakenID = -1;

        try (Connection connection1 = DBConnect.getInstance().getConnection();
                PreparedStatement stmt = connection1.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, learnerID);
            stmt.setInt(2, examID);

            System.out.println("=== Creating new exam taken ===");
            System.out.println("learnerID: " + learnerID);
            System.out.println("examID: " + examID);
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

    public void updateExamTaken(int examTakenID, float finalMark, Time timeTaken, int doneQues, int skipQues) throws SQLException {
        String query = "UPDATE Exam_Taken SET finalMark = ?, timeTaken = ?, doneQues = ?, skipQues = ? WHERE examTakenID = ?";

        System.out.println("\n=== Updating Exam Taken ===");
        System.out.println("ExamTakenID: " + examTakenID);
        System.out.println("Final Mark: " + finalMark);
        System.out.println("Time Taken: " + timeTaken);
        System.out.println("Done Questions: " + doneQues);
        System.out.println("Skipped Questions: " + skipQues);

        try (Connection connection1 = DBConnect.getInstance().getConnection();
                PreparedStatement stmt = connection1.prepareStatement(query)) {

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

            int examTakenID = examTakenDAO.createExamTaken(learnerID, examID);

            System.out.println("✅ examTakenID được tạo: " + examTakenID);
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi tạo examTaken: " + e.getMessage());
            e.printStackTrace();
        }
    }
}