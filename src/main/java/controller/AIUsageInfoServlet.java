package controller;

import dao.VipUserDAO;
import model.Learner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ai-usage-info")
public class AIUsageInfoServlet extends HttpServlet {
    private final VipUserDAO vipUserDAO = new VipUserDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        Learner learner = (Learner) req.getSession().getAttribute("learner");
        if (learner == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"not_logged_in\"}");
            return;
        }
        int learnerID = learner.getLearnerID();
        boolean isVip = false;
        int used = 0, left = 20;
        try {
            isVip = vipUserDAO.isVipUser(learnerID);
            if (!isVip) {
                used = vipUserDAO.getTodayUsageCount(learnerID);
                left = 20 - used;
            }
            resp.getWriter().write("{\"isVip\":" + isVip + ",\"used\":" + used + ",\"left\":" + (isVip ? -1 : left) + "}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"server_error\"}");
        }
    }
} 