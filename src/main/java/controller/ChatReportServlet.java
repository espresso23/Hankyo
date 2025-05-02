package controller;

import dao.ReportDAO;
import model.Report;
import model.ReportType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Enumeration;
import org.json.JSONObject;

@WebServlet("/ChatReportServlet")
public class gChatReportServlet extends HttpServlet {
    private ReportDAO reportDAO;

    @Override
    public void init() throws ServletException {
        reportDAO = new ReportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ReportType> reportTypes = null;
        try {
            reportTypes = reportDAO.getAllReportTypes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("reportTypes", reportTypes);
        request.getRequestDispatcher("/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Đọc dữ liệu JSON từ request
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            
            // Parse JSON data
            JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
            
            // Kiểm tra các tham số bắt buộc
            String chatIDStr = jsonObject.optString("chatID");
            String reporterIDStr = jsonObject.optString("reporterID");
            String reportedUserIDStr = jsonObject.optString("reportedUserID");
            String reason = jsonObject.optString("description");

            if (chatIDStr.isEmpty() || reporterIDStr.isEmpty() || reportedUserIDStr.isEmpty() || reason.isEmpty()) {
                throw new IllegalArgumentException("Missing required parameters");
            }

            int chatID = Integer.parseInt(chatIDStr);
            int reporterID = Integer.parseInt(reporterIDStr);
            int reportedUserID = Integer.parseInt(reportedUserIDStr);

            // Create report object
            Report report = new Report();
            report.setReporterID(reporterID);
            report.setReportedUserID(reportedUserID);
            report.setReason(reason);
            report.setChatID(chatID);

            // Save report to database using createChatReport
            boolean success = reportDAO.createChatReport(report);

            if (success) {
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Báo cáo đã được gửi thành công\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Không thể gửi báo cáo\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}