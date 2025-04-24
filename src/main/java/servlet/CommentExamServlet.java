//package servlet;
//
//import com.google.gson.Gson;
//import dao.CommentExamDAO;
//import model.CommentExam;
//import model.User;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@WebServlet(name = "CommentExamServlet", urlPatterns = {"/comment-exam"})
//public class CommentExamServlet extends HttpServlet {
//    private final CommentExamDAO commentExamDAO = new CommentExamDAO();
//    private final Gson gson = new Gson();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getParameter("action");
//        String examIdStr = request.getParameter("examId");
//
//        if (examIdStr == null || examIdStr.isEmpty()) {
//            sendErrorResponse(response, "Thiếu tham số examId");
//            return;
//        }
//
//        int examId;
//        try {
//            examId = Integer.parseInt(examIdStr);
//        } catch (NumberFormatException e) {
//            sendErrorResponse(response, "Định dạng examId không hợp lệ");
//            return;
//        }
//
//        try {
//            if ("getComments".equals(action)) {
//                List<CommentExam> comments = commentExamDAO.getCommentsByExam(examId);
//                sendJsonResponse(response, comments);
//            } else {
//                sendErrorResponse(response, "Hành động không hợp lệ");
//            }
//        } catch (SQLException e) {
//            sendErrorResponse(response, "Lỗi khi lấy bình luận: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getParameter("action");
//        HttpSession session = request.getSession();
//        User currentUser = (User) session.getAttribute("user");
//
//        if (currentUser == null) {
//            sendErrorResponse(response, "Vui lòng đăng nhập để thực hiện chức năng này");
//            return;
//        }
//
//        try {
//            switch (action) {
//                case "add":
//                    handleAddComment(request, response, currentUser);
//                    break;
//                case "edit":
//                    handleEditComment(request, response, currentUser);
//                    break;
//                case "delete":
//                    handleDeleteComment(request, response, currentUser);
//                    break;
//                case "vote":
//                    handleVoteComment(request, response, currentUser);
//                    break;
//                default:
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
//            }
//        } catch (SQLException e) {
//            sendErrorResponse(response, "Lỗi xử lý: " + e.getMessage());
//        }
//    }
//
//    private void handleAddComment(HttpServletRequest request, HttpServletResponse response, User currentUser)
//            throws SQLException, IOException {
//        String examIdStr = request.getParameter("examId");
//        String content = request.getParameter("content");
//        String parentCommentIdStr = request.getParameter("parentCommentId");
//
//        if (examIdStr == null || examIdStr.isEmpty()) {
//            sendErrorResponse(response, "Thiếu tham số examId");
//            return;
//        }
//        if (content == null || content.trim().isEmpty()) {
//            sendErrorResponse(response, "Nội dung bình luận không được để trống");
//            return;
//        }
//
//        int examId;
//        try {
//            examId = Integer.parseInt(examIdStr);
//        } catch (NumberFormatException e) {
//            sendErrorResponse(response, "Định dạng examId không hợp lệ");
//            return;
//        }
//
//        Integer parentCommentId = null;
//        if (parentCommentIdStr != null && !parentCommentIdStr.trim().isEmpty()) {
//            try {
//                parentCommentId = Integer.parseInt(parentCommentIdStr);
//            } catch (NumberFormatException e) {
//                sendErrorResponse(response, "Định dạng parentCommentId không hợp lệ");
//                return;
//            }
//        }
//
//        CommentExam newComment = commentExamDAO.addComment(examId, currentUser.getUserID(), content, parentCommentId);
//        if (newComment != null) {
//            sendJsonResponse(response, newComment);
//        } else {
//            sendErrorResponse(response, "Không thể thêm bình luận");
//        }
//    }
//
//    private void handleEditComment(HttpServletRequest request, HttpServletResponse response, User currentUser)
//            throws SQLException, IOException {
//        String commentIdStr = request.getParameter("commentId");
//        String newContent = request.getParameter("content");
//
//        if (commentIdStr == null || commentIdStr.isEmpty()) {
//            sendErrorResponse(response, "Thiếu tham số commentId");
//            return;
//        }
//        if (newContent == null || newContent.trim().isEmpty()) {
//            sendErrorResponse(response, "Nội dung bình luận không được để trống");
//            return;
//        }
//
//        int commentId;
//        try {
//            commentId = Integer.parseInt(commentIdStr);
//        } catch (NumberFormatException e) {
//            sendErrorResponse(response, "Định dạng commentId không hợp lệ");
//            return;
//        }
//
//        CommentExam updatedComment = commentExamDAO.editComment(commentId, currentUser.getUserID(), newContent);
//        if (updatedComment != null) {
//            sendJsonResponse(response, updatedComment);
//        } else {
//            sendErrorResponse(response, "Không thể sửa bình luận hoặc bình luận không tồn tại");
//        }
//    }
//
//    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, User currentUser)
//            throws SQLException, IOException {
//        String commentIdStr = request.getParameter("commentId");
//
//        if (commentIdStr == null || commentIdStr.isEmpty()) {
//            sendErrorResponse(response, "Thiếu tham số commentId");
//            return;
//        }
//
//        int commentId;
//        try {
//            commentId = Integer.parseInt(commentIdStr);
//        } catch (NumberFormatException e) {
//            sendErrorResponse(response, "Định dạng commentId không hợp lệ");
//            return;
//        }
//
//        boolean deleted = commentExamDAO.deleteComment(commentId, currentUser.getUserID());
//        Map<String, Object> result = new HashMap<>();
//        result.put("success", deleted);
//        result.put("message", deleted ? "Đã xóa bình luận" : "Không thể xóa bình luận");
//
//        sendJsonResponse(response, result);
//    }
//
//    private void handleVoteComment(HttpServletRequest request, HttpServletResponse response, User currentUser)
//            throws SQLException, IOException {
//        String commentIdStr = request.getParameter("commentId");
//        String voteTypeStr = request.getParameter("voteType");
//
//        if (commentIdStr == null || commentIdStr.isEmpty()) {
//            sendErrorResponse(response, "Thiếu tham số commentId");
//            return;
//        }
//        if (voteTypeStr == null || voteTypeStr.isEmpty()) {
//            sendErrorResponse(response, "Thiếu tham số voteType");
//            return;
//        }
//
//        int commentId;
//        int voteType;
//        try {
//            commentId = Integer.parseInt(commentIdStr);
//            voteType = Integer.parseInt(voteTypeStr);
//        } catch (NumberFormatException e) {
//            sendErrorResponse(response, "Định dạng commentId hoặc voteType không hợp lệ");
//            return;
//        }
//
//        CommentExam votedComment = commentExamDAO.voteComment(currentUser.getUserID(), commentId, voteType);
//        if (votedComment != null) {
//            sendJsonResponse(response, votedComment);
//        } else {
//            sendErrorResponse(response, "Không thể thực hiện vote");
//        }
//    }
//
//    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(gson.toJson(data));
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("success", false);
//        errorResponse.put("message", message);
//        response.getWriter().write(gson.toJson(errorResponse));
//    }
//}