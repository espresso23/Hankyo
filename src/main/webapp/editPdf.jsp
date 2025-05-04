<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <title>Chỉnh sửa PDF</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="asset/css/editPdf.css">
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

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Chỉnh sửa PDF</h2>
        <a href="course-content?action=addContentView&courseID=${param.courseID}" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-1"></i> Quay lại danh sách nội dung
        </a>
    </div>

    <div class="card mb-4">
        <div class="card-header bg-danger text-white">
            <i class="fas fa-file-pdf me-2"></i>Thông tin PDF
        </div>
        <div class="card-body">
            <form id="editPdfForm" action="edit-pdf" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="updatePDF">
                <input type="hidden" name="courseID" value="${param.courseID}">
                <input type="hidden" name="contentID" value="${pdfContent.courseContentID}">

                <div class="form-row">
                    <div class="form-col">
                        <div class="mb-3">
                            <label class="form-label">Tiêu đề PDF <span class="text-danger">*</span></label>
                            <input type="text" name="title" class="form-control" value="${pdfContent.title}" required maxlength="100">
                            <small class="text-muted">Tối đa 100 ký tự</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea name="description" class="form-control" rows="3" maxlength="500">${pdfContent.description}</textarea>
                            <small class="text-muted">Tối đa 500 ký tự</small>
                        </div>
                    </div>
                </div>

                <div class="pdf-section">
                    <div class="pdf-preview-section mb-4">
                        <label class="form-label">PDF hiện tại</label>
                        <div class="pdf-preview-container" id="currentPdfPreview">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="toggleFullscreen('currentPdfPreview')">
                                        <i class="fas fa-expand"></i> Xem toàn màn hình
                                    </button>
                                    <a href="${pdfContent.media}" class="btn btn-sm btn-outline-primary" download>
                                        <i class="fas fa-download"></i> Tải PDF
                                    </a>
                                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="printPdf('currentPdfViewer')">
                                        <i class="fas fa-print"></i> In
                                    </button>
                                </div>
                            </div>
                            <div class="pdf-preview-wrapper">
                                <iframe id="currentPdfViewer" src="${pdfContent.media}" width="100%" height="100%" frameborder="0"></iframe>
                            </div>
                        </div>
                    </div>

                    <div class="pdf-upload-section">
                        <label class="form-label">Thay đổi PDF (tùy chọn)</label>
                        <label for="pdfFile" class="file-upload-label">
                            <i class="fas fa-cloud-upload-alt fa-2x mb-2"></i>
                            <p class="mb-1">Kéo thả file PDF vào đây hoặc click để chọn</p>
                            <p class="text-muted small">Chỉ hỗ trợ file PDF (Tối đa 10MB)</p>
                            <span id="fileName" class="text-primary fw-bold"></span>
                        </label>
                        <input type="file" id="pdfFile" name="pdf" class="form-control d-none" accept="application/pdf">

                        <div class="progress" id="uploadProgress">
                            <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 0%"></div>
                        </div>

                        <div class="upload-success" id="uploadSuccess">
                            <i class="fas fa-check-circle me-1"></i> PDF đã tải lên thành công và sẵn sàng để submit!
                        </div>

                        <div id="newPdfPreview" class="pdf-preview-container mt-3" style="display: none;">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <h6 class="mb-0">Xem trước PDF mới</h6>
                                <button type="button" class="btn btn-sm btn-outline-secondary" onclick="toggleFullscreen('newPdfPreview')">
                                    <i class="fas fa-expand"></i>
                                </button>
                            </div>
                            <div class="pdf-preview-wrapper">
                                <iframe id="pdfPreview" width="100%" height="100%" frameborder="0"></iframe>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="alert alert-warning small mt-3">
                    <i class="fas fa-info-circle me-1"></i> Nếu bạn không chọn PDF mới, PDF hiện tại sẽ được giữ nguyên.
                </div>

                <button type="submit" class="btn btn-primary" id="pdfSubmitBtn">
                    <i class="fas fa-save me-1"></i> Lưu thay đổi
                </button>
            </form>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Xử lý file upload
    document.getElementById('pdfFile').addEventListener('change', function(e) {
        const file = e.target.files[0];
        const maxSize = 10 * 1024 * 1024; // 10MB
        const fileNameElement = document.getElementById('fileName');
        const pdfPreviewContainer = document.getElementById('newPdfPreview');
        const pdfPreview = document.getElementById('pdfPreview');
        const uploadSuccess = document.getElementById('uploadSuccess');

        if (file) {
            if (file.size > maxSize) {
                alert('File vượt quá giới hạn 10MB');
                resetPdfUpload();
                return;
            }

            if (file.type !== 'application/pdf') {
                alert('Chỉ chấp nhận file PDF');
                resetPdfUpload();
                return;
            }

            fileNameElement.textContent = file.name;
            uploadSuccess.style.display = 'block';

            // Hiển thị xem trước PDF
            const fileURL = URL.createObjectURL(file);
            pdfPreview.src = fileURL;
            pdfPreviewContainer.style.display = 'block';
        }
    });

    function resetPdfUpload() {
        document.getElementById('pdfFile').value = '';
        document.getElementById('fileName').textContent = '';
        document.getElementById('uploadSuccess').style.display = 'none';
        document.getElementById('newPdfPreview').style.display = 'none';
        document.getElementById('pdfPreview').src = '';
    }

    // Xử lý drag and drop
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
            document.getElementById('pdfFile').files = e.dataTransfer.files;
            const event = new Event('change');
            document.getElementById('pdfFile').dispatchEvent(event);
        }
    });

    // Xử lý form submit
    document.getElementById('editPdfForm').addEventListener('submit', function() {
        const progressBar = document.getElementById('uploadProgress');
        const submitBtn = document.getElementById('pdfSubmitBtn');
        const uploadSuccess = document.getElementById('uploadSuccess');

        if (document.getElementById('pdfFile').files.length > 0) {
            progressBar.style.display = 'block';
            uploadSuccess.style.display = 'none';
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i> Đang tải lên...';

            const progressBarInner = progressBar.querySelector('.progress-bar');
            let progress = 0;
            const interval = setInterval(() => {
                progress += 5;
                progressBarInner.style.width = `${progress}%`;
                if (progress >= 100) clearInterval(interval);
            }, 300);
        }
    });

    // Xử lý chế độ xem toàn màn hình
    function toggleFullscreen(containerId) {
        const container = document.getElementById(containerId);
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

    // Hàm in PDF
    function printPdf(iframeId) {
        const iframe = document.getElementById(iframeId);
        iframe.contentWindow.print();
    }
</script>
</body>
</html> 
