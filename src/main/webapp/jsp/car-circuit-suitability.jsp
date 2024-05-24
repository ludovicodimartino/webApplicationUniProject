<!--
Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
Version: 1.00
Since: 1.00
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>
    <title>Car-Circuit Suitability</title>
</head>

<body>

<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>

<div class="container">
    <h1 class="mt-4 mb-4">Car-Circuit Suitability</h1>
    <div id="liveAlertPlaceholder"></div>

    <div class="container bg-light rounded p-4 mb-4">
        <h3 class="mb-4">Associate Car Type with Circuit Type</h3>
        <form id="cCSuitForm" class="needs-validation" method="POST" novalidate>

            <%--    Car Types List--%>

            <div class="row mb-3">
                <div class="col-5">
                    <jsp:useBean id="carTypeList" scope="request" type="java.util.List"/>
                    <div class="input-group">
                        <select id="carTypeSelect" class="form-select" name="carType" required>
                            <option value="">Select car type</option>
                            <c:forEach items="${carTypeList}" var="car" varStatus="loop">
                                <option value="${car.name}"><c:out value="${car.name}"/></option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal"
                                data-bs-target="#newCarTypeModal">Add new <b>car type</b>
                        </button>
                    </div>
                </div>
                <div class="col-2 d-flex justify-content-center p-0">
                    <span class="material-symbols-outlined align-self-center">compare_arrows</span>
                </div>
                <%--    Circuit Types List--%>
                <div class="col-5">
                    <jsp:useBean id="circuitTypeList" scope="request" type="java.util.List"/>
                    <div class="input-group">
                        <select id="circuitTypeSelect" class="form-select" name="circuitType" required disabled>
                            <option value="">Select circuit type</option>
                            <c:forEach items="${circuitTypeList}" var="circuit" varStatus="loop">
                                <option value="${circuit.name}"><c:out value="${circuit.name}"/></option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal"
                                data-bs-target="#newCircuitTypeModal" id="circuitTypeBtn" disabled>Add new <b>circuit type</b>
                        </button>
                    </div>
                </div>
            </div>
            <div class="d-grid gap-2 mx-auto">
                <input type="submit" class="btn btn-primary" value="Create mapping">
            </div>
        </form>
    </div>

    <%--Show current associations--%>
    <div class="container bg-light rounded p-4 mb-4">
        <h3 class="mb-4">Current Associations</h3>
        <jsp:useBean id="cCSuitMap" scope="request" type="java.util.HashMap"/>
        <c:choose>
        <c:when test="${empty cCSuitMap}">
            <p>You don't have associations between car and circuits at the moment.</p>
        </c:when>
        <c:otherwise>
        <table id="associationTable" class="table table-light table-bordered rounded-3 overflow-hidden">
            <thead>
            <tr>
                <th scope="col">Car Type</th>
                <th scope="col">Circuit Types</th>
                <th scope="col" class="col-1">Delete</th>
            </tr>
            </thead>
            <tbody id="associationBody">
            <c:forEach items="${cCSuitMap}" var="cCSuit" varStatus="loop">
                <tr>
                    <td rowspan="<c:out value="${fn:length(cCSuit.value)}"/>"><c:out value="${cCSuit.key}"/></td>
                    <td><c:out value="${cCSuit.value[0]}"/></td>
                    <td class="deleteBtnCol">
                        <button carType="${cCSuit.key}" circuitType="<c:out value="${cCSuit.value[0]}"/>" class="deleteBtn btn btn-danger">
                            <span class="material-symbols-outlined">delete</span>
                        </button>
                    </td>
                </tr>
                <c:forEach items="${cCSuit.value}" var="circuitType" varStatus="loop">
                    <c:if test="${loop.index > 0}">
                        <tr>
                            <td><c:out value="${circuitType}"/></td>
                            <td class="deleteBtnCol">
                                    <button carType="${cCSuit.key}" circuitType="<c:out value="${circuitType}"/>" class="deleteBtn btn btn-danger">
                                        <span class="material-symbols-outlined">delete</span>
                                    </button>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </c:forEach>
            </tbody>
            </c:otherwise>
            </c:choose>
        </table>
    </div>

    <c:import url="/jsp/include/car-type-modal.jsp"/>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin-utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/insert-car-type.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/insert-circuit-type.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/car-circuit-suitability.js"></script>
</body>
</html>
