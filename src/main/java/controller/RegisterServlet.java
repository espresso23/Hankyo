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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String p = request.getParameter("p");
        if ("hadaccount".equals(p)) {
            request.getRequestDispatcher("/welcome.jsp").forward(request, response);
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

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || gender.isEmpty()) {
            request.setAttribute("message", "Please fill out all fields");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("message", "Passwords do not match");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("msg", "Invalid email format.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }


        UserDAO dao = new UserDAO();
        try {
            System.out.println("Checking if username exists: " + username);
            if (dao.usernameExists(username)) {
                request.setAttribute("message", "Username already exists.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            try {
                System.out.println("Checking if email exists: " + email);
                if (dao.userExists(email)) {
                    request.setAttribute("msg", "Email already exists.");
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

                SmtpProtocol smtpProtocol = new SmtpProtocol();
                System.out.println("Sending verification email...");
                int verifyCode = smtpProtocol.sendMail(email);
                System.out.println("Verification code sent: " + verifyCode);

                HttpSession session = request.getSession();
                session.setAttribute("gmail", email);
                session.setAttribute("user", user);
                session.setAttribute("otpCode", verifyCode);
                System.out.println("Redirecting to Verify page...");
                request.getRequestDispatcher("verify.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", "Error occurred: " + e.getMessage());
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error occurred: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }

    }
}

