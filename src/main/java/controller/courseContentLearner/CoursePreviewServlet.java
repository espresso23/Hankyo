package controller.courseContentLearner;

import dao.CourseContentDAO;
import model.CourseContent;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/preview")
public class CoursePreviewServlet extends HttpServlet {
    private CourseContentDAO courseContentDAO;

    @Override
    public void init() throws ServletException {
        courseContentDAO = new CourseContentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            int contentID = Integer.parseInt(request.getParameter("contentID"));

            // Lấy nội dung khóa học
            CourseContent content = courseContentDAO.getCourseContentById(contentID);
            if (content == null || content.getCourseID() != courseID) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Nội dung không tồn tại");
                return;
            }

            // Đặt nội dung vào request
            request.setAttribute("content", content);

            // Chuyển hướng đến trang xem trước
            request.getRequestDispatcher("/preview.jsp").forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
    }
} 