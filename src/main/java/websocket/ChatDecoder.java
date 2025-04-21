package websocket;

import com.google.gson.Gson;
import filter.BadwordsFilter;
import model.Chat;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatDecoder implements Decoder.Text<Chat> {
    private static final Gson gson = new Gson();
    private static final Logger LOGGER = Logger.getLogger(ChatDecoder.class.getName());

    @Override
    public Chat decode(String s) throws DecodeException {
        LOGGER.info("Starting to decode message: " + s);
        try {
            JSONObject json = new JSONObject(s);
            LOGGER.info("JSON parsed successfully");
            
            int userID = json.getInt("userID");
            String message = json.getString("message");
            String fullName = json.getString("fullName");
            String pictureSend = json.optString("pictureSend", null);
            
            LOGGER.info("Extracted values - userID: " + userID + 
                       ", fullName: " + fullName + 
                       ", message length: " + (message != null ? message.length() : 0) +
                       ", pictureSend length: " + (pictureSend != null ? pictureSend.length() : 0));
            
            // Censor bad words in the message
            message = BadwordsFilter.censorBadWords(message);
            
            // Use the correct constructor for new messages
            Chat chat = new Chat(userID, message, fullName, pictureSend);
            LOGGER.info("Chat object created successfully");
            return chat;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error decoding message: " + e.getMessage(), e);
            throw new DecodeException(s, "Error decoding message", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s != null && !s.trim().isEmpty();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}