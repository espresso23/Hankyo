<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
  <link rel="stylesheet" href="asset/css/quizlet.css">
  <link rel="icon" href="asset/png/icon/logo.jpg">
  <title>Select Topic</title>
  <link rel="icon" href="asset/png/icon/logo.jpg">
  <style>
    body {
      font-family: 'Inter', sans-serif;
      background-image: url("asset/png/background/background.png");
      background-size: auto;
      margin: 0;
      min-height: 100vh;
    }

  </style>
</head>

<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="container">
  <div class="custom-topics-section">
    <div class="custom-topics-content">
      <h1 class="custom-topics-title">Chọn các Topic tự tạo của bản thân:</h1>
      <div class="favorite-section">
        <a href="customFlashCard?topic=favorite" class="favorite-btn">Favorite Flashcard</a>
        <a href="customFlashCard?topic=custom" class="custom-btn">Custom FlashCard</a>
      </div>
    </div>
    <img src="asset/png/icon/logo.jpg" alt="Custom Topics Illustration">
  </div>
  <img src="asset/images/separator.jpg" alt="Separator Illustration" class="separator-image">
  <h1 class="system-topics">Bạn có thể chọn theo Topic có sẵn:</h1>
  <div class="system-topics-scroll" id="systemTopicsScroll" aria-label="System topic selection">
    <c:forEach var="item" items="${listTopic}">
      <div class="topic-box">
        <a href="flashCard?topic=${item}&flashCardID=1">${item}</a>
      </div>
    </c:forEach>
  </div>
</div>

<script>
  const systemScrollContainer = document.getElementById('systemTopicsScroll');
  const scrollAmount = 176;
  let systemAutoScroll;
  let isSystemAutoScrolling = true;

  function startSystemAutoScroll() {
    systemAutoScroll = setInterval(() => {
      systemScrollContainer.scrollLeft += scrollAmount;
      if (systemScrollContainer.scrollLeft + systemScrollContainer.clientWidth >= systemScrollContainer.scrollWidth) {
        systemScrollContainer.scrollLeft = 0;
      }
    }, 3000);
  }

  function toggleSystemAutoScroll() {
    if (isSystemAutoScrolling) {
      clearInterval(systemAutoScroll);
      isSystemAutoScrolling = false;
    } else {
      startSystemAutoScroll();
      isSystemAutoScrolling = true;
    }
  }

  systemScrollContainer.addEventListener('mouseenter', () => {
    clearInterval(systemAutoScroll);
  });

  systemScrollContainer.addEventListener('mouseleave', () => {
    if (isSystemAutoScrolling) {
      startSystemAutoScroll();
    }
  });

  systemScrollContainer.addEventListener('click', toggleSystemAutoScroll);

  startSystemAutoScroll();
</script>
</body>
<jsp:include page="footer.jsp"></jsp:include>
</html>