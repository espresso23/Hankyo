<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <title>Từ điển</title>
    <link href="asset/css/StyleHomePage.css" rel="stylesheet" type="text/css">
</head>
<style>
    table {
        width: 80%;
        margin: 20px auto;
        border-collapse: collapse;
    }
    th, td {
        border: 1px solid black;
        padding: 10px;
        text-align: left;
    }
    th {
        background-color: #f2f2f2;
    }
    .pagination {
        text-align: center;
        margin: 20px 0;
    }
    .pagination a {
        margin: 0 5px;
        text-decoration: none;
        padding: 5px 10px;
        border: 1px solid #ccc;
    }
    .pagination a.active {
        background-color: #4CAF50;
        color: white;
    }
</style>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="mainContainer">
    <h2 style="text-align: center;">Danh sách từ vựng</h2>
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
        </tr>
        <c:forEach var="word" items="${dictionaryList}" begin="${startIndex}" end="${endIndex}">
            <tr>
                <td>${word.word}</td>
                <td>${word.definition}</td>
                <td>${word.type}</td>
                <td>${word.mean}</td>
            </tr>
        </c:forEach>
    </table>

    <%-- Hiển thị các nút phân trang --%>
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
</body>
</html>