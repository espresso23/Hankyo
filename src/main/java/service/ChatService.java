package service;

import model.Chat;
import websocket.ChatEncoder;
import websocket.ChatWebSocket;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatService {
    private static final Logger LOGGER = Logger.getLogger(ChatService.class.getName());
    private static ChatService chatService = null;
    protected static final Set<ChatWebSocket> chatWebsockets = new CopyOnWriteArraySet<>();

    private ChatService() {
    }

    public synchronized static ChatService getInstance() {
        if (chatService == null) {
            chatService = new ChatService();
        }
        return chatService;
    }

    public boolean register(ChatWebSocket chatWebsocket) {
        if (chatWebsocket == null) {
            LOGGER.warning("Attempted to register null chat websocket");
            return false;
        }
        boolean added = chatWebsockets.add(chatWebsocket);
        if (added) {
            LOGGER.info("New chat websocket registered. Total connections: " + chatWebsockets.size());
        }
        return added;
    }

    public boolean close(ChatWebSocket chatWebsocket) {
        if (chatWebsocket == null) {
            LOGGER.warning("Attempted to close null chat websocket");
            return false;
        }
        boolean removed = chatWebsockets.remove(chatWebsocket);
        if (removed) {
            LOGGER.info("Chat websocket closed. Remaining connections: " + chatWebsockets.size());
        }
        return removed;
    }

    public void sendMessageToAll(Chat message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        chatWebsockets.stream()
                .filter(chatWebsocket -> chatWebsocket.getUserID() != message.getUserID())
                .forEach(chatWebsocket -> {
                    Session session = chatWebsocket.getSession();
                    if (session != null && session.isOpen()) {
                        try {
                            ChatEncoder messageEncoder = new ChatEncoder();
                            session.getBasicRemote().sendText(messageEncoder.encode(message));
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, "Error sending message to user " + chatWebsocket.getUserID(), e);
                            // Remove the websocket if there's an error
                            close(chatWebsocket);
                        } catch (EncodeException e) {
                            LOGGER.log(Level.SEVERE, "Error encoding message for user " + chatWebsocket.getUserID(), e);
                        }
                    } else {
                        LOGGER.warning("Session is null or closed for user: " + chatWebsocket.getUserID());
                        // Remove the websocket if session is closed
                        close(chatWebsocket);
                    }
                });
    }
}