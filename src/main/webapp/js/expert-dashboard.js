// Khởi tạo biến global
let revenueChart;
let currentPeriod = 'week';
const formatter = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
});

// Hàm load dữ liệu dashboard
async function loadDashboardData(startDate, endDate) {
    try {
        // Load thống kê tổng quan
        const statsResponse = await fetch(`/Hankyo/expert-dashboard/stats?startDate=${startDate}&endDate=${endDate}`);
        const stats = await statsResponse.json();
        updateStats(stats);

        // Load dữ liệu doanh thu
        const revenueResponse = await fetch(`/Hankyo/expert-dashboard/revenue?startDate=${startDate}&endDate=${endDate}`);
        const revenueData = await revenueResponse.json();
        updateRevenueChart(revenueData);


        // Load khóa học nổi bật
        const coursesResponse = await fetch('/Hankyo/expert-dashboard/top-courses');
        const coursesData = await coursesResponse.json();
        updateTopCourses(coursesData);
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showError('Có lỗi xảy ra khi tải dữ liệu');
    }
}

// Cập nhật thống kê tổng quan
function updateStats(stats) {
    document.getElementById('totalRevenue').textContent = formatter.format(stats.totalRevenue);
    document.getElementById('totalOrders').textContent = `${stats.totalOrders} đơn hàng`;
    
    const changeElement = document.getElementById('revenueChange');
    const changeValue = stats.comparedToLastPeriod;
    changeElement.textContent = `${changeValue > 0 ? '+' : ''}${changeValue}%`;
    changeElement.className = `text-${changeValue >= 0 ? 'success' : 'danger'}`;
}

// Cập nhật biểu đồ doanh thu
function updateRevenueChart(data) {
    const ctx = document.getElementById('revenueChart').getContext('2d');
    
    if (revenueChart) {
        revenueChart.destroy();
    }

    const chartData = {
        labels: data.map(item => formatDate(item.period)),
        datasets: [{
            label: 'Đã thanh toán',
            data: data.filter(item => item.status === 'Completed')
                .map(item => item.amount),
            backgroundColor: '#6f42c1'
        }, {
            label: 'Hủy',
            data: data.filter(item => item.status === 'Cancelled')
                .map(item => item.amount),
            backgroundColor: '#dc3545'
        }, {
            label: 'Chờ thanh toán',
            data: data.filter(item => item.status === 'Pending')
                .map(item => item.amount),
            backgroundColor: '#ffc107'
        }]
    };

    revenueChart = new Chart(ctx, {
        type: 'bar',
        data: chartData,
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    stacked: true
                },
                x: {
                    stacked: true
                }
            }
        }
    });
}

// Cập nhật thông tin kênh thanh toán
function updatePaymentChannels(data) {
    const container = document.getElementById('paymentChannels');
    container.innerHTML = '';

    Object.entries(data).forEach(([channel, amount]) => {
        const channelElement = document.createElement('div');
        channelElement.className = 'bank-info d-flex align-items-center justify-content-between';
        channelElement.innerHTML = `
            <div class="d-flex align-items-center">
                <img src="/images/${channel.toLowerCase()}.png" alt="${channel}">
                <div>
                    <div>Kênh: ${channel}</div>
                </div>
            </div>
            <div>${formatter.format(amount)}</div>
        `;
        container.appendChild(channelElement);
    });
}

// Cập nhật danh sách khóa học nổi bật
function updateTopCourses(courses) {
    const container = document.getElementById('topCourses');
    container.innerHTML = '';

    courses.forEach(course => {
        const courseElement = document.createElement('div');
        courseElement.className = 'course-item d-flex justify-content-between align-items-center mb-3';
        courseElement.innerHTML = `
            <div>
                <h6>${course.title}</h6>
                <small class="text-muted">${course.studentCount} học viên</small>
            </div>
            <div class="text-end">
                <div>${formatter.format(course.totalSales)}</div>
                <div class="text-warning">
                    ${'★'.repeat(Math.round(course.rating))}
                    ${'☆'.repeat(5 - Math.round(course.rating))}
                </div>
            </div>
        `;
        container.appendChild(courseElement);
    });
}

// Xử lý filter thời gian
document.querySelectorAll('.time-filter button').forEach(button => {
    button.addEventListener('click', () => {
        // Remove active class from all buttons
        document.querySelectorAll('.time-filter button').forEach(btn => {
            btn.classList.remove('active');
        });
        
        // Add active class to clicked button
        button.classList.add('active');
        
        // Get date range based on selected period
        const { startDate, endDate } = getDateRange(button.dataset.period);
        currentPeriod = button.dataset.period;
        
        // Load new data
        loadDashboardData(startDate.toISOString(), endDate.toISOString());
    });
});

// Helper function to get date range based on period
function getDateRange(period) {
    const end = new Date();
    let start = new Date();

    switch (period) {
        case 'yesterday':
            start.setDate(start.getDate() - 1);
            end.setDate(end.getDate() - 1);
            break;
        case 'today':
            break;
        case 'week':
            start.setDate(start.getDate() - 7);
            break;
        case 'month':
            start.setMonth(start.getMonth() - 1);
            break;
        case 'year':
            start.setFullYear(start.getFullYear() - 1);
            break;
    }

    return { startDate: start, endDate: end };
}

// Helper function to format dates
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('vi-VN', {
        month: '2-digit',
        day: '2-digit',
        weekday: 'short'
    }).format(date);
}

// Show error message
function showError(message) {
    const alertElement = document.createElement('div');
    alertElement.className = 'alert alert-danger alert-dismissible fade show';
    alertElement.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.querySelector('.main-content').prepend(alertElement);
}

// Load initial data
document.addEventListener('DOMContentLoaded', () => {
    const { startDate, endDate } = getDateRange('week');
    loadDashboardData(startDate.toISOString(), endDate.toISOString());
}); 