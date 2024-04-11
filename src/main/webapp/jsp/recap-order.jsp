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
		<title>Complete your order</title>
	</head>

    <body>
        <h1>Recap of your order: </h1>
        <li>User: <c:out value="${newOrder.account}"></li>
        <li>User: <c:out value="${newOrder.}"></li>
        <li>User: <c:out value="${newOrder.account}"></li>
        <li>User: <c:out value="${newOrder.account}"></li>
        <li>User: <c:out value="${newOrder.account}"></li>
        <li>User: <c:out value="${newOrder.account}"></li>
                        out.printf("<li>name: %s</li>%n", s.getName());
                        out.printf("<li>email: %s</li>%n", s.getEmail());
                        out.printf("<li>address: %s</li>%n", s.getAddress());
        <h3><c:out value="${newOrder.createdAt} ${newOrder.carModel}"/></h3>
    </body>
</html>