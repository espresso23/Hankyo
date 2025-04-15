package controller;

import dao.ExpertDAO;
import model.Expert;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/expert")
public class ExpertController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại mà không tạo mới nếu không tồn tại
        HttpSession session = request.getSession(false);

        // Kiểm tra session và expert trong session
        if (session == null || session.getAttribute("expert") == null) {
            System.out.println("Session is null or Expert is not set in session!");
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin expert từ session
        Expert expert = (Expert) session.getAttribute("expert");
        System.out.println("Expert found: " + expert.getExpertID());

        ExpertDAO expertDAO = new ExpertDAO();
        try {
            // Cập nhật thông tin expert từ database
            Expert updatedExpert = expertDAO.getExpertById(expert.getExpertID());
            if (updatedExpert != null) {
                // Cập nhật thông tin trong session và request
                session.setAttribute("expert", updatedExpert);
                request.setAttribute("expert", updatedExpert);

                // Forward đến trang JSP mà không thay đổi URL
                request.getRequestDispatcher("expert.jsp").forward(request, response);
            } else {
                System.out.println("Expert not found in DB.");
                session.setAttribute("errorMsg", "Không tìm thấy thông tin chuyên gia trong cơ sở dữ liệu.");
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Đã xảy ra lỗi khi truy cập thông tin chuyên gia.");
            response.sendRedirect("error.jsp");
        }
    }
}