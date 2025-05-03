package controller.exam;

import dao.ExamDAO;
import dao.ExamResultDAO;
import dao.ExamTakenDAO;
import model.Exam;
import model.ExamResult;
import model.ExamTaken;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/examHistory")
public class ExamHistoryServlet extends HttpServlet {
    private ExamDAO examDAO;
    private ExamResultDAO examResultDAO;
    private ExamTakenDAO examTakenDAO;

    @Override
    public void init() throws ServletException {
        examDAO = new ExamDAO(null);
        examResultDAO = new ExamResultDAO();
        examTakenDAO = new ExamTakenDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Kiểm tra đăng nhập
        Learner learner = (Learner) session.getAttribute("learner");
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int learnerID = learner.getLearnerID();

        try {
            // Lấy danh sách kết quả bài thi của người dùng
            List<ExamResult> examResults = examResultDAO.getExamResultsByLearner(learnerID);

            // Lấy danh sách đề thi mà người dùng đã tham gia từ bảng Exam_Taken
            List<ExamTaken> examTakens = examTakenDAO.getExamTakenByLearner(learnerID);

            // Lấy thông tin đề thi cho mỗi ExamTaken
            List<Exam> exams = new ArrayList<>();
            for (ExamTaken examTaken : examTakens) {
                Exam exam = examDAO.getExamById(examTaken.getExamID());
                exams.add(exam); // Thêm exam vào danh sách, có thể là null nếu không tìm thấy
            }

            // Đặt danh sách kết quả, đề thi, và thông tin đề thi vào request
            session.setAttribute("examResults", examResults);
            session.setAttribute("examTakens", examTakens);
            session.setAttribute("exams", exams);

            // Chuyển hướng đến trang hiển thị lịch sử bài thi
            request.getRequestDispatcher("examHistory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Có lỗi xảy ra khi lấy lịch sử bài thi: " + e.getMessage();
            if (e.getCause() != null) {
                errorMessage += " (Nguyên nhân gốc: " + e.getCause().getMessage() + ")";
            }
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}