
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Quizlet Flashcards</title>
    <link rel="stylesheet" href="./asset/css/selectTopic.css">
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
        }
        body {
            font-family: 'Poppins', sans-serif;
            background-image: url('asset/png/background/background-2.png');
            min-height: 100vh;
            background-position: left center;
        }
        .debug {
            color: #888;
            font-size: 0.9rem;
            margin: 5px 0;
        }
        .no-data {
            text-align: center;
            color: #888;
            font-size: 1.2rem;
        }
    </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
    <div class="containerSmall">
        <h1>Flashcards - ${topic}</h1>
        <p class="debug">Type: ${type}</p>
        <p class="debug">FlashCards size: <c:out value="${flashCards != null ? flashCards.size() : 'null'}" /></p>
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
