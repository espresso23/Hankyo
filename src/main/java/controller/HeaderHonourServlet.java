package controller;

import dao.HonourDAO;
import dao.HonourOwnedDAO;
import model.Honour;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/headerHonour")
public class HeaderHonourServlet extends HttpServlet {
    private HonourDAO honourDAO;
    private HonourOwnedDAO honourOwnedDAO;

    @Override
    public void init() throws ServletException {
        honourDAO = new HonourDAO();
        honourOwnedDAO = new HonourOwnedDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            // No user logged in, set default attributes
            request.setAttribute("equippedHonourName", null);
            request.setAttribute("equippedGradientStart", null);
            request.setAttribute("equippedGradientEnd", null);
            System.out.println("HeaderHonourServlet: No session or userID found");
        } else {
            Integer userID = (Integer) session.getAttribute("userID");
            System.out.println("HeaderHonourServlet: userID=" + userID);
            // Fetch equipped honour ID
            Integer equippedHonourID = honourOwnedDAO.getEquippedHonourID(userID);
            if (equippedHonourID != null) {
                // Fetch honour details
                Honour honour = honourDAO.getHonourById(equippedHonourID);
                if (honour != null) {
                    request.setAttribute("equippedHonourName", honour.getHonourName());
                    request.setAttribute("equippedGradientStart", honour.getGradientStart());
                    request.setAttribute("equippedGradientEnd", honour.getGradientEnd());
                    // Debug logging
                    System.out.println("HeaderHonourServlet: Equipped Honour ID=" + equippedHonourID +
                            ", Name=" + honour.getHonourName() +
                            ", GradientStart=" + honour.getGradientStart() +
                            ", GradientEnd=" + honour.getGradientEnd());
                } else {
                    request.setAttribute("equippedHonourName", null);
                    request.setAttribute("equippedGradientStart", null);
                    request.setAttribute("equippedGradientEnd", null);
                    System.out.println("HeaderHonourServlet: No honour found for ID=" + equippedHonourID);
                }
            } else {
                request.setAttribute("equippedHonourName", null);
                request.setAttribute("equippedGradientStart", null);
                request.setAttribute("equippedGradientEnd", null);
                System.out.println("HeaderHonourServlet: No equipped honour for userID=" + userID);
            }
        }

        // Forward to header-user.jsp
        request.getRequestDispatcher("courseHeader.jsp").forward(request, response);
    }
}