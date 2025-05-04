<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thêm Khóa Học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #ffd1d1 0%, #e8f0ff 100%);
            min-height: 100vh;
        }
        .add-course-card {
            background: rgba(255,255,255,0.95);
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.10);
            padding: 40px 36px 32px 36px;
            max-width: 600px;
            margin: 40px auto;
            transition: box-shadow 0.3s;
        }
        .add-course-card:hover {
            box-shadow: 0 12px 40px rgba(0,0,0,0.18);
        }
        .form-label {
            font-weight: 500;
            color: #ff8fa3;
        }
        .form-control, .form-select, textarea {
            border-radius: 12px;
            border: 1.5px solid #e8f0ff;
            box-shadow: none;
            transition: border-color 0.2s;
        }
        .form-control:focus, .form-select:focus, textarea:focus {
            border-color: #ff8fa3;
            box-shadow: 0 0 0 2px #ff8fa340;
        }
        .btn-primary {
            background: linear-gradient(45deg, #ff8fa3, #6cb4ff);
            border: none;
            border-radius: 12px;
            font-weight: 600;
            padding: 10px 28px;
            box-shadow: 0 4px 16px #ff8fa340;
            transition: background 0.2s, box-shadow 0.2s, transform 0.2s;
        }
        .btn-primary:hover {
            background: linear-gradient(45deg, #6cb4ff, #ff8fa3);
            box-shadow: 0 8px 24px #6cb4ff40;
            transform: translateY(-2px);
        }
        .btn-secondary {
            border-radius: 12px;
            font-weight: 500;
            padding: 10px 28px;
        }
        .form-text {
            color: #6cb4ff;
        }
        .page-title {
            font-size: 2.2rem;
            font-weight: 700;
            color: #6cb4ff;
            margin-bottom: 1.5rem;
            text-align: center;
            letter-spacing: 1px;
        }
        @media (max-width: 600px) {
            .add-course-card {
                padding: 18px 8px;
            }
        }
    </style>
</head>
<body>

<c:import url="header.jsp"/>

<div class="container">
    <div class="add-course-card">
        <div class="page-title mb-4">
            <i class="fas fa-plus-circle me-2"></i>Thêm Khóa Học Mới
        </div>
        <form action="course?action=add" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <label for="title" class="form-label">Tiêu đề khóa học</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>

            <div class="mb-3">
                <label for="description" class="form-label">Mô tả</label>
                <textarea class="form-control" id="description" name="description" rows="4" required></textarea>
            </div>

            <div class="mb-3">
                <label for="categoryID" class="form-label">Danh mục</label>
                <select class="form-select" id="categoryID" name="categoryID" required>
                    <option value="">Chọn danh mục</option>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.categoryID}">${category.categoryName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label for="image" class="form-label">Ảnh khóa học</label>
                <input type="file" class="form-control" id="image" name="image" accept="image/*" required>
                <div class="form-text">Chỉ chấp nhận file ảnh (JPG, PNG, GIF)</div>
            </div>

            <div class="mb-3">
                <label for="price" class="form-label">Giá khóa học (VNĐ)</label>
                <input type="number" class="form-control" id="price" name="price" min="0" required>
            </div>

            <div class="mb-3">
                <label for="originalPrice" class="form-label">Giá gốc (VNĐ)</label>
                <input type="number" class="form-control" id="originalPrice" name="originalPrice" min="0">
                <div class="form-text">Để trống nếu không có giá gốc</div>
            </div>

            <div class="mb-3">
                <label for="status" class="form-label">Trạng thái</label>
                <select class="form-select" id="status" name="status">
                    <option value="Active">Hoạt động</option>
                    <option value="Inactive">Tạm ngừng</option>
                </select>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-plus me-2"></i>Thêm khóa học
                </button>
                <a href="course?action=list" class="btn btn-secondary">
                    <i class="fas fa-times me-2"></i>Hủy
                </a>
            </div>
        </form>
    </div>
</div>

<c:import url="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
