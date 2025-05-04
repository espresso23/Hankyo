<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Media</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .media-preview {
            max-width: 150px;
            max-height: 150px;
            object-fit: contain;
        }
        .dropzone {
            border: 2px dashed #ccc;
            border-radius: 4px;
            padding: 20px;
            text-align: center;
            background: #f8f9fa;
            cursor: pointer;
        }
        .dropzone.dragover {
            background: #e9ecef;
            border-color: #0d6efd;
        }
        .media-item {
            border: 1px solid #dee2e6;
            border-radius: 4px;
            padding: 10px;
            margin-bottom: 15px;
        }
        .url-copy {
            cursor: pointer;
        }
        #uploadProgress {
            display: none;
            margin-top: 20px;
        }
        .progress {
            height: 25px;
            border-radius: 15px;
        }
    </style>
</head>
<body>
    <c:import url="header.jsp"/>

    <div class="container py-4">
        <div class="row mb-4">
            <div class="col">
                <h2><i class="fas fa-photo-video me-2"></i>Quản lý Media</h2>
                <p class="text-muted">Upload và quản lý hình ảnh, âm thanh cho câu hỏi</p>
            </div>
        </div>

        <!-- Upload Section -->
        <div class="row mb-4">
            <div class="col-md-8 mx-auto">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <i class="fas fa-upload me-2"></i>Upload Media
                    </div>
                    <div class="card-body">
                        <form id="uploadForm" enctype="multipart/form-data">
                            <div class="dropzone" id="dropzone">
                                <i class="fas fa-cloud-upload-alt fa-3x mb-3"></i>
                                <p class="mb-0">Kéo thả file vào đây hoặc click để chọn file</p>
                                <p class="text-muted small">Hỗ trợ: Images (PNG, JPG, GIF), Audio (MP3, WAV)</p>
                                <input type="file" id="fileInput" multiple accept="image/*,audio/*" style="display: none;">
                            </div>

                            <!-- Progress Bar -->
                            <div id="uploadProgress">
                                <div class="progress">
                                    <div class="progress-bar progress-bar-striped progress-bar-animated" 
                                         role="progressbar" style="width: 0%">0%</div>
                                </div>
                                <p id="uploadStatus" class="text-center mt-2"></p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Media List Section -->
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-list me-2"></i>Danh sách Media đã upload</span>
                        <span class="badge bg-light text-dark">${empty mediaList ? 0 : mediaList.size()} files</span>
                    </div>
                    <div class="card-body">
                        <div id="mediaList" class="row">
                            <c:if test="${empty mediaList}">
                                <div class="col-12 text-center text-muted">
                                    <i class="fas fa-inbox fa-3x mb-3"></i>
                                    <p>Chưa có media nào được upload</p>
                                </div>
                            </c:if>
                            <c:forEach items="${mediaList}" var="media">
                                <div class="col-md-4 mb-3">
                                    <div class="media-item">
                                        <div class="text-center mb-2">
                                            <c:choose>
                                                <c:when test="${media.type eq 'image'}">
                                                    <img src="<c:out value='${media.url}'/>" class="media-preview" alt="<c:out value='${media.fileName}'/>">
                                                </c:when>
                                                <c:otherwise>
                                                    <audio src="<c:out value='${media.url}'/>" controls class="w-100"></audio>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <small class="text-muted"><c:out value="${media.fileName}"/></small>
                                            <div>
                                                <button class="btn btn-sm btn-outline-primary url-copy" 
                                                        data-url="<c:out value='${media.url}'/>" title="Copy URL">
                                                    <i class="fas fa-copy"></i>
                                                </button>
                                                <button class="btn btn-sm btn-outline-danger delete-media" 
                                                        data-media-id="<c:out value='${media.mediaId}'/>" title="Delete">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </div>
                                        <div class="mt-1">
                                            <small class="text-muted">Upload: <c:out value="${media.uploadDate}"/></small>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
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
        $(document).ready(function() {
            const dropzone = $('#dropzone');
            const fileInput = $('#fileInput');
            const uploadProgress = $('#uploadProgress');
            const progressBar = $('.progress-bar');
            const uploadStatus = $('#uploadStatus');
            const mediaList = $('#mediaList');

            // Xử lý drag & drop
            dropzone.on('dragover', function(e) {
                e.preventDefault();
                e.stopPropagation();
                $(this).addClass('dragover');
            });

            dropzone.on('dragleave', function(e) {
                e.preventDefault();
                e.stopPropagation();
                $(this).removeClass('dragover');
            });

            dropzone.on('drop', function(e) {
                e.preventDefault();
                e.stopPropagation();
                $(this).removeClass('dragover');
                const files = e.originalEvent.dataTransfer.files;
                handleFiles(files);
            });

            // Sửa lại cách xử lý click trên dropzone
            $('#dropzone:not(input[type="file"])').on('click', function(e) {
                if (e.target === this) {
                    $('#fileInput')[0].click();
                }
            });

            // Xử lý sự kiện change của file input
            $('#fileInput').on('change', function(e) {
                const files = e.target.files;
                if (files && files.length > 0) {
                    handleFiles(files);
                }
            });

            function handleFiles(files) {
                if (!files || files.length === 0) return;

                const formData = new FormData();
                let validFiles = [];

                // Validate files
                Array.from(files).forEach(file => {
                    if (file.type.startsWith('image/') || file.type.startsWith('audio/')) {
                        if (file.size <= 10 * 1024 * 1024) { // 10MB limit
                            validFiles.push(file);
                            formData.append('files', file);
                        } else {
                            alert('File ' + file.name + ' quá lớn. Giới hạn 10MB.');
                        }
                    } else {
                        alert('File ' + file.name + ' không được hỗ trợ. Chỉ chấp nhận ảnh và âm thanh.');
                    }
                });

                if (validFiles.length === 0) return;

                // Show progress
                uploadProgress.show();
                progressBar.css('width', '0%').text('0%');
                uploadStatus.html('<i class="fas fa-spinner fa-spin"></i> Đang upload...');

                // Upload files
                $.ajax({
                    url: 'media-manager?action=upload',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    xhr: function() {
                        const xhr = new XMLHttpRequest();
                        xhr.upload.addEventListener('progress', function(e) {
                            if (e.lengthComputable) {
                                const percent = Math.round((e.loaded / e.total) * 100);
                                progressBar.css('width', percent + '%').text(percent + '%');
                            }
                        });
                        return xhr;
                    },
                    success: function(response) {
                        if (response.success) {
                            progressBar.removeClass('progress-bar-animated').addClass('bg-success');
                            uploadStatus.html('<i class="fas fa-check-circle text-success"></i> Upload thành công!');
                            
                            // Reset file input
                            $('#fileInput').val('');
                            
                            // Add new media items to list
                            if (response.files && Array.isArray(response.files)) {
                                response.files.forEach(function(file) {
                                    addMediaItem(file);
                                });
                            }
                        } else {
                            progressBar.addClass('bg-danger');
                            uploadStatus.html('<i class="fas fa-times-circle text-danger"></i> ' + (response.message || 'Upload thất bại'));
                        }
                    },
                    error: function(xhr, status, error) {
                        progressBar.addClass('bg-danger');
                        uploadStatus.html('<i class="fas fa-times-circle text-danger"></i> Lỗi: ' + error);
                    }
                });
            }

            function addMediaItem(file) {
                const mediaItem = $('<div class="col-md-4 mb-3">').html(
                    '<div class="media-item">' +
                        '<div class="text-center mb-2">' +
                        (file.type === 'image' 
                            ? '<img src="' + file.url + '" class="media-preview" alt="' + file.filename + '">'
                            : '<audio src="' + file.url + '" controls class="w-100"></audio>'
                        ) +
                        '</div>' +
                        '<div class="d-flex justify-content-between align-items-center">' +
                            '<small class="text-muted">' + file.filename + '</small>' +
                            '<button class="btn btn-sm btn-outline-primary url-copy" ' +
                                    'data-url="' + file.url + '" title="Copy URL">' +
                                '<i class="fas fa-copy"></i>' +
                            '</button>' +
                        '</div>' +
                    '</div>'
                );
                mediaList.prepend(mediaItem);
            }

            // Copy URL functionality
            $(document).on('click', '.url-copy', function() {
                const url = $(this).data('url');
                navigator.clipboard.writeText(url).then(function() {
                    const btn = $(this);
                    btn.tooltip('hide')
                      .attr('data-original-title', 'Copied!')
                      .tooltip('show');
                    
                    setTimeout(function() {
                        btn.tooltip('hide')
                           .attr('data-original-title', 'Copy URL');
                    }, 1000);
                }).catch(function(err) {
                    console.error('Could not copy text: ', err);
                });
            });

            // Delete media functionality
            $(document).on('click', '.delete-media', function() {
                if (confirm('Bạn có chắc chắn muốn xóa file này?')) {
                    const mediaId = $(this).data('media-id');
                    const mediaItem = $(this).closest('.col-md-4');
                    
                    $.ajax({
                        url: 'media-manager?action=delete',
                        type: 'POST',
                        data: { mediaId: mediaId },
                        success: function(response) {
                            if (response.success) {
                                mediaItem.fadeOut(300, function() { $(this).remove(); });
                            } else {
                                alert('Lỗi khi xóa: ' + response.message);
                            }
                        },
                        error: function(xhr, status, error) {
                            alert('Lỗi khi xóa: ' + error);
                        }
                    });
                }
            });

            // Initialize tooltips
            $('[data-bs-toggle="tooltip"]').tooltip();
        });
    </script>
</body>
</html> 
