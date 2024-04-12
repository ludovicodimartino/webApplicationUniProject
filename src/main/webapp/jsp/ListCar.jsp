<%@ page import="it.unipd.dei.webapp.wacar.resource.Car" %>
<%@ page import="java.util.List" %>
<!-- displayCars.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Cars</title>
</head>
<body>
<h2>List of Cars</h2>
<table border="1">
    <tr>
        <th>Brand</th>
        <th>Model</th>
        <th>Description</th>
        <th>Max Speed</th>
        <th>Horsepower</th>
        <th>0-100</th>
        <th>Available</th>
        <th>Type</th>
        <th>Image</th>
    </tr>
    <c:if test="${not empty cars}">
        <c:forEach var="car" items="${cars}">
            <tr>
                <td><c:out value="${car.brand}"/></td>
                <td><c:out value="${car.model}"/></td>
                <td><c:out value="${car.description}"/></td>
                <td><c:out value="${car.maxSpeed}"/></td>
                <td><c:out value="${car.horsepower}"/></td>
                <td><c:out value="${car.acceleration}"/></td>
                <td><c:out value="${car.available}"/></td>
                <td><c:out value="${car.type}"/></td>
            </tr>
        </c:forEach>
    </c:if>
</table>
</body>
</html>


