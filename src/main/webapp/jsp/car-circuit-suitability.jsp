<!--
    Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
    Version: 1.00
    Since: 1.00
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Car-Circuit Suitability</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        .removeBtn {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 5px 10px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            cursor: pointer;
            border-radius: 4px;
        }
    </style>
</head>

<body>
<h1>Car-Circuit Suitability</h1>
<hr/>
<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<h3>Associate Car Type with Circuit Type</h3>
<form id="cCSuitForm" method="POST" action="${pageContext.request.contextPath}/admin/insertMapping/">

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
</form>
<br><br>

<%--Show current associations--%>
<h3>Current Associations</h3>
<jsp:useBean id="cCSuitMap" scope="request" type="java.util.HashMap"/>
<c:choose>
<c:when test="${empty cCSuitMap}">
<p>You don't have associations between car and circuits at the moment.</p>
</c:when>
<c:otherwise>
<table id="associationTable">
    <thead>
    <tr>
        <th>Car Type</th>
        <th>Circuit Types</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody id="associationBody">
    <c:forEach items="${cCSuitMap}" var="cCSuit" varStatus="loop">
        <tr>
            <td rowspan="<c:out value="${fn:length(cCSuit.value)}"/>"><c:out value="${cCSuit.key}"/></td>
            <td><c:out value="${cCSuit.value[0]}"/></td>
            <td>
                <form name="f1" action="${pageContext.request.contextPath}/admin/deleteMapping/" method="POST">
                    <input type="hidden" name="carType" value="${cCSuit.key}"/>
                    <input type="hidden" name="circuitType" value="${cCSuit.value[0]}"/>
                    <input id="edit0" type="submit" name="delete" value="delete" class="removeBtn">
                </form>
            </td>
        </tr>
        <c:forEach items="${cCSuit.value}" var="circuitType" varStatus="loop">
            <c:if test="${loop.index > 0}">
                <tr>
                    <td><c:out value="${circuitType}"/></td>
                    <td>
                        <form name="f1" action="${pageContext.request.contextPath}/admin/deleteMapping/" method="POST">
                            <input type="hidden" name="carType" value="${cCSuit.key}"/>
                            <input type="hidden" name="circuitType" value="${circuitType}"/>
                            <input id="${loop.index}" type="submit" name="delete" value="delete" class="removeBtn">
                        </form>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </c:forEach>
    </tbody>
    </c:otherwise>
    </c:choose>
</table>
