package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RevenueStatDTO {
    private LocalDateTime period;
    private BigDecimal totalAmount;
    private long orderCount;
    private String status;

    public RevenueStatDTO(LocalDateTime period, BigDecimal totalAmount, long orderCount, String status) {
        this.period = period;
        this.totalAmount = totalAmount;
        this.orderCount = orderCount;
        this.status = status;
    }

    public LocalDateTime getPeriod() {
        return period;
    }

    public void setPeriod(LocalDateTime period) {
        this.period = period;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "RevenueStatDTO[period=%s, status=%s, totalAmount=%s, orderCount=%d]",
            period, status, totalAmount, orderCount
        );
    }
} 