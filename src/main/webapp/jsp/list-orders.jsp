<%--
  Created by IntelliJ IDEA.
  User: scapi
  Date: 15/04/2024
  Time: 11:50
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="it.unipd.dei.webapp.wacar.resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>List Orders</title>
</head>
<body>

<h2>List of Orders</h2>
<table border="1">
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
                            <a href="/wacar/user/rest/order/id/${order.id}" type="button">
                                Modify
                            </a>
                        </c:when>
                        <c:otherwise>
                            <button disabled>Modify</button>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</table>
</body>
</html>
