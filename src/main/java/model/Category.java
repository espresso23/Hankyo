package model;

import java.util.List;

public class Category {
    private int categoryID;
    private String categoryName;
    private String description;
    private Integer parentID;
    private List<Category> subCategories;
    private int courseCount;

    // Constructor
    public Category() {
    }

    public Category(int categoryID, String categoryName, String description, Integer parentID) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.parentID = parentID;
    }

    // Getters and Setters
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    // Phương thức kiểm tra xem có phải là danh mục cha không
    public boolean isParentCategory() {
        return parentID == null;
    }

    // Phương thức kiểm tra xem có phải là danh mục con không
    public boolean isSubCategory() {
        return parentID != null;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", parentID=" + parentID +
                ", courseCount=" + courseCount +
                '}';
    }
} 