<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String assignmentID = request.getParameter("assignmentID");
    String courseID = request.getParameter("courseID");
%>
<html>
<head>
    <title>Add Assignment Questions</title>
</head>
<body>
<h2>Add Questions to Assignment</h2>
<form action="AddQuestionServlet" method="post">
    <input type="hidden" name="assignmentID" value="<%= assignmentID %>">
    <input type="hidden" name="courseID" value="<%= courseID %>">

    <label>Question Text:</label>
    <input type="text" name="questionText" required>

    <label>Answer A:</label>
    <input type="text" name="answerA" required>
    <input type="radio" name="correctAnswer" value="A" required>

    <label>Answer B:</label>
    <input type="text" name="answerB" required>
    <input type="radio" name="correctAnswer" value="B" required>

    <label>Answer C:</label>
    <input type="text" name="answerC" required>
    <input type="radio" name="correctAnswer" value="C" required>

    <label>Answer D:</label>
    <input type="text" name="answerD" required>
    <input type="radio" name="correctAnswer" value="D" required>

    <button type="submit">Add Question</button>
</form>
</body>
</html>
