package servlet;

import dao.ExamResultDAO;
import dao.ExamTakenDAO;
import model.*;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/submitExam")
public class SubmitExamServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SubmitExamServlet.class.getName());
    private ExamTakenDAO examTakenDAO;
    private ExamResultDAO examResultDAO;

    @Override
    public void init() throws ServletException {
        examTakenDAO = new ExamTakenDAO();
        examResultDAO = new ExamResultDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Validate session and learner
        if (session == null || session.getAttribute("learnerID") == null) {
            LOGGER.warning("Invalid session or learnerID not found");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Retrieve session data
            int learnerID = (Integer) session.getAttribute("learnerID");
            int examID = Integer.parseInt(String.valueOf(session.getAttribute("examID")));
            List<Question> questions = (List<Question>) session.getAttribute("questions");

            if (questions == null || questions.isEmpty()) {
                throw new ServletException("No questions found for the exam");
            }

            LOGGER.info(String.format("Processing exam submission - learnerID: %d, examID: %d", learnerID, examID));

            // Create ExamTaken record
            int examTakenID = examTakenDAO.createExamTaken(learnerID, examID);
            LOGGER.info("Created ExamTaken ID: " + examTakenID);

            // Process time taken
            int timeTakenSeconds = parseTimeTaken(request.getParameter("timeTaken"));
            Time timeTaken = new Time(timeTakenSeconds * 1000L);

            // Process answers
            ExamResultSummary summary = processAnswers(
                    questions,
                    request,
                    learnerID,
                    examID,
                    examTakenID
            );

            // Update ExamTaken with final results
            examTakenDAO.updateExamTaken(
                    examTakenID,
                    summary.finalMark,
                    timeTaken,
                    summary.doneQues,
                    summary.skipQues
            );

            LOGGER.info(String.format(
                    "Exam completed - ID: %d, Score: %.2f, Correct: %d, Done: %d, Skipped: %d",
                    examTakenID, summary.finalMark, summary.correctAnswers, summary.doneQues, summary.skipQues
            ));

            // Redirect to results page
            response.sendRedirect("exam?action=result&examTakenID=" + examTakenID);

        } catch (NumberFormatException e) {
            handleError(request, response, "Invalid input format: " + e.getMessage(), e);
        } catch (ServletException e) {
            handleError(request, response, "Exam processing error: " + e.getMessage(), e);
        } catch (Exception e) {
            handleError(request, response, "Unexpected error during exam submission: " + e.getMessage(), e);
        }
    }

    private int parseTimeTaken(String timeTakenParam) {
        try {
            return Integer.parseInt(timeTakenParam);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid timeTaken parameter: " + timeTakenParam);
            return 0;
        }
    }

    private ExamResultSummary processAnswers(
            List<Question> questions,
            HttpServletRequest request,
            int learnerID,
            int examID,
            int examTakenID
    ) {
        int skipQues = 0;
        int doneQues = 0;
        int correctAnswers = 0;
        float totalPossibleMark = 0;
        float earnedMark = 0;

        for (Question question : questions) {
            totalPossibleMark += question.getQuestionMark();
            String answerLabel = request.getParameter("question_" + question.getQuestionID());

            boolean isCorrect = false;
            if (answerLabel == null || answerLabel.trim().isEmpty()) {
                skipQues++;
                answerLabel = "";
            } else {
                doneQues++;
                isCorrect = checkAnswerCorrectness(question, answerLabel);
                if (isCorrect) {
                    earnedMark += question.getQuestionMark();
                    correctAnswers++;
                }
            }

            saveExamResult(
                    question,
                    learnerID,
                    examID,
                    examTakenID,
                    answerLabel,
                    isCorrect
            );
        }

        float finalMark = totalPossibleMark > 0 ? (earnedMark / totalPossibleMark) * 10 : 0;
        return new ExamResultSummary(finalMark, correctAnswers, doneQues, skipQues);
    }

    private boolean checkAnswerCorrectness(Question question, String answerLabel) {
        for (Answer answer : question.getAnswers()) {
            if (answer.getOptionLabel().equals(answerLabel) && answer.isCorrect()) {
                LOGGER.fine("Correct answer for question " + question.getQuestionID());
                return true;
            }
        }
        return false;
    }

    private void saveExamResult(
            Question question,
            int learnerID,
            int examID,
            int examTakenID,
            String answerLabel,
            boolean isCorrect
    ) {
        try {
            // Retrieve eQuestID from Exam_Question table
            int eQuestID = getEQuestID(question.getQuestionID(), examID);
            if (eQuestID == -1) {
                throw new SQLException("No Exam_Question entry found for questionID: " + question.getQuestionID() + " and examID: " + examID);
            }

            ExamResult examResult = new ExamResult();
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.seteQuestID(eQuestID);
            examResult.setExamQuestion(examQuestion);
            examResult.setLearner(new Learner(learnerID));
            examResult.setExamTakenID(examTakenID);
            examResult.setQuestion(question);
            examResult.setAnswerLabel(answerLabel);
            examResult.setAnswerIsCorrect(isCorrect);
            examResult.setMark(isCorrect ? (float) question.getQuestionMark() : 0.0f);
            examResult.setDateTaken(LocalDateTime.now());

            examResultDAO.insertExamResult(examResult);
            LOGGER.fine("Saved result for question " + question.getQuestionID());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving result for question " + question.getQuestionID(), e);
            throw new RuntimeException("Failed to save exam result", e);
        }
    }

    private int getEQuestID(int questionID, int examID) throws SQLException {
        String sql = "SELECT eQuestID FROM Exam_Question WHERE questionID = ? AND examID = ?";
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionID);
            stmt.setInt(2, examID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("eQuestID");
            }
        }
        return -1; // Indicate no matching record found
    }

    private void handleError(
            HttpServletRequest request,
            HttpServletResponse response,
            String errorMessage,
            Exception e
    ) throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, errorMessage, e);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }

    private static class ExamResultSummary {
        final float finalMark;
        final int correctAnswers;
        final int doneQues;
        final int skipQues;

        ExamResultSummary(float finalMark, int correctAnswers, int doneQues, int skipQues) {
            this.finalMark = finalMark;
            this.correctAnswers = correctAnswers;
            this.doneQues = doneQues;
            this.skipQues = skipQues;
        }
    }
}