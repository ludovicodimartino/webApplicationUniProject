<%@ page import="it.unipd.dei.webapp.wacar.resource.Car" %>
<%@ page import="java.util.List" %>
<!-- displayCars.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Cars</title>
    <style>
        .car-container {
            cursor: pointer;
            display: inline-block;
            vertical-align: top;
            margin: 20px;
        }

        .car-card {
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

        .car-card img {
            height: 70%;
            width: 100%;
            object-fit: cover;
            object-position: center;
            border-radius: 10px;
        }

        .car-card h1 {
            font-size: 20px;
            margin-top: auto;
            margin-bottom: 5px;
        }

        .car-card h5 {
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

        .car-container:hover .car-card {
            transform: scale(1.1);
        }

        .car-container:hover .additional-info {
            display: block;
        }

        .car-container:hover {
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
                    <a href="/wacar/circuit_list/">Circuit List</a>
                    <a href="/wacar/user/listOrdersByAccount">Orders</a>
                    <a href="/wacar/user/user-info">Account</a>
                    <!-- Add more links as needed -->
                </div>
            </c:when>
            <c:when test="${accountType eq 'ADMIN'}">
                <div class="navbar">
                    <a href="/wacar/">Home</a>
                    <a href="/wacar/circuit_list/">Circuit List</a>
                    <a href="/wacar/admin/insertCar/">Insert new Car</a>
                    <a href="/wacar/admin/insertCircuit/">Insert new Circuit</a>
                    <a href="/wacar/admin/insertMapping/">Insert new Mapping</a>
                    <a href="/wacar/admin/admin-info/">Account</a>
                </div>
            </c:when>
        </c:choose>
    </c:when>
    <c:when test="${empty accountType}">
        <div class="navbar">
            <a href="/wacar/">Home</a>
            <a href="/wacar/circuit_list/">Circuit List</a>
            <!-- Add more links as needed -->
        </div>
    </c:when>
</c:choose>
<c:if test="${not empty cars}">
    <c:forEach var="car" items="${cars}">
        <div class="car-container">
            <div class="car-card">
                <img src="<c:url value='/loadCarImage'><c:param name='model' value='${car.model}'/><c:param name='brand' value='${car.brand}'/></c:url>" alt="car image"/>
                <h1><c:out value="${car.brand} ${car.model}"/></h1>
                <h5><c:out value="${car.type}"/></h5>
                <c:choose>
                    <c:when test="${accountType eq 'ADMIN'}">
                        <tr>
                            <td>
                                <a href="/wacar/admin/editCar/?brand=${car.brand}&model=${car.model}" class="btn link" type="button">
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
                            <td>Max Speed:</td>
                            <td><c:out value="${car.maxSpeed}"/></td>
                        </tr>
                        <tr>
                            <td>Horsepower:</td>
                            <td><c:out value="${car.horsepower}"/></td>
                        </tr>
                        <tr>
                            <td>Acceleration:</td>
                            <td><c:out value="${car.acceleration}"/></td>
                        </tr>
                        <tr>
                            <td>Available:</td>
                            <td><c:out value="${car.available}"/></td>
                        </tr>
                        <tr>
                            <td>Description:</td>
                            <td><c:out value="${car.description}"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </c:forEach>
</c:if>

</body>
</html>


