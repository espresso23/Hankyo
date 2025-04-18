package controller;

import dao.QuizletDAO;
import model.CustomFlashCard;
import model.Learner;
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
import java.util.stream.Collectors;

@WebServlet("/addFlashCard")
public class CustomFlashCardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private QuizletDAO quizletDAO;

    @Override
    public void init() throws ServletException {
        quizletDAO = new QuizletDAO();
    }

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

        request.getRequestDispatcher("quizlet").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");


        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String topic = request.getParameter("topic");
        String flashCardInput = request.getParameter("flashCards");
        String individualTopic = request.getParameter("individualTopic");
        String word = request.getParameter("word");
        String mean = request.getParameter("mean");

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        if (topic != null && flashCardInput != null) {
            // Manual Mode
            if (topic.trim().isEmpty() || flashCardInput.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập đầy đủ topic và flashcard.");
            } else {
                try {
                    String[] flashCardPairs = flashCardInput.split(";");
                    for (String pair : flashCardPairs) {
                        String trimmedPair = pair.trim();
                        if (trimmedPair.isEmpty()) continue;

                        String[] parts = trimmedPair.split(":");
                        if (parts.length != 2) {
                            errorMessages.add("Cú pháp không đúng cho: " + trimmedPair);
                            continue;
                        }

                        String w = parts[0].trim();
                        String m = parts[1].trim();

                        if (w.isEmpty() || m.isEmpty()) {
                            errorMessages.add("Từ hoặc nghĩa trống cho: " + trimmedPair);
                            continue;
                        }

                        CustomFlashCard customFlashCard = new CustomFlashCard(learnerID, w, m, topic);
                        boolean success = quizletDAO.addCustomFlashCard(customFlashCard);

                        if (success) {
                            successMessages.add("Từ vựng: " + w + " - Nghĩa: " + m);
                        } else {
                            errorMessages.add("Không thể thêm: " + w + ":" + m);
                        }
                    }
                } catch (Exception e) {
                    errorMessages.add("Lỗi xử lý: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else if (individualTopic != null && word != null && mean != null) {
            // Individual Mode
            if (individualTopic.trim().isEmpty() || word.trim().isEmpty() || mean.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập đầy đủ topic, từ và nghĩa.");
            } else {
                try {
                    CustomFlashCard customFlashCard = new CustomFlashCard(learnerID, word.trim(), mean.trim(), individualTopic.trim());
                    boolean success = quizletDAO.addCustomFlashCard(customFlashCard);

                    if (success) {
                        successMessages.add("Từ vựng: " + word + " - Nghĩa: " + mean);
                    } else {
                        errorMessages.add("Không thể thêm: " + word + ":" + mean);
                    }
                } catch (Exception e) {
                    errorMessages.add("Lỗi xử lý: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            errorMessages.add("Dữ liệu không hợp lệ.");
        }
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", !successMessages.isEmpty());
        jsonResponse.put("flashcards", successMessages.stream()
                .map(msg -> {
                    String[] parts = msg.split(" - Nghĩa: ");
                    JSONObject card = new JSONObject();
                    card.put("word", parts[0].replace("Từ vựng: ", ""));
                    card.put("mean", parts[1]);
                    return card;
                })
                .collect(Collectors.toList()));
        jsonResponse.put("errorMessages", errorMessages);
        out.print(jsonResponse.toString());
        out.flush();
        request.setAttribute("successMessages", successMessages);
        request.setAttribute("errorMessages", errorMessages);
        // request.getRequestDispatcher("quizlet").forward(request, response);
    }
}