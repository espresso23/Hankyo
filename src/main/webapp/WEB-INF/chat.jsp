<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Chat Room</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/asset/css/chat.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        // Kiểm tra lỗi load JavaScript
        window.onerror = function(msg, url, line) {
            console.error('Error: ' + msg + '\nURL: ' + url + '\nLine: ' + line);
            return false;
        };
    </script>
</head>
<body>
<div class="chat-container">
    <div class="chat-header">
        <h2>Global Chat</h2>
        <button onclick="closeChat()">Close</button>
    </div>

    <div id="message-container">
        <ul>
            <c:forEach items="${messages}" var="message">
                <li class="${message.userID == userID ? 'my-message' : 'other-message'}">
                    <span class="sender-name">${message.fullName}</span>
                    <div class="message-content">
                        <div class="message">
                            ${message.message}
                            <span class="timestamp-tooltip">${message.sendAt}</span>
                        </div>
                        <c:if test="${message.userID != sessionScope.user.userID}">
                            <button class="report-button" onclick="showReportForm('${message.chatID}', '${message.userID}')">Report</button>
                        </c:if>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>

    <form id="message-form">
        <div class="input-container">
            <input type="text" id="message-input" placeholder="Type your message...">
            <button type="button" id="emoji-button">😊</button>
            <button type="submit">Send</button>
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
<script src="${pageContext.request.contextPath}/asset/js/chat.js"></script>
</body>
</html>