<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 3/5/2025
  Time: 9:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="asset/css/header.css">
  <style>
    .button {
      display: block;
      position: relative;
      width: 56px;
      height: 56px;
      margin: 0;
      overflow: hidden;
      outline: none;
      background-color: transparent;
      cursor: pointer;
      border: 0;
    }

    .button:before,
    .button:after {
      content: "";
      position: absolute;
      border-radius: 50%;
      inset: 7px;
    }

    .button:before {
      border: 4px solid #96daf0 ;
      transition: opacity 0.4s cubic-bezier(0.77, 0, 0.175, 1) 80ms,
      transform 0.5s cubic-bezier(0.455, 0.03, 0.515, 0.955) 80ms;
    }

    .button:after {
      border: 4px solid  #F2EB80 ;
      transform: scale(1.3);
      transition: opacity 0.4s cubic-bezier(0.165, 0.84, 0.44, 1),
      transform 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
      opacity: 0;
    }

    .button:hover:before,
    .button:focus:before {
      opacity: 0;
      transform: scale(0.7);
      transition: opacity 0.4s cubic-bezier(0.165, 0.84, 0.44, 1),
      transform 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }

    .button:hover:after,
    .button:focus:after {
      opacity: 1;
      transform: scale(1);
      transition: opacity 0.4s cubic-bezier(0.77, 0, 0.175, 1) 80ms,
      transform 0.5s cubic-bezier(0.455, 0.03, 0.515, 0.955) 80ms;
    }

    .button-box {
      display: flex;
      position: absolute;
      top: 0;
      left: 0;
    }

    .button-elem {
      display: block;
      width: 20px;
      height: 20px;
      margin: 17px 18px 0 18px;
      transform: rotate(180deg);
      fill: #8AACFF;
    }

    .button:hover .button-box,
    .button:focus .button-box {
      transition: 0.4s;
      transform: translateX(-56px);
    }
    .back-btn .button-box {
      display: flex;
      align-items: center;
    }
    .back-btn .button-elem svg {
      width: 24px;
      height: 24px;
      fill: #333; /* Màu mặc định */
      transition: transform 0.3s ease, fill 0.3s ease;
    }
    .back-btn:hover .button-elem svg {
      transform: translateX(-5px); /* Di chuyển sang trái khi hover */
      fill: #007BFF; /* Đổi màu khi hover */
    }
    .logo {
      grid-column: 2 / 3; /* Đẩy logo sang cột tiếp theo */
      justify-self: start;
      width: 50px;
    }
  </style>
  <style>
    /* Mobile Menu Button Styles - New Class Name */
    .mobile-menu-btn {
      position: fixed;
      top: 15%;
      right: 96%;
      z-index: 1000;
      width: 50px;
      height: 50px;
      border-radius: 50%;
      background: linear-gradient(317deg, #ffc676, #eb8be6);
      border: none;
      cursor: pointer;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      transition: all 0.3s ease;
      outline: none;
    }

    .mobile-menu-btn:hover {
      transform: translateY(-3px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
    }

    .mobile-menu-btn:active {
      transform: translateY(1px);
    }

    .mobile-menu-line {
      width: 24px;
      height: 2px;
      background-color: white;
      margin: 3px 0;
      transition: all 0.3s ease;
      transform-origin: center;
    }

    .mobile-menu-btn.active .mobile-menu-line:nth-child(1) {
      transform: translateY(8px) rotate(45deg);
    }

    .mobile-menu-btn.active .mobile-menu-line:nth-child(2) {
      opacity: 0;
    }

    .mobile-menu-btn.active .mobile-menu-line:nth-child(3) {
      transform: translateY(-8px) rotate(-45deg);
    }

    /* Vertical Menu Styles - Also renamed to avoid conflict */
    .mobile-vertical-menu {
      position: fixed;
      top: 15%;
      right: 80%;
      background: white;
      border-radius: 12px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
      padding: 15px 0;
      z-index: 999;
      display: none;
      width: 220px;
      overflow: hidden;
      transform-origin: top right;
      animation: fadeIn 0.3s ease-out;
    }

    @keyframes fadeIn {
      from {
        opacity: 0;
        transform: scale(0.9) translateY(-10px);
      }
      to {
        opacity: 1;
        transform: scale(1) translateY(0);
      }
    }

    .mobile-menu-item {
      padding: 12px 20px;
      transition: all 0.2s ease;
    }

    .mobile-menu-item:hover {
      background-color: #f8f9fa;
    }

    .mobile-menu-item a {
      color: #333;
      text-decoration: none;
      font-size: 15px;
      font-weight: 500;
      display: flex;
      align-items: center;
    }

    .mobile-menu-item a:before {
      content: "";
      display: inline-block;
      width: 6px;
      height: 6px;
      background: linear-gradient(135deg, #6e8efb, #a777e3);
      border-radius: 50%;
      margin-right: 12px;
      transition: all 0.2s ease;
    }

    .mobile-menu-item:hover a:before {
      transform: scale(1.5);
    }
  </style>

</head>
<body>
<header>
  <!-- Nút Quay lại với SVG -->
  <button class="button" onclick="history.back()">
    <div class="button-box">
      <span class="button-elem">
        <svg viewBox="0 0 46 40" xmlns="http://www.w3.org/2000/svg">
          <path
                  d="M46 20.038c0-.7-.3-1.5-.8-2.1l-16-17c-1.1-1-3.2-1.4-4.4-.3-1.2 1.1-1.2 3.3 0 4.4l11.3 11.9H3c-1.7 0-3 1.3-3 3s1.3 3 3 3h33.1l-11.3 11.9c-1 1-1.2 3.3 0 4.4 1.2 1.1 3.3.8 4.4-.3l16-17c.5-.5.8-1.1.8-1.9z"
          ></path>
        </svg>
      </span>
      <span class="button-elem">
        <svg viewBox="0 0 46 40">
          <path
                  d="M46 20.038c0-.7-.3-1.5-.8-2.1l-16-17c-1.1-1-3.2-1.4-4.4-.3-1.2 1.1-1.2 3.3 0 4.4l11.3 11.9H3c-1.7 0-3 1.3-3 3s1.3 3 3 3h33.1l-11.3 11.9c-1 1-1.2 3.3 0 4.4 1.2 1.1 3.3.8 4.4-.3l16-17c.5-.5.8-1.1.8-1.9z"
          ></path>
        </svg>
      </span>
    </div>
  </button>
  <img class="logo" src="${pageContext.request.contextPath}/asset/png/loginPage/logo.png" alt="Logo" onclick="window.location.href='courseHeader.jsp'">
  <div class="navbarContainer">
    <div class="navbarContent"><a href="courses">Khóa Học</a></div>
    <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
    <div class="navbarContent"><a href="quizlet">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="library">Đề thi</a></div>
    <div class="navbarContent"><a href="entranceTest">Test Level</a></div>
    <div class="navbarContent"><a href="/about-us.html">Về Chúng Tôi</a></div>
    <div class="navbarContent"><a href="${pageContext.request.contextPath}/chat/<%= session.getAttribute("userID") %>">Phòng Chat</a></div>
    <!-- Thêm icon giỏ hàng vào đây -->
    <div class="navbarContent">
      <a class="nav-link position-relative" href="cart">
        <i class="fas fa-shopping-cart"></i>
        <span class="cart-badge position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              style="display: ${cartItemCount > 0 ? 'block' : 'none'}">
          ${cartItemCount}
        </span>
      </a>
    </div>


    <span>Xin chào, <%= session.getAttribute("username") %>!</span>
    <%
    String avatar = (String) session.getAttribute("avatar");
    System.out.println("Debug - Avatar from session: " + avatar);
    %>
    <img src="${pageContext.request.contextPath}/<%= avatar != null ? avatar : "asset/png/avatar/monkey.jpg" %>"
         onclick="togglePopup()"
         onerror="this.src='${pageContext.request.contextPath}/asset/png/avatar/monkey.jpg'">
  </div>
</header>

<!-- Popup Container -->
<div class="popupContainer" id="popupContainer">
  <div class="profile">
    <div class="profileContent"><a href="${pageContext.request.contextPath}/update-profile">Tài Khoản</a></div>

    <div class="profileContent">
      <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="logout-btn">Đăng Xuất</button>
      </form>
    </div>
  </div>
</div>


<!-- Menu Button and Vertical Menu -->
<!-- Menu Button and Vertical Menu -->
<button class="mobile-menu-btn" id="mobileMenuButton" onclick="toggleMobileMenu()">
  <span class="mobile-menu-line"></span>
  <span class="mobile-menu-line"></span>
  <span class="mobile-menu-line"></span>
</button>
<div class="mobile-vertical-menu" id="mobileVerticalMenu">
  <div class="mobile-menu-item"><a href="quizlet">Flashcard</a></div>
  <div class="mobile-menu-item"><a href="my-courses">Khóa Học Của Tôi</a></div>
  <div class="mobile-menu-item"><a href="dictionary">Từ điển</a></div>
  <div class="mobile-menu-item"><a href="chat">Chat</a></div>
</div>
<script>
  // Toggle Popup Container
  function togglePopup() {
    const popup = document.getElementById('popupContainer');
    popup.style.display = popup.style.display === 'block' ? 'none' : 'block';
  }

  // // Toggle Vertical Menu
  // function toggleMenu() {
  //   const menu = document.getElementById('verticalMenu');
  //   menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
  // }

  // Close popup and menu when clicking outside
  document.addEventListener('click', function(event) {
    const popup = document.getElementById('popupContainer');
    const menu = document.getElementById('verticalMenu');
    const avatar = document.querySelector('header img[onclick="togglePopup()"]');
    const menuBtn = document.querySelector('.menu-btn');

    if (!popup.contains(event.target) && !avatar.contains(event.target)) {
      popup.style.display = 'none';
    }

  });
</script>
<script>
  // Updated function name to match new class
  function toggleMobileMenu() {
    const menu = document.getElementById('mobileVerticalMenu');
    const btn = document.getElementById('mobileMenuButton');

    if (menu.style.display === 'block') {
      menu.style.animation = 'fadeIn 0.3s ease-out reverse';
      setTimeout(() => {
        menu.style.display = 'none';
      }, 250);
      btn.classList.remove('active');
    } else {
      menu.style.display = 'block';
      menu.style.animation = 'fadeIn 0.3s ease-out';
      btn.classList.add('active');
    }
  }

  // Updated click handler with new class names
  document.addEventListener('click', function(event) {
    const menu = document.getElementById('mobileVerticalMenu');
    const menuBtn = document.getElementById('mobileMenuButton');

    if (!menu.contains(event.target) && !menuBtn.contains(event.target)) {
      menu.style.animation = 'fadeIn 0.3s ease-out reverse';
      setTimeout(() => {
        menu.style.display = 'none';
      }, 250);
      menuBtn.classList.remove('active');
    }
  });
</script>
</body>
</html>
