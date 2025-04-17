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
    <style>
        /* Styles riêng cho Course Content */
        .course-sidebar {
            position: fixed;
            left: 0;
            top: 56px;
            height: calc(100vh - 56px);
            width: 320px;
            background: #fff;
            box-shadow: 2px 0 8px rgba(0,0,0,0.1);
            z-index: 1000;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            overflow-y: auto;
            border-right: 1px solid #e0e0e0;
        }

        .sidebar-toggle-container {
            position: fixed;
            top: 56px;
            left: 0;
            height: 56px;
            z-index: 1001;
            background: #fff;
            border-bottom: 1px solid #e0e0e0;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            display: flex;
            align-items: center;
            width: 320px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }

        .sidebar-toggle-container.collapsed {
            width: auto;
            border-right: 1px solid #e0e0e0;
            box-shadow: 2px 0 4px rgba(0,0,0,0.08);
        }

        .sidebar-toggle {
            background: none;
            border: none;
            color: #1a73e8;
            font-size: 14px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 12px 16px;
            cursor: pointer;
            width: 100%;
            transition: all 0.2s ease;
            border-radius: 4px;
        }

        .sidebar-toggle i {
            margin-right: 8px;
        }

        .sidebar-toggle:hover {
            background-color: rgba(26, 115, 232, 0.08);
        }

        .sidebar-hidden {
            transform: translateX(-320px);
        }

        .course-content {
            margin-left: 320px;
            margin-top: 56px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            padding: 20px;
        }

        .course-content.full-width {
            margin-left: 0;
        }

        .hide-menu-text {
            font-weight: 500;
            color: #1a73e8;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 24px;
            height: 24px;
            border-radius: 50%;
            transition: all 0.2s ease;
        }

        .sidebar-toggle:hover .hide-menu-text {
            background-color: rgba(26, 115, 232, 0.12);
        }

        .section-title {
            padding: 16px 20px;
            font-weight: 600;
            border-bottom: 1px solid #e0e0e0;
            font-size: 1rem;
            color: #202124;
            background-color: #f8f9fa;
        }

        .lessons-list {
            padding: 8px 0;
        }

        .lesson-item {
            padding: 12px 20px;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            border-left: 3px solid transparent;
        }

        .lesson-item:hover {
            background-color: rgba(0,0,0,0.04);
        }

        .lesson-item.active {
            background-color: rgba(26, 115, 232, 0.08);
            border-left: 3px solid #1a73e8;
        }

        .lesson-item.completed {
            color: #5f6368;
        }

        .lesson-icon {
            margin-right: 12px;
            color: #5f6368;
            font-size: 1rem;
            width: 20px;
            text-align: center;
        }

        .lesson-item.active .lesson-icon {
            color: #1a73e8;
        }

        .content-duration {
            margin-left: auto;
            font-size: 0.85rem;
            color: #5f6368;
        }

        .progress-info {
            padding: 16px 20px;
            background-color: #f8f9fa;
            border-bottom: 1px solid #e0e0e0;
        }

        .progress-bar {
            height: 6px;
            background-color: #e0e0e0;
            border-radius: 3px;
            margin-top: 8px;
            overflow: hidden;
        }

        .progress-value {
            height: 100%;
            background-color: #1a73e8;
            border-radius: 3px;
            transition: width 0.3s ease;
        }

        /* Video container styles */
        .fixed-video-container {
            position: sticky;
            top: 76px;
            z-index: 100;
            margin-bottom: 24px;
            max-width: 854px;
            margin-left: auto;
            margin-right: auto;
        }

        /* Nút điều khiển video container */
        .video-controls-overlay {
            position: absolute;
            top: 0;
            right: 0;
            padding: 10px;
            z-index: 20;
            display: flex;
            gap: 10px;
        }

        .video-control-btn {
            background: rgba(0,0,0,0.5);
            border: none;
            color: white;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.2s ease;
        }

        .video-control-btn:hover {
            background: rgba(0,0,0,0.7);
            transform: scale(1.1);
        }

        /* Video Title */
        .video-title-overlay {
            position: absolute;
            top: 15px;
            left: 15px;
            color: white;
            font-size: 1.1rem;
            font-weight: 500;
            text-shadow: 0 1px 2px rgba(0,0,0,0.5);
            max-width: 80%;
            z-index: 10;
        }

        /* Video container khi phát hiện fullscreen */
        .fixed-video-container.expanded {
            position: fixed;
            top: 76px;
            left: 0;
            right: 0;
            bottom: 0;
            width: auto;
            height: calc(100vh - 76px);
            z-index: 1100;
            padding: 0;
            border-radius: 0;
            margin: 0;
        }

        .fixed-video-container.expanded .video-container {
            height: 100%;
            border-radius: 0;
        }

        .content-description {
            max-width: 854px;
            margin-left: auto;
            margin-right: auto;
        }

        @media (max-width: 768px) {
            .course-sidebar {
                transform: translateX(-320px);
            }
            .course-content {
                margin-left: 0;
            }
            .sidebar-visible {
                transform: translateX(0);
            }
            .sidebar-toggle-container {
                width: auto;
            }
        }
    </style>
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
                 onclick="loadContent(${content.courseContentID})">
                <i class="fas ${not empty content.media ? 'fa-play-circle' : 'fa-file-alt'} lesson-icon"></i>
                <span class="lesson-title">${content.title}</span>
                <c:if test="${content.completed}">
                    <i class="fas fa-check-circle ms-auto text-success"></i>
                </c:if>
                <c:if test="${not empty content.media && !content.completed}">
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

    <div class="fixed-video-container" id="videoContainer">
        <div class="video-container">
            <div class="video-title-overlay">${currentContent.title}</div>
            <div class="video-controls-overlay">
                <button class="video-control-btn" id="expandVideo" title="Mở rộng"><i class="fas fa-expand"></i></button>
            </div>
            <div class="video-wrapper">
                <iframe src="${currentContent.media}?enablejsapi=1" frameborder="0" allowfullscreen></iframe>
            </div>
        </div>
    </div>

    <div class="content-description">
        <h3>${currentContent.title}</h3>
        <p>${currentContent.description}</p>

        <c:if test="${not empty currentContent.assignment}">
            <div class="mt-3">
                <p><strong>Bài tập:</strong> ${currentContent.assignment.assignmentTitle}</p>
            </div>
        </c:if>

        <c:if test="${not empty currentContent.exam}">
            <div class="mt-3">
                <p><strong>Bài kiểm tra:</strong> ${currentContent.exam.examName}</p>
                <p>Loại: ${currentContent.exam.examType}</p>
            </div>
        </c:if>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
function loadContent(contentId) {
    window.location.href = 'learn-course?courseID=${course.courseID}&contentID=' + contentId;
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

$('.video-duration').each(function() {
    const videoSrc = $(this).data('video-src');
    const video = document.createElement('video');

    video.onloadedmetadata = function() {
        const durationSeconds = video.duration;
        const formattedDuration = formatTime(durationSeconds);
        $(this).text(formattedDuration);

        totalDurationSeconds += durationSeconds;
        processedVideos++;

        if (processedVideos === videosToProcess) {
            updateTotalDuration();
        }
    }.bind(this);

    video.onerror = function() {
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

// Cập nhật tiến độ khi video kết thúc
document.querySelector('iframe').addEventListener('ended', function() {
    $.ajax({
        url: 'learn-course',
        method: 'POST',
        data: {
            courseID: ${course.courseID},
            contentID: ${currentContent.courseContentID}
        },
        success: function(response) {
            if (response.success) {
                // Cập nhật UI
                $('.lesson-item[data-id="${currentContent.courseContentID}"]').addClass('completed');
                $('.progress-value').css('width', response.progress + '%');
                $('.progress-info span:last-child').text(response.progress + '%');
            }
        }
    });
});
</script>
<script>
    $(document).ready(function() {
        // Xử lý hiển thị thêm/ẩn nội dung
        $('#show-more-content').click(function() {
            $('.content-hidden').slideDown(300);
            $(this).addClass('d-none');
            $('#show-less-content').removeClass('d-none');
        });

        $('#show-less-content').click(function() {
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

            setTimeout(function() {
                messageDiv.fadeOut(300, function() {
                    $(this).remove();
                });
            }, 3000);
        }

        // Xử lý thời lượng video
        let totalDurationSeconds = 0;
        let videosToProcess = $('.course-video').length;
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

        $('.course-video').each(function(index) {
            const video = this;
            const videoIndex = $(this).data('index');

            $(video).on('loadedmetadata', function() {
                const durationSeconds = video.duration;
                const formattedDuration = formatTime(durationSeconds);

                $('[data-video-src="' + $(video).find('source').attr('src') + '"]').text(formattedDuration);

                totalDurationSeconds += durationSeconds;
                processedVideos++;

                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            });

            $(video).on('error', function() {
                $('[data-video-src="' + $(video).find('source').attr('src') + '"]').text("Không khả dụng");
                processedVideos++;

                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            });

            $(video).attr('preload', 'metadata');
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

        $('.rating-star').click(function() {
            const value = $(this).data('value');
            $('#ratingValue').val(value);
            $('.rating-star').each(function(i) {
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

        $loadMoreBtn.click(function() {
            $('.review-hidden').slideDown(300);
            $(this).addClass('d-none');
            $showLessBtn.removeClass('d-none');
        });

        $showLessBtn.click(function() {
            $reviewCards.slice(reviewsPerPage).slideUp(300);
            $(this).addClass('d-none');
            $loadMoreBtn.removeClass('d-none');

            // Scroll back to reviews section
            $('html, body').animate({
                scrollTop: $('.reviews-list').offset().top - 100
            }, 300);
        });

        // Xử lý toggle sidebar
        $('#sidebarToggle').click(function() {
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

        // Xử lý expand video
        $('#expandVideo').click(function() {
            $('.fixed-video-container').toggleClass('expanded');
            
            if ($('.fixed-video-container').hasClass('expanded')) {
                $(this).html('<i class="fas fa-compress"></i>');
                $(this).attr('title', 'Thu nhỏ');
            } else {
                $(this).html('<i class="fas fa-expand"></i>');
                $(this).attr('title', 'Mở rộng');
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
</script>
</body>
</html> 