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
</head>
<body>
<c:import url="header.jsp"/>

<div class="course-sidebar">
    <div class="section-title">
        <i class="fas fa-list-ul me-2"></i>Nội dung khóa học
    </div>
    <div class="progress-info p-3">
        <div class="d-flex justify-content-between align-items-center">
            <span>Tiến độ khóa học</span>
            <span>${courseProgress}%</span>
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
                ${content.title}
                <c:if test="${content.completed}">
                    <i class="fas fa-check-circle float-end text-success"></i>
                </c:if>
                <c:if test="${not empty content.media}">
                    <span class="content-duration">
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
            <span class="me-3"><i class="fas fa-user-graduate"></i>${course.learnersCount} học viên</span>
            <span class="me-3"><i class="fas fa-star"></i>${course.rating} (${course.ratingCount} đánh giá)</span>
            <i class="bi bi-clock me-1"></i> <span id="total-duration">Đang tính...</span>
        </div>
    </div>

    <div class="video-container">
        <iframe src="${currentContent.media}" frameborder="0" allowfullscreen></iframe>
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

    $('.total-duration').text(formattedTotalDuration);
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

        // Xử lý thêm vào giỏ hàng
        $('.add-to-cart').click(function(e) {
            e.preventDefault();
            e.stopPropagation();

            const button = $(this);
            const courseID = button.data('course-id');

            button.html('<i class="bi bi-arrow-repeat spin me-2"></i>Đang xử lý...').prop('disabled', true);

            $.ajax({
                url: 'cart/add',
                type: 'POST',
                data: {courseID: courseID},
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                },
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        updateCartCount(response.count);
                        showMessage('Đã thêm vào giỏ hàng thành công!', 'success');
                    } else {
                        showMessage(response.message || 'Có lỗi xảy ra', 'error');
                    }
                },
                error: function() {
                    showMessage('Lỗi kết nối', 'error');
                },
                complete: function() {
                    button.html('<i class="bi bi-cart-plus me-2"></i>Thêm vào giỏ hàng').prop('disabled', false);
                }
            });
        });

        function updateCartCount(count) {
            const $badge = $('.cart-badge');
            $badge.text(count).toggle(count > 0);
        }

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

        // Xử lý đánh giá sao
        $('.rating-star').hover(
            function() {
                const value = $(this).data('value');
                $('.rating-star').each(function(i) {
                    $(this).toggleClass('text-warning', i < value);
                });
            },
            function() {
                const currentValue = $('#ratingValue').val();
                $('.rating-star').each(function(i) {
                    $(this).toggleClass('text-warning', i < currentValue);
                });
            }
        );

        $('.rating-star').click(function() {
            const value = $(this).data('value');
            $('#ratingValue').val(value);
            $('.rating-star').each(function(i) {
                $(this).toggleClass('text-warning', i < value);
            });
        });

        // Xử lý gửi đánh giá
        $('#reviewForm').submit(function(e) {
            e.preventDefault();

            const rating = $('#ratingValue').val();
            const comment = $('#comment').val();
            const courseID = ${course.courseID};

            if (rating == 0) {
                showMessage('Vui lòng chọn số sao đánh giá', 'error');
                return;
            }

            $.ajax({
                url: 'course-feedback',
                type: 'POST',
                data: {
                    courseID: courseID,
                    rating: rating,
                    comment: comment
                },
                success: function(response) {
                    if (response.success) {
                        showMessage('Cảm ơn bạn đã đánh giá!', 'success');
                        $('#reviewForm')[0].reset();
                        $('#ratingValue').val(0);
                        $('.rating-star').removeClass('text-warning');
                        // Reload page to show new review
                        location.reload();
                    } else {
                        showMessage(response.message || 'Có lỗi xảy ra', 'error');
                    }
                },
                error: function() {
                    showMessage('Lỗi kết nối', 'error');
                }
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

        // Xử lý đăng ký khóa học
        $('.btn-success:contains("Tham gia học")').click(function(e) {
            if (!$(this).hasClass('enrolled')) {
                e.preventDefault();
                const button = $(this);
                const courseID = ${course.courseID};

                button.html('<i class="bi bi-arrow-repeat spin me-2"></i>Đang xử lý...').prop('disabled', true);

                $.ajax({
                    url: 'enroll-course',
                    type: 'POST',
                    data: {courseID: courseID},
                    dataType: 'json',
                    success: function(response) {
                        if (response.success) {
                            showMessage(response.message, 'success');
                            button.addClass('enrolled');
                            // Chuyển hướng đến trang nội dung khóa học sau 1 giây
                            setTimeout(function() {
                                window.location.href = 'course-content?courseID=' + courseID;
                            }, 1000);
                        } else {
                            showMessage(response.message, 'error');
                            if (response.message.includes('đăng nhập')) {
                                setTimeout(function() {
                                    window.location.href = 'login';
                                }, 1000);
                            }
                        }
                    },
                    error: function() {
                        showMessage('Lỗi kết nối', 'error');
                    },
                    complete: function() {
                        if (!button.hasClass('enrolled')) {
                            button.html('Tham gia học').prop('disabled', false);
                        }
                    }
                });
            }
        });
    });
</script>
</body>
</html> 