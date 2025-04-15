package dao;

import cloud.CloudinaryConfig;
import com.cloudinary.utils.ObjectUtils;
import model.*;
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
    private Connection connection;

    public CourseContentDAO() {
        this.connection = DBConnect.getInstance().getConnection();
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courseContent.getTitle());
            preparedStatement.setString(2, courseContent.getMedia());
            preparedStatement.setString(3, courseContent.getDescription());
            preparedStatement.setString(4, courseContent.getImage());
            preparedStatement.setInt(5, courseContent.getCourseID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean addCourseContentAssignment(CourseContent courseContent) throws SQLException {
        String query = "Insert into Course_Content(courseID,title,description,assignmentID) values(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseContent.getCourseID());
            preparedStatement.setString(2, courseContent.getTitle());
            preparedStatement.setString(3, courseContent.getDescription());
            preparedStatement.setInt(4, courseContent.getAssignment().getAssignmentID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean addCourseContentExam(CourseContent courseContent) throws SQLException {
        String query = "Insert into Course_Content(courseID,title,description,examID) values(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseContent.getCourseID());
            preparedStatement.setString(2, courseContent.getTitle());
            preparedStatement.setString(3, courseContent.getDescription());
            preparedStatement.setInt(4, courseContent.getExam().getExamID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Thêm phương thức lấy danh sách nội dung khóa học theo courseID
    public List<CourseContent> listCourseContentsByCourseID(int courseID) throws SQLException {
        List<CourseContent> contents = new ArrayList<>();
        String query = "SELECT * FROM Course_Content WHERE courseID = ? ORDER BY course_contentID";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, courseID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CourseContent content = new CourseContent();
                    content.setCourseContentID(resultSet.getInt("course_contentID"));
                    content.setCourseID(resultSet.getInt("courseID"));
                    content.setTitle(resultSet.getString("title"));
                    content.setDescription(resultSet.getString("description"));
                    content.setImage(resultSet.getString("image"));
                    content.setMedia(resultSet.getString("media"));

                    int assignmentID = resultSet.getInt("assignmentID");
                    if (!resultSet.wasNull()) {
                        Assignment a = new Assignment();
                        a.setAssignmentID(assignmentID);
                        content.setAssignment(a);
                    }

                    int examID = resultSet.getInt("examID");
                    if (!resultSet.wasNull()) {
                        Exam e = new Exam();
                        e.setExamID(examID);
                        content.setExam(e);
                    }

                    contents.add(content);
                }
            }
        }

        return contents;
    }


    public CourseContent getCourseContentById(int contentID) throws SQLException {
        String query = "SELECT * FROM Course_Content WHERE course_contentID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, contentID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CourseContent content = new CourseContent();
                    content.setCourseContentID(resultSet.getInt("course_contentID"));
                    content.setCourseID(resultSet.getInt("courseID"));
                    content.setTitle(resultSet.getString("title"));
                    content.setDescription(resultSet.getString("description"));
                    content.setImage(resultSet.getString("image"));
                    content.setMedia(resultSet.getString("media"));

                    // Lấy assignmentID nếu có
                    int assignmentID = resultSet.getInt("assignmentID");
                    if (!resultSet.wasNull()) {
                        content.setAssignment(new Assignment());
                        content.getAssignment().setAssignmentID(assignmentID);
                    }

                    // Lấy examID nếu có
                    int examID = resultSet.getInt("examID");
                    if (!resultSet.wasNull()) {
                        content.setExam(new Exam());
                        content.getExam().setExamID(examID);
                    }

                    return content;
                }
            }
        }
        return null;
    }

    public boolean updateCourseContent(CourseContent content) throws SQLException {
        String query = "UPDATE Course_Content SET title = ?, description = ?, media = ?, image = ? WHERE course_contentID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, contentID);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public Assignment getAssignmentById(int assignmentID) throws SQLException {
        String query = "SELECT * FROM Assignment WHERE assignmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
                        //     currentQuestion.setAnswers(new ArrayList<>());
                        questions.add(currentQuestion);
                    }

//                    // Thêm câu trả lời vào câu hỏi hiện tại
//                    Answer answer = new Answer();
//                    answer.setAnswerID(rs.getInt("answerID"));
//                    answer.setAnswerText(rs.getString("answerText"));
//                    answer.setCorrect(rs.getBoolean("isCorrect"));
//                    answer.setOptionLabel(rs.getString("option_label").charAt(0));
//                    currentQuestion.getAnswers().add(answer);
//                }
                }
            }

            return questions;
        }


    }
}