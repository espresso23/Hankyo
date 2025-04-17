package controller;

import dao.ContentDAO;
import model.Newspaper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/NewspaperServlet")
public class NewspaperServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ContentDAO newspaperDAO = new ContentDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int newspaperID = Integer.parseInt(idParam);
                Newspaper newspaper = newspaperDAO.getNewspaperByID(newspaperID);
                request.setAttribute("newspaper", newspaper);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("newspaper.jsp").forward(request, response);
    }
}
