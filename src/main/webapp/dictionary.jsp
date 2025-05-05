<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Từ điển</title>
    <link href="asset/css/StyleHomePage.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Font và background */
        @font-face {
            font-family: 'Poppins';
            src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
        }

        body {
            font-family: 'Poppins', sans-serif;
            background-color: #fff9fb; /* Màu hồng pastel nhạt */
            color: #5a5a5a;
            background-image: url("asset/png/background/background-2.png");
            background-size: cover;
            background-attachment: fixed;
            padding-bottom: 50px;
        }

        /* Container chính */
        .mainContainer {
            max-width: 1200px;
            margin: 30px auto;
            padding: 25px;
            background-color: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 5px 25px rgba(255, 182, 193, 0.2);
        }

        /* Tiêu đề */
        h2 {
            color: #ff8fab; /* Màu hồng pastel */
            text-align: center;
            margin-bottom: 30px;
            font-weight: 600;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
        }

        /* Ô tìm kiếm */
        .search-container {
            background-color: #f0f9ff; /* Màu xanh pastel nhạt */
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 30px;
            box-shadow: 0 3px 15px rgba(168, 216, 234, 0.1);
            border: 1px solid #e0f2fe;
        }

        .search-form {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            align-items: center;
        }

        .search-input {
            flex: 1;
            min-width: 200px;
        }

        .form-control {
            border: 1px solid #d3e0f7;
            border-radius: 10px;
            padding: 10px 15px;
            font-size: 16px;
            transition: all 0.3s;
        }

        .form-control:focus {
            border-color: #a8d8ea;
            box-shadow: 0 0 0 0.25rem rgba(168, 216, 234, 0.2);
        }

        /* Nút tìm kiếm */
        .btn-primary {
            background-color: #89cff0; /* Màu xanh pastel */
            border-color: #89cff0;
            color: white;
            padding: 10px 25px;
            border-radius: 10px;
            font-weight: 500;
            transition: all 0.3s;
            box-shadow: 0 2px 10px rgba(137, 207, 240, 0.3);
        }

        .btn-primary:hover {
            background-color: #6cb4ee;
            border-color: #6cb4ee;
            transform: translateY(-2px);
        }

        /* Bảng từ vựng */
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 25px 0;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 5px 20px rgba(255, 182, 193, 0.1);
        }

        th {
            background-color: #ffb3c6; /* Màu hồng pastel đậm */
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: 600;
            position: sticky;
            top: 0;
        }

        td {
            padding: 12px 15px;
            border-bottom: 1px solid #f8e1e7;
            background-color: white;
            transition: all 0.2s;
        }

        tr:hover td {
            background-color: #faf0f3;
        }

        /* Nút thêm vào yêu thích */
        .add-toggle {
            display: inline-block;
            width: 28px;
            height: 28px;
            line-height: 28px;
            text-align: center;
            background-color: #89cff0; /* Màu xanh pastel */
            color: white;
            border-radius: 50%;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.3s;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .add-toggle:hover {
            background-color: #ff8fab; /* Màu hồng pastel */
            transform: scale(1.1) rotate(90deg);
        }

        /* Phân trang */
        .pagination-container {
            display: flex;
            justify-content: center;
            margin-top: 40px;
            overflow-x: auto;
            padding: 10px 0;
        }

        .pagination {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 8px;
            padding: 0;
            margin: 0;
        }

        .pagination a {
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 10px;
            color: #5a5a5a;
            border: 1px solid #e0c8d1;
            background-color: white;
            transition: all 0.3s;
            font-size: 15px;
            min-width: 40px;
            text-align: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
        }

        .pagination a:hover {
            background-color: #ffb3c6;
            color: white;
            border-color: #ffb3c6;
            transform: translateY(-2px);
        }

        .pagination a.active {
            background-color: #89cff0;
            color: white;
            border-color: #89cff0;
            font-weight: 500;
        }

        /* Popup */
        .popup-overlay {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(255, 182, 193, 0.5);
            z-index: 1000;
            display: none;
            backdrop-filter: blur(3px);
        }

        .popup {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 30px;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(255, 143, 171, 0.2);
            z-index: 1001;
            width: 380px;
            max-width: 90%;
            display: none;
            animation: popupFadeIn 0.3s;
        }

        @keyframes popupFadeIn {
            from { opacity: 0; transform: translate(-50%, -40%); }
            to { opacity: 1; transform: translate(-50%, -50%); }
        }

        .popup h3 {
            color: #ff8fab;
            margin-bottom: 25px;
            text-align: center;
            font-size: 22px;
        }

        #favoriteList {
            list-style: none;
            padding: 0;
            margin-bottom: 25px;
            max-height: 250px;
            overflow-y: auto;
            scrollbar-width: thin;
            scrollbar-color: #ffb3c6 #f8e1e7;
        }

        #favoriteList::-webkit-scrollbar {
            width: 6px;
        }

        #favoriteList::-webkit-scrollbar-track {
            background: #f8e1e7;
            border-radius: 10px;
        }

        #favoriteList::-webkit-scrollbar-thumb {
            background-color: #ffb3c6;
            border-radius: 10px;
        }

        #favoriteList li {
            padding: 12px 18px;
            margin-bottom: 10px;
            background-color: #faf0f3;
            border-radius: 12px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 15px;
        }

        #favoriteList li:hover {
            background-color: #ffe5ec;
            transform: translateX(5px);
        }

        #favoriteList li .status {
            color: #89cff0;
            font-weight: bold;
            font-size: 18px;
        }

        #newListName {
            width: 100%;
            padding: 12px 15px;
            margin-bottom: 20px;
            border: 1px solid #e0c8d1;
            border-radius: 12px;
            font-size: 15px;
            transition: all 0.3s;
        }

        #newListName:focus {
            border-color: #ff8fab;
            box-shadow: 0 0 0 3px rgba(255, 143, 171, 0.1);
        }

        #addToListBtn {
            width: 100%;
            padding: 12px;
            background-color: #ff8fab;
            color: white;
            border: none;
            border-radius: 12px;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: 500;
            font-size: 16px;
            box-shadow: 0 3px 10px rgba(255, 143, 171, 0.3);
        }

        #addToListBtn:hover {
            background-color: #ff759f;
            transform: translateY(-2px);
        }

        /* Responsive */
        @media (max-width: 768px) {
            .mainContainer {
                margin: 15px;
                padding: 20px;
            }

            .search-form {
                flex-direction: column;
                gap: 10px;
            }

            .search-input {
                width: 100%;
            }

            table {
                font-size: 14px;
            }

            th, td {
                padding: 10px 12px;
            }

            .pagination a {
                padding: 6px 12px;
                font-size: 14px;
            }
        }

        /* Style cho tìm kiếm AI */
        .ai-search-form {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 10px;
            margin-top: 20px;
        }

        .ai-search-form h4 {
            color: #6c757d;
            margin-bottom: 15px;
        }

        .input-group {
            gap: 10px;
        }

        #aiSearchResult {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .word-info {
            margin-bottom: 20px;
        }

        .word-info h4 {
            color: #007bff;
            margin-bottom: 15px;
        }

        .examples {
            border-top: 1px solid #dee2e6;
            padding-top: 15px;
        }

        .example-item {
            background-color: #f8f9fa;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
        }

        .example-item .korean {
            color: #007bff;
            margin-bottom: 5px;
        }

        .example-item .vietnamese {
            color: #6c757d;
        }

        /* Style cho ví dụ từ điển */
        .examples-container {
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }

        .examples-container h6 {
            color: #6c757d;
            margin-bottom: 10px;
        }

        .examples-list .example-item {
            margin-bottom: 10px;
            padding: 10px;
            background-color: white;
            border: 1px solid #dee2e6;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="mainContainer">
    <h2>Từ điển Hàn - Việt </h2>
    <div id="ai-usage-info"
         style="font-size:12px;color:#d483ce;text-align:right;margin-bottom: 8px; font-weight: 700;"></div>
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
            <button type="submit" class="btn btn-primary">🔍 Tìm kiếm</button>
        </form>
        
        <!-- Form tìm kiếm AI -->
        <div class="ai-search-form mt-3">
            <h4>Tìm kiếm nâng cao với Hankyo Translator</h4>
            <div class="input-group">
                <input type="text" id="aiSearchInput" class="form-control" placeholder="Nhập từ cần tìm kiếm...">
                <select id="fromLang" class="form-control">
                    <option value="vi">Tiếng Việt</option>
                    <option value="han">Tiếng Hàn</option>
                </select>
                <select id="toLang" class="form-control">
                    <option value="han">Tiếng Hàn</option>
                    <option value="vi">Tiếng Việt</option>
                </select>
                <button type="button" id="aiSearchBtn" class="btn btn-primary">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
        </div>
    </div>

    <!-- Thông báo lỗi AI -->
    <div id="ai-error-message" style="display:none; color:#d8000c; background:#fff4f4; border:1px solid #f5c6cb; border-radius:8px; padding:12px; margin:16px 0; font-weight:600; text-align:center;"></div>

    <!-- Kết quả tìm kiếm AI -->
    <div id="aiSearchResult" class="mt-4" style="display: none;">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Kết quả tìm kiếm AI</h5>
                <div id="aiResultContent"></div>
            </div>
        </div>
    </div>

    <table>
        <tr>
            <th>Từ vựng</th>
            <th>Định nghĩa</th>
            <th>Loại từ</th>
            <th>Nghĩa tiếng Việt</th>
            <th>Yêu thích</th>
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
            <!-- Ví dụ từ điển -->
            <tr class="examples-row" style="display: none;">
                <td colspan="5">
                    <div class="examples-container" data-wordid="${word.wordID}">
                        <h6>Ví dụ:</h6>
                        <div class="examples-list"></div>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>

    <div class="pagination-container">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}<c:if test="${not empty param.word}">&word=${param.word}</c:if><c:if test="${not empty param.mean}">&mean=${param.mean}</c:if><c:if test="${not empty param.type}">&type=${param.type}</c:if>">❮ Trước</a>
            </c:if>

            <c:set var="maxVisiblePages" value="5" />
            <c:set var="startPage" value="${currentPage - 2 > 1 ? currentPage - 2 : 1}" />
            <c:set var="endPage" value="${startPage + maxVisiblePages - 1 < totalPages ? startPage + maxVisiblePages - 1 : totalPages}" />

            <c:if test="${startPage > 1}">
                <a href="?page=1<c:if test="${not empty param.word}">&word=${param.word}</c:if><c:if test="${not empty param.mean}">&mean=${param.mean}</c:if><c:if test="${not empty param.type}">&type=${param.type}</c:if>">1</a>
                <c:if test="${startPage > 2}">
                    <span class="pagination-ellipsis">...</span>
                </c:if>
            </c:if>

            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <a href="?page=${i}<c:if test="${not empty param.word}">&word=${param.word}</c:if><c:if test="${not empty param.mean}">&mean=${param.mean}</c:if><c:if test="${not empty param.type}">&type=${param.type}</c:if>" class="${i == currentPage ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${endPage < totalPages}">
                <c:if test="${endPage < totalPages - 1}">
                    <span class="pagination-ellipsis">...</span>
                </c:if>
                <a href="?page=${totalPages}<c:if test="${not empty param.word}">&word=${param.word}</c:if><c:if test="${not empty param.mean}">&mean=${param.mean}</c:if><c:if test="${not empty param.type}">&type=${param.type}</c:if>">${totalPages}</a>
            </c:if>

            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}<c:if test="${not empty param.word}">&word=${param.word}</c:if><c:if test="${not empty param.mean}">&mean=${param.mean}</c:if><c:if test="${not empty param.type}">&type=${param.type}</c:if>">Sau ❯</a>
            </c:if>
        </div>
    </div>
</div>

<!-- Popup để chọn hoặc tạo danh sách yêu thích -->
<div class="popup-overlay" id="popupOverlay"></div>
<div class="popup" id="favoritePopup">
    <h3>❤️ Thêm vào danh sách yêu thích</h3>
    <ul id="favoriteList">
        <c:forEach var="listName" items="${favoriteListNames}">
            <li data-name="${listName}">${listName}<span class="status"></span></li>
        </c:forEach>
    </ul>
    <input type="text" id="newListName" placeholder="Tên danh sách mới...">
    <button id="addToListBtn">💖 Thêm vào</button>
</div>

<script>
    $(document).ready(function() {
        let currentWordID = null;

        $('.add-toggle').on('click', function() {
            const $addSign = $(this);
            currentWordID = $addSign.data('wordid');
            $('#popupOverlay, #favoritePopup').fadeIn(200);
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
            $('#popupOverlay, #favoritePopup').fadeOut(200);
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
                    $('#addToListBtn').prop('disabled', true).html('⏳ Đang thêm...');
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

                        // Hiệu ứng khi thêm thành công
                        $('#favoritePopup').animate({
                            'box-shadow': '0 0 0 5px rgba(137, 207, 240, 0.5)'
                        }, 100, function() {
                            $(this).animate({
                                'box-shadow': '0 10px 30px rgba(255, 143, 171, 0.2)'
                            }, 200);
                        });

                        setTimeout(function() {
                            $('#popupOverlay, #favoritePopup').fadeOut(200);
                            $('#newListName').val('');
                        }, 800);

                        // Hiệu ứng trái tim bay
                        const heart = $('<div>').html('❤️').css({
                            position: 'fixed',
                            fontSize: '20px',
                            zIndex: 1002,
                            top: $('#favoritePopup').offset().top + 50,
                            left: $('#favoritePopup').offset().left + $('#favoritePopup').width()/2 - 10,
                            pointerEvents: 'none',
                            opacity: 1
                        }).appendTo('body');

                        heart.animate({
                            top: '-=50',
                            opacity: 0
                        }, 1000, function() {
                            $(this).remove();
                        });
                    } else {
                        alert('Không thể thêm vào danh sách: ' + (response.error || 'Lỗi không xác định'));
                    }
                },
                error: function(xhr, status, error) {
                    console.error('AJAX error:', error);
                    alert('Có lỗi xảy ra khi thêm vào danh sách yêu thích');
                },
                complete: function() {
                    $('#addToListBtn').prop('disabled', false).html('💖 Thêm vào');
                }
            });
        }

        // Xử lý tìm kiếm AI
        $('#aiSearchBtn').click(function() {
            const word = $('#aiSearchInput').val().trim();
            const fromLang = $('#fromLang').val();
            const toLang = $('#toLang').val();

            if (!word) {
                alert('Vui lòng nhập từ cần tìm kiếm!');
                return;
            }

            $.ajax({
                url: 'dictionary',
                type: 'POST',
                data: {
                    action: 'searchAI',
                    word: word,
                    fromLang: fromLang,
                    toLang: toLang
                },
                success: function(response) {
                    $('#ai-error-message').hide();
                    try {
                        if (typeof response === 'object') {
                            displayAIResult(response);
                            return;
                        }
                        var result = extractJsonFromGeminiResponse(response);
                        if (!result) throw new Error('Không tìm thấy JSON hợp lệ trong phản hồi AI!');
                        displayAIResult(result);
                    } catch (e) {
                        console.error('Error parsing response:', e, response);
                        $('#ai-error-message').text('Có lỗi xảy ra khi xử lý kết quả! Bạn có thể copy response này gửi cho kỹ thuật: ' + response).show();
                    }
                },
                error: function(xhr, status, error) {
                    if (xhr.status === 403 && xhr.responseText && xhr.responseText.includes('hết lượt')) {
                        $('#ai-error-message').text('Bạn đã hết lượt sử dụng AI miễn phí hôm nay! Hãy nâng cấp VIP để sử dụng không giới hạn.').show();
                    } else {
                        $('#ai-error-message').text('Có lỗi xảy ra khi tìm kiếm!').show();
                    }
                }
            });
        });

        // Hàm chuyên lấy object JSON từ response Gemini (trả về object, không trả về chuỗi)
        function extractJsonFromGeminiResponse(response) {
            if (typeof response !== 'string') return null;
            response = response.trim();

            // 1. Tìm tất cả các block code markdown ```...```
            var codeBlocks = response.match(/```[a-z]*[\s\S]*?```/gi);
            if (codeBlocks && codeBlocks.length > 0) {
                for (var i = 0; i < codeBlocks.length; i++) {
                    var block = codeBlocks[i]
                        .replace(/^```[a-z]*[\r\n\s]*/i, '')
                        .replace(/```[\r\n\s]*$/i, '')
                        .trim();
                    // Thử parse block này
                    try {
                        var obj = JSON.parse(block);
                        return obj;
                    } catch (e) {
                        // Không parse được, thử block tiếp theo
                    }
                }
            }
            // 2. Nếu không có block code, thử parse đoạn JSON đầu tiên trong toàn bộ response
            var jsonMatch = response.match(/\{[\s\S]*\}/);
            if (jsonMatch) {
                try {
                    return JSON.parse(jsonMatch[0]);
                } catch (e) {}
            }
            // 3. Thử loại bỏ hết dấu ``` và parse toàn bộ response
            var cleaned = response.replace(/```[a-z]*[\r\n\s]*/gi, '').replace(/```/g, '').trim();
            try {
                return JSON.parse(cleaned);
            } catch (e) {}
            // 4. Không tìm thấy JSON
            return null;
        }

        // Hiển thị kết quả tìm kiếm AI
        function displayAIResult(result) {
            var container = $('#aiResultContent');
            var html = '';
            html += '<div class="word-info">';
            html += '<h4>' + result.word + '</h4>';
            html += '<p><strong>Nghĩa:</strong> ' + result.translation + '</p>';
            html += '<p><strong>Loại từ:</strong> ' + result.type + '</p>';
            html += '<p><strong>Định nghĩa:</strong> ' + result.definition + '</p>';
            html += '</div>';
            html += '<div class="examples">';
            html += '<h5>Ví dụ:</h5>';
            if (result.examples && result.examples.length > 0) {
                for (var i = 0; i < result.examples.length; i++) {
                    html += '<div class="example-item">';
                    html += '<p class="korean">' + result.examples[i].han + '</p>';
                    html += '<p class="vietnamese">' + result.examples[i].vi + '</p>';
                    html += '</div>';
                }
            }
            html += '</div>';
            container.html(html);
            $('#aiSearchResult').show();

            // Tự động lưu ví dụ đầu tiên nếu là tra từ Hàn sang Việt
            if ($('#fromLang').val() === 'han' && $('#toLang').val() === 'vi' && result.examples && result.examples.length > 0) {
                var example = result.examples[0];
                $.ajax({
                    url: 'dictionary',
                    type: 'POST',
                    data: {
                        action: 'addExample',
                        searchDirection: 'han2vi',
                        word: result.word,
                        mean: result.translation,
                        definition: result.definition,
                        type: result.type,
                        vietnameseExample: example.vi,
                        koreanExample: example.han
                    },
                    success: function(res) {
                        // Không alert, hoạt động ngầm
                    },
                    error: function(xhr, status, error) {
                        // Không alert, hoạt động ngầm
                    }
                });
            }
        }

        // Xử lý hiển thị ví dụ
        $('.add-toggle').click(function() {
            const wordID = $(this).data('wordid');
            const examplesRow = $(this).closest('tr').next('.examples-row');
            const examplesContainer = examplesRow.find('.examples-container');
            
            if (examplesRow.is(':visible')) {
                examplesRow.hide();
                return;
            }

            // Ẩn tất cả các hàng ví dụ khác
            $('.examples-row').hide();

            // Kiểm tra xem đã có ví dụ chưa
            if (examplesContainer.find('.examples-list').children().length === 0) {
                // Lấy ví dụ từ server
                $.ajax({
                    url: 'dictionary',
                    type: 'POST',
                    data: {
                        action: 'getExamples',
                        wordID: wordID
                    },
                    success: function(response) {
                        try {
                            const examples = JSON.parse(response);
                            displayExamples(examplesContainer, examples);
                        } catch (e) {
                            console.error('Error parsing examples:', e);
                            examplesContainer.html('<p class="text-muted">Không có ví dụ</p>');
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error('AJAX error:', error);
                        examplesContainer.html('<p class="text-danger">Lỗi khi tải ví dụ</p>');
                    }
                });
            }

            examplesRow.show();
        });

        // Hiển thị ví dụ
        function displayExamples(container, examples) {
            if (examples.length === 0) {
                container.html('<p class="text-muted">Không có ví dụ</p>');
                return;
            }
            var examplesList = container.find('.examples-list');
            var html = '';
            for (var i = 0; i < examples.length; i++) {
                html += '<div class="example-item">';
                html += '<p class="korean">' + examples[i].han + '</p>';
                html += '<p class="vietnamese">' + examples[i].vi + '</p>';
                html += '</div>';
            }
            examplesList.html(html);
        }
    });

    // --- Quota AI giống header-user.jsp ---
    window.aiUsageCache = window.aiUsageCache || {isVip: false, used: 0, left: 20};

    function updateAIUsageInfo(forceFetch = false) {
        // Nếu không force, ưu tiên dùng cache để cập nhật nhanh
        if (!forceFetch && window.aiUsageCache) {
            renderAIUsageInfo(window.aiUsageCache);
        }
        // Luôn fetch lại để đồng bộ số liệu mới nhất
        fetch('ai-usage-info')
          .then(res => res.json())
          .then(data => {
            window.aiUsageCache = data;
            renderAIUsageInfo(data);
          });
    }

    function renderAIUsageInfo(data) {
        let msg = '';
        if (data.isVip) msg = 'AI: Không giới hạn';
        else msg = 'AI: Đã dùng ' + data.used + '/20 lượt hôm nay. Còn lại: ' + data.left;
        if (!data.isVip && data.left <= 3 && data.left > 0) msg += ' <b>(Sắp hết lượt!)</b>';
        if (!data.isVip && data.left === 0) msg += ' <b>(Đã hết lượt miễn phí!)</b>';
        document.querySelectorAll('#ai-usage-info').forEach(function(el) {
          el.innerHTML = msg;
        });
    }

    document.addEventListener('DOMContentLoaded', function() {
        updateAIUsageInfo(true);
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
