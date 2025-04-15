<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thêm Khóa Học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>

<c:import url="header.jsp"/>

<div class="container mt-4">
    <h2 class="text-center">Thêm Khóa Học Mới</h2>

    <form action="course?action=add" method="post" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="title" class="form-label">Tiêu đề khóa học</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Mô tả</label>
            <textarea class="form-control" id="description" name="description" required></textarea>
        </div>

        <div class="mb-3">
            <label for="image" class="form-label">Ảnh khóa học</label>
            <input type="file" class="form-control" id="image" name="image" accept="image/*" required>
        </div>

        <div class="mb-3">
            <label for="price" class="form-label">Giá khóa học (VNĐ)</label>
            <input type="number" class="form-control" id="price" name="price" required>
        </div>

        <div class="mb-3">
            <label for="status" class="form-label">Trạng thái</label>
            <select class="form-select" id="status" name="status">
                <option value="Active">Hoạt động</option>
                <option value="Inactive">Tạm ngừng</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Thêm khóa học</button>
        <a href="course?action=list" class="btn btn-secondary">Hủy</a>
    </form>
</div>

<c:import url="footer.jsp"/>

</body>
</html>
