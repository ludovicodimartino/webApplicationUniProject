<%@ page import="it.unipd.dei.webapp.wacar.resource.User" %>
<%@ page import="java.util.Objects" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Welcome to WaCar</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer-style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home-style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">
    <h1>Welcome to 
        <img src="<c:url value="/images/logo_dark.png"/>" id="logo" alt="Wacar logo"
                         width="160">
    </h1>

    <div class="container-home">
        <!-- Circuit list -->
        <div class="card">
            <a href="/wacar/circuit_list/">List of CIRCUITS</a>
        </div>
    
        <!-- Car list -->
        <div class="card">
            <a href="/wacar/car_list/">List of CARS</a>
        </div>
    
        <c:choose>
            <c:when test="${not empty sessionScope.account}">
                <c:choose>
                    <c:when test="${sessionScope.account.type eq 'USER'}">
                        <!-- Create new order -->
                        <div class="card">
                            <a href="/wacar/user/create-order/cars">Create your NEW ORDER</a>
                        </div>
                        <div class="card">
                            <a href="/wacar/user/listOrdersByAccount">List of your ORDERS</a>
                        </div>
                        <div class="card">
                            <a href="/wacar/user/list-favourite">List of your FAVOURITES</a>
                        </div>
                        <div class="card">
                            <a href="/wacar/user/user-info">USER page</a>
                        </div>
                    </c:when>
                    <c:when test="${sessionScope.account.type eq 'ADMIN'}">
                        <div class="card">
                            <a href="/wacar/admin/insertCar/">Insert NEW CAR</a>
                        </div>
                        <div class="card">
                            <a href="/wacar/admin/insertCircuit/">Insert NEW CIRCUIT</a>
                        </div>
                        <div class="card">
                            <a href="/wacar/admin/insertMapping/">Map a CAR to a CIRCUIT</a>
                        </div>
                        <div class="card">
                            <a href="/wacar/admin/admin-info/">ADMIN page</a>
                        </div>
                    </c:when>
                </c:choose>
            </c:when>
        </c:choose>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<%@ include file="footer.jsp" %>
</body>
</html>
