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

@WebFilter("/*")
public class LoginDayFilter implements Filter {
    private HonourDAO honourDAO;
    private HonourOwnedDAO honourOwnedDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        honourDAO = new HonourDAO();
        honourOwnedDAO = new HonourOwnedDAO();
        System.out.println("LoginDayFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Skip filter for login page to avoid infinite loop
        if (httpRequest.getRequestURI().endsWith("/login")) {
            chain.doFilter(request, response);
            return;
        }

        if (session != null) {
            Integer learnerID = (Integer) session.getAttribute("learnerID");
            Integer userID = (Integer) session.getAttribute("userID");

            if (userID != null && learnerID != null) {
                checkAndUpdateLoginDays(userID, learnerID);
            } else {
                // Try to get userID from session if learnerID exists
                if (learnerID != null && userID == null) {
                    userID = honourDAO.getUserIDFromLearnerID(learnerID);
                    if (userID != null) {
                        session.setAttribute("userID", userID);
                        checkAndUpdateLoginDays(userID, learnerID);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    private void checkAndUpdateLoginDays(int userID, int learnerID) {
        System.out.println("checkAndUpdateLoginDays: userID=" + userID + ", learnerID=" + learnerID);

        // Check or create Login record
        Login login = honourDAO.getLoginByUserID(userID);
        if (login == null) {
            System.out.println("No Login record for userID=" + userID + ", creating new");
            honourDAO.createLogin(userID);
            login = honourDAO.getLoginByUserID(userID);
            if (login == null) {
                System.err.println("Failed to create or retrieve Login record for userID=" + userID);
                return;
            }
        }

        // Get dates
        Date currentDate = new Date();
        Date lastDateLogin = login.getLastDateLogin();
        int loginDays = login.getLoginDays();
        Date dateCreate = honourDAO.getUserDateCreate(userID);

        if (dateCreate == null) {
            System.err.println("No dateCreate found for userID=" + userID + ", using currentDate");
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
            System.out.println("lastDateLogin is null, using dateCreate=" + dateCreate);
        }
        truncateTime(lastLoginCal);
        Date lastLoginDateOnly = lastLoginCal.getTime();

        // Check if loginDays should increment
        boolean shouldIncrement = currentDateOnly.after(createDateOnly) &&
                (lastDateLogin == null || currentDateOnly.after(lastLoginDateOnly));

        if (shouldIncrement) {
            loginDays++;
            honourDAO.updateLoginDays(userID, loginDays, currentDate);
            System.out.println("Updated: loginDays=" + loginDays + ", lastDateLogin=" + currentDate + " for userID=" + userID);

            // Check for 7-day login honour (ID 12)
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
        int honourID = 12;
        if (!honourOwnedDAO.hasHonour(learnerID, honourID)) {
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            if (honourOwned.getHonour() == null) {
                System.err.println("Honour ID " + honourID + " not found");
                return;
            }
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwnedDAO.addHonour(honourOwned);
            System.out.println("Awarded honourID " + honourID + " for learnerID=" + learnerID);
        }
    }

    @Override
    public void destroy() {
        System.out.println("LoginDayFilter destroyed");
    }
}