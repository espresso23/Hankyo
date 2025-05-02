package filter;

import dao.HonourDAO;
import dao.HonourOwnedDAO;
import model.HonourOwned;
import model.Learner;
import model.Login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@WebFilter(urlPatterns = {"/home.jsp", "/home"})
public class LoginDayFilter implements Filter {
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
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            Learner learner = (Learner) session.getAttribute("learner");
            Integer userID = (Integer) session.getAttribute("userID");
            Boolean isNewLogin = (Boolean) session.getAttribute("isNewLogin");

            if (userID != null && learner != null && Boolean.TRUE.equals(isNewLogin)) {
                checkAndUpdateLoginDays(userID, learner.getLearnerID());
                // Reset flag after processing
                session.setAttribute("isNewLogin", false);
            }
        }

        chain.doFilter(request, response);
    }

    private void checkAndUpdateLoginDays(int userID, int learnerID) {
        // Check or create Login record
        Login login = honourDAO.getLoginByUserID(userID);
        if (login == null) {
            honourDAO.createLogin(userID);
            login = honourDAO.getLoginByUserID(userID);
            if (login == null) return;
        }

        // Get dates
        Date currentDate = new Date();
        Date lastDateLogin = login.getLastDateLogin();
        int loginDays = login.getLoginDays();
        Date dateCreate = honourDAO.getUserDateCreate(userID);

        if (dateCreate == null) {
            dateCreate = currentDate;
        }

        // Truncate time for date-only comparison
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentDate);
        truncateTime(currentCal);
        Date currentDateOnly = currentCal.getTime();

        Calendar createCal = Calendar.getInstance();
        createCal.setTime(dateCreate);
        truncateTime(createCal);
        Date createDateOnly = createCal.getTime();

        Calendar lastLoginCal = Calendar.getInstance();
        if (lastDateLogin != null) {
            lastLoginCal.setTime(lastDateLogin);
        } else {
            lastLoginCal.setTime(dateCreate);
        }
        truncateTime(lastLoginCal);
        Date lastLoginDateOnly = lastLoginCal.getTime();

        // Check if loginDays should increment
        boolean shouldIncrement = currentDateOnly.after(createDateOnly) &&
                (lastDateLogin == null || currentDateOnly.after(lastLoginDateOnly));

        if (shouldIncrement) {
            loginDays++;
            honourDAO.updateLoginDays(userID, loginDays, currentDate);

            // Check for 7-day login honour (ID 20)
            if (loginDays >= 7) {
                award7DayLoginHonour(learnerID);
            }
        }
    }

    private void truncateTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void award7DayLoginHonour(int learnerID) {
        int honourID = 20;
        if (!honourOwnedDAO.hasHonour(learnerID, honourID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            if (honourOwned.getHonour() == null) return;
            
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
        // Cleanup if needed
    }
}