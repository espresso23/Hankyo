package controller.courseContentExpert;

import dao.CourseContentDAO;
import model.CourseContent;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/edit-video")
public class EditVideoController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            int courseID = Integer.parseInt(request.getParameter("courseID"));

            // Lấy thông tin video từ database
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent videoContent = courseContentDAO.getCourseContentById(contentID);

            // Truyền dữ liệu sang JSP
            request.setAttribute("videoContent", videoContent);
            request.setAttribute("courseID", courseID);

            // Chuyển hướng đến trang editVideo.jsp
            request.getRequestDispatcher("editVideo.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải video");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int courseID = Integer.parseInt(request.getParameter("courseID"));

        switch (action) {
            case "updateVideo":
                try {
                    handleUpdateVideo(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                break;
        }
    }
    
    private void handleUpdateVideo(HttpServletRequest request, HttpServletResponse response, int courseID) 
            throws IOException, ServletException, SQLException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            // Lấy thông tin video hiện tại
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent currentContent = courseContentDAO.getCourseContentById(contentID);
            
            // Cập nhật thông tin cơ bản
            currentContent.setTitle(title);
            currentContent.setDescription(description);
            
            // Kiểm tra xem có video mới được tải lên không
            Part newVideoPart = request.getPart("video");
            if (newVideoPart != null && newVideoPart.getSize() > 0) {
                // Có video mới, tải lên và cập nhật URL
                String newVideoUrl = courseContentDAO.convertMediaToUrl(newVideoPart);
                currentContent.setMedia(newVideoUrl);
            }
            
            // Cập nhật vào database
            courseContentDAO.updateCourseContent(currentContent);
            
            // Thêm thông báo thành công và chuyển hướng
            request.getSession().setAttribute("successMessage", "Cập nhật video thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
            
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi database: " + e.getMessage());
            response.sendRedirect("edit-video?contentID=" + request.getParameter("contentID") + "&courseID=" + request.getParameter("courseID"));
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
        }
    }
} 