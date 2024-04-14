<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Car-Circuit Suitability</title>
</head>

<body>
<h1>Car-Circuit Suitability</h1>
<hr/>

<h3>Associate Car Type with Circuit Type</h3>
<form id="cCSuitForm">

    <%--    Car Types List--%>
    <label for="carType">Select Car Type:</label>
    <jsp:useBean id="carTypeList" scope="request" type="java.util.List"/>
    <c:choose>
        <c:when test="${empty carTypeList}">
            <p>You don't have car types in the database. Please, create one <a
                    href="${pageContext.request.contextPath}/admin/insertCarType">here</a>.</p>
        </c:when>
        <c:otherwise>
            <select id="carType" name="carType" required>
                <c:forEach items="${carTypeList}" var="car" varStatus="loop">
                    <option value="${car.name}"><c:out value="${car.name}"/></option>
                </c:forEach>
            </select>
        </c:otherwise>
    </c:choose>

    <%--    Circuit Types List--%>
    <label for="circuitType">Select Circuit Type:</label>
    <jsp:useBean id="circuitTypeList" scope="request" type="java.util.List"/>
    <c:choose>
        <c:when test="${empty circuitTypeList}">
            <p>You don't have circuit types in the database. Please, create one <a
                    href="${pageContext.request.contextPath}/admin/insertCircuitType">here</a>.</p>
        </c:when>
        <c:otherwise>
            <select id="circuitType" name="circuitType" required>
                <c:forEach items="${circuitTypeList}" var="circuit" varStatus="loop">
                    <option value="${circuit.name}"><c:out value="${circuit.name}"/></option>
                </c:forEach>
            </select>
        </c:otherwise>
    </c:choose>
    <input type="submit" value="Associate">
</form><br><br>

<%--Show current associations--%>
<h3>Current Associations</h3>
<jsp:useBean id="cCSuitList" scope="request" type="java.util.List"/>
<c:choose>
<c:when test="${empty cCSuitList}">
<p>You don't have associations between car and circuits at the moment.</p>
</c:when>
<c:otherwise>
<table id="associationTable">
    <thead>
    <tr>
        <th>Car Type</th>
        <th>Circuit Types</th>
    </tr>
    </thead>
    <tbody id="associationBody">
    <c:forEach items="${cCSuitList}" var="cCSuit" varStatus="loop">
        <tr>
            <td><c:out value="${cCSuit.carType}"/></td>
            <td><c:out value="${cCSuit.circuitType}"/></td>
        </tr>
    </c:forEach>
    </tbody>
    </c:otherwise>
    </c:choose>
</table>
