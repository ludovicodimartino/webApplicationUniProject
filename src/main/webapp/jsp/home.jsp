<%@ page import="it.unipd.dei.webapp.wacar.resource.User" %>
<%@ page import="java.util.Objects" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Welcome!</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Welcome to WaCar!</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item">
                            <form method="POST" action="/wacar/logout/">
                                <button class="nav-link">Logout</button>
                            </form>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="/wacar/signup/">Signup</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/wacar/login/">Login</a>
                        </li>
                        <i class="fa-solid fa-user"></i>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <h1>Welcome!</h1>

    <!-- Circuit list -->
    <div class="card">
        <a href="/wacar/circuit_list/">CIRCUIT list</a>
    </div>

    <!-- Car list -->
    <div class="card">
        <a href="/wacar/car_list/">CAR list</a>
    </div>




    <c:choose>
        <c:when test="${not empty sessionScope.account}">
            <c:choose>
                <c:when test="${sessionScope.account.type eq 'USER'}">
                    <!-- Create new order -->
                    <div class="card">
                        <a href="/wacar/user/create-order/cars">Create a NEW ORDER</a>
                    </div>
                    <div class="card">
                        <a href="/wacar/user/listOrdersByAccount">List ORDERS</a>
                    </div>
                    <div class="card">
                        <a href="/wacar/user/list-favourite">List FAVOURITE</a>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
