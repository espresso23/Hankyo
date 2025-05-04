<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>Quản lý khóa học - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background: #f8f9fa; }
        .course-card {
            border-radius: 20px;
            box-shadow: 0 2px 16px rgba(90,90,90,0.10);
            border: none;
            background: #fff;
            transition: transform 0.22s cubic-bezier(.4,2,.6,1), box-shadow 0.22s;
            min-height: 180px;
        }
        .course-card:hover {
            transform: translateY(-7px) scale(1.025);
            box-shadow: 0 8px 32px rgba(106,130,251,0.18);
        }
        .status-badge {
            display: inline-block;
            margin-top: 8px;
            margin-bottom: 8px;
            font-size: 1.05rem;
            padding: 7px 18px;
            border-radius: 14px;
            color: #fff;
            font-weight: 600;
            background: #1eb802;
            box-shadow: 0 2px 8px rgba(90,90,90,0.07);
            letter-spacing: 0.5px;
            border: none;
        }
        .status-badge.inactive {
            background-color: #9c9c926b !important;
            color: black;
        }
        .btn-info {
            background: black;
            border: none;
            color: #fff;
            font-weight: 600;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(90,90,90,0.07);
            transition: background 0.2s, box-shadow 0.2s;
        }
        .btn-info:hover, .btn-info:focus {
            background: #4d4f59;
            color: #fff;
            box-shadow: 0 4px 16px rgba(106,130,251,0.13);
        }
        .modal-content {
            border-radius: 22px;
            box-shadow: 0 2px 16px rgba(90,90,90,0.10);
        }
        .modal-header {
            background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%);
            color: #fff;
            border-radius: 22px 22px 0 0;
        }
        .star {
            color: #ffc107;
            font-size: 1.15rem;
        }
        .feedback-item {
            border-bottom: 1px solid #eee;
            padding: 12px 0;
        }
        .feedback-item:last-child {
            border-bottom: none;
        }
        .course-img {
            width: 64px;
            height: 64px;
            object-fit: cover;
            border-radius: 14px;
            margin-right: 14px;
            border: 2px solid #eee;
            box-shadow: 0 1px 4px rgba(90,90,90,0.07);
        }
        .expert-avatar {
            width: 28px;
            height: 28px;
            object-fit: cover;
            border-radius: 50%;
            margin-right: 7px;
            border: 1.5px solid #eee;
            vertical-align: middle;
        }
        .filter-bar {
            gap: 12px;
            margin-bottom: 18px;
        }
        @media (max-width: 768px) {
            .course-card { min-height: 220px; }
            .filter-bar { flex-direction: column; align-items: stretch; }
        }
        .loading-spinner {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 120px;
        }
        .loading-spinner .spinner-border {
            width: 2.5rem;
            height: 2.5rem;
            color: #6a82fb;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="admin-sidebar.jsp" />
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <h2 class="mb-4">Quản lý khóa học</h2>
            <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2 filter-bar">
                <div>
                    <span class="fw-bold">Tổng số khóa học: </span>
                    <span id="totalCourses" class="badge bg-primary" style="font-size:1.1em;"></span>
                </div>
                <div class="d-flex gap-2 flex-wrap">
                    <select id="statusFilter" class="form-select form-select-sm" style="width: 165px;">
                        <option value="">Tất cả trạng thái</option>
                        <option value="active">Hoạt động</option>
                        <option value="inactive">Chưa hoạt động</option>
                    </select>
                    <select id="expertFilter" class="form-select form-select-sm" style="width: 180px;">
                        <option value="">Tất cả giảng viên</option>
                    </select>
                    <select id="categoryFilter" class="form-select form-select-sm" style="width: 180px;">
                        <option value="">Tất cả danh mục</option>
                    </select>
                    <div class="input-group" style="max-width: 320px;">
                        <input type="text" id="searchInput" class="form-control" placeholder="Tìm kiếm khóa học...">
                        <span class="input-group-text"><i class="bi bi-search"></i></span>
                    </div>
                </div>
            </div>
            <div class="row" id="coursesGrid">
                <!-- Danh sách khóa học sẽ được render ở đây -->
            </div>
            <!-- Modal chi tiết khóa học -->
            <div class="modal fade" id="courseDetailModal" tabindex="-1">
                <div class="modal-dialog modal-xl">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Chi tiết khóa học</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body" id="courseDetailContent">
                            <!-- Nội dung chi tiết sẽ được render ở đây -->
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    let allCourses = [];
    let filteredCourses = [];
    const contextPath = window.location.pathname.split('/').slice(0,2).join('/');
    document.addEventListener('DOMContentLoaded', function() {
        loadCourses();
        loadExpertFilter();
        loadCategoryFilter();
        document.getElementById('searchInput').addEventListener('input', applyFilters);
        document.getElementById('statusFilter').addEventListener('change', applyFilters);
        document.getElementById('expertFilter').addEventListener('change', applyFilters);
        document.getElementById('categoryFilter').addEventListener('change', applyFilters);
        loadTotalCourses();
    });
    function loadCourses() {
        showLoading();
        fetch(contextPath + '/admin/courses')
            .then(response => response.json())
            .then(courses => {
                allCourses = courses;
                hideLoading();
                applyFilters();
            })
            .catch(error => { hideLoading(); console.error('Error:', error); });
    }
    function loadTotalCourses() {
        fetch(contextPath + '/admin/courses/total')
            .then(response => response.json())
    .then(data => {
                document.getElementById('totalCourses').textContent = data.total;
            })
            .catch(error => console.error('Error:', error));
    }
    function loadExpertFilter() {
        fetch(contextPath + '/admin/courses/experts')
            .then(response => response.json())
            .then(experts => {
                if (!Array.isArray(experts)) experts = [];
                const select = document.getElementById('expertFilter');
                select.innerHTML = '<option value="">Tất cả giảng viên</option>' + experts.map(function(e) { return '<option value="' + e + '">' + e + '</option>'; }).join('');
            });
    }
    function loadCategoryFilter() {
        fetch(contextPath + '/admin/courses/categories')
            .then(response => response.json())
            .then(categories => {
                if (!Array.isArray(categories)) categories = [];
                const select = document.getElementById('categoryFilter');
                select.innerHTML = '<option value="">Tất cả danh mục</option>' + categories.map(function(cat) { return '<option value="' + cat + '">' + cat + '</option>'; }).join('');
            });
    }
    function applyFilters() {
        const status = document.getElementById('statusFilter').value;
        const expert = document.getElementById('expertFilter').value;
        const category = document.getElementById('categoryFilter').value;
        const keyword = document.getElementById('searchInput').value.trim().toLowerCase();
        filteredCourses = allCourses.filter(c => {
            let match = true;
            if (status) match = match && c.status && c.status.toLowerCase() === status;
            if (expert) match = match && c.expert && c.expert.fullName === expert;
            if (category) match = match && c.category && c.category.categoryName === category;
            if (keyword) match = match && (
                (c.courseTitle && c.courseTitle.toLowerCase().includes(keyword)) ||
                (c.courseDescription && c.courseDescription.toLowerCase().includes(keyword))
            );
            return match;
        });
        document.getElementById('totalCourses').textContent = filteredCourses.length;
        displayCourses(filteredCourses);
    }
    function displayCourses(courses) {
        const grid = document.getElementById('coursesGrid');
        grid.innerHTML = '';
        if (!courses || courses.length === 0) {
            grid.innerHTML = '<div class="text-center text-muted py-5">Không có khóa học nào phù hợp.</div>';
            return;
        }
        courses.forEach(course => {
            const col = document.createElement('div');
            col.className = 'col-md-6 col-lg-4 mb-4';
            const isActive = course.status && course.status.toLowerCase() === 'active';
            const statusClass = 'status-badge' + (isActive ? '' : ' inactive');
            const statusText = isActive ? 'Hoạt động' : 'Chưa hoạt động';
            col.innerHTML =
                '<div class="card course-card">' +
                    '<div class="card-body d-flex align-items-center">' +
                        '<img src="' + (course.courseImg || 'default-course.png') + '" class="course-img" alt="Course image">' +
                        '<div>' +
                            '<h5 class="card-title mb-1">' + course.courseTitle + '</h5>' +
                            '<span class="' + statusClass + '" title="Trạng thái khóa học">' + statusText + '</span>' +
                            '<div class="mb-1" title="Giảng viên">' +
                                (course.expert && course.expert.avatar ? '<img src="' + course.expert.avatar + '" class="expert-avatar" alt="Avatar">' : '') +
                                (course.expert && course.expert.fullName ? course.expert.fullName : '---') +
                            '</div>' +
                            '<div class="mb-1" title="Danh mục">' + (course.category && course.category.categoryName ? course.category.categoryName : '---') + '</div>' +
                            '<div class="mb-1" title="Số học viên"><i class="bi bi-people"></i> ' + (course.learnersCount || 0) + ' học viên</div>' +
                            '<div class="mb-1" title="Đánh giá"><i class="bi bi-star-fill star"></i> ' + (course.rating ? course.rating.toFixed(1) : '0.0') + ' (' + (course.ratingCount || 0) + ' đánh giá)</div>' +
                            '<div class="mb-2" title="Doanh thu"><i class="bi bi-cash"></i> ' + (course.totalRevenue ? Number(course.totalRevenue).toLocaleString('vi-VN') : '0') + ' VND</div>' +
                            '<button class="btn btn-info btn-sm" onclick="viewCourseDetail(' + course.courseID + ')"><i class="fas fa-info-circle"></i> Chi tiết</button>' +
                        '</div>' +
                    '</div>' +
                '</div>';
            grid.appendChild(col);
        });
    }
    function viewCourseDetail(courseId) {
        fetch(contextPath + '/admin/courses/' + courseId)
            .then(response => response.json())
            .then(data => {
                document.getElementById('courseDetailContent').innerHTML = renderCourseDetail(data);
                new bootstrap.Modal(document.getElementById('courseDetailModal')).show();
            })
            .catch(error => console.error('Error:', error));
    }
    function renderCourseDetail(data) {
        const c = data.course;
        const feedbacks = data.feedbacks || [];
        let html = '';
        html += '<div class="row">';
        html +=   '<div class="col-md-4 text-center">';
        html +=     '<img src="' + (c.courseImg || 'default-course.png') + '" class="img-fluid rounded mb-3" style="max-width: 220px;">';
        html +=     '<div class="mt-2"><span class="badge ' + (c.status && c.status.toLowerCase() === 'active' ? 'bg-success' : 'bg-danger') + ' status-badge">' + (c.status && c.status.toLowerCase() === 'active' ? 'Hoạt động' : 'Chưa hoạt động') + '</span></div>';
        html +=   '</div>';
        html +=   '<div class="col-md-8">';
        html +=     '<h4>' + c.courseTitle + '</h4>';
        html +=     '<p><strong>Chuyên mục:</strong> ' + (c.category && c.category.categoryName ? c.category.categoryName : '---') + '</p>';
        html +=     '<p><strong>Expert:</strong> ' + (c.expert && c.expert.fullName ? c.expert.fullName : '---') + '</p>';
        html +=     '<p><strong>Giá:</strong> ' + (c.price ? Number(c.price).toLocaleString('vi-VN') : '0') + ' VND</p>';
        html +=     '<p><strong>Doanh thu:</strong> ' + (data.totalRevenue ? Number(data.totalRevenue).toLocaleString('vi-VN') : '0') + ' VND</p>';
        html +=     '<p><strong>Số lượt mua:</strong> ' + (data.purchaseCount || 0) + '</p>';
        html +=     '<p><strong>Số học viên:</strong> ' + (c.learnersCount || 0) + '</p>';
        html +=     '<p><strong>Điểm trung bình:</strong> <span class="star">' + (c.rating ? c.rating.toFixed(1) : '0.0') + '</span> (' + (c.ratingCount || 0) + ' đánh giá)</p>';
        html +=     '<p><strong>Mô tả:</strong> ' + (c.courseDescription || '') + '</p>';
        html +=   '</div>';
        html += '</div>';
        html += '<hr>';
        html += '<h5 class="mt-3">Đánh giá & Phản hồi</h5>';
        html += '<div>';
        if (feedbacks.length === 0) {
            html += '<div class="text-muted">Chưa có đánh giá nào.</div>';
        } else {
            feedbacks.forEach(function(fb) {
                html += '<div class="feedback-item">';
                html +=   '<div class="d-flex align-items-center mb-1">';
                html +=     '<img src="' + (fb.learner && fb.learner.avatar ? fb.learner.avatar : 'default-avatar.png') + '" class="rounded-circle me-2" style="width:36px;height:36px;object-fit:cover;">';
                html +=     '<strong>' + (fb.learner && fb.learner.fullName ? fb.learner.fullName : '---') + '</strong>';
                html +=     '<span class="ms-2 star">' + '★'.repeat(Math.round(fb.rating)) + '☆'.repeat(5-Math.round(fb.rating)) + '</span>';
                html +=     '<span class="ms-2 text-muted" style="font-size:0.95em;">' + (fb.createdAt ? new Date(fb.createdAt).toLocaleDateString('vi-VN') : '') + '</span>';
                html +=   '</div>';
                html +=   '<div>' + (fb.comment || '') + '</div>';
                html += '</div>';
            });
        }
        html += '</div>';
        return html;
    }
    function showLoading() {
        document.getElementById('coursesGrid').innerHTML = '<div class="loading-spinner"><div class="spinner-border" role="status"></div></div>';
    }
    function hideLoading() {
        document.getElementById('coursesGrid').innerHTML = '';
    }
</script>
</body>
</html> 
