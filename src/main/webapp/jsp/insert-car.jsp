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
    <title>Add new Car</title>
</head>
<body>



<div class="container">
    <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
        <symbol id="check-circle-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
        </symbol>
        <symbol id="info-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
        </symbol>
        <symbol id="exclamation-triangle-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
        </symbol>
    </svg>

    <h1 class="mb-4">Add new Car</h1>
    <div id="liveAlertPlaceholder"></div>
    <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCar/" method="POST">

        <div class="row mb-3">
            <div class="col-md-6">
                <img id="selectedImage" class="w-100 rounded mb-2"
                     src="https://mdbootstrap.com/img/Photos/Others/placeholder.jpg"
                     alt="selected car image"/>
                <div class="input-group mb-3">
                    <label class="form-label text-white" for="image"></label>
                    <input type="file" class="form-control" id="image" name="image"
                           accept="image/png, image/jpeg"/>

                </div>
            </div>
            <div class="col-md-6">
                <div class="row">
                    <div class="form-floating px-1 mb-3">
                        <input type="text" class="form-control" placeholder="Brand" id="brand" name="brand" required>
                        <label for="brand">Brand</label>
                    </div>
                    <div class="form-floating px-1 mb-3">
                        <input type="text" class="form-control" placeholder="Model" id="model" name="model" required>
                        <label for="model">Model</label>
                    </div>

                    <jsp:useBean id="carList" scope="request" type="java.util.List"/>
                    <div class="input-group px-1 mb-3">
                        <div class="form-floating">
                            <select id="type" class="form-select" name="type" required>
                                <option value="">Select car type</option>
                                <c:forEach items="${carList}" var="car" varStatus="loop">
                                    <option value="${car.name}"><c:out value="${car.name}"/></option>
                                </c:forEach>
                            </select>
                            <label for="type" class="form-label">Type</label>
                        </div>
                        <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal"
                                data-bs-target="#newCarTypeModal">Add new type
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


        <div class="d-grid gap-2 mx-auto mb-3">
            <input type="submit" class="btn btn-primary" value="Submit">
        </div>

    </form>


    <!-- Modal -->
    <div class="modal fade" id="newCarTypeModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">

            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="exampleModalLabel">New car type</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="carTypeForm" method="post">
                            <div class="form-floating mb-3">
                                <input type="text" class="form-control" id="typeName" name="name">
                                <label for="typeName">Car type name</label>
                            </div>
                            <input type="button" class="btn btn-primary" id="addTypeBtn" value="Add">
                        </form>
                    </div>
                </div>
            </div>
    </div>

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