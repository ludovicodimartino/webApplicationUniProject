<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Circuit</title>
</head>
<body>
<h1>Add New Circuit</h1>
<form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCircuit/" method="POST">

    <label for="name">Name:</label><br>
    <input type="text" id="name" name="name" required><br><br>

    <label for="address">Address:</label><br>
    <input type="text" id="address" name="address" required><br><br>


    <label for="description">Description:</label><br>
    <textarea id="description" name="description" required></textarea><br><br>

    <label for="length">Length:</label><br>
    <input type="number" id="length" name="length" required><br><br>

    <label for="cornersNumber">Number of corners:</label><br>
    <input type="number" id="cornersNumber" name="cornersNumber" required><br><br>

    <label for="lapPrice">Lap price:</label><br>
    <input type="number" id="lapPrice" name="lapPrice" required><br><br>

    <label for="availability">Availability:</label><br>
    <select id="availability" name="availability" required>
        <option value="true">True</option>
        <option value="false">False</option>
    </select><br><br>

    <label for="image">Choose the circuit image:</label><br>
    <input type="file" id="image" name="image" accept="image/png, image/jpeg, .jpg, .jpeg, .png" /><br><br>


    <label for="type">Type:</label><br>

    <%--    Display the circuit types--%>
    <jsp:useBean id="circuitList" scope="request" type="java.util.List"/>
    <c:choose>
        <c:when test="${empty circuitList}">
            <p>You don't have circuit types in the database. Please, create one <a
                    href="${pageContext.request.contextPath}/admin/insertCircuitType/">here</a>.</p>
            <input type="submit" value="Submit" disabled>
        </c:when>
        <c:otherwise>
            <select id="type" name="type" required>
                <c:forEach items="${circuitList}" var="circuit" varStatus="loop">
                    <option value="${circuit.name}"><c:out value="${circuit.name}"/></option>
                </c:forEach>
            </select>
            <p>You don't find the circuit type you are looking for? Create one <a
                    href="${pageContext.request.contextPath}/admin/insertCircuitType/">here</a>.</p><br><br>
            <input type="submit" value="Submit">
        </c:otherwise>
    </c:choose>

</form>
</body>
</html>