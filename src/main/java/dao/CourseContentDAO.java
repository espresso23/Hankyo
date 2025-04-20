package dao;

import cloud.CloudinaryConfig;
import com.cloudinary.utils.ObjectUtils;
import model.Answer;
import model.Assignment;
import model.CourseContent;
import model.Question;
import util.DBConnect;

import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseContentDAO {
    private Connection conn;

    public CourseContentDAO() {
        this.conn = DBConnect.getInstance().getConnection();
    }

    //function dùng để lấy url của media từ cloud và lưu vào database
    public String convertMediaToUrl(Part filePart) throws IOException {
        // Validate input
        if (filePart == null || filePart.getSize() == 0) {
            throw new IOException("No file uploaded or file is empty.");
        }

        // Get and validate filename
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IOException("Invalid file name");
        }

        // Sanitize filename
        fileName = fileName.replaceAll("[^\\w.-]", "_");

        // Create temp file
        File tempFile = null;
        try {
            tempFile = File.createTempFile("cloud_", "_" + fileName);

            // Copy file content
            try (InputStream in = filePart.getInputStream();
                 OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Prepare upload options
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "public_id", "courses/" + System.currentTimeMillis() + "_" + fileName,
                    "folder", "courses",
                    "overwrite", false,
                    "unique_filename", true
            );

            // Upload to Cloudinary
            Map<?, ?> uploadResult = CloudinaryConfig.getCloudinary()
                    .uploader()
                    .upload(tempFile, uploadOptions);

            // Get secure URL
            String fileUrl = (String) uploadResult.get("secure_url");
            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new IOException("Upload succeeded but no URL returned");
            }

            return fileUrl;
        } finally {
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public boolean addCourseContent(CourseContent courseContent) throws SQLException {
        String query = "Insert into Course_Content (title, media, description, image, courseID) values (?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, courseContent.getTitle());
            preparedStatement.setString(2, courseContent.getMedia());
            preparedStatement.setString(3, courseContent.getDescription());
            preparedStatement.setString(4, courseContent.getImage());
            preparedStatement.setInt(5, courseContent.getCourseID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean addCourseContentAssignment(CourseContent courseContent) throws SQLException {
        String query = "Insert into Course_Content(courseID,assignmentID) values(?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, courseContent.getCourseID());
            preparedStatement.setInt(2, courseContent.getAssignment().getAssignmentID());
            return preparedStatement.executeUpdate() > 0;
        }
    }


    // Thêm phương thức lấy danh sách nội dung khóa học theo courseID
    public List<CourseContent> listCourseContentsByCourseID(int courseID) throws SQLException {
        List<CourseContent> contents = new ArrayList<>();
        String sql = "SELECT cc.*, a.title as assignmentTitle, a.description as assignmentDes, " +
                    "a.assignmentID, a.lastUpdated " +
                    "FROM Course_Content cc " +
                    "LEFT JOIN Assignment a on cc.assignmentID = a.assignmentID " +
                    "WHERE cc.courseID = ? " +
                    "ORDER BY cc.course_contentID";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CourseContent content = new CourseContent();
                content.setCourseContentID(rs.getInt("course_contentID"));
                content.setCourseID(rs.getInt("courseID"));
                content.setTitle(rs.getString("title"));
                content.setDescription(rs.getString("description"));
                content.setMedia(rs.getString("media"));
                
                // Chỉ set assignment nếu có assignmentID
                int assignmentId = rs.getInt("assignmentID");
                if (!rs.wasNull()) {
                    Assignment assignment = new Assignment();
                    assignment.setAssignmentTitle(rs.getString("assignmentTitle"));
                    assignment.setDescription(rs.getString("assignmentDes"));
                    assignment.setLastUpdated(rs.getDate("lastUpdated"));
                    assignment.setAssignmentID(assignmentId);
                    content.setAssignment(assignment);
                }
                
                contents.add(content);
            }
        }
        return contents;
    }

    public CourseContent getCourseContentById(int contentID) throws SQLException {
        String sql = "SELECT * FROM Course_Content WHERE course_contentID = ?";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, contentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CourseContent content = new CourseContent();
                content.setCourseContentID(rs.getInt("course_contentID"));
                content.setCourseID(rs.getInt("courseID"));
                content.setTitle(rs.getString("title"));
                content.setDescription(rs.getString("description"));
                content.setMedia(rs.getString("media"));
                return content;
            }
        }
        return null;
    }

    public boolean updateCourseContent(CourseContent content) throws SQLException {
        String query = "UPDATE Course_Content SET title = ?, description = ?, media = ?, image = ? WHERE course_contentID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, content.getTitle());
            preparedStatement.setString(2, content.getDescription());
            preparedStatement.setString(3, content.getMedia());
            preparedStatement.setString(4, content.getImage());
            preparedStatement.setInt(5, content.getCourseContentID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean deleteCourseContent(int contentID) throws SQLException {
        String query = "DELETE FROM Course_Content WHERE course_contentID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, contentID);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public Assignment getAssignmentById(int assignmentID) throws SQLException {
        String query = "SELECT * FROM Assignment WHERE assignmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assignmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Assignment assignment = new Assignment();
                    assignment.setAssignmentID(rs.getInt("assignmentID"));
                    assignment.setAssignmentTitle(rs.getString("title"));
                    assignment.setDescription(rs.getString("description"));
                    return assignment;
                }
            }
        }
        return null;
    }

    public List<Question> getQuestionsByAssignmentId(int assignmentID) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT DISTINCT q.*, a.answerID, a.answerText, a.isCorrect, a.option_label " +
                "FROM Question q " +
                "JOIN Assignment_Question aq ON q.questionID = aq.questionID " +
                "JOIN Answer a ON aq.answerID = a.answerID " +
                "WHERE aq.assignmentID = ? " +
                "ORDER BY q.questionID, a.answerID";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assignmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                Question currentQuestion = null;
                while (rs.next()) {
                    int questionId = rs.getInt("questionID");

                    // Nếu là câu hỏi mới
                    if (currentQuestion == null || currentQuestion.getQuestionID() != questionId) {
                        currentQuestion = new Question();
                        currentQuestion.setQuestionID(questionId);
                        currentQuestion.setQuestionText(rs.getString("questionText"));
                        currentQuestion.setQuestionImage(rs.getString("questionImg"));
                        currentQuestion.setAudioFile(rs.getString("audio_file"));
                        currentQuestion.setQuestionType(rs.getString("questionType"));
                        currentQuestion.setQuestionMark(rs.getDouble("questionMark"));
                        currentQuestion.setAnswers(new ArrayList<>());
                        questions.add(currentQuestion);
                    }

                    // Thêm câu trả lời vào câu hỏi hiện tại
                    Answer answer = new Answer();
                    answer.setAnswerID(rs.getInt("answerID"));
                    answer.setAnswerText(rs.getString("answerText"));
                    answer.setCorrect(rs.getBoolean("isCorrect"));
                    answer.setOptionLabel(rs.getString("option_label"));
                    currentQuestion.getAnswers().add(answer);
                }
            }
        }

        return questions;
    }

    public int addQuestion(Question question) throws SQLException {
        String sqlQuestion = "INSERT INTO Question (questionText, questionImg, audio_file, questionType, questionMark) VALUES (?, ?, ?, ?, ?)";
        String sqlAnswer = "INSERT INTO Answer (answerText, isCorrect, option_label, questionID) VALUES (?, ?, ?, ?)";
        String sqlAssignmentQuestion = "INSERT INTO Assignment_Question (assignmentID, questionID, answerID) VALUES (?, ?, ?)";

        PreparedStatement pstmtQuestion = null;
        PreparedStatement pstmtAnswer = null;
        PreparedStatement pstmtAssignmentQuestion = null;
        ResultSet generatedKeys = null;

        try {
            // Bắt đầu transaction
            conn.setAutoCommit(false);

            // Lưu Question
            pstmtQuestion = conn.prepareStatement(sqlQuestion, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtQuestion.setString(1, question.getQuestionText());
            pstmtQuestion.setString(2, question.getQuestionImage() != null ? question.getQuestionImage() : null);
            pstmtQuestion.setString(3, question.getAudioFile() != null ? question.getAudioFile() : null);
            pstmtQuestion.setString(4, question.getQuestionType());
            pstmtQuestion.setDouble(5, question.getQuestionMark());
            pstmtQuestion.executeUpdate();

            // Lấy questionID vừa được tạo
            int questionID;
            generatedKeys = pstmtQuestion.getGeneratedKeys();
            if (generatedKeys.next()) {
                questionID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Không thể lấy ID của câu hỏi mới");
            }

            // Lưu Answers
            pstmtAnswer = conn.prepareStatement(sqlAnswer, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtAssignmentQuestion = conn.prepareStatement(sqlAssignmentQuestion);

            for (Answer answer : question.getAnswers()) {
                pstmtAnswer.setString(1, answer.getAnswerText());
                pstmtAnswer.setBoolean(2, answer.isCorrect());
                pstmtAnswer.setString(3, String.valueOf(answer.getOptionLabel()));
                pstmtAnswer.setInt(4, questionID);
                pstmtAnswer.executeUpdate();

                // Lấy answerID vừa được tạo
                int answerID;
                generatedKeys = pstmtAnswer.getGeneratedKeys();
                if (generatedKeys.next()) {
                    answerID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Không thể lấy ID của câu trả lời mới");
                }

                // Lưu vào bảng Assignment_Question
                pstmtAssignmentQuestion.setInt(1, question.getAssignmentID());
                pstmtAssignmentQuestion.setInt(2, questionID);
                pstmtAssignmentQuestion.setInt(3, answerID);
                pstmtAssignmentQuestion.executeUpdate();
            }

            // Commit transaction
            conn.commit();
            return questionID;

        } catch (SQLException e) {
            // Rollback nếu có lỗi
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            // Đóng các PreparedStatement
            if (pstmtQuestion != null) pstmtQuestion.close();
            if (pstmtAnswer != null) pstmtAnswer.close();
            if (pstmtAssignmentQuestion != null) pstmtAssignmentQuestion.close();
            if (generatedKeys != null) generatedKeys.close();

            // Reset auto-commit
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}