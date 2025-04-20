package controller;

import dao.ExamDAO;
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
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exam")
public class ExamController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchName = request.getParameter("searchName");
        String examType = request.getParameter("examType");
        HttpSession session = request.getSession(false);
        Learner learner = (Learner) session.getAttribute("learner");
        if( learner == null) {
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
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found.");
                            }
                        } catch (NumberFormatException e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid examID format.");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing examID.");
                    }
                    break;

                case "do":
                    String examIDParam2 = request.getParameter("examID");
                    String eQuesType = request.getParameter("eQuesType");
                    String time = request.getParameter("time");

                    System.out.println("\n=== DEBUG: Starting exam process ===");
                    System.out.println("Request URI: " + request.getRequestURI());
                    System.out.println("Request URL: " + request.getRequestURL());
                    System.out.println("Context Path: " + request.getContextPath());
                    System.out.println("Servlet Path: " + request.getServletPath());
                    System.out.println("Exam ID: " + examIDParam2);
                    System.out.println("Question Type: " + eQuesType);
                    System.out.println("Time: " + time);

                    if (examIDParam2 == null || examIDParam2.isEmpty()) {
                        System.out.println("ERROR: Missing examID parameter");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing examID parameter");
                        return;
                    }

                    try {
                        int examId = Integer.parseInt(examIDParam2);
                        int learnerID = learner.getLearnerID();
                        Exam exam = examDAO.getExamById(examId);

                        if (exam == null) {
                            System.out.println("ERROR: Exam not found with ID: " + examId);
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found");
                            return;
                        }

                        List<Question> questions = examDAO.getQuestionsByExamIdAndType(examId, eQuesType);
                        
                        if (questions == null || questions.isEmpty()) {
                            System.out.println("ERROR: No questions found for exam ID: " + examId + " and type: " + eQuesType);
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No questions found for this exam");
                            return;
                        }

                        System.out.println("\n=== DEBUG: Questions retrieved ===");
                        System.out.println("Number of questions: " + questions.size());
                        for (Question q : questions) {
                            System.out.println("Question ID: " + q.getQuestionID());
                            System.out.println("Question Text: " + q.getQuestionText());
                            System.out.println("Number of answers: " + (q.getAnswers() != null ? q.getAnswers().size() : "null"));
                        }

                        // Set session attributes
                        System.out.println("\n=== DEBUG: Session Before Setting Attributes ===");
                        java.util.Enumeration<String> beforeAttrs = session.getAttributeNames();
                        while (beforeAttrs.hasMoreElements()) {
                            String name = beforeAttrs.nextElement();
                            System.out.println(name + ": " + session.getAttribute(name));
                        }

                        session.setAttribute("skill", eQuesType);
                        session.setAttribute("time", time);
                        session.setAttribute("examID", examId);
                        session.setAttribute("questions", questions);

                        // Debug session after setting attributes  
                        System.out.println("\n=== DEBUG: Session After Setting Attributes ===");
                        System.out.println("skill: " + session.getAttribute("skill"));
                        System.out.println("time: " + session.getAttribute("time")); 
                        System.out.println("examId: " + session.getAttribute("examID"));
                        System.out.println("questions size: " + (questions != null ? questions.size() : "null"));
                        if (questions != null) {
                            System.out.println("\nFirst 3 questions preview:");
                            for (int i = 0; i < Math.min(3, questions.size()); i++) {
                                Question q = questions.get(i);
                                System.out.println("\nQuestion " + (i+1) + ":");
                                System.out.println("ID: " + q.getQuestionID());
                                System.out.println("Text: " + q.getQuestionText());
                                System.out.println("Answers: " + (q.getAnswers() != null ? q.getAnswers().size() : "null"));
                            }
                        }

                        // Set request attributes
                        request.setAttribute("exam", exam);
                        request.setAttribute("questions", questions);
                        request.setAttribute("isCountUp", time == null || time.isEmpty());
                        if (!time.isEmpty()) {
                            request.setAttribute("examDuration", Integer.parseInt(time) * 60);
                        }

                        System.out.println("\n=== DEBUG: Before forwarding to JSP ===");
                        System.out.println("Exam in request: " + request.getAttribute("exam"));
                        System.out.println("Questions in request: " + request.getAttribute("questions"));
                        System.out.println("Is Count Up: " + request.getAttribute("isCountUp"));
                        System.out.println("Exam Duration: " + request.getAttribute("examDuration"));
                        System.out.println("Forward path: /doExam.jsp");

                        ExamTakenDAO examTakenDAO = new ExamTakenDAO();
                        int examTakenID = examTakenDAO.createExamTaken(learnerID, examId);
                        session.setAttribute("examTakenID", examTakenID);
                        session.setAttribute("learnerID", learnerID); // Save learnerID in session

                        // Lưu thông tin vào session để dùng khi nộp bài
                        session.setAttribute("examID", examIDParam2);
                        session.setAttribute("questions", questions);
                        session.setAttribute("time", time);                        System.out.println("ERROR: Invalid examID format: " + examIDParam2);


                        request.getRequestDispatcher("/doExam.jsp").forward(request, response);
                        System.out.println("=== DEBUG: After forwarding to JSP ===");
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid examID format");
                    }
                    break;

//                case "create":
//                    String examName = request.getParameter("examName");
//                    String description = request.getParameter("description");
//                    Integer expertID = Integer.parseInt(request.getParameter("expertID"));
//                    Exam newExam = new Exam(examName, description, expertID);
//                    try {
//                        examDAO.createEmptyExam(newExam);
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                    response.sendRedirect("exam?action=list");
//                    break;

//                case "update":
//                    int id = Integer.parseInt(request.getParameter("id"));
//                    String updatedExamName = request.getParameter("examName");
//                    String updatedDescription = request.getParameter("description");
//                    Exam updatedExam = new Exam(id, updatedExamName, updatedDescription);
//                    examDAO.updateExam(updatedExam);
//                    response.sendRedirect("exam?action=list");
//                    break;

                case "result":
                    showExamResult(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Connection connection = DBConnect.getInstance().getConnection();
//        ExamDAO examDAO = new ExamDAO(connection);
//
//        String action = request.getParameter("action");
//        if (action.equals("create")) {
//            String examName = request.getParameter("examName");
//            String description = request.getParameter("description");
//            Integer expertID = Integer.parseInt(request.getParameter("expertID"));
//            Exam newExam = new Exam(examName, description, expertID);
//            try {
//                examDAO.createEmptyExam(newExam);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            response.sendRedirect("exam?action=list");
//        } else if (action.equals("update")) {
//            int id = Integer.parseInt(request.getParameter("id"));
//            String examName = request.getParameter("examName");
//            String description = request.getParameter("description");
//            Exam updatedExam = new Exam(id, examName, description);
//            examDAO.updateExam(updatedExam);
//            response.sendRedirect("exam?action=list");
//        }
//    }

    private void showExamResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("\n=== DEBUG: Showing exam result ===");
            
            // Lấy examTakenID từ parameter
            String examTakenIDStr = request.getParameter("examTakenID");
            //int examID = Integer.parseInt(request.getParameter("examID"));
            if (examTakenIDStr == null || examTakenIDStr.isEmpty()) {
                System.out.println("ERROR: Missing examTakenID parameter");
                response.sendRedirect("exam?action=list");
                return;
            }

            int examTakenID = Integer.parseInt(examTakenIDStr);
            System.out.println("ExamTakenID: " + examTakenID + " new");
            
            // Lấy thông tin bài thi đã làm từ database
            Connection connection = DBConnect.getInstance().getConnection();
            ExamDAO examDAO = new ExamDAO(connection);
            ExamTaken examTaken = examDAO.getExamTakenById(examTakenID);
            
            if (examTaken == null) {
                System.out.println("ERROR: ExamTaken not found with ID: " + examTakenID);
                response.sendRedirect("exam?action=list");
                return;
            }

            // Lấy thông tin exam gốc
            Exam exam = examDAO.getExamById(3);
            System.out.println("Retrieved exam: " + exam.getExamName());
            
            // Lấy danh sách câu hỏi của bài thi
            List<Question> questions = examDAO.getQuestionsByExamId(3);
            System.out.println("Retrieved " + questions.size() + " questions");
            
            // Set attributes
            request.setAttribute("examTaken", examTaken);
            request.setAttribute("exam", exam);
            request.setAttribute("questions", questions);
            request.setAttribute("totalQuestions", questions.size());
            request.setAttribute("correctAnswers", (int)((examTaken.getFinalMark() / 10.0) * questions.size()));
            request.setAttribute("score", Math.round(examTaken.getFinalMark() * 100.0) / 100.0);
            
            System.out.println("\n=== Result Summary ===");
            System.out.println("Total questions: " + questions.size());
            System.out.println("Correct answers: " + (int)((examTaken.getFinalMark() / 10.0) * questions.size()));
            System.out.println("Final score: " + examTaken.getFinalMark());
            System.out.println("Time taken: " + examTaken.getTimeTaken());
            
            // Forward to result page
            request.getRequestDispatcher("/examResult.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("ERROR in showExamResult: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}