<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <title>Từ điển</title>
    <link href="asset/css/StyleHomePage.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            min-height: 100vh;
        }
        .mainContainer {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
            font-weight: 600;
        }
        table {
            width: 100%;
            margin: 0 auto 30px;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }
        th {
            background-color: #007bff;
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 14px;
        }
        td {
            color: #555;
            font-size: 14px;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
            transition: background-color 0.3s ease;
        }
        .pagination {
            text-align: center;
            margin: 30px 0;
        }
        .pagination a {
            display: inline-block;
            margin: 0 5px;
            padding: 8px 14px;
            text-decoration: none;
            color: #007bff;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        .pagination a:hover {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .pagination a.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
            cursor: default;
        }
        .pagination a:active {
            transform: scale(0.95);
        }
        .favorite-toggle {
            cursor: pointer;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="mainContainer">
    <h2>Danh sách từ vựng</h2>
    <c:set var="itemsPerPage" value="50" />
    <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
    <c:set var="startIndex" value="${(currentPage - 1) * itemsPerPage}" />
    <c:set var="endIndex" value="${startIndex + itemsPerPage - 1}" />
    <c:set var="totalItems" value="${dictionaryList.size()}" />
    <c:set var="totalPages" value="${(totalItems + itemsPerPage - 1) / itemsPerPage}" />
    <table>
        <tr>
            <th>Từ</th>
            <th>Định nghĩa</th>
            <th>Loại từ</th>
            <th>Ý nghĩa</th>
            <th>Add to Favorite</th>
        </tr>
        <c:forEach var="word" items="${dictionaryList}" begin="${startIndex}" end="${endIndex}">
            <tr>
                <td>${word.word}</td>
                <td>${word.definition}</td>
                <td>${word.type}</td>
                <td>${word.mean}</td>
                <td>
                    <c:set var="isFavorite" value="false" />
                    <c:forEach var="FC" items="${FavoriteFlashCardList}">
                        <c:if test="${word.wordID == FC.dictionary.wordID}">
                            <c:set var="isFavorite" value="true" />
                        </c:if>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${isFavorite}">
                            <img src="asset/png/icon/star-on.png" style="width: 18px" class="favorite-toggle" data-wordid="${word.wordID}">
                        </c:when>
                        <c:otherwise>
                            <img src="asset/png/icon/star.png" style="width: 18px" class="favorite-toggle" data-wordid="${word.wordID}">
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="?page=${currentPage - 1}">Trước</a>
        </c:if>
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a href="?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <a href="?page=${currentPage + 1}">Sau</a>
        </c:if>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('.favorite-toggle').on('click', function() {
            const $star = $(this); // Lưu tham chiếu đến phần tử ngôi sao
            const wordID = $star.data('wordid');
            const isFavorite = $star.attr('src').includes('star-on.png');
            const action = isFavorite ? 'removeFavoriteFlashCard' : 'addFavoriteFlashCard';
            const newSrc = isFavorite ? 'asset/png/icon/star.png' : 'asset/png/icon/star-on.png';
            const oldSrc = $star.attr('src'); // Lưu trạng thái ban đầu để rollback nếu cần

            // Đổi ảnh ngay lập tức khi click
            $star.attr('src', newSrc);

            $.ajax({
                url: 'dictionary',
                type: 'POST',
                data: { wordID: wordID, action: action },
                dataType: 'json',
                success: function(response) {
                    if (!response.success) {
                        // Nếu thất bại, hoàn tác thay đổi
                        $star.attr('src', oldSrc);
                        console.error('Error: ' + (response.error || 'Unknown error'));
                        alert('Failed to update favorite: ' + (response.error || 'Unknown error'));
                    }
                },
                error: function(xhr, status, error) {
                    // Nếu có lỗi AJAX, hoàn tác thay đổi
                    $star.attr('src', oldSrc);
                    console.error('AJAX error: ' + error);
                    alert('An error occurred while processing your request.');
                }
            });
        });
    });
</script>
</body>
</html>