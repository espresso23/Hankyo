package controller.quizlet;

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
        
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer learnerID = learner.getLearnerID();
        request.getRequestDispatcher("quizlet").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        System.out.println("da goi ham pót");
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer learnerID = learner.getLearnerID();
        String mode = request.getParameter("mode");
        String manualTopic = request.getParameter("manualTopic");
        String manualFlashCards = request.getParameter("manualFlashCards");
        String individualTopic = request.getParameter("individualTopic");
        String word = request.getParameter("word");
        String mean = request.getParameter("mean");

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        if ("manual".equals(mode)) {
            if (manualTopic == null || manualTopic.trim().isEmpty() || manualFlashCards == null || manualFlashCards.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập đầy đủ topic và flashcard.");
            } else {
                try {
                    String[] flashCardPairs = manualFlashCards.split(";");
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
                        CustomFlashCard customFlashCard = new CustomFlashCard(learnerID, w, m, manualTopic);
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
        } else if ("individual".equals(mode)) {
            if (individualTopic == null || individualTopic.trim().isEmpty() || word == null || word.trim().isEmpty() || mean == null || mean.trim().isEmpty()) {
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

        // Store messages in session for display after redirect
        request.getSession().setAttribute("successMessages", successMessages);
        request.getSession().setAttribute("errorMessages", errorMessages);
        
        // Redirect back to quizlet.jsp
        response.sendRedirect("quizlet");
    }
}