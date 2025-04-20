package dao;

import model.Honour;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HonourDAO {
    private Connection connection;

    public HonourDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }
    public List<Honour> getAllHonours() {
        String query = "select * from honour";
        List<Honour> honours = new ArrayList<Honour>();
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Honour h = new Honour(
                        resultSet.getInt("honourID"),
                        resultSet.getString("honourName"),
                        resultSet.getString("honour_img"),
                        resultSet.getString("honour_type")
                );
                honours.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách honour: " + e.getMessage(), e);
        }
        return honours;
    }

        public static void main(String[] args) {
            HonourDAO dao = new HonourDAO();
            List<Honour> honours = dao.getAllHonours();

            for (Honour h : honours) {
                System.out.println("ID: " + h.getHonourID());
                System.out.println("Name: " + h.getHonourName());
                System.out.println("Image: " + h.getHonourImg());
                System.out.println("Type: " + h.getHonourType());
                System.out.println("-----------------------------");
            }
        }
    }



