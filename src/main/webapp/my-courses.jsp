<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Khóa học của tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center my-4">Khóa học của tôi</h1>
        
        <div class="row">
            <c:forEach items="${purchasedCourses}" var="course">
                <div class="col-md-4">
                    <div class="card course-card">
                        <img src="${course.courseImg}" class="card-img-top" alt="${course.courseTitle}">
                        <div class="card-body">
                            <h5 class="card-title">${course.courseTitle}</h5>
                            <p class="card-text">${course.courseDescription}</p>
                            
                            <!-- Progress bar -->
                            <div class="mb-3">
                                <div class="d-flex justify-content-between mb-1">
                                    <span>Tiến độ học tập</span>
                                    <span>${course.progress}%</span>
                                </div>
                                <div class="progress">
                                    <div class="progress-bar" role="progressbar" style="width: ${course.progress}%"></div>
                                </div>
                            </div>
                            
                            <div class="d-flex justify-content-between">
                                <a href="course-detail?id=${course.courseID}" class="btn btn-primary">Tiếp tục học</a>
                                <span class="text-muted">Mua ngày: ${course.purchaseDate}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty purchasedCourses}">
            <div class="text-center my-5">
                <h3>Bạn chưa mua khóa học nào</h3>
                <a href="courses" class="btn btn-primary mt-3">Xem danh sách khóa học</a>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 