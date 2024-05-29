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
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous">
    <title>Edit Circuit</title>
</head>

<body>

    <%@ include file="header.jsp" %>

        <div class="container">
            <jsp:useBean id="circuit" scope="request" type="it.unipd.dei.webapp.wacar.resource.Circuit" />

            <h1 class="mb-4 mt-4">Edit Circuit <c:out value="${circuit.name}" /></h1>
            <div id="liveAlertPlaceholder"></div>
            <form enctype="multipart/form-data" method="POST" class="needs-validation" id="editCircuitForm" novalidate>
                <input type="hidden" id="name" name="name" value="${circuit.name}">
                <div class="row mb-3">
                    <div class="col-md-6">
                        <img id="selectedImage" class="w-100 rounded mb-2"
                            src="<c:url value="/loadCircuitImage"><c:param name="circuitName" value="${circuit.name}"/></c:url>" alt="circuit image" />
                        <div class="input-group mb-3">
                            <label class="form-label text-white" for="image"></label>
                            <input type="file" class="form-control rounded" id="image" name="image"
                                accept="image/png, image/jpeg" />
                            <div class="invalid-feedback">
                                Please select a valid image. Only png and jpeg format are accepted.
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="row">
                            <div class="form-floating px-1 mb-3">
                                <input type="text" class="form-control" placeholder="Address" id="address" name="address" value="${circuit.address}" required>
                                <label for="address">Address</label>
                                <div class="invalid-feedback">
                                    Please insert a valid address.
                                </div>
                            </div>

                            <%--    Display the circuit types--%>
                            <jsp:useBean id="circuitTypeList" scope="request" type="java.util.List"/>
                            <div class="input-group px-1 mb-3">
                                <select id="circuitTypeSelect" class="form-select" name="type" required>
                                    <option value="">Select circuit type</option>
                                    <c:forEach items="${circuitTypeList}" var="circuitType" varStatus="loop">
                                        <option value="${circuitType.name}"
                                                <c:if test="${circuit.type == circuitType.name}">
                                                    selected="selected"
                                                </c:if>
                                        >
                                            <c:out value="${circuitType.name}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                                <button class="btn btn-outline-secondary" type="button"
                                    data-bs-toggle="modal" data-bs-target="#newCircuitTypeModal">Add new
                                    <b>circuit type</b>
                                </button>
                            </div>

                            <div class="input-group mb-3">
                                <div class="input-check">
                                    <input class="form-check-input" type="checkbox" value="true"
                                        id="availability" name="availability" 
                                        <c:if test="${circuit.available}">
                                                    checked
                                        </c:if>/>
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
                        placeholder="Brief description of the circuit" required><c:out value="${circuit.description}"/></textarea>
                    <label for="description">Description</label>
                    <div class="invalid-feedback">
                        Please provide a description.
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-4 mb-3">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="length" name="length" min="1"
                                max="50000" value="${circuit.length}" required>
                            <label for="length">Length</label>
                            <div class="invalid-feedback">
                                Please insert a valid circuit length.
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="cornersNumber"
                                name="cornersNumber" min="1" max="200" value="${circuit.cornersNumber}" required>
                            <label for="cornersNumber">Corners number</label>
                            <div class="invalid-feedback">
                                Please insert a valid corners number value.
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="lapPrice" name="lapPrice" min="1"
                                max="500" value="${circuit.lapPrice}" required>
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

            <c:import url="/jsp/include/circuit-type-modal.jsp" />

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
        <script type="text/javascript" src="<c:url value="/js/edit-circuit.js"/>"></script>
    </body>
    <%@ include file="footer.jsp" %>
</html>