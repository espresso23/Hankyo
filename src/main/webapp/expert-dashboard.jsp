<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Expert Dashboard</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <!-- Chart.js Zoom Plugin -->
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@2.0.1/dist/chartjs-plugin-zoom.min.js"></script>
    <!-- Custom CSS -->
    <style>
        .sidebar {
            height: 100vh;
            background-color: #f8f9fa;
            padding: 20px;
            position: fixed;
            left: 0;
            width: 250px;
            transition: all 0.3s ease;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 20px;
            transition: all 0.3s ease;
        }
        
        .stat-card {
            background: linear-gradient(135deg, #6f42c1 0%, #6610f2 100%);
            color: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
        }
        
        .stat-card.purple {
            background: linear-gradient(135deg, #6f42c1 0%, #6610f2 100%);
        }
        
        .stat-card.blue {
            background: linear-gradient(135deg, #0d6efd 0%, #0dcaf0 100%);
        }
        
        .time-filter {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        
        .time-filter button {
            padding: 8px 15px;
            border: 1px solid #dee2e6;
            border-radius: 20px;
            background: white;
            color: #6c757d;
            transition: all 0.2s ease;
        }
        
        .time-filter button.active {
            background: #6f42c1;
            color: white;
            border-color: #6f42c1;
        }
        
        .bank-info {
            background: white;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid #dee2e6;
            transition: all 0.3s ease;
        }
        
        .bank-info img {
            width: 40px;
            height: 40px;
            margin-right: 10px;
            object-fit: contain;
        }
        
        .chart-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            border: 1px solid #dee2e6;
            position: relative;
            height: 400px;
        }

        .course-item {
            background: white;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 10px;
            border: 1px solid #dee2e6;
            transition: all 0.3s ease;
        }

        .clickable-card {
            cursor: pointer;
            transition: transform 0.2s;
        }

        .clickable-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .loading {
            opacity: 0.6;
            pointer-events: none;
        }

        .loading-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(255,255,255,0.8);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
            border-radius: 10px;
        }

        .loading-spinner {
            width: 40px;
            height: 40px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid #6f42c1;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .nav-link {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px;
            color: #6c757d;
            transition: all 0.2s ease;
        }

        .nav-link:hover, .nav-link.active {
            color: #6f42c1;
            background-color: #fff;
            border-radius: 8px;
        }

        .nav-link i {
            width: 20px;
            text-align: center;
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
                margin-bottom: 20px;
            }
            
            .main-content {
                margin-left: 0;
            }
            
            .time-filter {
                overflow-x: auto;
                white-space: nowrap;
                flex-wrap: nowrap;
                -webkit-overflow-scrolling: touch;
                padding-bottom: 10px;
            }
            
            .time-filter::-webkit-scrollbar {
                display: none;
            }
            
            .stat-card {
                margin-bottom: 15px;
            }
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h5 class="mb-4"><c:out value="${expert.fullName}"/></h5>
    <div class="nav flex-column">
        <a href="#" class="nav-link active"><i class="fas fa-home"></i> Tổng quan</a>
        <a href="${pageContext.request.contextPath}/expert/transactions" class="nav-link"><i class="fas fa-exchange-alt"></i> Giao dịch</a>
        <a href="${pageContext.request.contextPath}/expert/bank-management" class="nav-link"><i class="fas fa-university"></i> Ngân hàng</a>
        <a href="#" class="nav-link"><i class="fas fa-credit-card"></i> Kênh thanh toán</a>
        <a href="#" class="nav-link"><i class="fas fa-link"></i> Tạo link thanh toán</a>
        <a href="#" class="nav-link"><i class="fas fa-cog"></i> Thiết lập</a>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Time Filter -->
    <div class="time-filter mb-4">
        <button data-period="yesterday" data-text="hôm qua">Hôm qua</button>
        <button data-period="today" data-text="hôm nay" class="active">Hôm nay</button>
        <button data-period="week" data-text="tuần này">Tuần này</button>
        <button data-period="month" data-text="tháng này">Tháng này</button>
        <button data-period="last-month" data-text="tháng trước">Tháng trước</button>
        <button data-period="year" data-text="năm nay">Năm nay</button>
        <button data-period="last-year" data-text="năm trước">Năm trước</button>
        <button data-period="all" data-text="toàn bộ">Toàn bộ</button>
        <button data-period="custom" data-text="">Tùy chỉnh</button>
    </div>

    <!-- Stats Cards -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="stat-card purple clickable-card" data-type="revenue">
                <h6 class="revenue-title">Tổng doanh thu hôm nay</h6>
                <div class="revenue-stats">
                    <h3 class="revenue-amount" id="revenue-amount">₫0</h3>
                    <small class="revenue-change text-white" id="revenue-change">
                        <i class="fas fa-arrow-up"></i> 0%
                    </small>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="stat-card blue clickable-card" data-type="orders">
                <h6 class="orders-title">Tổng đơn hàng hôm nay</h6>
                <div class="orders-stats">
                    <h3 class="orders-amount" id="orders-amount">0 đơn hàng</h3>
                    <small class="orders-change text-white" id="orders-change">
                        <i class="fas fa-arrow-up"></i> 0%
                    </small>
                </div>
            </div>
        </div>
    </div>

    <!-- Chart -->
    <div class="chart-container mb-4">
        <div id="chartLoading" class="loading-overlay" style="display: none;">
            <div class="loading-spinner"></div>
        </div>
        <div class="d-flex justify-content-end mb-2">
            <button id="resetZoom" class="btn btn-sm btn-outline-secondary">
                <i class="fas fa-search-minus"></i> Reset Zoom
            </button>
        </div>
        <canvas id="revenueChart"></canvas>
    </div>

    <!-- Top Courses -->
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="mb-0">Khóa học nổi bật</h5>
                <div class="course-loading spinner-border text-primary" role="status" style="display: none;">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>
            <div id="topCourses">
                <!-- Top courses will be dynamically inserted by JavaScript -->
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Kiểm tra xem Chart.js đã được load chưa
    if (typeof Chart === 'undefined') {
        console.error('Chart.js is not loaded!');
    } else {
        console.log('Chart.js loaded successfully');
    }

    // Đảm bảo tất cả code chạy sau khi DOM đã load
    document.addEventListener('DOMContentLoaded', function() {
        // Khởi tạo formatter cho tiền tệ
        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            maximumFractionDigits: 0
        });

        // Hàm hiển thị/ẩn loading
        function showLoading(show) {
            const loadingOverlay = document.getElementById('chartLoading');
            if (loadingOverlay) {
                loadingOverlay.style.display = show ? 'flex' : 'none';
            }
        }

        // Hàm hiển thị lỗi
        function showError(message) {
            console.error(message);
            // Thêm code hiển thị thông báo lỗi tại đây nếu cần
        }

        // Khởi tạo sự kiện cho các nút filter
        const timeFilterButtons = document.querySelectorAll('.time-filter button');
        timeFilterButtons.forEach(button => {
            button.addEventListener('click', function() {
                timeFilterButtons.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
                
                const period = this.dataset.period;
                const periodText = this.dataset.text;
                
                // Cập nhật tiêu đề
                document.querySelector('.revenue-title').textContent = `Tổng doanh thu ${periodText}`;
                document.querySelector('.orders-title').textContent = `Tổng đơn hàng ${periodText}`;
                
                // Load dữ liệu mới
                loadDashboardData(period);
            });
        });

        // Chọn nút mặc định và load dữ liệu ban đầu
        const defaultButton = document.querySelector('.time-filter button[data-period="today"]');
        if (defaultButton) {
            defaultButton.click();
        }
    });
</script>
<script src="${pageContext.request.contextPath}/js/expert-dashboard.js"></script>
</body>
</html> 