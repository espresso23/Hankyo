// Khởi tạo biến global
let revenueChart;
const formatter = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    maximumFractionDigits: 0
});

// Khởi tạo biểu đồ
function initializeChart() {
    const ctx = document.getElementById('revenueChart').getContext('2d');
    
    if (revenueChart) {
        revenueChart.destroy();
    }

    revenueChart = new Chart(ctx, {
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
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return formatter.format(value);
                        }
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return formatter.format(context.raw);
                        }
                    }
                },
                legend: {
                    display: true,
                    position: 'top'
                }
            }
        }
    });
}

// Cập nhật dữ liệu biểu đồ
function updateChart(data) {
    if (!revenueChart) {
        initializeChart();
    }

    // Chuyển đổi dữ liệu
    const chartData = {
        labels: data.map(item => formatDate(item.period)),
        datasets: [{
            label: 'Doanh thu',
            data: data.map(item => item.amount),
            backgroundColor: 'rgba(111, 66, 193, 0.5)',
            borderColor: 'rgba(111, 66, 193, 1)',
            borderWidth: 1
        }]
    };

    // Cập nhật biểu đồ
    revenueChart.data = chartData;
    revenueChart.update();
}

// Hàm tính toán khoảng thời gian dựa trên period
function getDateRange(period) {
    const now = new Date();
    let startDate = new Date();
    let endDate = new Date();

    switch (period) {
        case 'day':
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'week':
            startDate.setDate(now.getDate() - 7);
            break;
        case 'month':
            startDate.setMonth(now.getMonth() - 1);
            break;
        case 'year':
            startDate.setFullYear(now.getFullYear() - 1);
            break;
        case 'all':
            startDate = new Date(2000, 0, 1); // Đặt ngày bắt đầu từ xa trong quá khứ
            break;
        default:
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
    }

    return {
        startDate: startDate.toISOString(),
        endDate: endDate.toISOString()
    };
}

// Hàm tải dữ liệu dashboard
async function loadDashboardData(period) {
    try {
        showLoading(true);
        const dateRange = getDateRange(period);
        
        // Lấy context path từ thẻ meta
        const contextPath = document.querySelector('meta[name="context-path"]').content;
        
        // Chuẩn bị dữ liệu request
        const requestData = {
            startDate: dateRange.startDate,
            endDate: dateRange.endDate,
            period: period
        };
        
        console.log('Loading dashboard data with:', requestData);
        
        // Gọi API lấy thống kê
        const statsResponse = await fetch(`${contextPath}/expert-dashboard/stats`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (!statsResponse.ok) {
            const errorText = await statsResponse.text();
            throw new Error(`HTTP error! status: ${statsResponse.status}, message: ${errorText}`);
        }
        const stats = await statsResponse.json();
        console.log('Received stats:', stats);
        updateStats(stats);
        
        // Gọi API lấy dữ liệu doanh thu cho biểu đồ
        const revenueResponse = await fetch(`${contextPath}/expert-dashboard/revenue`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (revenueResponse.ok) {
            const revenueData = await revenueResponse.json();
            console.log('Received revenue data:', revenueData);
            updateChart(revenueData);
        } else {
            console.error('Failed to load revenue data:', await revenueResponse.text());
        }
        
        // Gọi API lấy danh sách khóa học nổi bật
        const coursesResponse = await fetch(`${contextPath}/expert-dashboard/top-courses`);
        if (coursesResponse.ok) {
            const coursesData = await coursesResponse.json();
            console.log('Received courses data:', coursesData);
            updateTopCourses(coursesData);
        } else {
            console.error('Failed to load courses data:', await coursesResponse.text());
        }
        
        showLoading(false);
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showError('Có lỗi xảy ra khi tải dữ liệu. Chi tiết: ' + error.message);
        showLoading(false);
    }
}

// Cập nhật thống kê tổng quan
function updateStats(stats) {
    document.getElementById('totalRevenue').textContent = formatter.format(stats.totalRevenue);
    document.getElementById('totalOrders').textContent = `${stats.totalOrders} đơn hàng`;
    
    const changeElement = document.getElementById('revenueChange');
    const changeValue = stats.comparedToLastPeriod;
    const icon = changeValue >= 0 ? 'up' : 'down';
    const color = changeValue >= 0 ? 'success' : 'danger';
    
    changeElement.innerHTML = `
        <i class="fas fa-arrow-${icon}"></i> ${Math.abs(changeValue)}%
    `;
    changeElement.className = `text-${color}`;
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
    container.innerHTML = courses.map(course => `
        <div class="course-item">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h6>${course.title}</h6>
                    <small class="text-muted">${course.studentCount} học viên</small>
                </div>
                <div class="text-end">
                    <div>${formatter.format(course.totalRevenue)}</div>
                    <small class="text-warning">
                        ${Array(Math.round(course.rating)).fill('★').join('')}
                        ${Array(5 - Math.round(course.rating)).fill('☆').join('')}
                    </small>
                </div>
            </div>
        </div>
    `).join('');
}

// Helper function để format ngày
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    }).format(date);
}

// Hàm hiển thị/ẩn loading
function showLoading(show) {
    const loadingElements = document.querySelectorAll('.placeholder-glow');
    loadingElements.forEach(element => {
        if (show) {
            element.style.display = 'block';
        } else {
            element.style.display = 'none';
        }
    });
}

// Hàm hiển thị lỗi
function showError(message) {
    // Thêm code hiển thị thông báo lỗi tại đây
    alert(message);
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
        
        // Update period text
        const periodText = button.dataset.text;
        document.querySelector('.stat-card.purple h6').textContent = `Tổng doanh thu ${periodText}`;
        document.querySelector('.stat-card.blue h6').textContent = `Tổng đơn hàng ${periodText}`;
        
        // Load new data
        loadDashboardData(button.dataset.period);
    });
});

// Khởi tạo biểu đồ và load dữ liệu ban đầu
document.addEventListener('DOMContentLoaded', () => {
    initializeChart();
    const { startDate, endDate } = getDateRange('today');
    loadDashboardData('today');
}); 