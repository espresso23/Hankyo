package java.controller;

import dao.UserDAO;
import model.Learner;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            // Redirect based on user role
            if ("Expert".equals(user.getRole())) {
                response.sendRedirect("expert.jsp"); // Redirect to admin page
            } else if("Admin".equals(user.getRole())) {
                response.sendRedirect("admin.jsp"); // Redirect to admin page
            }
            else {
                response.sendRedirect("home.jsp");
            }
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Username = request.getParameter("Username");
        String Password = request.getParameter("Password");

        request.removeAttribute("errorMsg");

        if (Username == null || Password == null || Username.trim().isEmpty() || Password.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Please enter both username and password.");
            request.getRequestDispatcher("welcome.jsp").forward(request, response);
            return;
        }

        UserDAO userDao = new UserDAO();
        boolean verify = userDao.login(Username, Password);

        if (verify) {
            try {
                User user = userDao.getUserByUserName(Username);
                if (user != null) {

                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate(); // Xóa session hiện tại nếu có
                    }
                    session = request.getSession(true);
                    session.setMaxInactiveInterval(1800);
                    session.setAttribute("user", user);
                    session.setAttribute("userID", user.getUserID());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("gmail", user.getGmail());
                    session.setAttribute("phone", user.getPhone());
                    session.setAttribute("role", user.getRole());
                    session.setAttribute("fullName", user.getFullName());
                    session.setAttribute("gender", user.getGender());

                    // Redirect based on user role
                    if ("Expert".equals(user.getRole())) {
                        response.sendRedirect("expert.jsp"); // Redirect to Expert page
                    }
                    else if("Admin".equals(user.getRole())) {
                        response.sendRedirect("admin.jsp");
                    }
                    else {
                        Learner learner = userDao.getLearnerByUserID(user.getUserID());
                        session.setAttribute("learnerID", learner.getLearnerID());
                        response.sendRedirect("home.jsp");
                        System.out.println("learnID" + learner.getLearnerID());
                    }

                } else {
                    request.setAttribute("msg", "Login failed. User not found.");
                    request.getRequestDispatcher("welcome.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                request.setAttribute("msg", "An error occurred while processing your login.");
                request.getRequestDispatcher("welcome.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("msg", "Incorrect username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
