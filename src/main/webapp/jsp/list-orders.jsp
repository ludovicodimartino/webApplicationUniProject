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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
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
    <div>
        <c:if test="${not empty afterorders}">
            <c:forEach var="afterorders" items="${afterorders}" varStatus="loop">
                <div class="order-item">
                    <nav style="--bs-breadcrumb-divider: url('data:image/svg+xml,%3Csvg width=\'32\' height=\'29\' viewBox=\'0 0 32 29\' fill=\'none\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M4.14551 3.84894L12.3841 12.345C13.5125 13.5086 13.5125 15.3582 12.3841 16.5219L4.14551 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3Cpath d=\'M18.8994 3.84894L27.138 12.345C28.2664 13.5086 28.2664 15.3582 27.138 16.5219L18.8994 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3C/svg%3E');" aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><c:out value="${afterorders.id}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.date}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.carBrand}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.carModel}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.circuit}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.createdAt}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.NLaps}"/></li>
                            <li class="breadcrumb-item"><c:out value="${afterorders.price}"/></li>
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
                                            <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#orderModal${afterorders.id}">Edit</a></li>
                                            <li><a class="dropdown-item text-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${afterorders.id}">Delete</a></li>
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
                                        <button type="submit" class="btn btn-danger">Confirm</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>



                    <!-- Modal -->
                    <div class="modal fade" id="orderModal${afterorders.id}" tabindex="-1" aria-labelledby="orderModalLabel${afterorders.id}" aria-hidden="true" data-lap-price="${circuits[afterorders.id].lapPrice}">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="orderModalLabel${afterorders.id}">Order ${afterorders.id} Details</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-6 image-container p-3">
                                            <img class="img-fluid rounded" src="<c:url value='/loadCarImage'><c:param name='model' value='${cars[afterorders.id].model}'/><c:param name='brand' value='${cars[afterorders.id].brand}'/></c:url>" alt="car image">
                                        </div>
                                        <div class="col-md-6 image-container p-3">
                                            <img class="img-fluid rounded" src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuits[afterorders.id].name}'/></c:url>" alt="circuit image">
                                        </div>
                                    </div>
                                    <form id="orderForm${afterorders.id}" action="/wacar/order/update/${afterorders.id}" method="post">
                                        <input type="hidden" name="orderId" value="${afterorders.id}">
                                        <div class="mb-3">
                                            <label for="orderDate${afterorders.id}" class="form-label">Date</label>
                                            <input type="date" name="date" min="2024-01-01" class="form-control" id="orderDate${afterorders.id}" value="${afterorders.date}">
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="orderCarBrand${afterorders.id}" class="form-label">Car Brand</label>
                                                <input type="text" class="form-control" id="orderCarBrand${afterorders.id}" value="${afterorders.carBrand}" disabled>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <label for="orderCarModel${afterorders.id}" class="form-label">Car Model</label>
                                                <input type="text" class="form-control" id="orderCarModel${afterorders.id}" value="${afterorders.carModel}" disabled>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="orderCircuit${afterorders.id}" class="form-label">Circuit</label>
                                                <input type="text" class="form-control" id="orderCircuit${afterorders.id}" value="${afterorders.circuit}" disabled>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <label for="orderNLaps${afterorders.id}" class="form-label">Number of Laps</label>
                                                <input type="number" class="form-control" id="nLaps" name="nLaps" value="${afterorders.NLaps}">
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="orderPrice${afterorders.id}" class="form-label">Price</label>
                                            <input type="text" class="form-control" id="orderPrice${afterorders.id}" value="${afterorders.price}" disabled>
                                        </div>
                                        <button id="confirmButton${afterorders.id}" type="submit" class="btn btn-primary w-100">Confirm changes</button>
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

        <c:if test="${not empty beforeorders}">
            <c:forEach var="beforeorders" items="${beforeorders}" varStatus="loop">
                <div class="order-item">
                    <nav style="--bs-breadcrumb-divider: url('data:image/svg+xml,%3Csvg width=\'32\' height=\'29\' viewBox=\'0 0 32 29\' fill=\'none\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M4.14551 3.84894L12.3841 12.345C13.5125 13.5086 13.5125 15.3582 12.3841 16.5219L4.14551 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3Cpath d=\'M18.8994 3.84894L27.138 12.345C28.2664 13.5086 28.2664 15.3582 27.138 16.5219L18.8994 25.0179\' stroke=\'%23C4C4C4\' stroke-width=\'7\' stroke-linecap=\'round\'/%3E%3C/svg%3E');" aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><c:out value="${beforeorders.id}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.date}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.carBrand}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.carModel}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.circuit}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.createdAt}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.NLaps}"/></li>
                            <li class="breadcrumb-item"><c:out value="${beforeorders.price}"/></li>
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