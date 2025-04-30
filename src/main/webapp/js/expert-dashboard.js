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

    // Cấu hình biểu đồ
    const config = {
        type: 'bar',
        data: {
            labels: [],
            datasets: [
                {
                    label: 'Doanh thu',
                    data: [],
                    backgroundColor: 'rgb(99, 61, 227)',
                    borderColor: 'rgb(99, 61, 227)',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    grid: {
                        display: false
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return formatter.format(value);
                        }
                    },
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return 'Doanh thu: ' + formatter.format(context.raw);
                        }
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
    console.log('Updating chart with data:', data);
    
    if (!revenueChart) {
        initializeChart();
    }

    if (!Array.isArray(data)) {
        console.error('Invalid data format for chart update');
        return;
    }

    // Nhóm dữ liệu theo ngày từ SQL query
    const groupedData = data.reduce((acc, item) => {
        // Lấy ngày từ timestamp
        const date = item.period.split('T')[0];
        if (!acc[date]) {
            acc[date] = {
                total: 0,
                completed: 0
            };
        }
        
        // Tính tổng doanh thu cho tất cả trạng thái
        acc[date].total += parseFloat(item.totalAmount) || 0;
        
        // Tính riêng doanh thu từ đơn hàng đã hoàn thành
        if (item.status === 'Completed') {
            acc[date].completed += parseFloat(item.totalAmount) || 0;
        }
        
        return acc;
    }, {});

    // Sắp xếp ngày tăng dần
    const sortedDates = Object.keys(groupedData).sort();
    
    // Chuẩn bị dữ liệu cho biểu đồ
    const revenueData = sortedDates.map(date => groupedData[date].completed);

    // Format ngày hiển thị theo định dạng Việt Nam
    const formattedDates = sortedDates.map(date => {
        return new Intl.DateTimeFormat('vi-VN', {
            day: '2-digit',
            month: '2-digit'
        }).format(new Date(date));
    });

    // Cập nhật dữ liệu biểu đồ
    revenueChart.data.labels = formattedDates;
    revenueChart.data.datasets[0].data = revenueData;

    // Thêm dataset cho tổng doanh thu
    revenueChart.data.datasets = [{
        label: 'Doanh thu hoàn thành',
        data: revenueData,
        backgroundColor: 'rgb(99, 61, 227)',
        borderColor: 'rgb(99, 61, 227)',
        borderWidth: 1
    }];

    console.log('Chart data updated:', {
        labels: formattedDates,
        revenue: revenueData
    });

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
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'month':
            startDate.setMonth(now.getMonth(), 1); // Ngày đầu tháng
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'last-month':
            startDate.setMonth(now.getMonth() - 1, 1); // Ngày đầu tháng trước
            startDate.setHours(0, 0, 0, 0);
            endDate.setMonth(now.getMonth(), 0); // Ngày cuối tháng trước
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'year':
            startDate.setMonth(0, 1); // Ngày đầu năm
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'last-year':
            startDate.setFullYear(now.getFullYear() - 1, 0, 1); // Ngày đầu năm trước
            startDate.setHours(0, 0, 0, 0);
            endDate.setFullYear(now.getFullYear() - 1, 11, 31); // Ngày cuối năm trước
            endDate.setHours(23, 59, 59, 999);
            break;
        case 'all':
            startDate = new Date(2000, 0, 1);
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
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
        console.log('Raw stats data:', stats);
        if (!stats) {
            console.error('Stats data is null or undefined');
            return;
        }

        // Cập nhật doanh thu
        const revenueAmount = document.querySelector('.revenue-amount');
        if (revenueAmount) {
            const revenue = stats.totalRevenue || 0;
            console.log('Updating revenue amount:', revenue);
            revenueAmount.textContent = formatter.format(revenue);
        } else {
            console.error('Revenue amount element not found');
        }

        // Cập nhật phần trăm thay đổi doanh thu
        const revenueChange = document.querySelector('.revenue-change');
        if (revenueChange) {
            const changeValue = stats.comparedToLastPeriod || 0;
            console.log('Revenue change value:', changeValue);
            const icon = changeValue >= 0 ? 'up' : 'down';
            revenueChange.innerHTML = `
                <i class="fas fa-arrow-${icon}"></i> ${Math.abs(changeValue).toFixed(1)}%
            `;
            revenueChange.classList.remove('text-success', 'text-danger');
            revenueChange.classList.add(changeValue >= 0 ? 'text-success' : 'text-danger');
        } else {
            console.error('Revenue change element not found');
        }

        // Cập nhật số đơn hàng
        const ordersAmount = document.querySelector('.orders-amount');
        if (ordersAmount) {
            const orders = stats.totalOrders || 0;
            console.log('Updating orders amount:', orders);
            ordersAmount.textContent = `${orders} đơn hàng`;
        } else {
            console.error('Orders amount element not found');
        }

        // Cập nhật phần trăm thay đổi đơn hàng
        const ordersChange = document.querySelector('.orders-change');
        if (ordersChange) {
            const orderChangeValue = stats.orderComparedToLastPeriod || 0;
            console.log('Orders change value:', orderChangeValue);
            const icon = orderChangeValue >= 0 ? 'up' : 'down';
            ordersChange.innerHTML = `
                <i class="fas fa-arrow-${icon}"></i> ${Math.abs(orderChangeValue).toFixed(1)}%
            `;
            ordersChange.classList.remove('text-success', 'text-danger');
            ordersChange.classList.add(orderChangeValue >= 0 ? 'text-success' : 'text-danger');
        } else {
            console.error('Orders change element not found');
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

// Load dữ liệu dashboard
async function loadDashboardData(period) {
    try {
        console.log('Loading data for period:', period);
        showLoading(true);
        
        // Lấy context path từ meta tag
        const contextPath = document.querySelector('meta[name="context-path"]')?.content || '';
        const dateRange = getDateRange(period);
        
        // Format ngày tháng theo ISO string và encode URI
        const startDate = encodeURIComponent(dateRange.startDate);
        const endDate = encodeURIComponent(dateRange.endDate);
        
        // Gọi API revenue để lấy dữ liệu cho biểu đồ
        const revenueUrl = `${contextPath}/expert-dashboard/revenue`;
        console.log('Revenue request URL:', revenueUrl);
        
        const revenueResponse = await fetch(revenueUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                startDate: dateRange.startDate,
                endDate: dateRange.endDate
            }),
            credentials: 'same-origin'
        });
        
        if (!revenueResponse.ok) {
            const errorText = await revenueResponse.text();
            console.error('Revenue server response:', revenueResponse.status, errorText);
            throw new Error(`HTTP error! status: ${revenueResponse.status}`);
        }
        
        const revenueData = await revenueResponse.json();
        console.log('Received revenue data:', revenueData);
        
        // Gọi API stats để lấy thống kê tổng quan
        const statsUrl = `${contextPath}/expert-dashboard/stats?startDate=${startDate}&endDate=${endDate}`;
        console.log('Stats request URL:', statsUrl);
        
        const statsResponse = await fetch(statsUrl, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            credentials: 'same-origin'
        });
        
        if (!statsResponse.ok) {
            const errorText = await statsResponse.text();
            console.error('Stats server response:', statsResponse.status, errorText);
            throw new Error(`HTTP error! status: ${statsResponse.status}`);
        }
        
        const statsData = await statsResponse.json();
        console.log('Received stats data:', statsData);
        
        // Cập nhật thống kê
        if (statsData.stats) {
            console.log('Updating stats with:', statsData.stats);
            updateStats(statsData.stats);
        }
        
        // Cập nhật biểu đồ với dữ liệu doanh thu
        if (Array.isArray(revenueData)) {
            console.log('Updating chart with revenue data:', revenueData);
            updateChart(revenueData);
        }
        
        // Cập nhật top khóa học
        if (statsData.topCourses) {
            console.log('Updating top courses with:', statsData.topCourses);
            updateTopCourses(statsData.topCourses);
        }
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showError('Có lỗi xảy ra khi tải dữ liệu');
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
    console.log('DOM Content Loaded');
    
    // Chỉ khởi tạo cấu trúc biểu đồ, không có dữ liệu mẫu
    initializeChart();
    console.log('Empty chart initialized');

    // Load khóa học nổi bật ngay khi trang được tải
    loadTopCourses();

    // Xử lý sự kiện click cho các nút filter
    const timeFilterButtons = document.querySelectorAll('.time-filter button');
    timeFilterButtons.forEach(button => {
        button.addEventListener('click', function() {
            console.log('Filter button clicked:', this.dataset.period);
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
        console.log('Clicking default button (today)');
        defaultButton.click();
    }
}); 