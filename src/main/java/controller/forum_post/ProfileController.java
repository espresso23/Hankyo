package controller.forum_post;

import cloud.CloudinaryConfig;
import dao.*;
import model.Comment;
import model.Post;
import model.Report;
import model.User;
import model.Expert;
import model.Course;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
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
    private UserDAO userDAO = new UserDAO();
    private ExpertDAO expertDAO = new ExpertDAO();
    private CourseDAO courseDAO = new CourseDAO();

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

        String usernameParam = request.getParameter("user");
        String tab = request.getParameter("tab") != null ? request.getParameter("tab") : "overview";

        try {
            ProfileAccessInfo accessInfo = determineProfileAccess(loggedInUser, usernameParam);
            if (accessInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Người dùng không tồn tại.");
                return;
            }

            request.setAttribute("profileUser", accessInfo.profileUser);
            request.setAttribute("isOwnProfile", accessInfo.isOwnProfile);
            request.setAttribute("user", loggedInUser);

            // Kiểm tra xem user có phải là expert không
            Expert expert = expertDAO.getExpertByUserID(accessInfo.profileUser.getUserID());
            if (expert != null) {
                request.setAttribute("isExpert", true);
                request.setAttribute("expert", expert);
            } else {
                request.setAttribute("isExpert", false);
            }

            setBasicStatistics(request, accessInfo.profileUser);

            handleProfileTabs(request, response, accessInfo.profileUser, tab, accessInfo.isOwnProfile);

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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String action = request.getParameter("action");

            if ("addPost".equals(action)) {
                handleAddPost(request, response, user);
                return;
            }

            if ("deleteReport".equals(action)) {
                handleDeleteReport(request, response);
                return;
            }

            handleProfileMediaUpdate(request, response, user);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("profile?tab=overview&msg=error");
        }
    }

    private ProfileAccessInfo determineProfileAccess(User loggedInUser, String usernameParam) throws SQLException {
        if (usernameParam == null || usernameParam.isEmpty() || usernameParam.equalsIgnoreCase(loggedInUser.getUsername())) {
            return new ProfileAccessInfo(loggedInUser, true);
        }

        User profileUser = userDAO.getUserByUserName(usernameParam);
        if (profileUser == null) {
            return null;
        }

        return new ProfileAccessInfo(profileUser, false);
    }

    private void setBasicStatistics(HttpServletRequest request, User profileUser) throws SQLException {
        request.setAttribute("postCount", postDAO.getPostCountByUserID(profileUser.getUserID()));
        request.setAttribute("commentCount", commentDAO.getCommentCountByUserID(profileUser.getUserID()));
    }

    private void handleProfileTabs(HttpServletRequest request, HttpServletResponse response,
                                   User profileUser, String tab, boolean isOwnProfile)
            throws Exception {

        switch (tab) {
            case "posts":
                List<Post> posts = postDAO.getPostsByUserID(profileUser.getUserID());
                enrichPostsWithAdditionalData(posts);
                request.setAttribute("userPosts", posts);
                request.getRequestDispatcher("posts.jsp").forward(request, response);
                break;

            case "comments":
                List<Comment> comments = commentDAO.getCommentsByUserID(profileUser.getUserID());
                request.setAttribute("userComments", comments);
                request.getRequestDispatcher("comments.jsp").forward(request, response);
                break;

            case "upvoted":
                List<Post> upvotedPosts = postDAO.getUpvotedPostsByUserID(profileUser.getUserID());
                enrichPostsWithCreatorInfo(upvotedPosts);
                request.setAttribute("upvotedPosts", upvotedPosts);
                request.getRequestDispatcher("upvoted.jsp").forward(request, response);
                break;

            case "downvoted":
                List<Post> downvotedPosts = postDAO.getDownvotedPostsByUserID(profileUser.getUserID());
                enrichPostsWithAdditionalData(downvotedPosts);
                request.setAttribute("downvotedPosts", downvotedPosts);
                request.getRequestDispatcher("downvoted.jsp").forward(request, response);
                break;

            case "reported":
                List<Report> reportedPosts = reportDAO.getReportedPostsByUserID(profileUser.getUserID());
                List<Report> reportedComments = reportDAO.getReportedCommentsByUserID(profileUser.getUserID());
                request.setAttribute("reportedPosts", reportedPosts);
                request.setAttribute("reportedComments", reportedComments);
                request.getRequestDispatcher("reported.jsp").forward(request, response);
                break;

            case "courses":
                Expert expert = expertDAO.getExpertByUserID(profileUser.getUserID());
                if (expert != null) {
                    List<Course> courses = courseDAO.getCourseByExpertProfile(expert.getExpertID());
                    request.setAttribute("expertCourses", courses);
                    request.getRequestDispatcher("expert-courses.jsp").forward(request, response);
                } else {
                    response.sendRedirect("profile?user=" + profileUser.getUsername() + "&tab=overview");
                }
                break;

            default: // overview
                List<Post> overviewPosts = postDAO.getPostsByUserID(profileUser.getUserID());
                enrichPostsWithAdditionalData(overviewPosts);
                request.setAttribute("userPosts", overviewPosts);
                request.getRequestDispatcher("overview.jsp").forward(request, response);
                break;
        }
    }

    private void enrichPostsWithAdditionalData(List<Post> posts) {
        for (Post post : posts) {
            post.setCommentCount(postDAO.getCommentCount(post.getPostID()));
        }
    }

    private void enrichPostsWithCreatorInfo(List<Post> posts) throws SQLException {
        for (Post post : posts) {
            post.setCommentCount(postDAO.getCommentCount(post.getPostID()));
            User creator = userDAO.getUserByID(post.getUserID());
            if (creator != null) {
                post.setUserName(creator.getUsername());
                post.setUserFullName(creator.getFullName());
                post.setAvtUserImg(creator.getAvatar());
            }
        }
    }

    private void handleAddPost(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Post newPost = new Post();
        newPost.setUserID(user.getUserID());
        newPost.setHeading(title);
        newPost.setContent(description);
        newPost.setCreatedDate(new java.util.Date());

        boolean success = postDAO.createPost(newPost);

        response.sendRedirect("profile?tab=overview&msg=" + (success ? "post_created" : "post_failed"));
    }

    private void handleDeleteReport(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int reportID = Integer.parseInt(request.getParameter("reportID"));
        boolean deleted = reportDAO.deleteReportByID(reportID);
        response.sendRedirect("profile?tab=reported&msg=" + (deleted ? "deleted" : "fail"));
    }

    private void handleProfileMediaUpdate(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException, ServletException, SQLException {
        CloudinaryConfig cloudinaryConfig = new CloudinaryConfig();
        boolean updated = false;

        Part avatarPart = request.getPart("avatar");
        if (avatarPart != null && avatarPart.getSize() > 0) {
            String avatarUrl = cloudinaryConfig.convertMediaToUrl(avatarPart);
            user.setAvatar(avatarUrl);
            userDAO.updateAvatar(user.getUserID(), avatarUrl);
            updated = true;
        }

        Part coverPart = request.getPart("coverPhoto");
        if (coverPart != null && coverPart.getSize() > 0) {
            String coverUrl = cloudinaryConfig.convertMediaToUrl(coverPart);
            user.setCoverPhoto(coverUrl);
            userDAO.updateCoverPhoto(user.getUserID(), coverUrl);
            updated = true;
        }

        if (updated) {
            request.getSession().setAttribute("user", user);
        }

        response.sendRedirect("profile?tab=overview&msg=" + (updated ? "updated" : "nochange"));
    }

    private static class ProfileAccessInfo {
        User profileUser;
        boolean isOwnProfile;

        ProfileAccessInfo(User profileUser, boolean isOwnProfile) {
            this.profileUser = profileUser;
            this.isOwnProfile = isOwnProfile;
        }
    }
}
