<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách khóa học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.1) 0%, rgba(173, 216, 230, 0.1) 100%);
            min-height: 100vh;
        }

        .course-container {
            padding: 2rem 0;
        }

        .course-card {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.2) 0%, rgba(173, 216, 230, 0.2) 100%);
            height: 100%;
            transition: transform 0.3s ease;
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .course-card:hover {
            transform: translateY(-5px);
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.3) 0%, rgba(173, 216, 230, 0.3) 100%);
        }

        .card-img-container {
            position: relative;
            padding-top: 56.25%; /* Tỷ lệ 16:9 */
            overflow: hidden;
            border-radius: 10px 10px 0 0;
        }

        .card-img-top {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
        }

        .course-card:hover .card-img-top {
            transform: scale(1.05);
        }

        .status-badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.85rem;
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.3) 0%, rgba(173, 216, 230, 0.3) 100%);
            color: #212529;
            backdrop-filter: blur(5px);
        }

        .status-active {
            background: linear-gradient(135deg, rgba(40, 167, 69, 0.8) 0%, rgba(32, 134, 55, 0.8) 100%);
            color: white;
        }

        .status-inactive {
            background: linear-gradient(135deg, rgba(220, 53, 69, 0.8) 0%, rgba(189, 33, 48, 0.8) 100%);
            color: white;
        }

        .detail-btn {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.8) 0%, rgba(173, 216, 230, 0.8) 100%);
            border: none;
            transition: all 0.3s ease;
            color: #212529;
        }

        .detail-btn:hover {
            transform: translateY(-2px);
            background: linear-gradient(135deg, rgba(255, 192, 203, 1) 0%, rgba(173, 216, 230, 1) 100%);
            box-shadow: 0 5px 15px rgba(173, 216, 230, 0.4);
        }

        .price-tag {
            font-weight: bold;
            color: #dc3545;
        }

        .empty-state {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 249, 250, 0.9) 100%);
            text-align: center;
            padding: 3rem;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .empty-state-icon {
            color: rgba(173, 216, 230, 0.8);
            margin-bottom: 20px;
        }

        .course-stats {
            margin: 15px 0;
            color: #6c757d;
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.2) 0%, rgba(173, 216, 230, 0.2) 100%);
            padding: 10px;
            border-radius: 8px;
        }

        .original-price {
            text-decoration: line-through;
            color: #6c757d;
            font-size: 0.9em;
        }

        .card-link {
            text-decoration: none;
            color: inherit;
            display: block;
        }

        .card-link:hover {
            color: inherit;
        }

        .btn-primary {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.8) 0%, rgba(173, 216, 230, 0.8) 100%);
            border: none;
            color: #212529;
            box-shadow: 0 4px 15px rgba(173, 216, 230, 0.2);
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, rgba(255, 192, 203, 1) 0%, rgba(173, 216, 230, 1) 100%);
            transform: translateY(-2px);
            color: #212529;
        }

        .card-body {
            background: transparent;
            border-radius: 0 0 10px 10px;
        }

        .badge {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.3) 0%, rgba(173, 216, 230, 0.3) 100%);
            color: #212529;
            border: none;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="course-container container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold mb-0">Danh sách khóa học</h2>
        <a href="course?action=addForm" class="btn btn-primary px-4">
            <i class="fas fa-plus me-2"></i>Thêm khóa học
        </a>
    </div>

    <div class="row g-4">
        <c:forEach var="course" items="${courses}">
            <div class="col-lg-4 col-md-6">
                <a href="course-content?action=addContentView&courseID=${course.courseID}"
                   class="card-link">
                    <div class="course-card card">
                        <div class="card-img-container">
                            <img src="${course.courseImg}" class="card-img-top" alt="${course.courseTitle}">
                        </div>
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${course.courseTitle}</h5>
                            <p class="card-text">${course.courseDescription}</p>
                            <p class="card-text">Được tạo bởi: ${course.expert.fullName}</p>

                            <div class="course-stats">
                                <div class="d-flex align-items-center mb-2">
                                    <i class="fas fa-users me-2 text-primary"></i>
                                    <span>${course.learnersCount} học viên</span>
                                </div>
                                <div class="d-flex align-items-center mb-2">
                                    <i class="fas fa-star me-2 text-warning"></i>
                                    <span>
                                        <fmt:formatNumber value="${course.rating}" type="number" maxFractionDigits="1" />
                                        <small class="text-muted">(${course.ratingCount} đánh giá)</small>
                                    </span>
                                </div>
                            </div>

                            <div class="mt-auto">
                                <div class="d-flex align-items-center mb-2">
                                    <span class="badge bg-secondary me-2">${course.category.categoryName}</span>
                                    <span class="status-badge status-${course.status eq 'Active' || course.status eq 'active' ? 'active' : 'inactive'}">
                                        <c:choose>
                                            <c:when test="${fn:toLowerCase(course.status) eq 'active'}">Active</c:when>
                                            <c:otherwise>${course.status}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <span class="price-tag">
                                            <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="VNĐ"/>
                                        </span>
                                        <c:if test="${course.originalPrice > course.price}">
                                            <span class="original-price ms-2">
                                                <fmt:formatNumber value="${course.originalPrice}" type="currency" currencySymbol="VNĐ"/>
                                            </span>
                                        </c:if>
                                    </div>
                                    <a href="course?action=detail&courseID=${course.courseID}"
                                       class="btn detail-btn text-white"
                                       onclick="event.stopPropagation()">
                                        <i class="fas fa-info-circle me-2"></i>Xem chi tiết
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty courses}">
        <div class="empty-state">
            <div class="empty-state-icon">
                <i class="fas fa-book-open fa-3x"></i>
            </div>
            <h4 class="text-muted mb-3">Bạn chưa có khóa học nào</h4>
            <a href="addCourse.jsp" class="btn btn-primary px-4">
                <i class="fas fa-plus me-2"></i>Tạo khóa học đầu tiên
            </a>
        </div>
    </c:if>
</div>

<c:import url="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>