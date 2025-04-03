<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 4/2/2025
  Time: 12:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Select Topic</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background-color: #f0f0f0;
      margin: 0;
    }
    .container {
      text-align: center;
      background-color: #fff;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.2);
    }
    h1 {
      color: #333;
    }
    select {
      padding: 10px;
      font-size: 16px;
      border-radius: 5px;
      margin-bottom: 20px;
      width: 200px;
    }
    button {
      padding: 10px 20px;
      font-size: 16px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    button:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Select a Topic</h1>
  <form action="quizlet" method="post">
    <select name="topic" required>
      <option value="" disabled selected>Choose a topic</option>
      <option value="Daily Life">Daily Life</option>
      <option value="Nature">Nature</option>
      <option value="Objects">Objects</option>
    </select>
    <!-- Thêm input hidden để gửi flashCardID -->
    <input type="hidden" name="flashCardID" value="1">
    <br>
    <button type="submit">Start Learning</button>
  </form>

</div>
</body>
</html>