package dao;

import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentDAO {

    public boolean isEnroll(int learnerID, int courseID) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE learnerID = ? AND courseID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, learnerID);
            ps.setInt(2, courseID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setEnrollForLearner(int learnerID, int courseID) {
        String sql = "INSERT INTO Enrollments(learnerID, courseID, enrollDate) VALUES (?, ?, GETDATE())";
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, learnerID);
            preparedStatement.setInt(2, courseID);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countEnrolledLearners(int courseID) {
        String sql = "SELECT COUNT(DISTINCT learnerID) FROM Enrollments WHERE courseID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

