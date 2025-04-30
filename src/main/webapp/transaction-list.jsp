<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Quản lý giao dịch</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link href="https://cdn.datatables.net/1.13.7/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap5.min.css" rel="stylesheet">
    <!-- DateRangePicker CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
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
        
        .filter-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        
        .table-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
        
        .date-filter {
            cursor: pointer;
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            background-color: white;
        }
        
        .status-badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.875rem;
        }
        
        .status-completed {
            background-color: #d1e7dd;
            color: #0f5132;
        }
        
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        
        .status-cancelled {
            background-color: #f8d7da;
            color: #842029;
        }
        
        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
            }
            
            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h5 class="mb-4"><c:out value="${expert.fullName}"/></h5>
    <div class="nav flex-column">
        <a href="${pageContext.request.contextPath}/expert-dashboard" class="nav-link">
            <i class="fas fa-home"></i> Tổng quan
        </a>
        <a href="${pageContext.request.contextPath}/expert/transactions" class="nav-link active">
            <i class="fas fa-exchange-alt"></i> Giao dịch
        </a>
        <a href="#" class="nav-link">
            <i class="fas fa-university"></i> Ngân hàng
        </a>
        <a href="#" class="nav-link">
            <i class="fas fa-credit-card"></i> Kênh thanh toán
        </a>
        <a href="#" class="nav-link">
            <i class="fas fa-link"></i> Tạo link thanh toán
        </a>
        <a href="#" class="nav-link">
            <i class="fas fa-cog"></i> Thiết lập
        </a>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <div class="container-fluid">
        <h2 class="mb-4">Quản lý giao dịch</h2>
        
        <!-- Filter Section -->
        <div class="filter-section">
            <div class="row g-3 align-items-center">
                <div class="col-md-4">
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-calendar"></i>
                        </span>
                        <input type="text" id="dateRange" class="form-control date-filter" 
                               placeholder="Chọn khoảng thời gian">
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-search"></i>
                        </span>
                        <input type="text" id="searchInput" class="form-control" 
                               placeholder="Tìm kiếm giao dịch...">
                    </div>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-primary" id="exportExcel">
                        <i class="fas fa-file-excel"></i> Xuất Excel
                    </button>
                </div>
            </div>
        </div>
        
        <!-- Table Section -->
        <div class="table-section">
            <table id="transactionTable" class="table table-striped dt-responsive nowrap" style="width:100%">
                <thead>
                    <tr>
                        <th>Mã giao dịch</th>
                        <th>Ngày đặt hàng</th>
                        <th>Khóa học</th>
                        <th>Học viên</th>
                        <th>Email</th>
                        <th>Số tiền</th>
                        <th>Trạng thái</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<!-- Required JavaScript -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.7/js/dataTables.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap5.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>

<script>
    // Định nghĩa contextPath cho JavaScript
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
</script>

<script src="${pageContext.request.contextPath}/js/transaction.js"></script>

</body>
</html> 