<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, model.Documentary" %>
<%
    List<Documentary> docs = (List<Documentary>) request.getAttribute("documents");
    int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
    int docsPerPage = 8;
    int totalPages = (int) Math.ceil((double) docs.size() / docsPerPage);

    int startIdx = (currentPage - 1) * docsPerPage;
    int endIdx = Math.min(startIdx + docsPerPage, docs.size());
    List<Documentary> pageDocs = docs.subList(startIdx, endIdx);

    String filterType = request.getParameter("filterType");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tài liệu học tiếng Hàn | Hankyo</title>
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
            --shadow: 0 3px 8px rgba(0, 0, 0, 0.06);
            --border-radius: 10px;
            --card-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-dark);
            line-height: 1.6;
            margin: 0;
            padding: 0;
        }

        .documents-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .page-title {
            text-align: center;
            color: var(--pink-primary);
            margin: 1.5rem 0;
            font-size: 2rem;
            font-weight: 700;
            position: relative;
            padding-bottom: 10px;
        }

        .page-title::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 100px;
            height: 3px;
            background: linear-gradient(to right, var(--pink-primary), var(--mint-primary));
            border-radius: 2px;
        }

        /* Filter styles */
        .filter-container {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 25px;
        }

        .filter-dropdown {
            padding: 10px 35px 10px 15px;
            font-size: 15px;
            border: 1px solid #e1e4e8;
            border-radius: 20px;
            appearance: none;
            background-color: white;
            background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg xmlns='http://www.w3.org/2000/svg' width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='%23555' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 15px center;
            background-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: var(--shadow);
        }

        .filter-dropdown:hover {
            border-color: var(--mint-primary);
        }

        .filter-dropdown:focus {
            outline: none;
            border-color: var(--mint-primary);
            box-shadow: 0 0 0 3px rgba(160, 229, 215, 0.2);
        }

        /* Document grid */
        .doc-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 25px;
            margin-bottom: 30px;
        }

        .doc-card {
            background-color: white;
            border-radius: var(--border-radius);
            overflow: hidden;
            box-shadow: var(--card-shadow);
            transition: all 0.3s ease;
            position: relative;
            display: flex;
            flex-direction: column;
        }

        .doc-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
        }

        .doc-thumbnail {
            width: 100%;
            height: 180px;
            object-fit: cover;
            transition: transform 0.5s ease;
        }

        .doc-card:hover .doc-thumbnail {
            transform: scale(1.05);
        }

        .doc-card-content {
            padding: 15px;
            flex-grow: 1;
        }

        .doc-title {
            font-weight: 600;
            font-size: 1rem;
            margin-top: 5px;
            color: var(--text-dark);
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            height: 48px;
        }

        .doc-author {
            color: var(--pink-primary);
            font-size: 0.85rem;
            margin-bottom: 5px;
        }

        .doc-actions {
            padding: 12px 15px;
            display: flex;
            justify-content: center;
            border-top: 1px solid rgba(0,0,0,0.05);
        }

        .doc-actions a {
            text-decoration: none;
            color: var(--mint-dark);
            font-size: 0.9rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .doc-actions a:hover {
            color: var(--pink-primary);
        }

        /* Empty state */
        .no-documents {
            grid-column: span 4;
            text-align: center;
            padding: 40px;
            background-color: white;
            border-radius: var(--border-radius);
            box-shadow: var(--shadow);
            color: var(--text-dark);
            font-size: 1.1rem;
        }

        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 30px;
            gap: 5px;
        }

        .pagination a, .pagination span {
            display: flex;
            align-items: center;
            justify-content: center;
            min-width: 35px;
            height: 35px;
            padding: 0 10px;
            border-radius: 50px;
            text-decoration: none;
            transition: all 0.3s ease;
            font-size: 0.9rem;
        }

        .pagination a {
            background-color: white;
            color: var(--text-dark);
            box-shadow: var(--shadow);
        }

        .pagination a:hover {
            background: linear-gradient(45deg, var(--pink-light), var(--mint-light));
            transform: translateY(-2px);
        }

        .pagination span.current {
            background: linear-gradient(45deg, var(--pink-primary), var(--mint-primary));
            color: white;
            font-weight: 600;
            box-shadow: 0 4px 10px rgba(240, 161, 184, 0.3);
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .doc-card {
            animation: fadeIn 0.6s ease forwards;
        }

        /* Responsive adjustments */
        @media (max-width: 1024px) {
            .doc-grid {
                grid-template-columns: repeat(3, 1fr);
            }

            .no-documents {
                grid-column: span 3;
            }
        }

        @media (max-width: 768px) {
            .doc-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .no-documents {
                grid-column: span 2;
            }

            .filter-container {
                justify-content: center;
            }
        }

        @media (max-width: 480px) {
            .doc-grid {
                grid-template-columns: 1fr;
            }

            .no-documents {
                grid-column: span 1;
            }

            .page-title {
                font-size: 1.5rem;
            }

            .pagination a, .pagination span {
                min-width: 30px;
                height: 30px;
            }
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>

<div class="documents-container">
    <h1 class="page-title"><i class="fas fa-book"></i> Tài liệu học tiếng Hàn</h1>

    <div class="filter-container">
        <form method="get" action="documents">
            <select name="filterType" onchange="this.form.submit()" class="filter-dropdown">
                <option value="">Tất cả loại tài liệu</option>
                <%
                    List<String> documentTypes = (List<String>) request.getAttribute("documentTypes");
                    for (String type : documentTypes) {
                %>
                <option value="<%= type %>"
                        <%= filterType != null && filterType.equals(type) ? "selected" : "" %>>
                    <%= type %>
                </option>
                <% } %>
            </select>
            <% if (filterType != null && !filterType.isEmpty()) { %>
            <input type="hidden" name="page" value="1">
            <% } %>
        </form>
    </div>

    <div class="doc-grid">
        <% if (pageDocs.isEmpty()) { %>
        <div class="no-documents">
            <i class="fas fa-search" style="font-size: 2rem; color: var(--pink-primary); margin-bottom: 15px;"></i>
            <p>Không có tài liệu nào được tìm thấy.</p>
        </div>
        <% } else { %>
        <% for (Documentary doc : pageDocs) { %>
        <div class="doc-card">
            <%-- Label VIP / Free --%>
            <div style="position: absolute; top: 0; left: 0;">
                <% if ("VIP Documents".equalsIgnoreCase(doc.getType())) { %>
                <div style="background: orange; color: white; padding: 5px 10px;
                        font-size: 12px; font-weight: bold; border-radius: 0 0 8px 0;">
                    VIP
                </div>
                <% } else { %>
                <div style="background: #00c897; color: white; padding: 5px 10px;
                        font-size: 12px; font-weight: bold; border-radius: 0 0 8px 0;">
                    Free
                </div>
                <% } %>
            </div>

            <a href="documents?docID=<%= doc.getDocID() %>" style="text-decoration: none; color: inherit;">
                <img src="<%= doc.getThumbnail() %>" alt="<%= doc.getTitle() %>" class="doc-thumbnail">
                <div class="doc-card-content">
                    <div class="doc-author"><i class="fas fa-user-edit"></i> <%= doc.getAuthor() %></div>
                    <div class="doc-title"><%= doc.getTitle() %></div>
                </div>
            </a>
            <div class="doc-actions">
                <a href="<%= doc.getSource() %>" download>
                    <i class="fas fa-download"></i> Tải xuống
                </a>
            </div>
        </div>
        <% } %>
        <% } %>
    </div>

    <% if (totalPages > 1) { %>
    <div class="pagination">
        <% if (currentPage > 1) { %>
        <a href="?page=<%= currentPage - 1 %><%= filterType != null && !filterType.isEmpty() ? "&filterType=" + filterType : "" %>">
            <i class="fas fa-chevron-left"></i>
        </a>
        <% } %>

        <%
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(totalPages, startPage + 4);
            if (endPage - startPage < 4) {
                startPage = Math.max(1, endPage - 4);
            }

            if (startPage > 1) { %>
        <a href="?page=1<%= filterType != null && !filterType.isEmpty() ? "&filterType=" + filterType : "" %>">1</a>
        <% if (startPage > 2) { %><span>...</span><% } %>
        <% } %>

        <% for (int i = startPage; i <= endPage; i++) { %>
        <% if (i == currentPage) { %>
        <span class="current"><%= i %></span>
        <% } else { %>
        <a href="?page=<%= i %><%= filterType != null && !filterType.isEmpty() ? "&filterType=" + filterType : "" %>"><%= i %></a>
        <% } %>
        <% } %>

        <% if (endPage < totalPages) { %>
        <% if (endPage < totalPages - 1) { %><span>...</span><% } %>
        <a href="?page=<%= totalPages %><%= filterType != null && !filterType.isEmpty() ? "&filterType=" + filterType : "" %>"><%= totalPages %></a>
        <% } %>

        <% if (currentPage < totalPages) { %>
        <a href="?page=<%= currentPage + 1 %><%= filterType != null && !filterType.isEmpty() ? "&filterType=" + filterType : "" %>">
            <i class="fas fa-chevron-right"></i>
        </a>
        <% } %>
    </div>
    <% } %>
</div>
<c:import url="footer.jsp"/>
</body>
</html>