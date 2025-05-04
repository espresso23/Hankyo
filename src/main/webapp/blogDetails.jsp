<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%@ page import="model.Comment" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    User user = (User) session.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hankyo Forum</title>
    <!-- Bootstrap Bundle JS (gồm cả Popper + Modal API) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/font/themify-icons/themify-icons.css">
    <link rel="stylesheet" href="asset/css/blogdetails.css">
    <link rel="stylesheet" href="asset/css/modal.css"/>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-image: url('asset/png/background/background-2.png');
            background-size: cover;
            background-position: left;
        }

        .modal {
            z-index: 1055; /* hoặc cao hơn */
        }

        .modal-backdrop {
            z-index: 1040;
        }

        /* === Các nút chính === */
        .btn-primary {
            background-color: #e472ad !important;
            border-color: #e472ad !important;
            border-radius: 20px;
            padding: 8px 20px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-primary:hover {
            background-color: #cc527b !important;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(228, 114, 173, 0.3);
        }

        /* === Comment section === */
        .comment-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 0 15px;
        }

        .comment-section {
            position: relative;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #f0f0f0;
        }

        .comment-avt {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            float: left;
            margin-right: 15px;
            object-fit: cover;
        }

        .comment-content {
            margin-left: 55px;
            position: relative;
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 12px;
            margin-bottom: 8px;
        }

        .comment-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8px;
            position: relative;
        }

        .comment-author {
            font-weight: bold;
            color: #365899;
        }

        .comment-time {
            font-size: 0.8em;
            color: #90949c;
        }

        .comment-text {
            margin-bottom: 10px;
            word-wrap: break-word;
            color: #333;
        }

        /* === Nút vote và actions === */
        .vote-controls {
            display: flex;
            align-items: center;
            margin-top: 8px;
        }

        .vote-btn {
            background: none;
            border: none;
            cursor: pointer;
            color: #65676b;
            padding: 0 5px;
            font-size: 16px;
        }

        .vote-btn:hover {
            color: #1877f2;
        }

        .vote-score {
            margin: 0 8px;
            font-weight: bold;
            min-width: 20px;
            text-align: center;
        }

        .comment-actions {
            display: flex;
            gap: 10px;
            margin-top: 8px;
        }

        .btn-update {
            background: none;
            border: none;
            color: #65676b;
            cursor: pointer;
            padding: 5px 8px;
            font-size: 0.9em;
        }

        .btn-update:hover {
            text-decoration: underline;
        }

        .view-reply-button {
            background: none;
            border: none;
            color: #65676b;
            cursor: pointer;
            padding: 5px 8px;
            font-size: 0.9em;
            margin-left: 0;
        }

        .view-reply-button:hover {
            text-decoration: underline;
        }

        .btn-report {
            background: none;
            border: none;
            color: #65676b;
            cursor: pointer;
            padding: 5px 8px;
            font-size: 0.9em;
        }

        .btn-report:hover {
            text-decoration: underline;
            color: #dc3545;
        }

        /* === Comment options (edit/delete) === */
        .comment-options {
            position: absolute;
            right: 0;
            top: 0;
            display: none;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            z-index: 1;
            min-width: 120px;
        }

        .comment-content:hover .comment-options {
            display: block;
        }

        .comment-options button {
            background: none;
            border: none;
            padding: 8px 12px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 5px;
            width: 100%;
            color: #65676b;
            font-size: 14px;
            text-align: left;
        }

        .comment-options button:hover {
            background: #f5f5f5;
            color: #e472ad;
        }

        /* === Reply section === */
        .comment-thread {
            list-style-type: none;
            padding-left: 15px;
            margin-top: 10px;
            margin-left: 40px;
            border-left: 2px solid #e4e6eb;
        }

        .comment-thread li {
            position: relative;
            margin-bottom: 12px;
        }

        .comment-thread .comment-content {
            margin-left: 15px;
            background-color: #f0f2f5;
            padding: 12px;
            border-radius: 12px;
            font-size: 0.95em;
        }

        /* === Modal edit post === */
        .modal-content {
            border-radius: 16px;
            border: none;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }

        .modal-header {
            border-bottom: 1px solid #f0f2f5;
            padding: 16px 24px;
            background: linear-gradient(135deg, #f8fbfd 60%, #fce4ec 100%);
            border-radius: 16px 16px 0 0;
        }

        .modal-title {
            color: #e472ad;
            font-weight: 700;
        }

        .modal-body {
            padding: 24px;
        }

        .input-field {
            width: 100%;
            padding: 12px;
            border: 1px solid #e0e9f0;
            border-radius: 12px;
            margin-bottom: 16px;
            transition: all 0.3s;
        }

        .input-field:focus {
            border-color: #e472ad;
            box-shadow: 0 0 0 3px rgba(228, 114, 173, 0.2);
        }

        .submit-button {
            background-color: #e472ad;
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 12px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            width: 100%;
        }

        .submit-button:hover {
            background-color: #cc527b;
            transform: translateY(-2px);
        }

        #blog-details {
            max-width: 900px;
            margin: 30px auto;
            padding: 30px;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
        }

        .blog-details-title h2 {
            font-size: 28px;
            font-weight: bold;
            color: #222;
            margin-bottom: 16px;
        }

        .blog-details-author {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 12px;
        }

        .details-avatar {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #eee;
        }

        .details-name {
            font-weight: 600;
            color: #444;
            margin: 0;
        }

        .blog-details-date {
            font-size: 13px;
            color: #888;
            margin-top: 4px;
        }

        .blog-content {
            font-size: 17px;
            line-height: 1.7;
            color: #333;
            margin-top: 20px;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .comment-content {
                margin-left: 0;
                margin-top: 10px;
            }

            .comment-avt {
                width: 36px;
                height: 36px;
            }

            .comment-thread {
                margin-left: 20px;
            }
        }

    </style>
</head>
<body>
<!-- HEADER -->
<c:import url="header.jsp"/>

<!-- BLOG DETAILS -->
<div id="blog-details">
    <c:if test="${not empty post}">
        <input type="hidden" name="postID" value="${post.postID}">
        <div class="blog-details-title">
            <h2>${post.heading}</h2>
            <c:if test="${user != null && user.userID == post.userID}">
                <div class="dropdown-container">
                    <i class="ti-more-alt" id="more-options"></i>
                    <div class="dropdown-menu" id="dropdown-menu" style="display:none;">
                        <a id="openModalBtn"><i class="ti-pencil"> Edit</i></a>
                        <a href="#" id="delete" onclick="confirmDelete(${post.getPostID()})"><i class="ti-trash">
                            Delete</i></a>
                    </div>
                </div>
                <!-- Hidden form for deletion -->
                <form id="deleteFormPost" action="postDetails" method="POST" style="display: none;">
                    <input type="hidden" name="action" value="deletePost">
                    <input type="hidden" name="postID" id="postID" value="${post.getPostID()}">
                </form>
            </c:if>
        </div>
        <div class="blog-details-author">
            <a href="profile?user=${post.getUserName()}">
                <img src="${avatar}" alt="Author Avatar"
                     class="details-avatar"
                     onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"/>
            </a>
            <div style="display: flex; flex-direction: column; justify-content: center;">
                <p class="details-name" style="margin: 0;">
                    <a href="profile?user=${post.getUserName()}"
                       style="color: inherit; text-decoration: none;">${fullName}</a>
                </p>
                <!-- Honour Badge -->
                <c:if test="${not empty equippedHonourImage}">
                    <div style="margin-top: 6px; display: inline-flex; align-items: center; gap: 6px;">
                        <span style="
                                font-weight: bold;
                                font-size: 13px;
                                background: linear-gradient(to right, ${equippedGradientStart}, ${equippedGradientEnd});
                                -webkit-background-clip: text;
                                -webkit-text-fill-color: transparent;
                                background-clip: text;
                                text-fill-color: transparent;
                                ">
                                ${equippedHonourName}
                        </span>
                        <img src="${equippedHonourImage}" alt="Badge" style="width: 20px; height: 20px;"/>
                    </div>
                </c:if>


                <div class="blog-details-date" style="margin-top: 4px;">${post.createdDate}</div>
            </div>
        </div>

        <div class="blog-content">
            <p>${post.content}</p>
        </div>
    </c:if>
    <c:if test="${empty post}">
        <p>This post is not available.</p>
    </c:if>
</div>

<!-- COMMENT SECTION -->
<div class="comment-container">
    <h2 style="color: #333; padding: 20px 0;">Bình luận</h2>

    <!-- Comment input form -->
    <c:choose>
        <c:when test="${user != null}">
            <form action="postDetails" method="POST">
                <div class="comment-box">
                    <textarea name="commentInput" rows="4" placeholder="Nhập bình luận của bạn..."
                              required></textarea><br>
                    <input type="hidden" name="postID" value="${post.postID}">
                    <input type="hidden" name="action" value="addComment">
                    <input type="submit" value="Gửi bình luận" class="btn btn-primary">
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <div class="login-to-comment">
                <p>Vui lòng <a href="login.jsp">đăng nhập</a> để bình luận.</p>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Display Comments Section -->
    <div class="comments-display" id="commentsDisplay">
        <c:if test="${not empty comments}">
            <c:forEach var="comment" items="${comments}">
                <div class="comment-section" id="comment-${comment.commentID}">
                    <a href="profile?user=${comment.username}">
                        <img src="${comment.userAvtURL}" alt="Avatar" class="comment-avt"
                             onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">
                    </a>
                    <div class="comment-content">
                        <div class="comment-header">
                            <div>
<span class="comment-author">
    <a href="profile?user=${comment.username}"
       style="color: #365899; text-decoration: none;">${comment.userFullName}</a>
</span>
                                <span class="comment-time">${comment.createdDate}</span>
                            </div>
                            <c:if test="${user != null}">
                                <div class="comment-options">
                                    <c:if test="${user.userID == comment.userID}">
                                        <button onclick="openEditCommentModal(${comment.commentID}, '${fn:escapeXml(comment.content)}')">
                                            <i class="ti-pencil"></i> Edit
                                        </button>
                                    </c:if>
                                    <c:if test="${user.userID == comment.userID || user.userID == post.userID}">
                                        <button onclick="confirmDeleteComment(${comment.commentID})">
                                            <i class="ti-trash"></i> Delete
                                        </button>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                        <div class="comment-text">${comment.content}</div>
                        <div class="vote-controls">
                            <button type="button" class="vote-btn upvote-btn" data-comment-id="${comment.commentID}"
                                    onclick="handleVote(this, 1)">
                                <i class="ti-angle-up"></i>
                            </button>
                            <span class="vote-score" id="score-${comment.commentID}">${comment.score}</span>
                            <button type="button" class="vote-btn downvote-btn" data-comment-id="${comment.commentID}"
                                    onclick="handleVote(this, -1)">
                                <i class="ti-angle-down"></i>
                            </button>
                        </div>
                        <div class="comment-actions">
                            <button class="btn-update"
                                    onclick="toggleReplyForm(${comment.commentID}, '${comment.userFullName}')">
                                <i class="ti-comment"></i> Reply
                            </button>
                            <c:if test="${not empty replyMap[comment.commentID]}">
                                <button class="view-reply-button" id="view-reply-button-${comment.commentID}"
                                        onclick="showReplies(${comment.commentID})">
                                    View reply (${fn:length(replyMap[comment.commentID])})
                                </button>
                            </c:if>
                            <c:if test="${user != null && user.userID != comment.userID}">
                                <button type="button" class="btn-report"
                                        onclick="openReportModal(${comment.commentID})">
                                    <i class="fas fa-flag"></i> Report
                                </button>
                            </c:if>
                        </div>

                        <div id="replyForm-${comment.commentID}" class="reply-form" style="display:none;">
                            <form method="POST" action="postDetails">
                                <textarea name="commentInput" rows="2" class="form-control"
                                          required>@${comment.userFullName} </textarea>
                                <input type="hidden" name="postID" value="${post.postID}">
                                <input type="hidden" name="parentID" value="${comment.commentID}">
                                <input type="hidden" name="action" value="addComment">
                                <button type="submit" class="btn btn-primary btn-sm mt-2">Gửi</button>
                            </form>
                        </div>

                        <!-- Show Replies -->
                        <c:if test="${not empty replyMap[comment.commentID]}">
                            <div id="reply-section${comment.commentID}" style="display: none;">
                                <ul class="comment-thread">
                                    <c:forEach var="reply" items="${replyMap[comment.commentID]}">
                                        <li>
                                            <div class="comment-content">
                                                <div class="comment-header">
                                                    <div>
                                                       <span class="comment-author">
    <a href="profile?user=${reply.username}" style="color: #365899; text-decoration: none;">${reply.userFullName}</a>
</span>
                                                        <a href="profile?user=${reply.username}">
                                                            <img src="${reply.userAvtURL}" alt="Avatar" class="comment-avt"
                                                                 onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">
                                                        </a>

                                                        <span class="comment-time">${reply.createdDate}</span>
                                                    </div>
                                                    <c:if test="${user != null && (user.userID == reply.userID || user.userID == post.userID)}">
                                                        <div class="comment-options">
                                                            <c:if test="${user.userID == reply.userID}">
                                                                <button onclick="openEditCommentModal(${reply.commentID}, '${fn:escapeXml(reply.content)}')">
                                                                    <i class="ti-pencil"></i> Edit
                                                                </button>
                                                            </c:if>
                                                            <button onclick="confirmDeleteComment(${reply.commentID})">
                                                                <i class="ti-trash"></i> Delete
                                                            </button>
                                                        </div>
                                                    </c:if>
                                                </div>
                                                <div class="comment-text">${reply.content}</div>
                                                <div class="vote-controls">
                                                    <button type="button" class="vote-btn upvote-btn"
                                                            data-comment-id="${reply.commentID}"
                                                            onclick="handleVote(this, 1)">
                                                        <i class="ti-angle-up"></i>
                                                    </button>
                                                    <span class="vote-score"
                                                          id="score-${reply.commentID}">${reply.score}</span>
                                                    <button type="button" class="vote-btn downvote-btn"
                                                            data-comment-id="${reply.commentID}"
                                                            onclick="handleVote(this, -1)">
                                                        <i class="ti-angle-down"></i>
                                                    </button>
                                                </div>
                                                <div class="comment-actions">
                                                    <button class="btn-update"
                                                            onclick="toggleReplyForm(${reply.commentID}, '${reply.userFullName}')">
                                                        <i class="ti-comment"></i> Reply
                                                    </button>
                                                    <c:if test="${user != null && user.userID != reply.userID}">
                                                        <button type="button" class="btn-report"
                                                                onclick="openReportModal(${comment.commentID})">
                                                            <i class="fas fa-flag"></i> Report
                                                        </button>
                                                    </c:if>
                                                </div>
                                                <div id="replyForm-${reply.commentID}" class="reply-form"
                                                     style="display:none;">
                                                    <form method="POST" action="postDetails">
                                                        <textarea name="commentInput" rows="2" class="form-control"
                                                                  required>@${reply.userFullName} </textarea>
                                                        <input type="hidden" name="postID" value="${post.postID}">
                                                        <input type="hidden" name="parentID" value="${reply.commentID}">
                                                        <input type="hidden" name="action" value="addComment">
                                                        <button type="submit" class="btn btn-primary btn-sm mt-2">Gửi
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </c:if>
    </div>
</div>

<!-- Edit Comment Modal -->
<div id="edit-comment-modal" class="modal" style="display:none;">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title">Chỉnh sửa bình luận</h5>
            <span class="close-modal">&times;</span>
        </div>
        <div class="modal-body">
            <form id="edit-comment-form" action="postDetails" method="POST">
                <input type="hidden" id="edit-comment-id" name="commentID">
                <input type="hidden" name="postID" value="${post.postID}">
                <input type="hidden" name="action" value="editComment">
                <textarea id="edit-comment-content" name="commentContent" rows="4" class="input-field"
                          required></textarea>
                <button type="submit" class="submit-button">Lưu thay đổi</button>
            </form>
        </div>
    </div>
</div>

<!-- Comment Report Modal -->
<div class="modal fade" id="commentReportModal" tabindex="-1" aria-labelledby="commentReportModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="commentReportModalLabel">Report Comment</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="commentReportForm">
                    <input type="hidden" id="modal-comment-id" name="commentID">
                    <input type="hidden" id="modal-post-id" name="postID" value="${post.postID}">
                    <div class="mb-3">
                        <label for="commentReportReason" class="form-label">Reason for reporting:</label>
                        <textarea class="form-control" id="commentReportReason" name="reason" rows="3"
                                  required></textarea>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" id="submit-report-btn" class="btn btn-danger">Submit Report</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Edit Post Modal -->
<div id="myModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title">Edit Post</h5>
            <span class="close">&times;</span>
        </div>
        <div class="modal-body">
            <form action="postDetails" method="POST" enctype="multipart/form-data">
                <div class="mb-3">
                    <label for="title" class="form-label">Heading</label>
                    <input type="text" class="input-field" id="title" name="title" value="${post.heading}" required>
                </div>
                <div class="mb-3">
                    <label for="default" class="form-label">Content</label>
                    <textarea id="default" name="description">${post.content}</textarea>
                </div>
                <input type="hidden" name="action" value="editPost">
                <input type="hidden" name="postID" value="${post.postID}">
                <button type="submit" class="submit-button">Save Changes</button>
            </form>
        </div>
    </div>
</div>

<c:import url="footer.jsp"/>

<script>
    // ===== Comment Functions =====
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
        if (confirm("Are you sure you want to delete this comment? This will also delete all replies to this comment.")) {
            const deleteForm = document.createElement("form");
            deleteForm.method = "POST";
            deleteForm.action = "postDetails";

            const postIdField = document.createElement("input");
            postIdField.type = "hidden";
            postIdField.name = "postID";
            postIdField.value = document.getElementsByName("postID")[0].value;

            const commentIdField = document.createElement("input");
            commentIdField.type = "hidden";
            commentIdField.name = "commentID";
            commentIdField.value = commentID;

            const actionField = document.createElement("input");
            actionField.type = "hidden";
            actionField.name = "action";
            actionField.value = "deleteComment";

            deleteForm.appendChild(postIdField);
            deleteForm.appendChild(commentIdField);
            deleteForm.appendChild(actionField);

            document.body.appendChild(deleteForm);
            deleteForm.submit();
        }
    }

    function toggleReplyForm(commentID, userName) {
        const form = document.getElementById("replyForm-" + commentID);
        if (!form) return;

        const textarea = form.querySelector("textarea");

        if (form.style.display === "none" || form.style.display === "") {
            form.style.display = "block";
            if (textarea && (textarea.value.trim() === "" || !textarea.value.includes("@" + userName))) {
                textarea.value = "@" + userName + " ";
            }
            textarea.focus();
            textarea.selectionStart = textarea.selectionEnd = textarea.value.length;
        } else {
            form.style.display = "none";
        }
    }

    function showReplies(commentID) {
        const replyDiv = document.getElementById("reply-section" + commentID);
        const button = document.getElementById("view-reply-button-" + commentID);

        if (!replyDiv || !button) return;

        if (replyDiv.style.display === "none" || replyDiv.style.display === "") {
            replyDiv.style.display = "block";
            button.textContent = "Hide replies";
        } else {
            replyDiv.style.display = "none";
            const replyCount = replyDiv.querySelectorAll('li').length;
            button.textContent = "View reply (" + replyCount + ")";
        }
    }

    // ===== Post Functions =====
    document.addEventListener("DOMContentLoaded", function () {
        const modal = document.getElementById("myModal");
        const btn = document.getElementById("openModalBtn");
        const span = document.getElementsByClassName("close")[0];

        if (btn) {
            btn.onclick = function () {
                modal.style.display = "block";
            }
        }

        if (span) {
            span.onclick = function () {
                modal.style.display = "none";
            }
        }

        // Đóng modal khi click bên ngoài
        window.addEventListener("click", function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }

            // Đóng dropdown khi click bên ngoài
            const dropdown = document.getElementById("dropdown-menu");
            if (dropdown && dropdown.style.display === "block" && !event.target.closest('.dropdown-container')) {
                dropdown.style.display = "none";
            }
        });

        // Toggle dropdown options
        const moreOptions = document.getElementById("more-options");
        if (moreOptions) {
            moreOptions.addEventListener("click", function (event) {
                event.stopPropagation();
                const dropdown = document.getElementById("dropdown-menu");
                dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
            });
        }

        // Đóng modal edit comment
        const closeModalBtn = document.querySelector(".close-modal");
        if (closeModalBtn) {
            closeModalBtn.onclick = function () {
                document.getElementById("edit-comment-modal").style.display = "none";
            }
        }

        // Initialize comment report modal
        const commentReportModal = document.getElementById('commentReportModal');
        if (commentReportModal) {
            commentReportModal.addEventListener('show.bs.modal', function (event) {
                const button = event.relatedTarget;
                const commentID = button.getAttribute('data-commentid');
                document.getElementById('modal-comment-id').value = commentID;
            });
        }

        // Handle report submission
        const submitReportBtn = document.getElementById('submit-report-btn');
        if (submitReportBtn) {
            submitReportBtn.addEventListener('click', function () {
                submitCommentReport();
            });
        }
    });

    function submitCommentReport() {
        const commentID = document.getElementById('modal-comment-id').value;
        const reason = document.getElementById('commentReportReason').value;
        const postID = document.getElementById('modal-post-id').value;

        if (!reason || reason.trim() === '') {
            alert("Please provide a reason for your report");
            return;
        }

        $.ajax({
            url: 'postDetails',
            type: 'POST',
            dataType: 'json',
            data: {
                action: 'reportComment',
                commentID: commentID,
                reason: reason,
                postID: postID
            },
            success: function (response) {
                if (response.success) {
                    alert("✅ Comment reported successfully!");
                    // Close the modal and reset form
                    const modal = bootstrap.Modal.getInstance(document.getElementById('commentReportModal'));
                    if (modal) {
                        modal.hide();
                    }
                    document.getElementById('commentReportForm').reset();
                } else {
                    alert("❌ Failed to submit report: " + (response.error || "Unknown error"));
                }
            },
            error: function (xhr, status, error) {
                console.error("Report error:", status, error);
                alert("⚠️ Error submitting report. Please try again later.");
            }
        });
    }

    function confirmDelete(postID) {
        if (confirm("Are you sure you want to delete this post?")) {
            document.getElementById('postID').value = postID;
            document.getElementById('deleteFormPost').submit();
        }
    }

    // ===== Vote Functions =====
    let userVotes = {};
    const isUserLoggedIn = <%= user != null %>;

    function handleVote(button, voteType) {
        const commentId = button.getAttribute('data-comment-id');
        const scoreElement = document.getElementById('score-' + commentId);

        if (!isUserLoggedIn) {
            alert("Vui lòng đăng nhập để vote!");
            return;
        }

        $.ajax({
            url: 'postDetails',
            type: 'POST',
            data: {
                action: "updateScore",
                commentID: commentId,
                voteType: voteType,
                postID: document.querySelector('input[name="postID"]').value
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

    // Load initial vote states
    if (isUserLoggedIn) {
        document.addEventListener('DOMContentLoaded', function () {
            const commentElements = document.querySelectorAll('[data-comment-id]');
            const commentIDs = Array.from(commentElements).map(el => el.getAttribute('data-comment-id'));

            if (commentIDs.length > 0) {
                $.ajax({
                    url: 'postDetails',
                    type: 'POST',
                    data: {
                        action: "loadUserVotes",
                        commentIDs: commentIDs,
                        postID: document.querySelector('input[name="postID"]').value
                    },
                    dataType: "json",
                    success: function (userVoteData) {
                        if (userVoteData) {
                            userVotes = userVoteData || {};

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
        });
    }

    // Hiệu ứng hover cho các nút
    document.querySelectorAll('.btn-update, .btn-primary, .vote-btn').forEach(btn => {
        btn.addEventListener('mouseenter', () => {
            btn.style.transform = 'translateY(-2px)';
        });
        btn.addEventListener('mouseleave', () => {
            btn.style.transform = '';
        });
    });

    function openReportModal(commentID) {
        const commentIdInput = document.getElementById('modal-comment-id');
        const postIdInput = document.getElementById('modal-post-id');

        commentIdInput.value = commentID;
        postIdInput.value = document.querySelector('input[name="postID"]').value;

        const modalElement = document.getElementById('commentReportModal');

        if (typeof bootstrap !== 'undefined' && bootstrap.Modal) {
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        } else {
            alert("Bootstrap modal is not loaded. Please check your script imports.");
        }
    }

</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script src="./asset/tinymce/tinymce.min.js"></script>
<script>
    tinymce.init({
        selector: 'textarea#default',
        width: '100%',
        height: 300,
        plugins: [
            'advlist', 'autolink', 'link', 'image', 'lists', 'charmap', 'prewiew', 'anchor', 'pagebreak',
            'searchreplace', 'wordcount', 'visualblocks', 'code', 'fullscreen', 'insertdatetime', 'media',
            'table', 'emoticons', 'template', 'codesample'
        ],
        toolbar: 'undo redo | styles | bold italic underline | alignleft aligncenter alignright alignjustify |' +
            'bullist numlist outdent indent | link image | print preview media fullscreen | ' +
            'forecolor backcolor emoticons',
        menu: {
            favs: { title: 'menu', items: 'code visualaid | searchreplace | emoticons' }
        },
        menubar: 'favs file edit view insert format tools table',
        file_picker_types: 'image',
        file_picker_callback: (cb, value, meta) => {
            const input = document.createElement('input');
            input.setAttribute('type', 'file');
            input.setAttribute('accept', 'image/*');

            input.addEventListener('change', (e) => {
                const file = e.target.files[0];
                const reader = new FileReader();
                reader.addEventListener('load', () => {
                    const id = 'blobid' + (new Date()).getTime();
                    const blobCache = tinymce.activeEditor.editorUpload.blobCache;
                    const base64 = reader.result.split(',')[1];
                    const blobInfo = blobCache.create(id, file, base64);
                    blobCache.add(blobInfo);
                    cb(blobInfo.blobUri(), { title: file.name });
                });
                reader.readAsDataURL(file);
            });

            input.click();
        },
        content_style: 'body{font-family:Helvetica,Arial,sans-serif; font-size:16px}'
    });
</script>
</body>
</html>
