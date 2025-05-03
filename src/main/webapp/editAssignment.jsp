<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa bài tập</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/css/editAssignment.css">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .card {
            margin-bottom: 15px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            border: none;
        }

        .card-header {
            border-radius: 8px 8px 0 0 !important;
            padding: 10px 15px;
            font-weight: 500;
        }

        .card-body {
            padding: 15px;
        }

        .preview-container {
            margin-top: 10px;
            max-width: 100%;
        }

        .preview-container img {
            max-height: 150px;
            width: auto;
            border-radius: 4px;
        }

        .answer-option {
            margin-bottom: 10px;
        }

        .loading {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.8);
            z-index: 1000;
        }

        .loading-content {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
        }

        .question-card {
            transition: all 0.3s ease;
        }

        .question-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .form-control, .form-select {
            border-radius: 6px;
        }

        .btn {
            border-radius: 6px;
            padding: 6px 12px;
        }

        .badge {
            border-radius: 4px;
            padding: 5px 8px;
        }

        .list-group-item {
            border-radius: 4px;
            margin-bottom: 5px;
        }

        .add-question-btn {
            position: fixed;
            bottom: 30px;
            right: 30px;
            width: 60px;
            height: 60px;
            border-radius: 50%;
            background-color: #28a745;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            transition: all 0.3s ease;
            z-index: 1000;
        }

        .add-question-btn:hover {
            transform: scale(1.1);
            background-color: #218838;
            color: white;
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1050;
        }

        .modal.show {
            display: block;
        }

        .modal-dialog {
            position: relative;
            width: auto;
            margin: 1.75rem auto;
            max-width: 800px;
        }

        .modal-content {
            position: relative;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16);
        }

        .modal-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 1rem;
            border-bottom: 1px solid #dee2e6;
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
        }

        .modal-body {
            position: relative;
            padding: 1rem;
        }

        .btn-close {
            background: transparent;
            border: 0;
            padding: 0.5rem;
            cursor: pointer;
            color: #fff;
            font-size: 1.5rem;
            line-height: 1;
        }

        .option-label {
            min-width: 40px;
            background-color: #e9ecef;
            border: 1px solid #ced4da;
            border-radius: 4px 0 0 4px;
            padding: 6px 12px;
            font-weight: bold;
            text-align: center;
        }

        .correct-label {
            margin-left: 5px;
            margin-bottom: 0;
        }


        .import-question-btn {
            position: fixed;
            bottom: 93px;
            right: 30px;
            height: 60px;
            border-radius: 50%;
            background: linear-gradient(45deg, #6cb4ff, #ff8fa3);
            color: #fff;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.15rem;
            font-weight: 600;
            box-shadow: 0 6px 18px rgba(108,180,255,0.18);
            z-index: 1000;
            transition: background 0.2s, box-shadow 0.2s, transform 0.2s;
            width: 60px;
        }

        .import-question-btn:hover {
            background: linear-gradient(45deg, #ff8fa3, #6cb4ff);
            box-shadow: 0 10px 28px rgba(255,143,163,0.18);
            transform: translateY(-2px) scale(1.04);
        }

        .import-question-btn:hover {
            background: linear-gradient(45deg, #ff8fa3, #6cb4ff);
            box-shadow: 0 10px 28px rgba(255,143,163,0.18);
            transform: translateY(-2px) scale(1.04);
        }

        .import-question-btn:hover {
            background: linear-gradient(45deg, #ff8fa3, #6cb4ff);
            box-shadow: 0 10px 28px rgba(255,143,163,0.18);
            transform: translateY(-2px) scale(1.04);
        }

        @media (max-width: 600px) {
            .import-question-btn {
                min-width: 56px;
                font-size: 1.1rem;
                padding: 0 10px;
            }
            .import-question-btn .import-text {
                display: none;
            }
        }

        #uploadProgress {
            max-width: 500px;
            margin: 0 auto;
        }

        .progress {
            height: 25px;
            border-radius: 15px;
            background-color: #f0f0f0;
        }

        .progress-bar {
            transition: width 0.3s ease-in-out;
        }

        #uploadStatus {
            font-weight: bold;
            margin-top: 10px;
        }

        .btn-delete-option {
            border-radius: 0 6px 6px 0 !important;
        }

        .input-group > .btn-delete-option {
            margin-left: -1px;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>
<!-- Loading Overlay -->
<div class="loading">
    <div class="loading-content">
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
        <p class="mt-2">Đang xử lý...</p>
    </div>
</div>

<div class="container-fluid py-3">
    <h4 class="mb-3">
        <i class="fas fa-edit me-2"></i>Chỉnh sửa bài tập
    </h4>

    <div class="row">
        <!-- Cột trái: Thông tin bài tập -->
        <div class="col-md-4">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <i class="fas fa-info-circle me-2"></i>Thông tin bài tập
                </div>
                <div class="card-body">
                    <form id="assignmentForm" action="edit-assignment" method="post">
                        <input type="hidden" name="action" value="updateAssignment">
                        <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">
                        <input type="hidden" name="courseID" value="${courseID}">

                        <div class="mb-3">
                            <label class="form-label">Tiêu đề bài tập</label>
                            <input type="text" name="title" class="form-control" value="${assignment.assignmentTitle}"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea name="description" class="form-control" rows="4"
                                      required>${assignment.description}</textarea>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fas fa-save me-1"></i> Lưu thay đổi
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Cột phải: Danh sách câu hỏi -->
        <div class="col-md-8">
            <div class="card">
                <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
                    <span><i class="fas fa-list me-2"></i>Danh sách câu hỏi</span>
                    <span class="badge bg-light text-dark">${questions.size()} câu hỏi</span>
                </div>
                <div class="card-body">
                    <div class="row">
                        <c:forEach items="${questions}" var="question" varStatus="status">
                            <div class="col-md-6 mb-3 question-item" style="display: ${status.index < 12 ? 'block' : 'none'}">
                                <div class="card question-card h-100">
                                    <div class="card-header d-flex justify-content-between align-items-center py-2">
                                        <span class="fw-bold">Câu ${status.index + 1}</span>
                                        <div>
                                            <button class="btn btn-primary btn-sm edit-question me-1"
                                                    data-question-id="${question.questionID}"
                                                    data-question-text="${question.questionText}"
                                                    data-question-type="${question.questionType}"
                                                    data-question-mark="${question.questionMark}"
                                                    data-assignment-id="${assignment.assignmentID}">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-danger btn-sm delete-question"
                                                    data-question-id="${question.questionID}"
                                                    data-assignment-id="${assignment.assignmentID}">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="card-body p-2">
                                        <div class="mb-1">
                                            <span class="badge bg-primary">
                                                ${question.questionType == 'multiple_choice' ? 'Trắc nghiệm' : 'Câu trả lời ngắn'}
                                            </span>
                                            <span class="badge bg-secondary ms-1">${question.questionMark} điểm</span>
                                        </div>
                                        <p class="mb-1 small">${question.questionText}</p>

                                        <c:if test="${not empty question.questionImage}">
                                            <div class="mb-1">
                                                <img src="${question.questionImage}" class="img-fluid rounded"
                                                     style="max-height: 100px">
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty question.audioFile}">
                                            <div class="mb-1">
                                                <audio controls class="w-100" style="height: 30px">
                                                    <source src="${question.audioFile}" type="audio/mpeg">
                                                </audio>
                                            </div>
                                        </c:if>

                                        <c:if test="${question.questionType == 'multiple_choice'}">
                                            <div class="mt-1">
                                                <c:forEach items="${question.answers}" var="answer"
                                                           varStatus="ansStatus">
                                                    <div class="d-flex align-items-center mb-1">
                                                        <span class="me-2 small">${answer.optionLabel}.</span>
                                                        <span class="small flex-grow-1">${answer.answerText}</span>
                                                        <c:if test="${answer.correct}">
                                                            <span class="badge bg-success">Đúng</span>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Phân trang -->
                    <div class="d-flex justify-content-center mt-4">
                        <nav aria-label="Page navigation">
                            <ul class="pagination">
                                <li class="page-item">
                                    <a class="page-link" href="#" id="prevPage" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                                <li class="page-item">
                                    <span class="page-link" id="currentPage">1</span>
                                </li>
                                <li class="page-item">
                                    <a class="page-link" href="#" id="nextPage" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Nút thêm câu hỏi -->
<button type="button" class="add-question-btn" id="addQuestionBtn">
    <i class="fas fa-plus"></i>
</button>

<!-- Modal thêm câu hỏi -->
<div id="addQuestionModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title">
                    <i class="fas fa-plus-circle me-2"></i>Thêm câu hỏi mới
                </h5>
                <button type="button" class="btn-close" id="closeModalBtn">&times;</button>
            </div>
            <div class="modal-body">
                <!-- Debug info -->
                <div class="alert alert-info">
                    Assignment ID: ${assignment.assignmentID}<br>
                    Course ID: ${courseID}
                </div>

                <form id="questionForm" method="post" action="edit-assignment" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="addQuestion">
                    <input type="hidden" name="courseID" value="${courseID}">
                    <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">

                    <div class="mb-3">
                        <label class="form-label">Loại câu hỏi</label>
                        <select name="questionType" class="form-select" id="questionType" required>
                            <option value="multiple_choice">Trắc nghiệm</option>
                            <option value="short_answer">Câu trả lời ngắn</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nội dung câu hỏi</label>
                        <textarea name="questionText" class="form-control" rows="3" required></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Điểm</label>
                        <input type="number" name="questionMark" class="form-control" min="0" step="0.01" required>
                        <small class="text-muted">Nhập số điểm (ví dụ: 0.25, 0.5, 1.0)</small>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Hình ảnh</label>
                            <input type="file" name="questionImage" class="form-control" accept="image/*">
                            <div class="preview-container mt-2" id="imagePreview"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Âm thanh</label>
                            <input type="file" name="audioFile" class="form-control" accept="audio/*">
                            <div class="preview-container mt-2" id="audioPreview"></div>
                        </div>
                    </div>

                    <div id="multipleChoiceAnswers" class="mb-3">
                        <label class="form-label">Các lựa chọn</label>
                        <div id="answerOptions">
                        </div>
                        <button type="button" class="btn btn-outline-primary btn-sm mt-2" id="addAnswerOption">
                            <i class="fas fa-plus me-1"></i> Thêm lựa chọn
                        </button>
                    </div>

                    <div class="text-end">
                        <button type="button" class="btn btn-secondary" id="cancelBtn">Hủy</button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-save me-1"></i> Lưu câu hỏi
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Modal chỉnh sửa câu hỏi -->
<div id="editQuestionModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">
                    <i class="fas fa-edit me-2"></i>Chỉnh sửa câu hỏi
                </h5>
                <button type="button" class="btn-close" id="closeEditModalBtn">&times;</button>
            </div>
            <div class="modal-body">
                <form id="editQuestionForm" method="post" action="edit-assignment" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="updateQuestion">
                    <input type="hidden" name="questionID" id="editQuestionID">
                    <input type="hidden" name="assignmentID" id="editAssignmentID">

                    <div class="mb-3">
                        <label class="form-label">Loại câu hỏi</label>
                        <select name="questionType" class="form-select" id="editQuestionType" required>
                            <option value="multiple_choice">Trắc nghiệm</option>
                            <option value="short_answer">Câu trả lời ngắn</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nội dung câu hỏi</label>
                        <textarea name="questionText" class="form-control" rows="3" id="editQuestionText" required></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Điểm</label>
                        <input type="number" name="questionMark" class="form-control" min="0" step="0.01" id="editQuestionMark" required>
                        <small class="text-muted">Nhập số điểm (ví dụ: 0.25, 0.5, 1.0)</small>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Hình ảnh</label>
                            <input type="file" name="questionImage" class="form-control" accept="image/*">
                            <div class="preview-container mt-2" id="editImagePreview"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Âm thanh</label>
                            <input type="file" name="audioFile" class="form-control" accept="audio/*">
                            <div class="preview-container mt-2" id="editAudioPreview"></div>
                        </div>
                    </div>

                    <div id="editMultipleChoiceAnswers" class="mb-3">
                        <label class="form-label">Các lựa chọn</label>
                        <div id="editAnswerOptions">
                        </div>
                        <button type="button" class="btn btn-outline-primary btn-sm mt-2" id="editAddAnswerOption">
                            <i class="fas fa-plus me-1"></i> Thêm lựa chọn
                        </button>
                    </div>

                    <div class="text-end">
                        <button type="button" class="btn btn-secondary" id="editCancelBtn">Hủy</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-1"></i> Lưu thay đổi
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Nút import câu hỏi -->
<button type="button" class="import-question-btn" id="importQuestionBtn">
    <i class="fas fa-file-import me-2"></i>
</button>

<!-- Modal import câu hỏi -->
<div id="importQuestionModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">
                    <i class="fas fa-file-import me-2"></i>Import câu hỏi từ file
                </h5>
                <button type="button" class="btn-close" id="closeImportModalBtn">&times;</button>
            </div>
            <div class="modal-body">
                <form id="importForm" method="post" action="edit-assignment" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="importQuestions">
                    <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">
                    
                    <div class="mb-3">
                        <label class="form-label">Tải template mẫu:</label>
                        <div class="mb-2">
                            <a href="templates/question_template.xlsx" class="btn btn-sm btn-outline-primary">
                                <i class="fas fa-file-excel me-1"></i> Excel Template
                            </a>
                            <a href="templates/questionTest.csv" class="btn btn-sm btn-outline-success ms-2">
                                <i class="fas fa-file-csv me-1"></i> CSV Template
                            </a>
                        </div>
                        <small class="text-muted d-block">
                            Template bao gồm các cột: questionType, questionText, questionMark, answer1-4, isCorrect1-4
                        </small>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Chọn file Excel/CSV</label>
                        <input type="file" name="questionFile" class="form-control" accept=".xlsx,.xls,.csv" required>
                        <small class="text-muted d-block">Hỗ trợ file Excel (.xlsx, .xls) và CSV (.csv)</small>
                        <small class="text-muted d-block">Kích thước file tối đa: 10MB</small>
                    </div>
                         <%--Tạo upload status --%>
                    <div class="mb-3">
                        <div class="progress" style="display: none;">
                            <div class="progress-bar" role="progressbar" style="width: 0%"></div>
                        </div>
                        <div id="uploadStatus"></div>
                    </div>

                    <div class="text-end">
                        <button type="button" class="btn btn-secondary" id="cancelImportBtn">Hủy</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-upload me-1"></i> Import
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        // Xử lý sự kiện click cho nút thêm câu hỏi
        $('#addQuestionBtn').on('click', function () {
            $('#addQuestionModal').css('display', 'block');
            document.body.style.overflow = 'hidden';
        });

        // Xử lý sự kiện click cho nút đóng modal
        $('#closeModalBtn, #cancelBtn').on('click', function () {
            $('#addQuestionModal').css('display', 'none');
            document.body.style.overflow = '';
            // Reset form
            $('#questionForm')[0].reset();
            $('#imagePreview, #audioPreview').empty();
            $('#multipleChoiceAnswers').hide();
        });

        // Đóng modal khi click bên ngoài
        $(window).on('click', function (event) {
            if ($(event.target).is('#addQuestionModal')) {
                $('#addQuestionModal').css('display', 'none');
                document.body.style.overflow = '';
            }
        });

        // Xử lý hiển thị/ẩn form câu trả lời trắc nghiệm
        $('#questionType').on('change', function () {
            if ($(this).val() === 'multiple_choice') {
                $('#multipleChoiceAnswers').show();
            } else {
                $('#multipleChoiceAnswers').hide();
            }
        });

        // Thêm lựa chọn mới
        $('#addAnswerOption').on('click', function (e) {
            e.preventDefault();
            const optionLabels = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'];
            const answerCount = $('.answer-option').length;

            if (answerCount >= optionLabels.length) {
                alert('Không thể thêm quá ' + optionLabels.length + ' lựa chọn');
                return;
            }

            const currentLabel = optionLabels[answerCount];
            const newOptionHtml =
                '<div class="answer-option">' +
                '<div class="input-group">' +
                '<div class="option-label">' + currentLabel + '</div>' +
                '<input type="text" name="answers" class="form-control" placeholder="Nhập lựa chọn" required>' +
                '<div class="input-group-text">' +
                '<input type="checkbox" name="isCorrect" class="answer-checkbox" data-option="' + currentLabel + '">' +
                '<label class="correct-label">Đúng</label>' +
                '</div>' +
                '<button type="button" class="btn btn-danger btn-delete-option">' +
                '<i class="fas fa-times"></i>' +
                '</button>' +
                '</div>' +
                '</div>';

            $('#answerOptions').append(newOptionHtml);
            updateOptionLabels();
        });

        // Xử lý xóa lựa chọn
        $(document).on('click', '.btn-delete-option', function() {
            $(this).closest('.answer-option').remove();
            updateOptionLabels();
        });

        // Cập nhật lại labels sau khi xóa
        function updateOptionLabels() {
            $('.answer-option').each(function(index) {
                const label = String.fromCharCode(65 + index); // 65 là mã ASCII của 'A'
                $(this).find('.option-label').text(label);
                $(this).find('.answer-checkbox').attr('data-option', label);
            });
        }

        // Hàm hiển thị modal chỉnh sửa
        function showEditModal() {
            document.getElementById('editQuestionModal').classList.add('show');
            document.body.style.overflow = 'hidden';
        }

        // Hàm ẩn modal chỉnh sửa
        function hideEditModal() {
            document.getElementById('editQuestionModal').classList.remove('show');
            document.body.style.overflow = '';
            // Reset form
            document.getElementById('editQuestionForm').reset();
            $('#editImagePreview, #editAudioPreview').empty();
            $('#editAnswerOptions').empty();
            $('#editMultipleChoiceAnswers').hide();
        }

        // Xử lý sự kiện click cho nút chỉnh sửa câu hỏi
        $('.edit-question').click(function() {
            const questionId = $(this).data('question-id');
            const assignmentId = $(this).data('assignment-id');
            
            // Hiển thị loading
            $('.loading').show();
            
            // Lấy thông tin câu hỏi từ server
            $.ajax({
                url: 'edit-assignment',
                method: 'POST',
                data: {
                    action: 'getQuestionDetails',
                    questionID: questionId,
                    assignmentID: assignmentId
                },
                success: function(response) {
                    $('.loading').hide();
                    try {
                        if (typeof response === 'string') {
                            response = JSON.parse(response);
                        }
                        
                        if (response.success) {
                            // Điền thông tin vào form
                            $('#editQuestionID').val(response.question.questionID);
                            $('#editAssignmentID').val(response.question.assignmentID);
                            $('#editQuestionText').val(response.question.questionText);
                            $('#editQuestionType').val(response.question.questionType);
                            $('#editQuestionMark').val(response.question.questionMark);
                            
                            // Hiển thị hình ảnh hiện tại nếu có
                            if (response.question.questionImage) {
                                $('#editImagePreview').html(`
                                    <div class="mb-2">
                                        <label class="form-label">Hình ảnh hiện tại:</label>
                                        <img src="${response.question.questionImage}" class="img-thumbnail" style="max-height: 150px">
                                        <input type="hidden" name="currentQuestionImage" value="${response.question.questionImage}">
                                    </div>
                                `);
                            }

                            // Hiển thị audio hiện tại nếu có
                            if (response.question.audioFile) {
                                $('#editAudioPreview').html(`
                                    <div class="mb-2">
                                        <label class="form-label">Audio hiện tại:</label>
                                        <audio controls class="w-100" style="height: 30px">
                                            <source src="${response.question.audioFile}" type="audio/mpeg">
                                        </audio>
                                        <input type="hidden" name="currentAudioFile" value="${response.question.audioFile}">
                                    </div>
                                `);
                            }
                            
                            // Xử lý hiển thị câu trả lời nếu là câu hỏi trắc nghiệm
                            if (response.question.questionType === 'multiple_choice') {
                                $('#editMultipleChoiceAnswers').show();
                                $('#editAnswerOptions').empty();
                                
                                response.question.answers.forEach(function(answer) {
                                    console.log('Đang xử lý câu trả lời:', answer); // Log để debug
                                    
                                    const optionHtml = 
                                        '<div class="answer-option">' +
                                            '<div class="input-group">' +
                                                '<div class="option-label">' + answer.optionLabel + '</div>' +
                                                '<input type="text" name="answers" class="form-control" value="' + answer.answerText + '" required>' +
                                                '<div class="input-group-text">' +
                                                    '<input type="checkbox" name="isCorrect" class="answer-checkbox"' + 
                                                    (answer.isCorrect ? ' checked="checked"' : '') +
                                                    ' data-option="' + answer.optionLabel + '">' +
                                                    '<label class="correct-label">Đúng</label>' +
                                                '</div>' +
                                                '<button type="button" class="btn btn-danger btn-delete-option">' +
                                                    '<i class="fas fa-times"></i>' +
                                                '</button>' +
                                            '</div>' +
                                        '</div>';
                                    $('#editAnswerOptions').append(optionHtml);

                                    // Debug log để kiểm tra trạng thái đúng/sai
                                    console.log('Câu trả lời ' + answer.optionLabel + ':', {
                                        text: answer.answerText,
                                        isCorrect: answer.isCorrect,
                                        checked: answer.isCorrect === true
                                    });
                                });

                                // Kiểm tra lại các checkbox sau khi thêm
                                $('#editAnswerOptions .answer-checkbox').each(function() {
                                    console.log('Checkbox ' + $(this).data('option') + ':', {
                                        checked: $(this).prop('checked'),
                                        hasCheckedAttr: $(this).attr('checked') !== undefined
                                    });
                                });
                            } else {
                                $('#editMultipleChoiceAnswers').hide();
                            }
                            
                            // Hiển thị modal
                            showEditModal();
                        } else {
                            alert('Có lỗi khi lấy thông tin câu hỏi: ' + response.message);
                        }
                    } catch (e) {
                        console.error('Lỗi khi xử lý phản hồi:', e);
                        alert('Có lỗi xảy ra khi xử lý phản hồi từ server');
                    }
                },
                error: function(xhr, status, error) {
                    $('.loading').hide();
                    console.error('Lỗi AJAX:', {
                        status: status,
                        error: error,
                        response: xhr.responseText
                    });
                    alert('Có lỗi xảy ra khi lấy thông tin câu hỏi');
                }
            });
        });

        // Xử lý sự kiện click cho nút đóng modal chỉnh sửa
        $('#closeEditModalBtn, #editCancelBtn').click(function() {
            hideEditModal();
        });

        // Xử lý xóa câu hỏi
        $('.delete-question').click(function () {
            if (confirm('Bạn có chắc chắn muốn xóa câu hỏi này?')) {
                const questionId = $(this).data('question-id');
                const assignmentId = $(this).data('assignment-id');

                console.log('Bắt đầu xóa câu hỏi:', {
                    questionId: questionId,
                    assignmentId: assignmentId
                });

                $('.loading').show();

                $.ajax({
                    url: 'edit-assignment',
                    method: 'POST',
                    data: {
                        action: 'deleteQuestion',
                        questionID: questionId,
                        assignmentID: assignmentId
                    },
                    success: function (response) {
                        $('.loading').hide();
                        console.log('Phản hồi từ server:', response);

                        try {
                            if (typeof response === 'string') {
                                response = JSON.parse(response);
                            }

                            if (response.success) {
                                console.log('Xóa câu hỏi thành công');
                                location.reload();
                            } else {
                                console.error('Lỗi khi xóa câu hỏi:', response.message);
                                alert('Có lỗi xảy ra khi xóa câu hỏi: ' + response.message);
                            }
                        } catch (e) {
                            console.error('Lỗi khi xử lý phản hồi:', e);
                            alert('Có lỗi xảy ra khi xử lý phản hồi từ server');
                        }
                    },
                    error: function (xhr, status, error) {
                        $('.loading').hide();
                        console.error('Lỗi AJAX:', {
                            status: status,
                            error: error,
                            response: xhr.responseText
                        });

                        let errorMessage = 'Có lỗi xảy ra khi xóa câu hỏi';
                        try {
                            const response = JSON.parse(xhr.responseText);
                            if (response.message) {
                                errorMessage = response.message;
                            }
                        } catch (e) {
                            console.error('Lỗi khi parse JSON response:', e);
                        }

                        alert(errorMessage);
                    }
                });
            }
        });

        // Phân trang
        const itemsPerPage = 12;
        let currentPage = 1;
        const totalItems = $('.question-item').length;
        const totalPages = Math.ceil(totalItems / itemsPerPage);

        function updatePagination() {
            // Cập nhật trạng thái nút Previous/Next
            $('#prevPage').parent().toggleClass('disabled', currentPage === 1);
            $('#nextPage').parent().toggleClass('disabled', currentPage === totalPages);

            // Cập nhật số trang hiện tại
            $('#currentPage').text(currentPage);

            // Hiển thị/ẩn các câu hỏi theo trang
            $('.question-item').each(function(index) {
                const start = (currentPage - 1) * itemsPerPage;
                const end = start + itemsPerPage;
                $(this).toggle(index >= start && index < end);
            });
        }

        // Xử lý sự kiện click nút Previous
        $('#prevPage').click(function(e) {
            e.preventDefault();
            if (currentPage > 1) {
                currentPage--;
                updatePagination();
            }
        });

        // Xử lý sự kiện click nút Next
        $('#nextPage').click(function(e) {
            e.preventDefault();
            if (currentPage < totalPages) {
                currentPage++;
                updatePagination();
            }
        });

        // Khởi tạo phân trang
        updatePagination();

        // Xử lý form submit
        $('#questionForm').on('submit', function (e) {
            e.preventDefault();

            // Kiểm tra các trường bắt buộc
            const questionText = $('textarea[name="questionText"]').val().trim();
            const questionType = $('#questionType').val();
            const questionMark = $('input[name="questionMark"]').val();

            if (!questionText) {
                alert('Vui lòng nhập nội dung câu hỏi');
                return false;
            }

            if (!questionType) {
                alert('Vui lòng chọn loại câu hỏi');
                return false;
            }

            if (!questionMark) {
                alert('Vui lòng nhập điểm cho câu hỏi');
                return false;
            }

            // Nếu là câu hỏi trắc nghiệm
            if (questionType === 'multiple_choice') {
                // Kiểm tra số lượng câu trả lời
                if ($('.answer-option').length === 0) {
                    alert('Vui lòng thêm ít nhất một câu trả lời');
                    return false;
                }

                // Kiểm tra đáp án đúng
                if ($('.answer-checkbox:checked').length === 0) {
                    alert('Vui lòng chọn ít nhất một đáp án đúng');
                    return false;
                }

                // Kiểm tra nội dung câu trả lời
                let hasEmptyAnswer = false;
                $('.answer-option input[type="text"]').each(function () {
                    if (!$(this).val().trim()) {
                        hasEmptyAnswer = true;
                        return false;
                    }
                });

                if (hasEmptyAnswer) {
                    alert('Vui lòng nhập đầy đủ nội dung cho tất cả các câu trả lời');
                    return false;
                }
            }

            // Tạo FormData object
            const formData = new FormData(this);

            // Xóa tất cả input hidden isCorrect và option_labels cũ
            formData.delete('isCorrect');
            formData.delete('option_labels');
            formData.delete('answers');

            // Thêm lại các giá trị mới cho câu hỏi trắc nghiệm
            if (questionType === 'multiple_choice') {
                $('.answer-option').each(function(index) {
                    const answerText = $(this).find('input[type="text"]').val().trim();
                    const optionLabel = $(this).find('.option-label').text();
                    const isChecked = $(this).find('.answer-checkbox').prop('checked');

                    formData.append('answers', answerText);
                    formData.append('option_labels', optionLabel);
                    formData.append('isCorrect', isChecked ? "1" : "0");
                });
            }

            // Log form data trước khi gửi
            console.log('Form data:', {
                action: formData.get('action'),
                assignmentID: formData.get('assignmentID'),
                courseID: formData.get('courseID'),
                questionType: formData.get('questionType'),
                questionText: formData.get('questionText'),
                questionMark: formData.get('questionMark'),
                answers: formData.getAll('answers'),
                option_labels: formData.getAll('option_labels'),
                isCorrect: formData.getAll('isCorrect')
            });

            // Hiển thị loading
            $('.loading').show();

            // Gửi request bằng AJAX
            $.ajax({
                url: 'edit-assignment',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    $('.loading').hide();
                    try {
                        if (typeof response === 'string') {
                            response = JSON.parse(response);
                        }
                        if (response.success) {
                            // Đóng modal
                            $('#addQuestionModal').css('display', 'none');
                            document.body.style.overflow = '';
                            // Reload trang
                            location.reload();
                        } else {
                            alert(response.message || 'Có lỗi xảy ra khi thêm câu hỏi');
                        }
                    } catch (e) {
                        console.error('Error parsing response:', e);
                        alert('Có lỗi xảy ra khi xử lý phản hồi từ server');
                    }
                },
                error: function(xhr, status, error) {
                    $('.loading').hide();
                    console.error('Error:', error);
                    console.error('Response:', xhr.responseText);

                    let errorMessage = 'Có lỗi xảy ra khi thêm câu hỏi';
                    try {
                        const response = JSON.parse(xhr.responseText);
                        if (response.message) {
                            errorMessage = response.message;
                        }
                    } catch (e) {
                        console.error('Error parsing error response:', e);
                    }

                    alert(errorMessage);
                }
            });
        });

        // Thêm lựa chọn mới trong modal chỉnh sửa
        $('#editAddAnswerOption').click(function(e) {
            e.preventDefault();
            const optionLabels = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'];
            const answerCount = $('#editAnswerOptions .answer-option').length;
            
            if (answerCount >= optionLabels.length) {
                alert('Không thể thêm quá ' + optionLabels.length + ' lựa chọn');
                return;
            }

            const currentLabel = optionLabels[answerCount];
            const newOptionHtml = 
                '<div class="answer-option">' +
                    '<div class="input-group">' +
                        '<div class="option-label">' + currentLabel + '</div>' +
                        '<input type="text" name="answers" class="form-control" placeholder="Nhập lựa chọn" required>' +
                        '<div class="input-group-text">' +
                            '<input type="checkbox" name="isCorrect" class="answer-checkbox">' +
                            '<label class="correct-label">Đúng</label>' +
                        '</div>' +
                        '<button type="button" class="btn btn-danger btn-delete-option">' +
                            '<i class="fas fa-times"></i>' +
                        '</button>' +
                    '</div>' +
                '</div>';

            $('#editAnswerOptions').append(newOptionHtml);
            updateEditOptionLabels();
        });

        // Xử lý xóa lựa chọn trong modal chỉnh sửa
        $(document).on('click', '#editAnswerOptions .btn-delete-option', function() {
            const totalOptions = $('#editAnswerOptions .answer-option').length;
            if (totalOptions <= 2) {
                alert('Câu hỏi trắc nghiệm phải có ít nhất 2 đáp án');
                return;
            }
            
            // Kiểm tra xem còn bao nhiêu đáp án đúng
            const correctAnswers = $('#editAnswerOptions .answer-checkbox:checked').length;
            const isCurrentCorrect = $(this).closest('.answer-option').find('.answer-checkbox').prop('checked');
            
            if (correctAnswers <= 1 && isCurrentCorrect) {
                alert('Phải có ít nhất một đáp án đúng');
                return;
            }
            
            $(this).closest('.answer-option').remove();
            updateEditOptionLabels();
        });

        // Cập nhật lại labels sau khi xóa
        function updateEditOptionLabels() {
            $('#editAnswerOptions .answer-option').each(function(index) {
                const label = String.fromCharCode(65 + index); // 65 là mã ASCII của 'A'
                $(this).find('.option-label').text(label);
                $(this).find('.answer-checkbox').attr('data-option', label);
            });
        }

        // Xử lý submit form chỉnh sửa
        $('#editQuestionForm').on('submit', function(e) {
            e.preventDefault();
            
            // Kiểm tra các trường bắt buộc
            const questionText = $('#editQuestionText').val().trim();
            const questionType = $('#editQuestionType').val();
            const questionMark = $('#editQuestionMark').val();

            if (!questionText || !questionType || !questionMark) {
                alert('Vui lòng điền đầy đủ thông tin bắt buộc');
                return false;
            }

            // Kiểm tra câu trả lời nếu là câu hỏi trắc nghiệm
            if (questionType === 'multiple_choice') {
                const answerCount = $('#editAnswerOptions .answer-option').length;
                
                // Kiểm tra số lượng đáp án tối thiểu
                if (answerCount < 2) {
                    alert('Câu hỏi trắc nghiệm phải có ít nhất 2 đáp án');
                    return false;
                }

                // Kiểm tra số lượng đáp án tối đa
                if (answerCount > 10) {
                    alert('Câu hỏi trắc nghiệm không được có quá 10 đáp án');
                    return false;
                }

                // Kiểm tra đáp án đúng
                if ($('#editAnswerOptions .answer-checkbox:checked').length === 0) {
                    alert('Vui lòng chọn ít nhất một đáp án đúng');
                    return false;
                }

                // Kiểm tra nội dung đáp án
                let hasEmptyAnswer = false;
                $('#editAnswerOptions input[type="text"]').each(function() {
                    if (!$(this).val().trim()) {
                        hasEmptyAnswer = true;
                        return false;
                    }
                });

                if (hasEmptyAnswer) {
                    alert('Vui lòng nhập đầy đủ nội dung cho tất cả các câu trả lời');
                    return false;
                }
            }

            // Tạo FormData object
            const formData = new FormData(this);

            // Thêm hình ảnh và audio hiện tại vào formData nếu không có file mới
            const imageFile = formData.get('questionImage');
            const audioFile = formData.get('audioFile');
            const currentImage = $('input[name="currentQuestionImage"]').val();
            const currentAudio = $('input[name="currentAudioFile"]').val();

            if (!imageFile || imageFile.size === 0) {
                formData.set('questionImage', currentImage || '');
            }
            if (!audioFile || audioFile.size === 0) {
                formData.set('audioFile', currentAudio || '');
            }

            // Xóa và thêm lại các giá trị cho câu trả lời trắc nghiệm
            if (questionType === 'multiple_choice') {
                formData.delete('isCorrect');
                formData.delete('option_labels');
                formData.delete('answers');

                $('#editAnswerOptions .answer-option').each(function() {
                    const answerText = $(this).find('input[type="text"]').val().trim();
                    const optionLabel = $(this).find('.option-label').text();
                    const isChecked = $(this).find('.answer-checkbox').prop('checked');
                    
                    formData.append('answers', answerText);
                    formData.append('option_labels', optionLabel);
                    formData.append('isCorrect', isChecked ? "1" : "0");
                });
            }

            // Hiển thị loading
            $('.loading').show();

            // Gửi request chỉnh sửa
            $.ajax({
                url: 'edit-assignment',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    $('.loading').hide();
                    try {
                        if (typeof response === 'string') {
                            response = JSON.parse(response);
                        }
                        
                        if (response.success) {
                            hideEditModal();
                            location.reload();
                        } else {
                            alert('Có lỗi xảy ra khi cập nhật câu hỏi: ' + response.message);
                        }
                    } catch (e) {
                        console.error('Lỗi khi xử lý phản hồi:', e);
                        alert('Có lỗi xảy ra khi xử lý phản hồi từ server');
                    }
                },
                error: function(xhr, status, error) {
                    $('.loading').hide();
                    console.error('Lỗi AJAX:', {
                        status: status,
                        error: error,
                        response: xhr.responseText
                    });
                    
                    let errorMessage = 'Có lỗi xảy ra khi cập nhật câu hỏi';
                    try {
                        const response = JSON.parse(xhr.responseText);
                        if (response.message) {
                            errorMessage = response.message;
                        }
                    } catch (e) {
                        console.error('Lỗi khi parse JSON response:', e);
                    }
                    
                    alert(errorMessage);
                }
            });
        });

        // Xử lý hiển thị modal import
        $('#importQuestionBtn').on('click', function() {
            $('#importQuestionModal').css('display', 'block');
            document.body.style.overflow = 'hidden';
        });

        // Xử lý đóng modal import
        $('#closeImportModalBtn, #cancelImportBtn').on('click', function() {
            $('#importQuestionModal').css('display', 'none');
            document.body.style.overflow = '';
            $('#importForm')[0].reset();
            $('.progress').hide();
            $('#uploadStatus').empty();
        });

        // Xử lý submit form import
        $('#importForm').on('submit', function(e) {
            e.preventDefault();
            var formData = new FormData(this);
            
            // Kiểm tra kích thước file
            var fileInput = $('input[name="questionFile"]')[0];
            if (fileInput.files.length > 0) {
                var fileSize = fileInput.files[0].size; // bytes
                var maxSize = 10 * 1024 * 1024; // 10MB
                if (fileSize > maxSize) {
                    alert('Kích thước file không được vượt quá 10MB');
                    return;
                }
            }
            
            // Hiển thị progress bar
            $('.progress').show();
            $('.progress-bar').css('width', '50%');
            $('#uploadStatus').html('<div class="text-center">' +
                '<i class="fas fa-spinner fa-spin me-2"></i>' +
                '<span>Đang import câu hỏi...</span>' +
                '</div>');

            $.ajax({
                url: 'edit-assignment',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    try {
                        if (typeof response === 'string') {
                            response = JSON.parse(response);
                        }
                        
                        if (response.success) {
                            $('.progress-bar').css('width', '100%')
                                .removeClass('bg-danger')
                                .addClass('bg-success');
                            
                            var successMessage = '<div class="alert alert-success mb-0">' +
                                '<i class="fas fa-check-circle me-2"></i>' +
                                response.message +
                                '</div>';
                            
                            $('#uploadStatus').html(successMessage);

                            // Delay trước khi reload trang
                            setTimeout(function() {
                                $('#importQuestionModal').css('display', 'none');
                                document.body.style.overflow = '';
                                location.reload();
                            }, 2000);
                        } else {
                            $('.progress-bar').css('width', '100%')
                                .removeClass('bg-success')
                                .addClass('bg-danger');
                            
                            var errorMessage = '<div class="alert alert-danger mb-0">' +
                                '<i class="fas fa-times-circle me-2"></i>' +
                                (response.message || 'Có lỗi xảy ra khi import file') +
                                '</div>';
                            
                            $('#uploadStatus').html(errorMessage);
                        }
                    } catch (e) {
                        console.error('Lỗi khi xử lý phản hồi:', e);
                        $('.progress-bar').css('width', '100%')
                            .removeClass('bg-success')
                            .addClass('bg-danger');
                        
                        var errorMessage = '<div class="alert alert-danger mb-0">' +
                            '<i class="fas fa-times-circle me-2"></i>' +
                            'Có lỗi xảy ra khi xử lý phản hồi từ server' +
                            '</div>';
                        
                        $('#uploadStatus').html(errorMessage);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Lỗi AJAX:', error);
                    $('.progress-bar').css('width', '100%')
                        .removeClass('bg-success')
                        .addClass('bg-danger');
                    
                    var errorMessage = '<div class="alert alert-danger mb-0">' +
                        '<i class="fas fa-times-circle me-2"></i>';
                    
                    try {
                        var response = JSON.parse(xhr.responseText);
                        errorMessage += response.message || 'Có lỗi xảy ra khi import file';
                    } catch (e) {
                        errorMessage += 'Có lỗi xảy ra khi import file';
                    }
                    
                    errorMessage += '</div>';
                    $('#uploadStatus').html(errorMessage);
                }
            });
        });
    });
</script>
</body>
</html>