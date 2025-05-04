<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Email Verification</title>
</head>
<body>
<div class="container-fluid">
    <div class="background-verify">
        <div class="row justify-content-center">
            <div class="verify-form">
                <div class="welcomeline">
                    <h2>ENTER THE CODE FROM YOUR EMAIL</h2>
                    <p>Enter the code Sent to ${sessionScope['gmail']}.</p>
                </div>
                <!-- Form to submit OTP for verification -->
                <form class="form" method="post" action="${pageContext.request.contextPath}/forgot">
                    <label for="code">OTP Code:</label>
                    <input type="text" id="code" name="otp-code" class="input-otp">
                    <div style="margin-top: 10px;" class="d-flex">
                        <!-- Validate OTP -->
                        <button type="submit" name="action" value="verifyresetcode"
                                class="btn btn-secondary continueButton"></button>
                        <!-- Resend OTP -->
                        <button class="resend btn btn-secondary" type="submit" name="action" value="resend">Send Email
                            Again
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
