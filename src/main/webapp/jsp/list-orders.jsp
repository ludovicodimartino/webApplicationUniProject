<%@ page import="it.unipd.dei.webapp.wacar.resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <title>List Orders</title>
</head>
<body>
<%@ include file="toolbar.jsp" %>


<div class="container">
    <h2>List of Orders</h2>
    <table class="table table-striped">
        <tbody>
        <tr>
            <th>Id</th>
            <th>Date</th>
            <th>Car Brand</th>
            <th>Car Model</th>
            <th>Circuit</th>
            <th>Created At</th>
            <th>Number of Laps</th>
            <th>Price</th>
            <th>Modify</th>
        </tr>
        <c:if test="${not empty orders}">
            <c:forEach var="order" items="${orders}" varStatus="loop">
                <tr>
                    <td><c:out value="${order.id}"/></td>
                    <td><c:out value="${order.date}"/></td>
                    <td><c:out value="${order.carBrand}"/></td>
                    <td><c:out value="${order.carModel}"/></td>
                    <td><c:out value="${order.circuit}"/></td>
                    <td><c:out value="${order.createdAt}"/></td>
                    <td><c:out value="${order.NLaps}"/></td>
                    <td><c:out value="${order.price}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${modifyAvailable[loop.index]}">
                                <form action="/wacar/user/update/${order.id}" method="get">
                                    <button class="btn btn-primary btn-sm" onclick="modifyOrder('${order.id}')">Modify order</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-primary btn-sm" disabled>Modify order</button>
                            </c:otherwise>
                        </c:choose>

                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
