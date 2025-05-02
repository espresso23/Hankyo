<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký làm Expert</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .form-container {
                max-width: 800px;
                margin: 50px auto;
                padding: 20px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                border-radius: 8px;
            }
            .form-title {
                text-align: center;
                margin-bottom: 30px;
                color: #333;
            }
            .form-label {
                font-weight: 500;
                margin-bottom: 8px;
            }
            .alert {
                margin-bottom: 20px;
            }
            .preview-image {
                max-width: 200px;
                max-height: 200px;
                margin-top: 10px;
                display: none;
                border: 1px solid #ddd;
                border-radius: 4px;
                padding: 5px;
            }
            .form-group {
                margin-bottom: 20px;
            }
            .invalid-feedback {
                display: none;
                color: #dc3545;
                font-size: 14px;
            }
            .form-control.is-invalid ~ .invalid-feedback {
                display: block;
            }
            .btn-submit {
                padding: 10px 30px;
                font-size: 16px;
            }
        </style>
    </head>
    <body>
        <div class="container">
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

                    <div class="form-group">
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

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-submit">Gửi đơn đăng ký</button>
                        <a href="home" class="btn btn-secondary">Quay lại trang chủ</a>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Xử lý preview ảnh
            function previewImage(input, previewId) {
                const preview = document.getElementById(previewId);
                if (input.files && input.files[0]) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        preview.src = e.target.result;
                        preview.style.display = 'block';
                    }
                    reader.readAsDataURL(input.files[0]);
                } else {
                    preview.style.display = 'none';
                }
            }

            // Xử lý validate form
            (function () {
                'use strict'

                const forms = document.querySelectorAll('.needs-validation');
                const password = document.getElementById('password');
                const confirmPassword = document.getElementById('confirmPassword');

                // Kiểm tra mật khẩu xác nhận
                function validatePassword() {
                    if (password.value !== confirmPassword.value) {
                        confirmPassword.setCustomValidity('Mật khẩu xác nhận không khớp');
                    } else {
                        confirmPassword.setCustomValidity('');
                    }
                }

                password.addEventListener('change', validatePassword);
                confirmPassword.addEventListener('keyup', validatePassword);

                // Xử lý validate form
                Array.from(forms).forEach(form => {
                    form.addEventListener('submit', event => {
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            })();
        </script>
    </body>
</html> 