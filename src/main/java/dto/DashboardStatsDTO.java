package dto;

import java.math.BigDecimal;

public class DashboardStatsDTO {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private double comparedToLastPeriod;
    private BigDecimal todayRevenue;
    private BigDecimal totalAllTimeRevenue;

    public DashboardStatsDTO() {
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getComparedToLastPeriod() {
        return comparedToLastPeriod;
    }

    public void setComparedToLastPeriod(double comparedToLastPeriod) {
        this.comparedToLastPeriod = comparedToLastPeriod;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(BigDecimal todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public BigDecimal getTotalAllTimeRevenue() {
        return totalAllTimeRevenue;
    }

    public void setTotalAllTimeRevenue(BigDecimal totalAllTimeRevenue) {
        this.totalAllTimeRevenue = totalAllTimeRevenue;
    }
} 