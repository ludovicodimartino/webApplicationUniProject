<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Car</title>
</head>
<body>
<h1>Add New Car</h1>
<form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCar/" method="POST">
    <label for="brand">Brand:</label><br>
    <input type="text" id="brand" name="brand" required><br><br>

    <label for="model">Model:</label><br>
    <input type="text" id="model" name="model" required><br><br>


    <label for="description">Description:</label><br>
    <textarea id="description" name="description" required></textarea><br><br>

    <label for="maxSpeed">Max Speed:</label><br>
    <input type="number" id="maxSpeed" name="maxSpeed" required><br><br>

    <label for="horsepower">Horsepower:</label><br>
    <input type="number" id="horsepower" name="horsepower" required><br><br>

    <label for="acceleration">0-100 (in seconds):</label><br>
    <input type="number" id="acceleration" name="acceleration" required><br><br>

    <label for="availability">Availability:</label><br>
    <select id="availability" name="availability" required>
        <option value="true">True</option>
        <option value="false">False</option>
    </select><br><br>

    <label for="image">Choose the car image:</label><br>
    <input type="file" id="image" name="image" accept="image/png, image/jpeg" /><br><br>


    <label for="type">Type:</label><br>

    <%--    Display the car types--%>
    <jsp:useBean id="carList" scope="request" type="java.util.List"/>
    <c:choose>
        <c:when test="${empty carList}">
            <p>You don't have car types in the database. Please, create one <a
                    href="${pageContext.request.contextPath}/admin/insertCarType">here</a>.</p>
            <input type="submit" value="Submit" disabled>
        </c:when>
        <c:otherwise>
            <select id="type" name="type" required>
                <c:forEach items="${carList}" var="car" varStatus="loop">
                    <option value="${car.name}"><c:out value="${car.name}"/></option>
                </c:forEach>
            </select><br><br>
            <input type="submit" value="Submit">
        </c:otherwise>
    </c:choose>

</form>
</body>
</html>