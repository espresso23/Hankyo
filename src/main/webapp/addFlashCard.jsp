<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thêm Flashcard Mới</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        /* Container Styling */
        .container {
            background-color: #ffffff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 450px; /* Slightly wider for better spacing */
            margin: 20px auto; /* Center horizontally with margin for spacing */
            position: relative;
        }

        /* Heading */
        h2 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 25px;
            font-size: 24px;
            font-weight: 600;
        }

        /* Form Styling */
        form {
            display: flex;
            flex-direction: column;
            gap: 18px; /* Slightly larger gap for better separation */
        }

        /* Input Fields */
        input[type="text"] {
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 16px;
            outline: none;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            background-color: #f9f9f9;
        }

        input[type="text"]:focus {
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }

        input[type="text"]::placeholder {
            color: #999;
        }

        /* Buttons */
        button {
            padding: 12px;
            background-color: #3498db;
            color: #ffffff;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }

        button:hover {
            background-color: #2980b9;
            transform: translateY(-2px); /* Subtle lift effect */
        }

        button:active {
            transform: translateY(0); /* Reset on click */
        }

        /* Toggle Button */
        .toggle-btn {
            background-color: #2ecc71;
            margin-bottom: 15px;
            font-size: 14px;
            padding: 10px;
        }

        .toggle-btn:hover {
            background-color: #27ae60;
        }

        /* Input Mode Containers */
        .manual-input, .individual-input {
            display: none;
        }

        .manual-input.active, .individual-input.active {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }

        /* Note Text */
        .note {
            font-size: 13px;
            color: #7f8c8d;
            text-align: center;
            margin-top: 15px;
            line-height: 1.4;
        }
    </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
    <h2>Thêm Flashcard Mới</h2>
    <button class="toggle-btn" onclick="toggleInputMode()">Chuyển sang nhập từng ô</button>
    <form id="flashcardForm" action="addFlashCard" method="post">
        <!-- Manual Input -->
        <div class="manual-input active">
            <input type="text" id="manualTopic" name="topic" placeholder="Nhập Topic" required />
            <input type="text" id="manualFlashCards" name="flashCards"
                   placeholder="Nhập từ:nghĩa (VD: hello:xin chào;good:tốt)" required />
        </div>

        <!-- Individual Input -->
        <div class="individual-input">
            <input type="text" id="individualTopic" name="individualTopic" placeholder="Nhập Topic" />
            <input type="text" id="word" name="word" placeholder="Nhập từ" />
            <input type="text" id="mean" name="mean" placeholder="Nhập nghĩa" />
        </div>

        <button type="submit">Thêm Flashcard</button>
    </form>
    <p class="note">Lưu ý: Nhập nhiều flashcard cách nhau bằng dấu ";", mỗi cặp theo cú pháp "từ:nghĩa" (cho chế độ thủ công).</p>
</div>

<script>
    let isManualMode = true;

    function toggleInputMode() {
        const manualInput = document.querySelector('.manual-input');
        const individualInput = document.querySelector('.individual-input');
        const toggleBtn = document.querySelector('.toggle-btn');

        if (isManualMode) {
            manualInput.classList.remove('active');
            individualInput.classList.add('active');
            toggleBtn.textContent = 'Chuyển sang nhập thủ công';
            document.getElementById('manualTopic').removeAttribute('required');
            document.getElementById('manualFlashCards').removeAttribute('required');
            document.getElementById('individualTopic').setAttribute('required', '');
            document.getElementById('word').setAttribute('required', '');
            document.getElementById('mean').setAttribute('required', '');
        } else {
            manualInput.classList.add('active');
            individualInput.classList.remove('active');
            toggleBtn.textContent = 'Chuyển sang nhập từng ô';
            document.getElementById('manualTopic').setAttribute('required', '');
            document.getElementById('manualFlashCards').setAttribute('required', '');
            document.getElementById('individualTopic').removeAttribute('required');
            document.getElementById('word').removeAttribute('required');
            document.getElementById('mean').removeAttribute('required');
        }
        isManualMode = !isManualMode;
    }
</script>
</body>
<jsp:include page="footer.jsp"></jsp:include>
</html>