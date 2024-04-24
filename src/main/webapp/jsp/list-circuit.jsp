<%@ page import="it.unipd.dei.webapp.wacar.resource.Car" %>
<%@ page import="java.util.List" %>
<!-- displayCircuits.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Circuits</title>
    <style>
        .circuit-container {
            cursor: pointer;
            display: inline-block; /* Display divs in a row */
            vertical-align: top; /* Align divs to the top */
            margin: 20px;
        }

        .circuit-card {
            border-radius: 10px;
            width: 350px;
            background-color: lightgrey;
            padding: 15px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            transition: all 0.3s ease;
        }

        .circuit-card img {
            height: 70%;
            width: 100%;
            object-fit: cover;
            object-position: center;
            border-radius: 10px;
        }

        .circuit-card h1 {
            font-size: 20px;
            margin-top: auto;
            margin-bottom: 5px;
        }

        .circuit-card h5 {
            font-size: 15px;
            color: grey;
            margin-top: 3px;
            margin-bottom: 10px;
        }

        .additional-info {
            display: none;
            background-color: white;
            border: 1px solid lightgrey;
            padding: 10px;
            border-radius: 5px;
            z-index: 1;
        }

        .circuit-container:hover .circuit-card {
            transform: scale(1.1);
        }

        .circuit-container:hover .additional-info {
            display: block;
        }

        .circuit-container:hover {
            height: auto; /* Expand the height to fit the additional-info */
        }

        .navbar {
            background-color: red;
            color: white;
            padding: 10px 20px; /* Add padding for spacing */
            display: flex;
            justify-content: space-around; /* Arrange links evenly */
            align-items: center; /* Align links vertically */
        }

        .navbar a {
            text-decoration: none; /* Remove underline from links */
            color: white; /* Set link color */
            font-weight: bold; /* Make text bold */
            transition: color 0.3s ease; /* Add transition effect */
        }

        .navbar a:hover {
            color: lightgrey; /* Change text color on hover */
        }

    </style>
</head>
<body>
<c:choose>
    <c:when test="${not empty accountType}">
        <c:choose>
            <c:when test="${accountType eq 'USER'}">
                <div class="navbar">
                    <a href="/wacar/">Home</a>
                    <a href="/wacar/car_list/">Car List</a>
                    <a href="/wacar/user/listOrdersByAccount">Orders</a>
                    <a href="/wacar/user/">Account</a>
                    <!-- Add more links as needed -->
                </div>
            </c:when>
            <c:when test="${accountType eq 'ADMIN'}">
                <div class="navbar">
                    <a href="/wacar/">Home</a>
                    <a href="/wacar/car_list/">Car List</a>
                    <a href="/wacar/admin/insertCar/">Insert new Car</a>
                    <a href="/wacar/admin/insertCircuit/">Insert new Circuit</a>
                    <a href="/wacar/admin/insertMapping/">Insert new Mapping</a>
                    <a href="/wacar/admin/">Account</a>
                </div>
            </c:when>
        </c:choose>
    </c:when>
    <c:when test="${empty accountType}">
        <div class="navbar">
            <a href="/wacar/">Home</a>
            <a href="/wacar/car_list/">Car List</a>
            <!-- Add more links as needed -->
        </div>
    </c:when>
</c:choose>
<c:if test="${not empty circuits}">
    <c:forEach var="circuit" items="${circuits}">
        <div class="circuit-container">
            <div class="circuit-card">
                <img src="<c:url value='/loadCircuitImage'><c:param name='name' value='${circuit.name}'/></c:url>"
                     alt="circuit image"/>
                <h1><c:out value="${circuit.name}"/></h1>
                <h5><c:out value="${circuit.type}"/></h5>
                <c:choose>
                    <c:when test="${accountType eq 'ADMIN'}">
                        <tr>
                            <td>
                                <a href="/wacar/admin/editCircuit/?name=${circuit.name}" class="btn link" type="button">
                                    Modify
                                </a>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <!-- Handle other cases if needed -->
                    </c:otherwise>
                </c:choose>
                <div class="additional-info">
                    <table>
                        <tr>
                            <td>Address:</td>
                            <td><c:out value="${circuit.address}"/></td>
                        </tr>
                        <tr>
                            <td>Length:</td>
                            <td><c:out value="${circuit.length}"/></td>
                        </tr>
                        <tr>
                            <td>Corners:</td>
                            <td><c:out value="${circuit.cornersNumber}"/></td>
                        </tr>
                        <tr>
                            <td>Description:</td>
                            <td><c:out value="${circuit.description}"/></td>
                        </tr>
                        <tr>
                            <td>Price per lap:</td>
                            <td><c:out value="${circuit.lapPrice}"/></td>
                        </tr>
                        <tr>
                            <td>Available:</td>
                            <td><c:out value="${circuit.available}"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </c:forEach>
</c:if>
</body>
</html>
