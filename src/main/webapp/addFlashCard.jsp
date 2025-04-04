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

        body {
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 400px;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        input[type="text"] {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            outline: none;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus {
            border-color: #007bff;
        }

        button {
            padding: 10px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }

        .note {
            font-size: 14px;
            color: #666;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Thêm Flashcard Mới</h2>
    <form action="addFlashCard" method="post">
        <input type="text" id="topic" name="topic" placeholder="Nhập Topic" required />
        <input type="text" id="content" name="flashCards"
               placeholder="Nhập từ:nghĩa (VD: hello:xin chào;good:tốt)" required />
        <button type="submit">Thêm Flashcard</button>
    </form>
    <p class="note">Lưu ý: Nhập nhiều flashcard cách nhau bằng dấu ";", mỗi cặp theo cú pháp "từ:nghĩa".</p>
</div>
</body>
</html>