<%@ page import="it.unipd.dei.webapp.wacar.resource.Circuit" %>
<%@ page import="java.util.List" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Circuits</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer-style.css">

</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <h1>Circuits list</h1>
    <hr>
    <c:if test="${not empty circuits}">
        <div class="container-form">
            <div class="row row-cols-3">
                <c:forEach var="circuit" items="${circuits}" varStatus="loop">
                    <div id="${circuit.name}" class="card" data-toggle="modal" data-target="#circuitModal">
                        <img src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuit.name}'/></c:url>" class="card-img-top" alt="circuit image">
                        <div class="card-body">
                            <p class="h5">${circuit.name}</h6>
                            <p class="h6">${circuit.type}</h6>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <!-- Opened circuit modal -->
        <div class="modal fade" id="circuitModal" tabindex="-1" role="dialog" aria-labelledby="circuitModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title fs-5" id="circuitModalTitle"></h3>
                        <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <img id="modal-img" class="card-img-top" alt="circuit image">
                            </div>
                            <div class="col-md-6">
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item"><strong>Type: </strong><span id="type"></span></li>
                                    <li class="list-group-item"><strong>Length: </strong><span id="length"></span> m</li>
                                    <li class="list-group-item"><strong>Corners number: </strong><span id="cornersNumber"></span></li>
                                    <li class="list-group-item"><strong>Address: </strong><span id="address"></span></li>
                                    <li class="list-group-item"><strong>Price per lap: </strong>€<span id="lapPrice"></span></li>
                                    <li class="list-group-item">
                                        <strong>Available: </strong>
                                        <span id="isAvailable"><strong id="available"></strong></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="col">
                            <p id="description"></p>
                        </div>
                    </div>
                    <div id="admin-edit" class="modal-footer">
                        <a id="admin-edit-circuit" class="btn btn-primary" type="button">
                            Edit
                        </a>
                    </div>
                </div>
            </div>
        </div>    
    </c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/js/list-circuit.js"/>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script> <!-- Font Awesome for icons -->
</body>
<%@ include file="footer.jsp" %>
</html>
