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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>
<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>

<c:if test="${not empty circuits}">
    <div class="container">
        <div class="row row-cols-3">
            <c:forEach var="circuit" items="${circuits}" varStatus="loop">
                <div class="card position-relative" data-toggle="modal" data-target="#circuitModal${loop.index}">
                    <img src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuit.name}'/></c:url>" class="card-img-top" alt="circuit image">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">${circuit.name}</h6>
                    </div>
                </div>
                <!-- Opened circuit card -->
                <div class="modal fade" id="circuitModal${loop.index}" tabindex="-1" role="dialog" aria-labelledby="circuitModalLabel${loop.index}" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="circuitModalLabel${loop.index}">${circuit.name}</h5>
                                <button type="button" class="close close-btn" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <img src="<c:url value='/loadCircuitImage'><c:param name='circuitName' value='${circuit.name}'/></c:url>" class="card-img-top" alt="circuit image">
                                <p>${circuit.description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script> <!-- Font Awesome for icons -->
</body>
<c:import url="footer.jsp"/>
</html>
