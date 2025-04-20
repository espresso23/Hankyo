<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Nội Dung Khóa Học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="asset/css/addCourseContent.css">
</head>
<body>
<c:import url="header.jsp"/>
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
                                                        <c:if test="${not empty content.media}">
                                                            <a href="edit-video?contentID=${content.courseContentID}&courseID=${param.courseID}"
                                                               class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${not empty content.assignment}">
                                                            <a href="edit-assignment?action=getAssignment&assignmentID=${content.assignment.assignmentID}&courseID=${param.courseID}"
                                                               class="btn btn-sm btn-outline-secondary" title="Chỉnh sửa">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                        </c:if>
                                                        <button class="btn btn-sm btn-outline-danger delete-content"
                                                                content-id="${content.courseContentID}"
                                                                content-type="${not empty content.media ? 'video' : not empty content.assignment ? 'assignment' : 'exam'}"
                                                                assignment-id="${not empty content.assignment ? content.assignment.assignmentID : ''}"
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
                                                        <div class="video-container">
                                                            <video preload="metadata" muted playsinline>
                                                                <source src="${content.media}" type="video/mp4">
                                                                Trình duyệt của bạn không hỗ trợ video.
                                                            </video>
                                                            <div class="video-play-button" data-bs-toggle="modal" data-bs-target="#videoModal${content.courseContentID}">
                                                                <i class="fas fa-play"></i>
                                                            </div>
                                                        </div>
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
                                                        <c:when test="${not empty content.media}">
                                                            <span class="badge bg-info">
                                                                <i class="fas fa-video me-1"></i> Video
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${not empty content.assignment}">
                                                            <span class="badge bg-success">
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
            // Lấy giá trị từ thuộc tính HTML
            const contentId = this.getAttribute('content-id');
            const contentType = this.getAttribute('content-type');
            const assignmentId = this.getAttribute('assignment-id');

            // Log chi tiết để debug
            console.log('Bắt đầu quá trình xóa nội dung:');
            console.log('- Loại nội dung:', contentType);
            console.log('- ID nội dung:', contentId);
            console.log('- ID bài tập:', assignmentId || 'Không có');

            // Kiểm tra nếu contentId rỗng
            if (!contentId) {
                console.error('Lỗi: contentId đang rỗng!');
                Swal.fire({
                    title: 'Lỗi',
                    text: 'Không thể xác định ID nội dung cần xóa',
                    icon: 'error'
                });
                return;
            }

            // Tạo URL xóa với các giá trị đã log
            let deleteUrl = 'course-content?action=deleteContent';
            deleteUrl += '&contentID=' + contentId;
            deleteUrl += '&courseID=' + ${courseID};

            if (contentType === 'assignment' && assignmentId && assignmentId.trim() !== '') {
                deleteUrl += '&assignmentID=' + assignmentId;
            }

            console.log('URL xóa:', deleteUrl);

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
                    console.log('Người dùng đã xác nhận xóa');

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
                        Swal.fire({
                            title: 'Thành công',
                            text: 'Đã xóa nội dung thành công',
                            icon: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => {
                            console.log('Đang tải lại trang...');
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
                } else {
                    console.log('Người dùng đã hủy thao tác xóa');
                }
            });
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
</script>
</body>
</html>