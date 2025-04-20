<%@ page import="model.User" %>
<%@ page import="dao.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null || !user.getRole().equals("admin")) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Upload Tài Liệu</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f7f9fc;
      padding: 40px;
    }

    .upload-container {
      max-width: 500px;
      margin: auto;
      background: #fff;
      border-radius: 10px;
      padding: 30px;
      box-shadow: 0 4px 10px rgba(0,0,0,0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 25px;
    }

    label {
      font-weight: bold;
      display: block;
      margin-top: 15px;
    }

    input[type="text"],
    input[type="file"],
    select,
    textarea {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      border: 1px solid #ccc;
      border-radius: 6px;
      box-sizing: border-box;
    }

    button {
      margin-top: 25px;
      width: 100%;
      background-color: #007BFF;
      color: white;
      padding: 12px;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    button:hover {
      background-color: #0056b3;
    }

    button:disabled {
      background-color: #cccccc;
      cursor: not-allowed;
    }

    .back-link {
      text-align: center;
      margin-top: 20px;
    }

    .back-link a {
      text-decoration: none;
      color: #007BFF;
    }

    .error-message {
      color: #dc3545;
      font-size: 14px;
      margin-top: 5px;
      display: none;
    }

    .file-info {
      font-size: 12px;
      color: #6c757d;
      margin-top: 5px;
    }
  </style>
</head>
<body>
<c:import url="header.jsp"/>
<div class="upload-container">
  <h2>📝 Upload Tài Liệu Mới</h2>

  <c:if test="${not empty errorMessage}">
    <div class="error-message" style="display: block; text-align: center; margin-bottom: 20px;">
        ${errorMessage}
    </div>
  </c:if>
  <form id="uploadForm" action="upload-document" method="post" enctype="multipart/form-data" onsubmit="return validateFiles()">
    <label for="title">Tiêu đề:</label>
    <input type="text" name="title" required>

    <label for="author">Tác giả:</label>
    <input type="text" name="author" required>

    <label for="type">Loại tài liệu:</label>
    <select name="type" required>
      <option value="">-- Chọn loại --</option>
      <option value="Từ vựng">Từ vựng</option>
      <option value="Tiếng Hàn tổng hợp">Tiếng Hàn tổng hợp</option>
      <option value="TOPIK master">TOPIK master</option>
      <option value="Kyunghee">Kyunghee</option>
      <option value="Sejong">Sejong</option>
      <option value="Kiip">Kiip</option>
      <option value="Seoul">Seoul</option>
      <option value="Sogang">Sogang</option>
      <option value="Yonsei">Yonsei</option>
    </select>

    <label for="content">Mô tả nội dung tài liệu:</label>
    <textarea name="content" rows="4" style="width:100%; padding:10px; border-radius:6px; border:1px solid #ccc;"></textarea>

    <label for="file">File tài liệu (PDF):</label>
    <input type="file" id="fileInput" name="file" accept=".pdf" required>
    <div id="fileError" class="error-message">Tài liệu vượt quá 10MB! Vui lòng thử lại</div>
    <div id="fileInfo" class="file-info"></div>

    <label for="audio">File audio (tuỳ chọn):</label>
    <input type="file" id="audioInput" name="audio" accept="audio/*">
    <div id="audioError" class="error-message">File audio vượt quá 10MB!</div>
    <div id="audioInfo" class="file-info"></div>

    <label for="thumbnail">Ảnh đại diện (thumbnail):</label>
    <input type="file" id="thumbnailInput" name="thumbnail" accept="image/*">
    <div id="thumbnailError" class="error-message">Ảnh đại diện vượt quá 2MB!</div>
    <div id="thumbnailInfo" class="file-info"></div>

    <button type="submit" id="submitBtn">📤 Tải lên</button>
  </form>

  <div class="back-link">
    <a href="documents">← Quay lại danh sách tài liệu</a>
  </div>
</div>
<c:import url="footer.jsp"/>

<script>
  // Maximum file sizes in bytes
  const MAX_DOC_SIZE = 10 * 1024 * 1024; // 10MB
  const MAX_AUDIO_SIZE = 10 * 1024 * 1024; // 10MB
  const MAX_THUMBNAIL_SIZE = 2 * 1024 * 1024; // 2MB

  // File input elements
  const fileInput = document.getElementById('fileInput');
  const audioInput = document.getElementById('audioInput');
  const thumbnailInput = document.getElementById('thumbnailInput');

  // Error message elements
  const fileError = document.getElementById('fileError');
  const audioError = document.getElementById('audioError');
  const thumbnailError = document.getElementById('thumbnailError');

  // File info elements
  const fileInfo = document.getElementById('fileInfo');
  const audioInfo = document.getElementById('audioInfo');
  const thumbnailInfo = document.getElementById('thumbnailInfo');

  // Form and submit button
  const uploadForm = document.getElementById('uploadForm');
  const submitBtn = document.getElementById('submitBtn');

  // Add event listeners for file changes
  fileInput.addEventListener('change', function() {
    validateFile(this, fileError, fileInfo, MAX_DOC_SIZE, 'Tài liệu');
  });

  audioInput.addEventListener('change', function() {
    validateFile(this, audioError, audioInfo, MAX_AUDIO_SIZE, 'Audio');
  });

  thumbnailInput.addEventListener('change', function() {
    validateFile(this, thumbnailError, thumbnailInfo, MAX_THUMBNAIL_SIZE, 'Ảnh đại diện');
  });

  // Validate individual file
  function validateFile(input, errorElement, infoElement, maxSize, fileType) {
    if (input.files.length > 0) {
      const file = input.files[0];
      const fileSize = file.size;

      // Display file info
      const sizeInMB = (fileSize / (1024 * 1024)).toFixed(2);
      infoElement.textContent = `${fileType} ${file.name} ${sizeInMB}`;

      // Check size
      if (fileSize > maxSize) {
        errorElement.style.display = 'block';
        input.setCustomValidity('File too large');
        return false;
      } else {
        errorElement.style.display = 'none';
        input.setCustomValidity('');
        return true;
      }
    } else {
      infoElement.textContent = '';
      errorElement.style.display = 'none';
      input.setCustomValidity('');
      return true;
    }
  }

  // Validate all files before form submission
  function validateFiles() {
    let isValid = true;

    // Validate required PDF file
    if (!validateFile(fileInput, fileError, fileInfo, MAX_DOC_SIZE, 'Tài liệu')) {
      isValid = false;
    }

    // Validate optional audio file
    if (audioInput.files.length > 0 &&
            !validateFile(audioInput, audioError, audioInfo, MAX_AUDIO_SIZE, 'Audio')) {
      isValid = false;
    }

    // Validate optional thumbnail
    if (thumbnailInput.files.length > 0 &&
            !validateFile(thumbnailInput, thumbnailError, thumbnailInfo, MAX_THUMBNAIL_SIZE, 'Ảnh đại diện')) {
      isValid = false;
    }

    if (!isValid) {
      // Scroll to first error
      const firstError = document.querySelector('.error-message[style="display: block;"]');
      if (firstError) {
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
      return false;
    }

    return true;
  }
</script>

</body>
</html>