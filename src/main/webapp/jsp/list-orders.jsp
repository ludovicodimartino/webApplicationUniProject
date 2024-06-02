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
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
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

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-top-margin">
                ${sessionScope.errorMessage.message}
        </div>
        <c:remove var="sessionScope.errorMessage" />
    </c:if>

    <h1>Your pending Orders</h1>
    <hr>
<!--    <table class="table table-striped">
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
        -->
    <%-- Display the list of pending Orders --%>
    <div class="container-table">
        <c:if test="${not empty after}">
            <c:forEach var="after" items="${after}" varStatus="loop">
                <div class="row">
                    <div class="col-md-2">
                        <span class="material-symbols-outlined"> calendar_month </span>
                        <h5 class="order-data"><c:out value="${after.date}"/></h5>
                    </div>
                    <div class="col-md-3">
                        <span class="material-symbols-outlined"> directions_car </span>
                        <h5><c:out value="${after.carBrand} ${after.carModel}"/></h5>
                    </div>
                    <div class="col-md-3">
                        <span class="material-symbols-outlined"> road </span>
                        <h5 class="order-data"><c:out value="${after.circuit}"/>
                    </div>
                    <div class="col-md-1">
                        <span class="material-symbols-outlined"> rotate_right</span>
                        <h5 class="order-data"><c:out value="${after.NLaps}"/></h5>
                    </div>
                    <div class="col-md-2">
                        <span class="material-symbols-outlined"> euro </span>
                        <h5 class="order-data"> <c:out value="${after.price}"/></h5>
                    </div>
                    <div class="col-md-1">
                        <span class="material-symbols-outlined"> edit </span>
                        <button class="btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                 class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                                <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                            </svg>
                        </button>
                        <ul class="dropdown-menu">
                            <c:choose>
                                <c:when test="${modifyAvailable[loop.index]}">
                                    <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#orderModal${after.id}">Edit</a></li>
                                    <li><a class="dropdown-item text-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${after.id}">Delete</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a class="dropdown-item disabled">Edit</a></li>
                                    <li><a class="dropdown-item text-danger disabled">Delete</a></li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>

                </div>
                <div class="order-item">
                    <nav style="--bs-breadcrumb-divider: url('data:image/svg+xml,%3Csvg width=\'32\' height=\'29\' viewBox=\'0 0 32 29\' fill=\'none\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M4.14551 3.84894L12.3841 12.345C13.5125 13.5086 13.5125 15.3582 12.3841 16.5219L4.14551 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3Cpath d=\'M18.8994 3.84894L27.138 12.345C28.2664 13.5086 28.2664 15.3582 27.138 16.5219L18.8994 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3C/svg%3E');" aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><c:out value="${after.id}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.date}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.carBrand}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.carModel}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.circuit}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.createdAt}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.NLaps}"/></li>
                            <li class="breadcrumb-item"><c:out value="${after.price}"/></li>
                            <li class="dropdown">
                                <button class="btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                         class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                                        <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                                    </svg>
                                </button>
                                <ul class="dropdown-menu">
                                    <c:choose>
                                        <c:when test="${modifyAvailable[loop.index]}">
                                            <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#orderModal${after.id}">Edit</a></li>
                                            <li><a class="dropdown-item text-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${after.id}">Delete</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a class="dropdown-item disabled">Edit</a></li>
                                            <li><a class="dropdown-item text-danger disabled">Delete</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </li>
                        </ol>
                    </nav>
                    <!-- Modal for DELETE -->
                    <div class="modal fade" id="deleteModal${after.id}" tabindex="-1" aria-labelledby="deleteModalLabel${after.id}" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="deleteModalLabel${after.id}">Deleting order ${after.id}</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <form id="deleteForm${after.id}" action="/wacar/order/delete/${after.id}" method="post">
                                        <input type="hidden" name="orderId" value="${after.id}">
                                        <p>Are you sure you want to delete this order?</p>
                                        <button type="submit" class="btn btn-danger">Confirm</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>



                    <!-- Modal -->
                    <div class="modal fade" id="orderModal${after.id}" tabindex="-1" aria-labelledby="orderModalLabel${after.id}" aria-hidden="true" data-lap-price="${circuits[after.id].lapPrice}">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="orderModalLabel${after.id}">Order ${after.id} Details</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-6 image-container p-3">
                                            <img class="img-fluid rounded" src="<c:url value='/loadCarImage'><c:param name='model' value='${cars[after.id].model}'/><c:param name='brand' value='${cars[after.id].brand}'/></c:url>" alt="car image">
                                        </div>
                                        <div class="col-md-6 image-container p-3">
                                            <img class="img-fluid rounded" src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuits[after.id].name}'/></c:url>" alt="circuit image">
                                        </div>
                                    </div>
                                    <form id="orderForm${after.id}" action="/wacar/order/update/${after.id}" method="post">
                                        <input type="hidden" name="orderId" value="${after.id}">
                                        <div class="mb-3">
                                            <label for="orderDate${after.id}" class="form-label">Date</label>
                                            <input type="date" name="date" min="2024-01-01" class="form-control" id="orderDate${after.id}" value="${after.date}">
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="orderCarBrand${after.id}" class="form-label">Car Brand</label>
                                                <input type="text" class="form-control" id="orderCarBrand${after.id}" value="${after.carBrand}" disabled>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <label for="orderCarModel${after.id}" class="form-label">Car Model</label>
                                                <input type="text" class="form-control" id="orderCarModel${after.id}" value="${after.carModel}" disabled>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="orderCircuit${after.id}" class="form-label">Circuit</label>
                                                <input type="text" class="form-control" id="orderCircuit${after.id}" value="${after.circuit}" disabled>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <label for="orderNLaps${after.id}" class="form-label">Number of Laps</label>
                                                <input type="number" class="form-control" id="nLaps" name="nLaps" value="${after.NLaps}">
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="orderPrice${after.id}" class="form-label">Price</label>
                                            <input type="text" class="form-control" id="orderPrice${after.id}" value="${after.price}" disabled>
                                        </div>
                                        <button id="confirmButton${after.id}" type="submit" class="btn btn-primary w-100">Confirm changes</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:if>
    </div>
</div>
<hr>

<div class="container">
    <h1>Your expired Orders</h1>
    <hr>
    <!-- <table class="table table-striped">
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
        -->
    <div classname="expired-orders">

        <c:if test="${not empty before}">
            <c:forEach var="before" items="${before}" varStatus="loop">
                <div class="order-item">
                    <nav style="--bs-breadcrumb-divider: url('data:image/svg+xml,%3Csvg width=\'32\' height=\'29\' viewBox=\'0 0 32 29\' fill=\'none\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M4.14551 3.84894L12.3841 12.345C13.5125 13.5086 13.5125 15.3582 12.3841 16.5219L4.14551 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3Cpath d=\'M18.8994 3.84894L27.138 12.345C28.2664 13.5086 28.2664 15.3582 27.138 16.5219L18.8994 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3C/svg%3E');" aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><c:out value="${before.id}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.date}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.carBrand}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.carModel}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.circuit}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.createdAt}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.NLaps}"/></li>
                            <li class="breadcrumb-item"><c:out value="${before.price}"/></li>
                            </li>
                        </ol>
                    </nav>
                </div>
            </c:forEach>
        </c:if>
    </div>
</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/js/modify-order.js"/>"></script>


<%@ include file="footer.jsp" %>

</html>