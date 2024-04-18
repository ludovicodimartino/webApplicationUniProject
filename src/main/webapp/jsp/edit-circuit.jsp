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
<jsp:useBean id="circuit" scope="request" type="it.unipd.dei.webapp.wacar.resource.Circuit"/>
<h1>Edit Circuit <c:out value="${circuit.name}"/></h1>
<form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/editCircuit/" method="POST">
    <input type="hidden" id="name" name="name" value="${circuit.name}" required>

    <label for="address">Address:</label><br>
    <input type="text" id="address" name="address" value="${circuit.address}" required><br><br>


    <label for="description">Description:</label><br>
    <textarea id="description" name="description" required><c:out value="${circuit.address}"/></textarea><br><br>

    <label for="length">Length:</label><br>
    <input type="number" id="length" name="length" value="${circuit.length}" required><br><br>

    <label for="cornersNumber">Number of corners:</label><br>
    <input type="number" id="cornersNumber" name="cornersNumber" value="${circuit.cornersNumber}" required><br><br>

    <label for="lapPrice">Lap price:</label><br>
    <input type="number" id="lapPrice" name="lapPrice" value="${circuit.lapPrice}" required><br><br>

    <label for="availability">Availability:</label><br>
    <select id="availability" name="availability" required>
        <c:choose>
            <c:when test="${circuit.available}">
                <option value="true" selected="selected">True</option>
                <option value="false">False</option>
            </c:when>
            <c:otherwise>
                <option value="true">True</option>
                <option value="false" selected="selected">False</option>
            </c:otherwise>
        </c:choose>
    </select><br><br>

    <label for="image">Choose the new circuit image (blank for not updating the image):</label><br>
    <input type="file" id="image" name="image" accept="image/png, image/jpeg, .jpg, .jpeg, .png" /><br><br>


    <label for="type">Type:</label><br>

    <%--    Display the circuit types--%>
    <jsp:useBean id="circuitTypeList" scope="request" type="java.util.List"/>
    <c:choose>
        <c:when test="${empty circuitTypeList}">
            <p>You don't have circuit types in the database. Please, create one <a
                    href="${pageContext.request.contextPath}/admin/insertCircuitType">here</a>.</p>
            <input type="submit" value="Submit" disabled>
        </c:when>
        <c:otherwise>
            <select id="type" name="type" required>
                <c:forEach items="${circuitTypeList}" var="circuitType" varStatus="loop">
                    <option value="${circuitType.name}"
                            <c:if test="${circuit.type == circuitType.name}">
                                selected="selected"
                            </c:if>
                    >
                        <c:out value="${circuitType.name}"/>
                    </option>
                </c:forEach>
            </select><br><br>
            <input type="submit" value="Submit">
        </c:otherwise>
    </c:choose>

</form>
</body>
</html>