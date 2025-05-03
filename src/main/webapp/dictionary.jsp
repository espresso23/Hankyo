<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <title>Từ điển</title>
    <link href="asset/css/StyleHomePage.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-image: url("asset/png/background/background-2.png");
            background-size: auto;
        }
        @font-face {
            font-family: 'Poppins';
            src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
        }
        body{
            font-family: 'Poppins', sans-serif;
        }
        .search-container {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .search-form {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .search-input {
            flex: 1;
            min-width: 200px;
        }
    </style>
</head>
<link rel="stylesheet" href="asset/css/dictionary.css">
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

    <!-- Form tìm kiếm -->
    <div class="search-container">
        <form action="dictionary" method="get" class="search-form">
            <div class="search-input">
                <input type="text" name="word" class="form-control" placeholder="Nhập từ tiếng Hàn..." value="${param.word}">
            </div>
            <div class="search-input">
                <input type="text" name="mean" class="form-control" placeholder="Nhập nghĩa tiếng Việt..." value="${param.mean}">
            </div>
            <div class="search-input">
                <select name="type" class="form-control">
                    <option value="">-- Chọn loại từ --</option>
                    <option value="noun" ${param.type == 'noun' ? 'selected' : ''}>Danh từ</option>
                    <option value="verb" ${param.type == 'verb' ? 'selected' : ''}>Động từ</option>
                    <option value="adjective" ${param.type == 'adjective' ? 'selected' : ''}>Tính từ</option>
                    <option value="adverb" ${param.type == 'adverb' ? 'selected' : ''}>Trạng từ</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>
    </div>

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
                    <span class="add-toggle" data-wordid="${word.wordID}">+</span>
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

<!-- Popup để chọn hoặc tạo danh sách yêu thích -->
<div class="popup-overlay" id="popupOverlay"></div>
<div class="popup" id="favoritePopup">
    <h3>Chọn danh sách yêu thích</h3>
    <ul id="favoriteList">
        <c:forEach var="listName" items="${favoriteListNames}">
            <li data-name="${listName}">${listName}<span class="status"></span></li>
        </c:forEach>
    </ul>
    <input type="text" id="newListName" placeholder="Tạo danh sách mới">
    <button id="addToListBtn">Thêm</button>
</div>

<script>
    $(document).ready(function() {
        let currentWordID = null;

        $('.add-toggle').on('click', function() {
            const $addSign = $(this);
            currentWordID = $addSign.data('wordid');
            $('#popupOverlay, #favoritePopup').show();
        });

        $('#favoriteList').on('click', 'li', function() {
            const nameOfList = $(this).data('name');
            addToFavorite(currentWordID, nameOfList);
        });

        $('#addToListBtn').on('click', function() {
            const nameOfList = $('#newListName').val().trim();
            if (nameOfList) {
                addToFavorite(currentWordID, nameOfList);
            } else {
                alert('Vui lòng nhập tên danh sách!');
            }
        });

        $('#popupOverlay').on('click', function() {
            $('#popupOverlay, #favoritePopup').hide();
            $('#newListName').val('');
        });

        function addToFavorite(wordID, nameOfList) {
            $.ajax({
                url: 'dictionary',
                type: 'POST',
                data: { 
                    wordID: wordID, 
                    action: 'addFavoriteFlashCard',
                    nameOfList: nameOfList 
                },
                dataType: 'json',
                beforeSend: function() {
                    $('#addToListBtn').prop('disabled', true).text('Đang thêm...');
                },
                success: function(response) {
                    if (response.success) {
                        if (!$('#favoriteList li[data-name="' + nameOfList + '"]').length) {
                            $('#favoriteList').append(
                                '<li data-name="' + nameOfList + '">' + 
                                nameOfList + 
                                '<span class="status">✓</span></li>'
                            );
                        } else {
                            $('#favoriteList li[data-name="' + nameOfList + '"]')
                                .find('.status').text('✓');
                        }
                        $('#popupOverlay, #favoritePopup').hide();
                        $('#newListName').val('');
                        alert('Đã thêm vào danh sách: ' + nameOfList);
                    } else {
                        alert('Không thể thêm vào danh sách: ' + (response.error || 'Unknown error'));
                    }
                },
                error: function(xhr, status, error) {
                    console.error('AJAX error:', error);
                    alert('Có lỗi xảy ra khi thêm vào danh sách yêu thích');
                },
                complete: function() {
                    $('#addToListBtn').prop('disabled', false).text('Thêm');
                }
            });
        }
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>