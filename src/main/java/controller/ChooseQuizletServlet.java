package controller;

import com.google.gson.Gson;
import dao.DictionaryDAO;
import dao.QuizletDAO;
import model.CustomFlashCard;
import model.FavoriteFlashCard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/flashCard")
public class ChooseQuizletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");
        String topic = request.getParameter("topic");
        String type = request.getParameter("type"); // favorite, custom, hoặc hệ thống
        Gson gson = new Gson();
        DictionaryDAO dictionaryDAO = new DictionaryDAO();
        QuizletDAO quizletDAO = new QuizletDAO();

        if (topic == null || topic.isEmpty()) {
            request.setAttribute("error", "No topic specified.");
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
            return;
        }

        if ("favorite".equals(type)) {
            if (learnerID == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            System.out.println("Fetching favorite flashcards for learnerID: " + learnerID + ", nameOfList: " + topic);

            List<FavoriteFlashCard> flashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID, topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No favorite flashcards found for list: " + topic);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
                request.setAttribute("nameOfList", topic);
            }
        } else if ("custom".equals(type)) {
            if (learnerID == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            System.out.println("Fetching custom flashcards for learnerID: " + learnerID + ", nameOfList: " + topic);

            List<CustomFlashCard> flashCards = quizletDAO.getAllCustomFlashCardByTopicAndLeanerID(learnerID, topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No custom flashcards found for list: " + topic);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
                request.setAttribute("nameOfList", topic);
            }
        }
        request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}