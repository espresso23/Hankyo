package controller;

import cloud.CloudinaryConfig;
import dao.DocumentaryDAO;
import model.Documentary;
import model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/upload-document")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 150,      // 150MB
        maxRequestSize = 1024 * 1024 * 200    // 200MB
)
public class UploadDocumentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }
        DocumentaryDAO dao = new DocumentaryDAO();
        List<String> types = dao.getAllDistinctTypes();

        // Đẩy vào request cho JSP
        request.setAttribute("types", types);
        request.getRequestDispatcher("upload-document.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"admin".equals(user.getRole())) {
            request.setAttribute("errorMessage", "❌ Bạn không có quyền upload tài liệu.");
            request.getRequestDispatcher("upload-document.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String type = request.getParameter("type");
        String content = request.getParameter("content");

        Part filePart = request.getPart("file");
        Part audioPart = request.getPart("audio");
        Part thumbnailPart = request.getPart("thumbnail");

        if (type == null || type.trim().isEmpty()) {
            request.setAttribute("errorMessage", "❌ Loại tài liệu không được để trống.");
            request.getRequestDispatcher("upload-document.jsp").forward(request, response);
            return;
        }

        try {
            CloudinaryConfig cloudinaryService = new CloudinaryConfig();

            String sourceUrl = null;
            String audioUrl = null;
            String thumbnailUrl = null;

            if (filePart != null && filePart.getSize() > 0) {
                sourceUrl = cloudinaryService.convertMediaToUrl(filePart);
            }

            if (audioPart != null && audioPart.getSize() > 0) {
                audioUrl = cloudinaryService.convertMediaToUrl(audioPart);
            }

            if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
                thumbnailUrl = cloudinaryService.convertMediaToUrl(thumbnailPart);
            }

            // Create Documentary object
            Documentary doc = new Documentary(
                    0, title, author, sourceUrl, content, type, audioUrl, thumbnailUrl
            );

            DocumentaryDAO dao = new DocumentaryDAO();
            dao.insertDocument(doc);

            response.sendRedirect("documents");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "❌ Upload thất bại: " + e.getMessage());
            request.getRequestDispatcher("upload-document.jsp").forward(request, response);
        }
    }
}
