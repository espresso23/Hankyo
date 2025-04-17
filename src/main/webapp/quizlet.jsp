<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/quizlet.css">
  <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
  <title>Quizlet - Select Topic</title>
  <style>
    body {
      font-family: 'Inter', sans-serif;
      background-image: url("${pageContext.request.contextPath}/asset/png/background/background.png");
      background-size: auto;
      margin: 0;
      min-height: 100vh;
    }
    .container {
      max-width: 1200px;
      margin: 0 auto;
      margin-bottom: 70px;
      padding: 32px;
      background-image: linear-gradient(#FFB8E0, #B4EBE6); /* Từ CSS mới */
      border-radius: 12px;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      min-height: 80vh;
      position: relative; /* Để hình ảnh đè lên */
    }
    .overlay-image {
      position: absolute;
      top: -75px; /* Đè nửa trên */
      left: 32px;
      width: 200px; /* Nhỏ hơn 300px cho phù hợp */
      height: 150px;
      object-fit: cover;
      border-radius: 12px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      z-index: 10;
      transition: transform 0.2s ease;
    }
    .overlay-image:hover {
      transform: scale(1.05); /* Hiệu ứng tương tự favorite-btn */
    }
    .tabs {
      display: flex;
      border-bottom: 2px solid #ddd;
      margin-bottom: 20px;
      margin-top: 100px; /* Tránh hình ảnh che */
    }
    .tab {
      flex: 1;
      text-align: center;
      padding: 15px;
      cursor: pointer;
      font-size: 18px;
      color: #333;
      background-color: #f9f9f9;
      transition: background-color 0.3s;
    }
    .tab.active {
      background-color: #2196F3; /* Đồng bộ với custom-btn */
      color: #fff;
      font-weight: bold;
    }
    .tab:hover {
      background-color: #e0e0e0;
    }
    .tab-content {
      display: none;
    }
    .tab-content.active {
      display: block;
    }
    .topics-scroll-container {
      position: relative;
      padding: 0 40px;
    }
    .topics-scroll {
      display: flex;
      overflow-x: auto;
      gap: 15px;
      padding: 16px;
      white-space: nowrap;
      width: 540px; /* 4 topics: 4*120px + 3*15px gap */
      scroll-behavior: smooth;
      background-color: #f8f9fa; /* Từ system-topics-scroll */
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
    .topic-box {
      flex: 0 0 auto;
      width: 120px;
      height: 120px;
      background: linear-gradient(135deg, #fff, #e8f0fe); /* Gradient nhẹ */
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      cursor: pointer;
      transition: transform 0.3s, box-shadow 0.3s;
      padding: 10px;
    }
    .topic-box:hover {
      transform: translateY(-4px); /* Từ CSS mới */
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
      background: linear-gradient(135deg, #fff, #d1e3ff);
    }
    .topic-box a {
      text-decoration: none;
      color: #333;
      font-size: 1rem; /* ≈16px, từ CSS mới */
      font-weight: 500;
      margin-bottom: 5px;
      display: block;
      transition: color 0.3s;
    }
    .topic-box:hover a {
      color: #2196F3; /* Đồng bộ với custom-btn */
    }
    .topic-count {
      font-size: 12px;
      color: #333;
      font-weight: 500; /* Từ flashcard-counter */
      background-color: rgba(255, 255, 255, 0.7);
      padding: 2px 8px;
      border-radius: 10px;
    }
    .topics-scroll::-webkit-scrollbar {
      height: 8px; /* Từ CSS mới */
    }
    .topics-scroll::-webkit-scrollbar-thumb {
      background-color: #888;
      border-radius: 4px;
    }
    .topics-scroll::-webkit-scrollbar-thumb:hover {
      background-color: #555;
    }
    .scroll-btn {
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      background-color: #2196F3; /* Đồng bộ với custom-btn */
      color: #fff;
      border: none;
      border-radius: 50%;
      width: 30px;
      height: 30px;
      cursor: pointer;
      font-size: 16px;
      line-height: 30px;
      text-align: center;
      opacity: 0.8;
      transition: opacity 0.3s, transform 0.2s;
    }
    .scroll-btn:hover {
      opacity: 1;
      transform: translateY(-50%) scale(1.1);
    }
    .scroll-btn.left {
      left: 5px;
    }
    .scroll-btn.right {
      right: 5px;
    }
    .error {
      color: red;
      text-align: center;
      margin: 20px 0;
    }
    .add-flashcard-form {
      background-color: #ffffff;
      padding: 25px;
      border-radius: 12px;
      box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
      width: 100%;
      max-width: 450px;
      margin: 20px auto;
    }
    .add-flashcard-form h2 {
      text-align: center;
      color: #1a1a1a; /* Từ custom-topics-title */
      margin-bottom: 25px;
      font-size: 24px;
      font-weight: 600;
    }
    .add-flashcard-form form {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }
    .add-flashcard-form input[type="text"] {
      padding: 12px;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 16px;
      outline: none;
      transition: border-color 0.3s ease, box-shadow 0.3s ease;
      background-color: #f9f9f9;
    }
    .add-flashcard-form input[type="text"]:focus {
      border-color: #2196F3;
      box-shadow: 0 0 5px rgba(33, 150, 243, 0.3);
    }
    .add-flashcard-form input[type="text"]::placeholder {
      color: #999;
    }
    .add-flashcard-form button {
      padding: 12px;
      background-color: #2196F3;
      color: #ffffff;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 500;
      cursor: pointer;
      transition: background-color 0.3s ease, transform 0.2s ease;
    }
    .add-flashcard-form button:hover {
      background-color: #1976D2;
      transform: translateY(-2px);
    }
    .add-flashcard-form button:active {
      transform: translateY(0);
    }
    .add-flashcard-form .toggle-btn {
      background-color: #4CAF50; /* Đồng bộ với favorite-btn */
      margin-bottom: 15px;
      font-size: 14px;
      padding: 10px;
    }
    .add-flashcard-form .toggle-btn:hover {
      background-color: #45a049;
    }
    .add-flashcard-form .manual-input, .add-flashcard-form .individual-input {
      display: none;
    }
    .add-flashcard-form .manual-input.active, .add-flashcard-form .individual-input.active {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }
    .add-flashcard-form .note {
      font-size: 13px;
      color: #7f8c8d;
      text-align: center;
      margin-top: 15px;
      line-height: 1.4;
    }
    .add-flashcard-form .success, .add-flashcard-form .error-list {
      margin-bottom: 20px;
    }
    .add-flashcard-form .success ul, .add-flashcard-form .error-list ul {
      list-style: none;
      padding: 0;
    }
    .add-flashcard-form .success li {
      color: #4CAF50;
    }
    .add-flashcard-form .error-list li {
      color: red;
    }
    .add-flashcard-form .result-container {
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="container">
  <img class="overlay-image" src="${pageContext.request.contextPath}/asset/png/quizlet/cuteRabbit.png" alt="Quizlet Logo">
  <h1>Chọn Topic Flashcard</h1>
  <div class="tabs">
    <div class="tab active" data-tab="system">System Flashcards</div>
    <div class="tab" data-tab="custom">Custom Flashcards</div>
    <div class="tab" data-tab="favorite">Favorite Flashcards</div>
    <div class="tab" data-tab="add">Add Flashcard</div>
  </div>

  <!-- System Flashcards Tab -->
  <div class="tab-content active" id="system">
    <h2>System Flashcards</h2>
    <div class="topics-scroll-container">
      <div class="topics-scroll">
        <c:forEach var="item" items="${systemTopics}">
          <div class="topic-box" data-topic="${item}" data-type="system">
            <a href="flashCard?topic=${item}&type=system">${item}</a>
            <span class="topic-count">0 từ</span>
          </div>
        </c:forEach>
      </div>
      <button class="scroll-btn left"><</button>
      <button class="scroll-btn right">></button>
    </div>
    <c:if test="${not empty systemError}">
      <p class="error">${systemError}</p>
    </c:if>
  </div>

  <!-- Custom Flashcards Tab -->
  <div class="tab-content" id="custom">
    <h2>Custom Flashcards</h2>
    <div class="topics-scroll-container">
      <div class="topics-scroll">
        <c:forEach var="item" items="${customTopics}">
          <div class="topic-box" data-topic="${item}" data-type="custom">
            <a href="flashCard?topic=${item}&type=custom">${item}</a>
            <span class="topic-count">0 từ</span>
          </div>
        </c:forEach>
      </div>
      <button class="scroll-btn left"><</button>
      <button class="scroll-btn right">></button>
    </div>
    <c:if test="${not empty customError}">
      <p class="error">${customError}</p>
    </c:if>
  </div>

  <!-- Favorite Flashcards Tab -->
  <div class="tab-content" id="favorite">
    <h2>Favorite Flashcards</h2>
    <div class="topics-scroll-container">
      <div class="topics-scroll">
        <c:forEach var="item" items="${favoriteTopics}">
          <div class="topic-box" data-topic="${item}" data-type="favorite">
            <a href="flashCard?topic=${item}&type=favorite">${item}</a>
            <span class="topic-count">0 từ</span>
          </div>
        </c:forEach>
      </div>
      <button class="scroll-btn left"><</button>
      <button class="scroll-btn right">></button>
    </div>
    <c:if test="${not empty favoriteError}">
      <p class="error">${favoriteError}</p>
    </c:if>
  </div>

  <!-- Add Flashcard Tab -->
  <div class="tab-content" id="add">
    <div class="add-flashcard-form">
      <h2>Thêm Flashcard Mới</h2>
      <c:if test="${not empty successMessages}">
        <div class="success">
          <p>Thêm thành công:</p>
          <ul>
            <c:forEach var="msg" items="${successMessages}">
              <li>${msg}</li>
            </c:forEach>
          </ul>
        </div>
      </c:if>
      <c:if test="${not empty errorMessages}">
        <div class="error-list">
          <p>Lỗi:</p>
          <ul>
            <c:forEach var="msg" items="${errorMessages}">
              <li>${msg}</li>
            </c:forEach>
          </ul>
        </div>
      </c:if>
      <button class="toggle-btn" onclick="toggleInputMode()">Chuyển sang nhập từng ô</button>
      <form id="flashcardForm" action="addFlashCard" method="post">
        <div class="manual-input active">
          <input type="text" id="manualTopic" name="topic" placeholder="Nhập Topic" required />
          <input type="text" id="manualFlashCards" name="flashCards"
                 placeholder="Nhập từ:nghĩa (VD: hello:xin chào;good:tốt)" required />
        </div>
        <div class="individual-input">
          <input type="text" id="individualTopic" name="individualTopic" placeholder="Nhập Topic" />
          <input type="text" id="word" name="word" placeholder="Nhập từ" />
          <input type="text" id="mean" name="mean" placeholder="Nhập nghĩa" />
        </div>
        <button type="submit">Thêm Flashcard</button>
      </form>
      <p class="note">Lưu ý: Nhập nhiều flashcard cách nhau bằng dấu ";", mỗi cặp theo cú pháp "từ:nghĩa" (cho chế độ thủ công).</p>
    </div>
  </div>
</div>

<script>
  window.contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/asset/js/quizlet.js"></script>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>