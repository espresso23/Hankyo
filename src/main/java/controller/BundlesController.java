package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.AdminService;
import model.Vip;
import model.User;
import model.Learner;
import dao.LearnerDAO;

@WebServlet("/bundles")
public class BundlesController extends HttpServlet {
    private final AdminService adminService;

    public BundlesController() {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Lấy danh sách các gói VIP đang hoạt động
            List<Vip> activeVips = adminService.getActiveVips();
            request.setAttribute("vips", activeVips);

            // Lấy user từ session và truyền learner vào request nếu có
            User user = (User) request.getSession().getAttribute("user");
            boolean hasActiveVip = false;
            if (user != null) {
                LearnerDAO learnerDAO = new LearnerDAO();
                Learner learner = learnerDAO.getLearnerByUserId(user.getUserID());
                if (learner != null) {
                    request.setAttribute("learner", learner);
                    // Kiểm tra learner đã có VIP active chưa
                    Vip activeVip = adminService.getUserActiveVip(learner.getLearnerID());
                    if (activeVip != null) {
                        hasActiveVip = true;
                        request.setAttribute("activeVip", activeVip);
                    }
                }
            }
            request.setAttribute("hasActiveVip", hasActiveVip);
            // Forward đến trang bundles.jsp
            request.getRequestDispatcher("bundles.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
} 