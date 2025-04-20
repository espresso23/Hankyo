package servlet;

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
        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        Learner learner = (Learner) session.getAttribute("user");
        if (learner == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int learnerID = learner.getLearnerID();
        try {
            // Lấy danh sách kết quả bài thi của người dùng
            List<ExamResult> examResults = examResultDAO.getExamResultsByLearner(learnerID);

            // Lấy danh sách đề thi mà người dùng đã tham gia
            List<ExamTaken>  examTakens= examTakenDAO.getExamTakenByLearner(learnerID);

            // Đặt danh sách kết quả và đề thi vào request để hiển thị trên JSP
            request.setAttribute("examResults", examResults);
            request.setAttribute("examTakens", examTakens);  // Truyền thêm danh sách đề thi vào request

            // Chuyển hướng đến trang hiển thị lịch sử bài thi
            request.getRequestDispatcher("examHistory.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi lấy lịch sử bài thi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}