package dao;

import util.DBConnect;
import model.Chat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;

import cloud.CloudinaryConfig;
import com.cloudinary.utils.ObjectUtils;

import java.io.*;
import java.util.Map;

public class ChatDAO {
    private static final Logger LOGGER = Logger.getLogger(ChatDAO.class.getName());
    private final Connection connection;

    public String uploadImageToCloudinary(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IOException("Không có file ảnh được chọn.");
        }

        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || !fileName.matches(".*\\.(png|jpg|jpeg|gif|webp)$")) {
            throw new IOException("Chỉ chấp nhận file ảnh (png, jpg, jpeg, gif, webp)");
        }

        fileName = fileName.replaceAll("[^\\w.-]", "_");
        File tempFile = File.createTempFile("chat_img_", "_" + fileName);
        try (InputStream input = filePart.getInputStream();
             OutputStream output = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "resource_type", "image",
                "folder", "chat_images",
                "public_id", "chat_" + System.currentTimeMillis(),
                "overwrite", false,
                "unique_filename", true
        );

        Map<?, ?> uploadResult = CloudinaryConfig.getCloudinary()
                .uploader()
                .upload(tempFile, uploadOptions);

        String url = (String) uploadResult.get("secure_url");
        if (url == null || url.isEmpty()) {
            throw new IOException("Upload ảnh thất bại.");
        }

        // Return the clean URL without any prefix
        return url;
    }

    public ChatDAO() {
        try {
            this.connection = DBConnect.getInstance().getConnection();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing database connection", e);
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public boolean save(Chat chat) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnect.getInstance().getConnection();
            LOGGER.info("Database connection established successfully");

            // Check if pictureSend is a URL (starts with http:// or https://)
            boolean isCloudUrl = chat.getPictureSend() != null &&
                    (chat.getPictureSend().startsWith("http://") ||
                            chat.getPictureSend().startsWith("https://"));
            
            boolean isBase64 = chat.getPictureSend() != null &&
                    chat.getPictureSend().startsWith("data:image");

            LOGGER.info("Picture data check - Length: " + (chat.getPictureSend() != null ? chat.getPictureSend().length() : 0) + 
                    ", Type: " + (chat.getPictureSend() != null ?
                    (isCloudUrl ? "Cloud URL" : 
                     isBase64 ? "Base64 data" : "Unknown format") : 
                    "No picture data"));

            // Build SQL statement
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO chat (userID, fullName, message, sendAt");
            
            if (chat.getPictureSend() != null) {
                sqlBuilder.append(", pictureSend");
            }
            
            sqlBuilder.append(") VALUES (?, ?, ?, GETDATE()");
            
            if (chat.getPictureSend() != null) {
                sqlBuilder.append(", ?");
            }
            
            sqlBuilder.append(")");
            
            String sql = sqlBuilder.toString();
            LOGGER.info("SQL statement: " + sql);

            stmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            
            // Set parameters
            stmt.setInt(paramIndex++, chat.getUserID());
            stmt.setString(paramIndex++, chat.getFullName());
            stmt.setString(paramIndex++, chat.getMessage());

            if (chat.getPictureSend() != null) {
                // For base64 images, store them as is without truncation
                String pictureData = chat.getPictureSend();
                LOGGER.info("Setting picture data - Length: " + pictureData.length() + 
                           ", First 100 chars: " + pictureData.substring(0, Math.min(100, pictureData.length())));
                stmt.setString(paramIndex, pictureData);
            }

            LOGGER.info("Executing SQL statement...");
            int rowsAffected = stmt.executeUpdate();
            result = rowsAffected > 0;
            LOGGER.info("SQL execution result: " + (result ? "Success" : "Failed") +
                    " (rows affected: " + rowsAffected + ")");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in save: " + e.getMessage(), e);
            throw new RuntimeException("Failed to save chat message", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database resources", e);
            }
        }

        return result;
    }

    public List<Chat> getAllChats() {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT chatID, userID, fullName, message, pictureSend, " +
                "CAST(CAST(sendAt AS DATETIME2) AS VARCHAR(23)) as sendAt " +
                "FROM Chat " +
                "ORDER BY sendAt ASC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Chat chat = new Chat(
                        resultSet.getInt("chatID"),
                        resultSet.getInt("userID"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("sendAt"),
                        resultSet.getString("fullName"),
                        resultSet.getString("pictureSend")
                );
                chats.add(chat);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving chat messages", e);
            throw new RuntimeException("Error retrieving chat messages", e);
        }

        return chats;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", e);
        }
    }
}
