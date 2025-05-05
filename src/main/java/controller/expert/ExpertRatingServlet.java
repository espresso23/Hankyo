package controller.expert;

import dao.ExpertRatingDAO;
import model.ExpertRating;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/rateExpert")
public class ExpertRatingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Hiển thị form đánh giá
        req.getRequestDispatcher("rateExpert.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int expertID = Integer.parseInt(req.getParameter("expertID"));
        int teachingQuality = Integer.parseInt(req.getParameter("teachingQuality"));
        int replyQuality = Integer.parseInt(req.getParameter("replyQuality"));
        int courseQuality = Integer.parseInt(req.getParameter("courseQuality"));
        int friendlyQuality = Integer.parseInt(req.getParameter("friendlyQuality"));

        HttpSession session = req.getSession();
        Learner learner = (Learner) session.getAttribute("learner");
        int learnerID = learner.getLearnerID();

        ExpertRating rating = new ExpertRating(expertID, learnerID, teachingQuality, replyQuality, courseQuality, friendlyQuality);

        ExpertRatingDAO dao = new ExpertRatingDAO();
        boolean success = dao.addRating(rating);

        req.setAttribute("success", success);
        req.getRequestDispatcher("rateExpert.jsp").forward(req, resp);
    }
} 