package controller;

import dao.DictionaryDAO;
import dao.QuizletDAO;
import model.CustomFlashCard;
import model.FavoriteFlashCard;
import model.Learner;
import model.SystemFlashCard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/quizlet")
public class QuizletServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();

        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        QuizletDAO quizletDAO = new QuizletDAO();
        DictionaryDAO dictionaryDAO = new DictionaryDAO();

        try {
            // System Flashcard Topics
            List<String> systemTopics = new ArrayList<>();
            try {
                systemTopics = quizletDAO.getAllTopics();
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("systemError", "Không thể tải danh sách topic system: " + e.getMessage());
            }
            request.setAttribute("systemTopics", systemTopics);

            // Custom Flashcard Topics
            List<String> customTopics = new ArrayList<>();
            try {
                customTopics = quizletDAO.getAllTopicsCustomFlashCardByLearnerID(learnerID);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("customError", "Không thể tải danh sách topic custom: " + e.getMessage());
            }
            request.setAttribute("customTopics", customTopics);

            // Favorite Flashcard Topics
            List<String> favoriteTopics = new ArrayList<>();
            try {
                favoriteTopics = dictionaryDAO.getFavoriteListNamesByLearnerID(learnerID);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("favoriteError", "Không thể tải danh sách topic favorite: " + e.getMessage());
            }
            request.setAttribute("favoriteTopics", favoriteTopics);

            request.setAttribute("learnerID", learnerID);
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        if (learnerID == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"count\": 0}");
            return;
        }

        String action = request.getParameter("action");
        if ("getFlashCardCount".equals(action)) {
            String type = request.getParameter("type");
            String topic = request.getParameter("topic");
            QuizletDAO quizletDAO = new QuizletDAO();
            DictionaryDAO dictionaryDAO = new DictionaryDAO();
            int count = 0;

            try {
                if ("system".equals(type)) {
                    List<SystemFlashCard> systemFlashCards = quizletDAO.getAllSystemFlashCardByTopic(topic);
                    count = systemFlashCards.size();
                } else if ("custom".equals(type)) {
                    List<CustomFlashCard> customFlashCards = quizletDAO.getAllCustomFlashCardByTopicAndLeanerID(learnerID, topic);
                    count = customFlashCards.size();
                } else if ("favorite".equals(type)) {
                    List<FavoriteFlashCard> favoriteFlashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID, topic);
                    count = favoriteFlashCards.size();
                }
                response.getWriter().write(String.format("{\"count\": %d}", count));
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("{\"count\": 0}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"count\": 0}");
        }
    }
}