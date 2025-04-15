<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="asset/css/header.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<header>
  <img class="logo" src="asset/png/loginPage/logo.png" alt="Logo">
  <div class="navbarContainer">
    <div class="navbarContent"><a href="/index.html">Khóa Học</a></div>
    <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
    <div class="navbarContent"><a href="/community.html">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="/docs.html">Tài Liệu</a></div>
    <div class="navbarContent"><a href="/test.html">Thi Thử</a></div>
    <div class="navbarContent"><a href="/about-us.html">Về Chúng Tôi</a></div>

    <!-- Thêm icon giỏ hàng vào đây -->
    <div class="navbarContent">
      <%--      <a class="cart-icon position-relative" href="cart">--%>
      <%--        <i class="fas fa-shopping-cart"></i>--%>
      <%--        <c:if test="${cartItemCount > 0}">--%>
      <%--                    <span class="cart-badge position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">--%>
      <%--                        ${cartItemCount}--%>
      <%--                    </span>--%>
      <%--        </c:if>--%>
      <%--      </a>--%>
      <a class="nav-link position-relative" href="cart">
        <i class="fas fa-shopping-cart"></i>
        <span class="cart-badge position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              style="display: ${cartItemCount > 0 ? 'block' : 'none'}">
          ${cartItemCount}
        </span>
      </a>
    </div>

    <span>Xin chào, <%= session.getAttribute("username") %>!</span>
    <img src="asset/png/avatar/monkey.jpg" onclick="profile()">
  </div>
</header>
<div class="popupContainer">
  <div class="profile">
    <div class="profileContent"><a href="/profiel.html">Tài Khoản</a></div>
    <div class="profileContent"><a href="/logout.html">Đăng Xuất</a></div>
  </div>
</div>
</body>
<script src="myscripts.js"></script>
</html>