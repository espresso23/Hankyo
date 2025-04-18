package controller;

import dao.QuizletDAO;
import model.CustomFlashCard;
<<<<<<< HEAD
=======
import org.json.JSONObject;
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447

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
<<<<<<< HEAD
=======
import java.util.stream.Collectors;
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447

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

<<<<<<< HEAD
        request.getRequestDispatcher("addFlashCard.jsp").forward(request, response);
=======
        request.getRequestDispatcher("quizlet").forward(request, response);
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
<<<<<<< HEAD
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
=======

>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447

        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");

        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

<<<<<<< HEAD
        String topic = request.getParameter("topic"); // Manual mode topic
        String flashCardInput = request.getParameter("flashCards"); // Manual mode flashcards
        String individualTopic = request.getParameter("individualTopic"); // Individual mode topic
        String word = request.getParameter("word"); // Individual mode word
        String mean = request.getParameter("mean"); // Individual mode meaning
=======
        String topic = request.getParameter("topic");
        String flashCardInput = request.getParameter("flashCards");
        String individualTopic = request.getParameter("individualTopic");
        String word = request.getParameter("word");
        String mean = request.getParameter("mean");
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        if (topic != null && flashCardInput != null) {
            // Manual Mode
            if (topic.trim().isEmpty() || flashCardInput.trim().isEmpty()) {
<<<<<<< HEAD
                out.println("<h3>Lỗi: Vui lòng nhập đầy đủ topic và flashcard!</h3>");
                return;
            }

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
                out.println("<h3>Lỗi xử lý: " + e.getMessage() + "</h3>");
                e.printStackTrace();
=======
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
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447
            }
        } else if (individualTopic != null && word != null && mean != null) {
            // Individual Mode
            if (individualTopic.trim().isEmpty() || word.trim().isEmpty() || mean.trim().isEmpty()) {
<<<<<<< HEAD
                out.println("<h3>Lỗi: Vui lòng nhập đầy đủ topic, từ và nghĩa!</h3>");
                return;
            }

            try {
                CustomFlashCard customFlashCard = new CustomFlashCard(learnerID, word.trim(), mean.trim(), individualTopic.trim());
                boolean success = quizletDAO.addCustomFlashCard(customFlashCard);

                if (success) {
                    successMessages.add("Từ vựng: " + word + " - Nghĩa: " + mean);
                } else {
                    errorMessages.add("Không thể thêm: " + word + ":" + mean);
                }
            } catch (Exception e) {
                out.println("<h3>Lỗi xử lý: " + e.getMessage() + "</h3>");
                e.printStackTrace();
            }
        } else {
            out.println("<h3>Lỗi: Dữ liệu không hợp lệ!</h3>");
            return;
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
        out.println("<p>Topic: " + (topic != null ? topic : individualTopic) + "</p>");
        out.println("<a href='quizlet'>Quay lại danh sách flashcard</a>");
        out.println("<a href='addFlashCard'>Thêm flashcard khác</a>");
=======
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
>>>>>>> 880bb7bc0259975e40dc8b8108c3d0689bcde447
    }
}