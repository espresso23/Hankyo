<%@ page import="model.User" %>
<%@ page import="model.Post" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bài viết đã downvote - ${user.username}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/themify-icons@1.0.1/css/themify-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="asset/css/profile.css">
    <link rel="stylesheet" href="asset/css/sidebar.css">

</head>
<style>
    :root {
        --primary-pink: #ff9ff3;
        --secondary-pink: #bc90b7;
        --primary-blue: #48dbfb;
        --secondary-blue: #536b70;
        --light-bg: #fafafa;
        --card-bg: #ffffff;
        --text-dark: #2d3436;
        --text-light: #636e72;
    }

    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: var(--light-bg);
        color: var(--text-dark);
        margin: 0;
        padding: 0;
    }

    .profile-container {
        width: calc(100% - 120px);
        margin: 30px auto;
        position: relative;
    }

    .cover-photo-container {
        position: relative;
        height: 350px;
        width: 100%;
        overflow: hidden;
        border-radius: 12px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    }

    .cover-photo {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .profile-info-section {
        position: relative;
        margin-bottom: 120px; /* Provide space for avatar and user info */
    }

    .avatar-username-wrapper {
        position: absolute;
        bottom: -80px;
        left: 60px;
        display: flex;
        align-items: flex-end;
        gap: 30px;
        z-index: 5;
    }

    .avatar-wrapper {
        position: relative;
        width: 180px;
        height: 180px;
        border-radius: 50%;
        border: 6px solid var(--card-bg);
        box-shadow: 0 6px 25px rgba(0,0,0,0.15);
        background: linear-gradient(135deg, #ffb6e6, #a2d7d8);
        flex-shrink: 0; /* Prevent avatar from shrinking */
    }

    .profile-avatar {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
    }

    .avatar-overlay {
        position: absolute;
        bottom: 10px;
        right: 10px;
        background-color: var(--primary-blue);
        color: white;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        box-shadow: 0 2px 5px rgba(0,0,0,0.2);
    }

    .user-info {
        text-align: left;
        background: var(--card-bg);
        padding: 25px 35px;
        border-radius: 20px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        min-width: 300px;
        z-index: 4;
        margin-bottom: 10px; /* Add space below user info */
    }

    .username {
        font-size: 32px;
        font-weight: 700;
        color: var(--text-dark);
        margin-bottom: 5px;
    }

    .user-handle {
        font-size: 18px;
        color: var(--text-light);
    }

    .nav-tabs {
        display: flex;
        justify-content: flex-start;
        background-color: var(--card-bg);
        padding: 0 60px;
        width: calc(100% - 120px);
        position: relative;
        z-index: 3;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        margin: 0 auto;
    }

    .nav-tab {
        padding: 16px 24px;
        text-decoration: none;
        color: #8e8e8e;
        font-weight: 500;
        font-size: 15px;
        position: relative;
        transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
        margin: 0 2px;
    }

    .nav-tab:hover {
        color: #ff94cc;
        background-color: rgba(255, 182, 193, 0.1);
    }

    .nav-tab:hover:before {
        content: '';
        position: absolute;
        bottom: 0;
        left: 24px;
        right: 24px;
        height: 2px;
        background-color: #ffb6e6;
        border-radius: 2px 2px 0 0;
    }

    .nav-tab.active {
        color: #ff7eb9;
        font-weight: 500;
    }

    .nav-tab.active:before {
        content: '';
        position: absolute;
        bottom: 0;
        left: 16px;
        right: 16px;
        height: 3px;
        background: linear-gradient(90deg, #ffb6e6, #ff8cc6);
        border-radius: 3px 3px 0 0;
        box-shadow: 0 2px 4px rgba(255, 140, 198, 0.2);
    }

    .main-content {
        display: flex;
        max-width: calc(100% - 120px);
        padding: 20px 0;
        gap: 25px;
        margin: 20px auto 0;
    }

    .profile-content {
        flex: 1;
        background-color: var(--card-bg);
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .sidebar {
        width: 300px;
        flex-shrink: 0; /* Prevent sidebar from shrinking */
    }

    .profile-card {
        background-color: var(--card-bg);
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        margin-bottom: 20px;
    }

    .profile-header {
        background: linear-gradient(135deg, var(--primary-pink), var(--primary-blue));
        padding: 15px;
        color: white;
    }

    .profile-header h3 {
        margin: 0;
        font-size: 18px;
    }

    .stats {
        display: flex;
        padding: 15px;
        border-bottom: 1px solid #eee;
    }

    .stat-item {
        flex: 1;
        text-align: center;
    }

    .stat-value {
        font-size: 20px;
        font-weight: bold;
        color: var(--secondary-blue);
    }

    .stat-label {
        font-size: 12px;
        color: var(--text-light);
    }

    .join-date {
        padding: 15px;
        font-size: 14px;
        color: var(--text-light);
        display: flex;
        align-items: center;
    }

    .settings-section {
        background-color: var(--card-bg);
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 20px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .settings-title {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 10px;
        color: var(--secondary-pink);
    }

    .settings-item {
        display: flex;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #eee;
    }

    .settings-item:last-child {
        border-bottom: none;
    }

    .settings-item i {
        margin-right: 10px;
        color: var(--primary-blue);
    }

    /* Styles for posts */
    .post-card {
        background-color: var(--card-bg);
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        margin-bottom: 20px;
        overflow: hidden;
        border: 1px solid rgba(0, 0, 0, 0.05);
        transition: box-shadow 0.3s ease, transform 0.2s ease;
    }

    .post-card:hover {
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
        transform: translateY(-2px);
        border-color: rgba(255, 159, 243, 0.2);
    }

    .post-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px 16px;
        background-color: rgba(255, 255, 255, 0.8);
        border-bottom: 1px solid rgba(0, 0, 0, 0.05);
    }

    .post-user-info {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .post-avatar {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid var(--primary-pink);
    }

    .post-username {
        font-weight: 600;
        color: var(--text-dark);
        font-size: 14px;
    }

    .post-username:hover {
        color: var(--primary-pink);
        text-decoration: underline;
        cursor: pointer;
    }

    .more-options-wrapper {
        position: relative;
    }

    .ti-more-alt {
        font-size: 18px;
        color: var(--text-light);
        cursor: pointer;
        padding: 5px;
        border-radius: 50%;
        transition: all 0.2s ease;
    }

    .ti-more-alt:hover {
        background-color: rgba(255, 159, 243, 0.1);
        color: var(--primary-pink);
    }

    .dropdown-options {
        display: none;
        position: absolute;
        right: 0;
        top: 25px;
        background-color: var(--card-bg);
        border-radius: 6px;
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        z-index: 100;
        min-width: 130px;
        border: 1px solid rgba(0, 0, 0, 0.05);
        overflow: hidden;
    }

    .dropdown-item {
        display: block;
        padding: 10px 15px;
        color: var(--text-dark);
        text-decoration: none;
        font-size: 14px;
        transition: all 0.2s ease;
    }

    .dropdown-item:hover {
        background-color: rgba(255, 159, 243, 0.1);
        color: var(--primary-pink);
    }

    .post-content {
        padding: 16px;
    }

    .post-title {
        margin: 0 0 8px 0;
        font-size: 18px;
        font-weight: 600;
        color: var(--text-dark);
    }

    .post-meta {
        font-size: 12px;
        color: var(--text-light);
        margin-bottom: 12px;
    }

    .post-body {
        margin: 12px 0;
        font-size: 14px;
        line-height: 1.6;
        color: var(--text-dark);
    }

    .post-image {
        margin: 15px 0;
        text-align: center;
        background-color: rgba(0, 0, 0, 0.02);
        border-radius: 8px;
        overflow: hidden;
    }

    .post-image img {
        max-width: 100%;
        max-height: 400px;
        object-fit: contain;
        border-radius: 8px;
    }

    .post-actions {
        display: flex;
        align-items: center;
        gap: 16px;
        padding: 10px 16px;
        border-top: 1px solid rgba(0, 0, 0, 0.05);
    }

    .action-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;
        color: var(--text-light);
        cursor: pointer;
        padding: 6px 10px;
        border-radius: 100px;
        transition: all 0.2s ease;
    }

    .action-item:hover {
        background-color: rgba(72, 219, 251, 0.1);
        color: var(--primary-blue);
    }

    .action-item i {
        font-size: 16px;
    }

    .no-posts {
        text-align: center;
        padding: 40px 20px;
        background-color: rgba(255, 255, 255, 0.5);
        border-radius: 12px;
        border: 1px dashed rgba(255, 159, 243, 0.3);
    }

    .no-posts i {
        font-size: 48px;
        color: var(--primary-pink);
        margin-bottom: 16px;
        opacity: 0.7;
    }

    .no-posts p {
        font-size: 16px;
        color: var(--text-light);
        margin: 0;
    }

    /* Responsive adjustments */
    @media (max-width: 992px) {
        .avatar-username-wrapper {
            flex-direction: column;
            align-items: flex-start;
            gap: 15px;
        }

        .user-info {
            margin-left: 0;
        }

        .main-content {
            flex-direction: column;
        }

        .sidebar {
            width: 100%;
        }
    }
</style>
<body>
<c:import url="header.jsp"/>

<div class="profile-container">
    <!-- Cover, Avatar, Tabs giống như trong comments.jsp -->
    <jsp:include page="profileHeader.jsp" />

    <div class="main-content">
        <c:import url="sidebar.jsp"/>
        <div class="profile-content">
            <h2 style="margin-bottom: 20px;">Bài viết bạn đã downvote</h2>
            <c:choose>
                <c:when test="${not empty downvotedPosts}">
                    <c:forEach var="post" items="${downvotedPosts}">
                        <div class="post-card" onclick="location.href='postDetails?postID=${post.postID}'" style="cursor: pointer;">
                            <div class="post-header">
                                <div class="post-user-info">
                                    <img class="post-avatar"
                                         src="${not empty post.avtUserImg ? post.avtUserImg : 'https://dongvat.edu.vn/upload/2025/01/avatar-cho-hai-04.webp'}"
                                         alt="User Avatar">
                                    <span class="post-username">${post.userFullName}</span>
                                </div>
                            </div>

                            <div class="post-content">
                                <h4 class="post-title">${post.heading}</h4>
                                <div class="post-meta">
                                    <fmt:formatDate value="${post.createdDate}" pattern="dd/MM/yyyy" />
                                    - ${post.commentCount} bình luận
                                </div>
                                <p class="post-body">${post.content}</p>

                                <div class="post-actions">
                                    <div class="action-item">
                                        <i class="fas fa-thumbs-down" style="color: red;"></i> ${post.score}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="no-posts">
                        <i class="fas fa-arrow-down"></i>
                        <p>u/${user.username} chưa downvote bài viết nào.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <!-- SIDEBAR giống như trong comments.jsp -->
    </div>
</div>

<c:import url="footer.jsp"/>
</body>
</html>
