<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>Sign-Up</title>
    <link rel="stylesheet" href="asset/css/register.css">
</head>
<jsp:include page="/header.jsp" />
<style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins&display=swap');
    body {
        font-family: 'Poppins', sans-serif;
        background-image: url("asset/png/background/background.png");
        background-repeat: no-repeat;
        background-size: auto;
    }
    .alert-danger {
        color: #721c24;
        background-color: #f8d7da;
        border-color: #f5c6cb;
        padding: 10px;
        margin: 10px 0;
        border-radius: 5px;
    }
</style>
<body>
<div class="container-fluid">
    <div class="loginGirl">
        <img src="asset/png/loginPage/loginGirl.png">
    </div>
    <div class="signUp-form">
        <h2>Register</h2>
        <div class="${not empty msg ? 'show' : ''}">
            <c:if test="${not empty msg}">
                <div class="alert alert-danger">
                    <c:out value="${msg}"/>
                </div>
            </c:if>
        </div>
        <div>
            <form method="POST" action="${pageContext.request.contextPath}/register">
                <div class="input">
                    <input type="text" id="fullName" name="fullName" placeholder="Enter your full name" required>
                </div>
                <div class="input">
                    <input type="text" id="username" name="username" placeholder="Username" required>
                </div>
                <div class="input">
                    <input type="email" id="gmail" name="gmail" placeholder="Gmail" required>
                </div>
                <div class="input">
                    <input type="password" id="password" name="password" placeholder="Password" required>
                </div>
                <div class="input">
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Re-enter Password" required>
                </div>
                <div class="input">
                    <input type="tel" id="phone" name="phone" placeholder="Phone Number" required>
                </div>
                <h6>Gender:</h6>
                <div class="gender-selection">
                    <label for="male">
                        <input type="radio" id="male" name="gender" value="Male" required>
                        Male
                    </label>
                    <label for="female">
                        <input type="radio" id="female" name="gender" value="Female" required>
                        Female
                    </label>
                </div>
                <a href="${pageContext.request.contextPath}/login" style="margin-left: 5px;">Already have an account?</a>
                <div class="buttonRegister" style="display: flex; justify-content: center; margin-top: 18px;">
                    <button type="submit">Sign Up</button>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
