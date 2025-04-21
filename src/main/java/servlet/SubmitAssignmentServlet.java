package servlet;

import com.google.gson.JsonObject;
import dao.AssignmentDAO;
import dao.AssignmentResultDAO;
import model.AssignmentResult;
import model.Learner;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "SubmitAssignmentServlet", value = "/submit-assignment")
public class SubmitAssignmentServlet extends HttpServlet {
    private AssignmentResultDAO assignmentResultDAO;
    private AssignmentDAO assignmentDAO;

    @Override
    public void init() {
        assignmentResultDAO = new AssignmentResultDAO();
        assignmentDAO = new AssignmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            if (learner == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Vui lòng đăng nhập để nộp bài");
                out.println(jsonResponse.toString());
                return;
            }

            // Lấy và kiểm tra các tham số
            String assignTakenIDStr = request.getParameter("assignTakenID");
            String assignmentQuesIDStr = request.getParameter("assignmentQuesID");
            String answerLabel = request.getParameter("answerLabel");
            String isCorrectStr = request.getParameter("isCorrect");
            String markStr = request.getParameter("mark");

            // Kiểm tra các tham số bắt buộc
            if (assignTakenIDStr == null || assignmentQuesIDStr == null || 
                answerLabel == null || isCorrectStr == null || markStr == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Thiếu thông tin cần thiết");
                out.println(jsonResponse.toString());
                return;
            }

            // Parse các giá trị
            int assignTakenID = Integer.parseInt(assignTakenIDStr);
            int assignmentQuesID = Integer.parseInt(assignmentQuesIDStr);
            boolean isCorrect = Boolean.parseBoolean(isCorrectStr);
            float mark = Float.parseFloat(markStr);

            // Tạo đối tượng AssignmentResult
            AssignmentResult result = new AssignmentResult();
            result.setAssignTakenID(assignTakenID);
            result.setAssignmentQuesID(assignmentQuesID);
            result.setLearnerID(learner.getLearnerID());
            result.setAnswerLabel(answerLabel);
            result.setAnswerIsCorrect(isCorrect);
            result.setMark(mark);

            // Lưu kết quả
            boolean saved = assignmentResultDAO.saveAnswer(result);

            if (saved) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Đã lưu câu trả lời thành công");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Có lỗi xảy ra khi lưu câu trả lời");
            }

        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Dữ liệu không hợp lệ: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi khi thao tác với cơ sở dữ liệu");
            e.printStackTrace();
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }

        out.println(jsonResponse.toString());
    }

    @Override
    public void destroy() {
        if (assignmentDAO != null) {
            assignmentDAO.closeConnection();
        }
    }
} 