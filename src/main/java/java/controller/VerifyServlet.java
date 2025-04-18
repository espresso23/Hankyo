package java.controller;

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

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("otpCode") == null || session.getAttribute("user") == null) {

            request.setAttribute("errorMsg", "Session expired or invalid request. Please try again.");
            request.getRequestDispatcher("verify.jsp").forward(request, response);
            return;
        }

        Integer otp = (Integer) session.getAttribute("otpCode");
        User user = (User) session.getAttribute("user");

        if ("resend".equals(action)) {

            String email = (String) session.getAttribute("gmail");
            SmtpProtocol smtpProtocol = new SmtpProtocol();
            otp = smtpProtocol.sendMail(email);
            session.setAttribute("otpCode", otp);
            request.getRequestDispatcher("verify.jsp").forward(request, response);
            return;
        }

        try {
            int inputOtp = Integer.parseInt(request.getParameter("otp-code"));

            if (otp != null && otp.equals(inputOtp)) {
                UserDAO userDao = new UserDAO();
                try {
                    String result = userDao.register(user);
                    request.setAttribute("msg", result);
                    request.getRequestDispatcher("welcome.jsp").forward(request, response);
                } catch (Exception e) {
                    // Xử lý lỗi khi đăng ký thất bại
                    request.setAttribute("errorMsg", "Registration failed: " + e.getMessage());
                    request.getRequestDispatcher("verify.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMsg", "Invalid OTP. Please try again.");
                request.getRequestDispatcher("verify.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMsg", "Invalid OTP format. Please enter a valid number.");
            request.getRequestDispatcher("verify.jsp").forward(request, response);
        }
    }

        @Override
    public String getServletInfo() {
        return "Handles OTP verification and user registration";
    }
}