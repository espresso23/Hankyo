<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <title>Reset Password</title>
</head>
<body>
<div class="container">
    <div class="verify-form row justify-content-center">
        <form method="post" action="reset">
            <div class="input">
                <input type="password" id="new-password" name="new-password" placeholder="Enter your new password" required>
            </div>
            <div class="input">
                <input type="password" id="confirm-password" name="confirm-password" placeholder="Confirm your password" required>
            </div>
            <c:if test="${not empty msg}">
                <div class="alert-success">
                    <c:out value="${msg}"/>
                </div>
            </c:if>
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger">
                    <c:out value="${errorMsg}"/>
                </div>
            </c:if>
            <div class="buttonSignUp">
                <button type="submit" class="btn btn-secondary">Save</button>
            </div>

        </form>
    </div>
</div>
</body>
</html>