package controller;

import service.GeminiService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/gemini/*")
public class GeminiController extends HttpServlet {
    private final GeminiService geminiService = new GeminiService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String response = "";

        try {
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

            resp.setContentType("application/json");
            resp.getWriter().write("{\"response\": \"" + response + "\"}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 