package model;

import java.io.Serializable;

public class CourseSales implements Serializable {
    private static final long serialVersionUID = 1L;
    private int courseID;
    private String title;
    private int totalSold;

    public CourseSales(int courseID, String title, int totalSold) {
        this.courseID = courseID;
        this.title = title;
        this.totalSold = totalSold;
    }

    // Getters and Setters
}
