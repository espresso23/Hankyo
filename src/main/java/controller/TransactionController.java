package controller;

import com.google.gson.*;
import dao.TransactionDAO;
import dto.TransactionDTO;
import model.Expert;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TransactionController", urlPatterns = {
    "/expert/transactions",
    "/api/transactions",
    "/api/transactions/search"
})
public class TransactionController extends HttpServlet {
    private final TransactionDAO transactionDAO;
    private final Gson gson;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public TransactionController() {
        this.transactionDAO = new TransactionDAO();
        
        // Tạo Gson với custom adapter cho LocalDateTime
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            })
            .create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println("TransactionController - doGet called with path: " + path);
        
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        System.out.println("TransactionController - Expert from session: " + expert);
        
        if (expert == null) {
            System.out.println("TransactionController - Expert is null, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/expert/transactions":
                System.out.println("TransactionController - Showing transaction page");
                showTransactionPage(request, response);
                break;
            case "/api/transactions":
                System.out.println("TransactionController - Getting transactions data");
                getTransactions(request, response, expert.getExpertID());
                break;
            case "/api/transactions/search":
                System.out.println("TransactionController - Searching transactions");
                searchTransactions(request, response);
                break;
            default:
                System.out.println("TransactionController - Path not found: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showTransactionPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Expert expert = (Expert) session.getAttribute("expert");
        
        if (expert == null) {
            System.out.println("TransactionController - showTransactionPage: Expert is null");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        System.out.println("TransactionController - showTransactionPage: Forwarding to JSP for expert: " + expert.getExpertID());
        request.setAttribute("expert", expert);
        request.getRequestDispatcher("/transaction-list.jsp").forward(request, response);
    }

    private void getTransactions(HttpServletRequest request, HttpServletResponse response, int expertId)
            throws IOException {
        System.out.println("TransactionController - getTransactions: Getting data for expertId: " + expertId);
        int page = getPageNumber(request);
        List<TransactionDTO> transactions = transactionDAO.getAllTransactions(expertId, page, DEFAULT_PAGE_SIZE);
        int total = transactionDAO.getTotalTransactions(expertId);
        
        System.out.println("TransactionController - getTransactions: Found " + transactions.size() + " transactions, total: " + total);
        sendJsonResponse(response, createDataTableResponse(transactions, page, total));
    }

    private void searchTransactions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Expert expert = (Expert) request.getSession().getAttribute("expert");
        if (expert == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            // Lấy các tham số
            String searchTerm = request.getParameter("search[value]");
            int draw = Integer.parseInt(request.getParameter("draw"));
            int start = Integer.parseInt(request.getParameter("start"));
            int length = Integer.parseInt(request.getParameter("length"));
            int page = (start / length) + 1;

            // Parse date range nếu có
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = ZonedDateTime.parse(startDate).toLocalDateTime();
            } else {
                startDateTime = LocalDateTime.now().minusYears(1);
            }
            
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = ZonedDateTime.parse(endDate).toLocalDateTime();
            } else {
                endDateTime = LocalDateTime.now();
            }

            System.out.println("Date range: " + startDateTime + " to " + endDateTime);

            // Lấy dữ liệu
            List<TransactionDTO> transactions = transactionDAO.searchTransactions(
                expert.getExpertID(), searchTerm, startDateTime, endDateTime, page, length);
            int totalRecords = transactionDAO.getTotalTransactions(expert.getExpertID());
            int filteredRecords = transactionDAO.getTotalFilteredTransactions(
                expert.getExpertID(), searchTerm, startDateTime, endDateTime);

            // Tạo response
            Map<String, Object> response_data = new HashMap<>();
            response_data.put("draw", draw);
            response_data.put("recordsTotal", totalRecords);
            response_data.put("recordsFiltered", filteredRecords);
            response_data.put("data", transactions);

            sendJsonResponse(response, response_data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in searchTransactions: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private int getPageNumber(HttpServletRequest request) {
        String start = request.getParameter("start");
        if (start != null && !start.isEmpty()) {
            int page = (Integer.parseInt(start) / DEFAULT_PAGE_SIZE) + 1;
            System.out.println("TransactionController - getPageNumber: Calculated page: " + page);
            return page;
        }
        System.out.println("TransactionController - getPageNumber: Returning default page 1");
        return 1;
    }

    private LocalDateTime getDateParameter(HttpServletRequest request, String paramName, LocalDateTime defaultValue) {
        String dateStr = request.getParameter(paramName);
        System.out.println("TransactionController - getDateParameter: " + paramName + " = " + dateStr);
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                return ZonedDateTime.parse(dateStr).toLocalDateTime();
            } catch (Exception e) {
                System.out.println("TransactionController - getDateParameter: Error parsing date: " + e.getMessage());
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(data));
    }

    private Map<String, Object> createDataTableResponse(List<TransactionDTO> data, int page, int total) {
        Map<String, Object> response = new HashMap<>();
        response.put("draw", page);
        response.put("recordsTotal", total);
        response.put("recordsFiltered", total);
        response.put("data", data);
        return response;
    }
} 