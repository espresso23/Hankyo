package dao;

import model.Dictionary;
import util.DBConnect;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDAO {
    private Connection connection;

    public DictionaryDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public List<Dictionary> readDictionaryFile(String filePath) {
        List<Dictionary> wordList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    Dictionary word = new Dictionary(parts[0], parts[1], parts[2], parts[3]);
                    wordList.add(word);
                } else {
                    System.out.println("Dòng không hợp lệ: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordList;
    }

    public void saveToDatabase(List<Dictionary> dictionaryList) {
        String query = "INSERT INTO dictionary ( word, mean, type, definition) VALUES ( ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Dictionary word : dictionaryList) {
                stmt.setString(1, word.getWord());
                stmt.setString(2, word.getDefinition());
                stmt.setString(3, word.getType());
                stmt.setString(4, word.getMean());
                stmt.executeUpdate();
            }
            System.out.println("Lưu dữ liệu thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Dictionary> getAllDictionary() {
        List<Dictionary> dictionaryList = new ArrayList<>();
        String query = "SELECT word, definition, type, mean FROM dictionary";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Dictionary word = new Dictionary(
                        resultSet.getString("word").trim(),
                        resultSet.getString("definition").trim(),
                        resultSet.getString("type").trim(),
                        resultSet.getString("mean").trim()
                );
                dictionaryList.add(word);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dictionaryList;
    }


    public static void main(String[] args) {
        DictionaryDAO dao = new DictionaryDAO();
        String filePath = "src/main/webapp/asset/font/DanhSachTuVung.txt";

        // Đọc danh sách từ file
        List<Dictionary> dictionaryList = dao.readDictionaryFile(filePath);

        // Lưu vào database
        dao.saveToDatabase(dictionaryList);

        System.out.println("Quá trình nhập dữ liệu hoàn tất!");
    }

}
