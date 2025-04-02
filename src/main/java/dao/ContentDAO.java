package dao;

import model.Learner;
import model.Newspaper;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContentDAO {
    private Connection connection;

    public ContentDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public Newspaper getNewspaperByID(int newspaperID) {
        String query = "SELECT newspaperID, title, content, pictureFilePath FROM Newspaper WHERE newspaperID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newspaperID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Newspaper(
                        rs.getInt("newspaperID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("pictureFilePath")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ContentDAO contentDAO = new ContentDAO();
        contentDAO.getNewspaperByID(1);
        System.out.println(contentDAO.getNewspaperByID(1));
    }
}
