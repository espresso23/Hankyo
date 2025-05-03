<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 3/5/2025
  Time: 9:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <link rel="stylesheet" href="asset/css/header.css">
    <title>Title</title>
</head>
<body>
<header>
    <img class="logo" src="${pageContext.request.contextPath}/asset/png/loginPage/logo.png" alt="Logo">
    <div class="navbarContainer">
        <div class="navbarContent"><a href="courses">Khóa Học</a></div>
        <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
        <div class="navbarContent"><a href="/community.html">Cộng Đồng</a></div>
        <div class="navbarContent"><a href="/exam">Đề thi</a></div>
        <div class="navbarContent"><a href="/entranceTest.jsp">Test Level</a></div>
        <div class="navbarContent"><a href="/about-us.html">Về Chúng Tôi</a></div>
       <div class="navbarContent"><a href="login" class="loginBtn">Đăng Nhập</a></div>
       <div class="navbarContent"><a href="register" class="registerBtn">Đăng Ký</a></div>
    </div>
</header>

</body>
</html>
