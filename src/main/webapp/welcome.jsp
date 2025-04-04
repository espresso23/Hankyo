<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ</title>
    <link href="asset/css/StyleHomePage.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="asset/png/icon/logo.jpg">
</head>
<style>
    body {
        background-image: url('asset/png/contentHomePage/background.png');
        background-repeat: no-repeat;
    }
</style>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="wrapper">
    <div class="searchBar">
        <form action="search.jsp" method="GET">
            <input type="text" name="query" placeholder="Tìm kiếm..." required>
            <button type="submit">🔍</button>
        </form>
    </div>
    <div class="sideContent">
        <div class="smallContent"><img src="asset/png/contentHomePage/TIENG-HAN-SO-CAP-CHO-NGUOI-MOI-BAT-DAU.jpg"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_1.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_2.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_3.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_4.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_5.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_6.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_7.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_8.png"></div>
    </div>
    <div class="mainContent">
        <div class="bigContent"><img src="asset/png/contentHomePage/banner-south-korea.jpg"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_9.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_10.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_12.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_13.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_14.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_15.png"></div>
        <div class="smallContent"><img src="asset/png/contentHomePage/img_16.png"></div>
    </div>
</div>

</body>
</html>
