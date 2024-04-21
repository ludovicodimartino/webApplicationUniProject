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
		<title>Recap of your order</title>
	</head>

    <body>
        <div>
            <c:choose>
                <c:when test="${message.error}">
                    <h1>Error while processing your request</h1>
                    <p><c:out value="${message.message}"/></p>
                </c:when>

                <c:otherwise>
                    <form>

                    </form>

                    <c:if test="${not empty order}">
                        <c:if test="${not empty carBrand}">
                            <
                        </c:if>

                        <h1>Order: </h1>
                        <ul>
                            <li>User: <c:out value="${order.account}"/></li>
                            <li>
                                Car: <c:out value="${newOrder.carBrand} ${newOrder.carModel}"/>
                                <a href="cars/" class="btn link" type="button">
                                    Modify
                                </a>
                            </li>
                            <li>
                                Circuit: <c:out value="${order.circuit}"/>
                                <c:choose>
                                    <c:when test="${not empty carType}">
                                        <a href="circuits/?carBrand=${order.carBrand}&carModel=${order.carModel}&carType=${carType}" class="btn link" type="button">
                                            Select
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <p>modify the car if you want to change the circuit</p>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                            <li>Date: <c:out value="${order.date}"/></li>
                            <li>Number of laps: <c:out value="${order.NLaps}"/></li>
                            <li>Total price: <c:out value="${order.price}"/></li>
                        </ul>
                        <a href="/wacar/">Back to home</a>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>