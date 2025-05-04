<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>Xem trước nội dung</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .preview-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .preview-header {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #dee2e6;
        }
        .preview-content {
            margin-bottom: 2rem;
        }
        .preview-footer {
            text-align: center;
            padding-top: 1rem;
            border-top: 1px solid #dee2e6;
        }
        .preview-badge {
            background-color: #28a745;
            color: white;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.8rem;
        }
    </style>
</head>
<body>
    <div class="preview-container">
        <div class="preview-header">
            <div class="d-flex justify-content-between align-items-center">
                <h2>${content.title}</h2>
                <span class="preview-badge">Xem trước</span>
            </div>
            <p class="text-muted">${content.description}</p>
        </div>

        <div class="preview-content">
            <c:if test="${not empty content.media}">
                <div class="ratio ratio-16x9 mb-4">
                    <video controls>
                        <source src="${content.media}" type="video/mp4">
                        Trình duyệt của bạn không hỗ trợ video.
                    </video>
                </div>
            </c:if>

            <c:if test="${not empty content.image}">
                <img src="${content.image}" alt="${content.title}" class="img-fluid mb-4">
            </c:if>

            <div class="content-text">
                ${content.content}
            </div>
        </div>

        <div class="preview-footer">
            <p class="text-muted">Đây là nội dung xem trước. Mua khóa học để xem toàn bộ nội dung.</p>
            <a href="course-details?courseID=${content.courseID}" class="btn btn-primary">Quay lại khóa học</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 
