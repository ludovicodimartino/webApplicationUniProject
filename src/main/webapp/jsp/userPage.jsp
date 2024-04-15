<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty sessionScope.account}">
        <h1>USER PAGE - SUCCESS</h1>
        <hr/>
        <p>Message</p>
        <ul>
            <li>surname: ${sessionScope.account.surname}</li>
            <li>name: ${sessionScope.account.name}</li>
            <li>email: ${sessionScope.account.email}</li>
            <li>address: ${sessionScope.account.address}</li>
        </ul>
        <form action="/jsp/recap-order.jsp" method="get">
            <button type="submit">Order list</button>
        </form>
        <form action="/jsp/recap-favourite.jsp" method="get">
            <button type="submit">Favourite list</button>
        </form>
    </c:when>
    <c:otherwise>
        <h1>LOGIN USER - ERROR</h1>
        <hr/>
        <ul>
            <li>Error message: An error occurred - null</li>
        </ul>
    </c:otherwise>
</c:choose>
</body>
</html>
