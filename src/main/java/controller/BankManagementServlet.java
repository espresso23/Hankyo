package controller;

import com.google.gson.Gson;
import dao.ExpertBankDAO;
import model.Expert;
import model.ExpertBank;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/expert/bank-management")
public class BankManagementServlet extends HttpServlet {
    
    private final Gson gson = new Gson();
    private final ExpertBankDAO expertBankDAO = new ExpertBankDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/expert-bank.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        
        if (expert == null) {
            sendJsonResponse(response, false, "Vui lòng đăng nhập");
            return;
        }

        String action = request.getParameter("action");
        Map<String, Object> result = new HashMap<>();

        try {
            switch (action) {
                case "add":
                    handleAddBank(request, response, expert);
                    break;
                case "edit":
                    handleEditBank(request, response, expert);
                    break;
                case "delete":
                    handleDeleteBank(request, response, expert);
                    break;
                case "list":
                    handleListBanks(request, response, expert);
                    break;
                default:
                    sendJsonResponse(response, false, "Hành động không hợp lệ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private void handleAddBank(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        String bankName = request.getParameter("bankName");
        String bankAccount = request.getParameter("bankAccount");
        String binCode = request.getParameter("binCode");
        
        ExpertBank expertBank = new ExpertBank(bankName, bankAccount, binCode, expert.getExpertID());
        boolean success = expertBankDAO.addExpertBank(expertBank);
        
        if (success) {
            sendJsonResponse(response, true, "Thêm tài khoản thành công");
        } else {
            sendJsonResponse(response, false, "Thêm tài khoản thất bại");
        }
    }

    private void handleEditBank(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        int bankId = Integer.parseInt(request.getParameter("bankId"));
        String bankAccount = request.getParameter("bankAccount");
        
        ExpertBank existingBank = expertBankDAO.getExpertBankById(bankId);
        if (existingBank == null || existingBank.getExpertID() != expert.getExpertID()) {
            sendJsonResponse(response, false, "Tài khoản không tồn tại hoặc không thuộc quyền sở hữu");
            return;
        }
        
        existingBank.setBankAccount(bankAccount);
        boolean success = expertBankDAO.updateExpertBank(existingBank);
        
        if (success) {
            sendJsonResponse(response, true, "Cập nhật thành công");
        } else {
            sendJsonResponse(response, false, "Cập nhật thất bại");
        }
    }

    private void handleDeleteBank(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        String bankIdStr = request.getParameter("bankId");
        if (bankIdStr == null || bankIdStr.trim().isEmpty()) {
            sendJsonResponse(response, false, "Mã ngân hàng không hợp lệ");
            return;
        }
        
        try {
            int bankId = Integer.parseInt(bankIdStr);
            
            ExpertBank existingBank = expertBankDAO.getExpertBankById(bankId);
            if (existingBank == null || existingBank.getExpertID() != expert.getExpertID()) {
                sendJsonResponse(response, false, "Tài khoản không tồn tại hoặc không thuộc quyền sở hữu");
                return;
            }
            
            boolean success = expertBankDAO.deleteExpertBank(bankId);
            
            if (success) {
                sendJsonResponse(response, true, "Xóa tài khoản thành công");
            } else {
                sendJsonResponse(response, false, "Xóa tài khoản thất bại");
            }
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Mã ngân hàng không hợp lệ");
        }
    }

    private void handleListBanks(HttpServletRequest request, HttpServletResponse response, Expert expert) 
            throws IOException {
        List<ExpertBank> banks = expertBankDAO.getExpertBanks(expert.getExpertID());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", banks);
        
        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(result));
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) 
            throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);
        
        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(result));
        }
    }
} 