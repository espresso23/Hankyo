<%--
  Created by IntelliJ IDEA.
  User: Le Phuong Uyen
  Date: 4/7/2025
  Time: 12:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${question.title} - Forum</title>
  <link rel="stylesheet" href="css/style.css">
  <!-- Include a rich text editor like TinyMCE if desired -->
  <script src="https://cdn.tiny.cloud/1/your-api-key/tinymce/5/tinymce.min.js" referrerpolicy="origin"></script>
  <script>
    tinymce.init({
      selector: '#answerContent',
      height: 200,
      plugins: 'link code table lists',
      toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist outdent indent | link code'
    });
  </script>
</head>
<body>
<div class="container">
  <div class="question-header">
    <h1>${question.title}</h1>
    <div class="question-meta">
      <span class="author">Posted by: ${question.authorName}</span>
      <span class="date">
                    <fmt:formatDate value="${question.createdAt}" pattern="MMM d, yyyy 'at' h:mm a" />
                </span>
      <c:if test="${question.tag != null}">
                    <span class="tag">
                        <a href="forum?tag=${question.tag.tagID}">${question.tag.name}</a>
                    </span>
      </c:if>
    </div>
  </div>

  <div class="question-content">
    ${question.content}
  </div>

  <div class="answers-section">
    <h2>${answers.size()} Answer(s)</h2>

    <c:forEach items="${answers}" var="answer">
      <div class="answer" id="answer-${answer.answerID}">
        <div class="answer-content">
            ${answer.content}
        </div>
        <div class="answer-meta">
          <span class="author">Answered by: ${answer.authorName}</span>
          <span class="date">
                            <fmt:formatDate value="${answer.createdAt}" pattern="MMM d, yyyy 'at' h:mm a" />
                        </span>
        </div>
      </div>
    </c:forEach>
  </div>

  <c:if test="${not empty sessionScope.user}">
    <div class="submit-answer">
      <h3>Your Answer</h3>
      <form action="create-answer" method="post">
        <input type="hidden" name="questionId" value="${question.questionID}">
        <div class="form-group">
          <textarea id="answerContent" name="content" required class="form-control"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Submit Answer</button>
      </form>
    </div>
  </c:if>

  <c:if test="${empty sessionScope.user}">
    <div class="login-prompt">
      <p>You must <a href="login.jsp">log in</a> to answer questions.</p>
    </div>
  </c:if>

  <div class="navigation">
    <a href="forum" class="btn btn-secondary">Back to Forum</a>
  </div>
</div>
</body>
</html>
