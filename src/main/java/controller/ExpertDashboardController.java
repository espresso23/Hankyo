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
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;

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
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        
        if (expert == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vui lòng đăng nhập");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Trả về trang dashboard
            request.getRequestDispatcher("/expert-dashboard.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            logger.info("Processing request for path: {}", pathInfo);
            switch (pathInfo) {
                case "/stats":
                    handleStats(request, response, expert);
                    break;
                case "/revenue":
                    handleRevenue(request, response, expert);
                    break;
                case "/top-courses":
                    handleTopCourses(request, response, expert);
                    break;
                default:
                    logger.warn("Invalid endpoint accessed: {}", pathInfo);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint không tồn tại");
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing request: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Có lỗi xảy ra khi xử lý yêu cầu: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void handleStats(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        try {
            // Đọc tham số từ request
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            logger.debug("Received stats request body: {}", requestBody);
            
            if (requestBody == null || requestBody.isEmpty()) {
                throw new IllegalArgumentException("Request body không được để trống");
            }

            JsonObject jsonRequest = gson.fromJson(requestBody, JsonObject.class);
            if (jsonRequest == null) {
                throw new IllegalArgumentException("JSON không hợp lệ");
            }

            if (!jsonRequest.has("startDate") || !jsonRequest.has("endDate")) {
                throw new IllegalArgumentException("Thiếu tham số startDate hoặc endDate");
            }
            
            LocalDateTime startDate = LocalDateTime.parse(
                jsonRequest.get("startDate").getAsString(), 
                DateTimeFormatter.ISO_DATE_TIME
            );
            LocalDateTime endDate = LocalDateTime.parse(
                jsonRequest.get("endDate").getAsString(), 
                DateTimeFormatter.ISO_DATE_TIME
            );

            logger.info("Getting stats for expert {} from {} to {}", 
                expert.getExpertID(), startDate, endDate);

            // Lấy thống kê từ service
            var stats = dashboardService.getDashboardStats(expert.getExpertID(), startDate, endDate);
            
            // Trả về kết quả
            String json = gson.toJson(stats);
            response.getWriter().write(json);
            
            logger.debug("Successfully returned stats: {}", json);
        } catch (Exception e) {
            logger.error("Error handling stats request: ", e);
            throw e;
        }
    }

    private void handleRevenue(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        try {
            // Đọc tham số từ request
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            logger.debug("Received revenue request body: {}", requestBody);
            
            if (requestBody == null || requestBody.isEmpty()) {
                throw new IllegalArgumentException("Request body không được để trống");
            }

            JsonObject jsonRequest = gson.fromJson(requestBody, JsonObject.class);
            if (jsonRequest == null) {
                throw new IllegalArgumentException("JSON không hợp lệ");
            }

            if (!jsonRequest.has("startDate") || !jsonRequest.has("endDate")) {
                throw new IllegalArgumentException("Thiếu tham số startDate hoặc endDate");
            }
            
            LocalDateTime startDate = LocalDateTime.parse(
                jsonRequest.get("startDate").getAsString(), 
                DateTimeFormatter.ISO_DATE_TIME
            );
            LocalDateTime endDate = LocalDateTime.parse(
                jsonRequest.get("endDate").getAsString(), 
                DateTimeFormatter.ISO_DATE_TIME
            );

            logger.info("Getting revenue stats for expert {} from {} to {}", 
                expert.getExpertID(), startDate, endDate);

            // Lấy dữ liệu doanh thu từ service
            var revenueStats = dashboardService.getRevenueStats(expert.getExpertID(), startDate, endDate);
            
            // Trả về kết quả
            String json = gson.toJson(revenueStats);
            response.getWriter().write(json);
            
            logger.debug("Successfully returned revenue stats: {}", json);
        } catch (Exception e) {
            logger.error("Error handling revenue request: ", e);
            throw e;
        }
    }

    private void handleTopCourses(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        try {
            logger.info("Getting top courses for expert {}", expert.getExpertID());
            
            // Lấy danh sách khóa học nổi bật
            var topCourses = dashboardService.getTopCourses(expert.getExpertID());
            
            // Trả về kết quả
            String json = gson.toJson(topCourses);
            response.getWriter().write(json);
            
            logger.debug("Successfully returned top courses: {}", json);
        } catch (Exception e) {
            logger.error("Error handling top courses request: ", e);
            throw e;
        }
    }
} 