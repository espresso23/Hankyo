package java.controller;

import dao.HonourDAO;
import model.Honour;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/listHonour")
public class HonourServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Integer learnerID = (Integer) session.getAttribute("learnerID");
        if (learnerID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        HonourDAO dao = new HonourDAO();
        List<Honour> listHonour = dao.getAllHonours();

        Set<String> uniqueTypes = listHonour.stream()
                .map(Honour::getHonourType)
                .collect(Collectors.toSet());

        request.setAttribute("listHonour", listHonour);
        request.setAttribute("uniqueTypes", uniqueTypes);
        request.getRequestDispatcher("listHonour.jsp").forward(request, response);
    }
}