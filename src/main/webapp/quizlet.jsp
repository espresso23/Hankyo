<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
  <link rel="stylesheet" href="asset/css/quizlet.css">
  <title>Select Topic</title>
  <link rel="icon" href="asset/png/icon/logo.jpg">
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f0f0f0;
    }

  </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
  <h1>Chọn các Topic tự tạo của bản thân: </h1>


  <div class="favorite-section">
    <a href="customFlashCard?topic=favorite" class="favorite-btn">Favorite Flashcard</a>
    <a href="customFlashCard?topic=custom" class="custom-btn">Custom FlashCard</a>
  </div>
  <h1>Bạn có thể chọn theo Topic có sẵn: </h1>

  <div class="topics-scroll" id="topicsScroll">
    <c:forEach var="item" items="${listTopic}">
      <div class="topic-box">
        <a href="flashCard?topic=${item}&flashCardID=1">${item}</a>
      </div>
    </c:forEach>
  </div>
</div>

<script>
  const scrollContainer = document.getElementById('topicsScroll');
  const scrollAmount = 165; // Width of topic-box (150px) + gap (15px)
  let autoScroll;

  // Function to scroll automatically
  function startAutoScroll() {
    autoScroll = setInterval(() => {
      scrollContainer.scrollLeft += scrollAmount;

      // If reached the end, scroll back to start
      if (scrollContainer.scrollLeft + scrollContainer.clientWidth >= scrollContainer.scrollWidth) {
        scrollContainer.scrollLeft = 0;
      }
    }, 3000); // Scrolls every 3 seconds
  }

  // Stop auto-scroll on hover
  scrollContainer.addEventListener('mouseenter', () => {
    clearInterval(autoScroll);
  });

  // Resume auto-scroll when mouse leaves
  scrollContainer.addEventListener('mouseleave', () => {
    startAutoScroll();
  });

  // Start auto-scrolling when page loads
  startAutoScroll();
</script>

</body>
<jsp:include page="footer.jsp"></jsp:include>
</html>