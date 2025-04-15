//package dao;
//
//import model.Answer;
//import model.Assignment;
//import model.Question;
//import util.DBConnect;
//
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AssignmentDAO {
//    private Connection connection;
//
//    public AssignmentDAO() {
//        this.connection = DBConnect.getInstance().getConnection();
//    }
//
//    public int createEmptyAssignment(String title, String description) throws SQLException {
//        String query = "INSERT INTO Assignment(title, description, createAt) VALUES (?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, title);
//            stmt.setString(2, description);
//            stmt.setDate(3, Date.valueOf(LocalDate.now())); // Ngày hiện tại
//
//            stmt.executeUpdate();
//
//            // Lấy ID vừa tạo
//            try (ResultSet rs = stmt.getGeneratedKeys()) {
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//            }
//        }
//        throw new SQLException("Không thể tạo assignment mới");
//    }
//    public void addQuestionsToAssignment(int assignmentID, Assignment assignment, List<Question> questions, List<List<Answer>> answersForQuestions) {
//        String sqlQuestion = "INSERT INTO Question (questionText, questionImg, audio_file, questionType, questionMark) VALUES (?, ?, ?, ?, ?)";
//        String sqlAnswer = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
//        String sqlAssignmentQuestion = "INSERT INTO Assignment_Question (assignmentID, questionID, answerID) VALUES (?, ?, ?)";
//
//        PreparedStatement pstmtQuestion = null;
//        PreparedStatement pstmtAnswer = null;
//        PreparedStatement pstmtAssignmentQuestion = null;
//        ResultSet generatedKeys = null;
//
//        try {
//            // Bắt đầu transaction
//            connection.setAutoCommit(false);
//
//            pstmtQuestion = connection.prepareStatement(sqlQuestion, PreparedStatement.RETURN_GENERATED_KEYS);
//            pstmtAnswer = connection.prepareStatement(sqlAnswer, PreparedStatement.RETURN_GENERATED_KEYS);
//            pstmtAssignmentQuestion = connection.prepareStatement(sqlAssignmentQuestion);
//
//            // Duyệt qua từng câu hỏi và câu trả lời tương ứng
//            for (int i = 0; i < questions.size(); i++) {
//                Question question = questions.get(i);
//                List<Answer> answers = answersForQuestions.get(i);
//
//                // Lưu Question
//                pstmtQuestion.setString(1, question.getQuestionText());
//                pstmtQuestion.setString(2, question.getQuestionImage() != null ? question.getQuestionImage() : null);
//                pstmtQuestion.setString(3, question.getAudioFile() != null ? question.getAudioFile() : null);
//                pstmtQuestion.setString(4, question.getQuestionType());
//                pstmtQuestion.setDouble(5, question.getQuestionMark());
//                pstmtQuestion.executeUpdate();
//
//                // Lấy questionID vừa được tạo
//                int questionID;
//                generatedKeys = pstmtQuestion.getGeneratedKeys();
//                if (generatedKeys.next()) {
//                    questionID = generatedKeys.getInt(1);
//                } else {
//                    throw new SQLException("Failed to get questionID, no ID obtained.");
//                }
//
//                // Lưu Answers
//                for (Answer answer : answers) {
//                    pstmtAnswer.setString(1, answer.getAnswerText());
//                    pstmtAnswer.setBoolean(2, answer.isCorrect());
//                    pstmtAnswer.setString(3, String.valueOf(answer.getOptionLabel()));
//                    pstmtAnswer.setInt(4, questionID);
//                    pstmtAnswer.executeUpdate();
//
//                    // Lấy answerID vừa được tạo
//                    int answerID;
//                    generatedKeys = pstmtAnswer.getGeneratedKeys();
//                    if (generatedKeys.next()) {
//                        answerID = generatedKeys.getInt(1);
//                    } else {
//                        throw new SQLException("Failed to get answerID, no ID obtained.");
//                    }
//
//                    // Lưu vào bảng Assignment_Question
//                    pstmtAssignmentQuestion.setInt(1, assignmentID);
//                    pstmtAssignmentQuestion.setInt(2, questionID);
//                    pstmtAssignmentQuestion.setInt(3, answerID);
//                    pstmtAssignmentQuestion.executeUpdate();
//                }
//            }
//
//            // Commit transaction
//            connection.commit();
//        } catch (SQLException e) {
//            try {
//                // Rollback nếu có lỗi
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        } finally {
//            try {
//                if (generatedKeys != null) {
//                    generatedKeys.close();
//                }
//                if (pstmtQuestion != null) {
//                    pstmtQuestion.close();
//                }
//                if (pstmtAnswer != null) {
//                    pstmtAnswer.close();
//                }
//                if (pstmtAssignmentQuestion != null) {
//                    pstmtAssignmentQuestion.close();
//                }
//                connection.setAutoCommit(true); // Đặt lại auto-commit
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public List<Assignment> getAllAssignments() {
//        List<Assignment> assignments = new ArrayList<>();
//        String query = "SELECT * FROM Assignment";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//             ResultSet rs = preparedStatement.executeQuery()) {
//
//            while (rs.next()) {
//                Assignment assignment = new Assignment();
//                assignment.setAssignmentID(rs.getInt("assignmentID"));
//                assignment.setAssignmentTitle(rs.getString("title"));
//                assignments.add(assignment);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return assignments;
//    }
//
//    // Trong AssignmentDAO.java
//    public void updateAssignment(Assignment assignment) throws SQLException {
//        String sql = "UPDATE Assignment SET title = ?, description = ? WHERE assignmentID = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, assignment.getAssignmentTitle());
//            stmt.setString(2, assignment.getDescription());
//            stmt.setInt(3, assignment.getAssignmentID());
//            stmt.executeUpdate();
//        }
//    }
//
//
//    public Assignment getAssignmentById(int assignmentID) throws SQLException {
//        System.out.println("Đang tìm assignment với ID: " + assignmentID);
//        String query = "SELECT * FROM Assignment WHERE assignmentID = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setInt(1, assignmentID);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    Assignment assignment = new Assignment();
//                    assignment.setAssignmentID(rs.getInt("assignmentID"));
//                    assignment.setAssignmentTitle(rs.getString("title"));
//                    assignment.setDescription(rs.getString("description"));
//                    System.out.println("Đã tìm thấy assignment: " + assignment.getAssignmentTitle());
//                    return assignment;
//                }
//            }
//        }
//        System.out.println("Không tìm thấy assignment với ID: " + assignmentID);
//        return null; // Trả về null nếu không tìm thấy
//    }
//
//    public void deleteAllQuestions(int assignmentID) throws SQLException {
//        String deleteQuery = "DELETE FROM Assignment_Question WHERE assignmentID = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
//            stmt.setInt(1, assignmentID);
//            stmt.executeUpdate();
//        }
//    }
//
//    public List<Question> getQuestionsByAssignmentId(int assignmentID) throws SQLException {
//        System.out.println("=== Bắt đầu lấy câu hỏi cho assignment ID: " + assignmentID + " ===");
//        List<Question> questions = new ArrayList<>();
//        String query = "SELECT DISTINCT q.*, a.answerID, a.answerText, a.isCorrect, a.option_label " +
//                      "FROM Question q " +
//                      "JOIN Assignment_Question aq ON q.questionID = aq.questionID " +
//                      "JOIN Answer a ON aq.answerID = a.answerID " +
//                      "WHERE aq.assignmentID = ? " +
//                      "ORDER BY q.questionID, a.answerID";
//
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setInt(1, assignmentID);
//            try (ResultSet rs = stmt.executeQuery()) {
//                Question currentQuestion = null;
//                while (rs.next()) {
//                    int questionID = rs.getInt("questionID");
//
//                    // Nếu là câu hỏi mới
//                    if (currentQuestion == null || currentQuestion.getQuestionID() != questionID) {
//                        currentQuestion = new Question();
//                        currentQuestion.setQuestionID(questionID);
//                        currentQuestion.setQuestionText(rs.getString("questionText"));
//                        currentQuestion.setQuestionImage(rs.getString("questionImg"));
//                        currentQuestion.setAudioFile(rs.getString("audio_file"));
//                        currentQuestion.setQuestionType(rs.getString("questionType"));
//                        currentQuestion.setQuestionMark(rs.getDouble("questionMark"));
//                        currentQuestion.setCorrectAnswer(rs.getString("correctAnswer"));
//                        currentQuestion.setAssignmentID(assignmentID);
//                        currentQuestion.setAnswers(new ArrayList<>());
//                        questions.add(currentQuestion);
//                        System.out.println("Đã tìm thấy câu hỏi: " + currentQuestion.getQuestionText());
//                    }
//
//                    // Thêm câu trả lời vào câu hỏi hiện tại
//                    Answer answer = new Answer();
//                    answer.setAnswerID(rs.getInt("answerID"));
//                    answer.setAnswerText(rs.getString("answerText"));
//                    answer.setCorrect(rs.getBoolean("isCorrect"));
//                    answer.setOptionLabel(rs.getString("option_label").charAt(0));
//                    answer.setQuestionID(questionID);
//                    currentQuestion.getAnswers().add(answer);
//                    System.out.println("Đã thêm câu trả lời: " + answer.getAnswerText() +
//                                     " (Đúng: " + answer.isCorrect() + ")");
//                }
//            }
//        }
//
//        System.out.println("Tổng số câu hỏi tìm thấy: " + questions.size());
//        return questions;
//    }
//
//    // Thay đổi method updateQuestionsForAssignment trong AssignmentDAO.java
////    public void updateQuestionsForAssignment(int assignmentID, List<Question> questions) throws SQLException {
////        System.out.println("=== Bắt đầu cập nhật câu hỏi cho assignment ID: " + assignmentID + " ===");
////
////        // Kiểm tra dữ liệu đầu vào
////        if (questions == null) {
////            throw new SQLException("Danh sách câu hỏi không hợp lệ");
////        }
////
////        // Bắt đầu transaction
////        connection.setAutoCommit(false);
////
////        try {
////            // Xóa tất cả câu hỏi cũ
////            deleteAllQuestions(assignmentID);
////
////            // Thêm các câu hỏi mới
////            for (Question question : questions) {
////                addQuestionToAssignment(assignmentID, question);
////            }
////
////            // Commit transaction
////            connection.commit();
////            System.out.println("Đã cập nhật thành công " + questions.size() + " câu hỏi");
////
////        } catch (SQLException e) {
////            // Rollback nếu có lỗi
////            connection.rollback();
////            throw e;
////        } finally {
////            connection.setAutoCommit(true);
////        }
////    }
//
//    public void addQuestionToAssignment(int assignmentID, Question question, List<Answer> answers) throws SQLException {
//        String sqlQuestion = "INSERT INTO Question (questionText, questionImg, audio_file, questionType, questionMark, correctAnswer) VALUES (?, ?, ?, ?, ?, ?)";
//        String sqlAnswer = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
//        String sqlAssignmentQuestion = "INSERT INTO Assignment_Question (assignmentID, questionID, answerID) VALUES (?, ?, ?)";
//
//        try {
//            // Bắt đầu transaction
//            connection.setAutoCommit(false);
//
//            // Lưu Question
//            int questionID;
//            try (PreparedStatement pstmt = connection.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS)) {
//                pstmt.setString(1, question.getQuestionText());
//                pstmt.setString(2, question.getQuestionImage());
//                pstmt.setString(3, question.getAudioFile());
//                pstmt.setString(4, question.getQuestionType());
//                pstmt.setDouble(5, question.getQuestionMark());
//                pstmt.setString(6, question.getCorrectAnswer());
//                pstmt.executeUpdate();
//
//                try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                    if (rs.next()) {
//                        questionID = rs.getInt(1);
//                    } else {
//                        throw new SQLException("Không thể lấy ID của câu hỏi mới");
//                    }
//                }
//            }
//
//            // Lưu Answers và tạo liên kết với Assignment
//            for (Answer answer : answers) {
//                int answerID;
//                try (PreparedStatement pstmt = connection.prepareStatement(sqlAnswer, Statement.RETURN_GENERATED_KEYS)) {
//                    pstmt.setString(1, answer.getAnswerText());
//                    pstmt.setBoolean(2, answer.isCorrect());
//                    pstmt.setString(3, String.valueOf(answer.getOptionLabel()));
//                    pstmt.setInt(4, questionID);
//                    pstmt.executeUpdate();
//
//                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                        if (rs.next()) {
//                            answerID = rs.getInt(1);
//                        } else {
//                            throw new SQLException("Không thể lấy ID của câu trả lời mới");
//                        }
//                    }
//                }
//
//                // Lưu vào bảng Assignment_Question
//                try (PreparedStatement pstmt = connection.prepareStatement(sqlAssignmentQuestion)) {
//                    pstmt.setInt(1, assignmentID);
//                    pstmt.setInt(2, questionID);
//                    pstmt.setInt(3, answerID);
//                    pstmt.executeUpdate();
//                }
//            }
//
//            // Commit transaction
//            connection.commit();
//        } catch (SQLException e) {
//            connection.rollback();
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    public void updateQuestion(Question question, List<Answer> answers) throws SQLException {
//        String updateQuestionSQL = "UPDATE Question SET questionText = ?, questionImg = ?, audio_file = ?, questionType = ?, questionMark = ?, correctAnswer = ? WHERE questionID = ?";
//        String deleteAnswersSQL = "DELETE FROM Answer WHERE questionID = ?";
//        String insertAnswerSQL = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
//        String updateAssignmentQuestionSQL = "UPDATE Assignment_Question SET answerID = ? WHERE questionID = ? AND answerID = ?";
//
//        try {
//            // Bắt đầu transaction
//            connection.setAutoCommit(false);
//
//            // Cập nhật thông tin câu hỏi
//            try (PreparedStatement pstmt = connection.prepareStatement(updateQuestionSQL)) {
//                pstmt.setString(1, question.getQuestionText());
//                pstmt.setString(2, question.getQuestionImage());
//                pstmt.setString(3, question.getAudioFile());
//                pstmt.setString(4, question.getQuestionType());
//                pstmt.setDouble(5, question.getQuestionMark());
//                pstmt.setString(6, question.getCorrectAnswer());
//                pstmt.setInt(7, question.getQuestionID());
//                pstmt.executeUpdate();
//            }
//
//            // Xóa các câu trả lời cũ
//            try (PreparedStatement pstmt = connection.prepareStatement(deleteAnswersSQL)) {
//                pstmt.setInt(1, question.getQuestionID());
//                pstmt.executeUpdate();
//            }
//
//            // Thêm các câu trả lời mới và cập nhật liên kết
//            for (Answer answer : answers) {
//                int answerID;
//                try (PreparedStatement pstmt = connection.prepareStatement(insertAnswerSQL, Statement.RETURN_GENERATED_KEYS)) {
//                    pstmt.setString(1, answer.getAnswerText());
//                    pstmt.setBoolean(2, answer.isCorrect());
//                    pstmt.setString(3, String.valueOf(answer.getOptionLabel()));
//                    pstmt.setInt(4, question.getQuestionID());
//                    pstmt.executeUpdate();
//
//                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                        if (rs.next()) {
//                            answerID = rs.getInt(1);
//                        } else {
//                            throw new SQLException("Không thể lấy ID của câu trả lời mới");
//                        }
//                    }
//                }
//
//                // Cập nhật liên kết trong bảng Assignment_Question
//                try (PreparedStatement pstmt = connection.prepareStatement(updateAssignmentQuestionSQL)) {
//                    pstmt.setInt(1, answerID);
//                    pstmt.setInt(2, question.getQuestionID());
//                    pstmt.setInt(3, answer.getAnswerID());
//                    pstmt.executeUpdate();
//                }
//            }
//
//            // Commit transaction
//            connection.commit();
//        } catch (SQLException e) {
//            connection.rollback();
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    public void deleteQuestion(int questionID) throws SQLException {
//        String deleteAssignmentQuestionSQL = "DELETE FROM Assignment_Question WHERE questionID = ?";
//        String deleteAnswersSQL = "DELETE FROM Answer WHERE questionID = ?";
//        String deleteQuestionSQL = "DELETE FROM Question WHERE questionID = ?";
//
//        try {
//            // Bắt đầu transaction
//            connection.setAutoCommit(false);
//
//            // Xóa các bản ghi trong Assignment_Question
//            try (PreparedStatement pstmt = connection.prepareStatement(deleteAssignmentQuestionSQL)) {
//                pstmt.setInt(1, questionID);
//                pstmt.executeUpdate();
//            }
//
//            // Xóa các câu trả lời
//            try (PreparedStatement pstmt = connection.prepareStatement(deleteAnswersSQL)) {
//                pstmt.setInt(1, questionID);
//                pstmt.executeUpdate();
//            }
//
//            // Xóa câu hỏi
//            try (PreparedStatement pstmt = connection.prepareStatement(deleteQuestionSQL)) {
//                pstmt.setInt(1, questionID);
//                pstmt.executeUpdate();
//            }
//
//            // Commit transaction
//            connection.commit();
//        } catch (SQLException e) {
//            connection.rollback();
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    public void deleteAssignment(int assignmentID) throws SQLException {
//        // Bắt đầu transaction
//        connection.setAutoCommit(false);
//        try {
//            // 1. Xóa các bản ghi trong Assignment_Question
//            String deleteAssignmentQuestionQuery = "DELETE FROM Assignment_Question WHERE assignmentID = ?";
//            try (PreparedStatement stmt = connection.prepareStatement(deleteAssignmentQuestionQuery)) {
//                stmt.setInt(1, assignmentID);
//                stmt.executeUpdate();
//            }
//
//            // 2. Xóa các câu hỏi liên quan
//            String deleteQuestionsQuery = "DELETE FROM Question WHERE questionID IN " +
//                    "(SELECT questionID FROM Assignment_Question WHERE assignmentID = ?)";
//            try (PreparedStatement stmt = connection.prepareStatement(deleteQuestionsQuery)) {
//                stmt.setInt(1, assignmentID);
//                stmt.executeUpdate();
//            }
//
//            // 3. Xóa các câu trả lời liên quan
//            String deleteAnswersQuery = "DELETE FROM Answer WHERE questionID IN " +
//                    "(SELECT questionID FROM Assignment_Question WHERE assignmentID = ?)";
//            try (PreparedStatement stmt = connection.prepareStatement(deleteAnswersQuery)) {
//                stmt.setInt(1, assignmentID);
//                stmt.executeUpdate();
//            }
//
//            // 4. Xóa assignment
//            String deleteAssignmentQuery = "DELETE FROM Assignment WHERE assignmentID = ?";
//            try (PreparedStatement stmt = connection.prepareStatement(deleteAssignmentQuery)) {
//                stmt.setInt(1, assignmentID);
//                stmt.executeUpdate();
//            }
//
//            // Commit transaction
//            connection.commit();
//        } catch (SQLException e) {
//            // Rollback nếu có lỗi
//            connection.rollback();
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    public void closeConnection() throws SQLException {
//        if (connection != null && !connection.isClosed()) {
//            connection.close();
//        }
//    }
//}
