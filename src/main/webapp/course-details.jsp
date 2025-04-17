<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết khóa học</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="asset/css/courseDetails.css">
</head>
<body>
    <c:import url="header.jsp"/>
    <div id="message-container"></div>

    <div class="container my-4">
        <div class="row">
            <!-- Left Content -->
            <div class="col-lg-8">
                <h1 class="display-5 fw-bold">${course.courseTitle}</h1>
                <div class="d-flex align-items-center mb-3 text-muted">
                    <span class="badge bg-secondary me-2">${course.category.categoryName}</span>
                    <span class="me-3"><i class="bi bi-people"></i> ${course.learnersCount} học viên</span>
                    <span><i class="bi bi-star-fill text-warning"></i> ${course.rating} (${course.ratingCount} đánh giá)</span>
                </div>

                <!-- Course Image -->
                <div class="course-preview">
                    <img src="${course.courseImg}" alt="Course Preview" class="img-fluid">
                </div>

                <!-- Instructor Info -->
                <div class="mb-4 instructor-card">
                    <h4>Giảng viên</h4>
                    <div class="d-flex align-items-center">
                        <img src="${course.expert.avatar}" alt="Expert" class="rounded-circle me-3" width="70" height="70" style="object-fit: cover;">
                        <div>
                            <h5 class="mb-0">${course.expert.fullName}</h5>
                            <small class="text-muted">${course.expert.certificate}</small>
                        </div>
                    </div>
                </div>

                <!-- Course Description -->
                <div class="mb-4">
                    <h4>Mô tả khóa học</h4>
                    <div class="bg-light p-3 rounded">
                        <p>${course.courseDescription}</p>
                    </div>
                </div>

                <!-- Course Content -->
                <div class="mb-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h4>Nội dung khóa học</h4>
                        <div class="course-stats text-muted">
                            <i class="bi bi-collection-play me-1"></i> ${contents.size()} bài học
                            • <i class="bi bi-clock me-1"></i> <span id="total-duration">Đang tính...</span>
                        </div>
                    </div>

                    <div class="accordion" id="courseContentAccordion">
                        <c:forEach var="content" items="${contents}" varStatus="status">
                            <div class="accordion-item ${status.index >= 10 ? 'content-hidden' : ''}">
                                <h2 class="accordion-header" id="heading-${status.index}">
                                    <button class="accordion-button ${status.index != 0 ? 'collapsed' : ''}" type="button"
                                            data-bs-toggle="collapse" data-bs-target="#collapse-${status.index}"
                                            aria-expanded="${status.index == 0 ? 'true' : 'false'}"
                                            aria-controls="collapse-${status.index}">
                                        <div class="content-info d-flex w-100 align-items-center">
                                            <div>
                                                <c:choose>
                                                    <c:when test="${not empty content.media}">
                                                        <i class="bi bi-play-circle-fill content-icon"></i>
                                                    </c:when>
                                                    <c:when test="${not empty content.assignment}">
                                                        <i class="bi bi-pencil-square content-icon"></i>
                                                    </c:when>
                                                    <c:when test="${not empty content.exam}">
                                                        <i class="bi bi-clipboard-check content-icon"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="bi bi-file-text content-icon"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                                ${content.title}
                                            </div>
                                            <c:if test="${not empty content.media}">
                                                <span class="content-duration">
                                                    <span class="video-duration" data-video-src="${content.media}">--:--</span>
                                                </span>
                                            </c:if>
                                        </div>
                                    </button>
                                </h2>
                                <div id="collapse-${status.index}" class="accordion-collapse collapse ${status.index == 0 ? 'show' : ''}"
                                     aria-labelledby="heading-${status.index}" data-bs-parent="#courseContentAccordion">
                                    <div class="accordion-body">
                                        <p><strong>Mô tả:</strong> ${content.description}</p>

                                        <c:if test="${not empty content.media}">
                                            <p><strong>Video/Media:</strong></p>
                                            <div class="ratio ratio-16x9">
                                                <video controls class="course-video" data-index="${status.index}">
                                                    <source src="${content.media}" type="video/mp4">
                                                    Trình duyệt của bạn không hỗ trợ video.
                                                </video>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty content.assignment}">
                                            <div class="mt-3">
                                                <p><strong>Bài tập:</strong> ${content.assignment.assignmentTitle}</p>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty content.exam}">
                                            <div class="mt-3">
                                                <p><strong>Bài kiểm tra:</strong> ${content.exam.examName}</p>
                                                <p>Loại: ${content.exam.examType}</p>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <c:if test="${contents.size() > 10}">
                        <div class="text-center mt-3">
                            <button id="show-more-content" class="btn btn-outline-primary">
                                <i class="bi bi-plus-circle me-2"></i>Hiển thị tất cả ${contents.size()} bài học
                            </button>
                            <button id="show-less-content" class="btn btn-outline-secondary d-none">
                                <i class="bi bi-dash-circle me-2"></i>Thu gọn nội dung
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Right Sidebar -->
            <div class="col-lg-4">
                <div class="card sticky-top" style="top: 80px;">
                    <div class="card-body">
                        <img src="${course.courseImg}" alt="Course Thumbnail" class="img-fluid rounded mb-3">

                        <!-- Pricing -->
                        <div class="mb-3">
                            <h3 class="text-danger fw-bold"><fmt:formatNumber value="${course.price}" type="currency" currencySymbol="đ"/></h3>
                            <span class="text-muted text-decoration-line-through"><fmt:formatNumber value="${course.originalPrice}" type="currency" currencySymbol="đ"/></span>
                            <span class="badge bg-danger ms-2">Giảm giá</span>
                        </div>

                        <c:choose>
                            <c:when test="${not empty sessionScope.learner && course.enrolled}">
                                <div class="d-flex gap-2 mb-3">
                                    <span class="btn btn-secondary w-50 disabled">
                                        <i class="bi bi-check-circle me-2"></i>Đã tham gia
                                    </span>
                                    <a href="course-content?courseID=${course.courseID}" class="btn btn-success w-50">
                                        <i class="bi bi-play-fill me-2"></i>Vào học
                                    </a>
                                </div>
                            </c:when>
                            <c:when test="${not empty sessionScope.learner && course.purchased}">
                                <a href="course-content?courseID=${course.courseID}" class="btn btn-success w-100 mb-3">
                                    <i class="bi bi-play-fill me-2"></i>Tham gia học
                                </a>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-primary w-100 mb-3 add-to-cart" data-course-id="${course.courseID}">
                                    <i class="bi bi-cart-plus me-2"></i>Thêm vào giỏ hàng
                                </button>
                            </c:otherwise>
                        </c:choose>

                        <ul class="list-unstyled mb-3">
                            <li><i class="bi bi-check-circle-fill text-success me-2"></i>Truy cập trọn đời</li>
                            <li><i class="bi bi-check-circle-fill text-success me-2"></i>Hỗ trợ 24/7</li>
                        </ul>

                        <div class="text-muted small">
                            <p>Cập nhật lần cuối: <fmt:formatDate value="${course.lastUpdated}" pattern="dd/MM/yyyy"/></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Course Reviews Section -->
    <div class="container mb-5">
        <div class="row">
            <div class="col-lg-8">
                <h4 class="mb-4">Đánh giá và bình luận</h4>
                
                <!-- Rating Summary -->
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="row align-items-center">
                            <div class="col-md-4 text-center">
                                <h2 class="display-4 fw-bold text-warning mb-0">
                                    <fmt:formatNumber value="${course.rating}" pattern="#.#" />
                                </h2>
                                <div class="stars mb-2">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="bi bi-star-fill ${i <= course.rating ? 'text-warning' : 'text-muted'}"></i>
                                    </c:forEach>
                                </div>
                                <p class="text-muted mb-0">${course.ratingCount} đánh giá</p>
                            </div>
                            <div class="col-md-8">
                                <div class="rating-bars">
                                    <c:forEach begin="1" end="5" var="star">
                                        <div class="d-flex align-items-center mb-2">
                                            <div class="text-warning me-2" style="min-width: 70px;">
                                                ${6-star} <i class="bi bi-star-fill"></i>
                                            </div>
                                            <div class="progress flex-grow-1" style="height: 6px;">
                                                <c:set var="ratingPercentage" value="${course.ratingCount > 0 ? (course.ratingCount * (6-star) / course.ratingCount) : 0}" />
                                                <div class="progress-bar bg-warning" role="progressbar" 
                                                     style="width: ${ratingPercentage * 100}%">
                                                </div>
                                            </div>
                                            <span class="ms-2 text-muted small" style="min-width: 30px;">
                                                <fmt:formatNumber value="${ratingPercentage * course.ratingCount}" pattern="#" />
                                            </span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Add Review Form -->
                <c:if test="${not empty sessionScope.learner && course.enrolled}">
                    <div class="card mb-4">
                        <div class="card-body">
                            <h5 class="card-title">Viết đánh giá của bạn</h5>
                            <form id="reviewForm" class="review-form">
                                <div class="mb-3">
                                    <label class="form-label">Đánh giá của bạn</label>
                                    <div class="rating-input">
                                        <c:forEach begin="1" end="5" var="i">
                                            <i class="bi bi-star rating-star" data-value="${i}"></i>
                                        </c:forEach>
                                    </div>
                                    <input type="hidden" name="rating" id="ratingValue" value="0">
                                </div>
                                <div class="mb-3">
                                    <label for="comment" class="form-label">Bình luận</label>
                                    <textarea class="form-control" id="comment" name="comment" rows="3" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
                            </form>
                        </div>
                    </div>
                </c:if>

                <!-- Reviews List -->
                <div class="reviews-list">
                    <c:set var="totalFeedbacks" value="${fn:length(feedbacks)}" />
                    <c:forEach items="${feedbacks}" var="feedback" varStatus="status">
                        <div class="card mb-3 review-card ${status.index >= 5 ? 'review-hidden' : ''}">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-3">
                                    <img src="${feedback.learner.avatar}" alt="User Avatar" class="rounded-circle me-3" width="50" height="50">
                                    <div>
                                        <h6 class="mb-0">${feedback.learner.fullName}</h6>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${feedback.createdAt}" pattern="dd/MM/yyyy"/>
                                        </small>
                                    </div>
                                </div>
                                <div class="stars mb-2">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="bi bi-star-fill ${i <= feedback.rating ? 'text-warning' : 'text-muted'}"></i>
                                    </c:forEach>
                                </div>
                                <p class="mb-0">${feedback.comment}</p>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${totalFeedbacks > 5}">
                        <div class="text-center mt-4">
                            <button id="load-more-reviews" class="btn btn-outline-primary">
                                <i class="bi bi-plus-circle me-2"></i>Xem thêm đánh giá
                            </button>
                            <button id="show-less-reviews" class="btn btn-outline-secondary d-none">
                                <i class="bi bi-dash-circle me-2"></i>Thu gọn
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <c:import url="footer.jsp"/>

    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

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