<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
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

        /* Thêm CSS cho AI Help */
        .ai-help-btn {
            background: lightpink !important;
            color: white !important;
            border: none !important;
            padding: 6px 12px !important;
            border-radius: 6px !important;
            font-size: 0.9rem !important;
            transition: all 0.2s !important;
        }
        .ai-help-btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 2px 8px rgba(108, 180, 255, 0.2);
        }
        
        .ai-help-btn i {
            margin-right: 4px;
        }
        
        .ai-analysis {
            padding: 16px;
            background: #f8f9fa;
            border-radius: 8px;
            margin-top: 16px;
        }
        
        .analysis-content {
            line-height: 1.6;
            color: #343a40;
        }
        
        .analysis-content p {
            margin-bottom: 12px;
        }
        
        #aiHelpModal .modal-content {
            border-radius: 12px;
            border: none;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }
        
        #aiHelpModal .modal-header {
            background: linear-gradient(90deg, #ff8fa3, #6cb4ff);
            color: white;
            border-radius: 12px 12px 0 0;
            padding: 16px 20px;
        }
        
        #aiHelpModal .modal-body {
            padding: 20px;
            max-height: 70vh;
            overflow-y: auto;
        }
        
        .loading-spinner {
            display: none;
            text-align: center;
            padding: 20px;
        }
        
        .loading-spinner i {
            font-size: 24px;
            color: #ff8fa3;
            animation: spin 1s linear infinite;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* AI Help Popover */
        .ai-help-popover {
            position: absolute;
            z-index: 1050;
            min-width: 320px;
            max-width: 60%;
            background: #fff;
            border: 1.5px solid #e0e0e0;
            border-radius: 10px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.12);
            padding: 18px 20px 14px 20px;
            animation: fadeInPop 0.25s cubic-bezier(.4,0,.2,1);
            transition: opacity 0.2s;
            color: #333;
        }
        @keyframes fadeInPop {
            from { opacity: 0; transform: translateY(10px) scale(0.98); }
            to { opacity: 1; transform: translateY(0) scale(1); }
        }
        .ai-help-popover .ai-help-title {
            font-weight: 600;
            font-size: 1.05rem;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .ai-help-popover .ai-help-close {
            background: none;
            border: none;
            font-size: 1.2rem;
            color: #aaa;
            position: absolute;
            top: 8px;
            right: 12px;
            cursor: pointer;
            transition: color 0.15s;
        }
        .ai-help-popover .ai-help-close:hover {
            color: #ff8fa3;
        }
        .ai-help-popover .ai-help-content {
            min-height: 40px;
            font-size: 0.98rem;
            line-height: 1.6;
        }
        .ai-help-popover .ai-help-loading {
            text-align: center;
            color: #aaa;
            padding: 18px 0 10px 0;
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
<script>
$(document).ready(function() {
    // Thêm nút AI Help vào mỗi câu hỏi
    $('.question-header').each(function() {
        const questionNumber = $(this).closest('.question-card').find('.quest-text').text();
        $(this).append(`
            <button class="btn btn-sm ai-help-btn" data-question-number="${questionNumber}">
                <img src="asset/png/icon/explain-ai.png" alt="AI Help" style="width:20px;height:20px;margin-right:4px;vertical-align:middle;">Xem gợi ý AI
            </button>
        `);
    });
    // Xóa modal AI Help cũ nếu có
    $('#aiHelpModal').remove();
    // Đảm bảo chỉ có một popover AI Help trên trang
    let currentPopover = null;
    let currentBtn = null;
    $(document).on('click', '.ai-help-btn', function(e) {
        e.preventDefault();
        // Đóng popover cũ nếu đang mở
        if(currentPopover) { currentPopover.remove(); currentPopover = null; }
        if(currentBtn === this) { currentBtn = null; return; }
        currentBtn = this;
        const btn = $(this);
        const questionCard = btn.closest('.question-card');
        // Tạo popover
        const popover = $(`
            <div class="ai-help-popover">
                <div class="ai-help-title">
                    <img src='asset/png/icon/explain-ai.png' style='width:22px;height:22px;'>
                    Gợi ý từ AI
                </div>
                <button class="ai-help-close" title="Đóng">&times;</button>
                <div class="ai-help-content ai-help-loading">
                    <i class="fas fa-spinner fa-spin"></i> Đang tạo gợi ý...
                </div>
            </div>
        `);
        // Đóng khi bấm nút close
        popover.find('.ai-help-close').click(function(){ popover.remove(); currentPopover = null; currentBtn = null; });
        // Đóng khi click ngoài popover
        setTimeout(()=>{
            $(document).on('mousedown.aihelp', function(ev){
                if(!popover.is(ev.target) && popover.has(ev.target).length === 0 && !btn.is(ev.target)) {
                    popover.remove();
                    $(document).off('mousedown.aihelp');
                    currentPopover = null;
                    currentBtn = null;
                }
            });
        }, 10);
        // Thêm popover vào DOM
        $('body').append(popover);
        // Định vị popover cạnh nút
        const btnOffset = btn.offset();
        const btnHeight = btn.outerHeight();
        const popW = popover.outerWidth();
        const popH = popover.outerHeight();
        let top = btnOffset.top + btnHeight + 6;
        let left = btnOffset.left;
        // Nếu vượt phải màn hình thì dịch sang trái
        if(left + popW > $(window).width() - 12) left = $(window).width() - popW - 12;
        // Nếu vượt dưới màn hình thì hiển thị lên trên
        if(top + popH > $(window).scrollTop() + $(window).height() - 12) top = btnOffset.top - popH - 8;
        popover.css({top: top, left: left, position:'absolute'});
        currentPopover = popover;
        // Lấy dữ liệu câu hỏi
        const questionData = {
            questionText: questionCard.find('.question-text p').text(),
            answers: [],
            correctAnswer: questionCard.find('.correct-answer .answer-label').text().replace('.', ''),
            userAnswer: questionCard.find('.user-answer .answer-label').text().replace('.', '')
        };
        // Lấy các phương án trả lời
        questionCard.find('.answer-options .list-group-item').each(function() {
            const label = $(this).find('.answer-label').text().replace('.', '');
            const text = $(this).find('.answer-text').text();
            questionData.answers.push({label, text});
        });
        // Gọi API lấy gợi ý
        $.ajax({
            url: 'ai-assignment-help',
            method: 'POST',
            data: JSON.stringify(questionData),
            contentType: 'application/json',
            success: function(response) {
                popover.find('.ai-help-content').removeClass('ai-help-loading').html(response);
                // Gọi API lấy quota AI và chèn vào cuối popover
                fetch('ai-usage-info')
                  .then(res => res.json())
                  .then(data => {
                    let msg = '';
                    if (data.isVip) {
                      msg = '<div style="font-size:13px;color:#888;margin-top:8px;">AI: Không giới hạn</div>';
                    } else {
                      msg = '<div style="font-size:13px;color:#888;margin-top:8px;">AI: Đã dùng ' + data.used + '/20 lượt. Còn lại: ' + data.left;
                      if (data.left <= 3 && data.left > 0) {
                        msg += ' <b>(Sắp hết lượt!)</b>';
                      }
                      if (data.left == 0) {
                        msg += ' <b>(Đã hết lượt miễn phí!)</b>';
                      }
                      msg += '</div>';
                    }
                    popover.find('.ai-help-content').append(msg);
                  });
            },
            error: function() {
                popover.find('.ai-help-content').removeClass('ai-help-loading').html('<div class="alert alert-danger">Không thể tạo gợi ý lúc này. Vui lòng thử lại sau.</div>');
            }
        });
    });
});
</script>
</body>
</html> 
