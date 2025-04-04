package controller;

import dao.QuizletDAO;
import model.CustomFlashCard;

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
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("addFlashCard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String topic = request.getParameter("topic");
        String flashCardInput = request.getParameter("flashCards");

        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        if (topic == null || topic.trim().isEmpty() || flashCardInput == null || flashCardInput.trim().isEmpty()) {
            out.println("<h3>Lỗi: Vui lòng nhập đầy đủ topic và flashcard!</h3>");
            return;
        }
        if (learnerID == null) {
            out.println("<h3>Lỗi: Bạn cần đăng nhập để thêm flashcard!</h3>");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String[] flashCardPairs = flashCardInput.split(";");
            List<String> successMessages = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();

            for (String pair : flashCardPairs) {
                String trimmedPair = pair.trim();
                if (trimmedPair.isEmpty()) continue; // Bỏ qua cặp rỗng

                String[] parts = trimmedPair.split(":");
                if (parts.length != 2) {
                    errorMessages.add("Cú pháp không đúng cho: " + trimmedPair);
                    continue;
                }

                String word = parts[0].trim();
                String mean = parts[1].trim();

                if (word.isEmpty() || mean.isEmpty()) {
                    errorMessages.add("Từ hoặc nghĩa trống cho: " + trimmedPair);
                    continue;
                }

                CustomFlashCard customFlashCard = new CustomFlashCard(learnerID, word, mean, topic);
                boolean success = quizletDAO.addCustomFlashCard(customFlashCard);

                if (success) {
                    successMessages.add("Từ vựng: " + word + " - Nghĩa: " + mean);
                } else {
                    errorMessages.add("Không thể thêm: " + word + ":" + mean);
                }
            }

            // Phản hồi kết quả
            out.println("<h3>Kết quả thêm flashcard</h3>");
            if (!successMessages.isEmpty()) {
                out.println("<p style='color: green;'>Thêm thành công:</p>");
                out.println("<ul>");
                for (String msg : successMessages) {
                    out.println("<li>" + msg + "</li>");
                }
                out.println("</ul>");
            }
            if (!errorMessages.isEmpty()) {
                out.println("<p style='color: red;'>Lỗi:</p>");
                out.println("<ul>");
                for (String msg : errorMessages) {
                    out.println("<li>" + msg + "</li>");
                }
                out.println("</ul>");
            }
            out.println("<p>Topic: " + topic + "</p>");
            out.println("<a href='quizlet'>Quay lại danh sách flashcard</a>");
            out.println("<a href='addFlashCard'>Thêm flashcard khác</a>");

        } catch (Exception e) {
            out.println("<h3>Lỗi xử lý: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}