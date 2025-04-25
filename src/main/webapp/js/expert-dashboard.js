// Khởi tạo biến global
let revenueChart = null;
const formatter = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    maximumFractionDigits: 0
});

// Khởi tạo biểu đồ
function initializeChart() {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) {
        console.error('Không tìm thấy canvas cho biểu đồ');
        return;
    }

    // Nếu đã có biểu đồ cũ, hủy nó đi
    if (revenueChart) {
        revenueChart.destroy();
    }

    // Dữ liệu mẫu
    const mockData = {
        labels: ['Th04-21 T2', 'Th04-22 T3', 'Th04-23 T4', 'Th04-24 T5', 'Th04-25 T6', 'Th04-26 T7', 'Th04-27 CN'],
        datasets: [
            {
                label: 'Đã thanh toán',
                data: [0, 2, 0, 0, 0, 8, 0],
                backgroundColor: 'rgb(99, 61, 227)',
                stack: 'Stack 0'
            },
            {
                label: 'Hủy',
                data: [0, 1, 0, 0, 0, 0, 0],
                backgroundColor: 'rgb(255, 73, 73)',
                stack: 'Stack 0'
            },
            {
                label: 'Chờ thanh toán',
                data: [0, 0, 0, 0, 0, 3, 0],
                backgroundColor: 'rgb(69, 123, 229)',
                stack: 'Stack 0'
            }
        ]
    };

    // Cấu hình biểu đồ
    const config = {
        type: 'bar',
        data: mockData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    stacked: true,
                    grid: {
                        display: false
                    }
                },
                y: {
                    stacked: true,
                    beginAtZero: true,
                    ticks: {
                        stepSize: 2
                    },
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        padding: 20
                    }
                }
            }
        }
    };

    // Tạo biểu đồ mới
    revenueChart = new Chart(ctx, config);
}

// Cập nhật dữ liệu biểu đồ
function updateChart(data) {
    if (!revenueChart) {
        initializeChart();
        return;
    }

    // Cập nhật dữ liệu mới
    revenueChart.data.labels = data.labels;
    revenueChart.data.datasets = data.datasets;
    revenueChart.update();
}

// Hàm tính toán khoảng thời gian dựa trên period
function getDateRange(period) {
    const now = new Date();
    let startDate = new Date();
    let endDate = new Date();

    switch (period) {
        case 'yesterday':
            startDate.setDate(now.getDate() - 1);
            startDate.setHours(0, 0, 0, 0);
            endDate.setDate(now.getDate() - 1);
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'today':
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'week':
            startDate.setDate(now.getDate() - 7);
            break;
        case 'month':
            startDate.setMonth(now.getMonth() - 1);
            break;
        case 'last-month':
            startDate.setMonth(now.getMonth() - 2);
            endDate.setMonth(now.getMonth() - 1);
            endDate.setDate(0);
            break;
        case 'year':
            startDate.setFullYear(now.getFullYear() - 1);
            break;
        case 'last-year':
            startDate.setFullYear(now.getFullYear() - 2);
            endDate.setFullYear(now.getFullYear() - 1);
            endDate.setMonth(11, 31);
            break;
        case 'all':
            startDate = new Date(2000, 0, 1); // Từ năm 2000
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

// Cập nhật thống kê tổng quan
function updateStats(stats) {
    try {
        // Cập nhật doanh thu
        const revenueAmount = document.querySelector('.revenue-amount');
        if (revenueAmount) {
            revenueAmount.textContent = formatter.format(stats.todayRevenue || 0);
        }

        // Cập nhật phần trăm thay đổi doanh thu
        const revenueChange = document.querySelector('.revenue-change');
        if (revenueChange) {
            const changeValue = stats.comparedToLastPeriod || 0;
            const icon = changeValue >= 0 ? 'up' : 'down';
            revenueChange.innerHTML = `
                <i class="fas fa-arrow-${icon}"></i> ${Math.abs(changeValue)}%
            `;
        }

        // Cập nhật số đơn hàng
        const ordersAmount = document.querySelector('.orders-amount');
        if (ordersAmount) {
            ordersAmount.textContent = `${stats.totalOrders || 0} đơn hàng`;
        }

        // Cập nhật phần trăm thay đổi đơn hàng
        const ordersChange = document.querySelector('.orders-change');
        if (ordersChange) {
            const orderChangeValue = stats.orderComparedToLastPeriod || 0;
            const icon = orderChangeValue >= 0 ? 'up' : 'down';
            ordersChange.innerHTML = `
                <i class="fas fa-arrow-${icon}"></i> ${Math.abs(orderChangeValue)}%
            `;
        }
    } catch (error) {
        console.error('Error updating stats:', error);
    }
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

// Hàm tải dữ liệu dashboard
async function loadDashboardData(period) {
    try {
        showLoading(true);
        const dateRange = getDateRange(period);
        
        // Lấy context path từ thẻ meta
        const contextPath = document.querySelector('meta[name="context-path"]')?.content || '';
        
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
            throw new Error(`HTTP error! status: ${statsResponse.status}`);
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

        if (!revenueResponse.ok) {
            throw new Error(`HTTP error! status: ${revenueResponse.status}`);
        }

        const revenueData = await revenueResponse.json();
        console.log('Received revenue data:', revenueData);
        updateChart(revenueData);
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showError(`Có lỗi xảy ra khi tải dữ liệu: ${error.message}`);
    } finally {
        showLoading(false);
    }
}

// Hàm tải và cập nhật khóa học nổi bật
async function loadTopCourses() {
    try {
        const courseLoading = document.querySelector('.course-loading');
        if (courseLoading) {
            courseLoading.style.display = 'block';
        }

        const contextPath = document.querySelector('meta[name="context-path"]')?.content || '';
        const coursesResponse = await fetch(`${contextPath}/expert-dashboard/top-courses`);
        
        if (!coursesResponse.ok) {
            throw new Error(`HTTP error! status: ${coursesResponse.status}`);
        }

        const coursesData = await coursesResponse.json();
        console.log('Received courses data:', coursesData);
        updateTopCourses(coursesData);
        
    } catch (error) {
        console.error('Error loading top courses:', error);
        showError(`Có lỗi xảy ra khi tải danh sách khóa học: ${error.message}`);
    } finally {
        const courseLoading = document.querySelector('.course-loading');
        if (courseLoading) {
            courseLoading.style.display = 'none';
        }
    }
}

// Khởi tạo khi trang được load
document.addEventListener('DOMContentLoaded', function() {
    // Khởi tạo biểu đồ với dữ liệu mẫu
    initializeChart();

    // Load khóa học nổi bật ngay khi trang được tải
    loadTopCourses();

    // Xử lý sự kiện click cho các nút filter
    const timeFilterButtons = document.querySelectorAll('.time-filter button');
    timeFilterButtons.forEach(button => {
        button.addEventListener('click', function() {
            timeFilterButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            
            const period = this.dataset.period;
            const periodText = this.dataset.text;
            
            // Cập nhật tiêu đề
            const revenueTitle = document.querySelector('.revenue-title');
            const ordersTitle = document.querySelector('.orders-title');
            
            if (revenueTitle) {
                revenueTitle.textContent = `Tổng doanh thu ${periodText}`;
            }
            if (ordersTitle) {
                ordersTitle.textContent = `Tổng đơn hàng ${periodText}`;
            }
            
            // Load dữ liệu mới
            loadDashboardData(period);
        });
    });

    // Chọn nút mặc định
    const defaultButton = document.querySelector('.time-filter button[data-period="today"]');
    if (defaultButton) {
        defaultButton.click();
    }
}); 