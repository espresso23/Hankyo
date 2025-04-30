package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String courseName;
    private String learnerName;
    private String gmail;
    private String status;
    private int courseId;
    private int learnerId;
} 