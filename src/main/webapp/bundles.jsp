<%--
  Created by IntelliJ IDEA.
  User: Le Phuong Uyen
  Date: 4/9/2025
  Time: 8:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
            <c:forEach items="${vips}" var="vip">
                <div class="single-pricing ${vip.vipType.toLowerCase()}">
                    <div class="header text-center">
                        <h3 class="title">${vip.vipName}</h3>
                        <p>${vip.description}</p>
                    </div>
                    <div class="content">
                        <span class="monthly-price price">
                            <c:choose>
                                <c:when test="${vip.price == 0}">Miễn phí</c:when>
                                <c:otherwise><fmt:formatNumber value="${vip.price}" type="number"/> VNĐ</c:otherwise>
                            </c:choose>
                        </span>
                        <span class="yearly-price price">
                            <c:choose>
                                <c:when test="${vip.yearlyPrice == 0}">Miễn phí</c:when>
                                <c:otherwise><fmt:formatNumber value="${vip.yearlyPrice}" type="number"/> VNĐ</c:otherwise>
                            </c:choose>
                        </span>
                        <ul class="list-inline">
                            <c:forEach var="feature" items="${fn:split(vip.features, ',')}">
                                <c:set var="step1" value="${fn:replace(feature, '[', '')}" />
                                <c:set var="step2" value="${fn:replace(step1, ']', '')}" />
                                <c:set var="cleanFeature" value="${fn:trim(step2)}" />
                                <c:choose>
                                    <c:when test="${fn:startsWith(cleanFeature, 'Không')}">
                                        <li class="no text-muted" style="opacity:0.6"><i class="far fa-times-circle"></i> ${cleanFeature}</li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="yes"><i class="far fa-check-circle"></i> ${cleanFeature}</li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="try-button">
                        <c:choose>
                            <c:when test="${hasActiveVip}">
                                <button class="register-vip btn btn-secondary" disabled>Đã là VIP</button>
                                <c:if test="${not empty activeVip}">
                                    <div style="color:#2ecc71; font-size:14px; margin-top:8px;">Bạn đang sử dụng gói: <b>${activeVip.vipName}</b></div>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <a href="#" class="register-vip" data-vip-id="${vip.vipID}">
                                    <c:choose>
                                        <c:when test="${vip.vipType == 'FREE'}">Thử ngay</c:when>
                                        <c:when test="${vip.vipType == 'POPULAR'}">Bắt đầu học</c:when>
                                        <c:otherwise>Đăng ký ngay</c:otherwise>
                                    </c:choose>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Form ẩn để submit thanh toán VIP -->
<form id="vip-payment-form" method="post" action="create-vip-payment-link" style="display:none;">
    <input type="hidden" name="vipID" id="vipID-hidden" />
    <input type="hidden" name="learnerID" id="learnerID-hidden" value="${learner.learnerID}" />
</form>

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

        // Xử lý đăng ký gói VIP
        $('.register-vip').click(function(e) {
            e.preventDefault();
            const vipId = $(this).data('vip-id');
            $('#vipID-hidden').val(vipId);
            // Nếu đã có learnerID trong session thì giữ nguyên, nếu chưa thì cần xử lý đăng nhập
            $('#vip-payment-form').submit();
        });
    });
</script>
</body>

</html>
