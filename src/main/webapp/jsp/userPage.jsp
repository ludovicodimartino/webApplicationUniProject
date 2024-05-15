<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>

<div class="container">
    <c:choose>
        <c:when test="${not empty sessionScope.account}">
            <c:choose>
                <c:when test="${sessionScope.account.type == 'USER'}">
                    <h1>USER PAGE - SUCCESS</h1>
                </c:when>
                <c:otherwise>
                    <h1>ADMIN PAGE - SUCCESS</h1>
                </c:otherwise>
            </c:choose>
            <hr/>
            <div class="user-info">
                <ul>
                    <li><strong>Surname:</strong> ${sessionScope.account.surname}</li>
                    <li><strong>Name:</strong> ${sessionScope.account.name}</li>
                </ul>
                <ul>
                    <li><strong>Email:</strong> ${sessionScope.account.email}</li>
                    <li><strong>Address:</strong> ${sessionScope.account.address}</li>
                </ul>
            </div>
            <div class="user-actions">
                <form action="/wacar/user/listOrdersByAccount" method="get">
                    <button type="submit" class="btn btn-success">Order list</button>
                </form>
                <form action="/wacar/user/list-favourite" method="get">
                    <button type="submit" class="btn btn-success">Favourite list</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-error">
                <h1>LOGIN USER - ERROR</h1>
                <hr/>
                <ul>
                    <li><strong>Error message:</strong> An error occurred - null</li>
                </ul>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>

