<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <title>Trang chủ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .btn-custom {
            margin: 10px;
            padding: 15px 30px;
            font-size: 1.1em;
        }
        .container {
            margin-top: 50px;
            text-align: center;
        }
        .btn-group-vertical {
            width: 100%;
            max-width: 300px;
            margin: 0 auto;
        }
        .welcome-text {
            color: #2c3e50;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="welcome-text mb-4">Chào mừng đến với Hệ thống học trực tuyến</h2>
        <div class="btn-group-vertical">
            <button type="button" class="btn btn-primary btn-custom mb-3" 
                    onclick="window.location.href='${pageContext.request.contextPath}/courses'">
                <i class="fas fa-graduation-cap"></i> Xem tất cả khóa học
            </button>
            
            <button type="button" class="btn btn-success btn-custom mb-3" 
                    onclick="window.location.href='${pageContext.request.contextPath}/my-courses'">
                <i class="fas fa-book-reader"></i> Khóa học của tôi
            </button>
            
            <button type="button" class="btn btn-info btn-custom" 
                    onclick="window.location.href='${pageContext.request.contextPath}/update'">
                <i class="fas fa-user-edit"></i> Thay đổi thông tin
            </button>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/a076d05399.js"></script>
</body>
</html>
