package dao;

import model.CourseSales;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseSalesDAO {
    public List<CourseSales> getSoldCoursesByExpert(int expertID) {
        List<CourseSales> list = new ArrayList<>();
        String sql = "SELECT c.courseID, c.title, COUNT(cp.courseID) AS totalSold " +
                "FROM Course c " +
                "LEFT JOIN Course_Paid cp ON c.courseID = cp.courseID " +
                "WHERE c.expertID = ? " +
                "GROUP BY c.courseID, c.title " +
                "ORDER BY totalSold DESC";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, expertID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int courseID = rs.getInt("courseID");
                String title = rs.getString("title");
                int totalSold = rs.getInt("totalSold");

                list.add(new CourseSales(courseID, title, totalSold));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
