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
            <div class="filter-section">
                <form action="courses" method="get" class="row g-3">
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="search"
                            placeholder="Tìm kiếm khóa học..."
                            value="${param.search}">
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="priceRange">
                            <option value>Khoảng giá</option>
                            <option value="0-1000000">Dưới 1 triệu</option>
                            <option value="1000000-3000000">1-3 triệu</option>
                            <option value="3000000-5000000">3-5 triệu</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-primary w-100">Tìm
                            kiếm</button>
                    </div>
                </form>
            </div>

            <!-- Course List -->
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach items="${courses}" var="course">
                    <div class="col">
                        <div class="card course-card"
                            data-id="${course.courseID}">
                            <img src="${course.courseImg}" class="card-img-top"
                                alt="${course.courseTitle}">
                            <div class="card-body d-flex flex-column">
                                <h5
                                    class="card-title">${course.courseTitle}</h5>
                                <p
                                    class="card-text">${course.courseDescription}</p>
                                <p class="card-text">Được tạo bởi:
                                    ${course.expert.fullName}</p>
                                <div class="mt-auto">
                                    <div class="d-flex align-items-center mb-2">
                                        <span
                                            class="badge bg-secondary me-2">${course.category.categoryName}</span>
                                        <span class="rating me-1">
                                            <fmt:formatNumber
                                                value="${course.rating}"
                                                type="number"
                                                maxFractionDigits="1" />
                                        </span>

                                        <div class="stars me-1">
                                            <c:forEach begin="1" end="5"
                                                var="i">
                                                <i
                                                    class="fas fa-star${i <= course.rating ? ' text-warning' : ' text-muted'}"></i>
                                            </c:forEach>
                                        </div>
                                        <span
                                            class="rating-count">(${course.ratingCount})</span>
                                    </div>
                                    <p
                                        class="learners-count mb-2">${course.learnersCount}
                                        học viên</p>
                                    <div
                                        class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <span
                                                class="price-tag"><fmt:formatNumber
                                                    value="${course.price}"
                                                    type="number"
                                                    groupingUsed="true" />
                                                VNĐ</span>
                                            <c:if
                                                test="${course.originalPrice > course.price}">
                                                <span
                                                    class="original-price ms-2">${course.originalPrice}
                                                    VNĐ</span>
                                            </c:if>
                                        </div>
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.learner && course.purchased}">
                                                <a
                                                    href="course-details?courseID=${course.courseID}"
                                                    class="btn btn-success">Tham
                                                    gia học</a>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button"
                                                    class="btn btn-primary add-to-cart"
                                                    data-course-id="${course.courseID}">Thêm
                                                    vào giỏ hàng</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <c:import url="footer.jsp" />

        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    <script>
    document.addEventListener("DOMContentLoaded", () => {
        const LONG_PRESS_THRESHOLD = 500;

        document.querySelectorAll('.course-card').forEach(card => {
            let timer;

            card.addEventListener('mousedown', (e) => {
                // Bỏ qua nếu click vào nút thêm vào giỏ hàng
                if (e.target.closest('.add-to-cart')) {
                    return;
                }
                timer = setTimeout(() => {
                    timer = null;
                }, LONG_PRESS_THRESHOLD);
            });

            card.addEventListener('mouseup', (e) => {
                // Bỏ qua nếu click vào nút thêm vào giỏ hàng
                if (e.target.closest('.add-to-cart')) {
                    return;
                }
                if (timer) {
                    clearTimeout(timer);
                    const id = card.getAttribute('data-id');
                    window.location.href = 'course-details?courseID=' + id;
                }
            });

            card.addEventListener('mouseleave', () => clearTimeout(timer));
        });
    });
</script>
    // Thêm vào cuối trang courses.jsp
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
                    button.html('Thêm vào giỏ hàng').prop('disabled', false);
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