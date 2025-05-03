<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý báo cáo</title>
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
        .header-bar { background: #23406e; color: #fff; border-radius: 12px; padding: 20px 32px; margin-bottom: 24px; box-shadow: 0 4px 24px rgba(106,130,251,0.08); }
        .filter-bar { gap: 12px; margin-bottom: 18px; }
        .badge-status { font-size: 0.95em; font-weight: 600; border-radius: 8px; padding: 5px 14px; }
        .badge-status.pending { background: #ffc107; color: #333; }
        .badge-status.approved { background: #2e7d32; color: #fff; }
        .badge-status.rejected { background: #b6b6b6; color: #fff; }
        .badge-status.blocked { background: #d32f2f; color: #fff; }
        .table thead th { background: #f4f6fa; }
        .btn-action { border-radius: 8px; font-weight: 600; margin-right: 6px; }
        .btn-approve { background: #2e7d32; color: #fff; }
        .btn-reject { background: #b6b6b6; color: #fff; }
        .btn-block { background: #d32f2f; color: #fff; }
        .btn-action:hover { opacity: 0.9; }
        .modal-content { border-radius: 18px; }
        .modal-header { background: #23406e; color: #fff; border-radius: 18px 18px 0 0; }
        .loading-spinner { display: flex; align-items: center; justify-content: center; height: 120px; }
        .loading-spinner .spinner-border { width: 2.5rem; height: 2.5rem; color: #23406e; }
        @media (max-width: 768px) { .filter-bar { flex-direction: column; align-items: stretch; } }
        .type-badge {
            display: inline-block;
            min-width: 80px;
            padding: 5px 12px;
            border-radius: 12px;
            font-weight: 600;
            text-align: center;
            font-size: 0.97em;
        }
        .type-course { background: #e3f2fd; color: #1976d2; }
        .type-chat { background: #fff3e0; color: #ef6c00; }
        .type-post { background: #fce4ec; color: #c2185b; }
        .type-comment { background: #e8f5e9; color: #388e3c; }
        .action-btn-group { display: flex; flex-direction: column; gap: 8px; align-items: center; justify-content: center; }
        .btn-action { border-radius: 10px; min-width: 38px; min-height: 36px; display: flex; align-items: center; justify-content: center; font-size: 1em; margin: 0 auto; }
        .btn-approve, .btn-reject { width: 90px; height: 36px; font-size: 1em; }
        .btn-info { width: 38px; height: 38px; padding: 0; }
        .table td, .table th { vertical-align: middle !important; text-align: center; }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="admin-sidebar.jsp">
            <jsp:param name="active" value="reports"/>
        </jsp:include>

        <!-- Main content -->
        <div class="col-md-9 col-lg-10 main-content">
            <div class="header-bar mb-4 d-flex align-items-center justify-content-between">
                <h2 class="mb-0">Quản lý báo cáo</h2>
            </div>
            <div class="row mb-3" id="reportStatsRow">
                <div class="col-auto"><span class="fw-bold">Tổng:</span> <span id="totalReports">0</span></div>
                <div class="col-auto"><span class="text-warning fw-bold">Chờ xử lý:</span> <span id="pendingReports">0</span></div>
                <div class="col-auto"><span class="text-success fw-bold">Đã duyệt:</span> <span id="approvedReports">0</span></div>
                <div class="col-auto"><span class="text-secondary fw-bold">Từ chối:</span> <span id="rejectedReports">0</span></div>
            </div>
            <div class="d-flex filter-bar flex-wrap align-items-center">
                <select id="statusFilter" class="form-select form-select-sm" style="width: 150px;">
                    <option value="">Tất cả trạng thái</option>
                    <option value="pending">Chờ xử lý</option>
                    <option value="approved">Đã duyệt</option>
                    <option value="rejected">Đã từ chối</option>
                    <option value="blocked">Đã khóa</option>
                </select>
                <select id="typeFilter" class="form-select form-select-sm" style="width: 150px;">
                    <option value="">Tất cả loại</option>
                    <option value="course">Khóa học</option>
                    <option value="chat">Tin nhắn</option>
                    <option value="post">Bài viết</option>
                    <option value="comment">Bình luận</option>
                </select>
                <input type="text" id="searchInput" class="form-control form-control-sm" style="max-width: 220px;" placeholder="Tìm kiếm...">
            </div>
            <div class="card">
                <div class="card-body p-0">
                    <div id="loading" class="loading-spinner" style="display:none"><div class="spinner-border" role="status"></div></div>
                    <table class="table table-bordered table-hover mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Người báo cáo</th>
                                <th>Bị báo cáo</th>
                                <th>Loại</th>
                                <th>Lý do</th>
                                <th>Trạng thái</th>
                                <th>Ngày gửi</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody id="reportTable"></tbody>
                    </table>
                    <nav class="mt-3 d-flex justify-content-center">
                        <ul class="pagination" id="pagination"></ul>
                    </nav>
                </div>
            </div>
            <!-- Modal chi tiết báo cáo -->
            <div class="modal fade" id="reportDetailModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Chi tiết báo cáo</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body" id="reportDetailContent"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
const contextPath = window.location.pathname.split('/').slice(0,2).join('/');
function showLoading() { document.getElementById('loading').style.display = 'flex'; }
function hideLoading() { document.getElementById('loading').style.display = 'none'; }
let allReports = [];
let currentPage = 1;
let pageSize = 10;
let filteredReports = [];
document.addEventListener('DOMContentLoaded', function() {
    loadReportStats();
    loadReports();
    document.getElementById('statusFilter').addEventListener('change', applyFilters);
    document.getElementById('typeFilter').addEventListener('change', applyFilters);
    document.getElementById('searchInput').addEventListener('input', applyFilters);
});
function loadReportStats() {
    fetch(contextPath + '/admin/reports/status-counts')
        .then(res => res.json())
        .then(data => {
            let total = 0;
            let pending = data['pending'] || 0;
            let approved = data['approved'] || 0;
            let rejected = data['rejected'] || 0;
            total = pending + approved + rejected;
            document.getElementById('totalReports').innerText = total;
            document.getElementById('pendingReports').innerText = pending;
            document.getElementById('approvedReports').innerText = approved;
            document.getElementById('rejectedReports').innerText = rejected;
        });
}
function loadReports() {
    showLoading();
    fetch(contextPath + '/admin/reports')
        .then(res => res.json())
        .then(data => {
            allReports = data;
            hideLoading();
            applyFilters();
        })
        .catch(() => { hideLoading(); });
}
function applyFilters() {
    const status = document.getElementById('statusFilter').value;
    const type = document.getElementById('typeFilter').value;
    const keyword = document.getElementById('searchInput').value.trim().toLowerCase();
    filteredReports = allReports.filter(r => {
        let match = true;
        if (status) match = match && r.status && r.status.toLowerCase() === status;
        if (type) match = match && r.typeName && r.typeName.toLowerCase() === type;
        if (keyword) match = match && (
            (r.reporterName && r.reporterName.toLowerCase().includes(keyword)) ||
            (r.reportedUserName && r.reportedUserName.toLowerCase().includes(keyword)) ||
            (r.reason && r.reason.toLowerCase().includes(keyword))
        );
        return match;
    });
    currentPage = 1;
    renderTable(filteredReports);
    renderPagination(filteredReports);
}
function renderTable(data) {
    const tb = document.getElementById('reportTable');
    tb.innerHTML = '';
    if (!data || data.length === 0) {
        tb.innerHTML = '<tr><td colspan="8" class="text-center text-muted">Không có báo cáo nào phù hợp.</td></tr>';
        return;
    }
    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const pageData = data.slice(start, end);
    pageData.forEach(function(r) {
        let typeClass = '';
        if (r.typeName === 'course') typeClass = 'type-course';
        else if (r.typeName === 'chat') typeClass = 'type-chat';
        else if (r.typeName === 'post') typeClass = 'type-post';
        else if (r.typeName === 'comment') typeClass = 'type-comment';
        tb.innerHTML += '<tr>'
            + '<td>' + r.reportID + '</td>'
            + '<td>' + (r.reporterName || '') + '</td>'
            + '<td>' + (r.reportedUserName || '') + '</td>'
            + '<td><span class="type-badge ' + typeClass + '">' + (r.typeName === 'course' ? 'Khóa học' : r.typeName === 'chat' ? 'Tin nhắn' : r.typeName === 'post' ? 'Bài viết' : r.typeName === 'comment' ? 'Bình luận' : r.typeName) + '</span></td>'
            + '<td>' + (r.reason || '') + '</td>'
            + '<td><span class="badge-status ' + (r.status ? r.status.toLowerCase() : '') + '">' + (r.status || '') + '</span></td>'
            + '<td>' + (r.reportDate ? new Date(r.reportDate).toLocaleDateString('vi-VN') : '') + '</td>'
            + '<td><div class="action-btn-group">'
                + '<button class="btn btn-info btn-sm btn-action" onclick="viewReportDetail(' + r.reportID + ')"><i class="bi bi-eye"></i></button>'
                + (r.status === 'pending' ? '<button class="btn btn-approve btn-sm btn-action" onclick="approveReport(' + r.reportID + ',\'approved\')">Duyệt</button>' : '')
                + (r.status === 'pending' ? '<button class="btn btn-reject btn-sm btn-action" onclick="approveReport(' + r.reportID + ',\'rejected\')">Từ chối</button>' : '')
            + '</div></td>'
            + '</tr>';
    });
}
function renderPagination(data) {
    const total = data.length;
    const totalPages = Math.ceil(total / pageSize);
    const pag = document.getElementById('pagination');
    pag.innerHTML = '';
    if (totalPages <= 1) return;
    let prevDisabled = currentPage === 1 ? 'disabled' : '';
    let nextDisabled = currentPage === totalPages ? 'disabled' : '';
    pag.innerHTML += '<li class="page-item ' + prevDisabled + '"><a class="page-link" href="#" onclick="gotoPage(' + (currentPage-1) + ');return false;">Previous</a></li>';
    for (let i = 1; i <= totalPages; i++) {
        pag.innerHTML += '<li class="page-item ' + (i===currentPage?'active':'') + '"><a class="page-link" href="#" onclick="gotoPage(' + i + ');return false;">' + i + '</a></li>';
    }
    pag.innerHTML += '<li class="page-item ' + nextDisabled + '"><a class="page-link" href="#" onclick="gotoPage(' + (currentPage+1) + ');return false;">Next</a></li>';
}
function gotoPage(page) {
    const totalPages = Math.ceil(filteredReports.length / pageSize);
    if (page < 1 || page > totalPages) return;
    currentPage = page;
    renderTable(filteredReports);
    renderPagination(filteredReports);
}
function viewReportDetail(id) {
    const r = allReports.find(x => x.reportID === id);
    if (!r) return;
    if (r.typeName === 'course' && r.courseID) {
        window.open('/Hankyo/learn-course?courseID=' + r.courseID, '_blank');
        return;
    }
    // Các loại khác hiện tại không làm gì nữa
}
function approveReport(id, status) {
    if (!confirm('Bạn chắc chắn muốn cập nhật trạng thái báo cáo này?')) return;
    fetch(contextPath + '/admin/reports/' + id + '/approve', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'status=' + encodeURIComponent(status)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) loadReports();
        else alert('Cập nhật thất bại!');
    });
}
</script>
</body>
</html> 