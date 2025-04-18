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

@WebServlet("/customFlashCard")
public class ListCustomFlashCardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");
        String topic = request.getParameter("topic"); // Lấy topic từ URL

        if (learnerID == null) {
            response.sendRedirect("login.jsp"); // Chuyển hướng nếu chưa đăng nhập
            return;
        }

        if (topic == null) {
            request.setAttribute("error", "Topic parameter is missing");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        QuizletDAO quizletDAO = new QuizletDAO();
        try {
            List<String> listTopic;
            if (topic.equals("favorite")) {
                listTopic = quizletDAO.getAllFavoriteFlashCardListNameByLearnerID(learnerID);
                String type = "favorite";
                request.setAttribute("type", type);
            } else {
                listTopic = quizletDAO.getAllTopicsCustomFlashCardByLearnerID(learnerID);
                String type = "custom";
                request.setAttribute("type", type);
            }
            request.setAttribute("listTopic", listTopic);
            request.setAttribute("learnerID", learnerID);
            request.getRequestDispatcher("listCustomFlashCard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi truy vấn CSDL", e);
        }
    }
}