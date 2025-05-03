<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/login.css">
    <title>Login Page</title>

</head>
<style>
    @font-face {
        font-family: 'Poppins';
        src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
    }
    body{
        font-family: 'Poppins',sans-serif;
    }
    body{
        background-image: url("asset/png/background/background.png");
        background-repeat: no-repeat;
        background-size: auto;
    }
</style>
<jsp:include page="header.jsp"/>
<body>
<div class="container-fluid">
    <div class="login-form">
        <h2>Đăng Nhập</h2>
        <form method="post" action="login">
            <div class="input">
                <div class="nameInput">
                    <input type="text" id="userName" name="Username" placeholder="Enter Username" required>
                </div>
                <div class="passInput">
                    <input type="password" id="pass" name="Password" placeholder="Enter Password" required>
                </div>
            </div>

            <div class="loginButton">
                <button type="submit">Login</button>
                <label>
                    Remember Me
                    <input type="checkbox" checked>
                </label>
            </div>
            <div class="loginMethod">
                <div class="btn btn-outline-danger d-flex align-items-center p-2 mb-2" onclick="location.href='https://accounts.google.com/o/oauth2/v2/auth?client_id=830752377691-2voobquum0stjf13a9sc5mr8oiivkebc.apps.googleusercontent.com&redirect_uri=http://localhost:8080/Hankyo/google&response_type=code&scope=profile email'">
                    <img src="https://www.google.com/favicon.ico" alt="Google Logo" width="20" class="me-2">
                    Đăng nhập bằng Google
                </div>

                <div class="loginFacebook btn-outline-primary d-flex align-items-center p-2" onclick="location.href='https://www.facebook.com/v19.0/dialog/oauth?client_id=512051824934535&redirect_uri=http://localhost:8080/Hankyo/facebook&scope=email,public_profile'">
                    <img src="https://www.facebook.com/favicon.ico" alt="Facebook Logo" width="20" class="me-2">
                    Đăng nhập bằng Facebook
                </div>
            </div>
        </form>
        <c:if test="${not empty errorMsg}">
            <div id="errorMsg">
                <c:out value="${errorMsg}"/>
            </div>
        </c:if>
        <c:if test="${not empty msg}">
            <div id="msg">
                <c:out value="${msg}"/>
            </div>
        </c:if>
        <!-- Check box rememberMe -->
        <div>
            <div class="forgotPassword" >
                <a href="${pageContext.request.contextPath}/forgot">Forgot Password?</a>
            </div>
        </div>
    </div>

</div>

</body>
<jsp:include page="footer.jsp"/>
</html>