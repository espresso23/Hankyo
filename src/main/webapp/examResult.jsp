<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Kết quả bài thi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-pink: #ffb6c1;
            --light-pink: #ffdfe4;
            --pastel-blue: #a2d2ff;
            --light-blue: #d9f0ff;
            --soft-purple: #cdb4db;
            --light-purple: #e9d8f5;
        }
        body {
            background-color: #fff9fb;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #5a5a5a;
        }
        .container {
            max-width: 900px;
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            padding: 30px;
            margin-top: 30px;
            margin-bottom: 30px;
        }
        .exam-title {
            color: #d46a92;
            font-size: 1.4rem;
            font-weight: 600;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }
        .exam-title .badge {
            font-size: 0.8rem;
            padding: 0.4em 0.8em;
            margin-left: 12px;
            background-color: var(--light-pink);
            color: #d46a92;
            border-radius: 50px;
        }
        .action-row {
            margin-bottom: 25px;
            display: flex;
            gap: 10px;
        }
        .action-row .btn {
            font-size: 0.9rem;
            padding: 0.4rem 1rem;
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        .btn-primary {
            background-color: var(--primary-pink);
            border-color: var(--primary-pink);
            color: white;
        }
        .btn-primary:hover {
            background-color: #ff8fab;
            border-color: #ff8fab;
        }
        .btn-outline {
            background-color: white;
            border: 1px solid var(--primary-pink);
            color: var(--primary-pink);
        }
        .btn-outline:hover {
            background-color: var(--light-pink);
        }
        .result-info {
            margin-bottom: 25px;
            font-size: 0.95rem;
            color: #777;
            background-color: #fef6f9;
            padding: 15px;
            border-radius: 10px;
            border-left: 4px solid var(--primary-pink);
        }
        .result-info div {
            margin-bottom: 8px;
            display: flex;
            justify-content: space-between;
            max-width: 300px;
        }
        .result-info span:first-child {
            font-weight: 500;
            color: #d46a92;
        }
        .stats-container {
            display: flex;
            gap: 20px;
            margin: 30px 0;
        }
        .stat-card {
            flex: 1;
            padding: 20px 15px;
            border-radius: 12px;
            text-align: center;
            background: white;
            box-shadow: 0 3px 10px rgba(0,0,0,0.05);
            transition: transform 0.3s ease;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .stat-card.correct {
            border-top: 4px solid #a8e6cf;
            background-color: #f5fffb;
        }
        .stat-card.incorrect {
            border-top: 4px solid #ffaaa5;
            background-color: #fff9f9;
        }
        .stat-card.skipped {
            border-top: 4px solid var(--pastel-blue);
            background-color: #f8fcff;
        }
        .stat-number {
            font-size: 2.2rem;
            font-weight: 700;
            margin-bottom: 5px;
            line-height: 1;
        }
        .stat-card.correct .stat-number {
            color: #5cb85c;
        }
        .stat-card.incorrect .stat-number {
            color: #ff6b6b;
        }
        .stat-card.skipped .stat-number {
            color: var(--pastel-blue);
        }
        .stat-label {
            font-size: 0.95rem;
            color: #888;
            margin-bottom: 3px;
        }
        .stat-sublabel {
            font-size: 0.8rem;
            color: #bbb;
        }
        .section-title {
            font-size: 1rem;
            color: #d46a92;
            margin: 30px 0 15px;
            padding-bottom: 8px;
            border-bottom: 2px solid #ffdfe4;
            font-weight: 500;
        }
        .progress-container {
            margin-top: 30px;
        }
        .progress {
            height: 10px;
            border-radius: 5px;
            background-color: #f0f0f0;
        }
        .progress-bar {
            background-color: var(--primary-pink);
        }
        .question-details {
            margin-bottom: 20px;
            padding: 15px;
            border-radius: 8px;
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .question-details p {
            margin-bottom: 10px;
        }
        .answer-correct {
            color: #28a745;
            font-weight: 500;
        }
        .answer-incorrect {
            color: #dc3545;
            font-weight: 500;
        }
        .answer-skipped {
            color: #6c757d;
            font-weight: 500;
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
        .answer-list {
            list-style-type: none;
            padding-left: 0;
        }
        .answer-item {
            margin-bottom: 5px;
        }
        .student-correct {
            color: #28a745;
            font-weight: 500;
        }
        .student-wrong {
            color: #dc3545;
            font-weight: 500;
        }
        .correct-answer {
            color: #28a745;
            font-weight: 500;
        }
        .correct-mark {
            color: #28a745;
            font-weight: 500;
        }
        .student-choice {
            color: #6c757d;
            font-weight: 500;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="container py-4">
    <c:if test="${empty exam || empty questions || empty examResults}">
        <div class="alert alert-warning">
            <h4>Thông báo</h4>
            <p>Không tìm thấy thông tin bài thi hoặc kết quả.</p>
            <a href="exam?action=list" class="btn btn-primary">Quay về thư viện đề</a>
        </div>
    </c:if>

    <c:if test="${not empty exam && not empty questions && not empty examResults}">
        <div class="exam-title">
            Kết quả luyện tập: ${exam.examName}
            <span class="badge">${skill}</span>
        </div>

        <div class="action-row">
            <a href="exam?action=do&examID=${exam.examID}&eQuesType=${skill}&time=${time}" class="btn btn-primary">Làm lại đề</a>
            <a href="${pageContext.request.contextPath}/examHistory" class="btn btn-outline">Xem lịch sử làm bài</a>
            <a href="exam?action=list" class="btn btn-outline">Quay về thư viện đề</a>
        </div>

        <div class="result-info">
            <div>
                <span>Kết quả làm bài:</span>
                <span>${doneQues}/${totalQuestions}</span>
            </div>
            <div>
                <span>Độ chính xác:</span>
                <span><fmt:formatNumber value="${score}" pattern="#.##"/>%</span>
            </div>
            <div>
                <span>Thời gian hoàn thành:</span>
                <span>
                    <c:choose>
                        <c:when test="${examTaken.timeTaken != null}">
                            <fmt:parseNumber var="seconds" value="${examTaken.timeTaken.time / 1000}" integerOnly="true"/>
                            <fmt:formatNumber value="${seconds div 60}" pattern="00"/>:<fmt:formatNumber value="${seconds mod 60}" pattern="00"/>
                        </c:when>
                        <c:otherwise>00:00</c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>

        <div class="stats-container">
            <div class="stat-card correct">
                <div class="stat-number">${correctAnswers}</div>
                <div class="stat-label">Trả lời đúng</div>
                <div class="stat-sublabel">câu hỏi</div>
            </div>
            <div class="stat-card incorrect">
                <div class="stat-number">${totalQuestions - correctAnswers - skipQues}</div>
                <div class="stat-label">Trả lời sai</div>
                <div class="stat-sublabel">câu hỏi</div>
            </div>
            <div class="stat-card skipped">
                <div class="stat-number">${skipQues}</div>
                <div class="stat-label">Bỏ qua</div>
                <div class="stat-sublabel">câu hỏi</div>
            </div>
        </div>

        <div class="progress-container">
            <div class="d-flex justify-content-between mb-2">
                <span>Tiến trình làm bài</span>
                <span><fmt:formatNumber value="${score}" pattern="#.##"/>%</span>
            </div>
            <div class="progress" role="progressbar" aria-valuenow="${score}" aria-valuemin="0" aria-valuemax="100">
                <div class="progress-bar" style="width: ${score}%"></div>
            </div>
        </div>

        <div class="section-title">${skill} • Chi tiết câu trả lời</div>

        <c:if test="${questions.size() == examResults.size()}">
            <c:forEach var="question" items="${questions}" varStatus="loop">
                <c:set var="result" value="${examResults[loop.index]}" />
                <c:if test="${result != null}">
                    <div class="question-details">
                        <p><strong>Câu ${loop.index + 1}:</strong> ${question.questionText}</p>
                        <div class="answer-list">
                            <c:forEach var="answer" items="${question.answers}">
                                <div class="answer-item ${result.answerLabel == answer.optionLabel ? (result.answerIsCorrect ? 'student-correct' : 'student-wrong') : ''} ${answer.correct ? 'correct-answer' : ''}">
                                    <strong>${answer.optionLabel}.</strong> ${answer.answerText}
                                    <c:if test="${result.answerLabel == answer.optionLabel}">
                                        <span class="student-choice">(Đáp án của bạn)</span>
                                     </c:if>
                                    <c:if test="${answer.correct}">
                                        <span class="correct-mark">(Đáp án đúng)</span>
                                    </c:if>
                                </div>
                            </c:forEach>
                            </span>
                        </p>
                        <button class="btn btn-sm ai-help-btn" type="button">
                            <img src="asset/png/icon/explain-ai.png" alt="AI Help" style="width:20px;height:20px;margin-right:4px;vertical-align:middle;"> Xem gợi ý AI
                        </button>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </c:if>

    </c:if>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
$(document).ready(function() {
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
        const questionCard = btn.closest('.question-details');
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
        const questionText = questionCard.find('p').first().text().replace(/^Câu \d+:\s*/, '');
        // Lấy các phương án trả lời
        const answers = [];
        questionCard.find('.answer-item').each(function() {
            const label = $(this).find('strong').first().text().replace('.', '').trim();
            const text = $(this).clone().children().remove().end().text().replace(/^\s*[A-Z]\./, '').trim();
            answers.push({label, text});
        });
        // Lấy đáp án đúng
        let correctAnswer = '';
        questionCard.find('.answer-item').each(function() {
            if($(this).hasClass('correct-answer')) {
                correctAnswer = $(this).find('strong').first().text().replace('.', '').trim();
            }
        });
        // Lấy đáp án của người dùng
        let userAnswer = '';
        questionCard.find('.answer-item').each(function() {
            if($(this).find('.student-choice').length > 0) {
                userAnswer = $(this).find('strong').first().text().replace('.', '').trim();
            }
        });
        const questionData = {
            questionText: questionText,
            answers: answers,
            correctAnswer: correctAnswer,
            userAnswer: userAnswer
        };
        // Gọi API lấy gợi ý
        $.ajax({
            url: 'ai-assignment-help',
            method: 'POST',
            data: JSON.stringify(questionData),
            contentType: 'application/json',
            success: function(response) {
                popover.find('.ai-help-content').removeClass('ai-help-loading').html(response);
            },
            error: function() {
                popover.find('.ai-help-content').removeClass('ai-help-loading').html('<div class="alert alert-danger">Không thể tạo gợi ý lúc này. Vui lòng thử lại sau.</div>');
            }
        });
    });
});
</script>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
