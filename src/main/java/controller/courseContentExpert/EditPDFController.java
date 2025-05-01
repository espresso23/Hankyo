package controller.courseContentExpert;

import dao.CourseContentDAO;
import model.CourseContent;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/edit-pdf")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
public class EditPDFController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String contentIDStr = request.getParameter("contentID");
            String courseIDStr = request.getParameter("courseID");
            
            if (contentIDStr == null || courseIDStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin ID nội dung hoặc khóa học");
                return;
            }
            
            int contentID = Integer.parseInt(contentIDStr);
            int courseID = Integer.parseInt(courseIDStr);

            // Lấy thông tin PDF từ database
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent pdfContent = courseContentDAO.getCourseContentById(contentID);
            
            if (pdfContent == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy nội dung PDF");
                return;
            }

            // Truyền dữ liệu sang JSP
            request.setAttribute("pdfContent", pdfContent);
            request.setAttribute("courseID", courseID);

            // Chuyển hướng đến trang editPdf.jsp
            request.getRequestDispatcher("editPdf.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải PDF");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            String courseIDStr = request.getParameter("courseID");
            
            if (action == null || courseIDStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin action hoặc courseID");
                return;
            }
            
            int courseID = Integer.parseInt(courseIDStr);

            switch (action) {
                case "updatePDF":
                    try {
                        handleUpdatePDF(request, response, courseID);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                    break;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CourseID không hợp lệ");
        }
    }
    
    private void handleUpdatePDF(HttpServletRequest request, HttpServletResponse response, int courseID) 
            throws IOException, ServletException, SQLException {
        try {
            String contentIDStr = request.getParameter("contentID");
            if (contentIDStr == null) {
                throw new IllegalArgumentException("Thiếu thông tin contentID");
            }
            
            int contentID = Integer.parseInt(contentIDStr);
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Tiêu đề không được để trống");
            }
            
            // Lấy thông tin PDF hiện tại
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent currentContent = courseContentDAO.getCourseContentById(contentID);
            
            if (currentContent == null) {
                throw new IllegalArgumentException("Không tìm thấy nội dung PDF");
            }
            
            // Cập nhật thông tin cơ bản
            currentContent.setTitle(title);
            currentContent.setDescription(description);
            
            // Kiểm tra xem có PDF mới được tải lên không
            Part newPdfPart = request.getPart("pdf");
            if (newPdfPart != null && newPdfPart.getSize() > 0) {
                // Kiểm tra định dạng file
                String fileName = newPdfPart.getSubmittedFileName();
                if (!fileName.toLowerCase().endsWith(".pdf")) {
                    throw new IllegalArgumentException("Chỉ chấp nhận file PDF");
                }
                
                // Kiểm tra kích thước file (giới hạn 10MB)
                if (newPdfPart.getSize() > 10 * 1024 * 1024) {
                    throw new IllegalArgumentException("File PDF không được vượt quá 10MB");
                }
                
                // Có PDF mới, tải lên và cập nhật URL
                String newPdfUrl = courseContentDAO.convertMediaToUrl(newPdfPart);
                currentContent.setMedia(newPdfUrl);
            }
            
            // Cập nhật vào database
            courseContentDAO.updateCourseContent(currentContent);
            
            // Thêm thông báo thành công và chuyển hướng
            request.getSession().setAttribute("successMessage", "Cập nhật PDF thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
            
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("edit-pdf?contentID=" + request.getParameter("contentID") + "&courseID=" + courseID);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi database: " + e.getMessage());
            response.sendRedirect("edit-pdf?contentID=" + request.getParameter("contentID") + "&courseID=" + courseID);
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
        }
    }
} 