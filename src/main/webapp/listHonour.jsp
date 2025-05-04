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
    :root {
      --primary-pink: #ff6b8b;       /* Màu hồng chủ đạo */
      --light-pink: #ffd6de;        /* Hồng nhạt */
      --primary-blue: #6cb4ee;      /* Xanh dương */
      --light-blue: #e6f2ff;        /* Xanh nhạt */
      --success-color: #4cc9a7;     /* Xanh lá */
      --text-dark: #3a3a3a;         /* Màu chữ tối */
      --text-light: #6c757d;        /* Màu chữ phụ */
      --bg-gradient: linear-gradient(135deg, #fff9fb 0%, #f0f8ff 100%);
    }

    body {
      font-family: 'Roboto', Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-image: url('asset/png/background/background-2.png');
      color: var(--text-dark);
      line-height: 1.6;
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
      max-width: 1000px;
      width: 100%;
      background-color: rgba(255, 255, 255, 0.95);
      border-radius: 12px;
      box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
      padding: 25px;
      backdrop-filter: blur(5px);
      border: 1px solid rgba(255, 255, 255, 0.2);
    }
    h1 {
      text-align: center;
      color: var(--primary-pink);
      margin-bottom: 1.5rem;
      font-weight: 700;
      position: relative;
      padding-bottom: 10px;
    }

    h1::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 80px;
      height: 3px;
      background: linear-gradient(90deg, var(--primary-pink), var(--primary-blue));
    }

    .message {
      text-align: center;
      padding: 12px;
      margin-bottom: 20px;
      border-radius: 8px;
      font-weight: 500;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
    }

    .message.success {
      background-color: rgba(76, 201, 167, 0.15);
      color: #155724;
      border-left: 4px solid var(--success-color);
    }

    .message.error {
      background-color: rgba(255, 107, 107, 0.15);
      color: #721c24;
      border-left: 4px solid var(--primary-pink);
    }

    .filter-list {
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      gap: 10px;
      margin-bottom: 25px;
    }

    .filter-list button {
      padding: 10px 20px;
      border: none;
      border-radius: 25px;
      background-color: white;
      color: var(--primary-pink);
      cursor: pointer;
      transition: all 0.3s;
      font-weight: 500;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
      border: 1px solid rgba(255, 107, 139, 0.3);
    }

    .filter-list button:hover {
      background-color: var(--light-pink);
      transform: translateY(-3px);
      box-shadow: 0 5px 10px rgba(255, 107, 139, 0.2);
    }

    .filter-list button.active {
      background: linear-gradient(135deg, var(--primary-pink), var(--primary-blue));
      color: white;
      box-shadow: 0 4px 10px rgba(255, 107, 139, 0.3);
    }

    .honour-container {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 25px;
      padding: 10px;
    }

    .honour-card {
      background: white;
      border-radius: 12px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
      padding: 20px;
      text-align: center;
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;
      border: 2px solid transparent;
    }

    .honour-card:hover {
      transform: translateY(-8px);
      box-shadow: 0 12px 25px rgba(0, 0, 0, 0.15);
    }

    .honour-card.equipped {
      border-color: var(--primary-pink);
      box-shadow: 0 0 25px rgba(255, 107, 139, 0.3);
      background: linear-gradient(135deg, #fff0f3, #f0f8ff);
    }

    .honour-card img {
      width: 140px;
      height: 140px;
      object-fit: cover;
      border-radius: 50%;
      transition: all 0.3s ease;
      margin-bottom: 15px;
      filter: grayscale(100%);
      border: 3px solid #f0f0f0;
    }

    .honour-card img.owned {
      filter: grayscale(0%);
      border-color: var(--light-blue);
    }

    .honour-card.equipped img {
      border-color: var(--primary-pink);
      box-shadow: 0 0 15px rgba(255, 107, 139, 0.4);
    }

    .honour-card h3 {
      font-size: 1.3em;
      margin: 15px 0;
      font-weight: 600;
      color: var(--text-dark);
      transition: all 0.3s;
    }

    /* Gradient khi đã sở hữu */
    <c:forEach var="honour" items="${listHonour}">
    .honour-card[data-honour-id="${honour.honourID}"].owned h3 {
      color: transparent;
      background: linear-gradient(45deg, ${honour.gradientStart != null ? honour.gradientStart : 'var(--primary-pink)'}, ${honour.gradientEnd != null ? honour.gradientEnd : 'var(--primary-blue)'});
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      font-weight: 700;
    }
    </c:forEach>

    .honour-card p {
      font-size: 0.95em;
      color: var(--text-light);
      margin-bottom: 15px;
    }

    .owned-badge {
      position: absolute;
      top: 15px;
      right: 15px;
      background: linear-gradient(135deg, var(--success-color), #20c997);
      color: white;
      padding: 4px 12px;
      border-radius: 15px;
      font-size: 0.85em;
      font-weight: bold;
      box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
    }

    .equip-btn {
      display: inline-block;
      padding: 10px 20px;
      margin-top: 10px;
      background: linear-gradient(135deg, var(--primary-pink), var(--primary-blue));
      color: white;
      border: none;
      border-radius: 25px;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 0.95em;
      font-weight: 500;
      box-shadow: 0 3px 8px rgba(255, 107, 139, 0.2);
    }

    .equip-btn:hover {
      transform: translateY(-3px);
      box-shadow: 0 5px 15px rgba(255, 107, 139, 0.3);
    }

    .equip-btn.equipped {
      background: linear-gradient(135deg, #ff4757, #ff6b6b);
    }

    .hidden {
      display: none;
    }

    .no-results {
      text-align: center;
      color: var(--text-light);
      grid-column: 1 / -1;
      padding: 30px;
      font-size: 1.2em;
    }

    .pagination {
      display: flex;
      justify-content: center;
      gap: 8px;
      margin-top: 30px;
      flex-wrap: wrap;
    }

    .pagination button {
      padding: 10px 15px;
      border: 1px solid rgba(255, 107, 139, 0.3);
      border-radius: 25px;
      background-color: white;
      color: var(--primary-pink);
      cursor: pointer;
      transition: all 0.3s;
      font-weight: 500;
    }

    .pagination button:hover:not(.disabled) {
      background-color: var(--light-pink);
      transform: translateY(-3px);
    }

    .pagination button.active {
      background: linear-gradient(135deg, var(--primary-pink), var(--primary-blue));
      color: white;
      border-color: transparent;
    }

    .pagination button.disabled {
      color: #ccc;
      cursor: not-allowed;
      transform: none !important;
      border-color: #eee;
    }

    /* Animation */
    @keyframes pulse {
      0% { transform: scale(1); }
      50% { transform: scale(1.05); }
      100% { transform: scale(1); }
    }

    .honour-card.equip-animation {
      animation: pulse 0.5s ease;
    }

    /* Responsive */
    @media (max-width: 768px) {
      .main-container {
        padding: 15px;
      }

      .honour-container {
        grid-template-columns: 1fr;
      }

      .honour-card {
        max-width: 350px;
        margin: 0 auto;
      }

      .filter-list {
        gap: 8px;
      }

      .filter-list button {
        padding: 8px 15px;
        font-size: 0.9em;
      }
    }

    /* Hiệu ứng khi load */
    @keyframes fadeInUp {
      from {
        opacity: 0;
        transform: translateY(20px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .honour-card {
      animation: fadeInUp 0.5s ease forwards;
      opacity: 0;
    }

    .honour-card:nth-child(1) { animation-delay: 0.1s; }
    .honour-card:nth-child(2) { animation-delay: 0.2s; }
    .honour-card:nth-child(3) { animation-delay: 0.3s; }
    .honour-card:nth-child(4) { animation-delay: 0.4s; }
    .honour-card:nth-child(5) { animation-delay: 0.5s; }
    .honour-card:nth-child(6) { animation-delay: 0.6s; }
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