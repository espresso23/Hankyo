package dao;

import model.Answer;
import model.Assignment;
import model.Question;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {
    private Connection connection;

    public AssignmentDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public int createEmptyAssignment(String title, String description) throws SQLException {
        String query = "INSERT INTO Assignment(title, description, lastUpdated) VALUES (?, ?, GETDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, description);

            stmt.executeUpdate();

            // Lấy ID vừa tạo
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Không thể tạo assignment mới");
    }

    public void addQuestionsToAssignment(int assignmentID, int questionID) {
        String sqlAssignmentQuestion = "INSERT INTO Assignment_Question (assignmentID, questionID) VALUES (?, ?)";
        PreparedStatement pstmtAssignmentQuestion = null;

        try {
            Connection connection1 = DBConnect.getInstance().getConnection();
            // Kiểm tra xem liên kết đã tồn tại chưa
            String checkSql = "SELECT COUNT(*) FROM Assignment_Question WHERE assignmentID = ? AND questionID = ?";
            PreparedStatement checkStmt = connection1.prepareStatement(checkSql);
            checkStmt.setInt(1, assignmentID);
            checkStmt.setInt(2, questionID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Liên kết giữa Assignment " + assignmentID + " và Question " + questionID + " đã tồn tại");
                return;
            }

            // Thêm liên kết mới
            pstmtAssignmentQuestion = connection1.prepareStatement(sqlAssignmentQuestion);
            pstmtAssignmentQuestion.setInt(1, assignmentID);
            pstmtAssignmentQuestion.setInt(2, questionID);
            int result = pstmtAssignmentQuestion.executeUpdate();

            if (result > 0) {
                System.out.println("Đã thêm liên kết giữa Assignment " + assignmentID + " và Question " + questionID);
            } else {
                System.out.println("Không thể thêm liên kết giữa Assignment " + assignmentID + " và Question " + questionID);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm liên kết Assignment-Question: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmtAssignmentQuestion != null) {
                    pstmtAssignmentQuestion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Assignment> getAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM Assignment";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentID(rs.getInt("assignmentID"));
                assignment.setAssignmentTitle(rs.getString("title"));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    // Trong AssignmentDAO.java
    public void updateAssignment(Assignment assignment) throws SQLException {
        String sql = "UPDATE Assignment SET title = ?, description = ?, lastUpdated = GETDATE() WHERE assignmentID = ?";
        try (Connection connection1 = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection1.prepareStatement(sql)) {
            stmt.setString(1, assignment.getAssignmentTitle());
            stmt.setString(2, assignment.getDescription());
            stmt.setInt(3, assignment.getAssignmentID());
            stmt.executeUpdate();
        }
    }


    public Assignment getAssignmentById(int assignmentID) throws SQLException {
        System.out.println("Đang tìm assignment với ID: " + assignmentID);
        String query = "SELECT * FROM Assignment WHERE assignmentID = ?";
        try (Connection connection1 = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection1.prepareStatement(query)) {
            stmt.setInt(1, assignmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Assignment assignment = new Assignment();
                    assignment.setAssignmentID(rs.getInt("assignmentID"));
                    assignment.setAssignmentTitle(rs.getString("title"));
                    assignment.setDescription(rs.getString("description"));
                    System.out.println("Đã tìm thấy assignment: " + assignment.getAssignmentTitle());
                    return assignment;
                }
            }
        }
        System.out.println("Không tìm thấy assignment với ID: " + assignmentID);
        return null; // Trả về null nếu không tìm thấy
    }

    public void deleteAllQuestions(int assignmentID) throws SQLException {
        String deleteQuery = "DELETE FROM Assignment_Question WHERE assignmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, assignmentID);
            stmt.executeUpdate();
        }
    }


    public List<Question> getAllQuestionOfAssignment(int assignmentID) {
        List<Question> questionList = new ArrayList<>();
        String SQL_GET_QUESTION = "Select distinct q.* from Assignment_Question aq join question q on aq.questionID = q.questionID where assignmentID = ?";
        try {
            Connection connection1 = DBConnect.getInstance().getConnection();
            PreparedStatement preparedStatement = connection1.prepareStatement(SQL_GET_QUESTION);
            preparedStatement.setInt(1, assignmentID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Question questionOfAssignment = new Question();
                questionOfAssignment.setQuestionID(rs.getInt("questionID"));
                questionOfAssignment.setQuestionText(rs.getString("questionText"));
                questionOfAssignment.setQuestionImage(rs.getString("questionImg"));
                questionOfAssignment.setAudioFile(rs.getString("audio_file"));
                questionOfAssignment.setQuestionType(rs.getString("questionType"));
                questionOfAssignment.setQuestionMark(rs.getDouble("questionMark"));
                questionList.add(questionOfAssignment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return questionList;
    }

    public void updateQuestionForAssignment(Question question) throws SQLException {
        String sql = "UPDATE Question SET questionText = ?, questionImg = ?, audio_file = ?, questionType = ?, questionMark = ? WHERE questionID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getQuestionImage());
            ps.setString(3, question.getAudioFile());
            ps.setString(4, question.getQuestionType());
            ps.setDouble(5, question.getQuestionMark());
            ps.setInt(6, question.getQuestionID());
            ps.executeUpdate();
        }
    }

    public void deleteQuestion(int questionID) throws SQLException {
        String deleteAssignmentQuestionSQL = "DELETE FROM Assignment_Question WHERE questionID = ?";
        String deleteAnswersSQL = "DELETE FROM Answer WHERE questionID = ?";
        String deleteQuestionSQL = "DELETE FROM Question WHERE questionID = ?";
        Connection connection1 = DBConnect.getInstance().getConnection();
        try {
            // Bắt đầu transaction
            connection1.setAutoCommit(false);

            // Xóa các bản ghi trong Assignment_Question
            try (PreparedStatement pstmt = connection1.prepareStatement(deleteAssignmentQuestionSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            // Xóa các câu trả lời
            try (PreparedStatement pstmt = connection1.prepareStatement(deleteAnswersSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            // Xóa câu hỏi
            try (PreparedStatement pstmt = connection1.prepareStatement(deleteQuestionSQL)) {
                pstmt.setInt(1, questionID);
                pstmt.executeUpdate();
            }

            // Commit transaction
            connection1.commit();
        } catch (SQLException e) {
            connection1.rollback();
            throw e;
        } finally {
            connection1.setAutoCommit(true);
        }
    }

    public void deleteAssignment(int assignmentID) throws SQLException {
        // Bắt đầu transaction
        connection.setAutoCommit(false);
        try {
            // 1. Xóa các bản ghi trong Assignment_Question
            String deleteAssignmentQuestionQuery = "DELETE FROM Assignment_Question WHERE assignmentID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteAssignmentQuestionQuery)) {
                stmt.setInt(1, assignmentID);
                stmt.executeUpdate();
            }

            // 2. Xóa các câu hỏi liên quan
            String deleteQuestionsQuery = "DELETE FROM Question WHERE questionID IN " +
                    "(SELECT questionID FROM Assignment_Question WHERE assignmentID = ?)";
            try (PreparedStatement stmt = connection.prepareStatement(deleteQuestionsQuery)) {
                stmt.setInt(1, assignmentID);
                stmt.executeUpdate();
            }

            // 3. Xóa các câu trả lời liên quan
            String deleteAnswersQuery = "DELETE FROM Answer WHERE questionID IN " +
                    "(SELECT questionID FROM Assignment_Question WHERE assignmentID = ?)";
            try (PreparedStatement stmt = connection.prepareStatement(deleteAnswersQuery)) {
                stmt.setInt(1, assignmentID);
                stmt.executeUpdate();
            }

            // 4. Xóa assignment
            String deleteAssignmentQuery = "DELETE FROM Assignment WHERE assignmentID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteAssignmentQuery)) {
                stmt.setInt(1, assignmentID);
                stmt.executeUpdate();
            }

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            // Rollback nếu có lỗi
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        QuestionAndAnswerDAO questionAndAnswerDAO = new QuestionAndAnswerDAO();
        List<Question> list = assignmentDAO.getAllQuestionOfAssignment(5);
        list.forEach(System.out::println);
        for (Question question : list) {
            List<Answer> answerList = questionAndAnswerDAO.getAllAnswerOfOneQuestion(question.getQuestionID());
            answerList.forEach(System.out::println);
        }
    }
}
