<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Quizlet Flashcards</title>
<%--    <link rel="stylesheet" href="./asset/css/selectTopic.css">--%>
    <link rel="icon" href="asset/png/icon/logo.jpg">
    <style>
        @font-face {
            font-family: 'Poppins';
            src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('truetype');
        }
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            transition: all 0.3s ease;
        }
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #f9e5ee 0%, #e5f1ff 100%);
            min-height: 100vh;
            background-position: left center;
            color: #555;
        }
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 20px;
        }
        .containerSmall {
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 8px 20px rgba(179, 186, 231, 0.2);
            margin-bottom: 30px;
        }
        .debug {
            color: #aaa;
            font-size: 0.8rem;
            margin: 5px 0;
        }
        h1 {
            text-align: center;
            color: #6a7cd0;
            margin-bottom: 20px;
            font-size: 2.2rem;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
        }
        .no-data {
            text-align: center;
            color: #888;
            font-size: 1.2rem;
            padding: 20px;
            background-color: rgba(249, 229, 238, 0.3);
            border-radius: 10px;
        }
        .play-game-btn {
            background: linear-gradient(135deg, #f9a8d4 0%, #95b5ee 100%);
            color: white;
            padding: 14px 28px;
            border: none;
            border-radius: 30px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin: 20px auto;
            display: block;
            transition: all 0.3s;
            box-shadow: 0 4px 15px rgba(249, 168, 212, 0.3);
        }
        .play-game-btn:hover {
            background: linear-gradient(135deg, #f9a8d4 20%, #95b5ee 80%);
            transform: translateY(-3px);
            box-shadow: 0 7px 20px rgba(249, 168, 212, 0.4);
        }
        .play-game-btn:disabled {
            background: linear-gradient(135deg, #e5e5e5 0%, #cccccc 100%);
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
            opacity: 0.7;
        }
        /* Flashcard Styling */
        .flashcard-container {
            width: 100%;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 30px 0;
            min-height: 280px;
        }
        .flashcard {
            width: 70%;
            height: 260px;
            perspective: 1000px;
        }
        .flashcard-inner {
            position: relative;
            width: 100%;
            height: 100%;
            transition: transform 0.8s;
            transform-style: preserve-3d;
            cursor: pointer;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(179, 186, 231, 0.3);
        }
        .flashcard-inner.flipped {
            transform: rotateY(180deg);
        }
        .flashcard-front, .flashcard-back {
            position: absolute;
            width: 100%;
            height: 100%;
            -webkit-backface-visibility: hidden;
            backface-visibility: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
            text-align: center;
            border-radius: 15px;
            font-size: 24px;
            font-weight: 500;
            color: #555;
        }
        .flashcard-front {
            font-size: 40px;
            background: linear-gradient(135deg, #f9e5ee 0%, #e5f1ff 100%);
            border: 2px solid #f9a8d4;
        }
        .flashcard-back {
            background: linear-gradient(135deg, #e5f1ff 0%, #f9e5ee 100%);
            transform: rotateY(180deg);
            border: 2px solid #95b5ee;
        }
        .previousButton, .nextButton {
            background-color: white;
            border: none;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0 4px 10px rgba(179, 186, 231, 0.3);
            color: #6a7cd0;
        }
        .previousButton:hover, .nextButton:hover {
            background-color: #f9a8d4;
            color: white;
            box-shadow: 0 6px 15px rgba(249, 168, 212, 0.4);
        }
        .flashcard-counter {
            text-align: center;
            margin-top: 10px;
            font-size: 16px;
            color: #777;
        }
        /* Word Table Styling */
        .wordContainer {
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 8px 20px rgba(179, 186, 231, 0.2);
        }
        .wordTable {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(179, 186, 231, 0.2);
        }
        .wordTable thead {
            background: linear-gradient(135deg, #f9a8d4 0%, #95b5ee 100%);
            color: white;
        }
        .wordTable th {
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
        }
        .wordTable tbody tr {
            border-bottom: 1px solid #eee;
            transition: all 0.3s;
        }
        .wordTable tbody tr:nth-child(even) {
            background-color: rgba(249, 229, 238, 0.3);
        }
        .wordTable tbody tr:hover {
            background-color: rgba(149, 181, 238, 0.2);
        }
        .wordTable td {
            padding: 12px 15px;
        }
        /* Responsive */
        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }
            .containerSmall, .wordContainer {
                padding: 15px;
            }
            .flashcard {
                width: 85%;
                height: 200px;
            }
            .flashcard-front, .flashcard-back {
                font-size: 20px;
            }
            .wordTable th, .wordTable td {
                padding: 8px 10px;
                font-size: 14px;
            }
            .play-game-btn {
                padding: 12px 20px;
                font-size: 14px;
            }
        }
    </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
    <div class="containerSmall">
        <h1>Flashcards - ${topic}</h1>

        <!-- Add Play Game button -->
        <c:if test="${not empty flashCards && flashCards.size() >= 10}">
            <form action="memory-game" method="GET">
                <input type="hidden" name="topic" value="${topic}">
                <input type="hidden" name="type" value="${type}">
                <button type="submit" class="play-game-btn">Play Memory Game</button>
            </form>
        </c:if>
        <c:if test="${empty flashCards || flashCards.size() < 10}">
            <button class="play-game-btn" disabled>Need at least 10 cards to play</button>
        </c:if>

        <div class="flashcard-container">
            <c:choose>
                <c:when test="${empty flashCardsJson}">
                    <p class="no-data">No flashcards available for this topic.</p>
                </c:when>
                <c:otherwise>
                    <div class="previousButton">←</div>
                    <div class="flashcard">
                        <div class="flashcard-inner">
                            <div class="flashcard-front"></div>
                            <div class="flashcard-back"></div>
                        </div>
                    </div>
                    <div class="nextButton">→</div>
                    <div class="flashcard-counter"></div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="wordContainer">
        <table class="wordTable">
            <thead>
            <tr>
                <th>Từ vựng</th>
                <th>Nghĩa</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${flashCards}" var="flashcard">
                <tr>
                    <td><c:out value="${flashcard.dictionary.word}" /></td>
                    <td><c:out value="${flashcard.dictionary.mean}" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    const flashCards = ${flashCardsJson != null ? flashCardsJson : '[]'};
    const flashCardType = "${type}";
    let currentIndex = 0;

    console.log("FlashCards:", flashCards);
    console.log("FlashCardType:", flashCardType);

    const flashcardInner = document.querySelector('.flashcard-inner');
    const frontElement = document.querySelector('.flashcard-front');
    const backElement = document.querySelector('.flashcard-back');
    const nextButton = document.querySelector('.nextButton');
    const previousButton = document.querySelector('.previousButton');

    function showFlashcard(index) {
        if (flashCards && flashCards[index]) {
            console.log("Showing flashcard at index:", index, flashCards[index]);
            if (flashCardType === 'system' || flashCardType === 'favorite') {
                frontElement.textContent = flashCards[index].dictionary && flashCards[index].dictionary.word ? flashCards[index].dictionary.word : 'No word';
                backElement.textContent = flashCards[index].dictionary && flashCards[index].dictionary.mean ? flashCards[index].dictionary.mean : 'No mean';
            } else if (flashCardType === 'custom') {
                frontElement.textContent = flashCards[index].word ? flashCards[index].word : 'No word';
                backElement.textContent = flashCards[index].mean ? flashCards[index].mean : 'No mean';
            } else {
                frontElement.textContent = 'Error: Unknown type';
                backElement.textContent = 'Error: Unknown type';
            }
            flashcardInner.classList.remove('flipped');
        } else {
            console.error("No flashcard data at index:", index);
            frontElement.textContent = 'No data';
            backElement.textContent = 'No data';
        }
    }

    if (flashCards && flashCards.length > 0) {
        showFlashcard(currentIndex);
    } else {
        console.error("FlashCards is empty or null");
        frontElement.textContent = 'No flashcards';
        backElement.textContent = 'No flashcards';
    }

    nextButton.addEventListener('click', function() {
        currentIndex++;
        if (currentIndex >= flashCards.length) {
            currentIndex = 0;
        }
        showFlashcard(currentIndex);
    });

    previousButton.addEventListener('click', function() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = flashCards.length - 1;
        }
        showFlashcard(currentIndex);
    });

    flashcardInner.addEventListener('click', function() {
        this.classList.toggle('flipped');
    });
</script>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
