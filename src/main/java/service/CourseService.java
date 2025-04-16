package service;

import dao.CourseDAO;
import model.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();
    }

    public Course getCourseById(int courseID) throws SQLException {
        return courseDAO.getCourseById(courseID);
    }

    public List<Course> getPurchasedCourses(int learnerID) throws SQLException {
        return courseDAO.getPurchasedCourses(learnerID);
    }

    public int getFirstContentID(int courseID) throws SQLException {
        return courseDAO.getFirstContentID(courseID);
    }

    public boolean isEnrolled(int learnerID, int courseID) throws SQLException {
        return courseDAO.isEnrolled(learnerID, courseID);
    }

    public void enrollCourse(int learnerID, int courseID) throws SQLException {
        courseDAO.enrollCourse(learnerID, courseID);
    }
} 