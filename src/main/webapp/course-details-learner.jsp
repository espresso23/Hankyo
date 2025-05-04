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
    <style>
        :root {
            --primary-color: #ff6b8b;  /* Hồng đậm hơn */
            --primary-light: #a8d8ea;  /* Xanh nhạt dịu */
            --secondary-color: #ffd3b6; /* Màu cam nhạt */
            --success-color: #4cc9a7;  /* Xanh lá dịu */
            --danger-color: #ff6b6b;   /* Đỏ cam */
            --text-color: #4a4a4a;     /* Màu chữ tối hơn */
            --text-light: #6c757d;     /* Màu chữ phụ */
            --bg-light: #fff9fb;       /* Nền hồng nhạt */
        }

        body {
            background: linear-gradient(135deg, #fff9fb 0%, #f0f8ff 100%);
            color: var(--text-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
        }

        /* Header và tiêu đề */
        h1, h2, h3, h4, h5, h6 {
            color: #3d3d3d;
            font-weight: 600;
        }

        .display-5 {
            color: var(--primary-color);
            margin-bottom: 1rem;
        }

        /* Card styling */
        .card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 12px;
            border: none;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
        }

        /* Nút bấm */
        .btn {
            font-weight: 500;
            border-radius: 8px;
            padding: 10px 20px;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--primary-color), #ff8fab);
            border: none;
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, #ff5a7d, #ff7a95);
            transform: translateY(-2px);
        }

        .btn-success {
            background: linear-gradient(135deg, var(--success-color), #5fd8b9);
            border: none;
        }

        .btn-outline-primary {
            color: var(--primary-color);
            border-color: var(--primary-color);
        }

        .btn-outline-primary:hover {
            background-color: var(--primary-color);
            color: white;
        }

        /* Accordion cải tiến */
        .accordion-button {
            background-color: rgba(255, 255, 255, 0.9);
            font-weight: 500;
            color: var(--text-color);
        }

        .accordion-button:not(.collapsed) {
            background-color: rgba(255, 107, 139, 0.1);
            color: var(--primary-color);
        }

        .accordion-button:focus {
            box-shadow: none;
            border-color: rgba(0, 0, 0, 0.05);
        }

        /* Rating stars */
        .bi-star-fill.text-warning {
            color: #ffb347 !important; /* Màu cam nhạt */
        }

        /* Instructor card */
        .instructor-card {
            background: white;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
        }

        .instructor-avatar {
            border: 3px solid rgba(255, 107, 139, 0.2);
        }

        /* Course features */
        .course-features li {
            padding: 8px 0;
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
        }

        .course-features li:last-child {
            border-bottom: none;
        }

        /* Review cards */
        .review-card {
            border-left: 3px solid var(--primary-light);
            transition: all 0.3s ease;
        }

        .review-card:hover {
            transform: translateX(5px);
        }

        /* Price section */
        .current-price {
            color: var(--primary-color);
            font-size: 2rem;
        }

        .discount-badge {
            background: linear-gradient(135deg, var(--danger-color), #ff8a8a);
        }

        /* Video container */
        .video-container {
            border-radius: 8px;
            overflow: hidden;
            background: #f8f9fa;
        }

        /* Text contrast */
        .text-muted {
            color: var(--text-light) !important;
        }

        /* Badges */
        .badge {
            font-weight: 500;
            padding: 6px 12px;
        }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            .card {
                margin-bottom: 20px;
            }

            .instructor-info {
                flex-direction: column;
                text-align: center;
            }
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .card, .review-card, .accordion-item {
            animation: fadeIn 0.5s ease forwards;
        }

        /* Loading spinner */
        .spin {
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* Message notification */
        .message {
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        /* Progress bars */
        .progress {
            height: 8px;
            border-radius: 4px;
        }

        /* Content items */
        .content-icon {
            background-color: rgba(255, 107, 139, 0.1);
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
        }

        /* Footer adjustments */
        footer {
            background-color: rgba(0, 0, 0, 0.03);
            border-top: 1px solid rgba(0, 0, 0, 0.05);
        }
    </style>
</head>
<body>
    <c:import url="header.jsp"/>
    <div id="message-container"></div>

    <!-- Thêm jQuery local như fallback -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        // Fallback nếu CDN không load được
        if (typeof jQuery == 'undefined') {
            document.write('<script src="asset/js/jquery-3.6.0.min.js"><\/script>');
        }
    </script>

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

                <!-- Course Image với fallback cloud -->
                <div class="course-preview">
                    <img src="${empty course.courseImg ? 'https://images.unsplash.com/photo-1513258496099-48168024aec0?auto=format&fit=facearea&w=400&h=225&facepad=2' : course.courseImg}"
                         alt="Course Preview"
                         class="img-fluid"
                         onerror="this.src='https://images.unsplash.com/photo-1513258496099-48168024aec0?auto=format&fit=facearea&w=400&h=225&facepad=2'">
                </div>

                <!-- Instructor Info với fallback cloud -->
                <div class="instructor-section">
                    <h4 class="mb-3">Giảng viên</h4>
                    <div class="instructor-info">
                        <img src="${empty course.expert.avatar ? 'https://ui-avatars.com/api/?name=Expert&background=eee&color=888&size=128' : course.expert.avatar}"
                             alt="Expert"
                             class="instructor-avatar"
                             onerror="this.src='https://ui-avatars.com/api/?name=Expert&background=eee&color=888&size=128'">
                        <div class="instructor-details">
                            <h5>${course.expert.fullName}</h5>
                            <span class="certificate">${course.expert.certificate}</span>
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
                                        <div class="content-info">
                                            <div class="content-title">
                                                <div class="content-icon">
                                                    <c:choose>
                                                        <c:when test="${not empty content.media}">
                                                            <i class="bi bi-play-circle-fill"></i>
                                                        </c:when>
                                                        <c:when test="${not empty content.assignment}">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </c:when>
                                                        <c:when test="${not empty content.exam}">
                                                            <i class="bi bi-clipboard-check"></i>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="bi bi-file-text"></i>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <span>${content.title}</span>
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
                                        <p class="mb-3">${content.description}</p>

                                        <c:if test="${not empty content.media}">
                                            <div class="video-container">
                                                <video controls class="course-video" data-index="${status.index}">
                                                    <source src="${content.media}" type="video/mp4">
                                                    Trình duyệt của bạn không hỗ trợ video.
                                                </video>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty content.assignment || not empty content.exam}">
                                            <div class="content-meta">
                                                <c:if test="${not empty content.assignment}">
                                                    <div class="content-meta-item">
                                                        <i class="bi bi-pencil-square"></i>
                                                        <span>Bài tập: ${content.assignment.assignmentTitle}</span>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty content.exam}">
                                                    <div class="content-meta-item">
                                                        <i class="bi bi-clipboard-check"></i>
                                                        <span>Bài kiểm tra: ${content.exam.examName}</span>
                                                    </div>
                                                    <div class="content-meta-item">
                                                        <i class="bi bi-info-circle"></i>
                                                        <span>Loại: ${content.exam.examType}</span>
                                                    </div>
                                                </c:if>
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
                        <div class="price-section">
                            <div class="current-price">
                                <span class="currency">₫</span>
                                <fmt:formatNumber value="${course.price}" type="number" pattern="#,###"/>
                            </div>
                            <div class="mt-2">
                                <c:if test="${course.originalPrice > course.price}">
                                    <span class="original-price">
                                        <span class="currency">₫</span>
                                        <fmt:formatNumber value="${course.originalPrice}" type="number" pattern="#,###"/>
                                    </span>
                                    <span class="discount-badge">
                                        <c:set var="discountPercent" value="${((course.originalPrice - course.price) / course.originalPrice) * 100}" />
                                        <fmt:formatNumber value="${discountPercent}" type="number" maxFractionDigits="0"/>% giảm
                                    </span>
                                </c:if>
                            </div>
                        </div>

                        <c:choose>
                            <c:when test="${not empty sessionScope.learner && course.enrolled}">
                                <div class="d-flex gap-2 mb-3">
                                    <span class="btn btn-secondary w-50 disabled">
                                        <i class="bi bi-check-circle me-2"></i>Đã tham gia
                                    </span>
                                    <a href="learn-course?courseID=${course.courseID}" class="btn btn-success w-50">
                                        <i class="bi bi-play-fill me-2"></i>Vào học
                                    </a>
                                </div>
                            </c:when>
                            <c:when test="${not empty sessionScope.learner && course.purchased}">
                                <a href="learn-course?courseID=${course.courseID}" class="btn btn-success w-100 mb-3">
                                    <i class="bi bi-play-fill me-2"></i>Tham gia học
                                </a>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-primary w-100 mb-3 add-to-cart" data-course-id="${course.courseID}">
                                    <i class="bi bi-cart-plus me-2"></i>Thêm vào giỏ hàng
                                </button>
                            </c:otherwise>
                        </c:choose>

                        <ul class="course-features">
                            <li><i class="bi bi-infinity"></i>Truy cập trọn đời</li>
                            <li><i class="bi bi-headset"></i>Hỗ trợ 24/7</li>
                        </ul>

                        <div class="last-updated">
                            Cập nhật lần cuối: <fmt:formatDate value="${course.lastUpdated}" pattern="dd/MM/yyyy"/>
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
                                                     style="width: ${ratingPercentage * 100}%;">
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

                <!-- Reviews List với fallback cho avatar cloud -->
                <div class="reviews-list">
                    <c:set var="totalFeedbacks" value="${fn:length(feedbacks)}" />
                    <c:forEach items="${feedbacks}" var="feedback" varStatus="status">
                        <div class="card mb-3 review-card ${status.index >= 5 ? 'review-hidden' : ''}">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-3">
                                    <img src="${empty feedback.learner.avatar ? 'https://ui-avatars.com/api/?name=User&background=eee&color=888&size=128' : feedback.learner.avatar}"
                                         alt="User Avatar"
                                         class="rounded-circle me-3"
                                         width="50"
                                         height="50"
                                         onerror="this.src='https://ui-avatars.com/api/?name=User&background=eee&color=888&size=128'">
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
                const currentCourseID = ${course.courseID};

                if (rating == 0) {
                    showMessage('Vui lòng chọn số sao đánh giá', 'error');
                    return;
                }

                $.ajax({
                    url: 'course-feedback',
                    type: 'POST',
                    data: {
                        courseID: currentCourseID,
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
                    const currentCourseID = ${course.courseID};

                    button.html('<i class="bi bi-arrow-repeat spin me-2"></i>Đang xử lý...').prop('disabled', true);

                    $.ajax({
                        url: 'enroll-course',
                        type: 'POST',
                        data: {courseID: currentCourseID},
                        dataType: 'json',
                        success: function(response) {
                            if (response.success) {
                                showMessage(response.message, 'success');
                                button.addClass('enrolled');
                                // Chuyển hướng đến trang nội dung khóa học sau 1 giây
                                setTimeout(function() {
                                    window.location.href = 'learn-course?courseID=' + currentCourseID;
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