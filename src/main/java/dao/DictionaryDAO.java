package dao;

import model.Dictionary;
import model.FavoriteFlashCard;
import model.Learner;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDAO {

    private Connection getConnection() {
        return DBConnect.getInstance().getConnection();
    }

    public List<Dictionary> getAllDictionary() {
        List<Dictionary> dictionaryList = new ArrayList<>();
        String query = "SELECT wordID, word, definition, type, mean FROM dictionary";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Dictionary word = new Dictionary(
                        resultSet.getInt("wordID"),
                        resultSet.getString("word").trim(),
                        resultSet.getString("definition").trim(),
                        resultSet.getString("type").trim(),
                        resultSet.getString("mean").trim()
                );
                dictionaryList.add(word);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách từ điển: " + e.getMessage(), e);
        }
        return dictionaryList;
    }

    public List<FavoriteFlashCard> getAllFavoriteFlashCardByLearnerID(int learnerID) {
        List<FavoriteFlashCard> favoriteFlashCardList = new ArrayList<>();
        String query = "SELECT d.wordID, f.FCID, l.learnerID, d.word, d.mean, d.definition, d.type " +
                "FROM Dictionary d " +
                "JOIN favoriteFlashCard f ON f.wordID = d.wordID " +
                "JOIN Learner l ON f.learnerID = l.learnerID " +
                "WHERE l.learnerID = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, learnerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );

                    FavoriteFlashCard favoriteFlashCard = new FavoriteFlashCard(
                            resultSet.getInt("FCID"),
                            dictionary,
                            new Learner(resultSet.getInt("learnerID"))
                    );

                    favoriteFlashCardList.add(favoriteFlashCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách flashcard yêu thích: " + e.getMessage(), e);
        }

        return favoriteFlashCardList;
    }

    public Dictionary getDictionaryByWordID(int wordID) {
        String query = "SELECT wordID, word, definition, type, mean FROM dictionary WHERE wordID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, wordID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Dictionary(
                            rs.getInt("wordID"),
                            rs.getString("word").trim(),
                            rs.getString("definition").trim(),
                            rs.getString("type").trim(),
                            rs.getString("mean").trim()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addFavoriteFlashCard(FavoriteFlashCard fc) {
        String checkQuery = "SELECT COUNT(*) FROM favoriteFlashCard WHERE learnerID = ? AND wordID = ?";
        String insertQuery = "INSERT INTO favoriteFlashCard (learnerID, wordID) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
            // Kiểm tra trùng lặp
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, fc.getLearner().getLearnerID());
                checkStmt.setInt(2, fc.getDictionary().getWordID());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Bản ghi đã tồn tại
                }
            }

            // Thêm mới (FCID sẽ tự động tăng)
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, fc.getLearner().getLearnerID());
                insertStmt.setInt(2, fc.getDictionary().getWordID());
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFavoriteFlashCard(int learnerID, int wordID) {
        String query = "DELETE FROM favoriteFlashCard WHERE learnerID = ? AND wordID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, learnerID);
            stmt.setInt(2, wordID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}