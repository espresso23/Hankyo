$(document).ready(function() {
    console.log('Transaction page initialized');
    console.log('Context path:', contextPath);

    // Khởi tạo DateRangePicker
    $('#dateRange').daterangepicker({
        locale: {
            format: 'DD/MM/YYYY',
            applyLabel: 'Áp dụng',
            cancelLabel: 'Hủy',
            fromLabel: 'Từ',
            toLabel: 'Đến',
            customRangeLabel: 'Tùy chỉnh',
            daysOfWeek: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
            monthNames: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
                        'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
            firstDay: 1
        },
        startDate: moment().subtract(30, 'days'),
        endDate: moment(),
        ranges: {
           'Hôm nay': [moment(), moment()],
           'Hôm qua': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
           '7 ngày qua': [moment().subtract(6, 'days'), moment()],
           '30 ngày qua': [moment().subtract(29, 'days'), moment()],
           'Tháng này': [moment().startOf('month'), moment().endOf('month')],
           'Tháng trước': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    });

    // Khởi tạo DataTable
    let table = $('#transactionTable').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: contextPath + '/api/transactions/search',
            method: 'GET',
            data: function(d) {
                let dates = $('#dateRange').val().split(' - ');
                let params = {
                    draw: d.draw,
                    start: d.start,
                    length: d.length,
                    'search[value]': d.search.value,
                    startDate: moment(dates[0], 'DD/MM/YYYY').startOf('day').toISOString(),
                    endDate: moment(dates[1], 'DD/MM/YYYY').endOf('day').toISOString()
                };
                console.log('Request params:', params);
                return params;
            },
            dataSrc: function(json) {
                console.log('Response data:', json);
                if (!json || !json.data) {
                    console.error('Invalid response format');
                    return [];
                }
                return json.data;
            },
            error: function(xhr, error, thrown) {
                console.error('AJAX Error:', error);
                console.error('Error details:', thrown);
                console.error('Response:', xhr.responseText);
            }
        },
        columns: [
            { 
                data: 'orderId',
                title: 'Mã giao dịch'
            },
            { 
                data: 'orderDate',
                title: 'Ngày đặt hàng',
                render: function(data) {
                    return moment(data).format('DD/MM/YYYY HH:mm');
                }
            },
            { 
                data: 'courseName',
                title: 'Khóa học'
            },
            { 
                data: 'learnerName',
                title: 'Học viên'
            },
            { 
                data: 'gmail',
                title: 'Email',
                defaultContent: 'Chưa cập nhật'
            },
            { 
                data: 'totalAmount',
                title: 'Số tiền',
                render: function(data) {
                    return new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }).format(data);
                }
            },
            { 
                data: 'status',
                title: 'Trạng thái',
                render: function(data) {
                    let statusText = {
                        'Completed': 'Hoàn thành',
                        'Pending': 'Đang xử lý',
                        'Cancelled': 'Đã hủy'
                    }[data] || data;
                    
                    let statusClass = {
                        'Completed': 'status-completed',
                        'Pending': 'status-pending',
                        'Cancelled': 'status-cancelled'
                    }[data] || '';
                    
                    return `<span class="status-badge ${statusClass}">${statusText}</span>`;
                }
            }
        ],
        order: [[1, 'desc']],
        language: {
            url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json'
        },
        pageLength: 10,
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "Tất cả"]]
    });

    // Xử lý sự kiện thay đổi date range
    $('#dateRange').on('apply.daterangepicker', function(ev, picker) {
        table.ajax.reload();
    });

    // Xử lý sự kiện tìm kiếm
    let searchTimeout;
    $('#searchInput').on('keyup', function() {
        clearTimeout(searchTimeout);
        let searchValue = $(this).val();
        searchTimeout = setTimeout(function() {
            table.search(searchValue).draw();
        }, 500);
    });

    // Xử lý xuất Excel
    $('#exportExcel').on('click', function() {
        let dates = $('#dateRange').val().split(' - ');
        let startDate = moment(dates[0], 'DD/MM/YYYY').startOf('day').toISOString();
        let endDate = moment(dates[1], 'DD/MM/YYYY').endOf('day').toISOString();
        let searchTerm = $('#searchInput').val();

        window.location.href = `${contextPath}/api/transactions/export?startDate=${startDate}&endDate=${endDate}&search=${searchTerm}`;
    });
}); 