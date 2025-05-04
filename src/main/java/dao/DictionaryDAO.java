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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        String insertQuery = "INSERT INTO favoriteFlashCard (learnerID, wordID, nameOfList) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, fc.getLearner().getLearnerID());
            insertStmt.setInt(2, fc.getDictionary().getWordID());
            insertStmt.setString(3, fc.getNameOfList());
            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0;
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
    public List<Dictionary> searchDictionary(String query) {
        List<Dictionary> dictionaryList = new ArrayList<>();
        String searchQuery = "SELECT wordID, word, definition, type, mean FROM dictionary WHERE word LIKE ? OR mean LIKE ? OR definition LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(searchQuery)) {

            String searchPattern = "%" + query + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm từ điển: " + e.getMessage(), e);
        }
        return dictionaryList;
    }

    public List<Dictionary> searchExactWord(String word) {
        List<Dictionary> dictionaryList = new ArrayList<>();
        String searchQuery = "SELECT wordID, word, definition, type, mean FROM dictionary WHERE word = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(searchQuery)) {

            statement.setString(1, word.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );
                    dictionaryList.add(dictionary);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm từ chính xác: " + e.getMessage(), e);
        }
        return dictionaryList;
    }

    public List<Dictionary> searchByType(String type) {
        List<Dictionary> dictionaryList = new ArrayList<>();
        String searchQuery = "SELECT wordID, word, definition, type, mean FROM dictionary WHERE type = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(searchQuery)) {

            statement.setString(1, type.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );
                    dictionaryList.add(dictionary);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm theo loại từ: " + e.getMessage(), e);
        }
        return dictionaryList;
    }

    public List<Dictionary> advancedSearch(String word, String type, String mean) {
        List<Dictionary> dictionaryList = new ArrayList<>();
        StringBuilder searchQuery = new StringBuilder(
            "SELECT wordID, word, definition, type, mean FROM dictionary WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (word != null && !word.trim().isEmpty()) {
            searchQuery.append(" AND word LIKE ?");
            params.add("%" + word.trim() + "%");
        }
        if (type != null && !type.trim().isEmpty()) {
            searchQuery.append(" AND type = ?");
            params.add(type.trim());
        }
        if (mean != null && !mean.trim().isEmpty()) {
            searchQuery.append(" AND mean LIKE ?");
            params.add("%" + mean.trim() + "%");
        }

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(searchQuery.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );
                    dictionaryList.add(dictionary);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm nâng cao: " + e.getMessage(), e);
        }
        return dictionaryList;
    }

    public boolean importDictionaryFromFile(String filePath) {
        String insertQuery = "INSERT INTO dictionary (word, mean, definition, type) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery);
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                // Định dạng: từ tiếng Hàn:từ tiếng Việt:định nghĩa
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String koreanWord = parts[0].trim();
                    String vietnameseWord = parts[1].trim();
                    String definition = parts.length > 2 ? parts[2].trim() : "";
                    
                    stmt.setString(1, koreanWord);
                    stmt.setString(2, vietnameseWord);
                    stmt.setString(3, definition);
                    stmt.setString(4, "noun"); // Mặc định là noun, có thể thay đổi sau
                    stmt.addBatch();
                }
            }
            
            int[] results = stmt.executeBatch();
            return true;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        // Thay đổi learnerID và nameOfList phù hợp với dữ liệu bạn có trong database
        int learnerID = 1;
        String nameOfList = "favorite";

        DictionaryDAO dao = new DictionaryDAO();
        boolean success = dao.importDictionaryFromFile("c:\\Users\\bearx\\Desktop\\가방Cặp sáchDanhNơi đựng sách vở đồ d.txt");
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

    public boolean addDictionaryExample(int wordID, String vietnameseExample, String koreanExample) {
        String query = "INSERT INTO dictionary_examples (wordID, vietnameseExample, koreanExample) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, wordID);
            stmt.setString(2, vietnameseExample);
            stmt.setString(3, koreanExample);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, String>> getDictionaryExamples(int wordID) {
        List<Map<String, String>> examples = new ArrayList<>();
        String query = "SELECT vietnameseExample, koreanExample FROM dictionary_examples WHERE wordID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, wordID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> example = new HashMap<>();
                    example.put("vi", rs.getString("vietnameseExample"));
                    example.put("han", rs.getString("koreanExample"));
                    examples.add(example);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examples;
    }

    public boolean updateDictionaryWithAI(int wordID, String definition, String type) {
        String query = "UPDATE dictionary SET definition = ?, type = ? WHERE wordID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, definition);
            stmt.setString(2, type);
            stmt.setInt(3, wordID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm từ mới vào bảng dictionary và trả về wordID vừa tạo
    public int insertDictionaryAndGetId(String word, String mean, String definition, String type) {
        String sql = "INSERT INTO dictionary (word, mean, definition, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, word);
            stmt.setString(2, mean);
            stmt.setString(3, definition);
            stmt.setString(4, type);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return -1;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}