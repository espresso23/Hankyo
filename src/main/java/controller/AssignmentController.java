//package controller;
//
//import dao.AssignmentDAO;
//import model.Assignment;
//import util.DBConnect;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//@WebServlet("/assignment")
//public class AssignmentController extends HttpServlet {
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        AssignmentDAO assignmentDAO = new AssignmentDAO();
//
//        if (action == null || action.equals("list")) {
//            List<Assignment> assignments = assignmentDAO.getAllAssignments();
//            request.setAttribute("assignments", assignments);
//            request.getRequestDispatcher("assignments.jsp").forward(request, response);
//        } else if (action.equals("delete")) {
//            int id = Integer.parseInt(request.getParameter("id"));
//            //assignmentDAO.deleteAssignment(id);
//            response.sendRedirect("assignment?action=list");
//        }
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Connection connection = DBConnect.getInstance().getConnection();
//        AssignmentDAO assignmentDAO = new AssignmentDAO();
//
//        String action = request.getParameter("action");
//        if (action.equals("create")) {
//            String title = request.getParameter("title");
//            Assignment newAssignment = new Assignment();
//            newAssignment.setAssignmentTitle(title);
//            try {
//                assignmentDAO.createEmptyAssignment();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            response.sendRedirect("assignment?action=list");
//        } else if (action.equals("update")) {
//            int id = Integer.parseInt(request.getParameter("id"));
//            String title = request.getParameter("title");
//            Assignment updatedAssignment = new Assignment();
//            updatedAssignment.setAssignmentID(id);
//            updatedAssignment.setAssignmentTitle(title);
////            assignmentDAO.updateAssignment(updatedAssignment);
//            response.sendRedirect("assignment?action=list");
//        }
//    }
//}
