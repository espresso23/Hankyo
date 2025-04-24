<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expert Dashboard</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
        }
        
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        
        .stat-card {
            background: linear-gradient(135deg, #6f42c1 0%, #6610f2 100%);
            color: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
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
        }
        
        .time-filter button {
            padding: 8px 15px;
            border: 1px solid #dee2e6;
            border-radius: 20px;
            background: white;
            color: #6c757d;
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
        }
        
        .bank-info img {
            width: 40px;
            height: 40px;
            margin-right: 10px;
        }
        
        .chart-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            border: 1px solid #dee2e6;
        }

        .course-item {
            background: white;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 10px;
            border: 1px solid #dee2e6;
        }

        .clickable-card {
            cursor: pointer;
            transition: transform 0.2s;
        }

        .clickable-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h5 class="mb-4">${expert.fullName}</h5>
    <div class="nav flex-column">
        <a href="#" class="nav-link active">Tổng quan</a>
        <a href="#" class="nav-link">Giao dịch</a>
        <a href="#" class="nav-link">Ngân hàng</a>
        <a href="#" class="nav-link">Kênh thanh toán</a>
        <a href="#" class="nav-link">Tạo link thanh toán</a>
        <a href="#" class="nav-link">Thiết lập</a>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Time Filter -->
    <div class="time-filter">
        <button data-period="yesterday">Hôm qua</button>
        <button data-period="today">Hôm nay</button>
        <button data-period="week" class="active">Tuần này</button>
        <button data-period="month">Tháng này</button>
        <button data-period="last-month">Tháng trước</button>
        <button data-period="year">Năm nay</button>
        <button data-period="last-year">Năm trước</button>
        <button data-period="custom">Tùy chỉnh</button>
    </div>

    <!-- Stats Cards -->
    <div class="row">
        <div class="col-md-6">
            <div class="stat-card purple clickable-card" data-type="revenue">
                <h6>Tổng doanh thu tuần này</h6>
                <h3 id="totalRevenue">0 VND</h3>
                <small id="revenueChange" class="text-danger">0%</small>
            </div>
        </div>
        <div class="col-md-6">
            <div class="stat-card blue clickable-card" data-type="orders">
                <h6>Tổng đơn hàng tuần này</h6>
                <h3 id="totalOrders">0 đơn hàng</h3>
                <small id="orderChange" class="text-danger">0%</small>
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
                            <div>${expert.fullName}</div>
                            <div class="text-muted">6261 1569 16</div>
                        </div>
                    </div>
                    <div>
                        <div>0 VND</div>
                        <span class="badge bg-success">Đang kết nối</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <h5>Doanh thu theo kênh thanh toán</h5>
            <div id="paymentChannels">
                <!-- Payment channels will be inserted here -->
            </div>
        </div>
    </div>

    <!-- Chart -->
    <div class="chart-container">
        <canvas id="revenueChart"></canvas>
    </div>

    <!-- Top Courses -->
    <div class="row mt-4">
        <div class="col-12">
            <h5>Khóa học nổi bật</h5>
            <div id="topCourses">
                <!-- Top courses will be inserted here -->
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/expert-dashboard.js"></script>
</body>
</html> 