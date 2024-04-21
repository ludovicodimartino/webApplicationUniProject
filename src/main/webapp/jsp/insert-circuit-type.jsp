<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Circuit Type</title>
</head>
<body>
<h1>Circuit Types</h1>
<hr/>
<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>
<h2>Add new circuit type</h2>
<form action="${pageContext.request.contextPath}/admin/insertCircuitType/" method="POST">
    <label for="name">Name:</label><br>
    <input type="text" id="name" name="name" required><br><br>
    <input type="submit" value="Add">
</form>
<br><br>

<%--Show tables of circuit types --%>
<h2>Circuit types</h2>

<jsp:useBean id="circuitTypesList" scope="request" type="java.util.List"/>
<c:choose>
    <c:when test="${empty circuitTypesList}">
        <p>You don't have circuit types at the moment.</p>
    </c:when>
    <c:otherwise>
        <table id="circuitTypesTable">
            <thead>
            <tr>
                <th>Circuit Type</th>
            </tr>
            </thead>
            <tbody id="circuitTypesTableBody">
            <c:forEach items="${circuitTypesList}" var="circuitType" varStatus="loop">
                <tr>
                    <td><c:out value="${circuitType.name}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>