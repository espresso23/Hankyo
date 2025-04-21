//package servlet;
//
//import dao.ExamResultDAO;
//import dao.ExamTakenDAO;
//import model.*;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.Time;
//import java.util.List;
//import java.util.logging.Logger;
//import java.util.logging.Level;
//import java.time.LocalDateTime;
//import java.sql.SQLException;
//
//@WebServlet("/submitExam")
//public class SubmitExamServlet extends HttpServlet {
//    private static final Logger LOGGER = Logger.getLogger(SubmitExamServlet.class.getName());
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("learnerID") == null) {
//            LOGGER.warning("Invalid session or learnerID not found");
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        try {
//            // Lấy thông tin từ session
//            int learnerID = (int) session.getAttribute("learnerID");
//            int examID = Integer.parseInt(String.valueOf(session.getAttribute("examID")));
//            List<Question> questions = (List<Question>) session.getAttribute("questions");
//
//            LOGGER.info("Processing exam submission for learnerID: " + learnerID + ", examID: " + examID);
//
//            if (questions == null || questions.isEmpty()) {
//                throw new ServletException("Không tìm thấy câu hỏi trong bài thi");
//            }
//
//            // Khởi tạo ExamTaken
//            ExamTakenDAO takenDAO = new ExamTakenDAO();
//            int examTakenID = takenDAO.createExamTaken(learnerID,examID);
//            LOGGER.info("Created new ExamTaken with ID: " + examTakenID);
//
//            // Khởi tạo các biến thống kê
//            int skipQues = 0;
//            int doneQues = 0;
//            int correctAnswers = 0;
//            float totalPossibleMark = 0;
//            float earnedMark = 0;
//
//            // Xử lý thời gian làm bài
//            int timeTakenSeconds;
//            try {
//                timeTakenSeconds = Integer.parseInt(request.getParameter("timeTaken"));
//            } catch (NumberFormatException e) {
//                LOGGER.warning("Invalid timeTaken parameter: " + request.getParameter("timeTaken"));
//                timeTakenSeconds = 0;
//            }
//            Time timeTaken = new Time(timeTakenSeconds * 1000L);
//
//            ExamResultDAO resultDAO = new ExamResultDAO();
//
//            // Xử lý từng câu trả lời
//            for (Question question : questions) {
//                totalPossibleMark += question.getQuestionMark();
//                String answerLabel = request.getParameter("question_" + question.getQuestionID());
//                LOGGER.fine("Processing question " + question.getQuestionID() + " with answer: " + answerLabel);
//
//                boolean isCorrect = false;
//
//                if (answerLabel == null || answerLabel.trim().isEmpty()) {
//                    skipQues++;
//                    answerLabel = "";
//                    LOGGER.fine("Question " + question.getQuestionID() + " was skipped");
//                } else {
//                    doneQues++;
//                    for (Answer answer : question.getAnswers()) {
//                        if (answer.getOptionLabel().equals(answerLabel) && answer.isCorrect()) {
//                            isCorrect = true;
//                            earnedMark += question.getQuestionMark();
//                            correctAnswers++;
//                            LOGGER.fine("Correct answer for question " + question.getQuestionID());
//                            break;
//                        }
//                    }
//                }
//
//                try {
//                    int eQuestID = ExamResultDAO.getEQuestID(examID, question.getQuestionID());
//                    // Lưu kết quả từng câu
//                     ExamResult examResult = new ExamResult();
//                    examResult.setExamTakenID(examTakenID);
//                    examResult.setExam(new Exam(examID));
//                    examResult.setAnswerLabel(answerLabel);
//                    examResult.setAnswerIsCorrect(isCorrect);
//                    examResult.setMark((float) (isCorrect ? question.getQuestionMark() : 0.0));
//                    examResult.setDateTaken(LocalDateTime.now());
//                    examResult.setLearner(new Learner(learnerID));
//                    examResult.setQuestion(question);
//                    examResult.seteQuesID(eQuestID);
//
//                    resultDAO.insertExamResult(examResult);
//
//                    LOGGER.fine("Saved result for question " + question.getQuestionID());
//                } catch (Exception e) {
//                    LOGGER.log(Level.SEVERE, "Error saving result for question " + question.getQuestionID(), e);
//                    throw e;
//                }
//            }
//
//            // Tính điểm cuối cùng (thang điểm 10)
//            float finalMark = (earnedMark / totalPossibleMark) * 10;
//
//            try {
//                // Cập nhật ExamTaken
//                ExamTakenDAO examTakenDAO = new ExamTakenDAO();
//                examTakenDAO.updateExamTaken(examTakenID, finalMark, timeTaken, doneQues, skipQues);
//
//                // Log kết quả chi tiết
//                LOGGER.info(String.format("Exam completed successfully - ID: %d, Score: %.2f/10, Correct: %d/%d, Done: %d, Skipped: %d, Time: %s",
//                    examTakenID, finalMark, correctAnswers, questions.size(), doneQues, skipQues, timeTaken));
//
//                // Chuyển đến trang kết quả
//                response.sendRedirect("exam?action=result&examTakenID=" + examTakenID);
//
//            } catch (SQLException e) {
//                LOGGER.log(Level.SEVERE, "Error updating exam results for examTakenID: " + examTakenID, e);
//                request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật kết quả bài thi: " + e.getMessage());
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//            }
//
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error processing exam submission", e);
//            request.setAttribute("errorMessage", "Có lỗi xảy ra khi nộp bài thi: " + e.getMessage());
//            request.getRequestDispatcher("error.jsp").forward(request, response);
//        }
//    }
//}
package servlet;

import dao.ExamResultDAO;
import dao.ExamTakenDAO;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.time.LocalDateTime;
import java.sql.SQLException;

@WebServlet("/submitExam")
public class SubmitExamServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SubmitExamServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("learnerID") == null) {
            LOGGER.warning("Invalid session or learnerID not found");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int learnerID = (int) session.getAttribute("learnerID");
            String examIDStr = String.valueOf(session.getAttribute("examID"));
            if (examIDStr == null || examIDStr.isEmpty()) {
                LOGGER.severe("examID not found in session for learnerID: " + learnerID);
                throw new ServletException("Không tìm thấy examID trong session");
            }
            int examID;
            try {
                examID = Integer.parseInt(examIDStr);
            } catch (NumberFormatException e) {
                LOGGER.severe("Invalid examID format in session: " + examIDStr);
                throw new ServletException("Định dạng examID không hợp lệ trong session");
            }

            // Lấy examTakenID từ session thay vì tạo mới
            Integer examTakenIDObj = (Integer) session.getAttribute("examTakenID");
            if (examTakenIDObj == null) {
                LOGGER.severe("examTakenID not found in session for learnerID: " + learnerID);
                throw new ServletException("Không tìm thấy examTakenID trong session");
            }
            int examTakenID = examTakenIDObj;

            List<Question> questions = (List<Question>) session.getAttribute("questions");
            LOGGER.info("Processing exam submission for learnerID: " + learnerID + ", examID: " + examID + ", examTakenID: " + examTakenID);

            if (questions == null || questions.isEmpty()) {
                throw new ServletException("Không tìm thấy câu hỏi trong bài thi");
            }

            int skipQues = 0;
            int doneQues = 0;
            int correctAnswers = 0;
            float totalPossibleMark = 0;
            float earnedMark = 0;

            int timeTakenSeconds;
            try {
                timeTakenSeconds = Integer.parseInt(request.getParameter("timeTaken"));
                if (timeTakenSeconds < 0) {
                    LOGGER.warning("Negative timeTakenSeconds: " + timeTakenSeconds + ", setting to 0");
                    timeTakenSeconds = 0;
                }
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid timeTaken parameter: " + request.getParameter("timeTaken"));
                timeTakenSeconds = 0;
            }
            Time timeTaken = new Time(timeTakenSeconds * 1000L);

            ExamResultDAO resultDAO = new ExamResultDAO();

            for (Question question : questions) {
                if (question == null || question.getAnswers() == null || question.getAnswers().isEmpty()) {
                    LOGGER.warning("Invalid question or answers for questionID: " + (question != null ? question.getQuestionID() : "null"));
                    continue;
                }

                totalPossibleMark += question.getQuestionMark();
                String answerLabel = request.getParameter("question_" + question.getQuestionID());
                LOGGER.info("Question ID: " + question.getQuestionID() + ", answerLabel: " + answerLabel);

                boolean isCorrect = false;

                if (answerLabel == null || answerLabel.trim().isEmpty()) {
                    skipQues++;
                    answerLabel = "";
                    LOGGER.fine("Question " + question.getQuestionID() + " was skipped");
                } else {
                    doneQues++;
                    for (Answer answer : question.getAnswers()) {
                        if (answer.getOptionLabel().equals(answerLabel) && answer.isCorrect()) {
                            isCorrect = true;
                            earnedMark += question.getQuestionMark();
                            correctAnswers++;
                            LOGGER.fine("Correct answer for question " + question.getQuestionID());
                            break;
                        }
                    }
                }

                try {
                    int eQuestID = ExamResultDAO.getEQuestID(examID, question.getQuestionID());
                    ExamResult examResult = new ExamResult();
                    examResult.setExamTakenID(examTakenID);
                    examResult.setExam(new Exam(examID));
                    examResult.setAnswerLabel(answerLabel);
                    examResult.setAnswerIsCorrect(isCorrect);
                    examResult.setMark((float) (isCorrect ? question.getQuestionMark() : 0.0));
                    examResult.setDateTaken(LocalDateTime.now());
                    examResult.setLearner(new Learner(learnerID));
                    examResult.setQuestion(question);
                    examResult.seteQuesID(eQuestID);

                    resultDAO.insertExamResult(examResult);
                    LOGGER.fine("Saved result for question " + question.getQuestionID());
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error saving result for question " + question.getQuestionID() + ": " + e.getMessage(), e);
                    throw e;
                }
            }

            if (doneQues == 0) {
                LOGGER.warning("No questions answered by learnerID: " + learnerID);
                request.setAttribute("errorMessage", "Bạn chưa trả lời câu hỏi nào. Vui lòng chọn ít nhất một đáp án.");
                request.getRequestDispatcher("doExam.jsp").forward(request, response);
                return;
            }

            if (doneQues + skipQues != questions.size()) {
                LOGGER.warning("Invalid question counts: doneQues=" + doneQues + ", skipQues=" + skipQues + ", totalQuestions=" + questions.size());
            }

            float finalMark = totalPossibleMark > 0 ? (earnedMark / totalPossibleMark) * 10 : 0;
            LOGGER.info("Before updating ExamTaken: finalMark=" + finalMark + ", timeTaken=" + timeTaken + ", doneQues=" + doneQues + ", skipQues=" + skipQues);

            try {
                ExamTakenDAO examTakenDAO = new ExamTakenDAO();
                examTakenDAO.updateExamTaken(examTakenID, finalMark, timeTaken, doneQues, skipQues);

                LOGGER.info(String.format("Exam completed successfully - ID: %d, Score: %.2f/10, Correct: %d/%d, Done: %d, Skipped: %d, Time: %s",
                        examTakenID, finalMark, correctAnswers, questions.size(), doneQues, skipQues, timeTaken));

                // Đảm bảo các giá trị session cần thiết được lưu lại
                session.setAttribute("examID", String.valueOf(examID)); // Lưu lại examID dưới dạng String
                session.setAttribute("skill", session.getAttribute("skill")); // Lưu lại skill
                session.setAttribute("time", session.getAttribute("time")); // Lưu lại time
                session.setAttribute("questions", questions); // Lưu lại questions

                response.sendRedirect("exam?action=result&examTakenID=" + examTakenID);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error updating exam results for examTakenID: " + examTakenID + ": " + e.getMessage(), e);
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật kết quả bài thi: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing exam submission: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi nộp bài thi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}