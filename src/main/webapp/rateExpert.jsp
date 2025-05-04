<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Đánh giá giáo viên</title>
    <style>
        .star-rating { direction: rtl; display: inline-flex; }
        .star-rating input { display: none; }
        .star-rating label {
            font-size: 2em;
            color: #ccc;
            cursor: pointer;
        }
        .star-rating input:checked ~ label,
        .star-rating label:hover,
        .star-rating label:hover ~ label {
            color: #f5b301;
        }
    </style>
</head>
<body>
    <h2>Đánh giá giáo viên</h2>
    <form method="post" action="rateExpert">
        <input type="hidden" name="expertID" value="1" /> <!-- Thay bằng ID thực tế -->
        <div>
            <label>Chất lượng giảng dạy:</label>
            <span class="star-rating">
                <input type="radio" name="teachingQuality" value="5" id="teach5"><label for="teach5">&#9733;</label>
                <input type="radio" name="teachingQuality" value="4" id="teach4"><label for="teach4">&#9733;</label>
                <input type="radio" name="teachingQuality" value="3" id="teach3"><label for="teach3">&#9733;</label>
                <input type="radio" name="teachingQuality" value="2" id="teach2"><label for="teach2">&#9733;</label>
                <input type="radio" name="teachingQuality" value="1" id="teach1"><label for="teach1">&#9733;</label>
            </span>
        </div>
        <div>
            <label>Chất lượng phản hồi:</label>
            <span class="star-rating">
                <input type="radio" name="replyQuality" value="5" id="reply5"><label for="reply5">&#9733;</label>
                <input type="radio" name="replyQuality" value="4" id="reply4"><label for="reply4">&#9733;</label>
                <input type="radio" name="replyQuality" value="3" id="reply3"><label for="reply3">&#9733;</label>
                <input type="radio" name="replyQuality" value="2" id="reply2"><label for="reply2">&#9733;</label>
                <input type="radio" name="replyQuality" value="1" id="reply1"><label for="reply1">&#9733;</label>
            </span>
        </div>
        <div>
            <label>Chất lượng khoá học:</label>
            <span class="star-rating">
                <input type="radio" name="courseQuality" value="5" id="course5"><label for="course5">&#9733;</label>
                <input type="radio" name="courseQuality" value="4" id="course4"><label for="course4">&#9733;</label>
                <input type="radio" name="courseQuality" value="3" id="course3"><label for="course3">&#9733;</label>
                <input type="radio" name="courseQuality" value="2" id="course2"><label for="course2">&#9733;</label>
                <input type="radio" name="courseQuality" value="1" id="course1"><label for="course1">&#9733;</label>
            </span>
        </div>
        <div>
            <label>Thân thiện:</label>
            <span class="star-rating">
                <input type="radio" name="friendlyQuality" value="5" id="friendly5"><label for="friendly5">&#9733;</label>
                <input type="radio" name="friendlyQuality" value="4" id="friendly4"><label for="friendly4">&#9733;</label>
                <input type="radio" name="friendlyQuality" value="3" id="friendly3"><label for="friendly3">&#9733;</label>
                <input type="radio" name="friendlyQuality" value="2" id="friendly2"><label for="friendly2">&#9733;</label>
                <input type="radio" name="friendlyQuality" value="1" id="friendly1"><label for="friendly1">&#9733;</label>
            </span>
        </div>
        <button type="submit">Gửi đánh giá</button>
    </form>
    <c:if test="${not empty success}">
        <p style="color:green;">Đánh giá thành công!</p>
    </c:if>
</body>
</html> 