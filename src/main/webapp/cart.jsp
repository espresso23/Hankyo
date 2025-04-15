<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giỏ Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="asset/css/cart.css">
</head>
<body>
<c:import url="header.jsp"/>
<div class="container mt-4">
    <h2>Giỏ Hàng Của Bạn</h2>
    <div id="message-container" style="position: fixed; top: 20px; right: 20px; z-index: 1100;"></div>

    <div id="cart-content" style="display: none;">
        <div class="row mt-4">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-body">
                        <c:forEach items="${cartItems}" var="item">
                            <div class="row mb-3 border-bottom pb-3 cart-item" id="cart-item-${item.cartID}">
                                <div class="col-md-3">
                                    <div class="course-image-container">
                                        <img src="${item.course.courseImg}" class="course-image" alt="${item.course.courseTitle}">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <h5>${item.course.courseTitle}</h5>
                                    <p class="text-muted">${item.course.courseDescription}</p>
                                </div>
                                <div class="col-md-3 text-end">
                                    <h5><fmt:formatNumber value="${item.course.price}" type="number"/> VNĐ</h5>
                                    <button type="button" class="btn btn-danger btn-sm remove-cart-item"
                                            data-cart-id="${item.cartID}" data-price="${item.course.price}">Xóa
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Tổng Cộng</h5>
                        <c:set var="total" value="0"/>
                        <c:forEach items="${cartItems}" var="item">
                            <c:set var="total" value="${total + item.course.price}"/>
                        </c:forEach>
                        <h4 class="text-primary" id="cart-total"><fmt:formatNumber value="${total}" type="number"/> VNĐ</h4>
                        <form action="create-payment-link" method="post" id="purchase-form">
                            <c:forEach items="${cartItems}" var="item">
                                <input type="hidden" name="courseIDs" value="${item.course.courseID}">
                                <input type="hidden" name="courseNames" value="${item.course.courseTitle}">
                                <input type="hidden" name="coursePrices" value="${item.course.price}">
                            </c:forEach>
                            <input type="hidden" name="learnerID" value="${sessionScope.learner.learnerID}">
                            <div id="button-container">
                                <button id="create-payment-link-btn" class="btn btn-success">
                                    <i class="fas fa-qrcode me-2"></i>Thanh toán bằng VietQR
                                </button>
                            </div>

                        </form>
                    </div>
                </div>
                <div id="embeded-payment-container" style="height: 350px"></div>
            </div>
        </div>
    </div>

    <div id="empty-cart-message" class="alert alert-info mt-4" style="display: none;">
        Giỏ hàng của bạn đang trống. <a href="courses">Tiếp tục mua sắm</a>
    </div>
</div>
<c:import url="footer.jsp"/>
</body>
<script src="https://cdn.payos.vn/payos-checkout/v1/stable/payos-initialize.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        // Kiểm tra và hiển thị nội dung phù hợp
        if ($('.cart-item').length === 0) {
            $('#empty-cart-message').show();
            $('#cart-content').hide();
        } else {
            $('#empty-cart-message').hide();
            $('#cart-content').show();
        }

        $('.remove-cart-item').click(function () {
            const cartID = $(this).data('cart-id');
            const price = $(this).data('price');
            const itemElement = $('#cart-item-' + cartID);

            $.ajax({
                url: 'cart/remove',
                type: 'POST',
                data: {cartID: cartID},
                success: function (response) {
                    if (response.success) {
                        // Hiệu ứng mờ và xóa item
                        itemElement.fadeOut(300, function () {
                            $(this).remove();
                            updateCartTotal(price);
                            updateCartCount(-1);
                            
                            // Kiểm tra nếu giỏ hàng trống
                            if ($('.cart-item').length === 0) {
                                $('#cart-content').fadeOut(300, function() {
                                    $('#empty-cart-message').fadeIn(300);
                                });
                            }
                        });

                        // Hiển thị thông báo
                        showMessage('Đã xóa khỏi giỏ hàng', 'success');
                    }
                },
                error: function () {
                    showMessage('Lỗi khi xóa sản phẩm', 'error');
                }
            });
        });

        function updateCartTotal(price) {
            const $totalElement = $('#cart-total');
            let currentTotal = parseFloat($totalElement.text().replace(/[^0-9.-]+/g, ''));
            let newTotal = currentTotal - price;
            $totalElement.text(newTotal.toLocaleString() + ' VNĐ');
        }

        function updateCartCount(change) {
            const $cartCount = $('.cart-badge');
            let count = parseInt($cartCount.text()) || 0;
            count += change;
            $cartCount.text(count > 0 ? count : '').toggle(count > 0);
        }

        function showMessage(message, type) {
            const messageDiv = $('<div>', {
                class: 'message ' + type,
                text: message
            }).css({
                padding: '10px 20px',
                margin: '5px 0',
                borderRadius: '4px',
                color: 'white',
                backgroundColor: type === 'success' ? '#28a745' : '#dc3545',
                boxShadow: '0 2px 5px rgba(0,0,0,0.2)'
            });

            $('#message-container').append(messageDiv);

            // Tự động xóa message sau 3 giây
            setTimeout(function() {
                messageDiv.fadeOut(300, function() {
                    $(this).remove();
                });
            }, 3000);
        }


    });
</script>
<%--<script src="js/processPayment.js"></script>--%>


</html>