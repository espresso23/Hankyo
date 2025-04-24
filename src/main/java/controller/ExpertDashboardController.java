package controller;

import com.google.gson.Gson;
import model.Expert;
import dto.DashboardStatsDTO;
import dto.RevenueStatDTO;
import dto.TopCourseDTO;
import service.ExpertDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet("/expert-dashboard/*")
public class ExpertDashboardController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ExpertDashboardController.class);
    private final ExpertDashboardService dashboardService;
    private final Gson gson;

    public ExpertDashboardController() {
        this.dashboardService = new ExpertDashboardService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");

        if (expert == null) {
            System.out.println("WARNING: Unauthorized access attempt to dashboard - No expert in session");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        System.out.println("\n========== EXPERT DASHBOARD ACCESS ==========");
        System.out.println("Expert ID: " + expert.getExpertID());
        System.out.println("Endpoint: " + pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            System.out.println("Loading main dashboard page for expert: " + expert.getExpertID());
            request.getRequestDispatcher("/expert-dashboard.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        LocalDateTime startDate = getStartDate(request);
        LocalDateTime endDate = getEndDate(request);
        
        System.out.println("\nTime Period: " + startDate + " to " + endDate);

        try {
            switch (pathInfo) {
                case "/stats":
                    DashboardStatsDTO stats = dashboardService.getDashboardStats(
                        expert.getExpertID(), startDate, endDate);
                    System.out.println("\n---------- DASHBOARD STATS ----------");
                    System.out.println("Total Revenue: " + stats.getTotalRevenue() + " VND");
                    System.out.println("Total Orders: " + stats.getTotalOrders());
                    System.out.println("Revenue Change: " + stats.getComparedToLastPeriod() + "%");
                    System.out.println("------------------------------------");
                    response.getWriter().write(gson.toJson(stats));
                    break;

                case "/revenue":
                    List<RevenueStatDTO> revenueStats = dashboardService.getRevenueStats(
                        expert.getExpertID(), startDate, endDate);
                    System.out.println("\n---------- REVENUE STATS ----------");
                    System.out.println("Total Records: " + revenueStats.size());
                    for (RevenueStatDTO stat : revenueStats) {
                        System.out.println("\nDate: " + stat.getPeriod());
                        System.out.println("Amount: " + stat.getAmount() + " VND");
                        System.out.println("Orders: " + stat.getOrderCount());
                        System.out.println("Status: " + stat.getStatus());
                    }
                    System.out.println("----------------------------------");
                    response.getWriter().write(gson.toJson(revenueStats));
                    break;

                case "/top-courses":
                    List<TopCourseDTO> topCourses = dashboardService.getTopCourses(
                        expert.getExpertID());
                    System.out.println("\n---------- TOP COURSES ----------");
                    System.out.println("Number of Courses: " + topCourses.size());
                    for (TopCourseDTO course : topCourses) {
                        System.out.println("\nCourse ID: " + course.getCourseId());
                        System.out.println("Title: " + course.getTitle());
                        System.out.println("Total Sales: " + course.getTotalRevenue() + " VND");
                        System.out.println("Student Count: " + course.getStudentCount());
                        System.out.println("Rating: " + course.getRating() + "/5");
                        System.out.println("--------------------------------");
                    }
                    response.getWriter().write(gson.toJson(topCourses));
                    break;

                default:
                    System.out.println("\nWARNING: Invalid endpoint accessed: " + pathInfo);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            System.out.println("\n========== ERROR ==========");
            System.out.println("Error for expert " + expert.getExpertID() + ": " + e.getMessage());
            e.printStackTrace(System.out);
            System.out.println("==========================");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Có lỗi xảy ra khi xử lý yêu cầu");
        }
        System.out.println("\n=========================================\n");
    }

    private LocalDateTime getStartDate(HttpServletRequest request) {
        String startDateStr = request.getParameter("startDate");
        if (startDateStr == null || startDateStr.isEmpty()) {
            return LocalDateTime.now().minusDays(7);
        }
        return LocalDateTime.parse(startDateStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    private LocalDateTime getEndDate(HttpServletRequest request) {
        String endDateStr = request.getParameter("endDate");
        if (endDateStr == null || endDateStr.isEmpty()) {
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(endDateStr, DateTimeFormatter.ISO_DATE_TIME);
    }
} 