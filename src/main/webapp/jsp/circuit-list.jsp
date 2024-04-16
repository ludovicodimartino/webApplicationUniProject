<%@ page import="it.unipd.dei.webapp.wacar.resource.Car" %>
<%@ page import="java.util.List" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Circuits</title>
</head>
<body>
<h2>List of Circuits</h2>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Type</th>
        <th>Length</th>
        <th>Corners Number</th>
        <th>Address</th>
        <th>Description</th>
        <th>Lap Price</th>
        <th>Available</th>
        <th>Image</th>
    </tr>
    <c:if test="${not empty circuits}">
        <c:forEach var="circuit" items="${circuits}">
            <tr>
                <td><c:out value="${circuit.name}"/></td>
                <td><c:out value="${circuit.type}"/></td>
                <td><c:out value="${circuit.length}"/></td>
                <td><c:out value="${circuit.cornersNumber}"/></td>
                <td><c:out value="${circuit.address}"/></td>
                <td><c:out value="${circuit.description}"/></td>
                <td><c:out value="${circuit.lapPrice}"/></td>
                <td><c:out value="${circuit.available}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${circuit.hasPhoto()}">
                        <td>
                            <img src="<c:url value="/loadCircuitImage"><c:param name="name" value="${circuit.name}"/><c:param name="type" value="${circuit.type}"/></c:url>" alt="circuit image"/>
                        </td>
                        </c:when>
                    <c:otherwise>
                        <!-- Define behavior when the car doesn't have a photo -->
                        <td>No photo available</td>
                    </c:otherwise>
                </c:choose>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</table>
</body>
</html>
