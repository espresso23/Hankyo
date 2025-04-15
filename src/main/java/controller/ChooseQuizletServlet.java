package controller;

import com.google.gson.Gson;
import dao.DictionaryDAO;
import dao.QuizletDAO;
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
        String type = request.getParameter("type");
        Gson gson = new Gson();
        DictionaryDAO dictionaryDAO = new DictionaryDAO();
        QuizletDAO quizletDAO = new QuizletDAO();

        if (topic == null || topic.isEmpty()) {
            request.setAttribute("error", "No topic specified.");
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
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
                request.setAttribute("type", "favorite");
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
                request.setAttribute("type", "custom");
            }
        } else {
            System.out.println("Fetching system flashcards for topic: " + topic);

            List<SystemFlashCard> flashCards = quizletDAO.getAllSystemFlashCardByTopic(topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No system flashcards found for topic: " + topic);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
                request.setAttribute("type", "system");
            }
        }

        request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        QuizletDAO dao = new QuizletDAO();

        if (action != null) {
            switch (action) {
                case "delete":
                    String cfcidStr = request.getParameter("cfcid");
                    try {
                        int cfcid = Integer.parseInt(cfcidStr);
                        boolean success = dao.deleteCustomFlashCard(cfcid);
                        if (success) {
                            response.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    break;

                case "update":
                    cfcidStr = request.getParameter("cfcid");
                    String word = request.getParameter("word");
                    String mean = request.getParameter("mean");
                    try {
                        int cfcid = Integer.parseInt(cfcidStr);
                        if (word != null && !word.trim().isEmpty() && mean != null && !mean.trim().isEmpty()) {
                            boolean success = dao.updateCustomFlashCard(cfcid, word, mean);
                            if (success) {
                                response.setStatus(HttpServletResponse.SC_OK);
                            } else {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    break;

                default:
                    doGet(request, response);
                    break;
            }
        } else {
            doGet(request, response);
        }
    }
}