package controller;

import cloud.CloudinaryConfig;
import com.cloudinary.utils.ObjectUtils;
import dao.ExpertRegisterDAO;
import model.ExpertRegister;
import util.Encrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "ExpertRegisterServlet", urlPatterns = {"/expert-register"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ExpertRegisterServlet extends HttpServlet {
    private ExpertRegisterDAO expertRegisterDAO;

    @Override
    public void init() throws ServletException {
        expertRegisterDAO = new ExpertRegisterDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/expert-register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        try {
            // Lấy thông tin từ form
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String gmail = request.getParameter("gmail");
            String phone = request.getParameter("phone");
            String fullName = request.getParameter("fullName");
            String gender = request.getParameter("gender");
            String cccd = request.getParameter("cccd");

            // Xử lý file avatar
            Part avatarPart = request.getPart("avatar");
            String avatarUrl = null;
            
            if (avatarPart != null && avatarPart.getSize() > 0) {
                // Upload avatar lên Cloudinary
                Map uploadResult = CloudinaryConfig.getCloudinary().uploader().upload(
                    avatarPart.getInputStream().readAllBytes(),
                    ObjectUtils.emptyMap()
                );
                avatarUrl = (String) uploadResult.get("url");
            }

            // Xử lý file chứng chỉ
            Part certificatePart = request.getPart("certificate");
            String certificateUrl = null;
            
            if (certificatePart != null && certificatePart.getSize() > 0) {
                // Upload chứng chỉ lên Cloudinary
                Map uploadResult = CloudinaryConfig.getCloudinary().uploader().upload(
                    certificatePart.getInputStream().readAllBytes(),
                    ObjectUtils.emptyMap()
                );
                certificateUrl = (String) uploadResult.get("url");
            }

            // Xử lý ảnh CCCD mặt trước
            Part cccdFrontPart = request.getPart("cccdFront");
            String cccdFrontUrl = null;
            
            if (cccdFrontPart != null && cccdFrontPart.getSize() > 0) {
                Map uploadResult = CloudinaryConfig.getCloudinary().uploader().upload(
                    cccdFrontPart.getInputStream().readAllBytes(),
                    ObjectUtils.emptyMap()
                );
                cccdFrontUrl = (String) uploadResult.get("url");
            }

            // Xử lý ảnh CCCD mặt sau
            Part cccdBackPart = request.getPart("cccdBack");
            String cccdBackUrl = null;
            
            if (cccdBackPart != null && cccdBackPart.getSize() > 0) {
                Map uploadResult = CloudinaryConfig.getCloudinary().uploader().upload(
                    cccdBackPart.getInputStream().readAllBytes(),
                    ObjectUtils.emptyMap()
                );
                cccdBackUrl = (String) uploadResult.get("url");
            }

            // Tạo đối tượng ExpertRegister
            ExpertRegister register = new ExpertRegister();
            register.setUsername(username);
            register.setPassword(Encrypt.hashPassword(password));
            register.setGmail(gmail);
            register.setPhone(phone);
            register.setRole("expert");
            register.setStatus("pending");
            register.setFullName(fullName);
            register.setGender(gender);
            register.setAvatar(avatarUrl);
            register.setCertificate(certificateUrl);
            register.setCccd(cccd);
            register.setCccdFront(cccdFrontUrl);
            register.setCccdBack(cccdBackUrl);
            register.setApproveStatus("waiting");

            // Kiểm tra username đã tồn tại chưa
            if (expertRegisterDAO.isUsernameExists(username)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
                request.getRequestDispatcher("/expert-register.jsp").forward(request, response);
                return;
            }

            // Kiểm tra email đã tồn tại chưa
            if (expertRegisterDAO.isGmailExists(gmail)) {
                request.setAttribute("error", "Email đã được sử dụng. Vui lòng dùng email khác.");
                request.getRequestDispatcher("/expert-register.jsp").forward(request, response);
                return;
            }

            if (expertRegisterDAO.createExpertRegister(register)) {
                request.setAttribute("success", "Đăng ký thành công! Chúng tôi sẽ xem xét đơn của bạn.");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi xử lý file: " + e.getMessage());
        }

        request.getRequestDispatcher("/expert-register.jsp").forward(request, response);
    }
} 