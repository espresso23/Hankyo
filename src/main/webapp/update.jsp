<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Profile</title>
</head>
<body>

<div>
    <div>
        <img src="${pageContext.request.contextPath}/${user.avatar}?rand=${System.currentTimeMillis()}"
             class="rounded-circle img-profile" width="100" height="100"/>
        <button type="button" class="editButton" onclick="openOverlay('avatar', '${user.avatar}')">Edit</button>

    </div>
    <div>
        <span>Email: ${user.gmail}</span>
    </div>

    <div>
        <span id="fullName">Full Name: ${user.fullName}</span>
        <button type="button" class="editButton" onclick="openOverlay('fullName', '${user.fullName}')">Edit</button>
    </div>

    <div>
        <span id="phone">Phone: ${user.phone}</span>
        <button type="button" class="editButton" onclick="openOverlay('phone', '${user.phone}')">Edit</button>
    </div>

    <div>
        <span id="dateOfBirth">Date of Birth: ${user.dateOfBirth}</span>
        <button type="button" class="editButton" onclick="openOverlay('dateOfBirth', '${user.dateOfBirth}')">Edit
        </button>
    </div>

    <div>
        <span>Password: *********</span>
        <button type="button" class="editButton" onclick="openOverlay('password', '')">Edit</button>
    </div>
</div>

<!-- Overlay chỉnh sửa -->
<div id="overlay" class="overlay">
    <div class="overlay-content">
        <h2 id="editTitle">Chỉnh sửa</h2>
        <div id="avatar-tab" class="overlay-tab" style="display: none;">
            <label for="editAvatar">Chọn ảnh mới:</label>
            <input type="file" id="editAvatar" accept="image/*">
        </div>

        <div id="text-tab" class="overlay-tab">
            <label for="editInput">Nhập giá trị mới:</label>
            <input type="text" id="editInput">
        </div>

        <div id="date-tab" class="overlay-tab">
            <input type="date" id="editDateInput" style="display: none;">
        </div>

        <div id="password-tab" class="overlay-tab" style="display: none;">
            <label>Old Password:</label>
            <input type="password" id="oldPassword">
            <label>New Password:</label>
            <input type="password" id="newPassword">
            <label>Confirm Password:</label>
            <input type="password" id="reNewPassword">
        </div>


        <button onclick="saveChanges()">Lưu</button>
        <button onclick="closeOverlay()">Hủy</button>
    </div>
</div>

<link rel="stylesheet" type="text/css" href="asset/update.css">
<script src="asset/update.js"></script>
</body>
</html>
