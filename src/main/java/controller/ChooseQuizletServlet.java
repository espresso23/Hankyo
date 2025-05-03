package controller;

import com.google.gson.Gson;
import dao.DictionaryDAO;
import dao.QuizletDAO;
import model.CustomFlashCard;
import model.FavoriteFlashCard;
import model.Learner;
import model.SystemFlashCard;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/flashCard")
public class ChooseQuizletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        String topic = request.getParameter("topic");
        String type = request.getParameter("type");
        Gson gson = new Gson();
        DictionaryDAO dictionaryDAO = new DictionaryDAO();
        QuizletDAO quizletDAO = new QuizletDAO();

        if (topic == null || topic.isEmpty()) {
            request.setAttribute("error", "No topic specified.");
            request.getRequestDispatcher("quizlet.jsp").forward(request, response);
            return;
        }

        if ("favorite".equals(type)) {
            if (learnerID == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            System.out.println("Fetching favorite flashcards for learnerID: " + learnerID + ", nameOfList: " + topic);

            List<FavoriteFlashCard> flashCards = dictionaryDAO.getAllFavoriteFlashCardByLearnerID(learnerID, topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No favorite flashcards found for list: " + topic);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                System.out.println("Favorite flashCardsJson: " + flashCardsJson);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
                request.setAttribute("type", "favorite");
            }
            request.setAttribute("sessionLearnerID", learnerID);
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
        } else if ("custom".equals(type)) {
            // Lấy learnerID từ request nếu có, nếu không thì lấy từ session
            String learnerIDParam = request.getParameter("learnerID");
            int targetLearnerID = learnerID;
            if (learnerIDParam != null && !learnerIDParam.isEmpty()) {
                try {
                    targetLearnerID = Integer.parseInt(learnerIDParam);
                } catch (NumberFormatException e) {
                    // fallback giữ nguyên learnerID session
                }
            }

            // Lấy danh sách flashcards và xử lý quyền edit
            List<CustomFlashCard> flashCards = quizletDAO.getAllCustomFlashCardByTopicAndLeanerID(targetLearnerID, topic);
            for (CustomFlashCard flashcard : flashCards) {
                // Set canEdit = true nếu flashcard thuộc về người dùng hiện tại
                if (flashcard.getLearnerID() != null && flashcard.getLearnerID().equals(learnerID)) {
                    flashcard.setCanEdit(true);
                }
            }

            // Lấy tên của các learner
            Map<Integer, String> learnerNames = quizletDAO.getLearnerFullNameMap(flashCards);
            
            request.setAttribute("learnerNames", learnerNames);
            String flashCardsJson = gson.toJson(flashCards);
            request.setAttribute("flashCardsJson", flashCardsJson);
            request.setAttribute("flashCards", flashCards);
            request.setAttribute("topic", topic);
            request.setAttribute("type", "custom");
            request.setAttribute("sessionLearnerID", learnerID);
            request.getRequestDispatcher("customQuizlet.jsp").forward(request, response);
        } else {
            System.out.println("Fetching system flashcards for topic: " + topic);

            List<SystemFlashCard> flashCards = quizletDAO.getAllSystemFlashCardByTopic(topic);

            if (flashCards == null || flashCards.isEmpty()) {
                request.setAttribute("error", "No system flashcards found for topic: " + topic);
            } else {
                String flashCardsJson = gson.toJson(flashCards);
                System.out.println("System flashCardsJson: " + flashCardsJson);
                request.setAttribute("flashCardsJson", flashCardsJson);
                request.setAttribute("flashCards", flashCards);
                request.setAttribute("topic", topic);
                request.setAttribute("type", "system");
            }
            request.setAttribute("sessionLearnerID", learnerID);
            request.getRequestDispatcher("selectTopic.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String action = request.getParameter("action");
        QuizletDAO dao = new QuizletDAO();
        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner.getLearnerID();
        String cfcidStr, word, mean, topic;

        if (learnerID == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Please login first\"}");
            return;
        }

        if (action != null) {
            switch (action) {
                case "delete":
                    cfcidStr = request.getParameter("cfcid");
                    try {
                        int cfcid = Integer.parseInt(cfcidStr);
                        boolean success = dao.deleteCustomFlashCard(cfcid);
                        if (success) {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"message\": \"Delete successful\"}");
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\": \"Delete failed: Flashcard not found\"}");
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Invalid cfcid\"}");
                    }
                    break;

                case "update":
                    cfcidStr = request.getParameter("cfcid");
                    word = request.getParameter("word");
                    mean = request.getParameter("mean");
                    try {
                        int cfcid = Integer.parseInt(cfcidStr);
                        if (word != null && !word.trim().isEmpty() && mean != null && !mean.trim().isEmpty()) {
                            boolean success = dao.updateCustomFlashCard(cfcid, word, mean);
                            if (success) {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"message\": \"Update successful\"}");
                            } else {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"error\": \"Update failed: Flashcard not found\"}");
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\": \"Word or mean cannot be empty\"}");
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Invalid cfcid\"}");
                    }
                    break;

                case "togglePublic":
                    cfcidStr = request.getParameter("cfcid");
                    String isPublicStr = request.getParameter("isPublic");
                    try {
                        int cfcid = Integer.parseInt(cfcidStr);
                        boolean isPublic = Boolean.parseBoolean(isPublicStr);
                        
                        // Verify ownership
                        CustomFlashCard flashcard = dao.getCustomFlashCardById(cfcid);
                        if (flashcard != null && flashcard.getLearnerID().equals(learnerID)) {
                            boolean success = dao.updateCustomFlashCardPublic(cfcid, !isPublic); // Toggle the current state
                            if (success) {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"success\": true, \"message\": \"Visibility updated successfully\"}");
                            } else {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"success\": false, \"error\": \"Failed to update visibility\"}");
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"success\": false, \"error\": \"You don't have permission to modify this flashcard\"}");
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"success\": false, \"error\": \"Invalid flashcard ID\"}");
                    }
                    break;

                case "add":
                    String mode = request.getParameter("mode");
                    topic = request.getParameter("topic");
                    List<CustomFlashCard> newFlashcards = new ArrayList<>();

                    if (topic == null || topic.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Topic is required\"}");
                        return;
                    }

                    if ("manual".equals(mode)) {
                        String flashCardsInput = request.getParameter("flashCards");
                        if (flashCardsInput == null || flashCardsInput.trim().isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\": \"Flashcards input is required\"}");
                            return;
                        }

                        String[] pairs = flashCardsInput.split(";");
                        for (String pair : pairs) {
                            String[] parts = pair.split(":");
                            if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                                continue; // Skip invalid pairs
                            }
                            String newWord = parts[0].trim();
                            String newMean = parts[1].trim();
                            CustomFlashCard cf = new CustomFlashCard(learnerID, newWord, newMean, topic);
                            if (dao.addCustomFlashCard(cf)) {
                                // Get the new CFCID
                                int newCfcid = getLastInsertedCfcid(dao);
                                cf.setCFCID(newCfcid);
                                newFlashcards.add(cf);
                            }
                        }
                    } else if ("individual".equals(mode)) {
                        word = request.getParameter("word");
                        mean = request.getParameter("mean");
                        if (word == null || word.trim().isEmpty() || mean == null || mean.trim().isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\": \"Word and mean are required\"}");
                            return;
                        }
                        CustomFlashCard cf = new CustomFlashCard(learnerID, word, mean, topic);
                        if (dao.addCustomFlashCard(cf)) {
                            int newCfcid = getLastInsertedCfcid(dao);
                            cf.setCFCID(newCfcid);
                            newFlashcards.add(cf);
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Invalid mode\"}");
                        return;
                    }

                    if (!newFlashcards.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(newFlashcards));
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"No flashcards added\"}");
                    }
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid action\"}");
                    break;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing action\"}");
        }
    }

    private int getLastInsertedCfcid(QuizletDAO dao) {
        try {
            Connection connection =  DBConnect.getInstance().getConnection();;


            PreparedStatement stmt = connection.prepareStatement("SELECT LAST_INSERT_ID() as cfcid");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cfcid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}