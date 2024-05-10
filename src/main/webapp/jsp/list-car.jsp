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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
</head>
<body>

<%@ include file="toolbar.jsp" %>

<c:if test="${not empty cars}">
<div class="outer-container">
<!-- for each car in database -->
    <c:forEach var="car" items="${cars}" varStatus="loop">
        <div class="item-container">
            <div class="card position-relative" data-toggle="modal" data-target="#carModal${loop.index}">
                <img src="<c:url value='/loadCarImage'><c:param name='model' value='${car.model}'/><c:param name='brand' value='${car.brand}'/></c:url>" class="card-img-top" alt="car image">
                <div class="card-img-overlay card-img-overlay-top-left">
                    <h5 class="card-title card-title-outside">${car.brand}</h5>
                </div>
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">${car.model}</h6>
                </div>
            </div>
            <!-- Opened car card -->
            <div class="modal fade" id="carModal${loop.index}" tabindex="-1" role="dialog" aria-labelledby="carModalLabel${loop.index}" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="carModalLabel${loop.index}">${car.brand} ${car.model}</h5>
                            <button type="button" class="close close-btn" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <img src="<c:url value='/loadCarImage'><c:param name='model' value='${car.model}'/><c:param name='brand' value='${car.brand}'/></c:url>" class="card-img-top" alt="car image">
                            <p>${car.description}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
</c:if>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script>
</body>
</html>
