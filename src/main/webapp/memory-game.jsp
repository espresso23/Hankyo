<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Memory Game - ${topic}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/memory-game.css">
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&family=Nanum+Gothic:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Noto Sans KR', 'Nanum Gothic', Arial, sans-serif;
            background-image: url("${pageContext.request.contextPath}/asset/png/background/background.png");
            background-size: auto;
            margin: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            overflow: hidden;
        }

        .game-wrapper {
            margin-top: 80px;
            padding: 20px;
            background: rgba(255, 255, 255, 0.9);
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
        }

        .container {
            /* min-width: 1200px; */
            max-width: 100vw;
            width: 100%;
            margin: 0;
            padding: 15px;
            background: rgba(255, 255, 255, 0.9);
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .game-header {
            text-align: center;
            margin-bottom: 15px;
        }

        .game-header h1 {
            color: #2c3e50;
            margin-bottom: 5px;
            font-size: 1.8em;
        }

        .game-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding: 8px;
            background: #f8f9fa;
            border-radius: 8px;
        }

        .score {
            font-size: 1.3em;
            font-weight: 700;
            color: #2c3e50;
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
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(44,62,80,0.1), 0 1px 2px rgba(44,62,80,0.05);
            transition: box-shadow 0.3s, transform 0.2s;
        }
        .card:hover {
            box-shadow: 0 6px 12px rgba(44,62,80,0.12), 0 2px 4px rgba(44,62,80,0.08);
            transform: scale(1.02);
        }
        .card-inner {
            position: relative;
            width: 100%;
            height: 100%;
            text-align: center;
            transition: transform 0.5s cubic-bezier(.4,2,.6,1);
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
            border-radius: 8px;
            box-shadow: none;
            padding: 8px;
            font-size: 1.1em;
            word-break: break-word;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        .card-front {
            background: linear-gradient(135deg, #f0f0f0 0%, #fff 100%);
            color: #2c3e50;
            font-size: 1.3em;
            font-weight: 700;
        }
        .card-back.card-korean, .card-back.card-vietnamese {
            background: linear-gradient(135deg, #FFB8E0 0%, #B4EBE6 100%);
            color: #fff;
            font-family: 'Noto Sans KR', 'Nanum Gothic', Arial, sans-serif;
            font-size: 1.2em;
            letter-spacing: 1px;
            transform: rotateY(180deg);
            border: none;
            box-shadow: none;
        }
        .card.flipped .card-back {
            background: linear-gradient(135deg, #FFD6EC 0%, #B4EBE6 100%);
            border: none;
            box-shadow: none;
        }
        .matched {
            pointer-events: none;
            opacity: 0.5;
            filter: grayscale(0.3) brightness(0.9);
        }
        .matched .card-inner {
            transform: rotateY(180deg);
        }
        @media (max-width: 1024px) {
            .game-container {
                grid-template-columns: repeat(3, 1fr);
            }
            .card {
                min-width: 60px;
                min-height: 90px;
            }
        }
        @media (max-width: 600px) {
            .game-container {
                grid-template-columns: repeat(2, 1fr);
            }
            .card {
                min-width: 50px;
                min-height: 70px;
            }
        }

        .controls {
            display: flex;
            flex-direction: column;
            margin-left: 20px;
        }

        .btn {
            padding: 10px 22px;
            font-size: 1.1em;
            border: none;
            border-radius: 24px;
            cursor: pointer;
            transition: background 0.2s, color 0.2s, box-shadow 0.2s;
            margin: 8px 0;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .btn-primary {
            background-color: #4CAF50;
            color: white;
            box-shadow: 0 2px 8px rgba(76,175,80,0.08);
        }

        .btn-primary:hover {
            background-color: #388e3c;
            color: #fff;
            box-shadow: 0 4px 16px rgba(76,175,80,0.18);
        }

        .btn-secondary {
            background-color: #f8f9fa;
            color: #2c3e50;
            border: 1.5px solid #FFD700;
            box-shadow: 0 2px 8px rgba(255,215,0,0.08);
        }

        .btn-secondary:hover {
            background-color: #ffe066;
            color: #2c3e50;
            box-shadow: 0 4px 16px rgba(255,215,0,0.18);
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div class="game-wrapper">
    <div class="container">
        <div class="game-header">
            <h1>Memory Game - ${topic}</h1>
            <p>Match the Korean words with their Vietnamese meanings</p>
        </div>

        <div class="game-info">
            <div class="score">Matches: <span id="matchCount">0</span>/10</div>
            <div class="moves">Moves: <span id="moveCount">0</span></div>
        </div>

        <div class="game-container">
            <c:forEach items="${words}" var="word" varStatus="status">
                <div class="card" data-word="${word}" data-pair-id="${pairIds[status.index]}">
                    <div class="card-inner">
                        <div class="card-front">?</div>
                        <div class="card-back card-korean">${word}</div>
                    </div>
                </div>
            </c:forEach>
            <c:forEach items="${meanings}" var="meaning" varStatus="status">
                <div class="card" data-meaning="${meaning}" data-pair-id="${shuffledPairIds[status.index]}">
                    <div class="card-inner">
                        <div class="card-front">?</div>
                        <div class="card-back card-vietnamese">${meaning}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="controls">
        <button class="btn btn-secondary" onclick="location.href='quizlet'"><span style="font-size:1.2em;">&#8592;</span> Back to Topics</button>
        <button class="btn btn-primary" onclick="resetGame()"><span style="font-size:1.2em;">&#8635;</span> Play Again</button>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script>
    let flippedCards = [];
    let matchedPairs = 0;
    let moveCount = 0;
    let canFlip = true;

    document.querySelectorAll('.card').forEach(card => {
        card.addEventListener('click', () => {
            if (!canFlip || card.classList.contains('flipped') || card.classList.contains('matched')) {
                return;
            }

            flipCard(card);
        });
    });

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
        // Ensure one card has data-word and the other has data-meaning
        const hasWord1 = card1.dataset.word !== undefined;
        const hasMeaning1 = card1.dataset.meaning !== undefined;
        const hasWord2 = card2.dataset.word !== undefined;
        const hasMeaning2 = card2.dataset.meaning !== undefined;

        if ((hasWord1 && hasMeaning2) || (hasMeaning1 && hasWord2)) {
            // Valid pair: one word and one meaning
            return card1.dataset.pairId === card2.dataset.pairId;
        }
        return false; // Invalid pair (e.g., two words or two meanings)
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
                alert('Congratulations! You completed the game in ' + moveCount + ' moves!');
            }, 500);
        }
    }

    function resetGame() {
        location.reload();
    }

    // Lật tất cả card khi bấm 'Bắt đầu chơi', sau 3s lật lại
    function revealAllCardsThenHide() {
        const allCards = document.querySelectorAll('.card');
        allCards.forEach(card => card.classList.add('flipped'));
        setTimeout(() => {
            allCards.forEach(card => card.classList.remove('flipped'));
        }, 3000);
    }

    window.addEventListener('DOMContentLoaded', function() {
        // Popup hướng dẫn lần đầu
        if (!sessionStorage.getItem('firstVisit')) {
            const popup = document.createElement('div');
            popup.id = 'guide-popup';
            popup.innerHTML = `<div style="background:rgba(255,255,255,0.98);border-radius:18px;padding:32px 28px;box-shadow:0 8px 32px rgba(0,0,0,0.18);position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);z-index:9999;text-align:center;max-width:90vw;">
                <h2 style='font-family:"Noto Sans KR","Nanum Gothic",Arial,sans-serif;font-size:1.5em;margin-bottom:12px;'>Hướng dẫn chơi</h2>
                <p style='font-size:1.1em;margin-bottom:18px;'>Lật hai thẻ để tìm cặp từ Hàn-Việt tương ứng! Hãy ghi nhớ vị trí các thẻ để chiến thắng nhanh nhất nhé.</p>
                <button id='startGameBtn' style='padding:8px 24px;border-radius:18px;background:#4CAF50;color:#fff;font-size:1.1em;border:none;cursor:pointer;'>Bắt đầu chơi</button>
            </div>`;
            document.body.appendChild(popup);
            sessionStorage.setItem('firstVisit', 'true');
            document.getElementById('startGameBtn').onclick = function() {
                document.getElementById('guide-popup').remove();
                revealAllCardsThenHide();
            };
        }
    });
</script>
</body>
</html>