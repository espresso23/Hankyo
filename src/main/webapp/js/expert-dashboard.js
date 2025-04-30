// Khởi tạo biến global
let revenueChart = null;
const formatter = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    maximumFractionDigits: 0
});

// Thêm biến để track trạng thái loading
let isLoading = false;

// Hàm hủy biểu đồ cũ
async function destroyChart() {
    try {
        if (revenueChart) {
            await revenueChart.destroy();
            revenueChart = null;
        }
    } catch (error) {
        console.error('Error destroying chart:', error);
    }
}

// Khởi tạo biểu đồ
async function initializeChart() {
    console.log('Initializing chart...');
    try {
        await destroyChart();
        
        const ctx = document.getElementById('revenueChart');
        if (!ctx) {
            console.error('Không tìm thấy canvas cho biểu đồ');
            return;
        }

        // Đợi một frame để đảm bảo DOM đã được cập nhật
        await new Promise(requestAnimationFrame);

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
                        borderWidth: 1,
                        yAxisID: 'y'
                    },
                    {
                        label: 'Số đơn hàng',
                        data: [],
                        backgroundColor: 'rgba(255, 99, 132, 0.8)',
                        borderColor: 'rgb(255, 99, 132)',
                        borderWidth: 1,
                        type: 'line',
                        yAxisID: 'y1'
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
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Số đơn hàng'
                        },
                        ticks: {
                            stepSize: 1,
                            callback: function(value) {
                                return Math.round(value);
                            }
                        },
                        grid: {
                            drawOnChartArea: false
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        align: 'center',
                        labels: {
                            usePointStyle: true,
                            padding: 20
                        }
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false,
                        callbacks: {
                            title: function(tooltipItems) {
                                const date = new Date(revenueChart.data.datasets[tooltipItems[0].datasetIndex].data[tooltipItems[0].dataIndex].date);
                                return date.toLocaleDateString('vi-VN', {
                                    weekday: 'long',
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                });
                            },
                            label: function(context) {
                                if (context.dataset.label === 'Doanh thu') {
                                    return `Doanh thu: ${formatter.format(context.raw)}`;
                                } else {
                                    return `Số đơn hàng: ${context.raw}`;
                                }
                            }
                        }
                    }
                }
            }
        };

        revenueChart = new Chart(ctx, config);
        console.log('Chart initialized successfully');
    } catch (error) {
        console.error('Error initializing chart:', error);
    }
}

// Hàm validate dữ liệu
function validateChartData(data) {
    if (!Array.isArray(data)) {
        console.error('Data must be an array');
        return false;
    }

    // Log chi tiết về dữ liệu đầu vào
    console.log('Validating data:', {
        length: data.length,
        sampleItems: data.slice(0, 3)
    });

    // Kiểm tra từng item trong mảng
    const isValid = data.every((item, index) => {
        if (!item) {
            console.error(`Null or undefined item at index ${index}`);
            return false;
        }

        // Kiểm tra period hoặc orderDate
        const dateValue = item.period || item.orderDate;
        if (!dateValue) {
            console.error(`Missing date at index ${index}:`, item);
            return false;
        }

        // Kiểm tra period/orderDate có phải date hợp lệ
        const date = new Date(dateValue);
        if (isNaN(date.getTime())) {
            console.error(`Invalid date at index ${index}:`, dateValue);
            return false;
        }

        // Kiểm tra totalAmount
        const amount = parseFloat(item.totalAmount);
        if (isNaN(amount)) {
            console.error(`Invalid totalAmount at index ${index}:`, item.totalAmount);
            return false;
        }

        return true;
    });

    return isValid;
}

// Cập nhật dữ liệu biểu đồ
async function updateChart(data) {
    console.log('Updating chart with data:', data);
    
    if (!validateChartData(data)) {
        console.error('Invalid chart data format');
        return;
    }

    try {
        await destroyChart();
        await new Promise(requestAnimationFrame);

        const ctx = document.getElementById('revenueChart');
        if (!ctx) {
            console.error('Canvas element not found');
            return;
        }

        const { labels, chartData } = await processChartData(data);
        
        if (!chartData.length) {
            console.error('No valid data to display');
            return;
        }

        // Log dữ liệu trước khi vẽ biểu đồ
        console.log('Chart configuration data:', {
            labels,
            revenue: chartData.map(d => d.revenue),
            orders: chartData.map(d => d.orders)
        });

        const config = {
            type: 'bar',
            data: {
                labels,
                datasets: [
                    {
                        label: 'Doanh thu',
                        data: chartData.map(item => item.revenue),
                        backgroundColor: 'rgb(99, 61, 227)',
                        borderColor: 'rgb(99, 61, 227)',
                        borderWidth: 1,
                        yAxisID: 'y',
                        order: 1
                    },
                    {
                        label: 'Số đơn hàng',
                        data: chartData.map(item => item.orders),
                        backgroundColor: 'rgba(255, 99, 132, 0.8)',
                        borderColor: 'rgb(255, 99, 132)',
                        borderWidth: 1,
                        type: 'line',
                        yAxisID: 'y1',
                        order: 0
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                interaction: {
                    mode: 'index',
                    intersect: false
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Doanh thu (VNĐ)'
                        },
                        ticks: {
                            callback: function(value) {
                                return formatter.format(value);
                            }
                        }
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        beginAtZero: true,
                        min: 0,
                        title: {
                            display: true,
                            text: 'Số đơn hàng'
                        },
                        ticks: {
                            stepSize: 1,
                            precision: 0
                        },
                        grid: {
                            drawOnChartArea: false
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        mode: 'index',
                        intersect: false,
                        callbacks: {
                            title: function(tooltipItems) {
                                if (!tooltipItems.length) return '';
                                const date = chartData[tooltipItems[0].dataIndex].date;
                                return date.toLocaleDateString('vi-VN', {
                                    weekday: 'long',
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                });
                            },
                            label: function(context) {
                                if (context.dataset.label === 'Doanh thu') {
                                    return `Doanh thu: ${formatter.format(context.raw)}`;
                                } else {
                                    return `Số đơn hàng: ${context.raw}`;
                                }
                            }
                        }
                    }
                }
            }
        };

        revenueChart = new Chart(ctx, config);
        console.log('Chart created successfully');
    } catch (error) {
        console.error('Error updating chart:', error);
    }
}

// Hàm xử lý dữ liệu cho chart
async function processChartData(data) {
    const period = document.querySelector('.time-filter button.active')?.dataset.period;
    const dateRange = getDateRange(period);
    const startDate = new Date(dateRange.startDate);
    const endDate = new Date(dateRange.endDate);

    console.log('Raw input data details:', {
        period,
        startDate: startDate.toLocaleString('vi-VN'),
        endDate: endDate.toLocaleString('vi-VN'),
        dataLength: data.length,
        rawData: data
    });

    // Khởi tạo object chứa dữ liệu theo ngày
    const groupedData = {};
    
    // Xử lý dữ liệu trước
    data.forEach((item, index) => {
        try {
            // Lấy giá trị ngày từ period hoặc orderDate
            const dateValue = item.period || item.orderDate;
            if (!item || !dateValue) {
                console.error(`Invalid item at index ${index}:`, item);
                return;
            }

            // Chuyển đổi ngày và chuẩn hóa về 00:00:00
            const itemDate = new Date(dateValue);
            itemDate.setHours(0, 0, 0, 0);
            
            if (isNaN(itemDate.getTime())) {
                console.error(`Invalid date at index ${index}:`, dateValue);
                return;
            }

            const dateKey = itemDate.toISOString().split('T')[0];
            
            // Kiểm tra ngày có trong khoảng cho phép
            const startDateOnly = new Date(startDate);
            startDateOnly.setHours(0, 0, 0, 0);
            const endDateOnly = new Date(endDate);
            endDateOnly.setHours(23, 59, 59, 999);

            if (itemDate >= startDateOnly && itemDate <= endDateOnly) {
                if (!groupedData[dateKey]) {
                    groupedData[dateKey] = {
                        date: itemDate,
                        revenue: 0,
                        orderCount: item.orderCount || 0, // Sử dụng orderCount từ API nếu có
                        totalAmount: item.totalAmount || 0 // Sử dụng totalAmount từ API
                    };
                } else {
                    // Nếu đã có dữ liệu cho ngày này, cập nhật nếu dữ liệu mới lớn hơn
                    if (item.orderCount > groupedData[dateKey].orderCount) {
                        groupedData[dateKey].orderCount = item.orderCount;
                    }
                    if (item.totalAmount > groupedData[dateKey].totalAmount) {
                        groupedData[dateKey].totalAmount = item.totalAmount;
                    }
                }

                // Log chi tiết xử lý từng item
                console.log(`Processing item for ${dateKey}:`, {
                    date: dateKey,
                    orderCount: item.orderCount,
                    totalAmount: item.totalAmount,
                    currentTotal: {
                        revenue: groupedData[dateKey].totalAmount,
                        orders: groupedData[dateKey].orderCount
                    }
                });
            } else {
                console.log(`Item out of range:`, {
                    itemDate: itemDate.toLocaleString('vi-VN'),
                    startDate: startDateOnly.toLocaleString('vi-VN'),
                    endDate: endDateOnly.toLocaleString('vi-VN')
                });
            }
        } catch (error) {
            console.error(`Error processing item ${index}:`, item, error);
        }
    });

    // Log chi tiết kết quả xử lý theo ngày
    Object.entries(groupedData).forEach(([date, data]) => {
        console.log(`Date ${date} summary:`, {
            orderCount: data.orderCount,
            totalAmount: data.totalAmount
        });
    });

    // Tạo mảng đầy đủ các ngày trong khoảng
    const dateArray = [];
    const tempDate = new Date(startDate);
    while (tempDate <= endDate) {
        const dateKey = tempDate.toISOString().split('T')[0];
        if (!groupedData[dateKey]) {
            groupedData[dateKey] = {
                date: new Date(tempDate),
                revenue: 0,
                orderCount: 0,
                totalAmount: 0
            };
        }
        dateArray.push(dateKey);
        tempDate.setDate(tempDate.getDate() + 1);
    }

    // Sắp xếp ngày và chuẩn bị dữ liệu cho biểu đồ
    const sortedDates = dateArray.sort();
    const chartData = sortedDates.map(dateKey => {
        const dayData = groupedData[dateKey];
        return {
            date: dayData.date,
            revenue: dayData.totalAmount || 0,  // Đảm bảo có giá trị mặc định
            orders: dayData.orderCount || 0     // Đảm bảo có giá trị mặc định
        };
    });

    // Log tổng kết cuối cùng
    const totalRevenue = chartData.reduce((sum, item) => sum + (item.revenue || 0), 0);
    const totalOrders = chartData.reduce((sum, item) => sum + (item.orders || 0), 0);
    
    console.log('Final processed data:', {
        totalDays: chartData.length,
        totalRevenue: totalRevenue,
        totalOrders: totalOrders,
        chartData: chartData
    });

    // Format nhãn dựa trên period
    const labels = chartData.map(item => {
        const date = item.date;
        switch (period) {
            case 'year':
                return date.toLocaleString('vi-VN', { month: 'long' });
            case 'month':
                return `${date.getDate()}/${date.getMonth() + 1}`;
            case 'week':
                return `${date.getDate()}/${date.getMonth() + 1}`;
            default:
                return `${date.getDate()}/${date.getMonth() + 1}`;
        }
    });

    // Cập nhật thống kê tổng quan
    updateStats({
        totalRevenue: totalRevenue,
        totalOrders: totalOrders,
        comparedToLastPeriod: 0,  // Sẽ được tính toán bởi backend
        orderComparedToLastPeriod: 0  // Sẽ được tính toán bởi backend
    });

    return { labels, chartData };
}

// Hàm tính toán khoảng thời gian dựa trên period
function getDateRange(period) {
    const now = new Date();
    let startDate = new Date();
    let endDate = new Date();

    // Đặt thời gian bắt đầu về đầu ngày và kết thúc về cuối ngày
    const setStartOfDay = (date) => {
        date.setHours(0, 0, 0, 0);
        return date;
    };

    const setEndOfDay = (date) => {
        date.setHours(23, 59, 59, 999);
        return date;
    };

    switch (period) {
        case 'yesterday':
            startDate = new Date(now);
            startDate.setDate(now.getDate() - 1);
            startDate = setStartOfDay(startDate);
            endDate = new Date(startDate);
            endDate = setEndOfDay(endDate);
            break;
            
        case 'today':
            startDate = setStartOfDay(new Date(now));
            endDate = setEndOfDay(new Date(now));
            break;
            
        case 'week':
            // Lấy ngày đầu tuần (Thứ 2)
            startDate = new Date(now);
            const day = startDate.getDay();
            const diff = startDate.getDate() - day + (day === 0 ? -6 : 1);
            startDate.setDate(diff);
            startDate = setStartOfDay(startDate);
            endDate = setEndOfDay(new Date(now));
            break;
            
        case 'month':
            // Lấy ngày đầu tháng hiện tại
            startDate = new Date(now.getFullYear(), now.getMonth(), 1);
            startDate = setStartOfDay(startDate);
            
            // Lấy ngày cuối tháng hiện tại
            endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);
            endDate = setEndOfDay(endDate);
            
            // Log chi tiết cho debug
            console.log('Month range calculation:', {
                currentDate: now.toLocaleString('vi-VN'),
                startDate: startDate.toLocaleString('vi-VN'),
                endDate: endDate.toLocaleString('vi-VN'),
                daysInMonth: endDate.getDate(),
                currentMonth: now.getMonth() + 1,
                startMonth: startDate.getMonth() + 1,
                endMonth: endDate.getMonth() + 1,
                startDateISO: startDate.toISOString(),
                endDateISO: endDate.toISOString()
            });
            break;
            
        case 'last-month':
            startDate = new Date(now.getFullYear(), now.getMonth() - 1, 1);
            startDate = setStartOfDay(startDate);
            endDate = new Date(now.getFullYear(), now.getMonth(), 0);
            endDate = setEndOfDay(endDate);
            break;
            
        case 'year':
            // Lấy ngày đầu năm hiện tại
            startDate = new Date(now.getFullYear(), 0, 1);
            startDate = setStartOfDay(startDate);
            // Lấy ngày cuối năm hiện tại (31/12)
            endDate = new Date(now.getFullYear(), 11, 31);
            endDate = setEndOfDay(endDate);
            
            // Log chi tiết cho debug
            console.log('Year range:', {
                startDate: startDate.toLocaleString('vi-VN'),
                endDate: endDate.toLocaleString('vi-VN'),
                totalDays: Math.floor((endDate - startDate) / (1000 * 60 * 60 * 24) + 1)
            });
            break;
            
        case 'last-year':
            startDate = new Date(now.getFullYear() - 1, 0, 1);
            startDate = setStartOfDay(startDate);
            endDate = new Date(now.getFullYear() - 1, 11, 31);
            endDate = setEndOfDay(endDate);
            break;
            
        case 'all':
            startDate = new Date(2000, 0, 1);
            startDate = setStartOfDay(startDate);
            endDate = setEndOfDay(new Date(now));
            break;
            
        case 'custom':
            // Xử lý custom date range nếu cần
            startDate = setStartOfDay(new Date(now));
            endDate = setEndOfDay(new Date(now));
            break;
            
        default:
            startDate = setStartOfDay(new Date(now));
            endDate = setEndOfDay(new Date(now));
    }

    // Log chi tiết về khoảng thời gian cuối cùng
    console.log('Final date range:', {
        period,
        startDate: {
            raw: startDate,
            iso: startDate.toISOString(),
            local: startDate.toLocaleString('vi-VN'),
            year: startDate.getFullYear(),
            month: startDate.getMonth() + 1,
            date: startDate.getDate(),
            hours: startDate.getHours(),
            minutes: startDate.getMinutes(),
            seconds: startDate.getSeconds()
        },
        endDate: {
            raw: endDate,
            iso: endDate.toISOString(),
            local: endDate.toLocaleString('vi-VN'),
            year: endDate.getFullYear(),
            month: endDate.getMonth() + 1,
            date: endDate.getDate(),
            hours: endDate.getHours(),
            minutes: endDate.getMinutes(),
            seconds: endDate.getSeconds()
        },
        totalDays: Math.floor((endDate - startDate) / (1000 * 60 * 60 * 24) + 1)
    });

    return {
        startDate: startDate.toISOString(),
        endDate: endDate.toISOString()
    };
}

// Cập nhật thống kê tổng quan
function updateStats(stats) {
    console.log('Updating stats with data:', stats);
    
    if (!stats) {
        console.error('Stats data is null or undefined');
        return;
    }

    try {
        // Cập nhật doanh thu - chỉ tính từ đơn hàng đã hoàn thành
        const revenueAmount = document.querySelector('.revenue-amount');
        if (revenueAmount) {
            const revenue = parseFloat(stats.totalRevenue) || 0;
            console.log('Revenue amount:', revenue);
            revenueAmount.textContent = formatter.format(revenue);
        }

        // Cập nhật phần trăm thay đổi doanh thu
        const revenueChange = document.querySelector('.revenue-change');
        if (revenueChange) {
            const changeValue = parseFloat(stats.comparedToLastPeriod) || 0;
            console.log('Revenue change:', changeValue);
            const icon = changeValue >= 0 ? 'up' : 'down';
            revenueChange.innerHTML = `
                <i class="fas fa-arrow-${icon}"></i> ${Math.abs(changeValue).toFixed(1)}%
            `;
            revenueChange.classList.remove('text-success', 'text-danger');
            revenueChange.classList.add(changeValue >= 0 ? 'text-success' : 'text-danger');
        }

        // Cập nhật số đơn hàng - tính tất cả các đơn
        const ordersAmount = document.querySelector('.orders-amount');
        if (ordersAmount) {
            const orders = parseInt(stats.totalOrders) || 0;
            console.log('Total orders:', orders);
            ordersAmount.textContent = `${orders} đơn hàng`;
        }

        // Cập nhật phần trăm thay đổi đơn hàng
        const ordersChange = document.querySelector('.orders-change');
        if (ordersChange) {
            const orderChangeValue = parseFloat(stats.orderComparedToLastPeriod) || 0;
            console.log('Orders change:', orderChangeValue);
            const icon = orderChangeValue >= 0 ? 'up' : 'down';
            ordersChange.innerHTML = `
                <i class="fas fa-arrow-${icon}"></i> ${Math.abs(orderChangeValue).toFixed(1)}%
            `;
            ordersChange.classList.remove('text-success', 'text-danger');
            ordersChange.classList.add(orderChangeValue >= 0 ? 'text-success' : 'text-danger');
        }

        // Cập nhật tiêu đề theo period
        const periodText = document.querySelector('.time-filter button.active')?.dataset.text || '';
        const revenueTitle = document.querySelector('.revenue-title');
        const ordersTitle = document.querySelector('.orders-title');
        if (revenueTitle) revenueTitle.textContent = `Tổng doanh thu ${periodText}`;
        if (ordersTitle) ordersTitle.textContent = `Tổng đơn hàng ${periodText}`;

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
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger alert-dismissible fade show';
    errorDiv.role = 'alert';
    errorDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // Thêm thông báo lỗi vào đầu main-content
    const mainContent = document.querySelector('.main-content');
    if (mainContent) {
        mainContent.insertBefore(errorDiv, mainContent.firstChild);
        
        // Tự động ẩn sau 5 giây
        setTimeout(() => {
            errorDiv.remove();
        }, 5000);
    }
}

// Cập nhật loadDashboardData
async function loadDashboardData(period) {
    // Kiểm tra nếu đang loading thì không thực hiện tiếp
    if (isLoading) {
        console.log('Dashboard data is already loading...');
        return;
    }

    try {
        isLoading = true;
        console.log('Loading dashboard data for period:', period);
        showLoading(true);

        // Đảm bảo chart cũ được hủy trước
        await destroyChart();

        const contextPath = document.querySelector('meta[name="context-path"]')?.content || '';
        const dateRange = getDateRange(period);

        // Gọi API revenue và đợi kết quả
        const revenueResponse = await fetch(`${contextPath}/expert-dashboard/revenue`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(dateRange),
            credentials: 'same-origin'
        });

        if (!revenueResponse.ok) {
            throw new Error(`Revenue API error: ${revenueResponse.status}`);
        }

        const revenueData = await revenueResponse.json();
        console.log('Revenue data received:', revenueData);

        // Gọi API stats và đợi kết quả
        const statsResponse = await fetch(
            `${contextPath}/expert-dashboard/stats?startDate=${encodeURIComponent(dateRange.startDate)}&endDate=${encodeURIComponent(dateRange.endDate)}`,
            {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                credentials: 'same-origin'
            }
        );

        if (!statsResponse.ok) {
            throw new Error(`Stats API error: ${statsResponse.status}`);
        }

        const statsData = await statsResponse.json();
        console.log('Stats data received:', statsData);

        // Cập nhật chart sau khi có dữ liệu
        if (Array.isArray(revenueData)) {
            await updateChart(revenueData);
        }

        // Cập nhật stats
        if (statsData && statsData.stats) {
            updateStats(statsData.stats);
        }

        // Cập nhật top khóa học
        if (statsData.topCourses) {
            updateTopCourses(statsData.topCourses);
        }

    } catch (error) {
        console.error('Error in loadDashboardData:', error);
        await destroyChart();
        await initializeChart();
        
        updateStats({
            totalRevenue: 0,
            totalOrders: 0,
            comparedToLastPeriod: 0,
            orderComparedToLastPeriod: 0
        });
        showError(`Lỗi tải dữ liệu: ${error.message}`);
    } finally {
        isLoading = false;
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

// Cập nhật event listener cho các nút filter
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM Content Loaded');
    
    try {
        // Khởi tạo biểu đồ trống
        initializeChart();
        console.log('Empty chart initialized');

        // Load khóa học nổi bật ngay khi trang được tải
        loadTopCourses();

        // Xử lý sự kiện click cho các nút filter
        const timeFilterButtons = document.querySelectorAll('.time-filter button');
        timeFilterButtons.forEach(button => {
            button.addEventListener('click', async function(e) {
                // Ngăn chặn việc xử lý nhiều lần
                if (isLoading) {
                    console.log('Still loading, ignoring click');
                    return;
                }

                console.log('Filter button clicked:', this.dataset.period);
                timeFilterButtons.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
                
                const period = this.dataset.period;
                await loadDashboardData(period);
            });
        });

        // Chọn nút mặc định
        const defaultButton = document.querySelector('.time-filter button[data-period="today"]');
        if (defaultButton) {
            console.log('Clicking default button (today)');
            defaultButton.click();
        }
    } catch (error) {
        console.error('Error during initialization:', error);
    }
}); 