package websocket;

import dao.ChatDAO;
import model.Chat;
import service.ChatService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;

@ServerEndpoint(value = "/chat/{userID}", 
    encoders = ChatEncoder.class, 
    decoders = ChatDecoder.class,
    configurator = ChatWebSocketConfigurator.class)
public class ChatWebSocket {
    private static final Logger LOGGER = Logger.getLogger(ChatWebSocket.class.getName());
    private Session session;
    private int userID;

    private final ChatService chatService = ChatService.getInstance();
    private final ChatDAO chatDAO = new ChatDAO();

    @OnOpen
    public void onOpen(@PathParam("userID") int userID, Session session) {
        try {
            this.userID = userID;
            this.session = session;
            session.setMaxTextMessageBufferSize(10 * 1024 * 1024); // 10MB
            LOGGER.info("WebSocket session opened - Session ID: " + session.getId() + 
                       ", User ID: " + userID + 
                       ", Query String: " + session.getQueryString() +
                       ", Max Message Size: " + session.getMaxTextMessageBufferSize());
            if (chatService.register(this)) {
                LOGGER.info("New chat connection established for user: " + userID);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in onOpen: " + e.getMessage(), e);
        }
    }

    @OnMessage
    public void onMessage(String messageJson, Session session) {
        try {
            LOGGER.info("Received WebSocket message - Length: " + (messageJson != null ? messageJson.length() : 0));
            
            if (messageJson == null || messageJson.trim().isEmpty()) {
                LOGGER.warning("Received empty message");
                return;
            }
            
            try {
                org.json.JSONObject json = new org.json.JSONObject(messageJson);
                int userID = json.getInt("userID");
                String fullName = json.getString("fullName");
                String message = json.optString("message", "");
                String pictureSend = json.optString("pictureSend", null);
                
                LOGGER.info("Parsed message - userID: " + userID + 
                           ", fullName: " + fullName + 
                           ", message length: " + message.length() + 
                           ", pictureSend length: " + (pictureSend != null ? pictureSend.length() : 0));
                
                // If pictureSend is base64 data, convert it to a URL
                if (pictureSend != null && pictureSend.startsWith("data:image")) {
                    LOGGER.info("Processing image upload - Base64 length: " + pictureSend.length());
                    
                    try {
                        ChatDAO chatDAO = new ChatDAO();
                        Part filePart = convertBase64ToPart(pictureSend);
                        LOGGER.info("Converted base64 to Part object - Size: " + filePart.getSize());
                        String originalPictureSend = pictureSend;
                        try {
                            pictureSend = chatDAO.uploadImageToCloudinary(filePart);
                            LOGGER.info("Image uploaded successfully to Cloudinary: " + pictureSend);
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Error processing image upload: " + e.getMessage(), e);
                            // Keep the original base64 data if upload fails
                            pictureSend = originalPictureSend;
                            LOGGER.info("Falling back to base64 data - Length: " + pictureSend.length());
                            session.getBasicRemote().sendText("{\"warning\": \"Image upload failed, saving as base64\"}");
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error converting base64 to Part: " + e.getMessage(), e);
                        session.getBasicRemote().sendText("{\"error\": \"Failed to process image: " + e.getMessage() + "\"}");
                        return;
                    }
                }
                
                Chat chat = new Chat(userID, message, fullName, pictureSend);
                LOGGER.info("Created Chat object - Message length: " + message.length() + 
                    ", Picture data: " + (pictureSend != null ? 
                        (pictureSend.startsWith("data:image") ? "base64 (" + pictureSend.length() + " chars)" : 
                         pictureSend.startsWith("http") ? "Cloudinary URL" : 
                         "unknown format") : 
                    "no image"));
                
                try {
                    boolean saved = chatDAO.save(chat);
                    LOGGER.info("Message saved to database: " + saved);
                    if (!saved) {
                        session.getBasicRemote().sendText("{\"error\": \"Failed to save message to database\"}");
                        return;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error saving to database: " + e.getMessage(), e);
                    session.getBasicRemote().sendText("{\"error\": \"Failed to save message: " + e.getMessage() + "\"}");
                    return;
                }
                
                chatService.sendMessageToAll(chat);
                LOGGER.info("Message broadcasted to all clients");
                
            } catch (org.json.JSONException e) {
                LOGGER.log(Level.SEVERE, "Error parsing JSON: " + e.getMessage(), e);
                session.getBasicRemote().sendText("{\"error\": \"Invalid message format: " + e.getMessage() + "\"}");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing message: " + e.getMessage(), e);
            try {
                session.getBasicRemote().sendText("{\"error\": \"Error processing message: " + e.getMessage() + "\"}");
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error sending error message: " + ex.getMessage(), ex);
            }
        }
    }

    private Part convertBase64ToPart(String base64Data) throws IOException {
        // Remove the data:image/xxx;base64, prefix
        String base64Image = base64Data.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        
        // Create a temporary file
        File tempFile = File.createTempFile("chat_img_", ".jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(imageBytes);
        }
        
        // Create a Part object from the temporary file
        return new Part() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(tempFile);
            }
            
            @Override
            public String getContentType() {
                return "image/jpeg";
            }
            
            @Override
            public String getName() {
                return "image";
            }
            
            @Override
            public String getSubmittedFileName() {
                return "chat_image.jpg";
            }
            
            @Override
            public long getSize() {
                return imageBytes.length;
            }
            
            @Override
            public void write(String fileName) throws IOException {
                Files.copy(getInputStream(), Paths.get(fileName));
            }
            
            @Override
            public void delete() throws IOException {
                tempFile.delete();
            }
            
            @Override
            public String getHeader(String name) {
                return null;
            }
            
            @Override
            public Collection<String> getHeaders(String name) {
                return Collections.emptyList();
            }
            
            @Override
            public Collection<String> getHeaderNames() {
                return Collections.emptyList();
            }
        };
    }

    @OnClose
    public void onClose(Session session) {
        try {
            LOGGER.info("WebSocket session closing - Session ID: " + session.getId() + 
                       ", User ID: " + userID);
            chatService.close(this);
            LOGGER.info("Chat connection closed for user: " + userID);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in onClose: " + e.getMessage(), e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.log(Level.SEVERE, "WebSocket error for session " + session.getId() + 
                  ": " + error.getMessage(), error);
    }

    public Session getSession() {
        return session;
    }

    public int getUserID() {
        return userID;
    }
}
