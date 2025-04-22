<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Khóa học của tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/css/myCourses.css">
</head>
<body>
<c:import url="header.jsp"/>

<div class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Khóa học của tui</h2>
    </div>

    <c:choose>
        <c:when test="${empty courses}">
            <div class="empty-state">
                <i class="fas fa-book-open"></i>
                <h3>Bạn chưa có khóa học nào</h3>
                <p class="text-muted">Hãy khám phá và đăng ký các khóa học mới!</p>
                <a href="courses" class="btn btn-primary mt-3">
                    <i class="fas fa-graduation-cap me-2"></i>Xem tất cả khóa học
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach items="${courses}" var="course">
                    <div class="col">
                        <div class="card course-card h-100">
                            <div class="card-img-container">
                                <img src="${course.courseImg}" class="card-img-top" alt="${course.courseTitle}">
                            </div>
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">${course.courseTitle}</h5>
                                <p class="card-text text-truncate">${course.courseDescription}</p>
                                
                                <div class="course-stats">
                                    <div class="d-flex align-items-center mb-2">
                                        <span class="badge bg-secondary me-2">${course.category.categoryName}</span>
                                        <div class="rating me-2">
                                            <fmt:formatNumber value="${course.rating}" type="number" maxFractionDigits="1"/>
                                        </div>
                                        <div class="stars me-2">
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star${i <= course.rating ? ' text-warning' : ' text-muted'}"></i>
                                            </c:forEach>
                                        </div>
                                        <span class="rating-count">(${course.ratingCount})</span>
                                    </div>

                                    <div class="progress-info">
                                        <div class="d-flex justify-content-between">
                                            <small>Tiến độ học tập</small>
                                            <small>${course.progress}%</small>
                                        </div>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" style="width: ${course.progress}%"></div>
                                        </div>
                                    </div>

                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                        <div class="text-muted">
                                            <i class="fas fa-users me-1"></i>${course.learnersCount} học viên
                                        </div>
                                        <a href="learn-course?courseID=${course.courseID}&contentID=${course.firstContentID}" 
                                           class="btn btn-success">
                                            <i class="fas fa-play me-2"></i>Tiếp tục học
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<c:import url="footer.jsp"/>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 