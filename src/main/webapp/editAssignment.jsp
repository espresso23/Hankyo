<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Chỉnh sửa bài tập">
    <meta name="author" content="Hankyo">
    <title>Chỉnh sửa Assignment</title>

    <!-- CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #0d6efd;
            --secondary-color: #6c757d;
            --success-color: #198754;
            --danger-color: #dc3545;
            --warning-color: #ffc107;
            --info-color: #0dcaf0;
            --light-color: #f8f9fa;
            --dark-color: #212529;
            --border-radius: 8px;
            --box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            --transition: all 0.3s ease;
        }

        body {
            background-color: var(--light-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
        }

        .main-container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        .card {
            border: none;
            border-radius: var(--border-radius);
            box-shadow: var(--box-shadow);
            margin-bottom: 1.5rem;
            transition: var(--transition);
            background: #fff;
        }

        .card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
        }

        .card-header {
            background-color: #fff;
            border-bottom: 1px solid #eee;
            padding: 1.25rem 1.5rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .question-card {
            background-color: #fff;
            border-radius: var(--border-radius);
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            box-shadow: var(--box-shadow);
            position: relative;
            border: 1px solid #eee;
        }

        .question-card:hover {
            border-color: var(--primary-color);
        }

        .question-number {
            position: absolute;
            top: -10px;
            left: -10px;
            background: var(--primary-color);
            color: white;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            box-shadow: var(--box-shadow);
        }

        .media-preview {
            max-width: 200px;
            max-height: 200px;
            border-radius: var(--border-radius);
            object-fit: cover;
            margin-top: 0.5rem;
            border: 1px solid #ddd;
        }

        .answer-option {
            margin-bottom: 0.75rem;
            padding: 0.75rem;
            background-color: var(--light-color);
            border-radius: var(--border-radius);
            transition: var(--transition);
        }

        .answer-option:hover {
            background-color: #e9ecef;
        }

        .btn {
            border-radius: var(--border-radius);
            padding: 0.5rem 1rem;
            font-weight: 500;
            transition: var(--transition);
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }

        .btn i {
            font-size: 1rem;
        }

        .btn-primary {
            background-color: var(--primary-color);
            border: none;
        }

        .btn-danger {
            background-color: var(--danger-color);
            border: none;
        }

        .btn-success {
            background-color: var(--success-color);
            border: none;
        }

        .form-control, .form-select {
            border-radius: var(--border-radius);
            padding: 0.75rem;
            border: 1px solid #ced4da;
            transition: var(--transition);
        }

        .form-control:focus, .form-select:focus {
            box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, .25);
            border-color: var(--primary-color);
        }

        .file-input-container {
            position: relative;
            overflow: hidden;
            display: inline-block;
            width: 100%;
        }

        .file-input-label {
            display: inline-block;
            padding: 0.75rem 1rem;
            background-color: var(--light-color);
            border: 1px solid #ced4da;
            border-radius: var(--border-radius);
            cursor: pointer;
            transition: var(--transition);
            width: 100%;
            text-align: center;
        }

        .file-input-label:hover {
            background-color: #e9ecef;
        }

        .file-input-label i {
            margin-right: 0.5rem;
        }

        .preview-container {
            margin-top: 0.75rem;
            text-align: center;
        }

        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.8);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .spinner {
            width: 40px;
            height: 40px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid var(--primary-color);
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .form-floating {
            position: relative;
            margin-bottom: 1rem;
        }

        .form-floating > .form-control,
        .form-floating > .form-select {
            height: calc(3.5rem + 2px);
            padding: 1rem 0.75rem;
        }

        .form-floating > label {
            position: absolute;
            top: 0;
            left: 0;
            height: 100%;
            padding: 1rem 0.75rem;
            pointer-events: none;
            border: 1px solid transparent;
            transform-origin: 0 0;
            transition: opacity .1s ease-in-out,transform .1s ease-in-out;
        }
    </style>
</head>
<body>
<div class="loading-overlay">
    <div class="spinner"></div>
</div>

<div class="main-container">
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h2 class="mb-0">
                        <i class="fas fa-edit me-2"></i>Chỉnh sửa Assignment
                    </h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb mb-0">
                            <li class="breadcrumb-item"><a href="#" onclick="history.back()">Quay lại</a></li>
                            <li class="breadcrumb-item active" aria-current="page">Chỉnh sửa Assignment</li>
                        </ol>
                    </nav>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form id="assignmentForm" action="edit-assignment" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">
                        <input type="hidden" name="courseID" value="${courseID}">
                        <input type="hidden" name="questionCount" value="${fn:length(questions)}">

                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-info-circle me-2"></i>Thông tin Assignment
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" id="title" name="title"
                                           value="${assignment.assignmentTitle}" required>
                                    <label for="title">Tiêu đề</label>
                                </div>
                                <div class="form-floating">
                                    <textarea class="form-control" id="description" name="description"
                                              style="height: 100px">${assignment.description}</textarea>
                                    <label for="description">Mô tả</label>
                                </div>
                            </div>
                        </div>

                        <div class="card mb-4">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">
                                    <i class="fas fa-list me-2"></i>Danh sách câu hỏi
                                </h5>
                                <button type="button" class="btn btn-primary" id="addQuestionBtn">
                                    <i class="fas fa-plus"></i>Thêm câu hỏi
                                </button>
                            </div>
                            <div class="card-body">
                                <div id="questionsContainer">
                                    <c:forEach items="${questions}" var="question" varStatus="loop">
                                        <div class="question-container mb-4" data-question-id="${question.questionID}">
                                            <input type="hidden" name="questionID_${loop.index}" value="${question.questionID}">
                                            
                                            <div class="form-floating mb-3">
                                                <input type="text" class="form-control" name="questionText_${loop.index}" 
                                                       value="${question.questionText}" required>
                                                <label>Nội dung câu hỏi</label>
                                            </div>
                                            
                                            <div class="row mb-3">
                                                <div class="col-md-6">
                                                    <div class="form-floating">
                                                        <select class="form-select" name="questionType_${loop.index}" required>
                                                            <option value="MULTIPLE_CHOICE" ${question.questionType == 'MULTIPLE_CHOICE' ? 'selected' : ''}>Trắc nghiệm</option>
                                                            <option value="TRUE_FALSE" ${question.questionType == 'TRUE_FALSE' ? 'selected' : ''}>Đúng/Sai</option>
                                                            <option value="SHORT_ANSWER" ${question.questionType == 'SHORT_ANSWER' ? 'selected' : ''}>Tự luận ngắn</option>
                                                            <option value="ESSAY" ${question.questionType == 'ESSAY' ? 'selected' : ''}>Tự luận dài</option>
                                                        </select>
                                                        <label>Loại câu hỏi</label>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-floating">
                                                        <input type="number" class="form-control" name="questionMark_${loop.index}" 
                                                               value="${question.questionMark}" required min="0" step="0.5">
                                                        <label>Điểm</label>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <div class="col-md-6">
                                                    <div class="file-input-container">
                                                        <label class="file-input-label">
                                                            <i class="fas fa-image"></i> Chọn ảnh
                                                        </label>
                                                        <input type="file" class="form-control" name="questionImage_${loop.index}" 
                                                               accept="image/*" style="display: none;">
                                                        <div class="preview-container">
                                                            <c:if test="${not empty question.questionImage}">
                                                                <img src="${question.questionImage}" class="media-preview">
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="file-input-container">
                                                        <label class="file-input-label">
                                                            <i class="fas fa-music"></i> Chọn file audio
                                                        </label>
                                                        <input type="file" class="form-control" name="audioFile_${loop.index}" 
                                                               accept="audio/*" style="display: none;">
                                                        <div class="preview-container">
                                                            <c:if test="${not empty question.audioFile}">
                                                                <audio controls class="mt-2 w-100">
                                                                    <source src="${question.audioFile}" type="audio/mpeg">
                                                                </audio>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="answers-container mb-3">
                                                <c:choose>
                                                    <c:when test="${question.questionType == 'MULTIPLE_CHOICE'}">
                                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                                            <h6 class="mb-0">Các lựa chọn</h6>
                                                            <div class="btn-group">
                                                                <button type="button" class="btn btn-sm btn-outline-primary add-option">
                                                                    <i class="fas fa-plus"></i> Thêm lựa chọn
                                                                </button>
                                                                <button type="button" class="btn btn-sm btn-outline-danger remove-option">
                                                                    <i class="fas fa-minus"></i> Xóa lựa chọn
                                                                </button>
                                                            </div>
                                                        </div>
                                                        <div class="answer-options">
                                                            <c:forEach items="${question.answers}" var="answer" varStatus="ansLoop">
                                                                <div class="answer-option">
                                                                    <div class="input-group">
                                                                        <div class="input-group-text">
                                                                            <input class="form-check-input mt-0" type="radio"
                                                                                   name="correctAnswer_${loop.index}" 
                                                                                   value="${ansLoop.index + 1}"
                                                                                   ${answer.correct ? 'checked' : ''}>
                                                                            <span class="ms-2">${answer.optionLabel}</span>
                                                                        </div>
                                                                        <input type="text" class="form-control"
                                                                               name="answerText_${loop.index}_${ansLoop.index + 1}" 
                                                                               value="${answer.answerText}" required>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                    </c:when>
                                                    <c:when test="${question.questionType == 'TRUE_FALSE'}">
                                                        <div class="answer-option">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="radio"
                                                                       name="correctAnswer_${loop.index}" value="1"
                                                                       ${question.correctAnswer == '1' ? 'checked' : ''}>
                                                                <label class="form-check-label">Đúng</label>
                                                            </div>
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="radio"
                                                                       name="correctAnswer_${loop.index}" value="2"
                                                                       ${question.correctAnswer == '2' ? 'checked' : ''}>
                                                                <label class="form-check-label">Sai</label>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="form-floating">
                                                            <textarea class="form-control" name="correctAnswer_${loop.index}"
                                                                      style="height: 100px" required>${question.correctAnswer}</textarea>
                                                            <label>Đáp án</label>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <div class="d-flex justify-content-end">
                                                <button type="button" class="btn btn-danger delete-question">
                                                    <i class="fas fa-trash"></i>Xóa câu hỏi
                                                </button>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between mt-4">
                            <button type="button" class="btn btn-secondary" onclick="history.back()">
                                <i class="fas fa-arrow-left"></i>Quay lại
                            </button>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-save"></i>Lưu thay đổi
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="toast-container"></div>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.all.min.js"></script>
<script>
    $(document).ready(function () {
        let questionCount = parseInt('${questions.size()}');
        const loadingOverlay = $('.loading-overlay');
        const toastContainer = $('.toast-container');

        // Thêm template cho câu hỏi mới
        function getNewQuestionTemplate(index) {
            return `
                <div class="question-container mb-4" data-question-id="new">
                    <input type="hidden" name="questionID_${index}" value="new">
                    
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="questionText_${index}" required>
                        <label>Nội dung câu hỏi</label>
                    </div>
                    
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="form-floating">
                                <select class="form-select" name="questionType_${index}" required>
                                    <option value="MULTIPLE_CHOICE">Trắc nghiệm</option>
                                    <option value="TRUE_FALSE">Đúng/Sai</option>
                                    <option value="SHORT_ANSWER">Tự luận ngắn</option>
                                    <option value="ESSAY">Tự luận dài</option>
                                </select>
                                <label>Loại câu hỏi</label>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-floating">
                                <input type="number" class="form-control" name="questionMark_${index}" 
                                       value="1" required min="0" step="0.5">
                                <label>Điểm</label>
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="file-input-container">
                                <label class="file-input-label">
                                    <i class="fas fa-image"></i> Chọn ảnh
                                </label>
                                <input type="file" class="form-control" name="questionImage_${index}" 
                                       accept="image/*" style="display: none;">
                                <div class="preview-container"></div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="file-input-container">
                                <label class="file-input-label">
                                    <i class="fas fa-music"></i> Chọn file audio
                                </label>
                                <input type="file" class="form-control" name="audioFile_${index}" 
                                       accept="audio/*" style="display: none;">
                                <div class="preview-container"></div>
                            </div>
                        </div>
                    </div>

                    <div class="answers-container mb-3">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <h6 class="mb-0">Các lựa chọn</h6>
                            <div class="btn-group">
                                <button type="button" class="btn btn-sm btn-outline-primary add-option">
                                    <i class="fas fa-plus"></i> Thêm lựa chọn
                                </button>
                                <button type="button" class="btn btn-sm btn-outline-danger remove-option">
                                    <i class="fas fa-minus"></i> Xóa lựa chọn
                                </button>
                            </div>
                        </div>
                        <div class="answer-options">
                            <div class="answer-option">
                                <div class="input-group">
                                    <div class="input-group-text">
                                        <input class="form-check-input mt-0" type="radio"
                                               name="correctAnswer_${index}" value="1" checked>
                                        <span class="ms-2">A</span>
                                    </div>
                                    <input type="text" class="form-control"
                                           name="answerText_${index}_1" required>
                                </div>
                            </div>
                            <div class="answer-option">
                                <div class="input-group">
                                    <div class="input-group-text">
                                        <input class="form-check-input mt-0" type="radio"
                                               name="correctAnswer_${index}" value="2">
                                        <span class="ms-2">B</span>
                                    </div>
                                    <input type="text" class="form-control"
                                           name="answerText_${index}_2" required>
                                </div>
                            </div>
                            <div class="answer-option">
                                <div class="input-group">
                                    <div class="input-group-text">
                                        <input class="form-check-input mt-0" type="radio"
                                               name="correctAnswer_${index}" value="3">
                                        <span class="ms-2">C</span>
                                    </div>
                                    <input type="text" class="form-control"
                                           name="answerText_${index}_3" required>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-end">
                        <button type="button" class="btn btn-danger delete-question">
                            <i class="fas fa-trash"></i>Xóa câu hỏi
                        </button>
                    </div>
                </div>
            `;
        }

        // Xử lý sự kiện click nút thêm câu hỏi
        $('#addQuestionBtn').on('click', function() {
            const newQuestionHtml = getNewQuestionTemplate(questionCount);
            $('#questionsContainer').append(newQuestionHtml);
            
            // Cập nhật số lượng câu hỏi
            questionCount++;
            $('input[name="questionCount"]').val(questionCount);
            
            // Log hành động
            logAction('add_new_question', {
                questionIndex: questionCount - 1
            });

            // Scroll đến câu hỏi mới
            $('html, body').animate({
                scrollTop: $(`[name="questionText_${questionCount - 1}"]`).offset().top - 100
            }, 500);
        });

        // Cập nhật số thứ tự câu hỏi
        function updateQuestionNumbers() {
            questionCount = $('.question-container').length;
            $('input[name="questionCount"]').val(questionCount);
        }

        // Hiển thị loading overlay
        function showLoading() {
            loadingOverlay.css('display', 'flex');
        }

        // Ẩn loading overlay
        function hideLoading() {
            loadingOverlay.css('display', 'none');
        }

        // Hiển thị toast thông báo
        function showToast(message, type = 'success') {
            const iconClass = type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle';
            const toast = $(`
                <div class="toast align-items-center text-white bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex">
                        <div class="toast-body">
                            <i class="fas ${iconClass} me-2"></i>
                            ${message}
                        </div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                    </div>
                </div>
            `);
            toastContainer.append(toast);
            const bsToast = new bootstrap.Toast(toast);
            bsToast.show();
            setTimeout(() => toast.remove(), 5000);
        }

        // Log hành động
        function logAction(action, details) {
            const timestamp = new Date().toISOString();
            console.log(`[${timestamp}] ${action}:`, details);
            // Có thể gửi log lên server nếu cần
            $.post('log-action', {
                action: action,
                timestamp: timestamp,
                details: JSON.stringify(details)
            });
        }

        // Xử lý preview file
        function handleFilePreview(input, previewContainer) {
            const file = input.files[0];
            if (!file) return;

            if (file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    previewContainer.html(`<img src="${e.target.result}" class="media-preview">`);
                    logAction('preview_image', { fileName: file.name, size: file.size });
                };
                reader.readAsDataURL(file);
            } else if (file.type.startsWith('audio/')) {
                const url = URL.createObjectURL(file);
                previewContainer.html(`
                    <audio controls class="mt-2 w-100">
                        <source src="${url}" type="${file.type}">
                    </audio>
                `);
                logAction('preview_audio', { fileName: file.name, size: file.size });
            }
        }

        // Xử lý thêm/xóa lựa chọn trắc nghiệm
        $(document).on('click', '.add-option', function() {
            const questionContainer = $(this).closest('.question-container');
            const answersContainer = questionContainer.find('.answer-options');
            const questionIdInput = questionContainer.find('input[name^="questionID_"]');
            const questionIndex = questionIdInput.attr('name').replace('questionID_', '');
            const currentOptions = answersContainer.find('.answer-option').length;
            
            if (currentOptions < 5) {
                const labels = ['A', 'B', 'C', 'D', 'E'];
                const newOption = `
                    <div class="answer-option">
                        <div class="input-group">
                            <div class="input-group-text">
                                <input class="form-check-input mt-0" type="radio"
                                       name="correctAnswer_${questionIndex}" value="${currentOptions + 1}">
                                <span class="ms-2">${labels[currentOptions]}</span>
                            </div>
                            <input type="text" class="form-control"
                                   name="answerText_${questionIndex}_${currentOptions + 1}" required>
                        </div>
                    </div>
                `;
                answersContainer.append(newOption);
                logAction('add_option', {
                    questionIndex: questionIndex,
                    optionCount: currentOptions + 1,
                    optionLabel: labels[currentOptions]
                });
            } else {
                showToast('Số lượng lựa chọn tối đa là 5', 'warning');
            }
        });

        $(document).on('click', '.remove-option', function() {
            const questionContainer = $(this).closest('.question-container');
            const answersContainer = questionContainer.find('.answer-options');
            const currentOptions = answersContainer.find('.answer-option').length;
            
            if (currentOptions > 3) {
                answersContainer.find('.answer-option').last().remove();
                logAction('remove_option', {
                    questionIndex: questionContainer.find('input[name^="questionID"]').attr('name').split('_')[1],
                    optionCount: currentOptions - 1
                });
            } else {
                showToast('Số lượng lựa chọn tối thiểu là 3', 'warning');
            }
        });

        // Cập nhật template cho câu hỏi trắc nghiệm
        function getMultipleChoiceTemplate(questionIndex, options = 3) {
            const labels = ['A', 'B', 'C', 'D', 'E'];
            let optionsHtml = '';
            
            for (let i = 0; i < options; i++) {
                const isFirst = i === 0;
                optionsHtml += `
                    <div class="answer-option">
                        <div class="input-group">
                            <div class="input-group-text">
                                <input class="form-check-input mt-0" type="radio"
                                       name="correctAnswer_${questionIndex}" value="${i + 1}" ${isFirst ? 'checked' : ''}>
                                <span class="ms-2">${labels[i]}</span>
                            </div>
                            <input type="text" class="form-control"
                                   name="answerText_${questionIndex}_${i + 1}" required>
                        </div>
                    </div>
                `;
            }
            
            return `
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h6 class="mb-0">Các lựa chọn</h6>
                    <div class="btn-group">
                        <button type="button" class="btn btn-sm btn-outline-primary add-option">
                            <i class="fas fa-plus"></i> Thêm lựa chọn
                        </button>
                        <button type="button" class="btn btn-sm btn-outline-danger remove-option">
                            <i class="fas fa-minus"></i> Xóa lựa chọn
                        </button>
                    </div>
                </div>
                <div class="answer-options">
                    ${optionsHtml}
                </div>
            `;
        }

        // Cập nhật xử lý thay đổi loại câu hỏi
        $(document).on('change', 'select[name^="questionType"]', function() {
            const questionContainer = $(this).closest('.question-container');
            const answersContainer = questionContainer.find('.answers-container');
            const questionType = $(this).val();
            const questionIndex = questionContainer.find('input[name^="questionID"]').attr('name').split('_')[1];
            const oldAnswers = [];
            
            // Lưu lại các đáp án hiện tại
            answersContainer.find('input[type="text"], textarea').each(function() {
                oldAnswers.push($(this).val());
            });
            
            let answersHtml = '';
            switch(questionType) {
                case 'MULTIPLE_CHOICE':
                    answersHtml = getMultipleChoiceTemplate(questionIndex, Math.max(3, oldAnswers.length));
                    break;
                case 'TRUE_FALSE':
                    answersHtml = `
                        <div class="answer-option">
                            <div class="form-check">
                                <input class="form-check-input" type="radio"
                                       name="correctAnswer_${questionIndex}" value="1" checked>
                                <label class="form-check-label">Đúng</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio"
                                       name="correctAnswer_${questionIndex}" value="2">
                                <label class="form-check-label">Sai</label>
                            </div>
                        </div>
                    `;
                    break;
                default:
                    answersHtml = `
                        <div class="form-floating">
                            <textarea class="form-control" name="correctAnswer_${questionIndex}"
                                      style="height: 100px" required>${oldAnswers[0] || ''}</textarea>
                            <label>Đáp án</label>
                        </div>
                    `;
            }
            answersContainer.html(answersHtml);
            
            // Khôi phục lại các đáp án nếu chuyển sang dạng trắc nghiệm
            if (questionType === 'MULTIPLE_CHOICE') {
                answersContainer.find('input[type="text"]').each(function(index) {
                    if (oldAnswers[index]) {
                        $(this).val(oldAnswers[index]);
                    }
                });
            }
            
            logAction('change_question_type', { 
                questionIndex: questionIndex,
                questionType: questionType,
                answersCount: oldAnswers.length
            });
        });

        // Xử lý upload file
        $(document).on('change', 'input[type="file"]', function() {
            const previewContainer = $(this).closest('.file-input-container').find('.preview-container');
            handleFilePreview(this, previewContainer);
        });

        // Xử lý xóa câu hỏi
        $(document).on('click', '.delete-question', function() {
            const questionContainer = $(this).closest('.question-container');
            const questionId = questionContainer.data('question-id');

            Swal.fire({
                title: 'Xác nhận xóa',
                text: 'Bạn có chắc chắn muốn xóa câu hỏi này?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#dc3545',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    if (questionId !== 'new') {
                        showLoading();
                        $.post('edit-assignment', {
                            action: 'delete',
                            questionID: questionId,
                            assignmentID: parseInt('${assignment.assignmentID}')
                        }, function(response) {
                            hideLoading();
                            if (response.success) {
                                questionContainer.remove();
                                updateQuestionNumbers();
                                showToast('Đã xóa câu hỏi thành công');
                                logAction('delete_question', { 
                                    questionId: questionId,
                                    assignmentId: '${assignment.assignmentID}'
                                });
                            } else {
                                showToast(response.message, 'danger');
                            }
                        });
                    } else {
                        questionContainer.remove();
                        updateQuestionNumbers();
                        showToast('Đã xóa câu hỏi thành công');
                        logAction('delete_new_question', { 
                            questionIndex: questionContainer.index()
                        });
                    }
                }
            });
        });

        // Xử lý submit form
        $('#assignmentForm').on('submit', function(e) {
            e.preventDefault();
            showLoading();

            const formData = new FormData(this);
            
            // Thêm optionLabel cho các câu hỏi trắc nghiệm
            $('.question-container').each(function(index) {
                const questionType = $(this).find(`select[name="questionType_${index}"]`).val();
                if (questionType === 'MULTIPLE_CHOICE') {
                    const labels = ['A', 'B', 'C', 'D', 'E'];
                    const options = $(this).find('.answer-option').length;
                    for (let i = 0; i < options; i++) {
                        formData.append(`optionLabel_${index}_${i + 1}`, labels[i]);
                    }
                }
            });

            // Validate dữ liệu trước khi submit
            let isValid = true;
            let errorMessage = '';

            $('.question-container').each(function(index) {
                const questionText = $(this).find(`input[name="questionText_${index}"]`).val();
                const questionType = $(this).find(`select[name="questionType_${index}"]`).val();
                const questionMark = $(this).find(`input[name="questionMark_${index}"]`).val();

                if (!questionText) {
                    isValid = false;
                    errorMessage = 'Vui lòng nhập nội dung cho tất cả các câu hỏi';
                    return false;
                }

                if (questionType === 'MULTIPLE_CHOICE') {
                    const hasAnswer = $(this).find(`input[name="correctAnswer_${index}"]:checked`).length > 0;
                    const emptyOptions = $(this).find(`input[name^="answerText_${index}_"]`).filter(function() {
                        return !this.value;
                    }).length > 0;

                    if (!hasAnswer || emptyOptions) {
                        isValid = false;
                        errorMessage = 'Vui lòng điền đầy đủ các lựa chọn và chọn đáp án đúng cho câu hỏi trắc nghiệm';
                        return false;
                    }
                }

                if (!questionMark || questionMark <= 0) {
                    isValid = false;
                    errorMessage = 'Điểm số phải lớn hơn 0';
                    return false;
                }
            });

            if (!isValid) {
                hideLoading();
                showToast(errorMessage, 'danger');
                return;
            }

            logAction('submit_form', {
                assignmentId: '${assignment.assignmentID}',
                questionCount: questionCount
            });

            $.ajax({
                url: $(this).attr('action'),
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    hideLoading();
                    if (response.success) {
                        showToast('Cập nhật assignment thành công');
                        setTimeout(() => window.location.reload(), 1500);
                    } else {
                        showToast(response.message, 'danger');
                    }
                },
                error: function(xhr, status, error) {
                    hideLoading();
                    showToast('Có lỗi xảy ra, vui lòng thử lại', 'danger');
                }
            });
        });
    });
</script>
</body>
</html>