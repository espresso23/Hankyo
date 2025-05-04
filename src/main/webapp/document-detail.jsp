<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="model.Documentary" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  Documentary doc = (Documentary) request.getAttribute("document");
  if (doc == null) {
    response.sendRedirect("documents");
    return;
  }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= doc.getTitle() %> | Hankyo</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <style>
    /* Thiết lập chung với màu sắc nhẹ nhàng hơn */
    :root {
      --pink-primary: #f0a1b8;
      --pink-light: #f7d6e0;
      --mint-primary: #a0e5d7;
      --mint-dark: #88c7ba;
      --mint-light: #d0f5ee;
      --text-dark: #4a5568;
      --text-light: #ffffff;
      --bg-color: #f8f9fa;
      --shadow: 0 3px 8px rgba(0, 0, 0, 0.05);
      --border-radius: 10px;
    }

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      transition: all 0.3s ease;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: var(--bg-color);
      color: var(--text-dark);
      line-height: 1.6;
      padding: 0;
      margin: 0;
    }

    /* Container điều chỉnh để sử dụng nhiều không gian hơn */
    .container {
      max-width: 95%;
      margin: 0 auto;
      padding: 0 15px;
    }

    /* Header */
    h1 {
      color: var(--pink-primary);
      text-align: center;
      margin: 1.5rem 0;
      font-size: 2rem;
      font-weight: 700;
    }

    h3 {
      color: var(--mint-dark);
      margin-bottom: 0.8rem;
      font-size: 1.2rem;
      font-weight: 600;
    }

    /* Document Info Section - đặt sang bên cạnh */
    .document-metadata {
      display: flex;
      flex-wrap: wrap;
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .document-info {
      flex: 1;
      min-width: 250px;
      background-color: white;
      border-radius: var(--border-radius);
      padding: 15px;
      box-shadow: var(--shadow);
    }

    .document-info p {
      margin: 8px 0;
      font-size: 1rem;
    }

    .document-info strong {
      color: var(--pink-primary);
      font-weight: 600;
    }

    /* Audio Player - đặt cạnh thông tin tài liệu */
    .audio-section {
      flex: 2;
      min-width: 300px;
      background: linear-gradient(145deg, var(--pink-light), var(--mint-light));
      border-radius: var(--border-radius);
      padding: 15px;
      box-shadow: var(--shadow);
    }

    audio {
      width: 100%;
      height: 40px;
      margin: 10px 0;
      border-radius: 20px;
    }

    /* PDF Viewer - lớn hơn */
    .document-viewer {
      background-color: white;
      border-radius: var(--border-radius);
      padding: 15px;
      box-shadow: var(--shadow);
      margin: 15px 0;
    }

    iframe {
      border-radius: 8px;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
      min-height: 700px;
    }

    /* Navigation Buttons */
    .navigation {
      display: flex;
      justify-content: flex-start;
      gap: 15px;
      margin: 15px 0;
    }

    .btn {
      display: inline-flex;
      align-items: center;
      padding: 10px 20px;
      background: linear-gradient(45deg, var(--pink-light), var(--mint-light));
      color: var(--text-dark);
      text-decoration: none;
      border-radius: 50px;
      font-weight: 600;
      font-size: 0.95rem;
      box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
    }

    .btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      background: linear-gradient(45deg, var(--mint-light), var(--pink-light));
    }

    .btn i {
      margin-right: 8px;
    }

    /* Responsive Design */
    @media (max-width: 768px) {
      .document-metadata {
        flex-direction: column;
      }

      .document-info,
      .audio-section {
        width: 100%;
      }

      iframe {
        height: 500px;
      }

      .navigation {
        flex-wrap: wrap;
      }
    }

    /* Nhẹ nhàng animation */
    @keyframes fadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }

    .document-info, .audio-section, .document-viewer {
      animation: fadeIn 0.6s ease forwards;
    }  </style>
</head>
<body>

<c:import url="header.jsp"/>

<div class="container">
  <h1><%= doc.getTitle() %></h1>

  <div class="document-metadata">
    <div class="document-info">
      <h3><i class="fas fa-info-circle"></i> Thông tin tài liệu</h3>
      <p><strong>Tác giả:</strong> <%= doc.getAuthor() %></p>
      <p><strong>Loại:</strong> <%= doc.getType() %></p>
    </div>

    <% if (doc.getAudioPath() != null && !doc.getAudioPath().isEmpty()) { %>
    <div class="audio-section">
      <h3><i class="fas fa-headphones"></i> Audio</h3>
      <audio controls>
        <source src="<%= doc.getAudioPath() %>" type="audio/mpeg">
      </audio>
    </div>
    <% } %>
  </div>

  <div class="document-viewer">
    <h3><i class="fas fa-file-alt"></i> Tài liệu</h3>
    <iframe src="<%= doc.getSource() %>" width="100%" height="700px"></iframe>
  </div>

  <div class="navigation">
    <a href="<%= doc.getSource() %>" download class="btn">
      <i class="fas fa-download"></i> Tải xuống
    </a>
    <a href="documents" class="btn">
      <i class="fas fa-arrow-left"></i> Quay lại danh sách
    </a>
  </div>
</div>
<c:if test="${vipOnly}">
  <div style="
        position: fixed;
        top: 0; left: 0;
        width: 100%; height: 100%;
        background-color: rgba(0,0,0,0.7);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 9999;">
    <div style="
            background-color: white;
            padding: 40px;
            border-radius: 12px;
            text-align: center;
            max-width: 500px;">
      <h2 style="color: #f06595;"><i class="fas fa-lock"></i> Nội dung VIP</h2>
      <p>Bạn cần nâng cấp tài khoản để xem nội dung này.</p>
      <a href="bundles" style="
                display: inline-block;
                margin-top: 20px;
                background-color: #48dbfb;
                color: white;
                padding: 10px 20px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: bold;
            ">Nâng cấp ngay</a>
    </div>
  </div>
</c:if>

<c:import url="footer.jsp"/>
</body>
</html>
