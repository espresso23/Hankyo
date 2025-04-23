<%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 4/10/2025
  Time: 11:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Chọn kỹ năng và thời gian làm bài</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #faf0f5; /* Màu hồng pastel nhạt */
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #4a4a4a;
    }

    .container {
      width: 100%;
      max-width: 650px;
      margin: 20px;
      background: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 5px 20px rgba(216, 27, 96, 0.1); /* Shadow hồng nhạt */
      transition: transform 0.3s ease;
    }

    .container:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 25px rgba(216, 27, 96, 0.15);
    }

    .title {
      text-align: center;
      color: #d81b60; /* Màu hồng đậm */
      margin-bottom: 35px;
      font-size: 28px;
      font-weight: 600;
      position: relative;
      padding-bottom: 15px;
    }

    .title::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 80px;
      height: 4px;
      background: linear-gradient(90deg, #b3e5fc 0%, #81d4fa 100%); /* Gradient xanh pastel */
      border-radius: 2px;
    }

    .section {
      margin-bottom: 30px;
      padding: 25px;
      background: #fff;
      border-radius: 12px;
      border: 1px solid #fce4ec; /* Viền hồng nhạt */
      box-shadow: 0 3px 10px rgba(248, 187, 208, 0.1);
    }

    .section-title {
      font-size: 18px;
      color: #0277bd; /* Màu xanh đậm */
      margin-bottom: 20px;
      font-weight: 600;
      display: flex;
      align-items: center;
    }

    .section-title::before {
      content: '';
      display: inline-block;
      width: 6px;
      height: 20px;
      background: #81d4fa; /* Xanh pastel */
      margin-right: 10px;
      border-radius: 3px;
    }

    .skill-options {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 15px;
      margin: 20px 0;
    }

    .skill-option {
      padding: 16px;
      background: white;
      border: 2px solid #e1f5fe; /* Xanh pastel nhạt */
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-size: 16px;
      font-weight: 500;
      text-align: center;
      color: #0277bd; /* Xanh đậm */
      box-shadow: 0 2px 5px rgba(179, 229, 252, 0.2);
    }

    .skill-option:hover {
      transform: translateY(-3px);
      box-shadow: 0 5px 15px rgba(179, 229, 252, 0.3);
      border-color: #81d4fa;
    }

    .skill-option.selected {
      background: linear-gradient(135deg, #81d4fa 0%, #4fc3f7 100%); /* Gradient xanh pastel */
      color: white;
      border-color: transparent;
      box-shadow: 0 5px 15px rgba(129, 212, 250, 0.4);
    }

    .time-input {
      width: 100%;
      padding: 14px 20px;
      border: 2px solid #e1f5fe;
      border-radius: 8px;
      font-size: 16px;
      margin-top: 10px;
      transition: all 0.3s;
      background: white;
      color: #0277bd;
    }

    .time-input:focus {
      outline: none;
      border-color: #4fc3f7;
      box-shadow: 0 0 0 3px rgba(129, 212, 250, 0.3);
    }

    .time-note {
      color: #90a4ae;
      font-size: 14px;
      margin-top: 8px;
    }

    .start-btn {
      display: block;
      width: 100%;
      padding: 18px;
      background: linear-gradient(135deg, #f8bbd0 0%, #f48fb1 100%); /* Gradient hồng pastel */
      color: white;
      text-align: center;
      text-decoration: none;
      border-radius: 8px;
      margin-top: 30px;
      font-size: 18px;
      font-weight: 600;
      transition: all 0.3s ease;
      border: none;
      cursor: pointer;
      box-shadow: 0 4px 10px rgba(244, 143, 177, 0.3);
    }

    .start-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 7px 15px rgba(244, 143, 177, 0.4);
      background: linear-gradient(135deg, #f48fb1 0%, #f06292 100%);
    }

    .start-btn:disabled {
      background: linear-gradient(135deg, #e0e0e0 0%, #bdbdbd 100%);
      cursor: not-allowed;
      transform: none;
      box-shadow: none;
    }

    /* Responsive */
    @media (max-width: 640px) {
      .container {
        padding: 30px 20px;
      }

      .skill-options {
        grid-template-columns: 1fr;
      }

      .section {
        padding: 20px 15px;
      }
    }
  </style>
</head>
<body>
<div class="container">
  <h1 class="title">Chọn kỹ năng và thời gian làm bài</h1>

  <div class="section">
    <h3 class="section-title">Chọn loại câu hỏi</h3>
    <div class="skill-options">
      <div class="skill-option" data-type="Listening">Listening</div>
      <div class="skill-option" data-type="Reading">Reading</div>
      <div class="skill-option" data-type="Full">Full Test</div>
    </div>
  </div>

  <div class="section">
    <h3 class="section-title">Thời gian làm bài (phút)</h3>
    <input type="number" class="time-input" id="timeInput" min="1" placeholder="Nhập thời gian">
    <p class="time-note">Để trống để sử dụng thời gian mặc định</p>
  </div>

  <button class="start-btn" id="startBtn" disabled>Bắt đầu làm bài</button>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const skillOptions = document.querySelectorAll('.skill-option');
    const startBtn = document.getElementById('startBtn');
    const timeInput = document.getElementById('timeInput');
    let selectedType = null;

    skillOptions.forEach(option => {
      option.addEventListener('click', function() {
        // Remove selected class from all options
        skillOptions.forEach(opt => opt.classList.remove('selected'));
        // Add selected class to clicked option
        this.classList.add('selected');
        selectedType = this.getAttribute('data-type');
        updateStartButton();
      });
    });

    function updateStartButton() {
      if (selectedType) {
        startBtn.disabled = false;
        const examId = '<%= request.getParameter("examID") %>';
        const time = timeInput.value ? timeInput.value : '';

        startBtn.onclick = function() {
          window.location.href = 'exam?action=do&examID=' + examId + '&eQuesType=' + selectedType + '&time=' + time;
        };
      } else {
        startBtn.disabled = true;
      }
    }

    timeInput.addEventListener('input', function() {
      if (this.value < 1) {
        this.value = 1;
      }
      updateStartButton();
    });
  });
</script>
</body>
</html>
