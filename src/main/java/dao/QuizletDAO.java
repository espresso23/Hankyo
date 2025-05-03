package dao;

import model.CustomFlashCard;
import model.Dictionary;
import model.FavoriteFlashCard;
import model.SystemFlashCard;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizletDAO {

    public List<SystemFlashCard> getAllSystemFlashCardByTopic(String topic) {
        List<SystemFlashCard> list = new ArrayList<>();
        int hiddentID = 1;
        String query = "SELECT s.SFCID, s.wordID, s.topic, d.word, d.definition, d.type, d.mean " +
                "FROM SystemFlashCard s " +
                "JOIN Dictionary d ON s.wordID = d.wordID " +
                "WHERE s.topic = ?";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            statement.setString(1, topic);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );

                    SystemFlashCard systemFlashCard = new SystemFlashCard(
                            hiddentID++,
                            resultSet.getInt("SFCID"),
                            resultSet.getInt("wordID"),
                            resultSet.getString("topic").trim()
                    );
                    systemFlashCard.setDictionary(dictionary);

                    list.add(systemFlashCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching system flashcards: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Dictionary> getAllDictionaryByWordID(int wordID) {
        List<Dictionary> list = new ArrayList<>();
        String query = "SELECT wordID, word, definition, type, mean FROM Dictionary WHERE wordID = ?";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            statement.setInt(1, wordID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );
                    list.add(dictionary);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching dictionary by wordID: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean addCustomFlashCard(CustomFlashCard cf) {
        String insertQuery = "INSERT INTO CustomFlashCard (learnerID, word, mean, topic) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            insertStmt.setInt(1, cf.getLearnerID());
            insertStmt.setString(2, cf.getWord());
            insertStmt.setString(3, cf.getMean());
            insertStmt.setString(4, cf.getTopic());
            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomFlashCard(int cfcid) {
        String query = "DELETE FROM CustomFlashCard WHERE CFCID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            stmt.setInt(1, cfcid);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomFlashCard(int cfcid, String word, String mean) {
        String query = "UPDATE CustomFlashCard SET word = ?, mean = ? WHERE CFCID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            stmt.setString(1, word);
            stmt.setString(2, mean);
            stmt.setInt(3, cfcid);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CustomFlashCard> getAllCustomFlashCardByTopicAndLeanerID(int learnerID, String topic) {
        List<CustomFlashCard> list = new ArrayList<>();
        String query = "SELECT CFCID, word, mean, topic, learnerID FROM CustomFlashCard WHERE topic = ? AND learnerID = ?";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            statement.setString(1, topic);
            statement.setInt(2, learnerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CustomFlashCard customFlashCard = new CustomFlashCard(
                            resultSet.getInt("learnerID"),
                            resultSet.getString("word") != null ? resultSet.getString("word").trim() : "",
                            resultSet.getString("mean") != null ? resultSet.getString("mean").trim() : "",
                            resultSet.getString("topic") != null ? resultSet.getString("topic").trim() : ""
                    );
                    customFlashCard.setCFCID(resultSet.getInt("CFCID"));
                    list.add(customFlashCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching custom flashcards: " + e.getMessage(), e);
        }
        return list;
    }

    public List<String> getAllFavoriteFlashCardListNameByLearnerID(int learnerID) throws SQLException {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT nameOfList FROM favoriteFlashCard WHERE learnerID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("nameOfList"));
                }
            }
        }
        return list;
    }

    public List<String> getAllTopics() throws SQLException {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM SystemFlashCard";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            while (rs.next()) {
                list.add(rs.getString("topic"));
            }
        }
        return list;
    }

    public List<String> getAllTopicsCustomFlashCardByLearnerID(int learnerID) throws SQLException {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM CustomFlashCard WHERE learnerID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            if (connection.isClosed()) {
                throw new SQLException("Connection is closed before use");
            }
            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("topic"));
                }
            }
        }
        return list;
    }

    public List<CustomFlashCard> getRandomFlashCards(int limit) throws SQLException {
        List<CustomFlashCard> flashCards = new ArrayList<>();
        String sql = "SELECT TOP (?) CFCID, learnerID, word, mean, topic FROM CustomFlashCard ORDER BY NEWID()";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CustomFlashCard card = new CustomFlashCard(
                    rs.getInt("learnerID"),
                    rs.getString("word"),
                    rs.getString("mean"),
                    rs.getString("topic")
                );
                card.setCFCID(rs.getInt("CFCID"));
                flashCards.add(card);
            }
        }
        
        return flashCards;
    }

    public List<SystemFlashCard> getRandomSystemFlashCards(String topic, int limit) throws SQLException {
        List<SystemFlashCard> list = new ArrayList<>();
        String query = "SELECT TOP (?) s.SFCID, s.wordID, s.topic, d.word, d.definition, d.type, d.mean " +
                "FROM SystemFlashCard s " +
                "JOIN Dictionary d ON s.wordID = d.wordID " +
                "WHERE s.topic = ? " +
                "ORDER BY NEWID()";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setString(2, topic);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );

                    SystemFlashCard systemFlashCard = new SystemFlashCard(
                            resultSet.getInt("SFCID"),
                            resultSet.getInt("wordID"),
                            resultSet.getString("topic").trim()
                    );
                    systemFlashCard.setDictionary(dictionary);
                    list.add(systemFlashCard);
                }
            }
        }
        return list;
    }

    public List<CustomFlashCard> getRandomCustomFlashCards(String topic, int learnerID, int limit) throws SQLException {
        List<CustomFlashCard> list = new ArrayList<>();
        String query = "SELECT TOP (?) CFCID, learnerID, word, mean, topic " +
                "FROM CustomFlashCard " +
                "WHERE topic = ? AND learnerID = ? " +
                "ORDER BY NEWID()";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setString(2, topic);
            statement.setInt(3, learnerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CustomFlashCard customFlashCard = new CustomFlashCard(
                            resultSet.getInt("learnerID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("mean").trim(),
                            resultSet.getString("topic").trim()
                    );
                    customFlashCard.setCFCID(resultSet.getInt("CFCID"));
                    list.add(customFlashCard);
                }
            }
        }
        return list;
    }

    public List<FavoriteFlashCard> getRandomFavoriteFlashCards(String topic, int learnerID, int limit) throws SQLException {
        List<FavoriteFlashCard> list = new ArrayList<>();
        String query = "SELECT TOP (?) f.FCID, f.learnerID, f.nameOfList, d.wordID, d.word, d.definition, d.type, d.mean " +
                "FROM FavoriteFlashCard f " +
                "JOIN Dictionary d ON f.wordID = d.wordID " +
                "WHERE f.nameOfList = ? AND f.learnerID = ? " +
                "ORDER BY NEWID()";

        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setString(2, topic);
            statement.setInt(3, learnerID);

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
                            resultSet.getString("nameOfList").trim()
                    );
                    list.add(favoriteFlashCard);
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        QuizletDAO dao = new QuizletDAO();
        int learnerID = 1;
        String topic = "1";
        List<CustomFlashCard> cards = dao.getAllCustomFlashCardByTopicAndLeanerID(learnerID, topic);
        if (cards.isEmpty()) {
            System.out.println("No custom flashcards found.");
        } else {
            for (CustomFlashCard card : cards) {
                System.out.println("CFCID: " + card.getCFCID());
                System.out.println("Word: " + card.getWord());
                System.out.println("Meaning: " + card.getMean());
                System.out.println("Topic: " + card.getTopic());
                System.out.println("LearnerID: " + card.getLearnerID());
                System.out.println("-------------------------");
            }
        }
    }
}
