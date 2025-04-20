<%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 4/14/2025
  Time: 9:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="model.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Answer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Làm bài thi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #faf0f5; /* Very light pastel pink background */
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .timer {
            position: fixed;
            top: 20px;
            right: 20px;
            background: linear-gradient(135deg, #e1f5fe 0%, #b3e5fc 100%);
            padding: 15px 20px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            z-index: 1000;
            border: none;
            animation: pulse 2s infinite;
            text-align: center;
            width: 240px;
        }

        .timer h4 {
            color: #0277bd; /* Darker blue for contrast */
            margin: 0;
        }

        .container {
            max-width: calc(100% - 300px);
            margin-right: 280px;
            margin-left: 20px;
            background-color: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 0 20px rgba(0,0,0,0.05);
            margin-top: 30px;
        }

        h2 {
            color: #d81b60; /* Pink accent color */
            margin-bottom: 20px;
        }

        .question-container {
            margin-bottom: 30px;
            padding: 25px;
            border: 1px solid #f8bbd0; /* Light pink border */
            border-radius: 10px;
            background-color: white;
            transition: all 0.3s ease;
        }

        .question-container:hover {
            box-shadow: 0 5px 15px rgba(216, 27, 96, 0.1);
        }

        .question-text {
            font-size: 1.1rem;
            margin-bottom: 20px;
            font-weight: normal;
            cursor: pointer;
            padding: 15px;
            border-radius: 8px;
            transition: all 0.3s ease;
            background-color: #f8f8f8;
            color: #333;
        }

        .question-text:hover {
            background-color: #f3e5f5; /* Light purple-pink */
        }

        .question-text.marked {
            background-color: #fff0f5; /* Very light pink */
            border: 1px solid #f8bbd0;
            box-shadow: 0 0 0 2px #f8bbd0;
        }

        .question-number {
            font-weight: bold;
            margin-right: 8px;
            color: #0277bd; /* Blue */
        }

        .answers {
            margin-left: 15px;
        }

        .answer-option {
            margin: 15px 0;
            padding: 12px 15px;
            border: 1px solid #e1f5fe; /* Pastel blue */
            border-radius: 8px;
            cursor: pointer;
            display: flex;
            align-items: flex-start;
            transition: all 0.2s ease;
            background-color: #f8fafc;
        }

        .answer-option:hover {
            background-color: #e1f5fe; /* Pastel blue */
            transform: translateX(5px);
        }

        .answer-label {
            font-weight: bold;
            margin-right: 12px;
            min-width: 20px;
            color: #d81b60; /* Pink */
        }

        .answer-text {
            flex: 1;
            color: #333;
        }

        .question-grid {
            display: grid;
            grid-template-columns: repeat(5, 1fr);
            gap: 10px;
            margin-bottom: 25px;
            padding: 20px;
            background: transparent;
            width: 240px;
            position: fixed;
            top: 300px;
            right: 30px;
        }

        .question-number-btn {
            padding: 10px;
            border: 1px solid #b3e5fc;
            border-radius: 6px;
            background: white;
            cursor: pointer;
            text-align: center;
            transition: all 0.3s ease;
            font-size: 0.9rem;
            color: #0277bd; /* Blue */
        }

        .question-number-btn:hover {
            background: #b3e5fc; /* Lighter blue */
        }

        .question-number-btn.active {
            background: #0277bd; /* Blue */
            color: white;
            border-color: #0277bd;
        }

        .question-number-btn.answered {
            background: #c8e6c9; /* Pastel green */
            border-color: #a5d6a7;
            color: #2e7d32;
        }

        .question-number-btn.marked {
            background: #f8bbd0; /* Pink */
            border-color: #f48fb1;
            color: #ad1457;
        }

        .question-number-btn.answered.marked {
            background: linear-gradient(135deg, #c8e6c9 50%, #f8bbd0 50%);
            border-color: #a5d6a7;
        }

        .legend {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 25px;
            padding: 20px;
            background: white;
            border-radius: 12px;
            border: 1px solid #e1f5fe;
            box-shadow: 0 3px 10px rgba(0,0,0,0.05);
            position: fixed;
            top: 120px;
            right: 20px;
            width: 240px;
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 0.9rem;
        }

        .legend-color {
            width: 20px;
            height: 20px;
            border-radius: 4px;
            border: 1px solid #e1f5fe;
        }

        .legend-current { background: #0277bd; }
        .legend-answered { background: #c8e6c9; }
        .legend-marked { background: #f8bbd0; }
        .legend-mixed { background: linear-gradient(135deg, #c8e6c9 50%, #f8bbd0 50%); }

        .btn-secondary {
            background-color: #b3e5fc; /* Light blue */
            border-color: #81d4fa;
            color: #01579b;
        }

        .btn-secondary:hover {
            background-color: #81d4fa;
            border-color: #4fc3f7;
            color: #01579b;
        }

        .btn-primary {
            background-color: #d81b60; /* Pink */
            border-color: #c2185b;
        }

        .btn-primary:hover {
            background-color: #c2185b;
            border-color: #ad1457;
        }

        .alert-warning {
            background-color: #fff8e1;
            border-color: #ffe0b2;
            color: #ff8f00;
        }

        @keyframes pulse {
            0% { box-shadow: 0 0 0 0 rgba(0,119,204,0.4); }
            70% { box-shadow: 0 0 0 10px rgba(0,119,204,0); }
            100% { box-shadow: 0 0 0 0 rgba(0,119,204,0); }
        }

        .shortcut-hint {
            position: fixed;
            bottom: 20px;
            right: 20px;
            background: white;
            padding: 10px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            font-size: 0.9rem;
            color: #666;
        }

        /* Main Content Area */
        .main-content {
            margin-right: 280px;
            padding: 20px;
            max-width: calc(100% - 280px);
        }

        @media (max-width: 992px) {
            .timer, .legend, .question-grid {
                position: static;
                width: 100%;
                margin-bottom: 20px;
            }

            .container {
                max-width: 100%;
                margin: 20px;
            }
        }
    </style>
</head>
<body>
<div class="timer">
    <h4>Thời gian còn lại: <span id="countdown">${not empty sessionScope.time ? sessionScope.time : '00'}:00</span></h4>
    <input type="hidden" id="countDirection" value="${not empty sessionScope.time ? 'down' : 'up'}">
</div>

<div class="container mt-4">
    <!-- Debug section -->
    <div style="display: none;" id="debugInfo">
        <h4>Debug Information</h4>
        <p>Exam ID: ${exam.examID}</p>
        <p>Exam Name: ${exam.examName}</p>
        <p>Number of Questions: ${questions.size()}</p>
        <p>Questions and Answers:</p>
        <c:forEach items="${questions}" var="q">
            <div>
                Question ${q.questionID}: ${q.questionText}
                <br>Number of answers: ${q.answers.size()}
                <br>Answers:
                <c:forEach items="${q.answers}" var="a">
                    <br>- ${a.optionLabel}: ${a.answerText}
                </c:forEach>
            </div>
        </c:forEach>
        <p>Exam Duration: ${examDuration} seconds</p>
        <p>Skill: ${sessionScope.skill}</p>
        <p>Time: ${sessionScope.time}</p>
    </div>

    <c:if test="${empty exam}">
        <div class="alert alert-warning">
            <h4>Thông báo</h4>
            <p>Không tìm thấy bài thi hoặc không có câu hỏi nào.</p>
            <a href="exam" class="btn btn-primary">Quay lại trang exam</a>
        </div>
    </c:if>

    <c:if test="${not empty exam}">
        <h2 class="mb-3">${exam.examName}</h2>
        <p class="mb-4">${exam.examDescription}</p>

        <div class="d-flex">
            <!-- Sidebar -->
            <div class="me-4">
                <div class="legend">
                    <div class="legend-item">
                        <div class="legend-color legend-current"></div>
                        <span>Câu hiện tại</span>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color legend-answered"></div>
                        <span>Đã trả lời</span>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color legend-marked"></div>
                        <span>Đánh dấu review</span>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color legend-mixed"></div>
                        <span>Đã trả lời + Review</span>
                    </div>
                </div>
                <div class="question-grid">
                    <c:forEach begin="1" end="${questions.size()}" var="i">
                        <button type="button" class="question-number-btn" data-index="${i-1}">${i}</button>
                    </c:forEach>
                </div>
            </div>
    
                <!-- Main content -->
                <div class="flex-fill">
                    <form id="examForm" action="submitExam" method="post" onsubmit="return validateForm()">
                        <!-- Add hidden fields for exam information -->
                        <input type="hidden" name="examId" value="${exam.examID}">
                        <input type="hidden" name="examTakenID" value="${sessionScope.examTakenID}">
                        <input type="hidden" name="timeTaken" id="timeTaken">
                        <input type="hidden" name="timeInput" value="${sessionScope.time}">
                        <input type="hidden" name="totalQuestions" value="${questions.size()}">

                        <c:forEach items="${questions}" var="question" varStatus="status">
                            <div class="question-container" id="question-${status.index}" ${status.index > 0 ? 'style="display: none;"' : ''}>
                                <div class="question-text">
                                    <span class="question-number">Câu ${status.index + 1}:</span>
                                    ${question.questionText}
                                </div>
                                <div class="answers">
                                    <c:forEach items="${question.answers}" var="answer" varStatus="answerStatus">
                                        <div class="answer-option">
                                            <input type="radio" 
                                                   name="answer_${question.questionID}" 
                                                   value="${answer.answerID}"
                                                   data-option-label="${answer.optionLabel}"
                                                   id="q${status.index + 1}_answer_${answerStatus.index + 1}">
                                            <span class="answer-label">${answer.optionLabel}.</span>
                                            <span class="answer-text">${answer.answerText}</span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
    
                      
                        <div class="text-center my-4">
                            <button type="button" class="btn btn-secondary me-2" id="prevBtnBottom">← Câu trước</button>
                            <button type="button" class="btn btn-secondary me-2" id="nextBtnBottom">Câu tiếp →</button>
                            <button type="submit" class="btn btn-primary btn-lg" id="submitExamBtn">Nộp bài</button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script>
    // Timer functionality
    function formatTime(seconds) {
        var minutes = Math.floor(seconds / 60);
        var remainingSeconds = seconds % 60;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        remainingSeconds = remainingSeconds < 10 ? "0" + remainingSeconds : remainingSeconds;
        return minutes + ":" + remainingSeconds;
    }

    function startTimer(duration) {
        var display = document.querySelector('#countdown');
        var seconds = duration;
        var timer = setInterval(function() {
            display.textContent = formatTime(seconds);

            // Warning when 5 minutes remaining
            if (seconds === 300) {
                alert('Còn 5 phút nữa hết giờ!');
            }

            if (seconds > 0) {
                seconds--;
            } else {
                clearInterval(timer);
                alert('Hết giờ! Bài thi của bạn sẽ được nộp tự động.');
                submitExam();
            }
        }, 1000);
    }

    // Initialize timer when page loads
    window.addEventListener('DOMContentLoaded', function() {
        var duration = 3600; // 60 minutes in seconds
        startTimer(duration);
    });

    // Save answers to localStorage
    function saveAnswers() {
        const answers = {};
        document.querySelectorAll('input[type="radio"]:checked').forEach(radio => {
            answers[radio.name] = radio.value;
        });
        localStorage.setItem('examAnswers', JSON.stringify(answers));

        // Save marked questions
        const markedQuestions = Array.from(document.querySelectorAll('.question-text.marked'))
            .map(q => q.closest('.question-container').id);
        localStorage.setItem('markedQuestions', JSON.stringify(markedQuestions));
    }

    // Load answers from localStorage
    function loadAnswers() {
        const savedAnswers = localStorage.getItem('examAnswers');
        if (savedAnswers) {
            const answers = JSON.parse(savedAnswers);
            Object.entries(answers).forEach(([name, value]) => {
                const radio = document.querySelector(`input[name="${name}"][value="${value}"]`);
                if (radio) {
                    radio.checked = true;
                    const index = parseInt(radio.closest('.question-container').id.split('-')[1]);
                    if (numberButtons[index]) {
                        numberButtons[index].classList.add('answered');
                    }
                }
            });
        }

        // Load marked questions
        const savedMarked = localStorage.getItem('markedQuestions');
        if (savedMarked) {
            const markedQuestions = JSON.parse(savedMarked);
            markedQuestions.forEach(id => {
                const index = parseInt(id.split('-')[1]);
                toggleReview(index);
            });
        }
    }

    // Auto save every 30 seconds
    setInterval(saveAnswers, 30000);

    // Update timeTaken when submitting
    let startTime = Date.now();
    let isSubmitting = false;
    
    function validateForm() {
        // Set submitting flag
        isSubmitting = true;
        
        // Calculate time taken in minutes
        const timeTaken = Math.floor((Date.now() - startTime) / 60000);
        document.getElementById('timeTaken').value = timeTaken;

        // Count answered questions
        let answered = 0;
        const total = document.querySelectorAll('.question-container').length;
        
        document.querySelectorAll('input[type="radio"]:checked').forEach(() => {
            answered++;
        });

        // Show warning if not all questions are answered
        if (answered < total) {
            const shouldSubmit = confirm('Bạn chưa trả lời hết các câu hỏi (' + answered + '/' + total + '). Bạn có chắc muốn nộp bài?');
            if (!shouldSubmit) {
                isSubmitting = false;
            }
            return shouldSubmit;
        }
        
        return true;
    }

    // Submit exam function
    function submitExam() {
        if (validateForm()) {
            const form = document.getElementById('examForm');
            form.submit();
        }
    }

    // Prevent accidental page leave only when not submitting
    window.onbeforeunload = function(e) {
        if (!isSubmitting) {
            saveAnswers();
            return 'Bạn có chắc chắn muốn rời khỏi trang? Các câu trả lời sẽ được lưu lại.';
        }
    };

    // Question navigation and marking functionality
    const questions = document.querySelectorAll('.question-container');
    const numberButtons = document.querySelectorAll('.question-number-btn');
    const questionTexts = document.querySelectorAll('.question-text');
    let currentIndex = 0;

    function showQuestion(index) {
        questions.forEach(q => q.style.display = 'none');
        questions[index].style.display = 'block';
        updateQuestionButtons(index);
    }

    function updateQuestionButtons(activeIndex) {
        numberButtons.forEach((btn, i) => {
            btn.classList.remove('active');
            if (i === activeIndex) btn.classList.add('active');
        });
    }

    function toggleReview(index) {
        if (numberButtons[index] && questionTexts[index]) {
            numberButtons[index].classList.toggle('marked');
            questionTexts[index].classList.toggle('marked');
        }
    }

    // Navigation buttons
    document.getElementById('prevBtnBottom')?.addEventListener('click', function() {
        if (currentIndex > 0) {
            currentIndex--;
            showQuestion(currentIndex);
        }
    });

    document.getElementById('nextBtnBottom')?.addEventListener('click', function() {
        if (currentIndex < questions.length - 1) {
            currentIndex++;
            showQuestion(currentIndex);
        }
    });

    // Add click handlers for question texts
    questionTexts.forEach((text, index) => {
        text.addEventListener('click', function(e) {
            toggleReview(index);
        });
    });

    // Question number buttons click handlers
    numberButtons.forEach((btn, index) => {
        btn.addEventListener('click', function(e) {
            if (e.ctrlKey || e.metaKey) {
                // Mark for review
                toggleReview(index);
            } else {
                // Navigate to question
                currentIndex = index;
                showQuestion(currentIndex);
            }
        });
    });

    // Mark questions as answered when an option is selected
    document.querySelectorAll('input[type="radio"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const questionContainer = this.closest('.question-container');
            const index = Array.from(questions).indexOf(questionContainer);
            if (numberButtons[index]) {
                numberButtons[index].classList.add('answered');
            }
        });
    });

    // Add keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        if (e.key === 'ArrowLeft' && currentIndex > 0) {
            currentIndex--;
            showQuestion(currentIndex);
        } else if (e.key === 'ArrowRight' && currentIndex < questions.length - 1) {
            currentIndex++;
            showQuestion(currentIndex);
        } else if (e.key === 'r' || e.key === 'R') {
            // Press R to toggle review for current question
            toggleReview(currentIndex);
        }
    });

    // Initialize
    window.addEventListener('DOMContentLoaded', function() {
        if (questions.length > 0) {
            showQuestion(0);
            loadAnswers();
        }
    });
</script>

<!-- Add keyboard shortcuts hint -->
<div class="shortcut-hint">
    Phím tắt: <br>
    ← → : Di chuyển câu<br>
    R : Đánh dấu review<br>
    Space : Chọn đáp án
</div>
</body>
</html>
