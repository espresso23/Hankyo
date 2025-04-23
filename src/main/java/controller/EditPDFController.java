package controller;

import dao.CourseContentDAO;
import model.CourseContent;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/edit-pdf")
public class EditPDFController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            int courseID = Integer.parseInt(request.getParameter("courseID"));

            // Lấy thông tin PDF từ database
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent pdfContent = courseContentDAO.getCourseContentById(contentID);

            // Truyền dữ liệu sang JSP
            request.setAttribute("pdfContent", pdfContent);
            request.setAttribute("courseID", courseID);

            // Chuyển hướng đến trang editPdf.jsp
            request.getRequestDispatcher("editPdf.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải PDF");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int courseID = Integer.parseInt(request.getParameter("courseID"));

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
    }
    
    private void handleUpdatePDF(HttpServletRequest request, HttpServletResponse response, int courseID) 
            throws IOException, ServletException, SQLException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            // Lấy thông tin PDF hiện tại
            CourseContentDAO courseContentDAO = new CourseContentDAO();
            CourseContent currentContent = courseContentDAO.getCourseContentById(contentID);
            
            // Cập nhật thông tin cơ bản
            currentContent.setTitle(title);
            currentContent.setDescription(description);
            
            // Kiểm tra xem có PDF mới được tải lên không
            Part newPdfPart = request.getPart("pdf");
            if (newPdfPart != null && newPdfPart.getSize() > 0) {
                // Có PDF mới, tải lên và cập nhật URL
                String newPdfUrl = courseContentDAO.convertMediaToUrl(newPdfPart);
                currentContent.setMedia(newPdfUrl);
            }
            
            // Cập nhật vào database
            courseContentDAO.updateCourseContent(currentContent);
            
            // Thêm thông báo thành công và chuyển hướng
            request.getSession().setAttribute("successMessage", "Cập nhật PDF thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
            
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi database: " + e.getMessage());
            response.sendRedirect("edit-pdf?contentID=" + request.getParameter("contentID") + "&courseID=" + request.getParameter("courseID"));
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
        }
    }
} 