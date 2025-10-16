<%@ page contentType="text/html;charset=UTF-8"%>
<%
  String ctx = request.getContextPath();
  response.sendRedirect(ctx + "/controller");
%>