package controller;

import dao.HonourDAO;
import dao.HonourOwnedDAO;
import model.Honour;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/listHonour")
public class HonourServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        Integer learnerID = learner != null ? learner.getLearnerID() : null;
        Integer userID = (Integer) session.getAttribute("userID");
        
        if (learnerID == null || userID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        HonourDAO dao = new HonourDAO();
        List<Honour> listHonour = dao.getAllHonours();

        // Lấy danh sách các honour mà người dùng đã sở hữu
        HonourOwnedDAO honourOwnedDAO = new HonourOwnedDAO();
        Map<Integer, Boolean> honourOwnedMap = new HashMap<>();
        for (Honour honour : listHonour) {
            boolean owned = honourOwnedDAO.hasHonour(learnerID, honour.getHonourID());
            honourOwnedMap.put(honour.getHonourID(), owned);
        }

        // Lấy ID của thành tựu đang được trang bị
        Integer equippedHonourID = honourOwnedDAO.getEquippedHonourID(userID);

        Set<String> uniqueTypes = listHonour.stream()
                .map(Honour::getHonourType)
                .collect(Collectors.toSet());

        // Lấy thông báo từ session (nếu có) và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");

        request.setAttribute("listHonour", listHonour);
        request.setAttribute("uniqueTypes", uniqueTypes);
        request.setAttribute("honourOwnedMap", honourOwnedMap);
        request.setAttribute("equippedHonourID", equippedHonourID);
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("listHonour.jsp").forward(request, response);
    }
}