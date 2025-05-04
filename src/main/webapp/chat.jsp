<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Chat Room</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="./asset/css/chat.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <script>
        window.onerror = function (msg, url, line) {
            console.error('Error: ' + msg + '\nURL: ' + url + '\nLine: ' + line);
            return false;
        };
    </script>
    <style>
        /* Modern Chat Room CSS */
        .timestamp-tooltip {
            display: inline-block;
            font-size: 0.7em;
            color: #888;
            margin-top: 4px;
            opacity: 0.8;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .my-message .timestamp-tooltip {
            color: rgba(255, 255, 255, 0.7);
        }

        .other-message .timestamp-tooltip {
            color: #0e0b0b;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #ffe0f7 0%, #e0f7fa 100%);
            margin: 0;
            padding: 0;
            color: #0e0b0b;
            line-height: 1.6;
        }

        /* Chat Container */
        .chat-container {
            max-width: 900px;
            height: 600px;
            margin: 20px auto;
            background-color: white;
            border-radius: 16px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
            overflow: hidden;
            display: flex;
            flex-direction: column;
            transition: all 0.3s ease;
        }

        /* Chat Header */
        .chat-header {
            background: linear-gradient(135deg, #a8d8ea 0%, #f8c3cd 100%);
            color: white;
            padding: 16px 20px;
            font-size: 1.2em;
            font-weight: 600;
            text-align: center;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
        }

        /* Message Container */
        #message-container {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
            background-color: #fff;
            background-image: radial-gradient(rgba(0, 0, 0, 0.02) 1px, transparent 1px),
            radial-gradient(rgba(0, 0, 0, 0.02) 1px, transparent 1px);
            background-size: 20px 20px;
            background-position: 0 0, 10px 10px;
            scroll-behavior: smooth;
        }

        #message-container ul {
            list-style-type: none;
            padding: 0;
            margin: 0;
        }

        #message-container li {
            margin-bottom: 18px;
            clear: both;
            display: flex;
            flex-direction: column;
            max-width: 85%;
        }

        /* Message Positioning */
        .my-message {
            align-self: flex-end;
            margin-left: auto;
        }

        .other-message {
            align-self: flex-start;
            margin-right: auto;
        }

        /* Message Content Styling */
        .message-content {
            display: flex;
            align-items: flex-end;
            gap: 8px;
        }

        .my-message .message-content {
            flex-direction: row-reverse;
        }

        /* Message Bubbles */
        .message {
            position: relative;
            padding: 12px 16px;
            border-radius: 18px;
            word-wrap: break-word;
            line-height: 1.4;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
            max-width: 100%;
        }

        .my-message .message {
            background-color: #0084ff;
            color: white;
            border-bottom-right-radius: 4px;
        }

        .other-message .message {
            background-color: #e4e6eb;
            color: #0e0b0b;
            border-bottom-left-radius: 4px;
        }

        /* Sender Name */
        .sender-name {
            font-size: 0.8em;
            margin-bottom: 4px;
            font-weight: 500;
        }

        .my-message .sender-name {
            text-align: right;
            color: #0084ff;
        }

        .other-message .sender-name {
            text-align: left;
            color: #65676b;
        }

        /* Timestamp */
        .timestamp-tooltip {
            display: block;
            font-size: 0.7em;
            margin-top: 4px;
            opacity: 0.7;
        }

        .my-message .timestamp-tooltip {
            color: #f0f0f0;
        }

        .other-message .timestamp-tooltip {
            color: #65676b;
        }

        /* Input Area */
        #message-form {
            padding: 12px 16px;
            background-color: #f5f5f5;
            border-top: 1px solid #e8e8e8;
        }

        .input-container {
            display: flex;
            align-items: center;
            background-color: white;
            border-radius: 24px;
            padding: 4px 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
        }

        #message-input {
            flex: 1;
            padding: 10px 16px;
            border: none;
            font-size: 0.95em;
            background: transparent;
            outline: none;
            border-radius: 24px;
        }

        .message-btn {
            background: none;
            border: none;
            cursor: pointer;
            padding: 8px;
            border-radius: 50%;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 38px;
            height: 38px;
            margin: 0 4px;
        }

        .message-btn:hover {
            background-color: #f0f0f0;
            transform: scale(1.05);
        }

        button[type="submit"].message-btn {
            background-color: #0084ff;
            margin-left: 8px;
        }

        button[type="submit"].message-btn img {
            filter: brightness(0) invert(1);
        }

        button[type="submit"].message-btn:hover {
            background-color: #0078e7;
        }

        /* Emoji Container */
        #emoji-container {
            display: flex;
            flex-wrap: wrap;
            padding: 12px;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            position: absolute;
            bottom: 80px;
            left: 16px;
            max-height: 200px;
            max-width: 320px;
            overflow-y: auto;
            z-index: 10;
            border: 1px solid #e8e8e8;
        }

        #emoji-container button {
            background: none;
            border: none;
            font-size: 1.5em;
            cursor: pointer;
            padding: 6px;
            transition: transform 0.2s ease;
            border-radius: 4px;
        }

        #emoji-container button:hover {
            transform: scale(1.2);
            background-color: #f5f5f5;
        }

        /* Image in Message */
        .message-image {
            margin-top: 10px;
            overflow: hidden;
        }

        .message-image img {
            border-radius: 12px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            max-width: 100%;
            transition: transform 0.3s ease;
            cursor: pointer;
        }

        .message-image img:hover {
            transform: scale(1.03);
        }

        /* Report Button */
        .report-button {
            opacity: 0.5;
            transition: opacity 0.2s ease, transform 0.2s ease;
            width: 18px !important;
            height: 18px !important;
        }

        .report-button:hover {
            opacity: 1;
            transform: scale(1.1);
        }

        /* Report Form Overlay */
        .report-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.6);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
            backdrop-filter: blur(3px);
            transition: all 0.3s ease;
        }

        .report-form {
            background-color: white;
            padding: 25px;
            border-radius: 16px;
            width: 90%;
            max-width: 500px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
            transform: translateY(0);
            transition: transform 0.3s ease;
            animation: slideUp 0.3s forwards;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .report-form h3 {
            margin-top: 0;
            color: #ff4757;
            text-align: center;
            font-size: 1.3em;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }

        .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            min-height: 120px;
            resize: vertical;
            font-family: inherit;
            transition: border-color 0.2s ease;
        }

        .form-group textarea:focus {
            border-color: #0084ff;
            outline: none;
            box-shadow: 0 0 0 2px rgba(0, 132, 255, 0.1);
        }

        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 12px;
        }

        .form-actions button {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.2s ease;
        }

        .form-actions button:first-child {
            background-color: #ff4757;
            color: white;
        }

        .form-actions button:first-child:hover {
            background-color: #ff3346;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(255, 71, 87, 0.3);
        }

        .form-actions button:last-child {
            background-color: #f0f0f0;
            color: #333;
        }

        .form-actions button:last-child:hover {
            background-color: #e0e0e0;
            transform: translateY(-2px);
        }

        /* Typing Indicator */
        .typing-indicator {
            display: flex;
            align-items: center;
            margin-left: 20px;
            margin-bottom: 20px;
            opacity: 0.7;
        }

        .typing-indicator span {
            height: 8px;
            width: 8px;
            margin: 0 1px;
            background-color: #606770;
            border-radius: 50%;
            display: inline-block;
            animation: bounce 1.3s linear infinite;
        }

        .typing-indicator span:nth-child(2) {
            animation-delay: 0.15s;
        }

        .typing-indicator span:nth-child(3) {
            animation-delay: 0.3s;
        }

        @keyframes bounce {
            0%, 60%, 100% {
                transform: translateY(0);
            }
            30% {
                transform: translateY(-4px);
            }
        }

        /* Scrollbar Styling */
        ::-webkit-scrollbar {
            width: 6px;
        }

        ::-webkit-scrollbar-track {
            background: transparent;
        }

        ::-webkit-scrollbar-thumb {
            background: #c1c1c1;
            border-radius: 10px;
        }

        ::-webkit-scrollbar-thumb:hover {
            background: #a8a8a8;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .chat-container {
                margin: 10px;
                height: calc(100vh - 140px);
                border-radius: 12px;
            }

            .message {
                padding: 10px 14px;
                font-size: 0.9em;
            }

            #message-container li {
                max-width: 90%;
            }

            .report-form {
                width: 95%;
                padding: 20px;
            }
        }

        @media (max-width: 900px) {
            .chat-container {
                height: 70vh;
            }
        }

        /* Message Status Indicator */
        .message-status {
            font-size: 0.7em;
            margin-top: 2px;
            text-align: right;
            color: rgba(255, 255, 255, 0.7);
        }

        /* Image Upload Preview */
        #image-preview {
            margin-top: 8px;
            max-width: 100px;
            max-height: 100px;
            border-radius: 8px;
            object-fit: cover;
            display: none;
        }

        /* Empty State */
        .empty-chat {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100%;
            color: #65676b;
            text-align: center;
            padding: 20px;
        }

        .empty-chat img {
            width: 80px;
            height: 80px;
            margin-bottom: 16px;
            opacity: 0.6;
        }

        .empty-chat h3 {
            font-weight: 500;
            margin-bottom: 8px;
        }

        .empty-chat p {
            font-size: 0.9em;
            max-width: 80%;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>
<div class="chat-container">
    <div class="chat-header">Phòng chat Cộng đồng Hankyo</div>
    <div id="message-container">
        <ul>
            <c:forEach items="${messages}" var="message">
                <li class="${message.userID == userID ? 'my-message' : 'other-message'}">
                    <span class="sender-name">${message.fullName}</span>
                    <div class="message-content">
                        <div class="message">
                                ${message.message}
                            <c:if test="${not empty message.pictureSend}">
                                <div class="message-image">
                                    <img src="${message.pictureSend}" alt="Sent image"
                                         style="max-width: 200px; max-height: 200px;">
                                </div>
                            </c:if>
                            <span class="timestamp-tooltip">
    <fmt:formatDate value="${message.sendAt}" pattern="HH:mm"/>
    •
    <fmt:formatDate value="${message.sendAt}" pattern="dd/MM/yyyy"/>
</span></div>
                        <c:if test="${message.userID != sessionScope.user.userID}">
                            <i class="fas fa-flag report-button"
                               onclick="showReportForm('${message.chatID}', '${message.userID}')"></i>
                        </c:if>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>

    <form id="message-form">
        <div class="input-container">
            <input type="text" id="message-input" placeholder="Type your message...">
            <button type="button" id="emoji-button" class="message-btn" style="font-size: 20px; color: #333;">😊</button>
            <label for="image-upload" class="message-btn">
                <i class="fa-regular fa-image" style="font-size: 20px;"></i>
                <input type="file" id="image-upload" accept="image/*" style="display: none;">
            </label>
            <button type="submit" class="message-btn">
                <i class="fa-solid fa-paper-plane" style="font-size: 20px;"></i>
            </button>
        </div>
        <div id="emoji-container" style="display: none;">
            <!-- Emoji buttons will be added here -->
        </div>
    </form>
</div>

<!-- Report Form Overlay -->
<div id="report-overlay" class="report-overlay" style="display: none;">
    <div class="report-form">
        <h3>Báo cáo tin nhắn</h3>
        <form id="report-form" onsubmit="return false;">
            <input type="hidden" id="report-chatID" name="chatID" value="">
            <input type="hidden" id="report-reporterID" name="reporterID" value="">
            <input type="hidden" id="report-reportedUserID" name="reportedUserID" value="">

            <div class="form-group">
                <label for="report-description">Mô tả chi tiết:</label>
                <textarea id="report-description" name="description" required></textarea>
            </div>

            <div class="form-actions">
                <button type="button" onclick="submitReport()">Gửi báo cáo</button>
                <button type="button" onclick="closeReportForm()">Hủy</button>
            </div>
        </form>
    </div>
</div>

<!-- Hidden inputs for user info -->
<input type="hidden" id="userID" value="${userID}">
<input type="hidden" id="fullName" value="${fullName}">

<!-- Chat JavaScript -->
<script src="${pageContext.request.contextPath}/js/chat.js"></script>
<c:import url="footer.jsp"/>
</body>
</html>
