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
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tài liệu học tiếng Hàn</title>
    <link rel="stylesheet" href="asset/css/documentlist.css">
</head>
<body>
<c:import url="header.jsp"/>

<div class="documents-container">
    <h1 class="page-title">Tài liệu học tiếng Hàn</h1>
    <div class="filter-container">
        <form method="get" action="documents">
            <select name="filterType" onchange="this.form.submit()" class="filter-dropdown">
                <option value="">Tất cả loại tài liệu</option>
                <%
                    List<String> documentTypes = (List<String>) request.getAttribute("documentTypes");
                    for (String type : documentTypes) {
                %>
                <option value="<%= type %>"
                        <%= request.getParameter("filterType") != null &&
                                request.getParameter("filterType").equals(type) ? "selected" : "" %>>
                    <%= type %>
                </option>
                <% } %>
            </select>
        </form>
    </div>
    <div class="doc-grid">
        <% if (pageDocs.isEmpty()) { %>
        <div class="no-documents">Không có tài liệu nào được tìm thấy.</div>
        <% } else { %>
        <% for (Documentary doc : pageDocs) { %>
        <div class="doc-card">
            <img src="${pageContext.request.contextPath}<%= doc.getThumbnail() %>"
                 alt="thumbnail" class="doc-thumbnail">
            <div class="doc-card-content">
                <div class="doc-author"><%= doc.getAuthor() %></div>
                <div class="doc-title"><%= doc.getTitle() %></div>
                <div class="doc-actions">
                    <a href="${pageContext.request.contextPath}<%= doc.getSource() %>" download>
                        <span>📥</span> Tải xuống
                    </a>
                    <a href="documents?docID=<%= doc.getDocID() %>">
                        <span>👁</span> Xem
                    </a>
                </div>
            </div>
        </div>
        <% } %>
        <% } %>
    </div>

    <% if (totalPages > 1) { %>
    <div class="pagination">
        <% if (currentPage > 1) { %>
        <a href="?page=<%= currentPage - 1 %>">&laquo; Trước</a>
        <% } %>

        <% for (int i = 1; i <= totalPages; i++) { %>
        <% if (i == currentPage) { %>
        <span class="current"><%= i %></span>
        <% } else { %>
        <a href="?page=<%= i %>"><%= i %></a>
        <% } %>
        <% } %>

        <% if (currentPage < totalPages) { %>
        <a href="?page=<%= currentPage + 1 %>">Tiếp &raquo;</a>
        <% } %>
    </div>
    <% } %>
</div>
<c:import url="footer.jsp"/>
</body>
</html>