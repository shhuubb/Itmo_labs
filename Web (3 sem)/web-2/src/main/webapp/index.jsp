<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>ИТМО Веб-2 — Проверка попадания точки</title>
    <% long v = System.currentTimeMillis(); %>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/images/img.png?v=<%= v %>">
    <link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath}/static/images/img.png?v=<%= v %>">
    <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/static/images/img.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <script src="${pageContext.request.contextPath}/static/index.js" defer></script>
</head>
<body>
<div class="wrapper">
    <div class="header">
        <h1>Лабораторная работа №2 — Веб</h1>
        <div class="meta">Студент: <b>Шубин Егор Вячеславович</b> Группа: <b>P3209</b> Вариант: <b>468125</b></div>
    </div>

    <div class="card canvas-box">
        <canvas id="plot" width="540" height="480" aria-label="Область допустимых значений"></canvas>
    </div>

    <div class="card">
        <h3>Параметры</h3>
        <form id="point-form" class="form" method="get" action="${pageContext.request.contextPath}/controller">
            <div id="errors" class="errors" aria-live="polite" role="alert" style="display:none"></div>
            <div class="field">
                <label for="x">X (−3 … 5)</label>
                <input id="x" name="x" type="text" placeholder="Например, 1.25" maxlength="10" inputmode="decimal" autocomplete="off" />
            </div>

            <div class="field">
                <label>Y</label>
                <div class="controls" id="y-controls"></div>
            </div>

            <div class="field">
                <label for="r">R (2 … 5)</label>
                <input id="r" name="r" type="text" placeholder="Например, 2.5" maxlength="10" inputmode="decimal" autocomplete="off" />
            </div>

            <button class="submit" type="submit">Проверить</button>
        </form>
    </div>

    <div class="card" style="grid-column: 1 / -1;">
        <h3 style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
            <span>Результаты</span>
            <button id="open-results" class="btn ghost">Открыть таблицу</button>
        </h3>
        <p style="margin:8px 0 0; color: var(--muted);">История будет доступна в модальном окне.</p>
    </div>
</div>
<!-- Results Modal -->
<div id="results-modal" class="modal hidden" aria-hidden="true" role="dialog" aria-modal="true">
    <div class="modal-backdrop" data-close></div>
    <div class="modal-dialog" role="document">
        <div class="modal-header">
            <h3>История результатов</h3>
            <button class="modal-close" type="button" title="Закрыть" aria-label="Закрыть" data-close>✕</button>
        </div>
        <div class="modal-content">
            <table class="table" id="results-table">
                <thead>
                <tr>
                    <th>Время</th>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Попадание</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    </div>
<script>
  window.__CTX__ = '<%= request.getContextPath() %>';
</script>
<script id="initial-results" type="application/json">
[
<%
    @SuppressWarnings("unchecked")
    java.util.List<sh_ub.server.Coordinates> results = (java.util.List<sh_ub.server.Coordinates>) session.getAttribute("results");
    if (results != null) {
        for (int i = 0; i < results.size(); i++) {
            sh_ub.server.Coordinates c = results.get(i);
%>
  {"x": <%= c.getX() %>, "y": <%= c.getY() %>, "r": <%= c.getR() %>, "hit": <%= c.isHit() %>, "creationTime": "<%= c.getTime() %>" }<%= (i < results.size()-1) ? "," : "" %>
<%
        }
    }
%>
]
</script>
</body>
</html>



