<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
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
        .action-buttons .btn {
            margin: 0 2px;
        }
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
                <button class="btn btn-light" onclick="showAddModal()">
                    <i class="bi bi-plus-circle"></i> Thêm danh mục
                </button>
            </div>
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Tên danh mục</th>
                                <th>Mô tả</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody id="categoryTable"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add/Edit Modal -->
<div class="modal fade" id="categoryModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalTitle">Thêm danh mục mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="categoryForm">
                    <input type="hidden" id="categoryId">
                    <div class="mb-3">
                        <label class="form-label">Tên danh mục</label>
                        <input type="text" class="form-control" id="categoryName" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mô tả</label>
                        <textarea class="form-control" id="description" rows="3"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-primary" onclick="saveCategory()">Lưu</button>
            </div>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xác nhận xóa</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc chắn muốn xóa danh mục này?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger" onclick="confirmDelete()">Xóa</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
let categoryModal;
let deleteModal;
let currentCategoryId;

document.addEventListener('DOMContentLoaded', function() {
    categoryModal = new bootstrap.Modal(document.getElementById('categoryModal'));
    deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
    loadCategories();
});

function loadCategories() {
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
                    + '<td class="action-buttons">'
                    + '<button class="btn btn-sm btn-primary" onclick="showEditModal(' + c.categoryID + ')"><i class="bi bi-pencil"></i></button>'
                    + '<button class="btn btn-sm btn-danger" onclick="showDeleteModal(' + c.categoryID + ')"><i class="bi bi-trash"></i></button>'
                    + '</td>'
                    + '</tr>';
            });
        });
}

function showAddModal() {
    document.getElementById('modalTitle').textContent = 'Thêm danh mục mới';
    document.getElementById('categoryForm').reset();
    document.getElementById('categoryId').value = '';
    categoryModal.show();
}

function showEditModal(categoryId) {
    document.getElementById('modalTitle').textContent = 'Sửa danh mục';
    fetch('/Hankyo/admin/categories/' + categoryId)
        .then(res => res.json())
        .then(data => {
            document.getElementById('categoryId').value = data.categoryID;
            document.getElementById('categoryName').value = data.categoryName;
            document.getElementById('description').value = data.description || '';
            categoryModal.show();
        });
}

function showDeleteModal(categoryId) {
    currentCategoryId = categoryId;
    deleteModal.show();
}

function saveCategory() {
    const categoryId = document.getElementById('categoryId').value;
    const categoryName = document.getElementById('categoryName').value;
    const description = document.getElementById('description').value;

    if (!categoryName) {
        alert('Vui lòng nhập tên danh mục');
        return;
    }

    const data = {
        categoryName: categoryName,
        description: description
    };

    const method = categoryId ? 'PUT' : 'POST';
    const url = categoryId ? 
        '/Hankyo/admin/categories/' + categoryId : 
        '/Hankyo/admin/categories';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        categoryModal.hide();
        loadCategories();
    })
    .catch(error => {
        alert('Có lỗi xảy ra: ' + error.message);
    });
}

function confirmDelete() {
    if (!currentCategoryId) return;

    fetch('/Hankyo/admin/categories/' + currentCategoryId, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        deleteModal.hide();
        loadCategories();
    })
    .catch(error => {
        alert('Có lỗi xảy ra: ' + error.message);
    });
}
</script>
</body>
</html> 
