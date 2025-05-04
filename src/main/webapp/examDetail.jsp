<%@ page import="model.User" %><%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 4/10/2025
  Time: 11:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    User user = (User) session.getAttribute("user");
%>

<%@ page isELIgnored="false" %>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
  <title>Chọn kỹ năng và thời gian làm bài</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #faf0f5; /* Màu hồng pastel nhạt */
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #4a4a4a;
    }

    .container {
      width: 100%;
      max-width: 650px;
      margin: 20px;
      background: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 5px 20px rgba(216, 27, 96, 0.1); /* Shadow hồng nhạt */
      transition: transform 0.3s ease;
    }

    .container:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 25px rgba(216, 27, 96, 0.15);
    }

    .title {
      text-align: center;
      color: #d81b60; /* Màu hồng đậm */
      margin-bottom: 35px;
      font-size: 28px;
      font-weight: 600;
      position: relative;
      padding-bottom: 15px;
    }

    .title::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 80px;
      height: 4px;
      background: linear-gradient(90deg, #b3e5fc 0%, #81d4fa 100%); /* Gradient xanh pastel */
      border-radius: 2px;
    }

    .section {
      margin-bottom: 30px;
      padding: 25px;
      background: #fff;
      border-radius: 12px;
      border: 1px solid #fce4ec; /* Viền hồng nhạt */
      box-shadow: 0 3px 10px rgba(248, 187, 208, 0.1);
    }

    .section-title {
      font-size: 18px;
      color: #0277bd; /* Màu xanh đậm */
      margin-bottom: 20px;
      font-weight: 600;
      display: flex;
      align-items: center;
    }

    .section-title::before {
      content: '';
      display: inline-block;
      width: 6px;
      height: 20px;
      background: #81d4fa; /* Xanh pastel */
      margin-right: 10px;
      border-radius: 3px;
    }

    .skill-options {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 15px;
      margin: 20px 0;
    }

    .skill-option {
      padding: 16px;
      background: white;
      border: 2px solid #e1f5fe; /* Xanh pastel nhạt */
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-size: 16px;
      font-weight: 500;
      text-align: center;
      color: #0277bd; /* Xanh đậm */
      box-shadow: 0 2px 5px rgba(179, 229, 252, 0.2);
    }

    .skill-option:hover {
      transform: translateY(-3px);
      box-shadow: 0 5px 15px rgba(179, 229, 252, 0.3);
      border-color: #81d4fa;
    }

    .skill-option.selected {
      background: linear-gradient(135deg, #81d4fa 0%, #4fc3f7 100%); /* Gradient xanh pastel */
      color: white;
      border-color: transparent;
      box-shadow: 0 5px 15px rgba(129, 212, 250, 0.4);
    }

    .time-input {
      width: 100%;
      padding: 14px 20px;
      border: 2px solid #e1f5fe;
      border-radius: 8px;
      font-size: 16px;
      margin-top: 10px;
      transition: all 0.3s;
      background: white;
      color: #0277bd;
    }

    .time-input:focus {
      outline: none;
      border-color: #4fc3f7;
      box-shadow: 0 0 0 3px rgba(129, 212, 250, 0.3);
    }

    .time-note {
      color: #90a4ae;
      font-size: 14px;
      margin-top: 8px;
    }

    .start-btn {
      display: block;
      width: 100%;
      padding: 18px;
      background: linear-gradient(135deg, #f8bbd0 0%, #f48fb1 100%); /* Gradient hồng pastel */
      color: white;
      text-align: center;
      text-decoration: none;
      border-radius: 8px;
      margin-top: 30px;
      font-size: 18px;
      font-weight: 600;
      transition: all 0.3s ease;
      border: none;
      cursor: pointer;
      box-shadow: 0 4px 10px rgba(244, 143, 177, 0.3);
    }

    .start-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 7px 15px rgba(244, 143, 177, 0.4);
      background: linear-gradient(135deg, #f48fb1 0%, #f06292 100%);
    }

    .start-btn:disabled {
      background: linear-gradient(135deg, #e0e0e0 0%, #bdbdbd 100%);
      cursor: not-allowed;
      transform: none;
      box-shadow: none;
    }

    /* Responsive */
    @media (max-width: 640px) {
      .container {
        padding: 30px 20px;
      }

      .skill-options {
        grid-template-columns: 1fr;
      }

      .section {
        padding: 20px 15px;
      }
    }

    .comment-section {
      margin-top: 2rem;
      padding: 1rem;
      background-color: #f8f9fa;
      border-radius: 8px;
    }

    .comment-form {
      margin-bottom: 2rem;
    }

    .comment {
      margin-bottom: 1.5rem;
      padding: 1rem;
      background-color: white;
      border-radius: 8px;
      box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }

    .comment-header {
      display: flex;
      align-items: center;
      margin-bottom: 0.5rem;
    }

    .user-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-right: 1rem;
    }

    .comment-info {
      flex-grow: 1;
    }

    .comment-content {
      margin: 0.5rem 0;
    }

    .comment-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 0.5rem;
    }

    .vote-buttons {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .score {
      font-weight: bold;
      margin: 0 0.5rem;
    }

    .replies {
      margin-left: 2rem;
      margin-top: 1rem;
    }

    .reply {
      margin-bottom: 1rem;
      padding: 0.8rem;
      background-color: #f8f9fa;
      border-radius: 8px;
    }

    .reply-form, .edit-form {
      margin-top: 1rem;
      padding: 1rem;
      background-color: #f8f9fa;
      border-radius: 8px;
    }

    .comment-container {
      max-width: 800px;
      margin: 20px auto;
      padding: 20px;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .comment-box {
      margin-bottom: 20px;
    }

    .comment-box textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      resize: vertical;
    }

    .comment-section {
      display: flex;
      margin-bottom: 20px;
      padding: 15px;
      background-color: #f8f9fa;
      border-radius: 8px;
    }

    .comment-avt {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-right: 15px;
    }

    .comment-content {
      flex-grow: 1;
    }

    .comment-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 5px;
    }

    .comment-author {
      font-weight: bold;
      color: #333;
    }

    .comment-time {
      color: #666;
      font-size: 0.9em;
    }

    .comment-text {
      margin: 10px 0;
    }

    .vote-controls {
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .vote-btn {
      background: none;
      border: none;
      cursor: pointer;
      font-size: 1.2em;
    }

    .vote-btn.active {
      color: #007bff;
    }

    .vote-score {
      font-weight: bold;
    }

    .comment-options {
      display: flex;
      gap: 10px;
    }

    .edit-comment-option, .delete-comment-option {
      color: #666;
      cursor: pointer;
    }

    .edit-comment-option:hover, .delete-comment-option:hover {
      color: #333;
    }

    .modal {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0,0,0,0.5);
    }

    .modal-content {
      background-color: #fff;
      margin: 15% auto;
      padding: 20px;
      width: 50%;
      border-radius: 8px;
    }

    .close-modal {
      float: right;
      cursor: pointer;
      font-size: 1.5em;
    }

    .view-reply-button {
      font-size: 16px;
      padding: 3px 2px;
      border: 2px solid #9f5863;
      border-radius: 25px;
      background-color: #f8f9fa;
      color: #9f5863;
      cursor: pointer;
      transition: all 0.3s ease;
      font-weight: 500;
      text-align: center;
      display: inline-block;
      margin: 5px 0;
    }

    .view-reply-button:hover {
      background-color: pink;
      color: #fff;
      border-color: #9f5863;
    }
  </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="container">
  <h1 class="title">Chọn kỹ năng và thời gian làm bài</h1>

  <div class="section">
    <h3 class="section-title">Chọn loại câu hỏi</h3>
    <div class="skill-options">
      <div class="skill-option" data-type="Listening">Listening</div>
      <div class="skill-option" data-type="Reading">Reading</div>
      <div class="skill-option" data-type="Full">Full Test</div>
    </div>
  </div>

  <div class="section">
    <h3 class="section-title">Thời gian làm bài (phút)</h3>
    <input type="number" class="time-input" id="timeInput" min="1" placeholder="Nhập thời gian">
    <p class="time-note">Để trống để sử dụng thời gian mặc định</p>
  </div>

  <button class="start-btn" id="startBtn" disabled>Bắt đầu làm bài</button>
</div>

<%--<!-- COMMENT SECTION -->--%>
<%--<div class="comment-container">--%>
<%--    <h2 style="color: #333333; padding: 20px;">Bình luận</h2>--%>

<%--    <!-- Comment input form -->--%>
<%--    <c:choose>--%>
<%--        <c:when test="${user != null}">--%>
<%--            <form action="examComment" method="POST">--%>
<%--                <div class="comment-box">--%>
<%--                    <textarea name="commentInput" rows="4" placeholder="Nhập bình luận của bạn..."--%>
<%--                              required></textarea><br><br>--%>
<%--                    <input type="hidden" name="examID" value="${exam.examID}">--%>
<%--                    <input type="hidden" name="action" value="addComment">--%>
<%--                    <input type="submit" value="Gửi bình luận" class="btn btn-primary">--%>
<%--                </div>--%>
<%--            </form>--%>
<%--        </c:when>--%>
<%--        <c:otherwise>--%>
<%--            <div class="login-to-comment">--%>
<%--                <p>Vui lòng <a href="login.jsp">đăng nhập</a> để bình luận.</p>--%>
<%--            </div>--%>
<%--        </c:otherwise>--%>
<%--    </c:choose>--%>

<%--    <!-- Display Comments Section -->--%>
<%--    <div class="comments-display" id="commentsDisplay">--%>
<%--        <c:if test="${not empty comments}">--%>
<%--            <c:forEach var="comment" items="${comments}">--%>
<%--                <c:set var="replyCount" value="0" />--%>
<%--                <c:forEach var="entry" items="${replyMap}">--%>
<%--                    <c:if test="${comment.examCommentID == entry.key}">--%>
<%--                        <c:set var="replyCount" value="${fn:length(entry.value)}" />--%>
<%--                    </c:if>--%>
<%--                </c:forEach>--%>

<%--                <div class="comment-section" id="comment-${comment.examCommentID}">--%>
<%--                    <img src="${comment.userAvtURL}" alt="Avatar" class="comment-avt"--%>
<%--                         onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">--%>
<%--                    <div class="comment-content">--%>
<%--                        <div class="comment-header">--%>
<%--                            <div class="comment-author">${comment.userFullName}</div>--%>
<%--                            <div class="comment-time">${comment.createdDate}</div>--%>
<%--                        </div>--%>

<%--                        <div class="comment-text">${comment.content}</div>--%>

<%--                        <div class="vote-controls">--%>
<%--                            <button type="button" class="vote-btn upvote-btn" data-comment-id="${comment.examCommentID}"--%>
<%--                                    onclick="handleVote(this, 1)">--%>
<%--                                <i class="ti-angle-up"></i>--%>
<%--                            </button>--%>
<%--                            <span class="vote-score" id="score-${comment.examCommentID}">${comment.score}</span>--%>
<%--                            <button type="button" class="vote-btn downvote-btn" data-comment-id="${comment.examCommentID}"--%>
<%--                                    onclick="handleVote(this, -1)">--%>
<%--                                <i class="ti-angle-down"></i>--%>
<%--                            </button>--%>
<%--                        </div>--%>

<%--                        <c:forEach var="entry" items="${replyMap}">--%>
<%--                            <c:if test="${comment.examCommentID == entry.key}">--%>
<%--                                <div id="reply-section${comment.examCommentID}" style="display: none;">--%>
<%--                                    <ul>--%>
<%--                                        <c:forEach var="reply" items="${entry.value}">--%>
<%--                                            <li>--%>
<%--                                                <div>--%>
<%--                                                    <img src="${reply.userAvtURL}" alt="Avatar" class="comment-avt"--%>
<%--                                                         onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">--%>
<%--                                                    <div class="comment-content">--%>
<%--                                                        <div class="comment-header">--%>
<%--                                                            <div class="comment-author">${reply.userFullName}</div>--%>
<%--                                                            <div class="comment-time">${reply.createdDate}</div>--%>
<%--                                                        </div>--%>

<%--                                                        <div class="comment-text">${reply.content}</div>--%>

<%--                                                        <div class="vote-controls">--%>
<%--                                                            <button type="button" class="vote-btn upvote-btn"--%>
<%--                                                                    data-comment-id="${reply.examCommentID}"--%>
<%--                                                                    onclick="handleVote(this, 1)">--%>
<%--                                                                <i class="ti-angle-up"></i>--%>
<%--                                                            </button>--%>
<%--                                                            <span class="vote-score"--%>
<%--                                                                  id="score-${reply.examCommentID}">${reply.score}</span>--%>
<%--                                                            <button type="button" class="vote-btn downvote-btn"--%>
<%--                                                                    data-comment-id="${reply.examCommentID}"--%>
<%--                                                                    onclick="handleVote(this, -1)">--%>
<%--                                                                <i class="ti-angle-down"></i>--%>
<%--                                                            </button>--%>
<%--                                                        </div>--%>
<%--                                                    </div>--%>
<%--                                                </div>--%>
<%--                                            </li>--%>
<%--                                        </c:forEach>--%>
<%--                                    </ul>--%>
<%--                                </div>--%>
<%--                            </c:if>--%>
<%--                        </c:forEach>--%>
<%--                    </div>--%>

<%--                    <button class="view-reply-button" id="view-reply-button-${comment.examCommentID}" --%>
<%--                            onclick="showReplies(${comment.examCommentID})">--%>
<%--                        View reply (${replyCount})--%>
<%--                    </button>--%>
<%--                    <button name="action" class="btn btn-update btn-sm" value="reply"--%>
<%--                            onclick="showDiv(${comment.examCommentID})">Reply--%>
<%--                    </button>--%>
<%--                    <div id="updateDiv${comment.examCommentID}" style="display: none;">--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${user != null}">--%>
<%--                                <form style="display: inline" action="examComment" method="POST">--%>
<%--                                    <div class="comment-box">--%>
<%--                                        <textarea name="commentInput" rows="4" placeholder="Nhập bình luận của bạn..."--%>
<%--                                                  required></textarea><br><br>--%>
<%--                                        <input type="hidden" name="examID" value="${exam.examID}">--%>
<%--                                        <input type="hidden" name="parentID" value="${comment.examCommentID}">--%>
<%--                                        <input type="hidden" name="action" value="addComment">--%>
<%--                                        <input type="submit" value="Gửi bình luận" class="btn btn-primary">--%>
<%--                                    </div>--%>
<%--                                </form>--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <div class="login-to-comment">--%>
<%--                                    <p>Vui lòng <a href="login.jsp">đăng nhập</a> để bình luận.</p>--%>
<%--                                </div>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </div>--%>

<%--                    <c:if test="${user != null && user.userID == comment.userID}">--%>
<%--                        <div class="comment-options">--%>
<%--                            <a href="javascript:void(0);" class="edit-comment-option"--%>
<%--                               onclick="openEditCommentModal(${comment.examCommentID}, '<c:out value="${comment.content}" escapeXml="true"/>')">--%>
<%--                                <i class="ti-pencil"></i>--%>
<%--                            </a>--%>
<%--                            <a href="javascript:void(0);" class="delete-comment-option"--%>
<%--                               onclick="confirmDeleteComment(${comment.examCommentID})">--%>
<%--                                <i class="ti-trash"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </c:if>--%>
<%--                </div>--%>
<%--            </c:forEach>--%>
<%--        </c:if>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<!-- Edit Comment Modal -->--%>
<%--<div id="edit-comment-modal" class="modal" style="display:none;">--%>
<%--    <div class="modal-content">--%>
<%--        <span class="close-modal">&times;</span>--%>
<%--        <h2>Chỉnh sửa bình luận</h2>--%>
<%--        <form id="edit-comment-form" action="examComment" method="POST">--%>
<%--            <input type="hidden" id="edit-comment-id" name="examCommentID">--%>
<%--            <input type="hidden" name="examID" value="${exam.examID}">--%>
<%--            <input type="hidden" name="action" value="editComment">--%>
<%--            <textarea id="edit-comment-content" name="commentContent" rows="4" required></textarea>--%>
<%--            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>--%>
<%--        </form>--%>
<%--    </div>--%>
<%--</div>--%>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const skillOptions = document.querySelectorAll('.skill-option');
    const startBtn = document.getElementById('startBtn');
    const timeInput = document.getElementById('timeInput');
    let selectedType = null;

    skillOptions.forEach(option => {
      option.addEventListener('click', function() {
        // Remove selected class from all options
        skillOptions.forEach(opt => opt.classList.remove('selected'));
        // Add selected class to clicked option
        this.classList.add('selected');
        selectedType = this.getAttribute('data-type');
        updateStartButton();
      });
    });

    function updateStartButton() {
      if (selectedType) {
        startBtn.disabled = false;
        const examId = '<%= request.getParameter("examID") %>';
        const time = timeInput.value ? timeInput.value : '';

        startBtn.onclick = function() {
          window.location.href = 'exam?action=do&examID=' + examId + '&eQuesType=' + selectedType + '&time=' + time;
        };
      } else {
        startBtn.disabled = true;
      }
    }

    timeInput.addEventListener('input', function() {
      if (this.value < 1) {
        this.value = 1;
      }
      updateStartButton();
    });
  });

  // Load user votes when page loads
  document.addEventListener('DOMContentLoaded', function() {
      let userVotes = {};
      const isUserLoggedIn = <%= user != null %>; // Sửa cú pháp JSP expression

      if (isUserLoggedIn) {
      const commentElements = document.querySelectorAll('[data-comment-id]');
      const commentIDs = Array.from(commentElements).map(el => el.getAttribute('data-comment-id'));

      if (commentIDs.length > 0) {
        $.ajax({
          url: 'examComment',
          type: 'POST',
          data: {
            action: "loadUserVotes",
            examCommentIDs: commentIDs,
            examID: document.querySelector('input[name="examID"]').value
          },
          dataType: "json",
          success: function(userVoteData) {
            if (userVoteData) {
              userVotes = userVoteData;
              Object.keys(userVotes).forEach(commentId => {
                const voteValue = userVotes[commentId];
                const upvoteBtn = document.querySelector('.upvote-btn[data-comment-id="' + commentId + '"]');
                const downvoteBtn = document.querySelector('.downvote-btn[data-comment-id="' + commentId + '"]');

                if (voteValue === 1 && upvoteBtn) {
                  upvoteBtn.classList.add('active');
                } else if (voteValue === -1 && downvoteBtn) {
                  downvoteBtn.classList.add('active');
                }
              });
            }
          }
        });
      }
    }
  });

  function handleVote(button, voteType) {
    const commentId = button.getAttribute('data-comment-id');
    const scoreElement = document.getElementById('score-' + commentId);

    if (!isUserLoggedIn) {
      alert("Vui lòng đăng nhập để vote!");
      return;
    }

    $.ajax({
      url: 'examComment',
      type: 'POST',
      data: {
        action: "updateScore",
        examCommentID: commentId,
        voteType: voteType,
        examID: document.querySelector('input[name="examID"]').value
      },
      dataType: "json",
      success: function (response) {
        if (response.success) {
          scoreElement.textContent = response.score;

          const upvoteBtn = document.querySelector('.upvote-btn[data-comment-id="' + commentId + '"]');
          const downvoteBtn = document.querySelector('.downvote-btn[data-comment-id="' + commentId + '"]');

          upvoteBtn.classList.remove('active');
          downvoteBtn.classList.remove('active');

          if (response.voteType === 1) {
            upvoteBtn.classList.add('active');
          } else if (response.voteType === -1) {
            downvoteBtn.classList.add('active');
          }

          userVotes[commentId] = response.voteType;
        }
      },
      error: function (xhr, status, error) {
        if (xhr.status === 401) {
          alert("Vui lòng đăng nhập để vote!");
        }
      }
    });
  }

  function openEditCommentModal(commentID, commentContent) {
    const modal = document.getElementById("edit-comment-modal");
    const contentField = document.getElementById("edit-comment-content");
    const idField = document.getElementById("edit-comment-id");

    if (modal && contentField && idField) {
      contentField.value = commentContent;
      idField.value = commentID;
      modal.style.display = "block";
    }
  }

  function confirmDeleteComment(commentID) {
    if (confirm("Are you sure you want to delete this comment?")) {
      const deleteForm = document.createElement("form");
      deleteForm.method = "POST";
      deleteForm.action = "examComment";

      const examIdField = document.createElement("input");
      examIdField.type = "hidden";
      examIdField.name = "examID";
      examIdField.value = document.getElementsByName("examID")[0].value;

      const commentIdField = document.createElement("input");
      commentIdField.type = "hidden";
      commentIdField.name = "examCommentID";
      commentIdField.value = commentID;

      const actionField = document.createElement("input");
      actionField.type = "hidden";
      actionField.name = "action";
      actionField.value = "deleteComment";

      deleteForm.appendChild(examIdField);
      deleteForm.appendChild(commentIdField);
      deleteForm.appendChild(actionField);

      document.body.appendChild(deleteForm);
      deleteForm.submit();
    }
  }

  function showDiv(commentID) {
    const updateDiv = document.getElementById("updateDiv" + commentID);
    if (updateDiv.style.display === "none") {
      updateDiv.style.display = "block";
    } else {
      updateDiv.style.display = "none";
    }
  }

  function showReplies(commentID) {
    const replyDiv = document.getElementById("reply-section" + commentID);
    const button = document.getElementById("view-reply-button-" + commentID);

    if (replyDiv.style.display === "none") {
      replyDiv.style.display = "block";
      button.innerHTML = "Hide replies";
    } else {
      replyDiv.style.display = "none";
      button.innerHTML = "View replies";
    }
  }
</script>
<jsp:include page="header.jsp"></jsp:include>
</body>
</html>
