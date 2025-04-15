<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách khóa học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="./asset/css/course.css">
</head>
<body>
<c:import url="header.jsp"/>

<div class="course-container container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold mb-0">Danh sách khóa học</h2>
        <a href="addCourse.jsp" class="btn btn-primary px-4">
            <i class="fas fa-plus me-2"></i>Thêm khóa học
        </a>
    </div>

    <div class="row g-4">
        <c:forEach var="course" items="${courses}">
            <div class="col-lg-4 col-md-6">
                <!-- Bao bọc toàn bộ card bằng thẻ a -->
                <a href="course-content?action=addContentView&courseID=${course.courseID}"
                   class="card-link"
                   style="text-decoration: none; color: inherit; display: block;">

                    <div class="course-card card">
                        <div class="card-img-container">
                            <img src="${course.courseImg}" class="card-img-top" alt="${course.courseTitle}">
                        </div>
                        <div class="card-body">
                            <h5 class="card-title">${course.courseTitle}</h5>
                            <p class="card-text">${course.courseDescription}</p>

                            <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="price-tag">
                                <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="VNĐ"/>
                            </span>
                                <span class="status-badge status-${course.status eq 'Active' ? 'active' : 'inactive'}">
                                        ${course.status}
                                </span>
                            </div>

                            <!-- Nút Xem chi tiết - cần ngăn sự kiện click lan ra thẻ a cha -->
                            <a href="course?action=detail&courseID=${course.courseID}"
                               class="btn detail-btn text-white"
                               onclick="event.stopPropagation()">
                                <i class="fas fa-info-circle me-2"></i>Xem chi tiết
                            </a>
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