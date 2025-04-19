/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ChatDAO;
import model.Chat;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import org.json.JSONObject;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.io.BufferedReader;
import java.lang.StringBuilder;

/**
 * @author HELLO
 */
@WebServlet("/chat/*")
public class ChatServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChatServlet.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChatServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChatServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Kiểm tra session và userID
        if (session == null || session.getAttribute("userID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy userID từ URL
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            return;
        }

        String userID = pathInfo.substring(1);
        Integer sessionUserID = (Integer) session.getAttribute("userID");

        // Kiểm tra userID từ URL có khớp với session không
        if (!userID.equals(sessionUserID.toString())) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            return;
        }

        // Lấy messages từ database
        ChatDAO dao = new ChatDAO();
        List<Chat> messages = dao.getAllChats();

        // Set attributes cho JSP
        if (!messages.isEmpty()) {
            request.setAttribute("chatID", messages);
            request.setAttribute("messages", messages);
            request.setAttribute("userID", sessionUserID);
            request.setAttribute("fullName", session.getAttribute("fullName"));
            request.getRequestDispatcher("/chat.jsp").forward(request, response);
        } else {
            // Nếu không có messages, vẫn forward đến chat.jsp nhưng không set attributes
            request.getRequestDispatcher("/chat.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            out.print("{\"error\": \"User not logged in\"}");
            return;
        }

        Integer userID = (Integer) session.getAttribute("userID");
        String fullName = (String) session.getAttribute("fullName");
        String message = request.getParameter("message");
        String pictureSend = request.getParameter("pictureSend");

        // Handle WebSocket message format
        if (message == null) {
            try {
                // Read the request body
                StringBuilder jsonData = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        jsonData.append(line);
                    }
                }

                if (jsonData.length() > 0) {
                    JSONObject jsonObject = new JSONObject(jsonData.toString());
                    message = jsonObject.optString("message", "");
                    pictureSend = jsonObject.optString("pictureSend", null);
                    
                    // If pictureSend is base64 data, convert it to a URL
                    if (pictureSend != null && pictureSend.startsWith("data:image")) {
                        try {
                            ChatDAO chatDAO = new ChatDAO();
                            Part filePart = convertBase64ToPart(pictureSend);
                            pictureSend = chatDAO.uploadImageToCloudinary(filePart);
                            LOGGER.info("Image uploaded successfully: " + pictureSend);
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Error converting base64 to image", e);
                            out.print("{\"error\": \"Failed to process image\"}");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing WebSocket data", e);
                out.print("{\"error\": \"Invalid message format\"}");
                return;
            }
        }

        if (message == null || message.trim().isEmpty()) {
            out.print("{\"error\": \"Message cannot be empty\"}");
            return;
        }

        try {
            Chat chat = new Chat(userID, message, fullName, pictureSend);
            ChatDAO chatDAO = new ChatDAO();
            boolean success = chatDAO.save(chat);

            if (success) {
                out.print("{\"status\": \"success\", \"pictureSend\": \"" + pictureSend + "\"}");
            } else {
                out.print("{\"error\": \"Failed to save message\"}");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving chat message", e);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Part convertBase64ToPart(String base64Data) throws IOException {
        // Remove the data:image/xxx;base64, prefix
        String base64Image = base64Data.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        
        // Create a temporary file
        File tempFile = File.createTempFile("chat_img_", ".jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(imageBytes);
        }
        
        // Create a Part object from the temporary file
        return new Part() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(tempFile);
            }
            
            @Override
            public String getContentType() {
                return "image/jpeg";
            }
            
            @Override
            public String getName() {
                return "image";
            }
            
            @Override
            public String getSubmittedFileName() {
                return "chat_image.jpg";
            }
            
            @Override
            public long getSize() {
                return imageBytes.length;
            }
            
            @Override
            public void write(String fileName) throws IOException {
                Files.copy(getInputStream(), Paths.get(fileName));
            }
            
            @Override
            public void delete() throws IOException {
                tempFile.delete();
            }
            
            @Override
            public String getHeader(String name) {
                return null;
            }
            
            @Override
            public Collection<String> getHeaders(String name) {
                return Collections.emptyList();
            }
            
            @Override
            public Collection<String> getHeaderNames() {
                return Collections.emptyList();
            }
        };
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles chat functionality";
    }// </editor-fold>

}