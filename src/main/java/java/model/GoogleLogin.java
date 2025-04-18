package java.model;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constants.Constants;
import dao.UserDAO;
import model.User;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.sql.SQLException;

public class GoogleLogin {

    public static String getToken(String code) throws IOException {
        // Call API to get token
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE)
                        .build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public static User getUserInfo(final String accessToken) throws IOException {
        String link = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        UserDAO userDAO = new UserDAO();
        // Create User object
        User user = new User();
        String socialID = jsonObject.get("sub").getAsString();
        user.setSocialID(socialID);
        user.setFullName(jsonObject.get("name").getAsString());
        user.setGmail(jsonObject.get("email").getAsString());
        String avatar = jsonObject.has("picture") && jsonObject.get("picture") != null ? jsonObject.get("picture").getAsString() : "avatar_df.png";
        user.setAvatar(avatar);


        // Check if user exists
        try {
            if (!userDAO.userExistsSocial(user.getSocialID())) {
                // Save new user
                boolean isSave = userDAO.saveUserSocialMedia(user);
                if (isSave)
                    user = userDAO.getUserBySocialID(user.getSocialID());
                user.displayInfo();
            } else {
                user = userDAO.getUserBySocialID(user.getSocialID());
            }
        } catch (SQLException e) {
            throw new IOException("Database error: " + e.getMessage(), e);
        }

        return user;
    }
}