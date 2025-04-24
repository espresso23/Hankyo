package websocket;

import com.google.gson.Gson;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/comment-ws/{examId}")
public class CommentWebSocket {
    private static final ConcurrentHashMap<String, Set<Session>> examSessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, @PathParam("examId") String examId) {
        examSessions.computeIfAbsent(examId, k -> Collections.synchronizedSet(new HashSet<>()))
                   .add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("examId") String examId) {
        Set<Session> examSet = examSessions.get(examId);
        if (examSet != null) {
            examSet.remove(session);
            if (examSet.isEmpty()) {
                examSessions.remove(examId);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Log error
    }

    public static void broadcastComment(String examId, Object comment) {
        Set<Session> examSet = examSessions.get(examId);
        if (examSet != null) {
            String jsonComment = gson.toJson(comment);
            examSet.forEach(session -> {
                try {
                    session.getBasicRemote().sendText(jsonComment);
                } catch (IOException e) {
                    // Log error
                }
            });
        }
    }
} 