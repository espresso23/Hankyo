<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="asset/css/header.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <script src="asset/js/header.js" defer></script>
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
      fill: #333;
      transition: transform 0.3s ease, fill 0.3s ease;
    }
    .back-btn:hover .button-elem svg {
      transform: translateX(-5px);
      fill: #007BFF;
    }
    .logo {
      grid-column: 2 / 3;
      justify-self: start;
      width: 50px;
    }

    /* Notification dropdown styles */
    .notification-bell {
      position: relative;
      cursor: pointer;
      margin-right: 20px;
      z-index: 1000;
    }

    .notification-bell i {
      font-size: 24px;
      color: #333;
      transition: color 0.3s ease;
    }

    .notification-bell:hover i {
      color: #007bff;
    }

    .notification-count {
      position: absolute;
      top: -8px;
      right: -8px;
      background-color: #ff4444;
      color: white;
      border-radius: 50%;
      padding: 2px 6px;
      font-size: 12px;
      font-weight: bold;
      min-width: 18px;
      text-align: center;
    }

    .notification-dropdown {
      display: none;
      position: absolute;
      top: 40px;
      right: -10px;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      width: 360px;
      max-height: 600px;
      overflow-y: auto;
      z-index: 1000;
    }

    .notification-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 20px;
      border-bottom: 1px solid #eee;
      position: sticky;
      top: 0;
      background-color: #fff;
      z-index: 1;
    }

    .notification-header span {
      font-weight: 600;
      color: #1c1e21;
      font-size: 24px;
    }

    .mark-all-read {
      background: none;
      border: none;
      color: #1877f2;
      cursor: pointer;
      font-size: 14px;
      font-weight: 500;
      padding: 8px 12px;
      border-radius: 6px;
      transition: background-color 0.2s ease;
    }

    .mark-all-read:hover {
      background-color: #e7f3ff;
    }

    .notification-item {
      padding: 12px 20px;
      display: flex;
      align-items: flex-start;
      gap: 12px;
      border-bottom: 1px solid #eee;
      cursor: pointer;
      transition: background-color 0.2s ease;
    }

    .notification-item:last-child {
      border-bottom: none;
    }

    .notification-item.unread {
      background-color: #e7f3ff;
    }

    .notification-item:hover {
      background-color: #f5f6f7;
    }

    .notification-icon {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      background-color: #e4e6eb;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .notification-icon i {
      font-size: 16px;
      color: #1877f2;
    }

    .notification-content {
      flex-grow: 1;
      font-size: 15px;
      color: #1c1e21;
      line-height: 1.3333;
    }

    .notification-time {
      font-size: 13px;
      color: #65676b;
      margin-top: 4px;
    }

    .empty-notifications {
      text-align: center;
      color: #65676b;
      padding: 32px 16px;
      font-size: 15px;
    }

    .notification-dropdown.show {
      display: block;
      animation: fadeIn 0.2s ease-out;
    }

    @keyframes fadeIn {
      from {
        opacity: 0;
        transform: translateY(-10px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    /* Mobile Menu Styles */
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
  <!-- Back Button -->
  <button class="button" onclick="history.back()">
    <div class="button-box">
      <span class="button-elem">
        <svg viewBox="0 0 46 40" xmlns="http://www.w3.org/2000/svg">
          <path d="M46 20.038c0-.7-.3-1.5-.8-2.1l-16-17c-1.1-1-3.2-1.4-4.4-.3-1.2 1.1-1.2 3.3 0 4.4l11.3 11.9H3c-1.7 0-3 1.3-3 3s1.3 3 3 3h33.1l-11.3 11.9c-1 1-1.2 3.3 0 4.4 1.2 1.1 3.3.8 4.4-.3l16-17c.5-.5.8-1.1.8-1.9z"></path>
        </svg>
      </span>
      <span class="button-elem">
        <svg viewBox="0 0 46 40">
          <path d="M46 20.038c0-.7-.3-1.5-.8-2.1l-16-17c-1.1-1-3.2-1.4-4.4-.3-1.2 1.1-1.2 3.3 0 4.4l11.3 11.9H3c-1.7 0-3 1.3-3 3s1.3 3 3 3h33.1l-11.3 11.9c-1 1-1.2 3.3 0 4.4 1.2 1.1 3.3.8 4.4-.3l16-17c.5-.5.8-1.1.8-1.9z"></path>
        </svg>
      </span>
    </div>
  </button>

  <img class="logo" src="${pageContext.request.contextPath}/asset/png/loginPage/logo.png" alt="Logo" onclick="window.location.href='courseHeader.jsp'">
  <div class="navbarContainer">
    <div class="navbarContent"><a href="courses">Khóa Học</a></div>
    <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
    <div class="navbarContent"><a href="quizlet">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="addFlashCard">Tài Liệu</a></div>
    <div class="navbarContent"><a href="dictionary">Thi Thử</a></div>
    <div class="navbarContent"><a href="/about-us.html">Về Chúng Tôi</a></div>
    <div class="navbarContent"><a href="${pageContext.request.contextPath}/chat/<%= session.getAttribute("userID") %>">Phòng Chat</a></div>

    <!-- Cart Icon -->
    <div class="navbarContent">
      <a class="nav-link position-relative" href="cart">
        <i class="fas fa-shopping-cart"></i>
        <span class="cart-badge position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              style="display: ${cartItemCount > 0 ? 'block' : 'none'}">
          ${cartItemCount}
        </span>
      </a>
    </div>

    <!-- Notification Bell -->
    <div class="notification-bell">
      <i class="fas fa-bell"></i>
      <span id="notificationCount" class="notification-count" style="display: none;">0</span>
      <div class="notification-dropdown">
        <div class="notification-header">
          <span>Thông báo</span>
          <button class="mark-all-read" onclick="markAllAsRead(event)">Đánh dấu đã đọc</button>
        </div>
        <div id="notificationList">
          <!-- Notifications will be loaded here -->
        </div>
      </div>
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

<!-- Mobile Menu -->
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
  // The functions are now in header.js, so we can remove them from here
</script>
</body>
</html>