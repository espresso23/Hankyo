//package dao;
//
//import model.CourseFeedback;
//import util.DBConnect;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CourseFeedbackDAO {
//    public List<CourseFeedback> getAllFeedbackByCourseID(int courseID){
//        List<CourseFeedback> courseFeedbacks = new ArrayList<>();
//        CourseFeedback courseFeedback = new CourseFeedback();
//        String sql = "Select * from CourseFeedback where courseID = ?";
//        try {
//            Connection connection = DBConnect.getInstance().getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1,courseID);
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()){
//                courseFeedback.setFeedbackID(rs.getInt("feedbackID"));
//                courseFeedback.setComment(rs.getString("comment"));
//                courseFeedback.setRating(rs.getInt("rating"));
//                courseFeedback.setCreatedAt(rs.getDate("createdAt"));
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
