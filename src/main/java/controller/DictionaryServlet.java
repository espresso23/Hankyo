package controller;

import dao.DictionaryDAO;
import model.Dictionary;
import model.FavoriteFlashCard;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/dictionary")
public class DictionaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DictionaryDAO dictionaryDAO = new DictionaryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<FavoriteFlashCard> favoriteFlashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID);
        List<Dictionary> dictionaryList = dictionaryDAO.getAllDictionary();

        request.setAttribute("FavoriteFlashCardList", favoriteFlashCards);
        request.setAttribute("dictionaryList", dictionaryList);
        request.getRequestDispatcher("dictionary.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        if (learnerID == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"error\": \"User not logged in\"}");
            out.flush();
            return;
        }

        String action = request.getParameter("action");
        String wordIDStr = request.getParameter("wordID");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (wordIDStr == null || action == null) {
            out.print("{\"success\": false, \"error\": \"Missing parameters\"}");
            out.flush();
            return;
        }

        try {
            int wordID = Integer.parseInt(wordIDStr);

            if ("addFavoriteFlashCard".equals(action)) {
                Dictionary dictionary = dictionaryDAO.getDictionaryByWordID(wordID);
                if (dictionary != null) {
                    FavoriteFlashCard fc = new FavoriteFlashCard(dictionary, new Learner(learnerID)); // Không truyền FCID
                    boolean success = dictionaryDAO.addFavoriteFlashCard(fc);
                    out.print("{\"success\": " + success + "}");
                } else {
                    out.print("{\"success\": false, \"error\": \"Word not found\"}");
                }
            } else if ("removeFavoriteFlashCard".equals(action)) {
                boolean success = dictionaryDAO.removeFavoriteFlashCard(learnerID, wordID);
                out.print("{\"success\": " + success + "}");
            } else {
                out.print("{\"success\": false, \"error\": \"Invalid action\"}");
            }
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"error\": \"Invalid wordID\"}");
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}