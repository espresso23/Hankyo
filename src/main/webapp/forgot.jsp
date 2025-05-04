<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>Forgot Password</title>
</head>
<body>

<div class="container">
    <div class="verify-form row justify-content-center">
        <form method="post" action="forgot">
            <input type="hidden" name="action" value="sendResetEmail">
            <div class="input">
                <input type="gmail" id="gmail" name="gmail" placeholder="Enter your gmail" required>
            </div>
            <div class="buttonSignUp">
                <button type="submit" class="btn btn-secondary">Send Reset Code</button>
            </div>
            <c:if test="${not empty msg}">
                <div class="alert alert-danger">
                    <c:out value="${msg}"/>
                </div>
            </c:if>
        </form>
    </div>
</div>

</body>
</html>
