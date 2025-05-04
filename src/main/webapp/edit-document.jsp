<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa tài liệu | Hankyo</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root {
            --pink-primary: #f0a1b8;
            --pink-light: #f7d6e0;
            --mint-primary: #a0e5d7;
            --mint-dark: #88c7ba;
            --mint-light: #d0f5ee;
            --text-dark: #4a5568;
            --text-light: #ffffff;
            --bg-color: #f8f9fa;
            --shadow: 0 3px 8px rgba(0, 0, 0, 0.06);
            --border-radius: 10px;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-dark);
            line-height: 1.6;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background: white;
            border-radius: var(--border-radius);
            box-shadow: var(--shadow);
        }

        .page-title {
            text-align: center;
            color: var(--pink-primary);
            margin-bottom: 2rem;
            font-size: 2rem;
            font-weight: 700;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: var(--text-dark);
        }

        input[type="text"],
        input[type="file"],
        select,
        textarea {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #e1e4e8;
            border-radius: 5px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        input[type="text"]:focus,
        select:focus,
        textarea:focus {
            outline: none;
            border-color: var(--mint-primary);
            box-shadow: 0 0 0 3px rgba(160, 229, 215, 0.2);
        }

        textarea {
            min-height: 150px;
            resize: vertical;
        }

        .btn-group {
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
            margin-top: 2rem;
        }

        .btn {
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 5px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--pink-primary), var(--mint-primary));
            color: white;
        }

        .btn-secondary {
            background: #e1e4e8;
            color: var(--text-dark);
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .alert {
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1rem;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .current-files {
            margin-top: 0.5rem;
            font-size: 0.9rem;
            color: #666;
        }

        .current-files i {
            margin-right: 0.5rem;
            color: var(--mint-dark);
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="container">
    <h1 class="page-title"><i class="fas fa-edit"></i> Chỉnh sửa tài liệu</h1>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">
            ${sessionScope.message}
            <% session.removeAttribute("message"); %>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
            <% session.removeAttribute("error"); %>
        </div>
    </c:if>

    <form action="edit-document" method="post" enctype="multipart/form-data">
        <input type="hidden" name="docID" value="${document.docID}">

        <div class="form-group">
            <label for="title">Tiêu đề tài liệu</label>
            <input type="text" id="title" name="title" value="${document.title}" required>
        </div>

        <div class="form-group">
            <label for="author">Tác giả</label>
            <input type="text" id="author" name="author" value="${document.author}" required>
        </div>

        <div class="form-group">
            <label for="type">Loại tài liệu</label>
            <select id="type" name="type" required>
                <c:forEach items="${types}" var="type">
                    <option value="${type}" ${document.type eq type ? 'selected' : ''}>${type}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="content">Nội dung</label>
            <textarea id="content" name="content">${document.docContent}</textarea>
        </div>

        <div class="form-group">
            <label for="file">File tài liệu</label>
            <input type="file" id="file" name="file">
            <div class="current-files">
                <i class="fas fa-file"></i> File hiện tại: ${document.source}
            </div>
        </div>

        <div class="form-group">
            <label for="audio">File âm thanh</label>
            <input type="file" id="audio" name="audio">
            <div class="current-files">
                <i class="fas fa-music"></i> File âm thanh hiện tại: ${document.audioPath}
            </div>
        </div>

        <div class="form-group">
            <label for="thumbnail">Hình ảnh thumbnail</label>
            <input type="file" id="thumbnail" name="thumbnail">
            <div class="current-files">
                <i class="fas fa-image"></i> Thumbnail hiện tại: ${document.thumbnail}
            </div>
        </div>

        <div class="btn-group">
            <a href="documents" class="btn btn-secondary">Hủy</a>
            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
        </div>
    </form>
</div>

<c:import url="footer.jsp"/>
</body>
</html> 