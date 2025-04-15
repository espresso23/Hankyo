<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${post.heading}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .post-header {
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .post-title {
            margin-top: 0;
            color: #333;
        }
        .post-meta {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .post-content {
            margin-bottom: 30px;
        }
        .post-image {
            max-width: 100%;
            height: auto;
            margin: 20px 0;
            border-radius: 4px;
        }
        .comments-section {
            margin-top: 30px;
            border-top: 1px solid #eee;
            padding-top: 20px;
        }
        .comment {
            background: #f9f9f9;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 15px;
        }
        .comment-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
        .comment-author {
            font-weight: bold;
        }
        .comment-date {
            color: #666;
            font-size: 12px;
        }
        .comment-content {
            margin-top: 10px;
        }
        .add-comment {
            margin-top: 20px;
        }
        .add-comment textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            resize: vertical;
            min-height: 100px;
        }
        .btn {
            display: inline-block;
            background-color: #4CAF50;
            color: white;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            font-size: 14px;
            margin-top: 10px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .btn-danger {
            background-color: #dc3545;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .actions {
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }
        .report-button {
            display: inline-block;
            background-color: #ff4d4d;
            color: white;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
        }
        .report-button:hover {
            background-color: #e60000;
        }
    </style>
</head>
<body>
<div class="container">
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="post-header">
        <h1 class="post-title">${post.heading}</h1>
        <div class="post-meta">
            <span>Posted by: <strong>${post.username}</strong></span> &bull;
            <span><fmt:formatDate value="${post.createdDate}" pattern="dd MMM yyyy HH:mm" /></span> &bull;
            <span>Score: ${post.scorePost}</span>
        </div>
    </div>

    <div class="post-content">
        <c:if test="${not empty post.imgURL}">
            <img src="${post.imgURL}" alt="Post image" class="post-image">
        </c:if>
        <p>${post.content}</p>
    </div>

    <div class="actions">
        <c:if test="${sessionScope.user != null}">
            <c:if test="${sessionScope.user.userID != post.userID}">
                <a href="ReportServlet?action=showReportForm&postID=${post.postID}" class="report-button">Report this post</a>
            </c:if>
            <a href="PostServlet?action=like&postID=${post.postID}" class="btn">Like</a>
        </c:if>
        <c:if test="${sessionScope.user.userID == post.userID || sessionScope.user.role == 'admin'}">
            <a href="PostServlet?action=edit&postID=${post.postID}" class="btn btn-secondary">Edit</a>
            <a href="PostServlet?action=delete&postID=${post.postID}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this post?')">Delete</a>
        </c:if>
    </div>

    <div class="comments-section">
        <h3>Comments (${comments.size()})</h3>

        <c:forEach items="${comments}" var="comment">
            <div class="comment">
                <div class="comment-header">
                    <span class="comment-author">${comment.username}</span>
                    <span class="comment-date"><fmt:formatDate value="${comment.createdDate}" pattern="dd MMM yyyy HH:mm" /></span>
                </div>
                <div class="comment-content">
                        ${comment.content}
                </div>

                <div class="comment-actions" style="margin-top: 10px; text-align: right;">
                    <c:if test="${sessionScope.user != null && sessionScope.user.userID != comment.userID}">
                        <a href="ReportServlet?action=showReportComment&commentID=${comment.commentID}" class="report-button" style="font-size: 12px; padding: 4px 8px;">Report</a>
                    </c:if>
                    <c:if test="${sessionScope.user.userID == comment.userID || sessionScope.user.role == 'admin'}">
                        <a href="CommentServlet?action=delete&commentID=${comment.commentID}&postID=${post.postID}"
                           onclick="return confirm('Are you sure you want to delete this comment?')"
                           style="color: #dc3545; font-size: 12px; margin-left: 10px;">Delete</a>
                    </c:if>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty comments}">
            <p>No comments yet. Be the first to comment!</p>
        </c:if>

        <c:if test="${sessionScope.user != null}">
            <div class="add-comment">
                <h4>Add a Comment</h4>
                <form action="CommentServlet" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="postID" value="${post.postID}">
                    <input type="hidden" name="userID" value="${sessionScope.user.userID}">

                    <div>
                        <textarea name="content" placeholder="Write your comment..." required></textarea>
                    </div>

                    <button type="submit" class="btn">Post Comment</button>
                </form>
            </div>
        </c:if>

        <c:if test="${sessionScope.user == null}">
            <p>Please <a href="login.jsp">login</a> to comment.</p>
        </c:if>
    </div>
</div>
</body>
</html>