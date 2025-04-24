<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa bài thi</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/css/editAssignment.css">
    <style>
        .question-item {
            display: none;
        }
        .question-item.active {
            display: block;
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
        <i class="fas fa-edit me-2"></i>Chỉnh sửa bài thi
    </h4>

    <div class="row">
        <!-- Cột trái: Thông tin bài tập -->
        <div class="col-md-4">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <i class="fas fa-info-circle me-2"></i>Thông Tin Bài Thi
                </div>
                <div class="card-body">
                    <form id="examForm" action="edit-exam" method="post">
                        <input type="hidden" name="action" value="updateExam">
                        <input type="hidden" name="examID" value="${exam.examID}">

                        <div class="mb-3">
                            <label class="form-label">Tiêu đề bài thi</label>
                            <input type="text" name="examName" class="form-control" value="${exam.examName}"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea name="examDescription" class="form-control" rows="4"
                                      required>${exam.examDescription}</textarea>
                        </div>
                        <div class="mb-3">
                            <label for="examType">Loại đề thi <span class="text-danger">*</span></label>
                            <select id="examType" name="examType" class="form-control" required>
                                <option value="">Chọn loại đề thi</option>
                                <option value="TOPIKI" ${exam.examType == 'TOPIKI' ? 'selected' : ''}>TOPIK I</option>
                                <option value="TOPIKII" ${exam.examType == 'TOPIKII' ? 'selected' : ''}>TOPIK II
                                </option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="status">Trạng thái <span class="text-danger">*</span></label>
                            <select id="status" name="status" class="form-control" required>
                                <option value="">Chọn trạng thái</option>
                                <option value="ACTIVE" ${exam.status == 'ACTIVE' ? 'selected' : ''}>Mở</option>
                                <option value="CLOSED" ${exam.status == 'CLOSED' ? 'selected' : ''}>Đóng</option>
                            </select>
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
                            <div class="col-md-6 mb-3 question-item" data-index="${status.index}">
                                <div class="card question-card h-100">
                                    <div class="card-header d-flex justify-content-between align-items-center py-2">
                                        <span class="fw-bold">Câu ${status.index + 1}</span>
                                        <div>
                                            <button class="btn btn-primary btn-sm edit-question me-1"
                                                    data-question-id="${question.questionID}"
                                                    data-question-text="${question.questionText}"
                                                    data-question-type="${question.questionType}"
                                                    data-question-mark="${question.questionMark}"
                                                    data-exam-id="${exam.examID}">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-danger btn-sm delete-question"
                                                    data-question-id="${question.questionID}"
                                                    data-exam-id="${exam.examID}">
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
                    Exam ID: ${exam.examID}<br>
                </div>

                <form id="questionForm" method="post" action="edit-exam" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="addQuestion">
                    <input type="hidden" name="examID" value="${exam.examID}">

                    <div class="mb-3">
                        <label for="questionType" class="form-label">Loại câu hỏi</label>
                        <select name="questionType" class="form-select" id="questionType" required>
                            <option value="multiple_choice">Trắc nghiệm</option>
                            <option value="short_answer">Câu trả lời ngắn</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="questionText" class="form-label">Nội dung câu hỏi</label>
                        <textarea id="questionText" name="questionText" class="form-control" rows="3" 
                            placeholder="Nhập nội dung câu hỏi" required></textarea>
                    </div>

                    <div class="mb-3">
                        <label for="questionMark" class="form-label">Điểm</label>
                        <input type="number" id="questionMark" name="questionMark" class="form-control" 
                            min="0" step="0.25" value="1" required>
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
                            <c:forEach begin="0" end="3" var="i">
                                <div class="answer-option mb-2">
                                    <div class="input-group">
                                        <div class="input-group-text option-label">${['A', 'B', 'C', 'D'][i]}</div>
                                        <input type="text" name="answers" class="form-control" placeholder="Nhập lựa chọn ${['A', 'B', 'C', 'D'][i]}" required>
                                        <input type="hidden" name="option_labels" value="${['A', 'B', 'C', 'D'][i]}">
                                        <div class="input-group-text">
                                            <input type="checkbox" name="isCorrect" value="1" class="answer-checkbox me-2">
                                            <label class="correct-label mb-0">Đúng</label>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
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
                <form id="editQuestionForm" method="post" action="edit-exam" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="updateQuestion">
                    <input type="hidden" name="questionID" id="editQuestionID">
                    <input type="hidden" name="examID" id="editExamID">

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
                            <c:forEach begin="0" end="3" var="i">
                                <div class="answer-option">
                                    <div class="input-group">
                                        <div class="option-label">${['A', 'B', 'C', 'D'][i]}</div>
                                        <input type="text" name="edit_answers" class="form-control" placeholder="Nhập lựa chọn" required>
                                        <div class="input-group-text">
                                            <input type="checkbox" name="edit_isCorrect" class="answer-checkbox" data-option="${['A', 'B', 'C', 'D'][i]}">
                                            <label class="correct-label">Đúng</label>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
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
                '</div>' +
                '</div>';

            $('#answerOptions').append(newOptionHtml);
        });

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
            const examID = $(this).data('exam-id');

            // Hiển thị loading
            $('.loading').show();

            // Lấy thông tin câu hỏi từ server
            $.ajax({
                url: 'edit-exam',
                method: 'POST',
                data: {
                    action: 'getQuestionDetails',
                    questionID: questionId,
                    examID: examID
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
                            $('#editExamID').val(response.question.examID);
                            $('#editQuestionText').val(response.question.questionText);
                            $('#editQuestionType').val(response.question.questionType);
                            $('#editQuestionMark').val(response.question.questionMark);

                            // Hiển thị hình ảnh hiện tại nếu có
                            if (response.question.questionImage) {
                                $('#editImagePreview').html(`
                                    <div class="mb-2">
                                        <label class="form-label">Hình ảnh hiện tại:</label>
                                        <img src="${response.question.questionImg}" class="img-thumbnail" style="max-height: 150px">
                                        <input type="hidden" name="currentQuestionImage" value="${response.question.questionImg}">
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
                                        '<input type="text" name="edit_answers" class="form-control" value="' + answer.answerText + '" required>' +
                                        '<div class="input-group-text">' +
                                        '<input type="checkbox" name="edit_isCorrect" class="answer-checkbox"' +
                                        (answer.isCorrect === true ? ' checked="checked"' : '') +
                                        ' data-option="' + answer.optionLabel + '">' +
                                        '<label class="correct-label">Đúng</label>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $('#editAnswerOptions').append(optionHtml);

                                    // Log trạng thái của câu trả lời sau khi thêm
                                    console.log('Câu trả lời ' + answer.optionLabel + ':', {
                                        text: answer.answerText,
                                        isCorrect: answer.correct,
                                        checked: answer.correct === true
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
                const examID = $(this).data('exam-id');

                console.log('Bắt đầu xóa câu hỏi:', {
                    questionId: questionId,
                    examID: examID
                });

                $('.loading').show();

                $.ajax({
                    url: 'edit-exam',
                    method: 'POST',
                    data: {
                        action: 'deleteQuestion',
                        questionID: questionId,
                        examID: examID
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
            $('.question-item').each(function() {
                const index = parseInt($(this).data('index'));
                const start = (currentPage - 1) * itemsPerPage;
                const end = start + itemsPerPage;
                $(this).toggleClass('active', index >= start && index < end);
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

            // Tạo FormData object
            const formData = new FormData(this);

            // Kiểm tra các trường bắt buộc
            const questionText = $('#questionText').val() || '';
            const questionType = $('#questionType').val();
            const questionMark = $('#questionMark').val();

            if (!questionText.trim()) {
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
                // Thu thập dữ liệu câu trả lời
                const answers = [];
                let hasCorrectAnswer = false;

                $('.answer-option').each(function() {
                    const answerInput = $(this).find('input[name="answers"]');
                    const answerText = answerInput.val() || '';
                    const optionLabel = $(this).find('.option-label').text() || '';
                    const isChecked = $(this).find('.answer-checkbox').is(':checked');

                    if (isChecked) {
                        hasCorrectAnswer = true;
                    }

                    if (!answerText.trim()) {
                        alert('Vui lòng nhập đầy đủ nội dung cho tất cả các câu trả lời');
                        e.preventDefault();
                        return false;
                    }

                    answers.push({
                        text: answerText.trim(),
                        label: optionLabel.trim(),
                        isCorrect: isChecked
                    });
                });

                // Kiểm tra đáp án đúng
                if (!hasCorrectAnswer) {
                    alert('Vui lòng chọn ít nhất một đáp án đúng');
                    return false;
                }

                // Xóa dữ liệu cũ
                formData.delete('answers');
                formData.delete('option_labels');
                formData.delete('isCorrect');

                // Thêm dữ liệu mới
                answers.forEach(answer => {
                    formData.append('answers', answer.text);
                    formData.append('option_labels', answer.label);
                    formData.append('isCorrect', answer.isCorrect ? '1' : '0');
                });

                // Log dữ liệu form trước khi gửi
                console.log('Form data before submit:', {
                    answers: formData.getAll('answers'),
                    option_labels: formData.getAll('option_labels'),
                    isCorrect: formData.getAll('isCorrect')
                });
            }

            // Hiển thị loading
            $('.loading').show();

            // Gửi request
            $.ajax({
                url: 'edit-exam',
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
                            $('#addQuestionModal').css('display', 'none');
                            document.body.style.overflow = '';
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
                    alert('Có lỗi xảy ra khi thêm câu hỏi: ' + error);
                }
            });
        });

        // Xử lý thêm lựa chọn mới trong modal chỉnh sửa
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
                '<input type="text" name="edit_answers" class="form-control" placeholder="Nhập lựa chọn" required>' +
                '<div class="input-group-text">' +
                '<input type="checkbox" name="edit_isCorrect" class="answer-checkbox">' +
                '<label class="correct-label">Đúng</label>' +
                '</div>' +
                '</div>' +
                '</div>';

            $('#editAnswerOptions').append(newOptionHtml);
        });

        // Xử lý submit form chỉnh sửa
        $('#editQuestionForm').on('submit', function(e) {
            e.preventDefault();

            // Kiểm tra các trường bắt buộc
            const questionText = $('#editQuestionText').val() || '';
            const questionType = $('#editQuestionType').val();
            const questionMark = $('#editQuestionMark').val();

            if (!questionText.trim()) {
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

            // Tạo FormData object
            const formData = new FormData(this);

            // Xử lý câu trả lời trắc nghiệm
            if (questionType === 'multiple_choice') {
                const answers = [];
                let hasCorrectAnswer = false;

                $('#editAnswerOptions .answer-option').each(function() {
                    const answerInput = $(this).find('input[name="edit_answers"]');
                    const answerText = answerInput.val() || '';
                    const optionLabel = $(this).find('.option-label').text() || '';
                    const isChecked = $(this).find('input[name="edit_isCorrect"]').is(':checked');

                    if (isChecked) {
                        hasCorrectAnswer = true;
                    }

                    if (!answerText.trim()) {
                        alert('Vui lòng nhập đầy đủ nội dung cho tất cả các câu trả lời');
                        e.preventDefault();
                        return false;
                    }

                    answers.push({
                        text: answerText.trim(),
                        label: optionLabel.trim(),
                        isCorrect: isChecked
                    });
                });

                // Kiểm tra đáp án đúng
                if (!hasCorrectAnswer) {
                    alert('Vui lòng chọn ít nhất một đáp án đúng');
                    return false;
                }

                // Xóa dữ liệu cũ
                formData.delete('answers');
                formData.delete('option_labels');
                formData.delete('isCorrect');

                // Thêm dữ liệu mới
                answers.forEach(answer => {
                    formData.append('answers', answer.text);
                    formData.append('option_labels', answer.label);
                    formData.append('isCorrect', answer.isCorrect ? '1' : '0');
                });
            }

            // Hiển thị loading
            $('.loading').show();

            // Gửi request chỉnh sửa
            $.ajax({
                url: 'edit-exam',
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
    });
</script>
</body>
</html>