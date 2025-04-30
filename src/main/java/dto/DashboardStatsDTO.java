package dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DashboardStatsDTO {
    private BigDecimal totalRevenue;
    private int totalOrders;
    private double comparedToLastPeriod;
    private double orderComparedToLastPeriod;
} 