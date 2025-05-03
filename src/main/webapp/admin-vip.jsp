<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý gói VIP - Hankyo Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/admin.css">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <jsp:include page="admin-sidebar.jsp">
                <jsp:param name="active" value="vip"/>
            </jsp:include>
            
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="header-gradient">
                    <h2>Quản lý gói VIP</h2>
                    <p>Quản lý và theo dõi các gói VIP của hệ thống</p>
                </div>

                <!-- Thống kê -->
                <div class="row mb-4">
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Tổng số gói VIP</h5>
                                <h2 class="card-text" id="totalVips">0</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Người dùng VIP</h5>
                                <h2 class="card-text" id="totalVipUsers">0</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Doanh thu VIP</h5>
                                <h2 class="card-text" id="totalVipRevenue">0 VNĐ</h2>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Danh sách gói VIP -->
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">Danh sách gói VIP</h5>
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addVipModal">
                            <i class="bi bi-plus-lg"></i> Thêm gói VIP
                        </button>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên gói</th>
                                        <th>Loại</th>
                                        <th>Giá (tháng)</th>
                                        <th>Giá (năm)</th>
                                        <th>Mô tả</th>
                                        <th>Thời hạn</th>
                                        <th>Hình ảnh</th>
                                        <th>Tính năng</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody id="vipList">
                                    <!-- Dữ liệu sẽ được load bằng JavaScript -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Modal thêm/sửa gói VIP -->
    <div class="modal fade" id="addVipModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm gói VIP mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="addVipForm">
                        <div class="mb-3">
                            <label class="form-label">Tên gói</label>
                            <input type="text" class="form-control" id="addVipName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" id="addDescription" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Hình ảnh</label>
                            <input type="file" class="form-control" id="addVip_img" accept="image/*">
                            <div id="addVip_img_preview" class="mt-2"></div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Tính năng</label>
                            <textarea class="form-control" id="addFeatures" rows="3" placeholder="Nhập các tính năng, phân cách bằng dấu phẩy"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Loại gói</label>
                            <select class="form-select" id="addVipType" required>
                                <option value="FREE">Miễn phí</option>
                                <option value="POPULAR">Phổ biến</option>
                                <option value="PREMIUM">Premium</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Giá (tháng)</label>
                            <input type="number" class="form-control" id="addPrice" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Giá (năm)</label>
                            <input type="number" class="form-control" id="addYearlyPrice" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Thời hạn (ngày)</label>
                            <input type="number" class="form-control" id="addDuration" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Trạng thái</label>
                            <select class="form-select" id="addStatus" required>
                                <option value="ACTIVE">Hoạt động</option>
                                <option value="INACTIVE">Không hoạt động</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary" id="addVipBtn">Thêm mới</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal chỉnh sửa gói VIP -->
    <div class="modal fade" id="editVipModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Chỉnh sửa gói VIP</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="editVipForm">
                        <input type="hidden" id="editVipId">
                        <div class="mb-3">
                            <label class="form-label">Tên gói</label>
                            <input type="text" class="form-control" id="editVipName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" id="editDescription" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Hình ảnh</label>
                            <input type="file" class="form-control" id="editVip_img" accept="image/*">
                            <div id="editVip_img_preview" class="mt-2"></div>
                            <input type="hidden" id="editVip_img_url">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Tính năng</label>
                            <textarea class="form-control" id="editFeatures" rows="3" placeholder="Nhập các tính năng, phân cách bằng dấu phẩy"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Loại gói</label>
                            <select class="form-select" id="editVipType" required>
                                <option value="FREE">Miễn phí</option>
                                <option value="POPULAR">Phổ biến</option>
                                <option value="PREMIUM">Premium</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Giá (tháng)</label>
                            <input type="number" class="form-control" id="editPrice" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Giá (năm)</label>
                            <input type="number" class="form-control" id="editYearlyPrice" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Thời hạn (ngày)</label>
                            <input type="number" class="form-control" id="editDuration" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Trạng thái</label>
                            <select class="form-select" id="editStatus" required>
                                <option value="ACTIVE">Hoạt động</option>
                                <option value="INACTIVE">Không hoạt động</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary" id="saveEditVipBtn">Lưu thay đổi</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        // Load danh sách VIP
        function loadVipList() {
            $.get('admin/vips', function(data) {
                const vipList = $('#vipList');
                vipList.empty();
                
                data.forEach(vip => {
                    vipList.append(
                        '<tr>' +
                            '<td>' + vip.vipID + '</td>' +
                            '<td>' + vip.vipName + '</td>' +
                            '<td>' + vip.vipType + '</td>' +
                            '<td>' + vip.price.toLocaleString() + ' VNĐ</td>' +
                            '<td>' + vip.yearlyPrice.toLocaleString() + ' VNĐ</td>' +
                            '<td>' + (vip.description || '') + '</td>' +
                            '<td>' + vip.duration + ' ngày</td>' +
                            '<td><img src="' + (vip.vip_img || '') + '" alt="VIP Image" style="max-width: 50px;"></td>' +
                            '<td>' + (vip.features ? vip.features.split(',').join('<br>') : '') + '</td>' +
                            '<td>' +
                                '<span class="badge ' + (vip.status === 'ACTIVE' ? 'bg-success' : 'bg-danger') + '">' +
                                    (vip.status === 'ACTIVE' ? 'Hoạt động' : 'Không hoạt động') +
                                '</span>' +
                            '</td>' +
                            '<td>' +
                                '<button class="btn btn-sm btn-primary" onclick="editVip(' + vip.vipID + ')">' +
                                    '<i class="bi bi-pencil"></i>' +
                                '</button>' +
                                '<button class="btn btn-sm btn-danger" onclick="deleteVip(' + vip.vipID + ')">' +
                                    '<i class="bi bi-trash"></i>' +
                                '</button>' +
                            '</td>' +
                        '</tr>'
                    );
                });
            });
        }

        // Load thống kê
        function loadVipStats() {
            $.get('admin/vips/stats', function(data) {
                $('#totalVips').text(data.totalVips);
                
                let totalUsers = 0;
                Object.values(data.userCounts).forEach(count => totalUsers += count);
                $('#totalVipUsers').text(totalUsers);
                
                let totalRevenue = 0;
                Object.values(data.revenues).forEach(revenue => totalRevenue += revenue);
                $('#totalVipRevenue').text(totalRevenue.toLocaleString() + ' VNĐ');
            });
        }

        // Thêm gói VIP mới
        $('#addVipBtn').click(function() {
            const formData = new FormData();
            const vipImg = $('#addVip_img')[0].files[0];
            
            if (vipImg) {
                formData.append('vip_img', vipImg);
            }
            
            formData.append('vipName', $('#addVipName').val());
            formData.append('description', $('#addDescription').val());
            formData.append('vipType', $('#addVipType').val());
            formData.append('price', $('#addPrice').val());
            formData.append('yearlyPrice', $('#addYearlyPrice').val());
            formData.append('duration', $('#addDuration').val());
            formData.append('features', $('#addFeatures').val());
            formData.append('status', $('#addStatus').val());

            $.ajax({
                url: 'admin/vips',
                method: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function() {
                    $('#addVipModal').modal('hide');
                    $('#addVipForm')[0].reset();
                    $('#addVip_img_preview').empty();
                    loadVipList();
                    loadVipStats();
                },
                error: function(xhr) {
                    alert('Có lỗi xảy ra: ' + xhr.responseText);
                }
            });
        });

        // Chỉnh sửa gói VIP
        function editVip(vipId) {
            $.ajax({
                url: 'admin/vips/' + vipId,
                method: 'GET',
                success: function(vip) {
                    $('#editVipId').val(vip.vipID);
                    $('#editVipName').val(vip.vipName);
                    $('#editDescription').val(vip.description);
                    $('#editVipType').val(vip.vipType);
                    $('#editPrice').val(vip.price);
                    $('#editYearlyPrice').val(vip.yearlyPrice);
                    $('#editDuration').val(vip.duration);
                    $('#editFeatures').val(vip.features);
                    $('#editStatus').val(vip.status);
                    $('#editVip_img_url').val(vip.vip_img);
                    
                    // Hiển thị ảnh hiện tại
                    if (vip.vip_img) {
                        $('#editVip_img_preview').html(
                            '<img src="' + vip.vip_img + '" alt="Current VIP Image" style="max-width: 200px;">'
                        );
                    } else {
                        $('#editVip_img_preview').empty();
                    }
                    
                    // Hiển thị modal
                    var editModal = new bootstrap.Modal(document.getElementById('editVipModal'));
                    editModal.show();
                },
                error: function(xhr) {
                    alert('Có lỗi xảy ra khi tải thông tin gói VIP: ' + xhr.responseText);
                }
            });
        }

        // Lưu thay đổi gói VIP
        $('#saveEditVipBtn').click(function() {
            const formData = new FormData();
            const vipImg = $('#editVip_img')[0].files[0];
            
            if (vipImg) {
                formData.append('vip_img', vipImg);
            }
            
            formData.append('vipName', $('#editVipName').val());
            formData.append('description', $('#editDescription').val());
            formData.append('vipType', $('#editVipType').val());
            formData.append('price', $('#editPrice').val());
            formData.append('yearlyPrice', $('#editYearlyPrice').val());
            formData.append('duration', $('#editDuration').val());
            formData.append('features', $('#editFeatures').val());
            formData.append('status', $('#editStatus').val());
            formData.append('currentVip_img', $('#editVip_img_url').val());

            $.ajax({
                url: 'admin/vips/' + $('#editVipId').val(),
                method: 'PUT',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    var editModal = bootstrap.Modal.getInstance(document.getElementById('editVipModal'));
                    editModal.hide();
                    loadVipList();
                    loadVipStats();
                    alert('Cập nhật gói VIP thành công!');
                },
                error: function(xhr) {
                    alert('Có lỗi xảy ra khi cập nhật gói VIP: ' + xhr.responseText);
                }
            });
        });

        // Preview ảnh khi chọn file
        $('#addVip_img').change(function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    $('#addVip_img_preview').html(
                        '<img src="' + e.target.result + '" alt="Preview" style="max-width: 200px;">'
                    );
                }
                reader.readAsDataURL(file);
            }
        });

        $('#editVip_img').change(function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    $('#editVip_img_preview').html(
                        '<img src="' + e.target.result + '" alt="Preview" style="max-width: 200px;">'
                    );
                }
                reader.readAsDataURL(file);
            }
        });

        // Xóa gói VIP
        function deleteVip(vipId) {
            if (confirm('Bạn có chắc chắn muốn xóa gói VIP này?')) {
                $.ajax({
                    url: 'admin/vips/' + vipId,
                    method: 'DELETE',
                    success: function() {
                        loadVipList();
                        loadVipStats();
                    },
                    error: function(xhr) {
                        alert('Có lỗi xảy ra: ' + xhr.responseText);
                    }
                });
            }
        }

        // Load dữ liệu khi trang được tải
        $(document).ready(function() {
            loadVipList();
            loadVipStats();
        });
    </script>
</body>
</html> 