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
        System.out.println("=== getDashboardStats ===");
        System.out.println("ExpertID: " + expertId);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        
        List<Order> orders = orderDAO.getOrdersByExpertId(expertId, DBConnect.getInstance().getConnection());
        System.out.println("Total orders found: " + orders.size());
        
        // Tính doanh thu toàn bộ
        BigDecimal totalAllTimeRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total all-time revenue: " + totalAllTimeRevenue + " VND");

        // Tính doanh thu hôm nay
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = todayStart.plusDays(1);
        BigDecimal todayRevenue = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(todayStart) &&
                        order.getOrderDate().isBefore(todayEnd))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Today's revenue: " + todayRevenue + " VND");
        
        // Lọc orders theo khoảng thời gian và tính tổng doanh thu
        BigDecimal periodRevenue = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) &&
                        order.getOrderDate().isBefore(endDate))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Period revenue: " + periodRevenue + " VND");

        // Tính toán số lượng đơn hàng trong khoảng thời gian
        long totalOrders = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) &&
                        order.getOrderDate().isBefore(endDate))
                .count();
        System.out.println("Total orders in period: " + totalOrders);

        double revenueChange = calculateRevenueChange(expertId, startDate, endDate);
        System.out.println("Revenue change compared to last period: " + revenueChange + "%");

        // Tạo đối tượng thống kê
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalRevenue(periodRevenue);
        stats.setTotalOrders(totalOrders);
        stats.setComparedToLastPeriod(revenueChange);
        stats.setTodayRevenue(todayRevenue);
        stats.setTotalAllTimeRevenue(totalAllTimeRevenue);

        System.out.println("=== End getDashboardStats ===\n");
        return stats;
    }

    private double calculateRevenueChange(int expertId, LocalDateTime currentStart, LocalDateTime currentEnd) {
        System.out.println("=== calculateRevenueChange ===");
        // Tính khoảng thời gian của period hiện tại
        long periodDays = ChronoUnit.DAYS.between(currentStart, currentEnd);
        System.out.println("Period days: " + periodDays);
        
        // Tính thời gian của period trước đó
        LocalDateTime previousStart = currentStart.minusDays(periodDays);
        LocalDateTime previousEnd = currentStart;
        System.out.println("Previous period: " + previousStart + " to " + previousEnd);
        System.out.println("Current period: " + currentStart + " to " + currentEnd);
        
        // Lấy danh sách đơn hàng
        List<Order> orders = orderDAO.getOrdersByExpertId(expertId, DBConnect.getInstance().getConnection());
        System.out.println("Total orders found: " + orders.size());
        
        // Tính doanh thu của period hiện tại
        BigDecimal currentRevenue = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(currentStart) &&
                        order.getOrderDate().isBefore(currentEnd))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Current period revenue: " + currentRevenue);
        
        // Tính doanh thu của period trước đó
        BigDecimal previousRevenue = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(previousStart) &&
                        order.getOrderDate().isBefore(previousEnd))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Previous period revenue: " + previousRevenue);
        
        // Tính phần trăm thay đổi
        double change;
        if (previousRevenue.compareTo(BigDecimal.ZERO) == 0) {
            change = currentRevenue.compareTo(BigDecimal.ZERO) == 0 ? 0 : 100;
        } else {
            change = currentRevenue.subtract(previousRevenue)
                    .multiply(new BigDecimal(100))
                    .divide(previousRevenue, 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        System.out.println("Revenue change: " + change + "%");
        System.out.println("=== End calculateRevenueChange ===\n");
        return change;
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
        System.out.println("=== getTopCourses ===");
        System.out.println("ExpertID: " + expertId);
        try {
            List<Course> courses = courseDAO.getHighlightedCoursesForExpert(expertId);
            System.out.println("Found " + courses.size() + " highlighted courses");
            
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
                System.out.println("Course: " + dto.getTitle() + 
                    ", Revenue: " + dto.getTotalRevenue() + 
                    ", Sales: " + dto.getTotalSales() + 
                    ", Students: " + dto.getStudentCount() + 
                    ", Rating: " + dto.getRating());
                topCourses.add(dto);
            }
            
            System.out.println("=== End getTopCourses ===\n");
            return topCourses;
        } catch (SQLException e) {
            System.out.println("Error in getTopCourses: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

} 