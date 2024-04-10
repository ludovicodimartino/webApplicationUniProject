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
<form action="/wacar/admin/insertCar/" method="POST">
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

    <label for="type">Type:</label><br>
    <select id="type" name="type" required>
        <c:forEach items="${carList}"  var="car" varStatus="loop">
            <option value="${car.name}">${car.name}</option>
        </c:forEach>
    </select><br><br>

    <input type="submit" value="Submit">
</form>
</body>
</html>