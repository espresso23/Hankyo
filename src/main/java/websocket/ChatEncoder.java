package websocket;

import com.google.gson.Gson;
import filter.BadwordsFilter;
import model.Chat;
import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ChatEncoder implements Encoder.Text<Chat> {
    private static final Gson gson = new Gson();

    @Override
    public String encode(Chat message) throws EncodeException {
        try {
            // Use BadwordsFilter to censor the message text before encoding to JSON
            String censoredMessage = BadwordsFilter.censorBadWords(message.getMessage());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", message.getUserID());
            jsonObject.put("message", censoredMessage);
            jsonObject.put("fullName", message.getFullName());
            if (message.getSendAt() != null) {
                jsonObject.put("sendAt", message.getSendAt().getTime());
            }
            if (message.getPictureSend() != null) {
                jsonObject.put("pictureSend", message.getPictureSend());
            }

            return jsonObject.toString();
        } catch (Exception e) {
            throw new EncodeException(message, "Failed to encode chat message", e);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
