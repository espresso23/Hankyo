<%@ page import="model.User" %><%--
  Created by IntelliJ IDEA.
  User: Le Phuong Uyen
  Date: 4/26/2025
  Time: 9:32 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Title</title>
</head>
<style>
    .profile-card {
        background-color: white;
        border-radius: 16px;
        box-shadow: 0 6px 20px rgba(0,0,0,0.07);
        padding: 20px;
        margin-bottom: 30px;
        font-family: 'Segoe UI', sans-serif;
    }

    .profile-header {
        background: linear-gradient(135deg, #ff9ff3, #48dbfb);
        padding: 20px;
        color: white;
        font-size: 24px;
        font-weight: bold;
        border-radius: 16px 16px 0 0;
        text-align: center;
    }

    .profile-stats {
        display: flex;
        justify-content: space-around;
        padding: 16px 0;
        border-bottom: 1px solid #eee;
        text-align: center;
    }

    .stat-block {
        flex: 1;
    }

    .stat-value {
        font-size: 32px;
        font-weight: bold;
        color: #2d3436;
        margin-bottom: 4px;
    }

    .stat-label {
        font-size: 14px;
        color: #888;
    }

    .profile-info, .join-date {
        padding: 12px 16px;
        font-size: 15px;
        color: #444;
        display: flex;
        align-items: center;
        gap: 10px;
        border-bottom: 1px solid #f1f1f1;
    }

    .profile-info:last-child {
        border-bottom: none;
    }

    .settings-section {
        background-color: white;
        border-radius: 12px;
        padding: 20px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        margin-bottom: 20px;
    }

    .settings-title {
        font-size: 16px;
        font-weight: 700;
        color: var(--secondary-pink);
        margin-bottom: 15px;
    }

    .settings-item {
        display: flex;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #eee;
        color: #444;
        font-size: 15px;
        cursor: pointer;
        transition: all 0.2s ease;
    }

    .settings-item i {
        margin-right: 12px;
        color: var(--primary-blue);
    }

    .settings-item:last-child {
        border-bottom: none;
    }

    .settings-item:hover {
        color: #ff7eb9;
    }
</style>
<body>
<div class="sidebar">
    <div class="profile-card">
        <div class="profile-header">
            <h3><c:out value="${profileUser.username}"/></h3>
        </div>

        <div class="profile-stats">
            <div class="stat-block">
                <div class="stat-value"><c:out value="${postCount}"/></div>
                <div class="stat-label">Bài viết</div>
            </div>
            <div class="stat-block">
                <div class="stat-value"><c:out value="${commentCount}"/></div>
                <div class="stat-label">Bình luận</div>
            </div>
        </div>

        <div class="join-date">
            <i class="fas fa-birthday-cake"></i>
            Ngày tham gia:
            <fmt:formatDate value="${profileUser.dateCreate}" pattern="d 'thg' M, yyyy"/>
        </div>

        <div class="profile-info">
            <i class="fas fa-venus-mars"></i>
            Giới tính:
            <c:choose>
                <c:when test="${profileUser.gender == 'male'}">Nam</c:when>
                <c:when test="${profileUser.gender == 'female'}">Nữ</c:when>
                <c:otherwise>Khác</c:otherwise>
            </c:choose>
        </div>

        <div class="profile-info">
            <i class="fas fa-calendar-alt"></i>
            Ngày sinh:
            <fmt:formatDate value="${profileUser.dateOfBirth}" pattern="d 'thg' M, yyyy"/>
        </div>
    </div>

    <!-- Thành tựu hiển thị cho cả chủ hồ sơ và người ngoài -->
    <div class="settings-section">
        <div class="settings-title">
            <i class="fas fa-trophy"></i> Các thành tựu đã đạt được
        </div>

        <c:if test="${not empty listHonour}">
            <div class="honour-section" style="margin-top: 10px;">
                <c:choose>
                    <c:when test="${isOwnProfile}">
                        <ul style="list-style: none; padding: 0;">
                            <c:forEach var="honour" items="${listHonour}">
                                <li style="margin-bottom: 10px;">
                                    <span style="font-weight: bold;"><c:out value="${honour.honourName}" /></span>
                                    <c:if test="${honourOwnedMap[honour.honourID]}">
                                        <span style="color: green;">(Đã sở hữu)</span>
                                    </c:if>
                                    <c:if test="${equippedHonourID == honour.honourID}">
                                        <span style="color: blue;">(Đang trang bị)</span>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <div style="display: flex; flex-wrap: wrap; gap: 10px;">
                            <c:forEach var="honour" items="${listHonour}">
                                <c:if test="${honourOwnedMap[honour.honourID]}">
                                    <div style="padding: 8px 12px; background-color: #dff9fb; border-radius: 10px;">
                                        <c:out value="${honour.honourName}" />
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>

    <!-- Hiển thị chứng chỉ cho expert -->
    <c:if test="${isExpert}">
        <div class="settings-section">
            <div class="settings-title">
                <i class="fas fa-certificate"></i> Chứng chỉ của tôi
            </div>
            <div class="certificate-section" style="margin-top: 10px;">
                <c:if test="${not empty expert.certificate}">
                    <c:choose>
                        <c:when test="${fn:endsWith(expert.certificate, '.pdf')}">
                            <div style="text-align:center;">
                                <i class="fas fa-file-pdf" style="font-size:48px;color:#e74c3c;"></i>
                                <div>
                                    <a href="${expert.certificate}" target="_blank" style="color:#e74c3c;font-weight:bold;text-decoration:none;">
                                        Xem file PDF
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="certificate-item" style="cursor: pointer;" onclick="viewImage('${expert.certificate}')">
                                <img src="${expert.certificate}" alt="Chứng chỉ" style="width: 100%; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                <div style="text-align: center; margin-top: 8px; color: #666;">
                                    <i class="fas fa-eye"></i> Nhấn để xem chi tiết
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>
    </c:if>

    <!-- Chỉ hiển thị phần cài đặt và liên kết cho chủ hồ sơ -->
    <c:if test="${isOwnProfile}">
        <div class="settings-section">
            <div class="settings-title">CÀI ĐẶT</div>
            <a href="update-profile" class="settings-item" style="text-decoration: none;">
                <i class="fas fa-user"></i>
                <span>Tùy chỉnh hồ sơ</span>
            </a>
        </div>

    </c:if>
</div>

</body>
</html>
