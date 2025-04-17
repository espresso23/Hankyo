package websocket;

import dao.ChatDAO;
import model.Chat;
import service.ChatService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{userID}", encoders = ChatEncoder.class, decoders = ChatDecoder.class)
public class ChatWebSocket {
    private static final Logger LOGGER = Logger.getLogger(ChatWebSocket.class.getName());
    private Session session;
    private int userID;

    private final ChatService chatService = ChatService.getInstance();
    private final ChatDAO chatDAO = new ChatDAO();

    @OnOpen
    public void onOpen(@PathParam("userID") int userID, Session session) {
        this.userID = userID;
        this.session = session;
        if (chatService.register(this)) {
            LOGGER.info("New chat connection established for user: " + userID);
        }
    }

    @OnMessage
    public void onMessage(String messageJson, Session session) {
        try {
            Chat message = new ChatDecoder().decode(messageJson);
            message.setUserID(this.userID); // Set the sender's userID
            chatService.sendMessageToAll(message);
            chatDAO.save(message);
        } catch (DecodeException e) {
            LOGGER.log(Level.SEVERE, "Error decoding message from user " + userID, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing message from user " + userID, e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        chatService.close(this);
        LOGGER.info("Chat connection closed for user: " + userID);
    }

    public Session getSession() {
        return session;
    }

    public int getUserID() {
        return userID;
    }
}
