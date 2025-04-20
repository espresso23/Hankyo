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
        String sqlResult = "INSERT INTO Exam_Result (examTakenID, mark, dateTaken) VALUES (?, ?, ?)";
        String sqlDetail = "INSERT INTO Exam_Result_Detail (resultID, questionID, answerLabel, answerIsCorrect) VALUES (?, ?, ?, ?)";

        PreparedStatement pstmtResult = null;
        PreparedStatement pstmtDetail = null;
        ResultSet generatedKeys = null;

        try {
            connection.setAutoCommit(false);

            // Lưu thông tin kết quả chung
            pstmtResult = connection.prepareStatement(sqlResult, Statement.RETURN_GENERATED_KEYS);
            pstmtResult.setInt(1, examTakenID);
            pstmtResult.setDouble(2, result.getMark());
            pstmtResult.setTimestamp(3, Timestamp.valueOf(result.getDateTaken()));
            pstmtResult.executeUpdate();

            // Lấy resultID vừa được tạo
            generatedKeys = pstmtResult.getGeneratedKeys();
            if (generatedKeys.next()) {
                result.setResultID(generatedKeys.getInt(1));
            }

            // Lưu chi tiết từng câu trả lời
            pstmtDetail = connection.prepareStatement(sqlDetail);
            pstmtDetail.setInt(1, result.getResultID());
            pstmtDetail.setInt(2, result.getQuestion().getQuestionID());
            pstmtDetail.setString(3, result.getAnswerLabel());
            pstmtDetail.setBoolean(4, result.isAnswerIsCorrect());
            pstmtDetail.executeUpdate();

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
            if (pstmtDetail != null) {
                pstmtDetail.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
            }
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, learnerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultID(rs.getInt("resultID"));
                result.setExamTakenID(rs.getInt("examTakenID"));
                result.setMark(rs.getFloat("mark"));
                result.setDateTaken(rs.getTimestamp("dateTaken").toLocalDateTime());

                // Tạo đối tượng Exam
                model.Exam exam = new model.Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
                result.setExam(exam);

                // Tạo đối tượng Learner
                model.Learner learner = new model.Learner();
                learner.setLearnerID(learnerID);
                result.setLearner(learner);

                // Load chi tiết câu trả lời
                loadResultDetails(result);
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void loadResultDetails(ExamResult result) {
        String sql = "SELECT questionID, answerLabel, answerIsCorrect FROM Exam_Result_Detail WHERE resultID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, result.getResultID());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                model.ExamQuestion examQuestion = new model.ExamQuestion();
                model.Question question = new model.Question();
                question.setQuestionID(rs.getInt("questionID"));
                result.setExamQuestion(examQuestion);
                result.setAnswerLabel(rs.getString("answerLabel"));
                result.setAnswerIsCorrect(rs.getBoolean("answerIsCorrect"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ExamResult getExamResultById(int resultID) {
        String sql = "SELECT er.*, et.examID, et.timeTaken, et.timeInput, et.dateCreated, et.finalMark, et.skipQues, et.doneQues, " +
                "e.examName, e.description, et.learnerID " +
                "FROM Exam_Result er " +
                "JOIN Exam_Taken et ON er.examTakenID = et.examTakenID " +
                "JOIN Exam e ON et.examID = e.examID " +
                "WHERE er.resultID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, resultID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultID(rs.getInt("resultID"));
                result.setExamTakenID(rs.getInt("examTakenID"));
                result.setMark(rs.getFloat("mark"));
                result.setDateTaken(rs.getTimestamp("dateTaken").toLocalDateTime());

                // Tạo đối tượng Exam
                model.Exam exam = new model.Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
                result.setExam(exam);

                // Tạo đối tượng Learner
                model.Learner learner = new model.Learner();
                learner.setLearnerID(rs.getInt("learnerID"));
                result.setLearner(learner);

                // Load chi tiết câu trả lời
                loadResultDetails(result);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveAnswer(int examTakenID, int questionID, int learnerID, String answerLabel, boolean isCorrect) {
        String sql = "INSERT INTO ExamResult (eQuestID, learnerID, answerLabel, answerIsCorrect, examTakenID) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("\n=== EXECUTING SQL ===");
            System.out.println("SQL: " + sql);
            System.out.println("Parameters:");
            System.out.println("1. eQuestID: " + questionID);
            System.out.println("2. learnerID: " + learnerID);
            System.out.println("3. answerLabel: " + answerLabel);
            System.out.println("4. answerIsCorrect: " + isCorrect);
            System.out.println("5. examTakenID: " + examTakenID);

            stmt.setInt(1, questionID);      // eQuestID
            stmt.setInt(2, learnerID);       // learnerID
            stmt.setString(3, answerLabel);  // answerLabel
            stmt.setBoolean(4, isCorrect);   // answerIsCorrect
            stmt.setInt(5, examTakenID);     // examTakenID

            int rowsAffected = stmt.executeUpdate();
            System.out.println("SQL Execution result: " + (rowsAffected > 0 ? "SUCCESS" : "FAILED"));
            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.out.println("\n=== SQL ERROR ===");
            System.out.println("Error message: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }


    public void insertExamResult(ExamResult examResult) {
        String sql = "INSERT INTO Exam_Result (eQuestID, learnerID,  dateTaken,  mark, answerLabel, answerIsCorrect,examTakenID) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1, examResult.getExamQuestion().geteQuestID());
            stmt.setInt(2, examResult.getLearner().getLearnerID());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(examResult.getDateTaken()));
            stmt.setFloat(4, examResult.getMark());
            stmt.setString(5, examResult.getAnswerLabel());
            stmt.setBoolean(6, examResult.isAnswerIsCorrect());
            stmt.setInt(7, examResult.getExamTakenID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting exam result: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert exam result", e);
        }
    }


        public static void main(String[] args) {
            // Tạo đối tượng ExamQuestion
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.seteQuestID(8); // eQuestID tồn tại trong DB

            // Tạo đối tượng Learner
            Learner learner = new Learner();
            learner.setLearnerID(1); // learnerID tồn tại trong DB

            // Tạo đối tượng ExamResult
            ExamResult examResult = new ExamResult();
            examResult.setExamQuestion(examQuestion);
            examResult.setLearner(learner);
            examResult.setDateTaken(LocalDateTime.now());
            examResult.setMark(1.0f);
            examResult.setAnswerLabel("A");
            examResult.setAnswerIsCorrect(true);
            examResult.setExamTakenID(10); // ID của lượt thi, phải tồn tại trong DB

            // Gọi DAO để insert
            ExamResultDAO dao = new ExamResultDAO();
            dao.insertExamResult(examResult);

            System.out.println("Insert exam result thành công!");
        }
    }


