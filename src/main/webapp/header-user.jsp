<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 3/5/2025
  Time: 9:20 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
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
    border: 4px solid #96daf0;
    transition: opacity 0.4s cubic-bezier(0.77, 0, 0.175, 1) 80ms,
    transform 0.5s cubic-bezier(0.455, 0.03, 0.515, 0.955) 80ms;
  }

  .button:after {
    border: 4px solid #F2EB80;
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
  <% if (request.getAttribute("equippedGradientStart") != null && request.getAttribute("equippedGradientEnd") != null) { %>
    color: transparent;
    background: linear-gradient(45deg, <%= request.getAttribute("equippedGradientStart") %>, <%= request.getAttribute("equippedGradientEnd") %>);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
  <% } else { %>
    color: #333;
  <% } %>
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
</style>
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
    <div class="navbarContent"><a href="/index.html">Khóa Học</a></div>
    <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
    <div class="navbarContent"><a href="listHonour">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="addFlashCard">Tài Liệu</a></div>
    <div class="navbarContent"><a href="dictionary">Thi Thử</a></div>
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
    <div class="user-info">
      <div class="user-info-text">
        <span class="username">Xin chào, <%= session.getAttribute("username") %>!</span>
        <% if (request.getAttribute("equippedHonourName") != null && request.getAttribute("equippedHonourImage") != null) { %>
        <span class="honour-name"><%= request.getAttribute("equippedHonourName") %></span>
        <% } %>
      </div>
      <% if (request.getAttribute("equippedHonourName") != null && request.getAttribute("equippedHonourImage") != null) { %>
      <img class="honour-image" src="${pageContext.request.contextPath}/<%= request.getAttribute("equippedHonourImage") %>" alt="Honour Image">
      <% } %>
      <%
        String avatar = (String) session.getAttribute("avatar");
        System.out.println("Debug - Avatar from session: " + avatar);
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
<button class="menu-btn" onclick="toggleMenu()">Menu</button>
<div class="vertical-menu" id="verticalMenu">
  <div class="menu-item"><a href="quizlet">Flashcard</a></div>
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
    const avatar = document.querySelector('header img[onclick="togglePopup()"]');
    const menuBtn = document.querySelector('.menu-btn');

    if (!popup.contains(event.target) && !avatar.contains(event.target)) {
      popup.style.display = 'none';
    }
    if (!menu.contains(event.target) && !menuBtn.contains(event.target)) {
      menu.style.display = 'none';
    }
  });

  // Debug request attributes
  console.log("equippedGradientStart: <%= request.getAttribute("equippedGradientStart") %>");
  console.log("equippedGradientEnd: <%= request.getAttribute("equippedGradientEnd") %>");
  console.log("equippedHonourName: <%= request.getAttribute("equippedHonourName") %>");
  console.log("equippedHonourImage: <%= request.getAttribute("equippedHonourImage") %>");
</script>