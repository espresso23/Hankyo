<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Quizlet Flashcards</title>
    <link rel="stylesheet" href="./asset/css/selectTopic.css">
    <style>
        @font-face {
            font-family: 'Poppins';
            src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
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

    </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
    <div class="containerSmall">
        <h1>Flashcards - ${topic}</h1>
        <div class="flashcard-container">
            <c:choose>
                <c:when test="${empty flashCardsJson}">
                    <p class="no-data">No flashcards available for this topic.</p>
                </c:when>
                <c:otherwise>
                    <div class="previousButton"><---</div>
                    <div class="flashcard">
                        <div class="flashcard-inner">
                            <div class="flashcard-front"></div>
                            <div class="flashcard-back"></div>
                        </div>
                    </div>
                    <div class="nextButton">---></div>
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
                <th>Chú thích</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${flashCards}" var="flashcard">
                <tr>
                    <td>${flashcard.dictionary.word}</td>
                    <td>${flashcard.dictionary.mean}</td>
                    <td>${flashcard.dictionary.definition}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    const flashCards = ${flashCardsJson};
    let currentIndex = 0;

    const flashcardInner = document.querySelector('.flashcard-inner');
    const frontElement = document.querySelector('.flashcard-front');
    const backElement = document.querySelector('.flashcard-back');
    const nextButton = document.querySelector('.nextButton');
    const previousButton = document.querySelector('.previousButton');

    function showFlashcard(index) {
        if (flashCards && flashCards[index]) {
            frontElement.textContent = flashCards[index].dictionary.word;
            backElement.textContent = flashCards[index].dictionary.mean;
            flashcardInner.classList.remove('flipped'); // Reset về mặt trước
        }
    }

    if (flashCards && flashCards.length > 0) {
        showFlashcard(currentIndex);
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