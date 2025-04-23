<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        .result-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 20px;
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
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .question-card {
            border: 1px solid #e9ecef;
            border-radius: 8px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }

        .question-card.correct {
            border-color: #28a745;
        }

        .question-card.incorrect {
            border-color: #dc3545;
        }

        .question-header {
            padding: 15px 20px;
            border-bottom: 1px solid #e9ecef;
            background-color: #f8f9fa;
            border-radius: 8px 8px 0 0;
        }

        .question-content {
            padding: 20px;
        }

        .question-image img {
            max-width: 100%;
            height: auto;
            border-radius: 4px;
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
            border: 1px solid #e9ecef;
            margin-bottom: 8px;
            border-radius: 4px !important;
            transition: all 0.2s ease;
            position: relative;
            padding: 12px 15px;
        }

        .answer-options .list-group-item:hover {
            background-color: #f8f9fa;
        }

        .answer-options .list-group-item.correct-answer {
            border-color: #28a745;
            background-color: rgba(40, 167, 69, 0.1);
        }

        .answer-options .list-group-item.user-answer {
            border-width: 2px;
        }

        .answer-options .list-group-item.list-group-item-success {
            background-color: rgba(40, 167, 69, 0.1);
            border-color: #28a745;
        }

        .answer-options .list-group-item.list-group-item-danger {
            background-color: rgba(220, 53, 69, 0.1);
            border-color: #dc3545;
        }

        .answer-label {
            font-weight: 600;
            color: #495057;
            min-width: 25px;
            display: inline-block;
        }

        .answer-text {
            flex: 1;
        }

        .answer-indicators {
            display: flex;
            gap: 8px;
        }

        .answer-indicators .badge {
            padding: 6px 8px;
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

        @media (max-width: 768px) {
            .result-container {
                padding: 15px;
            }

            .stats-card {
                margin-bottom: 15px;
            }

            .question-content {
                padding: 15px;
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
            <p class="text-muted">Hoàn thành lúc: <fmt:formatDate value="${assignmentTaken.dateCreated}" pattern="HH:mm dd/MM/yyyy"/></p>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <div class="stats-card text-center">
                    <h3 class="text-primary mb-2">${assignmentResult.score}/10</h3>
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
                    <h3 class="mb-2"><fmt:formatNumber value="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}" maxFractionDigits="1"/>%</h3>
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
                            <h5 class="mb-0">Câu ${status.index + 1}</h5>
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
                        <div class="question-text">
                            <p class="mb-3">${question.questionText}</p>
                            
                            <c:if test="${not empty question.questionImg}">
                                <div class="question-image mb-3">
                                    <img src="${question.questionImg}" alt="Hình minh họa" class="img-fluid rounded">
                                </div>
                            </c:if>

                            <c:if test="${not empty question.audioFile}">
                                <div class="audio-player mb-3">
                                    <audio controls>
                                        <source src="${question.audioFile}" type="audio/mpeg">
                                        Trình duyệt của bạn không hỗ trợ phát audio.
                                    </audio>
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="answer-options mb-4">
                            <h6 class="mb-3">Các phương án trả lời:</h6>
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

                        <div class="answer-explanation border-top pt-3">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-2">
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
                                <div class="col-md-6 text-md-end">
                                    <div class="mb-2">
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