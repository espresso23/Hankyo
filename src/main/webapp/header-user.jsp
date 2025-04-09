<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 3/5/2025
  Time: 9:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="asset/css/header.css">
  <style>

  </style>
</head>
<body>
<header>
  <img class="logo" src="asset/png/loginPage/logo.png" alt="Logo" onclick="window.location.href='home.jsp'">
  <div class="navbarContainer">
    <div class="navbarContent"><a href="/index.html">Khóa Học</a></div>
    <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
    <div class="navbarContent"><a href="quizlet">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="addFlashCard">Tài Liệu</a></div>
    <div class="navbarContent"><a href="dictionary">Thi Thử</a></div>
    <div class="navbarContent"><a href="/about-us.html">Về Chúng Tôi</a></div>
    <span>Xin chào, <%= session.getAttribute("username") %>!</span>
    <img src="asset/png/avatar/monkey.jpg" onclick="togglePopup()">
  </div>
</header>

<!-- Popup Container -->
<div class="popupContainer" id="popupContainer">
  <div class="profile">
    <div class="profileContent"><a href="/profile.html">Tài Khoản</a></div>
    <div class="profileContent"><a href="/logout.html">Đăng Xuất</a></div>
  </div>
</div>

<!-- Menu Button and Vertical Menu -->
<button class="menu-btn" onclick="toggleMenu()">Menu</button>
<div class="vertical-menu" id="verticalMenu">
  <div class="menu-item"><a href="flashCard">Flashcard</a></div>
  <div class="menu-item"><a href="dictionary">Từ điển</a></div>
  <div class="menu-item"><a href="chat">Chat</a></div>
</div>

<script>
  // Toggle Popup Container
  function togglePopup() {
    const popup = document.getElementById('popupContainer');
    popup.style.display = popup.style.display === 'block' ? 'none' : 'block';
  }

  // Toggle Vertical Menu
  function toggleMenu() {
    const menu = document.getElementById('verticalMenu');
    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
  }

  // Close popup and menu when clicking outside
  document.addEventListener('click', function(event) {
    const popup = document.getElementById('popupContainer');
    const menu = document.getElementById('verticalMenu');
    const avatar = document.querySelector('header img[src="asset/png/avatar/monkey.jpg"]');
    const menuBtn = document.querySelector('.menu-btn');

    if (!popup.contains(event.target) && !avatar.contains(event.target)) {
      popup.style.display = 'none';
    }
    if (!menu.contains(event.target) && !menuBtn.contains(event.target)) {
      menu.style.display = 'none';
    }
  });
</script>
</body>
</html>