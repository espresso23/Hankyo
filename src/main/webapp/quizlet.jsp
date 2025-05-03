<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/quizlet.css">
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Quizlet - Select Topic</title>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(90deg, #ffd6ec 0%, #b4ebe6 100%);
            background-size: cover;
            margin: 0;
            min-height: 100vh;
        }
        .file-wrapper {
            max-width: 1200px;
            margin: 40px auto 70px auto;
            position: relative;
        }
        .container {
            width: 100%;
            padding: 48px 32px 32px 32px;
            background: #fff;
            border-radius: 28px;
            border: none;
            box-shadow: 0 8px 32px 0 rgba(180, 235, 230, 0.18), 0 1.5px 0 #ffd6ec inset;
            min-height: 70vh;
            position: relative;
            z-index: 1;
        }
        .file-tab {
            position: absolute;
            top: -32px;
            left: 36px;
            min-width: 180px;
            height: 48px;
            background: linear-gradient(90deg, #ffd6ec 0%, #b4ebe6 100%);
            border: none;
            border-top-left-radius: 18px;
            border-top-right-radius: 18px;
            box-shadow: 0 6px 18px rgba(180,235,230,0.13);
            display: flex;
            align-items: center;
            padding: 0 32px;
            font-size: 22px;
            font-weight: bold;
            color: #1976D2;
            z-index: 2;
            letter-spacing: 1px;
        }
        .overlay-image {
            position: absolute;
            top: -75px; /* Đè nửa trên */
            left: 32px;
            width: 200px; /* Nhỏ hơn 300px cho phù hợp */
            height: 150px;
            object-fit: cover;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            z-index: 10;
            transition: transform 0.2s ease;
        }
        .overlay-image:hover {
            transform: scale(1.05); /* Hiệu ứng tương tự favorite-btn */
        }
        .tabs {
            display: flex;
            flex-direction: row;
            align-items: flex-start;
            justify-content: flex-start;
            gap: 0;
            margin-bottom: -18px;
            position: relative;
            z-index: 10;
            width: 100%;
        }
        .tab {
            display: flex;
            align-items: center;
            gap: 0;
            width: 44px;
            height: 44px;
            padding: 0 8px;
            font-size: 0px;
            color: #fff;
            background: linear-gradient(90deg, #ff6fae 0%, #3ec6e0 100%);
            border-top-left-radius: 18px;
            border-top-right-radius: 18px;
            border-bottom-left-radius: 0;
            border-bottom-right-radius: 0;
            margin-right: 2px;
            margin-bottom: 0;
            box-shadow: 0 2px 8px rgba(180,235,230,0.18);
            cursor: pointer;
            transition: 
                background 0.3s,
                color 0.3s,
                width 0.25s cubic-bezier(.4,2,.6,1),
                font-size 0.18s,
                box-shadow 0.18s;
            position: relative;
            z-index: 11;
            overflow: hidden;
        }
        .tab .folder-icon {
            font-size: 22px;
            margin-right: 0;
            transition: font-size 0.18s;
        }
        .tab .tab-label {
            opacity: 0;
            white-space: nowrap;
            margin-left: 0;
            font-size: 16px;
            transition: opacity 0.18s, margin-left 0.18s;
        }
        .tab.active,
        .tab:hover {
            background: linear-gradient(90deg, #3ec6e0 0%, #ff6fae 100%);
            color: #fff;
            font-weight: bold;
            box-shadow: 0 8px 24px rgba(180,235,230,0.22);
            width: 180px;
            font-size: 18px;
            z-index: 12;
            transform: translateY(8px) scale(1.08);
        }
        .tab.active .tab-label,
        .tab:hover .tab-label {
            opacity: 1;
            margin-left: 10px;
        }
        .tab.active .folder-icon,
        .tab:hover .folder-icon {
            font-size: 26px;
        }
        .tab-content {
            display: none;
            border: none;
        }
        .tab-content.active {
            display: block;
            border: none;
        }
        .topics-scroll-container {
            border: none !important;
            box-shadow: none !important;
        }
        .topics-scroll {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 24px 12px;
            width: 100%;
            background: none;
            border-radius: 0;
            box-shadow: none !important;
            max-height: none;
            overflow: visible !important;
            padding: 24px 0 0 0;
            margin: 0;
        }
        .topics-scroll::-webkit-scrollbar {
            display: none !important;
        }
        .topic-box {
            background: none;
            border: none;
            border-radius: 0;
            box-shadow: none;
            transition: color 0.18s, transform 0.18s;
            padding: 0;
            margin: 0;
            min-width: 0;
            min-height: 80px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', 'Arial', sans-serif;
            font-size: 16px;
            position: relative;
            cursor: pointer;
        }
        .topic-box .file-icon {
            font-size: 38px;
            margin-bottom: 6px;
            color: #7ed6df;
            filter: drop-shadow(0 1px 1px #ffd6ec);
        }
        .topic-box a {
            text-decoration: none;
            color: #222;
            font-size: 16px;
            font-family: inherit;
            font-weight: 600;
            margin-bottom: 4px;
            display: block;
            text-align: center;
            transition: color 0.2s;
        }
        .topic-box:hover a {
            color: #1976D2;
        }
        .topic-count {
            font-size: 12px;
            color: #888;
            font-weight: 400;
            background: none;
            padding: 0;
            border-radius: 0;
            text-align: center;
        }
        .scroll-btn {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            background-color: #2196F3; /* Đồng bộ với custom-btn */
            color: #fff;
            border: none;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            cursor: pointer;
            font-size: 16px;
            line-height: 30px;
            text-align: center;
            opacity: 0.8;
            transition: opacity 0.3s, transform 0.2s;
        }
        .scroll-btn:hover {
            opacity: 1;
            transform: translateY(-50%) scale(1.1);
        }
        .scroll-btn.left {
            left: 5px;
        }
        .scroll-btn.right {
            right: 5px;
        }
        .error {
            color: red;
            text-align: center;
            margin: 20px 0;
        }
        .add-flashcard-form {
            background-color: #ffffff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 450px;
            margin: 20px auto;
        }
        .add-flashcard-form h2 {
            text-align: center;
            color: #1a1a1a; /* Từ custom-topics-title */
            margin-bottom: 25px;
            font-size: 24px;
            font-weight: 600;
        }
        .add-flashcard-form form {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }
        .add-flashcard-form input[type="text"] {
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 16px;
            outline: none;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            background-color: #f9f9f9;
        }
        .add-flashcard-form input[type="text"]:focus {
            border-color: #2196F3;
            box-shadow: 0 0 5px rgba(33, 150, 243, 0.3);
        }
        .add-flashcard-form input[type="text"]::placeholder {
            color: #999;
        }
        .add-flashcard-form button {
            padding: 12px;
            background-color: #2196F3;
            color: #ffffff;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }
        .add-flashcard-form button:hover {
            background-color: #1976D2;
            transform: translateY(-2px);
        }
        .add-flashcard-form button:active {
            transform: translateY(0);
        }
        .add-flashcard-form .toggle-btn {
            background-color: #4CAF50; /* Đồng bộ với favorite-btn */
            margin-bottom: 15px;
            font-size: 14px;
            padding: 10px;
        }
        .add-flashcard-form .toggle-btn:hover {
            background-color: #45a049;
        }
        .add-flashcard-form .manual-input, .add-flashcard-form .individual-input {
            display: none;
        }
        .add-flashcard-form .manual-input.active, .add-flashcard-form .individual-input.active {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }
        .add-flashcard-form .note {
            font-size: 13px;
            color: #7f8c8d;
            text-align: center;
            margin-top: 15px;
            line-height: 1.4;
        }
        .add-flashcard-form .success, .add-flashcard-form .error-list {
            margin-bottom: 20px;
        }
        .add-flashcard-form .success ul, .add-flashcard-form .error-list ul {
            list-style: none;
            padding: 0;
        }
        .add-flashcard-form .success li {
            color: #4CAF50;
        }
        .add-flashcard-form .error-list li {
            color: red;
        }
        .add-flashcard-form .result-container {
            margin-bottom: 20px;
        }
        .container, .topic-box {
            transition: box-shadow 0.25s, border 0.18s, background 0.25s;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="file-wrapper">
    <div class="file-tab">Quizlet File</div>
    <div class="tabs">
        <div class="tab active" data-tab="system"><span class="tab-label">System Flashcards</span></div>
        <div class="tab" data-tab="custom"><span class="tab-label">Custom Flashcards</span></div>
        <div class="tab" data-tab="favorite"><span class="tab-label">Favorite Flashcards</span></div>
        <div class="tab" data-tab="add"><span class="tab-label">Add Flashcard</span></div>
    </div>
    <div class="container">
        <!-- System Flashcards Tab -->
        <div class="tab-content active" id="system">
            <h2>System Flashcards</h2>
            <div class="topics-scroll-container">
                <div class="topics-scroll">
                    <c:forEach var="item" items="${systemTopics}" varStatus="status">
                        <div class="topic-box" data-topic="${item}" data-type="system">
                            <span class="file-icon">🗂️</span>
                            <a href="flashCard?topic=${item}&type=system">${item}</a>
                            <span class="topic-count">0 từ</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <c:if test="${not empty systemError}">
                <p class="error">${systemError}</p>
            </c:if>
        </div>

        <!-- Custom Flashcards Tab -->
        <div class="tab-content" id="custom">
            <h2>Custom Flashcards</h2>
            <div class="topics-scroll-container">
                <div class="topics-scroll">
                    <c:forEach var="item" items="${customTopics}" varStatus="status">
                        <div class="topic-box" data-topic="${item}" data-type="custom">
                            <span class="file-icon">🗂️</span>
                            <a href="flashCard?topic=${item}&type=custom">${item}</a>
                            <span class="topic-count">
                                <c:out value="${customTopicCounts[item]}"/> từ
                            </span>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <c:if test="${not empty customError}">
                <p class="error">${customError}</p>
            </c:if>
        </div>

        <!-- Favorite Flashcards Tab -->
        <div class="tab-content" id="favorite">
            <h2>Favorite Flashcards</h2>
            <div class="topics-scroll-container">
                <div class="topics-scroll">
                    <c:forEach var="item" items="${favoriteTopics}" varStatus="status">
                        <div class="topic-box" data-topic="${item}" data-type="favorite">
                            <span class="file-icon">🗂️</span>
                            <a href="flashCard?topic=${item}&type=favorite">${item}</a>
                            <span class="topic-count">0 từ</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <c:if test="${not empty favoriteError}">
                <p class="error">${favoriteError}</p>
            </c:if>
        </div>

        <!-- Add Flashcard Tab -->
        <div class="tab-content" id="add">
            <div class="add-flashcard-form">
                <h2>Thêm Flashcard Mới</h2>
                <c:if test="${not empty successMessages}">
                    <div class="success">
                        <p>Thêm thành công:</p>
                        <ul>
                            <c:forEach var="msg" items="${successMessages}">
                                <li>${msg}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
                <c:if test="${not empty errorMessages}">
                    <div class="error-list">
                        <p>Lỗi:</p>
                        <ul>
                            <c:forEach var="msg" items="${errorMessages}">
                                <li>${msg}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
                <button class="toggle-btn" onclick="toggleInputMode()">Chuyển sang nhập từng ô</button>
                <form id="flashcardForm" method="post">
                    <div class="manual-input active">
                        <input type="text" id="manualTopic" name="manualTopic" placeholder="Nhập Topic" required />
                        <input type="text" id="manualFlashCards" name="manualFlashCards"
                               placeholder="Nhập từ:nghĩa (VD: hello:xin chào;good:tốt)" required />
                    </div>
                    <div class="individual-input">
                        <input type="text" id="individualTopic" name="individualTopic" placeholder="Nhập Topic" />
                        <input type="text" id="word" name="word" placeholder="Nhập từ" />
                        <input type="text" id="mean" name="mean" placeholder="Nhập nghĩa" />
                    </div>
                    <button type="submit">Thêm Flashcard</button>
                </form>
                <p class="note">Lưu ý: Nhập nhiều flashcard cách nhau bằng dấu ";", mỗi cặp theo cú pháp "từ:nghĩa" (cho chế độ thủ công).</p>
            </div>
        </div>
    </div>
</div>

<script>
    window.contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/asset/js/quizlet.js"></script>
<script src="${pageContext.request.contextPath}/asset/js/quizlet-add.js"></script>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>