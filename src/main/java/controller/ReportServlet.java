package controller;//package controller;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import dao.PostDAO;
//import dao.ReportDAO;
//import model.Post;
//import model.Report;
//import model.ReportType;
//import model.User;
//
//@WebServlet("/ReportServlet")
//public class ReportServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    private ReportDAO reportDAO;
//    private PostDAO postDAO;
//
//    public void init() {
//        reportDAO = new ReportDAO();
//        postDAO = new PostDAO();
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        HttpSession session = request.getSession();
//        User currentUser = (User) session.getAttribute("user");
//
//        // Check if user is logged in
//        if (currentUser == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        String action = request.getParameter("action");
//
//        if ("showReportForm".equals(action)) {
//            showReportForm(request, response);
//        } else {
//            response.sendRedirect("index.jsp");
//        }
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        HttpSession session = request.getSession();
//        User currentUser = (User) session.getAttribute("user");
//
//        // Check if user is logged in
//        if (currentUser == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        // Process form submission
//        try {
//            // Get form data
//            int postID = Integer.parseInt(request.getParameter("postID"));
//            int reporterID = currentUser.getUserID();
//            int reportTypeID = Integer.parseInt(request.getParameter("reportTypeID"));
//            String reason = request.getParameter("reason");
//
//            // Get post author's user ID
//            int reportedUserID = reportDAO.getPostAuthorID(postID);
//
//            // Create report object
//            Report report = new Report();
//            report.setReporterID(reporterID);
//            report.setReportedUserID(reportedUserID);
//            report.setReportTypeID(reportTypeID);
//            report.setReason(reason);
//            report.setPostID(postID);
//            report.setReportDate(new Timestamp(new Date().getTime()));
//            report.setStatus("pending");
//
//            // Save report to database
//            int reportID = reportDAO.createReport(report);
//
//            if (reportID > 0) {
//                // Report successfully created
//                request.setAttribute("message", "Your report has been submitted successfully.");
//                request.getRequestDispatcher("post-details.jsp?id=" + postID).forward(request, response);
//            } else {
//                // Error creating report
//                request.setAttribute("error", "Failed to submit report. Please try again.");
//                showReportForm(request, response);
//            }
//
//        } catch (SQLException e) {
//            request.setAttribute("error", "Database error: " + e.getMessage());
//            showReportForm(request, response);
//        } catch (NumberFormatException e) {
//            request.setAttribute("error", "Invalid input: " + e.getMessage());
//            response.sendRedirect("index.jsp");
//        }
//    }
//
//    private void showReportForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            int postID = Integer.parseInt(request.getParameter("postID"));
//
//            // Get post details
//            Post post = postDAO.getPostById(postID);
//            request.setAttribute("post", post);
//
//            // Get report types
//            List<ReportType> reportTypes = reportDAO.getAllReportTypes();
//            request.setAttribute("reportTypes", reportTypes);
//
//            RequestDispatcher dispatcher = request.getRequestDispatcher("report-post.jsp");
//            dispatcher.forward(request, response);
//
//        } catch (SQLException e) {
//            request.setAttribute("error", "Database error: " + e.getMessage());
//            response.sendRedirect("index.jsp");
//        } catch (NumberFormatException e) {
//            request.setAttribute("error", "Invalid post ID");
//            response.sendRedirect("index.jsp");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}