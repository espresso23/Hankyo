package controller.quizlet;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<String, Integer> customTopicCounts = new HashMap<>();
            try {
                customTopics = quizletDAO.getAllTopicsCustomFlashCardByLearnerID(learnerID);
                for (String topic : customTopics) {
                    List<CustomFlashCard> allCards = quizletDAO.getAllCustomFlashCardByTopicAndLeanerID(learnerID, topic);
                    Map<String, List<CustomFlashCard>> separatedCards = quizletDAO.separateCustomFlashCards(allCards, learnerID);
                    
                    // Đếm số lượng card của user
                    int userCardCount = separatedCards.get("userCards").size();
                    customTopicCounts.put(topic, userCardCount);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("customError", "Không thể tải danh sách topic custom: " + e.getMessage());
            }
            request.setAttribute("customTopics", customTopics);
            request.setAttribute("customTopicCounts", customTopicCounts);

            // Favorite Flashcard Topics
            List<String> favoriteTopics = new ArrayList<>();
            try {
                favoriteTopics = dictionaryDAO.getFavoriteListNamesByLearnerID(learnerID);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("favoriteError", "Không thể tải danh sách topic favorite: " + e.getMessage());
            }
            request.setAttribute("favoriteTopics", favoriteTopics);

            // Public Flashcards
            List<CustomFlashCard> publicFlashCards = new ArrayList<>();
            Map<Integer, String> publicLearnerNames = new HashMap<>();
            try {
                publicFlashCards = quizletDAO.getAllPublicCustomFlashCardsExcludeCurrentUser(learnerID);
                publicLearnerNames = quizletDAO.getLearnerFullNameMap(publicFlashCards);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("publicError", "Không thể tải danh sách public flashcards: " + e.getMessage());
            }
            request.setAttribute("publicFlashCards", publicFlashCards);
            request.setAttribute("publicLearnerNames", publicLearnerNames);

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
        } else if ("addFlashcard".equals(action)) {
            String topic = request.getParameter("topic");
            String word = request.getParameter("word");
            String mean = request.getParameter("mean");
            boolean isPublic = "true".equals(request.getParameter("isPublic"));

            QuizletDAO quizletDAO = new QuizletDAO();
            List<String> successMessages = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();

            try {
                CustomFlashCard flashCard = new CustomFlashCard(learnerID, word, mean, topic);
                flashCard.setPublic(isPublic);
                if (quizletDAO.addCustomFlashCard(flashCard)) {
                    successMessages.add("Thêm flashcard thành công: " + word);
                } else {
                    errorMessages.add("Không thể thêm flashcard: " + word);
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessages.add("Lỗi khi thêm flashcard: " + e.getMessage());
            }

            request.setAttribute("successMessages", successMessages);
            request.setAttribute("errorMessages", errorMessages);
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
        } else if ("togglePublic".equals(action)) {
            String cfcidStr = request.getParameter("cfcid");
            String isPublicStr = request.getParameter("isPublic");
            QuizletDAO quizletDAO = new QuizletDAO();
            try {
                int cfcid = Integer.parseInt(cfcidStr);
                boolean isPublic = Boolean.parseBoolean(isPublicStr);
                boolean success = quizletDAO.updateCustomFlashCardPublic(cfcid, isPublic);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": " + success + "}");
            } catch (Exception e) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"count\": 0}");
        }
    }
}