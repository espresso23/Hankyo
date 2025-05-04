<%@ page import="model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>User Profile - Reported</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/themify-icons@1.0.1/css/themify-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="asset/css/sidebar.css">
</head>
<style>
    :root {
        --primary-color: #4a6fa5;
        --secondary-color: #6b8cae;
        --accent-color: #ff6b6b;
        --light-bg: #f8f9fa;
        --card-bg: #ffffff;
        --text-dark: #2d3436;
        --text-light: #636e72;
        --border-color: #e0e0e0;
    }

    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: var(--light-bg);
        color: var(--text-dark);
        margin: 0;
        padding: 0;
        line-height: 1.6;
    }

    .profile-container {
        width: calc(100% - 120px);
        margin: 30px auto;
        position: relative;
    }

    .cover-photo-container {
        position: relative;
        height: 250px;
        width: 100%;
        overflow: hidden;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .cover-photo {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .profile-info-section {
        position: relative;
        margin-bottom: 100px;
    }

    .avatar-username-wrapper {
        position: absolute;
        bottom: -70px;
        left: 40px;
        display: flex;
        align-items: flex-end;
        gap: 20px;
        z-index: 5;
    }

    .avatar-wrapper {
        position: relative;
        width: 140px;
        height: 140px;
        border-radius: 50%;
        border: 4px solid var(--card-bg);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        background-color: #f0f0f0;
    }

    .profile-avatar {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
    }

    .user-info {
        background: var(--card-bg);
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        min-width: 250px;
    }

    .username {
        font-size: 24px;
        font-weight: 600;
        color: var(--text-dark);
        margin-bottom: 5px;
    }

    .user-handle {
        font-size: 16px;
        color: var(--text-light);
    }

    .nav-tabs {
        display: flex;
        border-bottom: 1px solid var(--border-color);
        margin-bottom: 20px;
    }

    .nav-tab {
        padding: 12px 20px;
        text-decoration: none;
        color: var(--text-light);
        font-weight: 500;
        font-size: 14px;
        position: relative;
        transition: all 0.2s ease;
    }

    .nav-tab:hover {
        color: var(--primary-color);
        background-color: rgba(74, 111, 165, 0.05);
    }

    .nav-tab.active {
        color: var(--primary-color);
        font-weight: 600;
    }

    .nav-tab.active:after {
        content: '';
        position: absolute;
        bottom: -1px;
        left: 0;
        right: 0;
        height: 2px;
        background-color: var(--primary-color);
    }

    .main-content {
        display: flex;
        max-width: calc(100% - 120px);
        padding: 20px 0;
        gap: 20px;
        margin: 20px auto 0;
    }

    .profile-content {
        flex: 1;
        background-color: var(--card-bg);
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    }

    .no-posts {
        text-align: center;
        padding: 40px 20px;
        color: var(--text-light);
        border-radius: 8px;
        background-color: rgba(255, 255, 255, 0.7);
        border: 1px dashed var(--border-color);
    }

    /* Reported Content Styles */
    .container {
        max-width: 800px;
        margin: 0 auto;
        padding: 0;
    }

    .section-title {
        font-size: 18px;
        color: var(--primary-color);
        margin: 30px 0 15px 0;
        padding-bottom: 8px;
        border-bottom: 1px solid var(--border-color);
        font-weight: 600;
    }

    .report-card {
        background-color: var(--card-bg);
        border-radius: 6px;
        padding: 16px;
        margin-bottom: 12px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
        border-left: 3px solid var(--accent-color);
        transition: all 0.2s ease;
    }

    .report-card:hover {
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
    }

    .report-card p {
        margin: 6px 0;
        font-size: 14px;
        line-height: 1.5;
    }

    .report-label {
        font-weight: 500;
        color: var(--text-dark);
    }

    .btn {
        padding: 6px 12px;
        border-radius: 4px;
        font-size: 13px;
        cursor: pointer;
        transition: all 0.2s ease;
        border: 1px solid transparent;
    }

    .btn-outline-danger {
        color: #dc3545;
        border-color: #dc3545;
        background-color: transparent;
    }

    .btn-outline-danger:hover {
        background-color: #dc3545;
        color: white;
    }

    .alert {
        padding: 12px 16px;
        border-radius: 4px;
        margin-bottom: 16px;
        font-size: 14px;
    }

    .alert-success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .alert-warning {
        background-color: #fff3cd;
        color: #856404;
        border: 1px solid #ffeeba;
    }

    .alert-danger {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    /* Sidebar adjustments */
    .sidebar {
        width: 280px;
    }

    @media (max-width: 992px) {
        .profile-container {
            width: calc(100% - 40px);
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
<c:if test="${not empty message}">
    <div class="alert alert-info" style="margin: 20px;">${message}</div>
</c:if>

<!-- Profile container to organize layout -->
<div class="profile-container">
    <!-- PROFILE INFO SECTION (Cover + Avatar + User Info) -->
    <jsp:include page="profileHeader.jsp" />

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <c:import url="sidebar.jsp"/>
        <!-- PROFILE CONTENT -->
        <div class="profile-content">
            <c:choose>
                <c:when test="${param.tab == 'posts'}">
                    <!-- Hiển thị bài đăng của người dùng -->
                    <h2 class="section-title">📝 Bài đăng của bạn</h2>
                    <c:choose>
                        <c:when test="${not empty userPosts}">
                            <c:forEach var="post" items="${userPosts}">
                                <!-- Hiển thị mỗi bài đăng -->
                                <div class="post-card">
                                    <!-- Nội dung bài đăng -->
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="no-posts">
                                <i class="fas fa-scroll"></i>
                                <p>Bạn chưa đăng bài viết nào</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>

                <c:when test="${param.tab == 'comments'}">
                    <!-- Hiển thị bình luận của người dùng -->
                    <h2 class="section-title">💬 Bình luận của bạn</h2>
                    <c:choose>
                        <c:when test="${not empty userComments}">
                            <c:forEach var="comment" items="${userComments}">
                                <!-- Hiển thị mỗi bình luận -->
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="no-posts">
                                <i class="fas fa-comment-slash"></i>
                                <p>Bạn chưa có bình luận nào</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>

                <c:when test="${param.tab == 'reported'}">
                    <!-- Hiển thị nội dung đã báo cáo -->
                    <div class="container">
                        <h2 class="section-title">📝 Bài viết đã report</h2>
                        <c:if test="${param.msg == 'deleted'}">
                            <div class="alert alert-success" style="margin: 15px;">✅ Xóa report thành công.</div>
                        </c:if>
                        <c:if test="${param.msg == 'fail'}">
                            <div class="alert alert-warning" style="margin: 15px;">⚠️ Không thể xóa report.</div>
                        </c:if>
                        <c:if test="${param.msg == 'error'}">
                            <div class="alert alert-danger" style="margin: 15px;">❌ Lỗi khi xử lý yêu cầu.</div>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty reportedPosts}">
                                <c:forEach var="report" items="${reportedPosts}">
                                    <div class="report-card" onclick="location.href='postDetails?postID=${report.postID}'">
                                        <p><span class="report-label">📰 Tiêu đề bài viết:</span> ${report.postTitle}</p>
                                        <p><span class="report-label">📌 Lý do:</span> ${report.reason}</p>
                                        <p><span class="report-label">📤 Trạng thái:</span> ${report.status}</p>
                                        <p><span class="report-label">📅 Ngày report:</span> ${report.reportDate}</p>
                                        <form method="post" action="profile" style="display:inline;"
                                              onsubmit="return confirm('Xóa report này?');">
                                            <input type="hidden" name="action" value="deleteReport"/>
                                            <input type="hidden" name="reportID" value="${report.reportID}"/>
                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                <i class="fas fa-trash"></i> Xoá
                                            </button>
                                        </form>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="no-posts">
                                    <i class="fas fa-exclamation-circle"></i>
                                    <p>Không có bài viết nào bạn đã report</p>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <h2 class="section-title">💬 Bình luận đã report</h2>
                        <c:choose>
                            <c:when test="${not empty reportedComments}">
                                <c:forEach var="report" items="${reportedComments}">
                                    <div class="report-card"
                                         onclick="location.href='postDetails?postID=${report.postID}#comment-${report.commentID}'">
                                        <p><span class="report-label">💬 Nội dung bình luận:</span> ${report.commentContent}</p>
                                        <p><span class="report-label">📌 Lý do:</span> ${report.reason}</p>
                                        <p><span class="report-label">📤 Trạng thái:</span> ${report.status}</p>
                                        <p><span class="report-label">📅 Ngày report:</span> ${report.reportDate}</p>

                                    </div>
                                    <form method="post" action="profile" style="display:inline;"
                                          onsubmit="return confirm('Xóa report này?');">
                                        <input type="hidden" name="action" value="deleteReport"/>
                                        <input type="hidden" name="reportID" value="${report.reportID}"/>
                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                            <i class="fas fa-trash"></i> Xoá
                                        </button>
                                    </form>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="no-posts">
                                    <i class="fas fa-exclamation-circle"></i>
                                    <p>Không có bình luận nào bạn đã report</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
        <c:otherwise>
                        <div class="no-posts">
                            <i class="fas fa-exclamation-circle"></i>
                            <p>Không có bình luận nào bạn đã report</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
</div>

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
<c:import url="footer.jsp"/>

</body>
</html>
