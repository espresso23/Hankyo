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
    /* Base Styles */
    html {
        scroll-behavior: smooth;
        color: #777;
        line-height: 1.8;
    }

    body {
        margin: 0;
        font-family: 'Quicksand', 'Segoe UI', Arial, sans-serif;
        background-image: url('asset/png/background/background-2.png');
        background-size: cover;
        background-position: left;
        background-repeat: no-repeat;
        color: #4a5c6c;
    }

    /* Layout Structure */
    #page-info {
        padding: 30px 0;
        margin: 0 auto;
        max-width: 1200px;
    }

    #content {
        padding: 60px 12%;
        max-width: 1400px;
        margin: 0 auto;
    }

    .post-row {
        display: flex;
        gap: 2rem;
        justify-content: center;
        padding-bottom: 30px;
    }

    /* Create Post Card */
    .create-post-card {
        display: flex;
        align-items: center;
        background: linear-gradient(135deg, #e3f2fd 60%, #fce4ec 100%);
        border-radius: 20px;
        padding: 22px 28px;
        box-shadow: 0 4px 18px rgba(160, 180, 200, 0.10);
        cursor: pointer;
        max-width: 1200px;
        margin: 40px auto;
        transition: all 0.3s ease;
        border: 1px solid #e0e9f0;
    }

    .create-post-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 32px rgba(160, 180, 200, 0.18);
        border-color: #b2dfdb;
    }

    .create-post-avatar {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        object-fit: cover;
        margin-right: 16px;
        border: 2px solid #b2dfdb;
        background: #fff;
    }

    .create-post-input {
        flex: 1;
        border: none;
        background: #f8fbfd;
        font-size: 18px;
        padding: 16px 24px;
        border-radius: 24px;
        color: #444;
        font-weight: 600;
        box-shadow: 0 2px 8px #e1bee7;
        transition: all 0.3s ease;
    }

    .create-post-input:focus {
        outline: none;
        background: #fff;
        box-shadow: 0 0 0 2px #b2dfdb;
    }

    .create-post-input::placeholder {
        color: #a8a8a8;
    }

    /* For logged out users */
    .openModalBtn {
        width: 100%;
        padding: 16px;
        border: none;
        border-radius: 16px;
        background-color: #ff9ff3;
        color: white;
        font-size: 16px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 12px rgba(255, 159, 243, 0.2);
    }

    .openModalBtn:hover {
        background-color: #ff7eb9;
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(255, 126, 185, 0.3);
    }

    /* Post Cards */
    /* Cập nhật phần card post */
    .card-post {
        border-radius: 12px;
        margin-bottom: 18px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        background: #fff;
        display: flex;
        flex-direction: column;
        padding: 0;
        border: 1px solid #e4e6eb;
        overflow: hidden; /* Quan trọng: ngăn nội dung tràn ra ngoài */
        width: 100%;
    }

    /* Header card */
    .card-post-author {
        display: flex;
        align-items: center;
        padding: 14px 18px;
        border-bottom: 1px solid #f0f2f5;
        background: none;
    }

    .author-post-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        object-fit: cover;
        margin-right: 12px;
        border: 1.5px solid #e4e6eb;
    }

    .author-post-info {
        display: flex;
        flex-direction: column;
        flex-grow: 1;
        min-width: 0; /* Quan trọng: ngăn text tràn */
    }

    .author-post-name {
        font-size: 15px;
        font-weight: 700;
        color: #222;
        margin: 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .author-post-time {
        font-size: 13px;
        color: #888;
        margin: 2px 0 0 0;
    }

    /* Body card */
    .card-body {
        padding: 12px 18px;
        margin: 0;
        word-wrap: break-word; /* Đảm bảo text dài tự xuống dòng */
        overflow-wrap: break-word;
    }

    .card-title-post {
        font-size: 18px;
        font-weight: 700;
        color: #1877f2;
        margin-bottom: 8px;
        margin-top: 0;
        line-height: 1.4;
    }

    .card-text {
        font-size: 15px;
        color: #050505;
        margin-bottom: 12px;
        line-height: 1.6;
        white-space: pre-line; /* Giữ nguyên định dạng xuống dòng */
    }

    /* Footer card */
    .card-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        background: none;
        border-top: 1px solid #f0f2f5;
        padding: 10px 18px;
    }

    .vote-controls {
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .btn-comment {
        font-size: 14px;
        border-radius: 8px;
        padding: 6px 12px;
        background: #f0f2f5;
        color: #65676b;
        border: none;
        transition: all 0.2s;
        display: flex;
        align-items: center;
        gap: 6px;
    }

    .btn-comment:hover {
        background: #e4e6eb;
    }

    .card-title-post,
    .card-text,
    .comment-text,
    .author-post-name,
    .author-post-time {
        color: #000 !important;
    }

    /* Thu nhỏ ảnh trong bài post */
    .card-body img {
        max-width: 320px;       /* Giới hạn chiều rộng */
        max-height: 220px;      /* Giới hạn chiều cao */
        width: auto;            /* Tự động điều chỉnh */
        height: auto;           /* Tự động điều chỉnh */
        border-radius: 10px;    /* Bo góc */
        margin: 8px 0;          /* Khoảng cách */
        object-fit: contain;    /* Giữ tỷ lệ ảnh */
        box-shadow: 0 2px 8px rgba(0,0,0,0.08); /* Đổ bóng nhẹ */
    }

    /* Responsive */
    @media (max-width: 768px) {
        .card-body {
            padding: 10px 15px;
        }

        .card-footer {
            padding: 8px 15px;
        }

        .card-title-post {
            font-size: 16px;
        }

        .card-text {
            font-size: 14px;
        }
    }

    /* Card Footer */
    .card-footer {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        background: none;
        border-top: 1px solid #f0f2f5;
        padding: 8px 18px 10px 70px;
        gap: 12px;
    }

    .btn-comment, .btn-report {
        font-size: 14px;
        border-radius: 8px;
        padding: 6px 14px;
        background: #f0f2f5;
        color: #65676b;
        border: none;
        margin-right: 8px;
        transition: all 0.2s;
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 5px;
    }

    .btn-comment {
        background: linear-gradient(90deg, #b2dfdb 60%, #fce4ec 100%);
        color: #4a5c6c;
        font-weight: 600;
    }

    .btn-comment:hover, .btn-report:hover {
        background: #e4e6eb;
        color: #1877f2;
    }

    .btn-report {
        background: linear-gradient(90deg, #fce4ec 60%, #e3f2fd 100%);
        color: #f06292;
    }

    .btn-report:hover {
        background: #f8bbd0;
        color: #fff;
    }

    /* Vote Controls */
    .vote-controls {
        display: flex;
        align-items: center;
        gap: 4px;
        margin-right: auto;
    }

    .vote-btn {
        background: none;
        border: none;
        cursor: pointer;
        color: #65676b;
        font-size: 16px;
        padding: 0 2px;
        transition: color 0.2s;
    }

    .vote-btn.active {
        color: #1877f2;
    }

    .vote-btn.upvote-btn.active {
        color: #4CAF50;
    }

    .vote-btn.downvote-btn.active {
        color: #F44336;
    }

    .vote-score {
        font-weight: 600;
        min-width: 20px;
        text-align: center;
    }

    /* Load More Button */
    #loadMore {
        width: 200px;
        color: #CCB2B0;
        display: block;
        text-align: center;
        margin: 20px auto;
        padding: 10px;
        border-radius: 10px;
        border: 1px solid #CCB2B0;
        background-color: white;
        transition: all 0.3s;
        text-decoration: none;
    }

    #loadMore:hover {
        color: #DF808F;
        border: 1px solid #DF808F;
        transform: scale(1.05);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    }

    .noContent {
        color: #000;
        background-color: transparent;
        pointer-events: none;
        border: 1px solid #97989f;
    }

    /* Top Rated Posts */
    .top-rated-section {
        margin: 30px auto;
        max-width: 1200px;
        padding: 0 20px;
    }

    .top-rated-section h3 {
        color: #a8c1d1;
        font-size: 24px;
        font-weight: 700;
        margin-bottom: 25px;
        padding-bottom: 8px;
        border-bottom: 1.5px dashed #e0e9f0;
        text-align: center;
        letter-spacing: 1px;
    }

    .top-rated-posts {
        display: flex;
        justify-content: space-between;
        gap: 25px;
    }

    .top-rated-card {
        flex: 1;
        min-width: 0;
        background: linear-gradient(135deg, #f8fbfd 60%, #fce4ec 100%);
        border-radius: 18px;
        overflow: hidden;
        box-shadow: 0 4px 18px rgba(160, 180, 200, 0.10);
        transition: all 0.25s;
        border: 1.5px solid #e0e9f0;
    }

    .top-rated-card:hover {
        transform: translateY(-5px) scale(1.02);
        box-shadow: 0 8px 32px rgba(160, 180, 200, 0.18);
        border-color: #b2dfdb;
    }

    .top-rated-card a {
        text-decoration: none;
        color: inherit;
        display: block;
        height: 100%;
    }

    .top-rated-author {
        display: flex;
        align-items: center;
        padding: 18px;
        background: linear-gradient(90deg, #e3f2fd 60%, #fce4ec 100%);
        border-bottom: 1px solid #eef5fb;
    }

    .top-rated-content {
        padding: 18px;
    }

    .top-rated-content h5 {
        color: #4a5c6c;
        font-size: 18px;
        font-weight: 700;
        margin-bottom: 12px;
        line-height: 1.4;
    }

    .top-rated-content p {
        color: #7a8a99;
        font-size: 15px;
        line-height: 1.6;
        margin-bottom: 16px;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .top-rated-score {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 8px 0;
        background: #e3f2fd;
        border-top: 1px solid #eef5fb;
        border-radius: 0 0 18px 18px;
    }

    .top-rated-score span {
        font-size: 15px;
        font-weight: 600;
        color: #7a9cc6;
    }

    /* Search Styles */
    .app-search {
        margin: 20px auto;
        max-width: 700px;
        padding: 0 20px;
    }

    .app-search .input-group {
        box-shadow: 0 2px 8px rgba(255, 105, 180, 0.15);
        border-radius: 24px;
        overflow: hidden;
        transition: all 0.3s ease;
    }

    .app-search .input-group:hover {
        box-shadow: 0 4px 12px rgba(255, 105, 180, 0.2);
    }

    .app-search .form-control {
        border: 1px solid #ffc0cb;
        border-right: none;
        padding: 10px 18px;
        font-size: 15px;
        color: #555;
        background-color: #fff;
        border-radius: 24px 0 0 24px;
    }

    .app-search .form-control:focus {
        border-color: #e1becf;
        box-shadow: none;
        background-color: white;
    }

    .app-search .btn-primary {
        background-color: #e472ad;
        border-color: #806271;
        border-radius: 0 24px 24px 0;
        padding: 0 16px;
        font-size: 14px;
        transition: all 0.2s ease;
    }

    .app-search .btn-primary:hover {
        background-color: #cc527b;
        border-color: #ff4785;
    }

    .search-results-header {
        background-color: #fff5f7;
        padding: 12px 20px;
        border-radius: 12px;
        margin: 15px auto 20px;
        max-width: 700px;
        border-left: 4px solid #ff69b4;
    }

    .search-results-header h4 {
        color: #ff4785;
        font-weight: 600;
        margin-bottom: 8px;
        font-size: 18px;
    }

    .search-results-header p {
        color: #666;
        font-size: 14px;
        margin: 0;
    }

    .search-results-container {
        display: flex;
        flex-direction: column;
        gap: 16px;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    /* Filter Section */
    .filter-section {
        margin-bottom: 20px;
        display: flex;
        justify-content: flex-end;
    }

    .dropdown-menu {
        min-width: 150px;
    }

    /* No Posts Message */
    .no-posts-message {
        background: #fce4ec;
        color: #7a8a99;
        border-radius: 18px;
        padding: 32px 0;
        text-align: center;
        margin: 32px 0;
        font-size: 20px;
        font-weight: 600;
        box-shadow: 0 2px 8px #e1bee7;
    }

    /* Modal Styles */
    .modal {
        z-index: 1050;
        border-radius: 18px;
    }

    .modal-backdrop {
        z-index: 1040;
    }

    #reportForm {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }

    #reportForm label {
        font-weight: bold;
    }

    #reportForm textarea {
        width: 100%;
        border-radius: 12px;
        border: 1px solid #e0e9f0;
        padding: 10px;
        background: #f8fbfd;
        font-family: inherit;
    }

    #reportForm button {
        align-self: flex-end;
        background-color: #4169e1;
        color: white;
        border: none;
        padding: 6px 14px;
        border-radius: 4px;
        font-weight: bold;
        transition: background-color 0.2s ease;
    }

    #reportForm button:hover {
        background-color: #2c4db1;
    }

    /* Responsive Adjustments */
    @media (max-width: 992px) {
        .top-rated-posts {
            flex-wrap: wrap;
        }

        .top-rated-card {
            flex: 0 0 calc(50% - 13px);
        }

        #content {
            padding: 60px 8%;
        }
    }

    @media (max-width: 768px) {
        #page-info {
            padding: 20px 15px;
        }

        .create-post-card {
            padding: 12px 16px;
        }

        .create-post-avatar {
            width: 44px;
            height: 44px;
            margin-right: 12px;
        }

        .create-post-input {
            font-size: 15px;
            padding: 10px 14px;
        }

        .card-body, .card-footer {
            padding-left: 60px;
            padding-right: 8px;
        }

        .card-post-author {
            padding-left: 8px;
            padding-right: 8px;
        }

        #content {
            padding: 60px 5%;
        }
    }

    @media (max-width: 576px) {
        .top-rated-card {
            flex: 0 0 100%;
        }

        .post-row {
            flex-direction: column;
            gap: 1rem;
        }

        .card-body {
            padding-left: 50px;
        }

        .card-footer {
            padding-left: 50px;
        }

        .vote-controls {
            margin-right: 8px;
        }

        .btn-comment, .btn-report {
            font-size: 13px;
            padding: 5px 10px;
        }
    }
</style>
<body data-user-logged-in="${user != null}">
<!-- HEADER -->
<c:import url="header.jsp"/>

<!-- PAGE INFO -->

<!-- Search Form -->

<form class="app-search" action="blog" method="GET">
    <div class="input-group mb-3">
        <input type="text" class="form-control" name="searchQuery" placeholder="Search posts..."
               value="${param.searchQuery}" aria-label="Search posts">
        <button class="btn btn-primary" type="submit" id="button-search">
            <i class="fas fa-search"></i> Search
        </button>
    </div>
</form>

<!-- Search Results Header -->
<c:if test="${not empty param.searchQuery}">
    <div class="search-results-header">
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
            <div class="card card-post content">
                <div class="post-container">
                    <a href="postDetails?postID=${post.getPostID()}" class="post-content-link" style="text-decoration: none;">
                        <div class="card-post-author">
                            <a href="profile?user=${post.getUserName()}">
                            <img src="${post.getAvtUserImg() != null ? post.getAvtUserImg() : userDAO.getAvatarByUserId(post.getUserID())}"
                                 onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                                 alt="Author Avatar" class="author-post-avatar"/>
                            <div class="author-post-info">
                                <span class="author-post-name">${post.getUserFullName()}</span>
                            </div>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title card-title-post">${post.getHeading()}</h5>
                            <p class="card-text">${post.getContent()}</p>
                        </div>
                    </a>
                </div>
                <div class="card-footer">
                    <div class="vote-controls" data-post-id="${post.getPostID()}">
                        <button type="button" class="vote-btn upvote-btn" data-post-id="${post.getPostID()}">
                            <i class="fas fa-arrow-up"></i>
                        </button>
                        <span class="vote-score" data-post-id="${post.getPostID()}">${post.getScore()}</span>
                        <button type="button" class="vote-btn downvote-btn" data-post-id="${post.getPostID()}">
                            <i class="fas fa-arrow-down"></i>
                        </button>
                    </div>
                    <button type="button" class="btn-comment"
                            onclick="location.href='postDetails?postID=${post.getPostID()}'">
                        <i class="fas fa-comment"></i> Comment (${post.getCommentCount()})
                    </button>
                    <button type="button" class="btn-outline-secondary btn-report"
                            data-bs-toggle="modal"
                            data-bs-target="#avatarModal"
                            data-postid="${post.getPostID()}">
                        <i class="fas fa-flag"></i>
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>


<div id="page-info">
    <div class="page-info-more">
        <c:choose>
            <c:when test="${user != null}">
                <div class="create-post-card" onclick="document.getElementById('myModal').style.display = 'block'">
                    <img src="${user.getAvatar() != null ? user.getAvatar() : 'https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg'}"
                         alt="User Avatar" class="create-post-avatar"/>
                    <input type="text" placeholder="${user.getFullName()} ơi, bạn đang nghĩ gì thế?" class="create-post-input" readonly/>
                </div>
            </c:when>
            <c:otherwise>
                <button class="openModalBtn" onclick="alert('Vui lòng đăng nhập để tạo bài viết.');">
                    Tạo bài viết mới
                </button>
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
                        <div class="post-container">
                            <div class="card-post-author">
                                <img src="${post.getAvtUserImg() != null ? post.getAvtUserImg() : userDAO.getAvatarByUserId(post.getUserID())}"
                                     onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                                     alt="Author Avatar" class="author-post-avatar"/>
                                <div class="author-post-info">
                                    <span class="author-post-name">${post.getUserFullName()}</span>
                                </div>
                            </div>
                            <a href="postDetails?postID=${post.getPostID()}" class="post-content-link" style="text-decoration: none;">
                                <div class="card-body">
                                    <h5 class="card-title card-title-post">${post.getHeading()}</h5>
                                    <p class="card-text">${post.getContent()}</p>
                                </div>
                            </a>
                        </div>
                        <div class="card-footer">
                            <div class="vote-controls" data-post-id="${post.getPostID()}">
                                <button type="button" class="vote-btn upvote-btn" data-post-id="${post.getPostID()}">
                                    <i class="fas fa-arrow-up"></i>
                                </button>
                                <span class="vote-score" data-post-id="${post.getPostID()}">${post.getScore()}</span>
                                <button type="button" class="vote-btn downvote-btn" data-post-id="${post.getPostID()}">
                                    <i class="fas fa-arrow-down"></i>
                                </button>
                            </div>
                            <button type="button" class="btn-comment"
                                    onclick="location.href='postDetails?postID=${post.getPostID()}'">
                                <i class="fas fa-comment"></i> Comment (${post.getCommentCount()})
                            </button>
                            <c:if test="${user == null || user.getUserID() != post.getUserID()}">
                                <button type="button" class="btn-report"
                                        data-bs-toggle="modal"
                                        data-bs-target="#avatarModal"
                                        data-postid="${post.getPostID()}">
                                    <i class="fas fa-flag"></i> Report
                                </button>
                            </c:if>
                        </div>
                    </div>
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
            <input type="hidden" id="imgPost" name="imgPost" value="" />
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
        function openPostModal() {
            const modal = document.getElementById("myModal");
            if (modal) {
                modal.style.display = "block";
            }
        }

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
    // var btn = document.getElementById("openModalBtn");
    var span = document.getElementsByClassName("close")[0];

    // if (btn) {
    //     btn.onclick = function () {
    //         modal.style.display = "block";
    //     }
    // }

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

<script>
    $('form.app-search').on('submit', function (e) {
        e.preventDefault(); // không submit mặc định
        const query = $('input[name="searchQuery"]').val().trim();
        if (query.length < 1) return;

        // Gửi request AJAX hoặc redirect
        window.location.href = 'blog?searchQuery=' + encodeURIComponent(query);
    });

</script>
</body>
</html>