<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Verification</title>
    <link rel="stylesheet" href="asset/css/verify.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap.min.css">
    <style>
        @font-face {
            font-family: 'Poppins';
            src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
        }
        body{
            font-family: 'Poppins',sans-serif;
        }
        body {
            background-image: url("${pageContext.request.contextPath}/asset/png/background/background.png");
            background-repeat: no-repeat;
            background-size: cover;
        }
    </style>
</head>
<body>

<!-- Header -->
<jsp:include page="/header.jsp" />

<!-- Main Content -->
<div class="background-verify">
    <div class="verify-form">
        <div class="welcomeline">
            <h2>ENTER THE CODE FROM YOUR EMAIL</h2>
            <p>Enter the code sent to <strong>${sessionScope['gmail']}</strong>.</p>
        </div>
        <!-- Form to submit OTP for verification -->
        <form class="form" method="post" action="${pageContext.request.contextPath}/verify">
            <div class="form-group">
                <input type="text" id="code" name="otp-code" required>
                <button type="submit" name="action" value="validate" class="submitBtn btn-primary">
                    Verify Code
                </button>
            </div>

            <div class="d-flex">
                <!-- Resend OTP -->
                <button class="sendBtn btn-secondary" type="submit" name="action" value="resend">
                    Send Email Again
                </button>

                <!-- Validate OTP -->

            </div>
        </form>
    </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/Welcome.js"></script>
</body>
<jsp:include page="/footer.jsp" />
</html>
