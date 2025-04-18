package java.controller;

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

@WebServlet("/update")
public class ChangeInforServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ChangeInforServlet.class.getName());
    private static final String SAVE_DIR = "uploadFiles";
    UserDAO userDAO = new UserDAO();

    User user;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userID = (Integer) session.getAttribute("userID");

        // Lấy userID từ request nếu có
        String userIDParam = request.getParameter("userID");
        if (userID == null && userIDParam != null) {
            try {
                userID = Integer.parseInt(userIDParam);
                session.setAttribute("userID", userID); // Lưu userID vào session
            } catch (NumberFormatException e) {
                request.setAttribute("errorMsg", "User ID không hợp lệ.");
                request.getRequestDispatcher("home.jsp").forward(request, response);
                return;
            }
        }
        if (userID != null) {
            try {
                request.setAttribute("userID", userID);
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
        int userID = (int) session.getAttribute("userID");
        String action = request.getParameter("action");
        String gmail = request.getParameter("gmail");
        String phone = request.getParameter("phone");
        String fullName = request.getParameter("fullName");
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        String reNewPassword = request.getParameter("reNewPassword");
        Part filePart = request.getPart("avatar");
        Date dateOfBirth = null;
        String gender = request.getParameter("gender");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            dateOfBirth = Date.valueOf(dateOfBirthStr);  // Chuyển từ String sang java.sql.Date
        }

        if ("save".equals(action)) {
            try {
                user = userDAO.getUserByID(userID);
            } catch (SQLException e) {
                request.setAttribute("errorMsg", "Error fetching user data: " + e.getMessage());
                request.getRequestDispatcher("update.jsp").forward(request, response);
                return;
            }

            boolean isUpdated = false;

            // Handle delete action
            // Update email if it has changed
//            if (gmail != null && !gmail.isEmpty() && !gmail.equals(user.getGmail())) {
//                System.out.println("Updating email from " + user.getGmail() + " to " + gmail);
//
//}

            // Update phone number if it has changed
            if (phone != null && !phone.isEmpty() && !phone.equals(user.getPhone())) {
                System.out.println("Updating phone number from " + user.getPhone() + " to " + phone);
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

            if (gender != null && !gender.isEmpty() && !gender.equals(user.getPhone())) {
                user.setGender(gender);
                isUpdated = true;
            }

            // Handle profile picture upload
            String fileName = extractFileName(filePart);
            if (filePart != null && filePart.getSize() > 0 && !fileName.isEmpty()) {
                String appPath = getServletContext().getRealPath("");
                String savePath = appPath + File.separator + SAVE_DIR;
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdir();
                }
                filePart.write(savePath + File.separator + fileName);
                user.setAvatar(SAVE_DIR + File.separator + fileName);
                logger.info("Profile picture updated: " + user.getAvatar());
                isUpdated = true;
            }


            // Update password if provided and valid
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!userDAO.verifyPassword(user, oldPassword)) {
                    request.setAttribute("errorMsg", "Old password is incorrect.");
                    request.getRequestDispatcher("update.jsp").forward(request, response);
                    return;
                }

                if (!newPassword.equals(reNewPassword)) {
                    request.setAttribute("errorMsg", "New password and confirmation password do not match.");
                    request.getRequestDispatcher("update.jsp").forward(request, response);
                    return;
                }

                try {
                    userDAO.updatePassword(user, newPassword);
                    isUpdated = true;
                    System.out.println("Password updated for user ID: " + userID);
                } catch (SQLException e) {
                    request.setAttribute("errorMsg", "Error updating password: " + e.getMessage());
                    request.getRequestDispatcher("update.jsp").forward(request, response);
                    return;
                }
            }

            // Update the user profile in the database if any changes were made
            try {
                if (isUpdated) {
                    userDAO.updateUserProfile(user);
                    request.getSession().setAttribute("user", user);
                    request.setAttribute("msg", "User updated successfully!");
                } else {
                    request.setAttribute("msg", "No changes detected.");
                }
                request.getRequestDispatcher("/home.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMsg", "Error updating user profile: " + e.getMessage());
                request.getRequestDispatcher("update.jsp").forward(request, response);
            }

        }

    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1).replace("\"", "");
            }
        }
        return "";
    }
}
