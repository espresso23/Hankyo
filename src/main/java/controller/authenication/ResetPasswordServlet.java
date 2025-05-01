package controller.authenication;

import dao.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/reset")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.setAttribute("errorMsg", "Session expired or invalid request. Please try again.");
            request.getRequestDispatcher("/forgot.jsp").forward(request, response);
            return;
        }

        String newPassword = request.getParameter("new-password");
        String confirmPassword = request.getParameter("confirm-password");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMsg", "Passwords do not match. Please try again.");
            request.getRequestDispatcher("/resetpassword.jsp").forward(request, response);
            return;
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            User user = (User) session.getAttribute("user");

            // Update the user's password
            UserDAO userDao = new UserDAO();
            try {
                userDao.updatePassword(user, newPassword);
                System.out.println("User password updated successfully.");
                request.removeAttribute("user"); // Clear user from session
                // Redirect to the login page or success page
                request.setAttribute("msg", "Password updated successfully.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "Error updating password. Please try again.");
                request.getRequestDispatcher("/resetpassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMsg", "Password cannot be empty. Please try again.");
            request.getRequestDispatcher("/resetpassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        request.getRequestDispatcher("/resetpassword.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles password reset functionality";
    }
}