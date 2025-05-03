package controller.quizlet;

import dao.QuizletDAO;
import model.CustomFlashCard;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/customFlashCard")
public class ListCustomFlashCardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        String topic = request.getParameter("topic");
        String type = request.getParameter("type");
        String learnerIDParam = request.getParameter("learnerID");
        int targetLearnerID = learnerIDParam != null ? Integer.parseInt(learnerIDParam) : learnerID;

        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (topic == null) {
            request.setAttribute("error", "Topic parameter is missing");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        QuizletDAO quizletDAO = new QuizletDAO();
        try {
            if ("public".equals(type)) {
                List<CustomFlashCard> publicFlashCards = quizletDAO.getAllCustomFlashCardByTopicAndLeanerID(targetLearnerID, topic);
                Map<Integer, String> learnerNames = quizletDAO.getLearnerFullNameMap(publicFlashCards);
                request.setAttribute("customFlashCards", publicFlashCards);
                request.setAttribute("learnerNames", learnerNames);
                request.setAttribute("type", "public");
                request.getRequestDispatcher("customQuizlet.jsp").forward(request, response);
            } else {
                List<String> listTopic;
                if (topic.equals("favorite")) {
                    listTopic = quizletDAO.getAllFavoriteFlashCardListNameByLearnerID(learnerID);
                    request.setAttribute("type", "favorite");
                } else {
                    listTopic = quizletDAO.getAllTopicsCustomFlashCardByLearnerID(learnerID);
                    request.setAttribute("type", "custom");
                }
                request.setAttribute("listTopic", listTopic);
                request.setAttribute("learnerID", learnerID);
                request.getRequestDispatcher("listCustomFlashCard.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi truy vấn CSDL", e);
        }
    }
}