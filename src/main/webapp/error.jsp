<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lỗi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .error-container {
            max-width: 600px;
            margin: 100px auto;
            text-align: center;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .error-icon {
            font-size: 48px;
            color: #dc3545;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-container">
            <div class="error-icon">⚠️</div>
            <h2 class="mb-4">Đã xảy ra lỗi</h2>
            <p class="text-danger mb-4">${error}</p>
            <div class="mt-4">
                <a href="javascript:history.back()" class="btn btn-secondary">Quay lại</a>
                <a href="index.jsp" class="btn btn-primary">Về trang chủ</a>
            </div>
        </div>
    </div>
</body>
</html> 