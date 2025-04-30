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

    public DashboardStatsDTO getDashboardStats(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // Lấy đơn hàng trong khoảng thời gian
        List<Order> currentOrders = orderDAO.getOrdersByExpertAndDateRange(expertId, startDate, endDate);
        
        // Tính tổng doanh thu từ các đơn hàng đã hoàn thành
        BigDecimal currentRevenue = currentOrders.stream()
            .filter(order -> "Completed".equals(order.getStatus()))
            .map(Order::getTotalAmount)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Đếm số đơn hàng (bao gồm cả pending và cancelled)
        int totalOrders = currentOrders.size();
        
        stats.setTotalRevenue(currentRevenue);
        stats.setTotalOrders(totalOrders);
        
        // Tính toán so sánh với kỳ trước
        double revenueChange = calculateRevenueChange(expertId, startDate, endDate);
        stats.setComparedToLastPeriod(revenueChange);
        
        // Tính toán phần trăm thay đổi số đơn hàng
        long previousOrders = getPreviousOrderCount(expertId, startDate, endDate);
        double orderChange = calculateOrderChange(totalOrders, previousOrders);
        stats.setOrderComparedToLastPeriod(orderChange);
        
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
        List<Order> orders = orderDAO.getOrdersByExpertAndDateRange(expertId, startDate, endDate);
        return orders.stream()
            .filter(order -> "Completed".equals(order.getStatus()))
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<RevenueStatDTO> getRevenueStats(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderDAO.getRevenueStatsByPeriod(expertId, startDate, endDate);
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

    private long getPreviousOrderCount(int expertId, LocalDateTime currentStart, LocalDateTime currentEnd) {
        long periodDays = ChronoUnit.DAYS.between(currentStart, currentEnd);
        LocalDateTime previousStart = currentStart.minusDays(periodDays);
        LocalDateTime previousEnd = currentStart;
        
        List<Order> previousOrders = orderDAO.getOrdersByExpertAndDateRange(expertId, previousStart, previousEnd);
        return previousOrders.size();
    }

    private double calculateOrderChange(long currentOrders, long previousOrders) {
        if (previousOrders == 0) {
            return currentOrders == 0 ? 0.0 : 100.0;
        }
        return ((double) (currentOrders - previousOrders) / previousOrders) * 100;
    }
} 