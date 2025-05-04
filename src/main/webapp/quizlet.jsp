<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/quizlet.css">
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <title>Quizlet - Select Topic</title>
    <style>
        :root {
            --pink-light: #ffe6f0;
            --pink-medium: #ffb6d5;
            --pink-dark: #ff9ec7;
            --blue-light: #d6f5f2;
            --blue-medium: #a0e6e0;
            --blue-dark: #7ad4cd;
            --text-dark: #4a4a4a;
            --text-light: #ffffff;
            --shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            --border-radius: 16px;
        }

        body {
            font-family: 'Nunito', sans-serif;
            background: linear-gradient(135deg, var(--pink-light) 0%, var(--blue-light) 100%);
            background-attachment: fixed;
            margin: 0;
            min-height: 100vh;
            color: var(--text-dark);
        }

        .file-wrapper {
            max-width: 1200px;
            margin: 60px auto 30px auto;
            padding: 0 20px;
        }

        /* Header Style */
        .quizlet-header {
            position: relative;
            margin-bottom: 20px;
            padding: 0 20px;
        }

        .header-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .header-title {
            color: var(--text-dark);
            font-size: 28px;
            font-weight: 700;
            margin: 0;
            display: flex;
            align-items: center;
        }

        .header-title i {
            margin-right: 12px;
            color: var(--pink-medium);
            font-size: 32px;
        }

        /* Floating Tabs Navigation */
        .tabs-nav {
            display: flex;
            background: white;
            border-radius: 50px;
            padding: 8px;
            box-shadow: var(--shadow);
            overflow-x: auto;
            scrollbar-width: none;
        }

        .tabs-nav::-webkit-scrollbar {
            display: none;
        }

        .tab-btn {
            padding: 10px 24px;
            border: none;
            background: transparent;
            color: var(--text-dark);
            font-weight: 600;
            cursor: pointer;
            border-radius: 50px;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            white-space: nowrap;
            font-size: 15px;
            margin: 0 2px;
        }

        .tab-btn i {
            margin-right: 8px;
            font-size: 16px;
            transition: all 0.3s ease;
        }

        .tab-btn:hover {
            background: rgba(255, 182, 213, 0.1);
            color: var(--pink-dark);
        }

        .tab-btn:hover i {
            transform: scale(1.1);
        }

        .tab-btn.active {
            background: linear-gradient(135deg, var(--pink-medium) 0%, var(--blue-medium) 100%);
            color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .tab-btn.active i {
            color: white;
        }

        /* Container Style */
        .container {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(6px);
            border-radius: var(--border-radius);
            box-shadow: var(--shadow);
            overflow: hidden;
            padding: 30px;
            border: 1px solid rgba(255, 255, 255, 0.8);
        }

        /* Tab Content */
        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
            animation: fadeIn 0.5s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .tab-content h2 {
            color: var(--pink-medium);
            margin-top: 0;
            margin-bottom: 25px;
            font-size: 24px;
            font-weight: 700;
            text-align: center;
            position: relative;
        }

        .tab-content h2::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 4px;
            background: linear-gradient(90deg, var(--pink-medium) 0%, var(--blue-medium) 100%);
            border-radius: 2px;
        }

        /* Topics Grid - 3 cards per row */
        .topics-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 25px;
        }

        .topic-card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: var(--border-radius);
            padding: 25px;
            box-shadow: var(--shadow);
            transition: all 0.3s ease;
            border: 1px solid rgba(255, 255, 255, 0.9);
            position: relative;
            overflow: hidden;
        }

        .topic-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
            border-color: var(--blue-medium);
        }

        .topic-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 5px;
            background: linear-gradient(90deg, var(--pink-medium) 0%, var(--blue-medium) 100%);
        }

        .card-icon {
            font-size: 36px;
            color: var(--pink-medium);
            margin-bottom: 15px;
            transition: all 0.3s ease;
        }

        .topic-card:hover .card-icon {
            color: var(--pink-dark);
            transform: scale(1.1) rotate(5deg);
        }

        .card-title {
            margin: 0 0 10px;
            font-size: 18px;
            font-weight: 700;
        }

        .card-title a {
            color: var(--text-dark);
            text-decoration: none;
            transition: color 0.3s;
        }

        .topic-card:hover .card-title a {
            color: var(--pink-dark);
        }

        .card-meta {
            font-size: 14px;
            color: #777;
            background: rgba(160, 230, 224, 0.2);
            padding: 6px 12px;
            border-radius: 50px;
            display: inline-block;
        }

        /* Alert Messages */
        .alert {
            padding: 15px;
            border-radius: var(--border-radius);
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-success {
            background: rgba(0, 200, 150, 0.1);
            border: 1px solid rgba(0, 200, 150, 0.2);
            color: #00c896;
        }

        .alert-danger {
            background: rgba(255, 100, 120, 0.1);
            border: 1px solid rgba(255, 100, 120, 0.3);
            color: #ff6478;
        }

        /* Public Cards Specific Styling */
        .public-card .card-icon {
            color: var(--blue-medium);
        }

        .public-card .card-description {
            color: #666;
            font-size: 14px;
            margin: 10px 0;
        }

        .public-card .card-author {
            font-size: 13px;
            color: #888;
        }

        /* Add Flashcard Form */
        .add-flashcard-form {
            background: rgba(255, 255, 255, 0.95);
            border-radius: var(--border-radius);
            padding: 30px;
            max-width: 600px;
            margin: 0 auto;
            box-shadow: 0 8px 24px rgba(180, 235, 230, 0.3);
            border: 1px solid rgba(255, 255, 255, 0.9);
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: var(--text-dark);
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid rgba(160, 230, 224, 0.5);
            border-radius: 12px;
            font-size: 16px;
            font-family: 'Nunito', sans-serif;
            transition: all 0.3s ease;
            background: rgba(255, 255, 255, 0.8);
        }

        .form-control:focus {
            border-color: var(--blue-medium);
            box-shadow: 0 0 10px rgba(137, 247, 254, 0.3);
            outline: none;
        }

        textarea.form-control {
            min-height: 100px;
            resize: vertical;
        }

        .btn {
            display: inline-block;
            padding: 12px 24px;
            background: linear-gradient(135deg, var(--pink-medium) 0%, var(--blue-medium) 100%);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-align: center;
        }

        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 15px rgba(255, 158, 187, 0.3);
            background: linear-gradient(135deg, var(--pink-dark) 0%, var(--blue-dark) 100%);
        }

        .btn-block {
            display: block;
            width: 100%;
        }

        /* Input Mode Toggle */
        .input-mode-toggle {
            display: flex;
            margin-bottom: 20px;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .input-mode-btn {
            flex: 1;
            padding: 12px;
            background: rgba(255, 255, 255, 0.7);
            border: none;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .input-mode-btn i {
            margin-right: 8px;
        }

        .input-mode-btn.active {
            background: linear-gradient(135deg, var(--pink-medium) 0%, var(--blue-medium) 100%);
            color: white;
        }

        /* Responsive Adjustments */
        @media (max-width: 992px) {
            .topics-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 768px) {
            .topics-grid {
                grid-template-columns: 1fr;
            }

            .tab-content {
                padding: 20px;
            }

            .header-title {
                font-size: 24px;
            }

            .tab-btn {
                padding: 8px 15px;
                font-size: 14px;
            }
        }

        @media (max-width: 480px) {
            .input-mode-toggle {
                flex-direction: column;
            }

            .add-flashcard-form {
                padding: 20px;
            }

            .header-title {
                font-size: 22px;
            }

            .header-title i {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<!-- Header Outside Container -->
<div class="quizlet-header">
    <div class="header-content">
        <h1 class="header-title"><i class="fas fa-layer-group"></i> My Flashcards</h1>
    </div>
    <div class="tabs-nav">
        <button class="tab-btn active" data-tab="system"><i class="fas fa-book"></i> System</button>
        <button class="tab-btn" data-tab="custom"><i class="fas fa-edit"></i> My Cards</button>
        <button class="tab-btn" data-tab="favorite"><i class="fas fa-star"></i> Favorites</button>
        <button class="tab-btn" data-tab="add"><i class="fas fa-plus"></i> Add New</button>
        <button class="tab-btn" data-tab="public"><i class="fas fa-globe"></i> Public</button>
    </div>
</div>

<div class="file-wrapper">
    <div class="container">
        <!-- System Flashcards Tab -->
        <div class="tab-content active" id="system">
            <h2>System Flashcards</h2>
            <div class="topics-grid">
                <c:forEach var="item" items="${systemTopics}" varStatus="status">
                    <div class="topic-card" data-topic="${item}" data-type="system">
                        <div class="card-icon"><i class="fas fa-book-open"></i></div>
                        <h3 class="card-title"><a href="flashCard?topic=${item}&type=system">${item}</a></h3>
                        <div class="card-meta">System Cards</div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${not empty systemError}">
                <div class="alert alert-danger">${systemError}</div>
            </c:if>
        </div>

        <!-- Custom Flashcards Tab -->
        <div class="tab-content" id="custom">
            <h2>My Flashcards</h2>
            <div class="topics-grid">
                <c:forEach var="item" items="${customTopics}" varStatus="status">
                    <div class="topic-card" data-topic="${item}" data-type="custom">
                        <div class="card-icon"><i class="fas fa-edit"></i></div>
                        <h3 class="card-title"><a href="flashCard?topic=${item}&type=custom">${item}</a></h3>
                        <div class="card-meta"><c:out value="${customTopicCounts[item]}"/> cards</div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${not empty customError}">
                <div class="alert alert-danger">${customError}</div>
            </c:if>
        </div>

        <!-- Favorite Flashcards Tab -->
        <div class="tab-content" id="favorite">
            <h2>Favorite Flashcards</h2>
            <div class="topics-grid">
                <c:forEach var="item" items="${favoriteTopics}" varStatus="status">
                    <div class="topic-card" data-topic="${item}" data-type="favorite">
                        <div class="card-icon"><i class="fas fa-star"></i></div>
                        <h3 class="card-title"><a href="flashCard?topic=${item}&type=favorite">${item}</a></h3>
                        <div class="card-meta">Favorite Cards</div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${not empty favoriteError}">
                <div class="alert alert-danger">${favoriteError}</div>
            </c:if>
        </div>

        <!-- Public Flashcards Tab -->
        <div class="tab-content" id="public">
            <h2>Public Flashcards</h2>
            <div class="topics-grid">
                <c:forEach var="card" items="${publicFlashCards}">
                    <div class="topic-card public-card" data-topic="${card.topic}" data-type="public">
                        <div class="card-icon"><i class="fas fa-globe"></i></div>
                        <h3 class="card-title"><a href="flashCard?topic=${card.topic}&type=custom&learnerID=${card.learnerID}">${card.topic}</a></h3>
                        <div class="card-description">${card.mean}</div>
                        <div class="card-author">By <c:out value="${publicLearnerNames[card.learnerID]}"/></div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${not empty publicError}">
                <div class="alert alert-danger">${publicError}</div>
            </c:if>
        </div>

        <!-- Add Flashcard Tab -->
        <div class="tab-content" id="add">
            <div class="add-flashcard-form">
                <h2>Add New Flashcards</h2>

                <c:if test="${not empty successMessages}">
                    <div class="alert alert-success">
                        <p><i class="fas fa-check-circle"></i> Added successfully:</p>
                        <ul>
                            <c:forEach var="msg" items="${successMessages}">
                                <li>${msg}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <c:if test="${not empty errorMessages}">
                    <div class="alert alert-danger">
                        <p><i class="fas fa-exclamation-circle"></i> Errors:</p>
                        <ul>
                            <c:forEach var="msg" items="${errorMessages}">
                                <li>${msg}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <div class="input-mode-toggle">
                    <button type="button" class="input-mode-btn active" data-mode="manual">
                        <i class="fas fa-bolt"></i> Quick Add
                    </button>
                    <button type="button" class="input-mode-btn" data-mode="individual">
                        <i class="fas fa-keyboard"></i> Single Add
                    </button>
                </div>

                <form id="flashcardForm" method="post">
                    <div class="manual-input active">
                        <div class="form-group">
                            <label for="manualTopic"><i class="fas fa-tag"></i> Topic Name</label>
                            <input type="text" id="manualTopic" name="manualTopic" class="form-control" placeholder="Enter topic name" required>
                        </div>
                        <div class="form-group">
                            <label for="manualFlashCards"><i class="fas fa-list"></i> Flashcards (word:meaning pairs)</label>
                            <textarea id="manualFlashCards" name="manualFlashCards" class="form-control" placeholder="Enter word:meaning pairs (e.g. hello:xin chào;good:tốt)" required></textarea>
                        </div>
                    </div>

                    <div class="individual-input">
                        <div class="form-group">
                            <label for="individualTopic"><i class="fas fa-tag"></i> Topic Name</label>
                            <input type="text" id="individualTopic" name="individualTopic" class="form-control" placeholder="Enter topic name">
                        </div>
                        <div class="form-group">
                            <label for="word"><i class="fas fa-font"></i> Word</label>
                            <input type="text" id="word" name="word" class="form-control" placeholder="Enter word">
                        </div>
                        <div class="form-group">
                            <label for="mean"><i class="fas fa-comment"></i> Meaning</label>
                            <input type="text" id="mean" name="mean" class="form-control" placeholder="Enter meaning">
                        </div>
                    </div>

                    <button type="submit" class="btn btn-block">
                        <i class="fas fa-plus-circle"></i> Add Flashcards
                    </button>
                </form>

                <div style="margin-top:20px;font-size:14px;color:#666;text-align:center;">
                    <p><i class="fas fa-info-circle"></i> For quick add, separate multiple flashcards with semicolons (;) and format each pair as "word:meaning"</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    window.contextPath = '${pageContext.request.contextPath}';

    // Tab switching functionality
    document.addEventListener('DOMContentLoaded', function() {
        const tabBtns = document.querySelectorAll('.tab-btn');
        const tabContents = document.querySelectorAll('.tab-content');

        tabBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                // Remove active class from all tabs and contents
                tabBtns.forEach(t => t.classList.remove('active'));
                tabContents.forEach(content => content.classList.remove('active'));

                // Add active class to clicked tab
                btn.classList.add('active');

                // Show corresponding content
                const tabId = btn.getAttribute('data-tab');
                document.getElementById(tabId).classList.add('active');
            });
        });

        // Input mode toggle
        const modeBtns = document.querySelectorAll('.input-mode-btn');
        const manualInput = document.querySelector('.manual-input');
        const individualInput = document.querySelector('.individual-input');

        modeBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                modeBtns.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');

                const mode = btn.getAttribute('data-mode');
                if (mode === 'manual') {
                    manualInput.classList.add('active');
                    individualInput.classList.remove('active');
                } else {
                    manualInput.classList.remove('active');
                    individualInput.classList.add('active');
                }
            });
        });

        // Animate cards on load
        const cards = document.querySelectorAll('.topic-card');
        cards.forEach((card, index) => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(20px)';
            card.style.transition = 'opacity 0.5s ease, transform 0.5s ease';

            setTimeout(() => {
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            }, index * 100);
        });
    });
</script>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>