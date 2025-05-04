<%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 5/1/2025
  Time: 9:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HANKYO Careers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
    <style>
        :root {
            --primary-color: #b2e0ff;
            --secondary-color: #f89fd3;
            --text-color: #333;
            --light-bg: #F8F9FA;
        }

        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            line-height: 1.6;
            color: var(--text-color);
            background-color: #fff;
            margin: 0;
            padding: 0;
        }

        .recruitment-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            min-height: 80vh;
        }

        .content-left {
            flex: 1;
            padding-right: 50px;
        }

        .content-right {
            flex: 1;
            position: relative;
        }

        .title {
            font-size: 2.5rem;
            color: var(--primary-color);
            margin-bottom: 20px;
            font-weight: bold;
        }

        .description {
            font-size: 1.2rem;
            color: var(--text-color);
            margin-bottom: 30px;
            line-height: 1.8;
        }

        .cta-button {
            display: inline-block;
            padding: 15px 30px;
            background-color: var(--secondary-color);
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
        }

        .cta-button:hover {
            background-color: #e88c08;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 159, 28, 0.3);
        }

        .image-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            position: relative;
        }

        .image-card {
            background: var(--light-bg);
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }

        .image-card img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .image-card:hover {
            transform: translateY(-5px);
        }

        .floating-shape {
            position: absolute;
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background-color: var(--primary-color);
            opacity: 0.1;
            z-index: -1;
        }

        .shape-1 {
            top: -20px;
            right: -20px;
        }

        .shape-2 {
            bottom: -20px;
            left: -20px;
        }

        .shape-3 {
            top: 50%;
            right: -50px;
            width: 150px;
            height: 150px;
        }

        @media (max-width: 768px) {
            .recruitment-container {
                flex-direction: column;
                text-align: center;
            }

            .content-left {
                padding-right: 0;
                margin-bottom: 40px;
            }

            .image-grid {
                grid-template-columns: 1fr;
            }
        }

        /* Thêm style mới cho phần statistics */
        .statistics-section {
            background-color: #0D6EFD;
            padding: 60px 0;
            color: white;
            text-align: center;
            margin-top: 40px;
        }

        .statistics-container {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-around;
            align-items: center;
            flex-wrap: wrap;
        }

        .statistic-item {
            padding: 20px;
            flex: 1;
            min-width: 250px;
        }

        .statistic-number {
            font-size: 3.5rem;
            font-weight: bold;
            margin-bottom: 15px;
            line-height: 1;
        }

        .statistic-label {
            font-size: 1.1rem;
            opacity: 0.9;
            line-height: 1.4;
        }

        @media (max-width: 768px) {
            .statistics-container {
                flex-direction: column;
            }

            .statistic-item {
                margin: 20px 0;
            }
        }

        /* Style cho phần special-section */
        .special-section {
            padding: 80px 0;
            background-color: #fff;
        }

        .special-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 50px;
        }

        .special-content {
            flex: 1;
        }

        .special-image {
            flex: 1;
        }

        .special-title {
            font-size: 2rem;
            color: #0D6EFD;
            margin-bottom: 25px;
            font-weight: bold;
        }

        .special-description {
            font-size: 1.1rem;
            color: #666;
            line-height: 1.8;
            margin-bottom: 20px;
        }

        .special-image img {
            width: 100%;
            max-width: 600px;
            height: auto;
        }

        @media (max-width: 768px) {
            .special-container {
                flex-direction: column;
                text-align: center;
            }

            .special-image {
                order: -1; /* Hiển thị hình ảnh trước nội dung trên mobile */
            }
        }

        /* Thêm style mới cho phần jobs */
        .jobs-section {
            padding: 80px 0;
            background-color: #f8f9fa;
        }

        .jobs-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .jobs-header {
            text-align: center;
            margin-bottom: 50px;
        }

        .jobs-title {
            font-size: 2.5rem;
            color: #333;
            margin-bottom: 15px;
            font-weight: bold;
        }

        .jobs-subtitle {
            font-size: 1.1rem;
            color: #666;
        }

        .jobs-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }

        .job-card {
            background: white;
            padding: 30px 20px;
            border-radius: 10px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .job-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .job-icon {
            font-size: 2.5rem;
            color: #0D6EFD;
            margin-bottom: 15px;
        }

        .job-title {
            font-size: 1.2rem;
            color: #333;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .job-positions {
            color: #666;
            font-size: 0.9rem;
        }

        @media (max-width: 1024px) {
            .jobs-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 576px) {
            .jobs-grid {
                grid-template-columns: 1fr;
            }
        }

        /* Thêm style mới cho phần slider */
        .love-prep-section {
            padding: 80px 0;
            background-color: #fff;
        }

        .love-prep-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .love-prep-header {
            text-align: center;
            margin-bottom: 50px;
        }

        .love-prep-title {
            font-size: 2.5rem;
            color: #333;
            margin-bottom: 30px;
            font-weight: bold;
        }

        .swiper {
            width: 100%;
            padding: 30px 50px;
        }

        .swiper-slide {
            background: #fff;
            border-radius: 20px;
            padding: 40px;
            display: flex;
            gap: 40px;
            align-items: center;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .slide-image {
            flex: 1;
            position: relative;
        }

        .slide-image img {
            width: 100%;
            max-width: 500px;
            border-radius: 15px;
        }

        .slide-content {
            flex: 1;
        }

        .slide-title {
            font-size: 2rem;
            color: #0D6EFD;
            margin-bottom: 25px;
            font-weight: bold;
        }

        .benefit-list {
            list-style: none;
            padding: 0;
        }

        .benefit-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            font-size: 1.1rem;
            color: #666;
        }

        .benefit-item:before {
            content: "✓";
            color: #0D6EFD;
            margin-right: 10px;
            font-weight: bold;
        }

        .swiper-button-next,
        .swiper-button-prev {
            color: #0D6EFD;
            background: white;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .swiper-button-next:after,
        .swiper-button-prev:after {
            font-size: 18px;
        }

        .swiper-pagination-bullet {
            background: #0D6EFD;
        }

        @media (max-width: 768px) {
            .swiper-slide {
                flex-direction: column;
                padding: 20px;
            }

            .slide-image {
                order: -1;
            }

            .slide-title {
                font-size: 1.5rem;
            }
        }

        /* Thêm style mới cho phần departments */
        .departments-section {
            padding: 80px 0;
            background-color: #fff;
        }

        .departments-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .departments-header {
            text-align: center;
            margin-bottom: 50px;
        }

        .departments-title {
            font-size: 2.5rem;
            color: #333;
            margin-bottom: 15px;
            font-weight: bold;
        }

        .departments-subtitle {
            font-size: 1.1rem;
            color: #666;
            max-width: 600px;
            margin: 0 auto;
        }

        .departments-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
        }

        .department-card {
            background: #f8f9fa;
            padding: 30px 20px;
            border-radius: 10px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .department-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .department-icon {
            width: 50px;
            height: 50px;
            margin: 0 auto 15px;
        }

        .department-icon svg {
            width: 100%;
            height: 100%;
            fill: #0D6EFD;
        }

        .department-name {
            font-size: 1.1rem;
            color: #333;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .department-positions {
            color: #666;
            font-size: 0.9rem;
        }

        @media (max-width: 1024px) {
            .departments-grid {
                grid-template-columns: repeat(3, 1fr);
            }
        }

        @media (max-width: 768px) {
            .departments-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 480px) {
            .departments-grid {
                grid-template-columns: 1fr;
            }
        }

        /* Thêm style mới cho phần recruitment steps */
        .steps-section {
            padding: 80px 0;
            background-color: #f8f9fa;
        }

        .steps-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .steps-header {
            text-align: center;
            margin-bottom: 60px;
        }

        .steps-title {
            font-size: 2.5rem;
            color: #333;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .steps-grid {
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: relative;
            margin: 0 auto;
            max-width: 900px;
        }

        .step-item {
            flex: 1;
            text-align: center;
            position: relative;
            z-index: 2;
        }

        .step-icon {
            width: 80px;
            height: 80px;
            margin: 0 auto 20px;
            background: #fff;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            position: relative;
            z-index: 2;
        }

        .step-icon img {
            width: 40px;
            height: 40px;
            color: #0D6EFD;
        }

        .step-title {
            font-size: 1.2rem;
            color: #333;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .step-description {
            font-size: 0.9rem;
            color: #666;
            max-width: 200px;
            margin: 0 auto;
        }

        /* Đường nét đứt nối giữa các bước */
        .steps-connection {
            position: absolute;
            top: 40px;
            left: 50%;
            transform: translateX(-50%);
            width: 80%;
            height: 2px;
            z-index: 1;
        }

        .connection-line {
            width: 100%;
            height: 100%;
            background-image: linear-gradient(to right, #0D6EFD 50%, transparent 50%);
            background-size: 20px 2px;
            background-repeat: repeat-x;
            animation: moveLines 20s linear infinite;
        }

        /* Animation cho đường nét đứt */
        @keyframes moveLines {
            from {
                background-position: 0 0;
            }
            to {
                background-position: 100px 0;
            }
        }

        /* Responsive */
        @media (max-width: 768px) {
            .steps-grid {
                flex-direction: column;
                gap: 40px;
            }

            .steps-connection {
                width: 2px;
                height: 80%;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
            }

            .connection-line {
                background-image: linear-gradient(to bottom, #0D6EFD 50%, transparent 50%);
                animation: moveLinesVertical 20s linear infinite;
            }

            @keyframes moveLinesVertical {
                from {
                    background-position: 0 0;
                }
                to {
                    background-position: 0 100px;
                }
            }

            .step-description {
                max-width: 300px;
            }
        }
    </style>
</head>
<body>
    <div class="recruitment-container">
        <div class="content-left">
            <h1 class="title">HANKYO Careers</h1>
            <p class="description">
                Đồng hành cùng HANKYO phát triển nền tảng học và luyện thi thông minh tại Việt Nam.
            </p>
            <a href="#" class="cta-button">Về với HANKYO Ngay !</a>
        </div>

        <div class="content-right">
            <div class="image-grid">
                <div class="image-card">
                    <img src="asset/png/about-us/teacher1.png" alt="Team member 1">
                </div>
                <div class="image-card">
                    <img src="asset/png/about-us/teacher2.png" alt="Team member 2">
                </div>
                <div class="image-card">
                    <img src="asset/png/about-us/teacher3.png" alt="Team member 3">
                </div>
            </div>
            <div class="floating-shape shape-1"></div>
            <div class="floating-shape shape-2"></div>
            <div class="floating-shape shape-3"></div>
        </div>
    </div>

    <!-- Thêm phần statistics mới -->
    <section class="statistics-section">
        <div class="statistics-container">
            <div class="statistic-item">
                <div class="statistic-number">1</div>
                <div class="statistic-label">Nền tảng giáo dục số hàng đầu tại Việt Nam</div>
            </div>

            <div class="statistic-item">
                <div class="statistic-number">100+</div>
                <div class="statistic-label">Nhân sự tài năng</div>
            </div>

            <div class="statistic-item">
                <div class="statistic-number">10,000+</div>
                <div class="statistic-label">Người học trên toàn quốc</div>
            </div>
        </div>
    </section>

    <!-- Thêm section mới sau phần statistics -->
    <section class="special-section">
        <div class="special-container">
            <div class="special-content">
                <h2 class="special-title">Mỗi Cá Thể Đặc Biệt Tạo Nên Sự Khác Biệt Tại HANKYO</h2>
                <p class="special-description">
                    Tại HANKYO, chúng tôi tin rằng dù bạn ở bất cứ nơi đâu cũng đều cần được hưởng chất lượng học tập tốt nhất.
                    Phát triển nền tảng giáo dục số, nền tảng luyện thi online toàn diện để đem "lớp học chất lượng cao" đến mọi miền tổ quốc là sứ mệnh của chúng tôi.
                </p>
                <p class="special-description">
                    Chính các bạn sẽ là những cá thể đặc biệt, tạo nên sự khác biệt tại #HANKYO. Cùng chúng tôi mang lại những điều tuyệt vời và tích cực nhất đến hàng triệu triệu người học trên toàn thế giới, đặc biệt ở Việt Nam.
                </p>
            </div>
            <div class="special-image">
                <img src="asset/png/recruitment/photo_recu1.jpg" alt="Online Learning Illustration">
            </div>
        </div>
    </section>

    <!-- Thêm section jobs sau special-section và trước đóng body -->
    <section class="jobs-section">
        <div class="jobs-container">
            <div class="jobs-header">
                <h2 class="jobs-title">Công Việc Nổi Bật</h2>
                <p class="jobs-subtitle">Biết giá trị của bản thân và tìm được công việc phù hợp với năng lực của bạn</p>
            </div>

            <div class="jobs-grid">
                <!-- Academic -->
                <div class="job-card">

                    <h3 class="job-title">Academic</h3>
                    <p class="job-positions">0 open positions</p>
                </div>

                <!-- Admission -->
                <div class="job-card">

                    <h3 class="job-title">Admission</h3>
                    <p class="job-positions">0 open positions</p>
                </div>

                <!-- Branding -->
                <div class="job-card">

                    <h3 class="job-title">Branding</h3>
                    <p class="job-positions">0 open positions</p>
                </div>

                <!-- Human Resources -->
                <div class="job-card">

                    <h3 class="job-title">Human Resources</h3>
                    <p class="job-positions">0 open position</p>
                </div>



                <!-- Marketing -->
                <div class="job-card">

                    <h3 class="job-title">Marketing</h3>
                    <p class="job-positions">0 open positions</p>
                </div>

                <!-- Product -->
                <div class="job-card">

                    <h3 class="job-title">Product</h3>
                    <p class="job-positions">0 open position</p>
                </div>

                <!-- Student Success -->
                <div class="job-card">

                    <h3 class="job-title">Student Success</h3>
                    <p class="job-positions">0 open positions</p>
                </div>

                <!-- Technology -->
                <div class="job-card">

                    <h3 class="job-title">Technology</h3>
                    <p class="job-positions">0 open positions</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Thêm section mới sau jobs-section và trước đóng body -->
    <section class="love-prep-section">
        <div class="love-prep-container">
            <div class="love-prep-header">
                <h2 class="love-prep-title">Bạn Sẽ Yêu HANKYO Vì</h2>
            </div>

            <div class="swiper">
                <div class="swiper-wrapper">
                    <!-- Slide 1 -->
                    <div class="swiper-slide">
                        <div class="slide-image">
                            <img src="asset/png/recruitment/re02.png" alt="Learning Environment">
                        </div>
                        <div class="slide-content">
                            <h3 class="slide-title">Cơ Hội Học Hỏi Phát Triển</h3>
                            <ul class="benefit-list">
                                <li class="benefit-item">Đồng nghiệp tài năng, giàu kinh nghiệm, chia sẻ tận tình</li>
                                <li class="benefit-item">Sẵn sàng hỗ trợ kinh phí học nâng cao nghiệp vụ</li>
                                <li class="benefit-item">Được học Tiếng Hàn - TOPIK miễn phí</li>
                                <li class="benefit-item">Lộ trình thăng tiến rõ ràng với tất cả vị trí</li>
                            </ul>
                        </div>
                    </div>

                    <!-- Slide 2 -->
                    <div class="swiper-slide">
                        <div class="slide-image">
                            <img src="asset/png/recruitment/re03.png" alt="Benefits">
                        </div>
                        <div class="slide-content">
                            <h3 class="slide-title">Đãi Ngộ Xứng Tầm</h3>
                            <ul class="benefit-list">
                                <li class="benefit-item">Mức lương cạnh tranh</li>
                                <li class="benefit-item">Thưởng KPI tương xứng kết quả làm việc</li>
                                <li class="benefit-item">Thưởng theo Doanh Thu hấp dẫn ở nhiều vị trí</li>
                                <li class="benefit-item">Review lương 2 lần/năm</li>
                            </ul>
                        </div>
                    </div>

                    <!-- Slide 3 -->
                    <div class="swiper-slide">
                        <div class="slide-image">
                            <img src="asset/png/recruitment/re04.png" alt="Work Environment">
                        </div>
                        <div class="slide-content">
                            <h3 class="slide-title">Môi Trường Trẻ Trung, Nhiệt Huyết, Thân Thiện</h3>
                            <ul class="benefit-list">
                                <li class="benefit-item">Cháy hết mình với Team-building, Party...</li>
                                <li class="benefit-item">Cấp trên thân thiện, đồng nghiệp vui nhộn</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Add Navigation -->
                <div class="swiper-button-next"></div>
                <div class="swiper-button-prev"></div>

                <!-- Add Pagination -->
                <div class="swiper-pagination"></div>
            </div>
        </div>
    </section>

    <!-- Thêm section mới sau love-prep-section và trước đóng body -->
    <section class="departments-section">
        <div class="departments-container">
            <div class="departments-header">
                <h2 class="departments-title">Các Bộ Phận Tại HANKYO</h2>
                <p class="departments-subtitle">Chọn đồng nghiệp tương lai và team đồng hành cùng bạn mỗi ngày.</p>
            </div>

            <div class="departments-grid">
                <!-- Academic -->
                <div class="department-card">

                    <h3 class="department-name">Academic</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Accounting -->
                <div class="department-card">

                    <h3 class="department-name">Accounting</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Admission -->
                <div class="department-card">

                    <h3 class="department-name">Admission</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Branding -->
                <div class="department-card">

                    <h3 class="department-name">Branding</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Human Resources -->
                <div class="department-card">

                    <h3 class="department-name">Human Resources</h3>
                    <p class="department-positions">0 open position</p>
                </div>



                <!-- Marketing -->
                <div class="department-card">

                    <h3 class="department-name">Marketing</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Media Designer -->
                <div class="department-card">

                    <h3 class="department-name">Media Designer</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Product -->
                <div class="department-card">

                    <h3 class="department-name">Product</h3>
                    <p class="department-positions">0 open position</p>
                </div>

                <!-- Product Content -->
                <div class="department-card">

                    <h3 class="department-name">Product Content</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Student Success -->
                <div class="department-card">

                    <h3 class="department-name">Student Success</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Technology -->
                <div class="department-card">

                    <h3 class="department-name">Technology</h3>
                    <p class="department-positions">0 open positions</p>
                </div>

                <!-- Video Production -->
                <div class="department-card">

                    <h3 class="department-name">Video Production</h3>
                    <p class="department-positions">0 open positions</p>
                </div>
            </div>
        </div>
    </section>

    <!-- HTML -->
    <section class="steps-section">
        <div class="steps-container">
            <div class="steps-header">
                <h2 class="steps-title">Tuyển dụng chỉ trong 3 bước</h2>
            </div>

            <div class="steps-grid">
                <!-- Đường nét đứt nối -->

                <!-- Step 1 -->
                <div class="step-item">

                    <h3 class="step-title">Lựa chọn công việc</h3>
                    <p class="step-description">
                        Lựa chọn công việc và gửi CV + bằng cấp chứng chỉ (nếu có) đến <strong>tuyendung@hankyo.edu.vn</strong>
                    </p>
                </div>

                <!-- Step 2 -->
                <div class="step-item">

                    <h3 class="step-title">Phỏng vấn</h3>
                    <p class="step-description">
                        HANKYO hẹn phỏng vấn online hoặc offline và thông báo kết quả trong vòng 1 tuần
                    </p>
                </div>

                <!-- Step 3 -->
                <div class="step-item">

                    <h3 class="step-title">HANKYO chào đón bạn!</h3>
                    <p class="step-description">
                        HANKYO sẽ gửi offer và các giấy tờ nhân sự cần chuẩn bị
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- Thêm Swiper JS -->
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>

    <!-- Initialize Swiper -->
    <script>
        const swiper = new Swiper('.swiper', {
            // Optional parameters
            loop: true,
            spaceBetween: 30,

            // Navigation arrows
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },

            // Pagination
            pagination: {
                el: '.swiper-pagination',
                clickable: true,
            },

            // Auto play
            autoplay: {
                delay: 5000,
                disableOnInteraction: false,
            },
        });
    </script>
</body>
</html>
