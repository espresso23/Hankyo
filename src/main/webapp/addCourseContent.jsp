<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Nội Dung Khóa Học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .content-type-card {
            cursor: pointer;
            transition: all 0.3s;
        }

        .content-type-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .content-item {
            border-left: 4px solid #0d6efd;
            margin-bottom: 10px;
        }

        .video-preview {
            max-width: 100%;
            max-height: 200px;
            margin-top: 10px;
            display: none;
        }

        .file-upload-label {
            display: block;
            padding: 10px;
            border: 2px dashed #ccc;
            border-radius: 5px;
            text-align: center;
            cursor: pointer;
        }

        .file-upload-label:hover {
            border-color: #0d6efd;
        }

        .progress {
            height: 20px;
            margin-top: 10px;
            display: none;
        }

        .upload-success {
            display: none;
            color: #198754;
            margin-top: 10px;
            padding: 8px;
            background-color: rgba(25, 135, 84, 0.1);
            border-radius: 4px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <!-- Thông báo trạng thái -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show">
                ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show">
                ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Quản lý Nội Dung Khóa Học</h2>
        <a href="course?action=list" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-1"></i> Quay lại khóa học
        </a>
    </div>

    <!-- Form thêm nội dung -->
    <div class="card mb-4">
        <div class="card-header bg-primary text-white">
            <i class="fas fa-plus-circle me-2"></i>Thêm nội dung mới
        </div>
        <div class="card-body">
            <ul class="nav nav-tabs" id="contentTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="video-tab" data-bs-toggle="tab" data-bs-target="#video"
                            type="button">
                        <i class="fas fa-video me-1"></i> Video
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="assignment-tab" data-bs-toggle="tab" data-bs-target="#assignment"
                            type="button">
                        <i class="fas fa-tasks me-1"></i> Assignment
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="exam-tab" data-bs-toggle="tab" data-bs-target="#exam" type="button">
                        <i class="fas fa-edit me-1"></i> Exam
                    </button>
                </li>
            </ul>

            <div class="tab-content p-3 border border-top-0 rounded-bottom">
                <!-- Tab Video -->
                <div class="tab-pane fade show active" id="video" role="tabpanel">
                    <form id="videoForm" action="course-content?action=addVideo" method="post"
                          enctype="multipart/form-data">
                        <input type="hidden" name="action" value="addVideo">
                        <input type="hidden" name="courseID" value="${param.courseID}">

                        <div class="mb-3">
                            <label class="form-label">Tiêu đề video <span class="text-danger">*</span></label>
                            <input type="text" name="title" class="form-control" required maxlength="100">
                            <small class="text-muted">Tối đa 100 ký tự</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea name="description" class="form-control" rows="3" maxlength="500"></textarea>
                            <small class="text-muted">Tối đa 500 ký tự</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">File video <span class="text-danger">*</span></label>
                            <label for="videoFile" class="file-upload-label">
                                <i class="fas fa-cloud-upload-alt fa-2x mb-2"></i>
                                <p class="mb-1">Kéo thả file video vào đây hoặc click để chọn</p>
                                <p class="text-muted small">Hỗ trợ: MP4, MOV, AVI (Tối đa 100MB)</p>
                                <span id="fileName" class="text-primary fw-bold"></span>
                            </label>
                            <input type="file" id="videoFile" name="video" class="form-control d-none"
                                   accept="video/mp4,video/quicktime,video/x-msvideo" required>

                            <div class="progress" id="uploadProgress">
                                <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                                     style="width: 0%"></div>
                            </div>

                            <!-- Thông báo upload thành công -->
                            <div class="upload-success" id="uploadSuccess">
                                <i class="fas fa-check-circle me-1"></i> Video đã tải lên thành công và sẵn sàng để
                                submit!
                            </div>

                            <video id="videoPreview" class="video-preview" controls></video>
                        </div>

                        <div class="alert alert-warning small">
                            <i class="fas fa-info-circle me-1"></i> Video sẽ được tải lên Cloudinary và có thể mất vài
                            phút để xử lý.
                        </div>

                        <button type="submit" class="btn btn-primary" id="videoSubmitBtn">
                            <i class="fas fa-upload me-1"></i> Tải lên Video
                        </button>
                    </form>
                </div>

                <!-- Tab Assignment -->
                <div class="tab-pane fade" id="assignment" role="tabpanel">
                    <form action="course-content" method="post">
                        <input type="hidden" name="action" value="createEmptyAssignment">
                        <input type="hidden" name="courseID" value="${param.courseID}">

                        <div class="mb-3">
                            <label class="form-label">Tiêu đề assignment <span class="text-danger">*</span></label>
                            <input type="text" name="titleA" class="form-control" required maxlength="100">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả assignment</label>
                            <textarea name="descriptionA" class="form-control" rows="3" maxlength="500"></textarea>
                        </div>

<%--                        <div class="mb-3">--%>
<%--                            <label class="form-label">Hạn nộp <span class="text-danger">*</span></label>--%>
<%--                            <input type="datetime-local" name="deadline" class="form-control" required>--%>
<%--                        </div>--%>

                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-save me-1"></i> Lưu Assignment
                        </button>
                    </form>
                </div>

                <!-- Tab Exam -->
                <div class="tab-pane fade" id="exam" role="tabpanel">
                    <form action="course-content" method="post">
                        <input type="hidden" name="action" value="addExam">
                        <input type="hidden" name="courseID" value="${param.courseID}">

                        <div class="mb-3">
                            <label class="form-label">Tiêu đề exam <span class="text-danger">*</span></label>
                            <input type="text" name="title" class="form-control" required maxlength="100">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả exam</label>
                            <textarea name="description" class="form-control" rows="3" maxlength="500"></textarea>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Thời gian làm bài (phút) <span
                                        class="text-danger">*</span></label>
                                <input type="number" name="duration" class="form-control" min="1" max="300" required>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Số lượng câu hỏi <span class="text-danger">*</span></label>
                                <input type="number" name="questionCount" class="form-control" min="1" max="100"
                                       required>
                            </div>
                        </div>

                        <div class="alert alert-info small">
                            <i class="fas fa-info-circle me-1"></i> Bạn sẽ thêm chi tiết các câu hỏi sau khi tạo exam.
                        </div>

                        <button type="submit" class="btn btn-warning">
                            <i class="fas fa-plus-circle me-1"></i> Tạo Exam
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Danh sách nội dung hiện có -->
    <div class="card">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <div>
                <i class="fas fa-list me-2"></i>Nội dung hiện có
            </div>
            <span class="badge bg-light text-dark">
                Tổng: ${not empty contents ? contents.size() : 0}
            </span>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty contents}">
                    <div class="text-center py-4">
                        <i class="fas fa-folder-open fa-3x text-muted mb-3"></i>
                        <p class="text-muted">Chưa có nội dung nào được thêm vào khóa học này</p>
                        <p>Hãy bắt đầu bằng cách thêm video, assignment hoặc exam</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="list-group">
                        <c:forEach var="content" items="${contents}" varStatus="loop">
                            <div class="list-group-item content-item">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div class="flex-grow-1">
                                        <h5 class="mb-1">
                                            <span class="badge bg-secondary me-2">${loop.index + 1}</span>
                                                ${content.title}
                                        </h5>
                                        <p class="mb-2 text-muted">${content.description}</p>

                                        <div class="d-flex flex-wrap gap-2 small">
                                            <c:choose>
                                                <c:when test="${not empty content.media}">
                                                    <span class="badge bg-info">
                                                        <i class="fas fa-video me-1"></i> Video
                                                    </span>
                                                    <a href="${content.media}" target="_blank"
                                                       class="text-decoration-none">
                                                        <i class="fas fa-external-link-alt me-1"></i>Xem video
                                                    </a>
                                                </c:when>
                                                <c:when test="${not empty content.assignment}">
                                                    <span class="badge bg-success">
                                                        <i class="fas fa-tasks me-1"></i> Assignment
                                                    </span>
<%--                                                    <span>--%>
<%--                                                        <i class="far fa-clock me-1"></i>--%>
<%--                                                        <fmt:formatDate value="${content.assignment.deadline}"--%>
<%--                                                                        pattern="dd/MM/yyyy HH:mm"/>--%>
<%--                                                    </span>--%>
                                                </c:when>
                                                <c:when test="${not empty content.exam}">
                                                    <span class="badge bg-warning text-dark">
                                                        <i class="fas fa-edit me-1"></i> Exam
                                                    </span>
                                                    <span>
                                                        <i class="fas fa-stopwatch me-1"></i>
                                                        ${content.exam.duration} phút
                                                    </span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="btn-group">
                                        <c:choose>
                                            <c:when test="${not empty content.media}">
                                                <a href="edit-video?contentID=${content.courseContentID}&courseID=${param.courseID}" class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                            </c:when>
                                            <c:when test="${not empty content.assignment}">
                                                <a href="edit-assignment?action=getAssignment&assignmentID=${content.assignment.assignmentID}&courseID=${param.courseID}" class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                            </c:when>
                                            <c:when test="${not empty content.exam}">
                                                <a href="edit-exam?examID=${content.exam.examID}&courseID=${param.courseID}" class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                            </c:when>
                                        </c:choose>
                                        <button class="btn btn-sm btn-outline-danger delete-content" 
                                                data-content-id="${content.courseContentID}"
                                                data-content-type="${not empty content.media ? 'video' : not empty content.assignment ? 'assignment' : 'exam'}"
                                                title="Xóa">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.all.min.js"></script>
<script>
    // Xử lý hiển thị file video được chọn
    document.getElementById('videoFile').addEventListener('change', function (e) {
        const file = e.target.files[0];
        const maxSize = 100 * 1024 * 1024; // 100MB

        if (file && file.size > maxSize) {
            alert('File size exceeds 100MB limit');
            e.target.value = ''; // Clear the file input
            document.getElementById('fileName').textContent = '';
            document.getElementById('videoPreview').style.display = 'none';
            document.getElementById('uploadSuccess').style.display = 'none';
            return;
        }
        const fileNameElement = document.getElementById('fileName');
        const videoPreview = document.getElementById('videoPreview');
        const uploadSuccess = document.getElementById('uploadSuccess');

        if (file) {
            // Hiển thị tên file
            fileNameElement.textContent = file.name;

            // Hiển thị preview video
            if (file.type.startsWith('video/')) {
                const videoURL = URL.createObjectURL(file);
                videoPreview.src = videoURL;
                videoPreview.style.display = 'block';
            }

            // Validate kích thước file
            if (file.size > 100 * 1024 * 1024) { // 100MB
                alert('File vượt quá giới hạn 100MB');
                e.target.value = '';
                fileNameElement.textContent = '';
                videoPreview.style.display = 'none';
                uploadSuccess.style.display = 'none';
            } else {
                // Hiển thị dấu tick xanh khi file hợp lệ
                uploadSuccess.style.display = 'block';
            }
        }
    });

    // Drag and drop cho video
    const fileUploadLabel = document.querySelector('.file-upload-label');

    fileUploadLabel.addEventListener('dragover', (e) => {
        e.preventDefault();
        fileUploadLabel.style.borderColor = '#0d6efd';
        fileUploadLabel.style.backgroundColor = 'rgba(13, 110, 253, 0.1)';
    });

    fileUploadLabel.addEventListener('dragleave', () => {
        fileUploadLabel.style.borderColor = '#ccc';
        fileUploadLabel.style.backgroundColor = '';
    });

    fileUploadLabel.addEventListener('drop', (e) => {
        e.preventDefault();
        fileUploadLabel.style.borderColor = '#ccc';
        fileUploadLabel.style.backgroundColor = '';

        if (e.dataTransfer.files.length) {
            document.getElementById('videoFile').files = e.dataTransfer.files;
            const event = new Event('change');
            document.getElementById('videoFile').dispatchEvent(event);
        }
    });

    // Hiển thị progress bar khi submit form video
    document.getElementById('videoForm').addEventListener('submit', function () {
        const progressBar = document.getElementById('uploadProgress');
        const submitBtn = document.getElementById('videoSubmitBtn');
        const uploadSuccess = document.getElementById('uploadSuccess');

        progressBar.style.display = 'block';
        uploadSuccess.style.display = 'none'; // Ẩn thông báo thành công khi bắt đầu tải lên
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i> Đang tải lên...';

        // Simulate progress (trong thực tế sẽ dùng XMLHttpRequest để theo dõi tiến trình thực)
        const progressBarInner = progressBar.querySelector('.progress-bar');
        let progress = 0;
        const interval = setInterval(() => {
            progress += 5;
            progressBarInner.style.width = `${progress}%`;

            if (progress >= 100) {
                clearInterval(interval);
                // Có thể hiển thị lại thông báo thành công ở đây sau khi upload hoàn tất
                // uploadSuccess.style.display = 'block';
            }
        }, 300);
    });

    // Xử lý xóa nội dung
    document.querySelectorAll('.delete-content').forEach(button => {
        button.addEventListener('click', function() {
            const contentId = this.dataset.contentId;
            const contentType = this.dataset.contentType;

            Swal.fire({
                title: 'Xác nhận xóa',
                text: `Bạn có chắc chắn muốn xóa ${contentType} này?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#dc3545',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Hiển thị loading
                    Swal.fire({
                        title: 'Đang xóa...',
                        text: 'Vui lòng đợi trong giây lát',
                        allowOutsideClick: false,
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    });

                    // Gửi request xóa
                    console.log('Bắt đầu xóa nội dung:', {
                        contentId: contentId,
                        contentType: contentType,
                        courseID: ${param.courseID}
                    });

                    fetch(`course-content?action=deleteContent&contentID=${contentId}&courseID=${param.courseID}`, {
                        method: 'GET'
                    })
                    .then(response => {
                        console.log('Response status:', response.status);
                        if (!response.ok) {
                            throw new Error('Lỗi khi xóa nội dung');
                        }
                        return response.text();
                    })
                    .then(text => {
                        console.log('Response text:', text);
                        // Xóa thành công, hiển thị thông báo và reload trang
                        Swal.fire({
                            title: 'Thành công',
                            text: 'Đã xóa nội dung thành công',
                            icon: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.reload();
                        });
                    })
                    .catch(error => {
                        console.error('Lỗi khi xóa:', error);
                        Swal.fire({
                            title: 'Lỗi',
                            text: error.message,
                            icon: 'error'
                        });
                    });
                }
            });
        });
    });
</script>
</body>
</html>