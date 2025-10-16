<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="sh_ub.server.Coordinates" %>
<%@ page import="sh_ub.server.Coordinates" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результат</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
<div class="wrapper">
    <h2>Результат проверки</h2>
    <%
        Coordinates result = (Coordinates) request.getAttribute("result");
        String error = (String) request.getAttribute("error");
    %>
    <div>
        <%
            if (error != null) {
        %>
            <div class="errors"><%= error %></div>
        <%
            } else if (result != null) {
        %>
            <p>Время: <%= result.getTime() %></p>
            <p>X: <%= result.getX() %></p>
            <p>Y: <%= result.getY() %></p>
            <p>R: <%= result.getR() %></p>
            <p>Попадание: <%= result.isHit() ? "Да" : "Нет" %></p>
        <%
            }
        %>
    </div>
    <p><a href="${pageContext.request.contextPath}/">Назад</a></p>
</div>
</body>
</html>


