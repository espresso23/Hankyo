<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trò chơi trí nhớ - ${topic}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/memory-game.css">
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&family=Nanum+Gothic:wght@400;700&family=Comfortaa:wght@400;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --pink-light: #fff0f6;
            --pink-medium: #ffd6ec;
            --pink-dark: #ffb8e0;
            --blue-light: #f3f8fd;
            --blue-medium: #e5f6fb;
            --blue-dark: #b8e0f7;
            --purple-light: #f6eaff;
            --yellow-light: #fffbe8;
            --white: #fff;
            --text-dark: #4A4A68;
            --main-pink: #ffb8e0;
        }

        body {
            font-family: 'Comfortaa', 'Noto Sans KR', 'Nanum Gothic', Arial, sans-serif;
            background-color: var(--white);
            background-image: none;
            background: linear-gradient(135deg, var(--pink-light) 0%, var(--blue-light) 100%);
            background-size: cover;
            margin: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            overflow-x: hidden;
            color: var(--text-dark);
        }

        .game-wrapper {
            margin-top: 60px;
            padding: 20px;
            background: rgba(255, 255, 255, 0.97);
            border-radius: 18px;
            box-shadow: 0 4px 16px rgba(149, 157, 165, 0.10);
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            max-width: 95%;
            border: 1.5px solid var(--blue-light);
        }

        .container {
            width: 100%;
            margin: 0;
            padding: 20px;
            background: rgba(255, 255, 255, 0.85);
            border-radius: 14px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
            overflow: hidden;
            border: 1px solid rgba(255, 255, 255, 0.4);
        }

        .game-header {
            text-align: center;
            margin-bottom: 18px;
            padding: 10px;
            background: linear-gradient(135deg, var(--blue-light) 0%, var(--pink-light) 100%);
            border-radius: 10px;
            color: #5a6a7a;
            text-shadow: none;
        }

        .game-header h1 {
            margin: 0;
            font-size: 1.6em;
            font-weight: 700;
            letter-spacing: 0.5px;
        }

        .game-header p {
            margin: 8px 0 0;
            font-size: 1.05em;
            opacity: 0.85;
        }

        .game-info {
            display: flex;
            justify-content: space-around;
            align-items: center;
            margin-bottom: 18px;
            padding: 10px;
            background: linear-gradient(135deg, var(--purple-light) 0%, var(--yellow-light) 100%);
            border-radius: 10px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.03);
        }

        .score, .moves {
            font-size: 1.1em;
            font-weight: 600;
            color: var(--text-dark);
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .score::before {
            content: "❤️";
        }

        .moves::before {
            content: "↻";
        }

        .game-container {
            display: grid;
            grid-template-columns: repeat(5, 1fr);
            gap: 15px;
            margin-bottom: 15px;
            width: 100%;
        }

        .card {
            aspect-ratio: 3/4;
            perspective: 1500px;
            cursor: pointer;
            min-width: 0;
            min-height: 110px;
            border-radius: 10px;
            box-shadow: 0 4px 16px rgba(255, 184, 224, 0.13), 0 2px 8px rgba(255, 184, 224, 0.10);
            transition: all 0.25s cubic-bezier(0.25, 0.8, 0.25, 1);
            background: #fff;
            border: 2.5px solid var(--main-pink);
        }

        .card:hover {
            transform: translateY(-3px) scale(1.02);
            box-shadow: 0 6px 16px rgba(74, 74, 104, 0.10);
        }

        .card-inner {
            position: relative;
            width: 100%;
            height: 100%;
            text-align: center;
            transition: transform 0.5s cubic-bezier(0.4, 0.2, 0.2, 1);
            transform-style: preserve-3d;
        }

        .card.flipped .card-inner {
            transform: rotateY(180deg);
        }

        .card-front, .card-back {
            position: absolute;
            width: 100%;
            height: 100%;
            backface-visibility: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 10px;
            padding: 10px;
            font-weight: 600;
            word-break: break-word;
            overflow: hidden;
        }

        .card-front {
            border: 2.5px dashed var(--main-pink);
            color: var(--main-pink);
            font-size: 2.5em;
            font-weight: bold;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #fff 0%, #fff0f6 100%);
        }

        .card-front::before { display: none !important; content: none !important; }

        .card-back {
            transform: rotateY(180deg);
            font-size: 1.1em;
            color: #4A4A68;
            text-shadow: none;
        }

        .card-back.card-korean {
            background: linear-gradient(135deg, #e6e6f7 0%, #b8e0f7 100%);
            font-family: 'Noto Sans KR', 'Nanum Gothic', sans-serif;
        }

        .card-back.card-vietnamese {
            background: linear-gradient(135deg, #e5f6fb 0%, #eaeafb 100%);
        }

        .matched {
            pointer-events: none;
            opacity: 0.85;
            filter: brightness(0.98) drop-shadow(0 0 6px rgba(200, 220, 255, 0.3));
        }

        .matched .card-back {
            background: linear-gradient(135deg, #eaeafb 0%, #e5f6fb 100%);
        }

        @media (max-width: 1024px) {
            .game-container {
                grid-template-columns: repeat(4, 1fr);
            }
        }

        @media (max-width: 768px) {
            .game-wrapper {
                flex-direction: column;
                align-items: center;
            }
            .game-container {
                grid-template-columns: repeat(3, 1fr);
            }
            .controls {
                margin-left: 0;
                margin-top: 20px;
                flex-direction: row;
            }
        }

        @media (max-width: 480px) {
            .game-container {
                grid-template-columns: repeat(2, 1fr);
            }
            .card {
                min-height: 80px;
            }
        }

        .controls {
            display: flex;
            flex-direction: column;
            margin-left: 20px;
            min-width: 200px;
        }

        .btn {
            padding: 12px 24px;
            font-size: 1.05em;
            border: none;
            border-radius: 24px;
            cursor: pointer;
            transition: all 0.25s ease;
            margin: 8px 0;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            font-family: 'Comfortaa', sans-serif;
            font-weight: 600;
            box-shadow: 0 2px 6px rgba(0,0,0,0.07);
            background: #fff;
            color: #4A4A68;
        }

        .btn-primary {
            background: linear-gradient(135deg, #b8e0f7 0%, #e6e6f7 100%);
            color: #4A4A68;
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, #e6e6f7 0%, #b8e0f7 100%);
            transform: translateY(-1px);
            box-shadow: 0 4px 10px rgba(123, 198, 224, 0.13);
        }

        .btn-secondary {
            background: linear-gradient(135deg, #eaeafb 0%, #e5f6fb 100%);
            color: #4A4A68;
        }

        .btn-secondary:hover {
            background: linear-gradient(135deg, #e5f6fb 0%, #eaeafb 100%);
            transform: translateY(-1px);
            box-shadow: 0 4px 10px rgba(200, 220, 255, 0.13);
        }

        @keyframes cardBounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-7px); }
        }

        .card.matched {
            animation: cardBounce 0.5s ease;
        }

        @keyframes float {
            0% { transform: translateY(0) rotate(0deg); opacity: 1; }
            100% { transform: translateY(-80px) rotate(20deg); opacity: 0; }
        }

        .heart {
            display: none !important;
        }

        .previousButton, .nextButton {
            color: var(--main-pink);
            background: #fff;
            font-size: 32px;
        }
        .previousButton:hover, .nextButton:hover {
            background: var(--main-pink);
            color: #fff;
        }

        /* VIP check modal */
        #vipCheckModal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .vip-modal-content {
            background: white;
            border-radius: 20px;
            padding: 30px;
            box-shadow: 0 16px 40px rgba(0,0,0,0.2);
            width: 90%;
            max-width: 450px;
            text-align: center;
            border: 1px solid #f0f0f0;
            animation: popupAppear 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        }

        .vip-modal-content h2 {
            color: #333;
            font-family: 'Comfortaa', sans-serif;
            font-size: 1.8em;
            margin-bottom: 15px;
            position: relative;
            display: inline-block;
        }

        .vip-modal-content h2::before {
            content: "⭐";
            color: gold;
            margin-right: 10px;
            display: inline-block;
            animation: shimmer 2s infinite;
        }

        .vip-modal-content p {
            color: #666;
            font-size: 1.1em;
            margin-bottom: 25px;
            line-height: 1.5;
        }

        .vip-modal-btn {
            padding: 12px 30px;
            border-radius: 30px;
            background: linear-gradient(to right, #FFD700, #FFC107);
            color: #333;
            font-size: 1.1em;
            border: none;
            cursor: pointer;
            transition: all 0.3s;
            font-family: 'Comfortaa', sans-serif;
            font-weight: 700;
            box-shadow: 0 4px 12px rgba(255, 193, 7, 0.3);
            position: relative;
            overflow: hidden;
        }

        .vip-modal-btn:hover {
            background: linear-gradient(to right, #FFC107, #FFD700);
            transform: translateY(-3px);
            box-shadow: 0 6px 18px rgba(255, 193, 7, 0.4);
        }

        .vip-modal-btn::after {
            content: "";
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: linear-gradient(
                transparent,
                rgba(255, 255, 255, 0.1),
                transparent
            );
            transform: rotate(30deg);
            animation: shineEffect 3s infinite;
        }

        @keyframes shimmer {
            0% { opacity: 0.8; text-shadow: 0 0 5px rgba(255, 215, 0, 0.3); }
            50% { opacity: 1; text-shadow: 0 0 15px rgba(255, 215, 0, 0.7), 0 0 30px rgba(255, 215, 0, 0.4); }
            100% { opacity: 0.8; text-shadow: 0 0 5px rgba(255, 215, 0, 0.3); }
        }

        @keyframes shineEffect {
            0% { transform: translateX(-300%) rotate(30deg); }
            100% { transform: translateX(300%) rotate(30deg); }
        }

        @keyframes popupAppear {
            0% {
                opacity: 0;
                transform: scale(0.5);
            }
            100% {
                opacity: 1;
                transform: scale(1);
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<c:if test="${isUserVip != true}">
    <div id="vipCheckModal">
        <div class="vip-modal-content">
            <h2>Tính năng chỉ dành cho thành viên VIP</h2>
            <p>Trò chơi trí nhớ là tính năng đặc biệt chỉ dành cho thành viên VIP. Vui lòng nâng cấp tài khoản của bạn để truy cập tính năng này và nhiều ưu đãi đặc biệt khác.</p>
            <button class="vip-modal-btn" onclick="redirectToVipPackages()">Đăng ký VIP ngay</button>
        </div>
    </div>
</c:if>

<div class="game-wrapper">
    <div class="container">
        <div class="game-header">
            <h1> Trò chơi trí nhớ - ${topic} </h1>
            <p>Ghép cặp từ tiếng Hàn và nghĩa tiếng Việt tương ứng!</p>
        </div>

        <div class="game-info">
            <div class="score">Cặp đã ghép: <span id="matchCount">0</span>/10</div>
            <div class="moves">Lượt chơi: <span id="moveCount">0</span></div>
        </div>

        <div class="game-container">
            <c:forEach items="${words}" var="word" varStatus="status">
                <div class="card" data-word="${word}" data-pair-id="${pairIds[status.index]}">
                    <div class="card-inner">
                        <div class="card-front">❓</div>
                        <div class="card-back card-korean">${word}</div>
                    </div>
                </div>
            </c:forEach>
            <c:forEach items="${meanings}" var="meaning" varStatus="status">
                <div class="card" data-meaning="${meaning}" data-pair-id="${shuffledPairIds[status.index]}">
                    <div class="card-inner">
                        <div class="card-front">❓</div>
                        <div class="card-back card-vietnamese">${meaning}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="controls">
        <button class="btn btn-secondary" onclick="location.href='quizlet'">🏠 Về chủ đề</button>
        <button class="btn btn-primary" onclick="resetGame()">🔄 Chơi lại</button>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script>
    let flippedCards = [];
    let matchedPairs = 0;
    let moveCount = 0;
    let canFlip = true;

    // Redirect to VIP packages page
    function redirectToVipPackages() {
        window.location.href = "bundles";
    }

    // Disable game if user is not VIP
    let isVip = false;
    <c:if test="${isUserVip == true}">
        isVip = true;
    </c:if>
    
    if (!isVip) {
        document.querySelectorAll('.card').forEach(card => {
            card.style.pointerEvents = 'none';
        });
    } else {
        document.querySelectorAll('.card').forEach(card => {
            card.addEventListener('click', () => {
                if (!canFlip || card.classList.contains('flipped') || card.classList.contains('matched')) {
                    return;
                }

                flipCard(card);
            });
        });
    }

    function flipCard(card) {
        card.classList.add('flipped');
        flippedCards.push(card);

        if (flippedCards.length === 2) {
            canFlip = false;
            moveCount++;
            document.getElementById('moveCount').textContent = moveCount;

            const [card1, card2] = flippedCards;
            const isMatch = checkMatch(card1, card2);

            if (isMatch) {
                handleMatch(card1, card2);
            } else {
                setTimeout(() => {
                    card1.classList.remove('flipped');
                    card2.classList.remove('flipped');
                    flippedCards = [];
                    canFlip = true;
                }, 1000);
            }
        }
    }

    function checkMatch(card1, card2) {
        const hasWord1 = card1.dataset.word !== undefined;
        const hasMeaning1 = card1.dataset.meaning !== undefined;
        const hasWord2 = card2.dataset.word !== undefined;
        const hasMeaning2 = card2.dataset.meaning !== undefined;

        if ((hasWord1 && hasMeaning2) || (hasMeaning1 && hasWord2)) {
            return card1.dataset.pairId === card2.dataset.pairId;
        }
        return false;
    }

    function handleMatch(card1, card2) {
        card1.classList.add('matched');
        card2.classList.add('matched');
        flippedCards = [];
        canFlip = true;
        matchedPairs++;
        document.getElementById('matchCount').textContent = matchedPairs;

        if (matchedPairs === 10) {
            setTimeout(() => {
                showVictoryPopup();
            }, 800);
        }
    }

    function showVictoryPopup() {
        const victoryPopup = document.createElement('div');
        victoryPopup.id = 'victory-popup';
        victoryPopup.innerHTML = `
            <div style="
                background: linear-gradient(135deg, var(--pink-medium) 0%, var(--blue-medium) 100%);
                border-radius: 24px;
                padding: 30px;
                box-shadow: 0 16px 40px rgba(0,0,0,0.2);
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                z-index: 9999;
                text-align: center;
                max-width: 90vw;
                width: 450px;
                animation: popupAppear 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
                color: white;
                border: 3px solid white;
            ">
                <h2 style="
                    font-family: 'Comfortaa', sans-serif;
                    font-size: 2em;
                    margin-bottom: 15px;
                    text-shadow: 0 2px 4px rgba(0,0,0,0.2);
                ">🎉 Chúc mừng! 🎉</h2>
                <p style="
                    font-size: 1.3em;
                    margin-bottom: 25px;
                ">Bạn đã hoàn thành với ${moveCount} lượt chơi!</p>
                <div style="display: flex; justify-content: center; gap: 15px; flex-wrap: wrap;">
                    <button onclick="resetGame()" style="
                        padding: 12px 30px;
                        border-radius: 30px;
                        background: white;
                        color: var(--pink-dark);
                        font-size: 1.1em;
                        border: none;
                        cursor: pointer;
                        transition: all 0.3s;
                        font-family: 'Comfortaa', sans-serif;
                        font-weight: 700;
                        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                    ">🔄 Chơi lại</button>
                    <button onclick="location.href='quizlet'" style="
                        padding: 12px 30px;
                        border-radius: 30px;
                        background: white;
                        color: var(--blue-dark);
                        font-size: 1.1em;
                        border: none;
                        cursor: pointer;
                        transition: all 0.3s;
                        font-family: 'Comfortaa', sans-serif;
                        font-weight: 700;
                        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                    ">🏠 Về chủ đề</button>
                </div>
            </div>
        `;
        document.body.appendChild(victoryPopup);

        // Add confetti effect
        createConfetti();

        // Add animation for popup
        const style = document.createElement('style');
        style.textContent = `
            @keyframes popupAppear {
                0% {
                    opacity: 0;
                    transform: translate(-50%, -50%) scale(0.5);
                }
                100% {
                    opacity: 1;
                    transform: translate(-50%, -50%) scale(1);
                }
            }
        `;
        document.head.appendChild(style);
    }

    function createConfetti() {
        const colors = ['#FFB8E0', '#B4EBE6', '#D4C4FB', '#FFF6B8', '#FF8CC6', '#7BD5D6'];

        for (let i = 0; i < 100; i++) {
            const confetti = document.createElement('div');
            confetti.style.position = 'fixed';
            confetti.style.width = '10px';
            confetti.style.height = '10px';
            confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
            confetti.style.borderRadius = '50%';
            confetti.style.left = `${Math.random() * 100}vw`;
            confetti.style.top = '-10px';
            confetti.style.zIndex = '9998';
            confetti.style.opacity = '0.8';
            confetti.style.transform = `rotate(${Math.random() * 360}deg)`;

            const animationDuration = `${2 + Math.random() * 3}s`;
            const animationDelay = `${Math.random() * 0.5}s`;

            confetti.style.animation = `confettiFall ${animationDuration} ${animationDelay} linear forwards`;

            document.body.appendChild(confetti);

            setTimeout(() => {
                confetti.remove();
            }, 5000);
        }

        const confettiStyle = document.createElement('style');
        confettiStyle.textContent = `
            @keyframes confettiFall {
                0% {
                    transform: translateY(0) rotate(0deg);
                    opacity: 0.8;
                }
                100% {
                    transform: translateY(100vh) rotate(360deg);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(confettiStyle);
    }

    function resetGame() {
        const popup = document.getElementById('victory-popup');
        if (popup) {
            popup.remove();
        }

        // Flip all cards first
        const allCards = document.querySelectorAll('.card');
        allCards.forEach(card => {
            card.classList.add('flipped');
        });

        // Then reset after delay
        setTimeout(() => {
            location.reload();
        }, 1500);
    }

    // Initial reveal of all cards
    function revealAllCardsThenHide() {
        const allCards = document.querySelectorAll('.card');
        allCards.forEach(card => card.classList.add('flipped'));
        setTimeout(() => {
            allCards.forEach(card => card.classList.remove('flipped'));
        }, 3000);
    }

    window.addEventListener('DOMContentLoaded', function() {
        // First visit popup
        if (!sessionStorage.getItem('firstVisitMemory') && isVip) {
            const popup = document.createElement('div');
            popup.id = 'guide-popup';
            popup.innerHTML = `<div style="
                background: linear-gradient(135deg, var(--pink-light) 0%, var(--blue-light) 100%);
                border-radius: 20px;
                padding: 30px;
                box-shadow: 0 16px 40px rgba(0,0,0,0.2);
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                z-index: 9999;
                text-align: center;
                max-width: 90vw;
                width: 400px;
                animation: popupAppear 0.5s ease-out;
                color: white;
                border: 3px solid white;
            ">
                <h2 style="font-family: 'Comfortaa', sans-serif; font-size: 1.8em; margin-bottom: 15px;">✨ Cách chơi ✨</h2>
                <p style="font-size: 1.1em; margin-bottom: 25px; line-height: 1.5;">
                    Lật 2 thẻ để tìm cặp từ Hàn-Việt tương ứng!<br>
                    Hãy ghi nhớ vị trí các thẻ để ghép đúng nhanh nhất nhé!
                </p>
                <button id='startGameBtn' style="
                    padding: 12px 30px;
                    border-radius: 30px;
                    background: white;
                    color: var(--pink-dark);
                    font-size: 1.1em;
                    border: none;
                    cursor: pointer;
                    transition: all 0.3s;
                    font-family: 'Comfortaa', sans-serif;
                    font-weight: 700;
                    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                ">Bắt đầu chơi</button>
            </div>`;
            document.body.appendChild(popup);
            sessionStorage.setItem('firstVisitMemory', 'true');

            document.getElementById('startGameBtn').onclick = function() {
                document.getElementById('guide-popup').remove();
                revealAllCardsThenHide();
            };
        }
    });
</script>
</body>
</html>