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
        <c:choose>
            <c:when test = "${sessionScope.account.type == 'USER'}">
                <h1>USER PAGE - SUCCESS</h1>
            </c:when>
            <c:otherwise>
                <h1>ADMIN PAGE - SUCCESS</h1>
            </c:otherwise>
        </c:choose>
        <hr/>
        <p>Message</p>
        <ul>
            <li>surname: ${sessionScope.account.surname}</li>
            <li>name: ${sessionScope.account.name}</li>
            <li>email: ${sessionScope.account.email}</li>
            <li>address: ${sessionScope.account.address}</li>
        </ul>
        <c:if test = "${account.type == 'USER'}">
            <form action="/wacar/user/listOrdersByAccount" method="get">
                <button type="submit">Order list</button>
            </form>
            <form action="/jsp/recap-favourite.jsp" method="get">
                <button type="submit">Favourite list</button>
            </form>
        </c:if>
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
