package controller;

import com.google.gson.Gson;
import dao.DictionaryDAO;
import dao.QuizletDAO;
import model.FavoriteFlashCard;
import model.SystemFlashCard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/flashCard")
public class ChooseQuizletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số từ URL
        String topic = request.getParameter("topic");
        String nameOfList = request.getParameter("nameOfList"); // Có thể null nếu không dùng cho SystemFlashCard
        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        QuizletDAO quizletDAO = new QuizletDAO();
        DictionaryDAO dictionaryDAO = new DictionaryDAO();

        // Nếu không có topic, hiển thị trang chọn topic
        if (topic == null || topic.isEmpty()) {
            System.out.println("No topic provided, showing topic selection page");
            // Lấy danh sách tất cả các topic từ hệ thống (giả sử QuizletDAO có phương thức này)
            List<String> listTopic = null; // Cần thêm phương thức này trong QuizletDAO
            try {
                listTopic = quizletDAO.getAllTopics();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (listTopic == null || listTopic.isEmpty()) {
                request.setAttribute("error", "No topics available");
            } else {
                request.setAttribute("listTopic", listTopic);
            }
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
            return;
        }

        // Nếu có topic, hiển thị flashcard tương ứng
        Gson gson = new Gson();
        if (topic.equals("favorite")) {
            if (learnerID == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            System.out.println("Fetching favorite flashcards for learnerID: " + learnerID + ", nameOfList: " + nameOfList);
            // Nếu không có nameOfList, mặc định là "favorite" hoặc xử lý lỗi
            String listName = (nameOfList != null && !nameOfList.isEmpty()) ? nameOfList : "favorite";
            List<FavoriteFlashCard> flashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID, listName);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No favorite flashcards found for list: " + listName);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
                request.setAttribute("nameOfList", listName);
            }
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
        } else {
            List<SystemFlashCard> flashCards = quizletDAO.getAllSystemFlashCardByTopic(topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No flashcards found for topic: " + topic);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
            }
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy topic và nameOfList từ form (nếu có)
        String topic = request.getParameter("topic");
        String nameOfList = request.getParameter("nameOfList");

        if (topic != null && !topic.isEmpty()) {
            // Chuyển hướng sang doGet với topic và nameOfList (nếu có)
            String redirectUrl = "flashCard?topic=" + topic;
            if (nameOfList != null && !nameOfList.isEmpty()) {
                redirectUrl += "&nameOfList=" + nameOfList;
            }
            response.sendRedirect(redirectUrl);
        } else {
            // Nếu không có topic, quay lại trang chọn
            response.sendRedirect("flashCard");
        }
    }
}