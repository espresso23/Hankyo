<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa Video</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .video-preview {
            max-width: 100%;
            max-height: 300px;
            margin-top: 10px;
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
        <h2 class="mb-0">Chỉnh sửa Video</h2>
        <a href="course-content?action=addContentView&courseID=${param.courseID}" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-1"></i> Quay lại danh sách nội dung
        </a>
    </div>

    <!-- Form chỉnh sửa video -->
    <div class="card mb-4">
        <div class="card-header bg-primary text-white">
            <i class="fas fa-video me-2"></i>Thông tin video
        </div>
        <div class="card-body">
            <form id="editVideoForm" action="course-content" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="updateVideo">
                <input type="hidden" name="courseID" value="${param.courseID}">
                <input type="hidden" name="contentID" value="${videoContent.courseContentID}">

                <div class="mb-3">
                    <label class="form-label">Tiêu đề video <span class="text-danger">*</span></label>
                    <input type="text" name="title" class="form-control" value="${videoContent.title}" required maxlength="100">
                    <small class="text-muted">Tối đa 100 ký tự</small>
                </div>

                <div class="mb-3">
                    <label class="form-label">Mô tả</label>
                    <textarea name="description" class="form-control" rows="3" maxlength="500">${videoContent.description}</textarea>
                    <small class="text-muted">Tối đa 500 ký tự</small>
                </div>

                <div class="mb-3">
                    <label class="form-label">Video hiện tại</label>
                    <div class="ratio ratio-16x9 mb-3">
                        <video controls>
                            <source src="${videoContent.media}" type="video/mp4">
                            Trình duyệt của bạn không hỗ trợ video.
                        </video>
                    </div>
                    <a href="${videoContent.media}" target="_blank" class="btn btn-sm btn-outline-primary">
                        <i class="fas fa-external-link-alt me-1"></i> Mở video trong tab mới
                    </a>
                </div>

                <div class="mb-3">
                    <label class="form-label">Thay đổi video (tùy chọn)</label>
                    <label for="videoFile" class="file-upload-label">
                        <i class="fas fa-cloud-upload-alt fa-2x mb-2"></i>
                        <p class="mb-1">Kéo thả file video vào đây hoặc click để chọn</p>
                        <p class="text-muted small">Hỗ trợ: MP4, MOV, AVI (Tối đa 100MB)</p>
                        <span id="fileName" class="text-primary fw-bold"></span>
                    </label>
                    <input type="file" id="videoFile" name="video" class="form-control d-none"
                           accept="video/mp4,video/quicktime,video/x-msvideo">

                    <div class="progress" id="uploadProgress">
                        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                             style="width: 0%"></div>
                    </div>

                    <!-- Thông báo upload thành công -->
                    <div class="upload-success" id="uploadSuccess">
                        <i class="fas fa-check-circle me-1"></i> Video đã tải lên thành công và sẵn sàng để submit!
                    </div>

                    <video id="videoPreview" class="video-preview" controls style="display: none;"></video>
                </div>

                <div class="alert alert-warning small">
                    <i class="fas fa-info-circle me-1"></i> Nếu bạn không chọn video mới, video hiện tại sẽ được giữ nguyên.
                </div>

                <button type="submit" class="btn btn-primary" id="videoSubmitBtn">
                    <i class="fas fa-save me-1"></i> Lưu thay đổi
                </button>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
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
    document.getElementById('editVideoForm').addEventListener('submit', function () {
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
</script>
</body>
</html> 