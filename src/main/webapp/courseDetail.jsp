<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${course.courseTitle} - Chi tiết khóa học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.1) 0%, rgba(173, 216, 230, 0.1) 100%);
            min-height: 100vh;
        }

        .course-detail-header {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.2) 0%, rgba(173, 216, 230, 0.2) 100%);
            border-radius: 10px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .course-image-container {
            width: 100%;
            height: 300px;
            position: relative;
            background: rgba(255, 255, 255, 0.5);
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 1rem;
            padding: 1rem 0rem;
        }

        .course-image {
            width: 100%;
            height: 100%;
            object-fit: contain;
            display: block;
            border-radius: 8px;
            transition: transform 0.3s ease;
        }

        .course-image:hover {
            transform: scale(1.02);
        }

        .info-label {
            font-weight: 600;
            color: #495057;
        }

        .info-value {
            color: #212529;
            margin-bottom: 1rem;
        }

        .edit-form {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 249, 250, 0.9) 100%);
            border-radius: 10px;
            padding: 2rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .preview-image {
            max-height: 200px;
            object-fit: contain;
            margin-top: 10px;
            display: none;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .price-original {
            text-decoration: line-through;
            color: #6c757d;
            font-size: 0.9em;
        }

        .price-discount {
            color: #dc3545;
            font-weight: bold;
        }

        .stats-card {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.2) 0%, rgba(173, 216, 230, 0.2) 100%);
            border-radius: 10px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
            backdrop-filter: blur(10px);
        }

        .stats-card:hover {
            transform: translateY(-5px);
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.3) 0%, rgba(173, 216, 230, 0.3) 100%);
        }

        .stats-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: #0d6efd;
        }

        .stats-label {
            color: #6c757d;
            font-size: 0.9rem;
        }

        .btn-primary {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.8) 0%, rgba(173, 216, 230, 0.8) 100%);
            border: none;
            box-shadow: 0 4px 15px rgba(13, 110, 253, 0.2);
            color: #212529;
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, rgba(255, 192, 203, 1) 0%, rgba(173, 216, 230, 1) 100%);
            transform: translateY(-2px);
            color: #212529;
        }

        .btn-outline-secondary {
            border-color: rgba(173, 216, 230, 0.8);
            color: #212529;
        }

        .btn-outline-secondary:hover {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.8) 0%, rgba(173, 216, 230, 0.8) 100%);
            color: #212529;
            transform: translateY(-2px);
            border-color: transparent;
        }

        .card {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.2) 0%, rgba(173, 216, 230, 0.2) 100%);
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
            backdrop-filter: blur(10px);
        }

        .card:hover {
            transform: translateY(-5px);
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.3) 0%, rgba(173, 216, 230, 0.3) 100%);
        }

        .card-header {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.3) 0%, rgba(173, 216, 230, 0.3) 100%);
            border-bottom: none;
            border-radius: 10px 10px 0 0 !important;
        }

        .badge {
            padding: 0.5em 1em;
            font-weight: 500;
        }

        .badge.bg-success {
            background: linear-gradient(135deg, rgba(40, 167, 69, 0.8) 0%, rgba(32, 134, 55, 0.8) 100%) !important;
        }

        .badge.bg-secondary {
            background: linear-gradient(135deg, rgba(108, 117, 125, 0.8) 0%, rgba(73, 80, 87, 0.8) 100%) !important;
        }

        .modal-dialog {
            max-width: 1000px;
        }

        .modal-content {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(248, 249, 250, 0.95) 100%);
            border: none;
            border-radius: 15px;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .modal-header {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.2) 0%, rgba(173, 216, 230, 0.2) 100%);
            border-bottom: none;
            border-radius: 15px 15px 0 0;
            padding: 1.5rem 2rem;
        }

        .modal-header .modal-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #212529;
        }

        .modal-body {
            padding: 2rem;
        }

        .form-section {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(248, 249, 250, 0.8) 100%);
            border-radius: 10px;
            padding: 2rem;
            margin-bottom: 1.5rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .form-section-title {
            font-size: 1.1rem;
            font-weight: 600;
            color: #495057;
            margin-bottom: 2rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            padding-bottom: 0.75rem;
            border-bottom: 1px solid rgba(173, 216, 230, 0.2);
        }

        .form-floating {
            margin-bottom: 2rem;
            position: relative;
        }

        .form-floating:last-child {
            margin-bottom: 0;
        }

        .form-floating > .form-control,
        .form-floating > .form-select {
            height: 60px;
            padding: 1.5rem 1rem 0.5rem;
            font-size: 1rem;
            line-height: 1.25;
            border: 2px solid rgba(173, 216, 230, 0.2);
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.9);
        }

        .form-floating > textarea.form-control {
            height: 160px;
            padding-top: 2rem;
        }

        .form-floating > label {
            padding: 1rem;
            font-size: 0.9rem;
            color: #6c757d;
            transform-origin: 0 0;
            transition: opacity .15s ease-in-out, transform .15s ease-in-out;
        }

        .form-floating > .form-control:focus ~ label,
        .form-floating > .form-control:not(:placeholder-shown) ~ label,
        .form-floating > .form-select ~ label {
            transform: scale(0.85) translateY(-0.75rem);
            color: rgba(173, 216, 230, 0.8);
            background: transparent;
        }

        .form-floating > .form-control:focus,
        .form-floating > .form-select:focus {
            border-color: rgba(173, 216, 230, 0.8);
            box-shadow: 0 0 0 0.25rem rgba(173, 216, 230, 0.25);
            background: white;
        }

        .form-text {
            font-size: 0.85rem;
            color: #6c757d;
            margin-top: 0.5rem;
            margin-left: 0.25rem;
        }

        .row.g-4 {
            margin: 0;
        }

        .col-md-8 {
            padding: 0 1rem;
            width: 70%;
        }

        .col-md-4 {
            padding: 0 1rem;
            width: 30%;
        }

        .row.g-3 {
            margin: 0 -0.75rem;
        }

        .row.g-3 > .col-md-6 {
            padding: 0 0.75rem;
        }

        .image-upload-container {
            border: 2px dashed rgba(173, 216, 230, 0.4);
            border-radius: 10px;
            padding: 1.5rem;
            text-align: center;
            transition: all 0.3s ease;
            background: rgba(255, 255, 255, 0.5);
            cursor: pointer;
            min-height: 250px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 0.75rem;
        }

        .image-upload-container:hover {
            border-color: rgba(173, 216, 230, 0.8);
            background: rgba(255, 255, 255, 0.8);
        }

        .image-upload-icon {
            font-size: 2rem;
            color: rgba(173, 216, 230, 0.8);
            margin-bottom: 0.25rem;
        }

        .image-upload-text {
            color: #495057;
            margin-bottom: 0.25rem;
            font-size: 0.9rem;
        }

        .image-upload-hint {
            color: #6c757d;
            font-size: 0.8rem;
        }

        .preview-image {
            max-height: 200px;
            width: auto;
            object-fit: contain;
            margin-top: 0.75rem;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }

        .preview-image:hover {
            transform: scale(1.02);
        }

        .modal-footer {
            border-top: 1px solid rgba(173, 216, 230, 0.2);
            padding: 1.5rem 2rem;
            gap: 1rem;
            justify-content: flex-end;
        }

        .btn-save,
        .btn-cancel {
            min-width: 140px;
            padding: 0.75rem 1.5rem;
            font-size: 1rem;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            transition: all 0.3s ease;
        }

        .btn-save {
            background: linear-gradient(135deg, rgba(255, 192, 203, 0.8) 0%, rgba(173, 216, 230, 0.8) 100%);
            border: none;
            color: #212529;
        }

        .btn-save:hover {
            background: linear-gradient(135deg, rgba(255, 192, 203, 1) 0%, rgba(173, 216, 230, 1) 100%);
            transform: translateY(-2px);
        }

        .btn-cancel {
            background: transparent;
            border: 2px solid rgba(173, 216, 230, 0.4);
            color: #495057;
        }

        .btn-cancel:hover {
            background: rgba(173, 216, 230, 0.1);
            border-color: rgba(173, 216, 230, 0.8);
            transform: translateY(-2px);
        }

        @media (max-width: 767.98px) {
            .modal-dialog {
                margin: 0.5rem;
            }

            .form-section {
                padding: 1.5rem;
            }

            .col-md-8, .col-md-4 {
                width: 100%;
                padding: 0 0.5rem;
            }

            .course-image-container {
                height: 200px;
            }

            .preview-image {
                max-height: 150px;
            }

            .course-detail-header {
                padding: 1rem;
            }

            .course-image-container {
                margin-bottom: 1.5rem;
            }
        }

        .alert {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .alert-success {
            background: linear-gradient(135deg, rgba(209, 231, 221, 0.9) 0%, rgba(186, 219, 204, 0.9) 100%);
            color: #0f5132;
        }

        .alert-danger {
            background: linear-gradient(135deg, rgba(248, 215, 218, 0.9) 0%, rgba(241, 174, 181, 0.9) 100%);
            color: #842029;
        }

        .rating-overview {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 249, 250, 0.9) 100%);
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .rating-overview .display-4 {
            font-size: 3.5rem;
            background: linear-gradient(135deg, #0d6efd 0%, #0099ff 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .stars {
            font-size: 0.9rem;
        }

        .rating-bars {
            font-size: 0.85rem;
        }

        .rating-bar-container {
            margin-bottom: 0.75rem;
        }

        .progress {
            height: 8px;
            border-radius: 4px;
            background-color: rgba(0, 0, 0, 0.05);
        }

        .progress-bar {
            background: linear-gradient(135deg, #ffc107 0%, #ffdb4d 100%);
            border-radius: 4px;
            transition: width 0.6s ease;
        }

        .rating-count {
            font-size: 0.8rem;
            color: #6c757d;
        }

        .revenue-stats {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 249, 250, 0.9) 100%);
            border-radius: 15px;
            padding: 2rem;
        }

        .revenue-stats .display-6 {
            font-size: 2rem;
            background: linear-gradient(135deg, #198754 0%, #20c997 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .stat-card {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(248, 249, 250, 0.95) 100%);
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-3px);
        }

        .stat-card h6 {
            font-size: 0.8rem;
            color: #6c757d;
        }

        .stat-card h3 {
            font-size: 1.5rem;
            background: linear-gradient(135deg, #0d6efd 0%, #0099ff 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        /* Phần feedback list */
        .feedback-list {
            max-height: 600px;
            overflow-y: auto;
            padding-right: 1rem;
        }

        .feedback-list::-webkit-scrollbar {
            width: 6px;
        }

        .feedback-list::-webkit-scrollbar-track {
            background: rgba(0, 0, 0, 0.05);
            border-radius: 3px;
        }

        .feedback-list::-webkit-scrollbar-thumb {
            background: linear-gradient(135deg, rgba(13, 110, 253, 0.5) 0%, rgba(0, 153, 255, 0.5) 100%);
            border-radius: 3px;
        }

        .feedback-item {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(248, 249, 250, 0.8) 100%);
            border-radius: 12px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: 1px solid rgba(0, 0, 0, 0.05);
        }

        .feedback-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }

        .feedback-item .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid rgba(13, 110, 253, 0.2);
        }

        .feedback-item h6 {
            font-size: 0.9rem;
            color: #212529;
            margin-bottom: 0.25rem;
        }

        .feedback-item .stars {
            font-size: 0.8rem;
        }

        .feedback-item small {
            font-size: 0.75rem;
            color: #6c757d;
        }

        .feedback-comment {
            font-size: 0.85rem;
            color: #495057;
            line-height: 1.5;
            margin-top: 0.75rem;
        }

        .badge {
            font-size: 0.75rem;
            padding: 0.4em 0.8em;
            background: linear-gradient(135deg, #0d6efd 0%, #0099ff 100%);
        }

        .text-muted {
            font-size: 0.85rem;
        }

        @media (max-width: 767.98px) {
            .rating-overview .display-4 {
                font-size: 2.5rem;
            }

            .revenue-stats .display-6 {
                font-size: 1.75rem;
            }

            .stat-card h3 {
                font-size: 1.25rem;
            }

            .feedback-list {
                max-height: 400px;
            }

            .feedback-item {
                padding: 1rem;
            }
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="container my-5">
    <!-- Thông báo thành công/error -->
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="course-detail-header">
        <div class="row">
            <div class="col-md-6">
                <div class="course-image-container">
                    <img src="${course.courseImg}" alt="${course.courseTitle}" class="course-image">
                </div>
            </div>
            <div class="col-md-6">
                <h2 class="fw-bold mb-3">${course.courseTitle}</h2>
                <div class="mb-3">
                    <span class="info-label">Trạng thái: </span>
                    <span class="badge ${course.status eq 'Active' ? 'bg-success' : 'bg-secondary'}">
                        ${course.status}
                    </span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Giá: </span>
                    <c:if test="${course.originalPrice != course.price}">
                        <span class="price-original">
                            <fmt:formatNumber value="${course.originalPrice}" type="currency" currencySymbol="VNĐ"/>
                        </span>
                        <span class="price-discount ms-2">
                            <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="VNĐ"/>
                        </span>
                    </c:if>
                    <c:if test="${course.originalPrice == course.price}">
                        <span class="info-value fw-bold text-danger">
                            <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="VNĐ"/>
                        </span>
                    </c:if>
                </div>
                <div class="mb-3">
                    <span class="info-label">Danh mục: </span>
                    <span class="info-value">${course.category.categoryName}</span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Ngày tạo: </span>
                    <span class="info-value">
                        <fmt:formatDate value="${course.dateCreated}" pattern="dd/MM/yyyy HH:mm"/>
                    </span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Cập nhật lần cuối: </span>
                    <span class="info-value">
                        <fmt:formatDate value="${course.lastUpdated}" pattern="dd/MM/yyyy HH:mm"/>
                    </span>
                </div>
                <div class="d-flex gap-2 mt-4">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editCourseModal">
                        <i class="fas fa-edit me-2"></i>Cập nhật
                    </button>
                    <a href="course?action=list" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Quay lại
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Thống kê -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="stats-card text-center">
                <div class="stats-value">${course.learnersCount}</div>
                <div class="stats-label">Học viên</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card text-center">
                <div class="stats-value">
                    <fmt:formatNumber value="${course.rating}" pattern="#.#"/>
                    <i class="fas fa-star text-warning"></i>
                </div>
                <div class="stats-label">Đánh giá (${course.ratingCount} lượt)</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card text-center">
                <div class="stats-value">${course.category.categoryName}</div>
                <div class="stats-label">Danh mục</div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="card mb-4">
                <div class="card-header bg-white">
                    <h4 class="mb-0"><i class="fas fa-info-circle me-2"></i>Mô tả khóa học</h4>
                </div>
                <div class="card-body">
                    <p class="card-text">${course.courseDescription}</p>
                </div>
            </div>

            <!-- Thêm phần đánh giá và doanh thu -->
            <div class="row">
                <!-- Phần đánh giá -->
                <div class="col-md-6">
                    <div class="card mb-4">
                        <div class="card-header bg-white">
                            <h4 class="mb-0">
                                <i class="fas fa-star me-2 text-warning"></i>Đánh giá từ học viên
                            </h4>
                        </div>
                        <div class="card-body">
                            <div class="rating-overview text-center mb-4">
                                <h1 class="display-4 mb-0 fw-bold text-primary">
                                    <fmt:formatNumber value="${course.rating}" maxFractionDigits="1"/>
                                </h1>
                                <div class="stars mb-2">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fas fa-star ${i <= course.rating ? 'text-warning' : 'text-muted'}"></i>
                                    </c:forEach>
                                </div>
                                <p class="text-muted">${course.ratingCount} đánh giá</p>
                            </div>
                            
                            <div class="rating-bars">
                                <c:forEach begin="5" end="1" var="star">
                                    <div class="rating-bar-container d-flex align-items-center mb-2">
                                        <div class="rating-label me-2" style="width: 60px;">
                                            ${star} <i class="fas fa-star text-warning"></i>
                                        </div>
                                        <div class="progress flex-grow-1" style="height: 10px;">
                                            <div class="progress-bar bg-warning" role="progressbar" 
                                                style="width: ${feedbackStats[star]}%">
                                            </div>
                                        </div>
                                        <div class="rating-count ms-2" style="width: 50px;">
                                            <fmt:formatNumber value="${feedbackStats[star]}" maxFractionDigits="1"/>%
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Phần doanh thu -->
                <div class="col-md-6">
                    <div class="card mb-4">
                        <div class="card-header bg-white">
                            <h4 class="mb-0">
                                <i class="fas fa-chart-line me-2 text-success"></i>Thống kê doanh thu
                            </h4>
                        </div>
                        <div class="card-body">
                            <div class="revenue-stats text-center">
                                <div class="mb-4">
                                    <h5 class="text-muted mb-2">Tổng doanh thu</h5>
                                    <h2 class="display-6 fw-bold text-success mb-0">
                                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="VNĐ"/>
                                    </h2>
                                </div>
                                <div class="row g-4">
                                    <div class="col-6">
                                        <div class="stat-card p-3 rounded bg-light">
                                            <h6 class="text-muted mb-2">Số lượng đã bán</h6>
                                            <h3 class="mb-0 fw-bold text-primary">
                                                ${purchaseCount}
                                            </h3>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="stat-card p-3 rounded bg-light">
                                            <h6 class="text-muted mb-2">Giá trung bình</h6>
                                            <h3 class="mb-0 fw-bold text-primary">
                                                <c:if test="${purchaseCount > 0}">
                                                    <fmt:formatNumber value="${totalRevenue / purchaseCount}" type="currency" currencySymbol="VNĐ"/>
                                                </c:if>
                                                <c:if test="${purchaseCount == 0}">
                                                    0 VNĐ
                                                </c:if>
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Phần danh sách đánh giá -->
            <div class="card mb-4">
                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                    <h4 class="mb-0">
                        <i class="fas fa-comments me-2 text-primary"></i>Đánh giá từ học viên
                    </h4>
                    <span class="badge bg-primary">${course.ratingCount} đánh giá</span>
                </div>
                <div class="card-body">
                    <c:if test="${empty feedbacks}">
                        <div class="text-center py-5">
                            <i class="fas fa-comment-slash fa-3x text-muted mb-3"></i>
                            <p class="text-muted">Chưa có đánh giá nào cho khóa học này</p>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty feedbacks}">
                        <div class="feedback-list">
                            <c:forEach items="${feedbacks}" var="feedback">
                                <div class="feedback-item border-bottom pb-4 mb-4">
                                    <div class="d-flex align-items-center mb-3">
                                        <img src="${feedback.learner.avatar}" alt="${feedback.learner.fullName}"
                                             class="rounded-circle me-3" style="width: 48px; height: 48px; object-fit: cover;">
                                        <div>
                                            <h6 class="mb-1">${feedback.learner.fullName}</h6>
                                            <div class="d-flex align-items-center">
                                                <div class="stars me-2">
                                                    <c:forEach begin="1" end="5" var="i">
                                                        <i class="fas fa-star ${i <= feedback.rating ? 'text-warning' : 'text-muted'}"></i>
                                                    </c:forEach>
                                                </div>
                                                <small class="text-muted">
                                                    <fmt:formatDate value="${feedback.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </small>
                                            </div>
                                        </div>
                                    </div>
                                    <p class="feedback-comment mb-0">
                                        ${feedback.comment}
                                    </p>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal chỉnh sửa -->
<div class="modal fade" id="editCourseModal" tabindex="-1" aria-labelledby="editCourseModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editCourseModalLabel">
                    <i class="fas fa-edit me-2"></i>Cập nhật khóa học
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="course" method="post" enctype="multipart/form-data" class="edit-form">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="courseID" value="${course.courseID}">

                <div class="modal-body">
                    <div class="row g-4">
                        <div class="col-md-8">
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-info-circle"></i>
                                    Thông tin cơ bản
                                </div>
                                <div class="form-floating">
                                    <input type="text" class="form-control" id="editTitle" name="title"
                                           value="${course.courseTitle}" required placeholder="Nhập tiêu đề khóa học">
                                    <label for="editTitle">Tiêu đề khóa học</label>
                                </div>

                                <div class="form-floating">
                                    <select class="form-select" id="editCategory" name="categoryID" required>
                                        <c:forEach items="${categories}" var="category">
                                            <option value="${category.categoryID}"
                                                ${course.category.categoryID == category.categoryID ? 'selected' : ''}>
                                                    ${category.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <label for="editCategory">Danh mục</label>
                                </div>

                                <div class="form-floating">
                                    <textarea class="form-control" id="editDescription" name="description"
                                              required
                                              placeholder="Nhập mô tả khóa học">${course.courseDescription}</textarea>
                                    <label for="editDescription">Mô tả khóa học</label>
                                </div>
                            </div>

                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-tag"></i>
                                    Giá và trạng thái
                                </div>
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="number" class="form-control" id="editPrice" name="price"
                                                   value="${course.price}" min="0" required
                                                   placeholder="Nhập giá khóa học">
                                            <label for="editPrice">Giá (VNĐ)</label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="number" class="form-control" id="editOriginalPrice"
                                                   name="originalPrice"
                                                   value="${course.originalPrice}" min="0" placeholder="Nhập giá gốc">
                                            <label for="editOriginalPrice">Giá gốc (VNĐ)</label>
                                            <div class="form-text">Để trống nếu không có giá gốc</div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <select class="form-select" id="editStatus" name="status">
                                                <option value="Active" ${course.status eq 'Active' ? 'selected' : ''}>
                                                    Hoạt động
                                                </option>
                                                <option value="Inactive" ${course.status eq 'Inactive' ? 'selected' : ''}>
                                                    Tạm ngừng
                                                </option>
                                            </select>
                                            <label for="editStatus">Trạng thái</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="form-section h-100">
                                <div class="form-section-title">
                                    <i class="fas fa-image"></i>
                                    Ảnh bìa
                                </div>
                                <div class="image-upload-container"
                                     onclick="document.getElementById('editImage').click()">
                                    <input type="file" class="form-control d-none" id="editImage" name="image"
                                           accept="image/*" onchange="previewImage(this)">
                                    <i class="fas fa-cloud-upload-alt image-upload-icon"></i>
                                    <p class="image-upload-text mb-0">Nhấp để chọn ảnh hoặc kéo thả file vào đây</p>
                                    <small class="image-upload-hint">Để trống nếu không thay đổi ảnh</small>
                                    <img id="imagePreview" src="${course.courseImg}" class="preview-image img-fluid">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-cancel" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Hủy
                    </button>
                    <button type="submit" class="btn btn-save">
                        <i class="fas fa-save me-2"></i>Lưu thay đổi
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<c:import url="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function previewImage(input) {
        const preview = document.getElementById('imagePreview');
        const container = document.querySelector('.image-upload-container');
        const icon = container.querySelector('.image-upload-icon');
        const text = container.querySelector('.image-upload-text');
        const hint = container.querySelector('.image-upload-hint');

        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
                icon.style.display = 'none';
                text.style.display = 'none';
                hint.style.display = 'none';
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    // Hiển thị ảnh preview khi modal được mở
    document.getElementById('editCourseModal').addEventListener('show.bs.modal', function () {
        const preview = document.getElementById('imagePreview');
        const container = document.querySelector('.image-upload-container');
        const icon = container.querySelector('.image-upload-icon');
        const text = container.querySelector('.image-upload-text');
        const hint = container.querySelector('.image-upload-hint');

        if (preview.src) {
            preview.style.display = 'block';
            icon.style.display = 'none';
            text.style.display = 'none';
            hint.style.display = 'none';
        }
    });

    // Thêm hỗ trợ kéo thả file
    const dropZone = document.querySelector('.image-upload-container');

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.style.borderColor = 'rgba(173, 216, 230, 0.8)';
        dropZone.style.background = 'rgba(255, 255, 255, 0.8)';
    });

    dropZone.addEventListener('dragleave', (e) => {
        e.preventDefault();
        dropZone.style.borderColor = 'rgba(173, 216, 230, 0.4)';
        dropZone.style.background = 'rgba(255, 255, 255, 0.5)';
    });

    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        const files = e.dataTransfer.files;
        if (files.length > 0 && files[0].type.startsWith('image/')) {
            document.getElementById('editImage').files = files;
            previewImage(document.getElementById('editImage'));
        }
        dropZone.style.borderColor = 'rgba(173, 216, 230, 0.4)';
        dropZone.style.background = 'rgba(255, 255, 255, 0.5)';
    });
</script>
</body>
</html>
