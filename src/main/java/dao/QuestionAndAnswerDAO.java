package dao;

import model.Question;
import util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionAndAnswerDAO {
    private Connection connection;

    public QuestionAndAnswerDAO(Connection connection) {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public boolean addQuestionMultiple(List<Question> questions) {
        String query = "Insert into Question(questionText, questionImg, audio_file, questionType) values(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Question question : questions) {
                preparedStatement.setString(1, question.getQuestionText());
                preparedStatement.setString(2, question.getQuestionImage());
                preparedStatement.setString(3, question.getAudioFile());
                preparedStatement.setString(4, question.getQuestionType());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

