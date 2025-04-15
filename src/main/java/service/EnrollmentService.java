package service;

import dao.EnrollmentDAO;

public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

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
}
