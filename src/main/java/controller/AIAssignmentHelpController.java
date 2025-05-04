package controller;

import service.GeminiService;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import dao.VipUserDAO;
import model.Learner;

@WebServlet("/ai-assignment-help")
public class AIAssignmentHelpController extends HttpServlet {
    private final GeminiService geminiService = new GeminiService();
    private final VipUserDAO vipUserDAO = new VipUserDAO();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            // Lấy learner từ session
            Learner learner = (Learner) req.getSession().getAttribute("learner");
            if (learner == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Bạn cần đăng nhập để sử dụng AI.");
                return;
            }
            int learnerID = learner.getLearnerID();
            // Kiểm tra quota AI
            if (!vipUserDAO.canUseAI(learnerID)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Bạn đã hết lượt sử dụng AI miễn phí trong ngày. Hãy nâng cấp VIP để sử dụng không giới hạn!");
                return;
            }
            // Đọc dữ liệu từ request
            String jsonData = req.getReader().lines().collect(Collectors.joining());
            JSONObject questionData = new JSONObject(jsonData);
            
            // Tạo prompt cho AI
            StringBuilder prompt = new StringBuilder();
            prompt.append("Hãy phân tích câu hỏi sau và đưa ra gợi ý chi tiết:\n\n");
            prompt.append("Câu hỏi: ").append(questionData.getString("questionText")).append("\n\n");
            prompt.append("Các phương án trả lời:\n");
            
            JSONArray answers = questionData.getJSONArray("answers");
            for (int i = 0; i < answers.length(); i++) {
                JSONObject answer = answers.getJSONObject(i);
                prompt.append(answer.getString("label")).append(". ")
                      .append(answer.getString("text")).append("\n");
            }
            
            prompt.append("\nĐáp án đúng: ").append(questionData.getString("correctAnswer")).append("\n");
            prompt.append("Câu trả lời của bạn: ").append(questionData.getString("userAnswer")).append("\n\n");
            prompt.append("Hãy phân tích và đưa ra gợi ý chi tiết, giải thích tại sao đáp án đó là đúng, " +
                         "và nếu câu trả lời của người dùng sai, hãy giải thích tại sao sai.");
            
            // Gọi Gemini API
            String analysis = geminiService.analyzeExercise(prompt.toString());
            
            // Tăng số lần sử dụng AI nếu không phải VIP
            if (!vipUserDAO.isVipUser(learnerID)) {
                vipUserDAO.incrementTodayUsage(learnerID);
            }
            
            // Trả về kết quả
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write(formatAIResponse(analysis));
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Xin lỗi, không thể tạo gợi ý lúc này. Vui lòng thử lại sau.");
        }
    }
    
    private String formatAIResponse(String analysis) {
        // Chuyển markdown đơn giản sang HTML
        String html = analysis;
        // In đậm **text**
        html = html.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
        // Danh sách bullet: * đầu dòng
        html = html.replaceAll("(?m)^\\* (.+)$", "<li>$1</li>");
        // Nếu có <li> thì bọc bằng <ul>
        if (html.contains("<li>")) {
            html = html.replaceAll("((<li>.*?</li>\s*)+)", "<ul>$1</ul>");
        }
        // Xuống dòng kép thành đoạn
        html = html.replaceAll("(\r?\n){2,}", "</p><p>");
        // Xuống dòng đơn thành <br>
        html = html.replaceAll("(\r?\n)", "<br>");
        // Bọc toàn bộ bằng <p>
        html = "<p>" + html + "</p>";
        return "<div class='ai-analysis'>" +
               "<div class='analysis-content'>" + html + "</div>" +
               "</div>";
    }
} 