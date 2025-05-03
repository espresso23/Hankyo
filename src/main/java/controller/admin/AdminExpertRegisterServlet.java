package controller.admin;

import dao.ExpertRegisterDAO;
import dao.ExpertDAO;
import model.ExpertRegister;
import model.Expert;
import util.SmtpProtocol;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/admin/expert-registers")
public class AdminExpertRegisterServlet extends HttpServlet {
    private ExpertRegisterDAO expertRegisterDAO = new ExpertRegisterDAO();
    private ExpertDAO expertDAO = new ExpertDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            List<ExpertRegister> pending = expertRegisterDAO.getPendingRegisters();
            resp.getWriter().write(gson.toJson(pending));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Lỗi DB\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int registerId = Integer.parseInt(req.getParameter("registerId"));
        String action = req.getParameter("action"); // "approve" hoặc "reject"
        SmtpProtocol smtp = new SmtpProtocol();

        try {
            ExpertRegister reg = expertRegisterDAO.getById(registerId);
            if (reg == null) {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Không tìm thấy đơn\"}");
                return;
            }

            if ("approve".equals(action)) {
                // Tạo expert mới
                Expert expert = ExpertDAO.convertFromExpertRegister(reg);
                expertDAO.createExpert(expert);

                // Cập nhật trạng thái đơn
                expertRegisterDAO.updateApproveStatus(registerId, "approved");

                // Gửi mail thành công kèm username
                String subject = "Đăng ký expert trên Hankyo thành công";
                String content = "Dear " + reg.getFullName() + ",\n\n"
                    + "Chúc mừng bạn đã đăng ký thành công và trở thành expert trên nền tảng HanKyo!\n\n"
                    + "Thông tin tài khoản của bạn:\n"
                    + "- Tên đăng nhập: " + reg.getUsername() + "\n"
                    + "- Email: " + reg.getGmail() + "\n\n"
                    + "Bạn có thể đăng nhập vào hệ thống tại https://hankyo.com/login để bắt đầu chia sẻ các khóa học của mình.\n\n"
                    + "Lưu ý: Vui lòng bảo mật thông tin tài khoản và không chia sẻ cho người khác. Nếu có bất kỳ thắc mắc nào, hãy liên hệ đội ngũ hỗ trợ của HanKyo.\n\n"
                    + "Trân trọng,\nĐội ngũ HanKyo";
                smtp.sendMailCustom(reg.getGmail(), subject, content);
                resp.getWriter().write("{\"success\":true, \"message\":\"Đã duyệt và gửi mail thành công\"}");
            } else if ("reject".equals(action)) {
                expertRegisterDAO.updateApproveStatus(registerId, "rejected");
                // Gửi mail từ chối chuyên nghiệp
                String subject = "Đăng ký expert trên Hankyo bị từ chối";
                String content = "Dear " + reg.getFullName() + ",\n\n"
                    + "Chúng tôi rất tiếc phải thông báo rằng đơn đăng ký trở thành expert trên nền tảng HanKyo của bạn chưa được chấp nhận vào thời điểm này.\n\n"
                    + "Bạn có thể xem lại thông tin đăng ký và nộp lại đơn trong tương lai nếu muốn. Nếu cần hỗ trợ hoặc có thắc mắc, vui lòng liên hệ đội ngũ HanKyo qua email support@hankyo.com.\n\n"
                    + "Cảm ơn bạn đã quan tâm và mong sẽ được hợp tác cùng bạn trong thời gian tới!\n\n"
                    + "Trân trọng,\nĐội ngũ HanKyo";
                smtp.sendMailCustom(reg.getGmail(), subject, content);
                resp.getWriter().write("{\"success\":true, \"message\":\"Đã từ chối và gửi mail\"}");
            } else {
                resp.setStatus(400);
                resp.getWriter().write("{\"error\":\"Hành động không hợp lệ\"}");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}