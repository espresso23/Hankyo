package controller;

import dao.UserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;

@WebServlet("/update")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ChangeInforServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ChangeInforServlet.class.getName());
    private static final String SAVE_DIR = "uploadFiles";
    UserDAO userDAO = new UserDAO();
    User user;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userID = (session != null) ? (Integer) session.getAttribute("userID") : null;

        String userIDParam = request.getParameter("userID");
        if (userID == null && userIDParam != null) {
            try {
                userID = Integer.parseInt(userIDParam);
                if (session != null) {
                    session.setAttribute("userID", userID);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMsg", "User ID không hợp lệ.");
                request.getRequestDispatcher("home.jsp").forward(request, response);
                return;
            }
        }

        if (userID != null) {
            try {
                user = userDAO.getUserByID(userID);
                request.setAttribute("user", user);
                request.getRequestDispatcher("update.jsp").forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Lỗi truy vấn dữ liệu người dùng", e);
            }
        } else {
            request.setAttribute("errorMsg", "User ID bị thiếu.");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost method called");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userID = (int) session.getAttribute("userID");

        String action = request.getParameter("action");
        String gmail = request.getParameter("gmail");
        String phone = request.getParameter("phone");
        String fullName = request.getParameter("fullName");
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        String reNewPassword = request.getParameter("reNewPassword");
        Part filePart = request.getPart("avatar");
        String gender = request.getParameter("gender");

        Date dateOfBirth = null;
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            dateOfBirth = Date.valueOf(dateOfBirthStr);
        }

        if ("save".equals(action)) {
            try {
                user = userDAO.getUserByID(userID);
            } catch (SQLException e) {
                session.setAttribute("errorMsg", "Error fetching user data: " + e.getMessage());
                response.sendRedirect("update.jsp");
                return;
            }

            boolean isUpdated = false;

            if (phone != null && !phone.isEmpty() && !phone.equals(user.getPhone())) {
                user.setPhone(phone);
                isUpdated = true;
            }
            if (fullName != null && !fullName.isEmpty() && !fullName.equals(user.getFullName())) {
                user.setFullName(fullName);
                isUpdated = true;
            }
            if (dateOfBirth != null && !dateOfBirth.equals(user.getDateOfBirth())) {
                user.setDateOfBirth(dateOfBirth);
                isUpdated = true;
            }
            if (gender != null && !gender.isEmpty() && !gender.equals(user.getGender())) {
                user.setGender(gender);
                isUpdated = true;
            }

            // Handle file upload
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + extractFileName(filePart);
                String appPath = getServletContext().getRealPath("");
                String savePath = appPath + File.separator + SAVE_DIR;

                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdir();
                }

                String filePath = savePath + File.separator + fileName;
                filePart.write(filePath);
                user.setAvatar(SAVE_DIR + File.separator + fileName);
                isUpdated = true;
            }

            // Handle password update
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!userDAO.verifyPassword(user, oldPassword)) {
                    session.setAttribute("errorMsg", "Mật khẩu cũ không đúng.");
                    response.sendRedirect("update.jsp");
                    return;
                }
                if (!newPassword.equals(reNewPassword)) {
                    session.setAttribute("errorMsg", "Mật khẩu mới và nhập lại mật khẩu không khớp.");
                    response.sendRedirect("update.jsp");
                    return;
                }
                try {
                    userDAO.updatePassword(user, newPassword);
                    isUpdated = true;
                } catch (SQLException e) {
                    session.setAttribute("errorMsg", "Lỗi cập nhật mật khẩu: " + e.getMessage());
                    response.sendRedirect("update.jsp");
                    return;
                }
            }

            try {
                if (isUpdated) {
                    userDAO.updateUserProfile(user);
                    session.setAttribute("user", user);
                    session.setAttribute("msg", "Cập nhật thông tin thành công!");
                } else {
                    session.setAttribute("msg", "Không có thay đổi nào được lưu.");
                }
                response.sendRedirect("home.jsp");
            } catch (Exception e) {
                session.setAttribute("errorMsg", "Lỗi cập nhật hồ sơ: " + e.getMessage());
                response.sendRedirect("update.jsp");
            }
        }

    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1).replace("\"", "");
            }
        }
        return "";
    }
}
