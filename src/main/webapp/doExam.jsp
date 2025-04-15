<%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 4/14/2025
  Time: 9:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Làm bài thi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* Header styles */
        .exam-header {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .exam-info {
            flex: 1;
        }

        .exam-title {
            font-size: 24px;
            color: #333;
            margin: 0 0 10px 0;
        }

        .exam-meta {
            color: #666;
            font-size: 14px;
        }

        .timer {
            background: #4CAF50;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 18px;
            font-weight: bold;
        }

        /* Question styles */
        .question-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .question-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .question-number {
            font-size: 18px;
            color: #333;
            font-weight: bold;
        }

        .question-content {
            margin-bottom: 20px;
        }

        .question-text {
            font-size: 16px;
            line-height: 1.5;
            color: #333;
            margin-bottom: 15px;
        }

        .question-image {
            max-width: 100%;
            height: auto;
            margin: 10px 0;
            border-radius: 4px;
        }

        .audio-player {
            margin: 10px 0;
            width: 100%;
        }

        /* Answer styles */
        .answer-options {
            display: grid;
            gap: 10px;
        }

        .answer-option {
            padding: 15px;
            border: 2px solid #ddd;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
        }

        .answer-option:hover {
            border-color: #4CAF50;
            background: #f9f9f9;
        }

        .answer-option.selected {
            border-color: #4CAF50;
            background: #e8f5e9;
        }

        .option-label {
            width: 30px;
            height: 30px;
            background: #4CAF50;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            font-weight: bold;
        }

        .option-text {
            flex: 1;
        }

        /* Navigation buttons */
        .nav-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .nav-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background 0.3s;
        }

        .prev-btn {
            background: #f44336;
            color: white;
        }

        .next-btn {
            background: #4CAF50;
            color: white;
        }

        .submit-btn {
            background: #2196F3;
            color: white;
        }

        .nav-btn:hover {
            opacity: 0.9;
        }

        .nav-btn:disabled {
            background: #cccccc;
            cursor: not-allowed;
        }

        /* Progress bar */
        .progress-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .progress-title {
            font-size: 18px;
            color: #333;
            margin-bottom: 10px;
        }

        .progress-bar {
            height: 10px;
            background: #eee;
            border-radius: 5px;
            overflow: hidden;
        }

        .progress {
            height: 100%;
            background: #4CAF50;
            transition: width 0.3s;
        }

        /* Responsive styles */
        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }

            .exam-header {
                flex-direction: column;
                gap: 10px;
            }

            .timer {
                width: 100%;
                text-align: center;
            }

            .nav-buttons {
                flex-direction: column;
                gap: 10px;
            }

            .nav-btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="exam-header">
        <div class="exam-info">
            <h1 class="exam-title">Đề thi: <%= request.getParameter("examName") %></h1>
            <div class="exam-meta">
                <p>Kỹ năng: <%= request.getParameter("skill") %></p>
                <p>Thời gian: <%= request.getParameter("time") %> phút</p>
            </div>
        </div>
        <div class="timer" id="timer">00:00:00</div>
    </div>

    <div class="progress-container">
        <div class="progress-title">Tiến độ làm bài</div>
        <div class="progress-bar">
            <div class="progress" id="progress" style="width: 0%"></div>
        </div>
    </div>

    <div class="question-container">
        <div class="question-header">
            <div class="question-number">Câu hỏi 1</div>
        </div>
        <div class="question-content">
            <div class="question-text">
                <!-- Question text will be inserted here -->
            </div>
            <img class="question-image" src="" alt="Question image">
            <audio class="audio-player" controls>
                <source src="" type="audio/mpeg">
                Your browser does not support the audio element.
            </audio>
        </div>
        <div class="answer-options">
            <!-- Answer options will be inserted here -->
        </div>
    </div>

    <div class="nav-buttons">
        <button class="nav-btn prev-btn" id="prevBtn" disabled>Câu trước</button>
        <button class="nav-btn next-btn" id="nextBtn">Câu tiếp theo</button>
        <button class="nav-btn submit-btn" id="submitBtn">Nộp bài</button>
    </div>
</div>

<script>
    // Timer functionality
    let timeLeft = <%= request.getParameter("time") %> * 60; // Convert minutes to seconds
    const timerDisplay = document.getElementById('timer');

    function updateTimer() {
        const hours = Math.floor(timeLeft / 3600);
        const minutes = Math.floor((timeLeft % 3600) / 60);
        const seconds = timeLeft % 60;

        timerDisplay.textContent = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            submitExam();
        } else {
            timeLeft--;
        }
    }

    const timerInterval = setInterval(updateTimer, 1000);

    // Navigation functionality
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const submitBtn = document.getElementById('submitBtn');
    let currentQuestion = 1;

    function updateNavigationButtons() {
        prevBtn.disabled = currentQuestion === 1;
        nextBtn.disabled = currentQuestion === totalQuestions;
    }

    // Progress bar functionality
    const progressBar = document.getElementById('progress');
    const totalQuestions = 10; // This should be set dynamically

    function updateProgress() {
        const progress = (currentQuestion / totalQuestions) * 100;
        progressBar.style.width = `${progress}%`;
    }

    // Answer selection functionality
    document.querySelectorAll('.answer-option').forEach(option => {
        option.addEventListener('click', function() {
            document.querySelectorAll('.answer-option').forEach(opt => {
                opt.classList.remove('selected');
            });
            this.classList.add('selected');
        });
    });

    // Submit exam functionality
    function submitExam() {
        // Implement exam submission logic here
        window.location.href = 'exam?action=result&examID=<%= request.getParameter("examID") %>';
    }

    submitBtn.addEventListener('click', submitExam);
</script>
</body>
</html>
