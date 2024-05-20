<%@ page import="it.unipd.dei.webapp.wacar.resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
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
    <h2>List of Orders</h2>
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
        <c:if test="${not empty orders}">
            <c:forEach var="order" items="${orders}" varStatus="loop">
                <tr>
                    <td><c:out value="${order.id}"/></td>
                    <td><c:out value="${order.date}"/></td>
                    <td><c:out value="${order.carBrand}"/></td>
                    <td><c:out value="${order.carModel}"/></td>
                    <td><c:out value="${order.circuit}"/></td>
                    <td><c:out value="${order.createdAt}"/></td>
                    <td><c:out value="${order.NLaps}"/></td>
                    <td><c:out value="${order.price}"/></td>
                    <td>
                        <div class="dropdown">
                            <button class="btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                                    <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                                </svg>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#orderModal${order.id}">Edit</a></li>
                                <li><a class="dropdown-item text-danger" href="#">Delete</a></li>
                            </ul>
                        </div>
                    </td>
                </tr>

                <!-- Modal -->
                <div class="modal fade" id="orderModal${order.id}" tabindex="-1" aria-labelledby="orderModalLabel${order.id}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="orderModalLabel${order.id}">Order ${order.id} Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="row">
                                <div class="col-md-6 image-container p-3">
                                    <img class="card-img-top" src="<c:url value='/loadCarImage'><c:param name='model' value='${cars[order.id].model}'/><c:param name='brand' value='${cars[order.id].brand}'/></c:url>" alt="car image">
                                </div>
                                <div class="col-md-6 image-container p-3">
                                    <img class="card-img-top" src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuits[order.id].name}'/></c:url>" alt="circuit image">
                                </div>
                            </div>
                            <div class="modal-body">                            <div class="modal-body">
                                <form id="orderForm${order.id}" action="/wacar/order/update/${order.id}" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <div class="mb-3">
                                        <label for="orderDate${order.id}" class="form-label">Date</label>
                                        <input type="date" name="date" min="2024-01-01" class="form-control" id="orderDate${order.id}" value="${order.date}">
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="orderCarBrand${order.id}" class="form-label">Car Brand</label>
                                            <input type="text" class="form-control" id="orderCarBrand${order.id}" value="${order.carBrand}" disabled>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="orderCarModel${order.id}" class="form-label">Car Model</label>
                                            <input type="text" class="form-control" id="orderCarModel${order.id}" value="${order.carModel}" disabled>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="orderCircuit${order.id}" class="form-label">Circuit</label>
                                            <input type="text" class="form-control" id="orderCircuit${order.id}" value="${order.circuit}" disabled>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="orderNLaps${order.id}" class="form-label">Number of Laps</label>
                                            <input type="number" class="form-control" id="nLaps" name="nLaps" value="${order.NLaps}">
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <label for="orderPrice${order.id}" class="form-label">Price</label>
                                        <input type="text" class="form-control" id="orderPrice${order.id}" value="${order.price}" disabled>
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
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>

<%@ include file="footer.jsp" %>

</html>
