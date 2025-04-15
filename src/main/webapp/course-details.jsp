<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <style>
        body {
            font-family: 'Helvetica Neue', sans-serif;
            background-color: #f9f9f9;
        }

        /* Course Image */
        .course-preview {
            position: relative;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            margin-bottom: 1.5rem;
        }

        .course-preview img {
            width: 100%;
            height: 400px;
            object-fit: cover;
            transition: transform 0.3s ease;
        }

        .course-preview:hover img {
            transform: scale(1.02);
        }

        /* Accordion improvements */
        .accordion-item {
            border: 1px solid rgba(0, 0, 0, 0.08);
            margin-bottom: 0.5rem;
            border-radius: 6px !important;
        }

        .accordion-button {
            font-weight: 500;
            padding: 1rem 1.25rem;
        }

        .accordion-button:not(.collapsed) {
            background-color: rgba(0, 123, 255, 0.05);
            color: #0d6efd;
        }

        /* Content type icons */
        .content-icon {
            width: 24px;
            height: 24px;
            margin-right: 10px;
            color: #6c757d;
        }

        .accordion-button:not(.collapsed) .content-icon {
            color: #0d6efd;
        }

        /* Instructor card */
        .instructor-card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 1.25rem;
        }

        /* Message container style */
        #message-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1100;
        }

        .message {
            padding: 10px 20px;
            margin: 5px 0;
            border-radius: 4px;
            color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }

        .message.success {
            background-color: #28a745;
        }

        .message.error {
            background-color: #dc3545;
        }

        /* Loading spinner */
        .spin {
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* Course content styles */
        .content-hidden {
            display: none;
        }

        .course-stats {
            font-size: 0.9rem;
            padding: 5px 10px;
            background-color: #f8f9fa;
            border-radius: 4px;
        }

        /* Nút hiển thị thêm */
        #show-more-content, #show-less-content {
            padding: 8px 16px;
            transition: all 0.2s ease;
        }

        /* Style mỗi phần content để thêm thời lượng */
        .content-duration {
            font-size: 0.85rem;
            color: #6c757d;
            margin-left: auto;
            padding-left: 10px;
        }

        /* Improved accordion appearance */
        .accordion-item {
            transition: all 0.2s ease;
        }

        .accordion-button .content-info {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 100%;
        }

        .accordion-button:hover {
            background-color: rgba(0, 123, 255, 0.03);
        }

        .content-duration {
            font-size: 0.85rem;
            color: #6c757d;
            margin-left: auto;
            padding-left: 15px;
            white-space: nowrap;
        }

        .content-info {
            display: flex;
            justify-content: space-between;
            width: 100%;
        }

        /* Style cho phần hiển thị tổng thời lượng */
        #total-duration {
            font-weight: 500;
        }

        /* Style cho loading indicator */
        #total-duration.loading:after {
            content: "";
            display: inline-block;
            width: 10px;
            height: 10px;
            border: 2px solid rgba(0, 0, 0, 0.1);
            border-radius: 50%;
            border-top-color: #0d6efd;
            animation: spin 1s ease-in-out infinite;
            margin-left: 5px;
            vertical-align: middle;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        /* Cải thiện hiển thị trên màn hình nhỏ */
        @media (max-width: 576px) {
            .course-stats {
                display: flex;
                flex-direction: column;
                align-items: flex-end;
            }

            .content-info {
                flex-wrap: wrap;
            }

            .content-duration {
                margin-top: 5px;
                margin-left: 25px;
                width: 100%;
            }
        }
    </style>
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
        });
    </script>
</body>
</html>