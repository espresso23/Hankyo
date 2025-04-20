<%@ page import="model.User" %>
<%@ page import="dao.PostDAO" %>
<%@ page import="model.Post" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.UserDAO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<%
    User user = (User) session.getAttribute("user");
    PostDAO postDAO = new PostDAO();
    UserDAO userDAO = new UserDAO();
%>
<%@ page isELIgnored="false" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Hankyo Forum</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="asset/font/themify-icons/themify-icons.css"/>
    <link rel="stylesheet" href="asset/css/blog.css"/>
    <link rel="stylesheet" href="asset/css/modal.css"/>
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
    textarea {
        display: block; /* Đảm bảo textarea không bị ẩn */
    }
    .top-rated-section {
        margin-bottom: 30px;
        padding: 20px;
        background-color: #f8f9fa;
        border-radius: 8px;
    }

    .top-rated-section h3 {
        color: #343a40;
        margin-bottom: 20px;
        font-weight: bold;
    }

    .top-rated-posts {
        display: flex;
        gap: 20px;
        overflow-x: auto;
        padding-bottom: 10px;
    }

    .top-rated-card {
        min-width: 300px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        padding: 15px;
        transition: transform 0.3s;
    }

    .top-rated-card:hover {
        transform: translateY(-5px);
    }

    .top-rated-author {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }

    .top-rated-author img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin-right: 10px;
    }

    .top-rated-content h5 {
        font-size: 1.1rem;
        margin-bottom: 8px;
        color: #212529;
    }

    .top-rated-content p {
        font-size: 0.9rem;
        color: #6c757d;
        margin-bottom: 10px;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .top-rated-score {
        color: #ffc107;
        font-weight: bold;
        font-size: 0.9rem;
    }

    .modal {
        z-index: 1050 !important; /* đảm bảo cao hơn overlay khác */
    }

    .modal-backdrop {
        z-index: 1040 !important;
    }

    .post-container {
        display: flex;
        align-items: flex-start;
    }

    .post-content-link {
        flex: 1;
        padding-left: 12px;
    }

    .btn-report {
        background: none;
        border: none;
        color: #878A8C;
        padding: 4px 8px;
        font-size: 14px;
        border-radius: 4px;
    }

    .btn-report:hover {
        background-color: #f6f7f8;
        color: #ff4500;
    }

    .card-footer {
        display: flex;
        justify-content: flex-end; /* Căn phải cho các nút */
        background: none;
        border-top: 1px solid #eee;
        padding-top: 8px;
    }

    .card-footer button {
        margin-left: 8px; /* Đảm bảo khoảng cách giữa các nút */
    }

    .btn-comment {
        background-color: #728ca8;
        border: none;
        color: white;
        padding: 6px 12px;
        font-size: 14px;
        border-radius: 4px;
        display: flex;
        margin-right: auto;
    }

    .btn-comment i {
        margin-right: 5px;
    }

    .btn-comment:hover {
        background-color: #294c75;
    }


</style>
<body data-user-logged-in="${user != null}">
<!-- HEADER -->
<c:import url="header.jsp"/>

<!-- PAGE INFO -->

<!-- Search Form -->
<div class="search-container" style="margin: 20px auto; max-width: 800px;">
    <form class="app-search" action="blog" method="GET">
        <div class="input-group mb-3">
            <input type="text" class="form-control" name="searchQuery" placeholder="Search posts..."
                   value="${param.searchQuery}" aria-label="Search posts" aria-describedby="button-search">
            <button class="btn btn-primary" type="submit" id="button-search">
                <i class="fas fa-search"></i> Search
            </button>
        </div>
    </form>
</div>

<!-- Search Results Header -->
<c:if test="${not empty param.searchQuery}">
    <div class="search-results-header" style="margin: 20px 0; padding: 0 20px;">
        <h4>Search Results for: "${param.searchQuery}"</h4>
        <c:if test="${empty searchResults}">
            <p>No posts found matching your search.</p>
        </c:if>
    </div>
</c:if>


<!-- Display Search Results -->
<c:if test="${not empty searchResults}">
    <div class="search-results-container">
        <c:forEach var="post" items="${searchResults}">
            <!-- Reuse your existing post card structure -->
            <div class="card card-post content" style="width: 100%">
                <div class="post-container" style="display: flex;">
                    <a href="postDetails?postID=${post.getPostID()}" class="post-content-link"
                       style="flex: 1; text-decoration: none; color: inherit;">
                        <div class="card-post-author">
                            <img src="${post.getAvtUserImg() != null ? post.getAvtUserImg() : userDAO.getAvatarByUserId(post.getUserID())}"
                                 onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                                 alt="Author Avatar" class="author-post-avatar"/>
                            <p class="author_name author-post-name">${post.getUserFullName()}</p>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title card-title-post">${post.getHeading()}</h5>
                            <p class="">${post.getContent()}</p>
                        </div>
                    </a>
                </div>
                <div class="card-footer"
                     style="display: flex; justify-content: flex-end; background: none; border-top: 1px solid #eee; padding-top: 8px;">
                    <!-- Include your vote and comment buttons here -->
                    <div class="card-footer"
                         style="display: flex; justify-content: flex-end; background: none; border-top: 1px solid #eee; padding-top: 8px;">
                        <div class="vote-controls" data-post-id="${post.getPostID()}">
                            <button type="button" class="vote-btn upvote-btn" data-post-id="${post.getPostID()}">
                                <i class="fas fa-arrow-up"></i>
                            </button>
                            <span class="vote-score" data-post-id="${post.getPostID()}">${post.getScore()}</span>
                            <button type="button" class="vote-btn downvote-btn" data-post-id="${post.getPostID()}">
                                <i class="fas fa-arrow-down"></i>
                            </button>
                        </div>
                        <button type="button" class="btn btn-primary btn-comment"
                                onclick="location.href='postDetails?postID=${post.getPostID()}'">
                            <i class="fas fa-comment"></i> Comment (${post.getCommentCount()})
                        </button>

                        <!-- Nút Report -->
                        <button
                                type="button"
                                class="btn-report"
                                data-bs-toggle="modal"
                                data-bs-target="#avatarModal"
                                data-postid="${post.getPostID()}">
                            <i class="fas fa-flag"></i> Report
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>

<div id="page-info">
    <div class="page-info-more">
        <c:choose>
            <c:when test="${user != null}">
                <button id="openModalBtn" class="submit-button">Create post</button>
            </c:when>
            <c:otherwise>
                <button class="openModalBtn" onclick="alert('Please log in to create a post.');">Create post</button>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- SLIDER IMG -->

<!-- CONTENT -->
<div id="content">
    <!-- Top Rated Posts Section -->
    <div class="top-rated-section">
        <h3>Top Rated Posts</h3>
        <div class="top-rated-posts">
            <c:forEach var="topPost" items="${topRatedPosts}" end="2">
                <div class="top-rated-card">
                    <a href="postDetails?postID=${topPost.getPostID()}">
                        <div class="top-rated-author">
                            <img src="${topPost.getAvtUserImg() != null ? topPost.getAvtUserImg() : userDAO.getAvatarByUserId(topPost.getUserID())}"
                                 onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                                 alt="Author Avatar" class="author-post-avatar"/>
                            <span class="author-post-name">${topPost.getUserFullName()}</span>
                        </div>
                        <div class="top-rated-content">
                            <h5>${topPost.getHeading()}</h5>
                            <p>${topPost.getContent()}</p>
                            <div class="top-rated-score">
                                <span>Score: ${topPost.getScore()}</span>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Filter Section -->
    <div class="filter-section">
        <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="filterDropdown"
                    data-bs-toggle="dropdown" aria-expanded="false">
                Lọc bài viết
            </button>
            <ul class="dropdown-menu" aria-labelledby="filterDropdown">
                <li><a class="dropdown-item" href="blog?filter=newest">Mới nhất</a></li>
                <li><a class="dropdown-item" href="blog?filter=oldest">Cũ nhất</a></li>
                <li><a class="dropdown-item" href="blog?filter=toprated">Hay nhất</a></li>
            </ul>
        </div>
    </div>
    <c:choose>
        <c:when test="${not empty postList}">
            <div class="post-row content-load">
                <c:forEach var="post" items="${postList}">
                    <div class="card card-post content" style="width: 100%">
                        <!-- Post flex container to hold vote controls + content -->
                        <div class="post-container" style="display: flex;">
                            <!-- Main content as a clickable area -->
                            <a href="postDetails?postID=${post.getPostID()}" class="post-content-link"
                               style="flex: 1; text-decoration: none; color: inherit;">
                                <div class="card-post-author">
                                    <img src="${post.getAvtUserImg() != null ? post.getAvtUserImg() : userDAO.getAvatarByUserId(post.getUserID())}"
                                         onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                                         alt="Author Avatar" class="author-post-avatar"/>
                                    <p class="author_name author-post-name">${post.getUserFullName()}</p>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title card-title-post">${post.getHeading()}</h5>
                                    <p class="">${post.getContent()}</p>
                                </div>
                            </a>
                        </div>

                        <!-- Footer with report button -->
                        <div class="card-footer"
                             style="display: flex; justify-content: flex-end; background: none; border-top: 1px solid #eee; padding-top: 8px;">
                            <div class="vote-controls" data-post-id="${post.getPostID()}">
                                <button type="button" class="vote-btn upvote-btn" data-post-id="${post.getPostID()}">
                                    <i class="fas fa-arrow-up"></i>
                                </button>
                                <span class="vote-score" data-post-id="${post.getPostID()}">${post.getScore()}</span>
                                <button type="button" class="vote-btn downvote-btn" data-post-id="${post.getPostID()}">
                                    <i class="fas fa-arrow-down"></i>
                                </button>
                            </div>
                            <button type="button" class="btn btn-primary btn-comment"
                                    onclick="location.href='postDetails?postID=${post.getPostID()}'">
                                <i class="fas fa-comment"></i> Comment (${post.getCommentCount()})
                            </button>

                            <!-- Nút Report -->
                            <button
                                    type="button"
                                    class="btn-report"
                                    data-bs-toggle="modal"
                                    data-bs-target="#avatarModal"
                                    data-postid="${post.getPostID()}">
                                <i class="fas fa-flag"></i> Report
                            </button>
                        </div>
                    </div>

                    <!-- Modal stays outside the card -->

                </c:forEach>
            </div>
            <a href="#" id="loadMore">Load More</a>
        </c:when>
        <c:otherwise>
            <div class="no-posts-message">
                <h3>No posts available yet</h3>
                <p>Be the first to create a post!</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<!-- Modal stays outside the card -->
<div class="modal fade" id="avatarModal" tabindex="-1" aria-labelledby="avatarModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="avatarModalLabel">Report Post</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="reportForm">
                    <div class="mb-3">
                        <label for="reportReason" class="form-label">Reason:</label>
                        <textarea class="form-control" id="reportReason" name="reason"
                                  required></textarea>
                    </div>
                    <input type="hidden" id="modal-post-id" name="postID" value="">
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">Submit Report</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<c:import url="footer.jsp"/>

<!-- Modal Structure -->
<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <form action="postDetails" method="POST" enctype="multipart/form-data">
            <h2>Add Blog Post</h2>
            <label for="imgPost">Picture Cover:</label>
            <input id="imgPost" name="imgPost" type="file" class="input-field" required/><br>
            <label for="title">Heading:</label>
            <input name="title" id="title" type="text" required class="input-field"/><br>
            <label for="description">Description:</label>
            <input type="hidden" id="description" name="userID" value="${user.getUserID()}"/>
            <textarea id="default" name="description"></textarea><br>
            <input type="hidden" name="action" value="addPost">
            <button type="submit" class="submit-button">Submit</button>
        </form>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        let loadMoreButton = document.getElementById("loadMore");
        if (loadMoreButton) {
            let posts = document.querySelectorAll(".post-row .content");
            let currentPosts = 6;

            // Hide posts beyond the initial display count
            for (let i = currentPosts; i < posts.length; i++) {
                posts[i].style.display = "none";
            }

            // Hide the load more button if there aren't enough posts
            if (posts.length <= currentPosts) {
                loadMoreButton.style.display = "none";
            }

            loadMoreButton.addEventListener("click", function (e) {
                e.preventDefault();
                let nextPosts = currentPosts + 6;
                for (let i = currentPosts; i < nextPosts && i < posts.length; i++) {
                    posts[i].style.display = "flex";
                }
                currentPosts += 6;

                if (currentPosts >= posts.length) {
                    loadMoreButton.style.display = "none";
                }
            });
        }
    });

    var modal = document.getElementById("myModal");
    var btn = document.getElementById("openModalBtn");
    var span = document.getElementsByClassName("close")[0];

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

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
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
<script>
    const isUserLoggedIn = <%= (user != null) %>;

    // Add this code to ensure the post ID is properly passed to the modal
    $('#avatarModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const postID = button.data('postid');
        $('#modal-post-id').val(postID);
    });

    $('#reportForm').on('submit', function (e) {
        e.preventDefault();
        if (!isUserLoggedIn) {
            alert("⚠️ Vui lòng đăng nhập để report.");
            return;
        }

        const postID = $('#modal-post-id').val();
        const reason = $('#reportReason').val();

        if (!reason) {
            alert("Please enter a reason for reporting");
            return;
        }

        $.ajax({
            url: 'blog',
            type: 'POST',
            dataType: 'json',
            data: {
                action: 'reportPost',
                postID: postID,
                reason: reason
            },
            success: function (response) {
                if (response.success) {
                    alert("✅ Report submitted successfully!");
                    $('#avatarModal').modal('hide');
                    $('#reportForm')[0].reset();
                } else if (response.warning) {
                    if (confirm(response.message)) {
                        // User confirmed reporting their own post
                        submitReport(postID, reason, true);
                    }
                } else {
                    alert("❌ Failed to submit report: " + (response.error || "Unknown error"));
                }
            },
            error: function (xhr, status, error) {
                alert("⚠️ Error submitting report. Please try again later.");
                console.error("Report submission error:", status, error);
            }
        });
    });

    // Helper function for confirmed reports
    function submitReport(postID, reason, confirmed) {
        $.ajax({
            url: 'blog',
            type: 'POST',
            dataType: 'json',
            data: {
                action: 'reportPost',
                postID: postID,
                reason: reason,
                confirmed: confirmed
            },
            success: function (response) {
                if (response.success) {
                    alert("✅ Report submitted successfully!");
                    $('#avatarModal').modal('hide');
                    $('#reportForm')[0].reset();
                } else {
                    alert("❌ Failed to submit confirmed report: " + (response.error || "Unknown error"));
                }
            },
            error: function () {
                alert("⚠️ Error submitting confirmed report.");
            }
        });
    }
</script>

<script>
    $(document).ready(function() {
        // Initialize vote buttons
        $('.vote-btn').click(function() {
            const postId = $(this).data('post-id');
            const isUpvote = $(this).hasClass('upvote-btn');
            const voteAction = isUpvote ? 'upvote' : 'downvote';

            const voteControls = $(this).closest('.vote-controls');
            const scoreElement = voteControls.find('.vote-score');
            const originalScore = scoreElement.text();

            // Show loading indicator
            scoreElement.html('<i class="fas fa-spinner fa-spin"></i>');

            // Disable buttons during request
            voteControls.find('.vote-btn').prop('disabled', true);

            $.ajax({
                url: 'blog',
                type: 'POST',
                data: {
                    action: 'vote',
                    postID: postId,
                    voteAction: voteAction
                },
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        // Update score display
                        scoreElement.text(response.newScore);

                        // Update button states
                        updateVoteButtons(voteControls, response.currentUserVote);
                    } else {
                        alert(response.message || 'Failed to process vote');
                        scoreElement.text(originalScore);
                    }
                },
                error: function(xhr, status, error) {
                    alert('Error processing vote: ' + error);
                    scoreElement.text(originalScore);
                },
                complete: function() {
                    voteControls.find('.vote-btn').prop('disabled', false);
                }
            });
        });

        // Load initial vote states for logged in user
        if ($('body').data('user-logged-in')) {
            loadInitialVoteStates();
        }
    });

    function updateVoteButtons(voteControls, currentVote) {
        // Reset all buttons
        voteControls.find('.vote-btn').removeClass('active text-primary text-danger');

        if (currentVote === 1) {
            voteControls.find('.upvote-btn').addClass('active text-primary');
        } else if (currentVote === -1) {
            voteControls.find('.downvote-btn').addClass('active text-danger');
        }
    }

    function loadInitialVoteStates() {
        const postIds = [];
        $('.vote-controls').each(function() {
            postIds.push($(this).data('post-id'));
        });

        if (postIds.length === 0) return;

        $.ajax({
            url: 'blog',
            type: 'POST',
            data: {
                action: 'getUserVotes',
                postIDs: postIds
            },
            dataType: 'json',
            success: function(response) {
                $.each(response, function(postId, voteType) {
                    const voteControls = $(`.vote-controls[data-post-id="${postId}"]`);
                    updateVoteButtons(voteControls, voteType);
                });
            },
            error: function(xhr, status, error) {
                console.error('Failed to load vote states:', error);
            }
        });
    }
</script>
<%--<script>--%>
<%--    // Global variables--%>
<%--    // Global variables--%>
<%--    let userPostVotes = {};--%>

<%--    document.addEventListener('DOMContentLoaded', function() {--%>
<%--        // Check if user is logged in by using data attribute on body tag--%>
<%--        const isUserLoggedIn = document.body.getAttribute('data-user-logged-in') === 'true';--%>

<%--        // Add click listeners to all vote buttons--%>
<%--        document.querySelectorAll('.upvote-btn').forEach(button => {--%>
<%--            button.addEventListener('click', function() {--%>
<%--                handlePostVote(this, 1);--%>
<%--            });--%>
<%--        });--%>

<%--        document.querySelectorAll('.downvote-btn').forEach(button => {--%>
<%--            button.addEventListener('click', function() {--%>
<%--                handlePostVote(this, -1);--%>
<%--            });--%>
<%--        });--%>

<%--        // Load user's votes if logged in--%>
<%--        if (isUserLoggedIn) {--%>
<%--            loadUserVotes();--%>
<%--        }--%>
<%--    });--%>

<%--    function handlePostVote(button, voteType) {--%>
<%--        const postId = button.getAttribute('data-post-id');--%>
<%--        const scoreElement = document.querySelector(`.vote-score[data-post-id="${postId}"]`);--%>

<%--        // Check if user is logged in using data attribute--%>
<%--        const isUserLoggedIn = document.body.getAttribute('data-user-logged-in') === 'true';--%>

<%--        if (!isUserLoggedIn) {--%>
<%--            alert("Please login to vote!");--%>
<%--            return;--%>
<%--        }--%>

<%--        // Show loading indicator--%>
<%--        const originalScore = scoreElement.textContent;--%>
<%--        scoreElement.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';--%>

<%--        // Send AJAX request--%>
<%--        $.ajax({--%>
<%--            url: 'blog',--%>
<%--            type: 'POST',--%>
<%--            data: {--%>
<%--                action: "updatePostVote",--%>
<%--                postID: postId,--%>
<%--                voteType: voteType--%>
<%--            },--%>
<%--            dataType: "json",--%>
<%--            success: function(response) {--%>
<%--                if (response.success) {--%>
<%--                    // Update displayed score--%>
<%--                    scoreElement.textContent = response.score;--%>

<%--                    // Update UI--%>
<%--                    const upvoteBtn = document.querySelector(`.upvote-btn[data-post-id="${postId}"]`);--%>
<%--                    const downvoteBtn = document.querySelector(`.downvote-btn[data-post-id="${postId}"]`);--%>

<%--                    // Remove active class from both buttons--%>
<%--                    upvoteBtn.classList.remove('active');--%>
<%--                    downvoteBtn.classList.remove('active');--%>

<%--                    // Add active class to selected button if there's a vote--%>
<%--                    if (response.voteType === 1) {--%>
<%--                        upvoteBtn.classList.add('active');--%>
<%--                    } else if (response.voteType === -1) {--%>
<%--                        downvoteBtn.classList.add('active');--%>
<%--                    }--%>

<%--                    // Update vote state--%>
<%--                    userPostVotes[postId] = response.voteType;--%>
<%--                } else {--%>
<%--                    // Restore original score on error--%>
<%--                    scoreElement.textContent = originalScore;--%>
<%--                    console.error("Vote update failed on server side");--%>
<%--                    alert(response.error || "Vote update failed");--%>
<%--                }--%>
<%--            },--%>
<%--            error: function(xhr, status, error) {--%>
<%--                // Restore original score on error--%>
<%--                scoreElement.textContent = originalScore;--%>

<%--                console.error("Vote update failed:", status, error);--%>
<%--                if (xhr.status === 401) {--%>
<%--                    alert("Please login to vote!");--%>
<%--                } else {--%>
<%--                    alert("An error occurred while processing your vote");--%>
<%--                }--%>
<%--            }--%>
<%--        });--%>
<%--    }--%>

<%--    function loadUserVotes() {--%>
<%--        // Get all post IDs on the page from vote-score elements--%>
<%--        const scoreElements = document.querySelectorAll('.vote-score[data-post-id]');--%>
<%--        const postIDs = Array.from(scoreElements).map(el => el.getAttribute('data-post-id'));--%>

<%--        if (postIDs.length === 0) return;--%>

<%--        // Create form data to send post IDs as array--%>
<%--        const formData = new FormData();--%>
<%--        formData.append('action', 'loadUserPostVotes');--%>

<%--        // Add each post ID to the formData--%>
<%--        postIDs.forEach(id => {--%>
<%--            formData.append('postIDs[]', id);--%>
<%--        });--%>

<%--        // Send AJAX request--%>
<%--        $.ajax({--%>
<%--            url: 'blog',--%>
<%--            type: 'POST',--%>
<%--            data: formData,--%>
<%--            processData: false,--%>
<%--            contentType: false,--%>
<%--            dataType: "json",--%>
<%--            success: function(userVoteData) {--%>
<%--                // Save vote state--%>
<%--                userPostVotes = userVoteData;--%>

<%--                // Update UI for each post--%>
<%--                Object.keys(userVoteData).forEach(postId => {--%>
<%--                    const voteType = userVoteData[postId];--%>
<%--                    const upvoteBtn = document.querySelector(`.upvote-btn[data-post-id="${postId}"]`);--%>
<%--                    const downvoteBtn = document.querySelector(`.downvote-btn[data-post-id="${postId}"]`);--%>

<%--                    if (upvoteBtn && downvoteBtn) {--%>
<%--                        // Reset previous state--%>
<%--                        upvoteBtn.classList.remove('active');--%>
<%--                        downvoteBtn.classList.remove('active');--%>

<%--                        // Set new state--%>
<%--                        if (voteType === 1) {--%>
<%--                            upvoteBtn.classList.add('active');--%>
<%--                        } else if (voteType === -1) {--%>
<%--                            downvoteBtn.classList.add('active');--%>
<%--                        }--%>
<%--                    }--%>
<%--                });--%>
<%--            },--%>
<%--            error: function(xhr, status, error) {--%>
<%--                console.error("Failed to load vote data:", status, error);--%>
<%--            }--%>
<%--        });--%>
<%--    }--%>
<%--</script>--%>
<%--search--%>
<script>
    $(document).ready(function () {
        // Live search functionality
        $('input[name="searchQuery"]').on('input', function () {
            const query = $(this).val().trim();

            if (query.length >= 2) { // Only search when at least 2 characters are entered
                $.get('blog', {searchQuery: query}, function (data) {
                });
            }
        });
    });
</script>
</body>
</html>