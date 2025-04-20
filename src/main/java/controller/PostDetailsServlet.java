package controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dao.CommentDAO;
import dao.PostDAO;
import dao.UserDAO;
import model.Comment;
import model.Post;
import model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;


@WebServlet(name = "PostDetailsServlet", urlPatterns = {"/postDetails"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class PostDetailsServlet extends HttpServlet {

    private PostDAO postDAO = new PostDAO();
    private CommentDAO commentDAO = new CommentDAO();
    private UserDAO userDAO = new UserDAO();
    private static final String SAVE_DIR = "blogImg";

    private Cloudinary cloudinary;

    @Override
    public void init(ServletConfig config) throws ServletException {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dchi76opz",
                "api_key", "625223392633453",
                "api_secret", "o9itY2xiaJMVu0pY660gYEfaX0I"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String postIdParam = request.getParameter("postID");
            if (postIdParam != null) {
                int postId = Integer.parseInt(postIdParam);
                Post post = postDAO.getPostById(postId);

                if (post != null) {
                    String fullName = postDAO.getFullNameByPostId(postId);
                    String avtURL = postDAO.getAvatarByPostId(postId);

                    request.setAttribute("postID", postIdParam);
                    request.setAttribute("post", post);
                    request.setAttribute("fullName", fullName);
                    request.setAttribute("avatar", avtURL);

                    List<Comment> comments = commentDAO.getCommentsByPostID(postId);
                    request.setAttribute("comments", comments);

                    request.getRequestDispatcher("blogDetails.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing post ID");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // Null check for the action parameter
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
            return;
        }
        switch (action) {
            case "addComment":
                handleAddComment(request, response);
                break;
            case "editComment":
                handleEditComment(request, response);
                break;
            case "deleteComment":
                handleDeleteComment(request, response);
                break;
            case "addPost":
                handleAddPost(request, response);
                break;
            case "editPost":
                handleEditPost(request, response);
                break;
            case "deletePost":
                handleDeletePost(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void handleAddComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String commentText = request.getParameter("commentInput");
            String postIdParam = request.getParameter("postID");

            if (postIdParam == null || commentText == null || commentText.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing post ID or comment text");
                return;
            }

            int postId = Integer.parseInt(postIdParam);
            int userID = loggedUser.getUserID();
            String userFullName = userDAO.getFullNameByUserId(userID);
            String userAvtURL = userDAO.getAvatarByUserId(userID);

            Comment comment = new Comment(userID, userFullName, userAvtURL, postId, commentText);
            comment.setCreatedDate(new Date());
            boolean isAdded = commentDAO.addComment(comment);

            if (isAdded) {
                response.sendRedirect("postDetails?postID=" + postId);
            } else {
                request.setAttribute("error", "Failed to post comment. Please try again.");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }
    }

    private void handleEditComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int commentID = Integer.parseInt(request.getParameter("commentID"));
            String commentContent = request.getParameter("commentContent");
            int postID = Integer.parseInt(request.getParameter("postID"));

            // Verify that the user owns this comment
            Comment existingComment = commentDAO.getCommentByID(commentID);
            if (existingComment == null || existingComment.getUserID() != loggedUser.getUserID()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot edit this comment");
                return;
            }

            boolean updated = commentDAO.updateComment(commentID, commentContent);

            if (updated) {
                response.sendRedirect("postDetails?postID=" + postID);
            } else {
                request.setAttribute("error", "Error updating comment.");
                doGet(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }
    }

    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int commentId = Integer.parseInt(request.getParameter("commentID"));
            int postID = Integer.parseInt(request.getParameter("postID"));

            // Verify that the user owns this comment or is the post owner
            Comment existingComment = commentDAO.getCommentByID(commentId);
            Post post = postDAO.getPostById(postID);

            if (existingComment == null ||
                    (existingComment.getUserID() != loggedUser.getUserID() && post.getUserID() != loggedUser.getUserID())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot delete this comment");
                return;
            }

            boolean success = commentDAO.deleteComment(commentId);

            if (success) {
                response.sendRedirect("postDetails?postID=" + postID);
            } else {
                request.setAttribute("error", "Failed to delete the comment.");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }
    }

    private void handleAddPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Part filePart = request.getPart("imgPost");

            if (title == null || description == null || filePart == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
                return;
            }

            int userID = loggedUser.getUserID();

            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileStream = filePart.getInputStream();
                byte[] fileBytes = fileStream.readAllBytes();

                Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());

                String imageUrl = (String) uploadResult.get("url");

                Post newPost = new Post();
                newPost.setUserID(userID);
                newPost.setImgURL(imageUrl); // Use the Cloudinary URL
                newPost.setHeading(title);
                newPost.setContent(description);
                newPost.setCreatedDate(new Date());

                boolean isAdded = postDAO.createPost(newPost);

                if (isAdded) {
                    response.sendRedirect("blog.jsp");
                } else {
                    request.setAttribute("error", "Failed to add post. Please try again.");
                    request.getRequestDispatcher("blog.jsp").forward(request, response);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image file is required.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while processing your request.");
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String postIdParam = request.getParameter("postID");

            if (postIdParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing post ID");
                return;
            }

            int postID = Integer.parseInt(postIdParam);

            // Check if the logged-in user owns this post
            Post post = postDAO.getPostById(postID);
            if (post == null || post.getUserID() != loggedUser.getUserID()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot delete this post");
                return;
            }

            boolean success = postDAO.deletePost(postID);

            if (success) {
                response.sendRedirect("blog.jsp");
            } else {
                request.setAttribute("error", "Failed to delete the post. Please try again.");
                request.getRequestDispatcher("blog.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while processing your request.");
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    private void handleEditPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Part filePart = request.getPart("imgPost");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String postIdParam = request.getParameter("postID");

            if (postIdParam == null || title == null || description == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
                return;
            }

            int postId = Integer.parseInt(postIdParam);

            // Check if the logged-in user owns this post
            Post existingPost = postDAO.getPostById(postId);
            if (existingPost == null || existingPost.getUserID() != loggedUser.getUserID()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot edit this post");
                return;
            }

            Post updatedPost = new Post();
            updatedPost.setPostID(postId);
            updatedPost.setUserID(loggedUser.getUserID());
            updatedPost.setHeading(title);
            updatedPost.setContent(description);

            // Keep the old image if no new image is uploaded
            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileStream = filePart.getInputStream();
                byte[] fileBytes = fileStream.readAllBytes();

                Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());

                String imageUrl = (String) uploadResult.get("url");
                updatedPost.setImgURL(imageUrl);
            } else {
                updatedPost.setImgURL(existingPost.getImgURL());
            }

            boolean success = postDAO.updatePost(updatedPost);

            if (success) {
                response.sendRedirect("postDetails?postID=" + postId);
            } else {
                request.setAttribute("error", "Failed to update the post.");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while processing your request.");
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles blog details and comments";
    }
}