package dao;

import model.Answer;
import model.Exam;
import model.Question;
import model.ExamTaken;
import util.DBConnect;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ExamDAO {
    private Connection connection;

    public ExamDAO(Connection connection) {
        this.connection = DBConnect.getInstance().getConnection();
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


    public Exam getExamById(int examID) {
        String query = "SELECT * FROM Exam WHERE examID = ?";
        try (Connection connection1 = DBConnect.getInstance().getConnection();
                PreparedStatement stmt = connection1.prepareStatement(query)) {
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

        try (Connection connection1 = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection1.prepareStatement(query)) {
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
        System.out.println("\n=== DEBUG: Getting questions for exam ID: " + examID + " ===");
        List<Question> questions = new ArrayList<>();

        // Tạo connection ở ngoài try-with-resources để có thể sử dụng cho cả việc lấy answers
        Connection connection = null;
        try {
            connection = DBConnect.getInstance().getConnection();

            // Query lấy questions
            String query = "SELECT * FROM Exam_Question eq " +
                    "JOIN Question q ON q.questionID = eq.questionID " +
                    "WHERE examID = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, examID);
                System.out.println("Executing query: " + query);
                ResultSet rs = stmt.executeQuery();

                Set<Integer> questionIds = new HashSet<>();
                int count = 0;

                while (rs.next()) {
                    int questionId = rs.getInt("questionID");
                    System.out.println("\nProcessing question ID: " + questionId);

                    if (!questionIds.contains(questionId)) {
                        Question question = new Question();
                        question.setQuestionID(questionId);
                        question.setQuestionText(rs.getString("questionText"));
                        question.setQuestionType(rs.getString("questionType"));
                        question.setQuestionImage(rs.getString("questionImg"));
                        question.setAudioFile(rs.getString("audio_file"));
                        question.setQuestionMark(rs.getDouble("mark"));

                        System.out.println("Question details:");
                        System.out.println("- Text: " + question.getQuestionText());
                        System.out.println("- Type: " + question.getQuestionType());
                        System.out.println("- Image: " + question.getQuestionImage());
                        System.out.println("- Audio: " + question.getAudioFile());
                        System.out.println("- Mark: " + question.getQuestionMark());

                        questions.add(question);
                        questionIds.add(questionId);
                        count++;
                    }
                }
                System.out.println("\nTotal unique questions found: " + count);

                // Load answers cho từng câu hỏi sử dụng cùng connection
                System.out.println("\nLoading answers for questions...");
                for (Question q : questions) {
                    List<Answer> answers = getAnswersByQuestionId(q.getQuestionID(), connection);
                    q.setAnswers(answers);
                    System.out.println("Question ID " + q.getQuestionID() + " has " + answers.size() + " answers");
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR in getQuestionsByExamId: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Đóng connection trong finally block
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return questions;
    }

    // Sửa method getAnswersByQuestionId để nhận connection từ method gọi
    private List<Answer> getAnswersByQuestionId(int questionID, Connection connection) throws SQLException {
        System.out.println("\n=== DEBUG: Getting answers for question ID: " + questionID + " ===");
        List<Answer> answers = new ArrayList<>();
        String query = "SELECT answerID, questionID, answerText, isCorrect, option_label " +
                "FROM Answer WHERE questionID = ? ORDER BY option_label";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionID);
            System.out.println("Executing query: " + query);
            System.out.println("Parameter: questionID = " + questionID);

            ResultSet rs = stmt.executeQuery();
            int count = 0;

            while (rs.next()) {
                count++;
                Answer answer = new Answer();
                answer.setAnswerID(rs.getInt("answerID"));
                answer.setQuestionID(rs.getInt("questionID"));
                answer.setAnswerText(rs.getString("answerText"));
                answer.setCorrect(rs.getBoolean("isCorrect"));
                answer.setOptionLabel(rs.getString("option_label"));

                System.out.println("\nAnswer " + count + " details:");
                System.out.println("- ID: " + answer.getAnswerID());
                System.out.println("- Question ID: " + answer.getQuestionID());
                System.out.println("- Text: " + answer.getAnswerText());
                System.out.println("- Correct: " + answer.isCorrect());
                System.out.println("- Option Label: " + answer.getOptionLabel());

                answers.add(answer);
            }
            System.out.println("\nTotal answers found for question " + questionID + ": " + count);
        }
        return answers;
    }

    public List<Answer> getCorrectAnswer(int questionID) {
        List<Answer> correctAnswers = new ArrayList<>();
        String query = "SELECT a.* FROM Question q " +
                "JOIN Answer a ON q.questionID = a.questionID " +
                "WHERE q.questionID = ? AND a.isCorrect = 1";

         try (Connection connection1 = DBConnect.getInstance().getConnection();
                 PreparedStatement stmt = connection1.prepareStatement(query)) {
            stmt.setInt(1, questionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Answer answer = new Answer();
                answer.setAnswerID(rs.getInt("answerID"));
                answer.setAnswerText(rs.getString("answerText"));
                answer.setCorrect(true); // vì WHERE đã lọc isCorrect = 1
                answer.setOptionLabel(rs.getString("option_label"));
                answer.setQuestionID(rs.getInt("questionID"));
                correctAnswers.add(answer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return correctAnswers;
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

        try (Connection connection1 = DBConnect.getInstance().getConnection();
                PreparedStatement stmt = connection1.prepareStatement(query)) {
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

    public List<Question> getQuestionsByExamIdAndType(int examID, String eQuesType) {
        System.out.println("\n=== DEBUG: Getting questions for exam ID: " + examID + " and type: " + eQuesType + " ===");
        List<Question> questions = new ArrayList<>();
        String query;
        
        if ("Full".equals(eQuesType)) {
            // Nếu là Full thì lấy tất cả câu hỏi của exam
            query = "SELECT DISTINCT q.* FROM Question q " +
                    "JOIN Exam_Question eq ON q.questionID = eq.questionID " +
                    "WHERE eq.examID = ?";
        } else {
            // Nếu là Listening hoặc Reading thì lọc theo eQuesType
            query = "SELECT DISTINCT q.* FROM Question q " +
                    "JOIN Exam_Question eq ON q.questionID = eq.questionID " +
                    "WHERE eq.examID = ? AND eq.eQuesType = ?";
        }

        try (Connection connection1 = DBConnect.getInstance().getConnection();
                PreparedStatement stmt = connection1.prepareStatement(query)) {
            stmt.setInt(1, examID);
            if (!"Full".equals(eQuesType)) {
                stmt.setString(2, eQuesType);
            }
            
            System.out.println("Executing query: " + query);
            System.out.println("Parameters: examID=" + examID + (!"Full".equals(eQuesType) ? ", eQuesType=" + eQuesType : ""));
            
            ResultSet rs = stmt.executeQuery();
            Set<Integer> questionIds = new HashSet<>();

            while (rs.next()) {
                int questionId = rs.getInt("questionID");
                System.out.println("\nProcessing question ID: " + questionId);

                if (!questionIds.contains(questionId)) {
                    Question question = new Question();
                    question.setQuestionID(questionId);
                    question.setQuestionText(rs.getString("questionText"));
                    question.setQuestionType(rs.getString("questionType"));
                    question.setQuestionImage(rs.getString("questionImg"));
                    question.setAudioFile(rs.getString("audio_file"));
                    question.setQuestionMark(rs.getDouble("mark"));
                    
                    System.out.println("Question details:");
                    System.out.println("- Text: " + question.getQuestionText());
                    System.out.println("- Type: " + question.getQuestionType());
                    System.out.println("- Image: " + question.getQuestionImage());
                    System.out.println("- Audio: " + question.getAudioFile());
                    System.out.println("- Mark: " + question.getQuestionMark());

                    // Load answers cho câu hỏi
                    List<Answer> answers = getAnswersByQuestionId(questionId, connection1);
                    if (answers != null && !answers.isEmpty()) {
                        question.setAnswers(answers);
                        System.out.println("- Number of answers: " + answers.size());
                        for (Answer answer : answers) {
                            System.out.println("  + Answer: " + answer.getOptionLabel() + " - " + answer.getAnswerText());
                        }
                    } else {
                        System.out.println("WARNING: No answers found for question " + questionId);
                    }

                    questions.add(question);
                    questionIds.add(questionId);
                }
            }
            System.out.println("\nTotal unique questions found: " + questions.size());
            for (Question q : questions) {
                System.out.println("Question " + q.getQuestionID() + " has " + 
                    (q.getAnswers() != null ? q.getAnswers().size() : "0") + " answers");
            }

        } catch (SQLException e) {
            System.out.println("ERROR in getQuestionsByExamIdAndType: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }


    public ExamTaken getExamTakenById(int examTakenID) {
        String sql = "SELECT * FROM Exam_Taken WHERE examTakenID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examTakenID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ExamTaken examTaken = new ExamTaken();
                examTaken.setExamTakenID(rs.getInt("examTakenID"));
                examTaken.setExamID(rs.getInt("examID"));
                examTaken.setLearnerID(rs.getInt("learnerID"));
                examTaken.setTimeTaken(rs.getTime("timeTaken"));
                examTaken.setFinalMark(rs.getFloat("finalMark"));
                examTaken.setDoneQues(rs.getInt("doneQues"));
                examTaken.setSkipQues(rs.getInt("skipQues"));
                return examTaken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
