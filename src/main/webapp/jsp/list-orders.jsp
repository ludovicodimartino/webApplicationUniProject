<!--
Author: Michele Scapinello (alessandro.leonardi.5@studenti.unipd.it)
Version: 1.01
Since: 1.01
-->

<!--
Author: Alessandro Leonardi (alessandro.leonardi.5@studenti.unipd.it)
Version: 1.01
Since: 1.01
-->

<!-- displayCircuits.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <c:import url="/jsp/include/head-links.jsp"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Symbols+Outlined" rel="stylesheet">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
    <title>List Orders</title>
</head>
<body>

<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.11.6/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list-orders.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer-style.css">


<%@ include file="header.jsp" %>

<div class="container">
    <c:if test="${not empty sessionScope.confirmMessage}">
        <div class="alert alert-success alert-top-margin">
                ${sessionScope.confirmMessage.message}
        </div>
        <c:remove var="sessionScope.confirmMessage"/>
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-top-margin">
                ${sessionScope.errorMessage.message}
        </div>
        <c:remove var="sessionScope.errorMessage"/>
    </c:if>

    <h1>Your pending Orders</h1>
    <hr>

    <c:if test="${not empty after}">
        <c:forEach var="after" items="${after}" varStatus="loop">
            <div class="container-table">
                <div class="row">
                    <div class="col-xl-2">
                        <span class="material-symbols-outlined"> calendar_month </span>
                        <h5 class="order-data"><c:out value="${after.date}"/></h5>
                    </div>
                    <div class="col-xl-3">
                        <span class="material-symbols-outlined"> directions_car </span>
                        <h5><c:out value="${after.carBrand} ${after.carModel}"/></h5>
                    </div>
                    <div class="col-xl-3">
                        <span class="material-symbols-outlined"> road </span>
                        <h5 class="order-data"><c:out value="${after.circuit}"/>
                    </div>
                    <div class="col-xl-1">
                        <span class="material-symbols-outlined"> rotate_right</span>
                        <h5 class="order-data"><c:out value="${after.NLaps}"/></h5>
                    </div>
                    <div class="col-xl-2">
                        <span class="material-symbols-outlined eurobutton"> euro </span>
                        <h5 class="order-data"><c:out value="${after.price}"/></h5>
                    </div>
                    <div class="col-xl-1">
                        <button class="btn btn-dropdown" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                 class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                                <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                            </svg>
                        </button>
                        <ul class="dropdown-menu">
                            <c:choose>
                                <c:when test="${modifyAvailable[loop.index]}">
                                    <li><a class="dropdown-item" data-bs-toggle="modal"
                                           data-bs-target="#orderModal${after.id}">Edit</a>
                                    </li>
                                    <li><a class="dropdown-item text-danger" data-bs-toggle="modal"
                                           data-bs-target="#deleteModal${after.id}">Delete</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a class="dropdown-item disabled">Edit</a></li>
                                    <li><a class="dropdown-item text-danger disabled">Delete</a></li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                        <div class="btn-grid">
                            <c:choose>
                                <c:when test="${modifyAvailable[loop.index]}">
                                    <button class="btn btn-primary" data-bs-toggle="modal"
                                            data-bs-target="#orderModal${after.id}">Edit
                                    </button>
                                    <button class="btn btn-danger" data-bs-toggle="modal"
                                            data-bs-target="#deleteModal${after.id}">Delete
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-primary disabled">Edit</button>
                                    <button class="btn btn-danger disabled">Delete</button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Modal for DELETE -->
            <div class="modal fade" id="deleteModal${after.id}" tabindex="-1"
                 aria-labelledby="deleteModalLabel${after.id}"
                 aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="deleteModalLabel${after.id}">Deleting order ${after.id}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="deleteForm${after.id}" action="/wacar/order/delete/${after.id}" method="post">
                                <div class="row">
                                    <div class="col-md-6 image-container p-3">
                                        <img class="img-fluid rounded"
                                             src="<c:url value='/loadCarImage'><c:param name='model' value='${cars[after.id].model}'/><c:param name='brand' value='${cars[after.id].brand}'/></c:url>"
                                             alt="car image">
                                    </div>
                                    <div class="col-md-6 image-container p-3">
                                        <img class="img-fluid rounded"
                                             src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuits[after.id].name}'/></c:url>"
                                             alt="circuit image">
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="orderDate${after.id}" class="form-label"><b>Date</b></label>
                                    <input type="date" name="date" min="2024-01-01" class="form-control"
                                           id="orderDate${after.id}" value="${after.date}" disabled>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCarBrand${after.id}" class="form-label"><b>Car Brand</b></label>
                                        <input type="text" readonly class="form-control"
                                               id="orderCarBrand${after.id}"
                                               value="${after.carBrand}" disabled>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCarModel${after.id}" class="form-label"><b>Car Model</b></label>
                                        <input type="text" readonly class="form-control"
                                               id="orderCarModel${after.id}"
                                               value="${after.carModel}" disabled>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCircuit${after.id}" class="form-label"><b>Circuit</b></label>
                                        <input type="text" class="form-control"
                                               id="orderCircuit${after.id}"
                                               value="${after.circuit}" disabled>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="orderNLaps${after.id}" class="form-label"><b>Number of Laps</b></label>
                                        <input type="number" class="form-control" id="nLapss" name="nLaps"
                                               value="${after.NLaps}" disabled>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="orderPrice${after.id}" class="form-label"><b>Price</b></label>
                                    <input type="text" class="form-control" id="orderPrice${after.id}"
                                           value="${after.price}" disabled>
                                </div>
                                <!----Deletion--->
                                <div class="row">
                                    <input type="hidden" name="orderId" value="${after.id}">
                                    <div class="col-md-6 mb-3">
                                        <button type="submit" class="btn btn-danger w-100">Confirm</button>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <button type="button" class="btn btn-secondary w-100" data-bs-dismiss="modal">Cancel</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Modal -->
            <div class="modal fade" id="orderModal${after.id}" tabindex="-1"
                 aria-labelledby="orderModalLabel${after.id}"
                 aria-hidden="true" data-lap-price="${circuits[after.id].lapPrice}">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="orderModalLabel${after.id}">Order ${after.id} Details</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-6 image-container p-3">
                                    <img class="img-fluid rounded"
                                         src="<c:url value='/loadCarImage'><c:param name='model' value='${cars[after.id].model}'/><c:param name='brand' value='${cars[after.id].brand}'/></c:url>"
                                         alt="car image">
                                </div>
                                <div class="col-md-6 image-container p-3">
                                    <img class="img-fluid rounded"
                                         src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuits[after.id].name}'/></c:url>"
                                         alt="circuit image">
                                </div>
                            </div>
                            <form id="orderForm${after.id}" action="/wacar/order/update/${after.id}" method="post">
                                <input type="hidden" name="orderId" value="${after.id}">
                                <div class="mb-3">
                                    <label for="orderDate${after.id}" class="form-label">Date</label>
                                    <input type="date" name="date" min="2024-01-01" class="form-control"
                                           id="orderDate${after.id}" value="${after.date}">
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCarBrand${after.id}" class="form-label">Car Brand</label>
                                        <input type="text" class="form-control" id="orderCarBrand${after.id}"
                                               value="${after.carBrand}" disabled>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCarModel${after.id}" class="form-label">Car Model</label>
                                        <input type="text" class="form-control" id="orderCarModel${after.id}"
                                               value="${after.carModel}" disabled>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="orderCircuit${after.id}" class="form-label">Circuit</label>
                                        <input type="text" class="form-control" id="orderCircuit${after.id}"
                                               value="${after.circuit}" disabled>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="orderNLaps${after.id}" class="form-label">Number of Laps</label>
                                        <input type="number" class="form-control" id="nLaps" name="nLaps"
                                               value="${after.NLaps}">
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="orderPrice${after.id}" class="form-label">Price</label>
                                    <input type="text" class="form-control" id="orderPrice${after.id}"
                                           value="${after.price}" disabled>
                                </div>
                                <button id="confirmButton${after.id}" type="submit" class="btn btn-primary w-100">
                                    Confirm
                                    changes
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
</div>
<hr>

<div class="container">
    <h1>Your expired Orders</h1>
    <hr>
    <c:if test="${not empty before}">
        <c:forEach var="before" items="${before}" varStatus="loop">
            <div class="container-table">
                <div class="row">
                    <div class="col-xl-2">
                        <span class="material-symbols-outlined"> calendar_month </span>
                        <h5 class="order-data"><c:out value="${before.date}"/></h5>
                    </div>
                    <div class="col-xl-3">
                        <span class="material-symbols-outlined"> directions_car </span>
                        <h5><c:out value="${before.carBrand} ${before.carModel}"/></h5>
                    </div>
                    <div class="col-xl-3">
                        <span class="material-symbols-outlined"> road </span>
                        <h5 class="order-data"><c:out value="${before.circuit}"/>
                    </div>
                    <div class="col-xl-1">
                        <span class="material-symbols-outlined"> rotate_right</span>
                        <h5 class="order-data"><c:out value="${before.NLaps}"/></h5>
                    </div>
                    <div class="col-xl-2">
                        <span class="material-symbols-outlined eurobutton"> euro </span>
                        <h5 class="order-data"><c:out value="${before.price}"/></h5>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
</div>

</body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/js/modify-order.js"/>"></script>


<%@ include file="footer.jsp" %>

</html>