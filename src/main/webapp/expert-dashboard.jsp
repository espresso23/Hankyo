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
    <h5 class="mb-4">${expert.fullName}</h5>
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
    <!-- Time Filter -->
    <div class="time-filter">
        <button data-period="yesterday">Hôm qua</button>
        <button data-period="today" class="active">Hôm nay</button>
        <button data-period="week">Tuần này</button>
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
                <h6>Tổng doanh thu hôm nay</h6>
                <h3 id="totalRevenue">0 VND</h3>
                <small id="revenueChange" class="text-white"><i class="fas fa-arrow-up"></i> 0%</small>
            </div>
        </div>
        <div class="col-md-6">
            <div class="stat-card blue clickable-card" data-type="orders">
                <h6>Tổng đơn hàng hôm nay</h6>
                <h3 id="totalOrders">0 đơn hàng</h3>
                <small id="orderChange" class="text-white"><i class="fas fa-arrow-up"></i> 0%</small>
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
                        <div class="text-end">55,000 VND</div>
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
                        55,000 VND
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
                <!-- Top courses will be inserted here -->
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
// Format currency function
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

// Initialize chart
const ctx = document.getElementById('revenueChart').getContext('2d');
const revenueChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: [],
        datasets: [{
            label: 'Doanh thu',
            data: [],
            backgroundColor: 'rgba(111, 66, 193, 0.5)',
            borderColor: 'rgba(111, 66, 193, 1)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback: function(value) {
                        return formatCurrency(value);
                    }
                }
            }
        },
        plugins: {
            tooltip: {
                callbacks: {
                    label: function(context) {
                        return formatCurrency(context.raw);
                    }
                }
            }
        }
    }
});

// Time filter handling
document.querySelectorAll('.time-filter button').forEach(button => {
    button.addEventListener('click', function() {
        // Remove active class from all buttons
        document.querySelectorAll('.time-filter button').forEach(btn => btn.classList.remove('active'));
        // Add active class to clicked button
        this.classList.add('active');
        // Update period text in stats cards
        document.querySelector('.stat-card.purple h6').textContent = `Tổng doanh thu ${this.textContent.toLowerCase()}`;
        document.querySelector('.stat-card.blue h6').textContent = `Tổng đơn hàng ${this.textContent.toLowerCase()}`;
        // Fetch new data
        fetchData(this.dataset.period);
    });
});

// Fetch data function
function fetchData(period) {
    // Show loading states
    document.getElementById('chartLoading').style.display = 'flex';
    document.querySelectorAll('.stat-card, .bank-info').forEach(el => el.classList.add('loading'));

    // Simulate API call
    setTimeout(() => {
        // Hide loading states
        document.getElementById('chartLoading').style.display = 'none';
        document.querySelectorAll('.stat-card, .bank-info').forEach(el => el.classList.remove('loading'));
        
        // Update data
        document.getElementById('totalRevenue').textContent = formatCurrency(55000);
        document.getElementById('totalOrders').textContent = '11 đơn hàng';
    }, 1000);
}

// Initial data fetch
fetchData('today');
</script>
</body>
</html> 