package controller;
import dao.UserDAO;
import model.User;
import util.SmtpProtocol;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String p = request.getParameter("p");
        if ("hadaccount".equals(p)) {
            request.getRequestDispatcher("login").forward(request, response);
        } else {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("gmail");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");

        System.out.println("Received data: " + fullName + ", " + username + ", " + email + ", " + phone + ", " + gender);

        UserDAO dao = new UserDAO();
        try {
            // Check username existence
            if (dao.usernameExists(username)) {
                request.setAttribute("msg", "Username already exists. Please choose another username.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Check email existence
            if (dao.userExists(email)) {
                request.setAttribute("msg", "Email already exists. Please use another email address.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Create new user
            User user = new User();
            user.setFullName(fullName);
            user.setUsername(username);
            user.setGmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setGender(gender);

            // Send verification email
            SmtpProtocol smtpProtocol = new SmtpProtocol();
            System.out.println("Sending verification email...");
            int verifyCode = smtpProtocol.sendMail(email);
            System.out.println("Verification code sent: " + verifyCode);

            // Store in session
            HttpSession session = request.getSession();
            session.setAttribute("gmail", email);
            session.setAttribute("user", user);
            session.setAttribute("otpCode", verifyCode);
            
            // Redirect to verification page
            request.getRequestDispatcher("verify").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("msg", "Database error occurred. Please try again later.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
