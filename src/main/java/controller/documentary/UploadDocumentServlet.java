package controller.documentary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
        fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
        maxFileSize = 1024 * 1024 * 100,       // 100MB
        maxRequestSize = 1024 * 1024 * 150     // 150MB
)
public class UploadDocumentServlet extends HttpServlet {
    private Cloudinary cloudinary;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dchi76opz",
                "api_key", "625223392633453",
                "api_secret", "o9itY2xiaJMVu0pY660gYEfaX0I"
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in and is admin
        if (user == null || !user.getRole().equals("admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("upload-document.jsp").forward(request, response);
    }


private File convertPartToFile(Part part, String fileName) throws IOException {
    File tempFile = File.createTempFile(fileName, null);
    part.write(tempFile.getAbsolutePath());
    return tempFile;
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");

    // Check if user is logged in and is admin
    if (user == null || !user.getRole().equals("admin")) {
        request.setAttribute("errorMessage", "❌ Chỉ quản trị viên mới có quyền upload tài liệu");
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

    String sourceUrl = null;
    String audioUrl = null;
    String thumbnailUrl = null;

    List<String> allowedTypes = Arrays.asList(
            "Từ vựng", "Tiếng Hàn tổng hợp", "TOPIK master",
            "Kyunghee", "Sejong", "Kiip", "Seoul", "Sogang", "Yonsei"
    );
    if (!allowedTypes.contains(type)) {
        response.getWriter().write("❌ Loại tài liệu không hợp lệ.");
        return;
    }
    try {
        if (filePart != null && filePart.getSize() > 0) {
            File pdfFile = convertPartToFile(filePart, "document_" + System.currentTimeMillis());
            Map fileUpload = cloudinary.uploader().upload(
                    pdfFile,
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "use_filename", true,
                            "public_id", "document_" + System.currentTimeMillis()
                    )
            );
            sourceUrl = (String) fileUpload.get("secure_url");
            pdfFile.delete();
        }

        if (audioPart != null && audioPart.getSize() > 0) {
            File audioFile = convertPartToFile(audioPart, "audio_" + System.currentTimeMillis());
            Map audioUpload = cloudinary.uploader().upload(
                    audioFile,
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "use_filename", true,
                            "public_id", "audio_" + System.currentTimeMillis()
                    )
            );
            audioUrl = (String) audioUpload.get("secure_url");
            audioFile.delete();
        }

        if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
            File thumbnailFile = convertPartToFile(thumbnailPart, "thumbnail_" + System.currentTimeMillis());
            Map thumbnailUpload = cloudinary.uploader().upload(
                    thumbnailFile,
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "use_filename", true,
                            "public_id", "thumbnail_" + System.currentTimeMillis()
                    )
            );
            thumbnailUrl = (String) thumbnailUpload.get("secure_url");
            thumbnailFile.delete();
        }

        Documentary doc = new Documentary(
                0, title, author, sourceUrl, content, type, audioUrl, thumbnailUrl);

        DocumentaryDAO dao = new DocumentaryDAO();
        dao.insertDocument(doc);

        response.sendRedirect("documents");

    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("Upload thất bại: " + e.getMessage());
    }
}
}
