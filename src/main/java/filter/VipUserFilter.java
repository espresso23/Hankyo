package filter;

import dao.VipUserDAO;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Chỉ áp dụng cho các trang không phải blog hoặc postDetails
@WebFilter(urlPatterns = {"/*"}, 
           dispatcherTypes = {DispatcherType.REQUEST})
public class VipUserFilter implements Filter {
    private VipUserDAO vipUserDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        vipUserDAO = new VipUserDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        
        // Lấy đường dẫn URL hiện tại
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());
        
        // Kiểm tra nếu không phải là các trang blog hoặc postDetails
        if (!path.startsWith("/blog") && !path.startsWith("/postDetails")) {
            if (session != null) {
                // Lấy userID từ session
                Object userObj = session.getAttribute("user");
                Integer userID = null;
                
                if (userObj != null && userObj instanceof model.User) {
                    userID = ((model.User) userObj).getUserID();
                } else {
                    // Kiểm tra cả trường hợp userID được lưu trực tiếp trong session
                    Object userIDObj = session.getAttribute("userID");
                    if (userIDObj instanceof String) {
                        try {
                            userID = Integer.parseInt((String) userIDObj);
                        } catch (NumberFormatException e) {
                            userID = null;
                        }
                    } else if (userIDObj instanceof Integer) {
                        userID = (Integer) userIDObj;
                    }
                }
                
                if (userID != null) {
                    try {
                        // Sử dụng hàm isVipUser đã được sửa đổi để chuyển đổi từ userID sang learnerID
                        boolean isVip = vipUserDAO.isVipUser(userID);
                        System.out.println("DEBUG - User VIP check in filter for userID: " + userID + ", VIP status: " + isVip);
                        request.setAttribute("isUserVip", isVip);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute("isUserVip", false);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Clean up resources if needed
    }
} 