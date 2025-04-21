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

    public int saveExamResult(int examId, int timeTaken, int timeInput, Timestamp dateCreated, 
                             int learnerId, double finalMark, int skipQues, int doneQues) {
        String sql = "INSERT INTO [Exam_Taken] (examID, timeTaken, timeInput, dateCreated, learnerID, finalMark, skipQues, doneQues) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?); SELECT SCOPE_IDENTITY() as examTakenID";
                    
        try (Connection conn = DBConnect.getInstance().getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, examId);
            stmt.setInt(2, timeTaken);
            stmt.setInt(3, timeInput);
            stmt.setTimestamp(4, dateCreated);
            stmt.setInt(5, learnerId);
            stmt.setDouble(6, finalMark);
            stmt.setInt(7, skipQues);
            stmt.setInt(8, doneQues);
            
            System.out.println("=== Saving exam result ===");
            System.out.println("examId: " + examId);
            System.out.println("timeTaken: " + timeTaken);
            System.out.println("timeInput: " + timeInput);
            System.out.println("dateCreated: " + dateCreated);
            System.out.println("learnerId: " + learnerId);
            System.out.println("finalMark: " + finalMark);
            System.out.println("skipQues: " + skipQues);
            System.out.println("doneQues: " + doneQues);
            
            stmt.executeUpdate();
            
            // Lấy examTakenID vừa được tạo
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int examTakenID = rs.getInt(1);
                System.out.println("Exam result saved successfully! ExamTakenID: " + examTakenID);
                return examTakenID;
            } else {
                System.out.println("Failed to get examTakenID!");
                throw new SQLException("Failed to get examTakenID");
            }
            
        } catch (SQLException e) {
            System.out.println("Error saving exam result: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving exam result", e);
        }
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
