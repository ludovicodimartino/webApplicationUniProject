<%--
  Created by IntelliJ IDEA.
  User: aleleo
  Date: 23/04/24
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update your Account</title>

</head>
<body>

</body>
</html>


<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delete Favorite</title>
</head>
<body>
<h1>Delete Favorite</h1>
<%-- Assuming you have the necessary information in your session or request attributes --%>
<%
    String circuitName = (String) request.getAttribute("circuitName");
    String carModel = (String) request.getAttribute("carModel");
    String carBrand = (String) request.getAttribute("carBrand");
%>

<form action="DeleteFavouriteServlet" method="GET">
    <input type="hidden" name="circuitName" value="<%= circuitName %>">
    <input type="hidden" name="carModel" value="<%= carModel %>">
    <input type="hidden" name="carBrand" value="<%= carBrand %>">
    <input type="submit" value="Delete Favorite">
</form>

</body>
</html>