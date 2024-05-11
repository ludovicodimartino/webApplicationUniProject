<!--
Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
Version: 1.00
Since: 1.00
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <title>Add New Car</title>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Add New Car</h1>
    <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCar/" method="POST">

        <div class="row mb-3">
            <div class="col-md-6">
                <img id="selectedImage" class="img-fluid rounded mb-2"
                     src="https://mdbootstrap.com/img/Photos/Others/placeholder.jpg"
                     alt="selected car image"/>
                <div class="input-group">
                    <label class="form-label text-white" for="image"></label>
                    <input type="file" class="form-control" id="image" name="image"
                           accept="image/png, image/jpeg"/>

                </div>
            </div>
            <div class="col-md-6">
                <div class="row gy-4">
                    <div class="form-floating px-1">
                        <input type="text" class="form-control" placeholder="Brand" id="brand" name="brand" required>
                        <label for="brand">Brand</label>
                    </div>
                    <div class="form-floating px-1">
                        <input type="text" class="form-control" placeholder="Model" id="model" name="model" required>
                        <label for="model">Model</label>
                    </div>

                    <jsp:useBean id="carList" scope="request" type="java.util.List"/>
                    <c:choose>
                        <c:when test="${empty carList}">
                            <p>You don't have car types in the database. Please, create one <a
                                    href="${pageContext.request.contextPath}/admin/insertCarType/">here</a>.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="form-floating px-1">
                                <select id="type" class="form-select" name="type" required>
                                    <option value="">Select car type</option>
                                    <c:forEach items="${carList}" var="car" varStatus="loop">
                                        <option value="${car.name}"><c:out value="${car.name}"/></option>
                                    </c:forEach>
                                </select>
                                <label for="type" class="form-label">Type</label>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="input-group">
                        <div class="input-check">
                            <input class="form-check-input" type="checkbox" value="true" id="availability"
                                   name="availability">
                            <label class="form-check-label" for="availability">
                                Available
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="form-floating mb-3">
            <textarea id="description" class="form-control" name="description"
                      placeholder="Brief description of the car" required></textarea>
            <label for="description">Description</label>
        </div>

        <div class="row mb-3">
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="maxSpeed" name="maxSpeed" required>
                    <label for="maxSpeed">Max Speed</label>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="horsepower" name="horsepower" required>
                    <label for="horsepower">Horsepower</label>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="acceleration" name="acceleration" step=".01" required>
                    <label for="acceleration">0-100 (in seconds)</label>
                </div>
            </div>
        </div>


        <div class="d-grid gap-2 mx-auto">
            <input type="submit" class="btn btn-primary" value="Submit">
        </div>


        <%--    <label for="availability">Availability:</label><br>--%>
        <%--    <select id="availability" name="availability" required>--%>
        <%--        <option value="true">True</option>--%>
        <%--        <option value="false">False</option>--%>
        <%--    </select><br><br>--%>


        <%--            <label for="type">Type:</label><br>--%>

        <%--            &lt;%&ndash;    Display the car types&ndash;%&gt;--%>
        <%--            <jsp:useBean id="carList" scope="request" type="java.util.List"/>--%>
        <%--            <c:choose>--%>
        <%--                <c:when test="${empty carList}">--%>
        <%--                    <p>You don't have car types in the database. Please, create one <a--%>
        <%--                            href="${pageContext.request.contextPath}/admin/insertCarType/">here</a>.</p>--%>
        <%--                    <input type="submit" value="Submit" disabled>--%>
        <%--                </c:when>--%>
        <%--                <c:otherwise>--%>
        <%--                    <select id="type" name="type" required>--%>
        <%--                        <c:forEach items="${carList}" var="car" varStatus="loop">--%>
        <%--                            <option value="${car.name}"><c:out value="${car.name}"/></option>--%>
        <%--                        </c:forEach>--%>
        <%--                    </select>--%>
        <%--                    <p>You don't find the car type you are looking for? Create one <a--%>
        <%--                            href="${pageContext.request.contextPath}/admin/insertCarType/">here</a>.</p><br><br>--%>
        <%--                    <input type="submit" value="Submit">--%>
        <%--                </c:otherwise>--%>
        <%--            </c:choose>--%>

    </form>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/insert-car.js"></script>
</body>
</html>