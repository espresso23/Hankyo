package dao;

import model.Answer;
import model.Exam;
import model.Question;
import util.DBConnect;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamDAO {
    private Connection connection;

    public ExamDAO(Connection connection) {
        this.connection = DBConnect.getInstance().getConnection();
    }


    public int createEmptyExam(Exam exam) throws SQLException {
        String query = "INSERT INTO Exam(examName, description, dateCreate, expertID) values(?, ?,?,?)";
        int examID = -1;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, exam.getExamName());
            preparedStatement.setString(2, exam.getExamDescription());

            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();
            Date sqlDate = Date.valueOf(today);

//            preparedStatement.setDate(3, new java.sql.Date(exam.getDateCreated().getTime()));// Đặt giá trị ngày
            preparedStatement.setDate(3, sqlDate);// Đặt giá trị ngày
            preparedStatement.setInt(4, exam.getExpertID());
            preparedStatement.executeUpdate();

            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                examID = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        return examID;
    }

    public void addQuestionsToExam(int examID, Exam exam, List<Question> questions, List<List<Answer>> answersForQuestions) {
        String sqlQuestion = "INSERT INTO Question (questionText, questionImg, audio_file, questionType, mark) VALUES (?, ?, ?, ?,?)";
        String sqlAnswer = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
        String sqlExamQuestion = "INSERT INTO Exam_Question (examID, questionID, answerID, orderIndex,description) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstmtQuestion = null;
        PreparedStatement pstmtAnswer = null;
        PreparedStatement pstmtExamQuestion = null;
        ResultSet generatedKeys = null;

        try {
            // Start transaction
            connection.setAutoCommit(false);

            pstmtQuestion = connection.prepareStatement(sqlQuestion, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtAnswer = connection.prepareStatement(sqlAnswer, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtExamQuestion = connection.prepareStatement(sqlExamQuestion);

            // Iterate through each question and its corresponding answers
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                List<Answer> answers = answersForQuestions.get(i);

                // Save Question
                pstmtQuestion.setString(1, question.getQuestionText());
                pstmtQuestion.setString(2, question.getQuestionImage());
                pstmtQuestion.setString(3, question.getAudioFile());
                pstmtQuestion.setString(4, question.getQuestionType());
                pstmtQuestion.setDouble(5, question.getQuestionMark()); // Get from Exam
                pstmtQuestion.executeUpdate();

                // Get the newly created questionID
                int questionID;
                generatedKeys = pstmtQuestion.getGeneratedKeys();
                if (generatedKeys.next()) {
                    questionID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get questionID, no ID obtained.");
                }

                // Save Answers (4 answers for each question)
                for (Answer answer : answers) {
                    pstmtAnswer.setString(1, answer.getAnswerText());
                    pstmtAnswer.setBoolean(2, answer.isCorrect());
                    pstmtAnswer.setString(3, String.valueOf(answer.getOptionLabel()));
                    pstmtAnswer.setInt(4, questionID);
                    pstmtAnswer.executeUpdate();

                    // Get the newly created answerID
                    int answerID;
                    generatedKeys = pstmtAnswer.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        answerID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to get answerID, no ID obtained.");
                    }

                    // Save to Exam_Question table
                    pstmtExamQuestion.setInt(1, examID);
                    pstmtExamQuestion.setInt(2, questionID);
                    pstmtExamQuestion.setInt(3, answerID);
                    pstmtExamQuestion.setInt(4, i); // Get from Exam
                    pstmtExamQuestion.setString(5, exam.getExamDescription()); // Get from Exam
                    pstmtExamQuestion.executeUpdate();
                }
            }

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            try {
                // Rollback if there's an error
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pstmtQuestion != null) {
                    pstmtQuestion.close();
                }
                if (pstmtAnswer != null) {
                    pstmtAnswer.close();
                }
                if (pstmtExamQuestion != null) {
                    pstmtExamQuestion.close();
                }
                connection.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        String query = "SELECT * FROM Exam";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Exam exam = new Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
                exam.setDateCreated(rs.getDate("dateCreate"));
                exam.setExpertID(rs.getInt("expertID"));
                exam.setExamType(rs.getString("examType"));
                exams.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public void updateExam(Exam exam) {
        String query = "UPDATE Exam SET examName = ?, description = ? WHERE examID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, exam.getExamName());
            stmt.setString(2, exam.getExamDescription());
            stmt.setInt(3, exam.getExamID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Exam getExamById(int examID) {
        String query = "SELECT * FROM Exam WHERE examID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Exam exam = new Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
                exam.setDateCreated(rs.getDate("dateCreate"));
                exam.setExpertID(rs.getInt("expertID"));
                exam.setExamType(rs.getString("examType"));
                return exam;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Exam> getExamsByType(String examType) {
        List<Exam> exams = new ArrayList<>();
        String query = "SELECT * FROM Exam WHERE examType = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, examType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Exam exam = new Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
                exam.setDateCreated(rs.getDate("dateCreate"));
                exam.setExpertID(rs.getInt("expertID"));
                exam.setExamType(rs.getString("examType"));
                exams.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public List<Question> getQuestionsByExamId(int examID) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM Exam e " +
                "JOIN Exam_Question eq ON e.examID = eq.examID " +
                "JOIN Question q ON q.questionID = eq.questionID " +
                "JOIN Answer a ON a.answerID = eq.answerID " +
                "WHERE e.examID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();

            Map<Integer, Question> questionMap = new HashMap<>();

            while (rs.next()) {
                int questionId = rs.getInt("questionID");
                Question question;
                if (!questionMap.containsKey(questionId)) {
                    question = new Question();
                    question.setQuestionID(questionId);
                    question.setQuestionText(rs.getString("questionText"));
                    question.setQuestionType(rs.getString("questionType"));
                    //    question.setAnswers(new ArrayList<model.Answer>());
                    questionMap.put(questionId, question);
                    questions.add(question);
                } else {
                    question = questionMap.get(questionId);
                }

//                Answer answer = new Answer();
//                answer.setAnswerID(rs.getInt("answerID"));
//                answer.setAnswerText(rs.getString("answerText"));
//                answer.setIsCorrect(rs.getBoolean("isCorrect"));
//                answer.setAnswerLabel(rs.getString("answerLabel"));
//                answer.setQuestionID(questionId);
//
//                question.getAnswers().add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public int getTotalQuestionsByExamId(int examID) {
        String query = "SELECT COUNT(*) as total FROM Exam_Question WHERE examID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, examID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Exam> searchExamsByName(String searchName) {
        List<Exam> exams = new ArrayList<>();
        String query = "SELECT * FROM Exam WHERE examName LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + searchName + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Exam exam = new Exam();
                exam.setExamID(rs.getInt("examID"));
                exam.setExamName(rs.getString("examName"));
                exam.setExamDescription(rs.getString("description"));
            }
            return exams;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
