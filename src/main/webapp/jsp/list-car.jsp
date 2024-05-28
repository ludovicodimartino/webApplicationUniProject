<%@ page import="it.unipd.dei.webapp.wacar.resource.Car" %>
<%@ page import="java.util.List" %>
<!-- displayCars.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Cars</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-order.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer-style.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>

<div class="container">
    <h1>Cars list</h1>
    <hr>
    <c:if test="${not empty cars}">
        <div class="container-form">
            <!-- for each car in database -->
            <div class="row">
                <c:forEach var="car" items="${cars}" varStatus="loop">
                    <div id="${car.brand},${car.model}" class="card" data-toggle="modal" data-target="#carModal">
                        <img class="card-img-top" src="<c:url value='/loadCarImage'><c:param name='model' value='${car.model}'/><c:param name='brand' value='${car.brand}'/></c:url>" class="card-img-top" alt="car image">
                        <div class="card-body">
                            <p class="h5"><c:out value="${car.brand} ${car.model}"/></p>
                            <p class="h6"><c:out value="${car.type}"/></p>
                        </div>
                    </div>
                    
                </c:forEach>
            </div>
        </div>
        <!-- Opened/clicked car card -->
        <div class="modal fade" id="carModal" tabindex="-1" role="dialog" aria-labelledby="carModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title fs-5" id="carModalTitle"></h3>
                        <button type="button" class="close btn-close" data-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <img id="modal-img" class="img-fluid rounded" alt="car image">
                            </div>
                            <div class="col-md-6">
                                
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item"><strong>Type: </strong><span id="type"></span></li>
                                    <li class="list-group-item"><strong>Max Speed: </strong><span id="maxSpeed"></span> km/h</li>
                                    <li class="list-group-item"><strong>Horsepower: </strong><span id="horsepower"></span> HP</li>
                                    <li class="list-group-item"><strong>Acceleration: </strong><span id="acceleration"></span> s</li>
                                    <li class="list-group-item">
                                        <strong>Available: </strong>
                                        <span id="isAvailable"><strong id="available"></strong></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="col">
                            <p class="h3">Description</p>
                            <p id="description"></p>
                        </div>
                        <div id="admin-edit" class="modal-footer">
                            <a id="admin-edit-car" class="btn btn-primary" type="button">
                                Edit
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
<script type="text/javascript" src="<c:url value="/js/list-car.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script>
</body>
<%@ include file="footer.jsp" %>
</html>
