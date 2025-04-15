<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${course.courseTitle} - Chi tiết khóa học</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="./asset/css/course.css">
    <style>
        .course-detail-header {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .course-image {
            height: 300px;
            object-fit: cover;
            border-radius: 8px;
            width: 100%;
        }

        .info-label {
            font-weight: 600;
            color: #495057;
        }

        .info-value {
            color: #212529;
            margin-bottom: 1rem;
        }

        .edit-form {
            background-color: #fff;
            border-radius: 10px;
            padding: 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .preview-image {
            max-height: 200px;
            object-fit: contain;
            margin-top: 10px;
            display: none;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="container my-5">
    <!-- Thông báo thành công/error -->
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="course-detail-header">
        <div class="row">
            <div class="col-md-6">
                <img src="${course.courseImg}" alt="${course.courseTitle}" class="course-image mb-3">
            </div>
            <div class="col-md-6">
                <h2 class="fw-bold mb-3">${course.courseTitle}</h2>
                <div class="mb-3">
                    <span class="info-label">Trạng thái: </span>
                    <span class="badge ${course.status eq 'Active' ? 'bg-success' : 'bg-secondary'}">
                        ${course.status}
                    </span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Giá: </span>
                    <span class="info-value fw-bold text-danger">
                        <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="VNĐ"/>
                    </span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Ngày tạo: </span>
                    <span class="info-value">
                        <fmt:formatDate value="${course.dateCreated}" pattern="dd/MM/yyyy HH:mm"/>
                    </span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Cập nhật lần cuối: </span>
                    <span class="info-value">
                        <fmt:formatDate value="${course.lastUpdated}" pattern="dd/MM/yyyy HH:mm"/>
                    </span>
                </div>
                <div class="d-flex gap-2 mt-4">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editCourseModal">
                        <i class="fas fa-edit me-2"></i>Cập nhật
                    </button>
                    <a href="course?action=list" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Quay lại
                    </a>
                </div>
            </div>
        </div>
    </div>
    <%-- Hiển thị thông báo --%>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-${sessionScope.messageType} alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="message" scope="session"/>
        <c:remove var="messageType" scope="session"/>
    </c:if>
    <div class="row">
        <div class="col-md-12">
            <div class="card mb-4">
                <div class="card-header bg-white">
                    <h4 class="mb-0"><i class="fas fa-info-circle me-2"></i>Mô tả khóa học</h4>
                </div>
                <div class="card-body">
                    <p class="card-text">${course.courseDescription}</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal chỉnh sửa -->
<div class="modal fade" id="editCourseModal" tabindex="-1" aria-labelledby="editCourseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editCourseModalLabel">Cập nhật khóa học</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="course" method="post" enctype="multipart/form-data" class="edit-form">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="courseID" value="${course.courseID}">

                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="editTitle" class="form-label">Tiêu đề</label>
                                <input type="text" class="form-control" id="editTitle" name="title"
                                       value="${course.courseTitle}" required>
                            </div>

                            <div class="mb-3">
                                <label for="editPrice" class="form-label">Giá (VNĐ)</label>
                                <input type="number" class="form-control" id="editPrice" name="price"
                                       value="${course.price}" min="0" required>
                            </div>

                            <div class="mb-3">
                                <label for="editStatus" class="form-label">Trạng thái</label>
                                <select class="form-select" id="editStatus" name="status">
                                    <option value="Active" ${course.status eq 'Active' ? 'selected' : ''}>Hoạt động
                                    </option>
                                    <option value="Inactive" ${course.status eq 'Inactive' ? 'selected' : ''}>Tạm
                                        ngừng
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="editImage" class="form-label">Ảnh bìa</label>
                                <input type="file" class="form-control" id="editImage" name="image"
                                       accept="image/*" onchange="previewImage(this)">
                                <small class="text-muted">Để trống nếu không thay đổi ảnh</small>
                                <img id="imagePreview" src="${course.courseImg}" class="preview-image img-thumbnail">
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="editDescription" class="form-label">Mô tả</label>
                        <textarea class="form-control" id="editDescription" name="description"
                                  rows="5" required>${course.courseDescription}</textarea>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<c:import url="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Hiển thị preview ảnh khi chọn file mới
    function previewImage(input) {
        const preview = document.getElementById('imagePreview');
        const file = input.files[0];

        if (file) {
            const reader = new FileReader();

            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
                console.log("Preview image loaded"); // Thêm dòng debug
            }

            reader.readAsDataURL(file);
        } else {
            console.log("No file selected"); // Thêm dòng debug
        }
    }

    // Hiển thị ảnh preview khi modal được mở
    document.addEventListener('DOMContentLoaded', function () {
        var editModal = document.getElementById('editCourseModal');
        var imagePreview = document.getElementById('imagePreview');

        // Luôn hiển thị ảnh preview với ảnh hiện tại
        if (imagePreview) {
            imagePreview.style.display = 'block';
            console.log("Initial image preview displayed"); // Debug
        }

        editModal.addEventListener('show.bs.modal', function () {
            if (imagePreview) {
                imagePreview.style.display = 'block';
                console.log("Modal opened - image preview shown"); // Debug
            }
        });
    });
</script>

</body>
</html>