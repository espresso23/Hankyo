package controller.courseContentExpert;

import dao.AssignmentDAO;
import dao.CourseContentDAO;
import model.Assignment;
import model.CourseContent;
import model.Expert;
import cloud.CloudinaryConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.net.URLEncoder;

@WebServlet("/course-content")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 100,      // 100 MB (increased from 10MB)
        maxRequestSize = 1024 * 1024 * 150    // 150 MB (increased from 100MB)
)
public class CourseContentController extends HttpServlet {
    private CourseContent courseContent = new CourseContent();
    private CloudinaryConfig cloudinaryConfig;
    CourseContentDAO courseContentDAO = new CourseContentDAO();

    @Override
    public void init() throws ServletException {
        cloudinaryConfig = new CloudinaryConfig();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        if (expert == null) {
            resp.sendRedirect("login");
            return;
        }
        String action = req.getParameter("action");
        String courseIDStr = req.getParameter("courseID");
        if (courseIDStr == null || courseIDStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số courseID");
            return;
        }
        int courseID = Integer.parseInt(courseIDStr);

        switch (action) {
            case "addContentView":
                try {
                    showAddContentPage(req, resp);
                } catch (SQLException e) {
                    System.out.println("Loi xay ra " + e.getMessage());
                }
                break;
            case "deleteContent":
                try {
                    handleDeleteContent(req, resp, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private void showAddContentPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        CourseContentDAO dao = null;
        try {
            dao = new CourseContentDAO();
            int courseId = Integer.parseInt(request.getParameter("courseID"));
            List<CourseContent> courseContents = dao.listCourseContentsByCourseID(courseId);
            System.out.println(courseContents);
            request.setAttribute("contents", courseContents);
            request.setAttribute("courseID", courseId);
            System.out.println("Open add course content page");
            request.getRequestDispatcher("addCourseContent.jsp").forward(request, response);
        } finally {
            if (dao != null) {
                dao.closeConnection();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int courseID = Integer.parseInt(request.getParameter("courseID"));

        switch (action) {
            case "addVideo":
                try {
                    handleAddMedia(request, response, courseID, "video");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "addPDF":
                try {
                    handleAddMedia(request, response, courseID, "pdf");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "createEmptyAssignment":
                try {
                    handleCreateEmptyAssignment(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "updateVideo":
                try {
                    handleUpdateVideo(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "updateAssignment":
                try {
                    handleUpdateAssignment(request, response, courseID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                break;
        }
    }

    private void handleAddMedia(HttpServletRequest request, HttpServletResponse response, int courseID, String type)
            throws ServletException, IOException, SQLException {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Part filePart = request.getPart(type);

            if (filePart == null || filePart.getSize() == 0) {
                String errorMessage = URLEncoder.encode("Vui lòng chọn file để tải lên", "UTF-8");
                response.sendRedirect("course-content?action=addContentView&courseID=" + courseID + "&error=" + errorMessage);
                return;
            }

            String fileUrl;
            if (type.equals("pdf")) {
                fileUrl = cloudinaryConfig.convertPdfToUrl(filePart);
            } else {
                fileUrl = courseContentDAO.convertMediaToUrl(filePart);
            }

            CourseContent content = new CourseContent();
            content.setTitle(title);
            content.setDescription(description);
            content.setMedia(fileUrl);
            content.setCourseID(courseID);

            CourseContentDAO dao = new CourseContentDAO();
            boolean success = dao.addCourseContent(content);

            if (success) {
                String successMessage = URLEncoder.encode("Thêm nội dung thành công!", "UTF-8");
                response.sendRedirect("course-content?action=addContentView&courseID=" + courseID + "&success=" + successMessage);
            } else {
                String errorMessage = URLEncoder.encode("Có lỗi xảy ra khi thêm nội dung", "UTF-8");
                response.sendRedirect("course-content?action=addContentView&courseID=" + courseID + "&error=" + errorMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = URLEncoder.encode("Lỗi: " + e.getMessage(), "UTF-8");
            response.sendRedirect("course-content?action=addContentView&courseID=" + courseID + "&error=" + errorMessage);
        }
    }

    private boolean isValidFileType(String contentType, String type) {
        switch (type) {
            case "video":
                return contentType.startsWith("video/");
            case "image":
                return contentType.startsWith("image/");
            case "pdf":
                return contentType.equals("application/pdf");
            default:
                return false;
        }
    }

    private void handleCreateEmptyAssignment(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, SQLException {
        AssignmentDAO assignmentDAO = null;
        CourseContentDAO courseContentDAO = null;
        try {
            // 1. Validate và lấy thông tin từ form
            String title = request.getParameter("titleA");
            String description = request.getParameter("descriptionA");

            // 2. Kiểm tra dữ liệu đầu vào
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Tiêu đề assignment không được để trống");
            }

            // 3. Tạo assignment với thông tin từ form
            assignmentDAO = new AssignmentDAO();
            int assignmentID = assignmentDAO.createEmptyAssignment(title, description);

            // 4. Tạo và liên kết course content
            CourseContent assignmentContent = new CourseContent();

            Assignment assignment = new Assignment();
            assignment.setAssignmentID(assignmentID);
            assignment.setAssignmentTitle(title);
            assignmentContent.setAssignment(assignment);
            assignmentContent.setCourseID(courseID);

            // 5. Lưu vào database
            courseContentDAO = new CourseContentDAO();
            courseContentDAO.addCourseContentAssignment(assignmentContent);

            // 6. Chuyển hướng với thông báo thành công
            request.setAttribute("successMessage", "Tạo assignment thành công!");
            response.sendRedirect("edit-assignment?assignmentID=" + assignmentID + "&courseID=" + courseID);

        } catch (NumberFormatException e) {
            handleError(request, response, "ID khóa học không hợp lệ", courseID);
        } catch (IllegalArgumentException e) {
            handleError(request, response, e.getMessage(), courseID);
        } catch (SQLException e) {
            handleError(request, response, "Lỗi database: " + e.getMessage(), courseID);
        } catch (Exception e) {
            handleError(request, response, "Lỗi hệ thống: " + e.getMessage(), courseID);
        } finally {
            if (assignmentDAO != null) {
                assignmentDAO.closeConnection();
            }
            if (courseContentDAO != null) {
                courseContentDAO.closeConnection();
            }
        }
    }

    private void handleUpdateVideo(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws ServletException, IOException, SQLException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Part filePart = request.getPart("video");

            CourseContentDAO dao = new CourseContentDAO();
            CourseContent content = dao.getCourseContentById(contentID);

            if (content == null) {
                request.setAttribute("errorMessage", "Không tìm thấy nội dung cần cập nhật");
                showAddContentPage(request, response);
                return;
            }

            content.setTitle(title);
            content.setDescription(description);

            if (filePart != null && filePart.getSize() > 0) {
                String fileUrl = cloudinaryConfig.convertMediaToUrl(filePart);
                content.setMedia(fileUrl);
            }

            boolean success = dao.updateCourseContent(content);

            if (success) {
                request.setAttribute("successMessage", "Cập nhật nội dung thành công!");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật nội dung");
            }

            showAddContentPage(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            showAddContentPage(request, response);
        }
    }

    private void handleUpdatePdf(HttpServletRequest request, HttpServletResponse response, int courseID) 
            throws ServletException, IOException, SQLException {
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Part filePart = request.getPart("pdf");

            CourseContentDAO dao = new CourseContentDAO();
            CourseContent content = dao.getCourseContentById(contentID);

            if (content == null) {
                request.setAttribute("errorMessage", "Không tìm thấy nội dung cần cập nhật");
                showAddContentPage(request, response);
                return;
            }

            content.setTitle(title);
            content.setDescription(description);

            if (filePart != null && filePart.getSize() > 0) {
                String fileUrl = cloudinaryConfig.convertPdfToUrl(filePart);
                content.setMedia(fileUrl);
            }

            boolean success = dao.updateCourseContent(content);

            if (success) {
                request.setAttribute("successMessage", "Cập nhật nội dung thành công!");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật nội dung");
            }

            showAddContentPage(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            showAddContentPage(request, response);
        }
    }

    private void handleUpdateAssignment(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, SQLException {
        AssignmentDAO assignmentDAO = null;
        try {
            // 1. Cập nhật thông tin cơ bản của Assignment
            Assignment assignment = new Assignment();
            assignment.setAssignmentID(Integer.parseInt(request.getParameter("assignmentID")));
            assignment.setAssignmentTitle(request.getParameter("title"));
            assignment.setDescription(request.getParameter("description"));

            assignmentDAO = new AssignmentDAO();
            assignmentDAO.updateAssignment(assignment);

            // 5. Thêm thông báo thành công và chuyển hướng
            request.setAttribute("successMessage", "Cập nhật Assignment thành công!");
            response.sendRedirect("course-content?action=addContentView&courseID=" + request.getParameter("courseID"));

        } catch (SQLException e) {
            handleError(request, response, "Lỗi database: " + e.getMessage(),
                    "edit-assignment?assignmentID=" + request.getParameter("assignmentID") + "&courseID=" + request.getParameter("courseID"));
        } catch (Exception e) {
            handleError(request, response, "Lỗi hệ thống: " + e.getMessage(),
                    "course-content?action=addContentView&courseID=" + request.getParameter("courseID"));
        } finally {
            if (assignmentDAO != null) {
                assignmentDAO.closeConnection();
            }
        }
    }


    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage, int courseID)
            throws IOException {
        request.setAttribute("errorMessage", errorMessage);
        response.sendRedirect("course-content?action=addContentView&courseID=" + courseID);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage, String redirectUrl)
            throws IOException {
        request.setAttribute("errorMessage", errorMessage);
        response.sendRedirect(redirectUrl);
    }

    private void handleDeleteContent(HttpServletRequest request, HttpServletResponse response, int courseID)
            throws IOException, SQLException {
        CourseContentDAO courseContentDAO = null;
        AssignmentDAO assignmentDAO = null;
        try {
            int contentID = Integer.parseInt(request.getParameter("contentID"));
            String assignmentIDStr = request.getParameter("assignmentID");
            System.out.println("Bắt đầu xóa nội dung với ID: " + contentID);

            courseContentDAO = new CourseContentDAO();

            // Lấy thông tin content trước khi xóa
            CourseContent content = courseContentDAO.getCourseContentById(contentID);
            System.out.println("Thông tin nội dung cần xóa: " + content);

            if (content == null) {
                System.out.println("Không tìm thấy nội dung với ID: " + contentID);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Không tìm thấy nội dung cần xóa!");
                return;
            }

            // Xóa các bản ghi liên quan trước
            if (content.getAssignment() != null) {
                System.out.println("Xóa assignment liên quan: " + content.getAssignment().getAssignmentID());
                assignmentDAO = new AssignmentDAO();
                assignmentDAO.deleteAllQuestions(content.getAssignment().getAssignmentID());
                assignmentDAO.deleteAssignment(content.getAssignment().getAssignmentID());
            }

            // Xóa content
            System.out.println("Xóa nội dung chính");
            boolean success = courseContentDAO.deleteCourseContent(contentID);

            if (success) {
                System.out.println("Xóa nội dung thành công");
                // Xóa file media nếu có
                if (content.getMedia() != null && !content.getMedia().isEmpty()) {
                    System.out.println("Có file media cần xóa: " + content.getMedia());
                    // TODO: Implement delete media file from storage
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Đã xóa nội dung thành công!");
            } else {
                System.out.println("Không thể xóa nội dung");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Không thể xóa nội dung!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Lỗi định dạng ID: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID nội dung không hợp lệ!");
        } catch (SQLException e) {
            System.out.println("Lỗi database: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi database: " + e.getMessage());
        } finally {
            if (courseContentDAO != null) {
                courseContentDAO.closeConnection();
            }
            if (assignmentDAO != null) {
                assignmentDAO.closeConnection();
            }
        }
    }
}
