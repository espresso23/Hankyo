<%--
  Created by IntelliJ IDEA.
  User: bearx
  Date: 3/5/2025
  Time: 9:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/header.css">
</head>
<body>  
<%
  if (session != null && session.getAttribute("user") != null) {
    request.getRequestDispatcher("/header-user.jsp").include(request, response);
  } else {
    request.getRequestDispatcher("/header-guest.jsp").include(request, response);
  }
%>

</body>
</html>
