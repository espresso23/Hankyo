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
    <style>
        :root {
            --primary-pink: #ff9bb3; /* Soft pastel pink */
            --light-pink: #ffd6e7; /* Very light pink */
            --soft-pink: #fff0f6; /* Almost white pink */
            --primary-blue: #89c4f4; /* Soft pastel blue */
            --light-blue: #e1f5fe; /* Very light blue */
            --dark-blue: #4b97e8; /* Slightly darker blue */
            --text-dark: #333;
            --text-medium: #555;
            --text-light: #777;
        }
    </style>
    <link rel="stylesheet" href="asset/css/learn-course.css">
    <link rel="stylesheet" href="asset/css/pdf-viewer.css">
    <link rel="stylesheet" href="asset/css/video-player.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.worker.min.js"></script>
</head>
<body>
<c:import url="header.jsp"/>

<!-- Main Container -->
<div class="main-content-wrapper">
    <!-- Sidebar Container -->
    <div class="course-sidebar" id="courseSidebar">
        <!-- Sidebar Header -->
        <div class="sidebar-header">
            <div class="d-flex align-items-center">
                <i class="fas fa-book me-2" style="color: var(--primary-pink);"></i>
                <span style="color: var(--primary-pink); font-weight: 600;">Nội dung khóa học</span>
            </div>
        </div>

        <!-- Toggle Button -->
        <button class="sidebar-toggle" id="sidebarToggle">
            <div class="toggle-icon">
                <i class="fas fa-chevron-left"></i>
            </div>
            <span class="toggle-text d-none">Hide menu</span>
        </button>

        <!-- Course Progress -->
        <div class="course-progress">
            <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 4px;">
                <span style="color: #888; font-size: 1rem;">Tiến độ hoàn thành</span>
                <div style="margin-left: auto; display: flex; align-items: baseline; gap: 8px;">
                    <span id="progress-percentage" class="progress-percentage" style="color: #888; font-size: 1rem;">${courseProgress}%</span>
                    <span id="progress-fraction" class="progress-fraction" style="color: #ff8fa3; font-weight: 600; font-size: 1rem;">
                        <c:set var="completedCount" value="${0}"/>
                        <c:forEach items="${courseContents}" var="content">
                            <c:if test="${content.completed}">
                                <c:set var="completedCount" value="${completedCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${completedCount}/${courseContents.size()} bài học
                    </span>
                </div>
            </div>
            <div style="background: #eee; border-radius: 8px; height: 8px; width: 100%; margin-top: 8px; overflow: hidden;">
                <div id="progress-bar" style="
                    height: 8px;
                    width: ${courseProgress}%;
                    background: linear-gradient(90deg, #ff8fa3, #6cb4ff);
                    border-radius: 8px;
                    transition: width 0.6s cubic-bezier(.4,2,.6,1);
                "></div>
            </div>
        </div>

        <!-- Lessons Container -->
        <div class="lessons-container">
            <div class="lessons-list">
                <c:forEach items="${courseContents}" var="content">
                    <div class="lesson-item ${content.courseContentID == currentContent.courseContentID ? 'active' : ''}
                          ${content.completed ? 'completed' : ''}"
                         data-content-id="${content.courseContentID}"
                         data-is-assignment="${not empty content.assignment}">

                        <div class="lesson-status">
                            <c:choose>
                                <c:when test="${content.completed}">
                                    <i class="fas fa-check-circle text-success"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="far fa-circle text-muted"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="lesson-content">
                            <div class="lesson-type">
                                <c:choose>
                                    <c:when test="${not empty content.media and !content.media.endsWith('.pdf')}">
                                        <i class="fas fa-play-circle text-primary"></i>
                                        <span>Video</span>
                                    </c:when>
                                    <c:when test="${not empty content.media and content.media.endsWith('.pdf')}">
                                        <i class="fas fa-book-reader text-info"></i>
                                        <span>Reading</span>
                                    </c:when>
                                    <c:when test="${not empty content.assignment}">
                                        <i class="fas fa-tasks text-warning"></i>
                                        <span>Quiz</span>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="lesson-title">${not empty content.assignment ? content.assignment.assignmentTitle : content.title}</div>
                            <div class="lesson-meta">
                                <c:if test="${not empty content.media && !content.media.endsWith('.pdf')}">
                                <span class="video-duration">
                                    <i class="far fa-clock"></i>
                                    <span data-video-src="${content.media}" data-content-id="${content.courseContentID}">--:--</span>
                                </span>
                                </c:if>
                                <c:if test="${not empty content.assignment}">
                                <span class="quiz-questions">
                                    <i class="fas fa-question-circle"></i>
                                    ${content.assignment.assignmentQuestions.size()} câu hỏi
                                </span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Course Content -->
    <div class="course-content">
        <!-- Course Header -->
        <div class="course-header">
            <h1 class="course-title">${course.courseTitle}</h1>
            <div class="course-meta">
                <span class="me-3"><i class="fas fa-user-graduate me-2"></i>${course.learnersCount} học viên</span>
                <span class="me-3"><i
                        class="fas fa-star me-2"></i>${course.rating} (${course.ratingCount} đánh giá)</span>
                <span><i class="fas fa-clock me-2"></i><span id="total-duration">Đang tính...</span></span>
            </div>
        </div>

        <!-- Content Area -->
        <div class="content-area">
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
                                    <p><i class="fas fa-list-ol me-2"></i>Số câu
                                        hỏi: ${not empty currentContent.assignment.assignmentQuestions ? currentContent.assignment.assignmentQuestions.size() : 0}
                                    </p>
                                    <p><i class="fas fa-clock me-2"></i>Thời gian làm bài: Không giới hạn</p>
                                    <p><i class="fas fa-check-circle me-2"></i>Yêu cầu: Trả lời đúng tất cả câu hỏi</p>
                                    <p><i class="fas fa-exclamation-circle me-2"></i>Lưu ý: Bạn chỉ có thể nộp bài một lần</p>
                                </div>

                                <div class="assignment-actions">
                                    <c:choose>
                                        <c:when test="${currentContent.completed && ((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) >= 80}">
                                            <div class="assignment-result">
                                                <div class="result-header mb-4">
                                                    <div class="d-flex align-items-center gap-2 text-success">
                                                        <i class="fas fa-check-circle fs-4"></i>
                                                        <h3 class="mb-0">Bài tập đã hoàn thành</h3>
                                                    </div>
                                                    <p class="text-muted mt-2 mb-0">
                                                        <i class="fas fa-clock me-2"></i>Lần làm gần nhất:
                                                        <fmt:formatDate value="${latestTaken.dateCreated}"
                                                                        pattern="dd/MM/yyyy HH:mm"/>
                                                    </p>
                                                </div>

                                                <c:if test="${not empty assignmentResult}">
                                                    <div class="result-overview ${((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) >= 80 ? 'bg-success-subtle' : 'bg-warning-subtle'} rounded-3 p-4">
                                                        <div class="mb-4">
                                                            <h5 class="mb-2">Điểm của bạn</h5>
                                                            <p class="text-muted mb-3">Để pass bạn cần đạt ít nhất 80% số điểm.
                                                                Chúng tôi sẽ lưu giữ số điểm cao nhất của bạn</p>

                                                            <div class="d-flex align-items-baseline">
                                                                <h1 class="display-4 mb-0 fw-bold me-2 ${((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) >= 80 ? 'text-success' : 'text-warning'}">
                                                                    <fmt:formatNumber
                                                                            value="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}"
                                                                            maxFractionDigits="0"/>%
                                                                </h1>
                                                                <span class="text-muted ms-2">(${assignmentResult.correctCount}/${assignmentResult.totalQuestions})</span>
                                                            </div>
                                                        </div>

                                                        <div class="d-flex gap-3 justify-content-center">
                                                            <a href="view-assignment-result?assignmentID=${currentContent.assignment.assignmentID}&courseID=${course.courseID}&courseContentID=${currentContent.courseContentID}"
                                                               class="btn btn-primary">
                                                                Xem bài làm
                                                            </a>
                                                            <button class="btn btn-primary"
                                                                    onclick="startAssignment('${currentContent.assignment.assignmentID}')">
                                                                Làm lại
                                                            </button>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:when>
                                        <c:when test="${currentContent.completed && ((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) < 80}">
                                            <div class="assignment-result">
                                                <div class="result-header mb-4">
                                                    <div class="d-flex align-items-center gap-2 text-warning">
                                                        <i class="fas fa-exclamation-circle fs-4"></i>
                                                        <h3 class="mb-0">Bài tập chưa đạt yêu cầu</h3>
                                                    </div>
                                                    <p class="text-muted mt-2 mb-0">
                                                        <i class="fas fa-clock me-2"></i>Lần làm gần nhất:
                                                        <fmt:formatDate value="${latestTaken.dateCreated}"
                                                                        pattern="dd/MM/yyyy HH:mm"/>
                                                    </p>
                                                </div>

                                                <c:if test="${not empty assignmentResult}">
                                                    <div class="result-overview ${((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) >= 80 ? 'bg-success-subtle' : 'bg-warning-subtle'} rounded-3 p-4">
                                                        <div class="mb-4">
                                                            <h4 class="mb-2">Điểm của bạn</h4>
                                                            <p class="text-muted mb-3">Để pass bạn cần đạt ít nhất 80% số điểm.
                                                                Chúng tôi sẽ lưu giữ số điểm cao nhất của bạn</p>

                                                            <div class="d-flex align-items-baseline">
                                                                <h1 class="display-4 mb-0 fw-bold me-2 ${((assignmentResult.correctCount / assignmentResult.totalQuestions) * 100) >= 80 ? 'text-success' : 'text-warning'}">
                                                                    <fmt:formatNumber
                                                                            value="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}"
                                                                            maxFractionDigits="0"/>%
                                                                </h1>
                                                                <span class="text-muted ms-2">(${assignmentResult.correctCount}/${assignmentResult.totalQuestions})</span>
                                                            </div>
                                                        </div>

                                                        <div class="d-flex gap-3 justify-content-center">
                                                            <a href="view-assignment-result?assignmentID=${currentContent.assignment.assignmentID}&courseID=${course.courseID}&courseContentID=${currentContent.courseContentID}"
                                                               class="btn btn-primary">
                                                                Xem bài làm
                                                            </a>
                                                            <button class="btn btn-primary"
                                                                    onclick="startAssignment('${currentContent.assignment.assignmentID}')">
                                                                Làm lại
                                                            </button>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-primary start-assignment-btn"
                                                    data-assignment-id="${currentContent.assignment.assignmentID}">
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
                        <div class="pdf-toolbar">
                            <div class="toolbar-left">
                                <button class="toolbar-btn prev-page" title="Trang trước">
                                    <i class="fas fa-chevron-left"></i>
                                </button>
                                <div class="page-info">
                                    <span class="current-page">1</span>
                                    <span>/</span>
                                    <span class="total-pages">1</span>
                                </div>
                                <button class="toolbar-btn next-page" title="Trang sau">
                                    <i class="fas fa-chevron-right"></i>
                                </button>
                            </div>

                            <div class="toolbar-center">
                                <button class="toolbar-btn zoom-out" title="Thu nhỏ">
                                    <i class="fas fa-search-minus"></i>
                                </button>
                                <span class="zoom-level">100%</span>
                                <button class="toolbar-btn zoom-in" title="Phóng to">
                                    <i class="fas fa-search-plus"></i>
                                </button>
                            </div>

                            <div class="toolbar-right">
                                <div class="search-wrapper">
                                    <div class="search-input-wrapper">
                                        <i class="fas fa-search search-icon"></i>
                                        <input type="text" class="search-input"
                                               placeholder="Tìm kiếm trong tài liệu...">
                                        <button class="clear-search" title="Xóa">
                                            <i class="fas fa-times"></i>
                                        </button>
                                    </div>
                                    <div class="search-results">
                                        <div class="results-info">
                                            <span class="results-count">0 kết quả</span>
                                            <div class="results-navigation">
                                                <button class="nav-btn prev-result" disabled>
                                                    <i class="fas fa-chevron-up"></i>
                                                </button>
                                                <button class="nav-btn next-result" disabled>
                                                    <i class="fas fa-chevron-down"></i>
                                                </button>
                                            </div>
                                        </div>
                                        <div class="results-list"></div>
                                    </div>
                                </div>
                                <button class="toolbar-btn download-btn" title="Tải xuống">
                                    <i class="fas fa-download"></i>
                                </button>
                                <button class="toolbar-btn fullscreen-btn" title="Toàn màn hình">
                                    <i class="fas fa-expand"></i>
                                </button>
                            </div>
                        </div>

                        <div class="pdf-content">
                            <div class="pdf-viewer">
                                <canvas id="pdf-canvas"></canvas>
                            </div>
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
    </div>
</div>

<!-- Modal Chi tiết bài làm -->
<div class="modal fade" id="assignmentResultModal" tabindex="-1"
     aria-labelledby="assignmentResultModalLabel" aria-hidden="true">
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

<!-- Toast thông báo tiến độ -->
<div id="progress-toast" style="
    display:none;
    position: fixed;
    bottom: 40px;
    left: 50%;
    transform: translateX(-50%);
    background: #323232;
    color: #fff;
    padding: 16px 32px;
    border-radius: 8px;
    font-size: 1rem;
    z-index: 9999;
    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
    opacity: 0;
    transition: opacity 0.5s;
"></div>

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
            const videoSrc = $(this).find('span').data('video-src');
            const contentId = $(this).find('span').data('content-id');
            const video = document.createElement('video');
            const $durationElement = $(this).find('span');

            console.log('Processing video for content ID:', contentId);
            console.log('Video source:', videoSrc);

            // Kiểm tra đường dẫn video
            if (!videoSrc) {
                console.error('Video source is empty for content ID:', contentId);
                $durationElement.text("Không có video");
                processedVideos++;
                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
                return;
            }

            // Kiểm tra URL có phải là Cloudinary không
            if (!videoSrc.includes('cloudinary.com')) {
                console.error('Invalid video source for content ID:', contentId, videoSrc);
                $durationElement.text("Nguồn video không hợp lệ");
                processedVideos++;
                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
                return;
            }

            video.onloadedmetadata = function () {
                console.log('Video metadata loaded for content ID:', contentId);
                if (video.duration && !isNaN(video.duration)) {
                    const durationSeconds = video.duration;
                    const formattedDuration = formatTime(durationSeconds);
                    $durationElement.text(formattedDuration);
                    totalDurationSeconds += durationSeconds;
                } else {
                    console.error('Invalid video duration for content ID:', contentId, video.duration);
                    $durationElement.text("Không thể xác định");
                }
                processedVideos++;
                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            };

            video.onerror = function (e) {
                console.error('Error loading video for content ID:', contentId, e);
                $durationElement.text("Lỗi tải video");
                processedVideos++;
                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            };

            // Thêm crossOrigin để xử lý CORS
            video.crossOrigin = 'anonymous';
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
            
            // Đổi hướng icon khi sidebar ẩn/hiện
            if ($('#courseSidebar').hasClass('sidebar-hidden')) {
                $('.toggle-icon i').removeClass('fa-chevron-left').addClass('fa-chevron-right');
                $('.mobile-menu-btn').removeClass('d-none');
            } else {
                $('.toggle-icon i').removeClass('fa-chevron-right').addClass('fa-chevron-left');
            }
        });

        // Xử lý responsive
        function handleResize() {
            if (window.innerWidth < 768) {
                $('#courseSidebar').removeClass('sidebar-hidden').addClass('sidebar-visible');
                $('.course-content').addClass('full-width');
            } else {
                $('#courseSidebar').removeClass('sidebar-visible');
                if (!$('#courseSidebar').hasClass('sidebar-hidden')) {
                    $('.course-content').removeClass('full-width');
                }
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

    function showProgressToast(message, isSuccess) {
        const toast = document.getElementById('progress-toast');
        toast.textContent = message;
        toast.style.background = isSuccess ? '#28a745' : '#dc3545';
        toast.style.display = 'block';
        setTimeout(() => { toast.style.opacity = 1; }, 10);
        setTimeout(() => {
            toast.style.opacity = 0;
            setTimeout(() => { toast.style.display = 'none'; }, 500);
        }, 2500);
    }

    function updateProgressUI(newPercent, newCompleted, total) {
        // Cập nhật thanh tiến độ
        const bar = document.getElementById('progress-bar');
        bar.style.width = newPercent + '%';
        // Cập nhật số %
        document.getElementById('progress-percentage').textContent = newPercent + '%';
        // Cập nhật số bài học (luôn giữ đúng định dạng x/y bài học)
        document.getElementById('progress-fraction').textContent = `${newCompleted}/${total} bài học`;
    }

    // Lấy tổng số bài học từ JSP
    const totalLessons = ${courseContents.size()};

    // --- Ví dụ cập nhật tiến độ cho video ---
    document.addEventListener('DOMContentLoaded', function() {
        const video = document.getElementById('videoPlayer');
        if (video) {
            video.addEventListener('ended', function() {
                $.post('learn-course', {
                    courseID: '${course.courseID}',
                    contentID: '${currentContent.courseContentID}',
                    type: 'video',
                    watchedAll: true
                }, function(res) {
                    if (res.success) {
                        // Luôn tính lại số bài học hoàn thành
                        let completedLessons = Math.round(res.progress / 100 * totalLessons);
                        updateProgressUI(res.progress, completedLessons, totalLessons);
                        showProgressToast(res.message, true);
                    } else {
                        showProgressToast(res.message, false);
                    }
                }, 'json');
            });
        }

        // --- Cập nhật tiến độ khi đọc hết bài reading ---
        const pdfContainer = document.querySelector('.pdf-content');
        let sentReading = false;
        if (pdfContainer) {
            pdfContainer.addEventListener('scroll', function() {
                if (!sentReading && pdfContainer.scrollTop + pdfContainer.clientHeight >= pdfContainer.scrollHeight - 10) {
                    sentReading = true;
                    $.post('learn-course', {
                        courseID: '${course.courseID}',
                        contentID: '${currentContent.courseContentID}',
                        type: 'reading',
                        readAll: true
                    }, function(res) {
                        if (res.success) {
                            let completedLessons = Math.round(res.progress / 100 * totalLessons);
                            updateProgressUI(res.progress, completedLessons, totalLessons);
                            showProgressToast(res.message, true);
                        } else {
                            showProgressToast(res.message, false);
                        }
                    }, 'json');
                }
            });
        }
    });

    // --- Hàm cập nhật tiến độ cho assignment (gọi khi đạt điểm >80%) ---
    function updateProgressAssignment(assignmentID) {
        $.post('learn-course', {
            courseID: '${course.courseID}',
            contentID: '${currentContent.courseContentID}',
            type: 'assignment',
            assignmentID: assignmentID
        }, function(res) {
            if (res.success) {
                let completedLessons = Math.round(res.progress / 100 * totalLessons);
                updateProgressUI(res.progress, completedLessons, totalLessons);
                showProgressToast(res.message, true);
            } else {
                showProgressToast(res.message, false);
            }
        }, 'json');
    }
</script>
</body>
</html> 