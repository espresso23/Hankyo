<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 4/17/2025
  Time: 8:46 PM
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh sách thành tựu</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Roboto', Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f4f4f4;
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }

    .page-container {
      flex: 1;
      display: flex;
      justify-content: center;
      padding: 20px;
    }

    .main-container {
      max-width: 900px;
      width: 100%;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      padding: 20px;
    }

    header, footer {
      background-color: #333;
      color: white;
      padding: 10px 20px;
      text-align: center;
    }

    header {
      position: sticky;
      top: 0;
      z-index: 1000;
    }

    footer {
      margin-top: auto;
    }

    h1 {
      text-align: center;
      color: #333;
    }

    .message {
      text-align: center;
      padding: 10px;
      margin-bottom: 15px;
      border-radius: 5px;
    }

    .message.success {
      background-color: #d4edda;
      color: #155724;
    }

    .message.error {
      background-color: #f8d7da;
      color: #721c24;
    }

    .filter-list {
      display: flex;
      justify-content: center;
      gap: 10px;
      margin-bottom: 20px;
    }

    .filter-list button {
      padding: 8px 16px;
      border: none;
      border-radius: 5px;
      background-color: #007bff;
      color: white;
      cursor: pointer;
      transition: all 0.3s;
    }

    .filter-list button:hover {
      background-color: #0056b3;
      transform: translateY(-2px);
    }

    .filter-list button.active {
      background-color: #28a745;
    }

    .honour-container {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 20px;
      padding: 20px;
      position: relative;
    }

    .honour-card {
      background: linear-gradient(135deg, #ffffff, #f8f9fa);
      border-radius: 10px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      padding: 20px;
      text-align: center;
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;
      border: 2px solid transparent;
    }

    .honour-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
    }

    .honour-card.equipped {
      border-color: gold;
      box-shadow: 0 0 20px rgba(255, 215, 0, 0.5);
      background: linear-gradient(135deg, #fff9e6, #fff0b3);
    }

    .honour-card img {
      width: 120px;
      height: 120px;
      object-fit: cover;
      transition: all 0.3s ease;
      margin-bottom: 15px;
      filter: grayscale(100%);
    }

    .honour-card img.owned {
      filter: grayscale(0%);
    }

    .honour-card.equipped img {
      box-shadow: 0 0 10px rgba(255, 215, 0, 0.7);
    }

    .honour-card h3 {
      font-size: 1.2em;
      margin: 10px 0;
      font-weight: 600;
      color: #333;
      transition: all 0.3s;
    }

    /* Gradient khi đã sở hữu */
    <c:forEach var="honour" items="${listHonour}">
    .honour-card[data-honour-id="${honour.honourID}"].owned h3 {
      color: transparent;
      background: linear-gradient(45deg, ${honour.gradientStart != null ? honour.gradientStart : '#28a745'}, ${honour.gradientEnd != null ? honour.gradientEnd : '#20c997'});
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    </c:forEach>

    .honour-card p {
      font-size: 0.9em;
      color: #666;
      margin-bottom: 15px;
    }

    .owned-badge {
      position: absolute;
      top: 10px;
      right: 10px;
      background-color: #28a745;
      color: white;
      padding: 3px 8px;
      border-radius: 10px;
      font-size: 0.8em;
      font-weight: bold;
      box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }

    .equip-btn {
      display: inline-block;
      padding: 8px 15px;
      margin-top: 10px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 0.9em;
      font-weight: 500;
    }

    .equip-btn:hover {
      background-color: #3e8e41;
      transform: translateY(-2px);
    }

    .equip-btn.equipped {
      background-color: #f44336;
    }

    .equip-btn.equipped:hover {
      background-color: #d32f2f;
    }

    .hidden {
      display: none;
    }

    .no-results {
      text-align: center;
      color: #666;
      grid-column: 1 / -1;
      padding: 20px;
      font-size: 1.1em;
    }

    .pagination {
      display: flex;
      justify-content: center;
      gap: 10px;
      margin-top: 20px;
      flex-wrap: wrap;
    }

    .pagination button {
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 5px;
      background-color: white;
      color: #333;
      cursor: pointer;
      transition: all 0.3s;
    }

    .pagination button:hover:not(.disabled) {
      background-color: #f0f0f0;
      transform: translateY(-2px);
    }

    .pagination button.active {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }

    .pagination button.disabled {
      color: #ccc;
      cursor: not-allowed;
      transform: none !important;
    }

    /* Animation for equip/unequip */
    @keyframes pulse {
      0% { transform: scale(1); }
      50% { transform: scale(1.05); }
      100% { transform: scale(1); }
    }

    .honour-card.equip-animation {
      animation: pulse 0.5s ease;
    }
  </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="page-container">
  <div class="main-container">
    <h1>Danh sách thành tựu</h1>
    <!-- Hiển thị thông báo -->
    <c:if test="${not empty successMessage}">
      <div class="message success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="message error">${errorMessage}</div>
    </c:if>
    <div class="filter-list">
      <button class="filter-btn active" data-type="all">Tất cả</button>
      <c:forEach var="type" items="${uniqueTypes}">
        <button class="filter-btn" data-type="${type}">${type}</button>
      </c:forEach>
    </div>
    <div class="honour-container">
      <c:choose>
        <c:when test="${empty listHonour}">
          <p class="no-results">Chưa có thành tựu nào.</p>
        </c:when>
        <c:otherwise>
          <c:forEach var="honour" items="${listHonour}" varStatus="status">
            <div class="honour-card hidden ${honourOwnedMap[honour.honourID] ? 'owned' : ''} ${equippedHonourID == honour.honourID ? 'equipped' : ''}"
                 data-type="${honour.honourType}" data-index="${status.index}"
                 data-honour-id="${honour.honourID}">
              <img src="${honour.honourImg}" alt="${honour.honourName}" loading="lazy"
                   class="${honourOwnedMap[honour.honourID] ? 'owned' : ''}" />
              <h3>${honour.honourName}</h3>
              <p>Loại: ${honour.honourType}</p>
              <c:if test="${honourOwnedMap[honour.honourID]}">
                <p class="owned-badge">Đã đạt được</p>
                <form action="${pageContext.request.contextPath}/equipHonour" method="post" style="display: inline;">
                  <input type="hidden" name="honourID" value="${honour.honourID}">
                  <input type="hidden" name="action" value="${equippedHonourID == honour.honourID ? 'unequip' : 'equip'}">
                  <button type="submit" class="equip-btn ${equippedHonourID == honour.honourID ? 'equipped' : ''}">
                      ${equippedHonourID == honour.honourID ? 'Bỏ trang bị' : 'Trang bị'}
                  </button>
                </form>
              </c:if>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="pagination" id="pagination"></div>
  </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
<script>
  const ITEMS_PER_PAGE = 6;
  const cards = document.querySelectorAll('.honour-card');
  const pagination = document.getElementById('pagination');
  let currentPage = 1;
  let currentFilter = 'all';

  function updatePagination() {
    const visibleCards = Array.from(cards).filter(card => {
      const type = card.getAttribute('data-type');
      return currentFilter === 'all' || type === currentFilter;
    });
    const totalItems = visibleCards.length;
    const totalPages = Math.ceil(totalItems / ITEMS_PER_PAGE);

    // Clear pagination
    pagination.innerHTML = '';

    // Handle no results
    const container = document.querySelector('.honour-container');
    const noResults = container.querySelector('.no-results');
    if (noResults) noResults.remove();
    if (totalItems === 0) {
      const noResultsMsg = document.createElement('p');
      noResultsMsg.className = 'no-results';
      noResultsMsg.textContent = 'Không có thành tựu nào cho loại này.';
      container.appendChild(noResultsMsg);
      return;
    }

    // Ensure currentPage is valid
    if (currentPage > totalPages) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;

    // Hide all cards
    cards.forEach(card => card.classList.add('hidden'));

    // Show cards for the current page
    const start = (currentPage - 1) * ITEMS_PER_PAGE;
    const end = Math.min(start + ITEMS_PER_PAGE, totalItems);
    visibleCards.slice(start, end).forEach(card => {
      card.classList.remove('hidden');
    });

    // Generate pagination buttons
    if (totalPages > 1) {
      // Previous button
      const prevBtn = document.createElement('button');
      prevBtn.textContent = '« Trước';
      prevBtn.className = currentPage === 1 ? 'disabled' : '';
      prevBtn.disabled = currentPage === 1;
      prevBtn.addEventListener('click', () => {
        if (currentPage > 1) {
          currentPage--;
          updatePagination();
        }
      });
      pagination.appendChild(prevBtn);

      // Page numbers
      for (let i = 1; i <= totalPages; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.textContent = i;
        pageBtn.className = i === currentPage ? 'active' : '';
        pageBtn.addEventListener('click', () => {
          currentPage = i;
          updatePagination();
        });
        pagination.appendChild(pageBtn);
      }

      // Next button
      const nextBtn = document.createElement('button');
      nextBtn.textContent = 'Sau »';
      nextBtn.className = currentPage === totalPages ? 'disabled' : '';
      nextBtn.disabled = currentPage === totalPages;
      nextBtn.addEventListener('click', () => {
        if (currentPage < totalPages) {
          currentPage++;
          updatePagination();
        }
      });
      pagination.appendChild(nextBtn);
    }
  }

  // Filter handling
  document.querySelectorAll('.filter-btn').forEach(button => {
    button.addEventListener('click', () => {
      document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
      button.classList.add('active');
      currentFilter = button.getAttribute('data-type');
      currentPage = 1;
      updatePagination();
    });
  });

  // Initial pagination
  updatePagination();
</script>
</body>
</html>