package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PlatformRevenue implements Serializable {
    private static final long serialVersionUID = 1L;
    private int revenueID;
    private int amount;
    private String description;
    private LocalDateTime date;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRevenueID() {
        return revenueID;
    }

    public void setRevenueID(int revenueID) {
        this.revenueID = revenueID;
    }
}
