package controller.documentary;

import dao.DocumentaryDAO;
import model.Documentary;
import model.Learner;
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
            boolean isVIP = isUserVIP(session);

            // Truy vấn chi tiết tài liệu
            if (docIDParam != null) {
                int docID = Integer.parseInt(docIDParam);
                Documentary doc = documentaryDAO.getDocumentById(docID);

                if (doc == null) {
                    response.sendRedirect("documents");
                    return;
                }

                if ("VIP Documents".equalsIgnoreCase(doc.getType()) && !isVIP && !isAdmin(user)) {
                    request.setAttribute("vipOnly", true); // thông báo overlay trong JSP
                }

                request.setAttribute("document", doc);
                request.getRequestDispatcher("document-detail.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách loại tài liệu
            List<String> allTypes = new ArrayList<>(documentaryDAO.getAllDocumentTypes());
            request.setAttribute("documentTypes", allTypes);

            // Lọc theo loại nếu có
            List<Documentary> docs;
            if (filterType != null && !filterType.isEmpty()) {
                docs = documentaryDAO.getDocumentsByType(filterType);
            } else {
                docs = documentaryDAO.getAllDocuments(); // luôn hiển thị toàn bộ
            }

            request.setAttribute("documents", docs);
            request.setAttribute("isVIP", isVIP);
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
        Learner learner = (Learner) session.getAttribute("learner");
        if (learner == null) return false;

        try {
            int learnerID = documentaryDAO.getLearnerIdByUserId(learner.getLearnerID());
            return documentaryDAO.isLearnerVIP(learnerID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
