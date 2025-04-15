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
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 20px;
      background-color: #f5f5f5;
    }

    .container {
      max-width: 600px;
      margin: 50px auto;
      background: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .title {
      text-align: center;
      color: #333;
      margin-bottom: 30px;
    }

    .section {
      margin-bottom: 25px;
      padding: 20px;
      background: #f9f9f9;
      border-radius: 8px;
    }

    .section-title {
      font-size: 18px;
      color: #333;
      margin-bottom: 15px;
    }

    .skill-options {
      display: flex;
      gap: 15px;
      margin: 15px 0;
    }

    .skill-option {
      padding: 12px 24px;
      background: #fff;
      border: 2px solid #ddd;
      border-radius: 5px;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 16px;
      flex: 1;
      text-align: center;
    }

    .skill-option:hover {
      border-color: #4CAF50;
    }

    .skill-option.selected {
      background: #4CAF50;
      color: white;
      border-color: #4CAF50;
    }

    .time-input {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 16px;
      margin-top: 10px;
    }

    .time-note {
      color: #666;
      font-size: 14px;
      margin-top: 5px;
    }

    .start-btn {
      display: block;
      width: 100%;
      padding: 15px;
      background: #4CAF50;
      color: white;
      text-align: center;
      text-decoration: none;
      border-radius: 5px;
      margin-top: 20px;
      font-size: 18px;
      transition: background 0.3s;
      border: none;
      cursor: pointer;
    }

    .start-btn:hover {
      background: #45a049;
    }

    .start-btn:disabled {
      background: #cccccc;
      cursor: not-allowed;
    }
  </style>
</head>
<body>
<div class="container">
  <h1 class="title">Chọn kỹ năng và thời gian làm bài</h1>

  <div class="section">
    <h3 class="section-title">Chọn kỹ năng làm bài</h3>
    <div class="skill-options">
      <div class="skill-option" data-skill="listening">Listening</div>
      <div class="skill-option" data-skill="reading">Reading</div>
      <div class="skill-option" data-skill="full">Full Test</div>
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
    let selectedSkill = null;

    skillOptions.forEach(option => {
      option.addEventListener('click', function() {
        // Remove selected class from all options
        skillOptions.forEach(opt => opt.classList.remove('selected'));
        // Add selected class to clicked option
        this.classList.add('selected');
        selectedSkill = this.getAttribute('data-skill');
        updateStartButton();
      });
    });

    function updateStartButton() {
      if (selectedSkill) {
        startBtn.disabled = false;
        const examId = '<%= request.getParameter("examID") %>';
        const time = timeInput.value ? timeInput.value : '';

        startBtn.onclick = function() {
          window.location.href = 'exam?action=do&examID=' + examId + '&skill=' + selectedSkill + '&time=' + time;
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
