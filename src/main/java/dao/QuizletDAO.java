package dao;

import model.CustomFlashCard;
import model.Dictionary;
import model.SystemFlashCard;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizletDAO {
    private Connection connection;

    public QuizletDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public List<SystemFlashCard> getAllSystemFlashCardByTopic(String topic) {
        List<SystemFlashCard> list = new ArrayList<>();
        int hiddentID = 1;
        String query = "SELECT s.SFCID, s.wordID, s.topic, d.word, d.definition, d.type, d.mean " +
                "FROM SystemFlashCard s " +
                "JOIN Dictionary d ON s.wordID = d.wordID " +
                "WHERE s.topic = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, topic); // Truyền giá trị topic vào SQL

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    // Tạo đối tượng Dictionary từ dữ liệu bảng Words
                    Dictionary dictionary = new Dictionary(
                            resultSet.getInt("wordID"),
                            resultSet.getString("word").trim(),
                            resultSet.getString("definition").trim(),
                            resultSet.getString("type").trim(),
                            resultSet.getString("mean").trim()
                    );

                    // Tạo đối tượng SystemFlashCard và gán Dictionary vào
                    SystemFlashCard systemFlashCard = new SystemFlashCard(
                            hiddentID++,
                            resultSet.getInt("SFCID"),
                            resultSet.getInt("wordID"),
                            resultSet.getString("topic").trim()
                    );
                    systemFlashCard.setDictionary(dictionary); // Gán Dictionary vào SystemFlashCard

                    list.add(systemFlashCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<Dictionary> getAllDictionaryByWordID(int wordID) {
        List<Dictionary> list = new ArrayList<>();
        String query = "SELECT wordID, word, definition, type, mean FROM Dictionary WHERE wordID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, wordID); // Truyền giá trị wordID vào SQL

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
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean addCustomFlashCard(CustomFlashCard cf) {
        String insertQuery = "INSERT INTO CustomFlashCard (learnerID, word, mean, topic) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, cf.getLearnerID());
                insertStmt.setString(2, cf.getWord());
                insertStmt.setString(3, cf.getMean());
                insertStmt.setString(4, cf.getTopic());
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
             catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CustomFlashCard> getAllCustomFlashCardByTopic(String topic) {
        List<CustomFlashCard> list = new ArrayList<>();
        String insertQuery = "SELECT * FROM CustomFlashCard WHERE topic = ?";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setString(1, topic);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CustomFlashCard customFlashCard = new CustomFlashCard(
                            resultSet.getInt("learnerID"),
                            resultSet.getString("word"),
                            resultSet.getString("mean"),
                            topic);
                    list.add(customFlashCard);
                }
                }
        }catch (SQLException e) {}
        return list;
    }


    public List<String> getAllTopics() throws SQLException {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM SystemFlashCard";
        try (Connection con = DBConnect.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("topic"));
            }
        }
        return list;
    }

    public List<String> getAllTopicsCustomFlashCardByLearnerID(int learnerID) throws SQLException {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM CustomFlashCard WHERE learnerID = ?";

        try (Connection con = DBConnect.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, learnerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("topic"));
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        QuizletDAO dao = new QuizletDAO();
        List<SystemFlashCard> systemFlashCards = dao.getAllSystemFlashCardByTopic("Nature");
        SystemFlashCard so = systemFlashCards.get(1);
        System.out.println(so.getDictionary().getWord()+ so.getDictionary().getMean());
        for (SystemFlashCard card : systemFlashCards) {
            System.out.println(
                    " SFCID: " + card.getSFCID() + ", Word: " + card.getDictionary().getWord() + ", Mean: " + card.getDictionary().getMean());
        }
    }
}