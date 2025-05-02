package controller.admin;

import model.Course;
import model.Expert;
import model.Post;
import model.User;
import service.AdminService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
    private final AdminService adminService;
    private final Gson gson;

    public AdminController() {
        this.adminService = new AdminService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            // Handle API requests
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            if (pathInfo.equals("/dashboard")) {
                // Dashboard stats API
                Map<String, Object> stats = adminService.getDashboardStats();
                response.getWriter().write(gson.toJson(stats));
            } else if (pathInfo.equals("/users")) {
                // Get all users
                List<User> users = adminService.getAllUsers();
                response.getWriter().write(gson.toJson(users));
            } else if (pathInfo.equals("/courses/unactive")) {
                // Get unactive courses
                List<Course> courses = adminService.getUnactiveCourses();
                response.getWriter().write(gson.toJson(courses));
            } else if (pathInfo.equals("/courses/high-rated")) {
                // Get high rated courses
                List<Map<String, Object>> courses = adminService.getHighRatedCourses();
                response.getWriter().write(gson.toJson(courses));
            } else if (pathInfo.equals("/courses/low-rated")) {
                // Get low rated courses
                List<Map<String, Object>> courses = adminService.getLowRatedCourses();
                response.getWriter().write(gson.toJson(courses));
            } else if (pathInfo.equals("/courses/total")) {
                // Get total courses
                int total = adminService.getTotalCourses();
                response.getWriter().write(gson.toJson(total));
            } else if (pathInfo.equals("/payments/recent")) {
                // Get recent payments
                List<Map<String, Object>> payments = adminService.getRecentPayments();
                response.getWriter().write(gson.toJson(payments));
            } else if (pathInfo.equals("/posts/reported")) {
                // Get reported posts
                List<Post> posts = adminService.getReportedPosts();
                response.getWriter().write(gson.toJson(posts));
            } else if (pathInfo.equals("/experts")) {
                // Get all experts
                List<Expert> experts = adminService.getExperts();
                response.getWriter().write(gson.toJson(experts));
            } else if (pathInfo.equals("/categories")) {
                // Get all categories
                List<Map<String, Object>> categories = adminService.getAllCategories();
                response.getWriter().write(gson.toJson(categories));
            } else if (pathInfo.equals("/reports")) {
                // Get all reports
                List<Map<String, Object>> reports = adminService.getReportDetails();
                response.getWriter().write(gson.toJson(reports));
            } else if (pathInfo.startsWith("/reports/status/")) {
                // Get reports by status
                String status = pathInfo.substring("/reports/status/".length());
                List<Map<String, Object>> reports = adminService.getReportsByStatus(status);
                response.getWriter().write(gson.toJson(reports));
            } else if (pathInfo.startsWith("/reports/reporter/")) {
                // Get reports by reporter
                int reporterId = Integer.parseInt(pathInfo.substring("/reports/reporter/".length()));
                List<Map<String, Object>> reports = adminService.getReportsByReporter(reporterId);
                response.getWriter().write(gson.toJson(reports));
            } else if (pathInfo.startsWith("/reports/reported-user/")) {
                // Get reports by reported user
                int reportedUserId = Integer.parseInt(pathInfo.substring("/reports/reported-user/".length()));
                List<Map<String, Object>> reports = adminService.getReportsByReportedUser(reportedUserId);
                response.getWriter().write(gson.toJson(reports));
            } else if (pathInfo.equals("/reports/date-range")) {
                // Get reports by date range
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                List<Map<String, Object>> reports = adminService.getReportsByDateRange(startDate, endDate);
                response.getWriter().write(gson.toJson(reports));
            } else if (pathInfo.startsWith("/categories/")) {
                // Get category by id
                int categoryId = Integer.parseInt(pathInfo.substring("/categories/".length()));
                Map<String, Object> category = adminService.getCategoryById(categoryId);
                response.getWriter().write(gson.toJson(category));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.startsWith("/reports/") && pathInfo.endsWith("/approve")) {
                // Duyệt report
                int reportId = Integer.parseInt(pathInfo.substring("/reports/".length(), pathInfo.length() - "/approve".length()));
                String status = request.getParameter("status");
                boolean success = adminService.approveReport(reportId, status);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.startsWith("/reports/") && pathInfo.endsWith("/block")) {
                // Khóa nội dung bị báo cáo
                int reportId = Integer.parseInt(pathInfo.substring("/reports/".length(), pathInfo.length() - "/block".length()));
                boolean success = adminService.blockReportedContent(reportId);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.startsWith("/users/") && pathInfo.endsWith("/block")) {
                // Block user
                int userId = Integer.parseInt(pathInfo.substring("/users/".length(), pathInfo.length() - "/block".length()));
                boolean success = adminService.blockUser(userId);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.startsWith("/withdraw-requests/") && pathInfo.endsWith("/process")) {
                // Process withdraw request
                int requestId = Integer.parseInt(pathInfo.substring("/withdraw-requests/".length(), pathInfo.length() - "/process".length()));
                boolean success = adminService.processDrawRequest(requestId);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.equals("/categories")) {
                // Add category
                String categoryName = request.getParameter("categoryName");
                String description = request.getParameter("description");
                boolean success = adminService.addCategory(categoryName, description);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.startsWith("/categories/")) {
                // Update category
                int categoryId = Integer.parseInt(pathInfo.substring("/categories/".length()));
                String categoryName = request.getParameter("categoryName");
                String description = request.getParameter("description");
                boolean success = adminService.updateCategory(categoryId, categoryName, description);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.startsWith("/categories/")) {
                // Delete category
                int categoryId = Integer.parseInt(pathInfo.substring("/categories/".length()));
                boolean success = adminService.deleteCategory(categoryId);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
        }
    }
} 