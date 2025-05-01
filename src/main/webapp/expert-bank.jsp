<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý tài khoản ngân hàng</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
            transition: all 0.3s ease;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 20px;
            transition: all 0.3s ease;
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

        .bank-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .bank-card:hover {
            border-color: #6f42c1;
            transform: translateY(-2px);
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .bank-card.selected {
            border-color: #6f42c1;
            background-color: #f8f2ff;
        }

        .bank-logo {
            width: 120px;
            height: auto;
            margin-bottom: 15px;
            object-fit: contain;
        }

        .form-label {
            font-weight: 500;
            color: #495057;
        }

        .bank-select {
            max-height: 400px;
            overflow-y: auto;
            padding: 15px;
            background: #fff;
            border-radius: 8px;
            box-shadow: inset 0 0 8px rgba(0,0,0,0.05);
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
        
        .bank-list {
            margin-top: 30px;
        }
        
        .bank-item {
            background: white;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            transition: all 0.2s ease;
        }
        
        .bank-item:hover {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .bank-item .bank-logo {
            width: 60px;
            height: 60px;
            object-fit: contain;
            margin-right: 15px;
        }
        
        .bank-item .bank-info {
            flex: 1;
        }
        
        .bank-item .bank-actions {
            display: flex;
            gap: 10px;
        }
        
        .bank-item .btn-action {
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            border: none;
            background: #f8f9fa;
            color: #6c757d;
            transition: all 0.2s ease;
        }
        
        .bank-item .btn-action:hover {
            background: #e9ecef;
            color: #495057;
        }
        
        .bank-item .btn-edit:hover {
            background: #e7f5ff;
            color: #0d6efd;
        }
        
        .bank-item .btn-delete:hover {
            background: #fff5f5;
            color: #dc3545;
        }
        
        .modal-content {
            border-radius: 12px;
        }
        
        .modal-header {
            border-bottom: none;
            padding: 20px;
        }
        
        .modal-body {
            padding: 20px;
        }
        
        .modal-footer {
            border-top: none;
            padding: 20px;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
        }

        .btn-add-account {
            background: #6f42c1;
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s ease;
        }

        .btn-add-account:hover {
            background: #5a32a3;
            transform: translateY(-1px);
        }

        .bank-item {
            background: white;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
        }

        .bank-item:hover {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .bank-item .bank-logo {
            width: 60px;
            height: 60px;
            object-fit: contain;
            margin-right: 15px;
        }

        .bank-item .bank-info {
            flex: 1;
        }

        .bank-item .bank-actions {
            display: flex;
            gap: 10px;
        }

        .bank-item .btn-action {
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            border: none;
            background: #f8f9fa;
            color: #6c757d;
            transition: all 0.2s ease;
        }

        .bank-item .btn-action:hover {
            background: #e9ecef;
            color: #495057;
        }

        .bank-item .btn-edit:hover {
            background: #e7f5ff;
            color: #0d6efd;
        }

        .bank-item .btn-delete:hover {
            background: #fff5f5;
            color: #dc3545;
        }

        .bank-status {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
        }

        .bank-status.connected {
            background: #d4edda;
            color: #155724;
        }

        .bank-select-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 16px;
            max-height: 400px;
            overflow-y: auto;
            padding: 16px;
        }

        .bank-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 16px;
            cursor: pointer;
            transition: all 0.2s ease;
            text-align: center;
        }

        .bank-card:hover {
            border-color: #6f42c1;
            transform: translateY(-2px);
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .bank-card.selected {
            border-color: #6f42c1;
            background-color: #f8f2ff;
        }

        .bank-card img {
            width: 100px;
            height: 60px;
            object-fit: contain;
            margin-bottom: 12px;
        }

        .bank-card h6 {
            margin: 0;
            font-size: 14px;
            color: #495057;
        }

        .bank-card p {
            margin: 4px 0 0;
            font-size: 12px;
            color: #6c757d;
        }

        @media (max-width: 768px) {
            .bank-select-grid {
                grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
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
        <a href="${pageContext.request.contextPath}/expert/transactions" class="nav-link">
            <i class="fas fa-exchange-alt"></i> Giao dịch
        </a>
        <a href="${pageContext.request.contextPath}/expert/bank-management" class="nav-link active">
            <i class="fas fa-university"></i> Ngân hàng
        </a>
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
        <!-- Page Header -->
        <div class="page-header">
            <h4 class="mb-0">Tài khoản ngân hàng</h4>
            <button class="btn-add-account" data-bs-toggle="modal" data-bs-target="#addBankModal">
                <i class="fas fa-plus"></i>
                Thêm tài khoản
            </button>
        </div>

        <!-- Bank List -->
        <div class="card">
            <div class="card-body">
                <div id="expertBanks">
                    <!-- Expert banks will be populated here -->
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Bank Modal -->
<div class="modal fade" id="addBankModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Thêm tài khoản ngân hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="bankForm">
                    <div class="mb-4">
                        <label class="form-label">Chọn ngân hàng</label>
                        <div class="bank-select-grid" id="bankList">
                            <!-- Banks will be populated here -->
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="bankAccount" class="form-label">Số tài khoản</label>
                        <input type="text" class="form-control" id="bankAccount" name="bankAccount" required>
                    </div>
                    <input type="hidden" id="selectedBankName" name="bankName">
                    <input type="hidden" id="selectedBinCode" name="binCode">
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary" id="addBankBtn">Thêm tài khoản</button>
            </div>
        </div>
    </div>
</div>

<!-- Edit Bank Modal -->
<div class="modal fade" id="editBankModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chỉnh sửa tài khoản ngân hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="editBankForm">
                    <input type="hidden" id="editBankId" name="bankId">
                    <div class="mb-3">
                        <label class="form-label">Ngân hàng</label>
                        <input type="text" class="form-control" id="editBankName" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="editBankAccount" class="form-label">Số tài khoản</label>
                        <input type="text" class="form-control" id="editBankAccount" name="bankAccount" required>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary" id="saveEdit">Lưu thay đổi</button>
            </div>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteBankModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xác nhận xóa</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc chắn muốn xóa tài khoản ngân hàng này?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger" id="confirmDelete">Xóa</button>
            </div>
        </div>
    </div>
</div>

<!-- Required JavaScript -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
$(document).ready(function() {
    // Load banks from JSON for the modal
    function loadBankList() {
        $.getJSON("${pageContext.request.contextPath}/asset/js/banks.json", function(data) {
            let bankListHtml = '';
            
            data.data.forEach(function(bank) {
                if (bank.transferSupported === 1) {
                    bankListHtml += `
                        <div class="bank-card" data-bank-name="\${bank.name}" data-bin="\${bank.bin}">
                            <img src="\${bank.logo}" alt="\${bank.shortName}">
                            <h6>\${bank.shortName}</h6>
                            <p class="text-muted">\${bank.name}</p>
                        </div>
                    `;
                }
            });
            
            $('#bankList').html(bankListHtml);
            
            // Handle bank selection
            $('.bank-card').click(function() {
                $('.bank-card').removeClass('selected');
                $(this).addClass('selected');
                
                $('#selectedBankName').val($(this).data('bank-name'));
                $('#selectedBinCode').val($(this).data('bin'));
            });
        });
    }

    // Load expert's banks
    function loadExpertBanks() {
        $.post("${pageContext.request.contextPath}/expert/bank-management", {
            action: "list"
        }, function(response) {
            if (response.success) {
                // Load banks data from JSON
                $.getJSON("${pageContext.request.contextPath}/asset/js/banks.json", function(banksData) {
                    let banksHtml = '';
                    
                    response.data.forEach(function(bank) {
                        // Find matching bank from banks.json
                        let bankInfo = banksData.data.find(b => b.bin === bank.binCode);
                        let bankLogo = bankInfo ? bankInfo.logo : '';
                        
                        banksHtml += `
                            <div class="bank-item" data-bank-id="\${bank.eBankID}">
                                <img src="\${bankLogo}" alt="\${bank.bankName}" class="bank-logo">
                                <div class="bank-info">
                                    <h5 class="mb-1">\${bank.bankName}</h5>
                                    <p class="text-muted mb-0">\${bank.bankAccount}</p>
                                </div>
                                <span class="bank-status connected me-3">Đã liên kết</span>
                                <div class="bank-actions">
                                    <button class="btn-action btn-edit" data-bank-id="\${bank.eBankID}" data-bs-toggle="modal" data-bs-target="#editBankModal">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn-action btn-delete" data-bs-toggle="modal" data-bs-target="#deleteBankModal">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        `;
                    });
                    
                    $('#expertBanks').html(banksHtml || '<p class="text-center text-muted my-4">Chưa có tài khoản ngân hàng nào được thêm</p>');
                    
                    // Handle edit button click
                    $('.btn-edit').click(function() {
                        let bankId = $(this).data('bank-id');
                        let bankItem = $(this).closest('.bank-item');
                        let bankName = bankItem.find('h5').text();
                        let bankAccount = bankItem.find('p').text();
                        
                        $('#editBankId').val(bankId);
                        $('#editBankName').val(bankName);
                        $('#editBankAccount').val(bankAccount);
                    });
                    
                    // Handle delete button click
                    $('.btn-delete').click(function() {
                        let bankId = $(this).closest('.bank-item').data('bank-id');
                        $('#confirmDelete').data('bank-id', bankId);
                    });
                });
            } else {
                alert('Có lỗi xảy ra: ' + response.message);
            }
        });
    }

    // Load initial data
    loadBankList();
    loadExpertBanks();

    // Handle add bank form submission
    $('#addBankBtn').click(function() {
        if (!$('#selectedBankName').val() || !$('#selectedBinCode').val()) {
            alert('Vui lòng chọn ngân hàng');
            return;
        }
        
        if (!$('#bankAccount').val()) {
            alert('Vui lòng nhập số tài khoản');
            return;
        }
        
        $.post("${pageContext.request.contextPath}/expert/bank-management", {
            action: "add",
            bankName: $('#selectedBankName').val(),
            bankAccount: $('#bankAccount').val(),
            binCode: $('#selectedBinCode').val()
        }, function(response) {
            if (response.success) {
                loadExpertBanks();
                $('#addBankModal').modal('hide');
                $('#bankForm')[0].reset();
                $('.bank-card').removeClass('selected');
                alert('Thêm tài khoản thành công');
            } else {
                alert('Có lỗi xảy ra: ' + response.message);
            }
        });
    });

    // Handle edit form submission
    $('#saveEdit').click(function() {
        let bankId = $('#editBankId').val();
        let bankAccount = $('#editBankAccount').val();
        
        $.post("${pageContext.request.contextPath}/expert/bank-management", {
            action: "edit",
            bankId: bankId,
            bankAccount: bankAccount
        }, function(response) {
            if (response.success) {
                loadExpertBanks();
                $('#editBankModal').modal('hide');
                alert('Cập nhật thành công');
            } else {
                alert('Có lỗi xảy ra: ' + response.message);
            }
        });
    });

    // Handle delete confirmation
    $('#confirmDelete').click(function() {
        let bankId = $(this).data('bank-id');
        
        $.post("${pageContext.request.contextPath}/expert/bank-management", {
            action: "delete",
            bankId: bankId
        }, function(response) {
            if (response.success) {
                loadExpertBanks();
                $('#deleteBankModal').modal('hide');
                alert('Xóa tài khoản thành công');
            } else {
                alert('Có lỗi xảy ra: ' + response.message);
            }
        });
    });

    // Reset form when modal is closed
    $('#addBankModal').on('hidden.bs.modal', function() {
        $('#bankForm')[0].reset();
        $('.bank-card').removeClass('selected');
    });
});
</script>

</body>
</html> 