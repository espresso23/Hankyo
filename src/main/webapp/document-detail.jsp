<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Documentary" %>
<%
  Documentary doc = (Documentary) request.getAttribute("document");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= doc.getTitle() %> | Hankyo</title>
  <style>
    :root {
      --primary-color: #6a11cb;
      --secondary-color: #2575fc;
      --text-color: #2d3748;
      --light-gray: #f7fafc;
      --white: #ffffff;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: var(--light-gray);
      color: var(--text-color);
      line-height: 1.6;
      margin: 0;
      padding: 0;
    }

    .doc-container {
      max-width: 1000px;
      margin: 40px auto;
      background: var(--white);
      border-radius: 15px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      overflow: hidden;
    }

    .doc-header {
      background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
      color: white;
      padding: 30px;
      position: relative;
    }

    .doc-header::after {
      content: '';
      position: absolute;
      bottom: -20px;
      left: 0;
      right: 0;
      height: 40px;
      background: var(--white);
      border-radius: 50% 50% 0 0 / 30px;
    }

    .doc-title {
      font-size: 28px;
      font-weight: 700;
      margin: 0 0 10px;
    }

    .doc-meta {
      display: flex;
      gap: 20px;
      font-size: 14px;
      opacity: 0.9;
    }

    .doc-content {
      padding: 30px;
    }

    .doc-audio {
      margin-bottom: 25px;
      background: var(--light-gray);
      border-radius: 10px;
      padding: 15px;
    }

    .doc-audio audio {
      width: 100%;
    }

    .doc-frame-container {
      position: relative;
      padding-bottom: 75%;
      height: 0;
      overflow: hidden;
      border-radius: 10px;
      margin-bottom: 25px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .doc-frame {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      border: none;
    }

    .doc-actions {
      display: flex;
      gap: 15px;
      margin-top: 20px;
    }

    .btn {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      padding: 12px 20px;
      border-radius: 50px;
      text-decoration: none;
      font-weight: 600;
      transition: all 0.3s ease;
    }

    .btn-download {
      background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
      color: white;
    }

    .btn-back {
      background: var(--light-gray);
      color: var(--text-color);
    }

    .btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .badge {
      display: inline-block;
      padding: 5px 10px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      background: rgba(255, 255, 255, 0.2);
      margin-left: 10px;
    }
  </style>
</head>
<body>
<div class="doc-container">
  <div class="doc-header">
    <h1 class="doc-title"><%= doc.getTitle() %></h1>
    <div class="doc-meta">
      <span>📚 <%= doc.getAuthor() %></span>
      <span>🏷️ <%= doc.getType() %></span>
      <% if (doc.getLevel() != null && !doc.getLevel().isEmpty()) { %>
      <span class="badge"><%= doc.getLevel() %></span>
      <% } %>
    </div>
  </div>

  <div class="doc-content">
    <% if (doc.getAudioPath() != null && !doc.getAudioPath().trim().isEmpty()) { %>
    <div class="doc-audio">
      <h3>🎧 Audio kèm theo</h3>
      <audio controls>
        <source src="<%= doc.getAudioPath() %>" type="audio/mpeg">
        Trình duyệt của bạn không hỗ trợ audio.
      </audio>
    </div>
    <% } %>

    <div class="doc-frame-container">
      <iframe class="doc-frame" src="<%= request.getContextPath() + doc.getSource() %>"></iframe>
    </div>

    <div class="doc-actions">
      <a href="<%= request.getContextPath() + doc.getSource() %>" download class="btn btn-download">
        📥 Tải xuống
      </a>
      <a href="documents" class="btn btn-back">
        ← Quay lại danh sách
      </a>
    </div>
  </div>
</div>
</body>
</html>