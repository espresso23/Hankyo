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

@WebServlet(name = "DictionaryServlet", urlPatterns = {"/dictionary"})
public class DictionaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DictionaryDAO dictionaryDAO;

    @Override
    public void init() throws ServletException {
        dictionaryDAO = new DictionaryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String word = request.getParameter("word");
        String mean = request.getParameter("mean");
        String type = request.getParameter("type");

        List<Dictionary> dictionaryList;
        
        if ((word != null && !word.trim().isEmpty()) || 
            (mean != null && !mean.trim().isEmpty()) || 
            (type != null && !type.trim().isEmpty())) {
            dictionaryList = dictionaryDAO.advancedSearch(word, type, mean);
        } else {
            dictionaryList = dictionaryDAO.getAllDictionary();
        }

        List<String> favoriteListNames = dictionaryDAO.getFavoriteListNamesByLearnerID(learnerID);

        request.setAttribute("dictionaryList", dictionaryList);
        request.setAttribute("favoriteListNames", favoriteListNames);
        request.getRequestDispatcher("dictionary.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        
        if (learner == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"error\": \"User not logged in\"}");
            out.flush();
            return;
        }

        String action = request.getParameter("action");
        String wordIDStr = request.getParameter("wordID");
        String nameOfList = request.getParameter("nameOfList");
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
                if (nameOfList == null || nameOfList.trim().isEmpty()) {
                    out.print("{\"success\": false, \"error\": \"List name is required\"}");
                    out.flush();
                    return;
                }
                Dictionary dictionary = dictionaryDAO.getDictionaryByWordID(wordID);
                if (dictionary != null) {
                    FavoriteFlashCard fc = new FavoriteFlashCard(dictionary, learner);
                    fc.setNameOfList(nameOfList.trim());
                    boolean success = dictionaryDAO.addFavoriteFlashCard(fc);
                    out.print("{\"success\": " + success + "}");
                } else {
                    out.print("{\"success\": false, \"error\": \"Word not found\"}");
                }
            } else if ("removeFavoriteFlashCard".equals(action)) {
                if (nameOfList == null || nameOfList.trim().isEmpty()) {
                    out.print("{\"success\": false, \"error\": \"List name is required\"}");
                    out.flush();
                    return;
                }
                boolean success = dictionaryDAO.removeFavoriteFlashCard(learnerID, wordID, nameOfList.trim());
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
