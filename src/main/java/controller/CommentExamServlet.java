//package controller;
//
//import com.google.gson.Gson;
//import dao.UserDAO;
//import dao.CommentExamDAO;
//import dao.ExamDAO;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.*;
//
//import model.CommentExam;
//import model.Exam;
//import model.User;
//
//@WebServlet(name = "CommentExamServlet", urlPatterns = {"/examComment"})
//public class CommentExamServlet extends HttpServlet {
//
//    private ExamDAO examDAO = new ExamDAO();
//    private CommentExamDAO commentExamDAO = new CommentExamDAO();
//    private UserDAO userDAO = new UserDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//        try {
//            String examIdParam = request.getParameter("examID");
//            if (examIdParam != null) {
//                int examId = Integer.parseInt(examIdParam);
//                Exam exam = examDAO.getExamById(examId);
//
//                if (exam != null) {
//                    List<CommentExam> comments = commentExamDAO.getCommentsByExamID(examId);
//                    Map<Integer, List<CommentExam>> replyMap = new HashMap<>();
//
//                    for (CommentExam comment : comments) {
//                        int examCommentID = comment.getExamCommentID();
//                        if (examCommentID != 0) {
//                            List<CommentExam> replyList = commentExamDAO.getRepliesByCommentID(examCommentID);
//                            replyMap.put(examCommentID, replyList);
//                        }
//                    }
//
//                    request.setAttribute("replyMap", replyMap);
//                    request.setAttribute("examID", examIdParam);
//                    request.setAttribute("exam", exam);
//                    request.setAttribute("comments", comments);
//
//                    request.getRequestDispatcher("examDetail.jsp").forward(request, response);
//                } else {
//                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found");
//                }
//            } else {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing exam ID");
//            }
//        } catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exam ID format");
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getParameter("action");
//
//        if (action == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
//            return;
//        }
//
//        switch (action) {
//            case "addComment":
//                handleAddComment(request, response);
//                break;
//            case "editComment":
//                handleEditComment(request, response);
//                break;
//            case "deleteComment":
//                handleDeleteComment(request, response);
//                break;
//            case "updateScore":
//                handleVote(request, response);
//                break;
//            case "getCommentScore":
//                getCommentScore(request, response);
//                break;
//            case "loadUserVotes":
//                loadUserVotes(request, response);
//                break;
//            default:
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
//        }
//    }
//
//    private void handleVote(HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        try {
//            HttpSession session = request.getSession();
//            User user = (User) session.getAttribute("user");
//
//            if (user == null) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("{\"error\": \"User not logged in\"}");
//                return;
//            }
//
//            int examCommentID = Integer.parseInt(request.getParameter("examCommentID"));
//            int voteType = Integer.parseInt(request.getParameter("voteType"));
//
//            if (voteType != 1 && voteType != -1) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("{\"error\": \"Invalid vote type\"}");
//                return;
//            }
//
//            boolean success = commentExamDAO.updateUserVote(user.getUserID(), examCommentID, voteType);
//            int newScore = commentExamDAO.getCommentScore(examCommentID);
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(
//                    "{\"success\": " + success +
//                            ", \"score\": " + newScore +
//                            ", \"voteType\": " + (success ? voteType : 0) + "}"
//            );
//        } catch (NumberFormatException e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\": \"Invalid parameters\"}");
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
//        }
//    }
//
//    private void getCommentScore(HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        try {
//            int examCommentID = Integer.parseInt(request.getParameter("examCommentID"));
//            int score = commentExamDAO.getCommentScore(examCommentID);
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write("{\"score\": " + score + "}");
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Server error\"}");
//        }
//    }
//
//    private void loadUserVotes(HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        try {
//            HttpSession session = request.getSession();
//            User user = (User) session.getAttribute("user");
//
//            if (user == null) {
//                response.setContentType("application/json");
//                response.getWriter().write("{}");
//                return;
//            }
//
//            String[] examCommentIdStrings = request.getParameterValues("examCommentIDs[]");
//            if (examCommentIdStrings == null || examCommentIdStrings.length == 0) {
//                response.setContentType("application/json");
//                response.getWriter().write("{}");
//                return;
//            }
//
//            List<Integer> examCommentIDs = new ArrayList<>();
//            for (String idStr : examCommentIdStrings) {
//                examCommentIDs.add(Integer.parseInt(idStr));
//            }
//
//            Map<Integer, Integer> userVotes = commentExamDAO.getUserVotes(user.getUserID(), examCommentIDs);
//
//            Gson gson = new Gson();
//            String userVotesJSON = gson.toJson(userVotes);
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(userVotesJSON);
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Server error\"}");
//        }
//    }
//
//    private void handleAddComment(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            HttpSession session = request.getSession();
//            User loggedUser = (User) session.getAttribute("user");
//            if (loggedUser == null) {
//                response.sendRedirect("login.jsp");
//                return;
//            }
//
//            String commentText = request.getParameter("commentInput");
//            String examIdParam = request.getParameter("examID");
//
//            if (examIdParam == null || commentText == null || commentText.trim().isEmpty()) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing exam ID or comment text");
//                return;
//            }
//
//            int examId = Integer.parseInt(examIdParam);
//            int userID = loggedUser.getUserID();
//            String userFullName = userDAO.getFullNameByUserId(userID);
//            String userAvtURL = userDAO.getAvatarByUserId(userID);
//
//            CommentExam comment = new CommentExam(userID, userFullName, userAvtURL, examId, commentText);
//            comment.setCreatedDate(new Date());
//
//            int parentID = 0;
//            try {
//                parentID = Integer.parseInt(request.getParameter("parentID"));
//                comment.setParentExamCommentID(parentID);
//            } catch (NumberFormatException e) {
//                System.out.println("root comment");
//            }
//
//            boolean isAdded;
//            if (parentID != 0) {
//                isAdded = commentExamDAO.addReplyComment(comment);
//            } else {
//                isAdded = commentExamDAO.addComment(comment);
//            }
//
//            if (isAdded) {
//                response.sendRedirect("examDetail?examID=" + examId);
//            } else {
//                request.setAttribute("error", "Failed to post comment. Please try again.");
//                doGet(request, response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
//        }
//    }
//
//    private void handleEditComment(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            HttpSession session = request.getSession();
//            User loggedUser = (User) session.getAttribute("user");
//            if (loggedUser == null) {
//                response.sendRedirect("login.jsp");
//                return;
//            }
//
//            int examCommentID = Integer.parseInt(request.getParameter("examCommentID"));
//            String commentContent = request.getParameter("commentContent");
//            int examID = Integer.parseInt(request.getParameter("examID"));
//
//            CommentExam existingComment = commentExamDAO.getCommentByID(examCommentID);
//            if (existingComment == null || existingComment.getUserID() != loggedUser.getUserID()) {
//                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot edit this comment");
//                return;
//            }
//
//            boolean updated = commentExamDAO.updateComment(examCommentID, commentContent);
//
//            if (updated) {
//                response.sendRedirect("exam?action=details&examID=" + examID);
//            } else {
//                request.setAttribute("error", "Error updating comment.");
//                doGet(request, response);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
//        }
//    }
//
//    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            HttpSession session = request.getSession();
//            User loggedUser = (User) session.getAttribute("user");
//            if (loggedUser == null) {
//                response.sendRedirect("login.jsp");
//                return;
//            }
//
//            int examCommentID = Integer.parseInt(request.getParameter("examCommentID"));
//            int examID = Integer.parseInt(request.getParameter("examID"));
//
//            CommentExam existingComment = commentExamDAO.getCommentByID(examCommentID);
//
//            if (existingComment == null || existingComment.getUserID() != loggedUser.getUserID()) {
//                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot delete this comment");
//                return;
//            }
//
//            boolean success = commentExamDAO.deleteComment(examCommentID);
//
//            if (success) {
//                response.sendRedirect("exam?action=details&examID=" + examID);
//            } else {
//                request.setAttribute("error", "Failed to delete the comment.");
//                doGet(request, response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Handles exam comments";
//    }
//}