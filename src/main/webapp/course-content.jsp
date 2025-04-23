<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${course.courseTitle} - Hankyo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/css/learn-course.css">
    <style>
        /* Styles riêng cho Course Content */
        .course-sidebar {
            position: fixed;
            left: 0;
            top: 56px;
            height: calc(100vh - 56px);
            width: 320px;
            background: #fff;
            box-shadow: 2px 0 8px rgba(0,0,0,0.1);
            z-index: 1000;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            overflow-y: auto;
            border-right: 1px solid #e0e0e0;
        }

        .sidebar-toggle-container {
            position: fixed;
            top: 56px;
            left: 0;
            height: 56px;
            z-index: 1001;
            background: #fff;
            border-bottom: 1px solid #e0e0e0;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            display: flex;
            align-items: center;
            width: 320px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }

        .sidebar-toggle-container.collapsed {
            width: auto;
            border-right: 1px solid #e0e0e0;
            box-shadow: 2px 0 4px rgba(0,0,0,0.08);
        }

        .sidebar-toggle {
            background: none;
            border: none;
            color: #1a73e8;
            font-size: 14px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 12px 16px;
            cursor: pointer;
            width: 100%;
            transition: all 0.2s ease;
            border-radius: 4px;
        }

        .sidebar-toggle i {
            margin-right: 8px;
        }

        .sidebar-toggle:hover {
            background-color: rgba(26, 115, 232, 0.08);
        }

        .sidebar-hidden {
            transform: translateX(-320px);
        }

        .course-content {
            margin-left: 320px;
            margin-top: 56px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            padding: 20px;
        }

        .course-content.full-width {
            margin-left: 0;
        }

        .hide-menu-text {
            font-weight: 500;
            color: #1a73e8;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 24px;
            height: 24px;
            border-radius: 50%;
            transition: all 0.2s ease;
        }

        .sidebar-toggle:hover .hide-menu-text {
            background-color: rgba(26, 115, 232, 0.12);
        }

        .section-title {
            padding: 16px 20px;
            font-weight: 600;
            border-bottom: 1px solid #e0e0e0;
            font-size: 1rem;
            color: #202124;
            background-color: #f8f9fa;
        }

        .lessons-list {
            padding: 8px 0;
        }

        .lesson-item {
            padding: 12px 20px;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            border-left: 3px solid transparent;
        }

        .lesson-item:hover {
            background-color: rgba(0,0,0,0.04);
        }

        .lesson-item.active {
            background-color: rgba(26, 115, 232, 0.08);
            border-left: 3px solid #1a73e8;
        }

        .lesson-item.completed {
            color: #5f6368;
        }

        .lesson-icon {
            margin-right: 12px;
            color: #5f6368;
            font-size: 1rem;
            width: 20px;
            text-align: center;
        }

        .lesson-item.active .lesson-icon {
            color: #1a73e8;
        }

        .content-duration {
            margin-left: auto;
            font-size: 0.85rem;
            color: #5f6368;
        }

        .progress-info {
            padding: 16px 20px;
            background-color: #f8f9fa;
            border-bottom: 1px solid #e0e0e0;
        }

        .progress-bar {
            height: 8px;
            background-color: #e8f0fe;
            border-radius: 4px;
            margin: 16px 0;
            overflow: hidden;
        }

        .progress-value {
            height: 100%;
            background-color: #1a73e8;
            border-radius: 4px;
            transition: width 0.3s ease;
        }

        /* Video container styles */
        .fixed-video-container {
            position: sticky;
            top: 76px;
            z-index: 100;
            margin-bottom: 24px;
            max-width: 854px;
            margin-left: auto;
            margin-right: auto;
        }

        /* Nút điều khiển video container */
        .video-controls-overlay {
            position: absolute;
            top: 0;
            right: 0;
            padding: 10px;
            z-index: 20;
            display: flex;
            gap: 10px;
        }

        .video-control-btn {
            background: rgba(0,0,0,0.5);
            border: none;
            color: white;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.2s ease;
        }

        .video-control-btn:hover {
            background: rgba(0,0,0,0.7);
            transform: scale(1.1);
        }

        /* Video Title */
        .video-title-overlay {
            position: absolute;
            top: 15px;
            left: 15px;
            color: white;
            font-size: 1.1rem;
            font-weight: 500;
            text-shadow: 0 1px 2px rgba(0,0,0,0.5);
            max-width: 80%;
            z-index: 10;
        }

        /* Video container khi phát hiện fullscreen */
        .fixed-video-container.expanded {
            position: fixed;
            top: 76px;
            left: 0;
            right: 0;
            bottom: 0;
            width: auto;
            height: calc(100vh - 76px);
            z-index: 1100;
            padding: 0;
            border-radius: 0;
            margin: 0;
        }

        .fixed-video-container.expanded .video-container {
            height: 100%;
            border-radius: 0;
        }

        .content-description {
            max-width: 854px;
            margin-left: auto;
            margin-right: auto;
        }

        @media (max-width: 768px) {
            .course-sidebar {
                transform: translateX(-320px);
            }
            .course-content {
                margin-left: 0;
            }
            .sidebar-visible {
                transform: translateX(0);
            }
            .sidebar-toggle-container {
                width: auto;
            }
        }

        .assignment-container {
            max-width: 854px;
            margin: 0 auto;
            padding: 20px;
        }

        .assignment-header {
            margin-bottom: 30px;
        }

        .assignment-header h2 {
            color: #1a73e8;
            margin-bottom: 10px;
        }

        .assignment-description {
            color: #5f6368;
            font-size: 1.1rem;
            line-height: 1.6;
        }

        .assignment-overview {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
        }

        .overview-card {
            background: white;
            border-radius: 8px;
            padding: 25px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .overview-card h3 {
            color: #202124;
            margin-bottom: 20px;
            font-size: 1.2rem;
        }

        .overview-content p {
            color: #5f6368;
            margin-bottom: 15px;
            font-size: 1rem;
        }

        .start-assignment-btn {
            width: 100%;
            padding: 12px;
            font-size: 1.1rem;
            margin-top: 20px;
            background: #1a73e8;
            border: none;
            transition: all 0.3s ease;
        }

        .start-assignment-btn:hover {
            background: #1557b0;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        /* Assignment Result Styles */
        .assignment-result {
            max-width: 854px;
            margin: 24px auto;
            padding: 24px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .result-header {
            font-size: 1.5rem;
            font-weight: 600;
            color: #202124;
            margin-bottom: 20px;
        }

        .result-stats {
            display: flex;
            align-items: center;
            margin-bottom: 16px;
        }

        .result-percentage {
            font-size: 2rem;
            font-weight: 700;
            color: #1a73e8;
            margin-right: 12px;
        }

        .result-fraction {
            color: #5f6368;
            font-size: 1rem;
        }

        .result-progress {
            height: 8px;
            background-color: #e8f0fe;
            border-radius: 4px;
            margin: 16px 0;
            overflow: hidden;
        }

        .result-progress-value {
            height: 100%;
            background-color: #1a73e8;
            border-radius: 4px;
            transition: width 0.3s ease;
        }

        .result-details {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 16px;
            margin: 24px 0;
        }

        .result-detail-item {
            text-align: center;
            padding: 16px;
            background: #f8f9fa;
            border-radius: 8px;
        }

        .detail-value {
            font-size: 1.5rem;
            font-weight: 600;
            color: #202124;
            margin-bottom: 8px;
        }

        .detail-label {
            color: #5f6368;
            font-size: 0.875rem;
        }

        .retake-button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #1a73e8;
            color: #fff;
            border-radius: 4px;
            text-decoration: none;
            font-weight: 500;
            transition: background-color 0.2s;
        }

        .retake-button:hover {
            background-color: #1557b0;
            color: #fff;
        }

        .stats-card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            height: 100%;
            transition: transform 0.2s;
        }

        .stats-card:hover {
            transform: translateY(-2px);
        }

        .stats-value h2 {
            color: #1a73e8;
            font-weight: 600;
        }

        .stats-value p {
            font-size: 0.9rem;
        }

        .progress {
            height: 8px !important;
            background-color: #e8f0fe !important;
            border-radius: 4px !important;
            margin: 16px 0 !important;
            overflow: hidden;
        }

        .progress .progress-bar {
            height: 100%;
            background-color: #1a73e8;
            border-radius: 4px;
            transition: width 0.3s ease;
        }

        .result-details .card {
            border: 1px solid #e9ecef;
            border-radius: 8px;
        }

        .result-details .card-title {
            color: #202124;
            font-size: 1.1rem;
            font-weight: 600;
        }

        .result-details .card-body {
            padding: 1.5rem;
        }

        .btn-primary {
            background-color: #1a73e8;
            border-color: #1a73e8;
            padding: 10px 20px;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #1557b0;
            border-color: #1557b0;
            transform: translateY(-1px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        /* Progress bar trong kết quả bài làm */
        .result-progress .progress {
            height: 10px !important;
        }

        .result-progress .progress-bar {
            background-color: #28a745 !important;
        }

        /* Ẩn text trong progress bar */
        .visually-hidden {
            position: absolute;
            width: 1px;
            height: 1px;
            padding: 0;
            margin: -1px;
            overflow: hidden;
            clip: rect(0, 0, 0, 0);
            white-space: nowrap;
            border: 0;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="sidebar-toggle-container" id="sidebarToggleContainer">
    <button class="sidebar-toggle" id="sidebarToggle">
        <span class="hide-menu-text"><i class="fas fa-chevron-left"></i></span>
        <span class="d-none d-md-inline">Ẩn menu</span>
    </button>
</div>

<div class="course-sidebar" id="courseSidebar">
    <div class="section-title">
        <i class="fas fa-list-ul me-2"></i>Nội dung khóa học
    </div>
    <div class="progress-info">
        <div class="d-flex justify-content-between align-items-center">
            <span class="fw-medium">Tiến độ khóa học</span>
            <span class="fw-bold text-primary">${courseProgress}%</span>
        </div>
        <div class="progress-bar">
            <div class="progress-value" style="width: ${courseProgress}%"></div>
        </div>
    </div>
    <div class="lessons-list">
        <c:forEach items="${courseContents}" var="content">
            <div class="lesson-item ${content.courseContentID == currentContent.courseContentID ? 'active' : ''} ${content.completed ? 'completed' : ''}"
                 data-content-id="${content.courseContentID}"
                 data-is-assignment="${not empty content.assignment}">
                <i class="fas ${not empty content.assignment ? 'fa-file-alt' : (not empty content.media ? 'fa-play-circle' : 'fa-file-text')} lesson-icon"></i>
                <span class="lesson-title">${not empty content.assignment ? content.assignment.assignmentTitle : content.title}</span>
                <c:if test="${content.completed}">
                    <i class="fas fa-check-circle ms-auto text-success"></i>
                </c:if>
                <c:if test="${not empty content.media && !content.completed}">
                    <span class="content-duration ms-auto">
                        <span class="video-duration" data-video-src="${content.media}">--:--</span>
                    </span>
                </c:if>
            </div>
        </c:forEach>
    </div>
</div>

<div class="course-content">
    <div class="course-header">
        <h1 class="course-title">${course.courseTitle}</h1>
        <div class="course-meta">
            <span class="me-3"><i class="fas fa-user-graduate me-2"></i>${course.learnersCount} học viên</span>
            <span class="me-3"><i class="fas fa-star me-2"></i>${course.rating} (${course.ratingCount} đánh giá)</span>
            <span><i class="fas fa-clock me-2"></i><span id="total-duration">Đang tính...</span></span>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty currentContent and not empty currentContent.assignment and currentContent.assignment.assignmentID > 0}">
            <div class="assignment-container">
                <div class="assignment-header">
                    <h2>${currentContent.assignment.assignmentTitle}</h2>
                    <p class="assignment-description">${currentContent.assignment.description}</p>
                </div>

                <div class="assignment-overview">
                    <div class="overview-card">
                        <h3><i class="fas fa-info-circle me-2"></i>Tổng quan bài tập</h3>
                        <div class="overview-content">
                            <p><i class="fas fa-list-ol me-2"></i>Số câu hỏi: ${not empty currentContent.assignment.assignmentQuestions ? currentContent.assignment.assignmentQuestions.size() : 0}</p>
                            <p><i class="fas fa-clock me-2"></i>Thời gian làm bài: Không giới hạn</p>
                            <p><i class="fas fa-check-circle me-2"></i>Yêu cầu: Trả lời đúng tất cả câu hỏi</p>
                            <p><i class="fas fa-exclamation-circle me-2"></i>Lưu ý: Bạn chỉ có thể nộp bài một lần</p>
                            
                            <c:if test="${not empty currentContent.assignment.assignmentQuestions}">
                                <div class="mt-4">
                                    <h4 class="mb-3">Danh sách câu hỏi:</h4>
                                    <div class="list-group">
                                        <c:forEach items="${currentContent.assignment.assignmentQuestions}" var="question" varStatus="status">
                                            <div class="list-group-item">
                                                <div class="d-flex w-100 justify-content-between align-items-center">
                                                    <h5 class="mb-1">Câu ${status.index + 1} (${question.questionMark} điểm)</h5>
                                                    <div>
                                                        <c:if test="${not empty question.questionImg}">
                                                            <span class="badge bg-info me-1" title="Có hình ảnh"><i class="fas fa-image"></i></span>
                                                        </c:if>
                                                        <c:if test="${not empty question.audioFile}">
                                                            <span class="badge bg-warning" title="Có audio"><i class="fas fa-volume-up"></i></span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <p class="mb-1">${question.questionText}</p>
                                                <small class="text-muted">
                                                    <i class="fas fa-tag me-1"></i>${question.questionType}
                                                </small>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="assignment-actions">
                            <c:choose>
                                <c:when test="${currentContent.completed}">
                                    <div class="assignment-result">
                                        <div class="result-header mb-4">
                                            <h3 class="text-success">
                                                <i class="fas fa-check-circle me-2"></i>
                                                Bài tập đã hoàn thành
                                            </h3>
                                            <p class="text-muted">
                                                <i class="fas fa-clock me-2"></i>Lần làm gần nhất: 
                                                <fmt:formatDate value="${latestTaken.dateCreated}" pattern="dd/MM/yyyy HH:mm"/>
                                            </p>
                                        </div>
                                        
                                        <c:if test="${not empty assignmentResult}">
                                            <div class="result-stats">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="stats-card mb-3">
                                                            <div class="stats-value">
                                                                <h2 class="display-4 mb-0 text-primary">
                                                                    <fmt:formatNumber value="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}" 
                                                                                    maxFractionDigits="1"/>%
                                                                </h2>
                                                                <p class="text-muted mb-0">Tỷ lệ trả lời đúng</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="stats-card mb-3">
                                                            <div class="stats-value">
                                                                <h2 class="display-4 mb-0">
                                                                    ${assignmentResult.correctCount}/${assignmentResult.totalQuestions}
                                                                </h2>
                                                                <p class="text-muted mb-0">Số câu trả lời đúng</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <div class="progress mb-4" style="height: 10px;">
                                                    <div class="progress-bar bg-success" 
                                                         role="progressbar" 
                                                         style="width: ${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}%" 
                                                         aria-valuenow="${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}" 
                                                         aria-valuemin="0" 
                                                         aria-valuemax="100">
                                                        <span class="visually-hidden">
                                                            ${(assignmentResult.correctCount / assignmentResult.totalQuestions) * 100}% hoàn thành
                                                        </span>
                                                    </div>
                                                </div>

                                                <div class="result-details">
                                                    <div class="card">
                                                        <div class="card-body">
                                                            <h5 class="card-title mb-3">Chi tiết kết quả</h5>
                                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                                <span>Điểm số đạt được:</span>
                                                                <span class="fw-bold">${assignmentResult.score}/10</span>
                                                            </div>
                                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                                <span>Số câu trả lời đúng:</span>
                                                                <span class="fw-bold text-success">${assignmentResult.correctCount}</span>
                                                            </div>
                                                            <div class="d-flex justify-content-between align-items-center">
                                                                <span>Tổng số câu hỏi:</span>
                                                                <span class="fw-bold">${assignmentResult.totalQuestions}</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mt-4 d-flex gap-3 justify-content-center">
                                                    <a href="view-assignment-result?assignmentID=${currentContent.assignment.assignmentID}&courseID=${course.courseID}&courseContentID=${currentContent.courseContentID}" 
                                                       class="btn btn-primary">
                                                        <i class="fas fa-eye me-2"></i>Xem chi tiết bài làm
                                                    </a>
                                                    <button class="btn btn-outline-primary" onclick="startAssignment('${currentContent.assignment.assignmentID}')">
                                                        <i class="fas fa-redo me-2"></i>Làm lại bài
                                                    </button>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-primary start-assignment-btn" data-assignment-id="${currentContent.assignment.assignmentID}">
                                        <i class="fas fa-play me-2"></i>Bắt đầu làm bài
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:when test="${not empty currentContent and not empty currentContent.media}">
            <div class="fixed-video-container" id="videoContainer">
                <div class="video-container">
                    <div class="video-title-overlay">${currentContent.title}</div>
                    <div class="video-controls-overlay">
                        <button class="video-control-btn" id="expandVideo" title="Mở rộng"><i class="fas fa-expand"></i></button>
                    </div>
                    <div class="video-wrapper">
                        <iframe src="${currentContent.media}?enablejsapi=1" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
            </div>

            <div class="content-description">
                <h3>${currentContent.title}</h3>
                <p>${currentContent.description}</p>
            </div>
        </c:when>
        <c:when test="${not empty currentContent}">
            <div class="content-description">
                <h3>${currentContent.title}</h3>
                <p>${currentContent.description}</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>Vui lòng chọn nội dung để xem
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Modal Chi tiết bài làm -->
<div class="modal fade" id="assignmentResultModal" tabindex="-1" aria-labelledby="assignmentResultModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="assignmentResultModalLabel">Chi tiết bài làm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="assignmentResultContent">
                    <!-- Nội dung chi tiết bài làm sẽ được load động -->
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    $(document).ready(function () {
        // Xử lý click vào lesson item
        $('.lesson-item').click(function () {
            const contentId = $(this).data('content-id');
            const isAssignment = $(this).data('is-assignment');

            // Xây dựng URL với các tham số cần thiết
            let url = 'learn-course?courseID=${course.courseID}&courseContentID=' + contentId;

            // Thêm tham số type nếu là assignment
            if (isAssignment === 'true') {
                url += '&type=assignment';
            }
            console.log(url)

            // Chuyển hướng đến URL đã xây dựng
            window.location.href = url;
        });

        // Xử lý start assignment
        $('.start-assignment-btn').click(function () {
            const assignmentID = $(this).data('assignment-id');
            startAssignment(assignmentID);
        });

        // Xử lý view assignment result
        $('.view-result-btn').click(function () {
            const assignmentID = $(this).data('assignment-id');
            viewAssignmentResult(assignmentID);
        });

        // Xử lý hiển thị thêm/ẩn nội dung
        $('#show-more-content').click(function () {
            $('.content-hidden').slideDown(300);
            $(this).addClass('d-none');
            $('#show-less-content').removeClass('d-none');
        });

        $('#show-less-content').click(function () {
            $('.content-hidden').slideUp(300);
            $(this).addClass('d-none');
            $('#show-more-content').removeClass('d-none');
            $('html, body').animate({
                scrollTop: $('#courseContentAccordion').offset().top - 100
            }, 300);
        });

        function showMessage(message, type) {
            const messageDiv = $('<div>', {
                class: 'message ' + type,
                text: message
            });

            $('#message-container').append(messageDiv);

            setTimeout(function () {
                messageDiv.fadeOut(300, function () {
                    $(this).remove();
                });
            }, 3000);
        }

        // Xử lý thời lượng video
        let totalDurationSeconds = 0;
        let videosToProcess = $('.video-duration').length;
        let processedVideos = 0;

        function formatTime(seconds) {
            if (isNaN(seconds) || seconds < 0) return "--:--";

            const hours = Math.floor(seconds / 3600);
            const minutes = Math.floor((seconds % 3600) / 60);
            const remainingSeconds = Math.floor(seconds % 60);

            if (hours > 0) {
                return hours + "h " + minutes + "m";
            } else if (minutes > 0) {
                return minutes + "m " + remainingSeconds + "s";
            } else {
                return remainingSeconds + "s";
            }
        }

        if (videosToProcess === 0) {
            $('#total-duration').text('0 phút');
        }

        $('.video-duration').each(function () {
            const videoSrc = $(this).data('video-src');
            const video = document.createElement('video');

            video.onloadedmetadata = function () {
                const durationSeconds = video.duration;
                const formattedDuration = formatTime(durationSeconds);
                $(this).text(formattedDuration);

                totalDurationSeconds += durationSeconds;
                processedVideos++;

                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            }.bind(this);

            video.onerror = function () {
                $(this).text("Không khả dụng");
                processedVideos++;

                if (processedVideos === videosToProcess) {
                    updateTotalDuration();
                }
            }.bind(this);

            video.src = videoSrc;
            video.preload = 'metadata';
        });

        function updateTotalDuration() {
            let formattedTotalDuration = formatTime(totalDurationSeconds);

            if (totalDurationSeconds >= 3600) {
                const hours = Math.floor(totalDurationSeconds / 3600);
                const minutes = Math.floor((totalDurationSeconds % 3600) / 60);
                formattedTotalDuration = hours + (hours > 1 ? " giờ " : " giờ ") +
                    (minutes > 0 ? (minutes + (minutes > 1 ? " phút" : " phút")) : "");
            } else if (totalDurationSeconds >= 60) {
                const minutes = Math.floor(totalDurationSeconds / 60);
                formattedTotalDuration = minutes + (minutes > 1 ? " phút" : " phút");
            } else {
                formattedTotalDuration = Math.floor(totalDurationSeconds) + " giây";
            }

            $('#total-duration').text(formattedTotalDuration);
        }

        $('.rating-star').click(function () {
            const value = $(this).data('value');
            $('#ratingValue').val(value);
            $('.rating-star').each(function (i) {
                $(this).toggleClass('text-warning', i < value);
            });
        });

        // Xử lý xem thêm đánh giá
        const reviewsPerPage = 5;
        const $reviewCards = $('.review-card');
        const $loadMoreBtn = $('#load-more-reviews');
        const $showLessBtn = $('#show-less-reviews');

        if ($reviewCards.length > reviewsPerPage) {
            $reviewCards.slice(reviewsPerPage).addClass('review-hidden');
        }

        $loadMoreBtn.click(function () {
            $('.review-hidden').slideDown(300);
            $(this).addClass('d-none');
            $showLessBtn.removeClass('d-none');
        });

        $showLessBtn.click(function () {
            $reviewCards.slice(reviewsPerPage).slideUp(300);
            $(this).addClass('d-none');
            $loadMoreBtn.removeClass('d-none');

            // Scroll back to reviews section
            $('html, body').animate({
                scrollTop: $('.reviews-list').offset().top - 100
            }, 300);
        });

        // Xử lý toggle sidebar
        $('#sidebarToggle').click(function () {
            $('#courseSidebar').toggleClass('sidebar-hidden');
            $('.course-content').toggleClass('full-width');
            $('#sidebarToggleContainer').toggleClass('collapsed');

            // Thay đổi icon và văn bản
            if ($('#courseSidebar').hasClass('sidebar-hidden')) {
                $('.hide-menu-text').html('<i class="fas fa-chevron-right"></i>');
                $('.d-none.d-md-inline').text('Hiện menu');
            } else {
                $('.hide-menu-text').html('<i class="fas fa-chevron-left"></i>');
                $('.d-none.d-md-inline').text('Ẩn menu');
            }
        });

        // Xử lý expand video
        $('#expandVideo').click(function () {
            $('.fixed-video-container').toggleClass('expanded');

            if ($('.fixed-video-container').hasClass('expanded')) {
                $(this).html('<i class="fas fa-compress"></i>');
                $(this).attr('title', 'Thu nhỏ');
            } else {
                $(this).html('<i class="fas fa-expand"></i>');
                $(this).attr('title', 'Mở rộng');
            }
        });

        // Xử lý responsive
        function handleResize() {
            if (window.innerWidth < 768) {
                $('#courseSidebar').addClass('sidebar-hidden');
                $('.course-content').addClass('full-width');
                $('#sidebarToggleContainer').addClass('collapsed');
                $('.hide-menu-text').html('<i class="fas fa-chevron-right"></i>');
            }
        }

        // Gọi hàm khi resize
        $(window).resize(handleResize);
        handleResize(); // Gọi lần đầu khi load trang
    });

    function startAssignment(assignmentID) {
        const button = $('.start-assignment-btn');
        const originalText = button.html();

        console.log('=== DEBUG START ASSIGNMENT ===');
        console.log('assignmentID:', assignmentID);

        button.html('<i class="fas fa-spinner fa-spin me-2"></i>Đang tải...');
        button.prop('disabled', true);

        $.ajax({
            url: 'do-assignment',
            method: 'POST',
            data: {
                action: 'create',
                assignmentID: assignmentID
            },
            success: function (response) {
                console.log('=== DEBUG RESPONSE ===');
                console.log('Response:', response);
                
                if (response.success) {
                    window.location.href = 'do-assignment?assignmentID=' + assignmentID + '&assignTakenID=' + response.assignTakenID;
                } else {
                    button.html(originalText);
                    button.prop('disabled', false);
                    alert(response.message || 'Có lỗi xảy ra khi bắt đầu làm bài. Vui lòng thử lại.');
                }
            },
            error: function (xhr, status, error) {
                console.log('=== DEBUG ERROR ===');
                console.log('Error:', error);
                console.log('Status:', status);
                console.log('Response:', xhr.responseText);
                
                button.html(originalText);
                button.prop('disabled', false);
                alert('Có lỗi xảy ra khi kết nối đến máy chủ. Vui lòng thử lại.');
            }
        });
    }

    function viewAssignmentResult(assignmentID) {
        window.location.href = 'view-assignment-result?assignmentID=' + assignmentID;
    }
</script>
</body>
</html> 