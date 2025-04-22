package controller;

import dao.ExpertDAO;
import dao.LearnerDAO;
import dao.UserDAO;
import model.Expert;
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

            if ("expert".equalsIgnoreCase(user.getRole())) {
                request.getRequestDispatcher("expert.jsp").forward(request, response);
            } else if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("admin.jsp");
            } else {
                response.sendRedirect("home.jsp");
            }
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("Username");
        String password = request.getParameter("Password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Vui lòng nhập cả tên đăng nhập và mật khẩu.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        UserDAO userDao = new UserDAO();
        boolean isValid = userDao.login(username, password);

        if (isValid) {
            try {
                User user = userDao.getUserByUserName(username);
                if (user != null) {
                    HttpSession session = request.getSession(true);
                    session.setMaxInactiveInterval(1800);
                    session.setAttribute("user", user);
                    session.setAttribute("userID", user.getUserID());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("gmail", user.getGmail());
                    session.setAttribute("phone", user.getPhone());
                    session.setAttribute("role", user.getRole());
                    session.setAttribute("fullName", user.getFullName());
                    session.setAttribute("gender", user.getGender());

                    System.out.println("User role: " + user.getRole());
                    if ("expert".equalsIgnoreCase(user.getRole())) {
                        ExpertDAO expertDAO = new ExpertDAO();
                        Expert expert = expertDAO.getExpertByUserID(user.getUserID());
                        System.out.println(expert.displayInfo());
                        System.out.println("Expert found: " + (expert != null ? "Yes" : "No"));

                        if (expert != null) {
                            session.setAttribute("expert", expert);
                            request.getRequestDispatcher("expert.jsp").forward(request, response);
                        } else {
                            session.setAttribute("errorMsg", "Không tìm thấy hồ sơ chuyên gia.");
                            response.sendRedirect("home.jsp");
                        }
                    } else if ("admin".equalsIgnoreCase(user.getRole())) {
                        response.sendRedirect("admin.jsp");
                    } else if ("learner".equalsIgnoreCase(user.getRole())) {
                        LearnerDAO learnerDAO = new LearnerDAO();
                        Learner learner = learnerDAO.getLearnerById(user.getUserID());
                        session.setAttribute("learner", learner);
                        response.sendRedirect("exam");
                    }
                } else {
                    request.setAttribute("errorMsg", "Đăng nhập thất bại. Không tìm thấy người dùng.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                request.setAttribute("errorMsg", "Đã xảy ra lỗi trong quá trình đăng nhập.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMsg", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}