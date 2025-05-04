<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/asset/png/icon/logo.jpg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upload Tài Liệu | Hankyo</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Thiết lập chung với màu sắc nhẹ nhàng */
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

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-dark);
            line-height: 1.6;
            padding: 20px;
            margin: 0;
        }

        .upload-container {
            max-width: 650px;
            margin: 20px auto 40px;
            background: #fff;
            border-radius: var(--border-radius);
            padding: 30px;
            box-shadow: var(--shadow);
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: var(--pink-primary);
            font-weight: 700;
            position: relative;
            padding-bottom: 10px;
        }

        h2::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background: linear-gradient(to right, var(--pink-primary), var(--mint-primary));
            border-radius: 2px;
        }

        label {
            font-weight: 600;
            display: block;
            margin-top: 15px;
            color: var(--text-dark);
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="file"],
        select,
        textarea {
            width: 100%;
            padding: 12px;
            margin-top: 5px;
            border: 1px solid #e1e4e8;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        input[type="text"]:focus,
        select:focus,
        textarea:focus {
            outline: none;
            border-color: var(--mint-primary);
            box-shadow: 0 0 0 3px rgba(160, 229, 215, 0.2);
        }

        input[type="file"] {
            padding: 10px;
            background-color: #f8f9fa;
            cursor: pointer;
            border: 1px dashed #ced4da;
        }

        input[type="file"]:hover {
            background-color: #f1f3f5;
        }

        select {
            appearance: none;
            background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg xmlns='http://www.w3.org/2000/svg' width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='%23555' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 15px center;
            background-size: 14px;
        }

        textarea {
            resize: vertical;
            min-height: 100px;
        }

        button {
            margin-top: 25px;
            width: 100%;
            background: linear-gradient(45deg, var(--pink-primary), var(--mint-primary));
            color: white;
            padding: 14px;
            border: none;
            border-radius: 50px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 10px rgba(240, 161, 184, 0.3);
        }

        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(240, 161, 184, 0.4);
            background: linear-gradient(45deg, var(--mint-primary), var(--pink-primary));
        }

        button:active {
            transform: translateY(0);
        }

        button:disabled {
            background: #cccccc;
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }

        .back-link-doc {
            text-align: center;
            margin-top: 20px;
        }

        .back-link-doc a,
        div a {
            text-decoration: none;
            color: var(--pink-primary);
            font-weight: 600;
            display: inline-block;
            padding: 8px 16px;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .back-link-doc a:hover,
        div a:hover {
            background-color: var(--pink-light);
            color: var(--text-dark);
        }

        .error-message {
            color: #dc3545;
            font-size: 14px;
            margin-top: 5px;
            padding: 8px;
            background-color: rgba(220, 53, 69, 0.1);
            border-radius: 6px;
            border-left: 3px solid #dc3545;
        }

        .file-info {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }

        .form-group {
            margin-bottom: 15px;
            position: relative;
        }

        .form-group i {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--mint-dark);
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .upload-container {
            animation: fadeIn 0.6s ease forwards;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .upload-container {
                max-width: 95%;
                padding: 20px;
            }

            h2 {
                font-size: 1.5rem;
            }

            button {
                padding: 12px;
            }
        }    </style>
</head>

<body>

<c:import url="header.jsp"/>

<div class="upload-container">
    <h2><i class="fas fa-file-upload"></i> Upload Tài Liệu Mới</h2>

    <c:if test="${not empty errorMessage}">
        <div class="error-message" style="display: block;">
            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
        </div>
    </c:if>

    <form action="upload-document" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="title"><i class="fas fa-heading"></i> Tiêu đề:</label>
            <input type="text" id="title" name="title" placeholder="Nhập tiêu đề tài liệu" required>
        </div>

        <div class="form-group">
            <label for="author"><i class="fas fa-user-edit"></i> Tác giả:</label>
            <input type="text" id="author" name="author" placeholder="Nhập tên tác giả" required>
        </div>

        <div class="form-group">
            <label for="type"><i class="fas fa-tag"></i> Loại tài liệu:</label>
            <input type="text" id="type" name="type" list="typeOptions" placeholder="Nhập hoặc chọn loại tài liệu..." required>

            <datalist id="typeOptions">
                <c:forEach var="t" items="${types}">
                    <option value="${t}"></option>
                </c:forEach>
            </datalist>
        </div>

        <div class="form-group">
            <label for="content"><i class="fas fa-align-left"></i> Mô tả nội dung tài liệu:</label>
            <textarea id="content" name="content" rows="4" placeholder="Mô tả ngắn về nội dung tài liệu"></textarea>
        </div>

        <div class="form-group">
            <label for="file"><i class="fas fa-file-pdf"></i> File tài liệu (PDF):</label>
            <input type="file" id="file" name="file" accept=".pdf" required>
            <div class="file-info"></div>
        </div>

        <div class="form-group">
            <label for="audio"><i class="fas fa-headphones"></i> File audio (tuỳ chọn):</label>
            <input type="file" id="audio" name="audio" accept="audio/*">
            <div class="file-info"></div>
        </div>

        <div class="form-group">
            <label for="thumbnail"><i class="fas fa-image"></i> Ảnh đại diện (thumbnail):</label>
            <input type="file" id="thumbnail" name="thumbnail" accept="image/*">
            <div class="file-info"></div>
        </div>

        <button type="submit"><i class="fas fa-cloud-upload-alt"></i> Tải lên</button>
    </form>

    <div class="back-link-doc">
        <a href="documents"><i class="fas fa-arrow-left"></i> Quay lại danh sách tài liệu</a>
    </div>
</div>

<c:import url="footer.jsp"/>

</body>
</html>
