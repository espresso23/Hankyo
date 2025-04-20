package dao;

import filter.BadwordsFilter;
import model.Chat;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatDAO {

    private static final Logger LOGGER = Logger.getLogger(ChatDAO.class.getName());

    private Connection getConnection() throws SQLException, Exception {
        // Use the DatabaseConnect utility for obtaining the connection
        return DBConnect.getInstance().getConnection();
    }

    public void save(Chat chat) {
        // First, retrieve characterName using JOIN
        String selectSql = "SELECT c.fullName FROM [user] c WHERE c.userID = ?";

        try (Connection conn = getConnection(); PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, chat.getUserID());
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("fullName");
                chat.setFullName(fullName);
                // Censor bad words in the message
                String censoredMessage = BadwordsFilter.censorBadWords(chat.getMessage());
                chat.setMessage(censoredMessage);

                // Now insert the chat message
                String sql = "INSERT INTO chat (userID, fullName, message) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, chat.getUserID());  // Corrected method call
                    stmt.setString(2, chat.getFullName()); // Added characterName from database
                    stmt.setString(3, chat.getMessage());
                    stmt.executeUpdate();
                }
            } else {
                LOGGER.warning("Character ID not found: " + chat.getUserID());
            }
        } catch (SQLException e) {
            // Log the exception for debugging
            LOGGER.log(Level.SEVERE, "SQL error while saving message", e);
        } catch (Exception e) {
            // Log any other exceptions
            LOGGER.log(Level.SEVERE, "Error while saving message", e);
        }
    }

    public List<Chat> getAll() {
        List<Chat> chat = new ArrayList<>();
        String sql = "SELECT * FROM chat ORDER BY sendAt ASC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int chatID = rs.getInt("chatID");
                int userID = rs.getInt("userID");
                String fullName = rs.getString("fullName");
                String message = rs.getString("message");
                Timestamp sendAt = rs.getTimestamp("sendAt");
                // Remove the extra comma at the end
                chat.add(new Chat(chatID, userID, message, sendAt, fullName));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error while fetching messages", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while fetching messages", e);
        }
        return chat;
    }

}
