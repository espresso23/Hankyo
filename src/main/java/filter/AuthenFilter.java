package filter;

import model.Expert;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthenFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        String requestURI = httpRequest.getRequestURI();

        // Cho phép truy cập các tài nguyên tĩnh và trang đăng nhập/đăng ký
        if (requestURI.endsWith("courseHeader.jsp") || 
            requestURI.contains("/home") ||
            requestURI.contains("/register") ||
            requestURI.endsWith("login.jsp") || 
            requestURI.contains("/login") ||
            requestURI.contains("/google") || 
            requestURI.contains("/asset/") || 
            requestURI.contains("/css/") || 
            requestURI.contains("/js/") || 
            requestURI.contains("/images/") ||
            requestURI.contains("/fonts/")) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra đăng nhập
        if (user == null) {
            httpResponse.sendRedirect("login.jsp");
            return;
        }

        // Kiểm tra quyền truy cập admin
        if (requestURI.contains("/admin/") && !"admin".equalsIgnoreCase(user.getRole())) {
            httpResponse.sendRedirect("home.jsp");
            return;
        }

        // Kiểm tra quyền truy cập expert
        if (requestURI.contains("/expert/") && !"expert".equalsIgnoreCase(user.getRole())) {
            httpResponse.sendRedirect("home.jsp");
            return;
        }

        // Nếu hợp lệ, tiếp tục xử lý request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}