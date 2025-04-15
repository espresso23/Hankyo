package dao;

import model.Expert;
import util.DBConnect;

import java.sql.Connection;
import java.util.List;

public class FeedbackDAO {
    private Connection connection;

    public FeedbackDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }


    public Expert getExpertFeedback(int expertID){
        String sql = "";

        return null;
    }
}
