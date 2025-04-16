package model;

public class CourseContent {
    private int courseContentID;
    private String title;
    private String description;
    private String image;
    private int courseID;
    private String media;
    private Assignment assignment;
    private Exam exam;
    private boolean completed;

    public CourseContent(){}

    public CourseContent(int courseContentID, int courseID, String title, String description, String image, String media, Exam exam, Assignment assignment) {
        this.courseContentID = courseContentID;
        this.courseID = courseID;
        this.title = title;
        this.description = description;
        this.image = image;
        this.media = media;
        this.exam = exam;
        this.assignment = assignment;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public int getCourseContentID() {
        return courseContentID;
    }

    public void setCourseContentID(int courseContentID) {
        this.courseContentID = courseContentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "CourseContent{" +
                "assignment=" + assignment +
                ", courseContentID=" + courseContentID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", courseID=" + courseID +
                ", media='" + media + '\'' +
                ", exam=" + exam +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
