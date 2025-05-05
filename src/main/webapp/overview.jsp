<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>User Profile - Overview</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="asset/css/profile.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/themify-icons@1.0.1/css/themify-icons.css">
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
.post-content img{
    max-width: 400px;
    max-height: 600px;
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
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .cover-photo {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .profile-info-section {
        position: relative;
        margin-bottom: 120px;
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
        box-shadow: 0 6px 25px rgba(0, 0, 0, 0.15);
        background: linear-gradient(135deg, #ffb6e6, #a2d7d8);
        flex-shrink: 0;
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
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }

    .user-info {
        text-align: left;
        background: var(--card-bg);
        padding: 25px 35px;
        border-radius: 20px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        min-width: 300px;
        z-index: 4;
        margin-bottom: 10px;
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
        transition: all 0.25s;
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
        flex-shrink: 0;
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

    .modal {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.7);
    }

    .modal-content {
        background-color: #fff;
        margin: 10% auto;
        padding: 20px;
        width: 60%;
        border-radius: 8px;
    }

    .close {
        float: right;
        cursor: pointer;
    }

    .create-post-box {
        display: flex;
        flex-direction: column;
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        padding: 15px 20px;
        margin-bottom: 25px;
        cursor: pointer;
    }

    .create-post-box:hover {
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    }

    .create-post-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        object-fit: cover;
        margin-bottom: 10px;
    }

    .create-post-input {
        background-color: #f0f2f5;
        border: none;
        padding: 12px 16px;
        border-radius: 20px;
        font-size: 16px;
        color: #444;
        margin-bottom: 10px;
        width: 100%;
    }

    .create-post-actions {
        display: flex;
        justify-content: space-between;
        font-size: 14px;
        color: #555;
        padding-top: 10px;
        border-top: 1px solid #eee;
    }

    .post-image {
        margin: 15px 0;
        text-align: center;
        background-color: rgba(0, 0, 0, 0.02);
        border-radius: 8px;
        overflow: hidden;
        max-height: 300px; /* Giới hạn chiều cao tối đa */
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .post-image img {
        max-width: 60%; /* Giảm kích thước tối đa so với container */
        max-height: 300px; /* Giới hạn chiều cao tối đa */
        object-fit: contain;
        border-radius: 8px;
        cursor: pointer;
        transition: transform 0.3s ease;
    }

    .post-image img:hover {
        transform: scale(1.02); /* Hiệu ứng phóng nhẹ khi hover */
    }
</style>
<body>
<c:import url="header.jsp"/>

<div class="profile-container">
    <!-- Cover, Avatar, Tabs giống như trong comments.jsp -->
    <jsp:include page="profileHeader.jsp"/>

    <!-- Nội dung riêng của trang overview -->
    <div class="main-content">
        <jsp:include page="sidebar.jsp"/>
        <div class="profile-content">
            <c:if test="${isOwnProfile}">
                <div class="create-post-box" onclick="document.getElementById('myModal').style.display = 'block'">
                    <img src="${user.avatar != null ? user.avatar : 'https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg'}"
                         alt="User Avatar" class="create-post-avatar"/>
                    <input type="text" class="create-post-input" placeholder="Bạn đang nghĩ gì?" readonly/>
                    <div class="create-post-actions">
                        <span><i class="fas fa-video text-danger"></i> Video trực tiếp</span>
                        <span><i class="fas fa-image text-success"></i> Ảnh/video</span>
                        <span><i class="fas fa-flag text-primary"></i> Sự kiện trong đời</span>
                    </div>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty userPosts}">
                    <c:forEach var="post" items="${userPosts}">
                        <div class="post-card" onclick="window.location.href='postDetails?postID=${post.postID}'"
                             style="cursor: pointer;">
                            <div class="post-header">
                                <div class="post-user-info">
                                    <img class="post-avatar"
                                         src="${not empty post.avtUserImg ? post.avtUserImg : 'https://dongvat.edu.vn/upload/2025/01/avatar-cho-hai-04.webp'}"
                                         alt="User Avatar">
                                    <a class="post-username"
                                       href="profile?user=${post.userName}">${post.userFullName}</a>
                                </div>


                                <c:if test="${post.userID == sessionScope.user.userID}">
                                    <div class="more-options-wrapper">
                                        <i class="ti-more-alt"
                                           onclick="event.stopPropagation(); toggleOptions(this)"></i>
                                        <div class="dropdown-options">
                                            <a href="editPost.jsp?postID=${post.postID}" class="dropdown-item"
                                               onclick="event.stopPropagation()">Sửa</a>
                                            <a href="deletePost?postID=${post.postID}" class="dropdown-item"
                                               onclick="event.stopPropagation()">Xoá</a>
                                        </div>
                                    </div>
                                </c:if>
                            </div>

                            <div class="post-content">
                                <h4 class="post-title">${post.heading}</h4>
                                <div class="post-meta">
                                    <fmt:formatDate value="${post.createdDate}" pattern="dd/MM/yyyy"/>
                                    - ${post.commentCount} bình luận
                                </div>
                                <p class="post-body">${post.content}
                                </p>


                                <div class="post-actions">
                                    <div class="action-item"><i class="far fa-comment"></i> ${post.commentCount} bình
                                        luận
                                    </div>
                                    <div class="action-item"><i class="far fa-heart"></i> Thích</div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="no-posts">
                        <i class="fas fa-scroll"></i>
                        <p>u/${isOwnProfile ? user.username : profileUser.username} vẫn chưa đăng gì</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:import url="footer.jsp"/>

    <!-- Modal Viewer -->
    <div id="imageModal">
        <img id="modalImg" src="" alt="Ảnh phóng to">
    </div>

    <div id="myModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="document.getElementById('myModal').style.display='none'">&times;</span>
            <form action="profile" method="POST">
                <h2>Thêm bài viết</h2>
                <input type="hidden" name="userID" value="${user.userID}"/>

                <label for="title">Tiêu đề:</label>
                <input name="title" id="title" type="text" required class="input-field"/><br>

                <label for="postContent">Nội dung:</label>
                <textarea id="postContent" name="description"></textarea><br>

                <input type="hidden" name="action" value="addPost">
                <button type="submit" class="submit-button">Đăng bài</button>
            </form>
        </div>
    </div>
    <script src="./asset/tinymce/tinymce.min.js"></script>
    <script>
        tinymce.init({
            selector: 'textarea#postContent',
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
    <script>
        function toggleOptions(icon) {
            const dropdown = icon.nextElementSibling;
            document.querySelectorAll('.dropdown-options').forEach(opt => {
                if (opt !== dropdown) opt.style.display = 'none';
            });
            dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
        }

        document.addEventListener('click', function (e) {
            if (!e.target.closest('.more-options-wrapper')) {
                document.querySelectorAll('.dropdown-options').forEach(opt => opt.style.display = 'none');
            }
        });
    </script>
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
</body>
</html>
