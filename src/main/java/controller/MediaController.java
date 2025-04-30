package controller;

import cloud.CloudinaryConfig;
import com.google.gson.Gson;
import dao.MediaDAO;
import model.Media;
import model.User;
import util.DBConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/media-manager")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
public class MediaController extends HttpServlet {
    private CloudinaryConfig cloudinaryConfig;
    private MediaDAO mediaDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        cloudinaryConfig = new CloudinaryConfig();
        Connection conn = DBConnect.getInstance().getConnection();
        mediaDAO = new MediaDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra đăng nhập
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Lấy danh sách media
            List<Media> mediaList = mediaDAO.getAllMedia();
            // Đảm bảo mediaList không null
            if (mediaList == null) {
                mediaList = new ArrayList<>();
            }
            request.setAttribute("mediaList", mediaList);
            
            // Forward to media manager page
            request.getRequestDispatcher("mediaManager.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách media");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Kiểm tra đăng nhập
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            out.write(gson.toJson(Map.of("success", false, "message", "Unauthorized")));
            return;
        }

        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "upload";
            }

            switch (action) {
                case "upload":
                    handleUpload(request, response);
                    break;
                case "delete":
                    handleDelete(request, response);
                    break;
                default:
                    out.write(gson.toJson(Map.of("success", false, "message", "Invalid action")));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write(gson.toJson(Map.of("success", false, "message", e.getMessage())));
        }
    }

    private void handleUpload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        List<Map<String, String>> uploadedFiles = new ArrayList<>();
        
        try {
            for (Part part : request.getParts()) {
                if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                    // Upload to Cloudinary
                    String url = cloudinaryConfig.convertMediaToUrl(part);
                    
                    // Save to database
                    Media media = new Media();
                    media.setFileName(part.getSubmittedFileName());
                    media.setUrl(url);
                    media.setType(part.getContentType().startsWith("image/") ? "image" : "audio");
                    mediaDAO.addMedia(media);
                    
                    // Add to result list
                    Map<String, String> fileInfo = new HashMap<>();
                    fileInfo.put("filename", media.getFileName());
                    fileInfo.put("url", media.getUrl());
                    fileInfo.put("type", media.getType());
                    uploadedFiles.add(fileInfo);
                }
            }
            
            Map<String, Object> response_data = new HashMap<>();
            response_data.put("success", true);
            response_data.put("files", uploadedFiles);
            out.write(gson.toJson(response_data));
            
        } catch (Exception e) {
            e.printStackTrace();
            out.write(gson.toJson(Map.of("success", false, "message", "Upload failed: " + e.getMessage())));
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            int mediaId = Integer.parseInt(request.getParameter("mediaId"));
            mediaDAO.deleteMedia(mediaId);
            out.write(gson.toJson(Map.of("success", true)));
        } catch (Exception e) {
            e.printStackTrace();
            out.write(gson.toJson(Map.of("success", false, "message", "Delete failed: " + e.getMessage())));
        }
    }
} 