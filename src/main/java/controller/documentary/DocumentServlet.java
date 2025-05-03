package controller;

import dao.DocumentaryDAO;
import model.Documentary;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/documents")
public class DocumentServlet extends HttpServlet {
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
        String docIDParam = request.getParameter("docID");
        String filterType = request.getParameter("filterType");

        try {
            // Truy vấn theo docID (xem chi tiết tài liệu)
            if (docIDParam != null) {
                int docID = Integer.parseInt(docIDParam);
                Documentary doc = documentaryDAO.getDocumentById(docID);

                if (doc == null) {
                    response.sendRedirect("documents");
                } else {
                    request.setAttribute("document", doc);
                    request.getRequestDispatcher("document-detail.jsp").forward(request, response);
                }
                return;
            }

            // Lấy danh sách loại tài liệu
            List<String> allTypes = new ArrayList<>(documentaryDAO.getAllDocumentTypes());

            boolean isVIP = isUserVIP(session);
            request.setAttribute("documentTypes", allTypes);


            // Lọc danh sách tài liệu
            List<Documentary> docs;
            if ("VIP Documents".equals(filterType)) {
                if (!isVIP && !isAdmin(user)) {
                    docs = List.of(); // không có quyền xem VIP
                } else {
                    docs = documentaryDAO.getDocumentsByType("VIP Documents");
                }
            } else if (filterType != null && !filterType.isEmpty()) {
                docs = documentaryDAO.getDocumentsByType(filterType);
            } else {
                docs = documentaryDAO.getAllDocuments();
                if (!isVIP && !isAdmin(user)) {
                    // Lọc bỏ tài liệu VIP khỏi danh sách
                    docs.removeIf(d -> "VIP Documents".equalsIgnoreCase(d.getType()));
                }
            }


            request.setAttribute("documents", docs);
            request.getRequestDispatcher("document-list.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("documents");
        }
    }
    private boolean isAdmin(User user) {
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }

    private boolean isUserVIP(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return false;

        try {
            int userID = user.getUserID();
            int learnerID = documentaryDAO.getLearnerIdByUserId(userID);
            return documentaryDAO.isLearnerVIP(learnerID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
