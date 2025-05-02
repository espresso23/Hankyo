package controller.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({
    "/admin-dashboard",
    "/admin-users",
    "/admin-courses",
    "/admin-payments",
    "/admin-posts",
    "/admin-experts",
    "/admin-reports",
    "/admin-categories"
})
public class AdminRequestPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getServletPath();
        switch (uri) {
            case "/admin-users":
                request.getRequestDispatcher("admin-users.jsp").forward(request, response);
                break;
            case "/admin-courses":
                request.getRequestDispatcher("admin-courses.jsp").forward(request, response);
                break;
            case "/admin-payments":
                request.getRequestDispatcher("admin-payments.jsp").forward(request, response);
                break;
            case "/admin-posts":
                request.getRequestDispatcher("admin-posts.jsp").forward(request, response);
                break;
            case "/admin-experts":
                request.getRequestDispatcher("admin-experts.jsp").forward(request, response);
                break;
            case "/admin-reports":
                request.getRequestDispatcher("admin-reports.jsp").forward(request, response);
                break;
            case "/admin-categories":
                request.getRequestDispatcher("admin-categories.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
        }
    }
}
