package controller.documentary;

import dao.DocumentaryDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete-document")
public class DeleteDocumentServlet extends HttpServlet {
    private DocumentaryDAO documentaryDAO;

    @Override
    public void init() throws ServletException {
        documentaryDAO = new DocumentaryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra quyền admin
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String docIDParam = request.getParameter("docID");
        if (docIDParam == null || docIDParam.trim().isEmpty()) {
            response.sendRedirect("documents");
            return;
        }

        try {
            int docID = Integer.parseInt(docIDParam);
            boolean success = documentaryDAO.deleteDocument(docID);

            if (success) {
                session.setAttribute("message", "Xóa tài liệu thành công!");
            } else {
                session.setAttribute("error", "Không thể xóa tài liệu. Vui lòng thử lại sau.");
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra khi xóa tài liệu.");
        }

        response.sendRedirect("documents");
    }
} 