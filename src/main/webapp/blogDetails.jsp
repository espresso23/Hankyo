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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          crossorigin="anonymous">
    <link rel="stylesheet" href="asset/font/themify-icons/themify-icons.css">
    <link rel="stylesheet" href="asset/css/blogdetails.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<style>
    body {
        margin: 0;
        font-family: Arial, sans-serif;
        background-image: url('asset/png/background/background-2.png');
        background-size: cover;
        background-position: left;
    }
    .view-reply-button {
        font-size: 16px; /* Kích thước chữ vừa phải để dễ đọc */
        padding: 3px 2px; /* Tạo khoảng cách bên trong nút */
        border: 2px solid #9f5863;; /* Đường viền màu xanh dương */
        border-radius: 25px; /* Bo góc để nút mềm mại */
        background-color: #f8f9fa; /* Màu nền nhẹ nhàng */
        color: #9f5863; /* Màu chữ xanh dương */
        cursor: pointer; /* Thêm con trỏ chuột khi hover */
        transition: all 0.3s ease; /* Thêm hiệu ứng mượt mà khi thay đổi */
        font-weight: 500; /* Làm cho chữ đậm hơn */
        text-align: center; /* Căn giữa nội dung trong nút */
        display: inline-block; /* Đảm bảo nút không chiếm quá nhiều không gian */
        margin: 5px 0; /* Tạo khoảng cách từ các phần tử xung quanh */
    }

    .view-reply-button:hover {
        background-color: pink; /* Màu nền đổi khi hover */
        color: #fff; /* Đổi màu chữ thành trắng khi hover */
        border-color: #9f5863; /* Đổi màu viền khi hover */
    }

    .view-reply-button:active {
        background-color: pink; /* Màu nền khi nhấn */
        border-color: #9f5863; /* Đổi màu viền khi nhấn */
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
                                    <input name="title" id="title" type="text" required class="input-field" value="${post.heading}"><br>
                                    <label for="description">Description:</label>
                                    <textarea id="default" name="description">${post.content}</textarea><br>
                                    <input type="hidden" name="action" value="editPost">
                                    <input type="hidden" name="postID" value="${post.postID}">
                                    <input type="hidden" name="userID" value="${user.userID}">
                                    <button type="submit" class="submit-button">Submit</button>
                                </form>
                            </div>
                        </div>
                        <a href="#" id="delete" onclick="confirmDelete(${post.getPostID()})"><i class="ti-trash"> Delete</i></a>
                    </div>
                </div>
                <!-- Hidden form for deletion -->
                <form id="deleteFormPost" action="postDetails" method="POST" style="display: none;">
                    <input type="hidden" name="action" value="deletePost">
                    <input type="hidden" name="postID" value="${post.getPostID()}">
                </form>
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
                <c:set var="replyCount" value="0" />
                <c:forEach var="entry" items="${replyMap}">
                    <c:if test="${comment.commentID == entry.key}">
                        <c:set var="replyCount" value="${fn:length(entry.value)}" />
                    </c:if>
                </c:forEach>

                <div class="comment-section" id="comment-${comment.commentID}">
                    <img src="${comment.userAvtURL}" alt="Avatar" class="comment-avt"
                         onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">
                    <div class="comment-content">
                        <div class="comment-header">
                            <div class="comment-author">${comment.userFullName}</div>
                            <div class="comment-time">${comment.createdDate}</div>
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

                        <c:forEach var="entry" items="${replyMap}">
                            <c:if test="${comment.commentID == entry.key}">
                                <div id="reply-section${comment.commentID}" style="display: none;">
                                    <ul>
                                        <c:forEach var="reply" items="${entry.value}">
                                            <li>
                                                <div>
                                                    <img src="${reply.userAvtURL}" alt="Avatar" class="comment-avt"
                                                         onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">
                                                    <div class="comment-content">
                                                        <div class="comment-header">
                                                            <div class="comment-author">${reply.userFullName}</div>
                                                            <div class="comment-time">${reply.createdDate}</div>
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
                                                    </div>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>

                    <button class="view-reply-button" id="view-reply-button-${comment.commentID}" onclick="showReplies(${comment.commentID})">
                        View reply (${replyCount})
                    </button>
                    <button name="action" class="btn btn-update btn-sm" value="reply"
                            onclick="showDiv(${comment.commentID})">Reply
                    </button>
                    <div id="updateDiv${comment.commentID}" style="display: none;">
                        <c:choose>
                            <c:when test="${user != null}">
                                <form style="display: inline" action="postDetails" method="POST">
                                    <div class="comment-box">
                                        <textarea name="commentInput" rows="4" placeholder="Nhập bình luận của bạn..."
                                                  required></textarea><br><br>
                                        <input type="hidden" name="postID" value="${post.postID}">
                                        <input type="hidden" name="parentID" value="${comment.commentID}">
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
                    </div>

                    <c:if test="${user != null && (user.userID == post.userID || user.userID == comment.userID)}">
                        <div class="comment-options">
                            <a href="javascript:void(0);" class="edit-comment-option"
                               onclick="openEditCommentModal(${comment.commentID}, '<c:out value="${comment.content}" escapeXml="true"/>')">
                                <i class="ti-pencil"></i>
                            </a>
                            <a href="javascript:void(0);" class="delete-comment-option"
                               onclick="confirmDeleteComment(${comment.commentID})">
                                <i class="ti-trash"></i>
                            </a>
                        </div>
                    </c:if>
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
<c:import url="footer.jsp"/>

<script>
    // Các hàm xử lý modal giữ nguyên
    const modal = document.getElementById("myModal");
    const btn = document.getElementById("openModalBtn");
    const span = document.getElementsByClassName("close")[0];

    btn.onclick = function () {
        modal.style.display = "block";
    }

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    function confirmDelete(postID) {
        const confirmAction = confirm("Are you sure you want to delete this post?");
        if (confirmAction) {
            document.getElementById('postID').value = postID;
            document.getElementById('deleteFormPost').submit();
        }
    }

    document.getElementById("more-options").addEventListener("click", function (event) {
        event.stopPropagation();
        const dropdown = document.getElementById("dropdown-menu");
        dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    });

    window.addEventListener("click", function (event) {
        const dropdown = document.getElementById("dropdown-menu");
        if (dropdown.style.display === "block" && !event.target.closest('.dropdown-container')) {
            dropdown.style.display = "none";
        }
    });

    document.querySelector(".close-modal").onclick = function () {
        document.getElementById("edit-comment-modal").style.display = "none";
    }
</script>

<script>
    //script hiển thị div reply cmt
    function showDiv(commentID) {
        id = "updateDiv" + commentID;
        var updateDiv = document.getElementById(id);

        // Kiểm tra trạng thái hiển thị của div
        if (updateDiv.style.display === "none") {
            updateDiv.style.display = "block"; // Hiển thị div
        } else {
            updateDiv.style.display = "none"; // Ẩn div
        }
    }

    function showReplies(commentID) {
        id = "reply-section" + commentID;
        var replyDiv = document.getElementById(id);
        var button = document.getElementById("view-reply-button-" + commentID);

        // Kiểm tra trạng thái hiển thị của div
        if (replyDiv.style.display === "none") {
            replyDiv.style.display = "block"; // Hiển thị div
            button.innerHTML = "Hide replies";
        } else {
            replyDiv.style.display = "none"; // Ẩn div
            button.innerHTML = "View replies (${fn:length(entry.value)})";
        }
    }
</script>

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
            favs: {title: 'menu', items: 'code visualaid | searchreplace | emoticons'}
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
                    cb(blobInfo.blobUri(), {title: file.name});
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