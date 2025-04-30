package controller;

import com.google.gson.*;
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
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;

@WebServlet(name = "ExpertDashboardController", urlPatterns = {
    "/expert-dashboard",
    "/expert-dashboard/stats",
    "/expert-dashboard/revenue",
    "/expert-dashboard/top-courses"
})
public class ExpertDashboardController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ExpertDashboardController.class);
    private final ExpertDashboardService dashboardService;
    private final Gson gson;

    public ExpertDashboardController() {
        this.dashboardService = new ExpertDashboardService();
        
        // Tạo TypeAdapter cho LocalDateTime
        JsonSerializer<LocalDateTime> serializer = new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME));
            }
        };
        
        JsonDeserializer<LocalDateTime> deserializer = new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
            }
        };
        
        // Cấu hình Gson với TypeAdapter
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, serializer)
            .registerTypeAdapter(LocalDateTime.class, deserializer)
            .create();
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

        String pathInfo = request.getServletPath();
        if ("/expert-dashboard".equals(pathInfo)) {
            // Trả về trang dashboard
            request.getRequestDispatcher("/expert-dashboard.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            logger.info("Processing request for path: {}", pathInfo);
            switch (pathInfo) {
                case "/expert-dashboard/stats":
                    handleStats(request, response, expert);
                    break;
                case "/expert-dashboard/revenue":
                    handleRevenue(request, response, expert);
                    break;
                case "/expert-dashboard/top-courses":
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
            // Đọc tham số từ query parameters
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            if (startDateStr == null || endDateStr == null) {
                throw new IllegalArgumentException("Thiếu tham số startDate hoặc endDate");
            }

            LocalDateTime startDate = LocalDateTime.parse(startDateStr, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, DateTimeFormatter.ISO_DATE_TIME);

            logger.info("Getting stats for expert {} from {} to {}", 
                expert.getExpertID(), startDate, endDate);

            // Lấy thống kê từ service
            DashboardStatsDTO stats = dashboardService.getDashboardStats(expert.getExpertID(), startDate, endDate);
            
            // Lấy dữ liệu doanh thu theo ngày
            List<RevenueStatDTO> dailyStats = dashboardService.getRevenueStats(expert.getExpertID(), startDate, endDate);
            
            // Lấy top khóa học
            List<TopCourseDTO> topCourses = dashboardService.getTopCourses(expert.getExpertID());
            
            // Tạo response object
            JsonObject response_data = new JsonObject();
            response_data.add("stats", gson.toJsonTree(stats));
            response_data.add("dailyStats", gson.toJsonTree(dailyStats));
            response_data.add("topCourses", gson.toJsonTree(topCourses));
            
            // Trả về kết quả
            response.getWriter().write(gson.toJson(response_data));
            
            logger.debug("Successfully returned dashboard data: {}", response_data);
        } catch (Exception e) {
            logger.error("Error handling stats request: ", e);
            JsonObject error = new JsonObject();
            error.addProperty("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(error));
        }
    }

    private void handleRevenue(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        try {
            // Đọc tham số từ request
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            System.out.println("Received revenue request body: " + requestBody);
            
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
            
            String startDateStr = jsonRequest.get("startDate").getAsString();
            String endDateStr = jsonRequest.get("endDate").getAsString();
            
            System.out.println("Parsing dates from request:");
            System.out.println("startDate: " + startDateStr);
            System.out.println("endDate: " + endDateStr);
            
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, DateTimeFormatter.ISO_DATE_TIME);

            System.out.println("Getting revenue stats for expert " + expert.getExpertID());
            System.out.println("From: " + startDate);
            System.out.println("To: " + endDate);

            // Lấy dữ liệu doanh thu từ service
            List<RevenueStatDTO> revenueStats = dashboardService.getRevenueStats(expert.getExpertID(), startDate, endDate);
            
            System.out.println("Retrieved " + revenueStats.size() + " revenue stats");
            for (RevenueStatDTO stat : revenueStats) {
                System.out.println("Revenue stat: " + stat.toString());
            }
            
            // Trả về kết quả
            String json = gson.toJson(revenueStats);
            System.out.println("Returning JSON response: " + json);
            
            response.getWriter().write(json);
        } catch (Exception e) {
            System.err.println("Error handling revenue request: " + e.getMessage());
            e.printStackTrace();
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