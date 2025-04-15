<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

        .course-preview img {
            width: 100%;
            max-height: 400px;
            object-fit: cover;
        }

        .card.sticky-top {
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
        }

        .list-unstyled li {
            margin-bottom: 0.5rem;
        }

        .badge.bg-danger {
            font-size: 0.9rem;
        }

        @media (max-width: 768px) {
            .card.sticky-top {
                position: relative !important;
                top: auto !important;
            }
        }
    </style>
</head>
<body>
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
            <div class="course-preview mb-4">
                <img src="${course.courseImg}" alt="Course Preview" class="img-fluid rounded">
            </div>

            <!-- Instructor Info -->
            <div class="mb-4">
                <h4>Giảng viên</h4>
                <div class="d-flex align-items-center">
                    <img src="${course.expert.avatar}" alt="Expert" class="rounded-circle me-3" width="70">
                    <div>
                        <h5 class="mb-0">${course.expert.fullName}</h5>
                        <small class="text-muted">${course.expert.certificate}</small>
                    </div>
                </div>
            </div>

            <!-- Course Description -->
            <div class="mb-4">
                <h4>Mô tả khóa học</h4>
                <p>${course.courseDescription}</p>
            </div>

            <!-- Course Content -->
            <!-- Course Content (Rendered via JSP) -->
            <div class="mb-4">
                <h4>Nội dung khóa học</h4>
                <div class="accordion" id="courseContentAccordion">
                    <c:forEach var="content" items="${contents}" varStatus="status">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="heading-${status.index}">
                                <button class="accordion-button ${status.index != 0 ? 'collapsed' : ''}" type="button"
                                        data-bs-toggle="collapse" data-bs-target="#collapse-${status.index}"
                                        aria-expanded="${status.index == 0 ? 'true' : 'false'}"
                                        aria-controls="collapse-${status.index}">
                                        ${content.title}
                                </button>
                            </h2>
                            <div id="collapse-${status.index}" class="accordion-collapse collapse ${status.index == 0 ? 'show' : ''}"
                                 aria-labelledby="heading-${status.index}" data-bs-parent="#courseContentAccordion">
                                <div class="accordion-body">
                                    <p><strong>Mô tả:</strong> ${content.description}</p>

                                    <c:if test="${not empty content.media}">
                                        <p><strong>Video/Media:</strong></p>
                                        <video controls width="50%">
                                            <source src="${content.media}" type="video/mp4">
                                            Trình duyệt của bạn không hỗ trợ video.
                                        </video>
                                    </c:if>

                                    <c:if test="${not empty content.assignment}">
                                        <div class="mt-3">
                                            <p><strong>Bài tập:</strong> ${content.assignment.name}</p>
                                            <p>${content.assignment.content}</p>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty content.exam}">
                                        <div class="mt-3">
                                            <p><strong>Bài kiểm tra:</strong> ${content.exam.name}</p>
                                            <p>Loại: ${content.exam.type}</p>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Right Sidebar -->
        <div class="col-lg-4">
            <div class="card sticky-top" style="top: 80px;">
                <div class="card-body">
                    <img src="${course.courseImg}" alt="Course Thumbnail" class="img-fluid rounded mb-3">

                    <!-- Pricing -->
                    <div class="mb-3">
                        <h3 class="text-danger fw-bold">${course.price}đ</h3>
                        <span class="text-muted text-decoration-line-through">${course.originalPrice}đ</span>
                        <span class="badge bg-danger ms-2">Giảm giá</span>
                    </div>

                    <button class="btn btn-primary w-100 mb-3">Đăng ký ngay</button>

                    <ul class="list-unstyled mb-3">
                        <li><i class="bi bi-check-circle-fill text-success me-2"></i>Truy cập trọn đời</li>
                        <li><i class="bi bi-check-circle-fill text-success me-2"></i>Chứng chỉ hoàn thành</li>
                        <li><i class="bi bi-check-circle-fill text-success me-2"></i>Hỗ trợ 24/7</li>
                    </ul>

                    <div class="text-muted small">
                        <p>Cập nhật lần cuối: ${course.lastUpdated}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript Libraries -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Course Content Rendering Script -->
</body>
</html>
