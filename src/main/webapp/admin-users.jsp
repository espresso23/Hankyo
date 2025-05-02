<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background: #f8f9fa; }
        .sidebar { min-height: 100vh; background: #fff; color: #5f72bd; border-right: 1px solid #e3e3e3; border-radius: 20px; }
        .sidebar .nav-link { color: #5f72bd; font-weight: 500; border-radius: 8px; }
        .sidebar .nav-link.active, .sidebar .nav-link:hover { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; }
        .main-content { padding: 20px; background: #f8f9fa; border-radius: 20px; }
        .header-gradient { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; border-radius: 12px; padding: 24px 32px; margin-bottom: 32px; box-shadow: 0 4px 24px rgba(106,130,251,0.08); }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 sidebar p-3">
            <h3 class="mb-4">Hankyo Admin</h3>
            <ul class="nav flex-column">
                <li class="nav-item"><a class="nav-link" href="admin-dashboard.jsp"><i class="bi bi-speedometer2 me-2"></i>Dashboard</a></li>
                <li class="nav-item"><a class="nav-link active" href="admin-users.jsp"><i class="bi bi-people me-2"></i>Quản lý người dùng</a></li>
                <li class="nav-item"><a class="nav-link" href="admin-courses.jsp"><i class="bi bi-book me-2"></i>Quản lý khóa học</a></li>
                <li class="nav-item"><a class="nav-link" href="admin-payments.jsp"><i class="bi bi-cash me-2"></i>Quản lý thanh toán</a></li>
                <li class="nav-item"><a class="nav-link" href="admin-posts.jsp"><i class="bi bi-file-text me-2"></i>Quản lý nội dung</a></li>
                <li class="nav-item"><a class="nav-link" href="admin-experts.jsp"><i class="bi bi-person-badge me-2"></i>Quản lý expert</a></li>
                <li class="nav-item"><a class="nav-link" href="admin-reports.jsp"><i class="bi bi-flag me-2"></i>Quản lý báo cáo</a></li>
                <li class="nav-item"><a class="nav-link" href="admin-categories.jsp"><i class="bi bi-tags me-2"></i>Quản lý danh mục</a></li>
            </ul>
        </div>
        <!-- Main content -->
        <div class="col-md-9 col-lg-10 main-content">
            <div class="header-gradient mb-4 d-flex align-items-center justify-content-between">
                <h2 class="mb-0">Quản lý người dùng</h2>
            </div>
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Họ tên</th>
                                <th>Email</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody id="userTable"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
fetch('/Hankyo/admin/users')
    .then(res => res.json())
    .then(data => {
        const tb = document.getElementById('userTable');
        tb.innerHTML = '';
        data.forEach(u => {
            tb.innerHTML += `<tr>
                <td>${u.userID}</td>
                <td>${u.fullName}</td>
                <td>${u.gmail}</td>
                <td>${u.status}</td>
            </tr>`;
        });
    });
</script>
</body>
</html> 