<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký làm Expert</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@700&family=Pacifico&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/expert-register.css">
    </head>
    <jsp:include page="header.jsp"></jsp:include>
    <body>
        <div class="content-section">
            <div class="banner-container">
                <div class="banner-left"></div>
                <div class="banner-right"></div>
            </div>
            <div class="form-side">
                <div class="form-container">
                    <h2 class="form-title">Đăng ký làm Expert</h2>
                    
                    <% if (request.getAttribute("success") != null) { %>
                        <div class="alert alert-success">
                            <%= request.getAttribute("success") %>
                        </div>
                    <% } %>
                    
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>

                    <form action="expert-register" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <div class="form-sections">
                            <div class="form-section">
                                <h4>Thông tin tài khoản</h4>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="username" class="form-label">Tên đăng nhập</label>
                                        <input type="text" class="form-control" id="username" name="username" required
                                               pattern="[a-zA-Z0-9_]{3,20}"
                                               title="Tên đăng nhập phải từ 3-20 ký tự, chỉ bao gồm chữ cái, số và dấu gạch dưới">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập tên đăng nhập hợp lệ (3-20 ký tự, chỉ bao gồm chữ cái, số và dấu gạch dưới)
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="password" class="form-label">Mật khẩu</label>
                                        <input type="password" class="form-control" id="password" name="password" required
                                               pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                                               title="Mật khẩu phải có ít nhất 8 ký tự, bao gồm cả chữ và số">
                                        <div class="invalid-feedback">
                                            Mật khẩu phải có ít nhất 8 ký tự, bao gồm cả chữ và số
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                                        <input type="password" class="form-control" id="confirmPassword" required>
                                        <div class="invalid-feedback">
                                            Mật khẩu xác nhận không khớp
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h4>Thông tin cá nhân</h4>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="fullName" class="form-label">Họ và tên</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" required
                                               pattern="^[\p{L} ]{2,50}$"
                                               title="Họ tên phải từ 2-50 ký tự, chỉ bao gồm chữ cái và khoảng trắng">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập họ tên hợp lệ (2-50 ký tự)
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="gmail" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="gmail" name="gmail" required
                                               pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập địa chỉ email hợp lệ
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="phone" class="form-label">Số điện thoại</label>
                                        <input type="tel" class="form-control" id="phone" name="phone" required
                                               pattern="[0-9]{10}"
                                               title="Số điện thoại phải có 10 chữ số">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập số điện thoại hợp lệ (10 chữ số)
                                        </div>
                                    </div>
                                    <div class="form-group gender-group">
                                        <label for="gender" class="form-label">Giới tính</label>
                                        <select class="form-select" id="gender" name="gender" required>
                                            <option value="">Chọn giới tính</option>
                                            <option value="male">Nam</option>
                                            <option value="female">Nữ</option>
                                            <option value="other">Khác</option>
                                        </select>
                                        <div class="invalid-feedback">
                                            Vui lòng chọn giới tính
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h4>Giấy tờ tùy thân</h4>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="cccd" class="form-label">Số CCCD/CMND</label>
                                        <input type="text" class="form-control" id="cccd" name="cccd" required
                                               pattern="[0-9]{9}|[0-9]{12}"
                                               title="CCCD phải có 9 hoặc 12 chữ số">
                                        <div class="invalid-feedback">
                                            CCCD/CMND phải có 9 hoặc 12 chữ số
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="cccdFront" class="form-label">Ảnh CCCD mặt trước</label>
                                        <input type="file" class="form-control" id="cccdFront" name="cccdFront" accept="image/*" required
                                               onchange="previewImage(this, 'cccdFrontPreview')">
                                        <img id="cccdFrontPreview" class="preview-image" alt="Preview CCCD mặt trước">
                                        <div class="invalid-feedback">
                                            Vui lòng chọn ảnh CCCD mặt trước
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="cccdBack" class="form-label">Ảnh CCCD mặt sau</label>
                                        <input type="file" class="form-control" id="cccdBack" name="cccdBack" accept="image/*" required
                                               onchange="previewImage(this, 'cccdBackPreview')">
                                        <img id="cccdBackPreview" class="preview-image" alt="Preview CCCD mặt sau">
                                        <div class="invalid-feedback">
                                            Vui lòng chọn ảnh CCCD mặt sau
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h4>Thông tin chuyên môn</h4>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="avatar" class="form-label">Ảnh đại diện</label>
                                        <input type="file" class="form-control" id="avatar" name="avatar" accept="image/*" required
                                               onchange="previewImage(this, 'avatarPreview')">
                                        <img id="avatarPreview" class="preview-image" alt="Preview">
                                        <div class="invalid-feedback">
                                            Vui lòng chọn ảnh đại diện
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="certificate" class="form-label">Chứng chỉ (Hình ảnh hoặc PDF)</label>
                                        <input type="file" class="form-control" id="certificate" name="certificate" accept="image/*,.pdf" required>
                                        <small class="form-text text-muted">Chấp nhận file hình ảnh (JPG, PNG) hoặc PDF</small>
                                        <div class="invalid-feedback">
                                            Vui lòng chọn file chứng chỉ
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="button-row">
                            <button type="submit" class="btn btn-primary btn-submit">Gửi đơn đăng ký</button>
                            <a href="home" class="btn btn-secondary">Quay lại trang chủ</a>
                        </div>
                    </form>
                </div>
            </div>
            <div class="info-side">
                <div class="large-background-image"></div>
                <div class="info-content">
                    <div class="info-title">ĐĂNG KÝ GIÁO VIÊN TIẾNG HÀN</div>
                    <div class="info-list">Trở thành giáo viên tiếng Hàn chuyên nghiệp</div>
                    <div class="info-list">Cơ hội giảng dạy online linh hoạt</div>
                    <div class="info-list">Thu nhập hấp dẫn, chủ động thời gian</div>
                    <div class="info-list">Tham gia cộng đồng giáo viên năng động</div>
                    <div class="info-list">Phát triển kỹ năng, mở rộng mối quan hệ</div>
                    <div class="info-images-art">
                        <img src="${pageContext.request.contextPath}/asset/png/expertRegister/giao-tiep-tieng-han.jpg" alt="Art 1" class="info-art-img">
                        <img src="${pageContext.request.contextPath}/asset/png/expertRegister/hócinh.jpg" alt="Art 2" class="info-art-img">
                    </div>
                </div>
                <img src="${pageContext.request.contextPath}/asset/png/expertRegister/girl.png" alt="Logo" class="info-girl-logo">
                <div class="form-wrapper">
                    <!-- ... form ... -->
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/asset/js/expert-register.js"></script>
    </body>
    <jsp:include page="footer.jsp"></jsp:include>
</html> 