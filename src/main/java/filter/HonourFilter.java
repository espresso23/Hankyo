package filter;

import dao.HonourDAO;
import dao.HonourOwnedDAO;
import model.Honour;
import model.HonourOwned;
import model.Learner;
import model.User;
import util.DBConnect;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
            Integer userID = (Integer) session.getAttribute("userID");
            User user = (User) session.getAttribute("user");
            if (userID != null) {
                // Fetch equipped honour details using userID
                Integer equippedHonourID = honourOwnedDAO.getEquippedHonourID(userID);
                if (equippedHonourID != null) {
                    Honour honour = honourDAO.getHonourById(equippedHonourID);
                    if (honour != null) {
                        httpRequest.setAttribute("equippedHonourName", honour.getHonourName());
                        httpRequest.setAttribute("equippedGradientStart", honour.getGradientStart());
                        httpRequest.setAttribute("equippedGradientEnd", honour.getGradientEnd());
                        httpRequest.setAttribute("equippedHonourImage", honour.getHonourImg());
                    } else {
                        httpRequest.setAttribute("equippedHonourName", null);
                        httpRequest.setAttribute("equippedGradientStart", null);
                        httpRequest.setAttribute("equippedGradientEnd", null);
                        httpRequest.setAttribute("equippedHonourImage", null);
                    }
                } else {
                    httpRequest.setAttribute("equippedHonourName", null);
                    httpRequest.setAttribute("equippedGradientStart", null);
                    httpRequest.setAttribute("equippedGradientEnd", null);
                    httpRequest.setAttribute("equippedHonourImage", null);
                }
            } else {
                httpRequest.setAttribute("equippedHonourName", null);
                httpRequest.setAttribute("equippedGradientStart", null);
                httpRequest.setAttribute("equippedGradientEnd", null);
                httpRequest.setAttribute("equippedHonourImage", null);
            }

            // Award honours using learnerID and userID from session
            Integer learnerID = (Integer) session.getAttribute("learnerID");
            if (learnerID != null && userID != null) {
                checkAndAwardHonours(learnerID, userID, session);
            }
        } else {
            httpRequest.setAttribute("equippedHonourName", null);
            httpRequest.setAttribute("equippedGradientStart", null);
            httpRequest.setAttribute("equippedGradientEnd", null);
            httpRequest.setAttribute("equippedHonourImage", null);
        }

        chain.doFilter(request, response);
    }

    private void checkAndAwardHonours(int learnerID, int userID, HttpSession session) {
        // Check for 10 custom flashcards honour (ID 26)
        check10CustomFlashcardsHonour(learnerID);
        // Check for 100 comments honour (ID 10)
        check100CommentsHonour(learnerID, userID, session);
        // Check for 10 upvotes honour (ID 15)
        check10UpVoteHonour(learnerID, userID, session);
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
            honourOwned.setEquipped(false);
            honourOwnedDAO.addHonour(honourOwned);
        }
    }

    private void check100CommentsHonour(int learnerID, int userID, HttpSession session) {
        int honourID = 10;

        if (honourOwnedDAO.hasHonour(learnerID, honourID)) {
            return;
        }

        if (honourDAO.if100Comments(userID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwned.setEquipped(false);
            honourOwnedDAO.addHonour(honourOwned);
        }
    }

    private void check10UpVoteHonour(int learnerID, int userID, HttpSession session) {
        int honourID = 15;

        if (honourOwnedDAO.hasHonour(learnerID, honourID)) {
            return;
        }

        if (honourDAO.if10UpVote(userID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwned.setEquipped(false);
            honourOwnedDAO.addHonour(honourOwned);
        }
    }

    @Override
    public void destroy() {
        // Clean up resources
    }
}