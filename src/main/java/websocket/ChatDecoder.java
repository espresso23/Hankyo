package websocket;

import com.google.gson.Gson;
import filter.BadwordsFilter;
import model.Chat;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ChatDecoder implements Decoder.Text<Chat> {
    private static final Gson gson = new Gson();

    @Override
    public Chat decode(String s) throws DecodeException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            int userID = jsonObject.getInt("userID");
            String message = jsonObject.getString("message");
            String fullName = jsonObject.getString("fullName");

            // Censor the message text before creating the Chat instance
            String censoredMessage = BadwordsFilter.censorBadWords(message);

            // Create and return a new Chat instance
            return new Chat(userID, censoredMessage, fullName);
        } catch (JSONException e) {
            throw new DecodeException(s, "Failed to decode JSON object", e);
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