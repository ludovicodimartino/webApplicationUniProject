<%@ page import="it.unipd.dei.webapp.wacar.resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer-style.css">
    <title>List Orders</title>
    <style>
        .modal-dialog {
            max-width: 600px;
        }
    </style>
</head>
<body>

<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.11.6/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/5.1.3/js/bootstrap.min.js"></script>


<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>

<div class="container">
    <h3>Pending orders</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Id</th>
            <th>Date</th>
            <th>Car Brand</th>
            <th>Car Model</th>
            <th>Circuit</th>
            <th>Created At</th>
            <th>Number of Laps</th>
            <th>Price</th>
            <th>Modify</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${not empty afterorders}">
        <c:forEach var="afterorders" items="${afterorders}" varStatus="loop">
        <tr>
            <td><c:out value="${afterorders.id}"/></td>
            <td><c:out value="${afterorders.date}"/></td>
            <td><c:out value="${afterorders.carBrand}"/></td>
            <td><c:out value="${afterorders.carModel}"/></td>
            <td><c:out value="${afterorders.circuit}"/></td>
            <td><c:out value="${afterorders.createdAt}"/></td>
            <td><c:out value="${afterorders.NLaps}"/></td>
            <td><c:out value="${afterorders.price}"/></td>
            <td>
                <div class="dropdown">
                    <button class="btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                            <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                        </svg>
                    </button>
                    <ul class="dropdown-menu">
                        <c:choose>
                            <c:when test="${modifyAvailable[loop.index]}">
                                <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#orderModal${afterorders.id}">Edit</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a class="dropdown-item disabled">Edit</a></li>
                            </c:otherwise>
                        </c:choose>
                        <li><a class="dropdown-item text-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${afterorders.id}">Delete</a></li>
                    </ul>
                </div>
            </td>
        </tr>

        <!-- Modal for DELETE -->
        <div class="modal fade" id="deleteModal${afterorders.id}" tabindex="-1" aria-labelledby="deleteModalLabel${afterorders.id}" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteModalLabel${afterorders.id}">Deleting order ${afterorders.id}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="deleteForm${afterorders.id}" action="/wacar/order/delete/${afterorders.id}" method="post">
                            <input type="hidden" name="orderId" value="${afterorders.id}">
                            <p>Are you sure you want to delete this order?</p>
                            <button type="submit" class="btn btn-primary">Confirm</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>



        <!-- Modal -->
        <div class="modal fade" id="orderModal${afterorders.id}" tabindex="-1" aria-labelledby="orderModalLabel${afterorders.id}"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderModalLabel${afterorders.id}">Order ${afterorders.id} Details</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="row">
                        <div class="col-md-6 image-container p-3">
                            <img class="card-img-top"
                                 src="<c:url value='/loadCarImage'><c:param name='model' value='${cars[afterorders.id].model}'/><c:param name='brand' value='${cars[afterorders.id].brand}'/></c:url>"
                                 alt="car image">
                        </div>
                        <div class="col-md-6 image-container p-3">
                            <img class="card-img-top"
                                 src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuits[afterorders.id].name}'/></c:url>"
                                 alt="circuit image">
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="modal-body">
                            <form id="orderForm${afterorders.id}" action="/wacar/order/update/${afterorders.id}" method="post">
                                <input type="hidden" name="orderId" value="${afterorders.id}">
                                <div class="mb-3">
                                    <label for="orderDate${afterorders.id}" class="form-label">Date</label>
                                    <input type="date" name="date" min="2024-01-01" class="form-control"
                                           id="orderDate${afterorders.id}" value="${afterorders.date}">
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCarBrand${afterorders.id}" class="form-label">Car Brand</label>
                                        <input type="text" class="form-control" id="orderCarBrand${afterorders.id}"
                                               value="${afterorders.carBrand}" disabled>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCarModel${afterorders.id}" class="form-label">Car Model</label>
                                        <input type="text" class="form-control" id="orderCarModel${afterorders.id}"
                                               value="${afterorders.carModel}" disabled>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCircuit${afterorders.id}" class="form-label">Circuit</label>
                                        <input type="text" class="form-control" id="orderCircuit${afterorders.id}"
                                               value="${afterorders.circuit}" disabled>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="orderNLaps${afterorders.id}" class="form-label">Number of Laps</label>
                                        <input type="number" class="form-control" id="nLaps" name="nLaps"
                                               value="${afterorders.NLaps}">
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="orderPrice${afterorders.id}" class="form-label">Price</label>
                                    <input type="text" class="form-control" id="orderPrice${afterorders.id}"
                                           value="${afterorders.price}" disabled>
                                </div>
                                <button type="submit" class="btn btn-primary">Confirm changes</button>
                            </form>
                        </div>

                    </div>
                </div>
            </div>
            </c:forEach>
            </c:if>
        </tbody>
    </table>

    <h3>Expired orders</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Id</th>
            <th>Date</th>
            <th>Car Brand</th>
            <th>Car Model</th>
            <th>Circuit</th>
            <th>Created At</th>
            <th>Number of Laps</th>
            <th>Price</th>
            <th>Modify</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${not empty beforeorders}">
        <c:forEach var="beforeorders" items="${beforeorders}" varStatus="loop">
        <tr>
            <td><c:out value="${beforeorders.id}"/></td>
            <td><c:out value="${beforeorders.date}"/></td>
            <td><c:out value="${beforeorders.carBrand}"/></td>
            <td><c:out value="${beforeorders.carModel}"/></td>
            <td><c:out value="${beforeorders.circuit}"/></td>
            <td><c:out value="${beforeorders.createdAt}"/></td>
            <td><c:out value="${beforeorders.NLaps}"/></td>
            <td><c:out value="${beforeorders.price}"/></td>
            <td></td>
            </c:forEach>
            </c:if>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>

<%@ include file="footer.jsp" %>

</html>
