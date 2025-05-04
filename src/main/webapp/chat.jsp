<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
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
<jsp:include page="header.jsp"></jsp:include>
<div class="chat-container">
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
                                    <img src="${message.pictureSend}" alt="Sent image" style="max-width: 200px; max-height: 200px;">
                                </div>
                            </c:if>
                            <span class="timestamp-tooltip">${message.sendAt}</span>
                        </div>
                        <c:if test="${message.userID != sessionScope.user.userID}">
                            <img src="${pageContext.request.contextPath}/asset/png/icon/report.png" 
                                 alt="Report" 
                                 class="report-button" 
                                 onclick="showReportForm('${message.chatID}', '${message.userID}')"
                                 style="width: 20px; height: 20px; cursor: pointer;">
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
                <img src="${pageContext.request.contextPath}/asset/png/icon/image-upload.png" alt="Upload Image" style="width: 20px; height: 20px;">
                <input type="file" id="image-upload" accept="image/*" style="display: none;">
            </label>
            <button type="submit" class="message-btn">
                <img src="${pageContext.request.contextPath}/asset/png/icon/send.png" alt="Send" style="width: 20px; height: 20px;">
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
<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>
