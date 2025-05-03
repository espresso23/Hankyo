<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
      .nav-item{
            margin: 5px 0 5px 0px;
        }
.sidebar { min-height: 100vh; background: #fff; color: #5f72bd; border-right: 1px solid #e3e3e3; border-radius: 20px; }
.sidebar .nav-link { color: #5f72bd; font-weight: 500; border-radius: 8px; }
.sidebar .nav-link.active, .sidebar .nav-link:hover { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; }
.main-content { padding: 20px; background: #f8f9fa; border-radius: 20px; }
.header-gradient { background: linear-gradient(90deg, #6a82fb 0%, #fc5c7d 100%); color: #fff; border-radius: 12px; padding: 24px 32px; margin-bottom: 32px; box-shadow: 0 4px 24px rgba(106,130,251,0.08); }
</style>
<div class="col-md-3 col-lg-2 sidebar p-3">
    <h3 class="mb-4">Hankyo Admin</h3>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link ${param.active == 'dashboard' ? 'active' : ''}" href="admin-dashboard">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'users' ? 'active' : ''}" href="admin-users">
                <i class="bi bi-people me-2"></i>Quản lý người dùng
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'courses' ? 'active' : ''}" href="admin-courses">
                <i class="bi bi-book me-2"></i>Quản lý khóa học
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'payments' ? 'active' : ''}" href="admin-payments">
                <i class="bi bi-cash me-2"></i>Quản lý thanh toán
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'posts' ? 'active' : ''}" href="admin-posts">
                <i class="bi bi-file-text me-2"></i>Quản lý nội dung
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'experts' ? 'active' : ''}" href="admin-experts">
                <i class="bi bi-person-badge me-2"></i>Quản lý expert
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'reports' ? 'active' : ''}" href="admin-reports">
                <i class="bi bi-flag me-2"></i>Quản lý báo cáo
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'categories' ? 'active' : ''}" href="admin-categories">
                <i class="bi bi-tags me-2"></i>Quản lý danh mục
            </a>
        </li>
    </ul>
</div> 