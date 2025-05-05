package controller.honour;

import dao.HonourOwnedDAO;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/equipHonour")
public class EquipHonourServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer userID = (Integer) session.getAttribute("userID");
        
        if (learner == null || userID == null) {
            session.setAttribute("errorMessage", "Bạn cần đăng nhập để thực hiện thao tác này");
            response.sendRedirect("login.jsp");
            return;
        }
        
        Integer learnerID = learner.getLearnerID();
        String action = request.getParameter("action");
        int honourID = Integer.parseInt(request.getParameter("honourID"));

        HonourOwnedDAO honourOwnedDAO = new HonourOwnedDAO();

        try {
            if ("equip".equals(action)) {
                boolean success = honourOwnedDAO.equipHonour(learnerID, honourID, userID);
                if (success) {
                    session.setAttribute("successMessage", "Trang bị thành tựu thành công");
                } else {
                    session.setAttribute("errorMessage", "Trang bị thất bại");
                }
            } else if ("unequip".equals(action)) {
                boolean success = honourOwnedDAO.unequipHonour(userID);
                if (success) {
                    session.setAttribute("successMessage", "Gỡ bỏ thành tựu thành công");
                } else {
                    session.setAttribute("errorMessage", "Gỡ bỏ thất bại");
                }
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
        }

        // Chuyển hướng về trang danh sách thành tựu
        response.sendRedirect(request.getContextPath() + "/listHonour");
    }
}