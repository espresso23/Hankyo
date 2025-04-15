//package filter;
//
//import model.Expert;
//import model.User;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.*;
//import java.io.IOException;
//
//@WebFilter("/*")
//public class AuthenFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        HttpSession session = httpRequest.getSession(false);
//        User user = (session != null) ? (User) session.getAttribute("user") : null;
//        Expert expert = (session != null) ? (Expert) session.getAttribute("expert") : null;
//        String requestURI = httpRequest.getRequestURI();
//
//        if (requestURI.endsWith("home.jsp") || requestURI.contains("/home") ||
//               requestURI.contains("/register") ||
//                requestURI.endsWith("login.jsp") || requestURI.contains("/login") ||
//           requestURI.contains("/google") || requestURI.contains("asset/ ") || requestURI.contains("css") || requestURI.contains("js") || requestURI.contains("images"))
//        {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        if (user == null) {
//            httpResponse.sendRedirect("login.jsp");
//
//            return;
//        }
//
//        if (requestURI.contains("admin.jsp") && !"Admin".equals(user.getRole())) {
//            httpResponse.sendRedirect("home.jsp");
//            return;
//        }
//
//        if (requestURI.contains("expert.jsp") && !"expert".equals(user.getRole())) {
//            httpResponse.sendRedirect("home.jsp");
//            return;
//        }
//
//        // Nếu hợp lệ, tiếp tục xử lý request
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}