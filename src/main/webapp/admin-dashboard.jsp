<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>Admin Dashboard - Hankyo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
        }
        .nav-item{
            margin: 5px 0 5px 0px;

        }
        .sidebar {
            min-height: 100vh;
            background: #fff;
            color: #5f72bd;
            border-right: 1px solid #e3e3e3;
        }
        .sidebar .nav-link {
            color: #5f72bd;
            font-weight: 500;
            border-radius: 8px;
        }
        .sidebar .nav-link.active, .sidebar .nav-link:hover {
            background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%);
            color: #fff;
        }
        .main-content {
            padding: 20px;
            background: #f8f9fa;
        }
        .header-gradient {
            background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%);
            color: #fff;
            border-radius: 12px;
            padding: 24px 32px;
            margin-bottom: 32px;
            box-shadow: 0 4px 24px rgba(106,130,251,0.08);
        }
        .stat-card {
            background-color: #ffffff !important;
            color: steelblue;
            border-radius: 24px;
            padding: 24px;
            margin-bottom: 24px;
            background: #fff;
            box-shadow: 0 2px 12px rgba(90,90,90,0.07);
            border: none;
        }
        .stat-card i {
            font-size: 1.5rem;
            margin-bottom: 10px;
            color: #6a82fb;
        }
        .btn-custom {
            background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 10px 24px;
            font-weight: 500;
            transition: background 0.2s, color 0.2s;
        }
        .btn-custom:hover, .btn-custom:focus {
            background: #fc5c7d;
            color: #fff;
        }
        .card-header {
            background: #fff;
            color: #6a82fb;
            border-bottom: 1px solid #f0f0f0;
            font-weight: 600;
            font-size: 1.1rem;
            border-radius: 12px 12px 0 0;
        }
        .card {
            border-radius: 20px;
            border: none;
            box-shadow: 0 2px 12px rgba(90,90,90,0.07);
        }
        .table thead th {
            background: #f8f9fa;
            color: #6a82fb;
            font-weight: 600;
        }
        .table-hover tbody tr:hover {
            background: #f0f8ff;
        }
        .stat-growth {
            font-size: 0.95rem;
            font-weight: 500;
            margin-top: 4px;
        }
        .stat-growth.up {
            color: #28a745;
        }
        .stat-growth.up i {
            color: #28a745 !important;
        }
        .stat-growth.down {
            color: #dc3545;
        }
        .stat-growth.down i {
            color: #dc3545 !important;
        }
        .main-content, .header-gradient, .sidebar {
            border-radius: 20px;
        }
        .table-top-courses th, .table-top-courses td {
            vertical-align: middle !important;
            text-align: center;
            padding: 16px 8px;
            font-size: 1rem;
        }
        .table-top-courses th:first-child, .table-top-courses td:first-child {
            text-align: left;
            min-width: 220px;
        }
        .table-top-courses td .course-info {
            display: flex;
            align-items: center;
        }
        .table-top-courses td .course-img {
            width: 48px;
            height: 48px;
            object-fit: cover;
            border-radius: 12px;
            margin-right: 12px;
            border: 1.5px solid #eee;
        }
        .table-top-courses td .course-title {
            font-weight: 600;
            font-size: 1.05rem;
            margin-bottom: 2px;
        }
        .table-top-courses td .course-desc {
            color: #888;
            font-size: 0.95rem;
            margin-bottom: 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            max-width: 180px;
        }
        .table-top-courses td .star-rating {
            font-size: 1.1rem;
            vertical-align: middle;
        }
        .table-top-courses td .rating-value {
            font-size: 0.98rem;
            color: #888;
            margin-left: 4px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <jsp:include page="admin-sidebar.jsp">
                <jsp:param name="active" value="dashboard"/>
            </jsp:include>

            <!-- Main content -->
            <div class="col-md-9 col-lg-10 main-content">
                <div class="d-flex justify-content-between align-items-center mb-4 header-gradient">
                    <h2>Dashboard</h2>
                    <div class="dropdown">
                        <button class="btn btn-custom dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown">
                            <i class="bi bi-person-circle me-2"></i>Admin
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">Thông tin cá nhân</a></li>
                            <li><a class="dropdown-item" href="#">Đổi mật khẩu</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="logout">Đăng xuất</a></li>
                        </ul>
                    </div>
                </div>

                <!-- Stats cards -->
                <div class="row d-flex align-items-stretch">
                    <div class="col-md-3">
                        <div class="stat-card bg-primary text-black h-100 d-flex flex-column justify-content-between" style="min-height: 160px;">
                            <i class="bi bi-people"></i>
                            <h3 id="totalUsers">0</h3>
                            <p>Tổng số người dùng</p>
                            <div id="userGrowthStat" class="stat-growth"></div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card bg-success text-black h-100 d-flex flex-column justify-content-between" style="min-height: 160px;">
                            <i class="bi bi-book"></i>
                            <h3 id="totalCourses">0</h3>
                            <p>Tổng số khóa học</p>
                            <div id="courseGrowthStat" class="stat-growth"></div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card bg-warning text-black h-100 d-flex flex-column justify-content-between" style="min-height: 160px;">
                            <i class="bi bi-cash"></i>
                            <h3 id="totalRevenue">0</h3>
                            <p>Tổng doanh thu</p>
                            <div id="revenueGrowthStat" class="stat-growth"></div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card bg-info text-black h-100 d-flex flex-column justify-content-between" style="min-height: 160px;">
                            <i class="bi bi-file-text"></i>
                            <h3 id="newPosts">0</h3>
                            <p>Bài viết mới (7 ngày)</p>
                            <div id="postGrowthStat" class="stat-growth"></div>
                        </div>
                    </div>
                </div>

                <!-- Charts -->
                <div class="row mt-4">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Doanh thu theo tháng</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="revenueChart"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Tăng trưởng người dùng</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="userGrowthChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Top Selling Courses -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Khóa học bán chạy trong tháng</h5>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover table-top-courses">
                                        <thead>
                                            <tr>
                                                <th>Khóa học</th>
                                                <th>Expert</th>
                                                <th>Số lượng bán</th>
                                                <th>Doanh thu</th>
                                                <th>Giá</th>
                                                <th>Đánh giá</th>
                                            </tr>
                                        </thead>
                                        <tbody id="topCoursesTable">
                                            <!-- Dữ liệu sẽ được render bằng JavaScript -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        // Lấy dữ liệu từ API
        fetch('/Hankyo/admin/dashboard')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('API Response:', data); // Debug log
                
                // Cập nhật thống kê
                document.getElementById('totalUsers').textContent = data.totalUsers || 0;
                document.getElementById('totalCourses').textContent = data.totalCourses || 0;
                document.getElementById('totalRevenue').textContent = data.totalRevenue || 0;
                document.getElementById('newPosts').textContent = data.newPosts || 0;

                // Hiển thị % tăng trưởng
                function renderGrowthStat(elementId, growth) {
                    const el = document.getElementById(elementId);
                    if (el) {
                        let icon = '';
                        let cls = '';
                        if (growth > 0) {
                            icon = '<i class="bi bi-arrow-up"></i>';
                            cls = 'up';
                        } else if (growth < 0) {
                            icon = '<i class="bi bi-arrow-down"></i>';
                            cls = 'down';
                        } else {
                            icon = '';
                            cls = '';
                        }
                        el.innerHTML = icon + ' ' + Math.abs(growth).toFixed(2) + '%';
                        el.className = 'stat-growth ' + cls;
                    }
                }
                renderGrowthStat('userGrowthStat', data.userGrowth || 0);
                renderGrowthStat('courseGrowthStat', data.courseGrowth || 0);
                renderGrowthStat('revenueGrowthStat', data.revenueGrowth || 0);
                renderGrowthStat('postGrowthStat', data.postGrowth || 0);

                // Vẽ biểu đồ doanh thu theo tháng (dạng cột)
                if (data.monthlyRevenue) {
                    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
                    new Chart(revenueCtx, {
                        type: 'bar',
                        data: {
                            labels: data.monthlyRevenue.map(function(item){ return 'Tháng ' + item.month }),
                            datasets: [{
                                label: 'Doanh thu',
                                data: data.monthlyRevenue.map(function(item){ return item.revenue }),
                                backgroundColor: 'rgba(255,120,81,0.8)', // cam
                                borderColor: '#ff7851',
                                borderWidth: 2,
                                borderRadius: 8,
                                maxBarThickness: 40
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                title: {
                                    display: true,
                                    text: 'Doanh thu theo tháng',
                                    color: '#6a82fb',
                                    font: { size: 18, weight: 'bold' }
                                },
                                legend: {
                                    labels: { color: '#6a82fb' }
                                }
                            },
                            animation: {
                                duration: 2000,
                                easing: 'easeInOutCubic'
                            },
                            scales: {
                                x: { ticks: { color: '#6a82fb' } },
                                y: { ticks: { color: '#6a82fb' } }
                            }
                        }
                    });
                }

                // Vẽ biểu đồ tăng trưởng người dùng theo tháng (dạng cột)
                if (data.userStats && Array.isArray(data.userStats)) {
                    // Lấy tháng hiện tại
                    const now = new Date();
                    const currentMonth = now.getMonth() + 1;
                    // Tạo map từ month -> totalUsers
                    const monthMap = {};
                    data.userStats.forEach(function(item) {
                        monthMap[item.month] = item.totalUsers;
                    });
                    // Tạo mảng đủ các tháng từ 1 đến tháng hiện tại
                    const labels = [];
                    const values = [];
                    for (let m = 1; m <= currentMonth; m++) {
                        labels.push('Tháng ' + m);
                        values.push(monthMap[m] !== undefined ? monthMap[m] : 0);
                    }
                    const userCtx = document.getElementById('userGrowthChart').getContext('2d');
                    new Chart(userCtx, {
                        type: 'bar',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: 'Người dùng mới',
                                data: values,
                                backgroundColor: 'rgba(106,130,251,0.8)',
                                borderColor: '#6a82fb',
                                borderWidth: 2,
                                borderRadius: 8,
                                maxBarThickness: 40
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                title: {
                                    display: true,
                                    text: 'Tăng trưởng người dùng theo tháng',
                                    color: '#6a82fb',
                                    font: { size: 18, weight: 'bold' }
                                },
                                legend: {
                                    labels: { color: '#6a82fb' }
                                }
                            },
                            animation: {
                                duration: 2000,
                                easing: 'easeInOutCubic'
                            },
                            scales: {
                                x: { ticks: { color: '#6a82fb' } },
                                y: { ticks: { color: '#6a82fb' } }
                            }
                        }
                    });
                }

                // Hiển thị khóa học bán chạy
                if (data.topSellingCourses && Array.isArray(data.topSellingCourses)) {
                    const tableBody = document.getElementById('topCoursesTable');
                    tableBody.innerHTML = '';
                    if (data.topSellingCourses.length === 0) {
                        const emptyRow = document.createElement('tr');
                        emptyRow.innerHTML = '<td colspan="5" class="text-center">Không có dữ liệu</td>';
                        tableBody.appendChild(emptyRow);
                    } else {
                        data.topSellingCourses.forEach(function(course) {
                            var formattedRevenue = Number(course.totalRevenue).toLocaleString('vi-VN') + ' VND';
                            var formattedPrice = Number(course.price).toLocaleString('vi-VN') + ' VND';
                            var imgSrc = course.courseImg && course.courseImg.trim() !== "" ? course.courseImg : "/images/no-image.png";
                            var row = document.createElement('tr');
                            var html = '';
                            html += '<td>';
                            html +=   '<div class="course-info">';
                            html +=     '<img src="' + imgSrc + '" alt="' + (course.title ? course.title : 'No title') + '" class="course-img" onerror="this.onerror=null;this.src=\'/images/no-image.png\';">';
                            html +=     '<div>';
                            html +=       '<div class="course-title">' + (course.title ? course.title : '---') + '</div>';
                            html +=       '<div class="course-desc">' + (course.description ? course.description.substring(0, 50) : '') + '...</div>';
                            html +=     '</div>';
                            html +=   '</div>';
                            html += '</td>';
                            html += '<td>' + (course.expertName ? course.expertName : '---') + '</td>';
                            html += '<td>' + (course.totalSales != null ? course.totalSales : 0) + '</td>';
                            html += '<td>' + formattedRevenue + '</td>';
                            html += '<td>' + formattedPrice + '</td>';
                            // Hiển thị rating star
                            var rating = course.rating != null ? Number(course.rating) : 0;
                            var starHtml = '<span class="star-rating">';
                            for (var i = 1; i <= 5; i++) {
                                if (i <= Math.round(rating)) {
                                    starHtml += '<i class="bi bi-star-fill text-warning"></i>';
                                } else {
                                    starHtml += '<i class="bi bi-star text-warning"></i>';
                                }
                            }
                            starHtml += '</span><span class="rating-value">' + rating.toFixed(1) + '</span>';
                            html += '<td>' + starHtml + '</td>';
                            row.innerHTML = html;
                            tableBody.appendChild(row);
                        });
                    }
                }
            })
            .catch(error => {
                console.error('Error fetching dashboard data:', error);
                alert('Không thể tải dữ liệu dashboard. Vui lòng thử lại sau.');
            });
    </script>
</body>
</html> 
