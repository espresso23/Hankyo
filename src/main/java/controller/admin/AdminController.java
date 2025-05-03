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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dao.CourseDAO;
import dao.CourseFeedbackDAO;
import dao.CoursePaidDAO;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
    private final AdminService adminService;
    private final Gson gson;

    public AdminController() {
        this.adminService = new AdminService();
        // Đăng ký TypeAdapter cho LocalDateTime
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME));
                }
            })
            .create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("Admin request: " + pathInfo);
        
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
            } else if (pathInfo.equals("/users/search")) {
                // Search users by name or email
                String searchTerm = request.getParameter("searchTerm");
                List<User> users = adminService.searchUsers(searchTerm);
                response.getWriter().write(gson.toJson(users));
            } else if (pathInfo.equals("/users/filter")) {
                // Filter users by role
                String role = request.getParameter("role");
                List<User> users = adminService.filterUsersByRole(role);
                response.getWriter().write(gson.toJson(users));
            } else if (pathInfo.startsWith("/users/")) {
                // Get user details
                int userId = Integer.parseInt(pathInfo.substring("/users/".length()));
                Map<String, Object> userDetails = adminService.getUserDetails(userId);
                response.getWriter().write(gson.toJson(userDetails));
            } else if (pathInfo.startsWith("/users/") && pathInfo.endsWith("/unblock")) {
                // Unblock user
                int userId = Integer.parseInt(pathInfo.substring("/users/".length(), pathInfo.length() - "/unblock".length()));
                boolean success = adminService.unblockUser(userId);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.startsWith("/users/") && pathInfo.endsWith("/block")) {
                // Block user
                int userId = Integer.parseInt(pathInfo.substring("/users/".length(), pathInfo.length() - "/block".length()));
                boolean success = adminService.blockUser(userId);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.equals("/courses/high-rated")) {
                // Get high rated courses
                List<Map<String, Object>> courses = adminService.getHighRatedCourses();
                response.getWriter().write(gson.toJson(courses));
            } else if (pathInfo.equals("/courses/low-rated")) {
                // Get low rated courses
                List<Map<String, Object>> courses = adminService.getLowRatedCourses();
                response.getWriter().write(gson.toJson(courses));
            } else if (pathInfo.equals("/courses/total")) {
                int total = adminService.getTotalCourses();
                response.getWriter().write(gson.toJson(Map.of("total", total)));
            } else if (pathInfo.equals("/courses/search")) {
                String keyword = request.getParameter("keyword");
                List<Course> courses = adminService.searchCourses(keyword);
                response.getWriter().write(gson.toJson(courses));
            } else if (pathInfo.equals("/payments/stats")) {
                // Get payment statistics
                Map<String, Object> stats = adminService.getPaymentStats();
                response.getWriter().write(gson.toJson(stats));
            } else if (pathInfo.equals("/payments/chart-data")) {
                String mode = request.getParameter("mode");
                Map<String, Object> chartData = adminService.getPaymentChartData(mode);
                response.getWriter().write(gson.toJson(chartData));
            } else if (pathInfo.equals("/payments/recent")) {
                // Get recent payments with filters
                String searchTerm = request.getParameter("search");
                String status = request.getParameter("status");
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                
                List<Map<String, Object>> payments = adminService.getFilteredPayments(searchTerm, status, startDate, endDate);
                response.getWriter().write(gson.toJson(payments));
            } else if (pathInfo.equals("/withdraw-requests")) {
                // Get all withdraw requests
                List<Map<String, Object>> requests = adminService.getAllWithdrawRequests();
                response.getWriter().write(gson.toJson(requests));
            } else if (pathInfo.equals("/payments/export")) {
                // Export payments to Excel
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=payments.xlsx");
                
                adminService.exportPaymentsToExcel(response.getOutputStream(), startDate, endDate);
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
            } else if (pathInfo != null && pathInfo.startsWith("/courses/experts")) {
                List<String> experts = adminService.getAllExpertNames();
                if (experts == null) experts = new java.util.ArrayList<>();
                response.getWriter().write(gson.toJson(experts));
            } else if (pathInfo != null && pathInfo.startsWith("/courses/categories")) {
                List<String> categories = adminService.getAllCategoryNames();
                if (categories == null) categories = new java.util.ArrayList<>();
                response.getWriter().write(gson.toJson(categories));
            } else if (pathInfo.startsWith("/courses/") && pathInfo.length() > "/courses/".length()) {
                // Lấy chi tiết khóa học cho admin
                String subPath = pathInfo.substring("/courses/".length());
                if (subPath.matches("\\d+")) {
                    int courseId = Integer.parseInt(subPath);
                    Map<String, Object> detail = adminService.getCourseDetailForAdmin(courseId);
                    if (detail == null) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write(gson.toJson(Map.of("error", "Course not found")));
                        return;
                    }
                    response.getWriter().write(gson.toJson(detail));
                } else if (subPath.matches("\\d+/revenue")) {
                    int courseId = Integer.parseInt(subPath.split("/")[0]);
                    Map<String, Object> revenue = adminService.getCourseRevenue(courseId);
                    response.getWriter().write(gson.toJson(revenue));
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson(Map.of("error", "Invalid course id")));
                }
            } else if (pathInfo.equals("/courses")) {
                String status = request.getParameter("status");
                String expert = request.getParameter("expert");
                String category = request.getParameter("category");
                if ((status != null && !status.isEmpty()) || (expert != null && !expert.isEmpty()) || (category != null && !category.isEmpty())) {
                    List<Course> courses = adminService.filterCourses(status, expert, category);
                    response.getWriter().write(gson.toJson(courses));
                } else {
                    List<Course> courses = adminService.getAllCourses();
                    response.getWriter().write(gson.toJson(courses));
                }
            } else if (pathInfo.startsWith("/withdraw-requests/") && pathInfo.endsWith("/process")) {
                // Process withdraw request
                int requestId = Integer.parseInt(pathInfo.substring("/withdraw-requests/".length(), pathInfo.length() - "/process".length()));
                Map<String, String> data = gson.fromJson(request.getReader(), Map.class);
                boolean approve = Boolean.parseBoolean(data.get("approve"));
                
                boolean success = adminService.processWithdrawRequest(requestId, approve);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else if (pathInfo.equals("/payments/all")) {
                // Get all payments (type course or vip)
                List<model.Payment> payments = adminService.getAllPayments();
                response.getWriter().write(gson.toJson(payments));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error: " + e.getMessage())));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Server Error: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.equals("/categories")) {
                // Add category
                Map<String, String> data = gson.fromJson(request.getReader(), Map.class);
                String categoryName = data.get("categoryName");
                String description = data.get("description");
                
                if (categoryName == null || categoryName.trim().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson(Map.of("error", "Category name is required")));
                    return;
                }
                
                boolean success = adminService.addCategory(categoryName, description);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error: " + e.getMessage())));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Server Error: " + e.getMessage())));
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
                Map<String, String> data = gson.fromJson(request.getReader(), Map.class);
                String categoryName = data.get("categoryName");
                String description = data.get("description");
                
                if (categoryName == null || categoryName.trim().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson(Map.of("error", "Category name is required")));
                    return;
                }
                
                boolean success = adminService.updateCategory(categoryId, categoryName, description);
                response.getWriter().write(gson.toJson(Map.of("success", success)));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("error", "Not Found")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Database Error: " + e.getMessage())));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Server Error: " + e.getMessage())));
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