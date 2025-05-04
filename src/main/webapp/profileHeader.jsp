<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .cover-photo-container {
        position: relative;
        height: 350px;
        width: 100%;
        overflow: hidden;
        border-radius: 12px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .cover-photo {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .change-cover-btn {
        position: absolute;
        bottom: 15px;
        right: 20px;
        background-color: white;
        color: #444;
        padding: 8px 12px;
        border-radius: 8px;
        border: none;
        cursor: pointer;
        font-weight: 500;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        display: flex;
        align-items: center;
        gap: 6px;
    }

    .change-cover-btn:hover {
        background-color: #f1f1f1;
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
        border: 6px solid white;
        box-shadow: 0 6px 25px rgba(0, 0, 0, 0.15);
        background: linear-gradient(135deg, #ffb6e6, #a2d7d8);
        flex-shrink: 0;
    }

    .profile-avatar {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
        cursor: pointer;
    }

    .avatar-overlay {
        position: absolute;
        bottom: 10px;
        right: 10px;
        background-color: #48dbfb;
        color: white;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }

    .user-info {
        background: white;
        padding: 25px 35px;
        border-radius: 20px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        min-width: 300px;
    }

    .username {
        font-size: 32px;
        font-weight: 700;
        color: #2d3436;
    }

    .user-handle {
        font-size: 18px;
        color: #636e72;
    }

    .nav-tabs {
        display: flex;
        justify-content: flex-start;
        background-color: white;
        padding: 0 60px;
        width: calc(100% - 120px);
        position: relative;
        z-index: 3;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        margin: 0 auto;
    }

    .nav-tab {
        padding: 16px 24px;
        text-decoration: none;
        color: #8e8e8e;
        font-weight: 500;
        font-size: 15px;
        position: relative;
        margin: 0 2px;
        transition: all 0.25s;
    }

    .nav-tab:hover {
        color: #ff94cc;
        background-color: rgba(255, 182, 193, 0.1);
    }

    .nav-tab.active {
        color: #ff7eb9;
    }

    .nav-tab.active::before {
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

    #imageModal {
        position: fixed;
        display: none;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background: rgba(0, 0, 0, 0.8);
        justify-content: center;
        align-items: center;
        z-index: 9999;
    }

    #imageModal img {
        max-width: 90%;
        max-height: 90%;
        border-radius: 10px;
    }
</style>

<div class="profile-info-section">
    <div class="cover-photo-container">
        <c:choose>
            <c:when test="${isOwnProfile}">
                <img class="cover-photo" id="coverPhoto"
                     src="${user.coverPhoto != null ? user.coverPhoto : 'https://images.unsplash.com/photo-1518621736915-f3b1c41bfd00?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80'}"
                     alt="Cover Photo">
                <button class="change-cover-btn" onclick="document.getElementById('coverInput').click()">
                    <i class="fas fa-camera"></i> Đổi ảnh bìa
                </button>
            </c:when>
            <c:otherwise>
                <img class="cover-photo" id="coverPhoto"
                     src="${profileUser.coverPhoto != null ? profileUser.coverPhoto : 'https://images.unsplash.com/photo-1518621736915-f3b1c41bfd00?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80'}"
                     alt="Cover Photo">
            </c:otherwise>
        </c:choose>
        <form id="coverForm" action="profile" method="post" enctype="multipart/form-data" style="display:none;">
            <input type="hidden" name="action" value="updateCover"/>
            <input type="file" name="coverPhoto" id="coverInput"
                   onchange="document.getElementById('coverForm').submit()" accept="image/*">
        </form>
    </div>

    <div class="avatar-username-wrapper">
        <div class="avatar-wrapper">
            <c:choose>
                <c:when test="${isOwnProfile}">
                    <img class="profile-avatar" id="avatarImage"
                         src="${user.avatar != null ? user.avatar : 'https://dongvat.edu.vn/upload/2025/01/avatar-cho-hai-04.webp'}"
                         alt="User Avatar" onclick="viewImage(this.src)">
                    <div class="avatar-overlay" onclick="document.getElementById('avatarInput').click()">
                        <i class="fas fa-camera"></i>
                    </div>
                </c:when>
                <c:otherwise>
                    <img class="profile-avatar" id="avatarImage"
                         src="${profileUser.avatar != null ? profileUser.avatar : 'https://dongvat.edu.vn/upload/2025/01/avatar-cho-hai-04.webp'}"
                         alt="User Avatar" onclick="viewImage(this.src)">
                </c:otherwise>
            </c:choose>
            <form id="avatarForm" action="profile" method="post" enctype="multipart/form-data" style="display:none;">
                <input type="hidden" name="action" value="updateAvatar"/>
                <input type="file" name="avatar" id="avatarInput"
                       onchange="document.getElementById('avatarForm').submit()" accept="image/*">
            </form>
        </div>

        <div class="user-info">
            <c:choose>
                <c:when test="${isOwnProfile}">
                    <div class="username">${user.username}</div>
                    <div class="user-handle">u/${user.username}</div>
                </c:when>
                <c:otherwise>
                    <div class="username">${profileUser.username}</div>
                    <div class="user-handle">u/${profileUser.username}</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- NAVIGATION TABS -->
<div class="nav-tabs">
    <a href="profile?user=${profileUser.username}&tab=overview"
       class="nav-tab ${tab == 'overview' ? 'active' : ''}">Tổng quan</a>
    <a href="profile?user=${profileUser.username}&tab=posts"
       class="nav-tab ${tab == 'posts' ? 'active' : ''}">Bài viết</a>
    <a href="profile?user=${profileUser.username}&tab=comments"
       class="nav-tab ${tab == 'comments' ? 'active' : ''}">Bình luận</a>

    <c:if test="${isOwnProfile}">
        <a href="profile?user=${profileUser.username}&tab=upvoted"
           class="nav-tab ${tab == 'upvoted' ? 'active' : ''}">Upvoted</a>
        <a href="profile?user=${profileUser.username}&tab=downvoted"
           class="nav-tab ${tab == 'downvoted' ? 'active' : ''}">Downvoted</a>
        <a href="profile?user=${profileUser.username}&tab=reported"
           class="nav-tab ${tab == 'reported' ? 'active' : ''}">Báo cáo</a>
    </c:if>

    <!-- Hiển thị tab Khóa học của tôi cho expert -->
    <c:if test="${isExpert}">
        <a href="profile?user=${profileUser.username}&tab=courses"
           class="nav-tab ${tab == 'courses' ? 'active' : ''}">Khóa học của tôi</a>
    </c:if>
</div>

<!-- Modal Viewer -->
<div id="imageModal">
    <img id="modalImg" src="" alt="áº¢nh phÃ³ng to">
</div>

<script>
    function viewImage(src) {
        const modal = document.getElementById('imageModal');
        const modalImg = document.getElementById('modalImg');
        modalImg.src = src;
        modal.style.display = 'flex';
    }

    window.addEventListener('click', function (e) {
        const modal = document.getElementById('imageModal');
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
</script>



