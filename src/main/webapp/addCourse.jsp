<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thêm Khóa Học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<c:import url="header.jsp"/>

<div class="container mt-4">
    <h2 class="text-center mb-4">Thêm Khóa Học Mới</h2>

    <form action="course?action=add" method="post" enctype="multipart/form-data" class="mx-auto" style="max-width: 800px;">
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

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i>Thêm khóa học
            </button>
            <a href="course?action=list" class="btn btn-secondary">
                <i class="fas fa-times me-2"></i>Hủy
            </a>
        </div>
    </form>
</div>

<c:import url="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
