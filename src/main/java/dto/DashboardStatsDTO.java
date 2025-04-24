package dto;

import java.math.BigDecimal;

public class DashboardStatsDTO {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private double comparedToLastPeriod;

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
} 