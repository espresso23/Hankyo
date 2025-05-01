<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Yêu Cầu Rút Tiền</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
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

        .withdraw-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            border: 1px solid #dee2e6;
        }

        .balance-card {
            background: linear-gradient(135deg, #6f42c1 0%, #6610f2 100%);
            color: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .request-history {
            background: white;
            border-radius: 10px;
            padding: 20px;
            border: 1px solid #dee2e6;
        }

        .status-badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.8rem;
        }

        .status-pending {
            background-color: #ffc107;
            color: #000;
        }

        .status-approved {
            background-color: #28a745;
            color: #fff;
        }

        .status-rejected {
            background-color: #dc3545;
            color: #fff;
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
                margin-bottom: 20px;
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
        <a href="${pageContext.request.contextPath}/expert/dashboard" class="nav-link">
            <i class="fas fa-home"></i> Tổng quan
        </a>
        <a href="${pageContext.request.contextPath}/expert/transactions" class="nav-link">
            <i class="fas fa-exchange-alt"></i> Giao dịch
        </a>
        <a href="${pageContext.request.contextPath}/expert/bank-management" class="nav-link">
            <i class="fas fa-university"></i> Ngân hàng
        </a>
        <a href="${pageContext.request.contextPath}/expert/withdraw" class="nav-link active">
            <i class="fas fa-link"></i> Tạo yêu cầu rút tiền
        </a>
        <a href="#" class="nav-link">
            <i class="fas fa-cog"></i> Thiết lập
        </a>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Balance Card -->
    <div class="balance-card mb-4">
        <h5>Số dư khả dụng</h5>
        <h2 class="mb-0">
            <fmt:formatNumber value="${expertRevenue.totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
        </h2>
    </div>

    <!-- Withdraw Form Card -->
    <div class="withdraw-card mb-4">
        <h5 class="mb-4">Tạo yêu cầu rút tiền mới</h5>
        <form id="withdrawForm">
            <div class="mb-3">
                <label for="bankAccount" class="form-label">Tài khoản ngân hàng</label>
                <select class="form-select" id="bankAccount" required>
                    <c:forEach items="${expertBanks}" var="bank">
                        <option value="${bank.eBankID}">
                            ${bank.bankName} - ${bank.bankAccount}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label for="amount" class="form-label">Số tiền rút</label>
                <input type="number" class="form-control" id="amount" required
                       min="50000" max="${expertRevenue.totalRevenue}"
                       step="1000">
                <div class="form-text">Số tiền tối thiểu: 50,000₫</div>
            </div>
            <div class="mb-3">
                <label for="note" class="form-label">Ghi chú (tùy chọn)</label>
                <textarea class="form-control" id="note" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Tạo yêu cầu</button>
        </form>
    </div>

    <!-- Request History -->
    <div class="request-history">
        <h5 class="mb-4">Lịch sử yêu cầu rút tiền</h5>
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Ngày yêu cầu</th>
                        <th>Số tiền</th>
                        <th>Ngân hàng</th>
                        <th>Trạng thái</th>
                        <th>Ghi chú</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${withdrawRequests}" var="request">
                        <tr>
                            <td>
                                <c:if test="${not empty request.formattedDate}">
                                    <fmt:formatDate value="${request.formattedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </c:if>
                            </td>
                            <td>
                                <fmt:formatNumber value="${request.amount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                            <td>${request.bankName}</td>
                            <td>
                                <span class="status-badge status-${request.status.toLowerCase()}">
                                    ${request.status}
                                </span>
                            </td>
                            <td>${request.note}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Custom JavaScript -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const withdrawForm = document.getElementById('withdrawForm');
    const amountInput = document.getElementById('amount');
    const maxAmount = ${expertRevenue.totalRevenue};

    // Format amount input
    amountInput.addEventListener('input', function() {
        if (this.value > maxAmount) {
            this.value = maxAmount;
        }
    });

    // Handle form submission
    withdrawForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = {
            eBankID: document.getElementById('bankAccount').value,
            amount: document.getElementById('amount').value,
            note: document.getElementById('note').value
        };

        fetch('${pageContext.request.contextPath}/expert/withdraw', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams(formData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Yêu cầu rút tiền đã được tạo thành công!');
                location.reload();
            } else {
                alert(data.message || 'Có lỗi xảy ra, vui lòng thử lại sau.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra, vui lòng thử lại sau.');
        });
    });
});
</script>
</body>
</html> 