<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý đơn đăng ký Expert</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background: #f8f9fa; }
        .nav-item{ margin: 5px 0 5px 0px; }
        .sidebar { min-height: 100vh; background: #fff; color: #5f72bd; border-right: 1px solid #e3e3e3; border-radius: 20px; }
        .sidebar .nav-link { color: #5f72bd; font-weight: 500; border-radius: 8px; }
        .sidebar .nav-link.active, .sidebar .nav-link:hover { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; }
        .main-content { padding: 20px; background: #f8f9fa; border-radius: 20px; }
        .header-gradient { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; border-radius: 12px; padding: 24px 32px; margin-bottom: 32px; box-shadow: 0 4px 24px rgba(106,130,251,0.08); }
        .table th, .table td { vertical-align: middle !important; text-align: center; }
        .table th { font-weight: bold; background: #f1f3f6; }
        .table td img { display: block; margin: 0 auto; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.07); }
        .table td, .table th { padding: 8px 6px; }
        .btn { min-width: 70px; }
        .btn-action {
            font-weight: 500;
            border-radius: 20px;
            padding: 6px 18px;
            margin: 2px 4px;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.04);
            transition: background 0.2s, color 0.2s;
        }
        .btn-approve {
            background: #41a30d;
            color: #fff;
            border: none;
        }
        .btn-approve:hover {
            background: #5cd31b;
            color: #fff;
        }
        .btn-reject {
            background: #bdb9b9;
            color: #fff;
            border: none;
        }
        .btn-reject:hover {
            background: #b2b280;
            color: #fff;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="admin-sidebar.jsp">
            <jsp:param name="active" value="experts"/>
        </jsp:include>
        <!-- Main content -->
        <div class="col-md-9 col-lg-10 main-content">
            <div class="header-gradient mb-4 d-flex align-items-center justify-content-between">
                <h2 class="mb-0">Duyệt đơn đăng ký Expert</h2>
            </div>
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Tên đăng nhập</th>
                                <th>Họ tên</th>
                                <th>Email</th>
                                <th>Số điện thoại</th>
                                <th>Ngày đăng ký</th>
                                <th>Giới tính</th>
                                <th>Avatar</th>
                                <th>Certificate</th>
                                <th>CCCD Front</th>
                                <th>CCCD Back</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody id="expertRegisterTable"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal xem ảnh lớn -->
<div class="modal fade" id="imgModal" tabindex="-1" aria-labelledby="imgModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content bg-transparent border-0">
      <div class="modal-body text-center">
        <img id="modalImg" src="" alt="Xem ảnh" style="max-width:100%;max-height:80vh;border-radius:10px;box-shadow:0 0 10px #333;">
      </div>
    </div>
  </div>
</div>
<script>
function loadExpertRegisters() {
    fetch('/Hankyo/admin/expert-registers')
        .then(res => res.json())
        .then(data => {
            const tb = document.getElementById('expertRegisterTable');
            tb.innerHTML = '';
            data.forEach(function(e) {
                tb.innerHTML +=
                    '<tr>'
                    + '<td>' + e.registerID + '</td>'
                    + '<td>' + e.username + '</td>'
                    + '<td>' + e.fullName + '</td>'
                    + '<td>' + e.gmail + '</td>'
                    + '<td>' + e.phone + '</td>'
                    + '<td>' + (e.dateCreate ? e.dateCreate : '') + '</td>'
                    + '<td>' + e.gender + '</td>'
                    + '<td>' + (e.avatar ? '<img src="' + e.avatar + '" alt="avatar" style="width:60px;height:60px;object-fit:cover;border-radius:8px;cursor:pointer;" onclick="showImgModal(\'' + e.avatar + '\')">' : '') + '</td>'
                    + '<td>' + (e.certificate ? (e.certificate.toLowerCase().endsWith('.pdf')
                        ? '<a href="' + e.certificate + '" target="_blank">Xem PDF</a>'
                        : '<img src="' + e.certificate + '" alt="certificate" style="width:60px;height:60px;object-fit:cover;cursor:pointer;" onclick="showImgModal(\'' + e.certificate + '\')">') : '') + '</td>'
                    + '<td>' + (e.cccdFront ? '<img src="' + e.cccdFront + '" alt="CCCD Front" style="width:60px;height:60px;object-fit:cover;cursor:pointer;" onclick="showImgModal(\'' + e.cccdFront + '\')">' : '') + '</td>'
                    + '<td>' + (e.cccdBack ? '<img src="' + e.cccdBack + '" alt="CCCD Back" style="width:60px;height:60px;object-fit:cover;cursor:pointer;" onclick="showImgModal(\'' + e.cccdBack + '\')">' : '') + '</td>'
                    + '<td>' + e.approveStatus + '</td>'
                    + '<td>'
                        + '<button class="btn btn-action btn-approve" onclick="approveRegister(' + e.registerID + ')"><i class=\'bi bi-check-circle\'></i> Duyệt</button>'
                        + '<button class="btn btn-action btn-reject" onclick="rejectRegister(' + e.registerID + ')"><i class=\'bi bi-x-circle\'></i> Từ chối</button>'
                    + '</td>'
                    + '</tr>';
            });
        });
}

function approveRegister(id) {
    if (!confirm('Bạn chắc chắn muốn duyệt đơn này?')) return;
    fetch('/Hankyo/admin/expert-registers', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'registerId=' + id + '&action=approve'
    })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            alert(data.message || 'Duyệt thành công!');
            loadExpertRegisters();
        });
}

function rejectRegister(id) {
    if (!confirm('Bạn chắc chắn muốn từ chối đơn này?')) return;
    fetch('/Hankyo/admin/expert-registers', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'registerId=' + id + '&action=reject'
    })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            alert(data.message || 'Đã từ chối!');
            loadExpertRegisters();
        });
}

function showImgModal(url) {
    var modalImg = document.getElementById('modalImg');
    modalImg.src = url;
    var modal = new bootstrap.Modal(document.getElementById('imgModal'));
    modal.show();
}

window.onload = loadExpertRegisters;
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 