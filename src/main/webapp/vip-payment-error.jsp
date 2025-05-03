<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thất bại</title>
    <style>
        body {
            background: linear-gradient(135deg, #ffe0e0 0%, #ffd6f0 100%);
            font-family: 'Segoe UI', Arial, sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .error-container {
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.12);
            padding: 48px 36px;
            text-align: center;
            max-width: 400px;
        }
        .error-icon {
            font-size: 64px;
            color: #e74c3c;
            margin-bottom: 18px;
        }
        h2 {
            color: #e74c3c;
            margin-bottom: 12px;
        }
        p {
            color: #444;
            margin-bottom: 32px;
        }
        .back-btn {
            background: linear-gradient(90deg, #e74c3c 0%, #e67e22 100%);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 12px 32px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.2s;
        }
        .back-btn:hover {
            background: linear-gradient(90deg, #e67e22 0%, #e74c3c 100%);
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<div class="error-container">
    <div class="error-icon"><i class="fas fa-times-circle"></i></div>
    <h2>Thanh toán thất bại!</h2>
    <p>Đã có lỗi xảy ra trong quá trình thanh toán.<br>Vui lòng thử lại hoặc liên hệ hỗ trợ.</p>
    <button class="back-btn" onclick="window.location.href='bundles'">Quay lại</button>
</div>
</body>
</html> 