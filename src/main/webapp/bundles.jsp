<%--
  Created by IntelliJ IDEA.
  User: Le Phuong Uyen
  Date: 4/9/2025
  Time: 8:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Hankyo</title>
    <link rel="stylesheet" href="asset/css/bundle.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/fontawesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.4.1/css/bootstrap.css">
    <link href="https://fonts.googleapis.com/css?family=Roboto|Ubuntu:500,700&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-image: url('asset/png/background/background-2.png');
            background-size: cover;
            background-position: left;
        }
    </style>
</head>

<body>
<c:import url="header.jsp"/>

<section class="section-pricing">
    <div class="container">
        <div class="row justify-content-center text-center">
            <div class="col-md-10 col-lg-8">
                <div class="header-section">
                    <h2 class="title">Chọn kế hoạch học tiếng Hàn phù hợp</h2>
                    <p class="description">Học tiếng Hàn hiệu quả với các gói dịch vụ linh hoạt từ chúng tôi. Các
                        khóa học phù hợp với mọi trình độ và mục tiêu học của bạn.</p>
                </div>
            </div>
        </div>
        <div class="row justify-content-center">
            <div class="plans">
                <span class="plan monthly active">Hàng tháng</span>
                <span class="plan yearly">Hàng năm</span>
                <span class="bg-active"></span>
            </div>
        </div>
        <div class="row justify-content-center">
            <!-- Start Single Pricing -->
            <!-- <div class="col-sm-6 col-md-4 col-lg-3"> -->
            <div class="single-pricing free">
                <div class="header text-center">
                    <h3 class="title">Dùng thử miễn phí</h3>
                    <p>Học thử miễn phí với các bài học cơ bản</p>
                </div>
                <div class="content">
                    <span class="monthly-price price">Miễn phí</span>
                    <span class="yearly-price price">Miễn phí</span>
                    <ul class="list-inline">
                        <li class="yes"><i class="far fa-check-circle"></i>Truy cập các bài học cơ bản</li>
                        <li class="yes"><i class="far fa-check-circle"></i>Thử nghiệm với các bài tập đơn giản
                        </li>
                        <li class="no"><i class="far fa-times-circle"></i>Không có bài học nâng cao</li>
                    </ul>
                </div>
                <div class="try-button">
                    <a href="#">Thử ngay</a>
                </div>
            </div>
            <!-- </div> -->
            <!-- / End Single Pricing -->
            <!-- Start Single Pricing -->
            <!-- <div class="col-sm-6 col-md-4 col-lg-3"> -->
            <div class="single-pricing popular">
                <div class="header text-center">
                    <h3 class="title">Phổ biến</h3>
                    <p>Khóa học tiếng Hàn cơ bản và nâng cao</p>
                </div>
                <div class="content">
                    <span class="monthly-price price">70.000 VNĐ</span>
                    <span class="yearly-price price">300.000 VNĐ</span>
                    <ul class="list-inline">
                        <li class="yes"><i class="far fa-check-circle"></i>Truy cập đầy đủ bài học</li>
                        <li class="yes"><i class="far fa-check-circle"></i>Được hỗ trợ giải đáp thắc mắc qua
                            chat</li>
                        <li class="yes"><i class="far fa-check-circle"></i>Các bài học nâng cao về ngữ pháp và
                            từ vựng</li>
                        <li class="no"><i class="far fa-times-circle"></i>Không có tài liệu học mở rộng</li>
                    </ul>
                </div>
                <div class="try-button">
                    <a href="#">Bắt đầu học</a>
                </div>
            </div>
            <!-- </div> -->
            <!-- / End Single Pricing -->
            <!-- Start Single Pricing -->
            <!-- <div class="col-sm-6 col-md-4 col-lg-3"> -->
            <div class="single-pricing premium">
                <div class="header text-center">
                    <h3 class="title">Premium</h3>
                    <p>Khóa học tiếng Hàn toàn diện cho mọi trình độ</p>
                </div>
                <div class="content">
                    <span class="monthly-price price">100.000 VNĐ</span>
                    <span class="yearly-price price">450.000 VNĐ</span>
                    <ul class="list-inline">
                        <li class="yes"><i class="far fa-check-circle"></i>Truy cập tất cả bài học và bài tập
                        </li>
                        <li class="yes"><i class="far fa-check-circle"></i>Hỗ trợ học viên trực tuyến 24/7</li>
                        <li class="yes"><i class="far fa-check-circle"></i>Giải đáp thắc mắc qua chat và email
                        </li>
                        <li class="yes"><i class="far fa-check-circle"></i>Tài liệu học mở rộng và đề thi mẫu
                        </li>
                        <li class="yes"><i class="far fa-check-circle"></i>Chứng chỉ hoàn thành khóa học</li>
                    </ul>
                </div>
                <div class="try-button">
                    <a href="#">Đăng ký ngay</a>
                </div>
            </div>
            <!-- </div> -->
            <!-- / End Single Pricing -->
        </div>
    </div>
</section>
<c:import url="footer.jsp"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
    $(function () {
        "use strict";

        $(".section-pricing .plans .plan").on("click", function () {
            $(this).siblings(".bg-active").css("transform", "translateX(" + $(this).position().left + "px)");
            $(".section-pricing .plans .plan").css("color", "#61648f");
            $(this).css("color", "#fff");
            if ($(this).hasClass("yearly")) {
                $(".section-pricing .single-pricing .content .price").css("opacity", 0);
                $(".section-pricing .single-pricing .content .yearly-price").css("opacity", 1);
            }
            else {
                $(".section-pricing .single-pricing .content .price").css("opacity", 0);
                $(".section-pricing .single-pricing .content .monthly-price").css("opacity", 1);
            }
        });

    });
</script>
</body>

</html>
