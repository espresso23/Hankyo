<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/transaction.css">

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
        <a href="${pageContext.request.contextPath}/expert/bank-management" class="nav-link"><i class="fas fa-university"></i> Ngân hàng</a>
        <a href="#" class="nav-link">
            <i class="fas fa-link"></i> Tạo yêu cầu rút tiền
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
            <div class="row g-3 align-items-center justify-content-between">
                <div class="col-auto">
                    <div class="input-group date-range-group">
                        <span class="input-group-text">
                            <i class="fas fa-calendar"></i>
                        </span>
                        <input type="text" id="dateRange" class="form-control date-filter" 
                               placeholder="Chọn khoảng thời gian">
                    </div>
                </div>
                <div class="col-auto">
                    <button class="btn btn-success" id="exportExcel">
                        <i class="fas fa-file-excel me-1"></i>Xuất Excel
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
