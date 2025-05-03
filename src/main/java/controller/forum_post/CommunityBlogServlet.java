package controller;

import com.google.gson.Gson;
import dao.ReportDAO;
import dao.UserDAO;
import dao.PostDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Post;
import model.Report;
import model.User;

@WebServlet(name = "CommunityBlog", urlPatterns = {"/blog"})
public class CommunityBlogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostDAO postDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        postDAO = new PostDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String filter = request.getParameter("filter");
            String searchQuery = request.getParameter("searchQuery");

            List<Post> postList;

            // Lọc nếu có filter
            if (filter != null) {
                switch (filter) {
                    case "newest":
                        postList = postDAO.getPostsOrderedByLatest();
                        break;
                    case "oldest":
                        postList = postDAO.getPostsOrderedByOldest();
                        break;
                    case "toprated":
                        postList = postDAO.getPostsOrderedFilterByScore();
                        break;
                    default:
                        postList = postDAO.getAllPostsHaveFullNameAndAvtImg();
                        break;
                }
            } else {
                postList = postDAO.getAllPostsHaveFullNameAndAvtImg();
            }

            // Đếm comment
            for (Post post : postList) {
                int commentCount = postDAO.getCommentCount(post.getPostID());
                post.setCommentCount(commentCount);
            }

            // Top rated
            Post newPost = postDAO.getLatestPost();
            String fullNameNewPost = (newPost != null) ? userDAO.getFullNameByUserId(newPost.getUserID()) : "";
            String avatarURLNewPost = (newPost != null) ? userDAO.getAvatarByUserId(newPost.getUserID()) : "";
            List<Post> topRatedPosts = postDAO.getPostsOrderedByScore();

            // Tìm kiếm nếu có
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                List<Post> searchResults = postDAO.searchPosts(searchQuery);
                request.setAttribute("searchResults", searchResults);
            }

            // Gửi lên JSP
            request.setAttribute("postList", postList);
            request.setAttribute("newPost", newPost);
            request.setAttribute("fullNameNewPost", fullNameNewPost);
            request.setAttribute("avatarURLNewPost", avatarURLNewPost);
            request.setAttribute("topRatedPosts", topRatedPosts);
            request.getRequestDispatcher("blog.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading blog data", e);
        }
    }

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Action parameter is missing\"}");
            return;
        }

        try {
            switch (action) {
                case "vote":
                    handleVote(request, response);
                    break;
                case "getUserVotes":
                    handleGetUserVotes(request, response);
                    break;
                case "reportPost":
                    handleReport(request, response);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid action\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    private void handleVote (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            sendJsonResponse(response, false, "Please login to vote", 401);
            return;
        }

        try {
            int postId = Integer.parseInt(request.getParameter("postID"));
            String voteAction = request.getParameter("voteAction");

            int voteType = 0;
            if ("upvote".equals(voteAction)) {
                voteType = 1;
            } else if ("downvote".equals(voteAction)) {
                voteType = -1;
            } else {
                sendJsonResponse(response, false, "Invalid vote action", 400);
                return;
            }

            boolean success = postDAO.addOrUpdateVote(user.getUserID(), postId, voteType);
            if (success) {
                int newScore = postDAO.getPostScore(postId);
                int currentUserVote = postDAO.getUserVote(user.getUserID(), postId);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("newScore", newScore);
                responseData.put("currentUserVote", currentUserVote);

                response.getWriter().write(new Gson().toJson(responseData));
            } else {
                sendJsonResponse(response, false, "Failed to process vote", 500);
            }
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid post ID", 400);
        }
    }

    private void sendJsonResponse (HttpServletResponse response,boolean success, String message,int status) throws
            IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", success);
        responseData.put("message", message);
        response.getWriter().write(new Gson().toJson(responseData));
    }
    private void handleGetUserVotes (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.getWriter().write("{}");
            return;
        }

        String[] postIds = request.getParameterValues("postIDs[]");
        if (postIds == null || postIds.length == 0) {
            response.getWriter().write("{}");
            return;
        }

        Map<Integer, Integer> votes = new HashMap<>();
        for (String postIdStr : postIds) {
            try {
                int postId = Integer.parseInt(postIdStr);
                int voteType = postDAO.getUserVote(user.getUserID(), postId);
                votes.put(postId, voteType);
            } catch (NumberFormatException e) {
                // Skip invalid post IDs
            }
        }

        response.getWriter().write(new Gson().toJson(votes));
    }
    private void handlePostVote (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"User not logged in\"}");
            return;
        }

        try {
            int postId = Integer.parseInt(request.getParameter("postID"));
            int voteType = Integer.parseInt(request.getParameter("voteType"));
            if (voteType != 1 && voteType != -1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid vote type\"}");
                return;
            }

            boolean success = postDAO.updateUserVote(user.getUserID(), postId, voteType);
            int newScore = postDAO.getPostScore(postId);

            response.getWriter().write(
                    "{\"success\": " + success + ", \"score\": " + newScore + ", \"voteType\": " + (success ? voteType : 0) + "}"
            );
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid parameters\"}");
        }
    }

    private void loadUserPostVotes (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.getWriter().write("{}");
            return;
        }

        String[] postIDs = request.getParameterValues("postIDs[]");
        List<Integer> postList = new ArrayList<>();
        if (postIDs != null) {
            for (String id : postIDs) {
                try {
                    postList.add(Integer.parseInt(id));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        Map<Integer, Integer> voteMap = postDAO.getUserVotes(user.getUserID(), postList);
        String json = new Gson().toJson(voteMap);
        response.getWriter().write(json);
    }

    private void handleReport (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.getWriter().write("{\"success\": false, \"error\": \"User not logged in\"}");
            return;
        }

        try {
            int postId = Integer.parseInt(request.getParameter("postID"));
            String reason = request.getParameter("reason");
            boolean isConfirmed = Boolean.parseBoolean(request.getParameter("confirmed"));

            int postOwnerId = postDAO.getUserIDByPostId(postId);
            if (postOwnerId == user.getUserID() && !isConfirmed) {
                response.getWriter().write("{\"success\": false, \"warning\": true, \"message\": \"Đây là bài viết của bạn. Bạn có thực sự muốn báo cáo không?\"}");
                return;
            }

            Report report = new Report();
            report.setPostID(postId);
            report.setReason(reason);
            report.setReportTypeID(3);
            report.setReporterID(user.getUserID());

            ReportDAO reportDAO = new ReportDAO();
            boolean success = reportDAO.createPostReport(report);
            response.getWriter().write("{\"success\": " + success + "}");

        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    public String getServletInfo () {
        return "CommunityBlogServlet handles blog features including post voting, reporting, and filtering.";
    }
}