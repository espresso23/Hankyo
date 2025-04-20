<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
            rel="stylesheet">
    <title>About Us</title>
    <link rel="stylesheet" href="asset/css/about-us.css">
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-image: url('asset/png/about-us/background-2.png');
            background-size: cover;
            background-position: left center;
        }
    </style>
</head>

<!-- Main Content -->
<jsp:include page="/header.jsp" />
<body>

<!-- Section 1 -->
<section class="about-us">
    <div class="title-container">
        <h2>Về chúng tôi</h2>
        <h3>Hankyo - Dự Giấc Mơ Hàn Quốc Đến Gần Hơn Với Bạn</h3>
    </div>
    <div class="about-container">
        <!-- Phần văn bản bên trái -->
        <div class="text-container">
            <!-- Chấm xanh -->
            <img src="asset/png/about-us/blue-dot.png" alt="Blue Dot" class="blue-dot">

            <h4>Chúng tôi cũng đã từng là bạn!</h4>
            <p>
                Là những người đã và đang trong quá trình học tiếng Hàn, chúng tôi hiểu những khó khăn và vất vả mà
                các bạn đã trải qua. Chính vì thế, chúng tôi xây dựng nền tảng này giúp các bạn tiết kiệm thời gian,
                công sức và việc học tiếng Hàn không còn là ước mơ.
            </p>
        </div>

        <!-- Hình ảnh bên phải -->
        <div class="image-container">
            <img src="asset/png/about-us/photo-section.png" alt="Ảnh minh họa 3 lớp" class="main-image">
        </div>
    </div>

    <!-- Mũi tên trang trí -->
    <img src="asset/png/about-us/pink-arrow.png" alt="Pink Arrow" class="pink-arrow">
</section>


<!-- Scholarship Section -->
<section class="scholarship">
    <div class="container scholarship-content">
        <div class="scholarship-images">
            <div class="scholarship-image">
                <img src="asset/png/about-us/scholarship1.png" alt="Học bổng">
            </div>
            <div class="scholarship-image">
                <img src="asset/png/about-us/scholarship2.png" alt="Giấy chứng nhận">
            </div>
            <div class="scholarship-image">
                <img src="asset/png/about-us/scholarship3.png" alt="Cờ Hàn Quốc">
            </div>
            <div class="scholarship-image">
                <img src="asset/png/about-us/scholarship4.png" alt="Sinh viên học tập">
            </div>
        </div>
        <div class="scholarship-text">
            <h2>Mục tiêu hướng đến:</h2>
            <p>Trở thành nền tảng học tiếng Hàn trực tuyến hàng đầu tại Việt Nam, cung cấp cho học viên một công cụ
                cần thiết để đạt điểm cao trong kỳ thi TOPIK, phục vụ cho công việc và ước mơ của bạn.</p>
        </div>
        <img src="asset/png/about-us/yellow-dot.png" alt="Yellow Star" class="yellow-star">
    </div>
</section>

<!-- Faculty Section -->
<section class="faculty">
    <div class="container">
        <img src="asset/png/about-us/orange-arrow.png" alt="Orange Arrow" class="orange-arrow">
        <h2>Đội ngũ giảng dạy</h2>
        <p>
            Bao gồm các chuyên gia trong lĩnh vực dạy tiếng Hàn, với nhiều năm kinh nghiệm và trình độ người từ bậc TOPIK 5 trở lên.
            Đồng thời, họ còn là những người đam mê lên lớp trực và luôn sẵn sàng hỗ trợ học viên hết mình.
        </p>

        <div class="box">
            <div class="card">
                <div class="imgBx">
                    <img src="asset/png/about-us/teacher1.png" alt="Giảng viên 1">
                </div>
                <div class="details">
                    <h2>Nguyễn Thị Mai<br><span>Chuyên gia TOPIK 6</span></h2>
                </div>
            </div>

            <div class="card">
                <div class="imgBx">
                    <img src="asset/png/about-us/teacher2.png" alt="Giảng viên 2">
                </div>
                <div class="details">
                    <h2>Trần Văn Hùng<br><span>Giảng viên tiếng Hàn</span></h2>
                </div>
            </div>

            <div class="card">
                <div class="imgBx">
                    <img src="asset/png/about-us/teacher3.png" alt="Giảng viên 3">
                </div>
                <div class="details">
                    <h2>Lê Khánh Linh<br><span>TOPIK Master & Biên dịch</span></h2>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Free Trial Section -->
<section class="free-trial">
    <img src="asset/png/about-us/trial-icon.png" alt="Trial Icon" class="trial-icon">
    <div class="container">
        <h2>Dùng thử miễn phí ngay</h2>
        <p>Khám phá ngay khóa học tiếng Hàn của chúng tôi và bắt đầu học mà không cần lo lắng về chi phí!</p>
        <a href="#" class="cta-button">Bắt đầu ngay</a>

    </div>
</section>

<!-- Thank You Section -->
<section class="thank-you">
    <img src="asset/png/about-us/blue-dot.png" alt="Blue Dot" class="blue-dot">
    <div class="container">
        <h2>Thanks for stopping by 👋</h2>
    </div>
</section>

</body>
<jsp:include page="/footer.jsp" />
</html>