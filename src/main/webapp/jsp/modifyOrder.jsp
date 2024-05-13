<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Form</title>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>
        $(function() {
            $("#date").datepicker();
        });

    </script>
</head>
<body>

<h1>Modify Order Form</h1>

<c:choose>
<c:when test="${not empty message.error}">
<div class="alert alert-danger" role="alert">
    <span>${message.message}</span>
</div>
</c:when>
</c:choose>

<form id="orderForm" action="/wacar/order/update/${order.id}" method="post">




    <ul>
        <li>User: <c:out value="${order.account}"/></li>
        <li>Circuit: <c:out value="${order.circuit}"/></li>
        <li>Car: <c:out value="${order.carBrand} ${order.carModel}"/></li>

        <li>Date: <label for="date"></label>
            <input type="date" name="date" min="2018-01-01" /></li>
        <li>Number of laps: <label for="nLaps"></label>
            <input type="number" id="nLaps" name="nLaps"></li>

        <li>Total price: <c:out value="${order.price}"/></li>
    </ul>

    <button type="submit" class="btn btn-primary btn-sm">Update </button>
</form>


