package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constants.Constants;
import dao.UserDAO;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public class FacebookLogin implements Serializable {
    private static final long serialVersionUID = 1L;

    public static String getToken(String code) throws IOException {
        String response = Request.Post(Constants.FACEBOOK_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.FACEBOOK_CLIENT_ID)
                        .add("client_secret", Constants.FACEBOOK_CLIENT_SECRET)
                        .add("redirect_uri", Constants.FACEBOOK_REDIRECT_URI)
                        .add("code", code)
                        .build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public static User getUserInfo(final String accessToken) throws IOException {
        String link = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        UserDAO userDAO = new UserDAO();
        // Create User object
        User user = new User();
        String socialID = jsonObject.get("id").getAsString();
        user.setSocialID(socialID);
        user.setFullName(jsonObject.get("name").getAsString());
        user.setGmail(jsonObject.get("email").getAsString());

        // Get profile picture URL
        JsonObject pictureObject = jsonObject.getAsJsonObject("picture").getAsJsonObject("data");

        // Check if the URL is null and assign a default value if so
        String avatarUrl = pictureObject.has("url") && pictureObject.get("url") != null
                ? pictureObject.get("url").getAsString()
                : "avatar_df.jpg";

        user.setAvatar(avatarUrl);

        // Check if user exists
        try {
            if (!userDAO.userExistsSocial(user.getSocialID())) {
                // Save new user
                boolean isSave = userDAO.saveUserSocialMedia(user);
                if (isSave)
                    user = userDAO.getUserBySocialID(user.getSocialID());
            } else {
                user = userDAO.getUserBySocialID(user.getSocialID());
            }
        } catch (SQLException e) {
            throw new IOException("Database error: " + e.getMessage(), e);
        }

        return user;
    }
}
