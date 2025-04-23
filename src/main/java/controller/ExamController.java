package controller;

import dao.ExamDAO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
                                session.setAttribute("exam", exam);
                                session.setAttribute("totalQuestions", examDAO.getTotalQuestionsByExamId(examID));
                                request.getRequestDispatcher("/examDetail.jsp").forward(request, response);
                            } else {
                                request.setAttribute("error", "Không tìm thấy đề thi");
                                request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.warning("Định dạng examID không hợp lệ: " + examIDParam);
                            request.setAttribute("error", "Định dạng ID không hợp lệ");
                            request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("error", "Thiếu ID đề thi");
                        request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                    }
                    break;

                case "do":
                    String examIDParam2 = request.getParameter("examID");
                    String eQuesType = request.getParameter("eQuesType");
                    String time = request.getParameter("time");

                    if (examIDParam2 == null || examIDParam2.isEmpty()) {
                        request.setAttribute("error", "Thiếu thông tin đề thi");
                        request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                        return;
                    }

                    try {
                        int examId = Integer.parseInt(examIDParam2);
                        int learnerID = learner.getLearnerID();
                        Exam exam = examDAO.getExamById(examId);

                        if (exam == null) {
                            request.setAttribute("error", "Không tìm thấy đề thi");
                            request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                            return;
                        }

                        List<Question> questions = examDAO.getQuestionsByExamIdAndType(examId, eQuesType);

                        if (questions == null || questions.isEmpty()) {
                            request.setAttribute("error", "Không tìm thấy câu hỏi cho đề thi này");
                            request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                            return;
                        }

                        int timeInMinutes = 60;
                        if (time != null && !time.isEmpty()) {
                            try {
                                timeInMinutes = Integer.parseInt(time);
                            } catch (NumberFormatException e) {
                                LOGGER.warning("Định dạng thời gian không hợp lệ: " + time);
                            }
                        }

                        session.setAttribute("skill", eQuesType);
                        session.setAttribute("time", String.valueOf(timeInMinutes));
                        session.setAttribute("examID", String.valueOf(examId));
                        session.setAttribute("questions", questions);

                        request.setAttribute("exam", exam);
                        request.setAttribute("questions", questions);
                        request.setAttribute("isCountUp", timeInMinutes == 0);
                        if (timeInMinutes != 0) {
                            request.setAttribute("examDuration", timeInMinutes * 60);
                        }

                        ExamTakenDAO examTakenDAO = new ExamTakenDAO();
                        int examTakenID = examTakenDAO.createExamTaken(learnerID, examId, timeInMinutes);
                        session.setAttribute("examTakenID", examTakenID);
                        session.setAttribute("learnerID", learnerID);

                        request.getRequestDispatcher("/doExam.jsp").forward(request, response);
                    } catch (Exception e) {
                        LOGGER.severe("Lỗi khi bắt đầu bài thi: " + e.getMessage());
                        request.setAttribute("error", "Không thể bắt đầu bài thi");
                        request.getRequestDispatcher("/examLibrary.jsp").forward(request, response);
                    }
                    break;

                case "result":
                    showExamResult(request, response);
                    break;

                case "create":
                    request.setAttribute("exam", new Exam());
                    request.getRequestDispatcher("/createExam.jsp").forward(request, response);
                    break;

                case "getExamLibrary":
                    if (searchName != null && !searchName.isEmpty()) {
                        exams = examDAO.searchExamsByName(searchName);
                    } else if (examType != null && !examType.isEmpty()) {
                        exams = examDAO.getExamsByType(examType);
                    } else {
                        exams = examDAO.getAllExams();
                    }
                    request.setAttribute("exams", exams);
                    request.getRequestDispatcher("/examManagement.jsp").forward(request, response);
                    break;

                case "edit":
                    String editExamIDParam = request.getParameter("examID");
                    if (editExamIDParam != null && !editExamIDParam.isEmpty()) {
                        try {
                            int examID = Integer.parseInt(editExamIDParam);
                            Exam exam = examDAO.getExamById(examID);
                            if (exam != null) {
                                request.setAttribute("exam", exam);
                                request.getRequestDispatcher("/editExam.jsp").forward(request, response);
                            } else {
                                redirectWithMessage(response, "exam?action=getExamLibrary", "Không tìm thấy đề thi");
                            }
                        } catch (NumberFormatException e) {
                            redirectWithMessage(response, "exam?action=getExamLibrary", "Định dạng ID không hợp lệ");
                        }
                    } else {
                        redirectWithMessage(response, "exam?action=getExamLibrary", "Thiếu ID đề thi");
                    }
                    break;

                case "delete":
                    String deleteExamIDParam = request.getParameter("examID");
                    if (deleteExamIDParam != null && !deleteExamIDParam.isEmpty()) {
                        try {
                            int examID = Integer.parseInt(deleteExamIDParam);
                            boolean deleted = examDAO.deleteExam(examID);
                            if (deleted) {
                                redirectWithMessage(response, "exam?action=getExamLibrary", "Xóa đề thi thành công");
                            } else {
                                redirectWithMessage(response, "exam?action=getExamLibrary", "Không thể xóa đề thi");
                            }
                        } catch (NumberFormatException e) {
                            redirectWithMessage(response, "exam?action=getExamLibrary", "Định dạng ID không hợp lệ");
                        }
                    } else {
                        redirectWithMessage(response, "exam?action=getExamLibrary", "Thiếu ID đề thi");
                    }
                    break;

                default:
                    redirectWithMessage(response, "exam?action=getExamLibrary", "Hành động không hợp lệ");
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi xử lý yêu cầu: " + e.getMessage());
            redirectWithMessage(response, "exam?action=getExamLibrary", "Đã xảy ra lỗi: " + e.getMessage());
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
            String time = request.getParameter("time");
            String examIDStr = (String) request.getSession().getAttribute("examID");
            System.out.println(examIDStr + "      ----    " + examTakenIDStr);//done

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

            List<ExamResult> examResults = examResultDAO.getExamResultsByExamTakenId(examTakenID, skill);

            if (questions == null || questions.isEmpty()) {
                LOGGER.severe("No questions found for examID: " + examID + ", skill: " + (skill != null ? skill : "Full"));
                request.setAttribute("errorMessage", "Không tìm thấy câu hỏi cho đề thi ID: " + examID + ", skill: " + (skill != null ? skill : "Full"));
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            if (examResults == null || examResults.size() != questions.size()) {
                LOGGER.warning("Mismatch between questions and examResults: questions=" + questions.size() + ", examResults=" + (examResults != null ? examResults.size() : 0));
                request.setAttribute("errorMessage", "Số lượng câu hỏi và kết quả không khớp: questions=" + questions.size() + ", examResults=" + (examResults != null ? examResults.size() : 0));
               // request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int correctAnswers = 0;
            for (ExamResult result : examResults) {
                if (result.isAnswerIsCorrect()) {
                    correctAnswers++;
                }
            }
HttpSession session = request.getSession();
            session.setAttribute("exam", exam);
            session.setAttribute("skill", skill != null ? skill : "Full");
            session.setAttribute("time", time);
            session.setAttribute("examTaken", examTaken);
            session.setAttribute("questions", questions);
            session.setAttribute("examResults", examResults);
            session.setAttribute("totalQuestions", questions.size());
            session.setAttribute("correctAnswers", correctAnswers);
            session.setAttribute("score", examTaken.getFinalMark() * 10.0);
            session.setAttribute("doneQues", examTaken.getDoneQues());
            session.setAttribute("skipQues", examTaken.getSkipQues());

            request.getRequestDispatcher("/examResult.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi trong showExamResult: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Lỗi trong showExamResult: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void redirectWithMessage(HttpServletResponse response, String url, String message) throws IOException {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
        response.sendRedirect(url + (url.contains("?") ? "&" : "?") + "message=" + encodedMessage);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        if (action == null) action = "list";

        Connection connection = DBConnect.getInstance().getConnection();
        ExamDAO examDAO = new ExamDAO(connection);

        try {
            switch (action) {
                case "create":
                    String examName = req.getParameter("examName");
                    String examType = req.getParameter("examType");
                    String examDescription = req.getParameter("examDescription");
                    
                    if (examName == null || examName.isEmpty() || 
                        examType == null || examType.isEmpty() || 
                        examDescription == null || examDescription.isEmpty()) {
                        req.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
                        req.getRequestDispatcher("/examManagement.jsp").forward(req, resp);
                        return;
                    }

                    Exam newExam = new Exam();
                    newExam.setExamName(examName);
                    newExam.setExamType(examType);
                    newExam.setExamDescription(examDescription);
                    newExam.setStatus("active");

                    boolean created = examDAO.addExam(newExam);
                    if (created) {
                        redirectWithMessage(resp, "exam?action=getExamLibrary", "Tạo đề thi thành công");
                    } else {
                        req.setAttribute("error", "Không thể tạo đề thi");
                        req.getRequestDispatcher("/examManagement.jsp").forward(req, resp);
                    }
                    break;

                case "update":
                    String updateExamIDParam = req.getParameter("examID");
                    if (updateExamIDParam != null && !updateExamIDParam.isEmpty()) {
                        try {
                            int examID = Integer.parseInt(updateExamIDParam);
                            String updatedName = req.getParameter("examName");
                            String updatedType = req.getParameter("examType");
                            String updatedDescription = req.getParameter("examDescription");
                            String updatedStatus = req.getParameter("status");

                            if (updatedName == null || updatedName.isEmpty() || 
                                updatedType == null || updatedType.isEmpty() || 
                                updatedDescription == null || updatedDescription.isEmpty()) {
                                req.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
                                req.getRequestDispatcher("/editExam.jsp").forward(req, resp);
                                return;
                            }

                            Exam examToUpdate = new Exam();
                            examToUpdate.setExamID(examID);
                            examToUpdate.setExamName(updatedName);
                            examToUpdate.setExamType(updatedType);
                            examToUpdate.setExamDescription(updatedDescription);
                            examToUpdate.setStatus(updatedStatus != null ? updatedStatus : "active");

                            boolean updated = examDAO.updateExam(examToUpdate);
                            if (updated) {
                                redirectWithMessage(resp, "exam?action=getExamLibrary", "Cập nhật đề thi thành công");
                            } else {
                                req.setAttribute("error", "Không thể cập nhật đề thi");
                                req.getRequestDispatcher("/editExam.jsp").forward(req, resp);
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.warning("Định dạng examID không hợp lệ: " + updateExamIDParam);
                            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng examID không hợp lệ.");
                        }
                    } else {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu examID.");
                    }
                    break;

                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi xử lý yêu cầu: " + e.getMessage(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}