<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <link rel="stylesheet" href="asset/css/newspaper.css">
  <title>Đọc báo</title>
</head>
<style>
  body{
    background-image: url("asset/png/background/background.png");
    background-size: auto;
  }
  @font-face {
    font-family: 'Poppins';
    src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
  }
  body{
    font-family: 'Poppins', sans-serif;
  }
</style>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
  <div class="contentContainer">
    <div class="title">
      <h2>${newspaper.title}</h2>
    </div>
    <div class="image" >
      <img src="${pageContext.request.contextPath}/${newspaper.pictureFilePath}" alt="Newspaper Image">
    </div>
    <div class="text">
      ${newspaper.content}
    </div>
  </div>
  <div class="questionContainer">
    <div class="question">Chủ đề chính của đoạn văn trên là gì?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Đoạn văn trên đề cập đến nội dung gì?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Vì sao tác giả lại đưa ra quan điểm này?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Bạn có đồng ý với ý kiến của tác giả không? Vì sao?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Bạn rút ra được bài học gì từ đoạn văn trên?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Nếu có thể thay đổi một ý trong đoạn văn, bạn sẽ thay đổi điều gì?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Bạn có ví dụ thực tế nào liên quan đến nội dung của đoạn văn không?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>
  </div>
</div>
</body>
<jsp:include page="footer.jsp"></jsp:include>
</html>
