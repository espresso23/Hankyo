package service;

import dao.OrderDAO;
import dao.PaymentDAO;
import dao.CourseDAO;
import model.Course;
import model.Order;
import dto.DashboardStatsDTO;
import dto.RevenueStatDTO;
import dto.TopCourseDTO;
import util.DBConnect;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.math.RoundingMode;

public class ExpertDashboardService {
    private final OrderDAO orderDAO;
    private final PaymentDAO paymentDAO;
    private final CourseDAO courseDAO;

    public ExpertDashboardService() {
        this.orderDAO = new OrderDAO();
        this.paymentDAO = new PaymentDAO();
        this.courseDAO = new CourseDAO();
    }

    public DashboardStatsDTO getDashboardStats(int expertId, LocalDateTime startDate, LocalDateTime endDate, String period) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        if ("all".equals(period)) {
            // Nếu là "all", lấy tất cả đơn hàng completed
            List<Order> allOrders = orderDAO.getAllCompletedOrdersByExpert(expertId);
            BigDecimal totalRevenue = allOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            stats.setTodayRevenue(totalRevenue);
            stats.setTotalOrders(allOrders.size());
            stats.setComparedToLastPeriod(0.0); // Không có so sánh cho toàn bộ
            stats.setOrderComparedToLastPeriod(0.0);
        } else {
            // Lấy đơn hàng trong khoảng thời gian

            List<Order> currentOrders = orderDAO.getOrdersByExpertAndDateRange(expertId, startDate, endDate, DBConnect.getInstance().getConnection());
            BigDecimal currentRevenue = currentOrders.stream()
                .filter(order -> "Completed".equals(order.getStatus()))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            stats.setTodayRevenue(currentRevenue);
            stats.setTotalOrders(currentOrders.size());
            
            // Tính toán so sánh với kỳ trước
            double revenueChange = calculateRevenueChange(expertId, startDate, endDate);
            stats.setComparedToLastPeriod(revenueChange);
            stats.setOrderComparedToLastPeriod(0.0); // TODO: Implement order comparison
        }
        
        return stats;
    }

    private double calculateRevenueChange(int expertId, LocalDateTime currentStart, LocalDateTime currentEnd) {
        // Tính khoảng thời gian của period hiện tại
        long periodDays = ChronoUnit.DAYS.between(currentStart, currentEnd);
        
        // Tính thời gian của period trước đó
        LocalDateTime previousStart = currentStart.minusDays(periodDays);
        LocalDateTime previousEnd = currentStart;
        
        // Lấy doanh thu của period hiện tại
        BigDecimal currentRevenue = getRevenueForPeriod(expertId, currentStart, currentEnd);
        
        // Lấy doanh thu của period trước đó
        BigDecimal previousRevenue = getRevenueForPeriod(expertId, previousStart, previousEnd);
        
        // Tính phần trăm thay đổi
        if (previousRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return currentRevenue.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : 100.0;
        }
        
        return currentRevenue.subtract(previousRevenue)
            .multiply(new BigDecimal(100))
            .divide(previousRevenue, 2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    private BigDecimal getRevenueForPeriod(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderDAO.getOrdersByExpertAndDateRange(expertId, startDate, endDate,DBConnect.getInstance().getConnection());
        return orders.stream()
            .filter(order -> "Completed".equals(order.getStatus()))
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<RevenueStatDTO> getRevenueStats(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderDAO.getRevenueStatsByPeriod(expertId, startDate, endDate, DBConnect.getInstance().getConnection());
    }

    public List<TopCourseDTO> getTopCourses(int expertId) {
        try {
            List<Course> courses = courseDAO.getHighlightedCoursesForExpert(expertId);
            List<TopCourseDTO> topCourses = new ArrayList<>();
            
            for (Course course : courses) {
                TopCourseDTO dto = new TopCourseDTO(
                    course.getCourseID(),
                    course.getCourseTitle(),
                    course.getTotalRevenue(),
                    course.getTotalSales(),
                    course.getLearnersCount(),
                    course.getRating()
                );
                topCourses.add(dto);
            }
            
            return topCourses;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 