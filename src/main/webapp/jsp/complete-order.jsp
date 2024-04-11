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
        <form method="GET" action="/wacar/user/complete-order/">
            <label>You do not know when you can race? save the car and the circuit!</label>
            <button type="submit">Save as favourite</button><br/>
        </form>
        <h4><c:out value="${carBrand} ${carModel}"/></h4>
        <form method="GET" action="/wacar/user/create-order/recap">
            <label for="date">Select a date:</label>
            <input type="date" name="date" min="2018-01-01" /></br>
            <label for="nLaps">Select the number of laps:</label>
            <input type="number" name="nLaps" min="1"></br>
            <button type="submit">Create order</button><br/>
            <input type="hidden" name="carBrand" value="${carBrand}">
            <input type="hidden" name="carModel" value="${carModel}">
            <input type="hidden" name="circuitName" value="${circuitName}">
            <input type="hidden" name="lapPrice" value="${lapPrice}">
        </form>
    </body>
</html>