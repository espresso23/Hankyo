<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 4/2/2025
  Time: 12:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
  <title>FlashCard tự tạo</title>
  <link rel="icon" href="asset/png/icon/logo.jpg">
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background-color: #f0f0f0;
      margin: 0;
    }
    .container {
      text-align: center;
      background-color: #fff;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.2);
      width: 80%;
      max-width: 1000px;
    }
    h1 {
      color: #333;
      margin-bottom: 20px;
    }
    .favorite-section {
      margin-bottom: 30px;
      display: flex;
      justify-content: center;
      gap: 20px; /* Khoảng cách giữa hai nút */
    }
    .favorite-btn, .custom-btn {
      padding: 15px 30px;
      font-size: 18px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      text-decoration: none;
      display: inline-block;
    }
    .custom-btn {
      background-color: #2196F3; /* Màu khác để phân biệt */
    }
    .favorite-btn:hover {
      background-color: #45a049;
    }
    .custom-btn:hover {
      background-color: #1976D2;
    }
    .topics-scroll {
      display: flex;
      overflow-x: auto;
      gap: 15px;
      padding: 10px;
      white-space: nowrap;
    }
    .topic-box {
      flex: 0 0 auto;
      width: 150px;
      height: 150px;
      background-color: #f9f9f9;
      border: 1px solid #ddd;
      border-radius: 5px;
      display: flex;
      justify-content: center;
      align-items: center;
      text-align: center;
      cursor: pointer;
      transition: background-color 0.3s;
    }
    .topic-box:hover {
      background-color: #e0e0e0;
    }
    .topic-box a {
      text-decoration: none;
      color: #333;
      font-size: 16px;
      padding: 10px;
      display: block;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    /* Tùy chỉnh thanh cuộn */
    .topics-scroll::-webkit-scrollbar {
      height: 8px;
    }
    .topics-scroll::-webkit-scrollbar-thumb {
      background-color: #888;
      border-radius: 4px;
    }
    .topics-scroll::-webkit-scrollbar-thumb:hover {
      background-color: #555;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Select a Topic</h1>
  <!-- Thành phần 2: Thanh cuộn ngang các topic -->
  <div class="topics-scroll">
    <c:forEach var="item" items="${listTopic}">
      <div class="topic-box">
        <a href="flashCard?topic=${item}&type=${type}">${item}</a>
      </div>
    </c:forEach>
  </div>
</div>
</body>
</html>