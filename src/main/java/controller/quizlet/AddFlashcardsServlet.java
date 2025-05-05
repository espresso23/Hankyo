package controller.quizlet;

import dao.QuizletDAO;
import model.CustomFlashCard;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add-flashcards")
public class AddFlashcardsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private QuizletDAO quizletDAO;

    @Override
    public void init() throws ServletException {
        quizletDAO = new QuizletDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer learnerID = learner.getLearnerID();
        String mode = request.getParameter("inputMode");
        String topic = request.getParameter("topic");
        
        if (topic == null || topic.trim().isEmpty()) {
            errorMessages.add("Tên chủ đề không được để trống");
            forwardWithMessages(request, response, successMessages, errorMessages);
            return;
        }
        
        // Xử lý theo chế độ nhập dữ liệu
        if ("manual".equals(mode)) {
            // Chế độ nhập nhanh nhiều flashcard
            String flashcards = request.getParameter("flashcards");
            if (flashcards == null || flashcards.trim().isEmpty()) {
                errorMessages.add("Bạn chưa nhập từ vựng");
                forwardWithMessages(request, response, successMessages, errorMessages);
                return;
            }
            
            // Tách các cặp từ, phân tách bằng dấu chấm phẩy
            String[] pairs = flashcards.split(";");
            int addedCount = 0;
            
            for (String pair : pairs) {
                if (pair.trim().isEmpty()) continue;
                
                String[] parts = pair.split(":");
                if (parts.length != 2) {
                    errorMessages.add("Định dạng không hợp lệ cho: " + pair + ". Hãy sử dụng định dạng 'từ:nghĩa'");
                    continue;
                }
                
                String word = parts[0].trim();
                String mean = parts[1].trim();
                
                if (word.isEmpty() || mean.isEmpty()) {
                    errorMessages.add("Từ và nghĩa không được để trống: " + pair);
                    continue;
                }
                
                try {
                    CustomFlashCard flashCard = new CustomFlashCard(learnerID, word, mean, topic);
                    if (quizletDAO.addCustomFlashCard(flashCard)) {
                        addedCount++;
                    } else {
                        errorMessages.add("Không thể thêm flashcard: " + word);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessages.add("Lỗi khi thêm flashcard: " + word + " - " + e.getMessage());
                }
            }
            
            if (addedCount > 0) {
                successMessages.add("Đã thêm thành công " + addedCount + " flashcard vào chủ đề " + topic);
            }
        } else if ("individual".equals(mode)) {
            // Chế độ nhập từng flashcard một
            String word = request.getParameter("word");
            String mean = request.getParameter("mean");
            
            if (word == null || word.trim().isEmpty()) {
                errorMessages.add("Từ không được để trống");
                forwardWithMessages(request, response, successMessages, errorMessages);
                return;
            }
            
            if (mean == null || mean.trim().isEmpty()) {
                errorMessages.add("Nghĩa không được để trống");
                forwardWithMessages(request, response, successMessages, errorMessages);
                return;
            }
            
            try {
                CustomFlashCard flashCard = new CustomFlashCard(learnerID, word, mean, topic);
                if (quizletDAO.addCustomFlashCard(flashCard)) {
                    successMessages.add("Đã thêm thành công: " + word);
                } else {
                    errorMessages.add("Không thể thêm flashcard: " + word);
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessages.add("Lỗi khi thêm flashcard: " + e.getMessage());
            }
        } else {
            errorMessages.add("Chế độ nhập không hợp lệ");
        }
        
        forwardWithMessages(request, response, successMessages, errorMessages);
    }
    
    private void forwardWithMessages(HttpServletRequest request, HttpServletResponse response,
                                     List<String> successMessages, List<String> errorMessages) 
                                     throws ServletException, IOException {
        request.setAttribute("successMessages", successMessages);
        request.setAttribute("errorMessages", errorMessages);
        request.setAttribute("activeTab", "add");
        request.getRequestDispatcher("quizlet?tab=add").forward(request, response);
    }
} 