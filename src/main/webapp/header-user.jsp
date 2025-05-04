<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="${pageContext.request.contextPath}/asset/js/header.js" defer></script>
</head>
<body>
<header>
  <!-- Nút Quay lại với SVG -->
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

  <img class="logo" src="${pageContext.request.contextPath}/asset/png/loginPage/logo.png" alt="Logo" onclick="window.location.href='home.jsp'">
  <div class="navbarContainer">
    <div class="navbarContent"><a href="courses">Khóa Học</a></div>
    <div class="navbarContent"><a href="/about.html">Giảng Viên</a></div>
    <div class="navbarContent"><a href="quizlet">Cộng Đồng</a></div>
    <div class="navbarContent"><a href="library">Đề thi</a></div>
    <div class="navbarContent"><a href="entranceTest">Test Level</a></div>
    <div class="navbarContent"><a href="listHonour">Danh Hiệu</a></div>
    <div class="navbarContent"><a href="addFlashCard">Tài Liệu</a></div>
    <div class="navbarContent"><a href="dictionary">Thi Thử</a></div>
    <div class="navbarContent"><a href="about-us.jsp">Về Chúng Tôi</a></div>
    <div class="navbarContent"><a href="${pageContext.request.contextPath}/chat/<%= session.getAttribute("userID") %>">Phòng Chat</a></div>

    <div class="user-info">
      <div class="user-info-text">
        <span class="username <%= request.getAttribute("equippedGradientStart") != null ? "gradient" : "" %>" 
              style="<%= request.getAttribute("equippedGradientStart") != null ? String.format("background-image: linear-gradient(45deg, %s, %s)", request.getAttribute("equippedGradientStart"), request.getAttribute("equippedGradientEnd")) : "" %>">
          Xin chào, <%= session.getAttribute("username") %>!
        </span>
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
      <img class="user-avatar" src="${pageContext.request.contextPath}/<%= avatar != null ? avatar : "asset/png/avatar/monkey.jpg" %>"
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
        menu.style.left = (btnRect.left + 50) + 'px';
      } else {
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

    const isClickInsideMenu = menu.contains(event.target);
    const isClickOnMenuButton = menuBtn.contains(event.target);
    const isClickInsidePopup = popup.contains(event.target);
    const isClickOnAvatar = avatar.contains(event.target);

    if (!isClickInsidePopup && !isClickOnAvatar) {
      popup.style.display = 'none';
    }

    if (!isClickInsideMenu && !isClickOnMenuButton) {
      menu.style.display = 'none';
      menuBtn.classList.remove('active');
    }
  });

  // Mobile menu button drag functionality
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

    function dragStart(e) {
      if (e.type === "touchstart") {
        initialX = e.touches[0].clientX - xOffset;
        initialY = e.touches[0].clientY - yOffset;
      } else {
        initialX = e.clientX - xOffset;
        initialY = e.clientY - yOffset;
      }

      if (e.target === mobileMenuBtn) {
        isDragging = true;
        mobileMenuBtn.classList.add('dragging');
      }
    }

    function drag(e) {
      if (isDragging) {
        e.preventDefault();

        if (e.type === "touchmove") {
          currentX = e.touches[0].clientX - initialX;
          currentY = e.touches[0].clientY - initialY;
        } else {
          currentX = e.clientX - initialX;
          currentY = e.clientY - initialY;
        }

        xOffset = currentX;
        yOffset = currentY;

        mobileMenuBtn.style.left = currentX + 'px';
        mobileMenuBtn.style.top = currentY + 'px';

        if (mobileMenu.style.display === 'block') {
          const btnRect = mobileMenuBtn.getBoundingClientRect();
          if (btnRect.left < 200) {
            mobileMenu.style.left = (btnRect.left + 50) + 'px';
          } else {
            mobileMenu.style.left = (btnRect.left - 180) + 'px';
          }
          mobileMenu.style.top = (btnRect.top + 25) + 'px';
        }
      }
    }

    function dragEnd() {
      initialX = currentX;
      initialY = currentY;
      isDragging = false;
      mobileMenuBtn.classList.remove('dragging');
    }

    mobileMenuBtn.addEventListener('touchstart', dragStart, false);
    mobileMenuBtn.addEventListener('mousedown', dragStart, false);
    document.addEventListener('touchmove', drag, false);
    document.addEventListener('mousemove', drag, false);
    document.addEventListener('touchend', dragEnd, false);
    document.addEventListener('mouseup', dragEnd, false);
  });
</script>
</body>
</html>
