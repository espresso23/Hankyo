package controller.quizlet;

import dao.QuizletDAO;
import model.CustomFlashCard;
import model.Learner;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ajaxAddFlashCard")
public class AddFlashCardAjaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private QuizletDAO quizletDAO;

    @Override
    public void init() throws ServletException {
        quizletDAO = new QuizletDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        List<String> success = new ArrayList<>();
        List<String> error = new ArrayList<>();

        HttpSession session = request.getSession(false);
        Learner learner = (session != null) ? (Learner) session.getAttribute("learner") : null;
        if (learner == null) {
            json.put("success", false);
            error.add("Bạn cần đăng nhập để sử dụng chức năng này.");
            json.put("errorMessages", error);
            out.print(json.toString());
            return;
        }
        int learnerID = learner.getLearnerID();
        String mode = request.getParameter("mode");

        if ("manual".equals(mode)) {
            String topic = request.getParameter("manualTopic");
            String flashCards = request.getParameter("manualFlashCards");
            if (topic == null || topic.trim().isEmpty() || flashCards == null || flashCards.trim().isEmpty()) {
                error.add("Vui lòng nhập đầy đủ topic và flashcard.");
            } else {
                String[] pairs = flashCards.split(";");
                for (String pair : pairs) {
                    String trimmed = pair.trim();
                    if (trimmed.isEmpty()) continue;
                    String[] parts = trimmed.split(":");
                    if (parts.length != 2) {
                        error.add("Cú pháp không đúng cho: " + trimmed);
                        continue;
                    }
                    String word = parts[0].trim();
                    String mean = parts[1].trim();
                    if (word.isEmpty() || mean.isEmpty()) {
                        error.add("Từ hoặc nghĩa trống cho: " + trimmed);
                        continue;
                    }
                    CustomFlashCard card = new CustomFlashCard(learnerID, word, mean, topic);
                    boolean ok = quizletDAO.addCustomFlashCard(card);
                    if (ok) {
                        success.add(word + ":" + mean);
                    } else {
                        error.add("Không thể thêm: " + word + ":" + mean);
                    }
                }
            }
        } else if ("individual".equals(mode)) {
            String topic = request.getParameter("individualTopic");
            String word = request.getParameter("word");
            String mean = request.getParameter("mean");
            if (topic == null || topic.trim().isEmpty() || word == null || word.trim().isEmpty() || mean == null || mean.trim().isEmpty()) {
                error.add("Vui lòng nhập đầy đủ topic, từ và nghĩa.");
            } else {
                CustomFlashCard card = new CustomFlashCard(learnerID, word.trim(), mean.trim(), topic.trim());
                boolean ok = quizletDAO.addCustomFlashCard(card);
                if (ok) {
                    success.add(word + ":" + mean);
                } else {
                    error.add("Không thể thêm: " + word + ":" + mean);
                }
            }
        } else {
            error.add("Dữ liệu không hợp lệ.");
        }

        json.put("success", !success.isEmpty());
        JSONArray arr = new JSONArray();
        for (String s : success) {
            String[] parts = s.split(":");
            JSONObject obj = new JSONObject();
            obj.put("word", parts[0]);
            obj.put("mean", parts[1]);
            arr.put(obj);
        }
        json.put("flashcards", arr);
        json.put("errorMessages", error);
        out.print(json.toString());
    }
} 