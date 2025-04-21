package filter;

import dao.HonourDAO;
import dao.HonourOwnedDAO;
import model.HonourOwned;
import model.Learner;
import util.DBConnect;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

@WebFilter("/*")
public class HonourFilter implements Filter {
    private HonourDAO honourDAO;
    private HonourOwnedDAO honourOwnedDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        honourDAO = new HonourDAO();
        honourOwnedDAO = new HonourOwnedDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            Integer learnerID = (Integer) session.getAttribute("learnerID");
            if (learnerID != null) {
                // Load equipped honour
                HonourOwnedDAO honourOwnedDAO = new HonourOwnedDAO();
                Integer equippedHonourID = honourOwnedDAO.getEquippedHonourID(learnerID);
                session.setAttribute("equippedHonourID", equippedHonourID);

                checkAndAwardHonours(learnerID, session);
            }
        }

        chain.doFilter(request, response);
    }

    private void checkAndAwardHonours(int learnerID, HttpSession session) {
        // Check for 10 custom flashcards honour (ID 26)
        check10CustomFlashcardsHonour(learnerID);
        // Check for 100 comments honour (ID 10)
        check100CommentsHonour(learnerID, session);
        // Check for 10 upvotes honour (ID 15)
        check10UpVoteHonour(learnerID, session);
        // Bỏ check7DayLoginHonour vì đã xử lý trong LoginDayFilter
    }

    private void check10CustomFlashcardsHonour(int learnerID) {
        int honourID = 26;

        if (honourOwnedDAO.hasHonour(learnerID, honourID)) {
            return;
        }

        if (honourDAO.if10CustomFlashCard(learnerID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwnedDAO.addHonour(honourOwned);
            System.out.println("Awarded honourID " + honourID + " for learnerID " + learnerID);
        }
    }

    private void check100CommentsHonour(int learnerID, HttpSession session) {
        int honourID = 10;

        if (honourOwnedDAO.hasHonour(learnerID, honourID)) {
            return;
        }

        Integer userID = honourDAO.getUserIDFromLearnerID(learnerID);
        if (userID != null && honourDAO.if100Comments(userID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwnedDAO.addHonour(honourOwned);
            System.out.println("Awarded honourID " + honourID + " for learnerID " + learnerID);
        }
    }

    private void check10UpVoteHonour(int learnerID, HttpSession session) {
        int honourID = 15;

        if (honourOwnedDAO.hasHonour(learnerID, honourID)) {
            return;
        }

        Integer userID = honourDAO.getUserIDFromLearnerID(learnerID);
        if (userID != null && honourDAO.if10UpVote(userID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwnedDAO.addHonour(honourOwned);
            System.out.println("Awarded honourID " + honourID + " for learnerID " + learnerID);
        }
    }

    @Override
    public void destroy() {
        // Clean up resources
    }
}
