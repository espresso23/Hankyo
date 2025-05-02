<%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 4/3/2025
  Time: 9:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrance Test - Study4</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }
        body {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: url("./asset/png/background/background.png") no-repeat center center/cover;
        }

        .title {
            font-size: 40px;
            font-weight: bold;
            color: #2C3EAA;
            margin-bottom: 15px;
            text-align: center;
        }
        .image-container img {
            max-width: 400px;
            height: auto;
            transition: transform 0.3s;
        }
        .image-container img:hover {
            transform: scale(1.1);
        }
        .start-btn {
            position: absolute;
            /*top: 50%; !* Cần dòng này để căn giữa theo chiều dọc *!*/
            left: 50%;
            transform: translate(-50%, -50%);
            display: inline-block;
            margin-top: 20px; /* Nếu cần cách hình phía trên thì để lại */
            padding: 14px 36px;
            font-size: 18px;
            font-weight: bold;
            color: white;
            background: linear-gradient(135deg, #3F51B5, #1A237E);
            border: none;
            border-radius: 25px;
            cursor: pointer;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.3);
        }

        .start-btn:hover {
            background: linear-gradient(135deg, #303F9F, #0D47A1);
            transform: translate(-50%, -50%) scale(1.05); /* Thêm translate để không bị lệch khi hover */
            box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.4);
        }

        .start-btn:active {
            transform: translate(-50%, -50%) scale(0.95); /* Giữ căn giữa khi bấm */
            box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.2);
        }
        .overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            justify-content: center;
            align-items: center;
            animation: fadeIn 0.3s ease-in-out;
        }
        .overlay-content {
            background: white;
            padding: 25px;
            border-radius: 12px;
            text-align: center;
            position: relative;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
            animation: scaleUp 0.3s ease-in-out;
        }
        .close-btn {
            position: absolute;
            top: 12px;
            right: 15px;
            font-size: 22px;
            cursor: pointer;
            color: #333;
            transition: color 0.3s;
        }
        .close-btn:hover {
            color: #FF0000;
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @keyframes scaleUp {
            from { transform: scale(0.8); }
            to { transform: scale(1); }
        }
        .test-options {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 30px;
        }

        .test-card {
            flex: 1;
            max-width: 400px;
            background: #E8F0FE;
            padding: 30px;
            border-radius: 20px;
            text-align: center;
            box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.15);
            transition: transform 0.3s ease-in-out;
        }

        .test-card:hover {
            transform: translateY(-8px);
        }

        .test-card h3 {
            color: #333;
            margin-bottom: 15px;
            font-size: 24px;
        }

        .test-card p {
            color: #666;
            margin-bottom: 20px;
            line-height: 1.6;
        }

        .test-btn {
            background: #3F51B5;
            color: white;
            padding: 15px 25px;
            font-size: 18px;
            border: none;
            border-radius: 12px;
            cursor: pointer;
            width: 100%;
            transition: background 0.3s ease-in-out;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .test-btn:hover {
            background: #303F9F;
        }

        /*.test-btn::after {*/
        /*    content: "→";*/
        /*    font-size: 20px;*/
        /*}*/

    </style>
    <script>
        function showOverlay() {
            document.getElementById("examOverlay").style.display = "flex";
        }
        function hideOverlay() {
            document.getElementById("examOverlay").style.display = "none";
        }

        function startExam(examID) {
            // Mặc định 60 phút cho entrance test
            window.location.href = 'exam?action=getQuestions&examID=' + examID + '&timeLimit=60';
        }
    </script>
</head>
<body>
<div class="container">
    <div class="title">Level Test</div>
    <div class="image-container">
        <img src= "./asset/png/let-try.png" alt="Level Test Image">
    </div>
    <button class="start-btn" onclick="showOverlay()">Bắt đầu kiểm tra</button>
</div>

<div class="overlay" id="examOverlay">
    <div class="overlay-content">
        <span class="close-btn" onclick="hideOverlay()">&times;</span>
        <h2>Chọn Level Test</h2>
        <p>Chọn bài kiểm tra phù hợp để đánh giá trình độ của bạn.</p>

        <div class="test-options">
            <div class="test-card">
                <h3>TOPIK I</h3>
                <p>Phù hợp với người mới học tiếng Hàn, gồm các câu hỏi về từ vựng, ngữ pháp và nghe hiểu.</p>
                <button class="test-btn" onclick="window.location.href='exam?action=do&examID=1&eQuesType=Full&time=60'">Bắt đầu kiểm tra</button>
            </div>
            <div class="test-card">
                <h3>TOPIK II</h3>
                <p>Dành cho người có trình độ trung cấp và cao cấp, bao gồm đọc hiểu, viết luận và nghe hiểu.</p>
                <button class="test-btn" onclick="window.location.href='exam?action=do&examID=2&eQuesType=Full&time=60'">Bắt đầu kiểm tra</button>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
