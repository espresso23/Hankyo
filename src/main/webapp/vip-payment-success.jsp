<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thành công</title>
    <style>
        body {
            background: linear-gradient(135deg, #e0ffe8 0%, #b2f0ff 100%);
            font-family: 'Segoe UI', Arial, sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .success-container {
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.12);
            padding: 48px 36px;
            text-align: center;
            max-width: 400px;
        }
        .success-icon {
            font-size: 64px;
            color: #2ecc71;
            margin-bottom: 18px;
        }
        h2 {
            color: #2ecc71;
            margin-bottom: 12px;
        }
        p {
            color: #444;
            margin-bottom: 32px;
        }
        .back-btn {
            background: linear-gradient(90deg, #2ecc71 0%, #27ae60 100%);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 12px 32px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.2s;
        }
        .back-btn:hover {
            background: linear-gradient(90deg, #27ae60 0%, #2ecc71 100%);
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<div class="success-container">
    <div class="success-icon"><i class="fas fa-check-circle"></i></div>
    <h2>Thanh toán thành công!</h2>
    <p>Bạn đã đăng ký VIP thành công.<br>Hãy tận hưởng các đặc quyền của mình!</p>
    <button class="back-btn" onclick="window.location.href='bundles'">Quay lại</button>
</div>
</body>
</html> 