<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Custom Quizlet Flashcards</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/selectTopic.css">
  <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container" data-topic="${topic}">
  <div class="containerSmall">
    <h1>${type eq 'public' ? 'Public' : 'Custom'} Flashcards - ${topic}</h1>
    <p class="debug">Type: ${type}</p>
    <p class="debug">FlashCards size: <c:out value="${flashCards != null ? flashCards.size() : 'null'}" /></p>

    <!-- Play Memory Game button -->
    <c:if test="${not empty flashCards && flashCards.size() >= 10}">
      <form action="memory-game" method="GET" style="margin-bottom: 18px;">
        <input type="hidden" name="topic" value="${topic}">
        <input type="hidden" name="type" value="${type}">
        <button type="submit" class="play-game-btn">Play Memory Game</button>
      </form>
    </c:if>
    <c:if test="${empty flashCards || flashCards.size() < 10}">
      <button class="play-game-btn" disabled>Need at least 10 cards to play</button>
    </c:if>

    <div class="tab-container">
      <div class="tab-buttons">
        <button class="tab-button active" data-tab="flashcard">Flashcard</button>
        <c:if test="${type ne 'public'}">
          <button class="tab-button" data-tab="edit">Edit</button>
        </c:if>
      </div>
      
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
              <div class="input-group">
                <label for="word">Từ vựng:</label>
                <input type="text" id="word" class="add-input" placeholder="Nhập từ" />
              </div>
              <div class="input-group">
                <label for="mean">Nghĩa:</label>
                <input type="text" id="mean" class="add-input" placeholder="Nhập nghĩa" />
              </div>
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
                <td class="word-cell">
                  <c:out value="${flashcard.word}" />
                </td>
                <td class="mean-cell"><c:out value="${flashcard.mean}" /></td>
                <td class="action-cell">
                  <c:if test="${flashcard.canEdit}">
                    <button class="edit-btn" data-word="${flashcard.word}" data-mean="${flashcard.mean}">Edit</button>
                    <button class="delete-btn" data-word="${flashcard.word}" data-mean="${flashcard.mean}">X</button>
                  </c:if>
                  <c:if test="${!flashcard.canEdit}">
                    <span class="creator-info">Created by: ${learnerNames[flashcard.learnerID]}</span>
                  </c:if>
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
  window.flashCardsJson = JSON.parse('${flashCardsJson != null ? flashCardsJson : "[]"}');
</script>
<script src="${pageContext.request.contextPath}/asset/js/customQuizlet.js"></script>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>