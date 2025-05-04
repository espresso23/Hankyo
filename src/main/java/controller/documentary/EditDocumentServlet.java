package controller.documentary;

import dao.DocumentaryDAO;
import model.Documentary;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/edit-document")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 150,      // 150MB
        maxRequestSize = 1024 * 1024 * 200    // 200MB
)
public class EditDocumentServlet extends HttpServlet {
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
            Documentary doc = documentaryDAO.getDocumentById(docID);
            
            if (doc == null) {
                session.setAttribute("error", "Không tìm thấy tài liệu.");
                response.sendRedirect("documents");
                return;
            }

            List<String> types = documentaryDAO.getAllDistinctTypes();
            request.setAttribute("document", doc);
            request.setAttribute("types", types);
            request.getRequestDispatcher("edit-document.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra khi tải thông tin tài liệu.");
            response.sendRedirect("documents");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String type = request.getParameter("type");
        String content = request.getParameter("content");

        if (docIDParam == null || title == null || author == null || type == null) {
            session.setAttribute("error", "Vui lòng điền đầy đủ thông tin.");
            response.sendRedirect("edit-document?docID=" + docIDParam);
            return;
        }

        try {
            int docID = Integer.parseInt(docIDParam);
            Documentary existingDoc = documentaryDAO.getDocumentById(docID);
            
            if (existingDoc == null) {
                session.setAttribute("error", "Không tìm thấy tài liệu.");
                response.sendRedirect("documents");
                return;
            }

            // Cập nhật thông tin cơ bản
            existingDoc.setTitle(title);
            existingDoc.setAuthor(author);
            existingDoc.setType(type);
            existingDoc.setDocContent(content);

            // Xử lý file upload nếu có
            Part filePart = request.getPart("file");
            Part audioPart = request.getPart("audio");
            Part thumbnailPart = request.getPart("thumbnail");

            // TODO: Xử lý upload file lên Cloudinary hoặc server
            // Giữ nguyên đường dẫn cũ nếu không có file mới

            boolean success = documentaryDAO.updateDocument(existingDoc);

            if (success) {
                session.setAttribute("message", "Cập nhật tài liệu thành công!");
                response.sendRedirect("documents");
            } else {
                session.setAttribute("error", "Không thể cập nhật tài liệu. Vui lòng thử lại sau.");
                response.sendRedirect("edit-document?docID=" + docID);
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra khi cập nhật tài liệu.");
            response.sendRedirect("edit-document?docID=" + docIDParam);
        }
    }
} 