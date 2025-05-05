package controller.AI;

import service.GeminiService;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import dao.VipUserDAO;
import model.Learner;

@WebServlet("/gemini/chat")
public class GeminiChatController extends HttpServlet {
    private final GeminiService geminiService = new GeminiService();
    private final Gson gson = new Gson();
    private final VipUserDAO vipUserDAO = new VipUserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        Map<String, String> result = new HashMap<>();
        // Kiểm tra quota AI
        Learner learner = (Learner) req.getSession().getAttribute("learner");
        if (learner == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("error", "Bạn cần đăng nhập để sử dụng AI.");
            resp.getWriter().write(gson.toJson(result));
            return;
        }
        int learnerID = learner.getLearnerID();
        try {
            if (!vipUserDAO.canUseAI(learnerID)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                result.put("error", "Bạn đã hết lượt sử dụng AI miễn phí trong ngày. Hãy nâng cấp VIP để sử dụng không giới hạn!");
                resp.getWriter().write(gson.toJson(result));
                return;
            }
            String message = req.getParameter("message");
            if (message == null || message.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("error", "Message is required");
                resp.getWriter().write(gson.toJson(result));
                return;
            }
            // Tạo prompt phù hợp cho chat
            String prompt = String.format("Bạn là một trợ lý AI chuyên về tiếng Hàn. Hãy trả lời câu hỏi sau một cách thân thiện và hữu ích: %s", message);
            String response = geminiService.generateResponse(prompt);
            String json = service.GeminiService.extractJsonFromGeminiResponse(response);
            result.put("response", json);
            // Tăng số lần sử dụng AI nếu không phải VIP
            if (!vipUserDAO.isVipUser(learnerID)) {
                vipUserDAO.incrementTodayUsage(learnerID);
            }
            resp.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(result));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("Vui lòng sử dụng phương thức POST để gửi câu hỏi cho AI chat bot.");
    }
} 