<!--
Author: Alessandro Leonardi (alessandro.leonardi.5@studenti.unipd.it)
Version: 1.01
Since: 1.00
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>List Favorites</title>
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
</head>
<body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.11.6/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="${pageContext.request.contextPath}/js/list-orders.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list-orders.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer-style.css">
<%@ include file="header.jsp" %>


<%-- Display any messages --%>
<div class="container">

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger">
                ${sessionScope.errorMessage.message}
        </div>
        <c:remove var="sessionScope.errorMessage" />
    </c:if>

    <h1>Your Favourites</h1>
    <hr>

    <%-- Display the list of favorites --%>

    <c:if test="${not empty favouritesList}">
        <c:forEach var="favorite" items="${favouritesList}">
            <div class="container-table">
                <div class="row justify-content-between align-items-center">
                    <div class="col-xl-3 col-md-6 text-center">
                        <span class="material-symbols-outlined">directions_car</span>
                        <h5><c:out value="${favorite.carBrand} ${favorite.carModel}"/></h5>
                    </div>
                    <div class="col-xl-2 col-md-5 text-center">
                        <span class="material-symbols-outlined">road</span>
                        <h5 class="orderdata"><c:out value="${favorite.circuit}"/></h5>
                    </div>
                    <div class="col-xl-2 col-md-5 text-center">
                        <span class="material-symbols-outlined">calendar_month</span>
                        <h5 class="orderdata">
                            <fmt:formatDate value="${favorite.createdAt}" pattern="yyyy-MM-dd HH:mm" />
                        </h5>
                    </div>
                    <div class="col-xl-2 col-md-5 text-center">
                        <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal"
                                data-circuit="${favorite.circuit}" data-carbrand="${favorite.carBrand}" data-carmodel="${favorite.carModel}">Delete</button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
</div>

<!-- Modal for DELETE -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Deleting selected Favourite</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="deleteForm" action="/wacar/user/deleteFavourite" method="post">
                    <input type="hidden" name="circuitName" id="circuitName" value="" />
                    <input type="hidden" name="carBrand" id="carBrand" value="" />
                    <input type="hidden" name="carModel" id="carModel" value="" />
                    <p>Are you sure you want to delete this favorite?</p>
                    <p><strong>Circuit:</strong> <span id="modal-circuit"></span></p>
                    <p><strong>Car Brand:</strong> <span id="modal-carBrand"></span></p>
                    <p><strong>Car Model:</strong> <span id="modal-carModel"></span></p>
                    <button type="submit" class="btn btn-danger">Confirm</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<c:url value="/js/list-favourites.js"/>"></script>
<%@ include file="footer.jsp" %>
</html>
