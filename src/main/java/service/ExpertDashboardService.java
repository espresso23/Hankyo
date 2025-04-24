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
        List<Order> orders = orderDAO.getOrdersByExpertId(expertId, DBConnect.getInstance().getConnection());
        BigDecimal totalRevenue = paymentDAO.getTotalRevenue(expertId);

        // Tính toán các thống kê
        long totalOrders = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) &&
                        order.getOrderDate().isBefore(endDate))
                .count();

        // Tạo đối tượng thống kê
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalRevenue(totalRevenue);
        stats.setTotalOrders(totalOrders);
        stats.setComparedToLastPeriod(calculateRevenueChange(expertId, startDate, endDate));

        return stats;
    }

    private double calculateRevenueChange(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        // Tính khoảng thời gian
        long periodDays = java.time.Duration.between(startDate, endDate).toDays();
        LocalDateTime previousPeriodStart = startDate.minusDays(periodDays);
        LocalDateTime previousPeriodEnd = startDate;

        // Lấy doanh thu của hai khoảng thời gian
        BigDecimal currentRevenue = getRevenueForPeriod(expertId, startDate, endDate);
        BigDecimal previousRevenue = getRevenueForPeriod(expertId, previousPeriodStart, previousPeriodEnd);

        // Tính phần trăm thay đổi
        if (previousRevenue.equals(BigDecimal.ZERO)) {
            return 0.0;
        }

        return currentRevenue.subtract(previousRevenue)
                .divide(previousRevenue, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private BigDecimal getRevenueForPeriod(int expertId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderDAO.getOrdersByExpertId(expertId, DBConnect.getInstance().getConnection()).stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) &&
                        order.getOrderDate().isBefore(endDate))
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