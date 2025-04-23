package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private int courseID;
    private String courseTitle;
    private BigDecimal price;
    private BigDecimal originalPrice;  // Giá gốc trước khi giảm giá
    private String courseImg;
    private String status;
    private String courseDescription;
    private Date dateCreated;
    private Date lastUpdated;
    private int expertID;
    private boolean purchased;// is paid?
    private Timestamp enrollDate;
    private String enrollStatus;
    private double rating;         // Điểm đánh giá trung bình
    private int ratingCount;       // Số lượng đánh giá
    private int learnersCount;// count number of students
    private Category category;
    private boolean enrolled;// type of course
    private Expert expert;
    private int progress;
    private int firstContentID;
    private BigDecimal totalRevenue;

    public Course() {
    }

    public Course(int courseID, String courseTitle, String courseDescription, String courseImg, String status, BigDecimal price, Date dateCreated, Date lastUpdated) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseImg = courseImg;
        this.status = status;
        this.price = price;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    public Course(int courseID, String courseTitle) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public Timestamp getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Timestamp enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(String enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getLearnersCount() {
        return learnersCount;
    }

    public void setLearnersCount(int learnersCount) {
        this.learnersCount = learnersCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }


    @Override
    public String toString() {
        return "Course{" +
                "category=" + category +
                ", courseID=" + courseID +
                ", courseTitle='" + courseTitle + '\'' +
                ", price=" + price +
                ", originalPrice=" + originalPrice +
                ", courseImg='" + courseImg + '\'' +
                ", status='" + status + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", expertID=" + expertID +
                ", purchased=" + purchased +
                ", enrollDate=" + enrollDate +
                ", enrollStatus='" + enrollStatus + '\'' +
                ", rating=" + rating +
                ", ratingCount=" + ratingCount +
                ", learnersCount=" + learnersCount +
                ", enrolled=" + enrolled +
                ", expert=" + expert +
                '}';
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getFirstContentID() {
        return firstContentID;
    }

    public void setFirstContentID(int firstContentID) {
        this.firstContentID = firstContentID;
    }

    public static void main(String[] args) {
        Course course = new Course();
        course.setCourseID(1);
        course.setCourseTitle("Khoa hoc Topik 1");
        course.setCourseDescription("Tan dep trai");
        course.setCourseImg("load.jpg");
        course.setPrice(BigDecimal.valueOf(50000));
        course.setStatus("active");
        course.setDateCreated(new Date());
        course.setLastUpdated(new Date());
        System.out.println(course.toString());

    }
}
