<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%@ page import="model.Comment" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    User user = (User) session.getAttribute("user");
%>
<script>
    // Sửa hàm openEditCommentModal
    function openEditCommentModal(commentID, commentContent) {
        const modal = document.getElementById("edit-comment-modal");
        const contentField = document.getElementById("edit-comment-content");
        const idField = document.getElementById("edit-comment-id");

        if (modal && contentField && idField) {
            contentField.value = commentContent;
            idField.value = commentID;
            modal.style.display = "block";
        } else {
            console.error("Không tìm thấy các phần tử modal");
        }
    }

    // Đóng modal
    document.querySelector(".close-modal").onclick = function () {
        document.getElementById("edit-comment-modal").style.display = "none";
    }

    // Đóng khi click bên ngoài
    window.onclick = function (event) {
        const modal = document.getElementById("edit-comment-modal");
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    function confirmDeleteComment(commentID) {
        console.log(`Attempting to delete comment with ID: ${commentID}`);
        if (confirm("Are you sure you want to delete this comment?")) {
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
</script>
<script>
    // Khai báo biến toàn cục
    let userVotes = {};
    const isUserLoggedIn = <%= user != null %>; // Sửa cú pháp JSP expression

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
                } else {
                    console.error("Vote update failed on server side");
                }
            },
            error: function (xhr, status, error) {
                console.error("Vote update failed:", status, error);
                if (xhr.status === 401) {
                    alert("Vui lòng đăng nhập để vote!");
                }
            }
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        if (isUserLoggedIn) {
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
                    },
                    error: function () {
                        console.error("Failed to load user votes");
                    }
                });
            }
        }
    });
</script>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hankyo Forum</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="asset/font/themify-icons/themify-icons.css">
    <link rel="stylesheet" href="asset/css/blogdetails.css">
    <link rel="stylesheet" href="asset/css/modal.css"/>
</head>
<style>
    body {
        margin: 0;
        font-family: Arial, sans-serif;
        background-image: url('asset/png/background/background-2.png');
        background-size: cover;
        background-position: left;
    }

    .reply-button,
    .view-reply-button {
        background: none;
        border: none;
        padding: 0;
        margin: 4px 0;
        font-size: 12px;
        font-weight: 500;
        color: var(--reddit-blue);
        cursor: pointer;
    }

    .reply-button:hover,
    .view-reply-button:hover {
        text-decoration: underline;
    }

    .btn-report {
        background: none;
        border: none;
        color: #6c757d;
        cursor: pointer;
        padding: 0;
        font-size: 14px;
        margin-left: 10px;
    }

    .btn-report:hover {
        color: #dc3545;
    }

    .btn-report i {
        margin-right: 5px;
    }

    .modal {
        z-index: 1060; /* Higher than default if needed */
    }

    .modal-backdrop {
        z-index: 1040; /* Below modal */
    }

    #edit-comment-modal {
        z-index: 1050;
    }

    /* General comments section styling */
    .comments-display {
        max-width: 800px;
        margin: 0 auto;
        font-family: Arial, sans-serif;
    }

    /* Main comment section */
    .comment-section {
        position: relative;
        margin-bottom: 20px;
        border-bottom: 1px solid #f0f0f0;
        padding-bottom: 10px;
    }

    /* Avatar styling */
    .comment-avt {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        float: left;
        margin-right: 15px;
        object-fit: cover;
    }

    /* Comment content container */
    .comment-content {
        margin-left: 55px;
        position: relative;
        background-color: #f8f9fa;
        padding: 12px;
        border-radius: 12px;
        margin-bottom: 8px;
    }

    /* Comment header with username and time */
    .comment-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
    }

    .comment-author {
        font-weight: bold;
        color: #365899;
    }

    .comment-time {
        font-size: 0.8em;
        color: #90949c;
    }

    /* Comment text */
    .comment-text {
        margin-bottom: 10px;
        word-wrap: break-word;
    }

    /* Vote controls */
    .vote-controls {
        display: flex;
        align-items: center;
        margin-top: 5px;
        margin-bottom: 5px;
    }

    .vote-btn {
        background: none;
        border: none;
        cursor: pointer;
        color: #65676b;
        padding: 0 5px;
    }

    .vote-btn:hover {
        color: #1877f2;
    }

    .vote-score {
        margin: 0 5px;
        font-weight: bold;
    }

    /* Reply section styling - Facebook-like threads */
    .comment-thread {
        list-style-type: none;
        padding-left: 15px;
        margin-top: 10px;
        margin-left: 40px;
        border-left: 2px solid #e4e6eb;
    }

    /* Reply items */
    .comment-thread li {
        position: relative;
        margin-bottom: 12px;
    }

    .comment-thread .comment-content {
        margin-left: 15px;
        background-color: #f0f2f5;
        padding: 10px;
        border-radius: 12px;
        font-size: 0.95em;
    }

    /* Reply form */
    .reply-form {
        margin-top: 10px;
        margin-left: 15px;
    }

    /* Show/hide replies button */
    .view-reply-button {
        background: none;
        border: none;
        color: #65676b;
        cursor: pointer;
        font-size: 0.9em;
        padding: 5px 0;
        margin-left: 5px;
        margin-bottom: 8px;
        display: block;
    }

    .view-reply-button:hover {
        text-decoration: underline;
        color: #1877f2;
    }

    /* Reply button */
    .reply-button, .btn-update {
        background: none;
        border: none;
        color: #65676b;
        cursor: pointer;
        margin-right: 10px;
        font-size: 0.9em;
        padding: 5px 8px;
    }

    .reply-button:hover, .btn-update:hover {
        background-color: #f0f2f5;
        border-radius: 4px;
    }

    /* Report button */
    .btn-report {
        background: none;
        border: none;
        color: #65676b;
        cursor: pointer;
        font-size: 0.85em;
        padding: 5px 8px;
    }

    .btn-report:hover {
        text-decoration: underline;
    }

    /* Comment options (edit/delete) */
    .comment-options {
        position: absolute;
        top: 10px;
        right: 10px;
        display: none;
    }

    .comment-content:hover .comment-options {
        display: block;
    }

    .comment-options a {
        margin-left: 10px;
        color: #65676b;
        font-size: 0.85em;
        text-decoration: none;
    }

    .comment-options a:hover {
        text-decoration: underline;
    }

    /* Reply section visibility */
    #reply-section {
        display: none;
    }

    /* Comment box for new replies */
    .comment-box {
        margin-top: 10px;
        margin-bottom: 15px;
    }

    .comment-box textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 20px;
        resize: vertical;
    }

    /* Make the reply thread visible when expanded */
    #reply-section[style*="display: block"] {
        display: block !important;
    }



</style>
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
                        <div id="myModal" class="modal">
                            <div class="modal-content">
                                <span class="close">&times;</span>
                                <form action="postDetails" method="POST" enctype="multipart/form-data">
                                    <h2>Edit Blog Post Id: ${post.postID}</h2>
                                    <label for="imgPost">Picture Cover:</label>
                                    <input name="imgPost" id="imgPost" type="file" class="input-field" required><br>
                                    <label for="title">Heading:</label>
                                    <input name="title" id="title" type="text" required class="input-field"
                                           value="${post.heading}"><br>
                                    <label for="description">Description:</label>
                                    <textarea id="default" name="description">${post.content}</textarea><br>
                                    <input type="hidden" name="action" value="editPost">
                                    <input type="hidden" name="postID" value="${post.postID}">
                                    <input type="hidden" name="userID" value="${user.userID}">
                                    <button type="submit" class="submit-button">Submit</button>
                                </form>
                            </div>
                        </div>
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
            <img src="${avatar}" alt="Author Avatar"
                 onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                 class="details-avatar">
            <p class="details-name">${fullName}</p>
            <div class="blog-details-date">${post.createdDate}</div>
        </div>
        <div class="blog-content">
            <img src="${post.imgURL}" alt="Post Image" class="blog-content-img">
            <p>${post.content}</p>
        </div>
    </c:if>
    <c:if test="${empty post}">
        <p>This post is not available.</p>
    </c:if>
</div>

<!-- COMMENT SECTION -->
<div class="comment-container">
    <h2 style="color: #333333; padding: 20px;">Bình luận</h2>

    <!-- Comment input form -->
    <c:choose>
        <c:when test="${user != null}">
            <form action="postDetails" method="POST">
                <div class="comment-box">
                    <textarea name="commentInput" rows="4" placeholder="Nhập bình luận của bạn..."
                              required></textarea><br><br>
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
                    <!-- Avatar và nội dung bình luận chính -->
                    <img src="${comment.userAvtURL}" alt="Avatar" class="comment-avt"
                         onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">
                    <div class="comment-content">
                        <div class="comment-header">
                            <div class="comment-author">${comment.userFullName}</div>
                            <div class="comment-time">${comment.createdDate}</div>
                        </div>
                        <div class="comment-text">${comment.content}</div>

                        <!-- Vote controls -->
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

                        <!-- Report -->
                        <c:if test="${user != null && user.userID != comment.userID}">
                            <button type="button" class="btn-report" data-bs-toggle="modal"
                                    data-bs-target="#commentReportModal" data-commentid="${comment.commentID}">
                                <i class="fas fa-flag"></i> Report
                            </button>
                        </c:if>

                        <!-- Sửa/Xoá -->
                        <div class="comment-options">
                            <c:if test="${user != null && user.userID == comment.userID}">
                                <a href="javascript:void(0);"
                                   class="edit-comment-btn"
                                   data-comment-id="${comment.commentID}"
                                   data-comment-content="${fn:escapeXml(comment.content)}">
                                    <i class="ti-pencil"></i>
                                </a>
                            </c:if>

                            <c:if test="${user != null && (user.userID == comment.userID || user.userID == post.userID)}">
                                <a href="javascript:void(0);" onclick="confirmDeleteComment(${comment.commentID})">
                                    <i class="ti-trash"></i>
                                </a>
                            </c:if>
                        </div>

                        <!-- Form trả lời -->
                        <c:if test="${not empty replyMap[comment.commentID] && fn:length(replyMap[comment.commentID]) > 0}">
                            <button class="view-reply-button"
                                    id="view-reply-button-${comment.commentID}"
                                    onclick="showReplies(${comment.commentID})">
                                View reply (${fn:length(replyMap[comment.commentID])})
                            </button>
                        </c:if>


                        <button class="btn btn-update btn-sm" onclick="showDiv(${comment.commentID})">Reply</button>

                        <div id="updateDiv${comment.commentID}" style="display: none;">
                            <c:choose>
                                <c:when test="${user != null}">
                                    <form method="POST" action="postDetails">
                                        <div class="comment-box">
                                            <textarea name="commentInput" rows="4"
                                                      placeholder="Nhập bình luận của bạn..."
                                                      required></textarea><br><br>
                                            <input type="hidden" name="postID" value="${post.postID}">
                                            <input type="hidden" name="parentID" value="${comment.commentID}">
                                            <input type="hidden" name="action" value="addComment">
                                            <input type="submit" value="Gửi bình luận" class="btn btn-primary">
                                        </div>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <p>Vui lòng <a href="login.jsp">đăng nhập</a> để bình luận.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>

                    <!-- Phần hiển thị replies -->
                        <c:if test="${not empty replyMap[comment.commentID]}">
                            <div id="reply-section${comment.commentID}" style="display: none;">
                                <ul class="comment-thread">
                                    <c:forEach var="reply" items="${replyMap[comment.commentID]}">
                                        <li>
                                            <div class="comment-content">
                                                <div class="comment-header">
                                                    <span class="comment-author">${reply.userFullName}</span>
                                                    <span class="comment-time">${reply.createdDate}</span>
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

                                                <button class="reply-button"
                                                        onclick="toggleReplyForm(${reply.commentID}, '${reply.userFullName}')">
                                                    Reply
                                                </button>
                                                <c:if test="${user != null && user.userID != reply.userID}">
                                                    <button type="button" class="btn-report"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#commentReportModal"
                                                            data-commentid="${reply.commentID}">
                                                        <i class="fas fa-flag"></i> Report
                                                    </button>
                                                </c:if>
                                                <div class="comment-options">
                                                    <c:if test="${user != null && user.userID == reply.userID}">
                                                        <a href="javascript:void(0);"
                                                           class="edit-comment-btn"
                                                           data-comment-id="${reply.commentID}"
                                                           data-comment-content="${fn:escapeXml(reply.content)}">
                                                            <i class="ti-pencil"></i>
                                                        </a>
                                                    </c:if>
                                                    <c:if test="${user != null && (user.userID == comment.userID || user.userID == post.userID)}">
                                                        <a href="javascript:void(0);" onclick="confirmDeleteComment(${reply.commentID})">
                                                            <i class="ti-trash"></i>
                                                        </a>
                                                    </c:if>
                                                </div>

                                                <div id="replyForm-${reply.commentID}" class="reply-form"
                                                     style="display:none;">
                                                    <form method="POST" action="postDetails">
                                                <textarea name="commentInput" rows="2" class="form-control" required>
@${reply.userFullName} </textarea>
                                                        <input type="hidden" name="postID" value="${post.postID}">
                                                        <input type="hidden" name="parentID" value="${reply.commentID}">
                                                        <input type="hidden" name="action" value="addComment">
                                                        <button type="submit" class="btn btn-primary btn-sm mt-1">Gửi
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
        <span class="close-modal">&times;</span>
        <h2>Chỉnh sửa bình luận</h2>
        <form id="edit-comment-form" action="postDetails" method="POST">
            <input type="hidden" id="edit-comment-id" name="commentID">
            <input type="hidden" name="postID" value="${post.postID}">
            <input type="hidden" name="action" value="editComment">
            <textarea id="edit-comment-content" name="commentContent" rows="4" required></textarea>
            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
        </form>
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
<c:import url="footer.jsp"/>

<script>
    // Các hàm xử lý modal cho phần edit post
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

    function confirmDelete(postID) {
        const confirmAction = confirm("Are you sure you want to delete this post?");
        if (confirmAction) {
            document.getElementById('postID').value = postID;
            document.getElementById('deleteFormPost').submit();
        }
    }

    const moreOptions = document.getElementById("more-options");
    if (moreOptions) {
        moreOptions.addEventListener("click", function (event) {
            event.stopPropagation();
            const dropdown = document.getElementById("dropdown-menu");
            dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
        });
    }

    const closeModalBtn = document.querySelector(".close-modal");
    if (closeModalBtn) {
        closeModalBtn.onclick = function () {
            document.getElementById("edit-comment-modal").style.display = "none";
        }
    }
</script>
<script>
    //script hiển thị div reply cmt
    function showDiv(commentID) {
        const div = document.getElementById(`updateDiv${commentID}`);
        if (div.style.display === 'none' || div.style.display === '') {
            div.style.display = 'block';
            // Focus on textarea
            const textarea = div.querySelector('textarea');
            if (textarea) {
                textarea.focus();
            }
        } else {
            div.style.display = 'none';
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

</script>
<script>
    // Initialize the comment report modal with proper data
    document.addEventListener('DOMContentLoaded', function () {
        // Get report modal and setup event handlers
        const commentReportModal = document.getElementById('commentReportModal');
        if (commentReportModal) {
            commentReportModal.addEventListener('show.bs.modal', function (event) {
                const button = event.relatedTarget;
                const commentID = button.getAttribute('data-commentid');
                document.getElementById('modal-comment-id').value = commentID;
                document.getElementById('modal-post-id').value = document.querySelector('input[name="postID"]').value;
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

    document.addEventListener("DOMContentLoaded", function () {
        const editButtons = document.querySelectorAll(".edit-comment-btn");

        editButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                const commentID = this.getAttribute("data-comment-id");
                const commentContent = this.getAttribute("data-comment-content");
                openEditCommentModal(commentID, commentContent);
            });
        });
    });

    function submitCommentReport() {
        // Check if user is logged in
        if (!isUserLoggedIn) {
            alert("⚠️ Please login to report comments.");
            return;
        }

        const commentID = document.getElementById('modal-comment-id').value;
        const reason = document.getElementById('commentReportReason').value;
        const postID = document.getElementById('modal-post-id').value;

        if (!reason || reason.trim() === '') {
            alert("Please provide a reason for your report");
            return;
        }

        // Submit the report via AJAX
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
                } else if (response.warning) {
                    if (confirm(response.message)) {
                        // Resubmit with confirmation
                        $.ajax({
                            url: 'postDetails',
                            type: 'POST',
                            dataType: 'json',
                            data: {
                                action: 'reportComment',
                                commentID: commentID,
                                reason: reason,
                                postID: postID,
                                confirmed: true
                            },
                            success: function (response) {
                                if (response.success) {
                                    alert("✅ Report submitted successfully!");
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
                                console.error("Report re-submission error:", status, error);
                                alert("⚠️ Error re-submitting report. Please try again later.");
                            }
                        });
                    }
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
</script>
<script>
    function renderComments(comments, parentElement) {
        comments.forEach(comment => {
            const div = document.createElement("div");
            div.classList.add("comment");
            div.innerHTML = `
            <p>${comment.userFullName}: ${comment.content}</p>
            <button onclick="showReplyBox(${comment.commentID})">Reply</button>
            <div id="replies-${comment.commentID}" class="replies"></div>
        `;
            parentElement.appendChild(div);
            renderComments(comment.replies, div.querySelector(`#replies-${comment.commentID}`));
        });
    }

    function toggleReplyForm(commentID, userName) {
        const form = document.getElementById("replyForm-" + commentID);
        if (!form) return;

        if (form.style.display === "none") {
            form.style.display = "block";
            const textarea = form.querySelector("textarea");
            if (textarea && (textarea.value.trim() === "" || !textarea.value.includes("@"))) {
                textarea.value = "@" + userName + " ";
            }
            textarea.focus();
            // Position cursor at the end of text
            textarea.selectionStart = textarea.selectionEnd = textarea.value.length;
        } else {
            form.style.display = "none";
        }
    }
</script>
</body>
</html>