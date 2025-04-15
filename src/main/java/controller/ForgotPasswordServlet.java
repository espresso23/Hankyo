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

@WebServlet("/forgot")
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("sendResetEmail".equals(action)) {
            String gmail = request.getParameter("gmail");
            UserDAO userDao = new UserDAO();

            try {
                User user = userDao.getUserByEmail(gmail);
                if (user != null) {
                    SmtpProtocol smtpProtocol = new SmtpProtocol();
                    int resetCode = smtpProtocol.sendMailReset(gmail);

                    session.setAttribute("resetCode", resetCode);
                    session.setAttribute("user", user);
                    session.setAttribute("gmail", gmail);
                    request.setAttribute("msg", "Reset code sent to your gmail.");

                    request.getRequestDispatcher("verifyresetcode.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMsg", "Email not found.");
                    request.getRequestDispatcher("forgot.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (session.getAttribute("resetCode") == null || session.getAttribute("user") == null) {
            request.setAttribute("errorMsg", "Session expired or invalid request. Please try again.");
            request.getRequestDispatcher("forgot.jsp").forward(request, response);
            return;
        }

        Integer resetCode = (Integer) session.getAttribute("resetCode");

        if ("verifyresetcode".equals(action)) {
            try {
                int inputResetCode = Integer.parseInt(request.getParameter("otp-code"));
                if (resetCode.equals(inputResetCode)) {
                    session.removeAttribute("resetCode");
                    request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMsg", "Invalid reset code. Please try again.");
                    request.getRequestDispatcher("verifyresetcode.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMsg", "Invalid reset code format. Please enter a valid number.");
                request.getRequestDispatcher("verifyresetcode.jsp").forward(request, response);
            }
        }

        // Handle OTP resend functionality
        if ("resend".equals(action)) {
            String email = (String) session.getAttribute("email"); // Retrieve email from session
            SmtpProtocol smtpProtocol = new SmtpProtocol();
            resetCode = smtpProtocol.sendMailReset(email); // Resend OTP to the same email
            session.setAttribute("resetCode", resetCode); // Update the reset code in session

            request.setAttribute("msg", "Reset code resent to your email.");
            request.getRequestDispatcher("verifyresetcode.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("forgot.jsp").forward(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Handles password reset process";
    }
}
