<!--
    Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
    Version: 1.00
    Since: 1.00
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Car</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/.css">
</head>
<body>
<div class="container">
    <div class="form-container">
        <h1>Add New Car</h1>
        <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCar/" method="POST">
            <div class="input-group mb-3">
                <label for="brand" class="form-label">Brand:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input type="text" class="form-control" id="brand" name="brand" required>
            </div>
            <div class="input-group mb-3">
                <label for="model" class="form-label">Model:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input type="text" class="form-control" id="model" name="model" required>
            </div>
            <div class="input-group mb-3">
                <label for="description" class="form-label">Description:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <textarea class="form-control" id="description" name="description" required></textarea>
            </div>
            <div class="input-group mb-3">
                <label for="maxSpeed" class="form-label">Max Speed:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input type="number" class="form-control" id="maxSpeed" name="maxSpeed" required>
            </div>
            <div class="input-group mb-3">
                <label for="horsepower" class="form-label">Horsepower:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input type="number" class="form-control" id="horsepower" name="horsepower" required>
            </div>
            <div class="input-group mb-3">
                <label for="acceleration" class="form-label">0-100 (in seconds):</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input type="number" class="form-control" id="acceleration" name="acceleration" step=".01" required>
            </div>
            <div class="input-group mb-3">
                <label for="availability" class="form-label">Availability:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <select class="form-select" id="availability" name="availability" required>
                    <option value="true">True</option>
                    <option value="false">False</option>
                </select>
            </div>
            <div class="input-group mb-3">
                <label for="image" class="form-label">Choose the car image:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input type="file" class="form-control" id="image" name="image" accept="image/png, image/jpeg">
            </div>
            <div class="input-group mb-3">
                <label for="type" class="form-label">Type:</label>
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <select class="form-select" id="type" name="type" required>
                    <c:forEach items="${carList}" var="car" varStatus="loop">
                        <option value="${car.name}"><c:out value="${car.name}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="input-group mb-3">
                <p>You don't find the car type you are looking for? Create one <a href="${pageContext.request.contextPath}/admin/insertCarType/">here</a>.</p>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
</div>
</body>
</html>
