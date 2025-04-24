<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Exam" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quản lý đề thi</title>
    <style>
        :root {
            --primary-pink: #ff9bb3;
            --light-pink: #ffd6e7;
            --soft-pink: #fff0f6;
            --primary-blue: #89c4f4;
            --light-blue: #e1f5fe;
            --dark-blue: #4b97e8;
            --text-dark: #333;
            --text-medium: #555;
            --text-light: #777;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #fff9fb;
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

        .management-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .btn-create-exam {
            padding: 12px 24px;
            background: linear-gradient(45deg, var(--primary-pink), var(--primary-blue));
            color: white;
            border: none;
            border-radius: 30px;
            font-weight: 500;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(255, 155, 179, 0.3);
        }

        .btn-create-exam:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(255, 155, 179, 0.4);
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

        .btn-edit {
            background: var(--light-blue);
            color: var(--dark-blue);
            border: 1px solid rgba(75, 151, 232, 0.2);
        }

        .btn-edit:hover {
            background: rgba(137, 196, 244, 0.2);
        }

        .btn-delete {
            background: #ffe5e5;
            color: #ff4d4d;
            border: 1px solid rgba(255, 77, 77, 0.2);
        }

        .btn-delete:hover {
            background: rgba(255, 77, 77, 0.1);
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
            .management-header {
                flex-direction: column;
                gap: 15px;
            }

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

        /* Modal styles */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 1000;
        }

        .modal-content {
            position: relative;
            background: white;
            margin: 50px auto;
            padding: 30px;
            width: 90%;
            max-width: 600px;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }

        .close-modal {
            position: absolute;
            right: 20px;
            top: 20px;
            font-size: 24px;
            cursor: pointer;
            color: var(--text-light);
            transition: color 0.3s;
        }

        .close-modal:hover {
            color: var(--text-dark);
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: var(--text-dark);
            font-weight: 500;
        }

        .form-control {
            width: 100%;
            padding: 10px 15px;
            border: 2px solid #e1f5fe;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary-blue);
            box-shadow: 0 0 0 3px rgba(137, 196, 244, 0.3);
        }

        .modal-footer {
            margin-top: 30px;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="management-header">
        <h1>Quản lý đề thi</h1>
        <button class="btn-create-exam" onclick="openCreateExamModal()">
            <i class="fas fa-plus"></i> Tạo đề thi mới
        </button>
    </div>

    <div class="filter-container">
        <div class="filter-tabs">
            <a href="exam?action=getExamLibrary"
               class="filter-tab ${param.examType == null || param.examType == 'all' ? 'active' : ''}">
                <i class="fas fa-list"></i> Tất cả
            </a>
            <a href="exam?action=getExamLibrary&examType=TOPIKI"
               class="filter-tab ${param.examType == 'TOPIKI' ? 'active' : ''}">
                <i class="fas fa-certificate"></i> TOPIK I
            </a>
            <a href="exam?action=getExamLibrary&examType=TOPIKII"
               class="filter-tab ${param.examType == 'TOPIKII' ? 'active' : ''}">
                <i class="fas fa-award"></i> TOPIK II
            </a>
        </div>

        <form class="search-form" action="exam" method="get">
            <input type="hidden" name="action" value="getExamLibrary"/>
            <input type="text" name="searchName" class="search-box" placeholder="🔍 Tìm kiếm theo tên đề thi..."
                   value="${param.searchName}"/>
        </form>
    </div>

    <div class="exam-grid">
        <c:choose>
            <c:when test="${not empty exams}">
                <c:forEach items="${exams}" var="exam">
                    <div class="exam-card">
                        <div class="exam-title">${exam.examName}</div>
                        <div class="exam-desc">${exam.examDescription}</div>
                        <div class="exam-meta">
                            <div>Loại đề: ${not empty exam.examType ? exam.examType : 'Chưa xác định'}</div>
                            <div>Ngày tạo: ${not empty exam.dateCreated ? exam.dateCreated : 'Chưa xác định'}</div>
                            <div>Trạng thái: ${not empty exam.status ? exam.status : 'Chưa xác định'}</div>
                        </div>
                        <div class="btn-container">
                            <a href="edit-exam?action=getExam&examID=${exam.examID}" class="btn btn-edit">
                                <i class="fas fa-edit"></i> Chỉnh sửa
                            </a>
                            <button class="btn btn-delete" onclick="confirmDelete(${exam.examID})">
                                <i class="fas fa-trash-alt"></i> Xóa
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-exams">
                    <i class="fas fa-book-open" style="font-size: 2rem; color: var(--primary-pink); margin-bottom: 15px;"></i>
                    <p>Hiện chưa có đề thi nào trong thư viện</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Modal tạo đề thi mới -->
<div id="createExamModal" class="modal">
    <div class="modal-content">
        <span class="close-modal" onclick="closeCreateExamModal()">&times;</span>
        <h2 style="margin-bottom: 20px;">Tạo đề thi mới</h2>
        <form id="createExamForm" action="exam" method="post">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
                <label for="examName">Tên đề thi <span class="text-danger">*</span></label>
                <input type="text" id="examName" name="examName" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="examType">Loại đề thi <span class="text-danger">*</span></label>
                <select id="examType" name="examType" class="form-control" required>
                    <option value="">Chọn loại đề thi</option>
                    <option value="TOPIKI">TOPIK I</option>
                    <option value="TOPIKII">TOPIK II</option>
                </select>
            </div>

            <div class="form-group">
                <label for="examDescription">Mô tả <span class="text-danger">*</span></label>
                <textarea id="examDescription" name="examDescription" class="form-control" rows="4" required></textarea>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeCreateExamModal()">Hủy</button>
                <button type="submit" class="btn btn-primary">Tạo đề thi</button>
            </div>
        </form>
    </div>
</div>

<script>
    // Xử lý tìm kiếm realtime
    document.querySelector('.search-box').addEventListener('input', function (e) {
        const searchTerm = e.target.value.toLowerCase();
        const examCards = document.querySelectorAll('.exam-card');
        let hasVisibleCard = false;

        examCards.forEach(card => {
            const title = card.querySelector('.exam-title').textContent.toLowerCase();
            const desc = card.querySelector('.exam-desc').textContent.toLowerCase();
            const type = card.querySelector('.exam-meta div:first-child').textContent.toLowerCase();

            if (title.includes(searchTerm) || desc.includes(searchTerm) || type.includes(searchTerm)) {
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

    // Modal functions
    function openCreateExamModal() {
        document.getElementById('createExamModal').style.display = 'block';
    }

    function closeCreateExamModal() {
        document.getElementById('createExamModal').style.display = 'none';
    }

    // Close modal when clicking outside
    window.onclick = function (event) {
        const modal = document.getElementById('createExamModal');
        if (event.target == modal) {
            closeCreateExamModal();
        }
    }

    // Xác nhận xóa đề thi
    function confirmDelete(examId) {
        if (confirm('Bạn có chắc chắn muốn xóa đề thi này không?')) {
            window.location.href = 'exam?action=delete&examID=' + examId;
        }
    }

    // Form validation
    document.getElementById('createExamForm').addEventListener('submit', function (e) {
        const examName = document.getElementById('examName').value.trim();
        const examType = document.getElementById('examType').value;
        const examDescription = document.getElementById('examDescription').value.trim();

        if (!examName || !examType || !examDescription) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin bắt buộc');
        }
    });
</script>
</body>
</html> 