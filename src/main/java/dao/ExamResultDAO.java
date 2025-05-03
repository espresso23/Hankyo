package dao;

import model.ExamQuestion;
import model.ExamResult;
import model.Learner;
import model.Question;
import util.DBConnect;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExamResultDAO {
    private Connection connection;

    public ExamResultDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public void saveExamResult(ExamResult result, int examTakenID) throws SQLException {
        String sqlResult = "INSERT INTO Exam_Result (examTakenID, eQuestID, mark, dateTaken, answerLabel, answerIsCorrect) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmtResult = null;
        ResultSet generatedKeys = null;

        try {
            connection.setAutoCommit(false);

            pstmtResult = connection.prepareStatement(sqlResult, Statement.RETURN_GENERATED_KEYS);
            pstmtResult.setInt(1, examTakenID);
            pstmtResult.setInt(2, result.geteQuesID()); // Lưu eQuestID thay vì questionID
            pstmtResult.setDouble(3, result.getMark());
            pstmtResult.setTimestamp(4, Timestamp.valueOf(result.getDateTaken()));
            pstmtResult.setString(5, result.getAnswerLabel());
            pstmtResult.setBoolean(6, result.isAnswerIsCorrect());
            pstmtResult.executeUpdate();

            generatedKeys = pstmtResult.getGeneratedKeys();
            if (generatedKeys.next()) {
                result.setResultID(generatedKeys.getInt(1));
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }
            if (pstmtResult != null) {
                pstmtResult.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static int getEQuestID(int examID, int questionID) throws SQLException {
        String query = "SELECT eQuestID FROM Exam_Question WHERE examID = ? AND questionID = ?";
        try (Connection connection1 = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection1.prepareStatement(query)) {
            stmt.setInt(1, examID);
            stmt.setInt(2, questionID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("eQuestID");
            }
            throw new SQLException("Không tìm thấy eQuestID cho examID=" + examID + ", questionID=" + questionID);
        }
    }

    public List<ExamResult> getExamResultsByLearner(int learnerID) {
        List<ExamResult> results = new ArrayList<>();
        String sql = "SELECT er.*, et.examID, et.timeTaken, et.timeInput, et.dateCreated, et.finalMark, et.skipQues, et.doneQues, " +
                "e.examName, e.description " +
                "FROM Exam_Result er " +
                "JOIN Exam_Taken et ON er.examTakenID = et.examTakenID " +
                "JOIN Exam e ON et.examID = e.examID " +
                "WHERE et.learnerID = ? " +
                "ORDER BY er.dateTaken DESC";

        try (Connection connection1 = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection1.prepareStatement(sql)) {
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultID(rs.getInt("resultID"));
                result.setExamTakenID(rs.getInt("examTakenID"));
                result.seteQuesID(rs.getInt("eQuestID"));
                result.setMark(rs.getFloat("mark"));
                Timestamp timestamp = rs.getTimestamp("dateTaken");
                LocalDateTime dateTaken = timestamp != null ? timestamp.toLocalDateTime() : null;
                result.setDateTaken(dateTaken);
                result.setAnswerLabel(rs.getString("answerLabel"));
                result.setAnswerIsCorrect(rs.getBoolean("answerIsCorrect"));

                model.Exam exam = new model.Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
                result.setExam(exam);

                model.Learner learner = new model.Learner();
                learner.setLearnerID(learnerID);
                result.setLearner(learner);

                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Lấy danh sách kết quả theo examTakenID
    public List<ExamResult> getExamResultsByExamTakenId(int examTakenID, String type) {
        List<ExamResult> results = new ArrayList<>();
        String sql;
        
        if (type == null || "Full".equals(type)) {
            sql = "SELECT distinct er.*, et.examID, et.learnerID, q.questionText \n" +
                "FROM Exam_Result er \n" +
                "JOIN Exam_Taken et ON er.examTakenID = et.examTakenID \n" +
                "JOIN Exam_Question eq ON er.eQuestID = eq.eQuestID \n" +
                "JOIN Question q ON eq.questionID = q.questionID \n" +
                "WHERE er.examTakenID = ?";
        } else {
            sql = "SELECT distinct er.*, et.examID, et.learnerID, q.questionText \n" +
                "FROM Exam_Result er \n" +
                "JOIN Exam_Taken et ON er.examTakenID = et.examTakenID \n" +
                "JOIN Exam_Question eq ON er.eQuestID = eq.eQuestID \n" +
                "JOIN Question q ON eq.questionID = q.questionID \n" +
                "WHERE er.examTakenID = ? and eq.eQuesType = ?";
        }

        try (Connection connection1 = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection1.prepareStatement(sql)) {
            stmt.setInt(1, examTakenID);
            if (type != null && !"Full".equals(type)) {
                stmt.setString(2, type);
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultID(rs.getInt("resultID"));
                result.setExamTakenID(rs.getInt("examTakenID"));
                result.seteQuesID(rs.getInt("eQuestID"));
                result.setMark(rs.getFloat("mark"));
                Timestamp timestamp = rs.getTimestamp("dateTaken");
                result.setDateTaken(timestamp != null ? timestamp.toLocalDateTime() : null);
                result.setAnswerLabel(rs.getString("answerLabel"));
                result.setAnswerIsCorrect(rs.getBoolean("answerIsCorrect"));

                model.Exam exam = new model.Exam();
                exam.setExamID(rs.getInt("examID"));
                result.setExam(exam);

                model.Learner learner = new model.Learner();
                learner.setLearnerID(rs.getInt("learnerID"));
                result.setLearner(learner);

                model.Question question = new model.Question();
                question.setQuestionText(rs.getString("questionText"));
                result.setQuestion(question);

                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void saveAnswer(int examTakenID, int questionID, int learnerID, String answerLabel, boolean isCorrect) {
        String sql = "INSERT INTO Exam_Result (eQuestID, learnerID, answerLabel, answerIsCorrect, examTakenID) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("\n=== ĐANG THỰC THI SQL ===");
            System.out.println("SQL: " + sql);
            System.out.println("Tham số:");
            System.out.println("1. eQuestID: " + questionID);
            System.out.println("2. learnerID: " + learnerID);
            System.out.println("3. answerLabel: " + answerLabel);
            System.out.println("4. answerIsCorrect: " + isCorrect);
            System.out.println("5. examTakenID: " + examTakenID);

            stmt.setInt(1, questionID);
            stmt.setInt(2, learnerID);
            stmt.setString(3, answerLabel);
            stmt.setBoolean(4, isCorrect);
            stmt.setInt(5, examTakenID);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Kết quả thực thi SQL: " + (rowsAffected > 0 ? "THÀNH CÔNG" : "THẤT BẠI"));
            System.out.println("Số hàng bị ảnh hưởng: " + rowsAffected);

        } catch (SQLException e) {
            System.out.println("\n=== LỖI SQL ===");
            System.out.println("Thông báo lỗi: " + e.getMessage());
            System.out.println("Trạng thái SQL: " + e.getSQLState());
            System.out.println("Mã lỗi: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    public void insertExamResult(ExamResult examResult) {
        String sql = "INSERT INTO Exam_Result (eQuestID, learnerID, dateTaken, mark, answerLabel, answerIsCorrect, examTakenID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examResult.geteQuesID());
            stmt.setInt(2, examResult.getLearner().getLearnerID());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(examResult.getDateTaken()));
            stmt.setFloat(4, examResult.getMark());
            stmt.setString(5, examResult.getAnswerLabel());
            stmt.setBoolean(6, examResult.isAnswerIsCorrect());
            stmt.setInt(7, examResult.getExamTakenID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Lỗi khi chèn kết quả bài thi: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể chèn kết quả bài thi", e);
        }
    }
}