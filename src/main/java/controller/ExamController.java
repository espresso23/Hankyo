package controller;

import dao.ExamDAO;
import model.Exam;
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
        if (action == null) action = "list";

        Connection connection = DBConnect.getInstance().getConnection();
        ExamDAO examDAO = new ExamDAO(connection);

        try {
            switch (action) {
                case "list":
                    String searchName = request.getParameter("searchName");
                    String examType = request.getParameter("examType");
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
                    int examID = Integer.parseInt(request.getParameter("examID"));
                    Exam exam = examDAO.getExamById(examID);
                    if (exam != null) {
                        int totalQuestions = examDAO.getTotalQuestionsByExamId(examID);
                        request.setAttribute("exam", exam);
                        request.setAttribute("totalQuestions", totalQuestions);
                        request.getRequestDispatcher("/examDetail.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found");
                    }
                    break;

                case "start":
                    examID = Integer.parseInt(request.getParameter("examID"));
                    String skill = request.getParameter("skill");
                    String time = request.getParameter("time");

                    // Lưu thông tin vào session
                    HttpSession session = request.getSession();
                    session.setAttribute("currentExamID", examID);
                    session.setAttribute("selectedSkill", skill);
                    session.setAttribute("examTime", time);

                    // Chuyển hướng đến trang làm bài
                    response.sendRedirect("doExam.jsp");
                    break;

                case "create":
                    String examName = request.getParameter("examName");
                    String description = request.getParameter("description");
                    Integer expertID = Integer.parseInt(request.getParameter("expertID"));
                    Exam newExam = new Exam(examName, description, expertID);
                    try {
                        examDAO.createEmptyExam(newExam);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    response.sendRedirect("exam?action=list");
                    break;

                case "update":
                    int id = Integer.parseInt(request.getParameter("id"));
                    String updatedExamName = request.getParameter("examName");
                    String updatedDescription = request.getParameter("description");
                    Exam updatedExam = new Exam(id, updatedExamName, updatedDescription);
                    examDAO.updateExam(updatedExam);
                    response.sendRedirect("exam?action=list");
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = DBConnect.getInstance().getConnection();
        ExamDAO examDAO = new ExamDAO(connection);

        String action = request.getParameter("action");
        if (action.equals("create")) {
            String examName = request.getParameter("examName");
            String description = request.getParameter("description");
            Integer expertID = Integer.parseInt(request.getParameter("expertID"));
            Exam newExam = new Exam(examName, description, expertID);
            try {
                examDAO.createEmptyExam(newExam);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect("exam?action=list");
        } else if (action.equals("update")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String examName = request.getParameter("examName");
            String description = request.getParameter("description");
            Exam updatedExam = new Exam(id, examName, description);
            examDAO.updateExam(updatedExam);
            response.sendRedirect("exam?action=list");
        }
    }
}