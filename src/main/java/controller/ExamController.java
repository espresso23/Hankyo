//
//        package controller;
//
//import dao.ExamDAO;
//import dao.ExamTakenDAO;
//import dao.ExamResultDAO;
//import model.*;
//import util.DBConnect;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.Connection;
//import java.util.List;
//import java.util.logging.Logger;
//
//@WebServlet("/exam")
//public class ExamController extends HttpServlet {
//    private static final Logger LOGGER = Logger.getLogger(ExamController.class.getName());
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        String searchName = request.getParameter("searchName");
//        String examType = request.getParameter("examType");
//        HttpSession session = request.getSession(false);
//        Learner learner = (Learner) session.getAttribute("learner");
//        if (learner == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//        if (action == null) action = "list";
//
//        Connection connection = DBConnect.getInstance().getConnection();
//        ExamDAO examDAO = new ExamDAO(connection);
//
//        try {
//            switch (action) {
//                case "list":
//                    List<Exam> exams;
//
//                    if (searchName != null && !searchName.isEmpty()) {
//                        exams = examDAO.searchExamsByName(searchName);
//                    } else if (examType != null && !examType.isEmpty()) {
//                        exams = examDAO.getExamsByType(examType);
//                    } else {
//                        exams = examDAO.getAllExams();
//                    }
//
//                    request.setAttribute("exams", exams);
//                    request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
//                    break;
//
//                case "details":
//                    String examIDParam = request.getParameter("examID");
//                    if (examIDParam != null && !examIDParam.isEmpty()) {
//                        try {
//                            int examID = Integer.parseInt(examIDParam);
//                            Exam exam = examDAO.getExamById(examID);
//                            if (exam != null) {
//                                int totalQuestions = examDAO.getTotalQuestionsByExamId(examID);
//                                request.setAttribute("exam", exam);
//                                request.setAttribute("totalQuestions", totalQuestions);
//                                request.getRequestDispatcher("/examDetail.jsp").forward(request, response);
//                            } else {
//                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đề thi.");
//                            }
//                        } catch (NumberFormatException e) {
//                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng examID không hợp lệ.");
//                        }
//                    } else {
//                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu examID.");
//                    }
//                    break;
//
//                case "do":
//                    String examIDParam2 = request.getParameter("examID");
//                    String eQuesType = request.getParameter("eQuesType");
//                    String time = request.getParameter("time");
//
//                    if (examIDParam2 == null || examIDParam2.isEmpty()) {
//                        LOGGER.warning("Thiếu tham số examID");
//                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số examID");
//                        return;
//                    }
//
//                    try {
//                        int examId = Integer.parseInt(examIDParam2);
//                        int learnerID = learner.getLearnerID();
//                        Exam exam = examDAO.getExamById(examId);
//
//                        if (exam == null) {
//                            LOGGER.warning("Không tìm thấy đề thi với ID: " + examId);
//                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đề thi");
//                            return;
//                        }
//
//                        List<Question> questions = examDAO.getQuestionsByExamIdAndType(examId, eQuesType);
//
//                        if (questions == null || questions.isEmpty()) {
//                            LOGGER.warning("Không tìm thấy câu hỏi cho đề thi ID: " + examId + " và loại: " + eQuesType);
//                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy câu hỏi cho đề thi này");
//                            return;
//                        }
//
//                        // Thiết lập các thuộc tính session
//                        session.setAttribute("skill", eQuesType);
//                        session.setAttribute("time", time);
//                        session.setAttribute("examID", examId);
//                        session.setAttribute("questions", questions);
//
//                        // Thiết lập các thuộc tính request
//                        request.setAttribute("exam", exam);
//                        request.setAttribute("questions", questions);
//                        request.setAttribute("isCountUp", time == null || time.isEmpty());
//                        if (!time.isEmpty()) {
//                            request.setAttribute("examDuration", Integer.parseInt(time) * 60);
//                        }
//
//                        ExamTakenDAO examTakenDAO = new ExamTakenDAO();
//                        int examTakenID = examTakenDAO.createExamTaken(learnerID, examId);
//                        session.setAttribute("examTakenID", examTakenID);
//                        session.setAttribute("learnerID", learnerID);
//
//                        request.getRequestDispatcher("/doExam.jsp").forward(request, response);
//                    } catch (Exception e) {
//                        LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi bắt đầu bài thi: " + e.getMessage(), e);
//                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng examID không hợp lệ");
//                    }
//                    break;
//
//                case "result":
//                    showExamResult(request, response);
//                    break;
//
//                default:
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
//                    break;
//            }
//        } catch (Exception e) {
//            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi xử lý yêu cầu: " + e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private void showExamResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            String examTakenIDStr = request.getParameter("examTakenID");
//            if (examTakenIDStr == null || examTakenIDStr.isEmpty()) {
//                LOGGER.warning("Thiếu tham số examTakenID");
//                response.sendRedirect("exam?action=list");
//                return;
//            }
//
//            int examTakenID = Integer.parseInt(examTakenIDStr);
//            String skill = (String) request.getSession().getAttribute("skill");
//            String time = (String) request.getSession().getAttribute("time");
//            String examIDStr = (String) request.getSession().getAttribute("examID");
//            int examID = Integer.parseInt(examIDStr);
//
//            Connection connection = DBConnect.getInstance().getConnection();
//            ExamDAO examDAO = new ExamDAO(connection);
//            ExamTakenDAO examTakenDAO = new ExamTakenDAO();
//            ExamResultDAO examResultDAO = new ExamResultDAO();
//
//            ExamTaken examTaken = examTakenDAO.getExamTakenById(examTakenID);
//            if (examTaken == null) {
//                LOGGER.warning("Không tìm thấy ExamTaken với ID: " + examTakenID);
//                response.sendRedirect("exam?action=list");
//                return;
//            }
//
//            Exam exam = examDAO.getExamById(examID);
//            if (exam == null) {
//                LOGGER.warning("Không tìm thấy đề thi với ID: " + examID);
//                response.sendRedirect("exam?action=list");
//                return;
//            }
//
//            List<Question> questions = examDAO.getQuestionsByExamIdAndType(examID, skill);
//            List<ExamResult> examResults = examResultDAO.getExamResultsByExamTakenId(examTakenID);
//
//            request.setAttribute("exam", exam);
//            request.setAttribute("skill", skill != null ? skill : "Full");
//            request.setAttribute("time", time);
//            request.setAttribute("examTaken", examTaken);
//            request.setAttribute("questions", questions);
//            request.setAttribute("examResults", examResults);
//            request.setAttribute("totalQuestions", questions.size());
//            request.setAttribute("correctAnswers", (int) ((examTaken.getFinalMark() / 10.0) * questions.size()));
//            request.setAttribute("score", examTaken.getFinalMark() * 10.0); // Chuyển đổi thành phần trăm
//            request.setAttribute("doneQues", examTaken.getDoneQues());
//            request.setAttribute("skipQues", examTaken.getSkipQues());
//
//            request.getRequestDispatcher("/examResult.jsp").forward(request, response);
//        } catch (NumberFormatException e) {
//            LOGGER.log(java.util.logging.Level.SEVERE, "Định dạng examTakenID không hợp lệ: " + e.getMessage(), e);
//            response.sendRedirect("error.jsp");
//        } catch (Exception e) {
//            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi trong showExamResult: " + e.getMessage(), e);
//            response.sendRedirect("error.jsp");
//        }
//    }
//}
package controller;

import dao.ExamDAO;
import dao.ExamTakenDAO;
import dao.ExamResultDAO;
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
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/exam")
public class ExamController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ExamController.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchName = request.getParameter("searchName");
        String examType = request.getParameter("examType");
        HttpSession session = request.getSession(false);
        Learner learner = (Learner) session.getAttribute("learner");
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if (action == null) action = "list";

        Connection connection = DBConnect.getInstance().getConnection();
        ExamDAO examDAO = new ExamDAO(connection);

        try {
            switch (action) {
                case "list":
                    List<Exam> exams;

                    if (searchName != null && !searchName.isEmpty()) {
                        exams = examDAO.searchExamsByName(searchName);
                    } else if (examType != null && !examType.isEmpty()) {
                        exams = examDAO.getExamsByType(examType);
                    } else {
                        exams = examDAO.getAllExams();
                    }

                    request.setAttribute("exams", exams);
                    request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                    break;

                case "details":
                    String examIDParam = request.getParameter("examID");
                    if (examIDParam != null && !examIDParam.isEmpty()) {
                        try {
                            int examID = Integer.parseInt(examIDParam);
                            Exam exam = examDAO.getExamById(examID);
                            if (exam != null) {
                                int totalQuestions = examDAO.getTotalQuestionsByExamId(examID);
                                request.setAttribute("exam", exam);
                                request.setAttribute("totalQuestions", totalQuestions);
                                request.getRequestDispatcher("/examDetail.jsp").forward(request, response);
                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đề thi.");
                            }
                        } catch (NumberFormatException e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng examID không hợp lệ.");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu examID.");
                    }
                    break;

                case "do":
                    String examIDParam2 = request.getParameter("examID");
                    String eQuesType = request.getParameter("eQuesType");
                    String time = request.getParameter("time");

                    if (examIDParam2 == null || examIDParam2.isEmpty()) {
                        LOGGER.warning("Thiếu tham số examID");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số examID");
                        return;
                    }

                    try {
                        int examId = Integer.parseInt(examIDParam2);
                        int learnerID = learner.getLearnerID();
                        Exam exam = examDAO.getExamById(examId);

                        if (exam == null) {
                            LOGGER.warning("Không tìm thấy đề thi với ID: " + examId);
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đề thi");
                            return;
                        }

                        List<Question> questions = examDAO.getQuestionsByExamIdAndType(examId, eQuesType);

                        if (questions == null || questions.isEmpty()) {
                            LOGGER.warning("Không tìm thấy câu hỏi cho đề thi ID: " + examId + " và loại: " + eQuesType);
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy câu hỏi cho đề thi này");
                            return;
                        }

                        session.setAttribute("skill", eQuesType);
                        session.setAttribute("time", time);
                        session.setAttribute("examID", String.valueOf(examId)); // Lưu examID dưới dạng String
                        session.setAttribute("questions", questions);

                        request.setAttribute("exam", exam);
                        request.setAttribute("questions", questions);
                        request.setAttribute("isCountUp", time == null || time.isEmpty());
                        if (!time.isEmpty()) {
                            request.setAttribute("examDuration", Integer.parseInt(time) * 60);
                        }

                        ExamTakenDAO examTakenDAO = new ExamTakenDAO();
                        int examTakenID = examTakenDAO.createExamTaken(learnerID, examId);
                        session.setAttribute("examTakenID", examTakenID);
                        session.setAttribute("learnerID", learnerID);

                        request.getRequestDispatcher("/doExam.jsp").forward(request, response);
                    } catch (Exception e) {
                        LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi bắt đầu bài thi: " + e.getMessage(), e);
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng examID không hợp lệ");
                    }
                    break;

                case "result":
                    showExamResult(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi xử lý yêu cầu: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void showExamResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String examTakenIDStr = request.getParameter("examTakenID");
            if (examTakenIDStr == null || examTakenIDStr.isEmpty()) {
                LOGGER.warning("Thiếu tham số examTakenID");
                response.sendRedirect("exam?action=list");
                return;
            }

            int examTakenID;
            try {
                examTakenID = Integer.parseInt(examTakenIDStr);
            } catch (NumberFormatException e) {
                LOGGER.warning("Định dạng examTakenID không hợp lệ: " + examTakenIDStr);
                request.setAttribute("errorMessage", "Định dạng examTakenID không hợp lệ: " + examTakenIDStr);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            String skill = (String) request.getSession().getAttribute("skill");
            String time = (String) request.getSession().getAttribute("time");
            String examIDStr = (String) request.getSession().getAttribute("examID");

            // Kiểm tra examIDStr
            if (examIDStr == null || examIDStr.isEmpty()) {
                LOGGER.warning("examID không có trong session cho examTakenID: " + examTakenID);
                request.setAttribute("errorMessage", "examID không có trong session cho examTakenID: " + examTakenID);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int examID;
            try {
                examID = Integer.parseInt(examIDStr);
            } catch (NumberFormatException e) {
                LOGGER.warning("Định dạng examID không hợp lệ: " + examIDStr);
                request.setAttribute("errorMessage", "Định dạng examID không hợp lệ: " + examIDStr);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            Connection connection = DBConnect.getInstance().getConnection();
            ExamDAO examDAO = new ExamDAO(connection);
            ExamTakenDAO examTakenDAO = new ExamTakenDAO();
            ExamResultDAO examResultDAO = new ExamResultDAO();

            ExamTaken examTaken = examTakenDAO.getExamTakenById(examTakenID);
            if (examTaken == null) {
                LOGGER.warning("Không tìm thấy ExamTaken với ID: " + examTakenID);
                request.setAttribute("errorMessage", "Không tìm thấy ExamTaken với ID: " + examTakenID);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            Exam exam = examDAO.getExamById(examID);
            if (exam == null) {
                LOGGER.warning("Không tìm thấy đề thi với ID: " + examID);
                request.setAttribute("errorMessage", "Không tìm thấy đề thi với ID: " + examID);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            List<Question> questions = examDAO.getQuestionsByExamIdAndType(examID, skill != null ? skill : "Full");
            List<ExamResult> examResults = examResultDAO.getExamResultsByExamTakenId(examTakenID);

            if (questions == null || questions.isEmpty()) {
                LOGGER.severe("No questions found for examID: " + examID + ", skill: " + (skill != null ? skill : "Full"));
                request.setAttribute("errorMessage", "Không tìm thấy câu hỏi cho đề thi ID: " + examID + ", skill: " + (skill != null ? skill : "Full"));
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            if (examResults == null || examResults.size() != questions.size()) {
                LOGGER.warning("Mismatch between questions and examResults: questions=" + questions.size() + ", examResults=" + (examResults != null ? examResults.size() : 0));
                request.setAttribute("errorMessage", "Số lượng câu hỏi và kết quả không khớp: questions=" + questions.size() + ", examResults=" + (examResults != null ? examResults.size() : 0));
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int correctAnswers = 0;
            for (ExamResult result : examResults) {
                if (result.isAnswerIsCorrect()) {
                    correctAnswers++;
                }
            }

            request.setAttribute("exam", exam);
            request.setAttribute("skill", skill != null ? skill : "Full");
            request.setAttribute("time", time);
            request.setAttribute("examTaken", examTaken);
            request.setAttribute("questions", questions);
            request.setAttribute("examResults", examResults);
            request.setAttribute("totalQuestions", questions.size());
            request.setAttribute("correctAnswers", correctAnswers);
            request.setAttribute("score", examTaken.getFinalMark() * 10.0);
            request.setAttribute("doneQues", examTaken.getDoneQues());
            request.setAttribute("skipQues", examTaken.getSkipQues());

            request.getRequestDispatcher("/examResult.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi trong showExamResult: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Lỗi trong showExamResult: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}