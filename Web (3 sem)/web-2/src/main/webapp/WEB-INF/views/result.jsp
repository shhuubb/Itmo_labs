<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="sh_ub.server.Coordinates" %>
<%@ page import="sh_ub.server.Coordinates" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результат</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <script src="${pageContext.request.contextPath}/static/result.js" defer></script>
</head>
<body>
<div class="wrapper" style="max-width: 880px;">
    <div class="header" style="margin-bottom:16px;">
        <h1 style="margin:0 0 6px 0; font-size:22px;">Результат проверки</h1>
        <div class="meta">ИТМО Веб-2 • Проверка попадания точки</div>
    </div>
    <%
        Coordinates result = (Coordinates) request.getAttribute("result");
        String error = (String) request.getAttribute("error");
    %>
    <div class="card">
        <%
            if (error != null) {
        %>
            <div class="errors"><div class="err"><%= error %></div></div>
        <%
            } else if (result != null) {
        %>
            <div style="display:grid; gap:14px;">
                <div style="display:grid; grid-template-columns: 1fr 1fr; gap:12px;">
                    <div class="card" style="padding:14px;">
                        <div style="color:#cbd5e1; font-weight:700;">Координаты</div>
                        <div style="margin-top:8px; color:#e5e7eb;">X: <b><%= result.getX() %></b></div>
                        <div style="color:#e5e7eb;">Y: <b><%= result.getY() %></b></div>
                        <div style="color:#e5e7eb;">R: <b><%= result.getR() %></b></div>
                    </div>
                    <div class="card" style="padding:14px; display:grid; grid-template-rows:auto 1fr; gap:10px;">
                        <div style="display:flex; align-items:center; justify-content:center;">
                            <span class="badge <%= result.isHit() ? "" : "fail" %>"><%= result.isHit() ? "Попадание: Да" : "Попадание: Нет" %></span>
                        </div>
                        <canvas id="result-plot" width="460" height="360" aria-label="Область и точка" style="width:100%; background:#0b1324; border-radius:12px; border:1px solid rgba(148,163,184,.18);"
                                data-x="<%= result.getX() %>" data-y="<%= result.getY() %>" data-r="<%= result.getR() %>" data-hit="<%= result.isHit() %>"></canvas>
                    </div>
                </div>
                <div class="card" style="padding:14px;">
                    <div style="color:#cbd5e1; font-weight:700;">Время</div>
                    <div style="margin-top:8px; color:#e5e7eb;"><%= result.getTime() %></div>
                </div>
                <div>
                    <a class="btn" href="${pageContext.request.contextPath}/" style="text-decoration:none;">Новый запрос</a>
                </div>
            </div>
        <%
            }
        %>
    </div>
    
</div>
</div>
<script></script>
</body>
</html>



