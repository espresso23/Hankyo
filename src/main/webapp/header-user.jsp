<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="asset/css/header.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <script src="asset/js/header.js" defer></script>
  <meta charset="UTF-8">
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

    /* Mobile Menu Button Styles */
    .mobile-menu-btn {
      position: fixed;
      top: 12%;
      right: 5%;
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
      user-select: none;
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

    /* Vertical Menu Styles */
    .mobile-vertical-menu {
      position: fixed;
      top: 15%;
      right: 10%;
      background: white;
      border-radius: 12px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
      padding: 15px 0;
      z-index: 999;
      width: 220px;
      display: none;
    }

    .mobile-vertical-menu.show {
      display: block;
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

  .navbarContainer {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }

  .user-info {
    display: flex;
    align-items: center;
    margin-right: 10px;
  }

  .user-info-text {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }

  .username {
    font-weight: bold;
    color: #333;
  }

  .honour-name {
    font-size: 0.8em;
    color: #666;
    margin-top: 2px;
    padding: 2px 6px;
    border-radius: 4px;
    text-shadow: 0 0 5px rgba(255, 255, 255, 0.8), 0 0 10px rgba(255, 255, 255, 0.5);
    background: rgba(255, 255, 255, 0.1);
  }

  .honour-image {
    width: 60px !important;
    height: 60px !important;
    margin: 0 5px !important;
    vertical-align: middle !important;
    object-fit: contain !important;
  }

  .avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    cursor: pointer;
  }

  /* Thêm style cho chat bot */
  .chat-container {
    overflow-y: auto;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 10px;
    margin-bottom: 20px;
    height: auto;
  }
  .message {
    margin-bottom: 15px;
    padding: 10px 15px;
    border-radius: 15px;
    max-width: 80%;
  }
  .user-message {
    background-color: #007bff;
    color: white;
    margin-left: auto;
  }
  .bot-message {
    background-color: #e9ecef;
    color: black;
    margin-right: auto;
  }
  .typing-indicator {
    display: none;
    padding: 10px 15px;
    background-color: #e9ecef;
    border-radius: 15px;
    margin-right: auto;
    max-width: 80%;
  }
  .typing-indicator span {
    height: 8px;
    width: 8px;
    background-color: #6c757d;
    border-radius: 50%;
    display: inline-block;
    margin-right: 3px;
    animation: typing 1s infinite;
  }
  .typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
  .typing-indicator span:nth-child(3) { animation-delay: 0.4s; }
  @keyframes typing {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-5px); }
  }
  .modal-dialog {
    max-width: 800px;
  }

  /* Thêm style cho nội dung bot-message */
  .bot-message strong { color: #007bff; }
  .bot-message ul { margin: 8px 0 8px 20px; padding-left: 18px; }
  .bot-message li { margin-bottom: 4px; }
  .bot-message p { margin-bottom: 8px; }

  /* Nút nổi bật mở chat bot */
  .floating-chat-btn {
    position: fixed;
    bottom: 32px;
    right: 32px;
    width: 64px;
    height: 64px;
    border-radius: 50%;
    background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
    box-shadow: 0 4px 16px rgba(108,180,255,0.18);
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 2000;
    cursor: pointer;
    transition: box-shadow 0.2s, transform 0.2s;
  }
  .floating-chat-btn:hover {
    box-shadow: 0 8px 24px rgba(255,143,163,0.25);
    transform: translateY(-3px) scale(1.07);
  }
  .floating-chat-btn img {
    width: 38px;
    height: 38px;
    border-radius: 50%;
    background: #fff;
    border: 2px solid #6cb4ff;
    box-shadow: 0 2px 8px rgba(108,180,255,0.10);
  }
  /* Widget chat bot nhỏ gọn & hiệu ứng mượt */
  #chatBotWidget {
    overflow: hidden;
    position: fixed;
    bottom: 100px;
    right: 30px;
    width: 400px;
    max-width: 188vw;
    height: 480px;
    max-height: 70vh;
    background: #f8f9fa;
    border-radius: 20px;
    box-shadow: 0 8px 32px rgba(108, 180, 255, 0.10);
    z-index: 3000;
    display: flex;
    flex-direction: column;
    opacity: 0;
    transform: scale(0.8) translateY(40px);
    pointer-events: none;
    transition: opacity 0.25s cubic-bezier(.4,0,.2,1), transform 0.25s cubic-bezier(.4,0,.2,1);
  }
  #chatBotWidget.open {
    opacity: 1;
    transform: scale(1) translateY(0);
    pointer-events: auto;
  }
  @media (max-width: 600px) {
    #chatBotWidget {
      width: 95vw;
      height: 60vh;
      right: 2vw;
      bottom: 2vh;
      min-width: 0;
      max-width: 100vw;
      max-height: 90vh;
    }
  }
  #chatBotWidget .chatbot-header {
    flex-shrink: 0;
    position: sticky;
    top: 0;
    z-index: 2;
    background: linear-gradient(90deg, #a8edea 0%, #fed6e3 100%);
    border-radius: 24px 24px 0 0;
    padding: 18px 18px 8px 18px;
    display: flex;
    align-items: center;
    gap: 12px;
    max-height: 25vh;
  }
  #chatBotWidget .close-btn {
    margin-left: auto;
    background: none;
    border: none;
    font-size: 1.3rem;
    color: #888;
    cursor: pointer;
  }
  #chatBotWidget .modal-body {
    flex: 1 1 auto;
    display: flex;
    flex-direction: column;
    padding: 0 18px 18px 18px;
    overflow: hidden;
    height: 100%;
    max-height: 100%;
  }
  .chat-widget-body {
    flex: 1 1 auto;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    height: 100%;
    max-height: 100%;
  }
  .chat-container {
    flex: 1 1 auto;
    overflow-y: auto;
    min-height: 0;
    background-color: #fff;
    border-radius: 16px;
    margin-bottom: 0;
    box-shadow: 0 2px 8px rgba(108,180,255,0.06);
    padding: 18px;
  }
  #chatForm {
    flex-shrink: 0;
    margin-top: 8px;
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
    <div class="navbarContent"><a href="blog">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="exam">Đề thi</a></div>
    <div class="navbarContent"><a href="listHonour">Danh Hiệu</a></div>
    <div class="navbarContent"><a href="addFlashCard">Tài Liệu</a></div>
    <div class="navbarContent"><a href="dictionary">Thi Thử</a></div>
    <div class="navbarContent"><a href="/about-us.html">Về Chúng Tôi</a></div>
    <div class="navbarContent"><a href="${pageContext.request.contextPath}/chat/<%= session.getAttribute("userID") %>">Phòng Chat</a></div>
    <!-- Thêm nút chat bot -->
    <div class="navbarContent">
      <button class="floating-chat-btn" onclick="toggleChatWidget()" title="Chat với AI">
        <img src="asset/png/icon/chat-logo.png" alt="Chat Bot">
      </button>
    </div>
    <!-- Thêm icon giỏ hàng vào đây -->
    <div class="navbarContent">
      <a class="nav-link position-relative" href="cart">
        <i class="fas fa-shopping-cart"></i>
        <span class="cart-badge position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              style="<%=(request.getAttribute("cartItemCount") != null && (Integer)request.getAttribute("cartItemCount") > 0) ? "display: block;" : "display: none;"%>">
          <%= request.getAttribute("cartItemCount") != null ? request.getAttribute("cartItemCount") : 0 %>
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

    <div class="user-info">
      <div class="user-info-text">
        <span class="username"
        <% if (request.getAttribute("equippedGradientStart") != null && request.getAttribute("equippedGradientEnd") != null) { %>
            style="color: transparent; background: linear-gradient(45deg, <%= request.getAttribute("equippedGradientStart") %>, <%= request.getAttribute("equippedGradientEnd") %>); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent;"
        <% } %>
        >Xin chào, <%= session.getAttribute("username") %>!</span>
        <% if (request.getAttribute("equippedHonourName") != null && request.getAttribute("equippedHonourImage") != null) { %>
        <span class="honour-name"><%= request.getAttribute("equippedHonourName") %></span>
        <% } %>
      </div>
      <% if (request.getAttribute("equippedHonourName") != null && request.getAttribute("equippedHonourImage") != null) { %>
      <img class="honour-image" src="${pageContext.request.contextPath}/<%= request.getAttribute("equippedHonourImage") %>" alt="Honour Image">
      <% } %>
      <%
        String avatar = (String) session.getAttribute("avatar");
      %>
      <img class="avatar" src="${pageContext.request.contextPath}/<%= avatar != null ? avatar : "asset/png/avatar/monkey.jpg" %>"
           onclick="togglePopup()"
           onerror="this.src='${pageContext.request.contextPath}/asset/png/avatar/monkey.jpg'">
    </div>
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
<button class="mobile-menu-btn" id="mobileMenuButton">
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

<!-- Widget chat bot -->
<div id="chatBotWidget">
  <div class="chatbot-header">
    <img src="asset/png/icon/chat-logo.png" class="chatbot-avatar" alt="Chat Bot" style="width: 20%; ">
    <div>
      <div class="chatbot-greeting">Hi, <%= session.getAttribute("username") != null ? session.getAttribute("username") : "bạn" %>!</div>
      <div class="chatbot-sub">Tôi có thể giúp gì cho bạn?</div>
    </div>
    <button class="close-btn" onclick="toggleChatWidget()">&times;</button>
  </div>
  <div class="modal-body">
    <div class="chat-widget-body">
      <div class="chat-container" id="chatContainer">
        <div class="message bot-message">
          Xin chào! Tôi là trợ lý AI của Hankyo. Tôi có thể giúp gì cho bạn?
        </div>
        <div class="typing-indicator" id="typingIndicator">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>
      <form id="chatForm" class="mt-3">
        <div class="input-group">
          <input type="text" id="userInput" class="form-control" 
                 placeholder="Nhập câu hỏi của bạn..." required>
          <button type="submit" class="btn">
            <i class="fas fa-paper-plane"></i> Gửi
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  // Toggle Popup Container
  function togglePopup() {
    const popup = document.getElementById('popupContainer');
    popup.style.display = popup.style.display === 'block' ? 'none' : 'block';
  }

  // Toggle mobile menu
  function toggleMobileMenu() {
    const menu = document.getElementById('mobileVerticalMenu');
    const btn = document.getElementById('mobileMenuButton');
    const btnRect = btn.getBoundingClientRect();
    const windowWidth = window.innerWidth;

    if (menu.style.display === 'block') {
      menu.style.animation = 'fadeIn 0.3s ease-out reverse';
      setTimeout(() => {
        menu.style.display = 'none';
      }, 250);
      btn.classList.remove('active');
    } else {
      menu.style.display = 'block';
      menu.style.animation = 'fadeIn 0.3s ease-out';

      // Kiểm tra vị trí của button
      if (btnRect.left < 200) {
        // Nếu button gần rìa trái, hiển thị menu bên phải
        menu.style.left = (btnRect.left + 50) + 'px';
      } else {
        // Nếu không, hiển thị menu bên trái
        menu.style.left = (btnRect.left - 180) + 'px';
      }
      menu.style.top = (btnRect.top + 25) + 'px';

      btn.classList.add('active');
    }
  }

  // Close popup and menu when clicking outside
  document.addEventListener('click', function(event) {
    const popup = document.getElementById('popupContainer');
    const menu = document.getElementById('mobileVerticalMenu');
    const avatar = document.querySelector('header img[onclick="togglePopup()"]');
    const menuBtn = document.getElementById('mobileMenuButton');

    // Kiểm tra xem click có phải từ menu hoặc nút menu không
    const isClickInsideMenu = menu.contains(event.target);
    const isClickOnMenuButton = menuBtn.contains(event.target);
    const isClickInsidePopup = popup.contains(event.target);
    const isClickOnAvatar = avatar.contains(event.target);

    // Chỉ đóng popup khi click bên ngoài popup và avatar
    if (!isClickInsidePopup && !isClickOnAvatar) {
      popup.style.display = 'none';
    }

    // Chỉ đóng menu khi click bên ngoài menu và nút menu
    if (!isClickInsideMenu && !isClickOnMenuButton) {
      menu.style.display = 'none';
      menuBtn.classList.remove('active');
    }
  });

  // Thêm code để xử lý kéo thả
  document.addEventListener('DOMContentLoaded', function() {
    const mobileMenuBtn = document.getElementById('mobileMenuButton');
    const mobileMenu = document.getElementById('mobileVerticalMenu');
    let isDragging = false;
    let currentX = 0;
    let currentY = 0;
    let initialX = 0;
    let initialY = 0;
    let xOffset = 0;
    let yOffset = 0;

    mobileMenuBtn.addEventListener('mousedown', dragStart);
    document.addEventListener('mousemove', drag);
    document.addEventListener('mouseup', dragEnd);

    function dragStart(e) {
      initialX = e.clientX - xOffset;
      initialY = e.clientY - yOffset;

      if (e.target === mobileMenuBtn) {
        isDragging = true;
        mobileMenuBtn.classList.add('dragging');
      }
    }

    function drag(e) {
      if (isDragging) {
        e.preventDefault();

        currentX = e.clientX - initialX;
        currentY = e.clientY - initialY;

        xOffset = currentX;
        yOffset = currentY;

        mobileMenuBtn.style.left = currentX + 'px';
        mobileMenuBtn.style.top = currentY + 'px';

        if (mobileMenu.style.display === 'block') {
          const btnRect = mobileMenuBtn.getBoundingClientRect();
          const windowWidth = window.innerWidth;

          // Kiểm tra vị trí của button
          if (btnRect.left < 200) {
            // Nếu button gần rìa trái, hiển thị menu bên phải
            mobileMenu.style.left = (btnRect.left + 50) + 'px';
          } else {
            // Nếu không, hiển thị menu bên trái
            mobileMenu.style.left = (btnRect.left - 180) + 'px';
          }
          mobileMenu.style.top = (btnRect.top + 25) + 'px';
        }
      }
    }

    function dragEnd(e) {
      initialX = currentX;
      initialY = currentY;
      isDragging = false;
      mobileMenuBtn.classList.remove('dragging');
    }
  });

  // Thêm xử lý cho touch events
  document.addEventListener('DOMContentLoaded', function() {
    const mobileMenuBtn = document.getElementById('mobileMenuButton');
    const mobileMenu = document.getElementById('mobileVerticalMenu');
    let isDragging = false;
    let currentX = 0;
    let currentY = 0;
    let initialX = 0;
    let initialY = 0;
    let xOffset = 0;
    let yOffset = 0;

    mobileMenuBtn.addEventListener('touchstart', dragStart);
    document.addEventListener('touchmove', drag);
    document.addEventListener('touchend', dragEnd);

    function dragStart(e) {
      initialX = e.touches[0].clientX - xOffset;
      initialY = e.touches[0].clientY - yOffset;

      if (e.target === mobileMenuBtn) {
        isDragging = true;
        mobileMenuBtn.classList.add('dragging');
      }
    }

    function drag(e) {
      if (isDragging) {
        e.preventDefault();

        currentX = e.touches[0].clientX - initialX;
        currentY = e.touches[0].clientY - initialY;

        xOffset = currentX;
        yOffset = currentY;

        mobileMenuBtn.style.left = currentX + 'px';
        mobileMenuBtn.style.top = currentY + 'px';

        if (mobileMenu.style.display === 'block') {
          const btnRect = mobileMenuBtn.getBoundingClientRect();
          const windowWidth = window.innerWidth;

          // Kiểm tra vị trí của button
          if (btnRect.left < 200) {
            // Nếu button gần rìa trái, hiển thị menu bên phải
            mobileMenu.style.left = (btnRect.left + 50) + 'px';
          } else {
            // Nếu không, hiển thị menu bên trái
            mobileMenu.style.left = (btnRect.left - 180) + 'px';
          }
          mobileMenu.style.top = (btnRect.top + 25) + 'px';
        }
      }
    }

    function dragEnd(e) {
      initialX = currentX;
      initialY = currentY;
      isDragging = false;
      mobileMenuBtn.classList.remove('dragging');
    }
  });

  // Thêm hàm mở chat bot (chỉ mở khi click nút AI Chat)
  function toggleChatWidget() {
    const widget = document.getElementById('chatBotWidget');
    widget.classList.toggle('open');
  }

  // Lấy contextPath từ JSP để fetch đúng đường dẫn
  var contextPath = '<%= request.getContextPath() %>';

  // Thêm xử lý form chat
  document.getElementById('chatForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const userInput = document.getElementById('userInput');
    const message = userInput.value.trim();
    
    if (message) {
      // Hiển thị tin nhắn của người dùng
      appendMessage(message, 'user');
      userInput.value = '';

      // Hiển thị indicator đang gõ
      showTypingIndicator();

      try {
        // Gọi API Gemini bằng POST
        const response = await fetch(contextPath + '/gemini/chat', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: 'message=' + encodeURIComponent(message)
        });

        const data = await response.json();

        // Ẩn indicator đang gõ
        hideTypingIndicator();

        // Hiển thị phản hồi từ bot hoặc lỗi
        if (data.response) {
          appendMessage(data.response, 'bot');
        } else if (data.error) {
          appendMessage('Lỗi: ' + data.error, 'bot');
        } else {
          appendMessage('Không nhận được phản hồi từ AI.', 'bot');
        }

      } catch (error) {
        hideTypingIndicator();
        appendMessage('Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau.', 'bot');
        console.error('Error:', error);
      }
    }
  });

  // Format markdown đơn giản sang HTML cho bot
  function formatAIMessage(text, sender) {
    if (sender !== 'bot') return text;
    let html = text;
    // In đậm **text**
    html = html.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');
    // Danh sách bullet: * đầu dòng
    html = html.replace(/(^|<br>|\n)\* (.+?)(?=<br>|$)/g, '$1<li>$2</li>');
    // Nếu có <li> thì bọc bằng <ul>
    if (html.includes('<li>')) {
      html = html.replace(/((<li>.*?<\/li>\s*)+)/g, '<ul>$1</ul>');
    }
    // Xuống dòng kép thành đoạn
    html = html.replace(/(\r?\n){2,}/g, '</p><p>');
    // Xuống dòng đơn thành <br>
    html = html.replace(/(\r?\n)/g, '<br>');
    // Bọc toàn bộ bằng <p>
    html = '<p>' + html + '</p>';
    return html;
  }

  function scrollChatToBottom() {
    const chatContainer = document.getElementById('chatContainer');
    if (chatContainer) chatContainer.scrollTop = chatContainer.scrollHeight;
  }

  function appendMessage(text, sender) {
    const chatContainer = document.getElementById('chatContainer');
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message ' + sender + '-message';
    messageDiv.innerHTML = formatAIMessage(text, sender);
    const typingDiv = document.getElementById('typingIndicator');
    if (typingDiv && typingDiv.style.display !== 'none') {
      chatContainer.insertBefore(messageDiv, typingDiv);
    } else {
      chatContainer.appendChild(messageDiv);
    }
    scrollChatToBottom();
    const userInput = document.getElementById('userInput');
    userInput.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    userInput.focus();
  }

  function showTypingIndicator() {
    const chatContainer = document.getElementById('chatContainer');
    const typingDiv = document.getElementById('typingIndicator');
    chatContainer.appendChild(typingDiv); // Đảm bảo luôn ở cuối
    typingDiv.style.display = 'block';
    scrollChatToBottom();
  }

  function hideTypingIndicator() {
    document.getElementById('typingIndicator').style.display = 'none';
    scrollChatToBottom();
    const userInput = document.getElementById('userInput');
    userInput.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    userInput.focus();
  }
</script>
</body>
</html>
