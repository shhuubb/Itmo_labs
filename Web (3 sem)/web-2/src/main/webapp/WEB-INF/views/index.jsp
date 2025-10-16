<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
        </h3>
        <table class="table" id="results">
            <thead>
            <tr>
                <th>Время</th>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Попадание</th>
                <th>Время скрипта, мс</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
</div>
</body>
</html>


