package controller.authentication;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String p = request.getParameter("p");
        LOGGER.info("RegisterServlet doGet called with parameter p=" + p);
        if ("hadaccount".equals(p)) {
            request.getRequestDispatcher("login").forward(request, response);
        } else {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("RegisterServlet doPost called");
        
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("gmail");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");

        LOGGER.info("Received registration data: " + 
            "fullName=" + fullName + ", " +
            "username=" + username + ", " +
            "email=" + email + ", " +
            "phone=" + phone + ", " +
            "gender=" + gender);

        UserDAO dao = new UserDAO();
        try {
            LOGGER.info("Checking if username exists: " + username);
            if (dao.usernameExists(username)) {
                LOGGER.warning("Validation failed: Username already exists");
                request.setAttribute("msg", "Username already exists.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            try {
                LOGGER.info("Checking if email exists: " + email);
                if (dao.userExists(email)) {
                    LOGGER.warning("Validation failed: Email already exists");
                    request.setAttribute("msg", "Email already exists. Please use a different email address.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                User user = new User();
                user.setFullName(fullName);
                user.setUsername(username);
                user.setGmail(email);
                user.setPassword(password);
                user.setPhone(phone);
                user.setGender(gender);

                LOGGER.info("Creating new user object: " + user.toString());

                SmtpProtocol smtpProtocol = new SmtpProtocol();
                LOGGER.info("Sending verification email...");
                int verifyCode = smtpProtocol.sendMail(email);
                LOGGER.info("Verification code sent: " + verifyCode);

                HttpSession session = request.getSession();
                session.setAttribute("gmail", email);
                session.setAttribute("user", user);
                session.setAttribute("otpCode", verifyCode);
                
                LOGGER.info("Session attributes set. Redirecting to verify page...");
                request.getRequestDispatcher("verify").forward(request, response);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error during user registration", e);
                request.setAttribute("message", "Error occurred: " + e.getMessage());
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration process", e);
            request.setAttribute("message", "Error occurred: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
