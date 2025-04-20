package controller;

import dao.UserDAO;
import model.GoogleLogin;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/google")
public class GoogleLoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            forwardWithMessage(request, response, "Login failed. Invalid authorization code.");
            return;
        }

        try {
            String accessToken = GoogleLogin.getToken(code);
            User user = GoogleLogin.getUserInfo(accessToken);

            if (user == null) {
                forwardWithMessage(request, response, "Login failed. User not found.");
                return;
            }

            System.out.println("Google Login Successful: " + user.displayInfo()); // Log thông tin user

            UserDAO userDao = new UserDAO();
            if (userDao.isUserBannedSocial(user.getSocialID())) {
                forwardWithMessage(request, response, "Your account is banned.");
                return;
            }

            createUserSession(request, user);
            response.sendRedirect("courseHeader.jsp");

        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi đầy đủ trong console
            forwardWithMessage(request, response, "An error occurred while processing your login.");
        }
    }

    private void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(1800);
        session.setAttribute("user", user);
        session.setAttribute("userID", user.getUserID());
        session.setAttribute("username", user.getFullName());
        session.setAttribute("email", user.getGmail());
        session.setAttribute("phone", user.getPhone());
        session.setAttribute("avatar", user.getAvatar());

        System.out.println("Session Created: " + user.getFullName() + " (ID: " + user.getUserID() + ")"); // Log session
    }

    private void forwardWithMessage(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        System.out.println("Login Error: " + message); // Log lỗi login
        request.setAttribute("msg", message);
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
