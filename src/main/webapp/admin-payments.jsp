<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <title>Quản lý thanh toán</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { background: #f8f9fa; }
        .nav-item { margin: 5px 0 5px 0px; }
        .sidebar { min-height: 100vh; background: #fff; color: #5f72bd; border-right: 1px solid #e3e3e3; border-radius: 20px; }
        .sidebar .nav-link { color: #5f72bd; font-weight: 500; border-radius: 8px; }
        .sidebar .nav-link.active, .sidebar .nav-link:hover { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; }
        .main-content { padding: 20px; background: #f8f9fa; border-radius: 20px; }
        .header-gradient { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; border-radius: 12px; padding: 24px 32px; margin-bottom: 32px; box-shadow: 0 4px 24px rgba(106,130,251,0.08); }
        .stats-card { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
        .stats-card h3 { font-size: 24px; margin-bottom: 10px; }
        .stats-card .value { font-size: 32px; font-weight: bold; color: #5f72bd; }
        .stats-card .trend { font-size: 14px; }
        .trend.up { color: #28a745; }
        .trend.down { color: #dc3545; }
        .chart-container { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
        .table-row-hover:hover { background: #f0f4ff !important; }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="admin-sidebar.jsp">
            <jsp:param name="active" value="payments"/>
        </jsp:include>

        <!-- Main content -->
        <div class="col-md-9 col-lg-10 main-content">
            <div class="header-gradient mb-4 d-flex align-items-center justify-content-between">
                <h2 class="mb-0">Quản lý thanh toán</h2>
                <div>
                    <button class="btn btn-light me-2" onclick="exportToExcel()">
                        <i class="bi bi-file-excel"></i> Xuất Excel
                    </button>
                    <button class="btn btn-light" onclick="showWithdrawModal()">
                        <i class="bi bi-cash"></i> Xử lý rút tiền
                    </button>
                </div>
            </div>

            <!-- Stats Cards -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>Tổng giao dịch hôm nay</h3>
                        <div class="value" id="todayTransactions">0</div>
                        <div class="trend" id="todayTransactionsTrend"></div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>Doanh thu hôm nay</h3>
                        <div class="value" id="todayRevenue">0₫</div>
                        <div class="trend" id="todayRevenueTrend"></div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>Yêu cầu rút tiền</h3>
                        <div class="value" id="pendingWithdrawals">0</div>
                        <div class="trend">Đang chờ xử lý</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>Tổng doanh thu</h3>
                        <div class="value" id="totalRevenue">0₫</div>
                        <div class="trend" id="totalRevenueTrend"></div>
                    </div>
                </div>
            </div>

            <!-- Charts -->
            <div class="row mb-4">
                <div class="col-md-8">
                    <div class="mb-2">
                        <select id="revenueMode" class="form-select w-auto d-inline" onchange="loadChartData()">
                            <option value="day">Theo ngày</option>
                            <option value="week" selected>Theo tuần</option>
                            <option value="month">Theo tháng</option>
                            <option value="year">Theo năm</option>
                        </select>
                    </div>
                    <div class="chart-container">
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="chart-container">
                        <canvas id="transactionTypeChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Filters -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3">
                            <input type="text" class="form-control" id="searchInput" placeholder="Tìm kiếm...">
                        </div>
                        <div class="col-md-3">
                            <select class="form-select" id="statusFilter">
                                <option value="">Tất cả trạng thái</option>
                                <option value="completed">Hoàn thành</option>
                                <option value="pending">Đang xử lý</option>
                                <option value="failed">Thất bại</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" id="dateRange" placeholder="Chọn khoảng thời gian">
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-primary w-100" onclick="applyFilters()">Lọc</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Transactions Table -->
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Người dùng</th>
                                <th>Loại</th>
                                <th>Số tiền</th>
                                <th>Ngày giao dịch</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody id="paymentTable"></tbody>
                    </table>
                    <div id="pagination"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Withdraw Request Modal -->
<div class="modal fade" id="withdrawModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xử lý yêu cầu rút tiền</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Expert</th>
                            <th>Số tiền</th>
                            <th>Ngân hàng</th>
                            <th>STK</th>
                            <th>Ngày yêu cầu</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="withdrawTable"></tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/moment/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
<script>
let withdrawModal;
let revenueChart;
let transactionTypeChart;
let allPayments = [];
let currentPage = 1;
const pageSize = 10;

document.addEventListener('DOMContentLoaded', function() {
    withdrawModal = new bootstrap.Modal(document.getElementById('withdrawModal'));
    
    // Initialize date range picker
    $('#dateRange').daterangepicker({
        opens: 'left',
        locale: {
            format: 'DD/MM/YYYY'
        }
    });

    // Load initial data
    loadDashboardStats();
    loadTransactions();
    loadWithdrawRequests();
    initializeCharts();
    // Gọi loadChartData với mode mặc định là week
    if (document.getElementById('revenueMode')) {
        document.getElementById('revenueMode').value = 'week';
        loadChartData();
    }
});

function loadDashboardStats() {
    fetch('/Hankyo/admin/payments/stats')
        .then(res => res.json())
        .then(data => {
            document.getElementById('todayTransactions').textContent = data.todayTransactions;
            document.getElementById('todayRevenue').textContent = formatCurrency(data.todayRevenue);
            document.getElementById('pendingWithdrawals').textContent = data.pendingWithdrawals;
            document.getElementById('totalRevenue').textContent = formatCurrency(data.totalRevenue);
            
            // Update trends
            updateTrend('todayTransactionsTrend', data.todayTransactionsTrend);
            updateTrend('todayRevenueTrend', data.todayRevenueTrend);
            updateTrend('totalRevenueTrend', data.totalRevenueTrend);
        });
}

function loadTransactions() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const status = document.getElementById('statusFilter').value;
    const dateRange = $('#dateRange').data('daterangepicker');

    fetch('/Hankyo/admin/payments/all')
        .then(res => {
            // In ra status và content-type để kiểm tra
            console.log('Status:', res.status);
            console.log('Content-Type:', res.headers.get('content-type'));
            return res.json().catch(e => {
                // Nếu không parse được JSON, in ra text để xem lỗi gì
                res.text().then(txt => {
                    console.error('Không parse được JSON, response:', txt);
                });
                throw e;
            });
        })
        .then(data => {
            console.log('API trả về:', data);
            if (!Array.isArray(data)) {
                console.error('API không trả về mảng!');
                return;
            }
            allPayments = data; // BỎ FILTER ĐỂ TEST
            currentPage = 1;
            renderPaymentTable();
            renderPagination();
        })
        .catch(err => {
            console.error('Lỗi khi fetch hoặc xử lý dữ liệu:', err);
        });
}

function loadWithdrawRequests() {
    fetch('/Hankyo/admin/withdraw-requests')
        .then(res => res.json())
        .then(data => {
            const tb = document.getElementById('withdrawTable');
            tb.innerHTML = '';
            data.forEach(function(w) {
                tb.innerHTML += '<tr>'
                    + '<td>' + w.requestID + '</td>'
                    + '<td>' + w.expertName + '</td>'
                    + '<td>' + formatCurrency(w.amount) + '</td>'
                    + '<td>' + w.bankName + '</td>'
                    + '<td>' + w.bankAccount + '</td>'
                    + '<td>' + formatDate(w.requestDate) + '</td>'
                    + '<td>' + getStatusBadge(w.status) + '</td>'
                    + '<td>'
                    + (w.status === 'PENDING' ? 
                        '<button class="btn btn-sm btn-success me-1" onclick="processWithdraw(' + w.requestID + ', true)"><i class="bi bi-check"></i></button>' +
                        '<button class="btn btn-sm btn-danger" onclick="processWithdraw(' + w.requestID + ', false)"><i class="bi bi-x"></i></button>' 
                        : '')
                    + '</td>'
                    + '</tr>';
            });
        });
}

function initializeCharts() {
    // Revenue Chart
    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
    revenueChart = new Chart(revenueCtx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Doanh thu',
                data: [],
                borderColor: '#6a82fb',
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Biểu đồ doanh thu'
                }
            }
        }
    });

    // Transaction Type Chart (chỉ 2 loại: course, vip)
    const typeCtx = document.getElementById('transactionTypeChart').getContext('2d');
    transactionTypeChart = new Chart(typeCtx, {
        type: 'doughnut',
        data: {
            labels: ['Khóa học', 'VIP'],
            datasets: [{
                data: [0, 0],
                backgroundColor: ['#6a82fb', '#fc5c7d']
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Phân loại doanh thu'
                }
            }
        }
    });

    // Load chart data
    loadChartData();
}

function loadChartData() {
    const mode = document.getElementById('revenueMode') ? document.getElementById('revenueMode').value : 'day';
    fetch('/Hankyo/admin/payments/chart-data?mode=' + mode)
        .then(res => res.json())
        .then(data => {
            revenueChart.data.labels = data.revenue.labels;
            revenueChart.data.datasets[0].data = data.revenue.data;
            revenueChart.update();
            transactionTypeChart.data.datasets[0].data = [
                data.typeRevenue.course,
                data.typeRevenue.vip
            ];
            transactionTypeChart.update();
        });
}

function showWithdrawModal() {
    loadWithdrawRequests();
    withdrawModal.show();
}

function processWithdraw(requestId, approve) {
    if (!confirm(approve ? 'Xác nhận duyệt yêu cầu rút tiền?' : 'Xác nhận từ chối yêu cầu rút tiền?')) {
        return;
    }

    fetch('/Hankyo/admin/withdraw-requests/' + requestId + '/process', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            approve: approve
        })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            loadWithdrawRequests();
            loadDashboardStats();
        } else {
            alert('Có lỗi xảy ra: ' + data.message);
        }
    });
}

function applyFilters() {
    loadTransactions();
}

function exportToExcel() {
    const dateRange = $('#dateRange').data('daterangepicker');
    let url = '/Hankyo/admin/payments/export';
    const params = new URLSearchParams();
    if (dateRange) {
        params.append('startDate', dateRange.startDate.format('YYYY-MM-DD'));
        params.append('endDate', dateRange.endDate.format('YYYY-MM-DD'));
    }
    
    window.location.href = url + '?' + params.toString();
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function formatDate(date) {
    return new Date(date).toLocaleString('vi-VN');
}

function getStatusBadge(status) {
    const badges = {
        'completed': '<span class="badge bg-success">Hoàn thành</span>',
        'pending': '<span class="badge bg-warning">Đang xử lý</span>',
        'failed': '<span class="badge bg-danger">Thất bại</span>',
        'PENDING': '<span class="badge bg-warning">Chờ duyệt</span>',
        'APPROVED': '<span class="badge bg-success">Đã duyệt</span>',
        'REJECTED': '<span class="badge bg-danger">Từ chối</span>'
    };
    return badges[status] || status;
}

function updateTrend(elementId, trend) {
    const element = document.getElementById(elementId);
    if (trend > 0) {
        element.innerHTML = '<i class="bi bi-arrow-up"></i> ' + trend + '%';
        element.className = 'trend up';
    } else if (trend < 0) {
        element.innerHTML = '<i class="bi bi-arrow-down"></i> ' + Math.abs(trend) + '%';
        element.className = 'trend down';
    } else {
        element.innerHTML = '0%';
        element.className = 'trend';
    }
}

function renderPaymentTable() {
    const tb = document.getElementById('paymentTable');
    tb.innerHTML = '';
    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const pageData = allPayments.slice(start, end);
    pageData.forEach(function(p) {
        // Badge cho loại giao dịch
        let typeBadge = '';
        if (p.type === 'course') {
            typeBadge = '<span class="badge bg-primary"><i class="bi bi-journal-bookmark"></i> Khóa học</span>';
        } else if (p.type === 'vip') {
            typeBadge = '<span class="badge bg-warning text-dark"><i class="bi bi-star-fill"></i> VIP</span>';
        } else {
            typeBadge = '<span class="badge bg-secondary">' + (p.type || '') + '</span>';
        }
        // Badge cho trạng thái
        let statusBadge = getStatusBadge(p.status);
        tb.innerHTML += '<tr class="align-middle table-row-hover">'
            + '<td>' + (p.paymentID || '') + '</td>'
            + '<td>' + (p.learnerName || p.learnerID || '') + '</td>'
            + '<td class="text-center">' + typeBadge + '</td>'
            + '<td class="fw-bold text-end">' + formatCurrency(p.totalAmount) + '</td>'
            + '<td>' + formatDate(p.payDate) + '</td>'
            + '<td class="text-center">' + statusBadge + '</td>'
            + '<td class="text-center">'
            + '<button class="btn btn-sm btn-info me-1" onclick="viewDetails(\'' + p.paymentID + '\')"><i class="bi bi-eye"></i></button>'
            + '</td>'
            + '</tr>';
    });
}

function renderPagination() {
    const totalPages = Math.ceil(allPayments.length / pageSize);
    let html = '';
    if (totalPages <= 1) {
        document.getElementById('pagination').innerHTML = '';
        return;
    }
    html += '<nav><ul class="pagination justify-content-center">';
    for (let i = 1; i <= totalPages; i++) {
        html += '<li class="page-item' + (i === currentPage ? ' active' : '') + '">';
        html += '<a class="page-link" href="#" onclick="gotoPage(' + i + ');return false;">' + i + '</a></li>';
    }
    html += '</ul></nav>';
    document.getElementById('pagination').innerHTML = html;
}

function gotoPage(page) {
    currentPage = page;
    renderPaymentTable();
    renderPagination();
}
</script>
</body>
</html> 
