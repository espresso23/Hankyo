package dto;

import java.math.BigDecimal;

public class TopCourseDTO {
    private int courseId;
    private String title;
    private BigDecimal totalRevenue;
    private BigDecimal totalSales;
    private int studentCount;
    private double rating;

    public TopCourseDTO(int courseId, String title, BigDecimal totalRevenue, BigDecimal totalSales, int studentCount, double rating) {
        this.courseId = courseId;
        this.title = title;
        this.totalRevenue = totalRevenue;
        this.totalSales = totalSales;
        this.studentCount = studentCount;
        this.rating = rating;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
} 