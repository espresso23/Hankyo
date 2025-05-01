<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${course.courseTitle} - Hankyo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/css/learn-course.css">
    <link rel="stylesheet" href="asset/css/pdf-viewer.css">
    <link rel="stylesheet" href="asset/css/video-player.css">
</head>
<body>
<c:import url="header.jsp"/>

<div class="sidebar-toggle-container" id="sidebarToggleContainer">
    <button class="sidebar-toggle" id="sidebarToggle">
        <span class="hide-menu-text"><i class="fas fa-chevron-left"></i></span>
        <span class="d-none d-md-inline">Ẩn menu</span>
    </button>
</div>

<div class="course-sidebar" id="courseSidebar">
    <div class="section-title">
        <i class="fas fa-list-ul me-2"></i>Nội dung khóa học
    </div>
    <div class="progress-info">
        <div class="d-flex justify-content-between align-items-center">
            <span class="fw-medium">Tiến độ khóa học</span>
            <span class="fw-bold text-primary">${courseProgress}%</span>
        </div>
        <div class="progress-bar">
            <div class="progress-value" style="width: ${courseProgress}%"></div>
        </div>
    </div>
    <div class="lessons-list">
        <c:forEach items="${courseContents}" var="content">
            <div class="lesson-item ${content.courseContentID == currentContent.courseContentID ? 'active' : ''} ${content.completed ? 'completed' : ''}"
                 data-content-id="${content.courseContentID}"
                 data-is-assignment="${not empty content.assignment}">
                <i class="fas ${not empty content.assignment ? 'fa-file-alt' : (not empty content.media and content.media.endsWith('.pdf') ? 'fa-file-text' : (not empty content.media ? 'fa-play-circle' : 'fa-file-text'))} lesson-icon"></i>
                <span class="lesson-title">${not empty content.assignment ? content.assignment.assignmentTitle : content.title}</span>
                <c:if test="${content.completed}">
                    <i class="fas fa-check-circle ms-auto text-success"></i>
                </c:if>
                <c:if test="${not empty content.media && !content.media.endsWith('.pdf') && !content.completed}">
                    <span class="content-duration ms-auto">
                        <span class="video-duration" data-video-src="${content.media}">--:--</span>
                    </span>
                </c:if>
            </div>
        </c:forEach>
    </div>
</div>

<div class="course-content">
    <div class="course-header">
        <h1 class="course-title">${course.courseTitle}</h1>
        <div class="course-meta">
            <span class="me-3"><i class="fas fa-user-graduate me-2"></i>${course.learnersCount} học viên</span>
            <span class="me-3"><i class="fas fa-star me-2"></i>${course.rating} (${course.ratingCount} đánh giá)</span>
            <span><i class="fas fa-clock me-2"></i><span id="total-duration">Đang tính...</span></span>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty currentContent and not empty currentContent.assignment and currentContent.assignment.assignmentID > 0}">
            <div class="assignment-container">
                <div class="assignment-header">
                    <h2>${currentContent.assignment.assignmentTitle}</h2>
                    <p class="assignment-description">${currentContent.assignment.description}</p>
                </div>

                <div class="assignment-overview">
                    <div class="overview-card">
                        <h3><i class="fas fa-info-circle me-2"></i>Tổng quan bài tập</h3>
                        <div class="overview-content">
                            <p><i class="fas fa-list-ol me-2"></i>Số câu hỏi: ${not empty currentContent.assignment.assignmentQuestions ? currentContent.assignment.assignmentQuestions.size() : 0}</p>
                            <p><i class="fas fa-clock me-2"></i>Thời gian làm bài: Không giới hạn</p>
                            <p><i class="fas fa-check-circle me-2"></i>Yêu cầu: Trả lời đúng tất cả câu hỏi</p>
                            <p><i class="fas fa-exclamation-circle me-2"></i>Lưu ý: Bạn chỉ có thể nộp bài một lần</p>

                            <c:if test="${not empty currentContent.assignment.assignmentQuestions}">
                                <div class="mt-4">
                                    <h4 class="mb-3">Danh sách câu hỏi:</h4>
                                    <div class="list-group">
                                        <c:forEach items="${currentContent.assignment.assignmentQuestions}" var="question" varStatus="status">
                                            <div class="list-group-item">
                                                <div class="d-flex w-100 justify-content-between align-items-center">
                                                    <h5 class="mb-1">Câu ${status.index + 1} (${question.questionMark} điểm)</h5>
                                                    <div>
                                                        <c:if test="${not empty question.questionImg}">
                                                            <span class="badge bg-info me-1" title="Có hình ảnh"><i class="fas fa-image"></i></span>
                                                        </c:if>
                                                        <c:if test="${not empty question.audioFile}">
                                                            <span class="badge bg-warning" title="Có audio"><i class="fas fa-volume-up"></i></span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <p class="mb-1">${question.questionText}</p>
                                                <small class="text-muted">
                                                    <i class="fas fa-tag me-1"></i>${question.questionType}
                                                </small>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>
                        </div>

                        <div class="assignment-actions">
                            <c:choose>
                                <c:when test="${currentContent.completed}">
                                    <div class="assignment-result">
                                        <div class="result-header mb-4">
                                            <h3 class="text-success">
                                                <i class="fas fa-check-circle me-2"></i>
                                                Bài tập đã hoàn thành
                                            </h3>
                                            <p class="text-muted">
                                                <i class="fas fa-clock me-2"></i>Lần làm gần nhất:
                                                <fmt:formatDate value="${latestTaken.dateCreated}" pattern="dd/MM/yyyy HH:mm"/>
                                            </p>
                                        </div>

                                        <c:if test="${not empty assignmentResult}">
                                            <div class="result-stats">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="stats-card mb-3">
                                                            <div class="stats-value">
                                                                <h2 class="display-4 mb-0 text-primary">
                                                                    <fmt:formatNumber
                                                                            value="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}"
                                                                            maxFractionDigits="1"/>%
                                                                </h2>
                                                                <p class="text-muted mb-0">Tỷ lệ trả lời đúng</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="stats-card mb-3">
                                                            <div class="stats-value">
                                                                <h2 class="display-4 mb-0">
                                                                        ${assignmentResult.correctCount}/${assignmentResult.totalQuestions}
                                                                </h2>
                                                                <p class="text-muted mb-0">Số câu trả lời đúng</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="progress mb-4" style="height: 10px;">
                                                    <div class="progress-bar bg-success"
                                                         role="progressbar"
                                                         style="width: ${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}%"
                                                         aria-valuenow="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}"
                                                         aria-valuemin="0"
                                                         aria-valuemax="100">
                                                        <span class="visually-hidden">
                                                            ${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}% hoàn thành
                                                        </span>
                                                    </div>
                                                </div>

                                                <div class="result-details">
                                                    <div class="card">
                                                        <div class="card-body">
                                                            <h5 class="card-title mb-3">Chi tiết kết quả</h5>
                                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                                <span>Điểm số đạt được:</span>
                                                                <span class="fw-bold">${assignmentResult.score}/10</span>
                                                            </div>
                                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                                <span>Số câu trả lời đúng:</span>
                                                                <span class="fw-bold text-success">${assignmentResult.correctCount}</span>
                                                            </div>
                                                            <div class="d-flex justify-content-between align-items-center">
                                                                <span>Tổng số câu hỏi:</span>
                                                                <span class="fw-bold">${assignmentResult.totalQuestions}</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mt-4 d-flex gap-3 justify-content-center">
                                                    <a href="view-assignment-result?assignmentID=${currentContent.assignment.assignmentID}&courseID=${course.courseID}&courseContentID=${currentContent.courseContentID}"
                                                       class="btn btn-primary">
                                                        <i class="fas fa-eye me-2"></i>Xem chi tiết bài làm
                                                    </a>
                                                    <button class="btn btn-outline-primary" onclick="startAssignment('${currentContent.assignment.assignmentID}')">
                                                        <i class="fas fa-redo me-2"></i>Làm lại bài
                                                    </button>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-primary start-assignment-btn" data-assignment-id="${currentContent.assignment.assignmentID}">
                                        <i class="fas fa-play me-2"></i>Bắt đầu làm bài
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:when test="${not empty currentContent and not empty currentContent.media and !currentContent.media.endsWith('.pdf')}">
            <div class="fixed-video-container">
                <div class="video-container" id="videoContainer">
                    <video id="videoPlayer" class="video-player" preload="metadata">
                        <source src="${currentContent.media}" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>

                    <div class="center-play-pause">
                        <i class="fas fa-play"></i>
                    </div>

                    <div class="video-overlay">
                        <div class="video-overlay-content">
                            <button class="video-play-button">
                                <i class="fas fa-play"></i>
                            </button>
                            <h3 class="video-title">${currentContent.title}</h3>
                        </div>
                    </div>

                    <div class="video-controls-container">
                        <div class="video-progress">
                            <div class="video-progress-filled"></div>
                            <div class="video-progress-buffer"></div>
                        </div>

                        <div class="video-controls">
                            <div class="video-controls-left">
                                <button class="video-control-btn play-pause">
                                    <i class="fas fa-play"></i>
                                </button>

                                <button class="video-control-btn rewind-10s">
                                    <i class="fas fa-history"></i>
                                    <span>10</span>
                                </button>

                                <button class="video-control-btn forward-10s">
                                    <i class="fas fa-history fa-flip-horizontal"></i>
                                    <span>10</span>
                                </button>

                                <div class="video-time">
                                    <span class="current-time">0:00</span>
                                    <span class="time-separator">/</span>
                                    <span class="duration">0:00</span>
                                </div>
                            </div>

                            <div class="video-controls-right">
                                <div class="volume-container">
                                    <button class="video-control-btn volume">
                                        <i class="fas fa-volume-up"></i>
                                    </button>
                                    <div class="volume-slider">
                                        <div class="volume-slider-bar">
                                            <div class="volume-slider-fill"></div>
                                            <div class="volume-slider-handle"></div>
                                            <div class="volume-tooltip">100%</div>
                                        </div>
                                    </div>
                                </div>

                                <div class="speed-toggle">
                                    <button class="video-control-btn">
                                        <span class="speed-label">1x</span>
                                    </button>
                                    <div class="speed-menu">
                                        <button class="speed-item" data-speed="0.25">0.25x</button>
                                        <button class="speed-item" data-speed="0.5">0.5x</button>
                                        <button class="speed-item" data-speed="0.75">0.75x</button>
                                        <button class="speed-item active" data-speed="1">1x</button>
                                        <button class="speed-item" data-speed="1.25">1.25x</button>
                                        <button class="speed-item" data-speed="1.5">1.5x</button>
                                        <button class="speed-item" data-speed="1.75">1.75x</button>
                                        <button class="speed-item" data-speed="2">2x</button>
                                    </div>
                                </div>

                                <button class="video-control-btn fullscreen">
                                    <i class="fas fa-expand"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="content-description">
                <h3>${currentContent.title}</h3>
                <p>${currentContent.description}</p>
            </div>
        </c:when>
        <c:when test="${not empty currentContent}">
            <div class="pdf-container" id="pdfViewer_${currentContent.courseContentID}"
                 data-pdf-url="${currentContent.media}">
                <div class="pdf-header">
                    <h2 class="pdf-title">${currentContent.title}</h2>
                    <div class="pdf-controls">
                        <button class="pdf-control-btn zoom-in" title="Phóng to">
                            <i class="fas fa-search-plus"></i>
                        </button>
                        <button class="pdf-control-btn zoom-out" title="Thu nhỏ">
                            <i class="fas fa-search-minus"></i>
                        </button>
                        <button class="pdf-control-btn prev-page" title="Trang trước">
                            <i class="fas fa-chevron-left"></i>
                        </button>
                        <button class="pdf-control-btn next-page" title="Trang sau">
                            <i class="fas fa-chevron-right"></i>
                        </button>
                        <button class="pdf-control-btn highlight-btn" title="Đánh dấu">
                            <i class="fas fa-highlighter"></i>
                        </button>
                        <button class="pdf-control-btn note-btn" title="Ghi chú">
                            <i class="fas fa-sticky-note"></i>
                        </button>
                    </div>
                </div>

                <div class="pdf-viewer">
                    <div class="pdf-loading">
                        <div class="pdf-loading-spinner"></div>
                    </div>
                    <div class="pdf-search-container">
                        <input type="text" class="pdf-search-input" placeholder="Tìm kiếm trong tài liệu...">
                        <div class="pdf-search-results"></div>
                    </div>
                    <div class="pdf-annotation-toolbar">
                        <button class="pdf-annotation-btn highlight-btn">
                            <i class="fas fa-highlighter"></i> Đánh dấu
                        </button>
                        <button class="pdf-annotation-btn note-btn">
                            <i class="fas fa-sticky-note"></i> Ghi chú
                        </button>
                    </div>
                    <div class="pdf-page-controls">
                        <span class="pdf-page-info"></span>
                    </div>
                </div>

                <div class="pdf-metadata">
                    <div class="pdf-metadata-item">
                        <i class="fas fa-file-pdf"></i>
                        <span>PDF Document</span>
                    </div>
                    <div class="pdf-metadata-item">
                        <i class="fas fa-clock"></i>
                        <span>Thời gian đọc ước tính: 10 phút</span>
                    </div>
                    <div class="pdf-metadata-item">
                        <i class="fas fa-download"></i>
                        <a href="${currentContent.media}" download>Download PDF</a>
                    </div>
                </div>

                <div class="pdf-description">
                    <h3>Mô tả tài liệu</h3>
                    <p>${currentContent.description}</p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>Vui lòng chọn nội dung để xem
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Modal Chi tiết bài làm -->
<div class="modal fade" id="assignmentResultModal" tabindex="-1" aria-labelledby="assignmentResultModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="assignmentResultModalLabel">Chi tiết bài làm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="assignmentResultContent">
                    <!-- Nội dung chi tiết bài làm sẽ được load động -->
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
<script src="asset/js/pdf-viewer.js"></script>
<script src="asset/js/video-player.js"></script>
<script>
    $(document).ready(function () {
        // Xử lý click vào lesson item
        $('.lesson-item').click(function () {
            const contentId = $(this).data('content-id');
            const isAssignment = $(this).data('is-assignment');

            // Xây dựng URL với các tham số cần thiết
            let url = 'learn-course?courseID=${course.courseID}&courseContentID=' + contentId;

            // Thêm tham số type nếu là assignment
            if (isAssignment === 'true') {
                url += '&type=assignment';
            }
            console.log(url)

            // Chuyển hướng đến URL đã xây dựng
            window.location.href = url;
        });

        // Xử lý start assignment
        $('.start-assignment-btn').click(function () {
            const assignmentID = $(this).data('assignment-id');
            startAssignment(assignmentID);
        });

        // Xử lý view assignment result
        $('.view-result-btn').click(function () {
            const assignmentID = $(this).data('assignment-id');
            viewAssignmentResult(assignmentID);
        });

        // Xử lý hiển thị thêm/ẩn nội dung
        $('#show-more-content').click(function () {
            $('.content-hidden').slideDown(300);
            $(this).addClass('d-none');
            $('#show-less-content').removeClass('d-none');
        });

        $('#show-less-content').click(function () {
            $('.content-hidden').slideUp(300);
            $(this).addClass('d-none');
            $('#show-more-content').removeClass('d-none');
            $('html, body').animate({
                scrollTop: $('#courseContentAccordion').offset().top - 100
            }, 300);
        });

        function showMessage(message, type) {
            const messageDiv = $('<div>', {
                class: 'message ' + type,
                text: message
            });

            $('#message-container').append(messageDiv);

            setTimeout(function () {
                messageDiv.fadeOut(300, function () {
                    $(this).remove();
                });
            }, 3000);
        }
        // Xử lý thời lượng video
        let totalDurationSeconds = 0;
        let videosToProcess = $('.video-duration').length;
        let processedVideos = 0;

        function formatTime(seconds) {
            if (isNaN(seconds) || seconds < 0) return "--:--";

            const hours = Math.floor(seconds / 3600);
            const minutes = Math.floor((seconds % 3600) / 60);
            const remainingSeconds = Math.floor(seconds % 60);

            if (hours > 0) {
                return hours + "h " + minutes + "m";
            } else if (minutes > 0) {
                return minutes + "m " + remainingSeconds + "s";
            } else {
                return remainingSeconds + "s";
            }
        }

        if (videosToProcess === 0) {
            $('#total-duration').text('0 phút');
        }

        $('.video-duration').each(function () {
            const videoSrc = $(this).data('video-src');
            const video = document.createElement('video');

            video.onloadedmetadata = function () {
                const durationSeconds = video.duration;
                const formattedDuration = formatTime(durationSeconds);
                $(this).text(formattedDuration);

                totalDurationSeconds += durationSeconds;
                processedVideos++;

                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            }.bind(this);

            video.onerror = function () {
                $(this).text("Không khả dụng");
                processedVideos++;

                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            }.bind(this);

            video.src = videoSrc;
            video.preload = 'metadata';
        });

        function updateTotalDuration() {
            let formattedTotalDuration = formatTime(totalDurationSeconds);

            if (totalDurationSeconds >= 3600) {
                const hours = Math.floor(totalDurationSeconds / 3600);
                const minutes = Math.floor((totalDurationSeconds % 3600) / 60);
                formattedTotalDuration = hours + (hours > 1 ? " giờ " : " giờ ") +
                    (minutes > 0 ? (minutes + (minutes > 1 ? " phút" : " phút")) : "");
            } else if (totalDurationSeconds >= 60) {
                const minutes = Math.floor(totalDurationSeconds / 60);
                formattedTotalDuration = minutes + (minutes > 1 ? " phút" : " phút");
            } else {
                formattedTotalDuration = Math.floor(totalDurationSeconds) + " giây";
            }

            $('#total-duration').text(formattedTotalDuration);
        }

        $('.rating-star').click(function () {
            const value = $(this).data('value');
            $('#ratingValue').val(value);
            $('.rating-star').each(function (i) {
                $(this).toggleClass('text-warning', i < value);
            });
        });

        // Xử lý xem thêm đánh giá
        const reviewsPerPage = 5;
        const $reviewCards = $('.review-card');
        const $loadMoreBtn = $('#load-more-reviews');
        const $showLessBtn = $('#show-less-reviews');

        if ($reviewCards.length > reviewsPerPage) {
            $reviewCards.slice(reviewsPerPage).addClass('review-hidden');
        }

        $loadMoreBtn.click(function () {
            $('.review-hidden').slideDown(300);
            $(this).addClass('d-none');
            $showLessBtn.removeClass('d-none');
        });

        $showLessBtn.click(function () {
            $reviewCards.slice(reviewsPerPage).slideUp(300);
            $(this).addClass('d-none');
            $loadMoreBtn.removeClass('d-none');

            // Scroll back to reviews section
            $('html, body').animate({
                scrollTop: $('.reviews-list').offset().top - 100
            }, 300);
        });

        // Xử lý toggle sidebar
        $('#sidebarToggle').click(function () {
            $('#courseSidebar').toggleClass('sidebar-hidden');
            $('.course-content').toggleClass('full-width');
            $('#sidebarToggleContainer').toggleClass('collapsed');

            // Thay đổi icon và văn bản
            if ($('#courseSidebar').hasClass('sidebar-hidden')) {
                $('.hide-menu-text').html('<i class="fas fa-chevron-right"></i>');
                $('.d-none.d-md-inline').text('Hiện menu');
            } else {
                $('.hide-menu-text').html('<i class="fas fa-chevron-left"></i>');
                $('.d-none.d-md-inline').text('Ẩn menu');
            }
        });

        // Xử lý responsive
        function handleResize() {
            if (window.innerWidth < 768) {
                $('#courseSidebar').addClass('sidebar-hidden');
                $('.course-content').addClass('full-width');
                $('#sidebarToggleContainer').addClass('collapsed');
                $('.hide-menu-text').html('<i class="fas fa-chevron-right"></i>');
            }
        }

        // Gọi hàm khi resize
        $(window).resize(handleResize);
        handleResize(); // Gọi lần đầu khi load trang
    });

    function startAssignment(assignmentID) {
        const button = $('.start-assignment-btn');
        const originalText = button.html();

        console.log('=== DEBUG START ASSIGNMENT ===');
        console.log('assignmentID:', assignmentID);

        button.html('<i class="fas fa-spinner fa-spin me-2"></i>Đang tải...');
        button.prop('disabled', true);

        $.ajax({
            url: 'do-assignment',
            method: 'POST',
            data: {
                action: 'create',
                assignmentID: assignmentID
            },
            success: function (response) {
                console.log('=== DEBUG RESPONSE ===');
                console.log('Response:', response);

                if (response.success) {
                    window.location.href = 'do-assignment?assignmentID=' + assignmentID + '&assignTakenID=' + response.assignTakenID;
                } else {
                    button.html(originalText);
                    button.prop('disabled', false);
                    alert(response.message || 'Có lỗi xảy ra khi bắt đầu làm bài. Vui lòng thử lại.');
                }
            },
            error: function (xhr, status, error) {
                console.log('=== DEBUG ERROR ===');
                console.log('Error:', error);
                console.log('Status:', status);
                console.log('Response:', xhr.responseText);

                button.html(originalText);
                button.prop('disabled', false);
                alert('Có lỗi xảy ra khi kết nối đến máy chủ. Vui lòng thử lại.');
            }
        });
    }

    function viewAssignmentResult(assignmentID) {
        window.location.href = 'view-assignment-result?assignmentID=' + assignmentID;
    }

    document.addEventListener('DOMContentLoaded', () => {
        // Initialize video player
        const videoContainer = document.getElementById('videoContainer');
        if (videoContainer) {
            const player = new VideoPlayer('videoContainer');
            // Force show controls initially
            player.showControls();
            player.container.classList.remove('hide-controls');
        }
    });
</script>
</body>
</html> 