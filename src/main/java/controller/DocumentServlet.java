package controller;

import dao.DocumentaryDAO;
import dao.UserDAO;
import model.Documentary;
import util.DBConnect;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/documents")
public class DocumentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DocumentaryDAO documentaryDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        documentaryDAO = new DocumentaryDAO();
        userDAO = new UserDAO();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String docIDParam = request.getParameter("docID");
        String filterType = request.getParameter("filterType");
        try {
            boolean isConnected = documentaryDAO.checkConnection();
            if (isConnected) {
                if (docIDParam != null) {
                    int docID = Integer.parseInt(docIDParam);
                    Documentary doc = documentaryDAO.getDocumentById(docID);
                    request.setAttribute("document", doc);
                    request.getRequestDispatcher("document-detail.jsp").forward(request, response);
                } else {
                    List<Documentary> docs;
                    if (filterType != null && !filterType.isEmpty()) {
                        docs = documentaryDAO.getDocumentsByType(filterType); // Lọc theo type
                    } else {
                        docs = documentaryDAO.getAllDocuments();
                    }
                    request.setAttribute("documents", docs);
                    request.setAttribute("documentTypes", documentaryDAO.getAllDocumentTypes());
                    request.getRequestDispatcher("document-list.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
