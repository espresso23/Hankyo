<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Custom Quizlet Flashcards</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/selectTopic.css">
  <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
  <style>
    @font-face {
      font-family: 'Poppins';
      src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('truetype');
    }
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }
    body {
      font-family: 'Poppins', sans-serif;
      background-image: url('${pageContext.request.contextPath}/asset/png/background/background-2.png');
      min-height: 100vh;
      background-position: left center;
    }
  </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container" data-topic="${topic}">
  <div class="containerSmall">
    <h1>Custom Flashcards - ${topic}</h1>
    <p class="debug">Type: custom</p>
    <p class="debug">FlashCards size: <c:out value="${flashCards != null ? flashCards.size() : 'null'}" /></p>

    <div class="tabs">
      <div class="tab active" data-tab="flashcard">Flashcard</div>
      <div class="tab" data-tab="edit">Edit</div>
    </div>

    <div class="tab-content-wrapper">
      <div class="tab-content active" id="flashcard-tab">
        <div class="flashcard-container">
          <c:choose>
            <c:when test="${empty flashCards}">
              <p class="no-data">No custom flashcards available for this topic.</p>
            </c:when>
            <c:otherwise>
              <div class="previousButton">←</div>
              <div class="flashcard">
                <div class="flashcard-inner">
                  <div class="flashcard-front" id="flashcard-front"></div>
                  <div class="flashcard-back" id="flashcard-back"></div>
                </div>
              </div>
              <div class="nextButton">→</div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="tab-content" id="edit-tab">
        <div class="add-container">
          <h2>Thêm Flashcard Mới</h2>
          <button class="toggle-btn" onclick="toggleInputMode()">Chuyển sang nhập từng ô</button>
          <div class="add-form" id="flashcardForm">
            <div class="manual-input active">
              <input type="text" id="manualFlashCards" class="add-input" placeholder="Nhập từ:nghĩa (VD: hello:xin chào;good:tốt)" required />
            </div>
            <div class="individual-input">
              <input type="text" id="word" class="add-input" placeholder="Nhập từ" />
              <input type="text" id="mean" class="add-input" placeholder="Nhập nghĩa" />
            </div>
            <button class="add-btn" onclick="addFlashcard()">Thêm Flashcard</button>
          </div>
          <p class="note">Lưu ý: Nhập nhiều flashcard cách nhau bằng ";", mỗi cặp theo cú pháp "từ:nghĩa" (cho chế độ thủ công).</p>
        </div>
        <div class="wordContainer">
          <table class="wordTable">
            <thead>
            <tr>
              <th>Từ vựng</th>
              <th>Nghĩa</th>
              <th class="action-header">Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${flashCards}" var="flashcard">
              <tr data-cfcid="${flashcard.CFCID}">
                <td class="word-cell"><c:out value="${flashcard.word}" /></td>
                <td class="mean-cell"><c:out value="${flashcard.mean}" /></td>
                <td class="action-cell">
                  <button class="edit-btn" data-word="${flashcard.word}" data-mean="${flashcard.mean}">Edit</button>
                  <button class="delete-btn" data-word="${flashcard.word}" data-mean="${flashcard.mean}">X</button>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  window.contextPath = '${pageContext.request.contextPath}';
  window.flashCardsJson = ${flashCardsJson != null ? flashCardsJson : '[]'};
</script>
<script src="${pageContext.request.contextPath}/asset/js/customQuizlet.js"></script>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>