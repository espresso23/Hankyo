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
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        <a href="#" class="nav-link"><i class="fas fa-exchange-alt"></i> Giao dịch</a>
        <a href="#" class="nav-link"><i class="fas fa-university"></i> Ngân hàng</a>
        <a href="#" class="nav-link"><i class="fas fa-credit-card"></i> Kênh thanh toán</a>
        <a href="#" class="nav-link"><i class="fas fa-link"></i> Tạo link thanh toán</a>
        <a href="#" class="nav-link"><i class="fas fa-cog"></i> Thiết lập</a>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Thêm nút xem toàn bộ doanh thu vào phần filter -->
    <div class="filter-buttons mb-4">
        <button class="btn btn-outline-primary me-2" data-period="day">Ngày</button>
        <button class="btn btn-outline-primary me-2" data-period="week">Tuần</button>
        <button class="btn btn-outline-primary me-2" data-period="month">Tháng</button>
        <button class="btn btn-outline-primary me-2" data-period="year">Năm</button>
        <button class="btn btn-outline-success" data-period="all">Toàn bộ</button>
    </div>

    <!-- Cards thống kê -->
    <div class="row mb-4">
        <!-- Card doanh thu hôm nay -->
        <div class="col-md-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Doanh thu hôm nay</h5>
                    <p class="card-text h3" id="todayRevenue">
                        <span class="placeholder-glow">
                            <span class="placeholder col-6"></span>
                        </span>
                    </p>
                </div>
            </div>
        </div>
        
        <!-- Card tổng doanh thu -->
        <div class="col-md-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Tổng doanh thu</h5>
                    <p class="card-text h3" id="totalRevenue">
                        <span class="placeholder-glow">
                            <span class="placeholder col-6"></span>
                        </span>
                    </p>
                    <p class="card-text text-muted" id="revenueChange">
                        <span class="placeholder-glow">
                            <span class="placeholder col-4"></span>
                        </span>
                    </p>
                </div>
            </div>
        </div>

        <!-- Card tổng đơn hàng -->
        <div class="col-md-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Tổng đơn hàng</h5>
                    <p class="card-text h3" id="totalOrders">
                        <span class="placeholder-glow">
                            <span class="placeholder col-6"></span>
                        </span>
                    </p>
                </div>
            </div>
        </div>

        <!-- Card tổng doanh thu từ trước đến nay -->
        <div class="col-md-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Tổng doanh thu từ trước đến nay</h5>
                    <p class="card-text h3" id="totalAllTimeRevenue">
                        <span class="placeholder-glow">
                            <span class="placeholder col-6"></span>
                        </span>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Time Filter -->
    <div class="time-filter">
        <button data-period="yesterday" data-text="hôm qua">Hôm qua</button>
        <button data-period="today" data-text="hôm nay" class="active">Hôm nay</button>
        <button data-period="week" data-text="tuần này">Tuần này</button>
        <button data-period="month" data-text="tháng này">Tháng này</button>
        <button data-period="last-month" data-text="tháng trước">Tháng trước</button>
        <button data-period="year" data-text="năm nay">Năm nay</button>
        <button data-period="last-year" data-text="năm trước">Năm trước</button>
        <button data-period="custom" data-text="">Tùy chỉnh</button>
    </div>

    <!-- Stats Cards -->
    <div class="row">
        <div class="col-md-6">
            <div class="stat-card purple clickable-card" data-type="revenue">
                <h6>Tổng doanh thu hôm nay</h6>
                <h3 id="totalRevenue">
                    <fmt:formatNumber value="0" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                </h3>
                <small id="revenueChange" class="text-white">
                    <i class="fas fa-arrow-up"></i> 0%
                </small>
            </div>
        </div>
        <div class="col-md-6">
            <div class="stat-card blue clickable-card" data-type="orders">
                <h6>Tổng đơn hàng hôm nay</h6>
                <h3 id="totalOrders">0 đơn hàng</h3>
                <small id="orderChange" class="text-white">
                    <i class="fas fa-arrow-up"></i> 0%
                </small>
            </div>
        </div>
    </div>

    <!-- Revenue Details -->
    <div class="row">
        <div class="col-md-6">
            <h5>Doanh thu theo ngân hàng</h5>
            <div id="bankRevenue">
                <div class="bank-info d-flex align-items-center justify-content-between">
                    <div class="d-flex align-items-center">
                        <img src="${pageContext.request.contextPath}/images/bidv-logo.png" alt="BIDV">
                        <div>
                            <div><c:out value="${expert.fullName}"/></div>
                            <div class="text-muted">6261 1569 16</div>
                        </div>
                    </div>
                    <div class="text-end">
                        <div>
                            <fmt:formatNumber value="55000" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                        </div>
                        <span class="badge bg-success">Đang kết nối</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <h5>Doanh thu theo kênh thanh toán</h5>
            <div id="paymentChannels">
                <div class="bank-info d-flex align-items-center justify-content-between">
                    <div class="d-flex align-items-center">
                        <img src="${pageContext.request.contextPath}/images/hankyo-logo.png" alt="Hankyo">
                        <div>
                            <div>Kênh: Hankyo</div>
                        </div>
                    </div>
                    <div class="text-end">
                        <fmt:formatNumber value="55000" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Chart -->
    <div class="chart-container">
        <div id="chartLoading" class="loading-overlay" style="display: none;">
            <div class="loading-spinner"></div>
        </div>
        <canvas id="revenueChart"></canvas>
    </div>

    <!-- Top Courses -->
    <div class="row mt-4">
        <div class="col-12">
            <h5>Khóa học nổi bật</h5>
            <div id="topCourses">
                <!-- Top courses will be dynamically inserted by JavaScript -->
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/expert-dashboard.js"></script>
<script>
    // Hàm format tiền tệ VND
    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    // Hàm cập nhật thống kê
    function updateStats(stats) {
        document.getElementById('todayRevenue').textContent = formatCurrency(stats.todayRevenue);
        document.getElementById('totalRevenue').textContent = formatCurrency(stats.totalRevenue);
        document.getElementById('totalOrders').textContent = stats.totalOrders;
        document.getElementById('totalAllTimeRevenue').textContent = formatCurrency(stats.totalAllTimeRevenue);

        const change = stats.comparedToLastPeriod;
        const changeElement = document.getElementById('revenueChange');
        if (change > 0) {
            changeElement.innerHTML = `<i class="fas fa-arrow-up text-success"></i> ${change}%`;
        } else if (change < 0) {
            changeElement.innerHTML = `<i class="fas fa-arrow-down text-danger"></i> ${change}%`;
        } else {
            changeElement.innerHTML = `<i class="fas fa-equals text-muted"></i> ${change}%`;
        }
    }

    // Xử lý sự kiện click nút filter
    document.querySelectorAll('.filter-buttons button').forEach(button => {
        button.addEventListener('click', function() {
            // Xóa active class từ tất cả các nút
            document.querySelectorAll('.filter-buttons button').forEach(btn => {
                btn.classList.remove('active', 'btn-primary');
                btn.classList.add('btn-outline-primary');
            });
            
            // Thêm active class cho nút được click
            this.classList.remove('btn-outline-primary');
            this.classList.add('active', 'btn-primary');
            
            // Lấy period từ data attribute
            const period = this.dataset.period;
            
            // Hiển thị loading state
            showLoading(true);
            
            // Gọi API với period tương ứng
            loadDashboardData(period);
        });
    });

    // Mặc định chọn filter "Ngày"
    document.querySelector('[data-period="day"]').click();
</script>
</body>
</html> 