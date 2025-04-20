package controller;

import dao.ExamTakenDAO;
import dao.ExamResultDAO;
import model.ExamTaken;
import model.ExamResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/history")
public class ExamHistoryController extends HttpServlet {
    private ExamTakenDAO examTakenDAO;
    private ExamResultDAO examResultDAO;

    @Override
    public void init() throws ServletException {
        examTakenDAO = new ExamTakenDAO();
        examResultDAO = new ExamResultDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Integer learnerId = (Integer) session.getAttribute("learnerID");
            if (learnerId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Lấy danh sách bài thi đã làm
            List<ExamTaken> examTakenList = examTakenDAO.getExamTakenByLearner(learnerId);
            
            // Lấy chi tiết kết quả cho mỗi bài thi
            for (ExamTaken examTaken : examTakenList) {
                List<ExamResult> results = examResultDAO.getExamResultsByLearner(learnerId);
                examTaken.setFinalMark(examTaken.getFinalMark());
            }

            request.setAttribute("examHistory", examTakenList);
            request.getRequestDispatcher("examHistory.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("ERROR in ExamHistoryController: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
} 