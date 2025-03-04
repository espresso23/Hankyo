<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 2/25/2025
  Time: 6:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Email Verification</title>
</head>
<body>
    <div class="background-verify">
        <div class="row justify-content-center">
            <div class="verify-form">
                <div class="welcomeline">
                    <h2>ENTER THE CODE FROM YOUR EMAIL</h2>
                    <p>Enter the code Sent to ${sessionScope['gmail']}.</p>
                </div>
                <!-- Form to submit OTP for verification -->
                <form class="form" method="post" action="${pageContext.request.contextPath}/Verify">
                    <label for="code" style="font-family: Pixelfred">OTP Code:</label>
                    <input type="text" id="code" name="otp-code">
                    <div style="margin-top: 10px;" class="d-flex">
                        <!-- Resend OTP -->
                        <button class="resend btn btn-secondary" type="submit" name="action" value="resend" style="font-family: Pixelfred">Send Email Again</button>

                        <!-- Validate OTP -->
                        <button type="submit" name="action" value="validate" class="btn btn-secondary continueButton"></button>
                    </div>
                </form>
            </div>
        </div>
    </div>

</body>
</html>