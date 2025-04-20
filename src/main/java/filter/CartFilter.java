//package filter;
//
//import dao.CartDAO;
//import model.User;
//import util.DBConnect;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.Connection;
//
//@WebFilter("/*")
//public class CartFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpSession session = httpRequest.getSession(false);
//
//        if (session != null) {
//            User user = (User) session.getAttribute("user");
//            if (user != null) {
//                CartDAO cartDAO = new CartDAO();
//                int cartItemCount = cartDAO.getCartItemCount(user.getUserID());
//                request.setAttribute("cartItemCount", cartItemCount);
//            }
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}