<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang Chủ</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .clickable-card {
            cursor: pointer;
            transition: transform 0.2s;
        }
        .clickable-card:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body>

<c:import url="header.jsp"/>

<div class="container text-center mt-5">
    <%-- Hiển thị tên expert từ session --%>
    <c:choose>
        <c:when test="${not empty sessionScope.expert}">
            <h1 class="mb-4">Xin chào ${sessionScope.expert.fullName},</h1>
        </c:when>
        <c:otherwise>
            <h1 class="mb-4">Xin chào Nguyễn Văn A,</h1>
        </c:otherwise>
    </c:choose>

    <div class="row justify-content-center">
        <%-- Card 1: Khóa Học Của Bạn --%>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/course?action=list" class="text-decoration-none">
                <div class="card p-3 shadow clickable-card">
                    <img src="./asset/jpg/cld-sample-3_Square.jpg" class="card-img-top" alt="Khóa Học Của Bạn">
                    <div class="card-body">
                        <h5 class="card-title">Khóa Học Của Bạn</h5>
                    </div>
                </div>
            </a>
        </div>

        <%-- Card 2: Thống kê chi tiết --%>
        <div class="col-md-3">
            <div class="card p-3 shadow">
                <img src="./asset/jpg/cld-sample_Square.jpg" class="card-img-top" alt="Thống kê chi tiết">
                <div class="card-body">
                    <h5 class="card-title">Thống kê chi tiết</h5>
                </div>
            </div>
        </div>

        <%-- Card 3: Xem Đề Thi, Bài Tập --%>
        <div class="col-md-3">
            <div class="card p-3 shadow">
                <img src="./asset/jpg/man-portrait_Square.jpg"  class="card-img-top" alt="Xem Đề Thi, Bài Tập">
                <div class="card-body">
                    <h5 class="card-title">Xem Đề Thi, Bài Tập</h5>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="footer.jsp"/>

</body>
</html>
