package controller;

import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@ServerEndpoint("/chat")
public class ChatWebSocket {
    private static final Logger logger = Logger.getLogger(ChatWebSocket.class.getName());
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("New connection established: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String type = jsonMessage.optString("type", "message");
            
            switch (type) {
                case "connect":
                    logger.info("User connected: " + jsonMessage.getString("userID"));
                    // Có thể lưu thông tin user vào session nếu cần
                    break;
                case "message":
                    logger.info("Received message from " + jsonMessage.getString("userID"));
                    broadcast(message);
                    break;
                default:
                    logger.warning("Unknown message type: " + type);
            }
        } catch (Exception e) {
            logger.severe("Error processing message: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        logger.info("Connection closed: " + session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        logger.severe("WebSocket error: " + error.getMessage());
    }

    private void broadcast(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        logger.severe("Error sending message: " + e.getMessage());
                    }
                }
            }
        }
    }
} 