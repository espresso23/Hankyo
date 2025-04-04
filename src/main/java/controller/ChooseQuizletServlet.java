package controller;

import dao.DictionaryDAO;
import dao.QuizletDAO;
import model.FavoriteFlashCard;
import model.SystemFlashCard;
import com.google.gson.Gson;

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy topic từ session
        String topic = (String) request.getSession().getAttribute("topic");
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");


        // Kiểm tra topic có tồn tại không
        if (topic == null || topic.isEmpty()) {
            response.sendRedirect("quizlet.jsp");
            return;
        }

        // Lấy danh sách flashcard từ database
        Gson gson = new Gson();
        QuizletDAO quizletDAO = new QuizletDAO();
        DictionaryDAO dictionaryDAO = new DictionaryDAO();
        if (topic.equals("favorite")) {
            System.out.println("favorite" + learnerID);
            List<FavoriteFlashCard> flashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID);



            String flashCardsJson = gson.toJson(flashCards);

            // Gửi dữ liệu về JSP
            request.setAttribute("flashCardsJson", flashCardsJson);
            request.setAttribute("flashCards", flashCards);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
        } else {
            List<SystemFlashCard> flashCards = quizletDAO.getAllSystemFlashCardByTopic(topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("flashCards", null);
                request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
                return;
            }

            // Chuyển danh sách flashCards thành JSON
            String flashCardsJson = gson.toJson(flashCards);

            // Gửi dữ liệu về JSP
            request.setAttribute("flashCardsJson", flashCardsJson);
            request.setAttribute("flashCards", flashCards);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy topic từ form
        String topic = request.getParameter("topic");

        // Kiểm tra topic hợp lệ
        if (topic != null && !topic.isEmpty()) {
            // Lưu topic vào session
            request.getSession().setAttribute("topic", topic);
            // Chuyển hướng sang doGet để hiển thị flashcard
            response.sendRedirect("quizlet");
        } else {
            // Nếu topic không hợp lệ, quay lại trang chọn
            response.sendRedirect("quizlet.jsp");
        }
    }
}