<!--
    Author: Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
    Version: 1.00
    Since: 1.00
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Recap of your new favourite item</title>
	</head>

    <body>
        <div>
            <c:if test="${not empty newFavourite}">
                <h1>Recap of your new favourite item: </h1>
                <ul>
                    <li>User: <c:out value="${newFavourite.account}"/></li>
                    <li>Circuit: <c:out value="${newFavourite.circuit}"/></li>
                    <li>Car: <c:out value="${newFavourite.carBrand} ${newFavourite.carModel}"/></li>
                </ul>
                <a href="/wacar/">Back to home</a>
            </c:if>
        </div>
    </body>
</html>