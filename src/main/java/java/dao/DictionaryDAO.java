package java.dao;

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
    public boolean removeFavoriteFlashCard(int learnerID, int wordID, String nameOfList) {
        String query = "DELETE FROM favoriteFlashCard WHERE learnerID = ? AND wordID = ? AND nameOfList = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, learnerID);
            stmt.setInt(2, wordID);
            stmt.setString(3, nameOfList);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public List<FavoriteFlashCard> getAllFavoriteFlashCardByLearnerID(int learnerID, String nameOfList) {
        List<FavoriteFlashCard> favoriteFlashCardList = new ArrayList<>();
        String query = "SELECT d.wordID, f.FCID, l.learnerID, f.nameOfList, d.word, d.mean, d.definition, d.type " +
                "FROM Dictionary d " +
                "JOIN favoriteFlashCard f ON f.wordID = d.wordID " +
                "JOIN Learner l ON f.learnerID = l.learnerID " +
                "WHERE l.learnerID = ? AND f.nameOfList = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, learnerID);
            ps.setString(2, nameOfList);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Dictionary dictionary = new Dictionary(
                            rs.getInt("wordID"),
                            safeTrim(rs.getString("word")),
                            safeTrim(rs.getString("definition")),
                            safeTrim(rs.getString("type")),
                            safeTrim(rs.getString("mean"))
                    );

                    FavoriteFlashCard favoriteFlashCard = new FavoriteFlashCard(
                            rs.getInt("FCID"),
                            dictionary,
                            rs.getString("nameOfList"),
                            new Learner(rs.getInt("learnerID"))
                    );

                    favoriteFlashCardList.add(favoriteFlashCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách flashcard yêu thích: " + e.getMessage(), e);
        }

        return favoriteFlashCardList;
    }


    public List<FavoriteFlashCard> getAllFavoriteFlashCardByLearnerIDWithoutnameOfList(int learnerID) {
        List<FavoriteFlashCard> favoriteFlashCardList = new ArrayList<>();
        String query = "SELECT d.wordID, f.FCID, l.learnerID, f.nameOfList, d.word, d.mean, d.definition, d.type " +
                "FROM Dictionary d " +
                "JOIN favoriteFlashCard f ON f.wordID = d.wordID " +
                "JOIN Learner l ON f.learnerID = l.learnerID " +
                "WHERE l.learnerID = ? ";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, learnerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Dictionary dictionary = new Dictionary(
                            rs.getInt("wordID"),
                            safeTrim(rs.getString("word")),
                            safeTrim(rs.getString("definition")),
                            safeTrim(rs.getString("type")),
                            safeTrim(rs.getString("mean"))
                    );

                    FavoriteFlashCard favoriteFlashCard = new FavoriteFlashCard(
                            rs.getInt("FCID"),
                            dictionary,
                            new Learner(rs.getInt("learnerID"))
                    );

                    favoriteFlashCardList.add(favoriteFlashCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách flashcard yêu thích: " + e.getMessage(), e);
        }

        return favoriteFlashCardList;
    }

    private String safeTrim(String str) {
        return str != null ? str.trim() : "";
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
        String checkQuery = "SELECT COUNT(*) FROM favoriteFlashCard WHERE learnerID = ? AND wordID = ? AND nameOfList = ?";
        String insertQuery = "INSERT INTO favoriteFlashCard (learnerID, wordID, nameOfList) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            // Kiểm tra trùng lặp
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, fc.getLearner().getLearnerID());
                checkStmt.setInt(2, fc.getDictionary().getWordID());
                checkStmt.setString(3, fc.getNameOfList());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Bản ghi đã tồn tại
                }
            }

            // Thêm mới (FCID sẽ tự động tăng)
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, fc.getLearner().getLearnerID());
                insertStmt.setInt(2, fc.getDictionary().getWordID());
                insertStmt.setString(3, fc.getNameOfList());
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFavoriteFlashCard(int learnerID, int wordID) {
        String query = "DELETE FROM favoriteFlashCard WHERE learnerID = ? AND wordID = ? ";

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
    public List<String> getFavoriteListNamesByLearnerID(int learnerID) {
        List<String> listNames = new ArrayList<>();
        String query = "SELECT DISTINCT nameOfList FROM favoriteFlashCard WHERE learnerID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("nameOfList");
                    if (name != null && !name.trim().isEmpty()) {
                        listNames.add(name.trim());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách nameOfList: " + e.getMessage(), e);
        }
        return listNames;
    }
    public static void main(String[] args) {
        // Thay đổi learnerID và nameOfList phù hợp với dữ liệu bạn có trong database
        int learnerID = 1;
        String nameOfList = "favorite";

        DictionaryDAO dao = new DictionaryDAO();
        List<FavoriteFlashCard> list = dao.getAllFavoriteFlashCardByLearnerID(learnerID, nameOfList);

        if (list.isEmpty()) {
            System.out.println("Không có flashcard yêu thích nào.");
        } else {
            System.out.println("Danh sách flashcard yêu thích:");
            for (FavoriteFlashCard card : list) {
                Dictionary d = card.getDictionary();
                System.out.println("- Word: " + d.getWord());
                System.out.println("  Mean: " + d.getMean());
                System.out.println("  Definition: " + d.getDefinition());
                System.out.println("  Type: " + d.getType());
                System.out.println("  List: " + card.getNameOfList());
                System.out.println("  FCID: " + card.getFCID());
                System.out.println("  LearnerID: " + card.getLearner().getLearnerID());
                System.out.println("----------------------");
            }
        }
    }
}