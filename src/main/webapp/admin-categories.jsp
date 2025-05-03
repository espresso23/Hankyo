<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý danh mục</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background: #f8f9fa; }
        .nav-item{
            margin: 5px 0 5px 0px;

        }
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
        <jsp:include page="admin-sidebar.jsp">
            <jsp:param name="active" value="categories"/>
        </jsp:include>

        <!-- Main content -->
        <div class="col-md-9 col-lg-10 main-content">
            <div class="header-gradient mb-4 d-flex align-items-center justify-content-between">
                <h2 class="mb-0">Quản lý danh mục</h2>
            </div>
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Tên danh mục</th>
                                <th>Mô tả</th>
                            </tr>
                        </thead>
                        <tbody id="categoryTable"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
fetch('/Hankyo/admin/categories')
    .then(res => res.json())
    .then(data => {
        const tb = document.getElementById('categoryTable');
        tb.innerHTML = '';
        data.forEach(function(c) {
            tb.innerHTML += '<tr>'
                + '<td>' + c.categoryID + '</td>'
                + '<td>' + c.categoryName + '</td>'
                + '<td>' + (c.description || '') + '</td>'
                + '</tr>';
        });
    });
</script>
</body>
</html> 