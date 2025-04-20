<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
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
            max-width: 800px;
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
    </style>
</head>
<body>
<div class="container py-4">
    <div class="exam-title">
        Kết quả luyện tập: ${examName}
        <span class="badge">${sectionName}</span>
    </div>

    <div class="action-row">
        <button class="btn btn-primary">Xem đáp án</button>
        <button class="btn btn-outline">Quay về trang đề thi</button>
    </div>

    <div class="result-info">
        <div>
            <span>Kết quả làm bài:</span>
            <span>${doneQues}/${totalQuestions}</span>
        </div>
        <div>
            <span>Độ chính xác:</span>
            <span><fmt:formatNumber value="${finalMark}" pattern="#.##"/>%</span>
        </div>
        <div>
            <span>Thời gian hoàn thành:</span>
            <span>${timeTaken}</span>
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
            <span><fmt:formatNumber value="${finalMark}" pattern="#.##"/>%</span>
        </div>
        <div class="progress">
            <div class="progress-bar" style="width: ${finalMark}%"></div>
        </div>
    </div>

    <div class="section-title">${sectionName} • Tổng quát</div>

    <!-- Thêm các phần chi tiết kết quả tại đây -->
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>