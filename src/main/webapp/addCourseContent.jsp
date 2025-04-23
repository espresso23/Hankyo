<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Nội Dung Khóa Học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="asset/css/addCourseContent.css">
    <style>
        .image-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0 auto;
            width: 100%;
        }

        .fixed-size-image {
            width: 200px;
            height: 200px;
            display: flex;
            justify-content: center;
            align-items: center;
            overflow: hidden;
            border-radius: 8px;
            background-color: #f8f9fa;
        }

        .fixed-size-image img {
            width: 100%;
            height: 100%;
            object-fit: contain;
            transition: transform 0.3s ease;
        }

        .fixed-size-image:hover img {
            transform: scale(1.05);
        }

        #pdfPreviewContainer {
            transition: all 0.3s ease;
        }

        #pdfPreviewContainer.fullscreen {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            z-index: 1050;
            background: white;
            padding: 20px;
        }

        #pdfPreviewContainer.fullscreen #pdfPreviewWrapper {
            height: calc(100vh - 100px) !important;
        }

        #pdfPreviewContainer.fullscreen .btn-outline-secondary {
            position: absolute;
            top: 10px;
            right: 10px;
        }

        .modal-xl {
            max-width: 90%;
        }

        .pdf-container {
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 8px;
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .pdf-preview {
            flex: 1;
            min-height: 200px;
            background: white;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            overflow: hidden;
        }

        .pdf-title {
            font-size: 1rem;
            margin: 0;
            color: #333;
        }

        .pdf-container .btn-group {
            gap: 5px;
        }

        .pdf-container .fa-file-pdf {
            transition: transform 0.3s ease;
        }

        .pdf-container:hover .fa-file-pdf {
            transform: scale(1.1);
        }

        .modal-body .ratio {
            height: 80vh;
        }

        /* Style cho modal PDF */
        .modal-xl {
            max-width: 90%;
        }

        .modal-content {
            height: 90vh;
        }

        .modal-body {
            flex: 1;
            overflow: hidden;
        }

        .ratio-16x9 {
            height: calc(90vh - 70px); /* Trừ đi chiều cao của header */
        }

        iframe {
            background: white;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>
<div class="container mt-4">
    <!-- Thông báo trạng thái -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show">
                ${param.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show">
                ${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Quản lý Nội Dung Khóa Học</h2>
        <a href="course?action=list" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-1"></i> Quay lại khóa học
        </a>
    </div>

    <div class="row">
        <!-- Cột trái: Form thêm nội dung -->
        <div class="col-md-4">
            <div class="card sticky-top" style="top: 20px;">
                <div class="card-header">
                    <i class="fas fa-plus-circle me-2"></i>Thêm nội dung mới
                </div>
                <div class="card-body">
                    <ul class="nav nav-tabs" id="contentTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="video-tab" data-bs-toggle="tab" data-bs-target="#video" type="button">
                                <i class="fas fa-video me-1"></i> Video
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="pdf-tab" data-bs-toggle="tab" data-bs-target="#pdf" type="button">
                                <i class="fas fa-file-pdf me-1"></i> PDF
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="assignment-tab" data-bs-toggle="tab" data-bs-target="#assignment" type="button">
                                <i class="fas fa-tasks me-1"></i> Assignment
                            </button>
                        </li>
                    </ul>

                    <div class="tab-content mt-3">
                        <!-- Tab Video -->
                        <div class="tab-pane fade show active" id="video" role="tabpanel">
                            <form id="videoForm" action="course-content" method="post" enctype="multipart/form-data">
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
                                    <input type="file" id="videoFile" name="video" class="form-control d-none" accept="video/mp4,video/quicktime,video/x-msvideo" required>

                                    <div class="progress" id="uploadProgress">
                                        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 0%"></div>
                                    </div>

                                    <div class="upload-success" id="uploadSuccess" style="display: none;">
                                        <i class="fas fa-check-circle me-1"></i> Video đã tải lên thành công và sẵn sàng để submit!
                                    </div>

                                    <video id="videoPreview" style="display: none;" class="video-preview" controls></video>
                                </div>

                                <div class="alert alert-warning small">
                                    <i class="fas fa-info-circle me-1"></i> Video sẽ được tải lên Cloudinary và có thể mất vài phút để xử lý.
                                </div>

                                <button type="submit" class="btn btn-primary w-100" id="videoSubmitBtn">
                                    <i class="fas fa-upload me-1"></i> Tải lên Video
                                </button>
                            </form>
                        </div>

                        <!-- Tab PDF -->
                        <div class="tab-pane fade" id="pdf" role="tabpanel">
                            <form id="pdfForm" action="course-content" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="action" value="addPDF">
                                <input type="hidden" name="courseID" value="${param.courseID}">

                                <div class="mb-3">
                                    <label class="form-label">Tiêu đề tài liệu <span class="text-danger">*</span></label>
                                    <input type="text" name="title" class="form-control" required maxlength="100">
                                    <small class="text-muted">Tối đa 100 ký tự</small>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Mô tả</label>
                                    <textarea name="description" class="form-control" rows="3" maxlength="500"></textarea>
                                    <small class="text-muted">Tối đa 500 ký tự</small>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">File PDF <span class="text-danger">*</span></label>
                                    <label for="pdfFile" class="file-upload-label">
                                        <i class="fas fa-cloud-upload-alt fa-2x mb-2"></i>
                                        <p class="mb-1">Kéo thả file PDF vào đây hoặc click để chọn</p>
                                        <p class="text-muted small">Hỗ trợ: PDF (Tối đa 10MB)</p>
                                        <span id="pdfFileName" class="text-primary fw-bold"></span>
                                    </label>
                                    <input type="file" id="pdfFile" name="pdf" class="form-control d-none"
                                           accept="application/pdf" required>

                                    <div class="progress" id="pdfUploadProgress" style="display: none;">
                                        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 0%"></div>
                                    </div>

                                    <div class="upload-success" id="pdfUploadSuccess" style="display: none;">
                                        <i class="fas fa-check-circle me-1"></i> PDF đã tải lên thành công và sẵn sàng để submit!
                                    </div>

                                    <!-- Thêm iframe để xem trước PDF -->
                                    <div id="pdfPreviewContainer" class="mt-3" style="display: none;">
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <h6 class="mb-0">Xem trước PDF</h6>
                                            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="togglePdfPreview()">
                                                <i class="fas fa-expand"></i>
                                            </button>
                                        </div>
                                        <div id="pdfPreviewWrapper" style="height: 400px; border: 1px solid #dee2e6; border-radius: 4px;">
                                            <iframe id="pdfPreview" style="width: 100%; height: 100%; border: none;"></iframe>
                                        </div>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-primary w-100" id="pdfSubmitBtn">
                                    <i class="fas fa-upload me-1"></i> Tải lên PDF
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

                                <button type="submit" class="btn btn-primary w-100">
                                    <i class="fas fa-save me-1"></i> Lưu Assignment
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cột phải: Danh sách nội dung -->
        <div class="col-md-8">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
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
                            <div class="text-center py-5">
                                <i class="fas fa-folder-open fa-3x text-muted mb-3"></i>
                                <p class="text-muted mb-0">Chưa có nội dung nào được thêm vào khóa học này</p>
                                <p class="small text-muted">Hãy bắt đầu bằng cách thêm video hoặc assignment</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:forEach var="content" items="${contents}" varStatus="loop">
                                    <div class="col-lg-4 col-md-6 mb-3">
                                        <div class="card h-100 content-card">
                                            <div class="card-header content-header">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <span class="badge bg-secondary me-2">#ContentNo.${loop.index + 1}</span>
                                                    <div class="btn-group">
                                                        <c:choose>
                                                            <c:when test="${fn:contains(content.media, '.mp4') || fn:contains(content.media, '.mov') || fn:contains(content.media, '.avi')}">
                                                                <a href="edit-video?contentID=${content.courseContentID}&courseID=${param.courseID}"
                                                                   class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                                    <i class="fas fa-edit"></i>
                                                                </a>
                                                            </c:when>
                                                            <c:when test="${fn:contains(content.media, '.pdf')}">
                                                                <a href="edit-pdf?contentID=${content.courseContentID}&courseID=${param.courseID}"
                                                                   class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                                    <i class="fas fa-edit"></i>
                                                                </a>
                                                            </c:when>
                                                            <c:when test="${not empty content.assignment}">
                                                                <a href="edit-assignment?assignmentID=${content.assignment.assignmentID}&courseID=${param.courseID}"
                                                                   class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                                    <i class="fas fa-edit"></i>
                                                                </a>
                                                            </c:when>
                                                        </c:choose>
                                                        <button class="btn btn-sm btn-outline-danger delete-content"
                                                                data-content-id="${content.courseContentID}"
                                                                data-content-type="${not empty content.media ? 'video' : not empty content.assignment ? 'assignment' : 'exam'}"
                                                                data-assignment-id="${not empty content.assignment ? content.assignment.assignmentID : ''}"
                                                                title="Xóa">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="card-body">
                                                <c:choose>
                                                    <c:when test="${not empty content.media}">
                                                        <h5 class="video-card-title text-center mb-3">
                                                                ${content.title}
                                                        </h5>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(content.media, '.mp4') || fn:contains(content.media, '.mov') || fn:contains(content.media, '.avi')}">
                                                                <!-- Video content -->
                                                                <div class="video-container">
                                                                    <video preload="metadata" muted playsinline>
                                                                        <source src="${content.media}" type="video/mp4">
                                                                        Trình duyệt của bạn không hỗ trợ video.
                                                                    </video>
                                                                    <div class="video-play-button" data-bs-toggle="modal" data-bs-target="#videoModal${content.courseContentID}">
                                                                        <i class="fas fa-play"></i>
                                                                    </div>
                                                                </div>
                                                            </c:when>
                                                            <c:when test="${fn:contains(content.media, '.pdf')}">
                                                                <!-- PDF content -->
                                                                <div class="pdf-container">
                                                                    <div class="text-center">
                                                                        <h5 class="pdf-title mb-2">${content.title}</h5>
                                                                        <div class="btn-group">
                                                                            <button type="button" class="btn btn-sm btn-primary"
                                                                                    data-bs-toggle="modal"
                                                                                    data-bs-target="#pdfModal${content.courseContentID}">
                                                                                <i class="fas fa-expand me-1"></i> Xem toàn màn hình
                                                                            </button>
                                                                            <a href="${content.media}" target="_blank" class="btn btn-sm btn-outline-primary">
                                                                                <i class="fas fa-download me-1"></i> Tải PDF
                                                                            </a>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </c:when>
                                                        </c:choose>
                                                        <p class="card-text text-muted text-center">
                                                                ${content.description}
                                                        </p>
                                                    </c:when>
                                                    <c:when test="${not empty content.assignment}">
                                                        <div class="assignment-content">
                                                            <h5 class="assignment-title">
                                                                    ${content.assignment.assignmentTitle}
                                                            </h5>
                                                            <p class="assignment-description">
                                                                    ${content.assignment.description}
                                                            </p>
                                                            <div class="assignment-meta">
                                                                <i class="fas fa-clock me-1"></i> Cập nhật:
                                                                <fmt:formatDate value="${content.assignment.lastUpdated}" pattern="dd/MM/yyyy"/>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                </c:choose>

                                                <div class="text-center mt-3">
                                                    <c:choose>
                                                        <c:when test="${fn:contains(content.media, '.mp4') || fn:contains(content.media, '.mov') || fn:contains(content.media, '.avi')}">
                                                    <span class="badge bg-info">
                                                        <i class="fas fa-video me-1"></i> Video
                                                    </span>
                                                        </c:when>
                                                        <c:when test="${fn:contains(content.media, '.pdf')}">
                                                            <span class="badge bg-danger">
                                                                <i class="fas fa-file-pdf me-1"></i> PDF
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${not empty content.assignment}">
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-tasks me-1"></i> Assignment
                                                            </span>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Modal xem video -->
                                    <c:if test="${not empty content.media}">
                                        <div class="modal fade" id="videoModal${content.courseContentID}" tabindex="-1">
                                            <div class="modal-dialog modal-lg modal-dialog-centered">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">${content.title}</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body p-0">
                                                        <div class="ratio ratio-16x9">
                                                            <video controls preload="metadata" playsinline>
                                                                <source src="${content.media}" type="video/mp4">
                                                                Trình duyệt của bạn không hỗ trợ video.
                                                            </video>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
<div class="custom-overlay"></div>
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
            // Lấy giá trị từ data attributes
            const contentId = this.getAttribute('data-content-id');
            const contentType = this.getAttribute('data-content-type');
            const assignmentId = this.getAttribute('data-assignment-id');
            const courseId = '${param.courseID}';

            // Log chi tiết để debug
            console.log('Bắt đầu quá trình xóa nội dung:');
            console.log('- Loại nội dung:', contentType);
            console.log('- ID nội dung:', contentId);
            console.log('- ID bài tập:', assignmentId || 'Không có');
            console.log('- ID khóa học:', courseId);

            // Kiểm tra nếu contentId rỗng
            if (!contentId) {
                console.error('Lỗi: contentId đang rỗng!');
                alert('Không thể xác định ID nội dung cần xóa');
                return;
            }

            // Tạo URL xóa với các giá trị đã log
            let deleteUrl = 'course-content?action=deleteContent';
            deleteUrl += '&contentID=' + contentId;
            deleteUrl += '&courseID=' + courseId;

            if (contentType === 'assignment' && assignmentId && assignmentId.trim() !== '') {
                deleteUrl += '&assignmentID=' + assignmentId;
            }

            console.log('URL xóa:', deleteUrl);

            if (confirm('Bạn có chắc chắn muốn xóa nội dung này?')) {
                console.log('Người dùng đã xác nhận xóa');

                // Hiển thị loading
                const submitBtn = this;
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';

                // Gửi request xóa
                console.log('Đang gửi request xóa đến server...');
                fetch(deleteUrl, {
                    method: 'GET'
                })
                    .then(response => {
                        console.log('Phản hồi từ server:', response.status, response.statusText);
                        if (!response.ok) {
                            return response.text().then(text => {
                                console.error('Lỗi từ server:', text);
                                throw new Error(text || 'Lỗi khi xóa nội dung');
                            });
                        }
                        return response.text();
                    })
                    .then(text => {
                        console.log('Xóa thành công, phản hồi:', text);
                        alert('Đã xóa nội dung thành công');
                        window.location.reload();
                    })
                    .catch(error => {
                        console.error('Lỗi khi xóa:', error);
                        alert('Lỗi: ' + error.message);
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = '<i class="fas fa-trash"></i>';
                    });
            } else {
                console.log('Người dùng đã hủy thao tác xóa');
            }
        });
    });

    document.addEventListener('DOMContentLoaded', function() {
        const overlay = document.querySelector('.custom-overlay');

        // Hàm để xóa modal backdrop
        function removeModalBackdrop() {
            const backdrops = document.querySelectorAll('.modal-backdrop');
            backdrops.forEach(backdrop => backdrop.remove());
            document.body.classList.remove('modal-open');
            // Không set overflow: hidden cho body
            document.body.style.paddingRight = '';
        }

        // Xử lý video preview trong card
        const videoContainers = document.querySelectorAll('.video-container');
        videoContainers.forEach(container => {
            const video = container.querySelector('video');
            const playButton = container.querySelector('.video-play-button');

            // Tự động play video khi hover (muted)
            container.addEventListener('mouseenter', () => {
                if (video.paused) {
                    video.play().catch(e => console.log('Auto-play prevented:', e));
                }
            });

            container.addEventListener('mouseleave', () => {
                if (!video.paused) {
                    video.pause();
                    video.currentTime = 0;
                }
            });

            // Xử lý click vào nút play
            playButton.addEventListener('click', () => {
                const modalId = playButton.getAttribute('data-bs-target');
                const modal = document.querySelector(modalId);
                const modalVideo = modal.querySelector('video');
                const bsModal = new bootstrap.Modal(modal, {
                    backdrop: false // Tắt backdrop mặc định của Bootstrap
                });

                // Reset video preview
                video.pause();
                video.currentTime = 0;

                // Hiển thị overlay
                overlay.classList.add('show');

                // Chuẩn bị video trong modal
                if (modalVideo) {
                    modalVideo.currentTime = 0;
                    modalVideo.muted = false;
                }

                // Hiển thị modal
                bsModal.show();
            });
        });

        // Xử lý modal video
        const videoModals = document.querySelectorAll('.modal');
        videoModals.forEach(modal => {
            const modalVideo = modal.querySelector('video');
            const closeButtons = modal.querySelectorAll('[data-bs-dismiss="modal"]');
            const bsModal = new bootstrap.Modal(modal, {
                backdrop: false, // Tắt backdrop mặc định của Bootstrap
                keyboard: false
            });

            // Xử lý khi modal được mở
            modal.addEventListener('show.bs.modal', () => {
                // Không set overflow: hidden cho body
                if (modalVideo) {
                    modalVideo.currentTime = 0;
                    modalVideo.muted = false;
                    modalVideo.play().catch(e => console.log('Modal video play error:', e));
                }
            });

            // Xử lý khi modal đóng
            modal.addEventListener('hide.bs.modal', () => {
                if (modalVideo) {
                    modalVideo.pause();
                    modalVideo.currentTime = 0;
                }
                overlay.classList.remove('show');
                removeModalBackdrop();
            });

            modal.addEventListener('hidden.bs.modal', () => {
                removeModalBackdrop();
            });

            // Xử lý nút đóng
            closeButtons.forEach(button => {
                button.addEventListener('click', () => {
                    bsModal.hide();
                });
            });
        });

        // Xử lý click vào overlay để đóng modal
        overlay.addEventListener('click', () => {
            const openModal = document.querySelector('.modal.show');
            if (openModal) {
                const bsModal = bootstrap.Modal.getInstance(openModal);
                if (bsModal) {
                    bsModal.hide();
                }
            }
            overlay.classList.remove('show');
        });

        // Thêm xử lý phím ESC để đóng modal
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                const openModal = document.querySelector('.modal.show');
                if (openModal) {
                    const bsModal = bootstrap.Modal.getInstance(openModal);
                    if (bsModal) {
                        bsModal.hide();
                    }
                }
                overlay.classList.remove('show');
            }
        });
    });

    // Xử lý file PDF
    document.getElementById('pdfFile').addEventListener('change', function(e) {
        const file = e.target.files[0];
        const maxSize = 10 * 1024 * 1024; // 10MB
        const fileNameElement = document.getElementById('pdfFileName');
        const uploadSuccess = document.getElementById('pdfUploadSuccess');
        const pdfPreviewContainer = document.getElementById('pdfPreviewContainer');
        const pdfPreview = document.getElementById('pdfPreview');

        if (file) {
            // Kiểm tra kích thước
            if (file.size > maxSize) {
                alert('File vượt quá giới hạn 10MB');
                resetPdfUpload();
                return;
            }

            // Kiểm tra định dạng file
            if (file.type !== 'application/pdf') {
                alert('Chỉ chấp nhận file PDF');
                resetPdfUpload();
                return;
            }

            // Hiển thị tên file
            fileNameElement.textContent = file.name;

            // Hiển thị thông báo thành công
            uploadSuccess.style.display = 'block';

            // Hiển thị xem trước PDF
            const fileURL = URL.createObjectURL(file);
            pdfPreview.src = fileURL;
            pdfPreviewContainer.style.display = 'block';
        }
    });

    function resetPdfUpload() {
        document.getElementById('pdfFile').value = '';
        document.getElementById('pdfFileName').textContent = '';
        document.getElementById('pdfUploadSuccess').style.display = 'none';
        document.getElementById('pdfPreviewContainer').style.display = 'none';
        document.getElementById('pdfPreview').src = '';
    }

    function togglePdfPreview() {
        const container = document.getElementById('pdfPreviewContainer');
        const button = container.querySelector('.btn-outline-secondary i');

        if (container.classList.contains('fullscreen')) {
            container.classList.remove('fullscreen');
            button.classList.remove('fa-compress');
            button.classList.add('fa-expand');
        } else {
            container.classList.add('fullscreen');
            button.classList.remove('fa-expand');
            button.classList.add('fa-compress');
        }
    }

    // Xử lý form submit cho PDF
    document.getElementById('pdfForm').addEventListener('submit', function() {
        const progressBar = document.getElementById('pdfUploadProgress');
        const submitBtn = document.getElementById('pdfSubmitBtn');
        const uploadSuccess = document.getElementById('pdfUploadSuccess');

        progressBar.style.display = 'block';
        uploadSuccess.style.display = 'none';
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i> Đang tải lên...';

        // Simulate progress
        const progressBarInner = progressBar.querySelector('.progress-bar');
        let progress = 0;
        const interval = setInterval(() => {
            progress += 5;
            progressBarInner.style.width = `${progress}%`;
            if (progress >= 100) clearInterval(interval);
        }, 300);
    });

    // Thêm xử lý drag and drop cho PDF
    const pdfUploadLabel = document.querySelector('label[for="pdfFile"]');

    pdfUploadLabel.addEventListener('dragover', (e) => {
        e.preventDefault();
        pdfUploadLabel.style.borderColor = '#0d6efd';
        pdfUploadLabel.style.backgroundColor = 'rgba(13, 110, 253, 0.1)';
    });

    pdfUploadLabel.addEventListener('dragleave', () => {
        pdfUploadLabel.style.borderColor = '#ccc';
        pdfUploadLabel.style.backgroundColor = '';
    });

    pdfUploadLabel.addEventListener('drop', (e) => {
        e.preventDefault();
        pdfUploadLabel.style.borderColor = '#ccc';
        pdfUploadLabel.style.backgroundColor = '';

        if (e.dataTransfer.files.length) {
            const input = document.getElementById('pdfFile');
            input.files = e.dataTransfer.files;
            input.dispatchEvent(new Event('change'));
        }
    });

    // Thêm xử lý chuyển trang PDF
    document.addEventListener('DOMContentLoaded', function() {
        // Lấy tất cả các modal PDF
        const pdfModals = document.querySelectorAll('[id^="pdfModal"]');

        pdfModals.forEach(modal => {
            const contentId = modal.id.replace('pdfModal', '');
            const pdfViewer = document.getElementById('pdfViewer' + contentId);
            const pdfImage = document.getElementById('pdfImage' + contentId);
            const pageNum = document.getElementById('pageNum' + contentId);
            const prevBtn = document.getElementById('prevPage' + contentId);
            const nextBtn = document.getElementById('nextPage' + contentId);
            const viewPdfBtn = document.getElementById('viewPdf' + contentId);
            const viewImageBtn = document.getElementById('viewImage' + contentId);
            const pageControls = document.getElementById('pageControls' + contentId);
            const downloadBtn = document.getElementById('downloadBtn' + contentId);
            const altDownloadBtn = document.getElementById('altDownloadBtn' + contentId);

            let currentPage = 1;
            let pdfUrl = '';
            let imageUrl = '';

            // Lấy URL từ data attribute và xử lý
            const mediaUrl = '${content.media}'.trim();
            if (mediaUrl.startsWith('{')) {
                // Nếu là JSON string
                try {
                    const urls = JSON.parse(mediaUrl);
                    pdfUrl = urls.raw;
                    imageUrl = urls.image;
                } catch (e) {
                    console.error('Error parsing PDF URLs:', e);
                }
            } else {
                // Nếu là URL đơn
                pdfUrl = mediaUrl;
                imageUrl = mediaUrl.replace('.pdf', '.jpg');
            }

            if (pdfViewer && pdfImage && pdfUrl && imageUrl) {
                // Hàm cập nhật URL hình ảnh với số trang
                function updatePdfImage(page) {
                    const baseUrl = imageUrl.split('/upload/')[0] + '/upload/';
                    const imagePath = imageUrl.split('/upload/')[1];
                    pdfImage.src = baseUrl + 'pg_' + page + '/' + imagePath;
                    pageNum.textContent = 'Trang ' + page;
                }

                // Xử lý chuyển đổi chế độ xem
                viewPdfBtn.addEventListener('click', () => {
                    viewPdfBtn.classList.add('active');
                    viewImageBtn.classList.remove('active');
                    pdfViewer.style.display = 'block';
                    pdfImage.style.display = 'none';
                    pageControls.style.display = 'none';
                    pdfViewer.data = pdfUrl;
                });

                viewImageBtn.addEventListener('click', () => {
                    viewImageBtn.classList.add('active');
                    viewPdfBtn.classList.remove('active');
                    pdfViewer.style.display = 'none';
                    pdfImage.style.display = 'block';
                    pageControls.style.display = 'block';
                    updatePdfImage(currentPage);
                });

                // Xử lý nút Previous
                prevBtn.addEventListener('click', () => {
                    if (currentPage > 1) {
                        currentPage--;
                        updatePdfImage(currentPage);
                        nextBtn.disabled = false;
                    }
                });

                // Xử lý nút Next
                nextBtn.addEventListener('click', () => {
                    currentPage++;
                    updatePdfImage(currentPage);
                });

                // Xử lý lỗi khi trang không tồn tại
                pdfImage.addEventListener('error', () => {
                    if (currentPage > 1) {
                        currentPage--;
                        updatePdfImage(currentPage);
                        nextBtn.disabled = true;
                    }
                });

                // Cập nhật các nút download
                downloadBtn.href = pdfUrl;
                altDownloadBtn.href = pdfUrl;

                // Reset về trang 1 và chế độ xem ảnh khi mở modal
                modal.addEventListener('show.bs.modal', () => {
                    currentPage = 1;
                    viewImageBtn.click();
                    nextBtn.disabled = false;
                });
            }
        });
    });

    // Hàm in PDF
    function printPdf(iframeId) {
        const iframe = document.getElementById(iframeId);
        iframe.contentWindow.print();
    }

    // Xử lý khi modal đóng
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('hidden.bs.modal', function () {
            const iframe = this.querySelector('iframe');
            const currentSrc = iframe.src;
            iframe.src = '';
            iframe.src = currentSrc;
        });
    });
</script>

<!-- Thêm modal xem PDF cho mỗi nội dung PDF -->
<c:forEach var="content" items="${contents}">
    <c:if test="${fn:contains(content.media, '.pdf')}">
        <div class="modal fade" id="pdfModal${content.courseContentID}" tabindex="-1">
            <div class="modal-dialog modal-xl modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${content.title}</h5>
                        <div class="ms-auto d-flex align-items-center">
                            <div class="btn-group me-3">
                                <a href="${content.media}" class="btn btn-sm btn-outline-primary" download>
                                    <i class="fas fa-download me-1"></i> Tải PDF
                                </a>
                                <button type="button" class="btn btn-sm btn-outline-primary" onclick="printPdf('pdfViewer${content.courseContentID}')">
                                    <i class="fas fa-print me-1"></i> In
                                </button>
                            </div>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                    </div>
                    <div class="modal-body p-0">
                        <div class="ratio ratio-16x9">
                            <iframe src="${content.media}" class="w-100 h-100" id="pdfViewer${content.courseContentID}" 
                                    frameborder="0" allowfullscreen></iframe>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</c:forEach>
</body>
</html>