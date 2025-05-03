<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background: #f8f9fa; }
        
        .user-card {
            border-radius: 20px;
            box-shadow: 0 2px 12px rgba(90,90,90,0.07);
            border: none;
            background: #fff;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .user-card:hover {
            transform: translateY(-5px) scale(1.02);
            box-shadow: 0 6px 24px rgba(106,130,251,0.13);
        }
        .status-badge {
            position: static;
            display: inline-block;
            margin-top: 8px;
            margin-bottom: 8px;
            font-size: 0.95rem;
            padding: 6px 14px;
            border-radius: 12px;
            color: #fff;
            font-weight: 500;
            box-shadow: 0 2px 8px rgba(90,90,90,0.07);
        }
        .bg-success {
            background: #28a745 !important;
        }
        .bg-danger {
            background: #dc3545 !important;
        }
        .bg-warning {
            background: #ffc107 !important;
        }
        .bg-secondary {
            background: #6c757d !important;
        }
        .search-box {
            max-width: 300px;
        }
        .filter-box {
            max-width: 200px;
        }
        .btn-primary {
            background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%);
            border: none;
            color: #fff;
            font-weight: 500;
            border-radius: 8px;
        }
        .btn-primary:hover, .btn-primary:focus {
            background: #fc5c7d;
            color: #fff;
        }
        .btn-info {
            background: #6a82fb;
            border: none;
            color: #fff;
            font-weight: 500;
            border-radius: 8px;
        }
        .btn-info:hover, .btn-info:focus {
            background: #fc5c7d;
            color: #fff;
        }
        .btn-danger {
            background: linear-gradient(90deg, #fc5c7d 0%, #ff7851 100%);
            border: none;
            color: #fff;
            font-weight: 500;
            border-radius: 8px;
        }
        .btn-danger:hover, .btn-danger:focus {
            background: #ff7851;
            color: #fff;
        }
        .btn-success {
            background: linear-gradient(90deg, #28a745 0%, #6a82fb 100%);
            border: none;
            color: #fff;
            font-weight: 500;
            border-radius: 8px;
        }
        .btn-success:hover, .btn-success:focus {
            background: #28a745;
            color: #fff;
        }
        .modal-content {
            border-radius: 20px;
            box-shadow: 0 2px 12px rgba(90,90,90,0.07);
        }
        .modal-header {
            background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%);
            color: #fff;
            border-radius: 20px 20px 0 0;
        }
        .form-select, .form-control {
            border-radius: 8px;
            border: 1.5px solid #e3e3e3;
        }
        .form-select:focus, .form-control:focus {
            border-color: #6a82fb;
            box-shadow: 0 0 0 0.2rem rgba(106,130,251,0.15);
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="admin-sidebar.jsp" />

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <h2 class="mb-4">Quản lý người dùng</h2>

                <!-- Search and Filter -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <div class="input-group search-box">
                            <input type="text" id="searchInput" class="form-control" placeholder="Tìm kiếm theo tên hoặc email...">
                            <button class="btn btn-primary" onclick="searchUsers()">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="input-group filter-box">
                            <select id="roleFilter" class="form-select" onchange="filterUsers()">
                                <option value="">Tất cả vai trò</option>
                                <option value="learner">Học viên</option>
                                <option value="expert">Chuyên gia</option>
                                <option value="admin">Quản trị viên</option>
                                <option value="examManager">Quản lý bài thi</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Users Grid -->
                <div class="row" id="usersGrid">
                    <!-- Users will be loaded here dynamically -->
                </div>

                <!-- User Details Modal -->
                <div class="modal fade" id="userDetailsModal" tabindex="-1">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Chi tiết người dùng</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="userDetailsContent">
                                <!-- User details will be loaded here -->
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Load all users on page load
        document.addEventListener('DOMContentLoaded', function() {
            loadUsers();
        });

        function loadUsers() {
            fetch('${pageContext.request.contextPath}/admin/users')
                .then(response => response.json())
                .then(users => displayUsers(users))
                .catch(error => console.error('Error:', error));
        }

        function searchUsers() {
            const searchTerm = document.getElementById('searchInput').value;
            fetch('${pageContext.request.contextPath}/admin/users/search?searchTerm=' + encodeURIComponent(searchTerm))
                .then(response => response.json())
                .then(users => displayUsers(users))
                .catch(error => console.error('Error:', error));
        }

        function filterUsers() {
            const role = document.getElementById('roleFilter').value;
            if (role) {
                fetch('${pageContext.request.contextPath}/admin/users/filter?role=' + encodeURIComponent(role))
                    .then(response => response.json())
                    .then(users => displayUsers(users))
                    .catch(error => console.error('Error:', error));
            } else {
                loadUsers();
            }
        }

        function displayUsers(users) {
            const grid = document.getElementById('usersGrid');
            grid.innerHTML = '';
            
            users.forEach(user => {
                const card = createUserCard(user);
                grid.appendChild(card);
            });
        }

        function formatDateVN(dateValue) {
            if (!dateValue) return 'Chưa cập nhật';
            var d = new Date(dateValue);
            if (isNaN(d.getTime())) return 'Chưa cập nhật';
            return d.toLocaleDateString('vi-VN');
        }

        function createUserCard(user) {
            const col = document.createElement('div');
            col.className = 'col-md-4 mb-4';
            let statusClass = '';
            let statusText = '';
            if (user.status === 'active') {
                statusClass = 'bg-success';
                statusText = 'Hoạt động';
            } else if (user.status === 'blocked') {
                statusClass = 'bg-danger';
                statusText = 'Bị khóa';
            } else if (user.status === 'warning') {
                statusClass = 'bg-warning text-dark';
                statusText = 'Cảnh báo';
            } else {
                statusClass = 'bg-secondary';
                statusText = user.status;
            }
            const reportIcon = user.isReported ? '<i class="fas fa-exclamation-circle text-danger ms-2" title="Người dùng này đang bị báo cáo"></i>' : '';
            col.innerHTML = 
                '<div class="card user-card">' +
                    '<div class="card-body">' +
                        '<h5 class="card-title">' + user.fullName + reportIcon + '</h5>' +
                        '<span class="badge ' + statusClass + ' status-badge">' + statusText + '</span>' +
                        '<p class="card-text">' +
                            '<i class="fas fa-envelope"></i> ' + user.gmail + '<br>' +
                            '<i class="fas fa-user-tag"></i> ' + user.role + '<br>' +
                            '<i class="fas fa-calendar"></i> ' + formatDateVN(user.dateCreate) +
                        '</p>' +
                        '<div class="btn-group">' +
                            '<button class="btn btn-info btn-sm" onclick="viewUserDetails(' + user.userID + ')">' +
                                '<i class="fas fa-info-circle"></i> Chi tiết' +
                            '</button>' +
                            (user.status === 'active' 
                                ? '<button class="btn btn-danger btn-sm" onclick="blockUser(' + user.userID + ')">' +
                                    '<i class="fas fa-ban"></i> Khóa' +
                                  '</button>'
                                : '<button class="btn btn-success btn-sm" onclick="unblockUser(' + user.userID + ')">' +
                                    '<i class="fas fa-unlock"></i> Mở khóa' +
                                  '</button>'
                            ) +
                        '</div>' +
                    '</div>' +
                '</div>';
            return col;
        }

        function viewUserDetails(userId) {
            fetch('${pageContext.request.contextPath}/admin/users/' + userId)
                .then(response => response.json())
                .then(userDetails => {
                    const content = document.getElementById('userDetailsContent');
                    content.innerHTML = generateUserDetailsHTML(userDetails);
                    new bootstrap.Modal(document.getElementById('userDetailsModal')).show();
                })
                .catch(error => console.error('Error:', error));
        }

        function generateUserDetailsHTML(userDetails) {
            let html = 
                '<div class="row">' +
                    '<div class="col-md-4 text-center">' +
                        '<img src="' + (userDetails.avatar || 'default-avatar.png') + '" class="img-fluid rounded-circle mb-3" style="max-width: 150px;">' +
                    '</div>' +
                    '<div class="col-md-8">' +
                        '<h4>' + userDetails.fullName + '</h4>' +
                        '<p><strong>Username:</strong> ' + userDetails.username + '</p>' +
                        '<p><strong>Email:</strong> ' + userDetails.gmail + '</p>' +
                        '<p><strong>Số điện thoại:</strong> ' + (userDetails.phone || 'Chưa cập nhật') + '</p>' +
                        '<p><strong>Vai trò:</strong> ' + userDetails.role + '</p>' +
                        '<p><strong>Trạng thái:</strong> ' + userDetails.status + '</p>' +
                        '<p><strong>Ngày tạo:</strong> ' + formatDateVN(userDetails.dateCreate) + '</p>' +
                        '<p><strong>Giới tính:</strong> ' + (userDetails.gender || 'Chưa cập nhật') + '</p>' +
                        '<p><strong>Ngày sinh:</strong> ' + formatDateVN(userDetails.dateOfBirth) + '</p>' +
                    '</div>' +
                '</div>';
            return html;
        }

        function blockUser(userId) {
            if (confirm('Bạn có chắc chắn muốn khóa người dùng này?')) {
                fetch('${pageContext.request.contextPath}/admin/users/' + userId + '/block', { method: 'POST' })
                    .then(response => response.json())
                    .then(result => {
                        if (result.success) {
                            loadUsers();
                        } else {
                            alert('Không thể khóa người dùng. Vui lòng thử lại.');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        }

        function unblockUser(userId) {
            if (confirm('Bạn có chắc chắn muốn mở khóa người dùng này?')) {
                fetch('${pageContext.request.contextPath}/admin/users/' + userId + '/unblock', { method: 'POST' })
                    .then(response => response.json())
                    .then(result => {
                        if (result.success) {
                            loadUsers();
                        } else {
                            alert('Không thể mở khóa người dùng. Vui lòng thử lại.');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        }
    </script>
</body>
</html> 