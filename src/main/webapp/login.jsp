<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login Page</title>
</head>
<body>
<div>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div>
            <input type="text" id="userName" name="Username" placeholder="Enter Username" required>
        </div>
        <div>
            <input type="password" id="pass" name="Password" placeholder="Enter Password" required>
        </div>
        <div>
            <button type="submit">Login</button>
            <a href="${pageContext.request.contextPath}/register">Sign Up</a>
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
        <div>
            <label>
                Remember Me
                <input type="checkbox" checked>
            </label>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/forgot">Forgot Password?</a>
        </div>
    </div>
    <!-- or login with... -->
    <p>- Or Sign In With -</p>
    <!-- Social Login -->
    <div>
        <a href="https://www.facebook.com/v19.0/dialog/oauth?client_id=511369765177582&redirect_uri=http://localhost:8080/Struggle/facebook&scope=email,public_profile">
            <button type="button">Facebook</button>
        </a>
        <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=795533140932-3a154s92gk1jr5haqpit326raetl2g7u.apps.googleusercontent.com&redirect_uri=http://localhost:8080/Struggle/google&response_type=code&scope=profile email">
            <button type="button">Google</button>
        </a>
    </div>
</div>

</body>
</html>
