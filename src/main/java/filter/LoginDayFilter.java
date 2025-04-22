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
        System.out.println("LoginDayFilter doFilter called");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        System.out.println("Session exists: " + (session != null));
        if (session != null) {
            Learner learner = (Learner) session.getAttribute("learner");
            Integer userID = (Integer) session.getAttribute("userID");
            System.out.println("learner: " + learner);
            System.out.println("userID: " + userID);

            if (userID != null && learner != null) {
                System.out.println("Both learner and userID exist, checking login days");
                checkAndUpdateLoginDays(userID, learner.getLearnerID());
            }
        }

        chain.doFilter(request, response);
    }

    private void checkAndUpdateLoginDays(int userID, int learnerID) {
        System.out.println("Checking login days for userID: " + userID + ", learnerID: " + learnerID);
        
        // Check or create Login record
        Login login = honourDAO.getLoginByUserID(userID);
        if (login == null) {
            System.out.println("No login record found, creating new one");
            honourDAO.createLogin(userID);
            login = honourDAO.getLoginByUserID(userID);
            if (login == null) {
                System.out.println("Failed to create login record");
                return;
            }
        }

        // Get dates
        Date currentDate = new Date();
        Date lastDateLogin = login.getLastDateLogin();
        int loginDays = login.getLoginDays();
        Date dateCreate = honourDAO.getUserDateCreate(userID);

        System.out.println("Current login days: " + loginDays);
        System.out.println("Last date login: " + lastDateLogin);
        System.out.println("Date create: " + dateCreate);

        if (dateCreate == null) {
            dateCreate = currentDate;
            System.out.println("Using current date as date create");
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
            System.out.println("Using date create as last login date");
        }
        truncateTime(lastLoginCal);
        Date lastLoginDateOnly = lastLoginCal.getTime();

        // Check if loginDays should increment
        boolean shouldIncrement = currentDateOnly.after(createDateOnly) &&
                (lastDateLogin == null || currentDateOnly.after(lastLoginDateOnly));

        System.out.println("Should increment login days: " + shouldIncrement);

        if (shouldIncrement) {
            loginDays++;
            honourDAO.updateLoginDays(userID, loginDays, currentDate);
            System.out.println("Updated login days to: " + loginDays);

            // Check for 7-day login honour (ID 20)
            if (loginDays >= 0) {
                System.out.println("Attempting to award 7-day login honour");
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
            System.out.println("User doesn't have honour ID " + honourID + ", attempting to award");
            HonourOwned honourOwned = new HonourOwned();
            honourOwned.setHonour(honourDAO.getHonourById(honourID));
            if (honourOwned.getHonour() == null) {
                System.out.println("Failed to get honour with ID " + honourID);
                return;
            }
            Learner learner = new Learner();
            learner.setLearnerID(learnerID);
            honourOwned.setLearner(learner);
            honourOwned.setDateAdded(new Date());
            honourOwned.setEquipped(false);
            honourOwnedDAO.addHonour(honourOwned);
            System.out.println("Successfully awarded honour ID " + honourID);
        } else {
            System.out.println("User already has honour ID " + honourID);
        }
    }

    @Override
    public void destroy() {
        System.out.println("LoginDayFilter destroyed");
    }
}