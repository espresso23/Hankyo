package servlet;

import com.google.gson.JsonObject;
import dao.AssignmentDAO;
import dao.AssignmentResultDAO;
import dao.AssignmentTakenDAO;
import model.AssignmentResult;
import model.AssignmentTaken;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "SubmitAssignmentServlet", value = "/submit-assignment")
public class SubmitAssignmentServlet extends HttpServlet {
    private AssignmentResultDAO assignmentResultDAO;
    private AssignmentDAO assignmentDAO;
    private AssignmentTakenDAO assignmentTakenDAO;

    @Override
    public void init() {
        assignmentResultDAO = new AssignmentResultDAO();
        assignmentDAO = new AssignmentDAO();
        assignmentTakenDAO = new AssignmentTakenDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SubmitAssignmentServlet.doPost() called"); // Log 1
        
        // Log tất cả parameters nhận được
        System.out.println("=== PARAMETERS RECEIVED ===");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println(key + ": " + Arrays.toString(values));
        });
        System.out.println("==========================");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            Learner learner = (Learner) session.getAttribute("learner");
            if (learner == null) {
                System.out.println("Learner not found in session"); // Log 2
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Vui lòng đăng nhập để nộp bài");
                out.println(jsonResponse.toString());
                return;
            }

            // Lấy và kiểm tra các tham số
            String assignTakenIDStr = request.getParameter("assignTakenID");
            String assignmentIDStr = request.getParameter("assignmentID");
            String[] assignQuesIDs = request.getParameterValues("assignQuesID[]");
            String[] answerLabels = request.getParameterValues("answerLabel[]");
            String[] isCorrects = request.getParameterValues("isCorrect[]");
            String[] marks = request.getParameterValues("mark[]");
            
            System.out.println("Parameters received:"); // Log 3
            System.out.println("assignTakenID: " + assignTakenIDStr);
            System.out.println("assignmentID: " + assignmentIDStr);
            System.out.println("assignQuesIDs: " + (assignQuesIDs != null ? Arrays.toString(assignQuesIDs) : "null"));
            System.out.println("answerLabels: " + (answerLabels != null ? Arrays.toString(answerLabels) : "null"));
            System.out.println("isCorrects: " + (isCorrects != null ? Arrays.toString(isCorrects) : "null"));
            System.out.println("marks: " + (marks != null ? Arrays.toString(marks) : "null"));
            
            // Kiểm tra các tham số bắt buộc
            if (assignTakenIDStr == null || assignmentIDStr == null || 
                assignQuesIDs == null || answerLabels == null || 
                isCorrects == null || marks == null) {
                System.out.println("Missing required parameters"); // Log 4
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Thiếu thông tin cần thiết");
                out.println(jsonResponse.toString());
                return;
            }

            // Parse các giá trị
            int assignTakenID = Integer.parseInt(assignTakenIDStr);
            int assignmentID = Integer.parseInt(assignmentIDStr);

            // Xử lý từng câu hỏi
            List<AssignmentResult> results = new ArrayList<>();
            float totalMark = 0;
            int doneCount = 0;
            int skipCount = 0;
            
            // Xử lý từng câu hỏi
            for (int i = 0; i < assignQuesIDs.length; i++) {
                int assignmentQuesID = Integer.parseInt(assignQuesIDs[i]);
                String answerLabel = answerLabels[i];
                boolean isCorrect = Boolean.parseBoolean(isCorrects[i]);
                float mark = Float.parseFloat(marks[i]);

                System.out.println("Processing question " + assignmentQuesID + ":"); // Log 5
                System.out.println("answerLabel: " + answerLabel);
                System.out.println("isCorrect: " + isCorrect);
                System.out.println("mark: " + mark);

                // Tạo đối tượng AssignmentResult
                AssignmentResult result = new AssignmentResult();
                result.setAssignTakenID(assignTakenID);
                result.setAssignmentQuesID(assignmentQuesID);
                result.setLearnerID(learner.getLearnerID());
                result.setAnswerLabel(answerLabel);
                result.setAnswerIsCorrect(isCorrect);
                result.setMark(mark);

                // Kiểm tra nếu câu này được trả lời hay bỏ qua
                if ("SKIPPED".equals(answerLabel)) {
                    skipCount++;
                } else {
                    doneCount++;
                    totalMark += mark;
                }

                results.add(result);
            }

            // Nếu không có kết quả nào được tạo
            if (results.isEmpty()) {
                System.out.println("No results to save"); // Log 6
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không có câu trả lời nào được gửi");
                out.println(jsonResponse.toString());
                return;
            }

            System.out.println("Saving " + results.size() + " answers"); // Log 7

            // Lưu tất cả kết quả
            boolean allSaved = true;
            for (AssignmentResult result : results) {
                boolean saved = assignmentResultDAO.saveAnswer(result);
                if (!saved) {
                    allSaved = false;
                    System.out.println("Failed to save answer for question " + result.getAssignmentQuesID()); // Log 8
                }
            }

            if (allSaved) {
                System.out.println("All answers saved successfully"); // Log 9

                // Cập nhật AssignmentTaken
                AssignmentTaken taken = assignmentTakenDAO.getAssignmentTakenByID(assignTakenID);
                if (taken != null) {
                    // Cập nhật thông tin bài làm
                    taken.setDoneQues(doneCount);
                    taken.setSkipQues(skipCount);
                    taken.setFinalMark(totalMark);

                    // Log thông tin cập nhật
                    System.out.println("Updating AssignmentTaken:");
                    System.out.println("Done Questions: " + doneCount);
                    System.out.println("Skipped Questions: " + skipCount);
                    System.out.println("Final Mark: " + totalMark);

                    // Lưu cập nhật
                    boolean updated = assignmentTakenDAO.updateAssignmentTaken(taken);
                    System.out.println("AssignmentTaken updated: " + updated); // Log 10
                }
                
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Đã lưu câu trả lời thành công");
                // Thêm thông tin để redirect
                jsonResponse.addProperty("redirect", true);
                jsonResponse.addProperty("redirectUrl", "learn-course?courseID=" + request.getParameter("courseID") + "&courseContentID=" + request.getParameter("courseContentID"));
            } else {
                System.out.println("Some answers failed to save"); // Log 11
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Có lỗi xảy ra khi lưu một số câu trả lời");
            }

        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage()); // Log 12
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Dữ liệu không hợp lệ: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()); // Log 13
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi khi thao tác với cơ sở dữ liệu");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage()); // Log 14
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