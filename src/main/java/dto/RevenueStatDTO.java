package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RevenueStatDTO {
    private LocalDateTime period;
    private BigDecimal amount;
    private long orderCount;
    private String status;

    public RevenueStatDTO(LocalDateTime period, BigDecimal amount, long orderCount, String status) {
        this.period = period;
        this.amount = amount;
        this.orderCount = orderCount;
        this.status = status;
    }

    public LocalDateTime getPeriod() {
        return period;
    }

    public void setPeriod(LocalDateTime period) {
        this.period = period;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
} 