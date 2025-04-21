<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử làm bài</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css">
    <style>
        /* Keep the existing CSS styles as they are */
        :root {
            --primary-pink: #ff9bb3;
            --light-pink: #ffdfea;
            --soft-pink: #fff5f8;
            --primary-blue: #89c4f4;
            --light-blue: #d6edff;
            --dark-blue: #4b97e8;
            --accent-green: #77dd77;
            --text-dark: #2d3436;
            --text-medium: #636e72;
            --text-light: #b2bec3;
        }

        body {
            background-color: var(--soft-pink);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: var(--text-dark);
        }

        .container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 0 25px;
        }

        .page-header {
            text-align: center;
            margin-bottom: 40px;
            position: relative;
            padding-bottom: 20px;
        }

        .page-title {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--dark-blue);
            margin-bottom: 10px;
            background: linear-gradient(135deg, var(--primary-pink), var(--dark-blue));
            -webkit-background-clip: text;
            background-clip: text;
            color: transparent;
            display: inline-block;
        }

        .page-subtitle {
            color: var(--text-medium);
            font-size: 1.1rem;
            font-weight: 400;
        }

        .exam-history {
            background: white;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(255, 155, 179, 0.1);
            overflow: hidden;
            border: 1px solid rgba(255, 155, 179, 0.1);
        }

        .exam-item {
            padding: 30px;
            border-bottom: 1px solid rgba(137, 196, 244, 0.2);
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            position: relative;
            overflow: hidden;
        }

        .exam-item:last-child {
            border-bottom: none;
        }

        .exam-item:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 35px rgba(255, 155, 179, 0.15);
        }

        .exam-item::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: linear-gradient(to bottom, var(--primary-pink), var(--primary-blue));
        }

        .exam-title {
            font-size: 1.3rem;
            font-weight: 600;
            color: var(--text-dark);
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
        }

        .exam-badge {
            font-size: 0.8rem;
            padding: 6px 12px;
            border-radius: 20px;
            font-weight: 600;
            letter-spacing: 0.5px;
            text-transform: uppercase;
        }

        .exam-badge.full {
            background: linear-gradient(135deg, var(--primary-blue), var(--dark-blue));
            color: white;
        }

        .exam-badge.recording {
            background: linear-gradient(135deg, var(--primary-pink), #ff6b9e);
            color: white;
        }

        .exam-badge.practice {
            background: linear-gradient(135deg, var(--accent-green), #5cb85c);
            color: white;
        }

        .exam-info {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 30px;
            margin-top: 20px;
            position: relative;
            padding-right: 150px;
        }

        .exam-info-item {
            display: flex;
            align-items: center;
            gap: 10px;
            color: var(--text-medium);
        }

        .exam-info-item i {
            color: var(--primary-blue);
            font-size: 1.1rem;
            width: 20px;
            text-align: center;
        }

        .exam-score {
            font-weight: 700;
            color: var(--accent-green);
            font-size: 1.1rem;
        }

        .view-detail {
            position: absolute;
            right: 0;
            top: 50%;
            transform: translateY(-50%);
            color: white;
            text-decoration: none;
            font-weight: 500;
            padding: 8px 20px;
            border-radius: 20px;
            transition: all 0.3s;
            background: linear-gradient(135deg, var(--primary-pink), var(--primary-blue));
            box-shadow: 0 4px 15px rgba(255, 155, 179, 0.3);
            display: flex;
            align-items: center;
            gap: 8px;
            white-space: nowrap;
        }

        .view-detail:hover {
            transform: translateY(-50%) scale(1.05);
            box-shadow: 0 6px 20px rgba(255, 155, 179, 0.4);
            color: white;
        }

        .date-bubble {
            position: absolute;
            right: 30px;
            top: 30px;
            background: white;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            color: var(--text-medium);
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            border: 1px solid rgba(137, 196, 244, 0.2);
        }

        .time-info-container {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 15px;
        }

        .time-info {
            color: var(--text-medium);
            font-size: 0.95rem;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        @media (max-width: 768px) {
            .exam-info {
                grid-template-columns: 1fr;
                gap: 15px;
                padding-right: 0;
            }

            .exam-title {
                padding-right: 80px;
            }

            .view-detail {
                position: relative;
                right: auto;
                top: auto;
                transform: none;
                margin-top: 15px;
                display: inline-flex;
                justify-content: center;
                width: 100%;
            }

            .date-bubble {
                position: relative;
                right: auto;
                top: auto;
                display: inline-block;
                margin-bottom: 15px;
            }

            .time-info-container {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .exam-item {
            animation: fadeIn 0.5s ease-out forwards;
        }

        .exam-item:nth-child(1) { animation-delay: 0.1s; }
        .exam-item:nth-child(2) { animation-delay: 0.2s; }
        .exam-item:nth-child(3) { animation-delay: 0.3s; }
        .exam-item:nth-child(4) { animation-delay: 0.4s; }
        .exam-item:nth-child(5) { animation-delay: 0.5s; }
    </style>
</head>
<body>
<div class="container">
    <header class="page-header">
        <h1 class="page-title">Lịch sử làm bài</h1>
        <p class="page-subtitle">Xem lại kết quả các bài kiểm tra bạn đã hoàn thành</p>
    </header>

    <div class="exam-history">
        <c:if test="${empty examTakens}">
            <div class="exam-item">
                <div class="exam-title">Chưa có lịch sử làm bài.</div>
            </div>
        </c:if>
        <c:forEach var="examTaken" items="${examTakens}" varStatus="status">
            <div class="exam-item">
                <div class="date-bubble">
                    <fmt:formatDate value="${examTaken.dateCreated}" pattern="dd/MM/yyyy"/>
                </div>
                <div class="exam-title">
                    <!-- Exam title needs to come from Exam object -->
                    <c:out value="Exam ID: ${examTaken.examID}"/>
                    <span class="exam-badge full">Full test</span>
                    <!-- Adjust badge logic based on exam type if available -->
                </div>
                <div class="exam-info">
                    <div class="exam-info-item">
                        <i class="bi bi-calendar"></i>
                        <span>Ngày làm: <fmt:formatDate value="${examTaken.dateCreated}" pattern="dd/MM/yyyy"/></span>
                    </div>
                    <div class="exam-info-item">
                        <i class="bi bi-star-fill"></i>
                        <span>Kết quả: <span class="exam-score">${examTaken.finalMark}</span></span>
                    </div>
                    <div class="exam-info-item">
                        <i class="bi bi-clock"></i>
                        <span>Thời gian làm bài:
                            <c:choose>
                                <c:when test="${not empty examTaken.timeTaken}">
                                    ${examTaken.timeTaken}
                                </c:when>
                                <c:otherwise>Chưa ghi nhận</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/examResult?examTakenId=${examTaken.examTakenID}" class="view-detail">
                    <i class="bi bi-chevron-right"></i> Xem chi tiết
                </a>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>