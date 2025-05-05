package controller.dictionary;

import dao.DictionaryDAO;
import model.Dictionary;
import model.FavoriteFlashCard;
import model.Learner;
import service.GeminiService;
import com.google.gson.Gson;
import dao.VipUserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DictionaryServlet", urlPatterns = {"/dictionary"})
public class DictionaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DictionaryDAO dictionaryDAO;
    private final VipUserDAO vipUserDAO = new VipUserDAO();

    @Override
    public void init() throws ServletException {
        dictionaryDAO = new DictionaryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String word = request.getParameter("word");
        String mean = request.getParameter("mean");
        String type = request.getParameter("type");

        List<Dictionary> dictionaryList;
        
        if ((word != null && !word.trim().isEmpty()) || 
            (mean != null && !mean.trim().isEmpty()) || 
            (type != null && !type.trim().isEmpty())) {
            dictionaryList = dictionaryDAO.advancedSearch(word, type, mean);
        } else {
            dictionaryList = dictionaryDAO.getAllDictionary();
        }

        List<String> favoriteListNames = dictionaryDAO.getFavoriteListNamesByLearnerID(learnerID);

        request.setAttribute("dictionaryList", dictionaryList);
        request.setAttribute("favoriteListNames", favoriteListNames);
        request.getRequestDispatcher("dictionary.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        
        if (learner == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"error\": \"User not logged in\"}");
            out.flush();
            return;
        }

        String action = request.getParameter("action");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            switch (action) {
                case "searchAI":
                    handleAISearch(request, response);
                    break;
                case "addExample":
                    handleAddExample(request, response);
                    break;
                case "getExamples":
                    handleGetExamples(request, response);
                    break;
                case "addFavoriteFlashCard":
                    String wordIDStr = request.getParameter("wordID");
                    String nameOfList = request.getParameter("nameOfList");
                    if (wordIDStr == null || nameOfList == null || nameOfList.trim().isEmpty()) {
                        out.print("{\"success\": false, \"error\": \"Missing parameters\"}");
                        out.flush();
                        return;
                    }
                    int wordID = Integer.parseInt(wordIDStr);
                    Dictionary dictionary = dictionaryDAO.getDictionaryByWordID(wordID);
                    if (dictionary != null) {
                        FavoriteFlashCard fc = new FavoriteFlashCard(dictionary, learner);
                        fc.setNameOfList(nameOfList.trim());
                        boolean success = dictionaryDAO.addFavoriteFlashCard(fc);
                        out.print("{\"success\": " + success + "}");
                    } else {
                        out.print("{\"success\": false, \"error\": \"Word not found\"}");
                    }
                    break;
                case "removeFavoriteFlashCard":
                    String wordIDStrToRemove = request.getParameter("wordID");
                    String nameOfListToRemove = request.getParameter("nameOfList");
                    if (wordIDStrToRemove == null || nameOfListToRemove == null || nameOfListToRemove.trim().isEmpty()) {
                        out.print("{\"success\": false, \"error\": \"Missing parameters\"}");
                        out.flush();
                        return;
                    }
                    int wordIDToRemove = Integer.parseInt(wordIDStrToRemove);
                    boolean success = dictionaryDAO.removeFavoriteFlashCard(learnerID, wordIDToRemove, nameOfListToRemove.trim());
                    out.print("{\"success\": " + success + "}");
                    break;
                default:
                    out.print("{\"success\": false, \"error\": \"Invalid action\"}");
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    private void handleAISearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();

        // Kiểm tra quota AI
        try {
            if (!vipUserDAO.canUseAI(learnerID)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"success\": false, \"error\": \"Bạn đã hết lượt sử dụng AI miễn phí hôm nay!\"}");
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"Lỗi kiểm tra quota AI: " + e.getMessage() + "\"}");
            return;
        }

        String word = request.getParameter("word");
        String fromLang = request.getParameter("fromLang");
        String toLang = request.getParameter("toLang");

        if (word == null || fromLang == null || toLang == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"Missing parameters\"}");
            return;
        }

        try {
            GeminiService geminiService = new GeminiService();
            String result = geminiService.searchAndTranslateDictionary(word, fromLang, toLang);
            String json = GeminiService.extractJsonFromGeminiResponse(result);
            response.getWriter().write(json);
            // Tăng số lần sử dụng AI nếu không phải VIP
            try {
                if (!vipUserDAO.isVipUser(learnerID)) {
                    vipUserDAO.incrementTodayUsage(learnerID);
                }
            } catch (Exception e) {
                // Không chặn trả kết quả, chỉ log lỗi tăng quota nếu có
                e.printStackTrace();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleAddExample(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String wordIDStr = request.getParameter("wordID");
        String vietnameseExample = request.getParameter("vietnameseExample");
        String koreanExample = request.getParameter("koreanExample");
        String searchDirection = request.getParameter("searchDirection"); // "han2vi" hoặc "vi2han"

        if (vietnameseExample == null || koreanExample == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"Missing parameters\"}");
            return;
        }

        try {
            int wordID = -1;
            if (wordIDStr != null && !wordIDStr.trim().isEmpty()) {
                wordID = Integer.parseInt(wordIDStr);
            } else if ("han2vi".equals(searchDirection)) {
                // Nếu là từ mới và search từ Hàn sang Việt thì thêm vào dictionary
                String word = request.getParameter("word");
                String mean = request.getParameter("mean");
                String definition = request.getParameter("definition");
                String type = request.getParameter("type");
                if (word != null && mean != null && definition != null && type != null) {
                    wordID = dictionaryDAO.insertDictionaryAndGetId(word, mean, definition, type);
                }
            }
            if (wordID == -1) {
                response.getWriter().write("{\"success\": false, \"error\": \"Không xác định được wordID để lưu ví dụ!\"}");
                return;
            }
            boolean success = dictionaryDAO.addDictionaryExample(wordID, vietnameseExample, koreanExample);
            response.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleGetExamples(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String wordIDStr = request.getParameter("wordID");

        if (wordIDStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"Missing wordID parameter\"}");
            return;
        }

        try {
            int wordID = Integer.parseInt(wordIDStr);
            List<Map<String, String>> examples = dictionaryDAO.getDictionaryExamples(wordID);
            response.getWriter().write(new Gson().toJson(examples));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
