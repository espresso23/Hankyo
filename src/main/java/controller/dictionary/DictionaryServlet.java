package controller.dictionary;

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
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<FavoriteFlashCard> favoriteFlashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID, "favorite"); // Giả sử mặc định là "favorite"
        List<Dictionary> dictionaryList = dictionaryDAO.getAllDictionary();
        List<String> favoriteListNames = dictionaryDAO.getFavoriteListNamesByLearnerID(learnerID);

        request.setAttribute("FavoriteFlashCardList", favoriteFlashCards);
        request.setAttribute("dictionaryList", dictionaryList);
        request.setAttribute("favoriteListNames", favoriteListNames); // Gửi danh sách nameOfList
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
        String nameOfList = request.getParameter("nameOfList"); // Lấy nameOfList từ yêu cầu
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (wordIDStr == null || action == null || (nameOfList == null && "addFavoriteFlashCard".equals(action))) {
            out.print("{\"success\": false, \"error\": \"Missing parameters\"}");
            out.flush();
            return;
        }

        try {
            int wordID = Integer.parseInt(wordIDStr);

            if ("addFavoriteFlashCard".equals(action)) {
                Dictionary dictionary = dictionaryDAO.getDictionaryByWordID(wordID);
                if (dictionary != null) {
                    FavoriteFlashCard fc = new FavoriteFlashCard(dictionary, new Learner(learnerID));
                    fc.setNameOfList(nameOfList); // Đặt nameOfList
                    boolean success = dictionaryDAO.addFavoriteFlashCard(fc);
                    out.print("{\"success\": " + success + "}");
                } else {
                    out.print("{\"success\": false, \"error\": \"Word not found\"}");
                }
            } else if ("removeFavoriteFlashCard".equals(action)) {
                boolean success = dictionaryDAO.removeFavoriteFlashCard(learnerID, wordID, nameOfList != null ? nameOfList : "favorite");
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