package service;

import dao.EnrollmentDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import util.DBConnect;

public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private static final Logger logger = Logger.getLogger(EnrollmentService.class.getName());

    /**
     * Đăng ký khóa học cho learner nếu chưa đăng ký trước đó.
     *
     * @param learnerID ID học viên
     * @param courseID  ID khóa học
     * @return true nếu đăng ký thành công, false nếu đã đăng ký trước đó
     */
    public boolean enrollIfNotExists(int learnerID, int courseID) {
        if (enrollmentDAO.isEnroll(learnerID, courseID)) {
            // Đã đăng ký trước đó
            return false;
        }
        // Thực hiện đăng ký mới
        return enrollmentDAO.setEnrollForLearner(learnerID, courseID);
    }

    public int calculateCourseProgress(int learnerID, int courseID) throws SQLException {
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM Course_Content WHERE courseID = ?) as totalContents, " +
                     "(SELECT COUNT(*) FROM Progress WHERE learnerID = ? AND courseID = ? AND status = 'COMPLETED') as completedContents";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseID);
            stmt.setInt(2, learnerID);
            stmt.setInt(3, courseID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int totalContents = rs.getInt("totalContents");
                int completedContents = rs.getInt("completedContents");
                
                if (totalContents == 0) return 0;
                return (int) ((completedContents * 100.0) / totalContents);
            }
        }
        return 0;
    }
}
