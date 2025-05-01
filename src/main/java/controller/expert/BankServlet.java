package controller.expert;

import model.Bank;
import service.BankLoader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/banks")
public class BankServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Bank> banks = BankLoader.loadBanksFromJson("C:/FPTU/OJT/HANKYO/Hankyo/banks.json");

        // Đẩy danh sách ngân hàng lên JSP
        request.setAttribute("bankList", banks);
        request.getRequestDispatcher("banks.jsp").forward(request, response);
    }
}
