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
    <title>Add new Circuit</title>
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">
    <h1 class="mb-4 mt-4">Add new Circuit</h1>
    <div id="liveAlertPlaceholder"></div>
    <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/insertCircuit/" method="POST"
          class="needs-validation" id="insertCircuitForm" novalidate>

        <div class="row mb-3">
            <div class="col-md-6">
                <img id="selectedImage" class="w-100 rounded mb-2"
                     src="${pageContext.request.contextPath}/images/circuitImagePlaceholder.png"
                     alt="selected circuit image"/>
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
                        <input type="text" class="form-control" placeholder="Name" id="name" name="name" required>
                        <label for="name">Name</label>
                        <div class="invalid-feedback">
                            Please insert a valid circuit name.
                        </div>
                    </div>
                    <div class="form-floating px-1 mb-3">
                        <input type="text" class="form-control" placeholder="Address" id="address" name="address"
                               required>
                        <label for="address">Address</label>
                        <div class="invalid-feedback">
                            Please insert a valid address.
                        </div>
                    </div>

                    <jsp:useBean id="circuitList" scope="request" type="java.util.List"/>
                    <div class="input-group px-1 mb-3">
                        <select id="circuitTypeSelect" class="form-select" name="type" required>
                            <option value="">Select circuit type</option>
                            <c:forEach items="${circuitList}" var="circuit" varStatus="loop">
                                <option value="${circuit.name}"><c:out value="${circuit.name}"/></option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal"
                                data-bs-target="#newCircuitTypeModal">Add new <b>circuit type</b>
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
                      placeholder="Brief description of the circuit" required></textarea>
            <label for="description">Description</label>
            <div class="invalid-feedback">
                Please provide a description.
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="length" name="length" min="1" max="50000" required>
                    <label for="length">Length</label>
                    <div class="invalid-feedback">
                        Please insert a valid circuit length.
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="cornersNumber" name="cornersNumber" min="1" max="200"
                           required>
                    <label for="cornersNumber">Corners number</label>
                    <div class="invalid-feedback">
                        Please insert a valid corners number value.
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="form-floating">
                    <input type="number" class="form-control" id="lapPrice" name="lapPrice" min="1"
                           max="500" required>
                    <label for="lapPrice">Lap Price</label>
                    <div class="invalid-feedback">
                        Please insert a valid lap price value.
                    </div>
                </div>
            </div>
        </div>


        <div class="d-grid gap-2 mx-auto mb-3">
            <input type="submit" class="btn btn-primary" value="Submit">
        </div>

    </form>

    <c:import url="/jsp/include/circuit-type-modal.jsp"/>

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
        <script type="text/javascript" src="<c:url value="/js/admin-utils.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/insert-circuit-type.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/insert-circuit.js"/>"></script>
</body>
<%@ include file="footer.jsp" %>
</html>