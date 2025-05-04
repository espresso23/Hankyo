<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Exam" %>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Thư viện đề thi</title>
    <style>
        :root {
            --primary-pink: #ff9bb3; /* Soft pastel pink */
            --light-pink: #ffd6e7; /* Very light pink */
            --soft-pink: #fff0f6; /* Almost white pink */
            --primary-blue: #89c4f4; /* Soft pastel blue */
            --light-blue: #e1f5fe; /* Very light blue */
            --dark-blue: #4b97e8; /* Slightly darker blue */
            --text-dark: #333;
            --text-medium: #555;
            --text-light: #777;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #fff9fb; /* Very light pastel pink background */
            min-height: 100vh;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 30px 20px;
        }

        h1 {
            color: var(--text-dark);
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5rem;
            background: linear-gradient(to right, var(--primary-pink), var(--primary-blue));
            -webkit-background-clip: text;
            background-clip: text;
            color: transparent;
            position: relative;
            padding-bottom: 15px;
        }

        h1::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 100px;
            height: 4px;
            background: linear-gradient(to right, var(--primary-pink), var(--primary-blue));
            border-radius: 2px;
        }

        .filter-container {
            margin-bottom: 30px;
            padding: 25px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(248, 187, 208, 0.2);
            border: 1px solid #fce4ec;
        }

        .filter-tabs {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .filter-tab {
            padding: 12px 24px;
            background: white;
            border-radius: 30px;
            cursor: pointer;
            text-decoration: none;
            color: var(--text-medium);
            transition: all 0.3s ease;
            font-weight: 500;
            box-shadow: 0 2px 8px rgba(137, 196, 244, 0.1);
            border: 1px solid #e1f5fe;
        }

        .exam-history-btn {
            width: 17vh;
            padding: 12px 24px;
            background: white;
            border-radius: 30px;
            cursor: pointer;
            text-decoration: none;
            color: var(--text-medium);
            transition: all 0.3s ease;
            font-weight: 500;
            box-shadow: 0 2px 8px rgba(137, 196, 244, 0.1);
            border: 1px solid #e1f5fe;
            position: absolute;
            left: 142.5vh;
            top: 24.8vh;
        }

        .exam-history-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(137, 196, 244, 0.2);
            color: var(--primary-blue);
            border-color: var(--primary-blue);
        }
        .filter-tab:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(137, 196, 244, 0.2);
            color: var(--primary-blue);
            border-color: var(--primary-blue);
        }

        .filter-tab.active {
            background: linear-gradient(45deg, var(--primary-pink), var(--primary-blue));
            color: white;
            box-shadow: 0 4px 15px rgba(255, 155, 179, 0.3);
            border: none;
        }

        .search-form {
            display: flex;
            gap: 10px;
            width: 100%;
        }

        .search-box {
            flex: 1;
            padding: 12px 20px;
            border: 2px solid #e1f5fe;
            border-radius: 30px;
            font-size: 16px;
            transition: all 0.3s;
            background: white;
            color: var(--text-dark);
            width: 100%;
        }

        .search-box:focus {
            outline: none;
            border-color: var(--primary-blue);
            box-shadow: 0 0 0 3px rgba(137, 196, 244, 0.3);
        }

        .exam-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            gap: 25px;
        }

        .exam-card {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(248, 187, 208, 0.15);
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            position: relative;
            overflow: hidden;
            border: 1px solid #fce4ec;
        }

        .exam-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 25px rgba(248, 187, 208, 0.25);
        }

        .exam-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 5px;
            height: 100%;
            background: linear-gradient(to bottom, var(--primary-pink), var(--primary-blue));
        }

        .exam-title {
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 15px;
            color: var(--text-dark);
            position: relative;
            padding-left: 15px;
        }

        .exam-title::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 6px;
            height: 6px;
            border-radius: 50%;
            background: var(--primary-pink);
        }

        .exam-desc {
            margin: 15px 0;
            color: var(--text-medium);
            font-size: 15px;
            line-height: 1.5;
        }

        .exam-meta {
            font-size: 14px;
            color: var(--text-light);
            margin: 15px 0;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .exam-meta div {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .exam-meta div::before {
            content: '•';
            color: var(--primary-blue);
            font-size: 18px;
        }

        .btn-container {
            margin-top: 20px;
            display: flex;
            gap: 12px;
        }

        .btn {
            padding: 10px 20px;
            border-radius: 30px;
            text-decoration: none;
            font-weight: 500;
            font-size: 14px;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            flex: 1;
        }

        .btn i {
            margin-right: 8px;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .btn-details {
            background: var(--light-blue);
            color: var(--dark-blue);
            border: 1px solid rgba(75, 151, 232, 0.2);
        }

        .btn-details:hover {
            background: rgba(137, 196, 244, 0.2);
        }

        .btn-start {
            background: linear-gradient(45deg, var(--primary-pink), var(--primary-blue));
            color: white;
            border: none;
            box-shadow: 0 4px 10px rgba(255, 155, 179, 0.3);
        }

        .btn-start:hover {
            background: linear-gradient(45deg, #ff8aa3, #7ab6f2);
            box-shadow: 0 6px 15px rgba(255, 155, 179, 0.4);
        }

        .no-exams {
            text-align: center;
            padding: 50px;
            color: var(--text-medium);
            font-size: 18px;
            grid-column: 1 / -1;
            background: white;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(248, 187, 208, 0.1);
            border: 1px dashed #f8bbd0;
        }

        @media (max-width: 768px) {
            .filter-tabs {
                justify-content: center;
            }

            .search-form {
                flex-direction: column;
            }

            .exam-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="container">
    <h1>Thư viện đề thi</h1>

    <div class="filter-container">
        <div class="filter-tabs">
            <a href="exam?action=list" class="filter-tab ${param.examType == null || param.examType == 'all' ? 'active' : ''}">
                <i class="fas fa-list"></i> Tất cả
            </a>
            <a href="exam?action=list&examType=TOPIKI" class="filter-tab ${param.examType == 'TOPIKI' ? 'active' : ''}">
                <i class="fas fa-certificate"></i> TOPIK I
            </a>
            <a href="exam?action=list&examType=TOPIKII" class="filter-tab ${param.examType == 'TOPIKII' ? 'active' : ''}">
                <i class="fas fa-award"></i> TOPIK II
            </a>
        </div>
        <div class="exam-history-btn" onclick="window.location.href='examHistory'">Lịch sử làm bài</div>
        <form class="search-form" action="exam" method="get">
            <input type="hidden" name="action" value="list" />
            <input type="text" name="searchName" class="search-box" placeholder="🔍 Tìm kiếm theo tên đề thi..." />
        </form>
    </div>

    <div class="exam-grid">
        <%
            List<Exam> exams = (List<Exam>) request.getAttribute("exams");
            if (exams != null && !exams.isEmpty()) {
                for (Exam exam : exams) {
        %>
        <div class="exam-card">
            <div class="exam-title"><%= exam.getExamName() %></div>
            <div class="exam-desc"><%= exam.getExamDescription() %></div>
            <div class="exam-meta">
                <div>Loại đề: <%= exam.getExamType() != null ? exam.getExamType() : "Chưa xác định" %></div>
                <div>Ngày tạo: <%= exam.getDateCreated() != null ? exam.getDateCreated() : "Chưa xác định" %></div>
            </div>
            <div class="btn-container">
                <a href="exam?action=details&examID=<%= exam.getExamID() %>" class="btn btn-details">
                    <i class="fas fa-info-circle"></i> Chi tiết
                </a>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="no-exams">
            <i class="fas fa-book-open" style="font-size: 2rem; color: var(--primary-pink); margin-bottom: 15px;"></i>
            <p>Hiện chưa có đề thi nào trong thư viện</p>
        </div>
        <%
            }
        %>
    </div>
</div>

<script>
    // Xử lý tìm kiếm realtime
    document.querySelector('.search-box').addEventListener('input', function(e) {
        const searchTerm = e.target.value.toLowerCase();
        const examCards = document.querySelectorAll('.exam-card');
        let hasVisibleCard = false;

        examCards.forEach(card => {
            const title = card.querySelector('.exam-title').textContent.toLowerCase();
            const desc = card.querySelector('.exam-desc').textContent.toLowerCase();

            if (title.includes(searchTerm) || desc.includes(searchTerm)) {
                card.style.display = 'block';
                hasVisibleCard = true;
            } else {
                card.style.display = 'none';
            }
        });

        const noExamsDiv = document.querySelector('.no-exams');
        if (noExamsDiv) {
            noExamsDiv.style.display = hasVisibleCard ? 'none' : 'block';
        }
    });
</script>
<jsp:include page="header.jsp"></jsp:include>
</body>
</html>
