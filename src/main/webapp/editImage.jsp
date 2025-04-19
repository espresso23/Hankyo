<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa hình ảnh</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 20px;
        }

        .card {
            background: rgba(255, 255, 255, 0.9);
            border: none;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
        }

        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 1.5rem;
        }

        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 10px 25px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .image-preview {
            max-width: 100%;
            max-height: 400px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
            display: block;
        }

        .file-upload-label {
            border: 2px dashed #667eea;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 200px;
            background-color: rgba(102, 126, 234, 0.05);
            margin: 20px 0;
        }

        .file-upload-label:hover {
            background: rgba(102, 126, 234, 0.1);
            border-color: #764ba2;
            transform: translateY(-2px);
        }

        .file-upload-label i {
            font-size: 2.5rem;
            color: #667eea;
            margin-bottom: 15px;
            transition: transform 0.3s ease;
        }

        .file-upload-label:hover i {
            transform: translateY(-5px);
        }

        .file-upload-label p {
            margin: 0;
            line-height: 1.5;
        }

        .file-upload-label p:first-of-type {
            font-size: 1.1rem;
            font-weight: 500;
            color: #333;
            margin-bottom: 8px;
        }

        .file-upload-label .text-muted {
            font-size: 0.9rem;
        }

        #fileName {
            margin-top: 10px;
            padding: 5px 10px;
            background-color: rgba(102, 126, 234, 0.1);
            border-radius: 5px;
            display: inline-block;
        }

        .upload-success {
            margin-top: 15px;
            padding: 10px;
            border-radius: 8px;
            background-color: rgba(72, 187, 120, 0.1);
            color: #2f855a;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .upload-success i {
            margin-right: 8px;
            color: #48bb78;
        }

        .progress {
            height: 10px;
            border-radius: 5px;
            margin: 15px 0;
        }

        .progress-bar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        .current-image-container {
            margin: 20px 0;
            text-align: center;
        }

        .image-wrapper {
            position: relative;
            display: inline-block;
            max-width: 100%;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .image-wrapper:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .current-image-preview {
            max-width: 100%;
            max-height: 400px;
            display: block;
            transition: transform 0.3s ease;
        }

        .image-wrapper:hover .current-image-preview {
            transform: scale(1.02);
        }

        .image-overlay {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
            padding: 20px;
            color: white;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .image-wrapper:hover .image-overlay {
            opacity: 1;
        }

        .image-info {
            font-size: 0.9rem;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>
<div class="container mt-4">
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

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h4 class="mb-0">
                        <i class="fas fa-edit me-2"></i>Chỉnh sửa hình ảnh
                    </h4>
                </div>
                <div class="card-body">
                    <form action="edit-image" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="updateImage">
                        <input type="hidden" name="contentID" value="${param.contentID}">
                        <input type="hidden" name="courseID" value="${param.courseID}">

                        <div class="mb-3">
                            <label class="form-label">Tiêu đề hình ảnh <span class="text-danger">*</span></label>
                            <input type="text" name="title" class="form-control" value="${imageContent.title}" required
                                   maxlength="100">
                            <small class="text-muted">Tối đa 100 ký tự</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea name="description" class="form-control" rows="3"
                                      maxlength="500">${imageContent.description}</textarea>
                            <small class="text-muted">Tối đa 500 ký tự</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Hình ảnh hiện tại</label>
                            <div class="current-image-container">
                                <div class="image-wrapper">
                                    <img src="${imageContent.media}" alt="${imageContent.title}"
                                         class="current-image-preview">
                                    <div class="image-overlay">
                                        <span class="image-info">
                                            <i class="fas fa-image me-1"></i>
                                            ${imageContent.title}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Thay đổi hình ảnh</label>
                            <label for="imageFile" class="file-upload-label">
                                <i class="fas fa-cloud-upload-alt"></i>
                                <p>Kéo thả hình ảnh vào đây hoặc click để chọn</p>
                                <p class="text-muted">Hỗ trợ: JPG, PNG, GIF (Tối đa 10MB)</p>
                                <span id="fileName" class="text-primary fw-bold" style="display: none;"></span>
                            </label>
                            <input type="file" id="imageFile" name="image" class="form-control d-none"
                                   accept="image/jpeg,image/png,image/gif">

                            <div class="progress" id="uploadProgress" style="display: none;">
                                <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                                     style="width: 0%"></div>
                            </div>

                            <div class="upload-success" id="uploadSuccess" style="display: none;">
                                <i class="fas fa-check-circle"></i>
                                <span>Hình ảnh đã tải lên thành công và sẵn sàng để submit!</span>
                            </div>

                            <img id="imagePreview" class="image-preview" style="display: none;">
                        </div>

                        <div class="d-flex justify-content-between">
                            <a href="course-content?action=addContentView&courseID=${param.courseID}"
                               class="btn btn-secondary">
                                <i class="fas fa-arrow-left me-1"></i> Quay lại
                            </a>
                            <button type="submit" class="btn btn-primary" id="submitBtn">
                                <i class="fas fa-save me-1"></i> Lưu thay đổi
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>

<script>
    // Xử lý file hình ảnh
    document.getElementById('imageFile').addEventListener('change', function (e) {
        const file = e.target.files[0];
        const maxSize = 10 * 1024 * 1024; // 10MB
        const fileNameElement = document.getElementById('fileName');
        const previewElement = document.getElementById('imagePreview');
        const uploadSuccess = document.getElementById('uploadSuccess');

        if (file) {
            // Kiểm tra kích thước
            if (file.size > maxSize) {
                alert('File vượt quá giới hạn 10MB');
                resetImageUpload();
                return;
            }

            // Kiểm tra định dạng file
            if (!file.type.startsWith('image/')) {
                alert('Chỉ chấp nhận file hình ảnh (JPG, PNG, GIF)');
                resetImageUpload();
                return;
            }

            // Hiển thị tên file
            fileNameElement.textContent = file.name;

            // Hiển thị preview
            const reader = new FileReader();
            reader.onload = function (e) {
                previewElement.src = e.target.result;
                previewElement.style.display = 'block';

                // Thêm hiệu ứng fade in
                previewElement.style.opacity = '0';
                setTimeout(() => {
                    previewElement.style.transition = 'opacity 0.3s ease';
                    previewElement.style.opacity = '1';
                }, 10);
            }
            reader.readAsDataURL(file);

            // Hiển thị thông báo thành công
            uploadSuccess.style.display = 'block';
        }
    });

    function resetImageUpload() {
        document.getElementById('imageFile').value = '';
        document.getElementById('fileName').textContent = '';
        document.getElementById('imagePreview').style.display = 'none';
        document.getElementById('uploadSuccess').style.display = 'none';
    }

    // Xử lý form submit
    document.querySelector('form').addEventListener('submit', function () {
        const progressBar = document.getElementById('uploadProgress');
        const submitBtn = document.getElementById('submitBtn');
        const uploadSuccess = document.getElementById('uploadSuccess');

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

    // Thêm xử lý drag and drop
    const fileUploadLabel = document.querySelector('.file-upload-label');

    fileUploadLabel.addEventListener('dragover', (e) => {
        e.preventDefault();
        fileUploadLabel.style.borderColor = '#667eea';
        fileUploadLabel.style.backgroundColor = 'rgba(102, 126, 234, 0.1)';
    });

    fileUploadLabel.addEventListener('dragleave', () => {
        fileUploadLabel.style.borderColor = '#667eea';
        fileUploadLabel.style.backgroundColor = '';
    });

    fileUploadLabel.addEventListener('drop', (e) => {
        e.preventDefault();
        fileUploadLabel.style.borderColor = '#667eea';
        fileUploadLabel.style.backgroundColor = '';

        if (e.dataTransfer.files.length) {
            const input = document.getElementById('imageFile');
            input.files = e.dataTransfer.files;
            input.dispatchEvent(new Event('change'));
        }
    });
</script>
</body>
</html> 