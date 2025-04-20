<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Danh sách khóa học</title>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
            rel="stylesheet">
        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
            rel="stylesheet">
        <link rel="stylesheet" href="asset/css/courseView.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <c:import url="header.jsp" />
        <div class="container">
            <!-- Category Navigation -->
            <div class="category-nav">
                <div class="d-flex">
                    <a href="courses"
                        class="nav-link ${empty param.categoryID ? 'active' : ''}">Tất
                        cả khóa học</a>
                    <c:forEach items="${categories}" var="category">
                        <a href="courses?categoryID=${category.categoryID}"
                            class="nav-link ${param.categoryID == category.categoryID ? 'active' : ''}">
                            ${category.categoryName}
                        </a>
                    </c:forEach>
                </div>
            </div>
            <div id="message-container"
                style="position: fixed; top: 20px; right: 20px; z-index: 1100;"></div>
            <c:if test="${not empty param.error}">
                <div class="alert alert-warning alert-dismissible fade show">
                    Khóa học đã có trong giỏ hàng!
                    <button type="button" class="btn-close"
                        data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <!-- Search and Filter Section -->
            <div class="filter-section mb-4">
                <form action="courses" method="get" class="row g-3">
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="search"
                            placeholder="Tìm kiếm khóa học..."
                            value="${param.search}">
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="priceRange">
                            <option value="">Khoảng giá</option>
                            <option value="0-1000000">Dưới 1 triệu</option>
                            <option value="1000000-3000000">1-3 triệu</option>
                            <option value="3000000-5000000">3-5 triệu</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fas fa-search me-2"></i>Tìm
                            kiếm</button>
                    </div>
                </form>
            </div>

            <!-- Course List -->
            <div class="row row-cols-1 row-cols-md-4 g-4">
                <c:forEach items="${courses}" var="course">
                    <div class="col">
                        <div class="card course-card h-100" data-id="${course.courseID}" onclick="window.location.href='course-details?courseID=${course.courseID}'">
                            <div class="card-img-container">
                                <img src="${course.courseImg}" class="card-img-top"
                                    alt="${course.courseTitle}">
                            </div>
                            <div class="card-body d-flex flex-column">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <h5 class="card-title text-truncate mb-0">${course.courseTitle}</h5>
                                </div>
                                <p class="card-text text-truncate">Được tạo bởi: ${course.expert.fullName}</p>
                                <div class="course-stats">
                                    <div class="d-flex align-items-center mb-1">
                                        <i class="fas fa-users me-2 text-primary"></i>
                                        <span>${course.learnersCount} học viên</span>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <div class="star-rating me-2">
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star ${i <= course.rating ? 'text-warning' : 'text-muted'}"></i>
                                            </c:forEach>
                                        </div>
                                        <span>
                                            <fmt:formatNumber value="${course.rating}" type="number" maxFractionDigits="1" />
                                            <small class="text-muted">(${course.ratingCount})</small>
                                        </span>
                                    </div>
                                </div>
                                <div class="mt-auto">
                                    <div class="d-flex align-items-center mb-2">
                                        <span class="badge bg-secondary me-2">${course.category.categoryName}</span>
                                        <span class="status-badge status-${course.status eq 'Active' ? 'active' : 'inactive'}">
                                            ${course.status}
                                        </span>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="price-container">
                                            <span class="price-tag">
                                                <fmt:formatNumber value="${course.price}" type="number"
                                                                  groupingUsed="true"/> VNĐ
                                            </span>
                                            <c:if test="${course.originalPrice > course.price}">
                                                <span class="original-price">
                                                    <fmt:formatNumber value="${course.originalPrice}" type="number"
                                                                      groupingUsed="true"/> VNĐ
                                                </span>
                                            </c:if>
                                        </div>
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.learner && course.purchased}">
                                                <a href="course-details?courseID=${course.courseID}"
                                                   class="btn btn-success btn-sm">Học ngay</a>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button"
                                                        class="btn btn-primary btn-sm add-to-cart"
                                                        data-course-id="${course.courseID}">
                                                    <i class="fas fa-cart-plus"></i>
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tooltip-content" data-course-id="${course.courseID}">
                            <div class="tooltip-header">
                                <h3>${course.courseTitle}</h3>
                                <div class="course-meta">
                                    <span><i
                                            class="far fa-calendar-alt"></i> Cập nhật tháng ${course.lastUpdated}</span>
                                </div>
                                <p class="course-description-text">${course.courseDescription}</p>
                            </div>
                            <div class="tooltip-body">
                                <ul>
                                    <li>
                                        <i class="fas fa-user-tie"></i>
                                        <span>Được tạo bởi <strong>${course.expert.fullName}</strong></span>
                                    </li>
                                    <li>
                                        <i class="fas fa-sync-alt"></i>
                                        <span>Nội dung cập nhật thường xuyên</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-mobile-alt"></i>
                                        <span>Học mọi lúc, mọi nơi trên mọi thiết bị</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-certificate"></i>
                                        <span>Cấp chứng chỉ sau khi hoàn thành</span>
                                    </li>
                                </ul>
                            </div>
                            <div class="tooltip-footer">
                                <div class="tooltip-price-container">
                                    <div class="tooltip-price">
                                        <fmt:formatNumber value="${course.price}" type="number"
                                                          groupingUsed="true"/> VNĐ
                                        <c:if test="${course.originalPrice > course.price}">
                                            <span class="tooltip-original-price">
                                                <fmt:formatNumber value="${course.originalPrice}" type="number"
                                                                  groupingUsed="true"/> VNĐ
                                            </span>
                                        </c:if>
                                    </div>
                                </div>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.learner && course.purchased}">
                                        <a href="course-details?courseID=${course.courseID}"
                                           class="tooltip-button start-learning">
                                            <i class="fas fa-play-circle"></i>
                                            Học ngay
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button"
                                                class="tooltip-button add-to-cart"
                                                data-course-id="${course.courseID}">
                                            <i class="fas fa-cart-plus"></i>
                                            Thêm vào giỏ
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Empty State -->
            <c:if test="${empty courses}">
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-book-open fa-3x"></i>
                    </div>
                    <h4 class="text-muted mb-3">Không tìm thấy khóa học nào</h4>
                </div>
            </c:if>
        </div>
        <c:import url="footer.jsp" />

        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const cards = document.querySelectorAll('.course-card');
            let currentTooltip = null;

            cards.forEach(card => {
                const courseId = card.getAttribute('data-id');
                const tooltip = document.querySelector(`.tooltip-content[data-course-id="${courseId}"]`);

                if (!tooltip) return;

                function updateTooltipPosition() {
                    const cardRect = card.getBoundingClientRect();
                    const tooltipRect = tooltip.getBoundingClientRect();
                    const viewportWidth = window.innerWidth;
                    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

                    // Luôn hiển thị tooltip bên phải
                    let tooltipLeft = cardRect.right + 20;
                    let tooltipTop = cardRect.top;

                    // Nếu tooltip sẽ bị tràn ra khỏi viewport bên phải
                    if (tooltipLeft + tooltipRect.width > viewportWidth) {
                        tooltipLeft = cardRect.left - tooltipRect.width - 20; // Hiển thị bên trái card
                    }

                    // Đảm bảo tooltip không bị tràn ra khỏi viewport phía trên và dưới
                    const maxTop = window.innerHeight - tooltipRect.height;
                    tooltipTop = Math.min(Math.max(0, tooltipTop), maxTop);

                    tooltip.style.left = `${tooltipLeft}px`;
                    tooltip.style.top = `${tooltipTop}px`;
                }

                function showTooltip() {
                    // Ẩn tooltip hiện tại nếu có
                    if (currentTooltip && currentTooltip !== tooltip) {
                        currentTooltip.classList.remove('show');
                    }

                    tooltip.style.display = 'block';
                    requestAnimationFrame(() => {
                        updateTooltipPosition();
                        tooltip.classList.add('show');
                    });

                    currentTooltip = tooltip;
                }

                function hideTooltip() {
                    if (currentTooltip) {
                        currentTooltip.classList.remove('show');
                        setTimeout(() => {
                            currentTooltip.style.display = 'none';
                        }, 300);
                    }
                }

                card.addEventListener('mouseenter', showTooltip);
                card.addEventListener('mouseleave', hideTooltip);
                tooltip.addEventListener('mouseenter', showTooltip);
                tooltip.addEventListener('mouseleave', hideTooltip);

                // Cập nhật vị trí tooltip khi scroll và resize
                ['scroll', 'resize'].forEach(event => {
                    window.addEventListener(event, () => {
                        if (tooltip.classList.contains('show')) {
                            updateTooltipPosition();
                        }
                    });
                });
            });
        });
    </script>
    <script>
    document.addEventListener('DOMContentLoaded', function () {
        // Tự động đóng thông báo sau 5 giây
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            setTimeout(() => {
                alert.classList.add('fade');
                setTimeout(() => alert.remove(), 150);
            }, 5000);
        });

        // Xử lý nút đóng thủ công
        document.querySelectorAll('.btn-close').forEach(btn => {
            btn.addEventListener('click', function () {
                const alert = this.closest('.alert');
                alert.classList.add('fade');
                setTimeout(() => alert.remove(), 150);
            });
        });
    });
</script>
    <script>
    $(document).ready(function () {
        $('body').on('click', '.add-to-cart', function (e) {
            e.preventDefault();
            e.stopPropagation();
            const button = $(this);
            const courseID = button.data('course-id');

            // Hiển thị trạng thái loading
            button.html('<i class="fas fa-spinner fa-spin"></i>').prop('disabled', true);

            $.ajax({
                url: 'cart/add',
                type: 'POST',
                data: {courseID: courseID},
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        // Cập nhật UI
                        updateCartCount(data.count);
                        showMessage('Đã thêm vào giỏ hàng thành công!', 'success');
                    } else {
                        showMessage(data.message || 'Có lỗi xảy ra', 'error');
                    }
                },
                error: function () {
                    showMessage('Lỗi kết nối', 'error');
                },
                complete: function () {
                    button.html('<i class="fas fa-cart-plus"></i>').prop('disabled', false);
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
            }).css({
                padding: '10px 20px',
                margin: '5px 0',
                borderRadius: '4px',
                color: 'white',
                backgroundColor: type === 'success' ? '#28a745' : '#dc3545',
                boxShadow: '0 2px 5px rgba(0,0,0,0.2)'
            });

            $('#message-container').append(messageDiv);

            // Tự động xóa message sau 3 giây
            setTimeout(function() {
                messageDiv.fadeOut(300, function() {
                    $(this).remove();
                });
            }, 3000);
        }

        // Xử lý đăng ký khóa học
        $('.btn-success:contains("Tham gia học")').click(function(e) {
            if (!$(this).hasClass('enrolled')) {
                e.preventDefault();
                const button = $(this);
                const courseID = button.data('course-id');

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
</html>