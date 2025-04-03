package controller;

import dao.DictionaryDAO;
import model.Dictionary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dictionary")
public class DictionaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DictionaryDAO dictionaryDAO = new DictionaryDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Dictionary> dictionaryList = dictionaryDAO.getAllDictionary();
        request.setAttribute("dictionaryList", dictionaryList);
        request.getRequestDispatcher("dictionary.jsp").forward(request, response);
    }
}
