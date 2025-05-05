package controller.quizlet;

import com.google.gson.Gson;
import dao.QuizletDAO;
import dao.VipUserDAO;
import model.CustomFlashCard;
import model.FavoriteFlashCard;
import model.SystemFlashCard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/memory-game")
public class MemoryGameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private QuizletDAO quizletDAO;
    private VipUserDAO vipUserDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        quizletDAO = new QuizletDAO();
        vipUserDAO = new VipUserDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String topic = request.getParameter("topic");
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");
        Integer userID = (Integer) session.getAttribute("userID");

        // Check if user is VIP
        boolean isUserVip = false;
        if (userID != null) {
            try {
                isUserVip = vipUserDAO.isVipUser(userID);
            } catch (Exception e) {
                System.err.println("Error checking VIP status: " + e.getMessage());
            }
        }
        request.setAttribute("isUserVip", isUserVip);

        if (topic == null || type == null) {
            request.setAttribute("error", "Missing topic or type parameter");
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
            return;
        }

        try {
            List<String> words = new ArrayList<>();
            List<String> meanings = new ArrayList<>();
            List<Integer> pairIds = new ArrayList<>(); // To track pair IDs

            switch (type) {
                case "system":
                    List<SystemFlashCard> systemCards = quizletDAO.getRandomSystemFlashCards(topic, 10);
                    if (systemCards.size() < 10) {
                        request.setAttribute("error", "Not enough system flashcards available for this topic");
                        request.getRequestDispatcher("quizlet.jsp").forward(request, response);
                        return;
                    }
                    for (int i = 0; i < systemCards.size(); i++) {
                        words.add(systemCards.get(i).getDictionary().getWord());
                        meanings.add(systemCards.get(i).getDictionary().getMean());
                        pairIds.add(i); // Assign a unique pair ID
                    }
                    break;

                case "custom":
                    if (learnerID == null) {
                        response.sendRedirect("login.jsp");
                        return;
                    }
                    List<CustomFlashCard> customCards = quizletDAO.getRandomCustomFlashCards(topic, learnerID, 10);
                    if (customCards.size() < 10) {
                        request.setAttribute("error", "Not enough custom flashcards available for this topic");
                        request.getRequestDispatcher("quizlet.jsp").forward(request, response);
                        return;
                    }
                    for (int i = 0; i < customCards.size(); i++) {
                        words.add(customCards.get(i).getWord());
                        meanings.add(customCards.get(i).getMean());
                        pairIds.add(i); // Assign a unique pair ID
                    }
                    break;

                case "favorite":
                    if (learnerID == null) {
                        response.sendRedirect("login.jsp");
                        return;
                    }
                    List<FavoriteFlashCard> favoriteCards = quizletDAO.getRandomFavoriteFlashCards(topic, learnerID, 10);
                    if (favoriteCards.size() < 10) {
                        request.setAttribute("error", "Not enough favorite flashcards available for this list");
                        request.getRequestDispatcher("quizlet.jsp").forward(request, response);
                        return;
                    }
                    for (int i = 0; i < favoriteCards.size(); i++) {
                        words.add(favoriteCards.get(i).getDictionary().getWord());
                        meanings.add(favoriteCards.get(i).getDictionary().getMean());
                        pairIds.add(i); // Assign a unique pair ID
                    }
                    break;

                default:
                    request.setAttribute("error", "Invalid flashcard type");
                    request.getRequestDispatcher("quizlet.jsp").forward(request, response);
                    return;
            }

            // Shuffle meanings and pairIds together to maintain correspondence
            List<Integer> shuffledIndices = new ArrayList<>();
            for (int i = 0; i < meanings.size(); i++) {
                shuffledIndices.add(i);
            }
            Collections.shuffle(shuffledIndices);

            List<String> shuffledMeanings = new ArrayList<>();
            List<Integer> shuffledPairIds = new ArrayList<>();
            for (int index : shuffledIndices) {
                shuffledMeanings.add(meanings.get(index));
                shuffledPairIds.add(pairIds.get(index));
            }

            // Set attributes for JSP
            request.setAttribute("words", words);
            request.setAttribute("meanings", shuffledMeanings);
            request.setAttribute("pairIds", pairIds); // Original pair IDs for words
            request.setAttribute("shuffledPairIds", shuffledPairIds); // Shuffled pair IDs for meanings
            request.setAttribute("topic", topic);
            request.setAttribute("type", type);

            // Forward to memory game JSP
            request.getRequestDispatcher("memory-game.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error loading flashcards: " + e.getMessage());
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
        }
    }
}