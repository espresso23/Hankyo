<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Custom Flashcards</title>
    <link rel="stylesheet" href="/asset/css/quizlet.css">
</head>
<body>
    <div class="container">
        <h1>Custom Flashcards</h1>
        
        <div class="flashcards-container">
            <c:forEach items="${customFlashcards}" var="flashcard">
                <div class="flashcard">
                    <div class="flashcard-header">
                        <h3>${flashcard.title}</h3>
                        <c:if test="${flashcard.learnerID == sessionScope.learnerID}">
                            <button class="toggle-public-btn" 
                                    data-flashcard-id="${flashcard.flashcardID}"
                                    data-public="${flashcard.isPublic}">
                                <span class="toggle-slider"></span>
                                <span class="toggle-label">${flashcard.isPublic ? 'Public' : 'Private'}</span>
                            </button>
                        </c:if>
                    </div>
                    <div class="flashcard-content">
                        <p>${flashcard.content}</p>
                    </div>
                    <div class="flashcard-footer">
                        <span class="learner-name">Created by: ${flashcard.learnerName}</span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script src="/asset/js/quizlet.js"></script>
</body>
</html> 