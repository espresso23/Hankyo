package dao;

import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnrollmentDAO {

    public boolean isEnroll(int learnerID, int courseID) {
        String sql = "Select * from Enrollments where learnerID = ? AND courseID = ? ";
        try {
            Connection connection = DBConnect.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, learnerID);
            preparedStatement.setInt(2, courseID);
            return preparedStatement.executeUpdate() > 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

}

