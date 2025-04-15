package servlet;

import com.google.gson.Gson;
import dao.CourseDAO;
import dao.CoursePaidDAO;
import dao.CategoryDAO;
import dao.EnrollmentDAO;
import model.Cart;
import model.Course;
import model.Learner;
import model.User;
import model.Category;
import service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CourseServlet", urlPatterns = {"/courses", "/purchase", "/my-courses", "/cart", "/cart/add", "/cart/remove"})
public class CourseServlet extends HttpServlet {
    private CourseDAO courseDAO;
    private CartService cartService;
    private CoursePaidDAO coursePaidDAO;
    private CategoryDAO categoryDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        cartService = new CartService();
        coursePaidDAO = new CoursePaidDAO();
        categoryDAO = new CategoryDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/courses":
                handleCourseList(request, response);
                break;
            case "/my-courses":
                handleMyCourses(request, response);
                break;
            case "/cart":
                handleViewCart(request, response);
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/purchase":
                handlePurchase(request, response);
                break;
            case "/cart/add":
                handleAddToCart(request, response);
                break;
            case "/cart/remove":
                handleRemoveFromCart(request, response);
                break;
        }
    }

    private void handleCourseList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String priceRange = request.getParameter("priceRange");
        String categoryID = request.getParameter("categoryID");

        try {
            List<Course> courses;

            // Lọc theo category nếu có
            if (categoryID != null && !categoryID.isEmpty()) {
                courses = courseDAO.getCoursesByCategory(Integer.parseInt(categoryID));
            }
            // Lọc theo search nếu có
            else if (search != null && !search.isEmpty()) {
                courses = courseDAO.searchCourses(search);
            }
            // Lọc theo khoảng giá nếu có
            else if (priceRange != null && !priceRange.isEmpty()) {
                String[] range = priceRange.split("-");
                double minPrice = Double.parseDouble(range[0]);
                double maxPrice = Double.parseDouble(range[1]);
                courses = courseDAO.getCoursesByPriceRange(minPrice, maxPrice);
            }
            // Mặc định lấy khóa học mới nhất
            else {
                courses = courseDAO.getNewestCourses(12);
            }

            // Kiểm tra xem người dùng đã mua khóa học nào chưa
            Learner learner = (Learner) request.getSession().getAttribute("learner");
            if (learner != null) {
                for (Course course : courses) {
                    course.setPurchased(coursePaidDAO.isCoursePurchased(learner.getLearnerID(), course.getCourseID()));//da mua hay chua
                    course.setEnrolled(enrollmentDAO.isEnroll(learner.getLearnerID(), course.getCourseID()));//da tham gia hay chua
                    course.setLearnersCount(enrollmentDAO.countEnrolledLearners(course.getCourseID()));//co bao nhieu hoc vien
                }
            }

            // Lấy danh sách category để hiển thị trong navigation
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);

            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/courses.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.sendRedirect(request.getContextPath() + "/courses");
            return;
        }
        setNoCacheHeaders(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Learner learner = (Learner) request.getSession().getAttribute("learner");
        PrintWriter out = response.getWriter();

        if (learner == null) {
            out.write("{\"success\":false,\"message\":\"Vui lòng đăng nhập\"}");
            return;
        }

        try {
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            boolean added = cartService.addToCart(learner.getLearnerID(), courseID);

            if (added) {
                int cartCount = cartService.getCartItemCount(learner.getLearnerID());
                out.write("{\"success\":true,\"count\":" + cartCount + "}");
            } else {
                out.write("{\"success\":false,\"message\":\"Khóa học đã có trong giỏ hàng\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\":false,\"message\":\"Lỗi server: " + e.getMessage() + "\"}");
        }
    }
    private void handleMyCourses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Learner learner = (Learner) request.getSession().getAttribute("learner");
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Course> enrolledCourses = courseDAO.getEnrolledCourses(learner.getLearnerID());
            request.setAttribute("purchasedCourses", enrolledCourses);
            request.getRequestDispatcher("/my-courses.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handlePurchase(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            courseDAO.enrollCourse(user.getUserID(), courseID);
            response.sendRedirect("my-courses");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private void handleViewCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Learner learner = (Learner) request.getSession().getAttribute("learner");
        if (learner == null) {
            System.out.println("user not found!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Cart> cartItems = cartService.getPendingCartItems(learner.getLearnerID());
            request.setAttribute("cartItems", cartItems);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bạn cần đăng nhập");
            return;
        }

        try {
            int cartID = Integer.parseInt(request.getParameter("cartID"));
            boolean success = cartService.removeFromCart(cartID, user.getUserID());

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\":" + success + "}");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server");
        }
    }
    private void handleGetCourseDetail(HttpServletResponse response, Integer courseID) throws SQLException {
        Course course = courseDAO.getCourseById(courseID);

    }

    private void handleGet(HttpServletResponse resp, Integer id) throws IOException {
        setNoCacheHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(id);
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }

    private void setNoCacheHeaders(HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
    }

} 