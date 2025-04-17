package controller;

import dao.QuizletDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/quizlet")
public class QuizletServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");
        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        QuizletDAO quizletDAO = new QuizletDAO();
        try {
            List<String> listTopic = quizletDAO.getAllTopics();
            request.setAttribute("listTopic", listTopic);
            request.setAttribute("learnerID", learnerID);
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
