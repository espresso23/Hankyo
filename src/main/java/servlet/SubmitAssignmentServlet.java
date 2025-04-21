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
            
            System.out.println("Parameters received:"); // Log 3
            System.out.println("assignTakenID: " + assignTakenIDStr);
            System.out.println("assignmentID: " + assignmentIDStr);
            
            // Kiểm tra các tham số bắt buộc
            if (assignTakenIDStr == null || assignmentIDStr == null) {
                System.out.println("Missing required parameters"); // Log 4
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Thiếu thông tin cần thiết");
                out.println(jsonResponse.toString());
                return;
            }

            // Parse các giá trị
            int assignTakenID = Integer.parseInt(assignTakenIDStr);
            int assignmentID = Integer.parseInt(assignmentIDStr);

            // Lấy danh sách các tham số từ request
            java.util.Enumeration<String> paramNames = request.getParameterNames();
            java.util.List<AssignmentResult> results = new java.util.ArrayList<>();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();

                // Kiểm tra nếu tham số là assignQuesID
                if (paramName.equals("assignQuesID")) {
                    String assignmentQuesIDStr = request.getParameter(paramName);
                    if (assignmentQuesIDStr != null && !assignmentQuesIDStr.isEmpty()) {
                        int assignmentQuesID = Integer.parseInt(assignmentQuesIDStr);

                        // Lấy các tham số tương ứng cho câu hỏi này
                        String answerLabel = request.getParameter("answerLabel_" + assignmentQuesID);
                        String isCorrectStr = request.getParameter("isCorrect_" + assignmentQuesID);
                        String markStr = request.getParameter("mark_" + assignmentQuesID);

                        System.out.println("Processing question " + assignmentQuesID + ":"); // Log 5
                        System.out.println("answerLabel: " + answerLabel);
                        System.out.println("isCorrect: " + isCorrectStr);
                        System.out.println("mark: " + markStr);

                        // Kiểm tra nếu answerLabel hoặc các trường trả lời câu hỏi bị null thì coi như câu đó bị bỏ qua
                        boolean isSkipped = (answerLabel == null || answerLabel.trim().isEmpty() ||
                                isCorrectStr == null || markStr == null);

                        // Tạo đối tượng AssignmentResult
                        AssignmentResult result = new AssignmentResult();
                        result.setAssignTakenID(assignTakenID);
                        result.setAssignmentQuesID(assignmentQuesID);
                        result.setLearnerID(learner.getLearnerID());

                        if (isSkipped) {
                            // Nếu câu hỏi bị bỏ qua, đặt giá trị mặc định
                            result.setAnswerLabel("SKIPPED");
                            result.setAnswerIsCorrect(false);
                            result.setMark(0.0f);
                            System.out.println("Question " + assignmentQuesID + " was skipped"); // Log 6
                        } else {
                            // Nếu câu hỏi được trả lời, đặt giá trị từ form
                            boolean isCorrect = Boolean.parseBoolean(isCorrectStr);
                            float mark = Float.parseFloat(markStr);

                            result.setAnswerLabel(answerLabel);
                            result.setAnswerIsCorrect(isCorrect);
                            result.setMark(mark);

                            System.out.println("Question " + assignmentQuesID + " was answered"); // Log 7
                            System.out.println("Answer: " + answerLabel + ", IsCorrect: " + isCorrect + ", Mark: " + mark); // Log 8
                        }

                        results.add(result);
                    }
                }
            }

            // Nếu không có kết quả nào được tạo
            if (results.isEmpty()) {
                System.out.println("No results to save"); // Log 9
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không có câu trả lời nào được gửi");
                out.println(jsonResponse.toString());
                return;
            }

            System.out.println("Saving " + results.size() + " answers"); // Log 10

            // Lưu tất cả kết quả
            boolean allSaved = true;
            for (AssignmentResult result : results) {
                boolean saved = assignmentResultDAO.saveAnswer(result);
                if (!saved) {
                    allSaved = false;
                    System.out.println("Failed to save answer for question " + result.getAssignmentQuesID()); // Log 11
                }
            }

            if (allSaved) {
                System.out.println("All answers saved successfully"); // Log 12

                // Cập nhật AssignmentTaken
                AssignmentTaken taken = assignmentTakenDAO.getAssignmentTakenByID(assignTakenID);
                if (taken != null) {
                    // Lấy danh sách kết quả đã lưu
                    List<AssignmentResult> savedResults = assignmentResultDAO.getResultsByTakenID(assignTakenID);

                    // Đếm số câu đã làm và số câu bỏ qua
                    int doneQues = savedResults.size();
                    int totalQues = assignmentDAO.getTotalQuestionsByAssignmentID(assignmentID);
                    int skipQues = totalQues - doneQues;

                    // Cập nhật AssignmentTaken
                    taken.setDoneQues(doneQues);
                    taken.setSkipQues(skipQues);

                    // Lưu cập nhật
                    boolean updated = assignmentTakenDAO.updateAssignmentTaken(taken);
                    System.out.println("AssignmentTaken updated: " + updated); // Log 13
                }
                
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Đã lưu câu trả lời thành công");
            } else {
                System.out.println("Some answers failed to save"); // Log 14
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Có lỗi xảy ra khi lưu một số câu trả lời");
            }

        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage()); // Log 15
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Dữ liệu không hợp lệ: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()); // Log 16
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi khi thao tác với cơ sở dữ liệu");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage()); // Log 17
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