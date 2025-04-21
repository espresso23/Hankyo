<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa hồ sơ</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/asset/update.css">
    <script src="${pageContext.request.contextPath}/asset/update.js" defer></script>
</head>
<body>
<style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');

    body {
        font-family: 'Poppins', sans-serif;
        background-image: url("asset/png/background/background.png");
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background-attachment: fixed;
    }

    .profile-container {
        background: white;
        padding: 40px;
        border-radius: 32px;
        width: 90%;
        max-width: 600px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        text-align: left;
        transition: transform 0.3s, box-shadow 0.3s ease-in-out;
        border: 2px solid #8EC5FC;
        animation: fadeIn 0.5s ease-in-out;
    }

    .section {
        background: white;
        padding: 15px 20px;
        margin-bottom: 15px;
        border-radius: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        transition: background-color 0.3s, transform 0.2s ease-in-out;
        border: 1px solid #8EC5FC;
    }

    .section:hover {
        background-color: #F0F9FF;
        transform: translateY(-5px);
    }

    .section strong {
        font-size: 18px;
        color: #333;
    }

    .section .section-content {
        display: flex;
        justify-content: space-between;
        width: 100%;
        align-items: center;
    }

    .arrow-button {
        background: none;
        border: none;
        cursor: pointer;
        font-size: 22px;
        color: #8EC5FC;
        transition: color 0.3s, transform 0.3s;
    }

    .arrow-button:hover {
        color: #FFAFBD;
        transform: scale(1.2);
    }
    .profile-picture {
        margin-bottom: 20px;
        text-align: center;
    }

    .profile-picture img {
        border-radius: 50%;
        width: 130px;
        height: 130px;
        object-fit: cover;
        border: 4px solid #FFAFBD;
        cursor: pointer;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .profile-picture img:hover {
        transform: scale(1.05);
        box-shadow: 0 0 10px rgba(255, 175, 189, 0.7);
    }
</style>

<div class="profile-container">
    <div class="profile-picture" onclick="openOverlay('avatar')">
        <img src="${pageContext.request.contextPath}/${user.avatar}?rand=${System.currentTimeMillis()}" alt="Avatar">
    </div>
    <div class="section">
        <div class="section-content">
            <strong>Thông tin liên hệ</strong>
            <div>Email: ${user.gmail}</div>
            <button class="arrow-button" onclick="openOverlay('gmail')">➔</button>
        </div>
    </div>
    <div class="section">
        <div class="section-content">
            <strong>Số điện thoại</strong>
            <div>${user.phone}</div>
            <button class="arrow-button" onclick="openOverlay('phone')">➔</button>
        </div>
    </div>
    <div class="section">
        <div class="section-content">
            <strong>Ngày sinh</strong>
            <div>${user.dateOfBirth}</div>
            <button class="arrow-button" onclick="openOverlay('dateOfBirth')">➔</button>
        </div>
    </div>
    <div class="section">
        <div class="section-content">
            <strong>Họ và tên</strong>
            <div>${user.fullName}</div>
            <button class="arrow-button" onclick="openOverlay('name')">➔</button>
        </div>
    </div>
    <div class="section">
        <div class="section-content">
            <strong>Mật khẩu</strong>
            <div>*********</div>
            <button class="arrow-button" onclick="openOverlay('password')">➔</button>
        </div>
    </div>
</div>

<!-- css overlay -->
<style>
    .overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: none;
        justify-content: center;
        align-items: center;
        z-index: 9999;
    }

    .overlay-content {
        background: white;
        padding: 30px;
        border-radius: 24px;
        width: 90%;
        max-width: 500px;
        text-align: left;
        animation: slideDown 0.4s ease;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    .overlay h2 {
        margin-bottom: 20px;
        font-size: 24px;
        color: #333;
        text-align: center;
    }

    .overlay-tab {
        margin-bottom: 20px;
    }

    .overlay-tab label {
        display: block;
        margin-bottom: 10px;
        font-weight: 500;
    }

    .overlay-tab input {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 8px;
        margin-bottom: 10px;
    }

    .overlay-buttons {
        display: flex;
        justify-content: space-between;
    }

    .overlay-buttons button {
        padding: 10px 20px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        background-color: #8EC5FC;
        color: white;
        font-size: 16px;
        transition: background-color 0.3s;
    }

    .overlay-buttons button:hover {
        background-color: #FFAFBD;
    }

    @keyframes slideIn {
        0% {
            transform: scale(0.8);
            opacity: 0;
        }
        100% {
            transform: scale(1);
            opacity: 1;
        }

    }
</style>

<div id="overlay" class="overlay">
    <div class="overlay-content">
        <h2 id="editTitle">Chỉnh sửa</h2>
        <input type="hidden" id="editInput">

        <div id="avatar-tab" class="overlay-tab" style="display: none;">
            <label for="editAvatar">Chọn ảnh mới:</label>
            <input type="file" id="editAvatar" accept="image/*">
        </div>

        <div id="gmail-tab" class="overlay-tab">
            <label for="editGmailInput">Nhập gmail mới:</label>
            <input type="text" id="editGmailInput">
        </div>
        <div id="phone-tab" class="overlay-tab">
            <label for="editPhoneInput">Nhập số điện thoại mới:</label>
            <input type="text" id="editPhoneInput">
        </div>
        <div id="name-tab" class="overlay-tab">
            <label for="editNameInput">Nhập tên mới:</label>
            <input type="text" id="editNameInput">
        </div>
        <div id="date-tab" class="overlay-tab">
            <label for="editDateInput">Chọn ngày:</label>
            <input type="date" id="editDateInput">
        </div>

        <div id="password-tab" class="overlay-tab" style="display: none;">
            <label>Mật khẩu cũ:</label>
            <input type="password" id="oldPassword">
            <label>Mật khẩu mới:</label>
            <input type="password" id="newPassword">
            <label>Xác nhận mật khẩu:</label>
            <input type="password" id="reNewPassword">
        </div>

        <div class="overlay-buttons">
            <button onclick="saveChanges()">Lưu</button>
            <button onclick="closeOverlay()">Hủy</button>
        </div>
    </div>
</div>
</body>
</html>
