<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết bài làm - Hankyo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .btn-outline-primary{
            color: lightpink !important;
            border-color: #1c6ca1 !important;
        }
        .back-button .btn {
        background: lightpink !important;
        color: #1c6ca1 !important;
        border-color: #1c6ca1 !important;
        font-weight: 500;
        transition: all 0.2s;
    }
    .back-button .btn:hover {
        background: #ffd6e7 !important;
        color: #1c6ca1 !important;
        border-color: #1c6ca1 !important;
        box-shadow: 0 2px 8px rgba(255,143,163,0.12);
    }
        .result-container {
            max-width: 100%;
            margin: 40px auto;
            padding: 80px;
        }

        .bg-success {
            background-color: #58d969 !important;
        }

        .result-header {
            margin-bottom: 30px;
            text-align: center;
        }

        .stats-card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            transition: transform 0.2s;
        }

        .stats-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .question-card {
            border: none;
            box-shadow: 0 2px 12px rgba(255, 143, 163, 0.08), 0 1.5px 8px rgba(108, 180, 255, 0.08);
            border-radius: 16px;
            margin-bottom: 32px;
            background: #fff;
        }

        .question-card.correct {
            border-color: #28a745;
        }

        .question-card.incorrect {
            border-color: #dc3545;
        }

        .question-header {
            background: #ffe0e0;
            color: #fff;
            border-radius: 16px 16px 0 0;
            padding: 18px 24px;
            font-weight: 600;
            font-size: 1.1rem;
            border-bottom: none;
        }

        .question-content {
            padding: 24px 28px 20px 28px;
        }

        .question-image {
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(90deg, #ff8fa3, #6cb4ff);
            border-radius: 12px;
            padding: 16px;
            margin-bottom: 18px;
            box-shadow: 0 2px 8px rgba(108, 180, 255, 0.08);
            min-height: 180px;
            max-width: 100%;
        }

        .question-image img {
            max-width: 320px;
            max-height: 180px;
            width: auto;
            height: auto;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(255, 143, 163, 0.12);
            background: #fff;
            padding: 4px;
            display: block;
            margin: 0 auto;
        }

        .quest-text {
            color: #343a40;
        }

        .audio-player {
            width: 100%;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 4px;
        }

        .audio-player audio {
            width: 100%;
        }

        .answer-options .list-group-item {
            border-radius: 8px !important;
            margin-bottom: 10px;
            border: 1.5px solid #e9ecef;
            background: #f7f9fa;
            transition: all 0.2s;
        }

        .answer-options .list-group-item:hover {
            background-color: #f8f9fa;
        }

        .answer-options .list-group-item.correct-answer {
            background: #e6f7f7;
            border-color: #28a745;
        }

        .answer-options .list-group-item.user-answer {
            border: 2px solid #ff8fa3;
            background: #fff0f6;
        }

        .answer-options .list-group-item.list-group-item-success {
            background: #e6f7f7;
            border-color: #28a745;
        }

        .answer-options .list-group-item.list-group-item-danger {
            background: #fff0f0;
            border-color: #dc3545;
        }

        .answer-label {
            color: #ff8fa3;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .answer-text {
            flex: 1;
        }

        .answer-indicators {
            display: flex;
            gap: 8px;
        }

        .answer-indicators .badge {
            background: linear-gradient(90deg, #07a44a, #14a141);
            color: #fff;
            font-weight: 600;
            border-radius: 6px;
            font-size: 0.95rem;
            box-shadow: 0 1px 4px rgba(255, 143, 163, 0.08);
        }

        .bg-primary {
            background: #4779bc !important;
        }

        .badge {
            font-weight: 500;
        }

        .badge i {
            font-size: 0.875rem;
        }

        .back-button {
            margin-bottom: 20px;
        }

        .back-button .btn {
            padding: 8px 16px;
            font-size: 0.95rem;
        }

        .progress {
            height: 10px;
            border-radius: 5px;
            margin: 20px 0;
        }

        .answer-explanation {
            background: #f7f9fa;
            border-radius: 8px;
            margin-top: 18px;
            padding: 12px 18px 8px 18px;
        }

        .questions-list {
            display: flex;
            flex-wrap: wrap;
            gap: 24px 18px;
            justify-content: flex-start;
        }

        .question-card {
            flex: 0 0 calc(33.333% - 18px);
            max-width: calc(33.333% - 18px);
            min-width: 200px;
            min-height: 340px;
            display: flex;
            flex-direction: column;
            box-sizing: border-box;
            margin-bottom: 0;
        }

        .question-header {
            min-height: 48px;
            padding: 12px 16px;
            font-size: 1rem;
        }

        .question-content {
            flex: 1;
            padding: 12px 14px 8px 14px;
        }

        .question-image {
            min-height: 80px;
            max-height: 120px;
            margin-bottom: 8px;
            padding: 4px;
        }

        .question-image img {
            max-width: 100%;
            max-height: 100px;
            object-fit: contain;
        }

        .answer-options {
            margin-bottom: 10px;
        }

        .answer-explanation {
            margin-top: auto;
        }

        @media (max-width: 1200px) {
            .question-card {
                flex: 0 0 calc(50% - 12px);
                max-width: calc(50% - 12px);
            }
        }

        @media (max-width: 900px) {
            .questions-list {
                gap: 16px 8px;
            }

            .question-card {
                flex: 0 0 100%;
                max-width: 100%;
                min-width: 160px;
            }
        }

        @media (max-width: 600px) {
            .questions-list {
                flex-direction: column;
                gap: 12px;
            }

            .question-card {
                max-width: 100%;
                min-width: 0;
            }
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="result-container">
    <div class="back-button">
        <a href="learn-course?courseID=${courseID}&courseContentID=${courseContentID}" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left me-2"></i>Quay lại bài học
        </a>
    </div>

    <div class="result-header">
        <h2>${assignment.assignmentTitle}</h2>
        <p class="text-muted">Hoàn thành lúc: <fmt:formatDate value="${assignmentTaken.dateCreated}"
                                                              pattern="HH:mm dd/MM/yyyy"/></p>
    </div>

    <div class="row mb-4">
        <div class="col-md-4">
            <div class="stats-card text-center">
                <h3 style="
    color: #198754 !important;" class="text-primary mb-2">${assignmentResult.score}/10</h3>
                <p class="text-muted mb-0">Điểm số</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card text-center">
                <h3 class="text-success mb-2">${assignmentResult.correctCount}/${assignmentResult.totalQuestions}</h3>
                <p class="text-muted mb-0">Câu trả lời đúng</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card text-center">
                <h3 class="mb-2 
                    ${((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) >= 80 ? 'text-success' : 'text-warning'}">
                    <fmt:formatNumber value="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}" maxFractionDigits="1"/>%
                </h3>
                <p class="text-muted mb-0">Tỷ lệ đúng</p>
            </div>
        </div>
    </div>

    <div class="progress">
        <div class="progress-bar bg-success"
             role="progressbar"
             style="width: ${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}%"
             aria-valuenow="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}"
             aria-valuemin="0"
             aria-valuemax="100">
        </div>
    </div>

    <h3 class="mb-4">Chi tiết từng câu hỏi</h3>

    <div class="questions-list">
        <c:forEach items="${questions}" var="question" varStatus="status">
            <div class="question-card ${resultMap[question.assignQuesID].answerIsCorrect ? 'correct' : 'incorrect'}">
                <div class="question-header d-flex justify-content-between align-items-center">
                    <div>
                        <h5 class="mb-0 quest-text">Câu ${status.index + 1}</h5>
                        <small class="text-muted">Điểm: ${resultMap[question.assignQuesID].mark}/${question.questionMark}</small>
                    </div>
                    <div class="d-flex align-items-center">
                        <c:choose>
                            <c:when test="${resultMap[question.assignQuesID].answerIsCorrect}">
                                    <span class="badge bg-success me-2">
                                        <i class="fas fa-check me-1"></i>Đúng
                                    </span>
                            </c:when>
                            <c:when test="${resultMap[question.assignQuesID].answerLabel eq 'SKIPPED'}">
                                    <span class="badge bg-warning me-2">
                                        <i class="fas fa-minus me-1"></i>Bỏ qua
                                    </span>
                            </c:when>
                            <c:otherwise>
                                    <span class="badge bg-danger me-2">
                                        <i class="fas fa-times me-1"></i>Sai
                                    </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="question-content">
                    <c:if test="${not empty question.questionImg}">
                        <div class="question-image mb-2">
                            <img src="${question.questionImg}" alt="Hình minh họa" class="img-fluid rounded">
                        </div>
                    </c:if>
                    <div class="question-text mb-2">
                        <p class="mb-2">${question.questionText}</p>
                    </div>
                    <div class="answer-options mb-2">
                        <h6 class="mb-2">Các phương án trả lời:</h6>
                        <div class="list-group">
                            <c:forEach items="${question.answers}" var="answer">
                                <div class="list-group-item ${answer.optionLabel eq resultMap[question.assignQuesID].answerLabel ? (resultMap[question.assignQuesID].answerIsCorrect ? 'list-group-item-success' : 'list-group-item-danger') : ''}
                                                              ${answer.correct ? 'correct-answer' : ''} 
                                                              ${answer.optionLabel eq resultMap[question.assignQuesID].answerLabel ? 'user-answer' : ''}">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="d-flex align-items-center">
                                            <span class="answer-label me-3">${answer.optionLabel}.</span>
                                            <span class="answer-text">${answer.answerText}</span>
                                        </div>
                                        <div class="answer-indicators">
                                            <c:if test="${answer.optionLabel eq resultMap[question.assignQuesID].answerLabel}">
                                                    <span class="badge bg-primary me-1" title="Câu trả lời của bạn">
                                                        <i class="fas fa-user"></i>
                                                    </span>
                                            </c:if>
                                            <c:if test="${answer.correct}">
                                                    <span class="badge bg-success" title="Đáp án đúng">
                                                        <i class="fas fa-check"></i>
                                                    </span>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="answer-explanation border-top pt-2">
                        <div class="row">
                            <div class="col-6">
                                <div class="mb-1">
                                    <strong>Câu trả lời của bạn:</strong>
                                    <c:choose>
                                        <c:when test="${resultMap[question.assignQuesID].answerLabel eq 'SKIPPED'}">
                                            <span class="text-warning">Bỏ qua</span>
                                        </c:when>
                                        <c:otherwise>
                                                <span class="${resultMap[question.assignQuesID].answerIsCorrect ? 'text-success' : 'text-danger'}">
                                                        ${resultMap[question.assignQuesID].answerLabel}
                                                </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="col-6 text-end">
                                <div class="mb-1">
                                    <strong>Điểm số:</strong>
                                    <span class="text-primary">${resultMap[question.assignQuesID].mark} / ${question.questionMark}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 