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
    <link rel="stylesheet" href="asset/css/courseView.css">
    <style>
        .course-card {
            transition: transform 0.3s;
            margin-bottom: 20px;
        }
        .course-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .progress {
            height: 10px;
        }

        .empty-state {
            text-align: center;
            padding: 50px 20px;
        }

        .empty-state i {
            font-size: 48px;
            color: #6c757d;
            margin-bottom: 20px;
        }

        .progress-bar {
            height: 5px;
            background-color: #e9ecef;
            border-radius: 3px;
            margin-top: 10px;
        }

        .progress-value {
            height: 100%;
            background-color: #28a745;
            border-radius: 3px;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="container my-4">
    <h2 class="mb-4">Khóa học của tôi</h2>

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
                            <img src="${course.courseImg}" class="card-img-top" alt="${course.courseTitle}">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">${course.courseTitle}</h5>
                                <p class="card-text">${course.courseDescription}</p>
                                <div class="mt-auto">
                                    <div class="d-flex align-items-center mb-2">
                                        <span class="badge bg-secondary me-2">${course.category.categoryName}</span>
                                        <span class="rating me-1">
                                                <fmt:formatNumber value="${course.rating}" type="number"
                                                                  maxFractionDigits="1"/>
                                            </span>
                                        <div class="stars me-1">
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star${i <= course.rating ? ' text-warning' : ' text-muted'}"></i>
                                            </c:forEach>
                                        </div>
                                        <span class="rating-count">(${course.ratingCount})</span>
                                    </div>

                                    <!-- Progress Bar -->
                                    <div class="progress-info mb-2">
                                        <div class="d-flex justify-content-between">
                                            <small>Tiến độ học tập</small>
                                            <small>${course.progress}%</small>
                                        </div>
                                        <div class="progress-bar">
                                            <div class="progress-value" style="width: ${course.progress}%"></div>
                                        </div>
                                    </div>

                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                                <span class="text-muted">
                                                    <i class="fas fa-users me-1"></i>${course.learnersCount} học viên
                                                </span>
                                        </div>
                                        <a href="course-content?courseID=${course.courseID}" class="btn btn-success">
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