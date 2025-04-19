package controller;

import dao.CourseContentDAO;
import model.CourseContent;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/edit-image")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class EditImageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            int courseID = Integer.parseInt(request.getParameter("courseID"));

            // Lấy thông tin hình ảnh từ database
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent imageContent = courseContentDAO.getCourseContentById(contentID);

            // Truyền dữ liệu sang JSP
            request.setAttribute("imageContent", imageContent);
            request.setAttribute("courseID", courseID);

            // Chuyển hướng đến trang editImage.jsp
            request.getRequestDispatcher("editImage.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải hình ảnh");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int courseID = Integer.parseInt(request.getParameter("courseID"));

        switch (action) {
            case "updateImage":
                try {
                    handleUpdateImage(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                break;
        }
    }

    private void handleUpdateImage(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, ServletException, SQLException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");

            // Lấy thông tin hình ảnh hiện tại
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent currentContent = courseContentDAO.getCourseContentById(contentID);

            // Cập nhật thông tin cơ bản
            currentContent.setTitle(title);
            currentContent.setDescription(description);

            // Kiểm tra xem có hình ảnh mới được tải lên không
            Part newImagePart = request.getPart("image");
            if (newImagePart != null && newImagePart.getSize() > 0) {
                // Có hình ảnh mới, tải lên và cập nhật URL
                String newImageUrl = courseContentDAO.convertMediaToUrl(newImagePart);
                currentContent.setMedia(newImageUrl);
            }

            // Cập nhật vào database
            courseContentDAO.updateCourseContent(currentContent);

            // Thêm thông báo thành công và chuyển hướng
            request.setAttribute("successMessage", "Cập nhật hình ảnh thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Lỗi database: " + e.getMessage());
            response.sendRedirect("edit-image?contentID=" + request.getParameter("contentID") + "&courseID=" + request.getParameter("courseID"));
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
        }
    }
} 