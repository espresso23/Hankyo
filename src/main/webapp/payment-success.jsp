<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh Toán Thành Công</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<c:import url="header.jsp"/>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card text-center">
                <div class="card-body">
                    <i class="fas fa-check-circle text-success" style="font-size: 4rem;"></i>
                    <h2 class="mt-3">Thanh Toán Thành Công!</h2>
                    <p class="text-muted">Cảm ơn bạn đã mua khóa học. Bạn có thể truy cập khóa học ngay bây giờ.</p>
                    <div class="mt-4">
                        <a href="my-courses" class="btn btn-primary me-2">
                            <i class="fas fa-book me-2"></i>Xem Khóa Học Của Tôi
                        </a>
                        <a href="courses" class="btn btn-outline-secondary">
                            <i class="fas fa-shopping-cart me-2"></i>Tiếp Tục Mua Sắm
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
</body>
</html> 