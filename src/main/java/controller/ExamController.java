package controller;

import dao.ExamDAO;
import model.Exam;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exam")
public class ExamController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Connection connection = DBConnect.getInstance().getConnection();
        ExamDAO examDAO = new ExamDAO(connection);

        if (action == null || action.equals("list")) {
            List<Exam> exams = examDAO.getAllExams();
            request.setAttribute("exams", exams);
            request.getRequestDispatcher("exams.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            //examDAO.deleteExam(id);
            response.sendRedirect("exam?action=list");
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
