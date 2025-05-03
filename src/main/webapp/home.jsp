<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Khóa Học Tiếng Hàn - HANKYO</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        /* Reset & Global Styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        :root {
            --pink: #FFD1DC;
            --light-pink: #FF9AA2;
            --blue: #B5EAD7;
            --light-blue: #C7CEEA;
            --white: #FFF;
            --text: #333;
            --accent: #4e6cef;
            --shadow: rgba(0, 0, 0, 0.1);
        }

        body {
            font-family: 'Segoe UI', 'Arial', sans-serif;
            background: var(--white);
            color: var(--text);
            line-height: 1.6;
        }

        /* Typography */
        h1, h2, h3 {
            color: var(--text);
            font-weight: 700;
        }

        h1 { font-size: 2.5rem; }
        h2 { font-size: 2rem; }
        h3 { font-size: 1.3rem; margin-bottom: 5px; }

        /* General Button Style */
        .btn {
            display: inline-block;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .btn-pink {
            background: linear-gradient(135deg, var(--light-pink), #FFB7B2);
            color: var(--white);
        }

        .btn-pink:hover {
            background: linear-gradient(135deg, #FF7B8B, var(--light-pink));
            box-shadow: 0 5px 15px rgba(255, 154, 162, 0.4);
        }

        /* Hero Section */
        .hero {
            background: linear-gradient(135deg, var(--pink), var(--blue));
            padding: 60px 20px;
            text-align: center;
            border-radius: 20px;
            margin: 40px auto;
            max-width: 1200px;
        }

        .hero h1 {
            margin-bottom: 20px;
            color: var(--text);
        }

        .hero p {
            font-size: 1.2rem;
            max-width: 700px;
            margin: 0 auto 30px;
            color: var(--text);
        }

        /* Original Carousel Styles */
        #homeCarousel {
            margin: 30px auto;
            max-width: 1200px;
        }

        .carousel-inner {
            border-radius: 20px;
            overflow: hidden;
        }

        .carousel-item img {
            object-fit: contain;
            width: 100%;
            height: auto;
            max-height: 500px;
            border-radius: 0;
            background: #fff;
        }

        .carousel-control-prev-icon,
        .carousel-control-next-icon {
            display: none;
        }

        .carousel-control-prev,
        .carousel-control-next {
            display: none !important;
        }

        .carousel-indicators [data-bs-target] {
            background-color: #6c7fd1;
            opacity: 0.5;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            margin: 0 6px;
            transition: background 0.2s, opacity 0.2s;
            border: none;
        }

        .carousel-indicators .active {
            background-color: #6c7fd1;
            opacity: 1;
            width: 32px;
            border-radius: 8px;
        }

        /* Pathway Section */
        .pathway-section {
            padding: 60px 20px;
            text-align: center;
            background: var(--white);
        }

        .pathway-title {
            color: var(--light-pink);
            margin-bottom: 40px;
            position: relative;
        }

        .pathway-title::after {
            content: '';
            display: block;
            width: 80px;
            height: 4px;
            background: var(--blue);
            margin: 15px auto;
            border-radius: 2px;
        }

        .pathway-container {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 25px;
            max-width: 1200px;
            margin: 0 auto;
        }

        .pathway-card {
            background: var(--white);
            border-radius: 20px;
            width: 280px;
            padding: 30px;
            box-shadow: 0 8px 20px var(--shadow);
            border: 2px solid var(--pink);
            transition: all 0.3s ease;
        }

        .pathway-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 12px 25px rgba(255, 154, 162, 0.2);
        }

        .pathway-level {
            font-size: 1.8rem;
            color: var(--text);
            margin-bottom: 20px;
        }

        .pathway-level::after {
            content: '';
            display: block;
            width: 50px;
            height: 3px;
            background: var(--light-blue);
            margin: 15px auto;
            border-radius: 3px;
        }

        /* Dual Column Section (Updated for "Tại Sao Chọn HANKYO?") */
        .dual-column-container {
            display: flex;
            max-width: 1200px;
            margin: 60px auto;
            padding: 0 20px;
            gap: 40px;
            align-items: center;
        }

        .image-column, .content-column {
            flex: 1;
        }

        .image-column img {
            width: 100%;
            border-radius: 12px;
            box-shadow: 0 4px 12px var(--shadow);
        }

        .content-header h1 {
            color: var(--accent);
            margin-bottom: 30px;
            position: relative;
            font-size: 2.5rem;
            font-weight: 800;
            text-transform: uppercase;
        }

        .content-header h1::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 0;
            width: 80px;
            height: 4px;
            background: #FFC107; /* Yellow underline as in the image */
            border-radius: 2px;
        }

        .reasons-list {
            display: flex;
            flex-direction: column;
            gap: 10px; /* Reduced gap for closer spacing */
        }

        .reason-item {
            display: flex;
            align-items: flex-start;
            background: #E6F0FA; /* Light blue background as in the image */
            border-radius: 10px;
            padding: 15px;
            font-size: 1rem;
            color: var(--text);
            line-height: 1.4;
        }

        .reason-item::before {
            content: '';
            display: inline-block;
            width: 20px;
            height: 20px;
            background: #4e6cef; /* Blue circle bullet */
            border-radius: 50%;
            margin-right: 15px;
            margin-top: 5px;
            flex-shrink: 0;
        }

        .reason-content {
            flex: 1;
        }

        .reason-content p {
            margin-bottom: 5px;
            font-size: 1rem;
            color: #555;
        }

        /* Support Section */
        .support-section {
            max-width: 1200px;
            margin: 60px auto;
            padding: 0 20px;
            display: flex;
            gap: 40px;
            align-items: center;
        }

        .support-img img {
            width: 100%;
            border-radius: 12px;
            box-shadow: 0 4px 12px var(--shadow);
        }

        .support-form {
            flex: 1;
            background: var(--white);
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px var(--shadow);
        }

        .support-form h2 {
            color: var(--light-pink);
            margin-bottom: 20px;
        }

        .support-form p {
            color: #555;
            margin-bottom: 30px;
        }

        .form-control, .form-select {
            border-radius: 8px;
            border: 1px solid var(--pink);
            padding: 10px;
        }

        .btn-submit {
            background: var(--light-pink);
            color: var(--white);
            padding: 12px 30px;
            border: none;
            border-radius: 25px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-submit:hover {
            background: #FF7B8B;
            box-shadow: 0 5px 15px rgba(255, 154, 162, 0.4);
        }

        /* Responsive Design */
        @media (max-width: 992px) {
            .dual-column-container, .support-section {
                flex-direction: column;
                gap: 20px;
            }

            .image-column, .content-column, .support-img, .support-form {
                flex: none;
                width: 100%;
            }
        }

        @media (max-width: 768px) {
            h1 { font-size: 2rem; }
            h2 { font-size: 1.8rem; }
            .hero { padding: 40px 20px; }
            .carousel-item img { max-height: 300px; }
            .pathway-card { width: 100%; max-width: 350px; }
            .reason-item { flex-direction: row; align-items: flex-start; }
        }

        @media (max-width: 576px) {
            .btn { padding: 10px 20px; font-size: 0.9rem; }
            .hero h1 { font-size: 1.8rem; }
            .hero p { font-size: 1rem; }
            .content-header h1 { font-size: 1.8rem; }
            .reason-item { font-size: 0.9rem; padding: 10px; }
            .reason-item::before { width: 15px; height: 15px; margin-right: 10px; }
            .reason-content h3 { font-size: 1.1rem; }
            .reason-content p { font-size: 0.9rem; }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<!-- Original Carousel Section -->
<div id="homeCarousel" class="carousel slide" data-bs-ride="carousel" style="margin: 30px auto; max-width: 1200px;">
    <div class="carousel-indicators">
        <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="0" class="active"></button>
        <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="1"></button>
        <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="2"></button>
    </div>
    <div class="carousel-inner" style="border-radius: 20px; overflow: hidden;">
        <div class="carousel-item active">
            <img src="asset/png/contentHomePage/slide1.png" class="d-block w-100" alt="Slide 1">
        </div>
        <div class="carousel-item">
            <img src="asset/png/contentHomePage/slide2.png" class="d-block w-100" alt="Slide 2">
        </div>
        <div class="carousel-item">
            <img src="asset/png/contentHomePage/slide3.png" class="d-block w-100" alt="Slide 3">
        </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#homeCarousel" data-bs-slide="prev">
        <span class="carousel-control-prev-icon"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#homeCarousel" data-bs-slide="next">
        <span class="carousel-control-next-icon"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>

<!-- Hero Section -->
<section class="hero">
    <h1>Khóa Học Tiếng Hàn Chất Lượng</h1>
    <p>Phương pháp học hiện đại, giáo viên bản ngữ, cam kết đầu ra TOPIK sau 3 tháng</p>
    <a href="register.jsp" class="btn btn-pink">Đăng Ký Ngay</a>
</section>

<!-- Pathway Section -->
<section class="pathway-section">
    <h2 class="pathway-title">Khám phá lộ trình học tiếng Hàn</h2>
    <div class="pathway-container">
        <div class="pathway-card">
            <div class="pathway-level">Sơ cấp</div>
            <a href="courses?categoryID=1" class="btn btn-pink">Xem khóa học</a>
        </div>
        <div class="pathway-card">
            <div class="pathway-level">Trung cấp</div>
            <a href="courses?categoryID=2" class="btn btn-pink">Xem khóa học</a>
        </div>
        <div class="pathway-card">
            <div class="pathway-level">TOPIK</div>
            <a href="courses?categoryID=3" class="btn btn-pink">Xem khóa học</a>
        </div>
    </div>
</section>

<!-- Why Choose HANKYO Section (Restored Original Content with Updated Styling) -->
<section class="dual-column-container">
    <div class="image-column">
        <img src="asset/png/contentHomePage/lydo.png" alt="Học viên HANKYO">
    </div>
    <div class="content-column">
        <div class="content-header">
            <h1>Tại Sao Chọn HANKYO?</h1>
        </div>
        <div class="reasons-list">
            <div class="reason-item">
                <div class="reason-content">
                    <h3>Kho Video Bài Giảng Chất Lượng</h3>
                    <p>Học mọi lúc với video 4K giải thích chi tiết ngữ pháp</p>
                    <p>Xem lại không giới hạn, tua/chỉnh tốc độ dễ dàng</p>
                </div>
            </div>
            <div class="reason-item">
                <div class="reason-content">
                    <h3>Học Từ Vựng Qua Flashcard Game</h3>
                    <p>Trò chơi tương tác phản xạ nhanh, ôn tập từ vựng bằng hình ảnh</p>
                    <p>Hệ thống nhắc lại spaced repetition giúp ghi nhớ lâu</p>
                </div>
            </div>
            <div class="reason-item">
                <div class="reason-content">
                    <h3>Diễn Đàn Hỏi Đáp 24/7</h3>
                    <p>Giải đáp thắc mắc trong 30 phút bởi GV & học viên khác</p>
                    <p>Chia sẻ tài liệu, kinh nghiệm thi cử</p>
                </div>
            </div>
            <div class="reason-item">
                <div class="reason-content">
                    <h3>Kho Đề Thi TOPIK "Sống"</h3>
                    <p>Cập nhật đề thi thật kèm đáp án chi tiết</p>
                    <p>Chấm điểm tự động & phân tích lỗi sai</p>
                </div>
            </div>
            <div class="reason-item">
                <div class="reason-content">
                    <h3>Học Là Vui</h3>
                    <p>Tích điểm đổi quà (stickers, voucher) khi hoàn thành bài học</p>
                    <p>Thi đua xếp hạng tuần với phần thưởng</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Support Section -->
<section class="support-section">
    <div class="support-img">
        <img src="asset/png/contentHomePage/tuvan.png" alt="Tư vấn hỗ trợ">
    </div>
    <form class="support-form" method="POST" action="https://formsubmit.co/luuhuyenvt2004@gmail.com">
        <input type="hidden" name="_subject" value="Yêu cầu tư vấn mới từ HANKYO">
        <input type="hidden" name="_template" value="table">
        <input type="hidden" name="_next" value="http://localhost:8080/thankyou.jsp">
        <h2>Bạn cần hỗ trợ tư vấn?</h2>
        <p>Để lại thông tin, chúng tôi sẽ liên hệ bạn trong thời gian sớm nhất!</p>
        <div class="row">
            <div class="col-md-6">
                <div class="mb-3">
                    <label for="name" class="form-label">Họ và Tên (*)</label>
                    <input type="text" class="form-control" id="name" name="Họ và Tên" required>
                </div>
            </div>
            <div class="col-md-6">
                <div class="mb-3">
                    <label for="phone" class="form-label">Số điện thoại (*)</label>
                    <input type="tel" class="form-control" id="phone" name="Số điện thoại" required>
                </div>
            </div>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email (*)</label>
            <input type="email" class="form-control" id="email" name="Email" required>
        </div>
        <div class="mb-3">
            <label for="course" class="form-label">Khóa học quan tâm</label>
            <select class="form-select" id="course" name="Khóa học quan tâm">
                <option value="">Chọn khóa học</option>
                <option value="Sơ cấp">Sơ cấp</option>
                <option value="Trung cấp">Trung cấp</option>
                <option value="TOPIK">Luyện thi TOPIK</option>
            </select>
        </div>
        <button type="submit" class="btn btn-submit">Đăng ký tư vấn</button>
    </form>
</section>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>