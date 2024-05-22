<!--
Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
Version: 2.00
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
    <title>Add new Car</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>

<div class="container">
    <h1 class="mb-4 mt-4">Add new Car</h1>
    <div id="liveAlertPlaceholder"></div>
    <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCar/" method="POST"
          class="needs-validation" id="insertCarForm" novalidate>

        <div class="row mb-3">
            <div class="col-md-6">
                <img id="selectedImage" class="w-100 rounded mb-2"
                     src="${pageContext.request.contextPath}/images/carImagePlaceholder.png"
                     alt="selected car image"/>
                <div class="input-group mb-3">
                    <label class="form-label text-white" for="image"></label>
                    <input type="file" class="form-control rounded" id="image" name="image"
                           accept="image/png, image/jpeg"/>
                    <div class="invalid-feedback">
                        Please select a valid image. Only png and jpeg format are accepted.
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="row h-100">
                    <div class="form-floating px-1 mb-3">
                        <input type="text" class="form-control" placeholder="Brand" id="brand" name="brand" required>
                        <label for="brand">Brand</label>
                        <div class="invalid-feedback">
                            Please insert a valid brand name.
                        </div>
                    </div>
                    <div class="form-floating px-1 mb-3">
                        <input type="text" class="form-control" placeholder="Model" id="model" name="model" required>
                        <label for="model">Model</label>
                        <div class="invalid-feedback">
                            Please insert a valid model name.
                        </div>
                    </div>

                    <jsp:useBean id="carList" scope="request" type="java.util.List"/>
                    <div class="input-group px-1 mb-3">
                        <select id="carTypeSelect" class="form-select" name="type" required>
                            <option value="">Select car type</option>
                            <c:forEach items="${carList}" var="car" varStatus="loop">
                                <option value="${car.name}"><c:out value="${car.name}"/></option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal"
                                data-bs-target="#newCarTypeModal">Add new <b>car type</b>
                        </button>
                    </div>

                    <div class="input-group mb-3">
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
            <div class="invalid-feedback">
                Please provide a description.
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="maxSpeed" name="maxSpeed" min="20" max="500" required>
                    <label for="maxSpeed">Max Speed</label>
                    <div class="invalid-feedback">
                        Please insert a valid max speed.
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="horsepower" name="horsepower" min="10" max="5000"
                           required>
                    <label for="horsepower">Horsepower</label>
                    <div class="invalid-feedback">
                        Please insert a valid horsepower value.
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="acceleration" name="acceleration" step=".01" min="0"
                           max="500" required>
                    <label for="acceleration">0-100 (in seconds)</label>
                    <div class="invalid-feedback">
                        Please insert a valid 0-100 value.
                    </div>
                </div>
            </div>
        </div>


        <div class="d-grid gap-2 mx-auto mb-3">
            <input type="submit" class="btn btn-primary" value="Submit">
        </div>

    </form>

    <c:import url="/jsp/include/car-type-modal.jsp"/>

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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin-utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/insert-car-type.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/insert-car.js"></script>
</body>
</html>