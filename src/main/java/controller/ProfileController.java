package controller;

import cloud.CloudinaryConfig;
import dao.*;
import model.Comment;
import model.Post;
import model.Report;
import model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,   // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
@WebServlet(name = "ProfileController", value = "/profile")
public class ProfileController extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private CommentDAO commentDAO = new CommentDAO();
    private ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String usernameParam = request.getParameter("user"); // user đang được xem
        String tab = request.getParameter("tab");
        if (tab == null) tab = "overview";

        UserDAO userDAO = new UserDAO();
        User profileUser;
        boolean isOwnProfile = false;

        try {
            if (usernameParam != null && !usernameParam.isEmpty() && !usernameParam.equalsIgnoreCase(loggedInUser.getUsername())) {
                profileUser = userDAO.getUserByUserName(usernameParam);
                if (profileUser == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Người dùng không tồn tại.");
                    return;
                }
                // Khi xem profile người khác
                request.setAttribute("profileUser", profileUser);
                request.setAttribute("isOwnProfile", false);
            } else {
                // Khi xem profile chính mình
                profileUser = loggedInUser;
                isOwnProfile = true;
                request.setAttribute("user", loggedInUser); // Sử dụng user từ session
                request.setAttribute("isOwnProfile", true);
            }

            int postCount = postDAO.getPostCountByUserID(profileUser.getUserID());
            int commentCount = commentDAO.getCommentCountByUserID(profileUser.getUserID());
            request.setAttribute("postCount", postCount);
            request.setAttribute("commentCount", commentCount);

            // Kiểm tra các tab chỉ dành cho chủ profile
            if (!isOwnProfile && (tab.equals("downvoted") || tab.equals("upvoted") || tab.equals("reported"))) {
                response.sendRedirect("profile?user=" + (usernameParam != null ? usernameParam : "") + "&tab=overview");
                return;
            }
            System.out.println("Logged in user: " + loggedInUser);
            System.out.println("Username param: " + usernameParam);
            System.out.println("Is own profile: " + isOwnProfile);
            System.out.println("Profile user: " + profileUser);

            switch (tab) {
                case "posts":
                    List<Post> posts = postDAO.getPostsByUserID(profileUser.getUserID());
                    for (Post post : posts) {
                        post.setCommentCount(postDAO.getCommentCount(post.getPostID()));
                    }
                    request.setAttribute("userPosts", posts);
                    request.getRequestDispatcher("posts.jsp").forward(request, response);
                    break;

                case "comments":
                    List<Comment> comments = commentDAO.getCommentsByUserID(profileUser.getUserID());
                    request.setAttribute("userComments", comments);
                    request.getRequestDispatcher("comments.jsp").forward(request, response);
                    break;

                case "upvoted":
                    if (isOwnProfile) {
                        List<Post> upvotedPosts = postDAO.getUpvotedPostsByUserID(profileUser.getUserID());
                        for (Post post : upvotedPosts) {
                            post.setCommentCount(postDAO.getCommentCount(post.getPostID()));
                        }
                        request.setAttribute("upvotedPosts", upvotedPosts);
                        request.getRequestDispatcher("upvoted.jsp").forward(request, response);
                    }
                    break;

                case "downvoted":
                    if (isOwnProfile) {
                        List<Post> downvotedPosts = postDAO.getDownvotedPostsByUserID(profileUser.getUserID());
                        for (Post post : downvotedPosts) {
                            post.setCommentCount(postDAO.getCommentCount(post.getPostID()));
                        }
                        request.setAttribute("downvotedPosts", downvotedPosts);
                        request.getRequestDispatcher("downvoted.jsp").forward(request, response);
                    }
                    break;

                case "reported":
                    if (isOwnProfile) {
                        List<Report> reportedPosts = reportDAO.getReportedPostsByUserID(profileUser.getUserID());
                        List<Report> reportedComments = reportDAO.getReportedCommentsByUserID(profileUser.getUserID());
                        request.setAttribute("reportedPosts", reportedPosts);
                        request.setAttribute("reportedComments", reportedComments);
                        request.getRequestDispatcher("reported.jsp").forward(request, response);
                    }
                    break;

                default:
                    List<Post> overviewPosts = postDAO.getPostsByUserID(profileUser.getUserID());
                    for (Post post : overviewPosts) {
                        post.setCommentCount(postDAO.getCommentCount(post.getPostID()));
                    }
                    request.setAttribute("userPosts", overviewPosts);
                    request.getRequestDispatcher("overview.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi xử lý profile", e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        UserDAO userDAO = new UserDAO();
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Xử lý addPost trước các xử lý khác
            if ("addPost".equals(action)) {
                String title = request.getParameter("title");
                String description = request.getParameter("description");

                Post newPost = new Post();
                newPost.setUserID(user.getUserID());
                newPost.setHeading(title);
                newPost.setContent(description);
                newPost.setCreatedDate(new java.util.Date());

                boolean success = postDAO.createPost(newPost);

                if (success) {
                    response.sendRedirect("profile?tab=overview&msg=post_created");
                } else {
                    response.sendRedirect("profile?tab=overview&msg=post_failed");
                }
                return; // Quan trọng: return ngay sau khi xử lý xong
            }

            // Các xử lý khác
            CloudinaryConfig cloudinaryConfig = new CloudinaryConfig();

            if ("deleteReport".equals(action)) {
                int reportID = Integer.parseInt(request.getParameter("reportID"));
                boolean deleted = reportDAO.deleteReportByID(reportID);
                response.sendRedirect("profile?tab=reported&msg=" + (deleted ? "deleted" : "fail"));
                return;
            }

            // Xử lý avatar và cover photo
            Part avatarPart = request.getPart("avatar");
            Part coverPart = request.getPart("coverPhoto");

            boolean updated = false;

            if (avatarPart != null && avatarPart.getSize() > 0) {
                String avatarUrl = cloudinaryConfig.convertMediaToUrl(avatarPart);
                user.setAvatar(avatarUrl);
                userDAO.updateAvatar(user.getUserID(), avatarUrl);
                updated = true;
            }

            if (coverPart != null && coverPart.getSize() > 0) {
                String coverUrl = cloudinaryConfig.convertMediaToUrl(coverPart);
                user.setCoverPhoto(coverUrl);
                userDAO.updateCoverPhoto(user.getUserID(), coverUrl);
                updated = true;
            }

            if (updated) {
                session.setAttribute("user", user);
                response.sendRedirect("profile?tab=overview&msg=updated");
            } else {
                response.sendRedirect("profile?tab=overview&msg=nochange");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("profile?tab=overview&msg=error");
        }
    }



}