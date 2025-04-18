<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 4/17/2025
  Time: 8:46 PM
  To change this template use File | Settings | File Templates.
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
      transition: background-color 0.3s;
    }

    .filter-list button:hover {
      background-color: #0056b3;
    }

    .filter-list button.active {
      background-color: #28a745;
    }

    .honour-container {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;
      padding: 20px;
    }

    .honour-card {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      padding: 15px;
      text-align: center;
      transition: transform 0.2s;
    }

    .honour-card:hover {
      transform: translateY(-5px);
    }

    .honour-card img {
      width: 200px;
      height: 200px;
      object-fit: cover;
      border-radius: 5px;
      filter: grayscale(100%);
      transition: filter 0.3s;
    }

    .honour-card img:hover {
      filter: grayscale(0%);
    }

    .honour-card h3 {
      font-size: 1.2em;
      margin: 10px 0;
      background: linear-gradient(45deg, #007bff, #28a745);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      transition: background 0.3s;
    }

    .honour-card:hover h3 {
      background: linear-gradient(45deg, #6f42c1, #e83e8c);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .honour-card p {
      font-size: 0.9em;
      color: #666;
    }

    .hidden {
      display: none;
    }

    .no-results {
      text-align: center;
      color: #666;
      grid-column: 1 / -1;
    }

    .pagination {
      display: flex;
      justify-content: center;
      gap: 10px;
      margin-top: 20px;
    }

    .pagination button {
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 5px;
      background-color: white;
      color: #333;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    .pagination button:hover:not(.disabled) {
      background-color: #f0f0f0;
    }

    .pagination button.active {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }

    .pagination button.disabled {
      color: #ccc;
      cursor: not-allowed;
    }
  </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="page-container">
  <div class="main-container">
    <h1>Danh sách thành tựu</h1>
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
            <div class="honour-card hidden" data-type="${honour.honourType}" data-index="${status.index}">
              <img src="${honour.honourImg}" alt="${honour.honourName}" loading="lazy" />
              <h3>${honour.honourName}</h3>
              <p>Loại: ${honour.honourType}</p>
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
    console.log('Updating pagination, filter:', currentFilter, 'currentPage:', currentPage);
    const visibleCards = Array.from(cards).filter(card => {
      const type = card.getAttribute('data-type');
      return currentFilter === 'all' || type === currentFilter;
    });
    const totalItems = visibleCards.length;
    const totalPages = Math.ceil(totalItems / ITEMS_PER_PAGE);
    console.log('Total items:', totalItems, 'Total pages:', totalPages);

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
      console.log('Showing card index:', card.getAttribute('data-index'));
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
      currentPage = 1; // Reset to first page when filter changes
      updatePagination();
    });
  });

  // Initial pagination
  console.log('Initial card count:', cards.length);
  updatePagination();
</script>
</body>
</html>