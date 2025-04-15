package model;

import java.time.LocalDateTime;

public class ExpertRevenue {
    private int expertID;
    private double totalRevenue;
    private LocalDateTime lastUpdated;

    public ExpertRevenue(int expertID, double totalRevenue, LocalDateTime lastUpdated) {
        this.expertID = expertID;
        this.totalRevenue = totalRevenue;
        this.lastUpdated = lastUpdated;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
