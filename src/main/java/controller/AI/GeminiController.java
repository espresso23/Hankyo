package controller.AI;

import service.GeminiService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import dao.VipUserDAO;
import model.Learner;

@WebServlet("/gemini/*")
public class GeminiController extends HttpServlet {
    private final GeminiService geminiService = new GeminiService();
    private final VipUserDAO vipUserDAO = new VipUserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Kiểm tra quota AI
        Learner learner = (Learner) req.getSession().getAttribute("learner");
        if (learner == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Bạn cần đăng nhập để sử dụng AI.\"}");
            return;
        }
        int learnerID = learner.getLearnerID();
        try {
            if (!vipUserDAO.canUseAI(learnerID)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Bạn đã hết lượt sử dụng AI miễn phí trong ngày. Hãy nâng cấp VIP để sử dụng không giới hạn!\"}");
                return;
            }
            String path = req.getPathInfo();
            String response = "";
            switch (path) {
                case "/generate-question":
                    String topic = req.getParameter("topic");
                    String level = req.getParameter("level");
                    response = geminiService.generateKoreanQuestion(topic, level);
                    break;
                case "/explain-grammar":
                    String grammarPoint = req.getParameter("grammar");
                    response = geminiService.explainGrammar(grammarPoint);
                    break;
                case "/translate":
                    String sentence = req.getParameter("sentence");
                    String fromLang = req.getParameter("from");
                    String toLang = req.getParameter("to");
                    response = geminiService.translateSentence(sentence, fromLang, toLang);
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response = "Invalid endpoint";
            }
            // Tăng số lần sử dụng AI nếu không phải VIP
            if (!vipUserDAO.isVipUser(learnerID)) {
                vipUserDAO.incrementTodayUsage(learnerID);
            }
            String json = GeminiService.extractJsonFromGeminiResponse(response);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 