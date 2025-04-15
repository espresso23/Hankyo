<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Kết quả bài thi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .result-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .result-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 10px;
        }

        .exam-info {
            color: #666;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .score-container {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            text-align: center;
        }

        .score {
            font-size: 48px;
            font-weight: bold;
            color: #4CAF50;
            margin: 10px 0;
        }

        .score-label {
            font-size: 18px;
            color: #666;
        }

        .stats-container {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-item {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
        }

        .stat-value {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin: 10px 0;
        }

        .stat-label {
            font-size: 14px;
            color: #666;
        }

        .details-container {
            margin-top: 30px;
        }

        .details-title {
            font-size: 20px;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #eee;
        }

        .question-item {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .question-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }

        .question-number {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .question-status {
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 14px;
        }

        .status-correct {
            background: #e8f5e9;
            color: #4CAF50;
        }

        .status-incorrect {
            background: #ffebee;
            color: #f44336;
        }

        .question-content {
            margin-bottom: 15px;
        }

        .question-text {
            font-size: 16px;
            color: #333;
            margin-bottom: 10px;
        }

        .answer-options {
            display: grid;
            gap: 10px;
        }

        .answer-option {
            padding: 10px;
            border: 2px solid #ddd;
            border-radius: 5px;
            display: flex;
            align-items: center;
        }

        .answer-option.correct {
            border-color: #4CAF50;
            background: #e8f5e9;
        }

        .answer-option.incorrect {
            border-color: #f44336;
            background: #ffebee;
        }

        .answer-option.selected {
            border-color: #2196F3;
            background: #e3f2fd;
        }

        .option-label {
            width: 25px;
            height: 25px;
            background: #4CAF50;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 10px;
            font-weight: bold;
        }

        .option-text {
            flex: 1;
        }

        .action-buttons {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 30px;
        }

        .action-btn {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background 0.3s;
        }

        .retry-btn {
            background: #4CAF50;
            color: white;
        }

        .back-btn {
            background: #2196F3;
            color: white;
        }

        .action-btn:hover {
            opacity: 0.9;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }

            .stats-container {
                grid-template-columns: 1fr;
            }

            .action-buttons {
                flex-direction: column;
                gap: 10px;
            }

            .action-btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="result-header">
        <h1 class="result-title">Kết quả bài thi</h1>
        <div class="exam-info">
            <p>Đề thi: <%= request.getParameter("examName") %></p>
            <p>Kỹ năng: <%= request.getParameter("skill") %></p>
            <p>Thời gian làm bài: <%= request.getParameter("time") %> phút</p>
        </div>
    </div>

    <div class="score-container">
        <div class="score-label">Điểm số của bạn</div>
        <div class="score"><%= request.getParameter("score") %>/<%= request.getParameter("totalScore") %></div>
    </div>

    <div class="stats-container">
        <div class="stat-item">
            <div class="stat-label">Số câu đúng</div>
            <div class="stat-value"><%= request.getParameter("correctAnswers") %></div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Số câu sai</div>
            <div class="stat-value"><%= request.getParameter("incorrectAnswers") %></div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Tổng số câu</div>
            <div class="stat-value"><%= request.getParameter("totalQuestions") %></div>
        </div>
    </div>

    <div class="details-container">
        <h2 class="details-title">Chi tiết bài làm</h2>
        <!-- Question items will be inserted here -->
    </div>

    <div class="action-buttons">
        <button class="action-btn retry-btn" onclick="window.location.href='exam?action=do&examID=<%= request.getParameter("examID") %>'">Làm lại</button>
        <button class="action-btn back-btn" onclick="window.location.href='exam?action=list'">Quay lại danh sách</button>
    </div>
</div>
</body>
</html>